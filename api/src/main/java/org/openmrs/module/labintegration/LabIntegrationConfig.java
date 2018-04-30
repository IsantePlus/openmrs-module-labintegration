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

import org.openmrs.api.context.Context;
import org.springframework.stereotype.Component;

/**
 * Contains module's config.
 */
@Component("labintegration.LabIntegrationConfig")
public class LabIntegrationConfig {
	
	public static final String MODULE_PRIVILEGE = "Lab Integration Privilege";
	
	public static final String LAB_ORDER_CONCEPT_CODE = "labintegration.order.conceptCode";
	
	public static final String LAB_ORDER_CONCEPT_CODE_DEFAULT = "1271";
	
	public int getLabOrderConceptCode() {
		return Integer.parseInt(getProperty(LAB_ORDER_CONCEPT_CODE, LAB_ORDER_CONCEPT_CODE_DEFAULT));
	}
	
	private String getProperty(String name, String defaultVal) {
		return Context.getAdministrationService().getGlobalProperty(name, defaultVal);
	}
}
