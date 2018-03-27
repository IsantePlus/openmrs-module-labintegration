package org.openmrs.module.labintegration.api.hl7.config.systems.openelis;

import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.openmrs.module.labintegration.api.hl7.config.systems.AbstractHL7Config;
import org.springframework.stereotype.Component;

@Component
public class OpenElisHL7Config extends AbstractHL7Config {
	
	private static final String RECEIVING_APP = "labintegration.openElis.receivingApplication";
	
	private static final String SENDING_APP = "labintegration.openElis.sendingApplication";
	
	private static final String PID_TYPE_UUID = "labintegration.openElis.pidTypeUuid";
	
	private static final String DEFAULT_RECEIVING_APP = "OpenELIS";
	
	private static final String DEFAULT_SENDING_APP = "iSantePlus";
	
	private static final String DEFAULT_PID_TYPE_UUID = "05a29f94-c0ed-11e2-94be-8c13b969e334";
	
	@Override
	public String getReceivingApplication() {
		return getPropertySource().getProperty(RECEIVING_APP, DEFAULT_RECEIVING_APP);
	}
	
	@Override
	public String getSendingApplication() {
		return getPropertySource().getProperty(SENDING_APP, DEFAULT_SENDING_APP);
	}
	
	@Override
	public String getPatientIdentifierTypeUuid() {
		return getPropertySource().getProperty(PID_TYPE_UUID, DEFAULT_PID_TYPE_UUID);
	}
	
	@Override
	public OrderIdentifier buildOrderIdentifier(Order order) {
		return new OpenElisOrderIdentifier(order);
	}
}
