package org.openmrs.module.labintegration.api.hl7.config;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.messages.util.ConceptUtil;

public interface OrderIdentifier {

	char ID_SEPARATOR = ';';

	void updateORC(ORC orc, Order order) throws HL7Exception;
	
	void updateOBR(OBR obr, Order order) throws HL7Exception;
	
	default void updateOrderTypeID(ORC orc, Order order) throws DataTypeException {
		String conceptCode = ConceptUtil.getLoincCode(order.getConcept());
		if (StringUtils.isBlank(conceptCode)) {
			throw new IllegalStateException("LOINC code is mandatory");
		}

		orc.getOrderType().getIdentifier().setValue(conceptCode);
	}
	
	default void updateUniversalServiceID(OBR obr, Order order) throws DataTypeException {
		String encounterType = order.getEncounter().getEncounterType().getName();
		String encounterUuid = order.getEncounter().getUuid();

		if (StringUtils.isBlank(encounterType) || StringUtils.isBlank(encounterUuid)) {
			throw new IllegalStateException("Encounter type and encounter UUID are mandatory");
		}

		String identifier = encounterType + ID_SEPARATOR + encounterUuid;

		obr.getUniversalServiceIdentifier().getIdentifier().setValue(identifier);
	}

}
