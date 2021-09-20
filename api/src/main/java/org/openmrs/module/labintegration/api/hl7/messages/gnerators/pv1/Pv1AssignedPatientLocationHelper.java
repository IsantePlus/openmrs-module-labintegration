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

	private static final String ECID_UUID = "f54ed6b9-f5b9-4fd5-a588-8f7561a78401";

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

		if (siteCode != null) {
			pv1.getAssignedPatientLocation().getPl1_PointOfCare().setValue(siteCode);
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

		try {
			PatientIdentifier id = PatientUtil
					.getPatientIdentifier(encounter.getPatient(), ECID_UUID);
			pv1.getAlternateVisitID().getIDNumber().setValue(id.getIdentifier());
			if (id.getLocation() != null) {
				pv1.getAssignedPatientLocation().getPl1_PointOfCare().setValue(siteCode);
			}
		} catch (MessageCreationException ex) {
			pv1.getAlternateVisitID().getIDNumber().setValue(encounter.getPatient().getUuid());
			pv1.getAssignedPatientLocation().getPl1_PointOfCare().setValue(siteCode);
		}

		// get encounter type uuid and encounter uuid
		pv1.getAlternateVisitID().getCheckDigit().setValue(encounter.getEncounterType().getUuid());
		pv1.getAlternateVisitID().getIdentifierTypeCode().setValue(encounter.getUuid());

		//get location location uuid 'To be tested'
		pv1.getAlternateVisitID().getAssigningAuthority().parse(encounter.getLocation().getUuid());

	}
}
