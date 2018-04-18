package org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.datatype.XCN;
import org.openmrs.PersonName;
import org.openmrs.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderInformationHelper {
	
	@Autowired
	private PersonNameHelper personNameHelper;
	
	public void updateProviderInformation(XCN xcn, Provider provider) throws DataTypeException {
		xcn.getIDNumber().setValue(provider.getIdentifier());
		PersonName personName = provider.getPerson().getPersonName();
		personNameHelper.updatePersonName(xcn, personName);
	}
}
