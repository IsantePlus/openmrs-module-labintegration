package org.openmrs.module.labintegration.api.reports;

import org.openmrs.Concept;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.annotation.Handler;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.labintegration.api.LabIntegrationReportService;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.common.ObjectUtil;
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

import static org.openmrs.module.labintegration.api.LabIntegrationReportsConstants.LOCATION_ISANTE_CODE_UUID;
import static org.openmrs.module.labintegration.api.LabIntegrationReportsConstants.ISANTEPLUS_IDENDTIFIER_TYPE_UUID;

@Handler(supports = LabResultDataSetDefinition.class)
public class LabResultDataSetEvaluator implements DataSetEvaluator {
	
	private final String COLUMN_NUMBER = "Number";
	
	private final String COLUMN_CLINIC_CODE = "Clinic Code";
	
	private final String COLUMN_ISANTEPLUS_ID = "ISantePlus ID";
	
	private final String COLUMN_PATIENT_NAME = "Patient Name";
	
	private final String COLUMN_DATE_ORDERED = "Date Ordered";
	
	private final String COLUMN_TEST_ORDERED = "Test Ordered";
	
	private final String COLUMN_DATE_RESULTED = "Date Resulted";
	
	private final String COLUMN_RESULT = "Result";
	
	@Override
	public DataSet evaluate(DataSetDefinition dataSetDefinition, EvaluationContext evalContext) throws EvaluationException {
		if (dataSetDefinition == null) {
			throw new EvaluationException(null, new AssertionError("dataSetDefinition is null"));
		} else if (!(dataSetDefinition instanceof LabResultDataSetDefinition)) {
			throw new EvaluationException(null,
			        new AssertionError("Attempted to evaluate a LabResultDataSetDefinition but it was "
			                + dataSetDefinition.getClass().getSimpleName()));
		}
		
		LabIntegrationReportService reportService = Context.getService(LabIntegrationReportService.class);
		
		SimpleDataSet simpleDataSet = new SimpleDataSet(dataSetDefinition, evalContext);
		
		List<Obs> results = reportService.getLabResults((Date) evalContext.getParameterValue("startDate"),
		    (Date) evalContext.getParameterValue("endDate"));
		
		List<DataSetColumn> columns = Arrays.asList(new DataSetColumn("", MessageUtil.translate(""), String.class),
		    new DataSetColumn(COLUMN_CLINIC_CODE, MessageUtil.translate("labintegration.results.clinic.code"), String.class),
		    new DataSetColumn(COLUMN_ISANTEPLUS_ID, MessageUtil.translate("labintegration.results.isanteplus.id"),
		            String.class),
		    new DataSetColumn(COLUMN_PATIENT_NAME, MessageUtil.translate("labintegration.results.patient.name"),
		            String.class),
		    new DataSetColumn(COLUMN_DATE_ORDERED, MessageUtil.translate("labintegration.results.date.ordered"), Date.class),
		    new DataSetColumn(COLUMN_TEST_ORDERED, MessageUtil.translate("labintegration.results.test.ordered"),
		            Concept.class),
		    new DataSetColumn(COLUMN_DATE_RESULTED, MessageUtil.translate("labintegration.results.date.resulted"),
		            Date.class),
		    new DataSetColumn(COLUMN_RESULT, MessageUtil.translate("labintegration.results.result"), String.class));
		
		ObsValueConverter obsValueConverter = new ObsValueConverter();
		Map<Location, String> locationCodeCache = new HashMap<>();
		for (int i = 0; i < results.size(); i++) {
			Obs obs = results.get(i);
			for (DataSetColumn column : columns) {
				Object value = null;
				
				switch (column.getName()) {
					case COLUMN_NUMBER:
						value = i + 1;
						break;
					case COLUMN_CLINIC_CODE:
						value = extractLocationCode(obs, locationCodeCache);
						break;
					case COLUMN_PATIENT_NAME:
						value = obs.getPerson().getPersonName().getFullName();
						break;
					case COLUMN_ISANTEPLUS_ID:
						value = getPatientIdentifier(obs.getPersonId());
						break;
					case COLUMN_DATE_ORDERED:
						value = obs.getEncounter().getEncounterDatetime();
						break;
					case COLUMN_TEST_ORDERED:
						value = ObjectUtil.format(obs.getConcept());
						break;
					case COLUMN_DATE_RESULTED:
						value = obs.getObsDatetime();
						break;
					case COLUMN_RESULT:
						value = obsValueConverter.convert(obs);
						break;
				}
				if (value != null) {
					simpleDataSet.addColumnValue(i, column, value);
				}
			}
		}
		if (results.isEmpty()) {
			for (DataSetColumn column : columns) {
				simpleDataSet.addColumnValue(0, column, "");
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
	
	protected String getPatientIdentifier(Integer personId) {
		PatientService patientService = Context.getPatientService();
		Patient patient = patientService.getPatient(personId);
		PatientIdentifierType pit = patientService.getPatientIdentifierTypeByUuid(ISANTEPLUS_IDENDTIFIER_TYPE_UUID);
		patient.getPatientIdentifier(pit);
		PatientIdentifier pid = patient.getPatientIdentifier(pit);
		return pid != null ? pid.getIdentifier() : "";
	}
}
