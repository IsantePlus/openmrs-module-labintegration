package org.openmrs.module.labintegration.api.event;

import java.lang.reflect.Method;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Encounter;
import org.openmrs.module.labintegration.api.LabIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("labintegration.SaveEncounterAfterAdvice")
public class SaveEncounterAfterAdvice implements AfterReturningAdvice {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SaveEncounterAfterAdvice.class);
	
	private static final String SAVE_ENCOUNTER_METHOD_NAME = "saveEncounter";
	
	private static final String LAB_ORDER_ENCOUNTER_TYPE_UUID =
			"f037e97b-471e-4898-a07c-b8e169e0ddc4";
	
	@Autowired
	private LabIntegrationService labIntegrationService;
	
	@Override
	public void afterReturning(Object savedObject, Method method, Object[] args, Object target) {
		if (StringUtils.equals(method.getName(), SAVE_ENCOUNTER_METHOD_NAME)
				&& savedObject != null) {
			Encounter encounter = (Encounter) savedObject;
			LOGGER.info("Invoked saveEncounter method in EncounterService. Saved encounter {}",
					encounter.getUuid());
			
			if (LAB_ORDER_ENCOUNTER_TYPE_UUID.equals(encounter.getEncounterType().getUuid())) {
				LOGGER.info("Order encounter occurred {}", encounter.getUuid());
				labIntegrationService.doOrder(encounter);
			}
		}
	}
}
