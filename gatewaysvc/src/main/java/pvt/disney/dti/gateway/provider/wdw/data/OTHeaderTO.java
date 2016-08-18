package pvt.disney.dti.gateway.provider.wdw.data;

import java.io.Serializable;

/**
 * This class is a transfer object which represents the Omni ticket header.
 * 
 * @author lewit019
 * 
 */
public class OTHeaderTO implements Serializable {

	/** Standard serial version UID. */
	public final static long serialVersionUID = 9129231995L;

	// Shared Fields
	/** The Omni Ticket reference number. */
	private String referenceNumber;

	/** The request type. */
	private String requestType;

	/** The request subtype. */
	private String requestSubType;

	// Out-bound Fields
	/** The operating area. */
	private String operatingArea;

	/** The user ID. */
	private Integer userId;

	/** The password. */
	private Integer password;

	// In-bound Fields
	/** The request number. */
	private Integer requestNumber;

	/** The session identifier. */
	private Integer sessionId;

	/** The quantity of items in the message that were valued. */
	private Integer valueMsgValuedQty;

	/** The quantity of items in the message that were non valued. */
	private Integer valueMsgNonValuedQty;

	/**
	 * @return the operatingArea
	 */
	public String getOperatingArea() {
		return operatingArea;
	}

	/**
	 * @return the password
	 */
	public Integer getPassword() {
		return password;
	}

	/**
	 * @return the referenceNumber
	 */
	public String getReferenceNumber() {
		return referenceNumber;
	}

	/**
	 * @return the requestNumber
	 */
	public Integer getRequestNumber() {
		return requestNumber;
	}

	/**
	 * @return the requestSubType
	 */
	public String getRequestSubType() {
		return requestSubType;
	}

	/**
	 * @return the requestType
	 */
	public String getRequestType() {
		return requestType;
	}

	/**
	 * @return the sessionId
	 */
	public Integer getSessionId() {
		return sessionId;
	}

	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}

	/**
	 * @return the valueMsgNonValuedQty
	 */
	public Integer getValueMsgNonValuedQty() {
		return valueMsgNonValuedQty;
	}

	/**
	 * @return the valueMsgValuedQty
	 */
	public Integer getValueMsgValuedQty() {
		return valueMsgValuedQty;
	}

	/**
	 * @param operatingArea
	 *            the operatingArea to set
	 */
	public void setOperatingArea(String operatingArea) {
		this.operatingArea = operatingArea;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(Integer password) {
		this.password = password;
	}

	/**
	 * @param referenceNumber
	 *            the referenceNumber to set
	 */
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	/**
	 * @param requestNumber
	 *            the requestNumber to set
	 */
	public void setRequestNumber(Integer requestNumber) {
		this.requestNumber = requestNumber;
	}

	/**
	 * @param requestSubType
	 *            the requestSubType to set
	 */
	public void setRequestSubType(String requestSubType) {
		this.requestSubType = requestSubType;
	}

	/**
	 * @param requestType
	 *            the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * @param valueMsgNonValuedQty
	 *            the valueMsgNonValuedQty to set
	 */
	public void setValueMsgNonValuedQty(Integer valudMsgNonValuedQty) {
		this.valueMsgNonValuedQty = valudMsgNonValuedQty;
	}

	/**
	 * @param valueMsgValuedQty
	 *            the valueMsgValuedQty to set
	 */
	public void setValueMsgValuedQty(Integer valueMsgValuedQty) {
		this.valueMsgValuedQty = valueMsgValuedQty;
	}

}
