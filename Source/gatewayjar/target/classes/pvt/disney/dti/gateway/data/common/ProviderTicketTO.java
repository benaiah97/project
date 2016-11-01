package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * This class represents the provider's view of a ticket, namely the target ticket numbers, ticket names, and active status.
 * 
 * @author lewit019
 * 
 */
public class ProviderTicketTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129227490L;

	/** The product code. */
	private String pdtCode;

	/** The mapped provider ticket number. */
	private BigInteger mappedProviderTktNbr;

	/** The mapped provider ticket name. */
	private String mappedProviderTktName;

	/** The mapped provider ticket active flag. */
	private boolean mappedProviderTktActive = false;

	/**
	 * @return the mappedProviderTktActive
	 */
	public boolean isMappedProviderTktActive() {
		return mappedProviderTktActive;
	}

	/**
	 * @return the mappedProviderTktName
	 */
	public String getMappedProviderTktName() {
		return mappedProviderTktName;
	}

	/**
	 * @return the mappedProviderTktNbr
	 */
	public BigInteger getMappedProviderTktNbr() {
		return mappedProviderTktNbr;
	}

	/**
	 * @return the pdtCode
	 */
	public String getPdtCode() {
		return pdtCode;
	}

	/**
	 * @param mappedProviderTktActive
	 *            the mappedProviderTktActive to set
	 */
	public void setMappedProviderTktActive(boolean mappedProviderTktActive) {
		this.mappedProviderTktActive = mappedProviderTktActive;
	}

	/**
	 * @param mappedProviderTktName
	 *            the mappedProviderTktName to set
	 */
	public void setMappedProviderTktName(String mappedProviderTktName) {
		this.mappedProviderTktName = mappedProviderTktName;
	}

	/**
	 * @param mappedProviderTktNbr
	 *            the mappedProviderTktNbr to set
	 */
	public void setMappedProviderTktNbr(BigInteger mappedProviderTktNbr) {
		this.mappedProviderTktNbr = mappedProviderTktNbr;
	}

	/**
	 * @param pdtCode
	 *            the pdtCode to set
	 */
	public void setPdtCode(String pdtCode) {
		this.pdtCode = pdtCode;
	}

}
