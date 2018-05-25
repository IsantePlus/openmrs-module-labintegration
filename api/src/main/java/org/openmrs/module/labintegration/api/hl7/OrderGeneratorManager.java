package org.openmrs.module.labintegration.api.hl7;

import org.openmrs.Encounter;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.ORMO01OrderConverter;
import org.openmrs.module.labintegration.api.hl7.messages.OrderControl;
import org.openmrs.module.labintegration.api.hl7.scc.SCCHL7Config;
import org.openmrs.module.labintegration.api.model.OrderDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderGeneratorManager {

    @Autowired
    private ORMO01OrderConverter orderConverter;

    @Autowired
    private SCCHL7Config scchl7Config;

    public String generateORMO01Message(Encounter encounter, OrderDestination destination) throws MessageCreationException {
        if (destination == OrderDestination.SCC) {
            return orderConverter.createMessage(encounter, OrderControl.NEW_ORDER, scchl7Config);
        } else {
            throw new MessageCreationException("Message destination " + destination + "is not supported.");
        }
    }
}
