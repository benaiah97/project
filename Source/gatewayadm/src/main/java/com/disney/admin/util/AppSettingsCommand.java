package com.disney.admin.util;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.SharedAdminCommand;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;


/**
 * Application Settings Admin Command
 * 
 * @version 1.0
 * @author fav2
 */
public class AppSettingsCommand extends SharedAdminCommand
{
    //~ Instance variables -------------------------------------------------------------------------

    private ArrayList propList = null;

    //~ Methods ------------------------------------------------------------------------------------

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
            if ((propList == null) || (action == null) || (action.equals (""))
                || (action.equalsIgnoreCase ("REFRESH")))
            {
                propList = AbstractInitializer.getInitializer().getPropFileList();
            }
            else
            {
                String propFileName = request.getParameter ("PROP_FILE_NAME");
                request.setAttribute ("currPropFileName", propFileName);

                if (action.equalsIgnoreCase ("LOOKUP"))
                {
                }
                else if (action.equalsIgnoreCase ("FORCE_RELOAD"))
                {
                    AbstractInitializer init = AbstractInitializer.getInitializer();
                    propList = init.getPropFileList ();

                    init.forceReload ();
                }
            }

            request.setAttribute ("propList", propList);
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
        }
		return "AppSettingsMain.jsp";
    }
}