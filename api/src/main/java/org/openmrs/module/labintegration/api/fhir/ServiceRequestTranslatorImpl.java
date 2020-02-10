package org.openmrs.module.labintegration.api.fhir;

import javax.inject.Inject;

import org.hl7.fhir.r4.model.ServiceRequest;
import org.openmrs.Obs;
import org.openmrs.module.fhir2.api.translators.ConceptTranslator;
import org.openmrs.module.fhir2.api.translators.ServiceRequestTranslator;
import org.openmrs.module.fhir2.api.translators.impl.BaseServiceRequestTranslatorImpl;
import org.openmrs.module.labintegration.api.hl7.ObsSelector;
import org.springframework.stereotype.Component;

@Component("labIntegrationServiceRequestTranslatorImpl")
public class ServiceRequestTranslatorImpl extends BaseServiceRequestTranslatorImpl implements ServiceRequestTranslator<Obs> {

	@Inject
	private ConceptTranslator conceptTranslator;

	@Override
	public ServiceRequest toFhirResource(Obs obs) {
		if (obs == null || obs.getConcept() == null || obs.getConcept().getId() != ObsSelector.TESTS_ORDERED_CONCEPT_ID) {
			return null;
		}

		ServiceRequest serviceRequest = new ServiceRequest();

		serviceRequest.setId(obs.getUuid());
		serviceRequest.setStatus(determineServiceRequestStatus(obs.getUuid()));
		serviceRequest.setCode(conceptTranslator.toFhirResource(obs.getValueCoded()));
		serviceRequest.setIntent(ServiceRequest.ServiceRequestIntent.ORDER);

		return serviceRequest;
	}
}
