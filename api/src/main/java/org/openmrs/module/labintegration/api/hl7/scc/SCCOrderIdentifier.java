package org.openmrs.module.labintegration.api.hl7.scc;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.openmrs.Encounter;
import org.openmrs.LocationAttribute;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers.LnspCodeHelper;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers.OrderingProviderHelper;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers.QuantityTimingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class SCCOrderIdentifier extends OrderIdentifier {

	private static final String DEFAULT_ACTION_CODE = "O";

	@Autowired
	private QuantityTimingHelper quantityTimingHelper;

	@Autowired
	private OrderingProviderHelper orderingProviderHelper;

	@Autowired
	private LnspCodeHelper lnspCodeHelper;

	@Autowired
	private HL7Config hl7Config;

	@Override
	public void updateORC(ORC orc, Obs obs) throws HL7Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(hl7Config.getDefaultDateFormat());
		updateOrderTypeID(orc, obs);
		updatePlacerOrderNumber(orc, obs);

		orderingProviderHelper.updateOrderingProvider(orc, obs);
		quantityTimingHelper.updateQuantityTiming(orc, obs);

		orc.getDateTimeOfTransaction().getTime().setValue(dateFormat.format(obs.getEncounter().getEncounterDatetime()));
	}

	@Override
	public void updatePlacerOrderNumber(ORC orc, Obs obs) throws DataTypeException {

		//added site code to Placer Order number: site code + encounter id

		Encounter encounter = obs.getEncounter();

		String siteCode = "";
		String uuid = "0e52924e-4ebb-40ba-9b83-b198b532653b";

		for (LocationAttribute locationAttribute : encounter.getLocation().getAttributes()) {

			if (locationAttribute.getAttributeType().getUuid().equals(uuid)) {
				siteCode = locationAttribute.getValueReference();
			}
		}

		Integer  encounterId = obs.getEncounter().getEncounterId();
		orc.getPlacerOrderNumber().getEntityIdentifier().setValue(siteCode + encounterId);

	/*	Integer  encounterLocationUuid = obs.getEncounter().getLocation().getId();
		orc.getPlacerOrderNumber().getEntityIdentifier().setValue(encounterLocationUuid.toString()); */

	}

	@Override
	public void updateOBR(OBR obr, Obs obs) throws HL7Exception {
		updateUniversalServiceID(obr, obs);

		orderingProviderHelper.updateOrderingProvider(obr, obs);
		quantityTimingHelper.updateQuantityTiming(obr, obs);
		
		String encounterLocationUuid = obs.getEncounter().getLocation().getUuid();
		obr.getPlacerOrderNumber().getEntityIdentifier().setValue(encounterLocationUuid);

		Encounter encounter = obs.getEncounter();

		String siteCode = "";
		String uuid = "0e52924e-4ebb-40ba-9b83-b198b532653b";

		for (LocationAttribute locationAttribute : encounter.getLocation().getAttributes()) {

			if (locationAttribute.getAttributeType().getUuid().equals(uuid)) {
				siteCode = locationAttribute.getValueReference();
			}
		}

		Integer  encounterId = obs.getEncounter().getEncounterId();
		obr.getPlacerOrderNumber().getEntityIdentifier().setValue(siteCode + encounterId);

		/*Integer encounterLocationUuid = obs.getEncounter().getLocation().getId();
		obr.getPlacerOrderNumber().getEntityIdentifier().setValue(encounterLocationUuid.toString());*/

		obr.getSpecimenActionCode().setValue(DEFAULT_ACTION_CODE);
	}

	@Override
	public void updateUniversalServiceID(OBR obr, Obs obs) throws DataTypeException {
		lnspCodeHelper.updateUniversalServiceID(obr, obs);
	}
}
