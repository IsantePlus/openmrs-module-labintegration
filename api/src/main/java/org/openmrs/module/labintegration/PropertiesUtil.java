package org.openmrs.module.labintegration;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;

public final class PropertiesUtil {
    
    private static final String GP_LAB_ORDER_ENCOUNTER_TYPE_UUID = "labintegration.orderEncounterTypeUuid";
    
    public static String getGlobalProperty(String propertyName) {
        String propertyValue = Context.getAdministrationService().getGlobalProperty(propertyName);
        if (StringUtils.isBlank(propertyValue)) {
            throw new APIException(String.format("Property value for '%s' is not set", propertyName));
        }
        return propertyValue;
    }

    public static boolean isGlobalPropertySet(String propertyName) {
        String propertyValue = Context.getAdministrationService().getGlobalProperty(propertyName);
        return !StringUtils.isBlank(propertyValue);
    }

    public static String getLabOrderEncounterTypeUuid() {
        return getGlobalProperty(GP_LAB_ORDER_ENCOUNTER_TYPE_UUID);
    }

    private PropertiesUtil() {
    }
}
