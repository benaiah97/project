package pvt.disney.dti.gateway.rules.dlr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.dao.data.GuestProductTO;
import pvt.disney.dti.gateway.dao.data.UpgradeCatalogTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryEligibilityProductsResponseTO;
import pvt.disney.dti.gateway.data.QueryEligibleProductsRequestTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.EligibleProductsTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.PayPlanEligibilityStatusType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.data.common.TicketTO.UpgradeEligibilityStatusType;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.UpgradePLU;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.UsageRecord;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.provider.dlr.xml.GWEnvelopeQueryProductXML;
import pvt.disney.dti.gateway.rules.DateTimeRules;
import pvt.disney.dti.gateway.rules.TicketRules;
import pvt.disney.dti.gateway.rules.TransformConstants;
import pvt.disney.dti.gateway.rules.TransformRules;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

public class DLRQueryEligibilityProductsRules implements TransformConstants {

	/** Object instance used for logging. */
	private static final DLRQueryEligibilityProductsRules THISINSTANCE = new DLRQueryEligibilityProductsRules();

	/** Numeric identifier indicating DLR */
	public static final Integer DLR_ID = new Integer(1);

	/** DLR Galaxy TPS Code constant (as of 2.17.X, NG) */
	private final static String DLR_TPS_CODE = "DLR-Galaxy";

	/** logger */
	private static EventLogger logger = EventLogger
			.getLogger(DLRQueryEligibilityProductsRules.class);

	/**
	 * Transforms a DTITransactionTO object containing a DLR query eligible
	 * ticket request into the format required by the DLR provider.
	 * 
	 * @param dtiTxn
	 *            The dtiTxn object containing the request.
	 * @return the DLR query ticket string in the provider's format.
	 * @throws DTIException
	 *             for any error. Contains enough detail to formulate an error
	 *             response to the seller.
	 */
	static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {
		String xmlRequest;
		GWEnvelopeTO envelopeTO = new GWEnvelopeTO(
				GWEnvelopeTO.GWTransactionType.QUERYTICKET);
		GWHeaderTO headerTO = envelopeTO.getHeaderTO();
		GWBodyTO bodyTO = envelopeTO.getBodyTO();
		GWQueryTicketRqstTO queryTicketTO = new GWQueryTicketRqstTO();

		// Set the Ticket ID (should be only one in the array)
		QueryEligibleProductsRequestTO queryReq = (QueryEligibleProductsRequestTO) dtiTxn
				.getRequest().getCommandBody();
		ArrayList<TicketTO> ticketList = queryReq.getTktList();
		TicketTO ticket = ticketList.get(0);

		// Set the Visual ID
		queryTicketTO.setVisualID(ticket.getExternal());
		bodyTO.setQueryTicketTO(queryTicketTO);

		// Set the source ID to the TS MAC
		String sourceID = dtiTxn.getRequest().getPayloadHeader().getTktSeller()
				.getTsMac();
		headerTO.setSourceID(sourceID);

		// Set MessageID to DTI Payload ID (to TpRefNum as of 2.16.1)
		headerTO.setMessageID(new BigInteger(dtiTxn.getTpRefNum().toString()));

		// Set the echo data to the DTI payload ID (as of 2.16.1, JTL)
		headerTO.setEchoData(dtiTxn.getRequest().getPayloadHeader()
				.getPayloadID());

		// Set the time stamp to the GMT date/time now. (as of 2.17.2, JTL)
		headerTO.setTimeStamp(DateTimeRules.getPTDateNow());

		// Set the message type to a fixed value
		headerTO.setMessageType(GW_QRY_TKT_MSG_TYPE);
		xmlRequest = GWEnvelopeQueryProductXML.getXML(envelopeTO);

		return xmlRequest;
	}

	/**
	 * Transforms a Query Eligible Product ticket response string from the DLR provider and
	 * updates the DTITransactionTO object with the response information.
	 * 
	 * @param dtiTxn
	 *            The transaction object for this request.
	 * @param xmlResponse
	 *            The DLR provider's response in string format.
	 * @return The DTITransactionTO object, enriched with the response
	 *         information.
	 * @throws DTIException
	 *             for any error. Contains enough detail to formulate an error
	 *             response to the seller.
	 */
	static DTITransactionTO transformResponse(DTITransactionTO dtiTxn,
			String xmlResponse) throws DTIException {
		// Parse the string into the Gateway Transfer Object!!
		GWEnvelopeTO gwRespTO = GWEnvelopeQueryProductXML.getTO(xmlResponse);
		DTIResponseTO dtiRespTO = new DTIResponseTO();

		// Set up the Payload and Command Header Responses.
		PayloadHeaderTO payloadHdrTO = TransformRules
				.createRespPayloadHdr(dtiTxn);

		// Command Header 
		CommandHeaderTO commandHdrTO = TransformRules.createRespCmdHdr(dtiTxn);
		dtiRespTO.setPayloadHeader(payloadHdrTO);
		dtiRespTO.setCommandHeader(commandHdrTO);

		// Search for blatant error first
		GWBodyTO gwBodyTO = gwRespTO.getBodyTO();
		if (gwBodyTO == null) {
		// throw bad provider response error
			throw new DTIException(TransformRules.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Gateway XML allowed a response with null body.");
		}

		GWStatusTO gwStatusTO = gwBodyTO.getStatusTO();
		if (gwStatusTO == null) {
		// throw bad provider response error
			throw new DTIException(TransformRules.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Gateway XML allowed a response with null status.");
		}

		String statusString = gwStatusTO.getStatusCode();
		if (statusString == null) {
		// throw bad provider response error
			throw new DTIException(TransformRules.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Gateway XML allowed a response with null status code.");
		}

		// Get the provider response status code
		int statusCode = -1;

		try {
			statusCode = Integer.parseInt(statusString);
			dtiRespTO.setProviderErrCode(statusString);
		} catch (NumberFormatException e) {
			throw new DTIException(TransformRules.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Provider responded with a non-numeric status code.");
		}

		// If the provider had an error, map it and generate the response. Copy the ticket identity and default the TktStatus Voidable to No
		if (statusCode != 0) {
			
		// We need to parse beyond just the status code so we can determine
		// if we need to throw based on an errorCode in the tickets clause
		// rather than just on the statusCode...this will keep us from
		// returning an invalid ticket error because we have a status code
		// of 1300 when down farther in the QueryTicketsElement we might
		// have a 999 error, which indicates that the error actually
		// occurred because of a critical failure inside of eGalaxy itself.
			
			dtiTxn = transformError(dtiTxn, dtiRespTO, statusString,xmlResponse);
			return dtiTxn;
		}
		
		// If the provider had no error, transform the response.
		TicketTO dtiTktTO = new TicketTO();
		GWQueryTicketRespTO gwQryTktRespTO = gwBodyTO.getQueryTicketResponseTO();
		GWDataRequestRespTO gwDataRespTO = gwQryTktRespTO.getDataRespTO();

		// GuestProductTO
		GuestProductTO globalGuestProduct = new GuestProductTO();

		// Putting up the original response in GuestProductTO
		globalGuestProduct.setGwDataRespTO(gwDataRespTO);

		// PLU's obtained from GWDataresponse , throw error if no PLU found.
		if ((gwDataRespTO.getPlu() == null)) {
		
			logger.sendEvent("Provider returned no ticket on a successful query ticket. ", EventType.EXCEPTION, THISINSTANCE);
			throw new DTIException(DLRQueryEligibilityProductsRules.class, DTIErrorCode.TP_INTERFACE_FAILURE,
						"Provider returned no ticket on a successful query ticket. ");
		} else {
				ArrayList<String> pluList=new ArrayList<String>();
				pluList.add(gwDataRespTO.getPlu());
			// Retrieving the list of guest Product detail for PLU
			DBProductTO guestDbProduct = ProductKey.getProductsByTktName(pluList);

			// Process only if details are found
			if (guestDbProduct == null) {
				logger.sendEvent("DB product of guestproductTO not found. ", EventType.DEBUG, THISINSTANCE);
				dtiTktTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.INELIGIBLE);

				// Populating the ticket information , provide the product catalog as null

				setQueryEligibleResponseCommand(globalGuestProduct, dtiTktTO, null);

				return setQueryEligibleToDTITransaction(dtiTktTO, dtiRespTO, dtiTxn);

			} else {
				// Putting the dbProduct to guestDbProduct
				globalGuestProduct.setDbproductTO(guestDbProduct);

				// Fetch the details List of Sellable product
				UpgradeCatalogTO globalUpgradeProduct = ProductKey.getAPUpgradeCatalog(dtiTxn.getEntityTO(), DLR_TPS_CODE);

				// Check usage if no usage is there put the result type as  INELIGIBLE
				if ((globalGuestProduct.getGwDataRespTO().getUsageRecords() != null)
							&& (globalGuestProduct.getGwDataRespTO().getUsageRecords().size() > 0)) {

					if (globalGuestProduct.getGwDataRespTO().getUsageRecords().get(0).getUseNo() == 0) {

						logger.sendEvent("Usage Information for guest is not provided. ", EventType.DEBUG, THISINSTANCE);
						dtiTktTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.INELIGIBLE);

						// Populating the ticket information , with sellable products as null
						setQueryEligibleResponseCommand(globalGuestProduct, dtiTktTO, null);

						return setQueryEligibleToDTITransaction(dtiTktTO, dtiRespTO, dtiTxn);
					}
				}

				// Upgraded PLU List from Galaxy
				ArrayList<UpgradePLU> upGradedPLuList = globalGuestProduct.getGwDataRespTO().getUpgradePLUList();

				// If Upgraded PLU List is empty
				if ((upGradedPLuList == null) || (upGradedPLuList.size() == 0)) {

					// If no upgrade PLUs from the response , result is INELIGIBLE.
					logger.sendEvent("Provider has not provided any Upgraded PLU list. ", EventType.DEBUG, THISINSTANCE);

					dtiTktTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.INELIGIBLE);

					// Populating the ticket information , with sellable products as null
					setQueryEligibleResponseCommand(globalGuestProduct, dtiTktTO, null);

					return setQueryEligibleToDTITransaction(dtiTktTO, dtiRespTO, dtiTxn);

				} else if (globalUpgradeProduct.getProductListCount() == 0) {

					// If no Sellable product found , result is INELIGIBLE
					logger.sendEvent("No Sellable Product found. ", EventType.DEBUG, THISINSTANCE);
					dtiTktTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.NOPRODUCTS);

					// Populating the ticket information , with sellable products as null
					setQueryEligibleResponseCommand(globalGuestProduct, dtiTktTO, null);

					return setQueryEligibleToDTITransaction(dtiTktTO, dtiRespTO, dtiTxn);

				} else {

					// Check the UpgradePLUList from DLR response and compare with each Sellable Product List
					ArrayList<String> PLUList = new ArrayList<String>();

					for (/* each */UpgradePLU upgradePLU : /* in */upGradedPLuList) {
						PLUList.add(upgradePLU.getPLU());
						// Checking for the payPlan
						if ((upgradePLU.getPaymentPlans() == null) || (upgradePLU.getPaymentPlans().size() == 0)) {
							dtiTktTO.setPayPlanEligibilityStatus(PayPlanEligibilityStatusType.NO);
						}
					}

					logger.sendEvent("Orignal List of Sellable Product. " + globalUpgradeProduct.toString(),
								EventType.DEBUG, THISINSTANCE);

					// Comparing each PLU's from response with each Sellable Product
					globalUpgradeProduct.retainDLRPLUs(PLUList);

					logger.sendEvent("Final List of Sellable Product after comaprison with each UpgradedPLU'S from Galaxy. "
								+ globalUpgradeProduct.toString(), EventType.DEBUG, THISINSTANCE);

					// if the product list obtained after comparison is empty put the result type as NOPRODUCTS
					if (globalUpgradeProduct.getProductListCount() == 0) {
						dtiTktTO.setUpgradeEligibilityStatus(UpgradeEligibilityStatusType.NOPRODUCTS);

						// Populating the ticket information , with sellable products as null.
						setQueryEligibleResponseCommand(globalGuestProduct, dtiTktTO, null);

						return setQueryEligibleToDTITransaction(dtiTktTO, dtiRespTO, dtiTxn);
					} else {

						//Mapping the globalGuestProduct and globalUpgradeProduct to response.
						setQueryEligibleResponseCommand(globalGuestProduct, dtiTktTO, globalUpgradeProduct.getProductList());
					}
				}
			}
		} 
		return setQueryEligibleToDTITransaction(dtiTktTO, dtiRespTO, dtiTxn);
	}
	
	/**
	 * Sets the query eligible product transaction.
	 *
	 * @param dtiTktTO the dti tkt TO
	 * @param dtiRespTO the dti resp TO
	 * @param dtiTxn the dti txn
	 * @return the DTI transaction TO
	 */
	private static DTITransactionTO setQueryEligibleToDTITransaction(TicketTO dtiTktTO,
			DTIResponseTO dtiRespTO, DTITransactionTO dtiTxn) {
		QueryEligibilityProductsResponseTO qtResp = new QueryEligibilityProductsResponseTO();

		// Adding Ticket info 
		qtResp.add(dtiTktTO);
		dtiRespTO.setCommandBody(qtResp);
		dtiTxn.setResponse(dtiRespTO);
		return dtiTxn;
	}

	/**
	 * Transforms the DLR Query Eligible Ticket error
	 * 
	 * @param dtiTxn
	 *            The DTITransacationTO object representing this transaction.
	 * @param dtiRespTO
	 *            The parsed version of the received response.
	 * @param errorCode
	 *            get to get the TP provider error information.
	 * @throws DTIException
	 *             Should the routine be unable to find the TP error lookup.
	 */
	static DTITransactionTO transformError(DTITransactionTO dtiTxn,
			DTIResponseTO dtiRespTO, String statusCode, String xmlResponse)
			throws DTIException {

		String errorCode = DLRErrorRules.processStatusError(
				dtiTxn.getTransactionType(), statusCode, xmlResponse);

		DTIErrorTO dtiErrorTO = ErrorKey.getTPErrorMap(errorCode);
		DTIErrorCode.populateDTIErrorResponse(dtiErrorTO, dtiTxn, dtiRespTO);

		// Copy over key ticket values for error response to match existing format.
		if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET) {

			QueryEligibleProductsRequestTO queryReq = (QueryEligibleProductsRequestTO) dtiTxn
					.getRequest().getCommandBody();
			QueryEligibilityProductsResponseTO queryResp = new QueryEligibilityProductsResponseTO();

			TicketTO dtiTktTO = new TicketTO();

			// Ticket Item
			dtiTktTO.setTktItem(new BigInteger(ITEM_1));

			// Ticket Identity
			if (queryReq.getTktList().size() != 0) {
				TicketTO dtiTktTOReq = queryReq.getTktList().get(0);
				if (dtiTktTOReq != null)
					dtiTktTO.setExternal(dtiTktTOReq.getExternal());
			}

			// Ticket Status Voidable NO
			ArrayList<TktStatusTO> tktStatusList = dtiTktTO.getTktStatusList();
			TktStatusTO tktStatus = dtiTktTO.new TktStatusTO();
			tktStatus.setStatusItem(VOIDABLE);
			tktStatus.setStatusValue(NO);
			tktStatusList.add(tktStatus);
			
			// Add the ticket to the response
			queryResp.getTicketList().add(dtiTktTO);

			dtiRespTO.setCommandBody(queryResp);

		}
		dtiTxn.setResponse(dtiRespTO);

		return dtiTxn;
	}

	/**
	 * If a type of transaction has a specific number of provider centric rules,
	 * implement them here, but if there are a very limited set of rules, mostly
	 * common to both providers, implement in the BusinessRules in the parent
	 * package.
	 * 
	 * @param dtiTxn
	 *            the DTI Transaction object
	 * @throws DTIException
	 *             if any rule fails.
	 */
	public static void applyDLRQueryTicketRules(DTITransactionTO dtiTxn)
			throws DTIException {

		QueryEligibleProductsRequestTO reqTO = (QueryEligibleProductsRequestTO) dtiTxn
				.getRequest().getCommandBody();
		ArrayList<TicketTO> aTktList = reqTO.getTktList();
		TicketTO aTicketTO = aTktList.get(0);
		
		TicketRules.validateExternalTktIdOnly(aTicketTO);

		return;
	}


	/**
	 * Sets the query eligible response command.
	 *
	 * @param guestProductTO the guest product TO
	 * @param dtiTktTO the dti tkt TO
	 * @param upgradedProductTOList the upgraded product TO list
	 * @throws DTIException the DTI exception
	 */
	private static void setQueryEligibleResponseCommand(GuestProductTO guestProductTO, TicketTO dtiTktTO,
				ArrayList<DBProductTO> upgradedProductTOList) throws DTIException {
		BigDecimal prodPrice = null, prodTax = null, prodUpgradePrice = null, prodUpgrdTax = null;

		EligibleProductsTO eligibleProductsTO;

		// Guest Product Detail
		DBProductTO dbProductTO = guestProductTO.getDbproductTO();
		GWDataRequestRespTO gwDataRespTO = guestProductTO.getGwDataRespTO();

		if (dbProductTO != null) {

			// Prod Code
			dtiTktTO.setProdCode(dbProductTO.getPdtCode());

			// ProdGuestType
			dtiTktTO.setGuestType(dbProductTO.getGuestType());

			// SRP Price
			dtiTktTO.setTktPrice(dbProductTO.getStandardRetailPrice());

			// SRP Tax
			dtiTktTO.setTktTax(dbProductTO.getStandardRetailTax());

			// TktValidity ValidStart and ValidEnd
			boolean startDateSet = false;
			if ((gwDataRespTO.getDateSold() != null) || (gwDataRespTO.getTicketDate() != null)
						|| (gwDataRespTO.getStartDateTime() != null)) {
				dtiTktTO.setTktValidityValidStart(dbProductTO.getStartSaleDate());
				startDateSet = true;
			}

			boolean endDateSet = false;
			if ((gwDataRespTO.getExpirationDate() != null) || (gwDataRespTO.getEndDateTime() != null)) {
				dtiTktTO.setTktValidityValidEnd(dbProductTO.getEndValidDate());
				endDateSet = true;
			}

			// Note: HARD CODED!!! Arbitrarily set the end date if the start date is set. This conforms with the other provider.
			if ((startDateSet == true) && (endDateSet == false)) {
				dtiTktTO.setTktValidityValidEnd(new GregorianCalendar(2099, 11, 31));
			}
		}

		// Set TktItem: Always only one.
		dtiTktTO.setTktItem(new BigInteger(ITEM_1));

		// Set TktID (for DLR it's External)
		String visualId = gwDataRespTO.getVisualID();
		dtiTktTO.setExternal(visualId);

		// Ticket Status PICTURE YES or NO and PAY Plan Present YES or NO

		ArrayList<TktStatusTO> tktStatusList = dtiTktTO.getTktStatusList();
		TktStatusTO tktStatus = dtiTktTO.new TktStatusTO();

		// Picture
		tktStatus.setStatusItem(PICTURE);

		// Removing Has Picture as for now
		/*
		 * if ((gwDataRespTO.getHasPicture() != null) &&
		 * (gwDataRespTO.getHasPicture().compareToIgnoreCase(YES) == 0)) {
		 * tktStatus.setStatusValue(YES); } else { tktStatus.setStatusValue(NO); }
		 */
		tktStatus.setStatusValue(NO);
		tktStatusList.add(tktStatus);

		tktStatusList = dtiTktTO.getTktStatusList();
		tktStatus = dtiTktTO.new TktStatusTO();

		// Pay plan
		tktStatus.setStatusItem(PAYPLAN);
		if (dtiTktTO.getPayPlanEligibilityStatus() == PayPlanEligibilityStatusType.YES) {
			tktStatus.setStatusValue(YES);
		} else {
			tktStatus.setStatusValue(NO);
		}
		tktStatusList.add(tktStatus);

		// if result type is INELIGIBLE or NOPRODUCTS no need for checking or
		// displaying eligible productList
		if ((dtiTktTO.getUpgradeEligibilityStatus() == UpgradeEligibilityStatusType.INELIGIBLE)
					|| (dtiTktTO.getUpgradeEligibilityStatus() == UpgradeEligibilityStatusType.NOPRODUCTS)) {
			return;
		}

		ArrayList<GregorianCalendar> useTimeList = new ArrayList<GregorianCalendar>();

		// for the first use Date
		GregorianCalendar firstUsageDateValue = Collections.min(useTimeList);
		if ((gwDataRespTO.getUsageRecords() != null) && (gwDataRespTO.getUsageRecords().size() > 0)) {
			for (UsageRecord usage : gwDataRespTO.getUsageRecords()) {
				if (usage.getUseNo() == 1) {
					firstUsageDateValue = usage.getUseTime();
				}
			}
		}
		// Sorting to get the first Usage Date
		
		logger.sendEvent("First Use Date:" + useTimeList.get(0).getTime(), EventType.DEBUG, THISINSTANCE);

		// Eligible products
		if ((upgradedProductTOList != null) && (upgradedProductTOList.size() > 0)) {
			for (/* each */DBProductTO productTO : /* in */upgradedProductTOList) {

				eligibleProductsTO = new EligibleProductsTO();

				// Set the product code
				eligibleProductsTO.setProdCode(productTO.getPdtCode());

				// Set the actual product price
				prodPrice = productTO.getUnitPrice();

				eligibleProductsTO.setProdPrice(String.valueOf(prodPrice));

				// Set the actual product tax
				prodTax = productTO.getTax();

				eligibleProductsTO.setProdTax(prodTax);

				// Upgraded Price
				if ((guestProductTO.getDbproductTO() != null)
							&& (guestProductTO.getDbproductTO().getStandardRetailPrice() != null)) {
					
					// Set the upgraded product price
					prodUpgradePrice = prodPrice.subtract(guestProductTO.getDbproductTO().getStandardRetailPrice());

					eligibleProductsTO.setUpgrdPrice(prodUpgradePrice.toString());
				}

				// Upgraded Tax
				if ((guestProductTO.getDbproductTO() != null)
							&& (guestProductTO.getDbproductTO().getStandardRetailTax() != null)) {
					
					// Set the upgraded product tax
					prodUpgrdTax = prodTax.subtract(guestProductTO.getDbproductTO().getStandardRetailTax());

					eligibleProductsTO.setUpgrdTax(prodUpgrdTax.toString());

				}

				// Setting valid End Value
				try {
					Integer dayCount = productTO.getDayCount();

					Calendar calendar = Calendar.getInstance();

					// adding first Usage Value for Valid End
					if (firstUsageDateValue != null) {
						calendar.setTime(firstUsageDateValue.getTime());
						calendar.add(Calendar.DAY_OF_MONTH, dayCount);

						GregorianCalendar gc = new GregorianCalendar();
						gc.setTime(calendar.getTime());

						eligibleProductsTO.setValidEnd(gc);
					}
				} catch (Exception e) {
					logger.sendEvent("Exception caught while parsing the first Usage Date " + e.toString(),
								EventType.EXCEPTION, THISINSTANCE);
					throw new DTIException(DLRQueryEligibilityProductsRules.class, DTIErrorCode.TP_INTERFACE_FAILURE,
								"Provider responded with a invalid usage Date.");
				}

				// add the eligible product element to the list
				dtiTktTO.addEligibleProducts(eligibleProductsTO);

				logger.sendEvent("Setting all the data in GuestProductTo", EventType.DEBUG, dtiTktTO.toString());
			}
		}

	}
}