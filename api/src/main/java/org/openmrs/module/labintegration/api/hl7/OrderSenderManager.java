package org.openmrs.module.labintegration.api.hl7;

import org.openmrs.Encounter;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.openelis.OpenElisOrderSender;
import org.openmrs.module.labintegration.api.model.OrderDestination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderSenderManager {

    @Autowired
    private OpenElisOrderSender openElisOrderSender;

    public void sendOrders(Encounter encounter, OrderDestination destination) throws NewOrderException {
        if (destination == OrderDestination.OPEN_ELIS) {
            for (Order order : encounter.getOrders()) {
                openElisOrderSender.sendNewOrder(order);
            }
        }
    }
}
