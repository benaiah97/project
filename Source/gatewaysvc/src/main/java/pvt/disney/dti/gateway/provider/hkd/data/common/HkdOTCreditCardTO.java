package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO;

/**
 * Class that represents an Omni Ticket Credit Card payment type.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class HkdOTCreditCardTO implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = 9129231995L;

  /** Credit card type. */
  public final static int CREDITCARDTYPE = 0;

  /** Gift card type. */
  public final static int GIFTTCARDTYPE = 1;

  /** SHA-1 card type. (As of 2.16.2, JTL) */
  public final static int SHA1CARDTYPE = 2;

  /** Wireless credit card type. */
  public final static int WIRELESSCREDITCARDTYPE = 3;

  /** Defines what possible variations of payments there are. */
  public enum CreditCardEntryType {
    MANUAL, SWIPED, UNSUPPORTED
  };

  private CreditCardEntryType ccManualOrSwiped = CreditCardEntryType.UNSUPPORTED;

  // Fields for a MANUAL payment type.
  /** Credit card number. */
  private String cardNumber;

  /** Expiration date of the credit card. */
  private String cardExpDate;

  /** Secure Hash Algorithm 1 (as of 2.16.2, JTL). */
  private String sha1;

  /** SubCode card type for stats when SHA1 is submitted (as of 2.16.2, JTL). */
  private String subCode;

  /** Pre-approved credit card (as of 2.16.2, JTL). */
  private Boolean isPreApproved;

  /** Authorization Code (as of 2.16.2, JTL). */
  private String authCode;

  /** Card Verification Value. */
  private String cVV;

  /** Billing street of the credit card. */
  private String avs_AvsStreet;

  /** Billing zip code of the credit card. */
  private String avs_AvsZipCode;

  /** Cardholder Authentication Verification Value format. */
  private String cAVVFormat;

  /** Cardholder Authentication Verification Value value. */
  private String cAVVValue;

  /** eCommerce indicator. */
  private String eCommerceValue;

  /** Gift Card Indicator. */
  private boolean giftCardIndicator = false;

  /** Wireless indicator */
  private boolean isWireless = false;

  /** SHA1 indicator (as of 2.16.2, JTL) */
  private boolean isSHA1 = false;

  // Fields for a SWIPED payment type.
  /** Track one of a credit card magnetic stripe. */
  private String track1;

  /** Track two of a credit card magnetic stripe. */
  private String track2;

  /** The POS terminal identifier (as of 2.12) */
  private String posTerminalId;

  /** The External Device Serial identifier number (as of 2.12) */
  private String externalDeviceSerialId;

  // Fields used in both requests and responses.
  /** Is this a gift card. */
  private Boolean isGiftCard;

  // Fields used in responses
  /** Authorization error code. */
  private String authErrorCode;

  /** Credit card authorization number. */
  private String authNumber;

  /** Credit card authorization message */
  private String authMessage;

  /** Credit card number played back from the provider system. */
  private String ccNumber;

  /** Retrieval reference number. */
  private String rRN;

  /** Remaining balance. */
  private BigDecimal remainingBalance;

  /** Promotion Expiration date */
  private GregorianCalendar promoExpDate;

  /**
   * @return the authNumber
   */
  public String getAuthNumber() {
    return authNumber;
  }

  /**
   * @return the ccNumber
   */
  public String getCcNumber() {
    return ccNumber;
  }

  /**
   * @param authNumber
   *          the authNumber to set
   */
  public void setAuthNumber(String ccAuthNumber) {
    this.authNumber = ccAuthNumber;
  }

  /**
   * @param ccNumber
   *          the ccNumber to set
   */
  public void setCcNumber(String ccNumber) {
    this.ccNumber = ccNumber;
  }

  /**
   * @return the eCommerceValue
   */
  public String getECommerceValue() {
    return eCommerceValue;
  }

  /**
   * @return the cardExpDate
   */
  public String getCardExpDate() {
    return cardExpDate;
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
   * @return the avs_AvsStreet
   */
  public String getAvs_AvsStreet() {
    return avs_AvsStreet;
  }

  /**
   * @return the track1
   */
  public String getTrack1() {
    return track1;
  }

  /**
   * @return the track2
   */
  public String getTrack2() {
    return track2;
  }

  /**
   * @return the cVV
   */
  public String getCVV() {
    return cVV;
  }

  /**
   * @return the avs_AvsZipCode
   */
  public String getAvs_AvsZipCode() {
    return avs_AvsZipCode;
  }

  /**
   * @param eCommerceValue
   *          the eCommerceValue to set
   */
  public void setECommerceValue(String ccEcommerce) {
    this.eCommerceValue = ccEcommerce;
  }

  /**
   * @param cardExpDate
   *          the cardExpDate to set
   */
  public void setCardExpDate(String ccExpiration) {
    this.cardExpDate = ccExpiration;
  }

  /**
   * @param avs_AvsStreet
   *          the avs_AvsStreet to set
   */
  public void setAvs_AvsStreet(String ccStreet) {
    this.avs_AvsStreet = ccStreet;
  }

  /**
   * @param track2
   *          the track2 to set
   */
  public void setTrack2(String ccTrack2) {
    this.track2 = ccTrack2;
  }

  /**
   * @param cVV
   *          the cVV to set
   */
  public void setCVV(String ccVV) {
    this.cVV = ccVV;
  }

  /**
   * @param avs_AvsZipCode
   *          the avs_AvsZipCode to set
   */
  public void setAvs_AvsZipCode(String ccZipCode) {
    this.avs_AvsZipCode = ccZipCode;
  }

  /**
   * @param cardNumber
   *          the cardNumber to set
   */
  public void setCardNumber(String ccNbr) {
    this.cardNumber = ccNbr;
    ccManualOrSwiped = CreditCardEntryType.MANUAL;
  }

  /**
   * @param track1
   *          the track1 to set
   */
  public void setTrack1(String ccTrack1) {
    this.track1 = ccTrack1;
    ccManualOrSwiped = CreditCardEntryType.SWIPED;
  }

  /**
   * @return the cAVVFormat
   */
  public String getCAVVFormat() {
    return cAVVFormat;
  }

  /**
   * @return the cAVVValue
   */
  public String getCAVVValue() {
    return cAVVValue;
  }

  /**
   * @return the isGiftCard
   */
  public Boolean getIsGiftCard() {
    return isGiftCard;
  }

  /**
   * @param format
   *          the cAVVFormat to set
   */
  public void setCAVVFormat(String format) {
    cAVVFormat = format;
  }

  /**
   * @param value
   *          the cAVVValue to set
   */
  public void setCAVVValue(String value) {
    cAVVValue = value;
  }

  /**
   * @param isGiftCard
   *          the isGiftCard to set
   */
  public void setIsGiftCard(Boolean isGiftCard) {
    this.isGiftCard = isGiftCard;
  }

  /**
   * @return the authErrorCode
   */
  public String getAuthErrorCode() {
    return authErrorCode;
  }

  /**
   * @return the authMessage
   */
  public String getAuthMessage() {
    return authMessage;
  }

  /**
   * @return the promoExpDate
   */
  public GregorianCalendar getPromoExpDate() {
    return promoExpDate;
  }

  /**
   * @return the remainingBalance
   */
  public BigDecimal getRemainingBalance() {
    return remainingBalance;
  }

  /**
   * @return the rRN
   */
  public String getRRN() {
    return rRN;
  }

  /**
   * @param authErrorCode
   *          the authErrorCode to set
   */
  public void setAuthErrorCode(String authErrorCode) {
    this.authErrorCode = authErrorCode;
  }

  /**
   * @param authMessage
   *          the authMessage to set
   */
  public void setAuthMessage(String authMessage) {
    this.authMessage = authMessage;
  }

  /**
   * @param promoExpDate
   *          the promoExpDate to set
   */
  public void setPromoExpDate(GregorianCalendar promoExpDate) {
    this.promoExpDate = promoExpDate;
  }

  /**
   * Sets the promo expiration date, but takes in a formatted string such as
   * yy-MM-dd (09-01-31) to do it.
   * 
   * @param promoExpDateString
   *          formatted date string
   * @throws ParseException
   *           if the string passed can't be parsed.
   */
  public void setPromoExpDate(String promoExpDateString) throws ParseException {

    this.promoExpDate = HkdOTCommandTO.convertOmniYYDate(promoExpDateString);
  }

  /**
   * @param remainingBalance
   *          the remainingBalance to set
   */
  public void setRemainingBalance(BigDecimal remainingBalance) {
    this.remainingBalance = remainingBalance;
  }

  /**
   * @param rrn
   *          the rRN to set
   */
  public void setRRN(String rrn) {
    rRN = rrn;
  }

  /**
   * @return true or false if the gift card indicator is active.
   */
  public boolean isGiftCardIndicator() {
    return giftCardIndicator;
  }

  /**
   * @return The integer mapped value for the card type.
   */
  public Integer getCardTypeInteger() {

    if (giftCardIndicator)
      return GIFTTCARDTYPE;
    else {

      if (isWireless) {
        return WIRELESSCREDITCARDTYPE;
      } else {
        if (isSHA1) {
          return SHA1CARDTYPE;
        } else {
          return CREDITCARDTYPE;
        }
      }
    }
  }

  /**
   * @param giftCardIndicator
   *          the gift card indicator to set
   */
  public void setGiftCardIndicator(boolean giftCardIndicator) {
    this.giftCardIndicator = giftCardIndicator;
  }

  /**
   * @param giftCardInt
   *          the gift card indicator to set, in integer form.
   */
  public void setGiftCardIndicator(int giftCardInt) {
    if (giftCardInt == GIFTTCARDTYPE)
      setGiftCardIndicator(true);
    else
      setGiftCardIndicator(false);
  }

  /**
   * @return the posTerminalId
   */
  public String getPosTerminalId() {
    return posTerminalId;
  }

  /**
   * @param posTerminalId
   *          the posTerminalId to set
   */
  public void setPosTerminalId(String posTerminalId) {
    this.posTerminalId = posTerminalId;
  }

  /**
   * @return the externalDeviceSerialId
   */
  public String getExternalDeviceSerialId() {
    return externalDeviceSerialId;
  }

  /**
   * @param externalDeviceSerialId
   *          the externalDeviceSerialId to set
   */
  public void setExternalDeviceSerialId(String externalDeviceSerialId) {
    this.externalDeviceSerialId = externalDeviceSerialId;
  }

  /**
   * @return the isWireless
   */
  public boolean isWireless() {
    return isWireless;
  }

  /**
   * @param isWireless
   *          the isWireless to set
   */
  public void setWireless(boolean isWireless) {
    this.isWireless = isWireless;
  }

  /**
   * @return the sha1
   */
  public String getSha1() {
    return sha1;
  }

  /**
   * @param sha1
   *          the sha1 to set
   */
  public void setSha1(String sha1) {
    this.sha1 = sha1;
    this.isSHA1 = true;
  }

  /**
   * @return the subCode
   */
  public String getSubCode() {
    return subCode;
  }

  /**
   * @param subCode
   *          the subCode to set
   */
  public void setSubCode(String subCode) {
    this.subCode = subCode;
  }

  /**
   * @return the isPreApproved
   */
  public Boolean getIsPreApproved() {
    return isPreApproved;
  }

  /**
   * @param isPreApproved
   *          the isPreApproved to set
   */
  public void setIsPreApproved(Boolean isPreApproved) {
    this.isPreApproved = isPreApproved;
  }

  /**
   * @return the authCode
   */
  public String getAuthCode() {
    return authCode;
  }

  /**
   * @param authCode
   *          the authCode to set
   */
  public void setAuthCode(String authCode) {
    this.authCode = authCode;
  }

}
