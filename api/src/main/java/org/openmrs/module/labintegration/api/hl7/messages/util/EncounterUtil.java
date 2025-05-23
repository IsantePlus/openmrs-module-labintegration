package org.openmrs.module.labintegration.api.hl7.messages.util;

import org.apache.commons.lang3.ObjectUtils;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Encounter;
import org.openmrs.Obs;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public final class EncounterUtil {
	
	private static final Locale[] LOCALE_LIST = { new Locale("ht"), Locale.FRENCH };
	
	public static String getCodedObsValue(Encounter encounter, int conceptId) {
		return getCodedObsValue(encounter, conceptId, null);
	}
	
	public static String getCodedObsValue(Encounter encounter, int conceptId, Integer groupId) {
		Obs obs = findObs(encounter, conceptId, groupId);
		
		if (obs != null) {
			Concept value = obs.getValueCoded();
			if (value != null) {
				ConceptName name = getConceptName(value);
				return name == null ? null : name.getName();
			}
		}
		
		return null;
	}
	
	public static String getObsTextValue(Encounter encounter, int conceptId) {
		return getObsTextValue(encounter, conceptId, null);
	}
	
	public static String getObsTextValue(Encounter encounter, int conceptId, Integer groupId) {
		Obs obs = findObs(encounter, conceptId, groupId);
		return obs == null ? null : obs.getValueText();
	}
	
	public static Obs findObs(Encounter encounter, int conceptId, Integer groupId) {
		Collection<Obs> obsToSearch = Collections.emptyList();
		if (groupId == null) {
			obsToSearch = encounter.getObs();
		} else {
			for (Obs obs : encounter.getObsAtTopLevel(false)) {
				if (ObjectUtils.equals(groupId, getConceptId(obs))) {
					obsToSearch = obs.getGroupMembers();
					break;
				}
			}
		}
		
		for (Obs obs : obsToSearch) {
			if (ObjectUtils.equals(conceptId, getConceptId(obs))) {
				return obs;
			}
		}
		
		return null;
	}
	
	public static Obs findObsByConceptUuid(Encounter encounter, String uuid) {
		for (Obs obs : encounter.getObs()) {
			Concept question = obs.getConcept();
			if (question != null && question.getUuid().equalsIgnoreCase(uuid)) {
				return obs;
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
