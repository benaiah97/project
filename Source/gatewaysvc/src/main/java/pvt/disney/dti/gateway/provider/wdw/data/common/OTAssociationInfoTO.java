package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents the Omni Ticket Association Info Transfer Object.
 * 
 * @author lewit019
 * 
 */
public class OTAssociationInfoTO implements Serializable {

	/** Standard serial version UID. */
	public final static long serialVersionUID = 9129231995L;

	/** The array list of Demographic data fields. */
	private ArrayList<OTFieldTO> demographicData = new ArrayList<OTFieldTO>();

	/** The association identifier. */
	private Integer associationId;

	/** The member identifier. */
	private Integer memberId;

	/** The member identifier string values. */
	private String memberField;

	/** The permission to allow member creation. */
	private boolean allowMemberCreation = false;

	/**
	 * @return the associationId
	 */
	public Integer getAssociationId() {
		return associationId;
	}

	/**
	 * @return the memberField
	 */
	public String getMemberField() {
		return memberField;
	}

	/**
	 * @return the memberId
	 */
	public Integer getMemberId() {
		return memberId;
	}

	/**
	 * @param associationId
	 *            the associationId to set
	 */
	public void setAssociationId(Integer associationId) {
		this.associationId = associationId;
	}

	/**
	 * @param memberField
	 *            the memberField to set
	 */
	public void setMemberField(String memberField) {
		this.memberField = memberField;
	}

	/**
	 * @param memberId
	 *            the memberId to set
	 */
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	/**
	 * @return true if member creation is allowed
	 */
	public boolean isMemberCreationAllowed() {
		return allowMemberCreation;
	}

	/**
	 * @param allowMemberCreation
	 *            the allowMemberCreation value to be set.
	 */
	public void setAllowMemberCreation(boolean allowMemberCreation) {
		this.allowMemberCreation = allowMemberCreation;
	}

	/**
	 * @return the array of Omni Ticket Field Transfer Objects for demographic data.
	 */
	public ArrayList<OTFieldTO> getDemographicData() {
		return demographicData;
	}

}
