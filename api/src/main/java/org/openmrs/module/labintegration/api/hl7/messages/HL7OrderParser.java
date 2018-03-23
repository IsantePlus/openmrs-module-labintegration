package org.openmrs.module.labintegration.api.hl7.messages;

import ca.uhn.hl7v2.model.v25.message.OML_O21;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.communnication.OrderParser;
import org.springframework.stereotype.Component;

@Component
public class HL7OrderParser implements OrderParser {
	
	@Override
	public String createMessage(Order order) {
		OML_O21 message = new OML_O21();
		
		return message.toString();
	}
}
