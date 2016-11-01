package pvt.disney.dti.gateway.provider.dlr.data;

/**
 * Represents an Order Contact or Order ShipTOContact
 * 
 * @author smoon
 * 
 */
public class GWOrderContactTO {

	/** contact first name */
	private String firstName;
	/** contact last name */
	private String lastName;
	/** street 1 */
	private String street1;
	/** street 2 */
	private String street2;
	/** street 3 */
	private String street3;
	/** city */
	private String city;
	/** state */
	private String state;
	/** ZIP */
	private String zip;
	/** country */
	private String country;
	/** phone */
	private String phone;
	/** email */
	private String email;
	/** same as order contact */
	private boolean isSameAsOrderContact = false;

	/**
	 * @return the street2
	 */
	public String getStreet2() {
		return street2;
	}

	/**
	 * @param street2
	 *            the street2 to set
	 */
	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	/**
	 * @return
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return
	 */
	public String getStreet1() {
		return street1;
	}

	/**
	 * @param street1
	 */
	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	/**
	 * @return
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * @return
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the street3
	 */
	public String getStreet3() {
		return street3;
	}

	/**
	 * @param street3
	 *            the street3 to set
	 */
	public void setStreet3(String street3) {
		this.street3 = street3;
	}

	/**
	 * @return the isSameAsOrderContact
	 */
	public boolean isSameAsOrderContact() {
		return isSameAsOrderContact;
	}

	/**
	 * @param isSameAsOrderContact
	 *            the isSameAsOrderContact to set
	 */
	public void setSameAsOrderContact(boolean isSameAsOrderContact) {
		this.isSameAsOrderContact = isSameAsOrderContact;
	}

}
