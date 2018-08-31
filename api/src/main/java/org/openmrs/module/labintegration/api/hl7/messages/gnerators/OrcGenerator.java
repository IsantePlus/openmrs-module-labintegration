package org.openmrs.module.labintegration.api.hl7.messages.gnerators;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.springframework.stereotype.Component;

@Component
public class OrcGenerator {

	public void updateOrc(ORC orc, Obs obs, String orderControl, OrderIdentifier orderIdentifier)
	        throws HL7Exception {
		orc.getOrderControl().setValue(orderControl);

		orderIdentifier.updateORC(orc, obs);
	}
}
