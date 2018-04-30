package org.openmrs.module.labintegration.web;

import ca.uhn.hl7v2.HL7Exception;
import org.openmrs.module.labintegration.api.hl7.oru.OruRo1Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "api/message/orur01")
public class OruR01MessageController {
	
	private static final String ORDER_RECEIVED_NO_SPEC_FILE = "ORU_R01.hl7";
	
	@Autowired
	private OruRo1Receiver receiver;


	@RequestMapping(method = RequestMethod.GET)
	public void parseOruR01Message(@RequestBody String msg) {
		try {
			receiver.receiveMsg(msg);
		} catch (HL7Exception e) {
			// TODO
		}
	}
}
