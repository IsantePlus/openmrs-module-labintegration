package org.openmrs.module.labintegration.api.hl7.scc;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers.LnspCodeHelper;
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

	@Autowired
	private LnspCodeHelper lnspCodeHelper;

	@Override
	public void updateORC(ORC orc, Obs obs) throws HL7Exception {
		updateOrderTypeID(orc, obs);
		updatePlacerOrderNumber(orc, obs);

		orderingProviderHelper.updateOrderingProvider(orc, obs);
		quantityTimingHelper.updateQuantityTiming(orc, obs);

		orc.getDateTimeOfTransaction().getTime().setValue(obs.getEncounter().getEncounterDatetime());
	}

	@Override
	public void updatePlacerOrderNumber(ORC orc, Obs obs) throws DataTypeException {
		String encounterLocationUuid = obs.getEncounter().getLocation().getUuid();
		orc.getPlacerOrderNumber().getEntityIdentifier().setValue(encounterLocationUuid);
	}

	@Override
	public void updateOBR(OBR obr, Obs obs) throws HL7Exception {
		updateUniversalServiceID(obr, obs);

		orderingProviderHelper.updateOrderingProvider(obr, obs);
		quantityTimingHelper.updateQuantityTiming(obr, obs);
		
		String encounterLocationUuid = obs.getEncounter().getLocation().getUuid();
		obr.getPlacerOrderNumber().getEntityIdentifier().setValue(encounterLocationUuid);

		obr.getSpecimenActionCode().setValue(DEFAULT_ACTION_CODE);
	}

	@Override
	public void updateUniversalServiceID(OBR obr, Obs obs) throws DataTypeException {
		lnspCodeHelper.updateUniversalServiceID(obr, obs);
	}
}
