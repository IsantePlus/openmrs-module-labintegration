package org.openmrs.module.labintegration.api.hl7.config;

public interface OrderIdentifier {
	
	char ID_SEPARATOR = ';';
	
	String value();
}
