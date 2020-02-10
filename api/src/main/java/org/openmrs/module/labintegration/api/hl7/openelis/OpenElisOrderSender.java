package org.openmrs.module.labintegration.api.hl7.openelis;

import java.util.stream.Collectors;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Task;
import org.openmrs.Encounter;
import org.openmrs.module.fhir2.api.FhirTaskService;
import org.openmrs.module.labintegration.api.hl7.NewOrderException;
import org.openmrs.module.labintegration.api.hl7.OrderCancellationException;
import org.openmrs.module.labintegration.api.hl7.OrderSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenElisOrderSender implements OrderSender {

	@Autowired
	private FhirTaskService taskService;

	@Autowired
	private OpenElisHL7Config config;

	@Autowired
	private FhirContext fhirContext;

	@Autowired
	private TaskFactory taskFactory;

	@Override
	public void sendNewOrder(Encounter encounter) throws NewOrderException {
		Task request;
		try {
			 request = taskFactory.createTask(encounter);
		} catch (TaskCreationException e) {
			throw new OpenElisNewOrderException("Error creating order in OpenELIS for encounter " + encounter, e);
		}

		IGenericClient client = getClient();
		MethodOutcome outcome = client.create().resource(request).encodedJson().execute();

		if (outcome == null) {
			throw new OpenElisNewOrderException("Could not determine status of request for encounter " + encounter);
		} else {
			OperationOutcome operationOutcome = (OperationOutcome) outcome.getOperationOutcome();
			if (hasErrorMessage(operationOutcome)) {
				throw new OpenElisNewOrderException(
						"Error creating order in OpenELIS for encounter "
								+ encounter
								+ ":\n"
								+ operationOutcome.getIssue().stream().map(this::operationOutcomeToMessage)
										.collect(Collectors.joining("\n")));

			}
		}

		taskService.saveTask(request);
	}

	@Override
	public void sendOrderCancellation(Encounter encounter) throws OrderCancellationException {
		Task existingRequest = taskFactory.getTask(encounter);
		existingRequest.setStatus(Task.TaskStatus.CANCELLED);

		Task updateRequest = new Task();
		updateRequest.setId(existingRequest.getId());
		updateRequest.setStatus(Task.TaskStatus.CANCELLED);

		IGenericClient client = getClient();
		MethodOutcome outcome = client.update().resource(existingRequest).encodedJson().execute();

		if (outcome == null) {
			throw new OpenElisOrderCancellationException("Could not determine status of request for encounter " + encounter);
		} else {
			OperationOutcome operationOutcome = (OperationOutcome) outcome.getOperationOutcome();
			if (hasErrorMessage(operationOutcome)) {
				throw new OpenElisOrderCancellationException(
						"Error cancelling order in OpenELIS for encounter "
								+ encounter
								+ ":\n"
								+ operationOutcome.getIssue().stream().map(this::operationOutcomeToMessage)
										.collect(Collectors.joining("\n")));
			}
		}


		taskService.saveTask(existingRequest);
	}

	@Override
	public boolean isEnabled() {
		return config.isOpenElisConfigured();
	}

	private IGenericClient getClient() {
		return fhirContext.newRestfulGenericClient(config.getOpenElisUrl().toString());
	}

	private boolean hasErrorMessage(OperationOutcome operationOutcome) {
		return operationOutcome.getIssue().stream().anyMatch(c ->
			c.hasSeverity() && c.getSeverity().ordinal() < OperationOutcome.IssueSeverity.WARNING.ordinal()
		);
	}

	private String operationOutcomeToMessage(OperationOutcome.OperationOutcomeIssueComponent component) {
		StringBuilder sb = new StringBuilder();

		if (component.hasSeverity() && component.getSeverity() != OperationOutcome.IssueSeverity.INFORMATION) {
			sb.append(component.getSeverity().getDisplay()).append(": ");
		}

		if (component.hasCode()) {
			sb.append(component.getCode()).append(" - ");
		}

		if (component.hasExpression()) {
			sb.append(component.getExpression()).append(" - ");
		}

		if (component.hasDetails()) {
			sb.append(component.getDetails());
		}

		return sb.toString();
	}
}
