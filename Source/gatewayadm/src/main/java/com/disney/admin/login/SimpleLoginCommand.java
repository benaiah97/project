package com.disney.admin.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.AdminCommand;

/**
 * Simple Admin Command
 *
 * @version 1.0
 * @author fav2
 */
public class SimpleLoginCommand extends AdminCommand
{
    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Constructor for SimpleLoginServlet
     */
    public SimpleLoginCommand()
    {
        super ();
    }


    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        String user = request.getParameter ("USERNAME");
        String password = request.getParameter ("PASSWORD");

        if ((user != null) && (password != null))
        {
            // Try to log in.
            if (user.equalsIgnoreCase ("eai"))
            {
                if (password.equals ("destdisney"))
                {
                    javax.servlet.http.HttpSession session = request.getSession (true);
                    session.setAttribute ("USERNAME", user);
                    session.setAttribute ("USERGROUP", "ADMIN");
                    session.setAttribute ("LOGGED_IN", new Boolean(true));
                }
            }
        }

        return "index.html";
    }
}