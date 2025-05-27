package org.openmrs.module.labintegration.api.advice;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.openmrs.CareSetting;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.TestOrder;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.OrderService;
import org.openmrs.api.context.Context;
import org.openmrs.module.labintegration.LabIntegrationConfig;
import org.openmrs.module.labintegration.api.hl7.ObsSelector;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncounterServiceRoundAdvice extends StaticMethodMatcherPointcutAdvisor implements Advisor {
	
	// we no longer register this aop round advice since it interfers with the proper functioning of the saveEncounter() in OpenMRS core ie in the ED module
	private OrderService orderService;
	
	private AdministrationService administrationService;
	
	private static final String TEST_CLASS = "Test";
	
	private static final String LAB_FORM_NAME = "Analyse de Laboratoire";
	
	private static final Integer DESTINATION_CONCEPT_ID = 160632;
	
	private static final String OPENELIS_DESTINATION = "OpenELIS";
	
	private Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public Advice getAdvice() {
		return new EncounterAdvice();
	}
	
	private class EncounterAdvice implements MethodInterceptor {
		
		@Override
		public Object invoke(MethodInvocation methodInvocation) throws Throwable {
			
			log.info("Round Advice for Method name :" + methodInvocation.getMethod().getName());
			Object[] args = methodInvocation.getArguments();
			orderService = Context.getOrderService();
			administrationService = Context.getAdministrationService();
			String labFormName = administrationService.getGlobalProperty(LabIntegrationConfig.GP_LABORATORY_FORM_NAME,
			    LAB_FORM_NAME);
			CareSetting careSetting = orderService.getCareSetting(2);
			ObsSelector obsSelector = new ObsSelector();
			if (args[0] instanceof Encounter) {
				Encounter encounter = (Encounter) args[0];
				log.debug("captured encounter " + encounter.getUuid());
				if (encounter.getForm() != null) {
					if (encounter.getForm().getName().equals(labFormName)) {
						
						Optional<Obs> destinationObs = encounter.getObs().stream()
						        .filter(obs -> obs.getConcept().getConceptId().equals(DESTINATION_CONCEPT_ID)).findFirst();
						if (destinationObs.isPresent()) {
							if (destinationObs.get().getValueText().equals(OPENELIS_DESTINATION)) {
								Set<Concept> testConcepts = new HashSet<>();
								encounter.getObs().forEach(obs -> {
									if (obsSelector.isValidTestType(obs)) {
										if (obs.getValueCoded().getConceptClass().getName().equals(TEST_CLASS)) {
											testConcepts.add(obs.getValueCoded());
										}
									}
									
								});
								Set<Order> orders = testConcepts.stream().map(testConcept -> {
									TestOrder order = new TestOrder();
									order.setConcept(testConcept);
									order.setEncounter(encounter);
									order.setPatient(encounter.getPatient());
									order.setOrderer(
									    encounter.getEncounterProviders().stream().findFirst().get().getProvider());
									order.setCareSetting(careSetting);
									return order;
								}).collect(Collectors.toSet());
								
								encounter.setOrders(orders);
								if (methodInvocation instanceof ReflectiveMethodInvocation) {
									ReflectiveMethodInvocation invocation = (ReflectiveMethodInvocation) methodInvocation;
									args[0] = encounter;
									invocation.setArguments(args);
								}
							}
						}
						
						log.info("EncounterAdvice Complemented Mofidying the Form Encounter : " + labFormName);
					}
				}
			}
			return methodInvocation.proceed();
		}
	}
	
	@Override
	public boolean isPerInstance() {
		return false;
	}
	
	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		if (method.getName().equals("saveEncounter")) {
			return true;
		}
		return false;
	}
	
}
