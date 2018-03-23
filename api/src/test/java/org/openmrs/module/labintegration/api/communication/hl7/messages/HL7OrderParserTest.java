package org.openmrs.module.labintegration.api.communication.hl7.messages;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.module.labintegration.api.hl7.messages.HL7OrderParser;

import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class HL7OrderParserTest {
	
	@InjectMocks
	private HL7OrderParser hl7OrderParser;
	
	@Test
	public void shouldGenerateMessage() {
		String msg = hl7OrderParser.createMessage(null);
		assertNotNull(msg);
	}
}
