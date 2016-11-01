package pvt.disney.dti.gateway.util;

import com.disney.logging.audit.LoggingInfoMapperStub;
import com.disney.logging.audit.MessageInfo;
import com.disney.logging.audit.MessageTracker;

/**
 * This class extends the LoggingInfoMapperStub to include the DTI specific information found in the DTITracker object (namely the payloadId).
 * 
 * @author lewit019
 * @version %version: 2 %
 */
public class DTILoggingInfoMapper extends LoggingInfoMapperStub {

	/**
	 * Constructor (default).
	 */
	public DTILoggingInfoMapper() {
		super();

	}

	/**
	 * Associates the payloadId on the object passed in (DTITracker) as the messageId placed on each log message during the life of this transaction.
	 * 
	 * @see com.disney.logging.audit.LoggingInfoMapper#createMessageInfo(com.disney.logging.audit.MessageTracker)
	 */
	public MessageInfo createMessageInfo(MessageTracker arg0) {

		DTITracker dtitr = (DTITracker) arg0;

		MessageInfo msgInfo = new MessageInfo();

		msgInfo.setMessageId(dtitr.getPayloadId());

		return msgInfo;
	}

}
