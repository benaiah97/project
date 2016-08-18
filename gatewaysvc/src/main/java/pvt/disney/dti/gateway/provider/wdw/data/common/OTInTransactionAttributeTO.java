package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;

/**
 * This class is a transfer object representing the Omni Ticket In-Transaction Attributes.
 * 
 * @author lewit019
 */
public class OTInTransactionAttributeTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The attribute command. */
	private String attributeCmd;

	/** The attribute key. */
	private Integer attributeKey;

	/** The attribute value. */
	private String attributeValue;

	/** The attribute type. */
	private Integer attributeType;

	/** The attribute flag. */
	private String attributeFlag;

	/**
	 * Gets the attribute cmd.
	 * 
	 * @return the attributeCmd
	 */
	public String getAttributeCmd() {
		return attributeCmd;
	}

	/**
	 * Gets the attribute flag.
	 * 
	 * @return the attributeFlag
	 */
	public String getAttributeFlag() {
		return attributeFlag;
	}

	/**
	 * Gets the attribute key.
	 * 
	 * @return the attributeKey
	 */
	public Integer getAttributeKey() {
		return attributeKey;
	}

	/**
	 * Gets the attribute type.
	 * 
	 * @return the attributeType
	 */
	public Integer getAttributeType() {
		return attributeType;
	}

	/**
	 * Gets the attribute value.
	 * 
	 * @return the attributeValue
	 */
	public String getAttributeValue() {
		return attributeValue;
	}

	/**
	 * Sets the attribute cmd.
	 * 
	 * @param attributeCmd
	 *            the attributeCmd to set
	 */
	public void setAttributeCmd(String attributeCmd) {
		this.attributeCmd = attributeCmd;
	}

	/**
	 * Sets the attribute flag.
	 * 
	 * @param attributeFlag
	 *            the attributeFlag to set
	 */
	public void setAttributeFlag(String attributeFlag) {
		this.attributeFlag = attributeFlag;
	}

	/**
	 * Sets the attribute key.
	 * 
	 * @param attributeKey
	 *            the attributeKey to set
	 */
	public void setAttributeKey(Integer attributeKey) {
		this.attributeKey = attributeKey;
	}

	/**
	 * Sets the attribute type.
	 * 
	 * @param attributeType
	 *            the attributeType to set
	 */
	public void setAttributeType(Integer attributeType) {
		this.attributeType = attributeType;
	}

	/**
	 * Sets the attribute value.
	 * 
	 * @param attributeValue
	 *            the attributeValue to set
	 */
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

}
