package org.openmrs.module.labintegration.api.date;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component("lab-datesource")
public class DateBasedDateSource implements DateSource {
	
	public Date now() {
		return new Date();
	}
}
