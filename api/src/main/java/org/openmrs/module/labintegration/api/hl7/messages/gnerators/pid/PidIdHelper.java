package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.PID;
import org.openmrs.Encounter;
import org.openmrs.LocationAttribute;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.util.PatientUtil;
import org.springframework.stereotype.Component;

@Component
public class PidIdHelper {
	
	public void updateIdNumber(PID pid, Patient patient, HL7Config hl7Config, Encounter encounter) throws DataTypeException,
	        MessageCreationException {

		String siteCode = "";
		String uuid = "0e52924e-4ebb-40ba-9b83-b198b532653b";
		String uuidiSanteId = "0e0c7cc2-3491-4675-b705-746e372ff346";

		for (LocationAttribute locationAttribute : encounter.getLocation().getAttributes()) {

			if (locationAttribute.getAttributeType().getUuid().equals(uuid)) {
				siteCode = locationAttribute.getValueReference();
			}
		}

		PatientIdentifier id = PatientUtil.getPatientIdentifier(patient, hl7Config.getPatientIdentifierTypeUuid());
		/* pid.getPatientID().getIDNumber().setValue(id.getIdentifier()); */
		//pid.getPatientID().getIDNumber().setValue(id.getId().toString());

		// old patientID from the legacy system iSante
		PatientIdentifier patientID = PatientUtil.getPatientIdentifier(patient, uuidiSanteId);

		if(patientID != null){
			pid.getPatientID().getIDNumber().setValue(patientID.getId().toString());
		} else {
			pid.getPatientID().getIDNumber().setValue(siteCode + id.getId().toString());
		}

		//pid.getPatientID().getIDNumber().setValue(siteCode + id.getId().toString());

		if (id.getLocation() != null) {
			pid.getPatientID().getAssigningFacility().getNamespaceID()
					.setValue(siteCode);
			/* pid.getPatientID().getAssigningFacility().getNamespaceID()
			        .setValue(String.valueOf(id.getLocation().getLocationId()));  */
		}
	}
}
