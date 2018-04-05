package org.openmrs.module.labintegration.api.hl7;

public abstract class OrderCancellationException extends Exception {
	
	private static final long serialVersionUID = -4619793125626467357L;
	
	protected OrderCancellationException(String message) {
		super(message);
	}
	
	protected OrderCancellationException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
