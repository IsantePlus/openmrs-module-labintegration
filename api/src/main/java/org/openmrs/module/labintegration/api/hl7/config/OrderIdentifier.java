package org.openmrs.module.labintegration.api.hl7.config;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;

public interface OrderIdentifier {
	
	char ID_SEPARATOR = ';';
	
	void updateORC(ORC orc) throws DataTypeException;
	
	void updateOBR(OBR obr) throws DataTypeException;
}
