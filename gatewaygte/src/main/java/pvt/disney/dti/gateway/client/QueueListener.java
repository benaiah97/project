package pvt.disney.dti.gateway.client;
/*
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import pvt.disney.dti.gateway.controller.DTIController;
import pvt.disney.dti.gateway.util.DTIFormatter;

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
 * the applications home config diretory. Once the message is received it is
 * passed to the HttpClient for processing. The response from the HttpClient is
 * placed on the MQ QUEUE specified in the ReplyTo field of the MQ Header.
 * 
 * @author Thomas A. Oliveri
 * @version %version: 4 %
 */
public class QueueListener {}/* implements MessageListener {

  private EventLogger eventLogger;

  *//**
   * Constructor for DTIListener
   *//*
  public QueueListener() {
    super();
  }

  *//**
   * Passes a message to the listener.
   * 
   * @see MessageListener#onMessage(Message)
   *//*
  public void onMessage(Message msg) {

    TextMessage tMsgIn;
    String xmlDataIn, errorMsg;
    String xmlDataOut = null;

    eventLogger = EventLogger.getLogger(this.getClass());

    // Log message received from Ticket Seller.
    eventLogger.sendEvent("Received ticket seller request from mqseries.", EventType.INFO, this);

    try {
      AbstractInitializer abstrInit = AbstractInitializer.getInitializer();

      Properties props = abstrInit.getProps("DTICLIENT.properties");

      if (msg instanceof TextMessage) {
        tMsgIn = (TextMessage) msg;

        xmlDataIn = tMsgIn.getText();

        // Send straight into the DTI Controller, unless overridden to go by way
        // of the HTTPS end-point.
        String ctrlString = PropertyHelper.readPropsValue("CONTROLLER", props, null);

        if ((ctrlString != null) && (ctrlString.compareToIgnoreCase("HTTPS") == 0)) {

          HttpClient controller = new HttpClient();

          eventLogger.sendEvent("Instantiated the HttpClient from QueueListener.", EventType.DEBUG,
              this);
          xmlDataOut = DTIFormatter.getDocumentToString(controller.processRequest(DTIFormatter
              .getStringToInputStream(xmlDataIn)));

        } else {

          DTIController controller = new DTIController();

          eventLogger.sendEvent("Instantiated the DTIController from QueueListener.",
              EventType.DEBUG, this);

          xmlDataOut = DTIFormatter.getDocumentToString(controller.processRequest(DTIFormatter
              .getStringToInputStream(xmlDataIn)));
        }

        // Retrieves ReplyTo queue from request message.
        Destination replyTo = msg.getJMSReplyTo();

        if ((replyTo != null) && (xmlDataOut != null)) {
          // Initializes jmsReplier object for ReplyTo specified in Request
          // message.
          JmsReplier jmsReplier = new JmsReplier(replyTo);

          // Call out to exposed method to allow sub-classes to override.
          Message reply = this.prepareResponse(msg, jmsReplier, xmlDataOut);

          jmsReplier.sendMessage(reply);

          // Log Event Successfully Sent Reply message to ticket seller.
          eventLogger.sendEvent("Sent Reply message to " + msg.getJMSReplyTo(), EventType.INFO,
              this);
        } else if (xmlDataOut != null) {
          eventLogger.sendEvent("No Reply To Specified and using default ReplyTO QUEUE.",
              EventType.INFO, this);

          JmsWriter jmsOutWriter = new JmsWriter(PropertyHelper.readPropsValue("MQ_REPLY", props,
              null));

          // Call out to exposed method to allow sub-classes to override.
          Message reply = this.prepareResponse(msg, jmsOutWriter, xmlDataOut);

          jmsOutWriter.sendMessage(reply);

          // Log Event Successfully Sent Reply message to ticket seller.
          eventLogger.sendEvent("Sent Reply message to "
              + PropertyHelper.readPropsValue("MQ_REPLY", props, null), EventType.DEBUG, this);
        } else {
          eventLogger.sendEvent("EXCEPTION Encountered xmlDataOut is NULL", EventType.EXCEPTION,
              this);
        }

      } else {
        errorMsg = "Non-Text Message Received by QueueListener: " + msg.toString();
        eventLogger.sendEvent(errorMsg, EventType.WARN, this);
      }
    } catch (Exception e) {
      errorMsg = "Exception in QueueListener : " + e.toString();
      eventLogger.sendEvent(errorMsg, EventType.EXCEPTION, this);
      eventLogger.sendException(EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, e, this);
    }
  }

  *//**
   * The default implementation of this method creates a JMS TextMessage Object,
   * and populates the JMSCorrelationID with the JMSMessageID from the Request.
   * 
   * ANY SUB-CLASS THAT OVERRIDES THIS METHOD SHOULD DOCUMENT THE LOGIC
   * IMPLEMENTED!
   * 
   * @param request
   *          Original JMS Request Message
   * @param jmsReplier
   *          JmsReplier to create the Response Message with.
   * @param rspText
   *          Response Text received from Message Processor.
   * @return Message JMS Reply Message
   * @throws com.disney.jms.JmsException
   *//*
  protected Message prepareResponse(Message request, JmsReplier jmsReplier, String rspText)
      throws JmsException {

    TextMessage reply = jmsReplier.createTextMessage(rspText);

    try {
      reply.setJMSCorrelationID(request.getJMSMessageID());
      eventLogger.sendEvent("Mapped JMSMessageID: " + request.getJMSMessageID()
          + " to JMSCorrelationID.", EventType.DEBUG, this);
    } catch (Exception e) {
      eventLogger.sendEvent("Exception in QueueListener prepareResponse method: " + e.toString(),
          EventType.EXCEPTION, this);
      eventLogger.sendException(EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, e, this);
    }

    return reply;
  }

  *//**
   * The default implementation of this method creates a JMS TextMessage Object,
   * and populates the JMSCorrelationID with the JMSMessageID from the Request.
   * 
   * ANY SUB-CLASS THAT OVERRIDES THIS METHOD SHOULD DOCUMENT THE LOGIC
   * IMPLEMENTED!
   * 
   * @param request
   *          Original JMS Request Message
   * @param jmsWriter
   *          JmsWriter to create the Response Message with.
   * @param rspText
   *          Response Text received from Message Processor.
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
      eventLogger.sendEvent("Mapped JMSMessageID: " + request.getJMSMessageID()
          + " to JMSCorrelationID.", EventType.DEBUG, this);
    } catch (Exception e) {
      errorMsg = "Exception in QueueListener prepareResponse method: " + e.toString();
      eventLogger.sendEvent(errorMsg, EventType.EXCEPTION, this);
      eventLogger.sendException(EventType.EXCEPTION, ErrorCode.MESSAGING_EXCEPTION, e, this);
    }

    return reply;
  }
}
*/