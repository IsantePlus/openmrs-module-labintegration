package org.openmrs.module.labintegration.web;

import org.openmrs.module.labintegration.api.hl7.oru.OruRo1Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "api/message/orur01")
public class OruR01MessageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OruR01MessageController.class);

	@Autowired
	private OruRo1Receiver receiver;

	@RequestMapping(method = RequestMethod.GET)
	public void parseOruR01Message(@RequestBody String msg) {
		receiver.receiveMsg(msg);
		// todo send ack
	}
}
