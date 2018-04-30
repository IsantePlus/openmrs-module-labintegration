package org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.datatype.FN;
import ca.uhn.hl7v2.model.v25.datatype.XCN;
import ca.uhn.hl7v2.model.v25.datatype.XPN;
import org.openmrs.PersonName;
import org.springframework.stereotype.Component;

@Component
public class PersonNameHelper {
	
	public void updatePersonName(XPN xpn, PersonName personName) throws DataTypeException {
		updateFamilyName(xpn.getFamilyName(), personName);
		
		xpn.getGivenName().setValue(personName.getGivenName());
		xpn.getSecondAndFurtherGivenNamesOrInitialsThereof().setValue(personName.getMiddleName());
		
		xpn.getPrefixEgDR().setValue(personName.getPrefix());
		xpn.getDegreeEgMD().setValue(personName.getDegree());
	}
	
	public void updatePersonName(XCN xcn, PersonName personName) throws DataTypeException {
		updateFamilyName(xcn.getFamilyName(), personName);
		
		xcn.getGivenName().setValue(personName.getGivenName());
		xcn.getSecondAndFurtherGivenNamesOrInitialsThereof().setValue(personName.getMiddleName());
		
		xcn.getPrefixEgDR().setValue(personName.getPrefix());
		xcn.getDegreeEgMD().setValue(personName.getDegree());
	}
	
	private void updateFamilyName(FN familyName, PersonName personName) throws DataTypeException {
		familyName.getSurname().setValue(personName.getFamilyName());
		familyName.getOwnSurnamePrefix().setValue(personName.getFamilyNamePrefix());
	}
	
}
