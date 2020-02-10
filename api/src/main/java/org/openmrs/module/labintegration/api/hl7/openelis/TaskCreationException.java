package org.openmrs.module.labintegration.api.hl7.openelis;

public class TaskCreationException extends Exception {

	public TaskCreationException(String message) {
		super(message);
	}

	public TaskCreationException(String message, Throwable cause) {
		super(message, cause);
	}
}
