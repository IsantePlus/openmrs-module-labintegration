package org.openmrs.module.labintegration.api.hl7.messages.ack;

public class UnsupportedMessageTypeException extends InvalidAckException {
	
	private static final long serialVersionUID = 4376585604635552460L;
	
	public UnsupportedMessageTypeException(String message) {
		super(message);
	}
	
	public UnsupportedMessageTypeException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
