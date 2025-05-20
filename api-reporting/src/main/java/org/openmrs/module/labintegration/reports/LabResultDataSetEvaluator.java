package org.openmrs.module.labintegration.reports;

import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.Obs;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.labintegration.LabIntegrationReportService;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.data.converter.ObsValueConverter;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.SimpleDataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.evaluator.DataSetEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openmrs.module.labintegration.LabIntegrationReportsConstants.LOCATION_ISANTE_CODE_UUID;

@Handler(supports= LabResultDataSetDefinition.class)
public class LabResultDataSetEvaluator implements DataSetEvaluator {

    @Override
    public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
        if (dataSetDefinition == null) {
            throw new EvaluationException(null, new AssertionError("dataSetDefinition is null"));
        } else if (!(dataSetDefinition instanceof LabResultDataSetDefinition)) {
            throw new EvaluationException(null, new AssertionError("Attempted to evaluate a LabResultDataSetDefinition but it was " + dataSetDefinition.getClass().getSimpleName()));
        }

        LabIntegrationReportService reportService = Context.getService(LabIntegrationReportService.class);

        SimpleDataSet simpleDataSet = new SimpleDataSet(dataSetDefinition, evalContext);

        List<Obs> results = reportService.getLabResults((Date) evalContext.getParameterValue("startDate"),
                (Date) evalContext.getParameterValue("endDate"));

        List<DataSetColumn> columns = Arrays.asList(
                new DataSetColumn("Clinic Code", MessageUtil.translate("labintegration.results.clinic.code"), String.class),
                new DataSetColumn("iSantePlus ID", MessageUtil.translate("labintegration.results.isanteplus.id"), String.class),
                new DataSetColumn("Patient Name", MessageUtil.translate("labintegration.results.patient.name"), String.class),
                new DataSetColumn("Date Ordered", MessageUtil.translate("labintegration.results.date.ordered"), Date.class),
                new DataSetColumn("Test Ordered", MessageUtil.translate("labintegration.results.test.ordered"), Concept.class),
                new DataSetColumn("Date Resulted", MessageUtil.translate("labintegration.results.date.resulted"), Date.class),
                new DataSetColumn("Result", MessageUtil.translate("labintegration.results.result"), String.class)
        );

        ObsValueConverter obsValueConverter = new ObsValueConverter();
        Map<Location, String> locationCodeCache = new HashMap<>();

        for (int i = 0; i < results.size(); i++) {
            Obs obs = results.get(i);

            for (DataSetColumn column : columns) {
                Object value = null;

                switch (column.getName()) {
                    case "Clinic Code":
                        value = extractLocationCode(obs, locationCodeCache);
                        break;
                    case "Patient Name":
                        value = obs.getPerson().getPersonName().getFullName();
                        break;
                    case "Date Ordered":
                        value = obs.getEncounter().getEncounterDatetime();
                        break;
                    case "Test Ordered":
                        break;
                    case "Date Resulted":
                        value = obs.getObsDatetime();
                        break;
                    case "Result":
                        value = obsValueConverter.convert(obs);
                        break;
                }

                if (value != null) {
                    simpleDataSet.addColumnValue(i, column, value);
                }
            }
        }

        return simpleDataSet;
    }

    protected String extractLocationCode(Obs obs, Map<Location, String> locationCodeCache) {
        Location location = obs.getEncounter().getLocation();

        if (locationCodeCache.containsKey(location)) {
            return locationCodeCache.get(location);
        }

        for (LocationAttribute attribute : location.getAttributes()) {
            if (attribute.getAttributeType().getUuid().equals(LOCATION_ISANTE_CODE_UUID)) {
                String result = attribute.getValueReference();
                locationCodeCache.put(location, result);
                return result;
            }
        }

        return null;
    }
}
