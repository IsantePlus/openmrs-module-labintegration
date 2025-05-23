package org.openmrs.module.labintegration;

import org.openmrs.module.reporting.report.manager.BaseReportManager;

public abstract class ActivatedReportManager extends BaseReportManager {
	
	public boolean isActivated() {
		return true;
	}
	
}
