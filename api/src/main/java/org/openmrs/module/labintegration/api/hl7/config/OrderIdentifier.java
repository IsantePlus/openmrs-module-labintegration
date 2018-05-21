package org.openmrs.module.labintegration.api.hl7.config;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.messages.util.ConceptUtil;

public abstract class OrderIdentifier {

	public static final char ID_SEPARATOR = ';';

	public abstract void updateORC(ORC orc, Obs obs) throws HL7Exception;
	
	public abstract void updateOBR(OBR obr, Obs obs) throws HL7Exception;
	
	protected void updateOrderTypeID(ORC orc, Obs obs) throws DataTypeException {
		String conceptCode = ConceptUtil.getLoincCode(obs.getConcept());
		if (StringUtils.isBlank(conceptCode)) {
			throw new IllegalStateException("LOINC code is mandatory");
		}

		orc.getOrderType().getIdentifier().setValue(conceptCode);
	}
	
	protected void updateUniversalServiceID(OBR obr, Obs obs) throws DataTypeException {
		String encounterType = obs.getEncounter().getEncounterType().getName();
		String encounterUuid = obs.getEncounter().getUuid();

		if (StringUtils.isBlank(encounterType) || StringUtils.isBlank(encounterUuid)) {
			throw new IllegalStateException("Encounter type and encounter UUID are mandatory");
		}

		String identifier = encounterType + ID_SEPARATOR + encounterUuid;

		obr.getUniversalServiceIdentifier().getIdentifier().setValue(identifier);
	}

}
