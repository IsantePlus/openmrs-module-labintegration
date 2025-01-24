package org.openmrs.module.labintegration.api.hl7.oru;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ACK;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import org.openmrs.hl7.HL7Service;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.ack.AckGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static org.openmrs.module.labintegration.api.hl7.messages.util.OruR01Util.changeMessageVersionFrom251To25;

@Component("OruRO1Receiver")
public class OruRo1Receiver {

    @Autowired
    @Qualifier("hL7ServiceLabIntegration")
    private HL7Service hl7Service;

    @Autowired
    private AckGenerator ackGenerator;

    public ACK receiveMsg(String msg) throws MessageCreationException, HL7Exception {
        String parsedMsg = changeMessageVersionFrom251To25(msg);
        Message message = hl7Service.parseHL7String(parsedMsg);

        try {
            ORU_R01 oruMessage = (ORU_R01) message;

            Message res = hl7Service.processHL7Message(oruMessage);

            return ackGenerator.generateACK(res);
        } catch (RuntimeException | IOException e) {
            return ackGenerator.generateACK(message, new HL7Exception(e.getMessage(), e), "AE");
        } catch (HL7Exception e) {
            return ackGenerator.generateACK(message, e, "AE");
        }
    }


}
