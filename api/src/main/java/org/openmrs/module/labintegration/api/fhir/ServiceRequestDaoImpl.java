package org.openmrs.module.labintegration.api.fhir;

import static org.hibernate.criterion.Restrictions.eq;

import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.SessionFactory;
import org.hibernate.sql.JoinType;
import org.openmrs.Obs;
import org.openmrs.module.fhir2.api.dao.FhirServiceRequestDao;
import org.openmrs.module.labintegration.api.hl7.ObsSelector;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class ServiceRequestDaoImpl implements FhirServiceRequestDao<Obs> {

	@Inject
	@Named("sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public Obs getServiceRequestByUuid(String uuid) {
		return (Obs) sessionFactory.getCurrentSession().createCriteria(Obs.class)
				.createAlias("concept", "c",
						JoinType.INNER_JOIN, eq("c.id", ObsSelector.TESTS_ORDERED_CONCEPT_ID))
				.add(eq("uuid", uuid)).add(eq("voided", false)).uniqueResult();
	}
}
