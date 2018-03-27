package org.openmrs.module.labintegration.api.hl7.messages.gnerators;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.springframework.stereotype.Component;

@Component
public class ObrGenerator {
	
	public void updateObr(OBR obr, Order order, HL7Config hl7Config) throws DataTypeException {
		obr.getRequestedDateTime().getTime().setValue(order.getScheduledDate());
		
		OrderIdentifier orderIdentifier = hl7Config.buildOrderIdentifier(order);
		obr.getUniversalServiceIdentifier().getIdentifier().setValue(orderIdentifier.value());
	}
}
