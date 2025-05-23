package org.openmrs.module.labintegration.api.hl7.config;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.messages.util.ConceptUtil;

public abstract class OrderIdentifier {
	
	public static final char ID_SEPARATOR = ';';
	
	public abstract void updateORC(ORC orc, Obs obs) throws HL7Exception;
	
	public abstract void updateOBR(OBR obr, Obs obs) throws HL7Exception;
	
	public abstract void updatePlacerOrderNumber(ORC orc, Obs obs) throws DataTypeException;
	
	public abstract void updateUniversalServiceID(OBR obr, Obs obs) throws DataTypeException;
	
	protected void updateOrderTypeID(ORC orc, Obs obs) throws DataTypeException {
		String conceptCode = ConceptUtil.getLoincCode(obs);
		
		orc.getOrderType().getIdentifier().setValue(conceptCode);
	}
}
