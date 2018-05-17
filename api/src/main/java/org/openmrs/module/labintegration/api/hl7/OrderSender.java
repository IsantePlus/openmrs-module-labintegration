package org.openmrs.module.labintegration.api.hl7;

import org.openmrs.Encounter;

public interface OrderSender {
	
	void sendNewOrder(Encounter encounter) throws NewOrderException;
	
	void sendOrderCancellation(Encounter encounter) throws OrderCancellationException;
	
	boolean isEnabled();
}
