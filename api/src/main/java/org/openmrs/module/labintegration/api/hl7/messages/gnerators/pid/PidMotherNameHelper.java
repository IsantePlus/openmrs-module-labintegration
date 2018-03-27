package org.openmrs.module.labintegration.api.hl7.messages.gnerators.pid;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.datatype.XPN;
import ca.uhn.hl7v2.model.v25.segment.PID;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.module.labintegration.api.hl7.config.HL7Config;
import org.openmrs.module.labintegration.api.hl7.messages.util.PatientUtil;
import org.springframework.stereotype.Component;

@Component
public class PidMotherNameHelper {
	
	public void updateMotherName(PID pid, Patient patient, HL7Config hl7Config) throws DataTypeException {
		String motherName = PatientUtil.getAttribute(patient, hl7Config.getMotherNameAttrTypeName());
		
		if (StringUtils.isNotBlank(motherName)) {
			XPN xpn = pid.getMotherSMaidenName(0);
			xpn.getFamilyName().getSurname().setValue(motherName);
		}
	}
}
