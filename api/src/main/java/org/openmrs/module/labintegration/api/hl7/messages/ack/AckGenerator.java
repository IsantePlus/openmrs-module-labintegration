package org.openmrs.module.labintegration.api.hl7.messages.ack;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ACK;
import ca.uhn.hl7v2.parser.PipeParser;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.util.VersionSwitcher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AckGenerator {
	
	private PipeParser pipeParser = new PipeParser();
	
	public ACK generateACK(Message msg) throws IOException, HL7Exception {
		Message tmp = msg.getMessage().generateACK();
		return convert(tmp);
	}
	
	public ACK generateACK(Message msg, HL7Exception e, String acknowledgementCode) throws MessageCreationException {
		try {
			Message tmp = msg.getMessage().generateACK(acknowledgementCode, getRootHL7Exception(e, e));
			return convert(tmp);
		}
		catch (HL7Exception ex) {
			throw new MessageCreationException("Unable to create ACK", ex);
		}
		catch (IOException ex) {
			throw new MessageCreationException("Unable to create ACK", ex);
		}
	}
	
	private ACK convert(Message msg) throws HL7Exception {
		ACK ack = getACK(msg);
		
		ack.getMSH().getMsh9_MessageType().getMsg3_MessageStructure().setValue("ACK");
		
		return convertVersion(ack);
	}
	
	private ACK getACK(Message msg) throws HL7Exception {
		ACK ack = new ACK();
		pipeParser.parse(ack, msg.getMessage().encode());
		return ack;
	}
	
	private ACK convertVersion(ACK ack) throws HL7Exception {
		String msg = ack.encode();
		msg = VersionSwitcher.switchVersion(msg);
		
		ACK newAck = new ACK();
		pipeParser.parse(newAck, msg);
		return newAck;
	}
	
	private HL7Exception getRootHL7Exception(Throwable ex, HL7Exception lastHl7ExInStack) {
		Throwable cause = ex.getCause();
		if (cause instanceof HL7Exception) {
			return getRootHL7Exception(cause, (HL7Exception) cause);
		} else if (cause != null) {
			return getRootHL7Exception(cause, lastHl7ExInStack);
		} else {
			return ex instanceof HL7Exception ? (HL7Exception) ex : lastHl7ExInStack;
		}
	}
}
