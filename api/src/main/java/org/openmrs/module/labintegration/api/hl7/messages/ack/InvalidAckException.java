package org.openmrs.module.labintegration.api.hl7.messages.ack;

public class InvalidAckException extends Exception {
	
	private static final long serialVersionUID = -8700723548295961807L;
	
	public InvalidAckException(String message) {
		super(message);
	}
	
	public InvalidAckException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
