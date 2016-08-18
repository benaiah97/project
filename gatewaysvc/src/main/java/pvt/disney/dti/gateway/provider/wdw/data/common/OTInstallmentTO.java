package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.provider.wdw.data.common.OTCreditCardTO.CreditCardEntryType;

/**
 * @author lewit019
 * @since 2.15
 */
public class OTInstallmentTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int CONTRACTALPHACODE = 3;

	private CreditCardEntryType ccManualOrSwiped = CreditCardEntryType.UNSUPPORTED;

	// Fields for a MANUAL payment type.
	/** Credit card number. */
	private String cardNumber;

	/** Expiration date of the credit card. */
	private String cardExpDate;

	/** Card Holder Name */
	private String cardHolderName;

	// Fields for a SWIPED payment type.
	/** Track one of a credit card magnetic stripe. */
	private String track1;

	/** Track two of a credit card magnetic stripe. */
	private String track2;

	public final static int CLIENTUNIQUEID = 0;

	/** The array list of Demographic data fields. */
	private ArrayList<OTFieldTO> demographicData = new ArrayList<OTFieldTO>();

	// Fields for output
	/** Contract ID */
	private String contractId;

	/**
	 * @return the demographicData
	 */
	public ArrayList<OTFieldTO> getDemographicData() {
		return demographicData;
	}

	/**
	 * @param demographicData
	 *            the demographicData to set
	 */
	public void setDemographicData(ArrayList<OTFieldTO> demographicData) {
		this.demographicData = demographicData;
	}

	/**
	 * @param cardNumber
	 *            the cardNumber to set
	 */
	public void setCardNumber(String ccNbr) {
		this.cardNumber = ccNbr;
		ccManualOrSwiped = CreditCardEntryType.MANUAL;
	}

	/**
	 * @param track1
	 *            the track1 to set
	 */
	public void setTrack1(String ccTrack1) {
		this.track1 = ccTrack1;
		ccManualOrSwiped = CreditCardEntryType.SWIPED;
	}

	/**
	 * @return the cardExpDate
	 */
	public String getCardExpDate() {
		return cardExpDate;
	}

	/**
	 * @param cardExpDate
	 *            the cardExpDate to set
	 */
	public void setCardExpDate(String cardExpDate) {
		this.cardExpDate = cardExpDate;
	}

	/**
	 * @return the cardHolderName
	 */
	public String getCardHolderName() {
		return cardHolderName;
	}

	/**
	 * @param cardHolderName
	 *            the cardHolderName to set
	 */
	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	/**
	 * @return the track2
	 */
	public String getTrack2() {
		return track2;
	}

	/**
	 * @param track2
	 *            the track2 to set
	 */
	public void setTrack2(String track2) {
		this.track2 = track2;
	}

	/**
	 * @return the contract alpha code
	 */
	public static int getContractalphacode() {
		return CONTRACTALPHACODE;
	}

	/**
	 * @return the ccManualOrSwiped
	 */
	public CreditCardEntryType getCcManualOrSwiped() {
		return ccManualOrSwiped;
	}

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * @return the track1
	 */
	public String getTrack1() {
		return track1;
	}

	/**
	 * @return the clientuniqueid
	 */
	public static int getClientuniqueid() {
		return CLIENTUNIQUEID;
	}

	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}

	/**
	 * @param contractId
	 *            the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

}
