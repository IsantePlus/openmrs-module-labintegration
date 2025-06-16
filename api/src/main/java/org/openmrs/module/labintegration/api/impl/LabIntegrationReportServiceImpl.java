package org.openmrs.module.labintegration.api.impl;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labintegration.api.LabIntegrationReportService;
import org.openmrs.module.labintegration.api.hl7.ObsSelector;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.openmrs.module.labintegration.api.LabIntegrationReportsConstants.FREE_TEXT_RESULT_CONCEPT_ID;
import static org.openmrs.module.labintegration.api.LabIntegrationReportsConstants.TESTS_ORDERED_CONCEPT_ID;

@Component("labintegration.LabIntegrationReportServiceImpl")
public class LabIntegrationReportServiceImpl extends BaseOpenmrsService implements LabIntegrationReportService {
	
	@Override
	public List<Obs> getLabResults(Date startDate, Date endDate) {
		ConceptService conceptService = Context.getConceptService();
		ObsService obsService = Context.getObsService();
		ObsSelector obsSelector = new ObsSelector();
		
		Concept labOrderConcept = conceptService.getConcept(TESTS_ORDERED_CONCEPT_ID);
		if (labOrderConcept == null) {
			return Collections.emptyList();
		}
		List<Concept> orderConcepts = new ArrayList<>();
		if (obsSelector.getViralLoadConceptId() != -1) {
			Concept viralLoad = conceptService.getConcept(obsSelector.getViralLoadConceptId());
			orderConcepts.add(viralLoad);
		}
		if (obsSelector.getEarlyInfantDiagnosisConceptId() != -1) {
			Concept earlyChildDiagnosis = conceptService.getConcept(obsSelector.getEarlyInfantDiagnosisConceptId());
			orderConcepts.add(earlyChildDiagnosis);
		}
		if (orderConcepts.isEmpty()) {
			return Collections.emptyList();
		}
		
		List<Obs> orders = obsService.getObservations(null, null, orderConcepts, null, null, null, null, null, null,
		    startDate, endDate, false, null);
		
		Set<Person> persons = new LinkedHashSet<>(orders.size());
		Set<Concept> resultTests = new LinkedHashSet<>(orders.size());
		Set<Encounter> resultEncounters = new LinkedHashSet<>(orders.size());
		
		for (Obs order : orders) {
			persons.add(order.getPerson());
			resultTests.add(order.getConcept());
			resultEncounters.add(order.getEncounter());
		}
		
		// freeTextResults are used to capture results with errors or other issues, so may not correspond
		// directly to an ordered test
		Concept freeTextResults = conceptService.getConcept(FREE_TEXT_RESULT_CONCEPT_ID);
		if (freeTextResults != null) {
			resultTests.add(freeTextResults);
		}
		List<Obs> testResults = obsService.getObservations(new ArrayList<>(persons), new ArrayList<>(resultEncounters),
		    new ArrayList<>(resultTests), null, null, null, Arrays.asList("obsDatetime desc", "obsId asc"), null, null, null,
		    null, false);
		
		Set<Integer> resultEncounterIds = resultEncounters.stream().map(Encounter::getId).collect(Collectors.toSet());
		
		// Unresulted Orders	
		List<Obs> unresultedOrders = obsService.getObservations(null, null, Collections.singletonList(labOrderConcept), null,
		    null, null, null, null, null, startDate, endDate, false, null);
		Set<Encounter> orderEncounters = new LinkedHashSet<>(unresultedOrders.size());
		Set<Person> orderPersons = new LinkedHashSet<>(unresultedOrders.size());
		for (Obs order : unresultedOrders) {
			if (!obsSelector.isValidTestType(order) || resultEncounterIds.contains(order.getEncounter().getId())) {
				continue;
			}
			
			orderPersons.add(order.getPerson());
			orderEncounters.add(order.getEncounter());
		}
		
		List<Obs> orderResults = obsService.getObservations(new ArrayList<>(orderPersons), new ArrayList<>(orderEncounters),
		    Collections.singletonList(labOrderConcept), null, null, null, Arrays.asList("obsDatetime desc", "obsId asc"),
		    null, null, null, null, false);
		
		if (testResults != null) {
			if (orderResults != null) {
				List<Obs> displayOrders = orderResults.stream().filter(o -> obsSelector.isValidTestType(o))
				        .map(o -> translateToDisplayResultTest(o)).collect(Collectors.toList());
				testResults.addAll(displayOrders);
			}
			return testResults;
		}
		
		return Collections.emptyList();
	}
	
	private Obs translateToDisplayResultTest(Obs obs) {
		ConceptService conceptService = Context.getConceptService();
		Obs displayResult = new Obs();
		displayResult.setId(obs.getId());
		displayResult.setPerson(obs.getPerson());
		displayResult.setEncounter(obs.getEncounter());
		displayResult.setConcept(conceptService.getConcept(obs.getValueCoded().getId()));
		displayResult.setObsDatetime(obs.getObsDatetime());
		displayResult.setValueText("");
		return displayResult;
	}
}
