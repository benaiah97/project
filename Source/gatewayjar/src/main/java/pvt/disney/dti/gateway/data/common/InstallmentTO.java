/**
 * 
 */
package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * @author lewit019
 * 
 */
public class InstallmentTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	// Output field
	private String contractId;

	// Input fields
	private boolean isForRenewal = false;
	private InstallmentCreditCardTO creditCard = new InstallmentCreditCardTO();
	private InstallmentDemographicsTO instllDemo = new InstallmentDemographicsTO();

	/**
	 * @return the creditCard
	 */
	public InstallmentCreditCardTO getCreditCard() {
		return creditCard;
	}

	/**
	 * @param creditCard
	 *            the creditCard to set
	 */
	public void setCreditCard(InstallmentCreditCardTO creditCard) {
		this.creditCard = creditCard;
	}

	/**
	 * @return the instllDemo
	 */
	public InstallmentDemographicsTO getInstllDemo() {
		return instllDemo;
	}

	/**
	 * @param instllDemo
	 *            the instllDemo to set
	 */
	public void setInstllDemo(InstallmentDemographicsTO instllDemo) {
		this.instllDemo = instllDemo;
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

  /**
   * @return the isForRenewal
   */
  public boolean isForRenewal() {
    return isForRenewal;
  }

  /**
   * @param isForRenewal the isForRenewal to set
   */
  public void setForRenewal(boolean isForRenewal) {
    this.isForRenewal = isForRenewal;
  }

}
