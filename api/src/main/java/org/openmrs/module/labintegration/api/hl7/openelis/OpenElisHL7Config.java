package org.openmrs.module.labintegration.api.hl7.openelis;

import org.openmrs.Encounter;
import org.openmrs.module.labintegration.PropertiesUtil;
import org.openmrs.module.labintegration.api.hl7.config.AbstractHL7Config;
import org.openmrs.module.labintegration.api.hl7.config.LabHL7ConfigurationException;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class OpenElisHL7Config extends AbstractHL7Config {
	
	private static final String RECEIVING_APP = "labintegration.openElis.receivingApplication";
	
	private static final String SENDING_APP = "labintegration.openElis.sendingApplication";
	
	private static final String PID_TYPE_UUID = "labintegration.openElis.pidTypeUuid";
	
	private static final String OPENELIS_URL = "labintegration.openElis.url";
	
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
	public OrderIdentifier buildOrderIdentifier(Encounter encounter) {
		return new OpenElisOrderIdentifier();
	}
	
	@Override
	public String getSendingFacilityNamespaceID() {
		return null;
	}
	
	@Override
	public String getBillingNumberTypeUuid() {
		return null;
	}

	@Override
	public String getPatientDateOfBirthFormat() {
		return null;
	}

	@Override
	public String getAdmitDateFormat() {
		return null;
	}

	public URI getOpenElisUrl() {
		String url = PropertiesUtil.getGlobalProperty(OPENELIS_URL);
		try {
			return new URI(url);
		}
		catch (URISyntaxException e) {
			throw new LabHL7ConfigurationException("Incorrect OpenELIS url: " + url, e);
		}
	}
	
	public boolean isOpenElisConfigured() {
		return PropertiesUtil.isGlobalPropertySet(OPENELIS_URL);
	}
}
