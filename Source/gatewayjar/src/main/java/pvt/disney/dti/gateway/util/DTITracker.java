package pvt.disney.dti.gateway.util;

import com.disney.logging.audit.MessageTracker;

/**
 * The purpose of this class is to create an object by which the message tracking functions of the core logging classes can uniquely identify a transaction as it passes through the system. Once created, a core interface is called which
 * associates the payloadId to a thread of execution. Anytime that thread logs, this payloadId appears in the log message!
 * 
 * @author lewit019
 * @version %version: 2 %
 */
public class DTITracker implements MessageTracker {

	/** Standard serial version uid. */
	private static final long serialVersionUID = 5365647609114138562L;

	/**
	 * A static number to increment for those situations where XML comes in without a parsable payloadId.
	 */
	private static int number = 0;

	/** payloadId from the in-flight transaction. */
	private String payloadId;

	/**
	 * Gets the payloadId.
	 * 
	 * @return the payloadId (a string).
	 */
	public String getPayloadId() {
		return payloadId;
	}

	/**
	 * Get the next number. Synchronized for protection from multiple thread hits at the same time. Synchronized against the class, not the object (hence "static").
	 */
	private static synchronized int getNextNumber() {
		int theNumber = number++;
		return theNumber;
	}

	/**
	 * Sets the payloadID in the tracker object. Defaults it to a unique ID (a very low number) if no payload ID is present.
	 * 
	 * @param string
	 *            The payloadID.
	 */
	public void setPayloadId(String string) {
		if ((string == null) || (string == "")) {
			payloadId = Integer.toString(getNextNumber());
		}
		else {
			payloadId = string;
		}
	}

}
