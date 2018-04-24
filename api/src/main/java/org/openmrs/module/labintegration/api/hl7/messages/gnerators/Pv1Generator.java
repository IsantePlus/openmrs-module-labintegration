package org.openmrs.module.labintegration.api.hl7.messages.gnerators;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.datatype.XCN;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import org.openmrs.Order;
import org.openmrs.Provider;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.pv1.Pv1AssignedPatientLocationHelper;
import org.openmrs.module.labintegration.api.hl7.messages.gnerators.helpers.ProviderInformationHelper;
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
	
	public void updatePv1(PV1 pv1, HL7Config hl7Config, Order order) throws HL7Exception, MessageCreationException {
		updateAttendingDoctor(pv1, order);
		
		assignedPatientLocationHelper.updateAssignedPatientLocation(pv1, hl7Config, order);

		if (hl7Config.getAdmitDateFormat() != null) {
			DateFormat df = new SimpleDateFormat(hl7Config.getAdmitDateFormat());
			pv1.getAdmitDateTime().getTime().setValue(df.format(order.getDateActivated()));
		} else {
			pv1.getAdmitDateTime().getTime().setValue(order.getDateActivated());
		}
	}
	
	private void updateAttendingDoctor(PV1 pv1, Order order) throws HL7Exception {
		int quantity = pv1.getAdmittingDoctorReps();
		pv1.insertAttendingDoctor(quantity);
		
		Provider doctor = order.getOrderer();
		XCN orderingProvider = pv1.getAdmittingDoctor(quantity);
		providerInformationHelper.updateProviderInformation(orderingProvider, doctor);
		
		pv1.getAttendingDoctor()[quantity] = orderingProvider;
	}
}
