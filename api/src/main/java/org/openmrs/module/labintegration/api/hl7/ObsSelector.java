package org.openmrs.module.labintegration.api.hl7;

import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class ObsSelector {

    private static final int TESTS_ORDERED_CONCEPT_ID = 1271;
    private static volatile int VIRAL_LOAD_CONCEPT_ID = -1;
    private static volatile int EARLY_INFANT_DIAGNOSIS_CONCEPT_ID = -1;

    private Set<Integer> conceptIds = new HashSet<>();

    public ObsSelector() {

    }

    public boolean isValidTestType(Obs obs) {
        return TESTS_ORDERED_CONCEPT_ID == obs.getConcept().getConceptId() && obs.getValueCoded() != null  && (
            (getViralLoadConceptId() != -1 && obs.getValueCoded().getConceptId() == getViralLoadConceptId()) ||
                    (getEarlyInfantDiagnosisConceptId() != -1 && obs.getValueCoded().getConceptId() == getEarlyInfantDiagnosisConceptId())
        );
    }

    private int getViralLoadConceptId() {
        if (VIRAL_LOAD_CONCEPT_ID == -1) {
            synchronized (ObsSelector.class) {
                if (VIRAL_LOAD_CONCEPT_ID == -1) {
                    ConceptService conceptService = Context.getService(ConceptService.class);
                    Concept concept = conceptService.getConceptByMapping("CIEL", "856");
                    if (concept == null) {
                        concept = conceptService.getConceptByMapping("LOINC", "25836-8");
                    }

                    if (concept != null) {
                        VIRAL_LOAD_CONCEPT_ID = concept.getConceptId();
                    }
                }
            }
        }

        return VIRAL_LOAD_CONCEPT_ID;
    }

    private int getEarlyInfantDiagnosisConceptId() {
        if (EARLY_INFANT_DIAGNOSIS_CONCEPT_ID == -1) {
            synchronized (ObsSelector.class) {
                if (EARLY_INFANT_DIAGNOSIS_CONCEPT_ID == -1) {
                    ConceptService conceptService = Context.getService(ConceptService.class);
                    Concept concept = conceptService.getConceptByMapping("CIEL", "844");
                    if (concept == null) {
                        concept = conceptService.getConceptByMapping("LOINC", "44871-2");
                    }

                    if (concept != null) {
                        EARLY_INFANT_DIAGNOSIS_CONCEPT_ID = concept.getConceptId();
                    }
                }
            }
        }

        return EARLY_INFANT_DIAGNOSIS_CONCEPT_ID;
    }
}
