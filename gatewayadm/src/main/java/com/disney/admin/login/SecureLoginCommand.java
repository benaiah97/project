package com.disney.admin.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.admin.AdminCommand;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;


/**
 * Secure Login Admin Command
 *
 * @version 1.0
 * @author fav2
 */
public class SecureLoginCommand extends AdminCommand
{
    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws javax.servlet.ServletException DOCUMENT ME!
     * @throws java.io.IOException DOCUMENT ME!
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        /*get username and password from request and compare against valid usernames and passwords from
         *admin.properties file. set group information for user on successful login.
         *one server, one user, one group currently.
         */
        String user = request.getParameter ("USERNAME");
        String password = request.getParameter ("PASSWORD");

        if ((user != null) && (password != null))
        {
        	AbstractInitializer init = AbstractInitializer.getInitializer();
            java.lang.String node = AbstractInitializer.getNodeName();
            java.util.Properties pAdmin = init.getProps("admin.properties");
            java.lang.String uPass = pAdmin.getProperty (node + "_" + user + "_password", null);

            if (uPass != null)
            {
                if (uPass.trim ().equals (password.trim ()))
                {
                    java.lang.String uGroup = pAdmin.getProperty (node + "_" + user + "_group", 
                                                                  "DEFAULT");
                    javax.servlet.http.HttpSession session = request.getSession (true);
                    session.setAttribute ("USERNAME", user);
                    session.setAttribute ("USERGROUP", uGroup);
                    session.setAttribute ("LOGGED_IN", (new Boolean(true)));
                }
                else
                {
                    //log unauthorized or failed login attempts
					evl.sendException("Attempt to login as user " + user + " failed", 
						EventType.EXCEPTION, ErrorCode.SECURITY_EXCEPTION, null, this);
                }
            }
        }

        return "index.html";
    }
}