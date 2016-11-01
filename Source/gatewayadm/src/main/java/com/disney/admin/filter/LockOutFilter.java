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
public class LockOutFilter implements ServletFilterIF
{
    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Constructor for LockOutFilter
     */
    public LockOutFilter()
    {
        super ();
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * @see ServletFilterIF#destroy()
     */
    public void destroy() {}


    /**
     * @see ServletFilterIF#doFilter(HttpServletRequest, HttpServletResponse, ServletFilterChain)
     */
    public void doFilter(HttpServletRequest request, HttpServletResponse response, 
                         ServletFilterChain chain)
        throws IOException, ServletException
    {
        try
        {
            RequestDispatcher rd = request.getRequestDispatcher ("welcome.jsp");
            rd.forward (request, response);
        }
        catch (Throwable th)
        {
            System.out.println ("Error forwarding login: " + th.toString ());
            chain.doFilter (request, response);
        }
    }
}