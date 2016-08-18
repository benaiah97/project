package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.util.ArrayList;

public class GWQueryOrderRqstTO implements Serializable {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	private String orderID;

	private ArrayList<String> dataRequest = new ArrayList<String>();

	/**
	 * 
	 * @param newRequest
	 */
	public void addDataRequest(String newRequest) {
		dataRequest.add(newRequest);
	}

	/**
	 * @return the dataRequest
	 */
	public ArrayList<String> getDataRequest() {
		return dataRequest;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

}
