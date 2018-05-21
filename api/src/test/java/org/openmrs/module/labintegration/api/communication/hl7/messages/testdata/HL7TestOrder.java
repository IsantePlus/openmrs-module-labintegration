package org.openmrs.module.labintegration.api.communication.hl7.messages.testdata;

import org.openmrs.Concept;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptMapType;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.Encounter;
import org.openmrs.EncounterProvider;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Provider;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HL7TestOrder {
	
	public static final String ENC_UUID = "549c78dc-31da-11e8-acac-c3add5b19973";
	
	public static final String ENC_TYPE_NAME = "Vitals";
	
	public static final String CONCEPT_CODE = "10081-8";
	
	public static final int SCHEDULED_DATE_MONTH = 2;

	public static final int ACTIVATED_DATE_MONTH = 3;

	public static final int EFFECTIVE_START_DATE_MONTH = 4;

	private final Encounter encounter;
	
	public HL7TestOrder(Patient patient, Provider provider) {
		encounter = mockEncounter(patient, provider);
		mockConceptAndObs();
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
	
	private Encounter mockEncounter(Patient patient, Provider provider) {
		Encounter encounter = mock(Encounter.class);
		when(encounter.getUuid()).thenReturn(ENC_UUID);
		
		EncounterType encounterType = new EncounterType(ENC_TYPE_NAME, "description");
		when(encounter.getEncounterType()).thenReturn(encounterType);

		when(encounter.getPatient()).thenReturn(patient);

		EncounterProvider encProvider = mock(EncounterProvider.class);
		when(encProvider.getProvider()).thenReturn(provider);
		when(encounter.getEncounterProviders()).thenReturn(new HashSet<>(singletonList(encProvider)));

		when(encounter.getEncounterDatetime()).thenReturn(getScheduledDate());

		return encounter;
	}
	
	private void mockConceptAndObs() {
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
		concept.setId(657);

		Obs obs = mock(Obs.class);
		when(obs.getEncounter()).thenReturn(encounter);
		when(obs.getConcept()).thenReturn(concept);

		when(encounter.getObs()).thenReturn(new HashSet<>(singletonList(obs)));
	}

	public Encounter value() {
		return encounter;
	}
}
