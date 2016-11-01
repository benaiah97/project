package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * This class represents an Agency in a DTI Request.
 * 
 * @author lewit019
 * 
 */
public class AgencyTO implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 9129231995L;

	/** The International Air Transport Association (IATA) number of the agency. */
	private String iATA;

	/** A value identifying the agent. */
	private String agent;

	/**
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * @return the iATA
	 */
	public String getIATA() {
		return iATA;
	}

	/**
	 * @param agent
	 *            the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * @param iata
	 *            the iATA to set
	 */
	public void setIATA(String iata) {
		iATA = iata;
	}

}
