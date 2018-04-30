package org.openmrs.module.labintegration.api.communication.hl7.messages.orur01;

import ca.uhn.hl7v2.HL7Exception;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.EncounterService;
import org.openmrs.hl7.HL7Service;
import org.openmrs.module.labintegration.api.hl7.messages.util.EncounterUtil;
import org.openmrs.module.labintegration.api.hl7.oru.OruRo1Receiver;
import org.openmrs.module.labintegration.api.hl7.util.HL7TestMsgUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OruRo1ReceiverTest extends BaseModuleContextSensitiveTest {

	private static final String DATASET = "lab-dataset.xml";

	private static final String ORDER_RECEIVED_NO_SPEC_FILE = "ORU_R01.hl7";
	private static final String FINAL_RESULT_ORU_RO1 = "ORU_R01_Final_Result.hl7";

	private static final String ENCOUNTER_UUID = "549c78dc-31da-11e8-acac-c3add5b19973";
	private static final int CONCEPT_ID = 678;

	@Autowired
	@Qualifier("hL7ServiceLabIntegration")
	private HL7Service hl7Service;

	@Autowired
	private OruRo1Receiver oruRo1Receiver;

	@Autowired
	private EncounterService encounterService;

	@Before
	public void setUp() throws Exception {
		executeDataSet(DATASET);
	}

	@Test
	public void shouldParseOruRo1FinalMessage() throws IOException, HL7Exception {
		String hl7Msg = HL7TestMsgUtil.readMsg(FINAL_RESULT_ORU_RO1);
		oruRo1Receiver.receiveMsg(hl7Msg);

		Encounter encounter = encounterService.getEncounterByUuid(ENCOUNTER_UUID);
		Obs obs = EncounterUtil.findObs(encounter, CONCEPT_ID, null);

		assertNotNull(obs);
		assertEquals("This is a note about the result", obs.getComment());
		assertEquals(12.0, obs.getValueNumeric(), 0.0);
	}
}
