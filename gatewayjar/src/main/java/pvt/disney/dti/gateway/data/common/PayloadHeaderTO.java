package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * This class is the object which represents the payload header of a DTI transaction request or response.
 * 
 * @author lewit019
 */
public class PayloadHeaderTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	// Request Fields
	/** The payload id. */
	private String payloadID;

	/** The target. */
	private String target;

	/** The version. */
	private String version;

	/** The comm protocol. */
	private String commProtocol;

	/** The comm method. */
	private String commMethod;

	/** The transmit date time. */
	private GregorianCalendar transmitDateTime;

	/** The tkt seller. */
	private TktSellerTO tktSeller;

	/** The command count. */
	private BigInteger commandCount;

	// Database supplied fields
	/** The db entity. */
	private EntityTO dbEntity;

	// Response Fields
	/** The ts payload id. */
	private String tsPayloadID;

	/** The tkt broker. */
	private String tktBroker;

	/**
	 * Gets a copy of this object.
	 * 
	 * @return the copy
	 */
	public PayloadHeaderTO getCopy() {
		PayloadHeaderTO copy = new PayloadHeaderTO();
		copy.setPayloadID(this.getPayloadID());
		copy.setTarget(this.getTarget());
		copy.setVersion(this.getVersion());
		copy.setCommProtocol(this.getCommProtocol());
		copy.setCommMethod(this.getCommMethod());
		copy.setTransmitDateTime(this.getTransmitDateTime());
		copy.setTktSeller(this.getTktSeller().getCopy());
		return copy;
	}

	/**
	 * Reformats this object in an easy-to-read/log string.
	 * 
	 * @return the string
	 */
	public String toString() {
		StringBuffer output = new StringBuffer();

		output.append("PayloadHeaderTO:\n\tpayloadId=[");

		if (payloadID == null) output.append("null");
		else output.append(payloadID);

		output.append("]\n\ttarget=[");
		if (target == null) output.append("null");
		else output.append(target);

		output.append("]\n\tversion=[");
		if (version == null) output.append("null");
		else output.append(version);

		output.append("]\n\tcommProtocol=[");
		if (commProtocol == null) output.append("null");
		else output.append(commProtocol);

		output.append("]\n\tcommMethod=[");
		if (commMethod == null) output.append("null");
		else output.append(commMethod);

		output.append("]\n\ttransmitDateTime=[");
		if (transmitDateTime == null) output.append("null");
		else {
			try {
				output.append(transmitDateTime.get(Calendar.YEAR) + "/");
				output.append(transmitDateTime.get(Calendar.MONTH) + "/");
				output.append(transmitDateTime.get(Calendar.DAY_OF_MONTH) + " ");
				output.append(transmitDateTime.get(Calendar.HOUR_OF_DAY) + ":");
				output.append(transmitDateTime.get(Calendar.MINUTE) + ":");
				output.append(transmitDateTime.get(Calendar.SECOND));
			}
			catch (NullPointerException npe) {
				output.append("Incomplete");
			}
		}
		output.append("]\n");

		return output.toString();
	}

	/**
	 * Gets the comm method.
	 * 
	 * @return commMethod
	 */
	public String getCommMethod() {
		return commMethod;
	}

	/**
	 * Gets the comm protocol.
	 * 
	 * @return commProtocol
	 */
	public String getCommProtocol() {
		return commProtocol;
	}

	/**
	 * Gets the payload id.
	 * 
	 * @return payloadID
	 */
	public String getPayloadID() {
		return payloadID;
	}

	/**
	 * Gets the target.
	 * 
	 * @return target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * Gets the version.
	 * 
	 * @return version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the comm method.
	 * 
	 * @param string
	 *            Comm Method to be set.
	 */
	public void setCommMethod(String string) {
		commMethod = string;
	}

	/**
	 * Sets the comm protocol.
	 * 
	 * @param string
	 *            Comm Protocol to be set.
	 */
	public void setCommProtocol(String string) {
		commProtocol = string;
	}

	/**
	 * Sets the payload id.
	 * 
	 * @param string
	 *            Payload ID to be set.
	 */
	public void setPayloadID(String string) {
		payloadID = string;
	}

	/**
	 * Sets the target.
	 * 
	 * @param string
	 *            Target to be set.
	 */
	public void setTarget(String string) {
		target = string;
	}

	/**
	 * Sets the version.
	 * 
	 * @param string
	 *            Version to be set.
	 */
	public void setVersion(String string) {
		version = string;
	}

	/**
	 * Gets the tkt seller.
	 * 
	 * @return the tktSeller
	 */
	public TktSellerTO getTktSeller() {
		return tktSeller;
	}

	/**
	 * Sets the tkt seller.
	 * 
	 * @param tktSeller
	 *            the tktSeller to set
	 */
	public void setTktSeller(TktSellerTO tktSeller) {
		this.tktSeller = tktSeller;
	}

	/**
	 * Gets the ts payload id.
	 * 
	 * @return the tsPayloadID
	 */
	public String getTsPayloadID() {
		return tsPayloadID;
	}

	/**
	 * Sets the ts payload id.
	 * 
	 * @param tsPayloadID
	 *            the tsPayloadID to set
	 */
	public void setTsPayloadID(String tsPayloadID) {
		this.tsPayloadID = tsPayloadID;
	}

	/**
	 * Gets the transmit date time.
	 * 
	 * @return the transmitDateTime
	 */
	public GregorianCalendar getTransmitDateTime() {
		return transmitDateTime;
	}

	/**
	 * Sets the transmit date time.
	 * 
	 * @param transmitDateTime
	 *            the transmitDateTime to set
	 */
	public void setTransmitDateTime(GregorianCalendar transmitDateTime) {
		this.transmitDateTime = transmitDateTime;
	}

	/**
	 * Gets the tkt broker.
	 * 
	 * @return the tktBroker
	 */
	public String getTktBroker() {
		return tktBroker;
	}

	/**
	 * Sets the tkt broker.
	 * 
	 * @param tktBroker
	 *            the tktBroker to set
	 */
	public void setTktBroker(String tktBroker) {
		this.tktBroker = tktBroker;
	}

	/**
	 * Gets the command count.
	 * 
	 * @return the commandCount
	 */
	public BigInteger getCommandCount() {
		return commandCount;
	}

	/**
	 * Sets the command count.
	 * 
	 * @param commandCount
	 *            the commandCount to set
	 */
	public void setCommandCount(BigInteger commandCount) {
		this.commandCount = commandCount;
	}

	/**
	 * Gets the db entity.
	 * 
	 * @return the dbEntity
	 */
	public EntityTO getDbEntity() {
		return dbEntity;
	}

	/**
	 * Sets the db entity.
	 * 
	 * @param dbEntity
	 *            the dbEntity to set
	 */
	public void setDbEntity(EntityTO dbEntity) {
		this.dbEntity = dbEntity;
	}

}
