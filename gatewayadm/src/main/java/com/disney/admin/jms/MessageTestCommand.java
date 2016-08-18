package com.disney.admin.jms;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.disney.admin.SharedAdminCommand;
import com.disney.jms.JmsReplyReader;
import com.disney.jms.JmsWriter;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.Converter;
import com.disney.util.GUIDFactory;
import com.disney.util.PrintStack;
import com.disney.xml.DOMHelper;


/**
 * Test Message Admin Command used to send Test Messages to JMS Destinations and show the response.
 *
 * @version 2.0
 * @author fav2
 */
public class MessageTestCommand extends SharedAdminCommand
{
    //~ Instance variables -------------------------------------------------------------------------

    protected HashMap allMsgs = null;
    protected java.io.File testMsgDir = null;
    protected String fileSep = (String)System.getProperties ().get ("file.separator");

    private ArrayList allJms = null;
	private final static String MESSAGE_DATA_XPATH = "//*/MessageData";
	private final static String CONVERSATION_ID_XPATH = "//*/ConversationId/text()";
	
    /**
     * Process incoming requests for information
     * 
     * @param request Object that encapsulates the request to the servlet
     * @param response Object that encapsulates the response from the servlet
     */
    public String performTask(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String action = request.getParameter ("ACTION");
            String jmsDefName = request.getParameter ("JMS_DEF");
            String timeoutStr = request.getParameter ("TIMEOUT");

            if ((timeoutStr == null) || (timeoutStr.length () == 0))
            {
                timeoutStr = "90";
            }

            String currentDir = request.getParameter ("CURRENT_DIR");
            String currentMsg = request.getParameter ("CURRENT_MSG");
            String fileToRead = request.getParameter ("FILE_TO_READ");
            evl.sendEvent ("Action received: " + action, EventType.DEBUG, this);

            if ((action == null) || (action.equals ("")) || (action.equalsIgnoreCase ("REFRESH")))
            {
                currentDir = "." + AbstractInitializer.getInitializer().getFileSep () + "Typical";
                this.allMsgs = this.buildFileList ();

                if (!(allMsgs.containsKey (currentDir)))
                {
                    Iterator it = allMsgs.keySet ().iterator ();

                    if (it.hasNext ())
                    {
                        currentDir = (String)it.next ();
                    }
                }

                this.allJms = new ArrayList();

                Properties jmsProps = AbstractInitializer.getInitializer().getProps ("jms.properties");
                Enumeration allPropNames = jmsProps.propertyNames ();

                while (allPropNames.hasMoreElements ())
                {
                    String thisPropName = ((String)allPropNames.nextElement ()).trim ();

                    if ((thisPropName.startsWith ("JMS_")) && (thisPropName.endsWith ("_TYPE")))
                    {
                        int typeStart = thisPropName.indexOf ("_TYPE");
                        String nextJmsDefName = thisPropName.substring (4, typeStart);
                        this.allJms.add (nextJmsDefName);
                    }
                }
                
                if (jmsDefName == null)
                {
                	jmsDefName = jmsProps.getProperty("DEFAULT_JMS_DEF", null);
                }
            }
            else if (action.equalsIgnoreCase ("READ_MSG"))
            {
                currentMsg = "";

                java.io.File thisDir = new java.io.File(this.testMsgDir, currentDir);

                if ((thisDir != null) && (thisDir.isDirectory ()))
                {
                    java.io.File inFile = new java.io.File(thisDir, fileToRead);
                    java.io.BufferedReader in = new java.io.BufferedReader(
                                                        new java.io.FileReader(inFile));
                    String thisLine = null;

                    while ((thisLine = in.readLine ()) != null)
                    {
                        currentMsg += (thisLine + "\n");
                    }

                    in.close ();
                }
                else
                {
                    evl.sendEvent ("Test Message Directory not available: "
                                   + testMsgDir.getAbsolutePath (), EventType.WARN, this);
                }
            }
            else if (action.equalsIgnoreCase ("SEND_MSG"))
            {
            	String uniqueId = null;
                try
                {
                    // Try to modify the MessageId
                    Document dom = DOMHelper.createDomFromString(currentMsg);
                    Element msgData = (Element)DOMHelper.selectSingleNode(dom, MESSAGE_DATA_XPATH);
                    if (msgData != null)
                    {
	                    uniqueId = GUIDFactory.createGUID (this);
	                    msgData.setAttribute("MessageId", uniqueId);
	                    currentMsg = DOMHelper.toXml(dom);
                    }
                }
                catch (Throwable th)
                {
                    evl.sendEvent ("Unable to modify the Message Id of test data.."
                                   + th.toString (), EventType.DEBUG, this);
                }

                try
                {
                    long timeout = Long.parseLong (timeoutStr) * 1000;
                    JmsWriter jmsWriteBoy = new JmsWriter(jmsDefName);
                    javax.jms.TextMessage reqMsg = jmsWriteBoy.createTextMessage (currentMsg);
                    evl.sendEvent ("Sending Test Message: " + reqMsg.toString (), EventType.DEBUG, 
                                   this);

                    long startTime = System.currentTimeMillis ();
                    JmsReplyReader replyReader = jmsWriteBoy.sendMessageCreateReplyReader (reqMsg);
                    evl.sendEvent ("Sent Test Message: " + reqMsg.toString (), EventType.DEBUG, 
                                   this);

                    javax.jms.Message rspMsg = replyReader.readReply (timeout);
                    long endTime = System.currentTimeMillis ();

                    request.setAttribute ("elapsedTime", 
                                          new Double((endTime - startTime) / 1000.000).toString ()
                                          + " seconds.");
                    request.setAttribute ("startTime", 
                                          Converter.printISOTimeMSOnly (
                                                  new java.util.Date(startTime)));
                    request.setAttribute ("endTime", 
                                          Converter.printISOTimeMSOnly (new java.util.Date(endTime)));

                    if (rspMsg == null)
                    {
                        request.setAttribute ("responseMessage", "Timed out waiting for response.");
                        if (uniqueId != null)
                        {
	                        request.setAttribute ("messageId", uniqueId);
                        }
                    }
                    else
                    {
                        if (rspMsg instanceof javax.jms.TextMessage)
                        {
                            String rspXml = ((javax.jms.TextMessage)rspMsg).getText ();
                            request.setAttribute ("responseMessage", rspXml);

                            // try to get the message id and conversation id..
                            try
                            {
			                    // Try to modify the MessageId
			                    Document dom = DOMHelper.createDomFromString(currentMsg);
			                    Element msgData = (Element)DOMHelper.selectSingleNode(dom, MESSAGE_DATA_XPATH);
			                    if (msgData != null)
			                    {
			                    	String messageId = msgData.getAttribute("MessageId");
	                                request.setAttribute ("messageId", messageId);
			                    }

			                    Node convIdElem = DOMHelper.selectSingleNode(dom, CONVERSATION_ID_XPATH);
			                    if (convIdElem != null)
			                    {
			                    	String conversationId = convIdElem.getNodeValue();
	                                request.setAttribute ("conversationId", conversationId);
			                    }
                            }
                            catch (Throwable th)
                            {
                                // Ignore..
                            }
                        }
                        else
                        {
                            request.setAttribute ("responseMessage", 
                                                  "Response not of type: TextMessage.\n"
                                                  + rspMsg.toString ());
                        }
                    }
                }
                catch (Throwable th)
                {
                    evl.sendException (EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, th, this);
                    request.setAttribute ("responseMessage", PrintStack.getTraceString (th));
                }
            }

            request.setAttribute ("testMessageMap", this.allMsgs);
            request.setAttribute ("jmsDefList", this.allJms);
            request.setAttribute ("jmsDefName", jmsDefName);
            request.setAttribute ("currentDir", currentDir);
            request.setAttribute ("currentMessage", currentMsg);
            request.setAttribute ("currentMsgName", fileToRead);
            request.setAttribute ("timeout", timeoutStr);
        }
        catch (Throwable th)
        {
            evl.sendException (EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, th, this);
        }
        
        return "MessageTestMain.jsp";
    }


    /**
     * Build File List.
     *
     * @return dirs HashMap[key=dir name, value=list of file names]
     */
    private HashMap buildFileList()
    {
        HashMap allDirs = new HashMap();
        this.testMsgDir = new java.io.File(AbstractInitializer.getInitializer().getTEST_MSG_DIR ());

        if (testMsgDir.isDirectory ())
        {
            this.collectFileNames (this.testMsgDir, allDirs, ".");
        }
        else
        {
            evl.sendEvent ("Test Message Directory not available: " + testMsgDir.getAbsolutePath (), 
                           EventType.WARN, this);
        }

        if (allDirs.containsKey ("."))
        {
            allDirs.remove (".");
        }

        return allDirs;
    }


    /**
     * Scans the sub-directories of the Input Directory and collects all Filenames found.
     *
     * @param thisDir TestMsgDir
     * @param dirs [key=dir name, value=list of file names]
     * @param name Relative Dir Name
     */
    private void collectFileNames(java.io.File thisDir, HashMap dirs, String name)
    {
        java.io.File[] files = thisDir.listFiles ();
        ArrayList thisList = new ArrayList();

        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isDirectory ())
            {
                this.collectFileNames (files[i], dirs, name + fileSep + files[i].getName ());
            }
            else
            {
                thisList.add (files[i].getName ());
            }
        }

        if (thisList.size () > 0)
        {
            dirs.put (name, thisList);
        }
    }
}