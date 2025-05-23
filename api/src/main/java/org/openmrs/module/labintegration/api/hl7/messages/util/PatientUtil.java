package org.openmrs.module.labintegration.api.hl7.messages.util;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonAttribute;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;

public final class PatientUtil {
	
	public static String getAttribute(Patient patient, String name) {
		PersonAttribute attribute = patient.getAttribute(name);
		return attribute == null ? null : attribute.getValue();
	}
	
	public static PatientIdentifier getPatientIdentifier(Patient patient, String typeUuid) throws MessageCreationException {
		PatientIdentifier id = null;
		
		if (typeUuid == null) {
			return null;
		}
		
		for (PatientIdentifier identifier : patient.getIdentifiers()) {
			if (StringUtils.equals(identifier.getIdentifierType().getUuid(), typeUuid)) {
				id = identifier;
				break;
			}
		}
		
		if (id == null) {
			throw new MessageCreationException(
			        String.format("Patient %s does not have an identifier with UUID: %s", patient.getPatientId(), typeUuid));
		}
		
		return id;
	}
	
	private PatientUtil() {
	}
}
