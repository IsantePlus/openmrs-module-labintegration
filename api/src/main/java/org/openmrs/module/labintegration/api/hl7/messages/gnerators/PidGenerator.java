package org.openmrs.module.labintegration.api.hl7.messages.gnerators;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.segment.PID;
import org.openmrs.Patient;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid.PidAddressHelper;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid.PidIdHelper;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid.PidMotherNameHelper;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid.PidNameHelper;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid.RegistrationDataHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PidGenerator {
	
	@Autowired
	private PidAddressHelper addressHelper;
	
	@Autowired
	private PidNameHelper nameHelper;
	
	@Autowired
	private PidIdHelper idHelper;
	
	@Autowired
	private PidMotherNameHelper motherNameHelper;
	
	@Autowired
	private RegistrationDataHelper registrationObsHelper;
	
	public void updatePid(PID pid, Patient patient, HL7Config hl7Config) throws DataTypeException, MessageCreationException {
		idHelper.updateIdNumber(pid, patient, hl7Config);
		
		nameHelper.updateNames(pid, patient);
		motherNameHelper.updateMotherName(pid, patient, hl7Config);
		
		updateDateTimeOfBirth(pid, patient);
		updateAdministrativeSex(pid, patient);
		
		addressHelper.updateAddresses(pid, patient);
		
		registrationObsHelper.updateWithRegistrationInformation(pid, patient, hl7Config);
	}
	
	private void updateDateTimeOfBirth(PID pid, Patient patient) throws DataTypeException {
		pid.getDateTimeOfBirth().getTime().setValue(patient.getBirthDateTime());
	}
	
	private void updateAdministrativeSex(PID pid, Patient patient) throws DataTypeException {
		pid.getAdministrativeSex().setValue(patient.getGender());
	}
}
