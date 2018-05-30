package org.openmrs.module.labintegration.api.hl7.openelis;

import org.openmrs.Encounter;
import org.openmrs.module.labintegration.api.hl7.NewOrderException;
import org.openmrs.module.labintegration.api.hl7.OrderCancellationException;
import org.openmrs.module.labintegration.api.hl7.OrderSender;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.OMLO21OrderConverter;
import org.openmrs.module.labintegration.api.hl7.messages.OrderControl;
import org.openmrs.module.labintegration.api.hl7.messages.ack.AckParser;
import org.openmrs.module.labintegration.api.hl7.messages.ack.Acknowledgement;
import org.openmrs.module.labintegration.api.hl7.messages.ack.InvalidAckException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OpenElisOrderSender implements OrderSender {
	
	@Autowired
	private OpenElisHL7Config config;
	
	@Autowired
	private OMLO21OrderConverter orderConverter;
	
	@Autowired
	private AckParser ackParser;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Override
	public void sendNewOrder(Encounter encounter) throws NewOrderException {
		try {
			Acknowledgement ack = sendToOpenElis(encounter, OrderControl.NEW_ORDER);
			
			if (!ack.isSuccess()) {
				String exMsg = String.format("Error code received from OpenELIS - %s: %s.", ack.getErrorCode(),
				    ack.getErrorDiagnosticsInformation());
				throw new OpenElisNewOrderException(exMsg);
			}
		}
		catch (MessageCreationException ex) {
			throw new OpenElisNewOrderException("Error creating HL7 message for OpenELIS new order", ex);
		}
		catch (InvalidAckException ex) {
			throw new OpenElisNewOrderException("Unable to parse ACK from OpenELIS", ex);
		}
	}
	
	@Override
	public void sendOrderCancellation(Encounter encounter) throws OrderCancellationException {
		try {
			Acknowledgement ack = sendToOpenElis(encounter, OrderControl.CANCEL_ORDER);
			
			if (!ack.isSuccess()) {
				String exMsg = String.format("Error code received from OpenELIS - %s: %s.", ack.getErrorCode(),
				    ack.getErrorDiagnosticsInformation());
				throw new OpenElisOrderCancellationException(exMsg);
			}
		}
		catch (MessageCreationException ex) {
			throw new OpenElisOrderCancellationException("Error creating HL7 message for OpenELIS order cancellation", ex);
		}
		catch (InvalidAckException ex) {
			throw new OpenElisOrderCancellationException("Unable to parse ACK from OpenELIS", ex);
		}
	}
	
	@Override
	public boolean isEnabled() {
		return config.isOpenElisConfigured();
	}
	
	private Acknowledgement sendToOpenElis(Encounter encounter, OrderControl orderControl) throws MessageCreationException,
	        InvalidAckException {
		String msg = orderConverter.createMessage(encounter, orderControl, config);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/hl7-v2"));
		HttpEntity<String> request = new HttpEntity<>(msg, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(config.getOpenElisUrl(), request, String.class);
		
		return ackParser.parse(response.getBody());
	}
}
