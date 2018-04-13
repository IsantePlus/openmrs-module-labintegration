package org.openmrs.module.labintegration.api.event;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.stereotype.Component;

@Component("labintegration.FormSubmitAfterAdvice")
public class FormSubmitAfterAdvice implements AfterReturningAdvice {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FormSubmitAfterAdvice.class);
	
	@Override
	public void afterReturning(Object o, Method method, Object[] objects, Object o1)
			throws Throwable {
		LOGGER.info("OK");
	}
}
