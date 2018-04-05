package org.openmrs.module.labintegration.api.hl7.openelis;

import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.NewOrderException;
import org.openmrs.module.labintegration.api.hl7.OrderSender;
import org.openmrs.module.labintegration.api.hl7.messages.HL7OrderMessageGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.OrderControl;
import org.openmrs.module.labintegration.api.hl7.messages.ack.AckParser;
import org.openmrs.module.labintegration.api.hl7.messages.ack.Acknowledgement;
import org.openmrs.module.labintegration.api.hl7.messages.ack.InvalidAckException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenElisOrderSender implements OrderSender {
	
	@Autowired
	private OpenElisHL7Config config;
	
	@Autowired
	private HL7OrderMessageGenerator msgGenerator;
	
	@Autowired
	private AckParser ackParser;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public void sendNewOrder(Order order) throws NewOrderException {
		try {
			String msg = msgGenerator.createMessage(order, OrderControl.NEW_ORDER, config);
			
			ResponseEntity<String> response = restTemplate.postForEntity(config.getOpenElisUrl(), msg, String.class);
			
			Acknowledgement ack = ackParser.parse(response.getBody());
			
			if (!ack.isSuccess()) {
				String exMsg = String
				        .format("Error code received from OpenELIS : %s.", ack.getErrorDiagnosticsInformation());
				throw new OpenElisNewOrderException(exMsg);
			}
		}
		catch (MessageCreationException ex) {
			throw new OpenElisNewOrderException("Error creating HL7 message for OpenELIS", ex);
		}
		catch (InvalidAckException ex) {
			throw new OpenElisNewOrderException("Unable to parse ACK from OpenELIS", ex);
		}
	}
	
	@Override
	public boolean isEnabled() {
		return config.getOpenElisUrl() != null;
	}
}
