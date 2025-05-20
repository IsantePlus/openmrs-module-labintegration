package org.openmrs.module.labintegration.impl;

import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.api.ConceptService;
import org.openmrs.api.ObsService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.labintegration.LabIntegrationReportService;
import org.openmrs.module.labintegration.api.hl7.ObsSelector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.openmrs.module.labintegration.LabIntegrationReportsConstants.FREE_TEXT_RESULT_CONCEPT_ID;
import static org.openmrs.module.labintegration.LabIntegrationReportsConstants.TESTS_ORDERED_CONCEPT_ID;

public class LabIntegrationReportServiceImpl extends BaseOpenmrsService implements LabIntegrationReportService {

    @Override
    public List<Obs> getLabResults(Date startDate, Date endDate) {
        ConceptService conceptService = Context.getConceptService();
        ObsService obsService = Context.getObsService();

        Concept labOrderConcept = conceptService.getConcept(TESTS_ORDERED_CONCEPT_ID);
        if (labOrderConcept == null) {
            return Collections.emptyList();
        }

        List<Obs> orders = obsService.getObservations(
                null,
                null,
                Collections.singletonList(labOrderConcept),
                null,
                null,
                null,
                null,
                null,
                null,
                startDate,
                endDate,
                false,
                null
        );

        Set<Person> persons = new LinkedHashSet<>(orders.size());
        Set<Concept> orderedTests = new LinkedHashSet<>(orders.size());
        Set<Encounter> orderEncounters = new LinkedHashSet<>(orders.size());

        ObsSelector obsSelector = new ObsSelector();

        for (Obs order : orders) {
            if (!obsSelector.isValidTestType(order)) {
                continue;
            }

            persons.add(order.getPerson());
            orderedTests.add(order.getConcept());
            orderEncounters.add(order.getEncounter());
        }

        // freeTextResults are used to capture results with errors or other issues, so may not correspond
        // directly to an ordered test
        Concept freeTextResults = conceptService.getConcept(FREE_TEXT_RESULT_CONCEPT_ID);
        if (freeTextResults != null) {
            orderedTests.add(freeTextResults);
        }

        List<Obs> testResults = obsService.getObservations(
                new ArrayList<>(persons),
                new ArrayList<>(orderEncounters),
                new ArrayList<>(orderedTests),
                null,
                null,
                null,
                Arrays.asList("obsDatetime desc", "obsId asc"),
                null,
                null,
                null,
                null,
                false
        );

        if (testResults != null) {
            return testResults;
        }

        return Collections.emptyList();
    }
}
