package org.openmrs.module.labintegration.api.exception;

public class LabIntegrationException extends RuntimeException {
	
	public LabIntegrationException(Throwable e) {
		super(e);
	}
	
	public LabIntegrationException(String message) {
		super(message);
	}
	
	public LabIntegrationException(String message, Throwable e) {
		super(message, e);
	}
}
