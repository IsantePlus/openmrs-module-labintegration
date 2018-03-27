package org.openmrs.module.labintegration.api.hl7.messages.util;

import org.openmrs.Patient;
import org.openmrs.PersonAttribute;

public final class PatientUtil {
	
	public static String getAttribute(Patient patient, String name) {
		PersonAttribute attribute = patient.getAttribute(name);
		return attribute == null ? null : attribute.getValue();
	}
	
	private PatientUtil() {
	}
}
