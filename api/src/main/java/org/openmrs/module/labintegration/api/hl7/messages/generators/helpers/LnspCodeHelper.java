package org.openmrs.module.labintegration.api.hl7.messages.generators.helpers;

import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v25.segment.OBR;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Obs;
import org.openmrs.module.labintegration.api.hl7.messages.util.ConceptUtil;
import org.openmrs.module.labintegration.api.hl7.messages.util.EncounterUtil;
import org.openmrs.module.labintegration.api.hl7.scc.SCCHL7Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LnspCodeHelper {

    @Autowired
    private SCCHL7Config scchl7Config;

    public void updateUniversalServiceID(OBR obr, Obs obs) throws DataTypeException {
        String conceptCode = ConceptUtil.getLoincCode(obs);
        String lnspCode = findLnspCodeIfExist(obs, conceptCode);

        if (StringUtils.isNotBlank(lnspCode)) {
            obr.getUniversalServiceIdentifier().getIdentifier().setValue(lnspCode);
        }
    }

    private String findLnspCodeIfExist(Obs obs, String conceptCode) {
        String result = null;
        String alternativeConceptUuid = scchl7Config.mapConceptToLnspTest(conceptCode);

        if (StringUtils.isNotBlank(alternativeConceptUuid)) {
            Obs alternativeObs = EncounterUtil.findObsByConceptUuid(obs.getEncounter(), alternativeConceptUuid);
            if (alternativeObs == null) {
                throw new IllegalStateException("Wrong concept used for ordered test.");
            }

            Concept alternativeConcept = alternativeObs.getValueCoded();
            if (alternativeConcept != null) {
                result = ConceptUtil.getLnspCode(alternativeConcept);
                if (StringUtils.isBlank(result)) {
                    throw new IllegalStateException("LNSP code is mandatory");
                }
            }
        }
        return result;
    }
}
