package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * The Class CosGrpTO.
 * 
 * @author moons012
 */
public class CosGrpTO implements Serializable {
	
	/**  Standard serial Version UID. */
	private static final long serialVersionUID = 9129431995L;
	
	/** The cosgrpid. */
	private int cosgrpid;
	
	/** The group name. */
	private String groupName;
	
	/** The endpoint url. */
	private String endpointUrl;
	
	/** The owner id. */
	private String ownerId;
	
	/**
	 * Instantiates a new cos grp TO.
	 */
	public CosGrpTO() {
	}

	/**
	 * Gets the cosgrpid.
	 *
	 * @return the cosgrpid
	 */
	public int getCosgrpid() {
		return cosgrpid;
	}

	/**
	 * Sets the cosgrpid.
	 *
	 * @param cosgrpid the new cosgrpid
	 */
	public void setCosgrpid(int cosgrpid) {
		this.cosgrpid = cosgrpid;
	}

	/**
	 * Gets the group name.
	 *
	 * @return the group name
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Sets the group name.
	 *
	 * @param groupName the new group name
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Gets the endpoint url.
	 *
	 * @return the endpoint url
	 */
	public String getEndpointUrl() {
		return endpointUrl;
	}

	/**
	 * Sets the endpoint url.
	 *
	 * @param endpointUrl the new endpoint url
	 */
	public void setEndpointUrl(String endpointUrl) {
		this.endpointUrl = endpointUrl;
	}

	/**
	 * Gets the owner id.
	 *
	 * @return the owner id
	 */
	public String getOwnerId() {
		return ownerId;
	}

	/**
	 * Sets the owner id.
	 *
	 * @param ownerId the new owner id
	 */
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}
