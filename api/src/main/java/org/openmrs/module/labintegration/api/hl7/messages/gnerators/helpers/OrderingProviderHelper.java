package org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.datatype.XCN;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.util.ProviderHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderingProviderHelper {

	@Autowired
	private ProviderInformationHelper providerInformationHelper;

	public void updateOrderingProvider(ORC orc, Obs obs) throws HL7Exception {
		int quantity = orc.getOrderingProviderReps();
		orc.insertOrderingProvider(quantity);

		XCN orderingProvider = orc.getOrderingProvider(quantity);
		providerInformationHelper.updateProviderInformation(orderingProvider, ProviderHelper.getProvider(obs), obs.getEncounter());

		orc.getOrderingProvider()[quantity] = orderingProvider;
	}

	public void updateOrderingProvider(OBR obr, Obs obs) throws HL7Exception {
		int quantity = obr.getOrderingProviderReps();
		obr.insertOrderingProvider(quantity);

		XCN orderingProvider = obr.getOrderingProvider(quantity);
		providerInformationHelper.updateProviderInformation(orderingProvider, ProviderHelper.getProvider(obs), obs.getEncounter());

		obr.getOrderingProvider()[quantity] = orderingProvider;
	}
}
