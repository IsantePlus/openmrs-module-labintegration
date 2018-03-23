package org.openmrs.module.labintegration.api;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DateSource {
	
	public Date now() {
		return new Date();
	}
}
