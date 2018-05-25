package org.openmrs.module.labintegration.api.hl7.config;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;

public interface HL7Config {
	
	String getReceivingApplication();
	
	String getSendingApplication();
	
	String getPatientIdentifierTypeUuid();
	
	String getMotherNameAttrTypeName();
	
	String getPhoneNumberAttrTypeName();
	
	OrderIdentifier buildOrderIdentifier(Encounter encounter);
	
	EncounterType getRegistrationFormEncounterType();
	
	Integer getReligionConceptId();
	
	Integer getCivilStatusConceptId();
	
	Integer getBirthPlaceGroupConceptId();
	
	Integer getBirthPlaceCityConceptId();

    String getSendingFacilityNamespaceID();

    boolean isBillingNumberNeeded();

	String getPatientDateOfBirthFormat();

	String getAdmitDateFormat();
}
