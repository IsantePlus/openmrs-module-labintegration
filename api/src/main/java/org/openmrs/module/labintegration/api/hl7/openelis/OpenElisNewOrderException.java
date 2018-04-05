package org.openmrs.module.labintegration.api.hl7.openelis;

import org.openmrs.module.labintegration.api.hl7.NewOrderException;

public class OpenElisNewOrderException extends NewOrderException {
	
	private static final long serialVersionUID = -6919815332748488455L;
	
	public OpenElisNewOrderException(String message) {
		super(message);
	}
	
	public OpenElisNewOrderException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
