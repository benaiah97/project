package pvt.disney.dti.gateway.data.common;

import java.math.BigInteger;
import java.util.GregorianCalendar;

/**
 * The Class UpgrdPathSeqTO.
 * @author MISHP012
 *
 */
public class UpgrdPathSeqTO {
	
	/** The path id. */
	private BigInteger pathId;
	
	/** The path id seq nb. */
	private BigInteger pathIdSeqNb;
	
	/** The active ind. */
	private String activeInd;
	
	/** The day class. */
	private String dayClass;
	
	/** The day subclass. */
	private String daySubclass;
	
	/** The creator id. */
	private BigInteger creatorId;
	
	/** The creation date. */
	private GregorianCalendar creationDate;
	
	/** The last user id. */
	private BigInteger lastUserId;
	
	/** The last update. */
	private GregorianCalendar lastUpdate;
	
	/** The owner id. */
	private String ownerId;

	/**
	 * Gets the path id.
	 *
	 * @return the path id
	 */
	public BigInteger getPathId() {
		return pathId;
	}

	/**
	 * Sets the path id.
	 *
	 * @param pathId the new path id
	 */
	public void setPathId(BigInteger pathId) {
		this.pathId = pathId;
	}

	/**
	 * Gets the path id seq nb.
	 *
	 * @return the path id seq nb
	 */
	public BigInteger getPathIdSeqNb() {
		return pathIdSeqNb;
	}

	/**
	 * Sets the path id seq nb.
	 *
	 * @param pathIdSeqNb the new path id seq nb
	 */
	public void setPathIdSeqNb(BigInteger pathIdSeqNb) {
		this.pathIdSeqNb = pathIdSeqNb;
	}

	/**
	 * Gets the active ind.
	 *
	 * @return the active ind
	 */
	public String getActiveInd() {
		return activeInd;
	}

	/**
	 * Sets the active ind.
	 *
	 * @param activeInd the new active ind
	 */
	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}

	/**
	 * Gets the day class.
	 *
	 * @return the day class
	 */
	public String getDayClass() {
		return dayClass;
	}

	/**
	 * Sets the day class.
	 *
	 * @param dayClass the new day class
	 */
	public void setDayClass(String dayClass) {
		this.dayClass = dayClass;
	}

	/**
	 * Gets the day subclass.
	 *
	 * @return the day subclass
	 */
	public String getDaySubclass() {
		return daySubclass;
	}

	/**
	 * Sets the day subclass.
	 *
	 * @param daySubclass the new day subclass
	 */
	public void setDaySubclass(String daySubclass) {
		this.daySubclass = daySubclass;
	}

	public BigInteger getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(BigInteger creatorId) {
		this.creatorId = creatorId;
	}

	public GregorianCalendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(GregorianCalendar creationDate) {
		this.creationDate = creationDate;
	}

	public BigInteger getLastUserId() {
		return lastUserId;
	}

	public void setLastUserId(BigInteger lastUserId) {
		this.lastUserId = lastUserId;
	}

	public GregorianCalendar getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(GregorianCalendar lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}
