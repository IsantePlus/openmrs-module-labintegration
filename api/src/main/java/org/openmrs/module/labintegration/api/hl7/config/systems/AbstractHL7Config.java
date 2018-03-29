package org.openmrs.module.labintegration.api.hl7.config.systems;

import org.openmrs.Concept;
import org.openmrs.EncounterType;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.config.LabModulePropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractHL7Config implements HL7Config {
	
	private static final String MOTHER_NAME_ATTR_NAME = "labintegration.hl7.motherNameAttrTypeName";
	
	private static final String PHONE_NUM_ATTR_NAME = "labintegration.hl7.phoneNumAttrTypeName";
	
	private static final String REG_ENC_TYPE_UUID = "labintegration.hl7.regForm.typeUuid";
	
	private static final String REG_RELIGION_CONCEPT = "labintegration.hl7.regForm.religion";
	
	private static final String REG_CIVIL_STATUS_CONCEPT = "labintegration.hl7.regForm.civilStatus";
	
	private static final String DEFAULT_MOTHER_NAME_ATTR_NAME = "First Name of Mother";
	
	private static final String DEFAULT_PHONE_NUM_ATTR_NAME = "Telephone Number";
	
	private static final String DEFAULT_REG_ENC_TYPE_UUID = "873f968a-73a8-4f9c-ac78-9f4778b751b6";
	
	private static final String DEFAULT_REG_RELIGION_CONCEPT = "CIEL:162929";
	
	private static final String DEFAULT_REG_CIVIL_STATUS_CONCEPT = "CIEL:1054";
	
	@Autowired
	private LabModulePropertySource propertySource;
	
	@Override
	public String getMotherNameAttrTypeName() {
		return propertySource.getProperty(MOTHER_NAME_ATTR_NAME, DEFAULT_MOTHER_NAME_ATTR_NAME);
	}
	
	@Override
	public String getPhoneNumberAttrTypeName() {
		return propertySource.getProperty(PHONE_NUM_ATTR_NAME, DEFAULT_PHONE_NUM_ATTR_NAME);
	}
	
	@Override
	public EncounterType getRegistrationFormEncounterType() {
		return propertySource.getEncounterTypeFromProp(REG_ENC_TYPE_UUID, DEFAULT_REG_ENC_TYPE_UUID);
	}
	
	@Override
	public Integer getReligionConceptId() {
		Concept concept = propertySource.getConceptFromProperty(REG_RELIGION_CONCEPT, DEFAULT_REG_RELIGION_CONCEPT);
		
		return concept.getConceptId();
	}
	
	@Override
	public Integer getCivilStatusConceptId() {
		Concept concept = propertySource.getConceptFromProperty(REG_CIVIL_STATUS_CONCEPT, DEFAULT_REG_CIVIL_STATUS_CONCEPT);
		
		return concept.getConceptId();
	}
	
	protected LabModulePropertySource getPropertySource() {
		return propertySource;
	}
}
