package com.disney.admin.login;

import netegrity.siteminder.javaagent.AgentAPI;
import netegrity.siteminder.javaagent.Attribute;
import netegrity.siteminder.javaagent.AttributeList;
import netegrity.siteminder.javaagent.InitDef;
import netegrity.siteminder.javaagent.RealmDef;
import netegrity.siteminder.javaagent.ResourceContextDef;
import netegrity.siteminder.javaagent.ServerDef;
import netegrity.siteminder.javaagent.SessionDef;
import netegrity.siteminder.javaagent.UserCredentials;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.disney.admin.AdminCommand;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.EncryptUtil;

import java.io.IOException;

import java.util.Properties;


/**
 * Site Minder Login Admin Command
 *
 * @author rra3
 */
public class SMLoginCommand extends AdminCommand
{
    //~ Classes ----------------------------------------------------------------


    /**
     * Internal class wraps the SiteMinder AgentAPI for access through the
     * servlet. Include initializer calls, etc... for dynamic configuration
     */
    private class SMAgent {
        //~ Static variables/initializers --------------------------------------

        private static final String ADMIN = "ADMIN";
        private static final String SUPERUSER = "SUPERUSER";
        private static final String USER = "USER";
        private static final String DEFAULT = "DEFAULT";
        private static final String USERGROUP = "USERGROUP";
        private static final String SM_ResourceContext = "SM_ResourceContext";
        private static final String SM_Realm = "SM_Realm";
        private static final String SM_Session = "SM_Session";
        private static final String SM_Attrs = "SM_Attrs";
        private static final String SM_InitDef = "SM_InitDef";

        //~ Instance variables -------------------------------------------------

        private final String node = System.getProperty("com.ibm.ejs.wlm.BootstrapNode",
                "localhost");
        private final Properties smProps = AbstractInitializer.getInitializer().getProps("siteminder.properties");
        private final String smAddress = smProps.getProperty(node + "_Server",
                "153.6.189.234");
        private final int smAccPort = Integer.parseInt(smProps.getProperty(node
                    + "_AccountingPort", "44441"));
        private final int smAuthentPort = Integer.parseInt(smProps.getProperty(node
                    + "_AuthenticationPort", "44442"));
        private final int smAuthorizePort = Integer.parseInt(smProps
                .getProperty(node + "_AuthorizationPort", "44443"));
        private final int smTimeout = Integer.parseInt(smProps.getProperty(node
                    + "_ServerTimeout", "120"));
        private final int smConnMin = Integer.parseInt(smProps.getProperty(node
                    + "_ConnectionMin", "1"));
        private final int smConnMax = Integer.parseInt(smProps.getProperty(node
                    + "_ConnectionMax", "1"));
        private final int smConnStep = Integer.parseInt(smProps.getProperty(node
                    + "_ConnectionStep", "1"));
        private final boolean secretIsPlain = (new Boolean(smProps.getProperty(node
                    + "_AgentSecretPlain", "FALSE"))).booleanValue();
        private final String agentName = smProps.getProperty(node
                + "_AgentName");
        private final String agentSecret = smProps.getProperty(node
                + "_AgentSecret");

        //Siteminder objects with noarg constructors
        private AgentAPI smAgent = new AgentAPI();
        private ServerDef smServer = new ServerDef();
        private RealmDef smRealm = new RealmDef();
        private SessionDef smSession = new SessionDef();
        private AttributeList smAttrs = new AttributeList();

        //Siteminder objects which require args to build
        private InitDef smInitDef = null;
        private ResourceContextDef smResourceCtx = null;
        private UserCredentials smUserCreds = null;

        //~ Constructors -------------------------------------------------------


        /**
         * Creates a new SMAgent object.
         */
        protected SMAgent() {
        }

        //~ Methods ------------------------------------------------------------


        /**
         * run agent initialization including overriding the configuration from
         * the properties file
         *
         * @return true if the initialization was successfull, through
         *         connection to the policy server
         *
         * @throws IOException bubble the exception if thrown by a method call
         *         to EncryptUtil.decrypt()
         * @throws IllegalArgumentException If the value of
         *         &lt;%node%&gt;_Agent SecretIsPlain is not either TRUE or
         *         FALSE
         */
        protected synchronized boolean doInit()
            throws IOException, IllegalArgumentException {
            boolean init = false;
            int status = -4;

            //server configuration
            smServer.serverIpAddress = smAddress;
            smServer.accountingPort = smAccPort;
            smServer.authenticationPort = smAuthentPort;
            smServer.authorizationPort = smAuthorizePort;
            smServer.timeout = smTimeout;
            smServer.connectionMax = smConnMax;
            smServer.connectionMin = smConnMin;
            smServer.connectionStep = smConnStep;

            //agent initialization
            if (secretIsPlain) {
                smInitDef = new InitDef(agentName, agentSecret, true, smServer);
                status = smAgent.init(smInitDef);
            } else if (!secretIsPlain) {
                smInitDef = new InitDef(agentName,
                        EncryptUtil.decrypt(agentSecret), true, smServer);
                status = smAgent.init(smInitDef);
            } else {
                IllegalArgumentException iae = new IllegalArgumentException(
                        "You must specify either \"TRUE\" or \"FALSE\""
                        + " for " + node
                        + "_AgentSecretPlain in the siteminder.properties"
                        + " file");
                throw iae;
            }

            if (status == 0) {
                init = true;
            }

            return init;
        }


        /**
         * DOCUMENT ME!
         */
        protected synchronized void doUnInit() {
            smAgent.unInit();
        }


        /**
         * run authorization sequence and set the user group for the UID
         * currently only four groups are supported:<br>
         * <code>ADMIN, SUPERUSER, USER, DEFAULT</code><br>
         * these correspond to the AD groups:<br>
         * <code>wdwmwadmin, wdwmwuser, wdwmwdefault, wdwmwguest</code><br>
         * DEFAULT(wdwmwguest) is the same as not being logged in at all
         *
         * @param uid User ID
         * @param pass User Password
         * @param req Request object from login servlet - used to pass
         *        information to siteminder
         * @param session session object from login servlet - used to pass
         *        information back to client
         *
         * @return whether or not the use has successfully logged in
         */
        protected synchronized boolean doAuth(String uid, String pass,
            HttpServletRequest req, HttpSession session) {
            boolean isAuth = false;
            int status = -4;

            smResourceCtx = new ResourceContextDef("", "",
                    req.getContextPath(), req.getMethod());

            if (smAgent.isProtected("", smResourceCtx, smRealm) == 1) {
                smUserCreds = new UserCredentials(uid, pass);
                status = smAgent.login(req.getRemoteAddr(), smResourceCtx,
                        smRealm, smUserCreds, smSession, smAttrs);
            }

            if (status == 1) {
                session.setAttribute(SM_ResourceContext, smResourceCtx);
                session.setAttribute(SM_Realm, smRealm);
                session.setAttribute(SM_Session, smSession);
                session.setAttribute(SM_Attrs, smAttrs);

                int iAttrs = smAttrs.getAttributeCount();

                for (int i = 0; i < iAttrs; i++) {
                    boolean bGroupSet = false;
                    Attribute a = smAttrs.getAttributeAt(i);
                    StringBuffer aVal = new StringBuffer();

                    for (int j = 0; j < a.value.length; j++) {
                        aVal.append((char)a.value[j]);
                    }

                    if (aVal.toString().equalsIgnoreCase(ADMIN)) {
                        session.setAttribute(USERGROUP, ADMIN);
                        bGroupSet = true;
                        isAuth = true;
                    } else if (aVal.toString().equalsIgnoreCase(SUPERUSER)) {
                        session.setAttribute(USERGROUP, SUPERUSER);
                        bGroupSet = true;
                        isAuth = true;
                    } else if (aVal.toString().equalsIgnoreCase(USER)) {
                        session.setAttribute(USERGROUP, USER);
                        bGroupSet = true;
                        isAuth = true;
                    } else if (aVal.toString().equalsIgnoreCase(DEFAULT)) {
                        session.setAttribute(USERGROUP, DEFAULT);
                        bGroupSet = true;
                        isAuth = true;
                    }

                    if (bGroupSet) {
                        break;
                    }
                }

                session.setAttribute(SM_InitDef, smInitDef);
            }

            smAgent.unInit();

            return isAuth;
        }
    }

    //~ Static variables/initializers ------------------------------------------

    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";
    private static final String LOGGED_IN = "LOGGED_IN";
    private static final String INDEX = "index.html";

    //~ Constructors -----------------------------------------------------------


    /**
     * Constructor for SMLoginServlet.
     */
    public SMLoginCommand() {
        super();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * does the work for the servlet gets the username and password from the
     * request creates an instance of the SiteMinder Agent (SMAgent) and
     * forwards the username, password, request, and session objects for
     * processing  forwards user back to index.html after successfull
     * processing.
     *
     * @param req The request object
     * @param res The response object
     *
     * @throws IOException DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    public String performTask(HttpServletRequest req, HttpServletResponse res)
    {
        String user = req.getParameter(USERNAME);
        String password = req.getParameter(PASSWORD);
        SMAgent sma = new SMAgent();

        javax.servlet.http.HttpSession session = req.getSession();
		try
		{
		    if ((user != null) && (password != null)) {
		        if (sma.doInit()) {
		            if (sma.doAuth(user, password, req, session)) {
	                    session.setAttribute (USERNAME, user);
		                session.setAttribute(LOGGED_IN, (new Boolean(true)));
		            }
		        }
		    }
		}
		catch (Exception e)
		{
			evl.sendException("Error Authorizing Site Minder Login Attempt.", 
				EventType.EXCEPTION, ErrorCode.SECURITY_EXCEPTION, e, this);
		}

		return INDEX;
    }
}
