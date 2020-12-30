package org.openmrs.module.labintegration.api.hl7.messages.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.PV1;

public final class OruR01Util {

	public static final String ORUR01_ORU_R01 = "ORU\\^R01\\^ORU_R01";

	public static final String ORU_R01 = "ORU^R01";

	public static final String VERSION_25 = "2.5";

	public static final String VERSION_251 = "2.5.1";

	private static final Log LOGGER = LogFactory.getLog(OruR01Util.class);

	private static final Pattern ONLY_UUID = Pattern
			.compile("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}");

	public static String changeMessageVersionFrom251To25(String message) {
		message = message.replaceFirst(ORUR01_ORU_R01, ORU_R01);
		message = message.replaceFirst(VERSION_251, VERSION_25);
		return message;
	}

	public static String getUuidFromOBRSegment4(OBR obr) {
		try {
			String segment4 = obr.getObr4_UniversalServiceIdentifier().getIdentifier().getValue();
			Matcher matcher = ONLY_UUID.matcher(segment4);

			if (matcher.find()) {
				return matcher.group();
			} else {
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("An error occured while getting uuid from OBR segment", e);
			return null;
		}
	}

	public static String getUuidFromPV1Segment(PV1 pv1) {
		try {
			String segment505 = pv1.getAlternateVisitID().getCx5_IdentifierTypeCode().getValue();
			Matcher matcher = ONLY_UUID.matcher(segment505);

			if (matcher.find()) {
				return matcher.group();
			} else {
				return null;
			}
		} catch (Exception e) {
			LOGGER.error("An error occured while getting uuid from PV1 segment", e);
			return null;
		}
	}
}
