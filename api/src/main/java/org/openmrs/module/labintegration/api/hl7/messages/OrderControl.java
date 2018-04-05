package org.openmrs.module.labintegration.api.hl7.messages;

public enum OrderControl {
	
	NEW_ORDER("NW"), CANCEL_ORDER("CA");
	
	private final String code;
	
	OrderControl(String code) {
		this.code = code;
	}
	
	public String code() {
		return code;
	}
}
