package com.disney.admin.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class SimpleLoginFilter implements ServletFilterIF
{
    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Constructor for StubFilter
     */
    public SimpleLoginFilter()
    {
        super ();
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * @see ServletFilterIF#destroy()
     */
    public void destroy() {}


    /**
     * @see ServletFilterIF#doFilter(ServletRequest, ServletResponse, ServletFilterChain)
     */
    public void doFilter(HttpServletRequest request, HttpServletResponse response, 
                         ServletFilterChain chain)
        throws IOException, ServletException
    {
        Boolean loggedIn = (Boolean)request.getSession (true).getAttribute ("LOGGED_IN");

        if ((loggedIn == null) || (!(loggedIn.booleanValue ())))
        {
            try
            {
                System.out.println ("Not Logged in.. forwarding to welcome.jsp");

                RequestDispatcher rd = request.getRequestDispatcher ("welcome.jsp");
                rd.forward (request, response);
            }
            catch (Throwable th)
            {
                System.out.println ("Error forwarding login: " + th.toString ());
            }
        }
        else
        {
            chain.doFilter (request, response);
        }
    }
}