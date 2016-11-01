package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * The command header of a dti request or response.
 * 
 * @author lewit019
 * 
 */
public class CommandHeaderTO implements Serializable {

	/** Serial Version UID */
	final static long serialVersionUID = 9129231995L;

	// Request Fields
	/** For use when there are more than one commands in a message */
	private BigInteger cmdItem = null;
	/**
	 * Time, in seconds, that the TicketSeller will wait for a response before generating a timeout error.
	 */
	private BigInteger cmdTimeout;
	/**
	 * Date that the original transaction occurred. In batch transactions, this would be the original time of the sale.
	 */
	private GregorianCalendar cmdDateTime;
	/** Invoice Number of the transaction. Assigned by the Ticket Seller. */
	private String cmdInvoice;
	/** Device Name where transaction occurred. */
	private String cmdDevice;
	/** Sales Associate ID that completed the transaction. */
	private String cmdOperator;
	/** Role of Associate that performed transaction. */
	private String cmdActor;
	/** Note at the command level. */
	private String cmdNote;
	/** Market of the transaction. */
	private String cmdMarket;
	/** List of command attributes. */
	protected ArrayList<CmdAttributeTO> cmdAttributeList = new ArrayList<CmdAttributeTO>();

	// Response Fields
	/** Time(in seconds) that it took the message to process through the system. */
	private BigDecimal cmdDuration;

	/**
	 * Used to represent the pair of repeating attribute items and attribte values.
	 * 
	 * @author lewit019
	 */
	public class CmdAttributeTO implements Serializable {

		public final static long serialVersionUID = 9129231995L;

		protected String attribName;
		protected String attribValue;

		/**
		 * @return the attribName
		 */
		public String getAttribName() {
			return attribName;
		}

		/**
		 * @return the attribValue
		 */
		public String getAttribValue() {
			return attribValue;
		}

		/**
		 * @param attribName
		 *            the attribName to set
		 */
		public void setAttribName(String attribName) {
			this.attribName = attribName;
		}

		/**
		 * @param attribValue
		 *            the attribValue to set
		 */
		public void setAttribValue(String attribValue) {
			this.attribValue = attribValue;
		}
	}

	/**
	 * Displays all of the information in the object in readable format.
	 */
	public String toString() {
		StringBuffer output = new StringBuffer();

		output.append("CommandHeaderTO:\n\tcmdItem=[");

		if (cmdItem == null) output.append("null");
		else output.append(cmdItem);

		output.append("]\n\tcmdTimeout=[");
		if (cmdTimeout == null) output.append("null");
		else output.append(cmdTimeout);

		output.append("]\n\tcmdDateTime=[");
		if (cmdDateTime == null) output.append("null");
		else {
			try {
				output.append(cmdDateTime.get(Calendar.YEAR) + "/");
				output.append(cmdDateTime.get(Calendar.MONTH) + "/");
				output.append(cmdDateTime.get(Calendar.DAY_OF_MONTH) + " ");
				output.append(cmdDateTime.get(Calendar.HOUR_OF_DAY) + ":");
				output.append(cmdDateTime.get(Calendar.MINUTE) + ":");
				output.append(cmdDateTime.get(Calendar.SECOND));
			}
			catch (NullPointerException npe) {
				output.append("Incomplete");
			}
		}

		output.append("]\n\tcmdInvoice=[");
		if (cmdInvoice == null) output.append("null");
		else output.append(cmdInvoice);

		output.append("]\n\tcmdDevice=[");
		if (cmdDevice == null) output.append("null");
		else output.append(cmdDevice);

		output.append("]\n\tcmdOperator=[");
		if (cmdOperator == null) output.append("null");
		else output.append(cmdOperator);

		output.append("]\n\tcmdActor=[");
		if (cmdActor == null) output.append("null");
		else output.append(cmdActor);

		output.append("]\n\tcmdDuration=[");
		if (cmdDuration == null) output.append("null");
		else output.append(cmdDuration);

		output.append("]\n");

		return output.toString();
	}

	/**
	 * Allows the caller to get their own copy of the object.
	 * 
	 * @return
	 */
	public CommandHeaderTO getCopy() {
		CommandHeaderTO copy = new CommandHeaderTO();
		copy.setCmdItem(this.getCmdItem());
		copy.setCmdTimeout(this.getCmdTimeout());
		copy.setCmdDateTime(this.getCmdDateTime());
		copy.setCmdInvoice(this.getCmdInvoice());
		copy.setCmdDevice(this.getCmdDevice());
		copy.setCmdOperator(this.getCmdOperator());
		copy.setCmdActor(this.getCmdActor());
		copy.setCmdDuration(this.getCmdDuration());
		return copy;

	}

	/**
	 * @return cmdActor
	 */
	public String getCmdActor() {
		return cmdActor;
	}

	/**
	 * @return cmdOperator
	 */
	public String getCmdOperator() {
		return cmdOperator;
	}

	/**
	 * @param the
	 *            Cmd Actor to set
	 */
	public void setCmdActor(String string) {
		cmdActor = string;
	}

	/**
	 * @param the
	 *            Cmd Operator to set
	 */
	public void setCmdOperator(String string) {
		cmdOperator = string;
	}

	/**
	 * @return the cmdDevice
	 */
	public String getCmdDevice() {
		return cmdDevice;
	}

	/**
	 * @param cmdDevice
	 *            the cmdDevice to set
	 */
	public void setCmdDevice(String cmdDevice) {
		this.cmdDevice = cmdDevice;
	}

	/**
	 * @return the cmdInvoice
	 */
	public String getCmdInvoice() {
		return cmdInvoice;
	}

	/**
	 * @param cmdInvoice
	 *            the cmdInvoice to set
	 */
	public void setCmdInvoice(String cmdInvoice) {
		this.cmdInvoice = cmdInvoice;
	}

	/**
	 * @return the cmdDateTime
	 */
	public GregorianCalendar getCmdDateTime() {
		return cmdDateTime;
	}

	/**
	 * @param cmdDateTime
	 *            the cmdDateTime to set
	 */
	public void setCmdDateTime(GregorianCalendar cmdDateTime) {
		this.cmdDateTime = cmdDateTime;
	}

	/**
	 * @return the cmdDuration
	 */
	public BigDecimal getCmdDuration() {
		return cmdDuration;
	}

	/**
	 * @param cmdDuration
	 *            the cmdDuration to set
	 */
	public void setCmdDuration(BigDecimal cmdDuration) {
		this.cmdDuration = cmdDuration;
	}

	/**
	 * @return the cmdItem
	 */
	public BigInteger getCmdItem() {
		return cmdItem;
	}

	/**
	 * @param cmdItem
	 *            the cmdItem to set
	 */
	public void setCmdItem(BigInteger cmdItem) {
		this.cmdItem = cmdItem;
	}

	/**
	 * @return the cmdTimeout
	 */
	public BigInteger getCmdTimeout() {
		return cmdTimeout;
	}

	/**
	 * @param cmdTimeout
	 *            the cmdTimeout to set
	 */
	public void setCmdTimeout(BigInteger cmdTimeout) {
		this.cmdTimeout = cmdTimeout;
	}

	/**
	 * @return the cmdNote
	 */
	public String getCmdNote() {
		return cmdNote;
	}

	/**
	 * @param cmdNote
	 *            the cmdNote to set
	 */
	public void setCmdNote(String cmdNote) {
		this.cmdNote = cmdNote;
	}

	/**
	 * @return the cmdMarket
	 */
	public String getCmdMarket() {
		return cmdMarket;
	}

	/**
	 * @param cmdMarket
	 *            the cmdMarket to set
	 */
	public void setCmdMarket(String cmdMarket) {
		this.cmdMarket = cmdMarket;
	}

	/**
	 * @return the cmdAttributeList
	 */
	public ArrayList<CmdAttributeTO> getCmdAttributeList() {
		return cmdAttributeList;
	}

	/**
	 * @param cmdAttributeList
	 *            the cmdAttributeList to set
	 */
	public void setCmdAttributeList(ArrayList<CmdAttributeTO> cmdAttributeList) {
		this.cmdAttributeList = cmdAttributeList;
	}

}
