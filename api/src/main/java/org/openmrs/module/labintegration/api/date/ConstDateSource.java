package org.openmrs.module.labintegration.api.date;

import java.util.Calendar;
import java.util.Date;

public class ConstDateSource implements DateSource {
	
	public static final int YEAR = 2018;
	
	public static final int MONTH = 3;
	
	public static final int DAY_OF_MONTH = 30;
	
	public static final int HOUR = 3;
	
	public static final int MINUTE = 7;
	
	public static final int SECOND = 3;
	
	public static final int MILLISECOND = 345;
	
	private final Date date;
	
	public ConstDateSource() {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(Calendar.AM_PM, Calendar.PM);
		calendar.set(Calendar.YEAR, YEAR);
		calendar.set(Calendar.MONTH, MONTH - 1);
		calendar.set(Calendar.DAY_OF_MONTH, DAY_OF_MONTH);
		calendar.set(Calendar.HOUR, HOUR);
		calendar.set(Calendar.MINUTE, MINUTE);
		calendar.set(Calendar.SECOND, SECOND);
		calendar.set(Calendar.MILLISECOND, MILLISECOND);
		
		date = calendar.getTime();
	}
	
	@Override
	public Date now() {
		return new Date(date.getTime());
	}
}
