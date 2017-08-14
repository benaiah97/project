package pvt.disney.dti.gateway.data.common;

import java.math.BigInteger;
import java.util.GregorianCalendar;

public class UpgrdPathSeqTO {
	private BigInteger pathId;
	private BigInteger pathIdSeqNb;
	private String activeInd;
	private String dayClass;
	private String daySubclass;
	private BigInteger creatorId;
	private GregorianCalendar creationDate;
	private BigInteger lastUserId;
	private GregorianCalendar lastUpdate;
	private String ownerId;

	public BigInteger getPathId() {
		return pathId;
	}

	public void setPathId(BigInteger pathId) {
		this.pathId = pathId;
	}

	public BigInteger getPathIdSeqNb() {
		return pathIdSeqNb;
	}

	public void setPathIdSeqNb(BigInteger pathIdSeqNb) {
		this.pathIdSeqNb = pathIdSeqNb;
	}

	public String getActiveInd() {
		return activeInd;
	}

	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
	}

	public String getDayClass() {
		return dayClass;
	}

	public void setDayClass(String dayClass) {
		this.dayClass = dayClass;
	}

	public String getDaySubclass() {
		return daySubclass;
	}

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
