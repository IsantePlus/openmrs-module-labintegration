package org.openmrs.module.labintegration.api.hl7.messages.ack;

import ca.uhn.hl7v2.model.v251.message.ACK;

public class Acknowledgement {
	
	private static final String APP_ACCEPT = "AA";
	
	private static final String COMMIT_ACCEPT = "CA";
	
	private final boolean success;
	
	private final String errorDiagnosticsInformation;
	
	private final String msgId;
	
	private final String errorCode;
	
	Acknowledgement(ACK ack) {
		String ackCode = ack.getMSA().getAcknowledgmentCode().getValue();
		this.success = APP_ACCEPT.equals(ackCode) || COMMIT_ACCEPT.equals(ackCode);
		
		this.errorDiagnosticsInformation = ack.getERR().getDiagnosticInformation().getValue();
		this.msgId = ack.getMSH().getMessageControlID().getValue();
		this.errorCode = ack.getERR().getHL7ErrorCode().getIdentifier().getValue();
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public String getErrorDiagnosticsInformation() {
		return errorDiagnosticsInformation;
	}
	
	public String getMsgId() {
		return msgId;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
}
