package org.openmrs.module.labintegration.api.communication.hl7.messages;

import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.module.labintegration.api.communication.hl7.messages.testdata.HL7TestMsgUtil;
import org.openmrs.module.labintegration.api.communication.hl7.messages.testdata.HL7TestOrder;
import org.openmrs.module.labintegration.api.hl7.messages.HL7OrderMessageGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.OrderControl;
import org.openmrs.module.labintegration.api.hl7.openelis.OpenElisHL7Config;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = { "classpath*:moduleApplicationContext.xml", "classpath*:applicationContext-service.xml",
        "classpath*:test-labContext.xml" }, inheritLocations = false)
public class HL7OrderMessageGeneratorTest extends BaseModuleContextSensitiveTest {
	
	private static final String DATASET = "lab-dataset.xml";
	
	private static final String EXPECTED_FILE = "OML_O21.hl7";
	
	private static final int PATIENT_ID = 10;
	
	@Autowired
	private HL7OrderMessageGenerator hl7OrderMessageGenerator;
	
	@Autowired
	private PatientService patientService;

	@Autowired
	private OpenElisHL7Config openElisHL7Config;
	
	@Test
	public void shouldGenerateMessage() throws Exception {
		executeDataSet(DATASET);
		Patient patient = patientService.getPatient(PATIENT_ID);
		HL7TestOrder order = new HL7TestOrder(patient);
		
		String msg = hl7OrderMessageGenerator.createMessage(order.value(),
				OrderControl.NW, openElisHL7Config);
		
		String expected = HL7TestMsgUtil.readMsg(EXPECTED_FILE);
		assertEquals(expected, msg);
	}
}
