package org.openmrs.module.labintegration.api.hl7.messages;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.OML_O21;
import org.hibernate.exception.DataException;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.communnication.OrderParser;
import org.openmrs.module.labintegration.api.hl7.config.LabIntegrationProperties;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.openmrs.module.labintegration.api.hl7.config.systems.openelis.OpenElisHL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.MshGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.ObrGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.OrcGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.PidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HL7OrderMessageGenerator implements OrderParser {
	
	@Autowired
	private MshGenerator mshGenerator;
	
	@Autowired
	private PidGenerator pidGenerator;
	
	@Autowired
	private OpenElisHL7Config openElisHL7Config;
	
	@Autowired
	private LabIntegrationProperties labIntegrationProperties;
	
	@Autowired
	private OrcGenerator orcGenerator;
	
	@Autowired
	private ObrGenerator obrGenerator;
	
	@Override
	public String createMessage(Order order, String orderControl) {
		try {
			OML_O21 message = new OML_O21();
			message.initQuickstart("OML", "O21", labIntegrationProperties.getHL7ProcessingId());
			
			mshGenerator.updateMSH(message.getMSH(), openElisHL7Config);
			pidGenerator.updatePid(message.getPATIENT().getPID(), order.getPatient(), openElisHL7Config);
			
			OrderIdentifier orderIdentifier = openElisHL7Config.buildOrderIdentifier(order);
			
			orcGenerator.updateOrc(message.getORDER().getORC(), order, orderControl, orderIdentifier);
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
