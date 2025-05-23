package org.openmrs.module.labintegration.api.hl7.util;

import ca.uhn.hl7v2.model.v25.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.OBR;

public final class OpenElisStatusHelper {
	
	public static String getStatus(ORU_R01 oru) {
		ORU_R01_ORDER_OBSERVATION orderObs = oru.getPATIENT_RESULT().getORDER_OBSERVATION(0);
		OBR obr = orderObs.getOBR();
		String resultStatus = obr.getObr25_ResultStatus().getValue();
		
		if ("O".equals(resultStatus)) {
			return "Order Received";
		} else if ("I".equals(resultStatus)) {
			return "Specimen Received";
		} else if ("P".equals(resultStatus)) {
			return "Preliminary Result";
		} else if ("A".equals(resultStatus)) {
			return "Result";
		} else if ("F".equals(resultStatus)) {
			return "Final Result";
		} else if ("C".equals(resultStatus)) {
			return "Correction";
		} else if ("X".equals(resultStatus)) {
			return "Order Cancelled";
		} else {
			return "Unknown";
		}
	}
	
	private OpenElisStatusHelper() {
	}
}
