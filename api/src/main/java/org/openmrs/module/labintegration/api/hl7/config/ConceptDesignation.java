package org.openmrs.module.labintegration.api.hl7.config;

import org.apache.commons.lang3.StringUtils;

public class ConceptDesignation {
	
	private final String code;
	
	private final String sourceName;
	
	public ConceptDesignation(String conceptProperty) {
		if (StringUtils.isBlank(conceptProperty)) {
			throw new LabHL7ConfigurationException("Concept designation property cannot be blank");
		}
		
		if (conceptProperty.indexOf(':') < 0) {
			throw new LabHL7ConfigurationException("Invalid concept designation : " + conceptProperty
			        + ". Concept designation should have the format SOURCE_NAME:CODE, i.e. CIEL:1056");
		}
		
		String[] parts = conceptProperty.split(":", 2);
		
		sourceName = parts[0];
		code = parts[1];
	}
	
	public String getCode() {
		return code;
	}
	
	public String getSourceName() {
		return sourceName;
	}
}
