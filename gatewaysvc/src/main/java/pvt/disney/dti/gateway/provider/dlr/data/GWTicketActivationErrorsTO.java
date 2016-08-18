/**
 * 
 */
package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author smoon
 * 
 */
public class GWTicketActivationErrorsTO extends GWErrorsTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	private ArrayList<TicketActivationError> ticketErrors;

	public GWTicketActivationErrorsTO() {
		super();
		ticketErrors = new ArrayList<TicketActivationError>();
	}

	/**
	 * Convenience method
	 * 
	 * @param error
	 */
	public void addError(TicketActivationError error) {
		ticketErrors.add(error);
	}

	/**
	 * Convenience method
	 * 
	 * @param error
	 */
	public void addError(String visualID, String code, String text) {
		TicketActivationError error = new TicketActivationError(visualID, code,
				text);
		ticketErrors.add(error);
	}

	/**
	 * Accessor
	 * 
	 * @return
	 */
	public ArrayList<TicketActivationError> getTicketErrors() {
		return ticketErrors;
	}

	/**
	 * Accessor
	 * 
	 * @param ticketErrors
	 */
	public void setTicketErrors(ArrayList<TicketActivationError> ticketErrors) {
		this.ticketErrors = ticketErrors;
	}

	/**
	 * Private inner class to support ticket activation errors.
	 * 
	 * @author smoon
	 * 
	 */
	private class TicketActivationError {
		/** the visualID for the ticket that had an error */
		private String visualID;
		/** the status code for the ticket that had an error */
		private String ticketStatusCode;
		/** the status text for the ticket that has an error */
		private String ticketStatusText;

		public String getVisualID() {
			return visualID;
		}

		/**
		 * Accessor
		 * 
		 * @param visualID
		 */
		public void setVisualID(String visualID) {
			this.visualID = visualID;
		}

		/**
		 * Accessor
		 * 
		 * @return
		 */
		public String getTicketStatusCode() {
			return ticketStatusCode;
		}

		/**
		 * Accessor
		 * 
		 * @param ticketStatusCode
		 */
		public void setTicketStatusCode(String ticketStatusCode) {
			this.ticketStatusCode = ticketStatusCode;
		}

		/**
		 * Accessor
		 * 
		 * @return
		 */
		public String getTicketStatusText() {
			return ticketStatusText;
		}

		/**
		 * Accessor
		 * 
		 * @param ticketStatusText
		 */
		public void setTicketStatusText(String ticketStatusText) {
			this.ticketStatusText = ticketStatusText;
		}

		/**
		 * Default Constructor
		 */
		public TicketActivationError() {
		}

		/**
		 * Constructor
		 * 
		 * @param visualID
		 * @param ticketStatusCode
		 * @param ticketStatusText
		 */
		public TicketActivationError(String visualID, String ticketStatusCode,
				String ticketStatusText) {
			this.visualID = visualID;
			this.ticketStatusCode = ticketStatusCode;
			this.ticketStatusText = ticketStatusText;
		}
	}

}
