package org.openmrs.module.labintegration.web;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.ACK;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.oru.OruRo1Receiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "api/message/orders/results")
public class OruR01MessageController {

	@Autowired
	private OruRo1Receiver receiver;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody public String parseOruR01Message(@RequestBody String msg) throws MessageCreationException, HL7Exception {
		ACK ack = receiver.receiveMsg(msg);
		return ack.encode();
	}
}
