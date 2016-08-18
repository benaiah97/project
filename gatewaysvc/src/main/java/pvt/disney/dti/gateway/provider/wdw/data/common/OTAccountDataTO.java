package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * This class represents the Omni Ticket Entitlement Account Data Info Transfer Object.
 * 
 * @author lewit019
 * 
 */
public class OTAccountDataTO implements Serializable {

	/** Standard serial version UID. */
	public final static long serialVersionUID = 9129231995L;

	/** The entitlement account identifier. */
	private BigInteger entitlementAccountId;

	private String accountExternalReference;

	private Byte accountExternalReferenceType;

	private String searchExistingMediaId;

	private String searchAccountId;

	private OTDemographicData demoData;

	private OTTicketTO ticketSearchMode;

	private ArrayList<OTMediaDataTO> mediaDataList = new ArrayList<OTMediaDataTO>(); // MWH

	/**
	 * @return the entitlementAccountId
	 */
	public BigInteger getEntitlementAccountId() {
		return entitlementAccountId;
	}

	/**
	 * @param entitlementAccountId
	 *            the entitlementAccountId to set
	 */
	public void setEntitlementAccountId(BigInteger entitlementAccountId) {
		this.entitlementAccountId = entitlementAccountId;
	}

	/**
	 * @param mediaDataList
	 *            the mediaDataList to set
	 */
	public void setMediaDataList(ArrayList<OTMediaDataTO> mediaDataList) {
		this.mediaDataList = mediaDataList;
	}

	/**
	 * @param accountExternalReference
	 *            the accountExternalReference to set
	 */
	public void setAccountExternalReference(String accountExternalReference) {
		this.accountExternalReference = accountExternalReference;
	}

	/**
	 * @return the accountExternalReference
	 */
	public String getAccountExternalReference() {
		return accountExternalReference;
	}

	/**
	 * @param accountExternalReferenceType
	 *            the accountExternalReferenceType to set
	 */
	public void setAccountExternalReferenceType(
			Byte accountExternalReferenceType) {
		this.accountExternalReferenceType = accountExternalReferenceType;
	}

	/**
	 * @return the accountExternalReferenceType
	 */
	public Byte getAccountExternalReferenceType() {
		return accountExternalReferenceType;
	}

	/**
	 * @param searchExistingMediaId
	 *            the searchExistingMediaId to set
	 */
	public void setSearchExistingMediaId(String searchExistingMediaId) {
		this.searchExistingMediaId = searchExistingMediaId;
	}

	/**
	 * @return the searchExistingMediaId
	 */
	public String getSearchExistingMediaId() {
		return searchExistingMediaId;
	}

	/**
	 * @param searchAccountId
	 *            the searchAccountId to set
	 */
	public void setSearchAccountId(String searchAccountId) {
		this.searchAccountId = searchAccountId;
	}

	/**
	 * @return the searchAccountId
	 */
	public String getSearchAccountId() {
		return searchAccountId;
	}

	/**
	 * @param ticketSearchMode
	 *            the ticketSearchMode to set
	 */
	public void setTicketSearchMode(OTTicketTO ticketSearchMode) {
		this.ticketSearchMode = ticketSearchMode;
	}

	/**
	 * @return the ticketSearchMode
	 */
	public OTTicketTO getTicketSearchMode() {
		return ticketSearchMode;
	}

	/**
	 * 2013.06.12 - MWH - logic for NewMediaData
	 * 
	 * @param mediaData
	 *            the mediaData to set
	 */
	public void addMediaData(OTMediaDataTO mediaData) {
		if (mediaData != null) {
			this.mediaDataList.add(mediaData);
		}
	}

	/**
	 * 2013.06.12 - MWH - logic for NewMediaData
	 * 
	 * @return the mediaData
	 */
	public ArrayList<OTMediaDataTO> getMediaDataList() {
		return this.mediaDataList;
	}

	/**
	 * @return the demoData
	 */
	public OTDemographicData getDemoData() {
		return demoData;
	}

	/**
	 * @param demoData
	 *            the demoData to set
	 */
	public void setDemoData(OTDemographicData demoData) {
		this.demoData = demoData;
	}

}
