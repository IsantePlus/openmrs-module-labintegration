package org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.datatype.XCN;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.ORC;
import org.openmrs.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderingProviderHelper {

	@Autowired
	private ProviderInformationHelper providerInformationHelper;

	public void updateOrderingProvider(ORC orc, Order order) throws HL7Exception {
		int quantity = orc.getOrderingProviderReps();
		orc.insertOrderingProvider(quantity);

		XCN orderingProvider = orc.getOrderingProvider(quantity);
		providerInformationHelper.updateProviderInformation(orderingProvider, order.getOrderer());

		orc.getOrderingProvider()[quantity] = orderingProvider;
	}

	public void updateOrderingProvider(OBR obr, Order order) throws HL7Exception {
		int quantity = obr.getOrderingProviderReps();
		obr.insertOrderingProvider(quantity);

		XCN orderingProvider = obr.getOrderingProvider(quantity);
		providerInformationHelper.updateProviderInformation(orderingProvider, order.getOrderer());

		obr.getOrderingProvider()[quantity] = orderingProvider;
	}
}
