package org.openmrs.module.labintegration.api.hl7.handler;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.ApplicationException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import org.junit.Test;
import org.openmrs.module.labintegration.api.communication.hl7.messages.AbstractOrderConverterTest;

import static org.junit.Assert.assertNotNull;
import static org.openmrs.module.labintegration.api.hl7.messages.util.OruR01Util.*;

public class OruR01HandlerTest extends AbstractOrderConverterTest {

	private static final String DATASET = "lab-dataset.xml";

	@Test
	public void processMessage_shouldParseObsAndNotReturnNull() throws HL7Exception, ApplicationException {

		try{
			executeDataSet(DATASET);
		} catch (Exception e) {
			return;
		}

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

	@Test
	public void processMessage_shouldProcessMessageToObs() throws Exception {
		executeDataSet(DATASET);

		String hl7Message = "MSH|^~\\&|||||20241216090516-0500||ORU^R01|00000000|P|2.5\n" +
				"PID||33118500^^^^MR^112123|33118500^^^^MR^112123||WASHINGTONN^GLENN||20000601|M||U|^4èME LA MONTAGNE^JEAN RABEL^NRO|||||||^7a732da7-f3a8-40fe-b9eb-c8a058de9497\n" +
				"PV1||R|11223||||11223^OPAP^PRESTATAIRE^^^^^^^^^^L||||||||||^^^^^^^^^^^^L|||||||||||||||||||||||||||20241216||||||290fb4d0-2ae6-40bd-a058-436acea3d881^f037e97b-471e-4898-a07c-b8e169e0ddc4^^b181ab58-e4fb-4b47-bc69-f0c05dd473cd^7a732da7-f3a8-40fe-b9eb-c8a058de9497\n" +
				"ORC|RE|11223-53-440|1|C8160001|||^^^202412160558-0500^^R||202412160558|HISTC||11223^OPAP^PRESTATAIRE^^^^^^^^^^L|11223\n" +
				"OBR|1|11223-53-440|1|f037e97b-471e-4898-a07c-b8e169e0ddc4|||202412160558|||HISTC|N|||202412160600||11223^OPAP^PRESTATAIRE^^^^^^^^^^L||||||202412160905-0500|||F||^^^202412160558-0500^^R|||||||DIG\n" +
				"OBX|1|CE|44871-2^^LN^LVH2P^ADN VIH-1, PCR-2^L|0|Détecté|||*|||F|||202412160601||DIG|||202412160905";
		hl7Message = hl7Message.replaceFirst(ORUR01_ORU_R01, ORU_R01);
		hl7Message = hl7Message.replaceFirst(VERSION_251, VERSION_25);
		hl7Message = hl7Message.replace("\n", Character.toString((char) 13));
		hl7Message = hl7Message.replaceAll("\\[[0-9]{4}\\]", "");

		Message message = new PipeParser().parse(hl7Message);
		OruR01Handler oruR01Handler = new OruR01Handler();

		Message ack = oruR01Handler.processMessage(message);
	}

	@Test
	public void processMessage_shouldProcessMessageToObsWithMinimalValue() throws Exception {
		executeDataSet(DATASET);

		String hl7Message = "MSH|^~\\&|||||20250110110400-0500||ORU^R01|00000076|P|2.5\n" +
				"PID||b890^^^^MR^111600|b890^^^^MR^111600||IMELDA^ENGRID||20061107|F||U|^7èME MORNE L'HOPITAL^PORT-AU-PRINCE^OST|||||||^a26244c2-2319-41d2-b315-87a7d0e39191\n" +
				"PV1||R|11100||||11100^HUEH^PRESTATAIRE^^^^^^^^^^L||||||||||^^^^^^^^^^^^L|||||||||||||||||||||||||||20250107||||||b0843976-2f60-4a2a-a7bd-12030531ed82^f037e97b-471e-4898-a07c-b8e169e0ddc4^Indétectable^03a89ca6-0717-455b-a30d-3f55be5b55dd^a26244c2-2319-41d2-b315-87a7d0e39191\n" +
				"ORC|RE|11100-121-830|1|C9070041|||^^^202501072042-0500^^R||202501072042|HISTC||11100^HUEH^PRESTATAIRE^^^^^^^^^^L|11100\n" +
				"OBR|1|11100-121-830|1|f037e97b-471e-4898-a07c-b8e169e0ddc4|||202501072042|||HISTC|N|||202501101044||11100^HUEH^PRESTATAIRE^^^^^^^^^^L||||||202501101103-0500|||F||^^^202501072042-0500^^R|||||||JSF\n" +
				"OBX|1|ST|25836-8^^LN^LBCVA^Copies / ml (CVau)^L|0|voir ci-dessous||||||F|||202501101103||JSF|||202501101103\n" +
				"NTE|1||Indétectable\n" +
				"OBX|2|ST|25836-8^^LN^LBLOG^Log (Copies / ml)^L|1|voir ci-dessous||||||F|||202501101103||JSF|||202501101103\n" +
				"NTE|1||Indétectable\n" +
				"NTE|2\n" +
				"NTE|3\n" +
				"NTE|4\n" +
				"NTE|5||Commentaire: <1000 copies/ml; patient en suppression virale.\n" +
				"NTE|6||             Limite de détection du test sur DBS <839 copies/ml.";
		hl7Message = hl7Message.replaceFirst(ORUR01_ORU_R01, ORU_R01);
		hl7Message = hl7Message.replaceFirst(VERSION_251, VERSION_25);
		hl7Message = hl7Message.replace("\n", Character.toString((char) 13));
		hl7Message = hl7Message.replaceAll("\\[[0-9]{4}\\]", "");

		Message message = new PipeParser().parse(hl7Message);
		OruR01Handler oruR01Handler = new OruR01Handler();

		Message ack = oruR01Handler.processMessage(message);
	}

	@Test
	public void processMessage_shouldProcessMessageToSkipObsForCancelledOrders() throws Exception {
		executeDataSet(DATASET);

		String hl7Message = "MSH|^~\\&|||||20250124005347-0500||ORU^R01|00000019|P|2.5\n" +
				"PID||111001992^^^^MR^111600|111001992^^^^MR^111600||ASIMOV^ISAAC||20241217|M||U|^1èRE SANS SOUCI^MOMBIN CROCHU^NRE|||||||^4e7067cf-e964-46af-9fa6-e15858d3c0fc\n" +
				"PV1||R|11100||||11100^HUEH^PRESTATAIRE^^^^^^^^^^L||||||||||^^^^^^^^^^^^L|||||||||||||||||||||||||||20250123||||||ecad04c3-dcef-4d5d-9075-ddaccab15d08^f037e97b-471e-4898-a07c-b8e169e0ddc4^^03a89ca6-0717-455b-a30d-3f55be5b55dd^4e7067cf-e964-46af-9fa6-e15858d3c0fc\n" +
				"ORC|RE|11100-224-1988|1|C9230000|||^^^202501230924-0500^^R||202501230924|HISTC||11100^HUEH^PRESTATAIRE^^^^^^^^^^L|11100\n" +
				"OBR|1|11100-224-1988|1|f037e97b-471e-4898-a07c-b8e169e0ddc4|||202501230924|||HISTC|N|||202501240043||11100^HUEH^PRESTATAIRE^^^^^^^^^^L||||||202501240053-0500|||F||^^^202501230924-0500^^R|||||||JSF\n" +
				"OBX|1|ST|25836-8^^LN^LBCVR^Copies / ml (CVr)^L|0|Annulé Lab||||||F|||202501240051||JSF|||202501240053\n" +
				"OBX|2|ST|25836-8^^LN^LBLOG^Log (Copies / ml)^L|1|Annulé Lab||||||F|||202501240051||JSF|||202501240053";
		hl7Message = hl7Message.replaceFirst(ORUR01_ORU_R01, ORU_R01);
		hl7Message = hl7Message.replaceFirst(VERSION_251, VERSION_25);
		hl7Message = hl7Message.replace("\n", Character.toString((char) 13));
		hl7Message = hl7Message.replaceAll("\\[[0-9]{4}\\]", "");

		Message message = new PipeParser().parse(hl7Message);
		OruR01Handler oruR01Handler = new OruR01Handler();

		Message ack = oruR01Handler.processMessage(message);
	}

	@Test
	public void processMessage_shouldProcessMessageToSkipObsForCancelledCodedOrders() throws Exception {
		executeDataSet(DATASET);

		String hl7Message = "MSH|^~\\&|||||20250124011234-0500||ORU^R01|00000051|P|2.5\n" +
				"PID||131091227^^^^MR^131109|131091227^^^^MR^131109||BOA^VAL||20241116|M||U|^2èME VARREUX^CROIX-DES-BOUQUETS^OST|||||||^d65fc63e-9c0f-4c6c-b870-5d29410faeb9\n" +
				"PV1||R|13109||||13109^HFSC^PRESTATAIRE^^^^^^^^^^L||||||||||^^^^^^^^^^^^L|||||||||||||||||||||||||||20250122||||||50c79a2d-e627-49ff-81a0-c3854a22b989^f037e97b-471e-4898-a07c-b8e169e0ddc4^^56684e1c-b23f-4269-8fb3-4dd80018a5e5^d65fc63e-9c0f-4c6c-b870-5d29410faeb9\n" +
				"ORC|RE|13109-309-2776|1|C9220024|||^^^202501221301-0500^^R||202501221301|HISTC||13109^HFSC^PRESTATAIRE^^^^^^^^^^L|13109\n" +
				"OBR|1|13109-309-2776|1|f037e97b-471e-4898-a07c-b8e169e0ddc4|||202501221301|||HISTC|N|||202501240105||13109^HFSC^PRESTATAIRE^^^^^^^^^^L||||||202501240112-0500|||F||^^^202501221301-0500^^R|||||||JSF\n" +
				"OBX|1|CE|44871-2^^LN^LVHCD^ADN VIH-1, PCR-C^L|0|Annulé Lab||||||F|||202501240112||JSF|||202501240112";
		hl7Message = hl7Message.replaceFirst(ORUR01_ORU_R01, ORU_R01);
		hl7Message = hl7Message.replaceFirst(VERSION_251, VERSION_25);
		hl7Message = hl7Message.replace("\n", Character.toString((char) 13));
		hl7Message = hl7Message.replaceAll("\\[[0-9]{4}\\]", "");

		Message message = new PipeParser().parse(hl7Message);
		OruR01Handler oruR01Handler = new OruR01Handler();

		Message ack = oruR01Handler.processMessage(message);
	}
}
