/**
 * 
 */
package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * Transfer object class which encapsulates the installment demographics. While extension of the Demographics class would seem to make sense here (as a way of ensuring type and name consistency), extension requires that all methods
 * available in the parent class are available in the child. As that is not the case, here, the DemographicsTO is used as an "inner template" to ensure type consistency, but that's all.
 * 
 * @author lewit019
 * @since 2.15
 */
public class InstallmentDemographicsTO implements Serializable {

	/** Default serial version UID */
	private static final long serialVersionUID = 1L;

	DemographicsTO demoTO = new DemographicsTO();

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return demoTO.getMiddleName();
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public void setMiddleName(String middleName) {
		demoTO.setMiddleName(middleName);
	}

	/**
	 * @return the altTelephone
	 */
	public String getAltTelephone() {
		return demoTO.getAltTelephone();
	}

	/**
	 * @param altTelephone
	 *            the altTelephone to set
	 */
	public void setAltTelephone(String altTelephone) {
		demoTO.setAltTelephone(altTelephone);
	}

	/**
	 * @return the demoTO
	 */
	public DemographicsTO getDemoTO() {
		return demoTO;
	}

	/**
	 * @param demoTO
	 *            the demoTO to set
	 */
	public void setDemoTO(DemographicsTO demoTO) {
		this.demoTO = demoTO;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return demoTO.getLastName();
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		demoTO.setLastName(lastName);
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return demoTO.getFirstName();
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		demoTO.setFirstName(firstName);
	}

	/**
	 * @return the addr1
	 */
	public String getAddr1() {
		return demoTO.getAddr1();
	}

	/**
	 * @param addr1
	 *            the addr1 to set
	 */
	public void setAddr1(String addr1) {
		demoTO.setAddr1(addr1);
	}

	/**
	 * @return the addr2
	 */
	public String getAddr2() {
		return demoTO.getAddr2();
	}

	/**
	 * @param addr2
	 *            the addr2 to set
	 */
	public void setAddr2(String addr2) {
		demoTO.setAddr2(addr2);
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return demoTO.getCity();
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city) {
		demoTO.setCity(city);
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return demoTO.getState();
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state) {
		demoTO.setState(state);
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return demoTO.getZip();
	}

	/**
	 * @param zip
	 *            the zip to set
	 */
	public void setZip(String zip) {
		demoTO.setZip(zip);
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return demoTO.getCountry();
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		demoTO.setCountry(country);
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return demoTO.getTelephone();
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		demoTO.setTelephone(telephone);
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return demoTO.getEmail();
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		demoTO.setEmail(email);
	}

	/**
	 * @return the dateOfBirth
	 */
	public GregorianCalendar getDateOfBirth() {
		return demoTO.getDateOfBirth();
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(GregorianCalendar dateOfBirth) {
		demoTO.setDateOfBirth(dateOfBirth);
	}

}
