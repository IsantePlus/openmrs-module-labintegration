package org.openmrs.module.labintegration.api.hl7.messages.gnerators;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import org.openmrs.module.labintegration.api.date.DateSource;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.msh.MessageControlIdSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MshGenerator {
	
	private static final String FIELD_SEPARATOR = "|";
	
	private static final String ENCODING_CHARS = "^!\\~";
	
	@Autowired
	private DateSource dateSource;
	
	@Autowired
	private MessageControlIdSource controlIdSource;
	
	public void updateMsh(MSH msh, HL7Config hl7Config) throws DataTypeException {
		Date msgDate = dateSource.now();
		
		msh.getFieldSeparator().setValue(FIELD_SEPARATOR);
		msh.getEncodingCharacters().setValue(ENCODING_CHARS);
		
		msh.getDateTimeOfMessage().getTime().setValue(msgDate);
		msh.getMessageControlID().setValue(controlIdSource.generateId(msgDate));
		
		msh.getReceivingApplication().getNamespaceID().setValue(hl7Config.getReceivingApplication());
		msh.getSendingApplication().getNamespaceID().setValue(hl7Config.getSendingApplication());
		
		msh.getSendingFacility().getNamespaceID().setValue(hl7Config.getSendingFacilityNamespaceID());
	}
}
