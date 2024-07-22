package org.openmrs.module.labintegration.api.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Encounter;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labintegration.api.LabIntegrationService;
import org.openmrs.module.labintegration.api.exception.LabIntegrationException;
import org.openmrs.module.labintegration.api.hl7.NewOrderException;
import org.openmrs.module.labintegration.api.hl7.OrderSenderManager;
import org.openmrs.module.labintegration.api.hl7.openelis.OpenElisHL7Config;
import org.openmrs.module.labintegration.api.model.OrderDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("labintegration.LabIntegrationServiceImpl")
public class LabIntegrationServiceImpl extends BaseOpenmrsService implements LabIntegrationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LabIntegrationServiceImpl.class);
	
	@Autowired
	private OpenElisHL7Config openElisHL7Config;

	@Autowired
	private OrderSenderManager orderSenderManager;
	
	@Override
	public void doOrder(Encounter encounter) throws NewOrderException {
		List<OrderDestination> orderDestinations = OrderDestination.getOrderDestinations(encounter);
		validateDestinations(orderDestinations);
		LOGGER.info("Started processing order (created or updated) in Encounter {} to {}", encounter.getUuid(),
				StringUtils.join(orderDestinations, ','));

		for (OrderDestination destination : orderDestinations) {
			if (!destination.equals(OrderDestination.SCC)) {
				orderSenderManager.sendOrders(encounter, destination);
			} else {
				LOGGER.info("Skipping order not bound for SCC");
			}
		}
	}
	
	private void validateDestinations(List<OrderDestination> orderDestinations) {
		if (orderDestinations.contains(OrderDestination.OPEN_ELIS)
				&& !openElisHL7Config.isOpenElisConfigured()) {
			throw new LabIntegrationException("Tried to order from OpenELIS that is not configured");
		}
	}
}
