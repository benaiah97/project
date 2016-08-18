package com.disney.admin.filter;

import java.util.HashMap;


/**
 * DOCUMENT ME!
 *
 * @version $Revision$
 * @author $author$
 */
public class ServletFilterManagerFactory
{
    //~ Static variables/initializers --------------------------------------------------------------

    private static HashMap mgrs = null;


    /**
     * DOCUMENT ME!
     *
     * @param caller DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ServletFilterManager getServletFilterManager(Object caller)
    {
        if (mgrs == null)
        {
            mgrs = new HashMap();
        }

        String fullClassName = caller.getClass ().getName ();
        String className = fullClassName.substring (fullClassName.lastIndexOf (".") + 1);

        if (mgrs.containsKey (className))
        {
            return (ServletFilterManager)mgrs.get (className);
        }
        else
        {
            ServletFilterManager mgr = new ServletFilterManager(className);
            mgrs.put (className, mgr);

            return mgr;
        }
    }
}