package org.openmrs.module.labintegration.api.communication.hl7.messages;

import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration(classes = TestConfiguration.class, inheritLocations = false)
public abstract class AbstractOrderConverterTest extends BaseModuleContextSensitiveTest {
	
	protected static final String DATASET = "lab-dataset.xml";
	
	protected static final int PATIENT_ID = 10;
	
	protected static final int PROVIDER_ID = 11;
}
