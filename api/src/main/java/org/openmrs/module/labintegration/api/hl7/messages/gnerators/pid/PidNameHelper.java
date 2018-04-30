package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.datatype.XPN;
import ca.uhn.hl7v2.model.v25.segment.PID;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers.PersonNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PidNameHelper {
	
	@Autowired
	private PersonNameHelper personNameHelper;
	
	public void updateNames(PID pid, Patient patient) throws DataTypeException {
		int i = 0;
		for (PersonName personName : patient.getNames()) {
			XPN xpn = pid.getPatientName(i);
			personNameHelper.updatePersonName(xpn, personName);
			i++;
		}
	}
	
}
