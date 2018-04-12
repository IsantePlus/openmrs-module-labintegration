package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v251.datatype.XPN;
import ca.uhn.hl7v2.model.v251.segment.PID;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.springframework.stereotype.Component;

@Component
public class PidNameHelper {
	
	public void updateNames(PID pid, Patient patient) throws DataTypeException {
		int i = 0;
		for (PersonName personName : patient.getNames()) {
			XPN xpn = pid.getPatientName(i);
			
			xpn.getFamilyName().getSurname().setValue(personName.getFamilyName());
			xpn.getFamilyName().getOwnSurnamePrefix().setValue(personName.getFamilyNamePrefix());
			
			xpn.getGivenName().setValue(personName.getGivenName());
			xpn.getSecondAndFurtherGivenNamesOrInitialsThereof().setValue(personName.getMiddleName());
			
			xpn.getPrefixEgDR().setValue(personName.getPrefix());
			xpn.getDegreeEgMD().setValue(personName.getDegree());
			
			i++;
		}
	}
	
}
