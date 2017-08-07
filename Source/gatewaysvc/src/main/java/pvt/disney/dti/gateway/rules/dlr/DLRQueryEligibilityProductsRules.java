package pvt.disney.dti.gateway.rules.dlr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
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
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO.GenderType;
import pvt.disney.dti.gateway.data.common.EligibleProductsTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.ResultStatusTo.ResultType;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.Contact;
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

	public static final int DLR_ANNUAL_PASS_GRACE_PERIOD_HOURS = 30;

	/**
	 * Numeric identifier indicating DLR
	 */
	public static final Integer DLR_ID = new Integer(1);

	/** DLR Galaxy TPS Code constant (as of 2.17.X, NG) */
	private final static String DLR_TPS_CODE = "DLR-Galaxy";

	//logger
	private static EventLogger logger = EventLogger.getLogger(DLRQueryEligibilityProductsRules.class);

	/**
	 * Transforms a DTITransactionTO object containing a DLR query eligible
	 * ticket request into the format required by the DLR provider.
	 * 
	 * @param dtiTxn
	 *           The dtiTxn object containing the request.
	 * @return the DLR query ticket string in the provider's format.
	 * @throws DTIException
	 *            for any error. Contains enough detail to formulate an error
	 *            response to the seller.
	 */
	static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

		String xmlRequest;
		GWEnvelopeTO envelopeTO = new GWEnvelopeTO(GWEnvelopeTO.GWTransactionType.QUERYTICKET);

		GWHeaderTO headerTO = envelopeTO.getHeaderTO();
		GWBodyTO bodyTO = envelopeTO.getBodyTO();
		GWQueryTicketRqstTO queryTicketTO = new GWQueryTicketRqstTO();

		// Set the Ticket ID (should be only one in the array)
		QueryEligibleProductsRequestTO queryReq = (QueryEligibleProductsRequestTO) dtiTxn.getRequest().getCommandBody();
		ArrayList<TicketTO> ticketList = queryReq.getTktList();
		TicketTO ticket = ticketList.get(0);

		// Set the Visual ID
		queryTicketTO.setVisualID(ticket.getExternal());

		bodyTO.setQueryTicketTO(queryTicketTO);

		// Set the source ID to the TS MAC
		String sourceID = dtiTxn.getRequest().getPayloadHeader().getTktSeller().getTsMac();
		headerTO.setSourceID(sourceID);

		// Set MessageID to DTI Payload ID (to TpRefNum as of 2.16.1)
		headerTO.setMessageID(new BigInteger(dtiTxn.getTpRefNum().toString()));

		// Set the echo data to the DTI payload ID (as of 2.16.1, JTL)
		headerTO.setEchoData(dtiTxn.getRequest().getPayloadHeader().getPayloadID());

		// Set the time stamp to the GMT date/time now. (as of 2.17.2, JTL)
		headerTO.setTimeStamp(DateTimeRules.getPTDateNow());

		// Set the message type to a fixed value
		headerTO.setMessageType(GW_QRY_TKT_MSG_TYPE);

		xmlRequest = GWEnvelopeQueryProductXML.getXML(envelopeTO);

		return xmlRequest;
	}

	/**
	 * Transforms a query ticket response string from the DLR provider and
	 * updates the DTITransactionTO object with the response information.
	 * 
	 * @param dtiTxn
	 *           The transaction object for this request.
	 * @param xmlResponse
	 *           The DLR provider's response in string format.
	 * @return The DTITransactionTO object, enriched with the response
	 *         information.
	 * @throws DTIException
	 *            for any error. Contains enough detail to formulate an error
	 *            response to the seller.
	 */
	static DTITransactionTO transformResponse(DTITransactionTO dtiTxn, String xmlResponse) throws DTIException {

		// Parse the string into the Gateway Transfer Object!!
		GWEnvelopeTO gwRespTO = GWEnvelopeQueryProductXML.getTO(xmlResponse);

		DTIResponseTO dtiRespTO = new DTIResponseTO();

		// Set up the Payload and Command Header Responses.
		PayloadHeaderTO payloadHdrTO = TransformRules.createRespPayloadHdr(dtiTxn);

		/* Command Header */
		CommandHeaderTO commandHdrTO = TransformRules.createRespCmdHdr(dtiTxn);

		dtiRespTO.setPayloadHeader(payloadHdrTO);
		dtiRespTO.setCommandHeader(commandHdrTO);

		// Search for blatant error first
		GWBodyTO gwBodyTO = gwRespTO.getBodyTO();
		if (gwBodyTO == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class, DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
						"Internal Error:  Gateway XML allowed a response with null body.");
		}

		GWStatusTO gwStatusTO = gwBodyTO.getStatusTO();
		if (gwStatusTO == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class, DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
						"Internal Error:  Gateway XML allowed a response with null status.");
		}

		String statusString = gwStatusTO.getStatusCode();
		if (statusString == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class, DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
						"Internal Error:  Gateway XML allowed a response with null status code.");
		}

		// Get the provider response status code
		int statusCode = -1;

		try {
			statusCode = Integer.parseInt(statusString);
			dtiRespTO.setProviderErrCode(statusString);
		} catch (NumberFormatException e) {
			throw new DTIException(TransformRules.class, DTIErrorCode.TP_INTERFACE_FAILURE,
						"Provider responded with a non-numeric status code.");
		}

		// If the provider had an error, map it and generate the response.
		// Copy the ticket identity and default the TktStatus Voidable to No
		if (statusCode != 0) {
			// we need to parse beyond just the status code so we can determine
			// if
			// we need to throw based on an errorCode in the tickets clause
			// rather
			// than just on the statusCode...this will keep us from returning
			// an invalid ticket error because we have a status code of 1300
			// when down farther in the QueryTicketsElement we might have a 999
			// error, which indicates that the error actually occurred because
			// of a
			// critical failure inside of eGalaxy itself.
			dtiTxn = transformError(dtiTxn, dtiRespTO, statusString, xmlResponse);
			return dtiTxn;
		}
		// If the provider had no error, transform the response.
		TicketTO dtiTktTO = new TicketTO();
		GWQueryTicketRespTO gwQryTktRespTO = gwBodyTO.getQueryTicketResponseTO();
		GWDataRequestRespTO gwDataRespTO = gwQryTktRespTO.getDataRespTO();

		// Creating GuestProductTO - Step 1
		GuestProductTO globalGuestProduct = new GuestProductTO();

		// putting up the orignal response in GuestProductTO
		globalGuestProduct.setGwDataRespTO(gwDataRespTO);

		/* PLU obtained from GWDataresponse */
		String PLU = gwDataRespTO.getPlu();

		/* throw error if ticket not found */
		if (PLU != null) {

			// executing Query to pull up Guest Ticket detail by PLU
			DBProductTO dBProduct = ProductKey.getProductsByTktName(gwDataRespTO.getPlu());

			// Process only if details are found
			if (dBProduct != null) {
				globalGuestProduct.setDbproductTO(dBProduct);

				// Creating UpgradeCatalogTO - Step 2:: fetch the details List of
				// Saleable product
				UpgradeCatalogTO globalUpgradeProduct = ProductKey.getAPUpgradeCatalog(dtiTxn.getEntityTO(), DLR_TPS_CODE);

				// Step 3A
				// If Picture is not present in response marking resultType as INELIGIBLE
				
				if ((globalGuestProduct.getGwDataRespTO().getHasPicture() == null)) {
					// TODO need to verify if Haspicture is NO ||
					// (globalGuestProduct.getGwDataRespTO().getHasPicture().compareTo("NO")
					// == 0)) {
					logger.sendEvent("Has Picture is not found ", EventType.DEBUG,
								THISINSTANCE);
					dtiTktTO.setResultType(ResultType.INELIGIBLE);
				}
				
				
				// Check usage 
				if ((globalGuestProduct.getGwDataRespTO().getUsageRecords() != null)
							&& (globalGuestProduct.getGwDataRespTO().getUsageRecords().size() > 0)) {
					
					if((globalGuestProduct.getGwDataRespTO().getUsageRecords().get(0).getUseNo()==0)){
						
						logger.sendEvent("No Usage is there ", EventType.DEBUG,
									THISINSTANCE);
						dtiTktTO.setResultType(ResultType.INELIGIBLE);
					}
					//TODO verify if the transaction should stop if step 3 A is INELIGIBLE
					
				}
				
				// Step 4 :: Check the Upgrade PLU's from DLR response and filter with list from UpgradeCatalogTO
				ArrayList<UpgradePLU> upGradedPLuList = globalGuestProduct.getGwDataRespTO().getUpgradePLUList();

				if ((upGradedPLuList != null) && (upGradedPLuList.size() > 0)) {

					if (globalUpgradeProduct.getProductListCount() > 0) {

						ArrayList<String> PLUList = new ArrayList<String>();
						for (UpgradePLU upgradePLU : upGradedPLuList) {
							PLUList.add(upgradePLU.getPLU());
						}

						logger.sendEvent("Orignal List of Salaeable Product Obtaned " + globalUpgradeProduct.getProductListCount(),
									EventType.DEBUG, THISINSTANCE);

						// Filter for the PLU's from response (Step 1) with what obtained Step 2
						globalUpgradeProduct.retainDLRPLUs(PLUList);
						
						logger.sendEvent("Final List of Salaeable Product Obtaned " + globalUpgradeProduct.getProductListCount(), EventType.DEBUG,
									THISINSTANCE);
						// checking if result Type is null or no value is set 
						if(dtiTktTO.getResultType()==null){
							dtiTktTO.setResultType(ResultType.ELIGIBLE);
						}
					} else {
						
						logger.sendEvent("No Upgradable Product found ",
									EventType.DEBUG, THISINSTANCE);
						dtiTktTO.setResultType(ResultType.NOPRODUCTS);
						
						dtiTktTO.setTktItem(new BigInteger(ITEM_1));
						String visualId = gwDataRespTO.getVisualID();
						dtiTktTO.setExternal(visualId);
						
						//TODO need to verify this with do we need to stop and skip step 5 
						return getQueryEligibleCommand(dtiTktTO,dtiRespTO,dtiTxn);
					}

				} else {
					// if no upgrade PLUs mark the result as INELIGIBLE and stop transaction
					logger.sendEvent("PLU List from the DLR response is empty ",
								EventType.DEBUG, THISINSTANCE);
					
					dtiTktTO.setResultType(ResultType.INELIGIBLE);
					
					dtiTktTO.setTktItem(new BigInteger(ITEM_1));
					String visualId = gwDataRespTO.getVisualID();
					dtiTktTO.setExternal(visualId);
					//TODO need to verify this with do we need to stop and skip step 5 
					return getQueryEligibleCommand(dtiTktTO,dtiRespTO,dtiTxn);
				}
			
				// Step 5 :: Mapping the globalGuestProduct and globalUpgradeProduct to response
				setTicketValues(globalGuestProduct, dtiTktTO, globalUpgradeProduct.getProductList());
				
				// set the result type as ELIGIBLE 
				if(dtiTktTO.getResultType()==null){
					dtiTktTO.setResultType(ResultType.ELIGIBLE);
				}
				

			} else {
				
				logger.sendEvent("No PLU's List  fetched from DTI Ticketing System", EventType.DEBUG, THISINSTANCE);
				dtiTktTO.setResultType(ResultType.INELIGIBLE);
			}
		} else {
			
			logger.sendEvent("No Ticket Type Information fetched", EventType.EXCEPTION, THISINSTANCE);
			throw new DTIException(DLRQueryEligibilityProductsRules.class, DTIErrorCode.TICKET_INVALID, "Ticket Provided is incorrect");

		}
		return getQueryEligibleCommand(dtiTktTO,dtiRespTO,dtiTxn);
	}
	
	/**
	 * Gets the query eligible command.
	 *
	 * @param dtiTktTO the dti tkt TO
	 * @param dtiRespTO the dti resp TO
	 * @param dtiTxn the dti txn
	 * @return the query eligible command
	 */
	private static DTITransactionTO getQueryEligibleCommand(TicketTO dtiTktTO,DTIResponseTO dtiRespTO,DTITransactionTO dtiTxn){
		QueryEligibilityProductsResponseTO qtResp = new QueryEligibilityProductsResponseTO();

		/* Adding Ticket info */
		qtResp.add(dtiTktTO);
		dtiRespTO.setCommandBody(qtResp);
		dtiTxn.setResponse(dtiRespTO);
		return dtiTxn;
	}
	

	/**
	 * Transforms the DLR Query Eligible Ticket error
	 * 
	 * @param dtiTxn
	 *           The DTITransacationTO object representing this transaction.
	 * @param dtiRespTO
	 *           The parsed version of the received response.
	 * @param errorCode
	 *           get to get the TP provider error information.
	 * @throws DTIException
	 *            Should the routine be unable to find the TP error lookup.
	 */
	static DTITransactionTO transformError(DTITransactionTO dtiTxn, DTIResponseTO dtiRespTO, String statusCode,
				String xmlResponse) throws DTIException {

		String errorCode = DLRErrorRules.processStatusError(dtiTxn.getTransactionType(), statusCode, xmlResponse);

		DTIErrorTO dtiErrorTO = ErrorKey.getTPErrorMap(errorCode);
		DTIErrorCode.populateDTIErrorResponse(dtiErrorTO, dtiTxn, dtiRespTO);

		// Copy over key ticket values for error response to match
		// existing format.
		if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET) {

			QueryEligibleProductsRequestTO queryReq = (QueryEligibleProductsRequestTO) dtiTxn.getRequest()
						.getCommandBody();
			QueryEligibilityProductsResponseTO queryResp = new QueryEligibilityProductsResponseTO();

			TicketTO dtiTktTO = new TicketTO();

			// Tkt Item
			dtiTktTO.setTktItem(new BigInteger(ITEM_1));

			// Tkt Identity
			if (queryReq.getTktList().size() != 0) {
				TicketTO dtiTktTOReq = queryReq.getTktList().get(0);
				if (dtiTktTOReq != null)
					dtiTktTO.setExternal(dtiTktTOReq.getExternal());
			}

			// Tkt Status (Voidable NO)
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
	 *           the DTI Transaction object
	 * @throws DTIException
	 *            if any rule fails.
	 */
	public static void applyDLRQueryTicketRules(DTITransactionTO dtiTxn) throws DTIException {

		QueryEligibleProductsRequestTO reqTO = (QueryEligibleProductsRequestTO) dtiTxn.getRequest().getCommandBody();
		ArrayList<TicketTO> aTktList = reqTO.getTktList();
		TicketTO aTicketTO = aTktList.get(0);

		TicketRules.validateExternalTktIdOnly(aTicketTO);

		return;
	}

	
	/**
	 * Sets the query eligible response command.
	 *
	 * @param upgradedProduct
	 *           the upgraded product
	 * @param dtiTktTO
	 *           the dti tkt TO
	 * @param gwDataRespTO
	 *           the gw data resp TO
	 * @throws DTIException
	 *            the DTI exception
	 */
	private static void setTicketValues(GuestProductTO guestProductTO, TicketTO dtiTktTO,
				ArrayList<DBProductTO> upgradedProductTOList) throws DTIException {
		BigDecimal prodPrice = null, prodTax = null, prodUpgradePrice = null, prodUpgrdTax = null;

		DemographicsTO ticketDemo = new DemographicsTO();
		EligibleProductsTO eligibleProductsTO;

		DBProductTO dbProductTO = guestProductTO.getDbproductTO();
		GWDataRequestRespTO gwDataRespTO = guestProductTO.getGwDataRespTO();

		if (dbProductTO != null) {
			/* Set TktItem: Always only one. */
			dtiTktTO.setTktItem(new BigInteger(ITEM_1));

			/* Set TktID (for DLR it's External) */
			String visualId = gwDataRespTO.getVisualID();
			dtiTktTO.setExternal(visualId);

			dtiTktTO.setProdCode(dbProductTO.getPdtCode());

			/* ProdGuestType */
			dtiTktTO.setGuestType(dbProductTO.getGuestType());

			/* Contact */
			if ((gwDataRespTO.getContact() != null) && (gwDataRespTO.getContact().size() > 0)) {
				
				Contact contact=gwDataRespTO.getContact().get(0);
				
					/* FirstName */
					if (contact.getFirstName() != null) {
						ticketDemo.setFirstName(contact.getFirstName());
					}

					/* LastName */
					if (contact.getLastName() != null) {
						ticketDemo.setLastName(contact.getLastName());
					}

					/* Addr1 */
					if (contact.getStreet1() != null) {
						ticketDemo.setAddr1(contact.getStreet1());
					}

					/* Addr2 */
					if (contact.getStreet2() != null) {
						ticketDemo.setAddr2(contact.getStreet2());
					}

					/* City */
					if (contact.getCity() != null) {
						ticketDemo.setCity(contact.getCity());
					}

					/* State */
					if (contact.getState() != null) {
						ticketDemo.setState(contact.getState());
					}

					/* ZIP */
					if (contact.getZip() != null) {
						ticketDemo.setZip(contact.getZip());
					}

					/* Country */
					if (contact.getCountryCode() != null) {
						ticketDemo.setCountry(contact.getCountryCode());
					}

					/* Telephone */
					if (contact.getPhone() != null) {
						ticketDemo.setTelephone(contact.getPhone());
					}

					/* Cell */
					if (contact.getCell() != null) {
						ticketDemo.setCellPhone(contact.getCell());
					}

					/* Email */
					if (contact.getEmail() != null) {
						ticketDemo.setEmail(contact.getEmail());
					}

					/* Gender*/
					if ((contact.getGender() != null)) {

						if (contact.getGender().equalsIgnoreCase("Male")) {
							ticketDemo.setGender(GenderType.MALE);
						} else if (contact.getGender().equalsIgnoreCase("Female")) {
							ticketDemo.setGender(GenderType.FEMALE);
						} else {
							ticketDemo.setGender(GenderType.UNSPECIFIED);
						}
					}

					/* DOB*/
					if ((contact.getDob() != null)) {
						ticketDemo.setDateOfBirth(contact.getDob());
					}

					dtiTktTO.addTicketDemographic(ticketDemo);
					
				}
			// Demographics Info end 

			// TktValidity ValidStart and ValidEnd
			boolean startDateSet = false;
			if (gwDataRespTO.getItemKind() == GWDataRequestRespTO.ItemKind.REGULAR_TICKET) {
				if (gwDataRespTO.getDateSold() != null || gwDataRespTO.getTicketDate() != null
							|| gwDataRespTO.getStartDateTime() != null) {
					dtiTktTO.setTktValidityValidStart(dbProductTO.getStartSaleDate());
					startDateSet = true;
				}

				boolean endDateSet = false;
				if (gwDataRespTO.getExpirationDate() != null || gwDataRespTO.getEndDateTime() != null) {
					dtiTktTO.setTktValidityValidEnd(dbProductTO.getEndValidDate());
					endDateSet = true;
				}

				/*
				 * Note: HARD CODED!!! Arbitrarily set the end date if the start
				 * date is set. This conforms with the other provider.
				 */
				if ((startDateSet == true) && (endDateSet == false)) {
					dtiTktTO.setTktValidityValidEnd(new GregorianCalendar(2099, 11, 31));
				}

			} else {
				if (gwDataRespTO.getDateOpened() != null) {
					dtiTktTO.setTktValidityValidStart(dbProductTO.getStartValidDate());
				}

				if (gwDataRespTO.getValidUntil() != null) {
					dtiTktTO.setTktValidityValidEnd(dbProductTO.getStartValidDate());
				}
			}

			/* SRP Price */
			dtiTktTO.setTktPrice(dbProductTO.getStandardRetailPrice());

			/* SRP Tax */
			dtiTktTO.setTktTax(dbProductTO.getStandardRetailTax());

			
			/* Tkt Status (PICTURE YES or NO and PAY Plan Present YES or NO) */
			 
			ArrayList<TktStatusTO> tktStatusList = dtiTktTO.getTktStatusList();
			TktStatusTO tktStatus = dtiTktTO.new TktStatusTO();

			/* picture */
			tktStatus.setStatusItem(PICTURE);

			if ((gwDataRespTO.getHasPicture() != null) && (gwDataRespTO.getHasPicture().compareToIgnoreCase(YES) == 0)) {
				tktStatus.setStatusValue(YES);
			} else {
				tktStatus.setStatusValue(NO);
			}
			tktStatusList.add(tktStatus);

			tktStatusList = dtiTktTO.getTktStatusList();
			tktStatus = dtiTktTO.new TktStatusTO();

			/* payplan */
			tktStatus.setStatusItem(PAYPLAN);
			if (gwDataRespTO.getPayPlan() != null && gwDataRespTO.getPayPlan().compareToIgnoreCase(YES) == 0) {
				tktStatus.setStatusValue(YES);
			} else {
				tktStatus.setStatusValue(NO);
			}
			tktStatusList.add(tktStatus);
		}

		
		// for the first use Date 
		ArrayList<GregorianCalendar> useTimeList=new ArrayList<GregorianCalendar>();
		
		for(UsageRecord usage:gwDataRespTO.getUsageRecords()){
			useTimeList.add(usage.getUseTime());
		}
		// Sorting to get the first Usage Date
		Collections.sort(useTimeList);
		
		GregorianCalendar firstUsageDateValue=useTimeList.get(0);
		logger.sendEvent("First Use Date:"+useTimeList.get(0).getTime(),EventType.DEBUG,THISINSTANCE);
		
		
		/* Eligible products */
		if (upgradedProductTOList != null && upgradedProductTOList.size() > 0) {
			for (/* each */DBProductTO productTO : /* in */upgradedProductTOList) {

				eligibleProductsTO = new EligibleProductsTO();

				/* set the product code */
				eligibleProductsTO.setProdCode(productTO.getPdtCode());

				/* set the actual product price */
				prodPrice = dbProductTO.getUnitPrice();

				eligibleProductsTO.setProdPrice(prodPrice.toString());

				/* set the actual product tax */
				prodTax = dbProductTO.getTax();

				eligibleProductsTO.setProdTax(prodTax);

				/* set the upgraded product price */
				prodUpgradePrice = prodPrice.subtract(guestProductTO.getDbproductTO().getStandardRetailPrice());
				
				if(prodUpgradePrice.compareTo(BigDecimal.ZERO)<0){
					prodUpgradePrice=BigDecimal.ZERO;
				}

				eligibleProductsTO.setUpgrdPrice(prodUpgradePrice.toString());

				/* set the upgraded product tax */
				prodUpgrdTax = prodTax.subtract(guestProductTO.getDbproductTO().getStandardRetailTax());
				
				if(prodUpgrdTax.compareTo(BigDecimal.ZERO)<0){
					prodUpgrdTax=BigDecimal.ZERO;
				}
				
				eligibleProductsTO.setUpgrdTax(prodUpgrdTax.toString());
				
				/* set the validity */
				try {
					Integer dayCount = productTO.getDayCount();

					Calendar calendar = Calendar.getInstance();
					
					// adding first Usage Value for Valid End
					if (firstUsageDateValue != null) {
						calendar.setTime(firstUsageDateValue.getTime());
						calendar.add(Calendar.DAY_OF_MONTH, dayCount);

						GregorianCalendar gc = new GregorianCalendar();
						gc.setTime(calendar.getTime());

						eligibleProductsTO.setValidEnd(DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
					} 
				} catch (Exception e) {
					
					logger.sendEvent("Exception caught while parsing the first Usage Date " + e.toString(), EventType.WARN, THISINSTANCE);
					throw new DTIException(DLRQueryEligibilityProductsRules.class, DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
								"Internal Error: Invalid First Use Date");
				}
				/* add the eligible product element to the list */
				dtiTktTO.addEligibleProducts(eligibleProductsTO);

				logger.sendEvent("Setting all the data in GuestProductTo", EventType.DEBUG, dtiTktTO.toString());
			}
		}
	}
}