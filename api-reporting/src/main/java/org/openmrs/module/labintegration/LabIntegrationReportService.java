package org.openmrs.module.labintegration;

import org.openmrs.Obs;
import org.openmrs.api.OpenmrsService;

import java.util.Date;
import java.util.List;

public interface LabIntegrationReportService extends OpenmrsService {

    List<Obs> getLabResults(Date startDate, Date endDate);
}
