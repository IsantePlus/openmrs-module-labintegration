package org.openmrs.module.labintegration.api.hl7;

import org.openmrs.Encounter;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.ORMO01OrderConverter;
import org.openmrs.module.labintegration.api.hl7.messages.OrderControl;
import org.openmrs.module.labintegration.api.hl7.scc.SCCHL7Config;
import org.openmrs.module.labintegration.api.model.OrderDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderGeneratorManager {

    @Autowired
    private ORMO01OrderConverter orderConverter;

    @Autowired
    private SCCHL7Config scchl7Config;


    public List<String> generateOrders(Encounter encounter, OrderDestination destination) throws MessageCreationException {
        List<String> orderMsgList = new ArrayList<>();

        if (destination == OrderDestination.SCC) {
            for (Order order : encounter.getOrders()) {
                if (OrderDestination.searchForExistence(encounter, destination)) {
                    orderMsgList.add(orderConverter.createMessage(order, OrderControl.NEW_ORDER, scchl7Config));
                }
            }
        }
        return orderMsgList;
    }
}
