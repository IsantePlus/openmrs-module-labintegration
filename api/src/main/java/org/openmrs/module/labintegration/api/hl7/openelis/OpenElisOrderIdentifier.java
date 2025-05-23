package org.openmrs.module.labintegration.api.hl7.openelis;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Obs;
import org.openmrs.Provider;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;

public class OpenElisOrderIdentifier extends OrderIdentifier {
	
	@Override
	public void updateORC(ORC orc, Obs obs) throws HL7Exception {
		updateOrderTypeID(orc, obs);
		updatePlacerOrderNumber(orc, obs);
	}
	
	@Override
	public void updateOBR(OBR obr, Obs obs) throws DataTypeException {
		updateUniversalServiceID(obr, obs);
	}
	
	@Override
	public void updatePlacerOrderNumber(ORC orc, Obs obs) throws DataTypeException {
		Provider provider = obs.getEncounter().getEncounterProviders().iterator().next().getProvider();
		
		String providerId = provider.getIdentifier();
		orc.getPlacerOrderNumber().getEntityIdentifier().setValue(providerId);
	}
	
	@Override
	public void updateUniversalServiceID(OBR obr, Obs obs) throws DataTypeException {
		String encounterType = obs.getEncounter().getEncounterType().getName();
		String encounterUuid = obs.getEncounter().getUuid();
		
		if (StringUtils.isBlank(encounterType) || StringUtils.isBlank(encounterUuid)) {
			throw new IllegalStateException("Encounter type and encounter UUID are mandatory");
		}
		
		String identifier = encounterType + ID_SEPARATOR + encounterUuid;
		
		obr.getUniversalServiceIdentifier().getIdentifier().setValue(identifier);
	}
	
}
