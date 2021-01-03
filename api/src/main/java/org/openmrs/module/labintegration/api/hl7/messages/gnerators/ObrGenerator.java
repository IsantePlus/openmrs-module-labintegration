package org.openmrs.module.labintegration.api.hl7.messages.gnerators;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component

public class ObrGenerator {
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";
	
	public void updateObr(OBR obr, Obs obs, OrderIdentifier orderIdentifier) throws HL7Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		//change OBR[6] to OBR[7]
		obr.getObservationDateTime().getTime().setValue(dateFormat.format(obs.getEncounter().getEncounterDatetime()));
		//obr.getRequestedDateTime().getTime().setValue(dateFormat.format(obs.getEncounter().getEncounterDatetime()));
		orderIdentifier.updateOBR(obr, obs);
	}
}
