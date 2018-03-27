package org.openmrs.module.labintegration.api.hl7.config;

import org.openmrs.Order;

public interface HL7Config {
	
	String getReceivingApplication();
	
	String getSendingApplication();
	
	String getPatientIdentifierTypeUuid();
	
	String getMotherNameAttrTypeName();
	
	String getPhoneNumberAttrTypeName();
	
	OrderIdentifier buildOrderIdentifier(Order order);
}
