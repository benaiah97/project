package pvt.disney.dti.gateway.data;

import java.io.Serializable;

import pvt.disney.dti.gateway.constants.TagConstants;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;

/**
 * This class is the primary superclass used by the query portion of the dti application response. It's comprised of a payload header, command header, and a command body.
 * 
 * @author lewit019
 * 
 */
public class DTIResponseTO implements Serializable, TagConstants {

	/** Standard serial version UID. */
	final static long serialVersionUID = 9129231995L;

	/** Payload header of the response. */
	private PayloadHeaderTO payloadHeader;
	/** Command header of the response. */
	private CommandHeaderTO commandHeader;
	/** Command body of the response. */
	private CommandBodyTO commandBody;
	/** DTI error in the response, if any. */
	private DTIErrorTO dtiError;
	/** Provider error code. */
	private String providerErrCode = "0";
	/** Provider error name. */
	private String providerErrName = "No error.";

	/**
	 * Renders the object as a loggable string.
	 */
	public String toString() {

		StringBuffer output = new StringBuffer();

		if (payloadHeader != null) {
			output.append(payloadHeader.toString());
		}
		else {
			output.append("/tPayloadHeaderTO: [null]\n");
		}

		if (commandHeader != null) {
			output.append(commandHeader.toString());
		}
		else {
			output.append("/tCommandHeaderTO: [null]\n");
		}

		if (commandBody != null) {
			output.append(commandBody.toString());
		}
		else {
			output.append("/tCommandBodyTO: [null]\n");
		}

		return output.toString();
	}

	/**
	 * @return the commandBody
	 */
	public CommandBodyTO getCommandBody() {
		return commandBody;
	}

	/**
	 * @return the commandHeader
	 */
	public CommandHeaderTO getCommandHeader() {
		return commandHeader;
	}

	/**
	 * @return the payloadHeader
	 */
	public PayloadHeaderTO getPayloadHeader() {
		return payloadHeader;
	}

	/**
	 * @param body
	 *            the command body to be set.
	 */
	public void setCommandBody(CommandBodyTO body) {
		commandBody = body;
	}

	/**
	 * @param header
	 *            the command header to be set.
	 */
	public void setCommandHeader(CommandHeaderTO header) {
		commandHeader = header;
	}

	/**
	 * @param header
	 *            the payload header to be set.
	 */
	public void setPayloadHeader(PayloadHeaderTO header) {
		payloadHeader = header;
	}

	/**
	 * @return the dtiError
	 */
	public DTIErrorTO getDtiError() {
		return dtiError;
	}

	public void setDtiError(DTIErrorTO dtiErrorIn) {
		dtiError = dtiErrorIn;
	}

	public String getProviderErrCode() {
		return providerErrCode;
	}

	public void setProviderErrCode(String providerErrCode) {
		this.providerErrCode = providerErrCode;
	}

	public String getProviderErrName() {
		return providerErrName;
	}

	public void setProviderErrName(String providerErrName) {
		this.providerErrName = providerErrName;
	}

}
