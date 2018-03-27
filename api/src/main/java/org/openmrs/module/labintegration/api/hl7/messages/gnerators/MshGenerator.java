package org.openmrs.module.labintegration.api.hl7.messages.gnerators;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import org.openmrs.module.labintegration.api.DateSource;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MshGenerator {
	
	private static final String FIELD_SEPARATOR = "|";
	
	private static final String ENCODING_CHARS = "^!\\&";
	
	@Autowired
	private DateSource dateSource;
	
	public void updateMSH(MSH msh, HL7Config hl7Config) throws DataTypeException {
		msh.getFieldSeparator().setValue(FIELD_SEPARATOR);
		msh.getEncodingCharacters().setValue(ENCODING_CHARS);
		
		msh.getDateTimeOfMessage().getTime().setValue(dateSource.now());
		msh.getMessageControlID().setValue(UUID.randomUUID().toString());
		
		msh.getReceivingApplication().getNamespaceID().setValue(hl7Config.getReceivingApplication());
		msh.getSendingApplication().getNamespaceID().setValue(hl7Config.getSendingApplication());
	}
}
