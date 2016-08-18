package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * This class represents the ticket provider lookup information (such as language, client type, pickup area, and maximum sales limit.
 * 
 * @author lewit019
 * 
 */
public class TPLookupTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = -5387496705423442264L;

	/** The enumeration of valid ticket provider lookup types. */
	public enum TPLookupType {
		LANGUAGE,
		CLIENT_TYPE,
		PICKUP_AREA,
		PICKUP_TYPE,
		SALES_TYPE,
		MAX_LIMIT,
		SHIP_METHOD,
		SHIP_DETAIL,
		CC_TYPE,
		INSTALLMENT,
		UNDEFINED
	};

	/** The command code. */
	private String cmdCode;

	/** The command identifier. */
	private BigInteger cmdId;

	/** The lookup type. */
	private TPLookupType lookupType;

	/** The lookup value. */
	private String lookupValue;

	/** The lookup description. */
	private String lookupDesc;

	/** Link ID (as of 2.16.1, JTL) */
	private BigInteger linkId;

	/**
	 * @return the cmdCode
	 */
	public String getCmdCode() {
		return cmdCode;
	}

	/**
	 * @return the cmdId
	 */
	public BigInteger getCmdId() {
		return cmdId;
	}

	/**
	 * @return the lookupDesc
	 */
	public String getLookupDesc() {
		return lookupDesc;
	}

	/**
	 * @return the lookupValue
	 */
	public String getLookupValue() {
		return lookupValue;
	}

	/**
	 * @param cmdCode
	 *            the cmdCode to set
	 */
	public void setCmdCode(String cmdCode) {
		this.cmdCode = cmdCode;
	}

	/**
	 * @param cmdId
	 *            the cmdId to set
	 */
	public void setCmdId(BigInteger cmdId) {
		this.cmdId = cmdId;
	}

	/**
	 * @param lookupDesc
	 *            the lookupDesc to set
	 */
	public void setLookupDesc(String lookupDesc) {
		this.lookupDesc = lookupDesc;
	}

	/**
	 * @return the lookupType
	 */
	public TPLookupType getLookupType() {
		return lookupType;
	}

	/**
	 * @param lookupType
	 *            the lookupType to set
	 */
	public void setLookupType(TPLookupType lookupType) {
		this.lookupType = lookupType;
	}

	/**
	 * @param lookupValue
	 *            the lookupValue to set
	 */
	public void setLookupValue(String lookupValue) {
		this.lookupValue = lookupValue;
	}

	/**
	 * @return the linkId
	 */
	public BigInteger getLinkId() {
		return linkId;
	}

	/**
	 * @param linkId
	 *            the linkId to set
	 */
	public void setLinkId(BigInteger linkId) {
		this.linkId = linkId;
	}

}
