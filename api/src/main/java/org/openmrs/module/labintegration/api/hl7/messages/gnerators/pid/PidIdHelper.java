package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.segment.PID;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.springframework.stereotype.Component;

@Component
public class PidIdHelper {
	
	public void updateIdNumber(PID pid, Patient patient, HL7Config hl7Config) throws DataTypeException,
	        MessageCreationException {
		PatientIdentifier id = null;
		
		String typeUuid = hl7Config.getPatientIdentifierTypeUuid();
		for (PatientIdentifier identifier : patient.getIdentifiers()) {
			if (StringUtils.equals(identifier.getIdentifierType().getUuid(), typeUuid)) {
				id = identifier;
				break;
			}
		}
		
		if (id == null) {
			throw new MessageCreationException(String.format("Patient %s does not have an identifier with UUID: %s",
			    patient.getPatientId(), typeUuid));
		}
		
		pid.getPatientID().getIDNumber().setValue(id.getIdentifier());
		if (id.getLocation() != null) {
			pid.getPatientID().getAssigningFacility().getNamespaceID()
			        .setValue(String.valueOf(id.getLocation().getLocationId()));
		}
	}
}
