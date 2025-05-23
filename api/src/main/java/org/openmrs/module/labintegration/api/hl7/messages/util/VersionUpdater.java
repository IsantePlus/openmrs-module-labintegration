package org.openmrs.module.labintegration.api.hl7.messages.util;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import org.openmrs.hl7.HL7Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class VersionUpdater {
	
	@Autowired
	@Qualifier("hL7ServiceLabIntegration")
	private HL7Service hl7Service;
	
	public ORU_R01 updateFrom25To251(String msg) throws HL7Exception {
		msg = OruR01Util.changeMessageVersionFrom251To25(msg);
		Message message = hl7Service.parseHL7String(msg);
		
		return (ORU_R01) message;
	}
	
}
