package org.openmrs.module.labintegration.api.model;

public enum OrderDestination {
	SCC("SCC"),
	OPEN_ELIS("OpenELIS");
	
	private String name;
	
	OrderDestination(String text) {
		this.name = text;
	}
	
	public String getName() {
		return this.name;
	}
	
	public static OrderDestination fromString(String text) {
		for (OrderDestination b : OrderDestination.values()) {
			if (b.name.equalsIgnoreCase(text)) {
				return b;
			}
		}
		return null;
	}
}
