package org.openmrs.module.labintegration.api.hl7.messages;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.ORM_O01;
import org.openmrs.Order;
import org.openmrs.module.labintegration.api.hl7.OrderConverter;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.config.LabIntegrationProperties;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.MshGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.ObrGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.OrcGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.PidGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.Pv1Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ORMO01OrderConverter implements OrderConverter {
	
	@Autowired
	private MshGenerator mshGenerator;
	
	@Autowired
	private PidGenerator pidGenerator;
	
	@Autowired
	private Pv1Generator pv1Generator;

	@Autowired
	private OrcGenerator orcGenerator;

	@Autowired
	private ObrGenerator obrGenerator;

	@Autowired
	private LabIntegrationProperties labIntegrationProperties;
	
	@Override
	public String createMessage(Order order, OrderControl orderControl, HL7Config hl7Config) throws MessageCreationException {
		try {
			ORM_O01 message = new ORM_O01();
			
			message.initQuickstart("ORM", "O01", labIntegrationProperties.getHL7ProcessingId());
			mshGenerator.updateMsh(message.getMSH(), hl7Config);
			pidGenerator.updatePid(message.getPATIENT().getPID(), order.getPatient(), hl7Config);
			pv1Generator.updatePv1(message.getPATIENT().getPATIENT_VISIT().getPV1(), hl7Config, order);

			OrderIdentifier orderIdentifier = hl7Config.buildOrderIdentifier(order);

			orcGenerator.updateOrc(message.getORDER().getORC(), order, orderControl.code(), orderIdentifier);
			obrGenerator.updateObr(message.getORDER().getORDER_DETAIL().getOBR(), order, orderIdentifier);
			
			return message.toString();
		}
		catch (IOException | HL7Exception e) {
			throw messageCreationException(e);
		}
	}
	
	private MessageCreationException messageCreationException(Exception cause) {
		return new MessageCreationException("Unable to create ORM^O01 message", cause);
	}
}
