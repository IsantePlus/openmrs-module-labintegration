package org.openmrs.module.labintegration.api.communication.hl7.messages.orur01;

import static org.openmrs.module.labintegration.api.hl7.messages.util.OruR01Util.changeMessageVersionFrom251To25;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import java.io.IOException;
import org.junit.Test;
import org.openmrs.hl7.HL7Service;
import org.openmrs.module.labintegration.api.communication.hl7.messages.testdata.HL7TestMsgUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class OruR01HandlerTest extends BaseModuleContextSensitiveTest {
	
	private static final String ORDER_RECEIVED_NO_SPEC_FILE = "ORU_R01.hl7";
	
	@Autowired
	@Qualifier("hL7ServiceLabIntegration")
	private HL7Service hl7Service;
	
	@Test
	public void shouldParseOruR01Messaggge() throws IOException, HL7Exception {
		String msg = HL7TestMsgUtil.readMsg(ORDER_RECEIVED_NO_SPEC_FILE);
		msg = changeMessageVersionFrom251To25(msg);
		
		Message message = hl7Service.parseHL7String(msg);
		ORU_R01 oruMessage = (ORU_R01) message;
		hl7Service.processHL7Message(oruMessage);
	}
	
}
