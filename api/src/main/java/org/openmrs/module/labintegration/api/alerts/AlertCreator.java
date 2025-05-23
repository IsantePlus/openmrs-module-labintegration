package org.openmrs.module.labintegration.api.alerts;

import org.openmrs.Encounter;
import org.openmrs.Obs;

public interface AlertCreator {
	
	void createAlert(Encounter encounter, Obs obs, String status);
}
