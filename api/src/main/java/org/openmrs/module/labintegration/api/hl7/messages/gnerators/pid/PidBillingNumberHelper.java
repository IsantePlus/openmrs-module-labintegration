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
public class PidBillingNumberHelper {
	
	public void updateBillingNumber(PID pid, Patient patient, HL7Config hl7Config) throws DataTypeException,
	        MessageCreationException {
		pid.getPatientAccountNumber().getAssigningFacility().getNamespaceID()
		        .setValue(hl7Config.getSendingFacilityNamespaceID());
		
		PatientIdentifier id = PatientUtil.getPatientIdentifier(patient, hl7Config.getBillingNumberTypeUuid());
		
		pid.getPatientAccountNumber().getCheckDigit().setValue(id != null ? id.getIdentifier() : null);
	}
}
