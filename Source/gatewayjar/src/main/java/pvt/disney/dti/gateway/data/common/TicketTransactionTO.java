package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * This class represents the Transaction identifier passed from the ticket provider.
 * 
 * @author lewit019
 */
public class TicketTransactionTO implements Serializable, Cloneable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The enumeration of possible deployment environments. */
	public enum TransactionIDType {
		TRANSDSSN,
		TRANNID,
		UNDEFINED
	}

	/** The transaction ID type. */
	private TransactionIDType txnIdType = TransactionIDType.UNDEFINED;

	/** The tkt provider. */
	private String tktProvider;

	/** The dssn date. */
	private GregorianCalendar dssnDate;

	/** The dssn site. */
	private String dssnSite;

	/** The dssn station. */
	private String dssnStation;

	/** The dssn number. */
	private String dssnNumber;

	/** The tran nid. */
	private String tranNID;

	/**
	 * Overrides Object.clone() to provide a shallow copy of the TicketTransactionTO object.
	 * 
	 * @return TicketTransactionTO
	 */
	public TicketTransactionTO clone() throws CloneNotSupportedException {

		TicketTransactionTO aClone = new TicketTransactionTO();

		aClone.txnIdType = this.getTxnIdType();

		if (this.tktProvider != null) {
			aClone.tktProvider = this.tktProvider;
		}

		if (this.dssnDate != null) {
			aClone.dssnDate = this.dssnDate;
		}

		if (this.getDssnSite() != null) {
			aClone.dssnSite = this.getDssnSite();
		}

		if (this.getDssnStation() != null) {
			aClone.dssnStation = this.getDssnStation();
		}

		if (this.getDssnNumber() != null) {
			aClone.dssnNumber = this.getDssnNumber();
		}

		if (this.getTranNID() != null) {
			aClone.tranNID = this.getTranNID();
		}

		return aClone;

	}

	/**
	 * Gets the dssn date.
	 * 
	 * @return the dssnDate
	 */
	public GregorianCalendar getDssnDate() {
		return dssnDate;
	}

	/**
	 * Gets the dssn date string.
	 * 
	 * @return the dssn date string
	 */
	public String getDssnDateString() {
		String dtiFormattedDate;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dtiFormattedDate = sdf.format(dssnDate.getTime());

		return dtiFormattedDate;
	}

	/**
	 * Sets the dssn.
	 * 
	 * @param dssnDateIn
	 *            the dssn date in
	 * @param dssnSiteIn
	 *            the dssn site in
	 * @param dssnStationIn
	 *            the dssn station in
	 * @param dssnNumberIn
	 *            the dssn number in
	 */
	public void setDssn(GregorianCalendar dssnDateIn, String dssnSiteIn,
			String dssnStationIn, String dssnNumberIn) {
		dssnDate = dssnDateIn;
		dssnSite = dssnSiteIn;
		dssnStation = dssnStationIn;
		dssnNumber = dssnNumberIn;
		txnIdType = TransactionIDType.TRANSDSSN;
	}

	/**
	 * Sets the dssn date.
	 * 
	 * @param dssnDate
	 *            the dssnDate to set
	 */
	public void setDssnDate(GregorianCalendar dssnDate) {
		this.dssnDate = dssnDate;
		txnIdType = TransactionIDType.TRANSDSSN;
	}

	/**
	 * Gets the dssn number.
	 * 
	 * @return the dssnNumber
	 */
	public String getDssnNumber() {
		return dssnNumber;
	}

	/**
	 * Sets the dssn number.
	 * 
	 * @param dssnNumber
	 *            the dssnNumber to set
	 */
	public void setDssnNumber(String dssnNumber) {
		this.dssnNumber = dssnNumber;
	}

	/**
	 * Gets the dssn site.
	 * 
	 * @return the dssnSite
	 */
	public String getDssnSite() {
		return dssnSite;
	}

	/**
	 * Sets the dssn site.
	 * 
	 * @param dssnSite
	 *            the dssnSite to set
	 */
	public void setDssnSite(String dssnSite) {
		this.dssnSite = dssnSite;
	}

	/**
	 * Gets the dssn station.
	 * 
	 * @return the dssnStation
	 */
	public String getDssnStation() {
		return dssnStation;
	}

	/**
	 * Sets the dssn station.
	 * 
	 * @param dssnStation
	 *            the dssnStation to set
	 */
	public void setDssnStation(String dssnStation) {
		this.dssnStation = dssnStation;
	}

	/**
	 * Gets the tran nid.
	 * 
	 * @return the tranNID
	 */
	public String getTranNID() {
		return tranNID;
	}

	/**
	 * Sets the tran nid.
	 * 
	 * @param tranNID
	 *            the tranNID to set
	 */
	public void setTranNID(String tranNID) {
		txnIdType = TransactionIDType.TRANNID;
		this.tranNID = tranNID;
	}

	/**
	 * Gets the tkt provider.
	 * 
	 * @return the tktProvider
	 */
	public String getTktProvider() {
		return tktProvider;
	}

	/**
	 * Sets the tkt provider.
	 * 
	 * @param tktProvider
	 *            the tktProvider to set
	 */
	public void setTktProvider(String tktProvider) {
		this.tktProvider = tktProvider;
	}

	/**
	 * 
	 * @return the transaction identifier type.
	 */
	public TransactionIDType getTxnIdType() {
		return txnIdType;
	}

}
