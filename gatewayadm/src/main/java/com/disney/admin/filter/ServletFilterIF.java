package com.disney.admin.filter;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public interface ServletFilterIF
{
    /**
     * DOCUMENT ME!
     */
    public void destroy();


    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     * @param chain DOCUMENT ME!
     *
     * @throws java.io.IOException DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    public void doFilter(HttpServletRequest request, HttpServletResponse response, 
                         ServletFilterChain chain)
        throws java.io.IOException, ServletException;
}