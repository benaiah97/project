package pvt.disney.dti.gateway.client;
/*
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import pvt.disney.dti.gateway.util.DTIFormatter;
import pvt.disney.dti.gateway.util.PCIControl;

import com.disney.jms.JmsDeliveryMode;
import com.disney.jms.JmsException;
import com.disney.jms.JmsReplier;
import com.disney.jms.JmsWriter;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.PropertyHelper;
*/
/**
 * Listens for message on configured queue within the jms.properties files of 
 * the applications home config diretory.  Once the message is received it
 * is passed to the HttpController for processing.  The response from the
 * HttpController is placed on the MQ QUEUE specified in the ReplyTo field 
 * of the MQ Header.
 * 
 * @author Thomas A. Oliveri
 * @version %version: 4 %   
 */
public class DLRListener {//implements MessageListener {
/*
  private EventLogger eventLogger;

  *//**
   * Constructor for DTIListener
   *//*
  public DLRListener() {
    super();
  }

  *//** Passes a message to the listener.
   * @see MessageListener#onMessage(Message)
   *//*
  public void onMessage(Message msg) {
    TextMessage tMsgIn; //, tMsgOut; Commented out unreferenced
    String xmlDataIn, errorMsg;
    String xmlDataOut = null;

    eventLogger = EventLogger.getLogger(this.getClass());

    try {
      AbstractInitializer abstrInit = AbstractInitializer.getInitializer();

      Properties props = abstrInit.getProps("DLRCLIENT.properties");

      if (msg instanceof TextMessage) {
        tMsgIn = (TextMessage)msg;

        xmlDataIn = tMsgIn.getText();

        //Log message received from Ticket Seller.
        String xmlData = PCIControl.overwritePciDataInXML(xmlDataIn);
        
        // MASK OUT <Endorsement>ZZZZZZZZZZZZZZZZ</Endorsement>
				String xmlMasked = PCIControl.overwritePciDataInXML(xmlData);
        eventLogger.sendEvent("Received message: " + xmlMasked, EventType.INFO, this);

        // Creating response message and sending to queue.
        eventLogger.sendEvent("About to Instantiate HttpController from DLRListener.", EventType.DEBUG, this);

        //DTIController
        HttpController controller = new HttpController();

        eventLogger.sendEvent(
          "Completed Instantiation of the HttpController from DLRListener.",
          EventType.DEBUG,
          this);

        //Retrieve response from DTIController			
        xmlDataOut =  
          DTIFormatter.getDocumentToString(
          controller.processRequest(DTIFormatter.getStringToInputStream(xmlDataIn)));

        //Retrieves ReplyTo queue from request message.
        Destination replyTo = msg.getJMSReplyTo();

        if ((replyTo != null) && (xmlDataOut != null)) {
          // Initializes jmsReplier object for ReplyTo specified in Request message.
          JmsReplier jmsReplier = new JmsReplier(replyTo);
          jmsReplier.setDeliveryMode(JmsDeliveryMode.NON_PERSISTANT); // ))(( JTL EXPERIMENTAL CODE AUG 04 

          // Call out to exposed method to allow sub-classes to override.
          Message reply = this.prepareResponse(msg, jmsReplier, xmlDataOut);

          jmsReplier.sendMessage(reply);

          // Log Event Successfully Sent Reply message to ticket seller.
          eventLogger.sendEvent("Sent Reply message to " + msg.getJMSReplyTo(), EventType.INFO, this);
        } else if (xmlDataOut != null) {
          eventLogger.sendEvent(
            "No Reply To Specified and using default ReplyTO QUEUE.",
            EventType.INFO,
            this);	

          JmsWriter jmsOutWriter = new JmsWriter(PropertyHelper.readPropsValue("MQ_REPLY", props, null));

          // Call out to exposed method to allow sub-classes to override.
          Message reply = this.prepareResponse(msg, jmsOutWriter, xmlDataOut);

          jmsOutWriter.sendMessage(reply);

          //Log Event Successfully Sent Reply message to ticket seller.
          eventLogger.sendEvent(
            "Sent Reply message to " + PropertyHelper.readPropsValue("MQ_REPLY", props, null),
            EventType.DEBUG,
            this);
        } else {
          eventLogger.sendEvent("EXCEPTION Encountered xmlDataOut is NULL", EventType.EXCEPTION, this);
        }

        eventLogger.sendEvent("Message Sent: " + xmlDataOut, EventType.INFO, this);
      } else {
        errorMsg = "Non-Text Message Received by DLRListener: " + msg.toString();
        eventLogger.sendEvent(errorMsg, EventType.WARN, this);
      }
    } catch (Exception e) {
      errorMsg = "Exception in DLRListener : " + e.toString();
      eventLogger.sendEvent(errorMsg, EventType.EXCEPTION, this);
      eventLogger.sendException(EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, e, this);
    }
  }

  *//**
   * The default implementation of this method creates a JMS TextMessage Object,
   * and populates the JMSCorrelationID with the JMSMessageID from the Request.
   * 
   * ANY SUB-CLASS THAT OVERRIDES THIS METHOD SHOULD DOCUMENT THE LOGIC IMPLEMENTED!
   * 
   * @param request Original JMS Request Message
   * @param jmsReplier JmsReplier to create the Response Message with.
   * @param rspText Response Text received from Message Processor.
   * @return Message JMS Reply Message
   * @throws com.disney.jms.JmsException
   *//*
  protected Message prepareResponse(Message request, JmsReplier jmsReplier, String rspText)
    throws JmsException {

    TextMessage reply = jmsReplier.createTextMessage(rspText);

    try {
      reply.setJMSCorrelationID(request.getJMSMessageID());
      eventLogger.sendEvent(
        "Mapped JMSMessageID: " + request.getJMSMessageID() + " to JMSCorrelationID.",
        EventType.DEBUG,
        this);
    } catch (Exception e) {
      eventLogger.sendEvent(
        "Exception in DLRListener prepareResponse method: " + e.toString(),
        EventType.EXCEPTION,
        this);
      eventLogger.sendException(EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, e, this);
    }

    return reply;
  }

  *//**
   * The default implementation of this method creates a JMS TextMessage Object,
   * and populates the JMSCorrelationID with the JMSMessageID from the Request.
   * 
   * ANY SUB-CLASS THAT OVERRIDES THIS METHOD SHOULD DOCUMENT THE LOGIC IMPLEMENTED!
   * 
   * @param request Original JMS Request Message
   * @param jmsWriter JmsWriter to create the Response Message with.
   * @param rspText Response Text received from Message Processor.
   * @return Message JMS Reply Message
   * @throws JMSException
   * @throws JmsException
   *//*
  protected Message prepareResponse(Message request, JmsWriter jmsWriter, String rspText)
    throws JMSException, JmsException {
    TextMessage reply = jmsWriter.createTextMessage(rspText);
    String errorMsg = "";

    try {
      reply.setJMSCorrelationID(request.getJMSMessageID());
      eventLogger.sendEvent(
        "Mapped JMSMessageID: " + request.getJMSMessageID() + " to JMSCorrelationID.",
        EventType.DEBUG,
        this);
    } catch (Exception e) {
      errorMsg = "Exception in DLRListener prepareResponse method: " + e.toString();
      eventLogger.sendEvent(errorMsg, EventType.EXCEPTION, this);
      eventLogger.sendException(EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, e, this);
    }

    return reply;
  }
*/
}
// Different examples of sending different messages to response queue.
/* Asynchronously send \ receive:
	 JmsWriter jmsWriteBoy = new JmsWriter("QTEST");
	 javax.jms.TextMessage reqMsg = jmsWriteBoy.createTextMessage(currentMsg);
	 JmsReplyReader replyReader = jmsWriteBoy.sendMessageCreateReplyReader(reqMsg);
	 javax.jms.Message rspMsg = replyReader.readReply(timeout);
*/

/* Synchronously send \ receive:
	 JmsWriter jmsWriteBoy = new JmsWriter("QTEST");
	 javax.jms.TextMessage reqMsg = jmsWriteBoy.createTextMessage(currentMsg);
	 javax.jms.Message rspMsg = jmsWriteBoy.sendMessage(reqMsg, timeout);
*/

/* Fire and forget:
	 JmsWriter jmsWriteBoy = new JmsWriter("QTEST");
	 javax.jms.TextMessage reqMsg = jmsWriteBoy.createTextMessage(currentMsg);
	 jmsWriteBoy.sendMessage(reqMsg);
*/
