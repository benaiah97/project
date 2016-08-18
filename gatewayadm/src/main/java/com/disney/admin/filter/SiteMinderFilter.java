package com.disney.admin.filter;

import netegrity.siteminder.javaagent.AgentAPI;
import netegrity.siteminder.javaagent.AttributeList;
import netegrity.siteminder.javaagent.InitDef;
import netegrity.siteminder.javaagent.RealmDef;
import netegrity.siteminder.javaagent.ResourceContextDef;
import netegrity.siteminder.javaagent.SessionDef;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import java.io.IOException;

/**
 * The SiteMinderFilter works like the simple login filter Except that it will
 * validate a users session against siteminder to make sure  that it is still
 * active. This Filter does not validate any groups and should only be used
 * for <i>public</i> Servlets.
 *
 * @author rra3
 */
public abstract class SiteMinderFilter implements ServletFilterIF {
	//~ Static variables/initializers ------------------------------------------

	protected static final EventLogger EVL = EventLogger.getLogger("ADMIN");
	protected static final String LOGGED_IN = "LOGGED_IN";
	protected static final String USERGROUP = "USERGROUP";
	protected static final String DEFAULT = "DEFAULT";
	protected static final String CONTINUE = "CONTINUE";
	protected static final String WELCOME_JSP = "welcome.jsp";
	protected static final String RESOURCE_CTX = "SM_ResourceContext";
	protected static final String REALM = "SM_Realm";
	protected static final String SESSION = "SM_Session";
	protected static final String ATTRS = "SM_Attrs";
	protected static final String INITDEF = "SM_InitDef";

	//~ Constructors -----------------------------------------------------------

	/**
	 * Constructor for SiteMinderFilter.
	 */
	public SiteMinderFilter() {
		super();
	}

	//~ Methods ----------------------------------------------------------------

	/**
	 * @see com.wdw.eai.admin.filter.ServletFilterIF#destroy()
	 */
	public void destroy() {
	}

	/**
	 * works just like the simple login filter, except that it will check your
	 * session against siteminder, must be used with the  SMLoginServlet
	 * otherwise you won't see anything ever.
	 *
	 * @see com.wdw.eai.admin.filter.ServletFilterIF#doFilter(HttpServletRequest,
	 *      HttpServletResponse, ServletFilterChain)
	 */
	public abstract void doFilter(
		HttpServletRequest request,
		HttpServletResponse response,
		ServletFilterChain chain)
		throws IOException, ServletException;

	/**
	 * Checks if the user is still logged in by looking at the LOGGED_IN
	 * attribut in the session
	 *
	 * @param request
	 * @param response
	 * @param smStatus
	 *
	 * @return true if the user is still logged in, otherwise false
	 *
	 * @throws ServletException
	 * @throws IOException
	 */
	protected boolean checkLoggedIn(
		HttpServletRequest request,
		HttpServletResponse response,
		int smStatus)
		throws ServletException, IOException {
		Boolean loggedIn =
			(Boolean) request.getSession().getAttribute(LOGGED_IN);

		if ((loggedIn == null)
			|| (!(loggedIn.booleanValue()))
			|| (smStatus != 1)) {
			try {
				EVL.sendEvent(
					"Not Logged in.. forwarding to Siteminder...",
					EventType.INFO,
					this);

				RequestDispatcher rd =
					request.getRequestDispatcher(WELCOME_JSP);
				rd.forward(request, response);
			} catch (Throwable th) {
				EVL.sendEvent(
					"Error forwarding login: " + th.toString(),
					EventType.EXCEPTION,
					this);
			}

			return false;
		} else {
			/* SiteMinder agent management will do nice things like expire the
			 * session, etc.
			 */
			return true;
		}
	}

	/**
	 * Checks to see if the user's SiteMinder session is still active, and
	 * updates  the session activity timers.   If the user has been inactive
	 * too long, the SiteMinder session is closed  and the browser session is
	 * invalidated
	 *
	 * @param request
	 *
	 * @return 1 if everything is good, and the uuser can continue to work with
	 *         this session. any other return signifies failure
	 */
	protected int checkAuthed(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		AgentAPI smAgent = new AgentAPI();
		ResourceContextDef smResourceCtx =
			(ResourceContextDef) request.getSession().getAttribute(
				RESOURCE_CTX);
		RealmDef smRealm = (RealmDef) request.getSession().getAttribute(REALM);
		SessionDef smSession =
			(SessionDef) request.getSession().getAttribute(SESSION);
		AttributeList smAttrs =
			(AttributeList) request.getSession().getAttribute(ATTRS);
		InitDef smInitDef =
			(InitDef) request.getSession().getAttribute(INITDEF);
		int smStatus = -4;

		if (smInitDef != null) {
			smAgent.init(smInitDef);

			if ((smResourceCtx != null)
				&& (smRealm != null)
				&& (smSession != null)
				&& (smAttrs != null)) {
				smAgent.authorize(
					"",
					"",
					smResourceCtx,
					smRealm,
					smSession,
					smAttrs);
				String sessionID = smSession.id;

				if (sessionID == null || sessionID.length() == 0) {
					smAgent.logout("", smSession);
					request.getSession().setAttribute(
						LOGGED_IN,
						new Boolean(false));
					request.getSession().setAttribute(USERGROUP, DEFAULT);
					RequestDispatcher rd =
						request.getRequestDispatcher(WELCOME_JSP);
                        rd.forward(request, response);
				} else {
					smStatus = 1;
				}
			}
		}

		return smStatus;
	}
}
