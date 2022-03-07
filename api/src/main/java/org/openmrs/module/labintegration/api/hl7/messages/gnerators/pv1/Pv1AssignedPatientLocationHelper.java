package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pv1;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import org.openmrs.Encounter;
import org.openmrs.LocationAttribute;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Pv1AssignedPatientLocationHelper {

	private static final String ECID_UUID = "05a29f94-c0ed-11e2-94be-8c13b969e334";

	private static final Logger LOGGER = LoggerFactory.getLogger(Pv1AssignedPatientLocationHelper.class);

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
			pv1.getAlternateVisitID().getIDNumber().setValue(encounter.getPatient().getUuid());
			pv1.getAssignedPatientLocation().getPl1_PointOfCare().setValue(siteCode);

		} catch (Exception ex) {
			LOGGER.error("Could not create PV1 message! \n" + ex.getLocalizedMessage());
		}

		// get encounter type uuid and encounter uuid
		pv1.getAlternateVisitID().getCheckDigit().setValue(encounter.getEncounterType().getUuid());
		pv1.getAlternateVisitID().getIdentifierTypeCode().setValue(encounter.getUuid());

		//get location location uuid 'To be tested'
		pv1.getAlternateVisitID().getAssigningAuthority().parse(encounter.getLocation().getUuid());
	}
}
