package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.math.BigDecimal;
import java.util.ArrayList;

import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.UpgradePLU.PaymentPlan;

public class OTUpgradePLU {

	/** The plu. */
	private String PLU;

	/** The price. */
	private BigDecimal price;

	/** The upgrade price. */
	private BigDecimal upgradePrice;

	/**
	 * Gets the plu.
	 *
	 * @return the pLU
	 */
	public String getPLU() {
		return PLU;
	}

	/**
	 * Sets the plu.
	 *
	 * @param pLU
	 *           the pLU to set
	 */
	public void setPLU(String pLU) {
		PLU = pLU;
	}

	/**
	 * Gets the price.
	 *
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * Sets the price.
	 *
	 * @param price
	 *           the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * Gets the upgrade price.
	 *
	 * @return the upgradePrice
	 */
	public BigDecimal getUpgradePrice() {
		return upgradePrice;
	}

	/**
	 * Sets the upgrade price.
	 *
	 * @param upgradePrice
	 *           the upgradePrice to set
	 */
	public void setUpgradePrice(BigDecimal upgradePrice) {
		this.upgradePrice = upgradePrice;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		StringBuffer sb = new StringBuffer("UpgradePluList: ");
		sb.append(" PLU: " + PLU);
		sb.append(" Price: " + price);
		sb.append(" Upgrade Price: " + upgradePrice);

		String resp = sb.toString();

		return resp;
	}

}
