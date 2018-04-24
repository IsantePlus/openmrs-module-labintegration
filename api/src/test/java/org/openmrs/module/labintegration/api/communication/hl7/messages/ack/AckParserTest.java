package org.openmrs.module.labintegration.api.communication.hl7.messages.ack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.module.labintegration.api.hl7.util.HL7TestMsgUtil;
import org.openmrs.module.labintegration.api.hl7.messages.ack.AckParser;
import org.openmrs.module.labintegration.api.hl7.messages.ack.Acknowledgement;
import org.openmrs.module.labintegration.api.hl7.messages.ack.InvalidAckException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class AckParserTest {
	
	private static final String ACK_COMMIT_ACCEPT_FILE = "ACK_Commit_Accept.hl7";
	
	private static final String ACK_COMMIT_ERROR_FILE = "ACK_Commit_Error.hl7";
	
	private static final String MSG_ID = "20070701151010000018";
	
	private AckParser ackParser = new AckParser();
	
	@Test
	public void shouldParseAck() throws IOException, InvalidAckException {
		String msg = HL7TestMsgUtil.readMsg(ACK_COMMIT_ACCEPT_FILE);
		
		Acknowledgement ack = ackParser.parse(msg);
		
		assertNotNull(ack);
		assertTrue(ack.isSuccess());
		assertNull(ack.getErrorDiagnosticsInformation());
		assertEquals(MSG_ID, ack.getMsgId());
		assertNull(ack.getErrorCode());
	}
	
	@Test
	public void shouldParseAckError() throws IOException, InvalidAckException {
		String msg = HL7TestMsgUtil.readMsg(ACK_COMMIT_ERROR_FILE);
		
		Acknowledgement ack = ackParser.parse(msg);
		
		assertNotNull(ack);
		assertFalse(ack.isSuccess());
		assertEquals("Invalid LOINC code", ack.getErrorDiagnosticsInformation());
		assertEquals(MSG_ID, ack.getMsgId());
		assertEquals("103", ack.getErrorCode());
	}
}
