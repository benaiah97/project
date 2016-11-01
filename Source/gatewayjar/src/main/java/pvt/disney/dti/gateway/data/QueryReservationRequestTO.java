package pvt.disney.dti.gateway.data;

import java.io.Serializable;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;

public class QueryReservationRequestTO extends CommandBodyTO implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 5930876491753661660L;

	private String requestType;

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	private String resCode;

	private String resNumber;

	private String externalResCode;

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResNumber() {
		return resNumber;
	}

	public void setResNumber(String resNumber) {
		this.resNumber = resNumber;
	}

	public String getExternalResCode() {
		return externalResCode;
	}

	public void setExternalResCode(String externalResCode) {
		this.externalResCode = externalResCode;
	}

}
