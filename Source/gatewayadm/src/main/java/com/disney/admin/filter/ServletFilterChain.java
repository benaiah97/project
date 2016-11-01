package com.disney.admin.filter;

import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;


/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class ServletFilterChain
{
    //~ Instance variables -------------------------------------------------------------------------

    private Vector filters;
    private Iterator filterIterator;


    /**
     * Creates a new ServletFilterChain object.
     *
     * @param filterList DOCUMENT ME!
     */
    protected ServletFilterChain(Vector filterList)
    {
        this.filters = filterList;
        this.filterIterator = this.filters.iterator ();
    }


    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws java.io.IOException DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    public void doFilter(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException
    {
        if (filterIterator.hasNext ())
        {
            Object obj = filterIterator.next ();

            if (obj instanceof ServletFilterIF)
            {
                ServletFilterIF filter = (ServletFilterIF)obj;
                filter.doFilter (request, response, this);
            }
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     */
    protected void addFilter(ServletFilterIF filter)
    {
        if (filters == null)
        {
            filters = new Vector();
        }

        filters.add (filter);
    }
}