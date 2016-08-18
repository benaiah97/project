package com.disney.admin.jms;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.SharedAdminCommand;
import com.disney.jms.JmsReader;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;


/**
 * Jms Explorer Admin Command
 *
 * @version 1.0
 * @author fav2
 */
public class JmsExplorerCommand extends SharedAdminCommand
{
    //~ Instance variables -------------------------------------------------------------------------

    private ArrayList jmsDefs = null;

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        String currentJmsDef = "";

        try
        {
            String action = request.getParameter ("ACTION");
            if (action == null)
            {
                action = "";
            }

            currentJmsDef = request.getParameter ("JMS_DEF_NAME");
            evl.sendEvent ("Action received: " + action + " -- " + currentJmsDef, EventType.DEBUG, 
                           this);

            if ((jmsDefs == null) || (action.equals ("")) || (action.equalsIgnoreCase ("REFRESH")))
            {
                // Refresh the Jms Definition List.
                jmsDefs = new ArrayList();

                Properties jmsProps = AbstractInitializer.getInitializer().getProps ("jms.properties");
                Enumeration propNames = jmsProps.propertyNames ();

                while (propNames.hasMoreElements ())
                {
                    String propName = ((String)propNames.nextElement ()).trim ();

                    if ((propName.toUpperCase ().startsWith ("JMS_"))
                        && (propName.toUpperCase ().endsWith ("_TYPE")))
                    {
                        String jmsDefName = propName.substring (4, propName.length () - 5);
                        jmsDefs.add (jmsDefName);
                    }
                }
            }
            else
            {
                if (action.equalsIgnoreCase ("REQ_EMPTY"))
                {
                    JmsReader readBoy = new JmsReader(currentJmsDef);

                    try
                    {
                        javax.jms.Message msg = null;

                        while ((msg = readBoy.readMessage (0)) != null)
                        {
                            // we could log these if we want.
                        }
                    }
                    catch (Throwable th)
                    {
                        evl.sendException ("Exception emptying RQ Queue: " + currentJmsDef, 
                                           EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, th, 
                                           this);
                    }

                    evl.sendEvent ("EMPTIED THE RQ QUEUE:" + currentJmsDef, EventType.AUDIT, this);
                    action = "EXPLORE";
                }
                else if (action.equalsIgnoreCase ("RSP_EMPTY"))
                {
                    JmsReader readBoy = new JmsReader(currentJmsDef);

                    try
                    {
                        javax.jms.Message msg = null;

                        while ((msg = readBoy.readReplyMessage (0)) != null)
                        {
                            // we could log these if we want.
                        }
                    }
                    catch (Throwable th)
                    {
                        evl.sendException ("Exception emptying RS Queue: " + currentJmsDef, 
                                           EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, th, 
                                           this);
                    }

                    evl.sendEvent ("EMPTIED THE RS QUEUE:" + currentJmsDef, EventType.AUDIT, this);
                    action = "EXPLORE";
                }

                if (action.equalsIgnoreCase ("EXPLORE"))
                {
                    JmsDetailsBean jmsDetails = new JmsDetailsBean(currentJmsDef);
                    request.setAttribute ("jmsDetailsBean", jmsDetails);
                } // End EXPLORE
            }
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
            request.setAttribute ("throwable", th);
        }
        finally
        {
            request.setAttribute ("jmsDefList", jmsDefs);
            request.setAttribute ("jmsDefName", currentJmsDef);
            return "JmsExplorerMain.jsp";
        }
    }
}