package org.openmrs.module.labintegration.reports;

import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.BaseReportManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Localized("labintegration.LabResultsReportManager")
public class LabResultsReportManager extends BaseReportManager implements ActivatedReportManager {

    private static volatile String version = null;

    @Override
    public String getUuid() {
        return "100db747-d65d-46d4-96dc-7e66fb672ab2";
    }

    @Override
    public String getName() {
        return MessageUtil.translate("labintegration.results.report.name");
    }

    @Override
    public String getDescription() {
        return MessageUtil.translate("labintegration.results.report.description");
    }

    @Override
    public List<Parameter> getParameters() {
        return Arrays.asList(
                new Parameter("startDate", MessageUtil.translate("labintegration.results.parameter.startDate"), Date.class),
                new Parameter("endDate", MessageUtil.translate("labintegration.results.parameter.endDate"), Date.class)
        );
    }

    @Override
    public ReportDefinition constructReportDefinition() {
        ReportDefinition reportDefinition = new ReportDefinition();
        reportDefinition.setUuid(getUuid());
        reportDefinition.setName(getName());
        reportDefinition.setDescription(getDescription());
        reportDefinition.setParameters(getParameters());

        LabResultDataSetDefinition dataSetDefinition = new LabResultDataSetDefinition();
        dataSetDefinition.setUuid(getUuid());
        dataSetDefinition.setName(getName());
        dataSetDefinition.setDescription(getDescription());
        dataSetDefinition.setParameters(getParameters());

        reportDefinition.addDataSetDefinition(new LabResultDataSetDefinition(), null);

        return reportDefinition;
    }

    @Override
    public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
        return Collections.emptyList();
    }

    @Override
    public String getVersion() {
        if (version == null) {
            synchronized (this) {
                if (version == null) {
                    Properties props = new Properties();
                    try (InputStream is = getClass().getClassLoader().getResourceAsStream("labintegration.properties")) {
                        props.load(is);
                        version = props.getProperty("labintegration.version");
                    } catch (IOException e) {
                        version = "0.0.1";
                    }
                }
            }
        }

        return version;
    }
}
