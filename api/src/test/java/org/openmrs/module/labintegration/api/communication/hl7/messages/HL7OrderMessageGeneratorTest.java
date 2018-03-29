package org.openmrs.module.labintegration.api.communication.hl7.messages;

import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.module.labintegration.api.communication.hl7.messages.testdata.HL7TestOrder;
import org.openmrs.module.labintegration.api.hl7.messages.HL7OrderMessageGenerator;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration(locations = { "classpath*:moduleApplicationContext.xml", "classpath*:applicationContext-service.xml" }, inheritLocations = false)
public class HL7OrderMessageGeneratorTest extends BaseModuleContextSensitiveTest {
	
	private static final String DATASET = "lab-dataset.xml";
	
	private static final int PATIENT_ID = 10;
	
	@Autowired
	private HL7OrderMessageGenerator hl7OrderMessageGenerator;
	
	@Autowired
	private PatientService patientService;
	
	@Test
	public void shouldGenerateMessage() throws Exception {
		executeDataSet(DATASET);
		Patient patient = patientService.getPatient(PATIENT_ID);
		HL7TestOrder order = new HL7TestOrder(patient);
		
		String msg = hl7OrderMessageGenerator.createMessage(order.value(), "NW");
		
		assertNotNull(msg);
	}
}
