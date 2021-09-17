package org.openmrs.module.labintegration.api.hl7.scc;

import org.openmrs.Encounter;
import org.openmrs.module.labintegration.api.hl7.config.AbstractHL7Config;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Primary
public class SCCHL7Config extends AbstractHL7Config {
	
	private static final String SENDING_FACILITY_NAMESPACE_ID = "LNSP";
	
	private static final String PID_TYPE_UUID = "labintegration.scc.pidTypeUuid";
	
	private static final String DEFAULT_PID_TYPE_UUID = "05a29f94-c0ed-11e2-94be-8c13b969e334";

	private static final String PATIENT_DATE_OF_BIRTH_FORMAT = "yyyyMMdd";

	private static final String ADMIT_DATE_FORMAT = "yyyyMMdd";

	private Map<String, String> lnspCodeMapping;

	public SCCHL7Config() {
		lnspCodeMapping = new HashMap<>();
		lnspCodeMapping.put("25836-8", "63cbd0ac-7b4d-477a-910d-8e75168275bf");
		lnspCodeMapping.put("9837-6", "4f318d58-7647-47e2-92dd-7b568aa26360");
		lnspCodeMapping.put("44871-2", "63cbd0ac-7b4d-477a-910d-8e75168275bf");
	}

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
	public OrderIdentifier buildOrderIdentifier(Encounter encounter) {
		return orderIdentifier;
	}

	@Override
	public boolean isBillingNumberNeeded() {
		return true;
	}

	@Override
	public String getSendingFacilityNamespaceID() {
		return SENDING_FACILITY_NAMESPACE_ID;
	}

	@Override
	public String getPatientDateOfBirthFormat() {
		return PATIENT_DATE_OF_BIRTH_FORMAT;
	}

	@Override
	public String getAdmitDateFormat() {
		return ADMIT_DATE_FORMAT;
	}

	public String mapConceptToLnspTest(String code) {
		String result = "";
		if (this.lnspCodeMapping.containsKey(code)) {
			result = this.lnspCodeMapping.get(code);
		}
		return result;
	}


}
