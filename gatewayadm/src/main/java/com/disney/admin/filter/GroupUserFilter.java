package com.disney.admin.filter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author rra3 To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class GroupUserFilter implements ServletFilterIF
{
    //~ Constructors -----------------------------------------------------------


    /**
     * Constructor for GroupUserFilter.
     */
    public GroupUserFilter()
    {
        super();
    }

    //~ Methods ----------------------------------------------------------------


    /**
     * @see com.disney.admin.filter.ServletFilterIF#destroy()
     */
    public void destroy() {}


    /**
     * @see com.disney.admin.filter.ServletFilterIF#doFilter(HttpServletRequest,
     *      HttpServletResponse, ServletFilterChain)
     */
    public void doFilter(
        HttpServletRequest request, HttpServletResponse response,
        ServletFilterChain chain
    ) throws IOException, ServletException
    {
        Boolean loggedIn =
            (Boolean)request.getSession(true).getAttribute("LOGGED_IN");
        String userGroup =
            (String)request.getSession(true).getAttribute("USERGROUP");

        if (
            (loggedIn == null) || (!(loggedIn.booleanValue()))
            || !(
                !(userGroup.equals("USER")) || !(
                    userGroup.equals("SUPERUSER")
                ) || !(userGroup.equals("ADMIN"))
            )
        )
        {
            try
            {
                System.out.println("Not Logged in.. forwarding to welcome.jsp");
                request.getSession(true).setAttribute(
                    "LOGGED_IN", new Boolean(false)
                );

                RequestDispatcher rd =
                    request.getRequestDispatcher("welcome.jsp");
                rd.forward(request, response);
            }
            catch (Throwable th)
            {
                System.out.println("Error forwarding login: " + th.toString());
            }
        }
        else
        {
            chain.doFilter(request, response);
        }
    }
}
