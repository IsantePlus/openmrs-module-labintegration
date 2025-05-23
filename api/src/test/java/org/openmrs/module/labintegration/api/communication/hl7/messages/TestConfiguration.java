package org.openmrs.module.labintegration.api.communication.hl7.messages;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({ "classpath:applicationContext-service.xml", "classpath*:moduleApplicationContext.xml",
        "classpath*:test-labContext.xml" })
public class TestConfiguration {
	
}
