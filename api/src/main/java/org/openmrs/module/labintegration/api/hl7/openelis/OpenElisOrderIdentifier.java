package org.openmrs.module.labintegration.api.hl7.openelis;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;

public class OpenElisOrderIdentifier extends OrderIdentifier {

	@Override
	public void updateORC(ORC orc, Obs obs) throws HL7Exception {
		updateOrderTypeID(orc, obs);
	}

	@Override
	public void updateOBR(OBR obr, Obs obs) throws DataTypeException {
		updateUniversalServiceID(obr, obs);
	}
}
