package org.openmrs.module.labintegration.api.hl7.config.systems.openelis;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
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
	public void updateORC(ORC orc) throws DataTypeException {
		orc.getOrderType().getIdentifier().setValue(conceptCode);
	}
	
	@Override
	public void updateOBR(OBR obr) throws DataTypeException {
		String identifier = encounterType + ';' + encounterUuid;
		obr.getUniversalServiceIdentifier().getIdentifier().setValue(identifier);
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
