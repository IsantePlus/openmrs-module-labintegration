package org.openmrs.module.labintegration.api.hl7.config.systems;

import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.config.LabModulePropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractHL7Config implements HL7Config {
	
	private static final String MOTHER_NAME_ATTR_NAME = "labintegration.hl7.motherNameAttrTypeName";
	
	private static final String PHONE_NUM_ATTR_NAME = "labintegration.hl7.phoneNumAttrTypeName";
	
	private static final String DEFAULT_MOTHER_NAME_ATTR_NAME = "Mother Name";
	
	private static final String DEFAULT_PHONE_NUM_ATTR_NAME = "Telephone Number";
	
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
	
	protected LabModulePropertySource getPropertySource() {
		return propertySource;
	}
}
