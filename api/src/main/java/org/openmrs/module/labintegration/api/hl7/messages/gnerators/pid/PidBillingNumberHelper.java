package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.PID;
import org.openmrs.Encounter;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.springframework.stereotype.Component;

@Component
public class PidBillingNumberHelper {
	
	public void updateBillingNumber(PID pid, Encounter encounter, HL7Config hl7Config) throws DataTypeException {
		pid.getPatientAccountNumber().getAssigningFacility().getNamespaceID()
		        .setValue(hl7Config.getSendingFacilityNamespaceID());

		if (hl7Config.isBillingNumberNeeded()) {
			/*Change encounter uuid to ID*/
			pid.getPatientAccountNumber().getIDNumber().setValue(encounter.getId().toString());
		}
	}
}
