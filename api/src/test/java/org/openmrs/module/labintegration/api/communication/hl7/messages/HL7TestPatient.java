package org.openmrs.module.labintegration.api.communication.hl7.messages;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.PersonName;

import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HL7TestPatient {
	
	private final Patient patient;
	
	public HL7TestPatient() {
		patient = mock(Patient.class);
		
		mockIdentifiers();
		mockNames();
		mockAddresses();
		mockAttributes();
		
		when(patient.getBirthdate()).thenReturn(new Date());
		when(patient.getGender()).thenReturn("M");
	}
	
	public Patient value() {
		return patient;
	}
	
	private void mockIdentifiers() {
		PatientIdentifierType openMrsIdType = new PatientIdentifierType();
		openMrsIdType.setUuid("05a29f94-c0ed-11e2-94be-8c13b969e334");
		PatientIdentifier openMrsId = new PatientIdentifier("ST-1000", openMrsIdType, new Location());
		
		PatientIdentifierType otherIdType = new PatientIdentifierType();
		otherIdType.setUuid(UUID.randomUUID().toString());
		PatientIdentifier otherId = new PatientIdentifier("OTHER ID", otherIdType, new Location());
		
		when(patient.getIdentifiers()).thenReturn(new HashSet<PatientIdentifier>(asList(otherId, openMrsId)));
	}
	
	private void mockNames() {
		PersonName name1 = new PersonName("John", "Tom", "Doe");
		PersonName name2 = new PersonName("Terry", "Jim", "Daniels");
		
		when(patient.getNames()).thenReturn(new HashSet<PersonName>(asList(name1, name2)));
	}
	
	private void mockAddresses() {
		PersonAddress address1 = new PersonAddress();
		address1.setCityVillage("Gdynia");
		address1.setStateProvince("Pomorskie");
		address1.setPostalCode("81651");
		
		PersonAddress address2 = new PersonAddress();
		address2.setCityVillage("Iłowo-Osada");
		address2.setStateProvince("Warmińsko-Mazurskie");
		address2.setPostalCode("13240");
		
		when(patient.getAddresses()).thenReturn(new HashSet<PersonAddress>(asList(address1, address2)));
	}
	
	private void mockAttributes() {
		PersonAttributeType motherNameType = new PersonAttributeType();
		motherNameType.setName("Mother Name");
		PersonAttribute motherAttr = new PersonAttribute(motherNameType, "Janine");
		
		PersonAttributeType phoneNumType = new PersonAttributeType();
		phoneNumType.setName("Telephone Number");
		PersonAttribute phoneNumAttr = new PersonAttribute(motherNameType, "775572734");
		
		when(patient.getAttribute(motherNameType.getName())).thenReturn(motherAttr);
		when(patient.getAttribute(phoneNumType.getName())).thenReturn(phoneNumAttr);
	}
}
