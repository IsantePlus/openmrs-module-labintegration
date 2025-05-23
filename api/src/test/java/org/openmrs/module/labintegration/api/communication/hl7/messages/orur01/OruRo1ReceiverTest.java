package org.openmrs.module.labintegration.api.communication.hl7.messages.orur01;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.ACK;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.parser.PipeParser;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.EncounterService;
import org.openmrs.hl7.HL7Service;
import org.openmrs.module.labintegration.api.communication.hl7.messages.AbstractOrderConverterTest;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.util.EncounterUtil;
import org.openmrs.module.labintegration.api.hl7.oru.OruRo1Receiver;
import org.openmrs.module.labintegration.api.hl7.util.HL7TestMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OruRo1ReceiverTest extends AbstractOrderConverterTest {
	
	private static final String DATASET = "lab-dataset.xml";
	
	private static final String FINAL_RESULT_ORU_RO1 = "ORU_R01_Final_Result.hl7";
	
	private static final String FINAL_RESULT_ERROR_ORU_RO1 = "ORU_R01_Final_Result_Error.hl7";
	
	private static final String ENCOUNTER_UUID = "549c78dc-31da-11e8-acac-c3add5b19973";
	
	private static final int CONCEPT_ID = 678;
	
	private PipeParser pipeParser = new PipeParser();
	
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
	public void shouldParseOruRo1FinalMessage() throws IOException, MessageCreationException, HL7Exception {
		String hl7Msg = HL7TestMsgUtil.readMsg(FINAL_RESULT_ORU_RO1);
		oruRo1Receiver.receiveMsg(hl7Msg);
		
		Encounter encounter = encounterService.getEncounterByUuid(ENCOUNTER_UUID);
		Obs obs = EncounterUtil.findObs(encounter, CONCEPT_ID, null);
		
		assertNotNull(obs);
		assertEquals("This is a note about the result", obs.getComment());
		assertEquals(12.0, obs.getValueNumeric(), 0.0);
	}
	
	@Test
	public void shouldParseOruRo1FinalMessageAndReturnACK() throws IOException, HL7Exception, MessageCreationException {
		String hl7Msg = HL7TestMsgUtil.readMsg(FINAL_RESULT_ORU_RO1);
		
		ACK actual = oruRo1Receiver.receiveMsg(hl7Msg);
		
		ORU_R01 oruR01 = new ORU_R01();
		pipeParser.parse(oruR01, hl7Msg);
		
		assertEquals("^~\\&", actual.getMSH().getMsh2_EncodingCharacters().getValue());
		assertEquals("ACK", actual.getMSH().getMsh9_MessageType().getMsg1_MessageCode().getValue());
		assertEquals(oruR01.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().getValue(),
		    actual.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().getValue());
		assertEquals("ACK", actual.getMSH().getMsh9_MessageType().getMsg3_MessageStructure().getValue());
		assertNotNull(actual.getMSH().getMsh7_DateTimeOfMessage().getTime().getValue());
		assertNotNull(actual.getMSH().getMsh10_MessageControlID().getValue());
		// Result Status = Preliminary Result
		assertEquals("P", actual.getMSH().getMsh11_ProcessingID().getProcessingID().getValue());
		assertEquals("2.5.1", actual.getMSH().getMsh12_VersionID().getVersionID().getValue());
		
		// AA = Application Accept
		assertEquals("AA", actual.getMSA().getMsa1_AcknowledgmentCode().getValue());
		assertEquals(oruR01.getMSH().getMsh10_MessageControlID().getValue(),
		    actual.getMSA().getMsa2_MessageControlID().getValue());
	}
	
	@Test
	public void shouldParseOruRo1ErrorMessageAndReturnACK() throws IOException, HL7Exception, MessageCreationException {
		String hl7Msg = HL7TestMsgUtil.readMsg(FINAL_RESULT_ERROR_ORU_RO1);
		
		ACK actual = oruRo1Receiver.receiveMsg(hl7Msg);
		
		ORU_R01 oruR01 = new ORU_R01();
		pipeParser.parse(oruR01, hl7Msg);
		
		assertEquals("^~\\&", actual.getMSH().getMsh2_EncodingCharacters().getValue());
		assertEquals("ACK", actual.getMSH().getMsh9_MessageType().getMsg1_MessageCode().getValue());
		assertEquals(oruR01.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().getValue(),
		    actual.getMSH().getMsh9_MessageType().getMsg2_TriggerEvent().getValue());
		assertEquals("ACK", actual.getMSH().getMsh9_MessageType().getMsg3_MessageStructure().getValue());
		assertNotNull(actual.getMSH().getMsh7_DateTimeOfMessage().getTime().getValue());
		assertNotNull(actual.getMSH().getMsh10_MessageControlID().getValue());
		// Result Status = Preliminary Result
		assertEquals("P", actual.getMSH().getMsh11_ProcessingID().getProcessingID().getValue());
		assertEquals("2.5.1", actual.getMSH().getMsh12_VersionID().getVersionID().getValue());
		
		assertEquals("AE", actual.getMSA().getMsa1_AcknowledgmentCode().getValue());
		assertEquals(oruR01.getMSH().getMsh10_MessageControlID().getValue(),
		    actual.getMSA().getMsa2_MessageControlID().getValue());
		
		assertEquals("ORUR01.error.InvalidEncounter",
		    actual.getERR().getErrorCodeAndLocation(0).getCodeIdentifyingError().getAlternateText().getValue());
	}
}
