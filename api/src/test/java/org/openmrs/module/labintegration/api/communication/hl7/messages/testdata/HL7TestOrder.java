package org.openmrs.module.labintegration.api.communication.hl7.messages.testdata;

import org.openmrs.Concept;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptMapType;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Order;
import org.openmrs.Patient;
import org.openmrs.Provider;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HL7TestOrder {
	
	public static final String ENC_UUID = "549c78dc-31da-11e8-acac-c3add5b19973";
	
	public static final String ENC_TYPE_NAME = "Vitals";
	
	public static final String CONCEPT_CODE = "10081-8";
	
	public static final int SCHEDULED_DATE_MONTH = 2;

	public static final int ACTIVATED_DATE_MONTH = 3;

	public static final int EFFECTIVE_START_DATE_MONTH = 4;

	private final Order order;
	
	public HL7TestOrder(Patient patient, Provider provider) {
		order = mock(Order.class);
		
		mockEncounter();
		mockConcept();
		mockOrder(patient, provider);
	}
	
	private static Date getScheduledDate() {
		return getDefaultDate(SCHEDULED_DATE_MONTH);
	}

	private static Date getActivatedDate() {
		return getDefaultDate(ACTIVATED_DATE_MONTH);
	}

	private static Date getEffectiveStartDate() {
		return getDefaultDate(EFFECTIVE_START_DATE_MONTH);
	}
	
	private static Date getDefaultDate(int month) {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.YEAR, 2018);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 25);
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 25);
		calendar.set(Calendar.SECOND, 33);
		calendar.set(Calendar.MILLISECOND, 400);
		
		return calendar.getTime();
	}
	
	private void mockOrder(Patient patient, Provider provider) {
		when(order.getScheduledDate()).thenReturn(getScheduledDate());
		when(order.getDateActivated()).thenReturn(getActivatedDate());
		when(order.getEffectiveStartDate()).thenReturn(getEffectiveStartDate());
		when(order.getPatient()).thenReturn(patient);
		when(order.getOrderer()).thenReturn(provider);
		when(order.getUrgency()).thenReturn(Order.Urgency.ROUTINE);
	}
	
	private void mockEncounter() {
		Encounter encounter = mock(Encounter.class);
		when(encounter.getUuid()).thenReturn(ENC_UUID);
		
		EncounterType encounterType = new EncounterType(ENC_TYPE_NAME, "description");
		when(encounter.getEncounterType()).thenReturn(encounterType);
		
		when(order.getEncounter()).thenReturn(encounter);
	}
	
	private void mockConcept() {
		ConceptSource loincSource = new ConceptSource();
		loincSource.setName("LOINC");
		
		ConceptSource otherSource = new ConceptSource();
		otherSource.setName("Other");
		
		ConceptReferenceTerm loincTerm = new ConceptReferenceTerm(loincSource, CONCEPT_CODE, "loinc name");
		ConceptMap loincMapping = new ConceptMap(loincTerm, new ConceptMapType());
		
		ConceptReferenceTerm otherTerm = new ConceptReferenceTerm(otherSource, "XXXX", "other name");
		ConceptMap otherMapping = new ConceptMap(otherTerm, new ConceptMapType());
		
		Concept concept = new Concept();
		concept.addConceptMapping(otherMapping);
		concept.addConceptMapping(loincMapping);
		
		when(order.getConcept()).thenReturn(concept);
	}
	
	public Order value() {
		return order;
	}
}
