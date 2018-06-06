package org.openmrs.module.labintegration.api.hl7;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Obs;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@Component
public class ObsSelector {

    private static final int TESTS_ORDERED_CONCEPT_ID = 1271;

    private Set<Integer> conceptIds = new HashSet<>();

    public ObsSelector() {
        InputStream in = null;
        try {
            in = getClass().getClassLoader().getResourceAsStream("test-concepts.txt");
            Scanner scanner = new Scanner(in, "UTF-8");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (StringUtils.isNotBlank(line)) {
                    conceptIds.add(Integer.valueOf(line));
                }
            }
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public Set<Integer> getConceptIds() {
        return conceptIds;
    }

    public boolean isTestType(Obs obs) {
        return TESTS_ORDERED_CONCEPT_ID == obs.getConcept().getConceptId();
    }
}
