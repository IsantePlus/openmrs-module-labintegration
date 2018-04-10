/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.labintegration.api.impl;

import org.openmrs.Encounter;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labintegration.api.LabIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LabIntegrationServiceImpl extends BaseOpenmrsService implements LabIntegrationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LabIntegrationServiceImpl.class);
	
	@Override
	public void doOrder(Encounter encounter) {
		LOGGER.info("Started processing order in Encounter {}", encounter.getUuid());
	}
}
