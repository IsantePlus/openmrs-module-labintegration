/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.labintegration;

import org.openmrs.Encounter;
import org.openmrs.api.context.Context;
import org.openmrs.event.Event;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.labintegration.api.event.EncounterEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class LabIntegrationActivator extends BaseModuleActivator {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LabIntegrationActivator.class);
	
	/**
	 * @see #started()
	 */
	@Override
	public void started() {
		LOGGER.info("Started Lab Integration");
		Event.subscribe(Encounter.class, null, getEncounterEventListener());
	}
	
	/**
	 * @see #stopped()
	 */
	@Override
	public void stopped() {
		LOGGER.info("Stopped Lab Integration");
		Event.unsubscribe(Encounter.class, null, getEncounterEventListener());
	}
	
	private EncounterEventListener getEncounterEventListener() {
		return Context.getRegisteredComponent(
				"labintegration.EncounterEventListener", EncounterEventListener.class);
	}
}
