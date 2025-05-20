package org.openmrs.module.labintegration.reports;

import org.openmrs.module.reporting.common.Localized;
import org.openmrs.module.reporting.common.TimeQualifier;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.dataset.definition.RowPerObjectDataSetDefinition;

import java.util.Collections;
import java.util.List;

@Localized("labintegration.LabResultDataSetDefinition")
public class LabResultDataSetDefinition extends RowPerObjectDataSetDefinition {

    @Override
    public List<Class<? extends DataDefinition>> getSupportedDataDefinitionTypes() {
        return Collections.emptyList();
    }

    @Override
    public void addColumn(String name, DataDefinition dataDefinition, String mappings, DataConverter... converters) {

    }

    @Override
    public void addColumns(String name, RowPerObjectDataSetDefinition dataSetDefinition, String mappings, TimeQualifier whichValues, Integer numberOfValues, DataConverter... converters) {

    }
}
