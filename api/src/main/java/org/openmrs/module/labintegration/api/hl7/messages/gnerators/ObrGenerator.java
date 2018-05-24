package org.openmrs.module.labintegration.api.hl7.messages.gnerators;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.springframework.stereotype.Component;

@Component
public class ObrGenerator {
	
	public void updateObr(OBR obr, Obs obs, OrderIdentifier orderIdentifier) throws HL7Exception {
		obr.getRequestedDateTime().getTime().setValue(obs.getEncounter().getEncounterDatetime());
		orderIdentifier.updateOBR(obr, obs);
	}
}
