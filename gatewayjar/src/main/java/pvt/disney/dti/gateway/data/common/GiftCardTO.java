package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

/**
 * The Class GiftCardTO represents a gift card payment.
 * 
 * @author lewit19
 */
public class GiftCardTO implements Serializable {

	/** Serial Version UID. */
	private static final long serialVersionUID = 9129231995L;

	/**
	 * Defines what possible variations of payments there are.
	 */
	public enum GiftCardType {
		GCMANUAL,
		GCSWIPE,
		UNSUPPORTED
	};

	/** The gc manual or swipe. */
	protected GiftCardType gcManualOrSwipe = GiftCardType.UNSUPPORTED;

	// Fields used in the request
	/** The gc nbr. */
	private String gcNbr;

	/** The gc start date. */
	private GregorianCalendar gcStartDate;

	/** The gc track1. */
	private String gcTrack1;

	/** The gc track2. */
	private String gcTrack2;

	// Fields used in the response
	/** The gc auth code. */
	private String gcAuthCode;

	/** The gc auth number. */
	private String gcAuthNumber;

	/** The gc auth sys response. */
	private String gcAuthSysResponse;

	/** The gc number. */
	private String gcNumber;

	/** The gc remaining balance. */
	private BigDecimal gcRemainingBalance;

	/** The gc promo exp date. */
	private GregorianCalendar gcPromoExpDate;

	/**
	 * Gets the gc auth code.
	 * 
	 * @return the gcAuthCode
	 */
	public String getGcAuthCode() {
		return gcAuthCode;
	}

	/**
	 * Gets the gc auth number.
	 * 
	 * @return the gcAuthNumber
	 */
	public String getGcAuthNumber() {
		return gcAuthNumber;
	}

	/**
	 * Gets the gc auth sys response.
	 * 
	 * @return the gcAuthSysResponse
	 */
	public String getGcAuthSysResponse() {
		return gcAuthSysResponse;
	}

	/**
	 * Gets the gc number.
	 * 
	 * @return the gcNumber
	 */
	public String getGcNumber() {
		return gcNumber;
	}

	/**
	 * Gets the gc promo exp date.
	 * 
	 * @return the gcPromoExpDate
	 */
	public GregorianCalendar getGcPromoExpDate() {
		return gcPromoExpDate;
	}

	/**
	 * Gets the gc remaining balance.
	 * 
	 * @return the gcRemainingBalance
	 */
	public BigDecimal getGcRemainingBalance() {
		return gcRemainingBalance;
	}

	/**
	 * Sets the gc auth code.
	 * 
	 * @param gcAuthCode
	 *            the gcAuthCode to set
	 */
	public void setGcAuthCode(String gcAuthCode) {
		this.gcAuthCode = gcAuthCode;
	}

	/**
	 * Sets the gc auth number.
	 * 
	 * @param gcAuthNumber
	 *            the gcAuthNumber to set
	 */
	public void setGcAuthNumber(String gcAuthNumber) {
		this.gcAuthNumber = gcAuthNumber;
	}

	/**
	 * Sets the gc auth sys response.
	 * 
	 * @param gcAuthSysResponse
	 *            the gcAuthSysResponse to set
	 */
	public void setGcAuthSysResponse(String gcAuthSysResponse) {
		this.gcAuthSysResponse = gcAuthSysResponse;
	}

	/**
	 * Sets the gc number.
	 * 
	 * @param gcNumber
	 *            the gcNumber to set
	 */
	public void setGcNumber(String gcNumber) {
		this.gcNumber = gcNumber;
	}

	/**
	 * Sets the gc promo exp date.
	 * 
	 * @param gcPromoExpDate
	 *            the gcPromoExpDate to set
	 */
	public void setGcPromoExpDate(GregorianCalendar gcPromoExpDate) {
		this.gcPromoExpDate = gcPromoExpDate;
	}

	/**
	 * Sets the gc remaining balance.
	 * 
	 * @param gcRemainingBalance
	 *            the gcRemainingBalance to set
	 */
	public void setGcRemainingBalance(BigDecimal gcRemainingBalance) {
		this.gcRemainingBalance = gcRemainingBalance;
	}

	/**
	 * Gets the gc nbr.
	 * 
	 * @return the gcNbr
	 */
	public String getGcNbr() {
		return gcNbr;
	}

	/**
	 * Gets the gc start date.
	 * 
	 * @return the gcStartDate
	 */
	public GregorianCalendar getGcStartDate() {
		return gcStartDate;
	}

	/**
	 * Gets the gc track1.
	 * 
	 * @return the gcTrack1
	 */
	public String getGcTrack1() {
		return gcTrack1;
	}

	/**
	 * Gets the gc track2.
	 * 
	 * @return the gcTrack2
	 */
	public String getGcTrack2() {
		return gcTrack2;
	}

	/**
	 * Sets the gc nbr.
	 * 
	 * @param gcNbr
	 *            the gcNbr to set
	 */
	public void setGcNbr(String gcNbr) {
		gcManualOrSwipe = GiftCardType.GCMANUAL;
		this.gcNbr = gcNbr;
	}

	/**
	 * Sets the gc start date.
	 * 
	 * @param gcStartDate
	 *            the gcStartDate to set
	 */
	public void setGcStartDate(GregorianCalendar gcStartDate) {
		this.gcStartDate = gcStartDate;
	}

	/**
	 * Sets the gc track1.
	 * 
	 * @param gcTrack1
	 *            the gcTrack1 to set
	 */
	public void setGcTrack1(String gcTrack1) {
		this.gcTrack1 = gcTrack1;
	}

	/**
	 * Sets the gc track2.
	 * 
	 * @param gcTrack2
	 *            the gcTrack2 to set
	 */
	public void setGcTrack2(String gcTrack2) {
		gcManualOrSwipe = GiftCardType.GCSWIPE;
		this.gcTrack2 = gcTrack2;
	}

	/**
	 * Gets the gc manual or swipe.
	 * 
	 * @return the gcManualOrSwipe
	 */
	public GiftCardType getGcManualOrSwipe() {
		return gcManualOrSwipe;
	}

}
