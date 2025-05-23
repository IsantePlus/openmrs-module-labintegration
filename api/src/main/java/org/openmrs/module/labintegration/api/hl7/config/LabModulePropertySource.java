package org.openmrs.module.labintegration.api.hl7.config;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LabModulePropertySource {
	
	@Autowired
	private AdministrationService adminService;
	
	@Autowired
	private ConceptService conceptService;
	
	@Autowired
	private EncounterService encounterService;
	
	public String getRequiredProperty(String propertyName) {
		String value = adminService.getGlobalProperty(propertyName);
		if (StringUtils.isBlank(value)) {
			throw new LabHL7ConfigurationException("Required property " + propertyName + " is not set");
		}
		return value;
	}
	
	public String getProperty(String propertyName, String defaultValue) {
		return adminService.getGlobalProperty(propertyName, defaultValue);
	}
	
	public Concept getConceptFromProperty(String propertyName, String defaultPropValue) {
		String conceptProp = getProperty(propertyName, defaultPropValue);
		ConceptDesignation conceptDesignation = new ConceptDesignation(conceptProp);
		
		Concept concept = conceptService.getConceptByMapping(conceptDesignation.getCode(),
		    conceptDesignation.getSourceName());
		
		if (concept == null) {
			throw new LabHL7ConfigurationException(
			        "Concept " + conceptProp + " not found, " + "proper concept must be configured");
		}
		
		return concept;
	}
	
	public EncounterType getEncounterTypeFromProp(String propertyName, String defaultPropValue) {
		String encounterTypeUuid = getProperty(propertyName, defaultPropValue);
		
		EncounterType encounterType = null;
		for (EncounterType type : encounterService.getAllEncounterTypes()) {
			if (encounterTypeUuid.equals(type.getUuid())) {
				encounterType = type;
				break;
			}
		}
		
		if (encounterType == null) {
			throw new LabHL7ConfigurationException("Encounter type does not exist with UUID: " + encounterTypeUuid);
		}
		
		return encounterType;
	}
}
