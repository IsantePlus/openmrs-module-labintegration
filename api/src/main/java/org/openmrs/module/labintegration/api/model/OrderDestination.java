package org.openmrs.module.labintegration.api.model;

import org.apache.commons.lang.ObjectUtils;
import org.openmrs.Encounter;
import org.openmrs.Obs;

import java.util.ArrayList;
import java.util.List;

public enum OrderDestination {
	
	SCC("SCC"), OPEN_ELIS("OpenELIS");
	
	public static final String ORDER_DESTINATION_CONCEPT_UUID = "160632AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	
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
	
	public static List<OrderDestination> getOrderDestinations(Encounter encounter) {
		List<OrderDestination> destinations = new ArrayList<>();
		
		for (Obs obs : encounter.getAllObs()) {
			if (ObjectUtils.equals(obs.getConcept().getUuid(), ORDER_DESTINATION_CONCEPT_UUID)) {
				OrderDestination destination = OrderDestination.fromString(obs.getValueText());
				if (destination != null) {
					destinations.add(destination);
				}
			}
		}
		
		return destinations;
	}
	
	public static boolean searchForExistence(Encounter encounter, OrderDestination... ods) {
		List<OrderDestination> orderDestinations = OrderDestination.getOrderDestinations(encounter);
		for (OrderDestination od : orderDestinations) {
			for (OrderDestination searchedOd : ods) {
				if (od.equals(searchedOd)) {
					return true;
				}
			}
		}
		return false;
	}
}
