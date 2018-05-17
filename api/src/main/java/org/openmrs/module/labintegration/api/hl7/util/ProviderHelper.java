package org.openmrs.module.labintegration.api.hl7.util;

import org.apache.commons.collections.CollectionUtils;
import org.openmrs.Encounter;
import org.openmrs.EncounterProvider;
import org.openmrs.Obs;
import org.openmrs.Provider;

import java.util.Set;

public final class ProviderHelper {

    public static Provider getProvider(Obs obs) {
        return getProvider(obs.getEncounter());
    }

    public static Provider getProvider(Encounter encounter) {
        Set<EncounterProvider> encProviders = encounter.getEncounterProviders();
        if (CollectionUtils.isNotEmpty(encProviders)) {
            return encProviders.iterator().next().getProvider();
        } else {
            return null;
        }
    }

    private ProviderHelper() {
    }
}
