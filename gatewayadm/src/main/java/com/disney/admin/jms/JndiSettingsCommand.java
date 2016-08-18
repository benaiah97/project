package com.disney.admin.jms;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.SharedAdminCommand;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.JndiManagerFactory;
import com.disney.util.Sorter;


/**
 * JNDI Settings Admin Command
 *
 * @version 1.0
 * @author fav2
 */
public class JndiSettingsCommand extends SharedAdminCommand
{
    //~ Instance variables -------------------------------------------------------------------------

    private List allJndi = null;

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String action = request.getParameter ("ACTION");
            evl.sendEvent ("Action received: " + action, EventType.DEBUG, this);

            if ((action == null) || (action.equals ("")) || (action.equalsIgnoreCase ("REFRESH")))
            {
                this.allJndi = JndiManagerFactory.createJndiManager ("jms.properties")
                                                 .getJmsEjbListing (10);
                Sorter.sortStringList(this.allJndi);
            }
            else if (action.equalsIgnoreCase ("LOOKUP"))
            {
                String jndiLookupName = request.getParameter ("JndiName");
                evl.sendEvent ("Attempting to lookup object: " + jndiLookupName, EventType.DEBUG, 
                               this);

                Object lookupResult = JndiManagerFactory.createJndiManager ("jms.properties")
                                                        .lookup (jndiLookupName);
                request.setAttribute ("lookupResult", lookupResult);
            }

            request.setAttribute ("jndiList", this.allJndi);
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
        }
        return "JndiSettingsMain.jsp";
    }
}