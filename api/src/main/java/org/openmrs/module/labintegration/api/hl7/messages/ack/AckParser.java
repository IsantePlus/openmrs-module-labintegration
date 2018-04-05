package org.openmrs.module.labintegration.api.hl7.messages.ack;

import ca.uhn.hl7v2.model.v25.message.ACK;
import ca.uhn.hl7v2.parser.PipeParser;
import org.springframework.stereotype.Component;

@Component
public class AckParser {
	
	private PipeParser pipeParser = new PipeParser();
	
	public Acknowledgement parse(String msg) throws InvalidAckException {
		try {
			ACK ack = new ACK();
			
			pipeParser.parse(ack, msg);
			
			return new Acknowledgement(ack);
		}
		catch (Exception ex) {
			throw new InvalidAckException("Error parsing ACK", ex);
		}
	}
}
