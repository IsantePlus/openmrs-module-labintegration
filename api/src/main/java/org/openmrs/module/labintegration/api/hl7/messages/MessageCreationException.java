package org.openmrs.module.labintegration.api.hl7.messages;

import ca.uhn.hl7v2.HL7Exception;

public class MessageCreationException extends HL7Exception {
	
	private static final long serialVersionUID = 6508119674472082974L;
	
	public MessageCreationException(String s) {
		super(s);
	}
	
	public MessageCreationException(String s, Throwable throwable) {
		super(s, throwable);
	}
}
