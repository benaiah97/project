package pvt.disney.dti.gateway.data;

import java.io.Serializable;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;

/**
 * This class is the primary superclass used by the query portion of the dti application request. It's comprised of a payload header, command header, and a command body.
 * 
 * @author lewit019
 * 
 */
public class DTIRequestTO implements Serializable {

	final static long serialVersionUID = 9129231995L;

	/** The payload header of the DTI Request. */
	private PayloadHeaderTO payloadHeader;
	/** The command header of the DTI Request. */
	private CommandHeaderTO commandHeader;
	/** The command body of the DTI Request. */
	private CommandBodyTO commandBody;

	/**
	 * Renders this object as a String for easy logging.
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
	 * @return commandBody
	 */
	public CommandBodyTO getCommandBody() {
		return commandBody;
	}

	/**
	 * @return commandHeader
	 */
	public CommandHeaderTO getCommandHeader() {
		return commandHeader;
	}

	/**
	 * @return payloadHeader
	 */
	public PayloadHeaderTO getPayloadHeader() {
		return payloadHeader;
	}

	/**
	 * @param body
	 *            The commandBody to be set
	 */
	public void setCommandBody(CommandBodyTO body) {
		commandBody = body;
	}

	/**
	 * @param header
	 *            The command header to be set.
	 */
	public void setCommandHeader(CommandHeaderTO header) {
		commandHeader = header;
	}

	/**
	 * @param header
	 *            The payload header to be set.
	 */
	public void setPayloadHeader(PayloadHeaderTO header) {
		payloadHeader = header;
	}

}
