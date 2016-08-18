package pvt.disney.dti.gateway.rules.wdw;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.LookupKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.RenewEntitlementRequestTO;
import pvt.disney.dti.gateway.data.RenewEntitlementResponseTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.PaymentTO.PaymentType;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTRenewTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicInfo;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;
import pvt.disney.dti.gateway.rules.PaymentRules;

/**
 * This class is responsible for three major functions for WDW renew entitlement:<BR>
 * 1. Defining the business rules specific to WDW renew ticket.<BR>
<<<<<<< HEAD
 * 2. Defining the rules for transforming requests from the DTI transfer objects
 * to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer
 * objects to the DTI transfer objects.<BR>
=======
 * 2. Defining the rules for transforming requests from the DTI transfer objects to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer objects to the DTI transfer objects.<BR>
 * 
>>>>>>> develop
 * @author lewit019
 * @since 2.16.1
 */
public class WDWRenewEntitlementRules {

<<<<<<< HEAD
  /** Constant indicating the Update Transaction XSD. */
  private final static String NO_NAMESPACE_SCHEMA_LOCATION = "RenewTicketRequest.xsd";
  
  /** Request type header constant. */
  private final static String REQUEST_TYPE_RENEW_ENT = "Renew";

  /** Request sub-type header constant. */
  private final static String REQUEST_SUBTYPE_RENEW_ENT = "RenewTicket";

  /** TP Lookup Type for Installment */
  private final static String INSTALLMENT = "Installmnt"; // misspelled intentionally for column size
  
  /** TP Lookup Description for Down payment */
  private final static String DOWNPAYMENT = "Downpayment";
  
  /** Constant indicating all tags should be created. */
  private final static String[] UT_TAGS = { "All" };  
  
  /**
   * Transforms the request in the DTI transfer object into a valid eGalaxy request string.
   * @param dtiTxn the DTI transfer object
   * @return eGalaxy request string
   * @throws DTIException should a problem with conversion occur
   */
  static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {
    
    String xmlString = null;

    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    RenewEntitlementRequestTO dtiRenewEntReq = (RenewEntitlementRequestTO) dtiCmdBody;

    // === Command Level ===
    OTCommandTO atsCommand = new OTCommandTO(OTCommandTO.OTTransactionType.RENEWTICKET);
    atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

    // === Header Level ===
    OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn, REQUEST_TYPE_RENEW_ENT, REQUEST_SUBTYPE_RENEW_ENT);
    atsCommand.setHeaderTO(hdr);

    // === Renew Ticket Level ===
    OTRenewTicketTO otRenewTktTO = new OTRenewTicketTO();

    // Tags
    ArrayList<String> tagList = new ArrayList<String>();

    for (int i = 0; i < UT_TAGS.length; i++) {
      tagList.add(UT_TAGS[i]);
    }

    otRenewTktTO.setTagsList(tagList);

    // SiteNumber
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn.getAttributeTOMap();
    AttributeTO anAttributeTO = aMap.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
    if (anAttributeTO == null) {
      otRenewTktTO.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
    } else {
      otRenewTktTO.setSiteNumber(Integer.parseInt(anAttributeTO.getAttrValue()));
    }

    HashMap<String, DBProductTO> dtiProdMap = dtiTxn.getDbProdMap();

    // === RenewTicketInfo ===
    ArrayList<OTTicketInfoTO> otRenewTktList = otRenewTktTO.getRenewTicketInfoList();
    ArrayList<TicketTO> dtiTktList = dtiRenewEntReq.getTktList();

    for /* each */(TicketTO dtiTicket : /* in */dtiTktList) {

      OTTicketInfoTO otTicketInfo = new OTTicketInfoTO();

      // Item
      otTicketInfo.setItem(dtiTicket.getTktItem());

      // TicketSearchMode (from Existing Ticket)
      TicketTO existingTicket = dtiTicket.getExistingTktID();
      ArrayList<TicketIdType> dtiTicketTypeList = existingTicket.getTicketTypes();
      TicketIdType dtiTicketType = dtiTicketTypeList.get(0);
      
      OTTicketTO otTicket = WDWBusinessRules.setOTTicketTO(existingTicket, dtiTicketType);
      otTicketInfo.setTicketSearchMode(otTicket);

      // TicketType
      if ((dtiProdMap != null) && (dtiTicket.getProdCode() != null)) {
        DBProductTO dtiProduct = dtiProdMap.get(dtiTicket.getProdCode());
        otTicketInfo.setTicketType(dtiProduct.getMappedProviderTktNbr());
      }

      // TktNote
      if (dtiTicket.getTktNote() != null)
        otTicketInfo.setTicketNote(dtiTicket.getTktNote());

      // DemographicData (as of 2.15, JTL)
      ArrayList<DemographicsTO> tktDemoList = dtiTicket.getTicketDemoList();
      if (tktDemoList.size() > 0) {
        OTDemographicInfo otDemoInfo = new OTDemographicInfo();
        WDWBusinessRules.transformTicketDemoData(tktDemoList, otDemoInfo);
        OTDemographicData otDemoData = otDemoInfo.getDemoDataList().get(0);
        otTicketInfo.setSeasonPassDemo(otDemoData);
      }

      otRenewTktList.add(otTicketInfo);

    }

    // === Payment Info ===
    ArrayList<OTPaymentTO> otPaymentList = otRenewTktTO.getPaymentInfoList();
    ArrayList<PaymentTO> dtiPayList = dtiRenewEntReq.getPaymentList();
    EntityTO entityTO = dtiTxn.getEntityTO();

    WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList, entityTO);

    // Payload ID must become the transaction note for tracing.
    String payloadID = dtiRequest.getPayloadHeader().getPayloadID();
    otRenewTktTO.setTransactionNote(payloadID);    
    
    // Upgrade event always set to true.  DTI 2.15.1, JTL 
    otRenewTktTO.setGenerateEvent(true);
    
    // Set the renew ticket TO on the command
    atsCommand.setRenewTicketTO(otRenewTktTO);

    // Get the XML String
    xmlString = OTCommandXML.getXML(atsCommand);

    return xmlString;

  }
  
  /**
   * Transforms a renew ticket response string from the WDW provider and updates
   * the DTITransactionTO object with the response information.
   * 
   * @param dtiTxn
   *          The transaction object for this request.
   * @param xmlResponse
   *          The WDW provider's response in string format.
   * @return The DTITransactionTO object, enriched with the response
   *         information.
   * @throws DTIException
   *           for any error. Contains enough detail to formulate an error
   *           response to the seller. 
   */
  static void transformResponseBody(DTITransactionTO dtiTxn, OTCommandTO otCmdTO, DTIResponseTO dtiRespTO)
      throws DTIException {

    RenewEntitlementResponseTO dtiResRespTO = new RenewEntitlementResponseTO();
    OTRenewTicketTO otRenewTktTO = otCmdTO.getRenewTicketTO();
    dtiRespTO.setCommandBody(dtiResRespTO);

    // Ticket List
    ArrayList<TicketTO> dtiTktList = dtiResRespTO.getTicketList();
    ArrayList<OTTicketInfoTO> otTicketList = otRenewTktTO.getRenewTicketInfoList();
    if ((otTicketList != null) && (otTicketList.size() > 0)) {

      for /* each */(OTTicketInfoTO otTicketInfo : /* in */otTicketList) {

        TicketTO dtiTicketTO = new TicketTO();
        OTTicketTO otTicketTO = otTicketInfo.getTicket();

        dtiTicketTO.setTktItem(otTicketInfo.getItem());

        GregorianCalendar dssnDate = otTicketTO.getTdssnDate();
        String site = otTicketTO.getTdssnSite();
        String station = otTicketTO.getTdssnStation();
        String number = otTicketTO.getTdssnTicketId();
        dtiTicketTO.setDssn(dssnDate, site, station, number);

        dtiTicketTO.setTktNID(otTicketTO.getTCOD());
        dtiTicketTO.setBarCode(otTicketTO.getBarCode());
        dtiTicketTO.setTktPrice(otTicketInfo.getPrice());
        dtiTicketTO.setTktTax(otTicketInfo.getTax());

        if (otTicketInfo.getValidityStartDate() != null)
          dtiTicketTO.setTktValidityValidStart(otTicketInfo.getValidityStartDate());

        if (otTicketInfo.getValidityEndDate() != null)
          dtiTicketTO.setTktValidityValidEnd(otTicketInfo.getValidityEndDate());

        dtiTktList.add(dtiTicketTO);
      }
    }
    
    // Product, not required here.

    // Payment List
    // Note: Carryover from old system. Payment type of "Voucher" cannot be
    // returned on the response. 
    ArrayList<PaymentTO> dtiPmtList = dtiResRespTO.getPaymentList();
    ArrayList<OTPaymentTO> otPmtList = otRenewTktTO.getPaymentInfoList();
    WDWBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);

    // Renewal.ContractID
    for /* each */(PaymentTO aPaymentTO : /* in */dtiPmtList) {
      if (aPaymentTO.getInstallment() != null) {
        if (aPaymentTO.getInstallment().getContractId() != null) {
          String contractId = aPaymentTO.getInstallment().getContractId();
          dtiResRespTO.setContractId(contractId);
          break; // Leave this loop when the condition is 1st satisfied.
        }
      }
    }

    // Renewal.TktTransaction
    TicketTransactionTO dtiTktTxn = new TicketTransactionTO();
    OTTransactionDSSNTO otTxnDSSN = otRenewTktTO.getTransactionDSSN();
    String otTxnNid = otRenewTktTO.getTransactionCOD();
    dtiTktTxn.setDssn(otTxnDSSN.getDate(), otTxnDSSN.getSite(), otTxnDSSN.getStation(),
        otTxnDSSN.getTransactionId().toString());
    dtiTktTxn.setTranNID(otTxnNid);
    dtiResRespTO.setTktTran(dtiTktTxn);

    // ClientData (omitted, because this isn't a WDW reservation, and does generate
    // client data).

    return;
  }
  
  /**
   * Apply WDW renew entitlement rules.
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             the DTI exception
   */
  public static void applyWDWRenewEntitlementRules(DTITransactionTO dtiTxn)
      throws DTIException {
    
    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    RenewEntitlementRequestTO dtiRenewReq = (RenewEntitlementRequestTO) dtiCmdBody;

    // Ensure TP_Lookups have been populated in the database
    validateAndSaveTPLookups(dtiTxn, dtiRenewReq);

    // Validate that the credit card fields have appropriate sizes. (as of 2.2)
    ArrayList<PaymentTO> pmtList = dtiRenewReq.getPaymentList();
    PaymentRules.validateWDWCreditCardSizes(pmtList);

    // RULE: Validate that if the "installment" type of payment is present,
    // NOT REQUIRED
    
    // Determine if this is an installment transaction, and if so, mark it in the transfer object.
    ArrayList<PaymentTO> payListTO = dtiRenewReq.getPaymentList();
    for (/* each */ PaymentTO aPayment: /* in */ payListTO) {
      
      if (aPayment.getPayType() == PaymentType.INSTALLMENT) {
        dtiRenewReq.setInstallmentRenewRequest(true);
      }
      
    }
    

    return;
    
  }
  
  /**
   * Ensure TP_Lookups have been populated in the database
   * For OT renew, only the "Installmnt" lookup is required for down payment) 
   * @param dtiTxn
   * @param dtiResReq
   * @throws DTIException
   */
  private static void validateAndSaveTPLookups(DTITransactionTO dtiTxn, RenewEntitlementRequestTO dtiResReq)
      throws DTIException {

    ArrayList<TPLookupTO> tpLookups = new ArrayList<TPLookupTO>();
    
//    String downpayment = LookupKey.getSimpleTPLookup(dtiTxn.getTpRefNum(), DOWNPAYMENT, INSTALLMENT);
//    
//    // validate that we found a down payment and error if we didn't.
//    if (downpayment == null) {
//      throw new DTIException(WDWRenewEntitlementRules.class, DTIErrorCode.INVALID_AREA,
//          "TPCommandLookup did not find all values (missing translation for downpayment)");
//    }
//    
//    TPLookupTO tpLookupTO = new TPLookupTO();
//    tpLookupTO.setLookupType(TPLookupTO.TPLookupType.INSTALLMENT);
//    tpLookupTO.setLookupDesc(DOWNPAYMENT);
//    tpLookupTO.setLookupValue(downpayment);
//    tpLookups.add(tpLookupTO);
    
    dtiTxn.setTpLookupTOList(tpLookups);
  }
  
=======
	/** Constant indicating the Update Transaction XSD. */
	private final static String NO_NAMESPACE_SCHEMA_LOCATION = "RenewTicketRequest.xsd";

	/** Request type header constant. */
	private final static String REQUEST_TYPE_RENEW_ENT = "Renew";

	/** Request sub-type header constant. */
	private final static String REQUEST_SUBTYPE_RENEW_ENT = "RenewTicket";

	/** TP Lookup Type for Installment */
	private final static String INSTALLMENT = "Installmnt"; // misspelled intentionally for column size

	/** TP Lookup Description for Down payment */
	private final static String DOWNPAYMENT = "Downpayment";

	/** Constant indicating all tags should be created. */
	private final static String[] UT_TAGS = { "All" };

	/**
	 * Transforms the request in the DTI transfer object into a valid eGalaxy request string.
	 * 
	 * @param dtiTxn
	 *            the DTI transfer object
	 * @return eGalaxy request string
	 * @throws DTIException
	 *             should a problem with conversion occur
	 */
	static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

		String xmlString = null;

		DTIRequestTO dtiRequest = dtiTxn.getRequest();
		CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
		RenewEntitlementRequestTO dtiRenewEntReq = (RenewEntitlementRequestTO) dtiCmdBody;

		// === Command Level ===
		OTCommandTO atsCommand = new OTCommandTO(
				OTCommandTO.OTTransactionType.RENEWTICKET);
		atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
		atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

		// === Header Level ===
		OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
				REQUEST_TYPE_RENEW_ENT, REQUEST_SUBTYPE_RENEW_ENT);
		atsCommand.setHeaderTO(hdr);

		// === Renew Ticket Level ===
		OTRenewTicketTO otRenewTktTO = new OTRenewTicketTO();

		// Tags
		ArrayList<String> tagList = new ArrayList<String>();

		for (int i = 0; i < UT_TAGS.length; i++) {
			tagList.add(UT_TAGS[i]);
		}

		otRenewTktTO.setTagsList(tagList);

		// SiteNumber
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
				.getAttributeTOMap();
		AttributeTO anAttributeTO = aMap
				.get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
		if (anAttributeTO == null) {
			otRenewTktTO
					.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
		}
		else {
			otRenewTktTO.setSiteNumber(Integer.parseInt(anAttributeTO
					.getAttrValue()));
		}

		HashMap<String, DBProductTO> dtiProdMap = dtiTxn.getDbProdMap();

		// === RenewTicketInfo ===
		ArrayList<OTTicketInfoTO> otRenewTktList = otRenewTktTO
				.getRenewTicketInfoList();
		ArrayList<TicketTO> dtiTktList = dtiRenewEntReq.getTktList();

		for /* each */(TicketTO dtiTicket : /* in */dtiTktList) {

			OTTicketInfoTO otTicketInfo = new OTTicketInfoTO();

			// Item
			otTicketInfo.setItem(dtiTicket.getTktItem());

			// TicketSearchMode (from Existing Ticket)
			TicketTO existingTicket = dtiTicket.getExistingTktID();
			ArrayList<TicketIdType> dtiTicketTypeList = existingTicket
					.getTicketTypes();
			TicketIdType dtiTicketType = dtiTicketTypeList.get(0);

			OTTicketTO otTicket = WDWBusinessRules.setOTTicketTO(
					existingTicket, dtiTicketType);
			otTicketInfo.setTicketSearchMode(otTicket);

			// TicketType
			if ((dtiProdMap != null) && (dtiTicket.getProdCode() != null)) {
				DBProductTO dtiProduct = dtiProdMap
						.get(dtiTicket.getProdCode());
				otTicketInfo
						.setTicketType(dtiProduct.getMappedProviderTktNbr());
			}

			// TktNote
			if (dtiTicket.getTktNote() != null) otTicketInfo
					.setTicketNote(dtiTicket.getTktNote());

			// DemographicData (as of 2.15, JTL)
			ArrayList<DemographicsTO> tktDemoList = dtiTicket
					.getTicketDemoList();
			if (tktDemoList.size() > 0) {
				OTDemographicInfo otDemoInfo = new OTDemographicInfo();
				WDWBusinessRules.transformTicketDemoData(tktDemoList,
						otDemoInfo);
				OTDemographicData otDemoData = otDemoInfo.getDemoDataList()
						.get(0);
				otTicketInfo.setSeasonPassDemo(otDemoData);
			}

			otRenewTktList.add(otTicketInfo);

		}

		// === Payment Info ===
		ArrayList<OTPaymentTO> otPaymentList = otRenewTktTO
				.getPaymentInfoList();
		ArrayList<PaymentTO> dtiPayList = dtiRenewEntReq.getPaymentList();
		EntityTO entityTO = dtiTxn.getEntityTO();

		WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
				entityTO);

		// Payload ID must become the transaction note for tracing.
		String payloadID = dtiRequest.getPayloadHeader().getPayloadID();
		otRenewTktTO.setTransactionNote(payloadID);

		// Upgrade event always set to true. DTI 2.15.1, JTL
		otRenewTktTO.setGenerateEvent(true);

		// Set the renew ticket TO on the command
		atsCommand.setRenewTicketTO(otRenewTktTO);

		// Get the XML String
		xmlString = OTCommandXML.getXML(atsCommand);

		return xmlString;

	}

	/**
	 * Transforms a renew ticket response string from the WDW provider and updates the DTITransactionTO object with the response information.
	 * 
	 * @param dtiTxn
	 *            The transaction object for this request.
	 * @param xmlResponse
	 *            The WDW provider's response in string format.
	 * @return The DTITransactionTO object, enriched with the response information.
	 * @throws DTIException
	 *             for any error. Contains enough detail to formulate an error response to the seller.
	 */
	static void transformResponseBody(DTITransactionTO dtiTxn,
			OTCommandTO otCmdTO, DTIResponseTO dtiRespTO) throws DTIException {

		RenewEntitlementResponseTO dtiResRespTO = new RenewEntitlementResponseTO();
		OTRenewTicketTO otRenewTktTO = otCmdTO.getRenewTicketTO();
		dtiRespTO.setCommandBody(dtiResRespTO);

		// Ticket List
		ArrayList<TicketTO> dtiTktList = dtiResRespTO.getTicketList();
		ArrayList<OTTicketInfoTO> otTicketList = otRenewTktTO
				.getRenewTicketInfoList();
		if ((otTicketList != null) && (otTicketList.size() > 0)) {

			for /* each */(OTTicketInfoTO otTicketInfo : /* in */otTicketList) {

				TicketTO dtiTicketTO = new TicketTO();
				OTTicketTO otTicketTO = otTicketInfo.getTicket();

				dtiTicketTO.setTktItem(otTicketInfo.getItem());

				GregorianCalendar dssnDate = otTicketTO.getTdssnDate();
				String site = otTicketTO.getTdssnSite();
				String station = otTicketTO.getTdssnStation();
				String number = otTicketTO.getTdssnTicketId();
				dtiTicketTO.setDssn(dssnDate, site, station, number);

				dtiTicketTO.setTktNID(otTicketTO.getTCOD());
				dtiTicketTO.setBarCode(otTicketTO.getBarCode());
				dtiTicketTO.setTktPrice(otTicketInfo.getPrice());
				dtiTicketTO.setTktTax(otTicketInfo.getTax());

				if (otTicketInfo.getValidityStartDate() != null) dtiTicketTO
						.setTktValidityValidStart(otTicketInfo
								.getValidityStartDate());

				if (otTicketInfo.getValidityEndDate() != null) dtiTicketTO
						.setTktValidityValidEnd(otTicketInfo
								.getValidityEndDate());

				dtiTktList.add(dtiTicketTO);
			}
		}

		// Product, not required here.

		// Payment List
		// Note: Carryover from old system. Payment type of "Voucher" cannot be
		// returned on the response.
		ArrayList<PaymentTO> dtiPmtList = dtiResRespTO.getPaymentList();
		ArrayList<OTPaymentTO> otPmtList = otRenewTktTO.getPaymentInfoList();
		WDWBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);

		// Renewal.ContractID
		for /* each */(PaymentTO aPaymentTO : /* in */dtiPmtList) {
			if (aPaymentTO.getInstallment() != null) {
				if (aPaymentTO.getInstallment().getContractId() != null) {
					String contractId = aPaymentTO.getInstallment()
							.getContractId();
					dtiResRespTO.setContractId(contractId);
					break; // Leave this loop when the condition is 1st satisfied.
				}
			}
		}

		// Renewal.TktTransaction
		TicketTransactionTO dtiTktTxn = new TicketTransactionTO();
		OTTransactionDSSNTO otTxnDSSN = otRenewTktTO.getTransactionDSSN();
		String otTxnNid = otRenewTktTO.getTransactionCOD();
		dtiTktTxn
				.setDssn(otTxnDSSN.getDate(), otTxnDSSN.getSite(), otTxnDSSN
						.getStation(), otTxnDSSN.getTransactionId().toString());
		dtiTktTxn.setTranNID(otTxnNid);
		dtiResRespTO.setTktTran(dtiTktTxn);

		// ClientData (omitted, because this isn't a WDW reservation, and does generate
		// client data).

		return;
	}

	/**
	 * Apply WDW renew entitlement rules.
	 * 
	 * @param dtiTxn
	 *            the DTI transaction
	 * 
	 * @throws DTIException
	 *             the DTI exception
	 */
	public static void applyWDWRenewEntitlementRules(DTITransactionTO dtiTxn) throws DTIException {

		DTIRequestTO dtiRequest = dtiTxn.getRequest();
		CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
		RenewEntitlementRequestTO dtiRenewReq = (RenewEntitlementRequestTO) dtiCmdBody;

		// Ensure TP_Lookups have been populated in the database
		validateAndSaveTPLookups(dtiTxn, dtiRenewReq);

		// Validate that the credit card fields have appropriate sizes. (as of 2.2)
		ArrayList<PaymentTO> pmtList = dtiRenewReq.getPaymentList();
		PaymentRules.validateWDWCreditCardSizes(pmtList);

		// RULE: Validate that if the "installment" type of payment is present,
		// NOT REQUIRED

		// Determine if this is an installment transaction, and if so, mark it in the transfer object.
		ArrayList<PaymentTO> payListTO = dtiRenewReq.getPaymentList();
		for (/* each */PaymentTO aPayment : /* in */payListTO) {

			if (aPayment.getPayType() == PaymentType.INSTALLMENT) {
				dtiRenewReq.setInstallmentRenewRequest(true);
			}

		}

		return;

	}

	/**
	 * Ensure TP_Lookups have been populated in the database For OT renew, only the "Installmnt" lookup is required for down payment)
	 * 
	 * @param dtiTxn
	 * @param dtiResReq
	 * @throws DTIException
	 */
	private static void validateAndSaveTPLookups(DTITransactionTO dtiTxn,
			RenewEntitlementRequestTO dtiResReq) throws DTIException {

		ArrayList<TPLookupTO> tpLookups = new ArrayList<TPLookupTO>();

		// String downpayment = LookupKey.getSimpleTPLookup(dtiTxn.getTpRefNum(), DOWNPAYMENT, INSTALLMENT);
		//
		// // validate that we found a down payment and error if we didn't.
		// if (downpayment == null) {
		// throw new DTIException(WDWRenewEntitlementRules.class, DTIErrorCode.INVALID_AREA,
		// "TPCommandLookup did not find all values (missing translation for downpayment)");
		// }
		//
		// TPLookupTO tpLookupTO = new TPLookupTO();
		// tpLookupTO.setLookupType(TPLookupTO.TPLookupType.INSTALLMENT);
		// tpLookupTO.setLookupDesc(DOWNPAYMENT);
		// tpLookupTO.setLookupValue(downpayment);
		// tpLookups.add(tpLookupTO);

		dtiTxn.setTpLookupTOList(tpLookups);
	}

>>>>>>> develop
}
