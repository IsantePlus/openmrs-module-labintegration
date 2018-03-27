package org.openmrs.module.labintegration.api.hl7.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LabIntegrationProperties {
	
	private static final String PRODUCTION = "labintegration.production";
	
	@Autowired
	private LabModulePropertySource propertySource;
	
	public boolean isProduction() {
		String value = propertySource.getProperty(PRODUCTION, "false");
		return Boolean.parseBoolean(value);
	}
	
	public String getHL7ProcessingId() {
		return isProduction() ? "P" : "D";
	}
}
