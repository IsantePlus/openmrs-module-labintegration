package org.openmrs.module.labintegration.api.hl7.scc;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers.OrderingProviderHelper;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers.QuantityTimingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SCCOrderIdentifier extends OrderIdentifier {

	private static final String DEFAULT_ACTION_CODE = "O";

	@Autowired
	private QuantityTimingHelper quantityTimingHelper;

	@Autowired
	private OrderingProviderHelper orderingProviderHelper;

	@Override
	public void updateORC(ORC orc, Order order) throws HL7Exception {
		updateOrderTypeID(orc, order);

		orderingProviderHelper.updateOrderingProvider(orc, order);
		quantityTimingHelper.updateQuantityTiming(orc, order);

		orc.getDateTimeOfTransaction().getTime().setValue(order.getDateActivated());
	}

	@Override
	public void updateOBR(OBR obr, Order order) throws HL7Exception {
		updateUniversalServiceID(obr, order);

		orderingProviderHelper.updateOrderingProvider(obr, order);
		quantityTimingHelper.updateQuantityTiming(obr, order);

		obr.getSpecimenActionCode().setValue(DEFAULT_ACTION_CODE);
	}

	protected void updateUniversalServiceID(OBR obr, Order order) throws DataTypeException {
		String encounterType = order.getEncounter().getEncounterType().getName();
		String encounterUuid = order.getEncounter().getUuid();
		String encounterLocationUuid = order.getEncounter().getLocation().getUuid();

		if (StringUtils.isBlank(encounterType) || StringUtils.isBlank(encounterUuid)) {
			throw new IllegalStateException("Encounter type and encounter UUID are mandatory");
		}

		String identifier = encounterLocationUuid + ID_SEPARATOR + encounterType + ID_SEPARATOR + encounterUuid;

		obr.getUniversalServiceIdentifier().getIdentifier().setValue(identifier);
	}
}
