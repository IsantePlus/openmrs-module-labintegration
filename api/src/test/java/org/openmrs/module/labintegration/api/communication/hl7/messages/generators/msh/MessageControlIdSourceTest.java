package org.openmrs.module.labintegration.api.communication.hl7.messages.generators.msh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.msh.MessageControlIdSource;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class MessageControlIdSourceTest {
	
	@InjectMocks
	private MessageControlIdSource idSource;
	
	@Test
	public void shouldGenerateId() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2017);
		calendar.set(Calendar.MONTH, Calendar.MAY);
		calendar.set(Calendar.DAY_OF_MONTH, 18);
		calendar.set(Calendar.HOUR, 4);
		calendar.set(Calendar.MINUTE, 5);
		calendar.set(Calendar.SECOND, 6);
		calendar.set(Calendar.AM_PM, Calendar.AM);
		Date date = calendar.getTime();
		
		String id = idSource.generateId(date);
		
		assertEquals("2017051804050600000", id);
		assertTrue(id.length() <= 20);
		
		id = idSource.generateId(date);
		
		assertEquals("2017051804050600001", id);
		assertTrue(id.length() <= 20);
	}
}
