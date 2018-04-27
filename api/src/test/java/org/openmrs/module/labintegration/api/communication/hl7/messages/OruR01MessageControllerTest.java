package org.openmrs.module.labintegration.api.communication.hl7.messages;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ORU_R01;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PatientService;
import org.openmrs.hl7.HL7Service;
import org.openmrs.module.labintegration.api.communication.hl7.messages.testdata.HL7TestOrder;
import org.openmrs.module.labintegration.api.hl7.messages.util.VersionUpdater;
import org.openmrs.module.labintegration.api.hl7.util.HL7TestMsgUtil;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertEquals;
import static org.openmrs.module.labintegration.api.hl7.messages.util.OruR01Util.changeMessageVersionFrom251To25;

@ContextConfiguration(locations = { "classpath*:moduleApplicationContext.xml",
        "classpath*:applicationContext-service.xml", "classpath*:test-labContext.xml" },
        inheritLocations = false)

public class OruR01MessageControllerTest extends BaseModuleContextSensitiveTest {

    private static final String DATA_SET = "lab-dataset.xml";

    private static final String ORDER_SPEC_RECEIVED = "ORU_R01_PRELIMINARY_RESULT.hl7";

    private static final int PATIENT_ID = 10;

    private static final int ENCOUNTER_TYPE_VITALS = 22;

    @Autowired
    private EncounterService encounterService;

    @Autowired
    @Qualifier("hL7ServiceLabIntegration")
    private HL7Service hl7Service;

    @Autowired
    private PatientService patientService;

    @Autowired
    private VersionUpdater versionUpdater;


    private Patient patient;

    @Before
    public void init() throws Exception {
        executeDataSet(DATA_SET);
        patient = patientService.getPatient(PATIENT_ID);
    }


    @Test
    public void shouldParseMessage() throws Exception {
        String msg25 = HL7TestMsgUtil.readMsg(ORDER_SPEC_RECEIVED);
        hl7Service.processHL7Message(versionUpdater.updateFrom25To251(msg25));


        //
        Encounter expected = null; // TODO: create expected based on ORU_R01_PRELIMINARY_RESULT file
        Encounter encounter = encounterService.getEncounterByUuid(expected.getUuid());
        assertEquals(expected, encounter);
    }


    @Test
    public void shouldParseEncounterId() throws Exception {
        String msg = HL7TestMsgUtil.readMsg(ORDER_SPEC_RECEIVED);
        hl7Service.processHL7Message(versionUpdater.updateFrom25To251(msg));

        Encounter expected = null; // TODO: create expected based on ORU_R01_PRELIMINARY_RESULT file
        Encounter encounter = encounterService.getEncounterByUuid(expected.getUuid());
        assertEquals(expected.getUuid(), encounter.getUuid());
    }
}
