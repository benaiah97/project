package com.disney.admin.filter;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.logging.audit.EventType;

/**
 * DOCUMENT ME!
 *
 * @author RRA3
 */
public class SiteMinderAdminFilter extends SiteMinderFilter {
    //~ Static variables/initializers ------------------------------------------

    protected static final String ADMIN = "ADMIN";

    //~ Constructors -----------------------------------------------------------


    /**
     * Constructor for SiteMinderAdminFilter.
     */
    public SiteMinderAdminFilter() {
        super();
    }

    //~ Methods ----------------------------------------------------------------


    /**
     * works just like the SiteMinderFilter, except that it will check your
     * session against siteminder, and valid the user group to make sure the
     * user is in the ADMIN group
     *
     * @see com.wdw.eai.admin.filter.ServletFilterIF#doFilter(HttpServletRequest,
     *      HttpServletResponse, ServletFilterChain)
     */
    public void doFilter(HttpServletRequest request,
        HttpServletResponse response, ServletFilterChain chain)
        throws IOException, ServletException {
        String uGroup = (String)request.getSession(true).getAttribute(USERGROUP);
        int smStatus = -4;

        if ((uGroup != null) && uGroup.equals(ADMIN)) {
            smStatus = checkAuthed(request,response);

            if (smStatus == 1 && checkLoggedIn(request, response, smStatus)) {
                chain.doFilter(request, response);
            }
        } else {
            EVL.sendEvent("Not Logged in.. forwarding to Siteminder...",
                EventType.INFO, this);

            RequestDispatcher rd = request.getRequestDispatcher(WELCOME_JSP);
            rd.forward(request, response);
        }
    }
}
