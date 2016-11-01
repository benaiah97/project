package com.disney.admin.filter;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;


/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class ServletFilterManager
{
    //~ Instance variables -------------------------------------------------------------------------

    private final String DEFAULT_FILTER_LIST = "com.disney.admin.filter.StubFilter";
    private java.util.Vector filters;
    private EventLogger evl = null;


    /**
     * Creates a new ServletFilterManager object.
     *
     * @param servletName DOCUMENT ME!
     */
    protected ServletFilterManager(String servletName)
    {
        evl = EventLogger.getLogger("ADMIN");
        loadSettings (servletName);
    }


    /**
     * DOCUMENT ME!
     *
     * @param servletName DOCUMENT ME!
     */
    private void loadSettings(String servletName)
    {
        filters = new java.util.Vector();

	    ResourceBundle rb = ResourceLoader.getResourceBundle("admin");
	    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);
        
        String filterList = props.getProperty (servletName + "_FILTER_LIST", DEFAULT_FILTER_LIST);
        java.util.StringTokenizer st = new java.util.StringTokenizer(filterList, ",");

        while (st.hasMoreTokens ())
        {
            try
            {
                String filterName = st.nextToken ();
                Class filterClass = Class.forName (filterName);
                Object filterObj = filterClass.newInstance ();

                if (filterObj instanceof ServletFilterIF)
                {
                    ServletFilterIF filter = (ServletFilterIF)filterObj;
                    filters.add (filter);
                }
            }
            catch (Throwable th)
            {
                evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
            }
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean doFilter(HttpServletRequest request, HttpServletResponse response)
    {
        ServletFilterChain chain = new ServletFilterChain(filters);

        try
        {
            chain.doFilter (request, response);
        }
        catch (Throwable th)
        {
            try
            {
                javax.servlet.RequestDispatcher rd;

                request.setAttribute ("ErrorText", "Request\\Response failed the Filter check.");
                rd = request.getRequestDispatcher ("Error.jsp");
                rd.forward (request, response);
            }
            catch (Throwable e) {}

            return false;
        }
        
        // avoiding retrying to forward in admin servlets once filter has kicked in
        if (response.isCommitted()){
        	return false;
        }

        return true;
    }
}