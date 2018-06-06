package org.openmrs.module.labintegration.api.hl7.config;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.messages.util.ConceptUtil;

public abstract class OrderIdentifier {

	public static final char ID_SEPARATOR = ';';

	public abstract void updateORC(ORC orc, Obs obs) throws HL7Exception;
	
	public abstract void updateOBR(OBR obr, Obs obs) throws HL7Exception;

	public abstract void updateUniversalServiceID(OBR obr, Obs obs) throws DataTypeException;
	
	protected void updateOrderTypeID(ORC orc, Obs obs) throws DataTypeException {
		Concept testConcept = obs.getValueCoded();
		if (testConcept == null) {
			throw new IllegalStateException("Wrong concept used for ordered test: " + obs.getConcept().getId());
		}

		String conceptCode = ConceptUtil.getLoincCode(testConcept);
		if (StringUtils.isBlank(conceptCode)) {
			throw new IllegalStateException("LOINC code is mandatory");
		}

		orc.getOrderType().getIdentifier().setValue(conceptCode);
	}
}
