package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;

/**
 * The Class GWRespTO. This is the parent class for all Gateway Response transfer objects.
 * 
 * @author moons012
 * @see pvt.disney.dti.gateway.provider.dlr.data.GWVoidTicketRespTO
 * @see pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRespTO
 */
public class GWRespTO implements Serializable {
	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The data response transfer object. */
	private GWDataRequestRespTO dataRespTO;

	/**
	 * Gets the data response transfer object.
	 * 
	 * @return the data response transfer object
	 */
	public GWDataRequestRespTO getDataRespTO() {
		return dataRespTO;
	}

	/**
	 * Sets the data response transfer object.
	 * 
	 * @param dataRespTO
	 *            the new data response transfer object
	 */
	public void setDataRespTO(GWDataRequestRespTO dataRespTO) {
		this.dataRespTO = dataRespTO;
	}
}
