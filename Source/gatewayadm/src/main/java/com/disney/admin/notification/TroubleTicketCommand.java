package com.disney.admin.notification;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.admin.AdminException;
import com.disney.admin.SharedAdminCommand;
import pvt.disney.dti.gateway.connection.ConnectionException;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.PrintStack;


/**
 * This servlet will service requests from the
 * <strong>TroubleTicket.jsp</strong> The JSP should contain issolated forms
 * for each object and action to be performed.<br>
 * It will also service requests from
 * <strong>TroubleTicketNotificationHistory.jsp</strong> The JSP should
 * contain a form for creating and submitting valid time-spans for history  queries.<br>
 * Finally the servlet will service requets from the
 * <strong>TroubleTicketThrottle.jsp</strong> This JSP should contain forms
 * for adding and deleting elements used for TTAgent throttling<br>
 *
 * @author Ric Alvarez
 */
public class TroubleTicketCommand extends SharedAdminCommand
{
    //~ Static variables/initializers ------------------------------------------

    //GLOBAL CONSTANTS FOR THIS SERVLET
    private static final String DEFAULT_TARGET_JSP = "TroubleTicket.jsp";
    private static final String HISTORY_TARGET_JSP = "TroubleTicketNotificationHistory.jsp";
    private static final String THROTTLING_JSP = "TroubleTicketThrottle.jsp";
    
    //GLOBAL VARIABLEs FOR THIS SERVLET
    private static Properties props = null;
    private static String dsName = null;

    //allow for configuration based refreshes, default is never
    private static int refresh = 0;
    private static long lastRefresh = 0;

    //~ Methods ----------------------------------------------------------------

    /**
     * Constructor for TroubleTicketCommand
     */
    public TroubleTicketCommand()
    {
    	super();
    	
	    ResourceBundle rb = ResourceLoader.getResourceBundle("notification");
	    props = ResourceLoader.convertResourceBundleToProperties(rb);

        dsName = props.getProperty("TT_DATA_SOURCE");
        refresh = Integer.parseInt(props.getProperty("ADMIN_REFRESH", "0"));
        lastRefresh = System.currentTimeMillis();
    }

    /**
     * parse through the parameter list in this request and decide what to do
     * the calling page will contain  isolated forms for each type and action
     * therefore there is no need to continue processing once i  determing the
     * action.   Let all exceptions bubble up to the user.
     *
     * @param request HttpServletRequest - the request object passed to this
     *        servlet
     * @param response HttpServletResponse - the response object for this
     *        servlet
     *
     * @throws IllegalArgumentException - if thrown by another method in this
     *         servlet
     * @throws SQLException - if thrown by another method in this servlet
     * @throws ConnectionException - if thrown by another method in this
     *         servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
	{
        //before i do any work, refresh my properties if i have to
        if (refresh > 0) {
            if ((lastRefresh + ((long)(refresh * 1000))) < System
                .currentTimeMillis()) {
                //clear everything out
                props = null;
                dsName = null;

                //repopulate the fields
        	    ResourceBundle rb = ResourceLoader.getResourceBundle("notification");
        	    props = ResourceLoader.convertResourceBundleToProperties(rb);

        	    dsName = props.getProperty("TT_DATA_SOURCE");
                refresh = Integer.parseInt(props.getProperty("ADMIN_REFRESH", "0"));
                lastRefresh = System.currentTimeMillis();
            }
        }

		try
		{
	        //check for the filter's input.
	        if (((request.getAttribute("CONTINUE") != null)
	            && ((Boolean)request.getAttribute("CONTINUE")).booleanValue())
	            || (request.getAttribute("CONTINUE") == null)) {
	            //get the names of all the parameters from the request
	            Enumeration atts = request.getParameterNames();
	
	            // parse through the parameters looking for an action
	            while (atts.hasMoreElements()) {
	                String thisAtt = atts.nextElement().toString();
	
	                //determine whether i should be adding or deleting
	                if (thisAtt.indexOf("DELETE") != -1) {
	                    //forward the entire parameter list, and the type to the delete method
	                    return delete(request.getParameterNames(), thisAtt.substring(6));
	                } else if (thisAtt.indexOf("ADD") != -1) {
	                    //forward the type and the entire request object to the add method 
	                    return add(thisAtt.substring(3), request);
	                } else if (thisAtt.indexOf("HISTORY") != -1) {
	                    return history(request, response);
	                } else if (thisAtt.indexOf("view") != -1) {
	                    if (request.getParameter("view").equalsIgnoreCase("ttadmin")) {
	                        return DEFAULT_TARGET_JSP;
	                    } else if (request.getParameter("view").equalsIgnoreCase("history")) {
	                        return HISTORY_TARGET_JSP;
	                    } else if (request.getParameter("view").equalsIgnoreCase("throttling")) {
	                        return THROTTLING_JSP;
	                    }
	                }
	            }
	        }
		}
		catch (Exception e)
		{
			evl.sendException("Error processing Trouble Ticket Admin Command:", 
				EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, this);
            request.setAttribute ("printStack", PrintStack.getTraceString (e));
		}
        
        return DEFAULT_TARGET_JSP;
    }


    /**
     * Create a Vector of TroubleTicketHistoryBeans that occured in the
     * database between the date range provided Add the vector to the request
     * and forward it to the client page
     *
     * @param request HttpServletRequest - the request object passed to this
     *        servlet
     * @param response HttpServletResponse - the response object for this
     *        servlet
     *
     * @throws IllegalArgumentException - if the start date is not set or is
     *         null
     * @throws SQLException - if thrown by the TroubleTicketHistoryBean while
     *         getting objects from the database
     * @throws ConnectionException - if thrown by the TroubleTicketHistoryBean
     *         while connecting to the database
     */
    private String history(HttpServletRequest request, HttpServletResponse response)
        throws IllegalArgumentException, SQLException, ConnectionException, AdminException 
    {
        //create an instance of the TroubleTicketHistoryBean to perform my work
        TroubleTicketHistoryBean tthb = new TroubleTicketHistoryBean();

        //Get the query information from properties
        String query = props.getProperty(TroubleTicketHistoryBean.TABLE + "_GETALLBYRANGE");

        //populate my arguments - start and end time from the request
        String start = request.getParameter("histStart");
        String end = request.getParameter("histEnd");

        //if the start time was not set, throw an exception, I don't want to get everything here
        if ((start == null) || start.trim().equalsIgnoreCase("")) {
            IllegalArgumentException iae = new IllegalArgumentException(
                    "The histStart parameter passed was not valid. [histStart="
                    + start + "]");
            throw (iae);
        } else {
            //create and populate a vector with the requested data
            Vector vHistory = null;
            vHistory = tthb.getAllForRange(dsName, query, start, end);

            // place the Vector in the request
            request.setAttribute("vHistory", vHistory);

            // send the request back to the client
            return HISTORY_TARGET_JSP;
        }
    }


    /**
     * The primary key for each item to be deleted will be derived by removing
     * the value of type from  the value of each attribute name in the items
     * enumeration.  For example: if type is <strong>groups</strong> and the
     * value for an element in items is <strong>groups1</strong>  then the
     * group with the primary key of <strong>1</strong> will be deleted.  Let
     * all exceptions bubble up to the user
     *
     * @param items Enumeration - enumeration of attribute names from servlet
     *        request
     * @param type String - the type of items to be deleted
     *
     * @throws IllegalArgumentException - if the type passed is not explicitly
     *         supported here
     * @throws SQLException - if any of the DataAccessBeans throw it
     * @throws ConnectionException - if any of the DataAccessBeans throw it
     */
    private String delete(Enumeration items, String type)
        throws IllegalArgumentException, SQLException, ConnectionException, AdminException 
    {
        int id = 0;

        //parse through the items
        while (items.hasMoreElements()) {
            String thisItem = items.nextElement().toString();

            //ignore the action item parse all others
            if (thisItem.indexOf("DELETE") == -1) {
                if (thisItem.indexOf(type) != -1) {
                    //get the primary key from the string value of this element of the items enumeration
                    id = (Integer.valueOf(thisItem.substring(type.length())))
                        .intValue();

                    //decide which object to call
                    if (type.trim().equalsIgnoreCase("groups")) {
                        //create an instance of the TroubleTicketGroupBean so i can use it's methods
                        TroubleTicketGroupBean ttgb = new TroubleTicketGroupBean();

                        //get the query for deleting an instance of class TroubleTicketGroupBean from the database
                        String query = props.getProperty(TroubleTicketGroupBean.TABLE
                                + "_DELETEONE");

                        //use my TroubleTicketGroupBean object to call the deleteMe() method
                        ttgb.deleteMe(dsName, query, id, TroubleTicketGroupBean.TABLE);

                        //if i didn't catch an exception, forward the user to TroubleTicket.jsp
                        return DEFAULT_TARGET_JSP;
                    } else if (type.trim().equalsIgnoreCase("types")) {
                        //exactly as above for TroubleTicketTypeBean
                        TroubleTicketTypeBean tttb = new TroubleTicketTypeBean();
                        String query = props.getProperty(TroubleTicketTypeBean.TABLE
                                + "_DELETEONE");
                        tttb.deleteMe(dsName, query, id, TroubleTicketTypeBean.TABLE);
                        return DEFAULT_TARGET_JSP;
                    } else if (type.trim().equalsIgnoreCase("keys")) {
                        //exactly as above for TroubleTicketKeyBean
                        TroubleTicketKeyBean ttkb = new TroubleTicketKeyBean();
                        String query = props.getProperty(TroubleTicketTypeBean.TABLE
                                + "_DELETEONE");
                        ttkb.deleteMe(dsName, query, id, TroubleTicketTypeBean.TABLE);
                        return DEFAULT_TARGET_JSP;
                    } else if (type.trim().equalsIgnoreCase("throt")) {
                        //exactly as above for TroubleTicketKeyTypeXRefBean
                        TroubleTicketKeyTypeXRefBean ttktxrb = new TroubleTicketKeyTypeXRefBean();
                        String query = props.getProperty(TroubleTicketKeyTypeXRefBean.TABLE
                                + "_DELETEONE");
                        ttktxrb.deleteMe(dsName, query, id, TroubleTicketKeyTypeXRefBean.TABLE);
                        return THROTTLING_JSP;
                    } else if (type.trim().equalsIgnoreCase("keygroup")) {
                        //exactly as above for TroubleTicketKeyGroupXRefBean
                        TroubleTicketKeyGroupXRefBean ttkgxrb = new TroubleTicketKeyGroupXRefBean();
                        String query = props.getProperty(TroubleTicketKeyGroupXRefBean.TABLE
                                + "_DELETEONE");
                        ttkgxrb.deleteMe(dsName, query, id, TroubleTicketKeyGroupXRefBean.TABLE);
                        return DEFAULT_TARGET_JSP;
                    } else {
                        // throw an exception if i'm trying to work on an object type that i don't know how to handle
                        // i should never reach this unless someone else is calling me 'illegally'
                        throw (new IllegalArgumentException("The type " + type
                            + " is not supported by the class "
                            + this.getClass()));
                    }
                }
            }
        }
        
        return DEFAULT_TARGET_JSP;
    }


    /**
     * Add a new instance of the type to the database with the values in items
     * Let all exceptions bubble up to the user
     *
     * @param type String - object type to add to the database, must have an
     *        associated access bean
     * @param req HttpServletRequest - request object with values to be added
     *
     * @throws IllegalArgumentException - if the type passed is not explicitly
     *         supported here
     * @throws SQLException - if any of the DataAccessBeans throw it
     * @throws ConnectionException - if any of the DataAccessBeans throw it
     */
    private String add(String type, HttpServletRequest req)
        throws IllegalArgumentException, SQLException, ConnectionException, 
            AdminException {
        if (type.trim().equalsIgnoreCase("group")) {
            // create an instance of TroubleTicketGroupBean object so i can populate its fields and used it's methods
            TroubleTicketGroupBean ttgb = new TroubleTicketGroupBean();

            //get the query string for the insert for this type
            String query = props.getProperty(TroubleTicketGroupBean.TABLE + "_INSERTNEW");

            //populate the fields for this type
            ttgb.setGroupName(req.getParameter("groupName"));
            ttgb.setDescription(req.getParameter("groupDescription"));

            //call this type's insert method. this object will be inserted into the database with it's current values
            ttgb.insertNew(dsName, query);

            //if i have not encountered an exception, forward the user back the the TroubleTicket.jsp        	 
            return DEFAULT_TARGET_JSP;
        } else if (type.trim().equalsIgnoreCase("type")) {
            // same as above for TroubleTicketTypeBean
            TroubleTicketTypeBean tttb = new TroubleTicketTypeBean();
            String query = props.getProperty(TroubleTicketTypeBean.TABLE + "_INSERTNEW");
            tttb.setName(req.getParameter("typeName"));
            tttb.setValue(req.getParameter("typeValue"));
            tttb.insertNew(dsName, query);
            return DEFAULT_TARGET_JSP;
        } else if (type.trim().equalsIgnoreCase("key")) {
            // same as above for TroubleTicketKeyBean
            TroubleTicketKeyBean ttkb = new TroubleTicketKeyBean();
            String query = props.getProperty(TroubleTicketKeyBean.TABLE + "_INSERTNEW");
            ttkb.setCompId(req.getParameter("keyComp"));
            ttkb.setErrCode(req.getParameter("keyErrCd"));
            ttkb.setEvent(req.getParameter("keyEvnt"));
            ttkb.insertNew(dsName, query);
            return DEFAULT_TARGET_JSP;
        } else if (type.trim().equalsIgnoreCase("throt")) {
            // same as above for TroubleTicketKeyTypeXRefBean
            TroubleTicketKeyTypeXRefBean ttktxrb = new TroubleTicketKeyTypeXRefBean();
            String query = props.getProperty(TroubleTicketKeyTypeXRefBean.TABLE + "_INSERTNEW");
            ttktxrb.setKeyId(Integer.parseInt(req.getParameter("key")));
            ttktxrb.setTypeId(Integer.parseInt(req.getParameter("type")));
            ttktxrb.setEaiNode(req.getParameter("node"));
            ttktxrb.setSvcType(req.getParameter("svc"));
            ttktxrb.setMaxAllowed(Integer.parseInt(req.getParameter("max")));
            ttktxrb.setInterval(Integer.parseInt(req.getParameter("int")));
            ttktxrb.setFrequency(Integer.parseInt(req.getParameter("freq")));
            ttktxrb.insertNew(dsName, query);
            return THROTTLING_JSP;
        } else if (type.trim().equalsIgnoreCase("keygroup")) {
            TroubleTicketKeyGroupXRefBean ttkgxrb = new TroubleTicketKeyGroupXRefBean();
            String query = props.getProperty(TroubleTicketKeyGroupXRefBean.TABLE + "_INSERTNEW");
            ttkgxrb.setKeyId(Integer.parseInt(req.getParameter("key")));
            ttkgxrb.setService(req.getParameter("svc"));
            ttkgxrb.setGroupId(Integer.parseInt(req.getParameter("group")));
            ttkgxrb.insertNew(dsName, query);
            return DEFAULT_TARGET_JSP;
        } else {
            // throw an exception if i'm trying to work on an object type that i don't know how to handle
            // i should never reach this unless someone else is calling me 'illegally'
            throw (new IllegalArgumentException("The type " + type
                + " is not supported by the class " + this.getClass()));
        }
    }
}
