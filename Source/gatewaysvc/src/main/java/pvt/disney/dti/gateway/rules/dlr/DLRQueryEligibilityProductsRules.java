package pvt.disney.dti.gateway.rules.dlr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.dao.LookupKey;
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
import pvt.disney.dti.gateway.data.common.EnttlGuidTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.ResultStatusTo;
import pvt.disney.dti.gateway.data.common.ResultStatusTo.ResultType;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.Contact;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.ItemKind;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.LineageRecord;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.Status;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.UpgradePLUList;
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

	/** Constant text for PassRenew Status INELIGIBLE (as of 2.16.1, JTL) */
	private final static String PASSRENEW_INELIGIBLE = "INELIGIBLE";

	/** Constant text for PassRenew Status STANDARD (as of 2.16.1, JTL) */
	private final static String PASSRENEW_STANDARD = "STANDARD";

	/** Constant text for PassRenew Status PARKING (as of 2.16.1, JTL) */
	private final static String PASSRENEW_PARKING = "PARKING";

	private static EventLogger logger = EventLogger
			.getLogger(DLRQueryEligibilityProductsRules.class);

	/**
	 * Transforms a DTITransactionTO object containing a DLR query eligible ticket
	 * request into the format required by the DLR provider.
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
	 * Transforms a query ticket response string from the DLR provider and
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
		
		QueryEligibilityProductsResponseTO qtResp = new QueryEligibilityProductsResponseTO();

		// Set up the Payload and Command Header Responses.
		PayloadHeaderTO payloadHdrTO = TransformRules
				.createRespPayloadHdr(dtiTxn);
		
		/*Command Header*/
		CommandHeaderTO commandHdrTO = TransformRules.createRespCmdHdr(dtiTxn);
		
	
		dtiRespTO.setPayloadHeader(payloadHdrTO);
		dtiRespTO.setCommandHeader(commandHdrTO);

		// Search for blatant error first
		GWBodyTO gwBodyTO = gwRespTO.getBodyTO();
		if (gwBodyTO == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class,
					DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
					"Internal Error:  Gateway XML allowed a response with null body.");
		}

		GWStatusTO gwStatusTO = gwBodyTO.getStatusTO();
		if (gwStatusTO == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class,
					DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
					"Internal Error:  Gateway XML allowed a response with null status.");
		}

		String statusString = gwStatusTO.getStatusCode();
		if (statusString == null) {
			// throw bad provider response error
			throw new DTIException(TransformRules.class,
					DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
					"Internal Error:  Gateway XML allowed a response with null status code.");
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
			dtiTxn = transformError(dtiTxn, dtiRespTO, statusString,
					xmlResponse);
			return dtiTxn;
		}
		// If the provider had no error, transform the response.
		TicketTO dtiTktTO = new TicketTO();
		GWQueryTicketRespTO gwQryTktRespTO = gwBodyTO
				.getQueryTicketResponseTO();
		GWDataRequestRespTO gwDataRespTO = gwQryTktRespTO.getDataRespTO();

		// Verifying for UpgradedPLU Based on this will , decide for eligible
		// and inelligible

		if (gwDataRespTO.getUpgradePLUList() != null
				&& gwDataRespTO.getUpgradePLUList().size() > 0) {
			
			ArrayList<String> upGradePluList=new ArrayList<String>();
			
			for(UpgradePLUList upgradePLU:gwDataRespTO.getUpgradePLUList()){
				
				String plu=upgradePLU.getPLU();
				upGradePluList.add(plu);
				
				/*Get the list of GuestProductTO , transaction will stop in case if no product is found*/
				GuestProductTO guestProduct = setGuestProductDetails(gwDataRespTO,dtiTktTO,plu);
				if (guestProduct != null) {
					
					/*If Picture is not present in response marking resultType as INELIGIBLE*/
					if(guestProduct.getGwDataRespTO().getHasPicture()==null){
						dtiTktTO.setResultType(ResultType.INELIGIBLE);
					}
					/* visualId*/
					String visualId = gwDataRespTO.getVisualID();
					
					/* contact GUID*/
					String contactGUID = null;// need to see if contact tag is present throw exception if contact is not there
					
					if((gwDataRespTO.getContact() != null) && (gwDataRespTO.getContact().size() > 0)){
						
						contactGUID=gwDataRespTO.getContact().get(0).getContactGUID();
						if(contactGUID != null){
							processGUID(visualId,contactGUID);
						}else{
							logger.sendEvent("No Contact GUID found , Product is INELIGIBLE ",EventType.DEBUG,THISINSTANCE);
							dtiTktTO.setResultType(ResultType.INELIGIBLE);
							//If DLR query ticket returns without a picture and without usage, then it should be INELIGIBLE. TODO
							//QEP should then return INELIGIBLE.
							//If someone attempts to upgrade with a ticket that had no demos, then it fails with 564.
						}
					}else{
						logger.sendEvent("No Contact GUID found , Product is INELIGIBLE ",EventType.DEBUG,THISINSTANCE);
						dtiTktTO.setResultType(ResultType.INELIGIBLE);
					}
										
					/*Insert the GUID in table if GUID is not present assuming contact tag will be present in the response as it is present in req */
					/*Setting up the Upgraded Product Detail*/
					
					ArrayList<DBProductTO> newProductcatalogList=
							getUpgradedProduct(upGradePluList, dtiTktTO, dtiTxn);
					
					/*Setting up the respArrayList<UpgradeCatalogTO>onse Detail*/
					setQueryEligibleResponseCommand(guestProduct,dtiTktTO,newProductcatalogList);
					
				}else{
					logger.sendEvent("Not able to find any Ticket Information in DTI.", EventType.DEBUG,THISINSTANCE);
				}
				/*Pass the object to QEP command Object*/
				qtResp.add(dtiTktTO);
			}
		} else {
			
			logger.sendEvent("No Ticket Detail Found.", EventType.DEBUG,THISINSTANCE);
			
			//Throw Exception 
			throw new DTIException(DTIErrorCode.TICKET_INVALID);
		}

		// Pass the information fetched in form of ticket to response
		
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

		// Copy over key ticket values for error response to match
		// existing format.
		if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET) {

			QueryEligibleProductsRequestTO queryReq = (QueryEligibleProductsRequestTO) dtiTxn
					.getRequest().getCommandBody();
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
	 *            the DTI Transaction object
	 * @throws DTIException
	 *             if any rule fails.
	 */
	public static void applyDLRQueryTicketRules(DTITransactionTO dtiTxn)
			throws DTIException {

		QueryTicketRequestTO reqTO = (QueryTicketRequestTO) dtiTxn.getRequest()
				.getCommandBody();
		ArrayList<TicketTO> aTktList = reqTO.getTktList();
		TicketTO aTicketTO = aTktList.get(0);

		TicketRules.validateExternalTktIdOnly(aTicketTO);

		return;
	}

	/**
	 * Sets the guest product details.
	 * 
	 * @param gwDataRespTO
	 *            the gw data resp TO
	 * @return the array list
	 * @throws DTIException
	 *             the DTI exception
	 */
	public static GuestProductTO setGuestProductDetails(
			GWDataRequestRespTO gwDataRespTO,TicketTO dtiTktTO, String plu) throws DTIException {
		
		/*This method is used for processing step 1 */
		GuestProductTO guestProductTO=new GuestProductTO();
		
		ArrayList<DBProductTO> dbProductTO = null;
		
		ArrayList<String> pluList = new ArrayList<String>();
		pluList.add(plu);
		try{
			dbProductTO = ProductKey.getProductsByTktName(pluList);
		}catch(Exception e){
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getProductsByTktName", e);
		}
		//Checking the dbProduct size
		if((dbProductTO != null) && (dbProductTO.size() > 0)){ 
			
		  logger.sendEvent(dbProductTO.toString(), EventType.DEBUG,THISINSTANCE);
		  ResultStatusTo resultStatusTo = new ResultStatusTo(
					ResultType.ELIGIBLE);
		  dtiTktTO.setResultType(ResultType.ELIGIBLE);
		  guestProductTO.setDbproductTO(dbProductTO.get(0));
		  
		  /*Setting up GWDataRequestRespTO in case of DLR*/
		  guestProductTO.setGwDataRespTO(gwDataRespTO);
		  
		}else{
		  logger.sendEvent("Not able to find any Ticket Information in DTI.", EventType.DEBUG,THISINSTANCE);
		  ResultStatusTo resultStatusTo = new ResultStatusTo(
					ResultType.INELIGIBLE);
		  dtiTktTO.setResultType(ResultType.INELIGIBLE);
			
		}
		/*Return the GuestProductTO*/
		return guestProductTO;
	}
	
	/**
	 * Process GUID.
	 *
	 * @param guid the guid
	 * @param visualid the visualid
	 * @throws DTIException the DTI exception
	 */
	private static void processGUID(String guid, String visualid)
			throws DTIException {
		/* Step 3B */
		try {
			// Checking for the visual Id in ENTTL_GUID insert if no visualid is
			// present
			EnttlGuidTO entGuid = ProductKey.getGuId(visualid);

			if (entGuid == null) {
				// Inserting the visual id in table ENTTL_GUID
				ProductKey.insertGuId(visualid, guid);
			}
		} catch (Exception dtie) {
			logger.sendEvent("database exception occured " + dtie.getMessage(),
					EventType.EXCEPTION, THISINSTANCE);
			throw new DTIException(TransformRules.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Provider responded with a non-numeric status code.");
		}
	}
	
	/**
	 * Gets the upgraded product.
	 *
	 * @return the upgraded product
	 * @throws DTIException the DTI exception
	 */
	public static ArrayList<DBProductTO> getUpgradedProduct(ArrayList<String> listfUpgradedPLUs,TicketTO dtiTktTO,DTITransactionTO dtiTxn)throws DTIException{
		
		ArrayList<DBProductTO> newProductCatalogTO=null;
		UpgradeCatalogTO upGradeCatalogTo=null;
		
		
		if((listfUpgradedPLUs == null) || (listfUpgradedPLUs.size() == 0)){
		  logger.sendEvent("No PLU's List  fetched from DTI Ticketing System",EventType.DEBUG,THISINSTANCE);
		  return newProductCatalogTO;
		}
		/*Call to fetch the saleable ProductList*/
		try{
			upGradeCatalogTo=ProductKey.getAPUpgradeCatalog(dtiTxn.getEntityTO(), DTITransactionTO.TPI_CODE_DLR);			
		
		}catch(Exception dtie){
			logger.sendEvent("database exception occured " + dtie.getMessage(),
					EventType.EXCEPTION, THISINSTANCE);
			throw new DTIException(TransformRules.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Provider responded with a non-numeric status code.");
		}
		
		if(upGradeCatalogTo != null){
			ArrayList<DBProductTO > productList = upGradeCatalogTo.getProductList();
			
			if((productList != null) && (productList.size() > 0)){
				
				logger.sendEvent("Orignal List of Salaeable Product Obtaned "+productList.size(),EventType.DEBUG,THISINSTANCE);
			
				if(matchUpgradedProductSize(listfUpgradedPLUs.size(),productList.size())){
				
					ResultStatusTo resultStatusTo = new ResultStatusTo(
							ResultType.ELIGIBLE);
					 dtiTktTO.setResultType(ResultType.ELIGIBLE);
					
					/*Retrieving the list of products for guest PLU's only*/
					upGradeCatalogTo.retainDLRPLUs(listfUpgradedPLUs);
					
					/*Obtaining the new List of saleable products*/
					newProductCatalogTO=upGradeCatalogTo.getProductList();
					
					if(newProductCatalogTO==null || newProductCatalogTO.size() ==0){
						dtiTktTO.setResultType(ResultType.INELIGIBLE);
						return newProductCatalogTO;
					}
					logger.sendEvent("List of Salaeable Product Obtaned for given PLU "+newProductCatalogTO.size(),EventType.DEBUG,null);
					
				}
			}else{
				logger.sendEvent("Not able to find any Ticket Information in DTI.", EventType.DEBUG,null);
				  ResultStatusTo resultStatusTo = new ResultStatusTo(
							ResultType.INELIGIBLE);
				  dtiTktTO.setResultType(ResultType.INELIGIBLE);
			}
		}
		return newProductCatalogTO;
	}
	
	/**
	 * Match upgraded product size.
	 *
	 * @param upGradedPluList the up graded plu list
	 * @param productCataLogList the product cata log list
	 * @return true, if successful
	 */
	private static boolean matchUpgradedProductSize(int upGradedPluList,int productCataLogList){
		
		boolean eligibleFlag=false;
		if((upGradedPluList >= productCataLogList) || (upGradedPluList < productCataLogList)){
			eligibleFlag=true;
		}
		
		return eligibleFlag;
	}
	
	/**
	 * Sets the query eligible response command.
	 *
	 * @param upgradedProduct the upgraded product
	 * @param dtiTktTO the dti tkt TO
	 * @param gwDataRespTO the gw data resp TO
	 * @throws DTIException the DTI exception
	 */
	private static void setQueryEligibleResponseCommand(
			GuestProductTO guestProductTO, TicketTO dtiTktTO,ArrayList<DBProductTO> upgradedProductTOList)
			throws DTIException {
		DemographicsTO ticketDemo = new DemographicsTO();
		BigDecimal prodPrice = null, prodTax = null, prodUpgradePrice = null, prodUpgrdTax = null;
		EligibleProductsTO eligibleProductsTO;
		
		DBProductTO dbProductTO=guestProductTO.getDbproductTO();
		GWDataRequestRespTO gwDataRespTO=guestProductTO.getGwDataRespTO();
		
		if(dbProductTO!=null && gwDataRespTO.getContact() != null && gwDataRespTO.getContact().size() > 0) {

			// Set TktItem: Always only one.
			dtiTktTO.setTktItem(new BigInteger(ITEM_1));
			
			// Set TktID (for DLR it's External)
			String visualId = gwDataRespTO.getVisualID();
			dtiTktTO.setExternal(visualId);
			
			dtiTktTO.setProdCode(dbProductTO.getPdtCode());
			
			if(gwDataRespTO.getContact()==null && gwDataRespTO.getContact().size()==0){
				dtiTktTO.setResultType(ResultType.INELIGIBLE);
			}else{
				
			}
			dtiTktTO.setGuestType(dbProductTO.getGuestType());
			
			for(Contact contact : gwDataRespTO.getContact()) {
				// FirstName
				if (contact.getFirstName() != null) {
					ticketDemo.setFirstName(contact.getFirstName());
				}

				// LastName
				if (contact.getLastName() != null) {
					ticketDemo.setLastName(contact.getLastName());
				}

				// Addr1
				if (contact.getStreet1() != null) {
					ticketDemo.setAddr1(contact.getStreet1());
				}

				// Addr2
				if (contact.getStreet2() != null) {
					ticketDemo.setAddr2(contact.getStreet2());
				}
				
				// City
				if (contact.getCity() != null) {
					ticketDemo.setCity(contact.getCity());
				}

				// State
				if (contact.getState() != null) {
					ticketDemo.setState(contact.getState());
				}

				// ZIP
				if (contact.getZip() != null) {
					ticketDemo.setZip(contact.getZip());
				}

				// Country
				if (contact.getCountryCode() != null) {
					ticketDemo.setCountry(contact.getCountryCode());
				}

				// Telephone
				if (contact.getPhone() != null) {
					ticketDemo.setTelephone(contact.getPhone());
				}

				// Cell
				if (contact.getEmail() != null) {
					ticketDemo.setCellPhone(contact.getCell());
				}
				
				// Email
				if (contact.getEmail() != null) {
					ticketDemo.setEmail(contact.getEmail());
				}
				
				// Gender (as of 2.16.1, JTL) only returned if Demographics
				// "ALL" specified.
				// Note: We're using Gender Resp String here because Galaxy
				// types vary between
				// request (Integer) and response (String)
				if ((contact.getGender() != null)) {

					if (contact.getGender()
							.equalsIgnoreCase("Male")) {
						ticketDemo.setGender(GenderType.MALE);
					} else if (contact.getGender()
							.equalsIgnoreCase("Female")) {
						ticketDemo.setGender(GenderType.FEMALE);
					} else {
						ticketDemo.setGender(GenderType.UNSPECIFIED);
					}
				}

				// DOB (as of 2.16.1, JTL) only returned if Demographics
				// "ALL" specified.
				if ((contact.getDob() != null)) {
					DateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
					Date date;
					try {
						date = format.parse(contact.getDob());
						GregorianCalendar gregorianCalendar = new GregorianCalendar();
						gregorianCalendar.setTime(date);
						ticketDemo.setDateOfBirth(gregorianCalendar);
					} catch (ParseException e) {
						// TODO
					}
				}
				
				dtiTktTO.addTicketDemographic(ticketDemo);
				ticketDemo = new DemographicsTO();
			}
			
			/*Demographics start*/
			// FirstName
			if (gwDataRespTO.getFirstName() != null) {
				ticketDemo.setFirstName(gwDataRespTO.getFirstName());
			}

			// LastName
			if (gwDataRespTO.getLastName() != null) {
				ticketDemo.setLastName(gwDataRespTO.getLastName());
			}

			// Addr1
			if (gwDataRespTO.getStreet1() != null) {
				ticketDemo.setAddr1(gwDataRespTO.getStreet1());
			}

			// Addr2
			if (gwDataRespTO.getStreet2() != null) {
				ticketDemo.setAddr2(gwDataRespTO.getStreet2());
			}

			// City
			if (gwDataRespTO.getCity() != null) {
				ticketDemo.setCity(gwDataRespTO.getCity());
			}

			// State
			if (gwDataRespTO.getState() != null) {
				ticketDemo.setState(gwDataRespTO.getState());
			}

			// ZIP
			if (gwDataRespTO.getZip() != null) {
				ticketDemo.setZip(gwDataRespTO.getZip());
			}

			// Country
			if (gwDataRespTO.getCountryCode() != null) {
				ticketDemo.setCountry(gwDataRespTO.getCountryCode());
			}

			// Telephone
			if (gwDataRespTO.getPhone() != null) {
				ticketDemo.setTelephone(gwDataRespTO.getPhone());
			}

			// Email
			if (gwDataRespTO.getEmail() != null) {
				ticketDemo.setEmail(gwDataRespTO.getEmail());
			}

			// Gender (as of 2.16.1, JTL) only returned if Demographics
			// "ALL" specified.
			// Note: We're using Gender Resp String here because Galaxy
			// types vary between
			// request (Integer) and response (String)
			if ((gwDataRespTO.getGenderRespString() != null)) {

				if (gwDataRespTO.getGenderRespString()
						.equalsIgnoreCase("Male")) {
					ticketDemo.setGender(GenderType.MALE);
				} else if (gwDataRespTO.getGenderRespString()
						.equalsIgnoreCase("Female")) {
					ticketDemo.setGender(GenderType.FEMALE);
				} else {
					ticketDemo.setGender(GenderType.UNSPECIFIED);
				}
			}

			// DOB (as of 2.16.1, JTL) only returned if Demographics
			// "ALL" specified.
			if ((gwDataRespTO.getDateOfBirth() != null)) {
				ticketDemo
						.setDateOfBirth(gwDataRespTO.getDateOfBirth());
			}
			/*Demographics end*/
			
			/*Adding ticketDemo to the ticket*/
			if (ticketDemo != null) {
				dtiTktTO.addTicketDemographic(ticketDemo);
			}
			
			// TktValidity ValidStart and ValidEnd
			boolean startDateSet = false;
			if (gwDataRespTO.getItemKind() == GWDataRequestRespTO.ItemKind.REGULAR_TICKET) {
				if (gwDataRespTO.getDateSold() != null || gwDataRespTO.getTicketDate() != null || gwDataRespTO.getStartDateTime() != null) {
					dtiTktTO.setTktValidityValidStart(dbProductTO
							.getStartSaleDate());
					startDateSet = true;
				}

				boolean endDateSet = false;
				if (gwDataRespTO.getExpirationDate() != null || gwDataRespTO.getEndDateTime() != null) {
					dtiTktTO.setTktValidityValidEnd(dbProductTO
							.getEndValidDate());
					endDateSet = true;
				}

				// Note: HARD CODED!!! Arbitrarily set the end date if
				// the start date is set.
				// This conforms with the other provider.
				if ((startDateSet == true) && (endDateSet == false)) {
					dtiTktTO.setTktValidityValidEnd(new GregorianCalendar(
							2099, 11, 31));
				}

			} else {
				if (gwDataRespTO.getDateOpened() != null) {
					dtiTktTO.setTktValidityValidStart(dbProductTO
							.getStartValidDate());
				}

				if (gwDataRespTO.getValidUntil() != null) {
					dtiTktTO.setTktValidityValidEnd(dbProductTO
							.getStartValidDate());
				}
			}
			
			// SRP Price
			dtiTktTO.setTktPrice(dbProductTO.getStandardRetailPrice());

			// SRP Tax
			dtiTktTO.setTktTax(dbProductTO.getStandardRetailTax());
			
			// Tkt Status (Voidable YES or NO)
			// Note: Locked out must be false for ticket to be voidable.
			// Note: Use count must be zero for ticket to be voidable.
			ArrayList<TktStatusTO> tktStatusList = dtiTktTO
					.getTktStatusList();
			TktStatusTO tktStatus = dtiTktTO.new TktStatusTO();
			tktStatus.setStatusItem(VOIDABLE);
			if ((gwDataRespTO.getReturnable().booleanValue() == true)
					&& (gwDataRespTO.getLockedOut().booleanValue() == false)
					&& (gwDataRespTO.getUseCount().intValue() == 0))
				tktStatus.setStatusValue(YES);
			else {
				tktStatus.setStatusValue(NO);
			}
			tktStatusList.add(tktStatus);

			// Tkt Status (Active YES or NO)
			// Note: Locked out must be false for ticket to be active.
			if (gwDataRespTO.getItemKind() == GWDataRequestRespTO.ItemKind.PASS) {

				tktStatus = dtiTktTO.new TktStatusTO();
				tktStatus.setStatusItem(ACTIVE);

				GregorianCalendar startDateCal = gwDataRespTO
						.getDateOpened();

				// V 2.4 - 2011-12-05; CUS - date adjusted for validity,
				// but
				// original end date returned
				GregorianCalendar endDateCal = (GregorianCalendar) gwDataRespTO
						.getValidUntil().clone();

				// V 2.2 - JTL - 12/13/2010 - Adjust calendar by a grace
				// period
				// (add)
				// to account for east-coast/west-coast plus late park
				// closings.
				endDateCal.add(Calendar.HOUR_OF_DAY,
						DLR_ANNUAL_PASS_GRACE_PERIOD_HOURS);

				// Is the pass within the dates established?
				if ((startDateCal != null) && (endDateCal != null)) {

					if (DateTimeRules.isNowWithinDate(
							startDateCal.getTime(),
							endDateCal.getTime())) {

						if ((gwDataRespTO.getStatus() == GWDataRequestRespTO.Status.VALID)
								&& (gwDataRespTO.getLockedOut()
										.booleanValue() == false))
							tktStatus.setStatusValue(YES);
						else
							tktStatus.setStatusValue(NO);

					} else
						tktStatus.setStatusValue(NO);
				} else {
					tktStatus.setStatusValue(NO);
				}
				
				/*picture*/
				tktStatus.setStatusItem(PICTURE);
				
				if((gwDataRespTO.getHasPicture() != null) && (gwDataRespTO.getHasPicture().compareToIgnoreCase(YES)== 0)){
					tktStatus.setStatusValue(YES);
				}else{
					tktStatus.setStatusValue(NO);
				}
				
				/*payplan*/
				tktStatus.setStatusItem(PAYLAN);
				if(gwDataRespTO.getPayPlan().compareToIgnoreCase(YES)==0){
					tktStatus.setStatusValue(YES);
				}else{
					tktStatus.setStatusValue(NO);
				}
				tktStatusList.add(tktStatus);
			}
			

			// TktAttributes
			// PassType
			// TO DO need to add the code for UPGRADE
			if (gwDataRespTO.getItemKind() == GWDataRequestRespTO.ItemKind.PASS) {

				if (gwDataRespTO.getKind() != null) {

					String passType = LookupKey.getSimpleTPLookup(
							DLR_ID, gwDataRespTO.getKind(), PASS_KIND);
					dtiTktTO.setPassType(passType);

					String renewable = PASSRENEW_INELIGIBLE;

					if ((gwDataRespTO.getRenewable() != null)
							&& (gwDataRespTO.getRenewable())) {

						String passRenew = LookupKey.getSimpleTPLookup(
								DLR_ID, gwDataRespTO.getKind(),
								PASS_RENEW);

						if (passRenew == null) {
							renewable = PASSRENEW_STANDARD;
						} else if (passRenew.equalsIgnoreCase(PARKING)) {
							renewable = PASSRENEW_PARKING;
						} else {
							renewable = PASSRENEW_STANDARD;
						}
					}

					dtiTktTO.setPassRenew(renewable);

				} else {
					dtiTktTO.setPassType(ANNUAL_ATTR);
				}

			}

			// PassName
			if (gwDataRespTO.getPassKindName() != null) {
				dtiTktTO.setPassName(gwDataRespTO.getPassKindName());
			}

			// LastDateUsed
			if (gwDataRespTO.getDateUsed() != null) {
				dtiTktTO.setLastDateUsed(gwDataRespTO.getDateUsed());
			}

			// TimesUsed
			// Most of these don't have to be checked against the
			// request,
			// since they are not returned by eGalaxy. However, UseCount
			// is
			// called all the time, and it cannot be allowed to display
			// unless
			// explicitly requested.
			if (gwDataRespTO.getUseCount() != null) {
				int x = gwDataRespTO.getUseCount().intValue();
				String value = Integer.toString(x);
				dtiTktTO.setTimesUsed(new BigInteger(value));
			}

			// ReplacedByPass
			if ((gwDataRespTO.getStatus() == GWDataRequestRespTO.Status.REPLACED)
					|| (gwDataRespTO.getStatus() == GWDataRequestRespTO.Status.REPRINTED)) {

				// DLR Business rule - non-validated AP's are 19 digits
				// in length and then
				// get converted to 18 digits once ID is shown at the
				// gate.
				if ((gwDataRespTO.getVisualID().length() == 19)
						&& (gwDataRespTO.getLineageArray().size() > 0)) {

					ArrayList<LineageRecord> lineageList = gwDataRespTO
							.getLineageArray();

					for (/* each */LineageRecord aLineage : /* in */lineageList) {

						if ((aLineage.getStatus() == GWDataRequestRespTO.Status.BLOCKED)
								|| (aLineage.getStatus() == GWDataRequestRespTO.Status.VALID)) {

							if (aLineage.getVisualID().length() == 18) {
								dtiTktTO.setReplacedByPass(aLineage
										.getVisualID());
								break;
							}
						}
					}
				}
			}
			
			/*Ticket Status*/
			tktStatus = dtiTktTO.new TktStatusTO();
			tktStatus.setStatusItem(BLOCKED);
			if (gwDataRespTO.getStatus() == GWDataRequestRespTO.Status.BLOCKED) {
				tktStatus.setStatusValue(YES);
			} else {
				tktStatus.setStatusValue(NO);
			}
			tktStatusList.add(tktStatus);

			// Map "REDEEMABLE" to a status (as of 2.14)
			if (gwDataRespTO.getItemKind() == ItemKind.REGULAR_TICKET) {
				tktStatus = dtiTktTO.new TktStatusTO();
				tktStatus.setStatusItem(REDEEMABLE);

				boolean redeemable = true;

				// Has it been used?
				if (gwDataRespTO.getUseCount().intValue() > 0) {
					redeemable = false;
				}

				// Is it expired?
				if (gwDataRespTO.getExpirationDate() != null) {
					GregorianCalendar expirationDate = gwDataRespTO
							.getExpirationDate();
					GregorianCalendar todaysDate = new GregorianCalendar();

					if (expirationDate.compareTo(todaysDate) < 0) {
						redeemable = false;
					}
				}

				// Is its status valid?
				if (gwDataRespTO.getStatus() != Status.VALID) {
					redeemable = false;
				}

				// Does it have any remaining use?
				if (gwDataRespTO.getRemainingUse().intValue() <= 0) {
					redeemable = false;
				}

				if (redeemable) {
					tktStatus.setStatusValue(YES);
				} else {
					tktStatus.setStatusValue(NO);
				}
				tktStatusList.add(tktStatus);
			}
			
			// Eligible products
			for (/*each*/ DBProductTO productTO : /*in*/ upgradedProductTOList) {
				
				eligibleProductsTO = new EligibleProductsTO();
				
				// set the product code
				eligibleProductsTO.setProdCode(productTO.getPdtCode());
				
				// set the actual product price
				prodPrice = dbProductTO.getStandardRetailPrice();
				
				eligibleProductsTO.setProdPrice(prodPrice.toString());
				
				// set the actual product tax
				prodTax = dbProductTO.getStandardRetailTax();
				
				eligibleProductsTO.setProdTax(prodTax);
				
				// set the upgraded product price
				prodUpgradePrice = prodPrice.subtract(guestProductTO.getDbproductTO().getStandardRetailPrice());
				
				eligibleProductsTO.setUpgrdPrice(prodUpgradePrice.toString());
				
				// set the upgraded product tax
				prodUpgrdTax = prodTax.subtract(guestProductTO.getDbproductTO().getStandardRetailTax());
				eligibleProductsTO.setUpgrdTax(prodUpgrdTax.toString());
				// set the validity
				try {
					Integer dayCount = Integer.parseInt(productTO.getDayCount());
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dbProductTO.getStartSaleDate().getTime());
					
					calendar.add(calendar.DAY_OF_MONTH, dayCount);
					
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(calendar.getTime());
					
					eligibleProductsTO.setValidEnd(
							DatatypeFactory.newInstance().newXMLGregorianCalendar(gc));
				} catch (NullPointerException exception) { 
				
				} catch (NumberFormatException ne) { 
				
				} catch (Exception e) {
					throw new DTIException(ProductKey.class, DTIErrorCode.TP_INTERFACE_FAILURE,
					           "Exception executing getAPUpgradeCatalog", e);
				}
				// add the eligible product element to the list 
				dtiTktTO.addEligibleProducts(eligibleProductsTO);
			}
		} else {
			dtiTktTO = new TicketTO();
			dtiTktTO.setResultType(ResultType.INELIGIBLE);
		}
		
		// Need to add exception block here
		ResultStatusTo resultStatusTo = new ResultStatusTo(
				ResultType.ELIGIBLE);
		//dtiTktTO.setResultType(resultStatusTo.toString());
		// logger
		logger.sendEvent("Setting all the data in GuestProductTo",
				EventType.DEBUG, dtiTktTO.toString());
	}

}