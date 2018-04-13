package org.openmrs.module.labintegration.api.model;

public enum OrderDestination {
	SCC("SCC"),
	OPEN_ELIS("OpenELIS");
	
	private String text;
	
	OrderDestination(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	public static OrderDestination fromString(String text) {
		for (OrderDestination b : OrderDestination.values()) {
			if (b.text.equalsIgnoreCase(text)) {
				return b;
			}
		}
		return null;
	}
}
