package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.PID;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.util.PatientUtil;
import org.springframework.stereotype.Component;

@Component
public class PidIdHelper {
	
	public void updateIdNumber(PID pid, Patient patient, HL7Config hl7Config) throws DataTypeException,
	        MessageCreationException {
		
		PatientIdentifier id = PatientUtil.getPatientIdentifier(patient, hl7Config.getPatientIdentifierTypeUuid());
		
		pid.getPatientID().getIDNumber().setValue(id.getIdentifier());
		if (id.getLocation() != null) {
			pid.getPatientID().getAssigningFacility().getNamespaceID()
			        .setValue(String.valueOf(id.getLocation().getLocationId()));
		}
	}
}
