package org.openmrs.module.labintegration.api.hl7.messages.generators.pid;

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
import org.apache.commons.lang3.StringUtils;

@Component
public class PidIdHelper {
	
	private static final String OLD_SITE_CODE_UUID = "0e52924e-4ebb-40ba-9b83-b198b532653b";
	
	private static final String NEW_SITE_CODE_UUID = "6242bf19-207e-4076-9d28-9290525b8ed9";
	
	private static final String ISANTE_ID_UUID = "0e0c7cc2-3491-4675-b705-746e372ff346";
	
	public void updateIdNumber(PID pid, Patient patient, HL7Config hl7Config, Encounter encounter)
	        throws DataTypeException, MessageCreationException {
		
		String oldSiteCode = null;
		String newSiteCode = null;
		PatientIdentifier isanteIdentifier = null;
		
		for (LocationAttribute locationAttribute : encounter.getLocation().getAttributes()) {
			if (locationAttribute.getAttributeType().getUuid().equals(OLD_SITE_CODE_UUID)) {
				oldSiteCode = locationAttribute.getValueReference();
			} else if (locationAttribute.getAttributeType().getUuid().equals(NEW_SITE_CODE_UUID)) {
				newSiteCode = locationAttribute.getValueReference();
			}
		}
		
		String siteCode = oldSiteCode != null ? oldSiteCode : newSiteCode != null ? newSiteCode : "";
		
		PatientIdentifier id = PatientUtil.getPatientIdentifier(patient, hl7Config.getPatientIdentifierTypeUuid());
		
		for (PatientIdentifier identifier : patient.getIdentifiers()) {
			if (StringUtils.equals(identifier.getIdentifierType().getUuid(), ISANTE_ID_UUID)) {
				isanteIdentifier = identifier;
				pid.getPatientID().getIDNumber().setValue(isanteIdentifier.toString());
				
				break;
			}
		}
		
		if (isanteIdentifier == null) {
			pid.getPatientID().getIDNumber().setValue(siteCode + "4" + patient.getPatientId().toString());
		}
		
		if (id.getLocation() != null) {
			pid.getPatientID().getAssigningFacility().getNamespaceID().setValue(siteCode);
		}
		
		pid.getPatientID().getAssigningFacility().getNamespaceID().setValue(siteCode);
	}
}
