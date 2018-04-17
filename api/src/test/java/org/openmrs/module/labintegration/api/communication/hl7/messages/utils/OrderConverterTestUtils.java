package org.openmrs.module.labintegration.api.communication.hl7.messages.utils;

import org.openmrs.module.labintegration.api.hl7.messages.gnerators.msh.MessageControlIdSource;

import static org.mockito.Mockito.when;

public final class OrderConverterTestUtils {
	
	private static final String ROLLING_NUMBER = "00000";
	
	public static void mockRollingNumber(MessageControlIdSource controlIdSource) {
		when(controlIdSource.rollingNumber()).thenReturn(ROLLING_NUMBER);
	}
}
