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
        System.out.println("SimpleLoginFilter");
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
    		System.out.println("Executing SimpleLoginFilter");
        Boolean loggedIn = (Boolean)request.getSession (true).getAttribute ("LOGGED_IN");

        if ((loggedIn == null) || (!(loggedIn.booleanValue ())))
        {
        		System.out.println ("Not Logged in.. attempting forwarding to welcome.jsp");
            try
            {
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
        		System.out.println("chaining SimpleLoginFilter");
            chain.doFilter (request, response);
        }
    }
}