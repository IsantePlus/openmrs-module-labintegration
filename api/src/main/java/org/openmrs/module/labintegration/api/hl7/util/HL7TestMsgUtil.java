package org.openmrs.module.labintegration.api.hl7.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public final class HL7TestMsgUtil {
	
	private static final int HOUR = 60 * 60 * 1000;
	
	public static String readMsg(String filename) throws IOException {
		InputStream in = null;
		try {
			in = HL7TestMsgUtil.class.getClassLoader().getResourceAsStream(filename);
			String expected = IOUtils.toString(in);
			// remove lfs from file
			expected = expected.replace("\r\n", "\r");
			expected = expected.replace("\n", "\r");
			// timezones
			int offset = Calendar.getInstance().getTimeZone().getRawOffset();
			offset = offset / HOUR;
			return expected.replace("{time_offset}", String.format("%02d", offset));
		}
		finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	private HL7TestMsgUtil() {
	}
}
