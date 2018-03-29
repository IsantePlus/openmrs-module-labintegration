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

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HL7TestOrder {
	
	public static final String ENC_UUID = "549c78dc-31da-11e8-acac-c3add5b19973";
	
	public static final String ENC_TYPE_NAME = "Vitals";
	
	public static final String CONCEPT_CDOE = "10081-8";
	
	public static final Date SCHEDULED_DATE = new Date();
	
	public static final String PROVIDER_ID = "PROVIDER_ID";
	
	private final Order order;
	
	public HL7TestOrder(Patient patient) {
		order = mock(Order.class);
		
		mockEncounter();
		mockConcept();
		mockProvider();
		
		when(order.getScheduledDate()).thenReturn(SCHEDULED_DATE);
		
		when(order.getPatient()).thenReturn(patient);
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
		
		ConceptReferenceTerm loincTerm = new ConceptReferenceTerm(loincSource, CONCEPT_CDOE, "loinc name");
		ConceptMap loincMapping = new ConceptMap(loincTerm, new ConceptMapType());
		
		ConceptReferenceTerm otherTerm = new ConceptReferenceTerm(otherSource, "XXXX", "other name");
		ConceptMap otherMapping = new ConceptMap(otherTerm, new ConceptMapType());
		
		Concept concept = new Concept();
		concept.addConceptMapping(otherMapping);
		concept.addConceptMapping(loincMapping);
		
		when(order.getConcept()).thenReturn(concept);
	}
	
	private void mockProvider() {
		Provider provider = mock(Provider.class);
		when(provider.getIdentifier()).thenReturn(PROVIDER_ID);
		when(order.getOrderer()).thenReturn(provider);
	}
	
	public Order value() {
		return order;
	}
}
