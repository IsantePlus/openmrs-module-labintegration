package org.openmrs.module.labintegration.api.hl7.oru;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import org.openmrs.hl7.HL7Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static org.openmrs.module.labintegration.api.hl7.messages.util.OruR01Util.changeMessageVersionFrom251To25;

@Component
public class OruRo1Receiver {

    @Autowired
    @Qualifier("hL7ServiceLabIntegration")
    private HL7Service hl7Service;

    public void receiveMsg(String msg) throws HL7Exception {
        String parsedMsg = changeMessageVersionFrom251To25(msg);

        Message message = hl7Service.parseHL7String(parsedMsg);
        ORU_R01 oruMessage = (ORU_R01) message;

        // TODO: wil this work?
        hl7Service.processHL7Message(oruMessage);
    }
}
