package org.openmrs.module.labintegration.api.hl7.config.systems.openelis;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.openmrs.module.labintegration.api.hl7.messages.util.ConceptUtil;

public class OpenElisOrderIdentifier implements OrderIdentifier {
	
	private final String encounterType;
	
	private final String encounterUuid;
	
	private final String conceptCode;
	
	OpenElisOrderIdentifier(Order order) {
		this.encounterType = order.getEncounter().getEncounterType().getName();
		this.encounterUuid = order.getEncounter().getUuid();
		this.conceptCode = ConceptUtil.getLoincCode(order.getConcept());
		
		if (StringUtils.isBlank(encounterType) || StringUtils.isBlank(encounterUuid) || StringUtils.isBlank(conceptCode)) {
			throw new IllegalStateException("Encounter type, encounter UUID and concept LOINC code " + "are mandatory");
		}
	}
	
	@Override
	public String value() {
		return StringUtils.join(new String[] { encounterType, encounterUuid, conceptCode }, ID_SEPARATOR);
	}
	
	public String getEncounterType() {
		return encounterType;
	}
	
	public String getEncounterUuid() {
		return encounterUuid;
	}
	
	public String getConceptCode() {
		return conceptCode;
	}
}
