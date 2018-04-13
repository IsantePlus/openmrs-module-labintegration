package org.openmrs.module.labintegration.api.hl7.scc;

import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.config.AbstractHL7Config;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.springframework.stereotype.Component;

@Component
public class SCCHL7Config extends AbstractHL7Config {
	
	private static final String SENDING_FACILITY_NAMESPACE_ID = "LNSP";
	
	@Override
	public String getReceivingApplication() {
		return null;
	}
	
	@Override
	public String getSendingApplication() {
		return null;
	}
	
	@Override
	public String getPatientIdentifierTypeUuid() {
		return null;
	}
	
	@Override
	public OrderIdentifier buildOrderIdentifier(Order order) {
		return null;
	}
	
	@Override
	public String getSendingFacilityNamespaceID() {
		return SENDING_FACILITY_NAMESPACE_ID;
	}
}
