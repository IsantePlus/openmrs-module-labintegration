package org.openmrs.module.labintegration.api.hl7.messages.util;

import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Encounter;
import org.openmrs.Obs;

import java.util.Locale;

public final class EncounterUtil {
	
	private static final Locale[] LOCALE_LIST = { new Locale("ht"), Locale.FRENCH };
	
	public static String getCodedObsValue(Encounter encounter, Integer conceptId) {
		for (Obs obs : encounter.getObs()) {
			if (conceptId.equals(getConceptId(obs))) {
				Concept value = obs.getValueCoded();
				if (value != null) {
					ConceptName name = getConceptName(value);
					return name == null ? null : name.getName();
				}
			}
		}
		return null;
	}
	
	private static Integer getConceptId(Obs obs) {
		Concept concept = obs.getConcept();
		return concept == null ? null : concept.getConceptId();
	}
	
	private static ConceptName getConceptName(Concept concept) {
		ConceptName name = null;
		for (Locale locale : LOCALE_LIST) {
			name = concept.getName(locale);
			if (name != null) {
				break;
			}
		}
		
		if (name == null) {
			name = concept.getName();
		}
		
		return name;
	}
	
	private EncounterUtil() {
	}
}
