package org.openmrs.module.labintegration.api.hl7.messages.util;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptMap;
import org.openmrs.ConceptReferenceTerm;
import org.openmrs.ConceptSource;
import org.openmrs.Obs;

public final class ConceptUtil {
	
	public static final String LOINC = "LOINC";
	public static final String LNSP_SOURCE_UUID = "f124da75-9f50-4b25-be97-13c029e3a65b";

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

	public static  String getLoincCode(Obs obs) {
		Concept testConcept = obs.getValueCoded();
		if (testConcept == null) {
			throw new IllegalStateException("Wrong concept used for ordered test: " + obs.getConcept().getId());
		}

		String conceptCode = ConceptUtil.getLoincCode(testConcept);
		if (StringUtils.isBlank(conceptCode)) {
			throw new IllegalStateException("LOINC code is mandatory");
		}
		return conceptCode;
	}
	
	public static String getLnspCode(Concept concept) {
		for (ConceptMap mapping : concept.getConceptMappings()) {
			ConceptReferenceTerm referenceTerm = mapping.getConceptReferenceTerm();
			ConceptSource conceptSource = referenceTerm.getConceptSource();

			if (LNSP_SOURCE_UUID.equalsIgnoreCase(conceptSource.getUuid())) {
				return referenceTerm.getCode();
			}
		}

		return null;
	}

	private ConceptUtil() {
	}
}
