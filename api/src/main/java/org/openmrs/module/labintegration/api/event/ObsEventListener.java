package org.openmrs.module.labintegration.api.event;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openmrs.event.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("labintegration.ObsEventListener")
public class ObsEventListener implements EventListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ObsEventListener.class);
	
	@Override
	public void onMessage(Message message) {
		try {
			MapMessage mapMessage = (MapMessage) message;
			String messageAction = mapMessage.getString("action");
			LOGGER.info("Obs event fetched. Action: {}", messageAction);
		} catch (JMSException e) {
			LOGGER.error(
					String.format("Event error occurred. Error code: %s, %n"
							+ "Linked exception: %n %s",
							e.getErrorCode(),
							ExceptionUtils.getFullStackTrace(e.getLinkedException())),
					e);
		}
	}
	
}
