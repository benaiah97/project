package pvt.disney.dti.gateway.dao.data;

/**
 * This class represents the result set for a database ticket attribute query.
 * 
 * @author lewit019
 * 
 */
public class DBTicketAttributes {

	/** The Ticket Code. */
	private String tktCode;

	/** The Age Group. */
	private String ageGroup;

	/** The Media Type. */
	private String mediaType;

	/** The Pass Type. */
	private String passType;

	/** The Pass Class */
	private String passClass;

	/** The Resident. */
	private String resident;

	/** The Pass Renew. */
	private String passRenew;

	/**
	 * @return the age group.
	 */
	public String getAgeGroup() {
		return ageGroup;
	}

	/**
	 * Sets the age group
	 * 
	 * @param ageGroup
	 *            the age group to set
	 */
	public void setAgeGroup(String ageGroup) {
		this.ageGroup = ageGroup;
	}

	/**
	 * 
	 * @return the media type
	 */
	public String getMediaType() {
		return mediaType;
	}

	/**
	 * Sets the media type.
	 * 
	 * @param mediaType
	 *            the media type to set.
	 */
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	/**
	 * 
	 * @return gets the pass class.
	 */
	public String getPassClass() {
		return passClass;
	}

	/**
	 * Sets the pass class
	 * 
	 * @param passClass
	 *            the pass class to set
	 */
	public void setPassClass(String passClass) {
		this.passClass = passClass;
	}

	/**
	 * 
	 * @return the pass renewal string
	 */
	public String getPassRenew() {
		return passRenew;
	}

	/**
	 * Sets the pass renewal string
	 * 
	 * @param passRenew
	 *            the pass renewal string to set.
	 */
	public void setPassRenew(String passRenew) {
		this.passRenew = passRenew;
	}

	/**
	 * 
	 * @return the pass type
	 */
	public String getPassType() {
		return passType;
	}

	/**
	 * Sets the pass type
	 * 
	 * @param passType
	 *            the pass type to set
	 */
	public void setPassType(String passType) {
		this.passType = passType;
	}

	/**
	 * 
	 * @return the resident string
	 */
	public String getResident() {
		return resident;
	}

	/**
	 * Sets the resident string
	 * 
	 * @param resident
	 *            the resident string to set.
	 */
	public void setResident(String resident) {
		this.resident = resident;
	}

	/**
	 * 
	 * @return the ticket code
	 */
	public String getTktCode() {
		return tktCode;
	}

	/**
	 * Sets the ticket code
	 * 
	 * @param tktCode
	 *            the ticket code to set.
	 */
	public void setTktCode(String tktCode) {
		this.tktCode = tktCode;
	}

}
