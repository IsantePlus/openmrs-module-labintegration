package org.openmrs.module.labintegration.api.hl7.messages.gnerators.msh;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MessageControlIdSource {
	
	private static final int RESET_BOUNDARY = 99999;
	
	private static final String DATE_FORMAT = "yyyyMMddHHmmss";
	
	private int rollingNumber;
	
	private final Object counterLock = new Object();
	
	public String generateId(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return dateFormat.format(date) + rollingNumber();
	}
	
	private String rollingNumber() {
		synchronized (counterLock) {
			int value = rollingNumber;
			
			rollingNumber++;
			if (rollingNumber > RESET_BOUNDARY) {
				rollingNumber = 0;
			}
			
			return String.format("%05d", value);
		}
	}
}
