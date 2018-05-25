package org.openmrs.module.labintegration.api.hl7.messages;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.ORM_O01;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.ObsSelector;
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

	@Autowired
	private ObsSelector obsSelector;
	
	@Override
	public String createMessage(Encounter encounter, OrderControl orderControl, HL7Config hl7Config) throws MessageCreationException {
		try {
			ORM_O01 message = new ORM_O01();
			
			message.initQuickstart("ORM", "O01", labIntegrationProperties.getHL7ProcessingId());
			mshGenerator.updateMsh(message.getMSH(), hl7Config);
			pidGenerator.updatePid(message.getPATIENT().getPID(), encounter, hl7Config);
			pv1Generator.updatePv1(message.getPATIENT().getPATIENT_VISIT().getPV1(), hl7Config, encounter);

			OrderIdentifier orderIdentifier = hl7Config.buildOrderIdentifier(encounter);

			int i = 0;
			for (Obs obs : encounter.getObs()) {
				if (obsSelector.isTestType(obs)) {
					orcGenerator.updateOrc(message.getORDER(i).getORC(), obs, orderControl.code(), orderIdentifier);
					obrGenerator.updateObr(message.getORDER(i).getORDER_DETAIL().getOBR(), obs, orderIdentifier);
					i++;
				}
			}
			
			return message.toString();
		}
		catch (IOException e) {
			throw messageCreationException(e);
		}
		catch (HL7Exception e) {
			throw messageCreationException(e);
		}
	}
	
	private MessageCreationException messageCreationException(Exception cause) {
		return new MessageCreationException("Unable to create ORM^O01 message", cause);
	}
}
