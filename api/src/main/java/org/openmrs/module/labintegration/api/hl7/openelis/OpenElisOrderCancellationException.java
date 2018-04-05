package org.openmrs.module.labintegration.api.hl7.openelis;

import org.openmrs.module.labintegration.api.hl7.OrderCancellationException;

public class OpenElisOrderCancellationException extends OrderCancellationException {
	
	private static final long serialVersionUID = 6956179372140483363L;
	
	public OpenElisOrderCancellationException(String message) {
		super(message);
	}
	
	public OpenElisOrderCancellationException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
