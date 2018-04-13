package org.openmrs.module.labintegration.api.impl;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Encounter;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labintegration.api.LabIntegrationService;
import org.openmrs.module.labintegration.api.model.OrderDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("labintegration.LabIntegrationServiceImpl")
public class LabIntegrationServiceImpl extends BaseOpenmrsService implements LabIntegrationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LabIntegrationServiceImpl.class);
	
	private static final String ORDER_DESTINATION_CONCEPT_UUID =
			"160632AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
	@Override
	public void doOrder(Encounter encounter) {
		List<OrderDestination> orderDestinations = getOrderDestinations(encounter);
		LOGGER.info("Started processing order in Encounter {} to {}", encounter.getUuid(),
				StringUtils.join(orderDestinations, ','));
		
		
	}
	
	private List<OrderDestination> getOrderDestinations(Encounter encounter) {
		return encounter.getAllObs().stream()
				.filter(e -> e.getConcept().getUuid().equals(ORDER_DESTINATION_CONCEPT_UUID))
				.map(e -> OrderDestination.fromString(e.getValueText()))
				.collect(Collectors.toList());
	}
}
