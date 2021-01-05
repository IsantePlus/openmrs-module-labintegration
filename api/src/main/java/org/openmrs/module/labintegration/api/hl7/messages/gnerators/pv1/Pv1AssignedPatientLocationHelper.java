package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pv1;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import org.openmrs.Encounter;
import org.openmrs.PatientIdentifier;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.util.PatientUtil;
import org.springframework.stereotype.Component;

@Component
public class Pv1AssignedPatientLocationHelper {
	
	public void updateAssignedPatientLocation(PV1 pv1, HL7Config hl7Config, Encounter encounter) throws DataTypeException,
	        MessageCreationException {
		
		PatientIdentifier id = PatientUtil
		        .getPatientIdentifier(encounter.getPatient(), hl7Config.getPatientIdentifierTypeUuid());
		
		if (id.getLocation() != null) {
			pv1.getAssignedPatientLocation().getPl1_PointOfCare().setValue(String.valueOf(id.getLocation().getLocationId()));
		}
	}
}
