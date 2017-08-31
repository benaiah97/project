package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;


/**
 * The Class DemographicDataTO.
 */
public class DemographicDataTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5736485738L;

	/** The association id. */
	private String associationId;
	
	/** The clean missing fields. */
	private boolean cleanMissingFields;

	/**
	 * Gets the association id.
	 *
	 * @return the association id
	 */
	public String getAssociationId() {
		return associationId;
	}

	/**
	 * Sets the association id.
	 *
	 * @param associationId the new association id
	 */
	public void setAssociationId(String associationId) {
		this.associationId = associationId;
	}

	/**
	 * Checks if is clean missing fields.
	 *
	 * @return true, if is clean missing fields
	 */
	public boolean isCleanMissingFields() {
		return cleanMissingFields;
	}

	/**
	 * Sets the clean missing fields.
	 *
	 * @param cleanMissingFields the new clean missing fields
	 */
	public void setCleanMissingFields(boolean cleanMissingFields) {
		this.cleanMissingFields = cleanMissingFields;
	}
	
	
}
