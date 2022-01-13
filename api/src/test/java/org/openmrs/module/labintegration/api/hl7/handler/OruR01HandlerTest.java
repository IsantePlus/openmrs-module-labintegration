package org.openmrs.module.labintegration.api.hl7.handler;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.ApplicationException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import static org.junit.Assert.assertNotNull;
import static org.openmrs.module.labintegration.api.hl7.messages.util.OruR01Util.*;

public class OruR01HandlerTest extends BaseModuleContextSensitiveTest {

	private static final String DATASET = "lab-dataset.xml";

	@Before
	public void setUp() throws Exception {
		executeDataSet(DATASET);
	}

	@Test
	public void processMessage_shouldParseObsAndNotReturnNull() throws HL7Exception, ApplicationException {
		String hl7Message = "MSH|^~\\&|SOFTLAB|||LNSP|20210910132219-0400||ORU^R01|00000110|P|2.5\n"
				+ "PID||1310932929^^^^MR|1310932929^^^^MR||RAMON^RAZOR||19751201[0000]|M||U|^1??RE L'ARBRE^ANSE ROUGE^ART|||||||^549c78dc-31da-11e8-acac-c3add5b19973\n"
				+ "PV1||R|13109||||13109^HFSC^PRESTATAIRE^^^^^^^^^^L||||||||||^^^^^^^^^^^^L|||||||||||||||||||||||||||20210903||||||07d33a40-f185-11eb-ae5f-0242ac120009^f037e97b-471e-4898-a07c-b8e169e0ddc4^Indétectable^054edae9-1660-39e7-9920-6c814ed2df43^549c78dc-31da-11e8-acac-c3add5b19973\n"
				+ "ORC|RE|1310998951|1|77030005|||^^^202109031040-0400^^R||202109031040|HISTC||13109^HFSC^PRESTATAIRE^^^^^^^^^^L|13109\n"
				+ "OBR|1|1310998951|1|f037e97b-471e-4898-a07c-b8e169e0ddc4|||202109031040|||HISTC|N|||202109031046||13109^HFSC^PRESTATAIRE^^^^^^^^^^L||||||202109031128-0400|||F||^^^202109031040-0400^^R|||||||MCN\n"
				+ "OBX|1|SN|25836-8^^LN^LCV1P^Copies / ml (CVi-1)^L|0|^999||||||F|||202109031128||MCN|||202109031128\n"
				+ "OBX|2|ST|25836-8^^LN^LPLOG^Log (Copies / ml)^L|1|1,0||||||F|||202109031128||MCN|||202109031128\n"
				+ "NTE|1||Indétectable\n"
				+ "NTE|2\n"
				+ "NTE|3\n"
				+ "NTE|4\n"
				+ "NTE|5||Commentaire: <1000 copies/ml; patient en suppression virale.\n"
				+ "NTE|6||             Limite de détection sur plasma <150 Copies/ml.\n"
				+ "NTE|7||.\n"
				+ "NTE|8";
		hl7Message = hl7Message.replaceFirst(ORUR01_ORU_R01, ORU_R01);
		hl7Message = hl7Message.replaceFirst(VERSION_251, VERSION_25);
		hl7Message = hl7Message.replace("\n", Character.toString((char) 13));
		hl7Message = hl7Message.replaceAll("\\[[0-9]{4}\\]", "");

		PipeParser pipeParser = new PipeParser();
		Message message = null;
		message = pipeParser.parse(hl7Message);
		OruR01Handler oruR01Handler = new OruR01Handler();
		Message message1 = oruR01Handler.processMessage(message);
		assertNotNull(message);
	}
}
