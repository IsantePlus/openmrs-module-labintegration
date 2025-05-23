package org.openmrs.module.labintegration.api.reports;

import org.openmrs.module.labintegration.ActivatedReportManager;
import org.openmrs.module.reporting.common.MessageUtil;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.ReportDesign;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.manager.ReportManagerUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LabResultsReportManager extends ActivatedReportManager {
	
	@Override
	public String getUuid() {
		return "100db747-d65d-46d4-96dc-7e66fb672ab2";
	}
	
	public String getExcelDesignUuid() {
		return "32639f66-1cc8-4ccc-93b3-20077ea33d7d";
	}
	
	@Override
	public String getName() {
		return MessageUtil.translate("labintegration.results.report.name");
	}
	
	public String getDataSetName() {
		return MessageUtil.translate("labintegration.results.report.dataset");
	}
	
	@Override
	public String getDescription() {
		return MessageUtil.translate("labintegration.results.report.description");
	}
	
	@Override
	public List<Parameter> getParameters() {
		return Arrays.asList(
		    new Parameter("startDate", MessageUtil.translate("labintegration.results.parameter.startDate"), Date.class),
		    new Parameter("endDate", MessageUtil.translate("labintegration.results.parameter.endDate"), Date.class));
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
		
		Map<String, Object> parameterMappings = new HashMap<String, Object>();
		parameterMappings.put("startDate", "${startDate}");
		parameterMappings.put("endDate", "${endDate}");
		
		reportDefinition.addDataSetDefinition(getDataSetName(), dataSetDefinition, parameterMappings);
		
		return reportDefinition;
	}
	
	@Override
	public List<ReportDesign> constructReportDesigns(ReportDefinition reportDefinition) {
		return Arrays.asList(ReportManagerUtil.createExcelDesign(getExcelDesignUuid(), reportDefinition));
	}
	
	@Override
	public String getVersion() {
		return "1.0.0-SNAPSHOT";
	}
}
