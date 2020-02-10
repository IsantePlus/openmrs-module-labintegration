package org.openmrs.module.labintegration.api.hl7.openelis;

import static org.hibernate.criterion.Restrictions.eq;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Subqueries;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.Task;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.module.fhir2.api.translators.PatientTranslator;
import org.openmrs.module.fhir2.api.translators.ServiceRequestTranslator;
import org.openmrs.module.fhir2.api.translators.TaskTranslator;
import org.openmrs.module.labintegration.api.hl7.ObsSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TaskFactory {

	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@Autowired
	private ObsSelector obsSelector;

	@Autowired
	private PatientTranslator patientTranslator;

	@Autowired
	@Qualifier("labIntegrationServiceRequestTranslatorImpl")
	private ServiceRequestTranslator<Obs> serviceRequestTranslator;

	@Autowired
	private TaskTranslator taskTranslator;

	public Task createTask(Encounter encounter) throws TaskCreationException {
		AtomicReference<Obs> orderObs = new AtomicReference<>();
		encounter.getObs().stream().filter(obsSelector::isTestType).findFirst().ifPresent(orderObs::set);

		if (orderObs.get() == null) {
			throw new TaskCreationException("Could not find order for encounter " + encounter);
		}

		ServiceRequest serviceRequest = serviceRequestTranslator.toFhirResource(orderObs.get());

		if (serviceRequest == null) {
			throw new TaskCreationException("Could not generate service request for order " + orderObs);
		}

		Patient patient = patientTranslator.toFhirResource(encounter.getPatient());

		if (patient == null) {
			throw new TaskCreationException("Could not determine patient for encounter " + encounter);
		}

		org.openmrs.module.fhir2.Task task = new org.openmrs.module.fhir2.Task();
		task.setBasedOn("ServiceRequest/" + serviceRequest.getId());
		task.setStatus(org.openmrs.module.fhir2.Task.TaskStatus.REQUESTED);
		task.setIntent(org.openmrs.module.fhir2.Task.TaskIntent.ORDER);

		Task fhirTask = taskTranslator.toFhirResource(task);
		fhirTask.addContained(serviceRequest);
		fhirTask.addContained(patient);

		return fhirTask;
	}

	@Transactional
	public Task getTask(Encounter encounter) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Obs.class)
				.setProjection(Projections.sqlProjection("concat('ServiceRequest/', uuid) as fhir_reference",
						new String[] { "fhir_reference" }, new Type[] { new StringType() }))
				.createAlias("concept", "c")
				.add(eq("encounter", encounter))
				.add(eq("c.conceptId", ObsSelector.TESTS_ORDERED_CONCEPT_ID));

		List<org.openmrs.module.fhir2.Task> tasks = sessionFactory.getCurrentSession()
				.createCriteria(org.openmrs.module.fhir2.Task.class).add(Subqueries.eq("basedOn", detachedCriteria)).list();

		if (tasks == null || tasks.size() < 1) {
			return null;
		}

		return taskTranslator.toFhirResource(tasks.get(0));
	}
}
