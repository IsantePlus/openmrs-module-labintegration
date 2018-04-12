package org.openmrs.module.labintegration.api.hl7.messages;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v251.message.OML_O21;
import org.hibernate.exception.DataException;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.OrderParser;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.config.LabIntegrationProperties;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.MshGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.ObrGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.OrcGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.PidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OMLO21OrderParser implements OrderParser {
	
	@Autowired
	private MshGenerator mshGenerator;
	
	@Autowired
	private PidGenerator pidGenerator;
	
	@Autowired
	private LabIntegrationProperties labIntegrationProperties;
	
	@Autowired
	private OrcGenerator orcGenerator;
	
	@Autowired
	private ObrGenerator obrGenerator;
	
	@Override
	public String createMessage(Order order, OrderControl orderControl, HL7Config hl7Config) throws MessageCreationException {
		try {
			OML_O21 message = new OML_O21();
			message.initQuickstart("OML", "O21", labIntegrationProperties.getHL7ProcessingId());
			
			mshGenerator.updateMSH(message.getMSH(), hl7Config);
			pidGenerator.updatePid(message.getPATIENT().getPID(), order.getPatient(), hl7Config);
			
			OrderIdentifier orderIdentifier = hl7Config.buildOrderIdentifier(order);
			
			orcGenerator.updateOrc(message.getORDER().getORC(), order, orderControl.code(), orderIdentifier);
			obrGenerator.updateObr(message.getORDER().getOBSERVATION_REQUEST().getOBR(), order, orderIdentifier);
			
			return message.toString();
		}
		catch (IOException e) {
			throw messageCreationException(e);
		}
		catch (HL7Exception e) {
			throw messageCreationException(e);
		}
		catch (DataException e) {
			throw messageCreationException(e);
		}
	}
	
	private MessageCreationException messageCreationException(Exception cause) {
		return new MessageCreationException("Unable to create OML^O21 message", cause);
	}
}
