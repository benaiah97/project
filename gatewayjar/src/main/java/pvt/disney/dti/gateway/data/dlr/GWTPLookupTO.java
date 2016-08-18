package pvt.disney.dti.gateway.data.dlr;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * This class represents the dlr egalaxy gateway provider lookup information for shipping info.
 * 
 * @author smoon
 * 
 */
public class GWTPLookupTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = -5387496705423442264L;

	/** The enumeration of valid ticket provider lookup types. */
	public enum TPLookupType {
		MAX_LIMIT,
		PAYMENT,
		VOID,
		ELIG_GROUP,
		SALES_TYPE,
		SHIP_METHOD,
		SHIP_DETAIL,
		CC_TYPE,
		UNDEFINED
	};

	private HashMap<String, String> dlrCardMap;

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

	/**
	 * @return the dlrCardMap
	 */
	public HashMap<String, String> getDlrCardMap() {
		return dlrCardMap;
	}

	/**
	 * @param dlrCardMap
	 *            the dlrCardMap to set
	 */
	public void setDlrCardMap(HashMap<String, String> dlrCardMap) {
		this.dlrCardMap = dlrCardMap;
	}

	/**
	 * public helper method for setting up map of dlr credit card info
	 * 
	 * @param @param lookupValue - two letter card type such as VI, DC, MC, etc
	 * @param lookupDesc
	 *            - two digit numeric code used in dlr order paymentcode
	 */
	public void addDlRCard(String lookupValue, String lookupDesc) {
		dlrCardMap.put(lookupValue, lookupDesc);
	}

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

}
