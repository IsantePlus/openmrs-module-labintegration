package org.openmrs.module.labintegration.api.hl7.scc;

import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.config.AbstractHL7Config;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SCCHL7Config extends AbstractHL7Config {
	
	private static final String SENDING_FACILITY_NAMESPACE_ID = "LNSP";
	
	private static final String PID_TYPE_UUID = "labintegration.scc.pidTypeUuid";
	
	private static final String DEFAULT_PID_TYPE_UUID = "05a29f94-c0ed-11e2-94be-8c13b969e334";

	private static final String BILLING_NUMBER_TYPE_UUID = "labintegration.scc.billingNumberTypeUuid";

	private static final String DEFAULT_BILLING_NUMBER_TYPE_UUID = "a41eb133-8c9c-4359-889f-50046cc13b0d";

	private static final String PATIENT_DATE_OF_BIRTH_FORMAT = "yyyyMMdd[HHmm]";

	private static final String ADMIT_DATE_FORMAT = "yyyyMMdd";

	@Autowired
	private SCCOrderIdentifier orderIdentifier;

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
		return getPropertySource().getProperty(PID_TYPE_UUID, DEFAULT_PID_TYPE_UUID);
	}
	
	@Override
	public OrderIdentifier buildOrderIdentifier(Order order) {
		return orderIdentifier;
	}
	
	@Override
	public String getSendingFacilityNamespaceID() {
		return SENDING_FACILITY_NAMESPACE_ID;
	}
	
	@Override
	public String getBillingNumberTypeUuid() {
		return getPropertySource().getProperty(BILLING_NUMBER_TYPE_UUID, DEFAULT_BILLING_NUMBER_TYPE_UUID);
	}

	@Override
	public String getPatientDateOfBirthFormat() {
		return PATIENT_DATE_OF_BIRTH_FORMAT;
	}

	@Override
	public String getAdmitDateFormat() {
		return ADMIT_DATE_FORMAT;
	}
}
