package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

import pvt.disney.dti.gateway.data.common.CreditCardTO.CreditCardType;

/**
 * Transfer object class which encapsulates the installment credit card. While extension of the CreditCardTO class would seem to make sense here (as a way of ensuring type and name consistency), extension requires that all methods available
 * in the parent class are available in the child. As that is not the case, here, the CreditCardTO is used as an "inner template" to ensure type consistency, but that's all.
 * 
 * @since 2.15
 * @author lewit019
 * 
 */
public class InstallmentCreditCardTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 6754636741669215586L;

  CreditCardTO cCardTO = new CreditCardTO();

  /**
   * @return the cCard
   */
  public CreditCardTO getcCard() {
    return cCardTO;
  }

  /**
   * @param cCard
   *            the cCard to set
   */
  public void setcCard(CreditCardTO cCard) {
    this.cCardTO = cCard;
  }

  /**
   * @return the ccExpiration
   */
  public String getCcExpiration() {
    return cCardTO.getCcExpiration();
  }

  /**
   * @return the ccManualOrSwipe
   */
  public CreditCardType getCcManualOrSwipe() {
    return cCardTO.getCcManualOrSwipe();
  }

  /**
   * @return the ccName
   */
  public String getCcName() {
    return cCardTO.getCcName();
  }

  /**
   * @return the ccNbr
   */
  public String getCcNbr() {
    return cCardTO.getCcNbr();
  }

  /**
   * @return the ccTrack1
   */
  public String getCcTrack1() {
    return cCardTO.getCcTrack1();
  }

  /**
   * @return the ccTrack2
   */
  public String getCcTrack2() {
    return cCardTO.getCcTrack2();
  }

  /**
   * @param ccExpiration
   *            the ccExpiration to set
   */
  public void setCcExpiration(String ccExpiration) {
    cCardTO.setCcExpiration(ccExpiration);
  }

  /**
   * @param ccManualOrSwipe
   *            the ccManualOrSwipe to set
   */
  public void setCcManualOrSwipe(CreditCardType ccManualOrSwipe) {
    cCardTO.setCcManualOrSwipe(ccManualOrSwipe);
  }

  /**
   * @param ccName
   *            the ccName to set
   */
  public void setCcName(String ccName) {
    cCardTO.setCcName(ccName);
  }

  /**
   * @param ccTrack2
   *            the ccTrack2 to set
   */
  public void setCcTrack2(String ccTrack2) {
    cCardTO.setCcTrack2(ccTrack2);
  }

  /**
   * @param ccNbr
   *            the ccNbr to set
   */
  public void setCcNbr(String ccNbr) {
    cCardTO.setCcNbr(ccNbr);
  }

  /**
   * @param ccTrack1
   *            the ccTrack1 to set
   */
  public void setCcTrack1(String ccTrack1) {
    cCardTO.setCcTrack1(ccTrack1);
  }

  /**
   * @return the ccType
   */
  public String getCcType() {
    return cCardTO.getCcType();
  }

  /**
   * @param ccType
   *            the ccType to set
   */
  public void setCcType(String ccType) {
    cCardTO.setCcType(ccType);
  }

  /**
   * @param ccZipCode
   *            the ccZipCode to set
   */
  public void setCcZipCode(String ccZipCode) {
    cCardTO.setCcZipCode(ccZipCode);
  }

  /**
   * @return the ccZipCode
   */
  public String getCcZipCode() {
    return cCardTO.getCcZipCode();
  }

  /**
   * @return the ccVV
   */
  public String getCcVV() {
    return cCardTO.getCcVV();
  }

  /**
   * @param ccVV
   *            the ccVV to set
   */
  public void setCcVV(String ccVV) {
    cCardTO.setCcVV(ccVV);
    ;
  }

  /**
   * 
   * @param ccStreet
   */
  public void setCcStreet(String ccStreet) {
    cCardTO.setCcStreet(ccStreet);
  }

  /**
   * 
   * @return
   */
  public String getCcStreet() {
    return cCardTO.getCcStreet();
  }

}
