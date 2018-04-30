package org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.datatype.TQ;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.messages.mappers.PriorityMapper;
import org.springframework.stereotype.Component;

@Component
public class QuantityTimingHelper {

	public void updateQuantityTiming(ORC orc, Order order) throws HL7Exception {
		int quantity = orc.getQuantityTimingReps();
		orc.insertQuantityTiming(quantity);

		TQ quantityTiming = orc.getQuantityTiming(quantity);
		quantityTiming.getStartDateTime().getTime().setValue(order.getEffectiveStartDate());
		quantityTiming.getPriority().setValue(PriorityMapper.map(order.getUrgency()));

		orc.getQuantityTiming()[quantity] = quantityTiming;
	}

	public void updateQuantityTiming(OBR obr, Order order) throws HL7Exception {
		int quantity = obr.getQuantityTimingReps();
		obr.insertQuantityTiming(quantity);

		TQ quantityTiming = obr.getQuantityTiming(quantity);
		quantityTiming.getStartDateTime().getTime().setValue(order.getEffectiveStartDate());
		quantityTiming.getPriority().setValue(PriorityMapper.map(order.getUrgency()));

		obr.getQuantityTiming()[quantity] = quantityTiming;
	}
}
