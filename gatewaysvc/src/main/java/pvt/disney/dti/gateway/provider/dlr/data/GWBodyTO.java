package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;

/**
 * This class represents the Body portion of an eGalaxy XML.
 * 
 * @author lewit019, moons012
 */
public class GWBodyTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** The status to. */
	private GWStatusTO statusTO;

	/** The query ticket request transfer object. */
	private GWQueryTicketRqstTO queryTicketRqstTO;

	/** The query ticket response transfer object. */
	private GWQueryTicketRespTO queryTicketResponseTO;

	/** The query ticket errors transfer object. */
	private GWQueryTicketErrorsTO queryTicketErrorsTO;

	/** The ticket activation (void) request transfer object. */
	private GWTicketActivationRqstTO ticketActivationRqstTO;

	/** The ticket activation (void) ticket response transfer object. */
	private GWTicketActivationRespTO ticketActivationResponseTO;

	/** The ticket activation (void) ticket errors transfer object. */
	private GWTicketActivationErrorsTO ticketActivationtErrorsTO;

	/** The gateway galaxy orders request transfer object. */
	private GWOrdersRqstTO ordersRqstTO;

	/** The gateway galaxy orders response transfer object */
	private GWOrdersRespTO ordersRespTO;

	/** The gateway galaxy orders error transfer object; */
	private GWOrdersErrorsTO ordersErrorsTO;

	/** The gateway galaxy query order request transfer object. */
	private GWQueryOrderRqstTO queryOrderRqstTO;

	/** The gateway galaxy query order response transfer object. */
	private GWQueryOrderRespTO queryOrderRespTO;

	/** The gateway galaxy query order error transfer object; */
	private GWQueryOrderErrorsTO queryOrderErrorsTO;

	/**
	 * @return the queryOrderErrorsTO
	 */
	public GWQueryOrderErrorsTO getQueryOrderErrorsTO() {
		return queryOrderErrorsTO;
	}

	/**
	 * @param queryOrderErrorsTO
	 *            the queryOrderErrorsTO to set
	 */
	public void setQueryOrderErrorsTO(GWQueryOrderErrorsTO queryOrderErrorsTO) {
		this.queryOrderErrorsTO = queryOrderErrorsTO;
	}

	/**
	 * @return the queryOrderRqstTO
	 */
	public GWQueryOrderRqstTO getQueryOrderRqstTO() {
		return queryOrderRqstTO;
	}

	/**
	 * @param queryOrderRqstTO
	 *            the queryOrderRqstTO to set
	 */
	public void setQueryOrderRqstTO(GWQueryOrderRqstTO queryOrderRqstTO) {
		this.queryOrderRqstTO = queryOrderRqstTO;
	}

	/**
	 * @return the queryOrderRespTO
	 */
	public GWQueryOrderRespTO getQueryOrderRespTO() {
		return queryOrderRespTO;
	}

	/**
	 * @param queryOrderRespTO
	 *            the queryOrderRespTO to set
	 */
	public void setQueryOrderRespTO(GWQueryOrderRespTO queryOrderRespTO) {
		this.queryOrderRespTO = queryOrderRespTO;
	}

	/**
	 * @return the ordersErrorsTO
	 */
	public GWOrdersErrorsTO getOrdersErrorsTO() {
		return ordersErrorsTO;
	}

	/**
	 * @param ordersErrorsTO
	 *            the ordersErrorsTO to set
	 */
	public void setOrdersErrorsTO(GWOrdersErrorsTO ordersErrorsTO) {
		this.ordersErrorsTO = ordersErrorsTO;
	}

	/**
	 * @return the ordersRespTO
	 */
	public GWOrdersRespTO getOrdersRespTO() {
		return ordersRespTO;
	}

	/**
	 * @param ordersRespTO
	 *            the ordersRespTO to set
	 */
	public void setOrdersRespTO(GWOrdersRespTO ordersRespTO) {
		this.ordersRespTO = ordersRespTO;
	}

	/**
	 * Gets the query ticket response transfer object.
	 * 
	 * @return the queryTicketResponseTO
	 */
	public GWQueryTicketRespTO getQueryTicketResponseTO() {
		return queryTicketResponseTO;
	}

	/**
	 * Gets the query ticket transfer object.
	 * 
	 * @return the queryTicketTO
	 */
	public GWQueryTicketRqstTO getQueryTicketTO() {
		return queryTicketRqstTO;
	}

	/**
	 * Gets the status transfer object.
	 * 
	 * @return the statusTO
	 */
	public GWStatusTO getStatusTO() {
		return statusTO;
	}

	/**
	 * Sets the query ticket response transfer object.
	 * 
	 * @param queryTicketResponseTO
	 *            the queryTicketResponseTO to set
	 */
	public void setQueryTicketResponseTO(
			GWQueryTicketRespTO queryTicketResponseTO) {
		this.queryTicketResponseTO = queryTicketResponseTO;
	}

	/**
	 * Sets the query ticket transfer object.
	 * 
	 * @param queryTicketTO
	 *            the queryTicketTO to set
	 */
	public void setQueryTicketTO(GWQueryTicketRqstTO queryTicketTO) {
		this.queryTicketRqstTO = queryTicketTO;
	}

	/**
	 * Sets the status transfer object.
	 * 
	 * @param statusTO
	 *            the statusTO to set
	 */
	public void setStatusTO(GWStatusTO statusTO) {
		this.statusTO = statusTO;
	}

	/**
	 * Gets the query ticket errors transfer object.
	 * 
	 * @return the queryTicketErrorsTO
	 */
	public GWQueryTicketErrorsTO getQueryTicketErrorsTO() {
		return queryTicketErrorsTO;
	}

	/**
	 * Sets the query ticket errors transfer object.
	 * 
	 * @param queryTicketErrorsTO
	 *            the queryTicketErrorsTO to set
	 */
	public void setQueryTicketErrorsTO(GWQueryTicketErrorsTO queryTicketErrorsTO) {
		this.queryTicketErrorsTO = queryTicketErrorsTO;
	}

	/**
	 * Gets the query ticket request transfer object.
	 * 
	 * @return the queryTicketRqstTO
	 */
	public GWQueryTicketRqstTO getQueryTicketRqstTO() {
		return queryTicketRqstTO;
	}

	/**
	 * Sets the query ticket request transfer object.
	 * 
	 * @param queryTicketRqstTO
	 *            the queryTicketRqstTO to set
	 */
	public void setQueryTicketRqstTO(GWQueryTicketRqstTO queryTicketRqstTO) {
		this.queryTicketRqstTO = queryTicketRqstTO;
	}

	/**
	 * Gets the orders request transfer object.
	 * 
	 * @return the ordersRqstTO
	 */
	public GWOrdersRqstTO getOrdersRqstTO() {
		return ordersRqstTO;
	}

	/**
	 * Gets the ticket activation request transfer object.
	 * 
	 * @return the ticketActivationRqstTO
	 */
	public GWTicketActivationRqstTO getTicketActivationRqstTO() {
		return ticketActivationRqstTO;
	}

	/**
	 * Sets the orders request transfer object.
	 * 
	 * @param orderRqstTO
	 *            the ordersRqstTO to set
	 */
	public void setOrdersRqstTO(GWOrdersRqstTO ordersRqstTO) {
		this.ordersRqstTO = ordersRqstTO;
	}

	/**
	 * Sets the ticket activation request transfer object.
	 * 
	 * @param ticketActivationRqstTO
	 *            the ticketActivationRqstTO to set
	 */
	public void setTicketActivationRqstTO(
			GWTicketActivationRqstTO ticketActivationRqstTO) {
		this.ticketActivationRqstTO = ticketActivationRqstTO;
	}

	/**
	 * @return
	 */
	public GWTicketActivationRespTO getTicketActivationResponseTO() {
		return ticketActivationResponseTO;
	}

	/**
	 * @param ticketActivationResponseTO
	 */
	public void setTicketActivationResponseTO(
			GWTicketActivationRespTO ticketActivationResponseTO) {
		this.ticketActivationResponseTO = ticketActivationResponseTO;
	}

	/**
	 * @return
	 */
	public GWTicketActivationErrorsTO getTicketActivationtErrorsTO() {
		return ticketActivationtErrorsTO;
	}

	/**
	 * @param ticketActivationtErrorsTO
	 */
	public void setTicketActivationtErrorsTO(
			GWTicketActivationErrorsTO ticketActivationtErrorsTO) {
		this.ticketActivationtErrorsTO = ticketActivationtErrorsTO;
	}

}
