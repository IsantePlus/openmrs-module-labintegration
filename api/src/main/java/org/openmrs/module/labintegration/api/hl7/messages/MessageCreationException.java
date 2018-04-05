package org.openmrs.module.labintegration.api.hl7.messages;

public class MessageCreationException extends Exception {
	
	private static final long serialVersionUID = 6508119674472082974L;
	
	public MessageCreationException(String s) {
		super(s);
	}
	
	public MessageCreationException(String s, Throwable throwable) {
		super(s, throwable);
	}
}
