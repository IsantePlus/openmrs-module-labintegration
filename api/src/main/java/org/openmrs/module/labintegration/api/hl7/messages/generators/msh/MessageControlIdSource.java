package org.openmrs.module.labintegration.api.hl7.messages.generators.msh;

import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.scc.SCCHL7Config;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MessageControlIdSource {
	
	private static final int RESET_BOUNDARY = 99999;
	
	private int rollingNumber;
	
	private HL7Config hl7Config = new SCCHL7Config();
	
	public String generateId(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(hl7Config.getDefaultDateFormat());
		return dateFormat.format(date) + rollingNumber();
	}
	
	public synchronized String rollingNumber() {
		int value = rollingNumber;
		
		rollingNumber++;
		if (rollingNumber > RESET_BOUNDARY) {
			rollingNumber = 0;
		}
		
		return String.format("%05d", value);
	}
}
