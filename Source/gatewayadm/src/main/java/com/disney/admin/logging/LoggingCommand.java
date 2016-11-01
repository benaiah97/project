package com.disney.admin.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.disney.admin.SharedAdminCommand;
import com.disney.admin.SystemShutdownListener;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.xml.DOMHelper;

/**
 * Logging Admin Command
 *
 * @version 1.0
 * @author fav2
 */
public class LoggingCommand extends SharedAdminCommand implements SystemShutdownListener
{
	private static final String LOG4J_CONFIG_FILE = "log4j.xml";
	private static final String LOG_LEVEL_XPATH = "//*/param[@name=\"logLevel\"]";
	private static final String LOG_OBJECTS_XPATH = "//*/param[@name=\"logObjects\"]";

	/**
	 * @see com.wdw.eai.admin.SystemShutdownListener#onSystemShutdown()
	 */
	public void onSystemShutdown()
	{
		LogManager.shutdown();
		System.out.println("Shutdown Log4j Appenders.");
	}

    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        //String logMgrName = Initializer.PROPERTIES_FILE_NAME;
        String logLevel = "";
        String logObjects = "";

        try
        {
            String action = request.getParameter ("ACTION");
            if (action == null)
            {
                action = "";
            }

            evl.sendEvent ("Action received: " + action, EventType.DEBUG, this);
            
			AbstractInitializer init = AbstractInitializer.getInitializer();
			String fullLog4jFileName = init.getBASE_DIR() + init.getFileSep() + LOG4J_CONFIG_FILE;
			Document dom = DOMHelper.createDomFromFile(fullLog4jFileName);
			Node logLevelNode = DOMHelper.selectSingleNode(dom, LOG_LEVEL_XPATH);
			Node logObjectsNode = DOMHelper.selectSingleNode(dom, LOG_OBJECTS_XPATH);
			
            logLevel = logLevelNode.getAttributes().getNamedItem("value").getNodeValue();
            logObjects = logObjectsNode.getAttributes().getNamedItem("value").getNodeValue();
            
            if (action.equalsIgnoreCase ("UPDATE_LOG_LEVEL"))
            {
                // Update existing logging objects.
                try
                {
                    logLevel = request.getParameter ("LOG_LEVEL");
                    logObjects = request.getParameter ("LOG_OBJECTS");
                }
                catch (Throwable th)
                {
                    evl.sendException ("Error getting log level update parameters.", 
                                       EventType.WARN, ErrorCode.GENERAL_EXCEPTION, th, this);
                    request.setAttribute ("throwable", th);
                }

				logLevelNode.getAttributes().getNamedItem("value").setNodeValue(logLevel);
				logObjectsNode.getAttributes().getNamedItem("value").setNodeValue(logObjects);
				
				try
				{
					BufferedWriter out = new BufferedWriter(new FileWriter(fullLog4jFileName));
					out.write(DOMHelper.toXml(dom));
					out.flush();
					out.close();
	
	                evl.sendEvent ("Set Logging Level to : " + logLevel + " : " + logObjects, 
                         EventType.AUDIT, this);
				}
				catch (Exception e)
				{
					evl.sendException("Error writing Log4j configuration file: " + fullLog4jFileName, 
						EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, e, this);
				}
            }
            
            request.getSession(true).setAttribute("LOG4J_CONFIG_XML", DOMHelper.toXml(dom));
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
            request.setAttribute ("throwable", th);
        }
        finally
        {
            request.setAttribute ("logLevel", logLevel);
            request.setAttribute ("logObjects", logObjects);
            return "LoggingMain.jsp";
        }
    }

    
    public class Level {
    	
    	public com.disney.admin.logging.LoggingCommand.Level next() {
    		return null;
    	}
    	
    	public com.disney.admin.logging.LoggingCommand.Level getName() {
    		return null;
    	}
    	public com.disney.admin.logging.LoggingCommand.Level getValue() {
    		return null;
    	}
    	public com.disney.admin.logging.LoggingCommand.Level getClassName() {
    		return null;
    	}

    	
    	
    	
    }
    
    
    
    
    
    
    
}