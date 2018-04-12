package org.openmrs.module.labintegration.api.hl7.messages.gnerators;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.segment.OBR;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.springframework.stereotype.Component;

@Component
public class ObrGenerator {
	
	public void updateObr(OBR obr, Order order, OrderIdentifier orderIdentifier) throws DataTypeException {
		obr.getRequestedDateTime().getTime().setValue(order.getScheduledDate());
		orderIdentifier.updateOBR(obr);
	}
}
