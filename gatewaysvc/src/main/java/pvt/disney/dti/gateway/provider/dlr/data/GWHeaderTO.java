package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.GregorianCalendar;

/**
 * This class represents the Header portion of an eGalaxy XML.
 * 
 * @author lewit019
 * 
 */
public class GWHeaderTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	private String sourceID;
	private BigInteger messageID;
	private String messageType;
	private Integer sessionID;
	private GregorianCalendar timeStamp;
	private String echoData; // As of 2.16.1, in use for the DTI Payload ID

	/**
	 * @return the echoData
	 */
	public String getEchoData() {
		return echoData;
	}

	/**
	 * @return the messageID
	 */
	public BigInteger getMessageID() {
		return messageID;
	}

	/**
	 * @return the messageType
	 */
	public String getMessageType() {
		return messageType;
	}

	/**
	 * @return the sessionID
	 */
	public Integer getSessionID() {
		return sessionID;
	}

	/**
	 * @return the sourceID
	 */
	public String getSourceID() {
		return sourceID;
	}

	/**
	 * @return the timeStamp
	 */
	public GregorianCalendar getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param echoData
	 *            the echoData to set
	 */
	public void setEchoData(String echoData) {
		this.echoData = echoData;
	}

	/**
	 * @param messageID
	 *            the messageID to set
	 */
	public void setMessageID(BigInteger messageID) {
		this.messageID = messageID;
	}

	/**
	 * @param messageType
	 *            the messageType to set
	 */
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	/**
	 * @param sessionID
	 *            the sessionID to set
	 */
	public void setSessionID(Integer sessionID) {
		this.sessionID = sessionID;
	}

	/**
	 * @param sourceID
	 *            the sourceID to set
	 */
	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	/**
	 * @param timeStamp
	 *            the timeStamp to set
	 */
	public void setTimeStamp(GregorianCalendar timeStamp) {
		this.timeStamp = timeStamp;
	}

}
