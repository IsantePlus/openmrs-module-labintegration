package org.openmrs.module.labintegration.api;

import org.openmrs.Encounter;
import org.openmrs.api.OpenmrsService;

public interface LabIntegrationService extends OpenmrsService {
	
	void doOrder(Encounter encounter);
}
