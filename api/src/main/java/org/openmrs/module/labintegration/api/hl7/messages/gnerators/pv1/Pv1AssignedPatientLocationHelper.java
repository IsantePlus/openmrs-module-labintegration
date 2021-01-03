package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pv1;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import org.openmrs.Encounter;
import org.openmrs.LocationAttribute;
import org.openmrs.PatientIdentifier;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.util.PatientUtil;
import org.springframework.stereotype.Component;

@Component
public class Pv1AssignedPatientLocationHelper {
	
	public void updateAssignedPatientLocation(PV1 pv1, HL7Config hl7Config, Encounter encounter) throws DataTypeException,
	        MessageCreationException {

		//Code added to set location id to siteCode
		String siteCode = "";
		String uuid = "0e52924e-4ebb-40ba-9b83-b198b532653b";

		for (LocationAttribute locationAttribute : encounter.getLocation().getAttributes()) {

			if (locationAttribute.getAttributeType().getUuid().equals(uuid)) {
				siteCode = locationAttribute.getValueReference();
			}
		}
		
		PatientIdentifier id = PatientUtil
		        .getPatientIdentifier(encounter.getPatient(), hl7Config.getPatientIdentifierTypeUuid());
		
		if (id.getLocation() != null) {
			pv1.getAssignedPatientLocation().getPl1_PointOfCare().setValue(siteCode);
			//pv1.getAssignedPatientLocation().getPl1_PointOfCare().setValue(String.valueOf(id.getLocation().getLocationId()));
		}
	}


	public void assignedAlternativeVisitId(PV1 pv1, HL7Config hl7Config, Encounter encounter) throws HL7Exception,
			MessageCreationException {

		//Code added to set location id to siteCode
		String siteCode = "";
		String uuid = "0e52924e-4ebb-40ba-9b83-b198b532653b";

		for (LocationAttribute locationAttribute : encounter.getLocation().getAttributes()) {

			if (locationAttribute.getAttributeType().getUuid().equals(uuid)) {
				siteCode = locationAttribute.getValueReference();
			}
		}

		PatientIdentifier id = PatientUtil
				.getPatientIdentifier(encounter.getPatient(), hl7Config.getPatientIdentifierTypeUuid());
		pv1.getAlternateVisitID().getIDNumber().setValue(id.getIdentifier());

		// get encounter type uuid and encounter uuid
		pv1.getAlternateVisitID().getCheckDigit().setValue(encounter.getEncounterType().getUuid());
		pv1.getAlternateVisitID().getIdentifierTypeCode().setValue(encounter.getUuid());

		//get location location uuid 'To be tested'
			pv1.getAlternateVisitID().getAssigningAuthority().parse(encounter.getLocation().getUuid());


		if (id.getLocation() != null) {
			pv1.getAssignedPatientLocation().getPl1_PointOfCare().setValue(siteCode);
			//pv1.getAssignedPatientLocation().getPl1_PointOfCare().setValue(String.valueOf(id.getLocation().getLocationId()));
		}
	}
}
