package org.openmrs.module.labintegration.api.hl7;

public abstract class NewOrderException extends Exception {
	
	private static final long serialVersionUID = 3395845631921958576L;
	
	protected NewOrderException(String message) {
		super(message);
	}
	
	protected NewOrderException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
