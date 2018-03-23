package org.openmrs.module.labintegration.api;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.AdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LabModuleProperties {
	
	@Autowired
	private AdministrationService adminService;
	
	public String getRequiredProperty(String propertyName) {
		String value = adminService.getGlobalProperty(propertyName);
		if (StringUtils.isBlank(value)) {
			throw new IllegalStateException("Required property " + propertyName + " is not set");
		}
		return value;
	}
	
	public String getProperty(String propertyName, String defaultValue) {
		return adminService.getGlobalProperty(propertyName, defaultValue);
	}
}
