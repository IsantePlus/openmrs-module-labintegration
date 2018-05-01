package org.openmrs.module.labintegration.api.hl7.messages.ack;


import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ACK;
import ca.uhn.hl7v2.parser.PipeParser;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AckGenerator {

    private static final String VERSION_25 = "2.5";

    private static final String VERSION_251 = "2.5.1";

    private PipeParser pipeParser = new PipeParser();

    public ACK generateACK(Message msg) throws IOException, HL7Exception {
        Message tmp = msg.getMessage().generateACK();
        return convert(tmp);
    }

    public ACK generateACK(Message msg, HL7Exception e, String acknowledgementCode) throws IOException, HL7Exception {
        Message tmp = msg.getMessage().generateACK(acknowledgementCode, e);
        return convert(tmp);
    }

    private ACK convert(Message msg) throws HL7Exception {
        ACK ack = getACK(msg);

        ack.getMSH().getMsh9_MessageType().getMsg3_MessageStructure().setValue("ACK");
        ack = convertFrom25To251(ack);

        return ack;
    }

    private ACK getACK(Message msg) throws HL7Exception {
        ACK ack = new ACK();
        pipeParser.parse(ack, msg.getMessage().encode());
        return ack;
    }

    private ACK convertFrom25To251(ACK ack) throws HL7Exception {
        String msg = ack.getMessage().encode().replaceFirst(VERSION_25, VERSION_251);
        pipeParser.parse(ack, msg);
        return ack;
    }
}
