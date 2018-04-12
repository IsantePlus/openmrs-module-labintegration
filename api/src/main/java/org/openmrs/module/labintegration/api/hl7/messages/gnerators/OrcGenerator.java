package org.openmrs.module.labintegration.api.hl7.messages.gnerators;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.segment.ORC;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.springframework.stereotype.Component;

@Component
public class OrcGenerator {
	
	public void updateOrc(ORC orc, Order order, String orderControl, OrderIdentifier orderIdentifier)
	        throws DataTypeException {
		orc.getOrderControl().setValue(orderControl);
		
		String providerId = order.getOrderer().getIdentifier();
		orc.getPlacerOrderNumber().getEntityIdentifier().setValue(providerId);
		
		orderIdentifier.updateORC(orc);
	}
}
