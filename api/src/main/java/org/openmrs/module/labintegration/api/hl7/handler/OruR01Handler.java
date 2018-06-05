package org.openmrs.module.labintegration.api.hl7.handler;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.app.ApplicationException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v25.datatype.CE;
import ca.uhn.hl7v2.model.v25.datatype.DTM;
import ca.uhn.hl7v2.model.v25.datatype.FT;
import ca.uhn.hl7v2.model.v25.datatype.ID;
import ca.uhn.hl7v2.model.v25.datatype.NM;
import ca.uhn.hl7v2.model.v25.datatype.ST;
import ca.uhn.hl7v2.model.v25.datatype.TS;
import ca.uhn.hl7v2.model.v25.datatype.TX;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v25.group.ORU_R01_PATIENT_RESULT;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import ca.uhn.hl7v2.model.v25.segment.OBX;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.ConceptName;
import org.openmrs.ConceptProposal;
import org.openmrs.Encounter;
import org.openmrs.EncounterProvider;
import org.openmrs.Obs;
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.hl7.HL7Constants;
import org.openmrs.module.labintegration.api.alerts.AlertCreator;
import org.openmrs.module.labintegration.api.hl7.messages.util.OruR01Util;
import org.openmrs.module.labintegration.api.hl7.util.OpenElisStatusHelper;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@SuppressWarnings("PMD.CyclomaticComplexity")
public class OruR01Handler implements Application {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OruR01Handler.class);
	
	private static final String MESSAGE_VERSION = "2.5";
	
	/**
	 * Always returns true, assuming that the router calling this handler will only call this
	 * handler with ORU_R01 messages.
	 * 
	 * @return true
	 */
	@Override
	public boolean canProcess(Message message) {
		return message != null && "ORU_R01".equals(message.getName());
	}
	
	/**
	 * Processes an ORU R01 event message
	 */
	@Override
	public Message processMessage(Message message) throws ApplicationException, HL7Exception {
		if (!(message instanceof ORU_R01)) {
			throw new ApplicationException(Context.getMessageSourceService().getMessage("ORUR01.error.invalidMessage"));
		}
		
		LOGGER.debug("Processing ORU_R01 message");
		
		Message response;
		try {
			ORU_R01 oruR01Massage = (ORU_R01) message;
			response = processOruR01(oruR01Massage);
		}
		catch (ClassCastException ex) {
			LOGGER.warn("Error casting " + message.getClass().getName() + " to ORU_R01", ex);
			throw new ApplicationException(Context.getMessageSourceService().getMessage("ORUR01.error.invalidMessageType ",
			    new Object[] { message.getClass().getName() }, null), ex);
		}
		catch (HL7Exception ex) {
			LOGGER.warn("Error while processing ORU_R01 message", ex);
			throw new ApplicationException(Context.getMessageSourceService().getMessage("ORUR01.error.WhileProcessing"), ex);
		}
		
		LOGGER.debug("Finished processing ORU_R01 message");
		
		return response;
	}
	
	/**
	 * Bulk of the processing done here. Called by the main processMessage method
	 * 
	 * @param message the message to process
	 * @return the processed message
	 */
	private Message processOruR01(ORU_R01 message) throws HL7Exception, ApplicationException {
		
		validateMessageVersion(message);
		
		// Extract segments from message
		MSH msh = getMSH(message);
		
		//ORC orc = getORC(message);
		
		// ORC values
		//ID orderControl = orc.getOrc1_OrderControl();
		//EI placerOrderNumber = orc.getOrc2_PlacerOrderNumber();
		//EI fillerOrderNumber = orc.getFillerOrderNumber();
		//CWE orderType = orc.getOrderType();
		
		// Obtain message control id (unique ID for message from sending application)
		String messageControlId = msh.getMessageControlID().getValue();
		LOGGER.debug("Found HL7 message in inbound queue with control id = {}", messageControlId);
		
		ORU_R01_PATIENT_RESULT patientResult = message.getPATIENT_RESULT();
		int numObr = patientResult.getORDER_OBSERVATIONReps();
		for (int i = 0; i < numObr; i++) {
			
			LOGGER.debug("Processing OBR {}", i);
			LOGGER.debug(" of {}", numObr);
			
			ORU_R01_ORDER_OBSERVATION orderObs = patientResult.getORDER_OBSERVATION(i);
			OBR obr = orderObs.getOBR();
			
			// OBR values
			//ID observationResultStatus = obr.getResultStatus();
			
			if (!StringUtils.hasText(obr.getUniversalServiceIdentifier().getIdentifier().getValue())) {
				throw new HL7Exception(Context.getMessageSourceService().getMessage("ORUR01.error.InvalidOBR",
				    new Object[] { messageControlId }, null));
			}

			String encounterId = OruR01Util.getUuidFromOBRSegment4(obr);
			// Get the encounter
			Encounter encounter = Context.getEncounterService().getEncounterByUuid(encounterId);
			if (encounter == null) {
				throw new HL7Exception(Context.getMessageSourceService().getMessage("ORUR01.error.InvalidEncounter",
				    new Object[] { messageControlId }, null));
			}
			
			// Loop over the obs and create each object, adding it to the encounter
			int numObs = orderObs.getOBSERVATIONReps();
			for (int j = 0; j < numObs; j++) {
				LOGGER.debug("Processing OBS {}", j);
				
				// OBX values
				OBX obx = orderObs.getOBSERVATION(j).getOBX();
				LOGGER.debug("Parsing observation");
				Obs obs = parseObs(encounter, obx, messageControlId);
				voidPreviousObs(encounter, obs);

				LOGGER.debug("Finished creating observations");
				if (obs != null) {
					// set this obs on the encounter object that we will be saving later
					encounter.addObs(obs);
					createAlert(encounter, obs, message);
				}
			}
			
			LOGGER.debug("Creating the encounter object");
			Context.getEncounterService().saveEncounter(encounter);
		}
		
		return message;
	}
	
	/**
	 * Creates a ConceptProposal object that will need to be saved to the database at a later point.
	 */
	private ConceptProposal createConceptProposal(Encounter encounter, Concept concept, String originalText) {
		// value is a proposed concept, create a ConceptProposal
		// instead of an Obs for this observation
		// TODO: at this point if componentSeparator (^) is in text,
		// we'll only use the text before that delimiter!
		ConceptProposal conceptProposal = new ConceptProposal();
		conceptProposal.setOriginalText(originalText);
		conceptProposal.setState(OpenmrsConstants.CONCEPT_PROPOSAL_UNMAPPED);
		conceptProposal.setEncounter(encounter);
		conceptProposal.setObsConcept(concept);
		return conceptProposal;
	}
	
	/**
	 * Get an openmrs Concept object out of the given hl7 coded element
	 * 
	 * @param codedElement ce to pull from
	 * @param uid unique string for this message for any error reporting purposes
	 * @return new Concept object
	 * @throws HL7Exception if parsing errors occur
	 */
	private Concept getConcept(CE codedElement, String uid) throws HL7Exception {
		String hl7ConceptId = codedElement.getIdentifier().getValue();
		
		String codingSystem = codedElement.getNameOfCodingSystem().getValue();

		if ("LN".equals(codingSystem)) {
			codingSystem = "LOINC";
		}

		return getConcept(hl7ConceptId, codingSystem, uid);
	}
	
	/**
	 * Get a concept object representing this conceptId and coding system.<br>
	 * If codingSystem is 99DCT, then a new Concept with the given conceptId is returned.<br>
	 * Otherwise, the coding system is looked up in the ConceptMap for an openmrs concept mapped to
	 * that code.
	 * 
	 * @param hl7ConceptId the given hl7 conceptId
	 * @param codingSystem the coding system for this conceptid (e.g. 99DCT)
	 * @param uid unique string for this message for any error reporting purposes
	 * @return a Concept object or null if no conceptId with given coding system found
	 * @should return null if codingSystem not found
	 * @should return a Concept if given local coding system
	 * @should return a mapped Concept if given a valid mapping
	 */
	protected Concept getConcept(String hl7ConceptId, String codingSystem, String uid) throws HL7Exception {
		if (codingSystem == null || HL7Constants.HL7_LOCAL_CONCEPT.equals(codingSystem)) {
			// the concept is local
			Integer conceptId = Integer.valueOf(hl7ConceptId);
			return Context.getConceptService().getConcept(conceptId);
		} else {
			// the concept is not local, look it up in our mapping
			return Context.getConceptService().getConceptByMapping(hl7ConceptId, codingSystem);
		}
	}
	
	/**
	 * Creates the Obs from the OBX message
	 */
	private Obs parseObs(Encounter encounter, OBX obx, String uid) throws HL7Exception {
		
		LOGGER.debug("parsing observation: {}", obx);
		Varies[] values = obx.getObservationValue();
		if (values == null || values.length < 1) {
			return null;
		}
		
		String dataType = values[0].getName();
		LOGGER.debug(" datatype = {}", dataType);

		Concept concept = getConcept(obx.getObservationIdentifier(), uid);
		LOGGER.debug(" concept = {}", concept);
		
		ConceptName conceptName = getConceptName(obx.getObservationIdentifier());
		LOGGER.debug(" concept-name = {}", conceptName);
		
		Date datetime = getDatetime(obx);
		LOGGER.debug(" timestamp = {}", datetime);
		if (datetime == null) {
			datetime = encounter.getEncounterDatetime();
		}

		//Search concept
		//Concept conceptQuestion = Context.getConceptService().getConcept(labIntegrationConfig.getLabOrderConceptCode());
		//Get obs
		//List<Obs> obsList = Context.getObsService().getObservations(null, asList(encounter), asList(conceptQuestion),
		//asList(concept), null, null, null, null, null, null, null, true);

		Obs obs = new Obs();
		obs.setPerson(encounter.getPatient());
		obs.setConcept(concept);
		obs.setEncounter(encounter);
		obs.setObsDatetime(datetime);
		obs.setLocation(encounter.getLocation());
		obs.setCreator(encounter.getCreator());
		obs.setDateCreated(encounter.getDateCreated());
		
		// Set comments if there are any
		ORU_R01_OBSERVATION parent = (ORU_R01_OBSERVATION) obx.getParent();
		// Iterate over all OBX NTEs
        List<String> commentList = new ArrayList<>();
        for (int i = 0; i < parent.getNTEReps(); i++) {
			for (FT obxComment : parent.getNTE(i).getComment()) {
				commentList.add(obxComment.getValue());
			}
		}
		String comments = org.apache.commons.lang3.StringUtils.join(commentList, " ");

		// Only set comments if there are any
		if (StringUtils.hasText(comments)) {
			obs.setComment(comments);
		}
		
		Type obx5 = values[0].getData();
		if ("NM".equals(dataType)) {
			String value = ((NM) obx5).getValue();
			if (value == null || value.length() == 0) {
				LOGGER.warn("Not creating null valued obs for concept {}", concept);
				return null;
			} else if ("0".equals(value) || "1".equals(value)) {
				concept = concept.hydrate(concept.getConceptId().toString());
				obs.setConcept(concept);
				if (concept.getDatatype().isBoolean()) {
					obs.setValueBoolean("1".equals(value));
				} else if (concept.getDatatype().isNumeric()) {
					try {
						obs.setValueNumeric(Double.valueOf(value));
					}
					catch (NumberFormatException e) {
						throw new HL7Exception(Context.getMessageSourceService().getMessage(
						    "ORUR01.error.notnumericConcept",
						    new Object[] { value, concept.getConceptId(), conceptName.getName(), uid }, null), e);
					}
				} else if (concept.getDatatype().isCoded()) {
					Concept answer = "1".equals(value) ? Context.getConceptService().getTrueConcept() : Context
					        .getConceptService().getFalseConcept();
					boolean isValidAnswer = false;
					Collection<ConceptAnswer> conceptAnswers = concept.getAnswers();
					if (conceptAnswers != null && !conceptAnswers.isEmpty()) {
						for (ConceptAnswer conceptAnswer : conceptAnswers) {
							if (conceptAnswer.getAnswerConcept().getId().equals(answer.getId())) {
								obs.setValueCoded(answer);
								isValidAnswer = true;
								break;
							}
						}
					}
					// Answer the boolean answer concept was't found
					if (!isValidAnswer) {
						throw new HL7Exception(Context.getMessageSourceService().getMessage("ORUR01.error.invalidAnswer",
						    new Object[] { answer.toString(), uid }, null));
					}
				} else {
					// Throw this exception to make sure that the handler doesn't silently ignore bad hl7 message
					throw new HL7Exception(Context.getMessageSourceService().getMessage("ORUR01.error.CannotSetBoolean",
					    new Object[] { obs.getConcept().getConceptId() }, null));
				}
			} else {
				try {
					obs.setValueNumeric(Double.valueOf(value));
				}
				catch (NumberFormatException ex) {
					throw new HL7Exception(Context.getMessageSourceService().getMessage("ORUR01.error.notnumericConcept",
					    new Object[] { value, concept.getConceptId(), conceptName.getName(), uid }, null), ex);
				}
			}
		} else if ("CE".equals(dataType)) {
			CE value = (CE) obx5;
			String valueIdentifier = value.getIdentifier().getValue();
			String valueName = value.getText().getValue();
			if (isConceptProposal(valueIdentifier)) {
				if (!StringUtils.isEmpty(valueName)) {
					ConceptProposal conceptProposal = createConceptProposal(encounter, concept, valueName);
					Context.getConceptService().saveConceptProposal(conceptProposal);
				} else {
					throw new HL7Exception(Context.getMessageSourceService().getMessage("Hl7.proposed.concept.name.empty"));
				}
			} else {
				obs.setValueCoded(getConcept(value, uid));
				obs.setValueCodedName(getConceptName(value));
			}
		} else if ("TX".equals(dataType)) {
			TX value = (TX) obx5;
			if (value == null || value.getValue() == null || value.getValue().trim().length() == 0) {
				LOGGER.warn("Not creating null valued obs for concept " + concept);
				return null;
			}
			obs.setValueText(value.getValue());
		} else {
			// Unsupported data type
			throw new HL7Exception(Context.getMessageSourceService().getMessage("ORUR01.error.UpsupportedObsType",
			    new Object[] { dataType }, null));
		}
		
		return obs;
	}

	private void voidPreviousObs(Encounter encounter, Obs newObs) {
		for (Obs obs : encounter.getObs()) {
			if (obs.getConcept().getId().equals(newObs.getConcept().getId())
				&& obs.getId() != null) {
				obs.setVoided(true);
				obs.setDateVoided(new Date());
				obs.setVoidReason("New result arrived from OpenELIS");
				obs.setVoidedBy(getUserForEncounter(encounter));

				Obs group = obs.getObsGroup();
				if (group != null) {
					group.addGroupMember(newObs);
				}
			}
		}
	}

	private User getUserForEncounter(Encounter encounter) {
		for (EncounterProvider encProvider : encounter.getEncounterProviders()) {
			Person person = encProvider.getProvider().getPerson();
			List<User> users = Context.getUserService().getUsersByPerson(person, false);
			if (!users.isEmpty()) {
				return users.get(0);
			}
		}
		return null;
	}

	private boolean isConceptProposal(String identifier) {
		return OpenmrsUtil.nullSafeEquals(identifier, OpenmrsConstants.PROPOSED_CONCEPT_IDENTIFIER);
	}
	
	private Date getDate(int year, int month, int day, int hour, int minute, int second) {
		Calendar cal = Calendar.getInstance();
		// Calendar.set(MONTH, int) is zero-based, Hl7 is not
		cal.set(year, month - 1, day, hour, minute, second);
		return cal.getTime();
	}
	
	/**
	 * Pull the timestamp for this obx out. if an invalid date is found, null is returned
	 * 
	 * @param obx the obs to parse and get the timestamp from
	 * @return an obx timestamp or null
	 * @see #getDatetime(TS)
	 */
	private Date getDatetime(OBX obx) throws HL7Exception {
		TS ts = obx.getDateTimeOfTheObservation();
		return getDatetime(ts);
	}
	
	/**
	 * Return a java date object for the given TS
	 * 
	 * @param ts TS to parse
	 * @return date object or null
	 */
	private Date getDatetime(TS ts) throws HL7Exception {
		DTM value = ts.getTime();
		
		if (value.getYear() == 0 || value.getValue() == null) {
			return null;
		}
		
		Date datetime = getDate(value.getYear(), value.getMonth(), value.getDay(), value.getHour(), value.getMinute(),
		    value.getSecond());
		
		return datetime;
		
	}
	
	/**
	 * Derive a concept name from the CE component of an hl7 message.
	 */
	private ConceptName getConceptName(CE ce) throws HL7Exception {
		ST altIdentifier = ce.getAlternateIdentifier();
		ID altCodingSystem = ce.getNameOfAlternateCodingSystem();
		return getConceptName(altIdentifier, altCodingSystem);
	}
	
	/**
	 * Derive a concept name from the CWE component of an hl7 message.
	 */
	private ConceptName getConceptName(ST altIdentifier, ID altCodingSystem) throws HL7Exception {
		if (altIdentifier != null && HL7Constants.HL7_LOCAL_CONCEPT_NAME.equals(altCodingSystem.getValue())) {
			String hl7ConceptNameId = altIdentifier.getValue();
			return getConceptName(hl7ConceptNameId);
		}
		
		return null;
	}
	
	/**
	 * Utility method to retrieve the openmrs ConceptName specified in an hl7 message observation
	 * segment. This method assumes that the check for 99NAM has been done already and is being
	 * given an openmrs conceptNameId
	 * 
	 * @param hl7ConceptNameId internal ConceptNameId to look up
	 * @return ConceptName from the database
	 */
	private ConceptName getConceptName(String hl7ConceptNameId) throws HL7Exception {
		ConceptName specifiedConceptName = null;
		if (hl7ConceptNameId != null) {
			// get the exact concept name specified by the id
			try {
				Integer conceptNameId = Integer.valueOf(hl7ConceptNameId);
				specifiedConceptName = new ConceptName();
				specifiedConceptName.setConceptNameId(conceptNameId);
			}
			catch (NumberFormatException e) {
				// if it is not a valid number, more than likely it is a bad hl7 message
				LOGGER.debug("Invalid concept name ID {}", hl7ConceptNameId, e);
			}
		}
		return specifiedConceptName;
		
	}
	
	private MSH getMSH(ORU_R01 message) {
		return message.getMSH();
	}
	
	//	private ORC getORC(ORU_R01 message) {
	//		return message.getPATIENT_RESULT().getORDER_OBSERVATION().getORC();
	//	}
	
	private void validateMessageVersion(ORU_R01 message) throws ApplicationException {
		if (!message.getVersion().equals(MESSAGE_VERSION)) {
			throw new ApplicationException(Context.getMessageSourceService()
			        .getMessage("ORUR01.error.invalidMessageVersion"));
		}
	}

	private void createAlert(Encounter encounter, Obs obs, ORU_R01 oru) {
		AlertCreator alertCreator = Context.getRegisteredComponent("alertCreator", AlertCreator.class);
		alertCreator.createAlert(encounter, obs, OpenElisStatusHelper.getStatus(oru));
	}
}
