package org.openmrs.module.labintegration.api.hl7.messages.util;

import org.openmrs.Concept;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;

public final class ConceptUtil {
	
	private static final String LOINC = "LOINC";
	
	public static String getLoincCode(Concept concept) {
		for (ConceptMap mapping : concept.getConceptMappings()) {
			ConceptReferenceTerm referenceTerm = mapping.getConceptReferenceTerm();
			ConceptSource conceptSource = referenceTerm.getConceptSource();
			
			if (LOINC.equalsIgnoreCase(conceptSource.getName())) {
				return referenceTerm.getCode();
			}
		}
		
		return null;
	}
	
	private ConceptUtil() {
	}
}
