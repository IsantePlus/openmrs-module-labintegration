package org.openmrs.module.labintegration.api.hl7.messages;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.ORM_O01;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.OrderParser;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.config.LabIntegrationProperties;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.MshGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ORMO01OrderParser implements OrderParser {
	
	@Autowired
	private MshGenerator mshGenerator;
	
	@Autowired
	private LabIntegrationProperties labIntegrationProperties;
	
	@Override
	public String createMessage(Order order, OrderControl orderControl, HL7Config hl7Config) throws MessageCreationException {
		try {
			ORM_O01 message = new ORM_O01();
			
			message.initQuickstart("ORM", "O01", labIntegrationProperties.getHL7ProcessingId());
			mshGenerator.updateMSH(message.getMSH(), hl7Config);
			
			return message.toString();
		}
		catch (IOException e) {
			throw messageCreationException(e);
		}
		catch (HL7Exception e) {
			throw messageCreationException(e);
		}
	}
	
	private MessageCreationException messageCreationException(Exception cause) {
		return new MessageCreationException("Unable to create ORM^O01 message", cause);
	}
}
