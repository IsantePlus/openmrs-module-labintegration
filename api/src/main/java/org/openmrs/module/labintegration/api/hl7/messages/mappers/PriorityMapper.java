package org.openmrs.module.labintegration.api.hl7.messages.mappers;

import org.openmrs.Order;

public final class PriorityMapper {

	public static String map(Order.Urgency urgency) {
		switch (urgency) {
			case STAT:
				return "S";
			case ROUTINE:
				return "R";
			case ON_SCHEDULED_DATE:
				return "T";
			default:
				throw new IllegalArgumentException(urgency + " is not a proper urgency value");
		}
	}
}
