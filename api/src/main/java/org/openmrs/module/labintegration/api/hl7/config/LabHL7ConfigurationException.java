package org.openmrs.module.labintegration.api.hl7.config;

public class LabHL7ConfigurationException extends RuntimeException {
	
	private static final long serialVersionUID = 2262131326966684075L;
	
	public LabHL7ConfigurationException(String message) {
		super(message);
	}
	
	public LabHL7ConfigurationException(String s, Throwable throwable) {
		super(s, throwable);
	}
}
