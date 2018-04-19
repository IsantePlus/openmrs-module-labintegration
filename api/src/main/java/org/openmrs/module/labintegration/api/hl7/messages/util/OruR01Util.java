package org.openmrs.module.labintegration.api.hl7.messages.util;

public final class OruR01Util {
	
	public static final String ORUR01_ORU_R01 = "ORU\\^R01\\^ORU_R01";
	
	public static final String ORU_R01 = "ORU^R01";
	
	public static final String VERSION_25 = "2.5";
	
	public static final String VERSION_251 = "2.5.1";
	
	public static String changeMessageVersionFrom251To25(String message) {
		message = message.replaceFirst(ORUR01_ORU_R01, ORU_R01);
		message = message.replaceFirst(VERSION_251, VERSION_25);
		return message;
	}
}
