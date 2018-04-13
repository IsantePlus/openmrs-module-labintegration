package org.openmrs.module.labintegration.api.impl;

import org.openmrs.Encounter;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labintegration.api.LabIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("labintegration.LabIntegrationServiceImpl")
public class LabIntegrationServiceImpl extends BaseOpenmrsService implements LabIntegrationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LabIntegrationServiceImpl.class);
	
	@Override
	public void doOrder(Encounter encounter) {
		LOGGER.info("Started processing order in Encounter {}", encounter.getUuid());
	}
}
