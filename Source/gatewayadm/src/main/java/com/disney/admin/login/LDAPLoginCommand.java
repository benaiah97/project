package com.disney.admin.login;

import com.disney.admin.AdminCommand;

import com.disney.auth.LDAPConnection;
import com.disney.auth.UserLevel;

import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * The LDAPLoginCommand allows user authentication using an LDAPConnection
 */
public class LDAPLoginCommand extends AdminCommand
{
    //~ Static variables/initializers ------------------------------------------

    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String LOGGED_IN = "LOGGED_IN";
    private static final String USERGROUP = "USERGROUP";
    private static final String GUEST = "GUEST";
    private static final String INDEX = "index.html";

    //~ Constructors -----------------------------------------------------------


    /**
     * Constructor for LDAPLoginCommand.
     */
    public LDAPLoginCommand()
    {
        super();
    }

    //~ Methods ----------------------------------------------------------------


    /**
     * Here we retrieve the user information from the request and pass it on to
     * the LDAPConnection for authentication. The LDAPConnection returns the
     * memberOf attribute, allowing us to look at the user's group
     * memberships. This can then be compared to the UserLevel to determine
     * the permissions for the user that has been authenticated.
     *
     * @see com.disney.admin.AdminCommand#performTask(HttpServletRequest,
     *      HttpServletResponse)
     */
    protected String performTask(
        HttpServletRequest request, HttpServletResponse response
    )
    {
        LDAPConnection conn = new LDAPConnection();
        String uid = request.getParameter(USERNAME);
        String cred = request.getParameter(PASSWORD);
        Attribute attr = null;

        try
        {
            /* don't want the user to have to enter the special characters so,
             * I have to put them in myself
             */
            int comma = uid.indexOf(',');

            if (comma != -1)
            {
                uid = uid.substring(0, comma) + "\\" + uid.substring(comma);
            }

            javax.servlet.http.HttpSession session = request.getSession();

            if (
                ((uid != null) || (uid.length() != 0))
                && ((cred != null) || (cred.length() != 0))
            )
            {
                attr = conn.getContext(uid, cred);
            }

            if (attr != null)
            {
                session.setAttribute(USERNAME, uid);
                session.setAttribute(LOGGED_IN, (new Boolean(true)));

                NamingEnumeration enum1 = attr.getAll();
                
                String group = getUserLevel(enum1);

                session.setAttribute(USERGROUP, group);
                
                evl.sendEvent("Logged in as: " + uid + " with group " + group, EventType.AUDIT, this);
            }
        }
        catch (NamingException e)
        {
            evl.sendException(
                EventType.EXCEPTION, ErrorCode.SECURITY_EXCEPTION, e, this
            );
        }
        finally
        {
            conn.closeContext();
        }

        return INDEX;
    }


    /**
     * This should be overriden by a subclass in order to read the correct
     * UserLevel subclass for the implementation. This compares the items in
     * the NamingEnumeration passed to the groups available in the UserLevel
     * class.
     *
     * @param enum The NamingEnumeration object that was retrieved from the
     *        attribute that  was returned by authentications
     *
     * @return The group name that has been determined for the user that was
     *         authenticated defaults to GUEST if the user was authenticated
     *         but is not a member of a supported group
     */
    private String getUserLevel(NamingEnumeration enum1)
    {
        String group = null;

        while (enum1.hasMoreElements() && (group == null))
        {
            String element = (String)enum1.nextElement();
            System.err.println(element);
            group = (UserLevel.parse(element));
        }

        if (group == null)
        {
            group = GUEST;
        }

        return group;
    }
}
