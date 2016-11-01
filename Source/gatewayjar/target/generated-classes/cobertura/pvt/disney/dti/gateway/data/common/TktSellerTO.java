package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * This class represents the Ticket Seller identification, found in DTI requests.
 * 
 * @author lewit019
 * 
 */
public class TktSellerTO implements Serializable {

	/** Serial Version UID */
	final static long serialVersionUID = 9129231995L;

	/** The ticket seller master agreement code. */
	private String TsMac = "";

	/** The ticket seller system. */
	private String TsSystem = "";

	/** The ticket seller location. */
	private String TsLocation = "";

	/** The ticket seller security. */
	private String TsSecurity = "";

	/**
	 * Gets a copy of this object.
	 * 
	 * @return
	 */
	public TktSellerTO getCopy() {
		TktSellerTO copy = new TktSellerTO();

		copy.setTsMac(this.getTsMac());
		copy.setTsSystem(this.getTsSystem());
		copy.setTsLocation(this.getTsLocation());
		copy.setTsSecurity(this.getTsSystem());

		return copy;
	}

	/**
	 * @return the tsLocation
	 */
	public String getTsLocation() {
		return TsLocation;
	}

	/**
	 * @param tsLocation
	 *            the tsLocation to set
	 */
	public void setTsLocation(String tsLocation) {
		TsLocation = tsLocation;
	}

	/**
	 * @return the tsMac
	 */
	public String getTsMac() {
		return TsMac;
	}

	/**
	 * @param tsMac
	 *            the tsMac to set
	 */
	public void setTsMac(String tsMac) {
		TsMac = tsMac;
	}

	/**
	 * @return the tsSecurity
	 */
	public String getTsSecurity() {
		return TsSecurity;
	}

	/**
	 * @param tsSecurity
	 *            the tsSecurity to set
	 */
	public void setTsSecurity(String tsSecurity) {
		TsSecurity = tsSecurity;
	}

	/**
	 * @return the tsSystem
	 */
	public String getTsSystem() {
		return TsSystem;
	}

	/**
	 * @param tsSystem
	 *            the tsSystem to set
	 */
	public void setTsSystem(String tsSystem) {
		TsSystem = tsSystem;
	}

}
