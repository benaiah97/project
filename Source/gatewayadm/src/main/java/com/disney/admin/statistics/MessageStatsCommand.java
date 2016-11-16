package com.disney.admin.statistics;

import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.admin.SharedAdminCommand;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.PrintStack;

//import com.disney.msgstats.dao.MessageStats;
import pvt.disney.dti.gateway.connection.MessageStats;

//import com.disney.msgstats.dao.MessageStatsKey;
import pvt.disney.dti.gateway.connection.MessageStatsKey;


/**
 * Message Statistics Admin Command
 * 
 * @version 1.0
 * @author fav2
 */
public class MessageStatsCommand extends SharedAdminCommand
{
    //~ Static variables/initializers --------------------------------------------------------------

    private static final String DEFAULT_MSG_STATS_URL = "MessageStats";

    //~ Constructors -------------------------------------------------------------------------------

    /**
     * Constructor for MessageStatsServlet
     */
    public MessageStatsCommand()
    {
        super ();
    }

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static String getMessageStatsUrl()
    {
	    ResourceBundle rb = ResourceLoader.getResourceBundle("admin");
	    Properties props = ResourceLoader.convertResourceBundleToProperties(rb);

	    String node = AbstractInitializer.getNodeName();

        String msgStatsUrl = props.getProperty (node + "_MSG_STATS_URL", null);

        if (msgStatsUrl == null)
        {
            msgStatsUrl = props.getProperty ("MSG_STATS_URL", DEFAULT_MSG_STATS_URL);
        }

        return msgStatsUrl;
    }

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        String msgName = null;
        String forwardTo = "MessageStatsMain.jsp";
        try
        {
            String action = request.getParameter ("ACTION");
            if (action == null)
            {
                action = "";
            }

            if (action.equalsIgnoreCase ("QUERY"))
            {
                forwardTo = "MessageStatsResults.jsp";

                String msgId = request.getParameter ("MSG_ID");

                if ((msgId == null) || (msgId.length () == 0) || (msgId.equalsIgnoreCase ("null")))
                {
                    msgId = null;
                }

                String convId = request.getParameter ("CONV_ID");

                if ((convId == null) || (convId.length () == 0)
                    || (convId.equalsIgnoreCase ("null")))
                {
                    convId = null;
                }

                MessageStatsKey key = new MessageStatsKey();
                key.setMessageIdFilter (msgId);
                key.setConversationIdFilter (convId);

                MessageStats stats = key.getMessageStats();
                MessageStatsBean bean = new MessageStatsBean(stats);

                request.setAttribute("bean", bean);  
			}
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
            request.setAttribute ("errorMsg", th.getMessage ());
            request.setAttribute ("printStack", PrintStack.getTraceString (th));
        }
        finally
        {
        	return forwardTo;
        }
    }
}