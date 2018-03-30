package org.openmrs.module.labintegration.api.hl7;

import org.openmrs.Order;

public interface OrderSender {
	
	void sendOrder(Order order);
}
