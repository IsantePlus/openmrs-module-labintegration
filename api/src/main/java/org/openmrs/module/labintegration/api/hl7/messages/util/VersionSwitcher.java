package org.openmrs.module.labintegration.api.hl7.messages.util;

/**
 * Hack util for switching the versions of messages between 2.5 and 2.5.1
 * in order to keep using the HAPI version we have on our path.
 */
public final class VersionSwitcher {

    public static String switchVersion(String msg) {
        return msg.replace("|2.5\r", "|2.5.1\r");
    }

    private VersionSwitcher() {
    }
}
