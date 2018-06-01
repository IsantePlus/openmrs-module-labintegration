package org.openmrs.module.labintegration.web;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.ACK;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.labintegration.api.hl7.messages.MessageCreationException;
import org.openmrs.module.labintegration.api.hl7.openelis.OpenElisHL7Config;
import org.openmrs.module.labintegration.api.hl7.oru.OruRo1Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResultServlet extends HttpServlet {

    private static final long serialVersionUID = -236230490819244022L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!allowMsg(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, req.getRemoteAddr()
                    + " is not allowed to send inbound messages");
            return;
        }

        OruRo1Receiver receiver = Context.getRegisteredComponent("OruRO1Receiver", OruRo1Receiver.class);

        String msg = IOUtils.toString(req.getInputStream());

        try {
            addProxyPermissions();

            ACK ack = receiver.receiveMsg(msg);
            String ackMsg = ack.encode();

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/hl7-v2");
            resp.setContentLength(ackMsg.length());
            resp.getWriter().write(ackMsg);

        } catch (MessageCreationException e) {
            LOGGER.error("Unable to create an HL7v2 message", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch (HL7Exception e) {
            LOGGER.error("HL7 Error", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private void addProxyPermissions() {
        Context.addProxyPrivilege("Get Encounters");
        Context.addProxyPrivilege("Get Concepts");
        Context.addProxyPrivilege("Get Users");
        Context.addProxyPrivilege("Manage Alerts");
        Context.addProxyPrivilege("Edit Encounters");
        Context.addProxyPrivilege("Add Encounters");
        Context.addProxyPrivilege("Get Locations");
        Context.addProxyPrivilege("Edit Observations");
        Context.addProxyPrivilege("Add Observations");
        Context.addProxyPrivilege("Get Observations");
    }

    private boolean allowMsg(HttpServletRequest req) {
        OpenElisHL7Config config = Context.getRegisteredComponent("OpenElisHL7Config", OpenElisHL7Config.class);

        String expectedHost = config.getOpenelisInboundHost();

        return StringUtils.equals(req.getRemoteAddr(), expectedHost);
    }
}
