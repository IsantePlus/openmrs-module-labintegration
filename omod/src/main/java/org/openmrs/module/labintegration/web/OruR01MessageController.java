package org.openmrs.module.labintegration.web;

import ca.uhn.hl7v2.HL7Exception;
import java.io.IOException;
import org.openmrs.hl7.HL7Service;
import org.openmrs.module.labintegration.api.hl7.messages.util.VersionUpdater;
import org.openmrs.module.labintegration.api.hl7.util.HL7TestMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "api/message/orur01")
public class OruR01MessageController {
	
	private static final String ORDER_RECEIVED_NO_SPEC_FILE = "ORU_R01.hl7";
	
	@Autowired
	@Qualifier("hL7ServiceLabIntegration")
	private HL7Service hl7Service;

	@Autowired
	private VersionUpdater versionUpdater;
	
	@RequestMapping(method = RequestMethod.GET)
	public void parseOruR01Message() throws IOException, HL7Exception {
		String msg = HL7TestMsgUtil.readMsg(ORDER_RECEIVED_NO_SPEC_FILE);

		hl7Service.processHL7Message(versionUpdater.updateFrom25To251(msg));
	}
	
}
