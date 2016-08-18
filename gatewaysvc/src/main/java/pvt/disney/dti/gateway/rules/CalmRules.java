package pvt.disney.dti.gateway.rules;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import pvt.disney.dti.gateway.constants.DTICalmException;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.QueryTicketResponseTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.util.DTIFormatter;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.PropertyHelper;

/**
 * 
 * @author lewit019
 * 
 */
public class CalmRules {

	/** The standard core logging mechanism. */
	private EventLogger logger = EventLogger.getLogger(this.getClass());

	private static String wdwDownFileName = null;

	private static String dlrDownFileName = null;

	private static CalmRules thisObj = null;

	private static boolean dlrCalmActive = false;

	private static boolean wdwCalmActive = false;

	private static ArrayList<String> queryTicketReplyMacs = new ArrayList<String>();

	/**
   * 
   * 
   */
	private CalmRules(Properties props) throws DTIException {

		wdwDownFileName = PropertyHelper.readPropsValue(
				PropertyName.CALM_WDW_DOWN_FILENAME, props, null);
		if (wdwDownFileName == null) {
			wdwCalmActive = false;
		}
		else {
			wdwCalmActive = true;
		}

		dlrDownFileName = PropertyHelper.readPropsValue(
				PropertyName.CALM_DLR_DOWN_FILENAME, props, null);
		if (dlrDownFileName == null) {
			wdwCalmActive = false;
		}
		else {
			dlrCalmActive = true;
		}

		String qtMacs = PropertyHelper.readPropsValue(
				PropertyName.CALM_QUERYTICKET_REPLYMACS, props, null);
		if (qtMacs != null) {
			String[] result = qtMacs.split(",");
			for (int x = 0; x < result.length; x++) {
				queryTicketReplyMacs.add(result[x]);
			}
		}

		logger.sendEvent(
				"Contingency Actions Logic Module (CALM) rules initialized.",
				EventType.DEBUG, this);

		return;

	}

	/**
	 * 
	 * @param props
	 * @return
	 * @throws DTIException
	 */
	public static CalmRules getInstance(Properties props) throws DTIException {

		if (thisObj == null) {
			thisObj = new CalmRules(props);
		}

		return thisObj;
	}

	/**
	 * Contingency Actions Logic Module (CALM)
	 * 
	 * @param dtiTxn
	 * @throws DTIException
	 */
	public void checkContingencyActionsLogicModule(DTITransactionTO dtiTxn) throws DTIException,
			DTICalmException {

		String tpiCode = dtiTxn.getTpiCode();
		File downFile = null;

		if (tpiCode.equals(DTITransactionTO.TPI_CODE_WDW) && wdwCalmActive) {

			downFile = new File(wdwDownFileName);
			if (downFile.exists()) {
				executeWDWDownRules(dtiTxn);
			}

		}
		else if (tpiCode.equals(DTITransactionTO.TPI_CODE_DLR) && dlrCalmActive) {

			downFile = new File(dlrDownFileName);
			if (downFile.exists()) {
				executeDLRDownRules(dtiTxn);
			}

		}

		return;
	}

	/**
	 * Contingency Actions Logic Module (CALM)
	 * 
	 * @param dtiTxn
	 * @throws DTIException
	 */
	private void executeWDWDownRules(DTITransactionTO dtiTxn) throws DTIException,
			DTICalmException {

		DTIRequestTO dtiRequest = dtiTxn.getRequest();
		PayloadHeaderTO payloadHdr = dtiRequest.getPayloadHeader();
		TktSellerTO tktSeller = payloadHdr.getTktSeller();
		String tsMac = tktSeller.getTsMac();

		logger.sendEvent(
				"Contingency Actions Logic Module (CALM) being checked for " + tsMac,
				EventType.DEBUG, this);

		if (dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.QUERYTICKET) {

			boolean containsMac = false;
			for (int i = 0; i < queryTicketReplyMacs.size(); i++) {
				String replyMac = queryTicketReplyMacs.get(i);

				if (replyMac.compareToIgnoreCase(tsMac) == 0) {
					containsMac = true;
					break;
				}
			}

			if (containsMac) {
				createAPQueryWDWTicketResp(dtiTxn);
				throw new DTICalmException(dtiTxn);
			}
		}

		throw new DTIException(BusinessRules.class,
				DTIErrorCode.INVALID_SALES_DATE_TIME,
				"WDW Request attempted when WDWDown outage wall file is present (CALM).");
	}

	/**
	 * Contingency Actions Logic Module (CALM)
	 * 
	 * @param dtiTxn
	 * @throws DTIException
	 */
	private void executeDLRDownRules(DTITransactionTO dtiTxn) throws DTIException,
			DTICalmException {

		DTIRequestTO dtiRequest = dtiTxn.getRequest();
		PayloadHeaderTO payloadHdr = dtiRequest.getPayloadHeader();
		TktSellerTO tktSeller = payloadHdr.getTktSeller();
		String tsMac = tktSeller.getTsMac();

		if (dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.QUERYTICKET) {

			boolean containsMac = false;
			for (int i = 0; i < queryTicketReplyMacs.size(); i++) {
				String replyMac = queryTicketReplyMacs.get(i);
				if (replyMac.compareToIgnoreCase(tsMac) == 0) {
					containsMac = true;
					break;
				}
			}

			if (containsMac) {
				createAPQueryDLRTicketResp(dtiTxn);
				throw new DTICalmException(dtiTxn);
			}
		}

		throw new DTIException(BusinessRules.class,
				DTIErrorCode.INVALID_SALES_DATE_TIME,
				"DLR Request attempted when DLRDown outage wall file is present (CALM).");
	}

	/**
	 * Creates a simulated Annual Pass Query WDW Ticket Response based on input. Presumes that basic editing (DTI Rules) has already taken place.
	 * 
	 * @param dtiTxn
	 * @return
	 */
	private void createAPQueryWDWTicketResp(DTITransactionTO dtiTxn) {

		logger.sendEvent(
				"Contingency Actions Logic Module (CALM) generating AP Query WDW Response.",
				EventType.DEBUG, this);

		/** Create the response in the DTI Transaction object. */
		DTIResponseTO dtiResp = new DTIResponseTO();
		dtiTxn.setResponse(dtiResp);

		// Create Payload Header and Command Header
		DTIFormatter.formatDefaultDTIResponseHeaders(dtiTxn, dtiResp);

		// Create Command body
		QueryTicketRequestTO qryTktRqst = (QueryTicketRequestTO) dtiTxn
				.getRequest().getCommandBody();
		QueryTicketResponseTO qryTktResp = new QueryTicketResponseTO();
		dtiResp.setCommandBody(qryTktResp);

		// Get the first ticket and place it into response.
		ArrayList<TicketTO> requestTktList = qryTktRqst.getTktList();
		TicketTO ticket = requestTktList.get(0);
		qryTktResp.addTicket(ticket);

		ticket.setTktPrice(new BigDecimal("0.0"));
		ticket.setTktTax(new BigDecimal("0.0"));

		TktStatusTO tktStatusTO = ticket.new TktStatusTO();
		tktStatusTO.setStatusItem("Voidable");
		tktStatusTO.setStatusValue("NO");
		ticket.addTicketStatus(tktStatusTO);

		tktStatusTO = ticket.new TktStatusTO();
		tktStatusTO.setStatusItem("Active");
		tktStatusTO.setStatusValue("YES");
		ticket.addTicketStatus(tktStatusTO);

		// Default pass dates to start 5 days prior and end 5 days from now.
		Date now = new Date();
		long startTime = now.getTime() - (5 * (86400 * 1000));
		Date startDate = new Date();
		startDate.setTime(startTime);
		GregorianCalendar startValidity = new GregorianCalendar();
		startValidity.setTime(startDate);
		ticket.setTktValidityValidStart(startValidity);

		long endTime = now.getTime() + (5 * (86400 * 1000));
		Date endDate = new Date();
		endDate.setTime(endTime);
		GregorianCalendar endValidity = new GregorianCalendar();
		endValidity.setTime(endDate);
		ticket.setTktValidityValidEnd(endValidity);

		ticket.setResident("MOK");
		ticket.setPassType("ANNUAL ");
		ticket.setPassClass("CALM");
		ticket.setMediaType("A");
		ticket.setAgeGroup("A");

		return;
	}

	/**
	 * Creates a simulated Annual Pass Query DLR Ticket Response based on input. Presumes that basic editing (DTI Rules) has already taken place.
	 * 
	 * @param dtiTxn
	 * @return
	 */
	private void createAPQueryDLRTicketResp(DTITransactionTO dtiTxn) {

		logger.sendEvent(
				"Contingency Actions Logic Module (CALM) generating AP Query DLR Response.",
				EventType.DEBUG, this);

		/** Create the response in the DTI Transaction object. */
		DTIResponseTO dtiResp = new DTIResponseTO();
		dtiTxn.setResponse(dtiResp);

		QueryTicketRequestTO qryTktRqst = (QueryTicketRequestTO) dtiTxn
				.getRequest().getCommandBody();

		// Create Payload Header and Command Header
		DTIFormatter.formatDefaultDTIResponseHeaders(dtiTxn, dtiResp);

		QueryTicketResponseTO qryTktResp = new QueryTicketResponseTO();
		dtiResp.setCommandBody(qryTktResp);

		// Get the first ticket and place it into response.
		ArrayList<TicketTO> requestTktList = qryTktRqst.getTktList();
		TicketTO ticket = requestTktList.get(0);
		qryTktResp.addTicket(ticket);

		ticket.setTktPrice(new BigDecimal("0.0"));
		ticket.setTktTax(new BigDecimal("0.0"));

		TktStatusTO tktStatusTO = ticket.new TktStatusTO();
		tktStatusTO.setStatusItem("Voidable");
		tktStatusTO.setStatusValue("NO");
		ticket.addTicketStatus(tktStatusTO);

		tktStatusTO = ticket.new TktStatusTO();
		tktStatusTO.setStatusItem("Active");
		tktStatusTO.setStatusValue("YES");
		ticket.addTicketStatus(tktStatusTO);

		// Default pass dates to start 5 days prior and end 5 days from now.
		Date now = new Date();
		long startTime = now.getTime() - (5 * (86400 * 1000));
		Date startDate = new Date();
		startDate.setTime(startTime);
		GregorianCalendar startValidity = new GregorianCalendar();
		startValidity.setTime(startDate);
		ticket.setTktValidityValidStart(startValidity);

		long endTime = now.getTime() + (5 * (86400 * 1000));
		Date endDate = new Date();
		endDate.setTime(endTime);
		GregorianCalendar endValidity = new GregorianCalendar();
		endValidity.setTime(endDate);
		ticket.setTktValidityValidEnd(endValidity);

		ticket.setPassType("SIGNTUREPL"); // As of 2.15.3 - JTL
		// ticket.setPassType("ANNUAL ");
		ticket.setPassClass("CALM");

		return;
	}

}
