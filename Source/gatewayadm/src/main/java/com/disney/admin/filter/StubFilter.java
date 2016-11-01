package com.disney.admin.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;


/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class StubFilter implements ServletFilterIF
{
    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Constructor for StubFilter
     */
    public StubFilter()
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
        chain.doFilter (request, response);
    }
}