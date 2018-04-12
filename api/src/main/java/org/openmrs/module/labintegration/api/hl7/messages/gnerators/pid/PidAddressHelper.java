package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.datatype.XAD;
import ca.uhn.hl7v2.model.v251.segment.PID;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.springframework.stereotype.Component;

@Component
public class PidAddressHelper {
	
	private static final String HAITI = "HTI";
	
	public void updateAddresses(PID pid, Patient patient) throws DataTypeException {
		int i = 0;
		for (PersonAddress personAddress : patient.getAddresses()) {
			XAD xad = pid.getPatientAddress(i);
			
			xad.getStreetAddress().getSad1_StreetOrMailingAddress().setValue(personAddress.getAddress2());
			xad.getOtherDesignation().setValue(personAddress.getAddress3());
			xad.getCity().setValue(personAddress.getCityVillage());
			xad.getStateOrProvince().setValue(personAddress.getStateProvince());
			xad.getZipOrPostalCode().setValue(personAddress.getPostalCode());
			xad.getCountry().setValue(HAITI);
			xad.getOtherGeographicDesignation().setValue(personAddress.getAddress1());
			
			i++;
		}
	}
}
