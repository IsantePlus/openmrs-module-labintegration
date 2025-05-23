package org.openmrs.module.labintegration.api.hl7.scc;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.datatype.EI;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.openmrs.Encounter;
import org.openmrs.LocationAttribute;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.openmrs.module.labintegration.api.hl7.messages.generators.helpers.LnspCodeHelper;
import org.openmrs.module.labintegration.api.hl7.messages.generators.helpers.OrderingProviderHelper;
import org.openmrs.module.labintegration.api.hl7.messages.generators.helpers.QuantityTimingHelper;
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
		generatePlacerOrderNumber(obs, orc.getPlacerOrderNumber());
	}
	
	@Override
	public void updateOBR(OBR obr, Obs obs) throws HL7Exception {
		updateUniversalServiceID(obr, obs);
		
		orderingProviderHelper.updateOrderingProvider(obr, obs);
		quantityTimingHelper.updateQuantityTiming(obr, obs);
		
		String encounterLocationUuid = obs.getEncounter().getLocation().getUuid();
		obr.getPlacerOrderNumber().getEntityIdentifier().setValue(encounterLocationUuid);
		
		generatePlacerOrderNumber(obs, obr.getPlacerOrderNumber());
		
		obr.getSpecimenActionCode().setValue(DEFAULT_ACTION_CODE);
	}
	
	@Override
	public void updateUniversalServiceID(OBR obr, Obs obs) throws DataTypeException {
		lnspCodeHelper.updateUniversalServiceID(obr, obs);
	}
	
	private void generatePlacerOrderNumber(Obs obs, EI placerOrderNumber) throws DataTypeException {
		//added site code to Placer Order number: site code + obs id
		
		Encounter encounter = obs.getEncounter();
		
		String siteCode = "";
		String uuid = "0e52924e-4ebb-40ba-9b83-b198b532653b";
		
		for (LocationAttribute locationAttribute : encounter.getLocation().getActiveAttributes()) {
			
			if (locationAttribute.getAttributeType().getUuid().equals(uuid)) {
				siteCode = locationAttribute.getValueReference();
			}
		}
		
		placerOrderNumber.getEntityIdentifier().setValue(siteCode + '-' + encounter.getEncounterId() + '-' + obs.getObsId());
	}
}
