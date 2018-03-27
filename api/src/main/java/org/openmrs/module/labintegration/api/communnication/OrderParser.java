package org.openmrs.module.labintegration.api.communnication;

import org.openmrs.Order;

public interface OrderParser {
	
	String createMessage(Order order, String orderControl);
}
