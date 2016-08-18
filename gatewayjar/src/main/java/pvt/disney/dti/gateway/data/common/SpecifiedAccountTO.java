package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Allows the WDW seller to specify which Entitlement account to associate with what products.
 * 
 * @author LEWIT019
 * @author SHIEC014
 * 
 */
public class SpecifiedAccountTO implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -5991653313448898623L;

	private BigInteger accountItem;
	private String existingMediaId;
	private TicketIdTO existingTktID;
	private String existingAccountId;
	private DemographicsTO accountDemo;
	private ArrayList<NewMediaDataTO> newMediaDataList = new ArrayList<NewMediaDataTO>(); // MWH
	private String newExternalReferenceType;
	private String newExternalReferenceValue;

	/**
	 * @param accountItem
	 *            the accountItem to set
	 */
	public void setAccountItem(BigInteger accountItem) {
		this.accountItem = accountItem;
	}

	/**
	 * @return the accountItem
	 */
	public BigInteger getAccountItem() {
		return accountItem;
	}

	/**
	 * @param existingMediaId
	 *            the existingMediaId to set
	 */
	public void setExistingMediaId(String existingMediaId) {
		this.existingMediaId = existingMediaId;
	}

	/**
	 * @return the existingMediaId
	 */
	public String getExistingMediaId() {
		return existingMediaId;
	}

	/**
	 * @return the existingAccountId
	 */
	public String getExistingAccountId() {
		return existingAccountId;
	}

	/**
	 * @param existingAccountId
	 *            the existingAccountId to set
	 */
	public void setExistingAccountId(String existingAccountId) {
		this.existingAccountId = existingAccountId;
	}

	/**
	 * @param newMediaDataList
	 *            the newMediaDataList to set
	 */
	public void setNewMediaDataList(ArrayList<NewMediaDataTO> newMediaDataList) {
		this.newMediaDataList = newMediaDataList;
	}

	/**
	 * @param newExternalReferenceType
	 *            the newExternalReferenceType to set
	 */
	public void setNewExternalReferenceType(String newExternalReferenceType) {
		this.newExternalReferenceType = newExternalReferenceType;
	}

	/**
	 * @return the newExternalReferenceType
	 */
	public String getNewExternalReferenceType() {
		return newExternalReferenceType;
	}

	/**
	 * @param newExternalReferenceValue
	 *            the newExternalReferenceValue to set
	 */
	public void setNewExternalReferenceValue(String newExternalReferenceValue) {
		this.newExternalReferenceValue = newExternalReferenceValue;
	}

	/**
	 * @return the newExternalReferenceValue
	 */
	public String getNewExternalReferenceValue() {
		return newExternalReferenceValue;
	}

	/**
	 * @param existingTktID
	 *            the existingTktID to set
	 */
	public void setExistingTktID(TicketIdTO existingTktID) {
		this.existingTktID = existingTktID;
	}

	/**
	 * @return the existingTktID
	 */
	public TicketIdTO getExistingTktID() {
		return existingTktID;
	}

	/**
	 * 2013.06.12 - MWH - logic for NewMediaData
	 * 
	 * @param newMediaData
	 *            the newMediaData to set
	 */
	public void addNewMediaData(NewMediaDataTO newMediaData) {
		if (newMediaData != null) {
			this.newMediaDataList.add(newMediaData);
		}
	}

	/**
	 * 2013.06.12 - MWH - logic for NewMediaData
	 * 
	 * @return the newMediaData
	 */
	public ArrayList<NewMediaDataTO> getNewMediaDataList() {
		return newMediaDataList;
	}

	/**
	 * @param accountDemo
	 *            the accountDemo to set
	 */
	public void setAccountDemo(DemographicsTO accountDemo) {
		this.accountDemo = accountDemo;
	}

	/**
	 * @return the accountDemo
	 */
	public DemographicsTO getAccountDemo() {
		return accountDemo;
	}

}
