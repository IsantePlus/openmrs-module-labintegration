//package org.openmrs.module.labintegration.api.event;
//
//import static org.openmrs.event.Event.Action.CREATED;
//import static org.openmrs.event.Event.Action.UPDATED;
//
//import java.util.Arrays;
//import java.util.List;
//import javax.jms.JMSException;
//import javax.jms.MapMessage;
//import javax.jms.Message;
//import org.apache.commons.lang.exception.ExceptionUtils;
//import org.openmrs.Encounter;
//import org.openmrs.api.EncounterService;
//import org.openmrs.event.EventListener;
//import org.openmrs.module.labintegration.api.LabIntegrationService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component("labintegration.EncounterEventListener")
//public class EncounterEventListener implements EventListener {
//
//	private static final Logger LOGGER = LoggerFactory.getLogger(EncounterEventListener.class);
//
//	private static final String UUID_STRING_NAME = "uuid";
//	private static final String ACTION_STRING_NAME = "action";
//
//	@Autowired
//	private EncounterService encounterService;
//
//	@Autowired
//	private LabIntegrationService labIntegrationService;
//
//	private static final String ADULT_LAB_ORDER_ENCOUNTER_TYPE_UUID =
//			"10d73929-54b6-4d18-a647-8b7316bc1ae3";
//
//	private static final String PEDIATRIC_LAB_ORDER_ENCOUNTER_TYPE_UUID =
//			"a9392241-109f-4d67-885b-57cc4b8c638f";
//
//	private static final List<String> ACCEPTED_ENCOUNTERS_UUIDS = Arrays.asList(
//			ADULT_LAB_ORDER_ENCOUNTER_TYPE_UUID,
//			PEDIATRIC_LAB_ORDER_ENCOUNTER_TYPE_UUID);
//
//	@Override
//	public void onMessage(Message message) {
//		try {
//			MapMessage mapMessage = (MapMessage) message;
//			String uuid = ((MapMessage) message).getString(UUID_STRING_NAME);
//			String messageAction = mapMessage.getString(ACTION_STRING_NAME);
//			LOGGER.info("Fetched Encounter {} event. Action: {}", uuid, messageAction);
//
//			Encounter encounter = encounterService.getEncounterByUuid(uuid);
//			if ((CREATED.toString().equals(messageAction)
//					|| UPDATED.toString().equals(messageAction))
//					&& ACCEPTED_ENCOUNTERS_UUIDS.stream().anyMatch(x -> x.equals(uuid))) {
//				LOGGER.info("Found order encounter {}", uuid);
//				labIntegrationService.doOrder(encounter);
//			}
//		} catch (JMSException e) {
//			LOGGER.error(
//					String.format("Event error occurred. Error code: %s, %n"
//							+ "Linked exception: %n %s",
//							e.getErrorCode(),
//							ExceptionUtils.getFullStackTrace(e.getLinkedException())),
//					e);
//		}
//	}
//
//}
