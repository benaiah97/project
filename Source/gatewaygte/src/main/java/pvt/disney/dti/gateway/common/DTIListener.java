package pvt.disney.dti.gateway.common;
/*
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.BytesMessage;

import pvt.disney.dti.gateway.controller.DTIController;
import pvt.disney.dti.gateway.util.DTIFormatter;

import com.disney.jms.JmsException;
import com.disney.jms.JmsReplier;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
*/
/**
 * Lisetens for message on configured queue within the jms.properties files of
 * the applications home config diretory. Once the message is received it is
 * passed to the DTIController for processing. The response from the
 * DTIController is placed on the MQ QUEUE specified in the ReplyTo field of the
 * MQ Header.
 * 
 * @author Thomas A. Oliveri
 * @version %version: 2 %
 *  
 */
public class DTIListener {//implements MessageListener {

	//protected EventLogger eventLogger = null;

	/**
	 * Constructor for DTIListener
	 */
	public DTIListener() {
		super();
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	/*public void onMessage(Message msg) {
		TextMessage tMsgIn;//, tMsgOut;
		//BytesMessage bMsgIn; // ))(( Commented out unreferenced
		//boolean msgBinary = false;
		//boolean msgText = false;

		//JmsWriter jmsOut;
		String xmlDataIn = null, errorMsg;
		String xmlDataOut = null;

		eventLogger = EventLogger.getLogger(DTIListener.class);

		eventLogger.sendEvent("Entering onMessage(Message)", EventType.DEBUG,
				this);

		//Log message received from Ticket Seller.
		eventLogger.sendEvent("Received ticket seller request from mqseries.",
				EventType.INFO, this);

		try {

			if (msg instanceof BytesMessage) {
				xmlDataIn = convertBytesToString(msg);
				//msgBinary = true;  Commented out unreferenced
			}

			if (msg instanceof TextMessage) {
				tMsgIn = (TextMessage) msg;
				xmlDataIn = tMsgIn.getText();
				//msgText = true;  Commented out unreferenced
			}
			
			if (xmlDataIn != null) {
				
				// Creating response message and sending to queue.
				eventLogger.sendEvent(
						"About to Instantiate DTIController from DTIListener.",
						EventType.DEBUG, this);

				//DTIController
				DTIController controller = new DTIController();

				eventLogger.sendEvent("DTIController Instantiated",
						EventType.DEBUG, this);

				//Retrieve response from DTIController
				xmlDataOut = DTIFormatter.getDocumentToString(controller
						.processRequest(DTIFormatter
								.getStringToInputStream(xmlDataIn)));
				
				eventLogger
						.sendEvent(
								"DTIController Processed Request... xmlDataOut generated",
								EventType.DEBUG, this);

				//Retrieves ReplyTo queue from request message.
				Destination replyTo = msg.getJMSReplyTo();
        // TO DO:  Check for NULL pointer.  If so, then log that 
        // no response queue was selected (WARN).        
				eventLogger.sendEvent("Reply to queue = " + replyTo.toString(),
						EventType.DEBUG, this);

				if ((replyTo != null) && (xmlDataOut != null)) {
					eventLogger.sendEvent("About to initialize the jmsReplier",
							EventType.DEBUG, this);

					// Initializes jmsReplier object for ReplyTo specified in
					// Request message.
					JmsReplier jmsReplier = new JmsReplier(replyTo);

					eventLogger.sendEvent("Configure reply message",
							EventType.DEBUG, this);

					// Call out to exposed method to allow sub-classes to
					// override.
					Message reply = this.prepareResponse(msg, jmsReplier,
							xmlDataOut);

					eventLogger.sendEvent("About to send reply",
							EventType.DEBUG, this);

					jmsReplier.sendMessage(reply);

					// Log Event Successfully Sent Reply message to ticket
					// seller.
					eventLogger.sendEvent(
							"Returned response to mqseries ticket seller on queue "
									+ msg.getJMSReplyTo().toString(),
							EventType.INFO, this);

				} else {
					if (xmlDataOut != null) {
						eventLogger
								.sendEvent(
										"No Reply To Specified and non-dynamic destination.",
										EventType.WARN, this);
					}
				}

			} else {
				errorMsg = "No/Empty Message Received by DTIListener: "
						+ msg.toString();
				eventLogger.sendEvent(errorMsg, EventType.WARN, this);
			}
		} catch (Exception e) {
			errorMsg = "EXCEPTION in DTIListener : " + e.toString();
			eventLogger.sendEvent(errorMsg, EventType.EXCEPTION, this);
			eventLogger.sendException(EventType.EXCEPTION,
					ErrorCode.MESSAGING_EXCEPTION, e, this);
		}
	}

	*//**
	 * The default implementation of this method creates a JMS TextMessage
	 * Object, and populates the JMSCorrelationID with the JMSMessageID from the
	 * Request.
	 * 
	 * ANY SUB-CLASS THAT OVERRIDES THIS METHOD SHOULD DOCUMENT THE LOGIC
	 * IMPLEMENTED!
	 * 
	 * @param request
	 *            Original JMS Request Message
	 * @param jmsReplier
	 *            JmsReplier to create the Response Message with.
	 * @param rspText
	 *            Response Text received from Message Processor.
	 * @return Message JMS Reply Message
	 * @throws JMSException
	 * @throws JmsException
	 *//*
	protected Message prepareResponse(Message request, JmsReplier jmsReplier,
			String rspText) throws JMSException, JmsException {

		eventLogger.sendEvent("Entering prepareResponse(,,)", EventType.DEBUG,
				this);

		TextMessage reply = jmsReplier.createTextMessage(rspText);
		String errorMsg = "";

		try {
			reply.setJMSCorrelationID(request.getJMSMessageID());
			eventLogger.sendEvent("Mapped JMSMessageID: "
					+ request.getJMSMessageID() + " to JMSCorrelationID.",
					EventType.DEBUG, this);
		} catch (Exception e) {
			errorMsg = "EXCEPTION in DTIListener prepareResponse method: "
					+ e.toString();
			eventLogger.sendEvent(errorMsg, EventType.EXCEPTION, this);
			eventLogger.sendException(EventType.EXCEPTION,
					ErrorCode.MESSAGING_EXCEPTION, e, this);
		}

		return reply;
	}

	*//**
	 * convertBytesToString method converts binary message to a string
	 * 
	 * @param javax.jms.Message
	 * @return String
	 * @throws ArchiveException
	 *//*
	public String convertBytesToString(javax.jms.Message msg) throws Exception {
		try {
			byte[] b = new byte[8000];
			String s = null;
			StringBuffer buffer = new StringBuffer();
			int length = 0;
			while (length != -1) {
				length = ((BytesMessage) msg).readBytes(b, 8000);
				if (length != -1) {
					buffer.append(new String(b, 0, length));
				}
			}
			s = buffer.toString();
			return s;
		} catch (Throwable th) {
			Exception ee = new Exception(
					"Throwable caught Reading Bytes Message");
			eventLogger.sendException(EventType.EXCEPTION,
					ErrorCode.MESSAGING_EXCEPTION, th, this);
			throw ee;
		}
	}
*/
}