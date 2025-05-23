package org.openmrs.module.labintegration.api.hl7.messages.ack;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.ACK;
import ca.uhn.hl7v2.model.v25.message.ORL_O22;
import ca.uhn.hl7v2.parser.PipeParser;
import org.springframework.stereotype.Component;

@Component
public class AckParser {
	
	private static final String ACK_TYPE = "ACK";
	
	private static final String ORL_O22_TYPE = "ORL_O22";
	
	private PipeParser pipeParser = new PipeParser();
	
	public Acknowledgement parse(String msg) throws InvalidAckException {
		Acknowledgement result;
		
		try {
			switch (getAckType(msg)) {
				case ACK_TYPE:
					ACK ack = new ACK();
					pipeParser.parse(ack, msg);
					result = new Acknowledgement(ack);
					break;
				case ORL_O22_TYPE:
					ORL_O22 orl = new ORL_O22();
					pipeParser.parse(orl, msg);
					result = new Acknowledgement(orl);
					break;
				default:
					throw new UnsupportedMessageTypeException("Message type is unsupported");
			}
			
			return result;
		}
		catch (HL7Exception ex) {
			throw new InvalidAckException("Error while parsing a message", ex);
		}
	}
	
	private String getAckType(String msg) throws InvalidAckException {
		try {
			ACK tmp = new ACK();
			pipeParser.parse(tmp, msg);
			
			return tmp.getMSH().getMessageType().getMsg3_MessageStructure().getValue();
		}
		catch (HL7Exception e) {
			throw new InvalidAckException("Error while parsing a message", e);
		}
	}
}
