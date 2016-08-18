package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author smoon
 * 
 */
public class GWOrdersErrorsTO extends GWErrorsTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	private ArrayList<OrderError> orderErrorsList;

	/**
	 * Convenience method
	 * 
	 * @param error
	 */
	public void addError(OrderError error) {
		orderErrorsList.add(error);
	}

	/**
	 * Accessor
	 * 
	 * @return
	 */
	public ArrayList<OrderError> getOrderErrorsList() {
		return orderErrorsList;
	}

	/**
	 * Accessor
	 * 
	 * @param ticketErrors
	 */
	public void setOrdertErrorsList(ArrayList<OrderError> orderErrors) {
		this.orderErrorsList = orderErrors;
	}

	/**
	 * Private inner class to support order errors.
	 * 
	 * @author smoon
	 * 
	 */
	private class OrderError {

		/** the status code for the order that had an error */
		private String errorCode;
		/** the status text for the order that has an error */
		private String errorText;

		/**
		 * @return the errorCode
		 */
		public String getErrorCode() {
			return errorCode;
		}

		/**
		 * @param errorCode
		 *            the errorCode to set
		 */
		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		/**
		 * @return the errorText
		 */
		public String getErrorText() {
			return errorText;
		}

		/**
		 * @param errorText
		 *            the errorText to set
		 */
		public void setErrorText(String errorText) {
			this.errorText = errorText;
		}

	}
}
