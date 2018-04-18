package org.openmrs.module.labintegration.api.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Encounter;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labintegration.api.LabIntegrationService;
import org.openmrs.module.labintegration.api.exception.LabIntegrationException;
import org.openmrs.module.labintegration.api.hl7.openelis.OpenElisHL7Config;
import org.openmrs.module.labintegration.api.model.OrderDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("labintegration.LabIntegrationServiceImpl")
public class LabIntegrationServiceImpl extends BaseOpenmrsService implements LabIntegrationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LabIntegrationServiceImpl.class);
	
	private static final String ORDER_DESTINATION_CONCEPT_UUID =
			"160632AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	@Autowired
	private OpenElisHL7Config openElisHL7Config;
	
	@Override
	public void doOrder(Encounter encounter) {
		List<OrderDestination> orderDestinations = getOrderDestinations(encounter);
		validateDestinations(orderDestinations);
		LOGGER.info("Started processing order (created or updated) in Encounter {} to {}", encounter.getUuid(),
				StringUtils.join(orderDestinations, ','));
		
		
	}
	
	private void validateDestinations(List<OrderDestination> orderDestinations) {
		if (orderDestinations.contains(OrderDestination.OPEN_ELIS)
				&& !openElisHL7Config.isOpenElisConfigured()) {
			throw new LabIntegrationException("Tried to order from OpenELIS that is not configured");
		}
	}
	
	private List<OrderDestination> getOrderDestinations(Encounter encounter) {
		return encounter.getAllObs().stream()
				.filter(e -> e.getConcept().getUuid().equals(ORDER_DESTINATION_CONCEPT_UUID))
				.map(e -> OrderDestination.fromString(e.getValueText()))
				.collect(Collectors.toList());
	}
}
