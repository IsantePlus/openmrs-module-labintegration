package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.datatype.XAD;
import ca.uhn.hl7v2.model.v25.segment.PID;
import org.openmrs.Patient;
import org.openmrs.PersonAddress;
import org.springframework.stereotype.Component;

@Component
public class PidAddressHelper {
	
	public void updateAddresses(PID pid, Patient patient) throws DataTypeException {
		int i = 0;
		for (PersonAddress personAddress : patient.getAddresses()) {
			XAD xad = pid.getPatientAddress(i);
			
			xad.getCity().setValue(personAddress.getCityVillage());
			xad.getStateOrProvince().setValue(personAddress.getStateProvince());
			xad.getZipOrPostalCode().setValue(personAddress.getPostalCode());
			
			i++;
		}
	}
}
