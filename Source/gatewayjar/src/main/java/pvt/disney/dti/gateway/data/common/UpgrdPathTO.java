package pvt.disney.dti.gateway.data.common;

import java.math.BigInteger;
import java.util.GregorianCalendar;

public class UpgrdPathTO {
	BigInteger pathId;
	String pathNm;
	String activeInd;
	BigInteger creatorId;
	GregorianCalendar creationDate;
	BigInteger lastUserId;
	GregorianCalendar lastUpdate;
	String ownerId;
	public BigInteger getPathId() {
		return pathId;
	}
	public void setPathId(BigInteger pathId) {
		this.pathId = pathId;
	}
	public String getPathNm() {
		return pathNm;
	}
	public void setPathNm(String pathNm) {
		this.pathNm = pathNm;
	}
	public String getActiveInd() {
		return activeInd;
	}
	public void setActiveInd(String activeInd) {
		this.activeInd = activeInd;
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
