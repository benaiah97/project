package pvt.disney.dti.gateway.rules.dlr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.dao.LookupKey;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.QueryTicketResponseTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO.GenderType;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.Status;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.ItemKind;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.LineageRecord;
import pvt.disney.dti.gateway.provider.dlr.xml.GWEnvelopeXML;
import pvt.disney.dti.gateway.rules.DateTimeRules;
import pvt.disney.dti.gateway.rules.TicketRules;
import pvt.disney.dti.gateway.rules.TransformConstants;
import pvt.disney.dti.gateway.rules.TransformRules;

/**
 * Class contains the DLR-specific query ticket rules.
 * 
 * @author lewit019
 * 
 */
public class DLRQueryTicketRules implements TransformConstants {

<<<<<<< HEAD
  /**
   * The number of hours to add to a pass expiration to ensure that
   * east-coast/west-coast time differences are accounted for (3) and the impact
   * of late closings (as many as 3 hours) (3 + 3 = 6 hours). Because the dates
   * from eGalaxy are returned as 12/27/2010 00:00:00, another 24 hours must be
   * added, making the grand total of the padding 30 hours.
   */
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

  /**
   * Transforms a DTITransactionTO object containing a DLR query ticket request
   * into the format required by the DLR provider.
   * 
   * @param dtiTxn
   *          The dtiTxn object containing the request.
   * @return the DLR query ticket string in the provider's format.
   * @throws DTIException
   *           for any error. Contains enough detail to formulate an error
   *           response to the seller.
   */
  static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

    String xmlRequest;
    GWEnvelopeTO envelopeTO = new GWEnvelopeTO(GWEnvelopeTO.GWTransactionType.QUERYTICKET);

    GWHeaderTO headerTO = envelopeTO.getHeaderTO();
    GWBodyTO bodyTO = envelopeTO.getBodyTO();
    GWQueryTicketRqstTO queryTicketTO = new GWQueryTicketRqstTO();

    // Set the Ticket ID (should be only one in the array)
    QueryTicketRequestTO queryReq = (QueryTicketRequestTO) dtiTxn.getRequest().getCommandBody();
    ArrayList<TicketTO> ticketList = queryReq.getTktList();
    TicketTO ticket = ticketList.get(0);

    // Set the Visual ID
    queryTicketTO.setVisualID(ticket.getExternal());

    // Set the include ticket demographics flag
    queryTicketTO.setIncludeTktDemographics(queryReq.isIncludeTktDemographics());

    // Set the include pass attributes flag
    queryTicketTO.setIncludePassAttributes(queryReq.isIncludePassAttributes());

    // Set the include ticket redeemability flag
    queryTicketTO.setIncludeTicketRedeemability(queryReq.isIncludeTicketRedeemability());
    
    // Set the include renewal attributes flag (as of 2.16.1, JTL)
    queryTicketTO.setIncludeRenewalAttributes(queryReq.isIncludeRenewalAttributes());

    bodyTO.setQueryTicketTO(queryTicketTO);

    // Set the source ID to the TS MAC
    String sourceID = dtiTxn.getRequest().getPayloadHeader().getTktSeller().getTsMac();
    headerTO.setSourceID(sourceID);

    // Set MessageID to DTI Payload ID (to TpRefNum as of 2.16.1)
    headerTO.setMessageID(new BigInteger(dtiTxn.getTpRefNum().toString()));

    // Set the echo data to the DTI payload ID (as of 2.16.1, JTL)
    headerTO.setEchoData(dtiTxn.getRequest().getPayloadHeader().getPayloadID());

    // Set the time stamp to the GMT date/time now.
    headerTO.setTimeStamp(DateTimeRules.getGMTDateNow());

    // Set the message type to a fixed value
    headerTO.setMessageType(GW_QRY_TKT_MSG_TYPE);

    xmlRequest = GWEnvelopeXML.getXML(envelopeTO);

    return xmlRequest;
  }

  /**
   * Transforms a query ticket response string from the DLR provider and updates
   * the DTITransactionTO object with the response information.
   * 
   * @param dtiTxn
   *          The transaction object for this request.
   * @param xmlResponse
   *          The DLR provider's response in string format.
   * @return The DTITransactionTO object, enriched with the response
   *         information.
   * @throws DTIException
   *           for any error. Contains enough detail to formulate an error
   *           response to the seller.
   */
  static DTITransactionTO transformResponse(DTITransactionTO dtiTxn, String xmlResponse) throws DTIException {

    // Parse the string into the Gateway Transfer Object!!
    GWEnvelopeTO gwRespTO = GWEnvelopeXML.getTO(xmlResponse);

    DTIResponseTO dtiRespTO = new DTIResponseTO();

    // Set up the Payload and Command Header Responses.
    PayloadHeaderTO payloadHdrTO = TransformRules.createRespPayloadHdr(dtiTxn);
    CommandHeaderTO commandHdrTO = TransformRules.createRespCmdHdr(dtiTxn);
    QueryTicketRequestTO queryReq = (QueryTicketRequestTO) dtiTxn.getRequest().getCommandBody();

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
      // we need to parse beyond just the status code so we can determine if
      // we need to throw based on an errorCode in the tickets clause rather
      // than just on the statusCode...this will keep us from returning
      // an invalid ticket error because we have a status code of 1300
      // when down farther in the QueryTicketsElement we might have a 999
      // error, which indicates that the error actually occurred because of a
      // critical failure inside of eGalaxy itself.
      dtiTxn = transformError(dtiTxn, dtiRespTO, statusString, xmlResponse);
      return dtiTxn;
    }

    // If the provider had no error, transform the response.
    TicketTO dtiTktTO = new TicketTO();
    DemographicsTO ticketDemo = null;
    GWQueryTicketRespTO gwQryTktRespTO = gwBodyTO.getQueryTicketResponseTO();
    GWDataRequestRespTO gwDataRespTO = gwQryTktRespTO.getDataRespTO();

    // Set TktItem: Always only one.
    dtiTktTO.setTktItem(new BigInteger(ITEM_1));

    // Set TktID (for DLR it's External)
    dtiTktTO.setExternal(gwDataRespTO.getVisualID());

    // Tkt Price
    dtiTktTO.setTktPrice(gwDataRespTO.getPrice());

    // Tkt Tax
    dtiTktTO.setTktTax(gwDataRespTO.getTax());

    // Tkt Status (Voidable YES or NO)
    // Note: Locked out must be false for ticket to be voidable.
    // Note: Use count must be zero for ticket to be voidable.
    ArrayList<TktStatusTO> tktStatusList = dtiTktTO.getTktStatusList();
    TktStatusTO tktStatus = dtiTktTO.new TktStatusTO();
    tktStatus.setStatusItem(VOIDABLE);
    if ((gwDataRespTO.getReturnable().booleanValue() == true) && (gwDataRespTO.getLockedOut().booleanValue() == false)
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

      GregorianCalendar startDateCal = gwDataRespTO.getDateOpened();

      // V 2.4 - 2011-12-05; CUS - date adjusted for validity, but
      // original end date returned
      GregorianCalendar endDateCal = (GregorianCalendar) gwDataRespTO.getValidUntil().clone();

      // V 2.2 - JTL - 12/13/2010 - Adjust calendar by a grace period
      // (add)
      // to account for east-coast/west-coast plus late park closings.
      endDateCal.add(Calendar.HOUR_OF_DAY, DLR_ANNUAL_PASS_GRACE_PERIOD_HOURS);

      // Is the pass within the dates established?
      if ((startDateCal != null) && (endDateCal != null)) {

        if (DateTimeRules.isNowWithinDate(startDateCal.getTime(), endDateCal.getTime())) {

          if ((gwDataRespTO.getStatus() == GWDataRequestRespTO.Status.VALID)
              && (gwDataRespTO.getLockedOut().booleanValue() == false))
            tktStatus.setStatusValue(YES);
          else
            tktStatus.setStatusValue(NO);

        } else
          tktStatus.setStatusValue(NO);
      } else {
        tktStatus.setStatusValue(NO);
      }
      tktStatusList.add(tktStatus);
    }

    // TktValidity ValidStart and ValidEnd
    boolean startDateSet = false;
    if (gwDataRespTO.getItemKind() == GWDataRequestRespTO.ItemKind.REGULAR_TICKET) {
      if (gwDataRespTO.getDateSold() != null) {
        dtiTktTO.setTktValidityValidStart(gwDataRespTO.getDateSold());
        startDateSet = true;
      } else if (gwDataRespTO.getTicketDate() != null) {
        dtiTktTO.setTktValidityValidStart(gwDataRespTO.getTicketDate());
        startDateSet = true;
      } else if (gwDataRespTO.getStartDateTime() != null) {
        dtiTktTO.setTktValidityValidStart(gwDataRespTO.getStartDateTime());
        startDateSet = true;
      }

      boolean endDateSet = false;
      if (gwDataRespTO.getExpirationDate() != null) {
        dtiTktTO.setTktValidityValidEnd(gwDataRespTO.getExpirationDate());
        endDateSet = true;
      } else if (gwDataRespTO.getEndDateTime() != null) {
        dtiTktTO.setTktValidityValidEnd(gwDataRespTO.getEndDateTime());
        endDateSet = true;
      }

      // Note: HARD CODED!!! Arbitrarily set the end date if the start
      // date is
      // set.
      // This maintains conformance with the other provider.
      if ((startDateSet == true) && (endDateSet == false)) {
        dtiTktTO.setTktValidityValidEnd(new GregorianCalendar(2099, 11, 31));
      }

    } else {
      if (gwDataRespTO.getDateOpened() != null) {
        dtiTktTO.setTktValidityValidStart(gwDataRespTO.getDateOpened());
      }

      if (gwDataRespTO.getValidUntil() != null) {
        dtiTktTO.setTktValidityValidEnd(gwDataRespTO.getValidUntil());
      }
    }

    // TktAttributes
    // PassType 
    if (gwDataRespTO.getItemKind() == GWDataRequestRespTO.ItemKind.PASS) {

      if (gwDataRespTO.getKind() != null) {

        String passType = LookupKey.getSimpleTPLookup(DLR_ID, gwDataRespTO.getKind(), PASS_KIND);
        dtiTktTO.setPassType(passType);
        
        // If pass type known and all requested, provide pass renew type.
        if (queryReq.isIncludeRenewalAttributes()) {
        
           String renewable = PASSRENEW_INELIGIBLE;
           
           if ((gwDataRespTO.getRenewable() != null) && (gwDataRespTO.getRenewable())) {
             
             String passRenew = LookupKey.getSimpleTPLookup(DLR_ID, gwDataRespTO.getKind(), PASS_RENEW);  
             
             if (passRenew == null) {
               renewable = PASSRENEW_STANDARD;
             } else if (passRenew.equalsIgnoreCase(PARKING)) {
               renewable = PASSRENEW_PARKING;
             } else {
               renewable = PASSRENEW_STANDARD;
             }
           }
          
         dtiTktTO.setPassRenew(renewable);
        
        }
        
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
    // Most of these don't have to be checked against the request,
    // since they are not returned by eGalaxy. However, UseCount is
    // called all the time, and it cannot be allowed to display unless
    // explicitly requested.
    if (queryReq.isIncludePassAttributes()) {
      if (gwDataRespTO.getUseCount() != null) {
        int x = gwDataRespTO.getUseCount().intValue();
        String value = Integer.toString(x);
        dtiTktTO.setTimesUsed(new BigInteger(value));
      }
    }

    // FirstName
    if (gwDataRespTO.getFirstName() != null) {
      if (ticketDemo == null)
        ticketDemo = new DemographicsTO();
      ticketDemo.setFirstName(gwDataRespTO.getFirstName());
    }

    // LastName
    if (gwDataRespTO.getLastName() != null) {
      if (ticketDemo == null)
        ticketDemo = new DemographicsTO();
      ticketDemo.setLastName(gwDataRespTO.getLastName());
    }

    // Addr1
    if (gwDataRespTO.getStreet1() != null) {
      if (ticketDemo == null)
        ticketDemo = new DemographicsTO();
      ticketDemo.setAddr1(gwDataRespTO.getStreet1());
    }

    // Addr2
    if (gwDataRespTO.getStreet2() != null) {
      if (ticketDemo == null)
        ticketDemo = new DemographicsTO();
      ticketDemo.setAddr2(gwDataRespTO.getStreet2());
    }

    // City
    if (gwDataRespTO.getCity() != null) {
      if (ticketDemo == null)
        ticketDemo = new DemographicsTO();
      ticketDemo.setCity(gwDataRespTO.getCity());
    }

    // State
    if (gwDataRespTO.getState() != null) {
      if (ticketDemo == null)
        ticketDemo = new DemographicsTO();
      ticketDemo.setState(gwDataRespTO.getState());
    }

    // ZIP
    if (gwDataRespTO.getZip() != null) {
      if (ticketDemo == null)
        ticketDemo = new DemographicsTO();
      ticketDemo.setZip(gwDataRespTO.getZip());
    }

    // Country
    if (gwDataRespTO.getCountryCode() != null) {
      if (ticketDemo == null)
        ticketDemo = new DemographicsTO();
      ticketDemo.setCountry(gwDataRespTO.getCountryCode());
    }

    // Telephone
    if (gwDataRespTO.getPhone() != null) {
      if (ticketDemo == null)
        ticketDemo = new DemographicsTO();
      ticketDemo.setTelephone(gwDataRespTO.getPhone());
    }

    // Email
    if (gwDataRespTO.getEmail() != null) {
      if (ticketDemo == null)
        ticketDemo = new DemographicsTO();
      ticketDemo.setEmail(gwDataRespTO.getEmail());
    }

    // Gender (as of 2.16.1, JTL) only returned if Demographics "ALL" specified.
    // Note: We're using Gender Resp String here because Galaxy types vary between
    // request (Integer) and response (String)
    if ((gwDataRespTO.getGenderRespString() != null)
        && (queryReq.isIncludeRenewalAttributes())) {
      if (ticketDemo == null)
        ticketDemo = new DemographicsTO();

      if (gwDataRespTO.getGenderRespString().equalsIgnoreCase("Male")) {
        ticketDemo.setGender(GenderType.MALE);
      } else if (gwDataRespTO.getGenderRespString().equalsIgnoreCase("Female")) {
        ticketDemo.setGender(GenderType.FEMALE);
      } else {
        ticketDemo.setGender(GenderType.UNSPECIFIED);
      }
    }

    // DOB (as of 2.16.1, JTL) only returned if Demographics "ALL" specified.
    if ((gwDataRespTO.getDateOfBirth() != null)
        && (queryReq.isIncludeRenewalAttributes())) {
      ticketDemo.setDateOfBirth(gwDataRespTO.getDateOfBirth());
    }

    // ReplacedByPass
    if ((gwDataRespTO.getStatus() == GWDataRequestRespTO.Status.REPLACED)
        || (gwDataRespTO.getStatus() == GWDataRequestRespTO.Status.REPRINTED)) {

      // DLR Business rule - non-validated AP's are 19 digits in length and then
      // get converted to 18 digits once ID is shown at the gate.
      if ((gwDataRespTO.getVisualID().length() == 19) && (gwDataRespTO.getLineageArray().size() > 0)) {

        ArrayList<LineageRecord> lineageList = gwDataRespTO.getLineageArray();

        for (/* each */LineageRecord aLineage : /* in */lineageList) {

          if ((aLineage.getStatus() == GWDataRequestRespTO.Status.BLOCKED)
              || (aLineage.getStatus() == GWDataRequestRespTO.Status.VALID)) {

            if (aLineage.getVisualID().length() == 18) {
              dtiTktTO.setReplacedByPass(aLineage.getVisualID());
              break;
            }
          }
        }
      }
    }

    if (ticketDemo != null) {
      dtiTktTO.addTicketDemographic(ticketDemo);
    }

    // mapped "BLOCKED" to status.
    QueryTicketRequestTO dtiQryTktRqstTO = (QueryTicketRequestTO) dtiTxn.getRequest().getCommandBody();
    if (dtiQryTktRqstTO.isIncludePassAttributes()) {
      tktStatus = dtiTktTO.new TktStatusTO();
      tktStatus.setStatusItem(BLOCKED);
      if (gwDataRespTO.getStatus() == GWDataRequestRespTO.Status.BLOCKED) {
        tktStatus.setStatusValue(YES);
      } else {
        tktStatus.setStatusValue(NO);
      }
      tktStatusList.add(tktStatus);
    }

    // Map "REDEEMABLE" to a status (as of 2.14)
    if (dtiQryTktRqstTO.isIncludeTicketRedeemability() && (gwDataRespTO.getItemKind() == ItemKind.REGULAR_TICKET)) {
      tktStatus = dtiTktTO.new TktStatusTO();
      tktStatus.setStatusItem(REDEEMABLE);

      boolean redeemable = true;

      // Has it been used?
      if (gwDataRespTO.getUseCount().intValue() > 0) {
        redeemable = false;
      }

      // Is it expired?
      if (gwDataRespTO.getExpirationDate() != null) {
        GregorianCalendar expirationDate = gwDataRespTO.getExpirationDate();
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

    QueryTicketResponseTO qtResp = new QueryTicketResponseTO();
    qtResp.addTicket(dtiTktTO);
    dtiRespTO.setCommandBody(qtResp);

    dtiTxn.setResponse(dtiRespTO);

    return dtiTxn;
  }

  /**
   * Transforms the DLR Query Ticket error
   * 
   * @param dtiTxn
   *          The DTITransacationTO object representing this transaction.
   * @param dtiRespTO
   *          The parsed version of the received response.
   * @param errorCode
   *          get to get the TP provider error information.
   * @throws DTIException
   *           Should the routine be unable to find the TP error lookup.
   */
  static DTITransactionTO transformError(DTITransactionTO dtiTxn, DTIResponseTO dtiRespTO, String statusCode,
      String xmlResponse) throws DTIException {

    String errorCode = DLRErrorRules.processStatusError(dtiTxn.getTransactionType(), statusCode, xmlResponse);

    DTIErrorTO dtiErrorTO = ErrorKey.getTPErrorMap(errorCode);
    DTIErrorCode.populateDTIErrorResponse(dtiErrorTO, dtiTxn, dtiRespTO);

    // Copy over key ticket values for error response to match
    // existing format.
    if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET) {

      QueryTicketRequestTO queryReq = (QueryTicketRequestTO) dtiTxn.getRequest().getCommandBody();
      QueryTicketResponseTO queryResp = new QueryTicketResponseTO();

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
      queryResp.addTicket(dtiTktTO);

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
   *          the DTI Transaction object
   * @throws DTIException
   *           if any rule fails.
   */
  public static void applyDLRQueryTicketRules(DTITransactionTO dtiTxn) throws DTIException {

    QueryTicketRequestTO reqTO = (QueryTicketRequestTO) dtiTxn.getRequest().getCommandBody();
    ArrayList<TicketTO> aTktList = reqTO.getTktList();
    TicketTO aTicketTO = aTktList.get(0);

    TicketRules.validateExternalTktIdOnly(aTicketTO);

    return;
  }
=======
	/**
	 * The number of hours to add to a pass expiration to ensure that east-coast/west-coast time differences are accounted for (3) and the impact of late closings (as many as 3 hours) (3 + 3 = 6 hours). Because the dates from eGalaxy are
	 * returned as 12/27/2010 00:00:00, another 24 hours must be added, making the grand total of the padding 30 hours.
	 */
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

	/**
	 * Transforms a DTITransactionTO object containing a DLR query ticket request into the format required by the DLR provider.
	 * 
	 * @param dtiTxn
	 *            The dtiTxn object containing the request.
	 * @return the DLR query ticket string in the provider's format.
	 * @throws DTIException
	 *             for any error. Contains enough detail to formulate an error response to the seller.
	 */
	static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

		String xmlRequest;
		GWEnvelopeTO envelopeTO = new GWEnvelopeTO(
				GWEnvelopeTO.GWTransactionType.QUERYTICKET);

		GWHeaderTO headerTO = envelopeTO.getHeaderTO();
		GWBodyTO bodyTO = envelopeTO.getBodyTO();
		GWQueryTicketRqstTO queryTicketTO = new GWQueryTicketRqstTO();

		// Set the Ticket ID (should be only one in the array)
		QueryTicketRequestTO queryReq = (QueryTicketRequestTO) dtiTxn
				.getRequest().getCommandBody();
		ArrayList<TicketTO> ticketList = queryReq.getTktList();
		TicketTO ticket = ticketList.get(0);

		// Set the Visual ID
		queryTicketTO.setVisualID(ticket.getExternal());

		// Set the include ticket demographics flag
		queryTicketTO.setIncludeTktDemographics(queryReq
				.isIncludeTktDemographics());

		// Set the include pass attributes flag
		queryTicketTO.setIncludePassAttributes(queryReq
				.isIncludePassAttributes());

		// Set the include ticket redeemability flag
		queryTicketTO.setIncludeTicketRedeemability(queryReq
				.isIncludeTicketRedeemability());

		// Set the include renewal attributes flag (as of 2.16.1, JTL)
		queryTicketTO.setIncludeRenewalAttributes(queryReq
				.isIncludeRenewalAttributes());

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

		// Set the time stamp to the GMT date/time now.
		headerTO.setTimeStamp(DateTimeRules.getGMTDateNow());

		// Set the message type to a fixed value
		headerTO.setMessageType(GW_QRY_TKT_MSG_TYPE);

		xmlRequest = GWEnvelopeXML.getXML(envelopeTO);

		return xmlRequest;
	}

	/**
	 * Transforms a query ticket response string from the DLR provider and updates the DTITransactionTO object with the response information.
	 * 
	 * @param dtiTxn
	 *            The transaction object for this request.
	 * @param xmlResponse
	 *            The DLR provider's response in string format.
	 * @return The DTITransactionTO object, enriched with the response information.
	 * @throws DTIException
	 *             for any error. Contains enough detail to formulate an error response to the seller.
	 */
	static DTITransactionTO transformResponse(DTITransactionTO dtiTxn,
			String xmlResponse) throws DTIException {

		// Parse the string into the Gateway Transfer Object!!
		GWEnvelopeTO gwRespTO = GWEnvelopeXML.getTO(xmlResponse);

		DTIResponseTO dtiRespTO = new DTIResponseTO();

		// Set up the Payload and Command Header Responses.
		PayloadHeaderTO payloadHdrTO = TransformRules
				.createRespPayloadHdr(dtiTxn);
		CommandHeaderTO commandHdrTO = TransformRules.createRespCmdHdr(dtiTxn);
		QueryTicketRequestTO queryReq = (QueryTicketRequestTO) dtiTxn
				.getRequest().getCommandBody();

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
		}
		catch (NumberFormatException e) {
			throw new DTIException(TransformRules.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Provider responded with a non-numeric status code.");
		}

		// If the provider had an error, map it and generate the response.
		// Copy the ticket identity and default the TktStatus Voidable to No
		if (statusCode != 0) {
			// we need to parse beyond just the status code so we can determine if
			// we need to throw based on an errorCode in the tickets clause rather
			// than just on the statusCode...this will keep us from returning
			// an invalid ticket error because we have a status code of 1300
			// when down farther in the QueryTicketsElement we might have a 999
			// error, which indicates that the error actually occurred because of a
			// critical failure inside of eGalaxy itself.
			dtiTxn = transformError(dtiTxn, dtiRespTO, statusString,
					xmlResponse);
			return dtiTxn;
		}

		// If the provider had no error, transform the response.
		TicketTO dtiTktTO = new TicketTO();
		DemographicsTO ticketDemo = null;
		GWQueryTicketRespTO gwQryTktRespTO = gwBodyTO
				.getQueryTicketResponseTO();
		GWDataRequestRespTO gwDataRespTO = gwQryTktRespTO.getDataRespTO();

		// Set TktItem: Always only one.
		dtiTktTO.setTktItem(new BigInteger(ITEM_1));

		// Set TktID (for DLR it's External)
		dtiTktTO.setExternal(gwDataRespTO.getVisualID());

		// Tkt Price
		dtiTktTO.setTktPrice(gwDataRespTO.getPrice());

		// Tkt Tax
		dtiTktTO.setTktTax(gwDataRespTO.getTax());

		// Tkt Status (Voidable YES or NO)
		// Note: Locked out must be false for ticket to be voidable.
		// Note: Use count must be zero for ticket to be voidable.
		ArrayList<TktStatusTO> tktStatusList = dtiTktTO.getTktStatusList();
		TktStatusTO tktStatus = dtiTktTO.new TktStatusTO();
		tktStatus.setStatusItem(VOIDABLE);
		if ((gwDataRespTO.getReturnable().booleanValue() == true) && (gwDataRespTO
				.getLockedOut().booleanValue() == false) && (gwDataRespTO
				.getUseCount().intValue() == 0)) tktStatus.setStatusValue(YES);
		else {
			tktStatus.setStatusValue(NO);
		}
		tktStatusList.add(tktStatus);

		// Tkt Status (Active YES or NO)
		// Note: Locked out must be false for ticket to be active.
		if (gwDataRespTO.getItemKind() == GWDataRequestRespTO.ItemKind.PASS) {

			tktStatus = dtiTktTO.new TktStatusTO();
			tktStatus.setStatusItem(ACTIVE);

			GregorianCalendar startDateCal = gwDataRespTO.getDateOpened();

			// V 2.4 - 2011-12-05; CUS - date adjusted for validity, but
			// original end date returned
			GregorianCalendar endDateCal = (GregorianCalendar) gwDataRespTO
					.getValidUntil().clone();

			// V 2.2 - JTL - 12/13/2010 - Adjust calendar by a grace period
			// (add)
			// to account for east-coast/west-coast plus late park closings.
			endDateCal.add(Calendar.HOUR_OF_DAY,
					DLR_ANNUAL_PASS_GRACE_PERIOD_HOURS);

			// Is the pass within the dates established?
			if ((startDateCal != null) && (endDateCal != null)) {

				if (DateTimeRules.isNowWithinDate(startDateCal.getTime(),
						endDateCal.getTime())) {

					if ((gwDataRespTO.getStatus() == GWDataRequestRespTO.Status.VALID) && (gwDataRespTO
							.getLockedOut().booleanValue() == false)) tktStatus
							.setStatusValue(YES);
					else tktStatus.setStatusValue(NO);

				}
				else tktStatus.setStatusValue(NO);
			}
			else {
				tktStatus.setStatusValue(NO);
			}
			tktStatusList.add(tktStatus);
		}

		// TktValidity ValidStart and ValidEnd
		boolean startDateSet = false;
		if (gwDataRespTO.getItemKind() == GWDataRequestRespTO.ItemKind.REGULAR_TICKET) {
			if (gwDataRespTO.getDateSold() != null) {
				dtiTktTO.setTktValidityValidStart(gwDataRespTO.getDateSold());
				startDateSet = true;
			}
			else if (gwDataRespTO.getTicketDate() != null) {
				dtiTktTO.setTktValidityValidStart(gwDataRespTO.getTicketDate());
				startDateSet = true;
			}
			else if (gwDataRespTO.getStartDateTime() != null) {
				dtiTktTO.setTktValidityValidStart(gwDataRespTO
						.getStartDateTime());
				startDateSet = true;
			}

			boolean endDateSet = false;
			if (gwDataRespTO.getExpirationDate() != null) {
				dtiTktTO.setTktValidityValidEnd(gwDataRespTO
						.getExpirationDate());
				endDateSet = true;
			}
			else if (gwDataRespTO.getEndDateTime() != null) {
				dtiTktTO.setTktValidityValidEnd(gwDataRespTO.getEndDateTime());
				endDateSet = true;
			}

			// Note: HARD CODED!!! Arbitrarily set the end date if the start
			// date is
			// set.
			// This maintains conformance with the other provider.
			if ((startDateSet == true) && (endDateSet == false)) {
				dtiTktTO.setTktValidityValidEnd(new GregorianCalendar(2099, 11,
						31));
			}

		}
		else {
			if (gwDataRespTO.getDateOpened() != null) {
				dtiTktTO.setTktValidityValidStart(gwDataRespTO.getDateOpened());
			}

			if (gwDataRespTO.getValidUntil() != null) {
				dtiTktTO.setTktValidityValidEnd(gwDataRespTO.getValidUntil());
			}
		}

		// TktAttributes
		// PassType
		if (gwDataRespTO.getItemKind() == GWDataRequestRespTO.ItemKind.PASS) {

			if (gwDataRespTO.getKind() != null) {

				String passType = LookupKey.getSimpleTPLookup(DLR_ID,
						gwDataRespTO.getKind(), PASS_KIND);
				dtiTktTO.setPassType(passType);

				// If pass type known and all requested, provide pass renew type.
				if (queryReq.isIncludeRenewalAttributes()) {

					String renewable = PASSRENEW_INELIGIBLE;

					if ((gwDataRespTO.getRenewable() != null) && (gwDataRespTO
							.getRenewable())) {

						String passRenew = LookupKey.getSimpleTPLookup(DLR_ID,
								gwDataRespTO.getKind(), PASS_RENEW);

						if (passRenew == null) {
							renewable = PASSRENEW_STANDARD;
						}
						else if (passRenew.equalsIgnoreCase(PARKING)) {
							renewable = PASSRENEW_PARKING;
						}
						else {
							renewable = PASSRENEW_STANDARD;
						}
					}

					dtiTktTO.setPassRenew(renewable);

				}

			}
			else {
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
		// Most of these don't have to be checked against the request,
		// since they are not returned by eGalaxy. However, UseCount is
		// called all the time, and it cannot be allowed to display unless
		// explicitly requested.
		if (queryReq.isIncludePassAttributes()) {
			if (gwDataRespTO.getUseCount() != null) {
				int x = gwDataRespTO.getUseCount().intValue();
				String value = Integer.toString(x);
				dtiTktTO.setTimesUsed(new BigInteger(value));
			}
		}

		// FirstName
		if (gwDataRespTO.getFirstName() != null) {
			if (ticketDemo == null) ticketDemo = new DemographicsTO();
			ticketDemo.setFirstName(gwDataRespTO.getFirstName());
		}

		// LastName
		if (gwDataRespTO.getLastName() != null) {
			if (ticketDemo == null) ticketDemo = new DemographicsTO();
			ticketDemo.setLastName(gwDataRespTO.getLastName());
		}

		// Addr1
		if (gwDataRespTO.getStreet1() != null) {
			if (ticketDemo == null) ticketDemo = new DemographicsTO();
			ticketDemo.setAddr1(gwDataRespTO.getStreet1());
		}

		// Addr2
		if (gwDataRespTO.getStreet2() != null) {
			if (ticketDemo == null) ticketDemo = new DemographicsTO();
			ticketDemo.setAddr2(gwDataRespTO.getStreet2());
		}

		// City
		if (gwDataRespTO.getCity() != null) {
			if (ticketDemo == null) ticketDemo = new DemographicsTO();
			ticketDemo.setCity(gwDataRespTO.getCity());
		}

		// State
		if (gwDataRespTO.getState() != null) {
			if (ticketDemo == null) ticketDemo = new DemographicsTO();
			ticketDemo.setState(gwDataRespTO.getState());
		}

		// ZIP
		if (gwDataRespTO.getZip() != null) {
			if (ticketDemo == null) ticketDemo = new DemographicsTO();
			ticketDemo.setZip(gwDataRespTO.getZip());
		}

		// Country
		if (gwDataRespTO.getCountryCode() != null) {
			if (ticketDemo == null) ticketDemo = new DemographicsTO();
			ticketDemo.setCountry(gwDataRespTO.getCountryCode());
		}

		// Telephone
		if (gwDataRespTO.getPhone() != null) {
			if (ticketDemo == null) ticketDemo = new DemographicsTO();
			ticketDemo.setTelephone(gwDataRespTO.getPhone());
		}

		// Email
		if (gwDataRespTO.getEmail() != null) {
			if (ticketDemo == null) ticketDemo = new DemographicsTO();
			ticketDemo.setEmail(gwDataRespTO.getEmail());
		}

		// Gender (as of 2.16.1, JTL) only returned if Demographics "ALL" specified.
		// Note: We're using Gender Resp String here because Galaxy types vary between
		// request (Integer) and response (String)
		if ((gwDataRespTO.getGenderRespString() != null) && (queryReq
				.isIncludeRenewalAttributes())) {
			if (ticketDemo == null) ticketDemo = new DemographicsTO();

			if (gwDataRespTO.getGenderRespString().equalsIgnoreCase("Male")) {
				ticketDemo.setGender(GenderType.MALE);
			}
			else if (gwDataRespTO.getGenderRespString().equalsIgnoreCase(
					"Female")) {
				ticketDemo.setGender(GenderType.FEMALE);
			}
			else {
				ticketDemo.setGender(GenderType.UNSPECIFIED);
			}
		}

		// DOB (as of 2.16.1, JTL) only returned if Demographics "ALL" specified.
		if ((gwDataRespTO.getDateOfBirth() != null) && (queryReq
				.isIncludeRenewalAttributes())) {
			ticketDemo.setDateOfBirth(gwDataRespTO.getDateOfBirth());
		}

		// ReplacedByPass
		if ((gwDataRespTO.getStatus() == GWDataRequestRespTO.Status.REPLACED) || (gwDataRespTO
				.getStatus() == GWDataRequestRespTO.Status.REPRINTED)) {

			// DLR Business rule - non-validated AP's are 19 digits in length and then
			// get converted to 18 digits once ID is shown at the gate.
			if ((gwDataRespTO.getVisualID().length() == 19) && (gwDataRespTO
					.getLineageArray().size() > 0)) {

				ArrayList<LineageRecord> lineageList = gwDataRespTO
						.getLineageArray();

				for (/* each */LineageRecord aLineage : /* in */lineageList) {

					if ((aLineage.getStatus() == GWDataRequestRespTO.Status.BLOCKED) || (aLineage
							.getStatus() == GWDataRequestRespTO.Status.VALID)) {

						if (aLineage.getVisualID().length() == 18) {
							dtiTktTO.setReplacedByPass(aLineage.getVisualID());
							break;
						}
					}
				}
			}
		}

		if (ticketDemo != null) {
			dtiTktTO.addTicketDemographic(ticketDemo);
		}

		// mapped "BLOCKED" to status.
		QueryTicketRequestTO dtiQryTktRqstTO = (QueryTicketRequestTO) dtiTxn
				.getRequest().getCommandBody();
		if (dtiQryTktRqstTO.isIncludePassAttributes()) {
			tktStatus = dtiTktTO.new TktStatusTO();
			tktStatus.setStatusItem(BLOCKED);
			if (gwDataRespTO.getStatus() == GWDataRequestRespTO.Status.BLOCKED) {
				tktStatus.setStatusValue(YES);
			}
			else {
				tktStatus.setStatusValue(NO);
			}
			tktStatusList.add(tktStatus);
		}

		// Map "REDEEMABLE" to a status (as of 2.14)
		if (dtiQryTktRqstTO.isIncludeTicketRedeemability() && (gwDataRespTO
				.getItemKind() == ItemKind.REGULAR_TICKET)) {
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
			}
			else {
				tktStatus.setStatusValue(NO);
			}
			tktStatusList.add(tktStatus);
		}

		QueryTicketResponseTO qtResp = new QueryTicketResponseTO();
		qtResp.addTicket(dtiTktTO);
		dtiRespTO.setCommandBody(qtResp);

		dtiTxn.setResponse(dtiRespTO);

		return dtiTxn;
	}

	/**
	 * Transforms the DLR Query Ticket error
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
			DTIResponseTO dtiRespTO, String statusCode, String xmlResponse) throws DTIException {

		String errorCode = DLRErrorRules.processStatusError(
				dtiTxn.getTransactionType(), statusCode, xmlResponse);

		DTIErrorTO dtiErrorTO = ErrorKey.getTPErrorMap(errorCode);
		DTIErrorCode.populateDTIErrorResponse(dtiErrorTO, dtiTxn, dtiRespTO);

		// Copy over key ticket values for error response to match
		// existing format.
		if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET) {

			QueryTicketRequestTO queryReq = (QueryTicketRequestTO) dtiTxn
					.getRequest().getCommandBody();
			QueryTicketResponseTO queryResp = new QueryTicketResponseTO();

			TicketTO dtiTktTO = new TicketTO();

			// Tkt Item
			dtiTktTO.setTktItem(new BigInteger(ITEM_1));

			// Tkt Identity
			if (queryReq.getTktList().size() != 0) {
				TicketTO dtiTktTOReq = queryReq.getTktList().get(0);
				if (dtiTktTOReq != null) dtiTktTO.setExternal(dtiTktTOReq
						.getExternal());
			}

			// Tkt Status (Voidable NO)
			ArrayList<TktStatusTO> tktStatusList = dtiTktTO.getTktStatusList();
			TktStatusTO tktStatus = dtiTktTO.new TktStatusTO();
			tktStatus.setStatusItem(VOIDABLE);
			tktStatus.setStatusValue(NO);
			tktStatusList.add(tktStatus);

			// Add the ticket to the response
			queryResp.addTicket(dtiTktTO);

			dtiRespTO.setCommandBody(queryResp);

		}

		dtiTxn.setResponse(dtiRespTO);

		return dtiTxn;
	}

	/**
	 * If a type of transaction has a specific number of provider centric rules, implement them here, but if there are a very limited set of rules, mostly common to both providers, implement in the BusinessRules in the parent package.
	 * 
	 * @param dtiTxn
	 *            the DTI Transaction object
	 * @throws DTIException
	 *             if any rule fails.
	 */
	public static void applyDLRQueryTicketRules(DTITransactionTO dtiTxn) throws DTIException {

		QueryTicketRequestTO reqTO = (QueryTicketRequestTO) dtiTxn.getRequest()
				.getCommandBody();
		ArrayList<TicketTO> aTktList = reqTO.getTktList();
		TicketTO aTicketTO = aTktList.get(0);

		TicketRules.validateExternalTktIdOnly(aTicketTO);

		return;
	}
>>>>>>> develop

}
