package org.openmrs.module.labintegration.api.hl7.messages;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.OML_O21;
import org.hibernate.exception.DataException;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.ObsSelector;
import org.openmrs.module.labintegration.api.hl7.OrderConverter;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.config.LabIntegrationProperties;
import org.openmrs.module.labintegration.api.hl7.config.OrderIdentifier;
import org.openmrs.module.labintegration.api.hl7.messages.generators.MshGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.generators.ObrGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.generators.OrcGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.generators.PidGenerator;
import org.openmrs.module.labintegration.api.hl7.messages.util.VersionSwitcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OMLO21OrderConverter implements OrderConverter {
	
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
	
	@Autowired
	private ObsSelector obsSelector;
	
	@Override
	public String createMessage(Encounter encounter, OrderControl orderControl, HL7Config hl7Config)
	        throws MessageCreationException {
		try {
			OML_O21 message = new OML_O21();
			message.initQuickstart("OML", "O21", labIntegrationProperties.getHL7ProcessingId());
			
			mshGenerator.updateMsh(message.getMSH(), hl7Config);
			
			pidGenerator.updatePid(message.getPATIENT().getPID(), encounter, hl7Config);
			
			OrderIdentifier orderIdentifier = hl7Config.buildOrderIdentifier(encounter);
			
			int i = 0;
			for (Obs obs : encounter.getObs()) {
				if (obsSelector.isValidTestType(obs)) {
					orcGenerator.updateOrc(message.getORDER(i).getORC(), obs, orderControl.code(), orderIdentifier);
					obrGenerator.updateObr(message.getORDER(i).getOBSERVATION_REQUEST().getOBR(), obs, orderIdentifier);
					i++;
				}
			}
			
			String msg = message.toString();
			
			return VersionSwitcher.switchVersion(msg);
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
