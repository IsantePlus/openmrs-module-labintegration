package org.openmrs.module.labintegration.api.hl7.messages.gnerators;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.datatype.XCN;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import org.openmrs.Encounter;
import org.openmrs.Provider;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers.ProviderInformationHelper;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.pv1.Pv1AssignedPatientLocationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Component
public class Pv1Generator {
	
	@Autowired
	private Pv1AssignedPatientLocationHelper assignedPatientLocationHelper;
	
	@Autowired
	private ProviderInformationHelper providerInformationHelper;
	
	public void updatePv1(PV1 pv1, HL7Config hl7Config, Encounter encounter) throws HL7Exception, MessageCreationException {
		updateAttendingDoctor(pv1, encounter);
		
		assignedPatientLocationHelper.updateAssignedPatientLocation(pv1, hl7Config, encounter);

		if (hl7Config.getAdmitDateFormat() != null) {
			DateFormat df = new SimpleDateFormat(hl7Config.getAdmitDateFormat());
			pv1.getAdmitDateTime().getTime().setValue(df.format(encounter.getEncounterDatetime()));
		} else {
			pv1.getAdmitDateTime().getTime().setValue(encounter.getEncounterDatetime());
		}
	}
	
	private void updateAttendingDoctor(PV1 pv1, Encounter encounter) throws HL7Exception {
		int quantity = pv1.getAttendingDoctorReps();
		pv1.insertAttendingDoctor(quantity);

		Provider doctor = encounter.getEncounterProviders().iterator().next().getProvider();
		XCN orderingProvider = pv1.getAttendingDoctor(quantity);
		providerInformationHelper.updateProviderInformation(orderingProvider, doctor, encounter);
		
		pv1.getAttendingDoctor()[quantity] = orderingProvider;
	}
}
