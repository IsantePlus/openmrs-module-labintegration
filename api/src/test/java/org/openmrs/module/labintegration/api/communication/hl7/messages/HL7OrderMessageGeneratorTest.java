package org.openmrs.module.labintegration.api.communication.hl7.messages;

import org.junit.Test;
import org.openmrs.module.labintegration.api.hl7.messages.HL7OrderMessageGenerator;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = { "classpath*:moduleApplicationContext.xml", "classpath*:applicationContext-service.xml" }, inheritLocations = false)
public class HL7OrderMessageGeneratorTest extends BaseModuleContextSensitiveTest {
	
	@Autowired
	private HL7OrderMessageGenerator hl7OrderMessageGenerator;
	
	private HL7TestPatient patient = new HL7TestPatient();
	
	private HL7TestOrder order = new HL7TestOrder(patient);
	
	@Test
	public void shouldGenerateMessage() {
		String msg = hl7OrderMessageGenerator.createMessage(order.value(), "NO");
		assertNotNull(msg);
	}
}
