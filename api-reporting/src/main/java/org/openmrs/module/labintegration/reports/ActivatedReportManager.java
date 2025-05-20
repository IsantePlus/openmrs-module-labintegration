package org.openmrs.module.labintegration.reports;

import org.openmrs.module.reporting.report.manager.ReportManager;

public interface ActivatedReportManager extends ReportManager {

    default boolean isActivated() {
        return true;
    }

}
