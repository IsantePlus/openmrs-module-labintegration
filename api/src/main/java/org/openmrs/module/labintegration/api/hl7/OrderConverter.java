package org.openmrs.module.labintegration.api.hl7;

import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.OrderControl;

public interface OrderConverter {
	
	String createMessage(Order order, OrderControl orderControl, HL7Config hl7Config) throws MessageCreationException;
}
