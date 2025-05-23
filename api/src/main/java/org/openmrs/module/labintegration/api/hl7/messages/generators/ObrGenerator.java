package org.openmrs.module.labintegration.api.hl7.messages.generators;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class ObrGenerator {
	
	@Autowired
	private HL7Config hl7Config;
	
	public void updateObr(OBR obr, Obs obs, OrderIdentifier orderIdentifier) throws HL7Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(hl7Config.getDefaultDateFormat());
		
		//change OBR[6] to OBR[7]
		obr.getObservationDateTime().getTime().setValue(dateFormat.format(obs.getEncounter().getEncounterDatetime()));
		
		orderIdentifier.updateOBR(obr, obs);
	}
}
