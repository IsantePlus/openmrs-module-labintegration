package org.openmrs.module.labintegration.api.hl7.messages.generators.pid;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.PID;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.util.EncounterUtil;
import org.openmrs.parameter.EncounterSearchCriteria;
import org.openmrs.parameter.EncounterSearchCriteriaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class RegistrationDataHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(RegistrationDataHelper.class);
	
	@Autowired
	private EncounterService encounterService;
	
	public void updateWithRegistrationInformation(PID pid, Patient patient, HL7Config hl7Config) throws DataTypeException {
		Encounter regEncounter = getRegEncounter(patient, hl7Config);
		
		if (regEncounter == null) {
			LOG.warn("No registration form found for patient: " + patient.toString());
		} else {
			String religion = EncounterUtil.getCodedObsValue(regEncounter, hl7Config.getReligionConceptId());
			pid.getReligion().getText().setValue(religion);
			
			String civilStatus = EncounterUtil.getCodedObsValue(regEncounter, hl7Config.getCivilStatusConceptId());
			pid.getMaritalStatus().getText().setValue(civilStatus);
			
			String birthCity = EncounterUtil.getObsTextValue(regEncounter, hl7Config.getBirthPlaceCityConceptId(),
			    hl7Config.getBirthPlaceGroupConceptId());
			pid.getBirthPlace().setValue(birthCity);
		}
	}
	
	private Encounter getRegEncounter(Patient patient, HL7Config hl7Config) {
		EncounterType encounterType = hl7Config.getRegistrationFormEncounterType();
		
		EncounterSearchCriteria encounterSearchCriteria = new EncounterSearchCriteriaBuilder()
		        .setEncounterTypes(Collections.singletonList(encounterType)).setPatient(patient)
		        .createEncounterSearchCriteria();
		
		List<Encounter> encounters = encounterService.getEncounters(encounterSearchCriteria);
		
		return encounters.isEmpty() ? null : encounters.get(0);
	}
	
}
