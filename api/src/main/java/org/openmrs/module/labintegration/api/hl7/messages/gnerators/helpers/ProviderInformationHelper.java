package org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.datatype.XCN;

import org.openmrs.Encounter;
import org.openmrs.PersonName;
import org.openmrs.Provider;
import org.openmrs.LocationAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderInformationHelper {
	
	@Autowired
	private PersonNameHelper personNameHelper;
	
	public void updateProviderInformation(XCN xcn, Provider provider, Encounter encounter) throws DataTypeException {
		String siteCode = "";
		String uuid = "0e52924e-4ebb-40ba-9b83-b198b532653b";
		
		for (LocationAttribute locationAttribute : encounter.getLocation().getAttributes()) {
			
			if (locationAttribute.getAttributeType().getUuid().equals(uuid)) {				
				siteCode = locationAttribute.getValueReference();
			}
		}
		
		//xcn.getIDNumber().setValue(provider.getIdentifier());
		xcn.getIDNumber().setValue(siteCode);
		PersonName personName = provider.getPerson().getPersonName();
		personNameHelper.updatePersonName(xcn, personName);
	}
}


