package org.openmrs.module.labintegration.api;

import org.openmrs.Encounter;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.labintegration.api.hl7.NewOrderException;

public interface LabIntegrationService extends OpenmrsService {
	
	void doOrder(Encounter encounter) throws NewOrderException;
}
