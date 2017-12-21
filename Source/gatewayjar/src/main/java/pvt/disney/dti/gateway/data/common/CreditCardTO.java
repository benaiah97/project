package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * Class that represents a credit card payment type.
 * 
 * @author lewit019
 * 
 */
public class CreditCardTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

  // Fields generalized to multiple credit card situations
	
	/** Defines what possible variations of payments there are. */
	public enum CreditCardType {
		CCMANUAL,
		CCSWIPE,
		UNSUPPORTED
	};

  private CreditCardType ccManualOrSwipe = CreditCardType.UNSUPPORTED;
	
	/** Pre-approved Credit Card (as of 2.16.2) */
	private Boolean ccPreApproved;
	
	// Fields for a CCMANUAL payment type.
	
	/** Credit card number. */
	private String ccNbr;
	/** Expiration date of the credit card. */
	private String ccExpiration;
	/** Name of the credit card (generally "MasterCard" or "Amex" or Billing Name) */
	private String ccName;
	/** Billing street of the credit card. */
	private String ccStreet;
	/** Billing ZIP code of the credit card. */
	private String ccZipCode;
	/** Card-holder Authentication Verification Value. */
	private String ccCAVV;
	/** eCommerce indicator. */
	private String ccEcommerce;
	/** Credit card type.(VI for visa) */
	private String ccType;
	/** SHA-1 Hash of Credit Card (as of 2.16.2) */
	private String ccSha1;
	/** CCSubCode (as of 2.16.2) card type */
	private String ccSubCode;
	
	// Fields for a CCSWIPE or CCWireless (as of 2.12) payment type.
	/** Track one of a credit card magnetic stripe. */
	private String ccTrack1;
	/** Track two of a credit card magnetic stripe. */
	private String ccTrack2;
	/** The POS Terminal ID (as of 2.12) */
	private String posTermID;
	/** The External Device Serial Number (as of 2.12) */
	private String extnlDevSerial;
	/** Is the card present situation wireless? (as of 2.12) */
	private boolean isWireless = false;

	// Fields shared by both payment types.
	/** Card Verification Value. */
	private String ccVV;

	// Fields used in responses
	/** Credit card authorization code */
	private String ccAuthCode;
	/** Credit card authorization number. */
	private String ccAuthNumber;
	/** Authorization system response. */
	private String ccAuthSysResponse;
	/** Credit card number played back from the provider system. */
	private String ccNumber;
	
	/** The xid for 3DS authentication. */
	private String xid;
	

	/**
	 * @return the ccAuthCode
	 */
	public String getCcAuthCode() {
		return ccAuthCode;
	}

	/**
	 * @return the ccAuthNumber
	 */
	public String getCcAuthNumber() {
		return ccAuthNumber;
	}

	/**
	 * @return the ccAuthSysResponse
	 */
	public String getCcAuthSysResponse() {
		return ccAuthSysResponse;
	}

	/**
	 * @return the ccNumber
	 */
	public String getCcNumber() {
		return ccNumber;
	}

	/**
	 * @param ccAuthCode
	 *            the ccAuthCode to set
	 */
	public void setCcAuthCode(String ccAuthCode) {
		this.ccAuthCode = ccAuthCode;
	}

	/**
	 * @param ccAuthNumber
	 *            the ccAuthNumber to set
	 */
	public void setCcAuthNumber(String ccAuthNumber) {
		this.ccAuthNumber = ccAuthNumber;
	}

	/**
	 * @param ccAuthSysResponse
	 *            the ccAuthSysResponse to set
	 */
	public void setCcAuthSysResponse(String ccAuthSysResponse) {
		this.ccAuthSysResponse = ccAuthSysResponse;
	}

	/**
	 * @param ccNumber
	 *            the ccNumber to set
	 */
	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}

	/**
	 * @return the ccEcommerce
	 */
	public String getCcEcommerce() {
		return ccEcommerce;
	}

	/**
	 * @return the ccExpiration
	 */
	public String getCcExpiration() {
		return ccExpiration;
	}

	/**
	 * @return the ccManualOrSwipe
	 */
	public CreditCardType getCcManualOrSwipe() {
		return ccManualOrSwipe;
	}

	/**
	 * @return the ccName
	 */
	public String getCcName() {
		return ccName;
	}

	/**
	 * @return the ccNbr
	 */
	public String getCcNbr() {
		return ccNbr;
	}

	/**
	 * @return the ccStreet
	 */
	public String getCcStreet() {
		return ccStreet;
	}

	/**
	 * @return the ccTrack1
	 */
	public String getCcTrack1() {
		return ccTrack1;
	}

	/**
	 * @return the ccTrack2
	 */
	public String getCcTrack2() {
		return ccTrack2;
	}

	/**
	 * @return the ccType
	 */
	public String getCcType() {
		return ccType;
	}

	/**
	 * @return the ccVV
	 */
	public String getCcVV() {
		return ccVV;
	}

	/**
	 * @return the ccZipCode
	 */
	public String getCcZipCode() {
		return ccZipCode;
	}

	/**
	 * @param ccEcommerce
	 *            the ccEcommerce to set
	 */
	public void setCcEcommerce(String ccEcommerce) {
		this.ccEcommerce = ccEcommerce;
	}

	/**
	 * @param ccExpiration
	 *            the ccExpiration to set
	 */
	public void setCcExpiration(String ccExpiration) {
		this.ccExpiration = ccExpiration;
	}

	/**
	 * @param ccManualOrSwipe
	 *            the ccManualOrSwipe to set
	 */
	public void setCcManualOrSwipe(CreditCardType ccManualOrSwipe) {
		this.ccManualOrSwipe = ccManualOrSwipe;
	}

	/**
	 * @param ccName
	 *            the ccName to set
	 */
	public void setCcName(String ccName) {
		this.ccName = ccName;
	}

	/**
	 * @param ccStreet
	 *            the ccStreet to set
	 */
	public void setCcStreet(String ccStreet) {
		this.ccStreet = ccStreet;
	}

	/**
	 * @param ccTrack2
	 *            the ccTrack2 to set
	 */
	public void setCcTrack2(String ccTrack2) {
		this.ccTrack2 = ccTrack2;
	}

	/**
	 * @param ccType
	 *            the ccType to set
	 */
	public void setCcType(String ccType) {
		this.ccType = ccType;
	}

	/**
	 * @param ccVV
	 *            the ccVV to set
	 */
	public void setCcVV(String ccVV) {
		this.ccVV = ccVV;
	}

	/**
	 * @param ccZipCode
	 *            the ccZipCode to set
	 */
	public void setCcZipCode(String ccZipCode) {
		this.ccZipCode = ccZipCode;
	}

	/**
	 * @param ccNbr
	 *            the ccNbr to set
	 */
	public void setCcNbr(String ccNbr) {
		this.ccNbr = ccNbr;
		ccManualOrSwipe = CreditCardType.CCMANUAL;
	}

	/**
	 * @param ccTrack1
	 *            the ccTrack1 to set
	 */
	public void setCcTrack1(String ccTrack1) {
		this.ccTrack1 = ccTrack1;
		ccManualOrSwipe = CreditCardType.CCSWIPE;
	}

	/**
	 * @return the ccCAVV
	 */
	public String getCcCAVV() {
		return ccCAVV;
	}

	/**
	 * @param ccCAVV
	 *            the ccCAVV to set
	 */
	public void setCcCAVV(String ccCAVV) {
		this.ccCAVV = ccCAVV;
	}

	/**
	 * @return the posTermID
	 */
	public String getPosTermID() {
		return posTermID;
	}

	/**
	 * @param posTermID
	 *            the posTermID to set
	 */
	public void setPosTermID(String posTermID) {
		this.posTermID = posTermID;
	}

	/**
	 * @return the extnlDevSerial
	 */
	public String getExtnlDevSerial() {
		return extnlDevSerial;
	}

	/**
	 * @param extnlDevSerial
	 *            the extnlDevSerial to set
	 */
	public void setExtnlDevSerial(String extnlDevSerial) {
		this.extnlDevSerial = extnlDevSerial;
	}

	/**
	 * @return the isWireless
	 */
	public boolean isWireless() {
		return isWireless;
	}

	/**
	 * @param isWireless
	 *            the isWireless to set
	 */
	public void setWireless(boolean isWireless) {
		this.isWireless = isWireless;
	}

  /**
   * @return the preApprovedCC
   */
  public Boolean getPreApprovedCC() {
    return ccPreApproved;
  }

  /**
   * @param preApprovedCC the preApprovedCC to set
   */
  public void setPreApprovedCC(Boolean preApprovedCC) {
    this.ccPreApproved = preApprovedCC;
  }

  /**
   * @return the sha1
   */
  public String getCcSha1() {
    return ccSha1;
  }

  /**
   * @param sha1 the sha1 to set
   */
  public void setCcSha1(String sha1) {
    this.ccSha1 = sha1;
  }

  /**
   * @return the ccSubCode
   */
  public String getCcSubCode() {
    return ccSubCode;
  }

  /**
   * @param ccSubCode the ccSubCode to set
   */
  public void setCcSubCode(String ccSubCode) {
    this.ccSubCode = ccSubCode;
  }

   /**
    * @return the xid
    */
   public String getXid() {
      return xid;
   }

   /**
    * @param xid
    *           the xid to set
    */
   public void setXid(String xid) {
      this.xid = xid;
   }
  

}
