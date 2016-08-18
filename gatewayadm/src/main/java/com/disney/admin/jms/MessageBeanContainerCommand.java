package com.disney.admin.jms;

import com.disney.jms.JmsConnectionPool;
import com.disney.jms.JmsException;
import com.disney.jms.MessageBeanContainerFacade;
import com.disney.jms.MessageBeanContainerFacadeFactory;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.admin.SharedAdminCommand;
import com.disney.admin.SystemShutdownListener;
import com.disney.admin.SystemStartupListener;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Starts the Message Bean Container listeners as configured in jms.properties CONTAINERS
 * and administers them.
 * 
 * @version 1.0
 * @author fav2
 */
public class MessageBeanContainerCommand extends SharedAdminCommand 
	implements SystemStartupListener, SystemShutdownListener
{
    /**
     * Initializes the message bean container listeners
	 * @see com.wdw.eai.admin.SystemStartupListener#onSystemStartup()
	 */
	public void onSystemStartup()
	{
        try
        {
            JmsConnectionPool.getInstance().open();
        }
        catch (Exception e)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, e, this);
        }

        AbstractInitializer init = AbstractInitializer.getInitializer();
        Properties props = init.getProps ("jms.properties");
        String containerListStr = props.getProperty (init.getAppName() + "_CONTAINERS", null);

        if (containerListStr == null)
        {
            containerListStr = props.getProperty ("CONTAINERS", "");
        }

        StringTokenizer containerList = new StringTokenizer(containerListStr, ",");

        while (containerList.hasMoreTokens ())
        {
            String containerName = containerList.nextToken ();
            startContainerListener (containerName);
        }
    }

    /**
     * Stops the message bean container listeners
	 * @see com.wdw.eai.admin.SystemShutdownListener#onSystemShutdown()
	 */
	public void onSystemShutdown()
	{
    }

    /**
     * Starts an individual listener for a named message bean container
     *
     * @param containerName String container Name
     */
    private void startContainerListener(String containerName)
    {
        try
        {
            MessageBeanContainerFacade container = 
                    MessageBeanContainerFacadeFactory.getMessageBeanContainerFacade (containerName);

            if (container.isSubscribing ())
            {
                evl.sendEvent ("Message Bean Container Listener Already Started: " + containerName, 
                               EventType.INFO, this);
            }
            else
            {
                container.start ();
                evl.sendEvent ("Message Bean Container Listener Started: " + containerName, 
                               EventType.INFO, this);
            }
        }
        catch (JmsException e)
        {
            evl.sendException ("Error starting Message Bean Container: " + containerName, 
                               EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, e, this);
        }
    }

    /**
     * Restarts the named Message Bean Container listener
     *
     * @param containerName String container name
     */
    private void resetContainerListener(String containerName)
    {
        try
        {
            MessageBeanContainerFacade container = 
                    MessageBeanContainerFacadeFactory.getMessageBeanContainerFacade (containerName);
            container.start ();
            evl.sendEvent ("Message Bean Container Listener Reset: " + containerName, 
                           EventType.INFO, this);
        }
        catch (JmsException e)
        {
            evl.sendException ("Error reseting Message Bean Container: " + containerName, 
                               EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, e, this);
        }
    }

    /**
     * Stops the named Message Bean Container listener
     *
     * @param containerName String container name
     */
    private void stopContainerListener(String containerName)
    {
        try
        {
            MessageBeanContainerFacade container = 
                    MessageBeanContainerFacadeFactory.getMessageBeanContainerFacade (containerName);

            if (container.isSubscribing ())
            {
                container.stop ();
                evl.sendEvent ("Message Bean Container Listener Stopped: " + containerName, 
                               EventType.INFO, this);
            }
            else
            {
                evl.sendEvent ("Message Bean Container Listener Already Stopped: " + containerName, 
                               EventType.INFO, this);
            }
        }
        catch (JmsException e)
        {
            evl.sendException ("Error stopping Message Bean Container: " + containerName, 
                               EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, e, this);
        }
    }

    /**
     * Process incoming requests for information
     * 
     * @param request HttpServletRequest that encapsulates the request to the servlet
     * @param response HttpServletResponse that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String action = request.getParameter ("ACTION");
            if (action == null)
            {
                action = "";
            }

            evl.sendEvent ("Received ACTION=\"" + action + "\".", EventType.DEBUG, this);

            String containerName = request.getParameter ("ContainerName");
            try
            {
                if (action.equalsIgnoreCase ("STOP"))
                {
                    this.stopContainerListener (containerName);
                }
                else if (action.equalsIgnoreCase ("START"))
                {
                    this.startContainerListener (containerName);
                }
                else if (action.equalsIgnoreCase ("RESET"))
                {
                    this.resetContainerListener (containerName);
                }
            }
            catch (Throwable th)
            {
                evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
                request.setAttribute ("throwable", th);
            }
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
        }
        
        return "MessageBeanContainerMain.jsp";
    }

}