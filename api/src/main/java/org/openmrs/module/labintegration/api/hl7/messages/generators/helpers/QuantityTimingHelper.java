package org.openmrs.module.labintegration.api.hl7.messages.generators.helpers;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.datatype.TQ;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class QuantityTimingHelper {
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";

	@Autowired
	private HL7Config hl7Config;

	public void updateQuantityTiming(ORC orc, Obs obs) throws HL7Exception {
		int quantity = orc.getQuantityTimingReps();
		orc.insertQuantityTiming(quantity);
		SimpleDateFormat dateFormat = new SimpleDateFormat(hl7Config.getDefaultDateFormat());

		TQ quantityTiming = orc.getQuantityTiming(quantity);
		quantityTiming.getStartDateTime().getTime().setValue(dateFormat.format(obs.getEncounter().getEncounterDatetime()));

		// TODO
		//quantityTiming.getPriority().setValue(PriorityMapper.map(order.getUrgency()));

		orc.getQuantityTiming()[quantity] = quantityTiming;
	}

	public void updateQuantityTiming(OBR obr, Obs obs) throws HL7Exception {
		int quantity = obr.getQuantityTimingReps();
		obr.insertQuantityTiming(quantity);
		SimpleDateFormat dateFormat = new SimpleDateFormat(hl7Config.getDefaultDateFormat());

		TQ quantityTiming = obr.getQuantityTiming(quantity);
		quantityTiming.getStartDateTime().getTime().setValue(dateFormat.format(obs.getEncounter().getEncounterDatetime()));

		// TODO
		//quantityTiming.getPriority().setValue(PriorityMapper.map(order.getUrgency()));

		obr.getQuantityTiming()[quantity] = quantityTiming;
	}
}
