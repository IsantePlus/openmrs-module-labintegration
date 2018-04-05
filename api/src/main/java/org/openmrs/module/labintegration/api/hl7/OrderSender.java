package org.openmrs.module.labintegration.api.hl7;

import org.openmrs.Order;

public interface OrderSender {
	
	void sendNewOrder(Order order) throws NewOrderException;
	
	void sendOrderCancellation(Order order) throws OrderCancellationException;
	
	boolean isEnabled();
}
