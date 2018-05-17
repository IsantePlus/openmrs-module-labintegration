package org.openmrs.module.labintegration.api.hl7.scc;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers.OrderingProviderHelper;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers.QuantityTimingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SCCOrderIdentifier extends OrderIdentifier {

	private static final String DEFAULT_ACTION_CODE = "O";

	@Autowired
	private QuantityTimingHelper quantityTimingHelper;

	@Autowired
	private OrderingProviderHelper orderingProviderHelper;

	@Override
	public void updateORC(ORC orc, Obs obs) throws HL7Exception {
		updateOrderTypeID(orc, obs);

		orderingProviderHelper.updateOrderingProvider(orc, obs);
		quantityTimingHelper.updateQuantityTiming(orc, obs);

		orc.getDateTimeOfTransaction().getTime().setValue(obs.getEncounter().getEncounterDatetime());
	}

	@Override
	public void updateOBR(OBR obr, Obs obs) throws HL7Exception {
		updateUniversalServiceID(obr, obs);

		orderingProviderHelper.updateOrderingProvider(obr, obs);
		quantityTimingHelper.updateQuantityTiming(obr, obs);

		obr.getSpecimenActionCode().setValue(DEFAULT_ACTION_CODE);
	}
}
