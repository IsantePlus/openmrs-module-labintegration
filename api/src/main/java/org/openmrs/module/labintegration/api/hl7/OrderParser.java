package org.openmrs.module.labintegration.api.hl7;

import org.openmrs.Order;

public interface OrderParser {
	
	String createMessage(Order order, String orderControl);
}
