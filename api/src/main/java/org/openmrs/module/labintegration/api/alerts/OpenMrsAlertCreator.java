package org.openmrs.module.labintegration.api.alerts;

import org.apache.commons.collections.CollectionUtils;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterProvider;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.notification.Alert;
import org.openmrs.notification.AlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("alertCreator")
public class OpenMrsAlertCreator implements AlertCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMrsAlertCreator.class);

    @Autowired
    private AlertService alertService;

    @Autowired
    private UserService userService;

    @Override
    public void createAlert(Encounter encounter, Obs obs, String status) {
        List<User> users = findUsers(encounter);
        if (CollectionUtils.isEmpty(users)) {
            LOGGER.warn("No users found for encounter: {}", encounter.getUuid());
        } else {
            registerAlerts(obs.getConcept(), encounter.getPatient(), users, status);
        }
    }


    private List<User> findUsers(Encounter encounter) {
        List<User> users = new ArrayList<>();
        for (EncounterProvider encProvider : encounter.getEncounterProviders()) {
            Person person = encProvider.getProvider().getPerson();
            users.addAll(userService.getUsersByPerson(person, false));
        }
        return users;
    }

    private void registerAlerts(Concept concept, Patient patient, List<User> users, String status) {
        String text = String.format("Lab: %s  received for %s test for patient %s", status,
                concept.getName().getName(), patient.getPersonName().toString());
        Alert alert = new Alert(text, users);
        alert.setCreator(users.get(0));
        alertService.saveAlert(alert);
    }
}
