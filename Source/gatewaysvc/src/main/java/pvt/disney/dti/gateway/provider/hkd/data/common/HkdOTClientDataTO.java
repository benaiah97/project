package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is a transfer object which holds the Omni Ticket client data.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class HkdOTClientDataTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The client unique identifier. */
	private Integer clientUniqueId;

	/** The client type. */
	private String clientType;

	/** The client subtype. */
	private Integer clientSubtype;

	/** The client category. */
	private String clientCategory;

	/** The client language. */
	private Integer clientLanguage;

	/** The array list of Demographic data fields. */
	private ArrayList<HkdOTFieldTO> demographicData = new ArrayList<HkdOTFieldTO>();

	/**
	 * @return the clientCategory
	 */
	public String getClientCategory() {
		return clientCategory;
	}

	/**
	 * @return the clientLanguage
	 */
	public Integer getClientLanguage() {
		return clientLanguage;
	}

	/**
	 * @return the clientType
	 */
	public String getClientType() {
		return clientType;
	}

	/**
	 * @return the clientUniqueId
	 */
	public Integer getClientUniqueId() {
		return clientUniqueId;
	}

	/**
	 * @return the demographicData
	 */
	public ArrayList<HkdOTFieldTO> getDemographicData() {
		return demographicData;
	}

	/**
	 * @param clientCategory
	 *            the clientCategory to set
	 */
	public void setClientCategory(String clientCategory) {
		this.clientCategory = clientCategory;
	}

	/**
	 * @param clientLanguage
	 *            the clientLanguage to set
	 */
	public void setClientLanguage(Integer clientLanguage) {
		this.clientLanguage = clientLanguage;
	}

	/**
	 * @param clientType
	 *            the clientType to set
	 */
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	/**
	 * @param clientUniqueId
	 *            the clientUniqueId to set
	 */
	public void setClientUniqueId(Integer clientUniqueId) {
		this.clientUniqueId = clientUniqueId;
	}

	/**
	 * @param demographicData
	 *            the demographicData to set
	 */
	public void setDemographicData(ArrayList<HkdOTFieldTO> demographicData) {
		this.demographicData = demographicData;
	}

	/**
	 * @return the clientSubtype
	 */
	public Integer getClientSubtype() {
		return clientSubtype;
	}

	/**
	 * @param clientSubtype
	 *            the clientSubtype to set
	 */
	public void setClientSubtype(Integer clientSubtype) {
		this.clientSubtype = clientSubtype;
	}

}
