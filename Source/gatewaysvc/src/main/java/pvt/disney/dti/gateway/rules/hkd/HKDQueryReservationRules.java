package pvt.disney.dti.gateway.rules.hkd;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.disney.logging.EventLogger;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryReservationRequestTO;
import pvt.disney.dti.gateway.data.QueryReservationResponseTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTCommandTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTHeaderTO;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTManageReservationTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTClientDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTPaymentTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTProductTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketTO;
import pvt.disney.dti.gateway.provider.hkd.xml.HkdOTCommandXML;
//import pvt.disney.dti.gateway.rules.wdw.WDWReservationRules;

/**
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class HKDQueryReservationRules {


  //private static final Class<WDWQueryReservationRules> THISOBJECT = WDWQueryReservationRules.class;

  /** Request type header constant. */
  private final static String REQUEST_TYPE_MANAGE = "Manage";

  /** Request sub-type header constant. */
  private final static String REQUEST_SUBTYPE_MANAGERES = "ManageReservation";

  /** Constant indicating all tags should be created. */
  private final static String ALL_TAGS = "All";

  /** Constant indicating the Manage Reservation XSD. */
  private final static String NO_NAMESPACE_SCHEMA_LOCATION = "ManageReservationRequest.xsd";

  /**
   * Constant indicating the Query variant of the Manage Reservation command.
   */
  private final static String COMMAND_TYPE = "Query";

  @SuppressWarnings("unused")
  private static final EventLogger logger = EventLogger
      .getLogger(HKDQueryReservationRules.class.getCanonicalName());

  /**
   * Transform the DTITransactionTO value object to the provider value objects and then pass those to XML Marshaling routines to create an XML string.
   * 
   * @param dtiTxn
   *            the DTI Transaction object.
   * @return the XML string version of the provider request.
   * @throws DTIException
   *             when any transformation error is encountered.
   */
  static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

    String xmlString = null;
    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    QueryReservationRequestTO dtiResReq = (QueryReservationRequestTO) dtiCmdBody;

    // === Command Level ===
    HkdOTCommandTO atsCommand = new HkdOTCommandTO(
        HkdOTCommandTO.OTTransactionType.MANAGERESERVATION);
    atsCommand.setXmlSchemaInstance(HKDBusinessRules.XML_SCHEMA_INSTANCE);
    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

    // === Header Level ===
    HkdOTHeaderTO hdr = HKDBusinessRules.transformOTHeader(dtiTxn,
        REQUEST_TYPE_MANAGE, REQUEST_SUBTYPE_MANAGERES);
    atsCommand.setHeaderTO(hdr);

    // === Manage Reservation Level ===
    HkdOTManageReservationTO otManageRes = new HkdOTManageReservationTO();

    // Tags (Work Rules)
    ArrayList<String> tagList = new ArrayList<String>();
    tagList.add(ALL_TAGS);
    otManageRes.setTagsList(tagList);

    // SiteNumber
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
        .getAttributeTOMap();
    AttributeTO anAttributeTO = aMap
        .get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
    if (anAttributeTO == null) {
      otManageRes.setSiteNumber(HKDBusinessRules.getSiteNumberProperty());
    }
    else {
      otManageRes.setSiteNumber(Integer.parseInt(anAttributeTO
          .getAttrValue()));
    }

    // CommandType
    otManageRes.setCommandType(COMMAND_TYPE);

    // ReservationCode
    if (dtiResReq.getResCode() != null) {
      if (dtiResReq.getResCode().length() != 0) {
        otManageRes.setReservationCode(dtiResReq.getResCode());
      }
    }
    else if (dtiResReq.getResNumber() != null) {
      if (dtiResReq.getResNumber().length() != 0) {
        otManageRes.setReservationId(new Integer(dtiResReq
            .getResNumber()));
      }
    }

    // Generate Event - Querying a reservation shouldn't trigger an event
    otManageRes.setGenerateEvent(false);

    // Set the manage reservation TO on the command
    atsCommand.setManageReservationTO(otManageRes);

    // Get the XML String
    xmlString = HkdOTCommandXML.getXML(atsCommand);

    return xmlString;

  }

  /**
   * Transforms a reservation response string from the WDW provider and updates the DTITransactionTO object with the response information.
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
      HkdOTCommandTO otCmdTO, DTIResponseTO dtiRespTO) throws DTIException {

    QueryReservationResponseTO dtiResRespTO = new QueryReservationResponseTO();
    HkdOTManageReservationTO otMngResTO = otCmdTO.getManageReservationTO();
    dtiRespTO.setCommandBody(dtiResRespTO);

    // Price mismatch warning
    if (dtiTxn.isPriceMismatch()) {
      DTIErrorTO mismatchWarn = ErrorKey
          .getErrorDetail(DTIErrorCode.PRICE_MISMATCH_WARNING);
      dtiRespTO.setDtiError(mismatchWarn);
    }

    // ResponseType
    dtiResRespTO.setResponseType(otMngResTO.getCommandType());

    // Ticket List
    ArrayList<TicketTO> dtiTktList = dtiResRespTO.getTicketList();
    ArrayList<HkdOTTicketInfoTO> otTicketList = otMngResTO.getTicketInfoList();
    if ((otTicketList != null) && (otTicketList.size() > 0)) {

      for (HkdOTTicketInfoTO otTicketInfo : otTicketList) {

        TicketTO dtiTicketTO = new TicketTO();
        HkdOTTicketTO otTicketTO = otTicketInfo.getTicket();

        dtiTicketTO.setTktItem(otTicketInfo.getItem());

        GregorianCalendar dssnDate = otTicketTO.getTdssnDate();
        String site = otTicketTO.getTdssnSite();
        String station = otTicketTO.getTdssnStation();
        String number = otTicketTO.getTdssnTicketId();
        dtiTicketTO.setDssn(dssnDate, site, station, number);

        dtiTicketTO.setMag(otTicketTO.getMagTrack());
        dtiTicketTO.setBarCode(otTicketTO.getBarCode());
        dtiTicketTO.setTktNID(otTicketTO.getTCOD());
        dtiTicketTO.setExternal(otTicketTO.getExternalTicketCode());

        dtiTicketTO.setTktPrice(otTicketInfo.getPrice());
        dtiTicketTO.setTktTax(otTicketInfo.getTax());

        if (otTicketInfo.getValidityStartDate() != null) dtiTicketTO
            .setTktValidityValidStart(otTicketInfo
                .getValidityStartDate());

        if (otTicketInfo.getValidityEndDate() != null) dtiTicketTO
            .setTktValidityValidEnd(otTicketInfo
                .getValidityEndDate());

        // AccountId
        dtiTicketTO.setAccountId(otTicketInfo.getAccountId());

        dtiTktList.add(dtiTicketTO);
      }
      dtiResRespTO.setTicketList(dtiTktList);
    }

    // Product List
    ArrayList<ProductTO> dtiProdList = dtiResRespTO.getProductList();
    ArrayList<HkdOTProductTO> otProductList = otMngResTO.getProductList();
    if ((otProductList != null) && (otProductList.size() > 0)) {

      for (HkdOTProductTO otProduct : otProductList) {
        ProductTO dtiProduct = new ProductTO();
        dtiProduct.setProdItem(otProduct.getItem());
        dtiProduct.setProdCode(otProduct.getItemAlphaCode());
        dtiProduct.setProdQty(otProduct.getQuantity());
        dtiProduct.setProdPrice(otProduct.getPrice());
        dtiProduct.setProdTax1(otProduct.getTax());
        dtiProduct.setProdDescription(otProduct.getDescription());
        dtiProdList.add(dtiProduct);
      }
      dtiResRespTO.setProductList(dtiProdList);
    }

    // Payment List
    // Note: Carryover from old system. Payment type of "Voucher" cannot be
    // returned on the response. Not supported in the XSD (RE: JTL
    // 09/15/2008)
    ArrayList<PaymentTO> dtiPmtList = dtiResRespTO.getPaymentList();
    ArrayList<HkdOTPaymentTO> otPmtList = otMngResTO.getPaymentInfoList();
    HKDBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);
    dtiResRespTO.setPaymentList(dtiPmtList);

    // Receipt
    Long entityId = new Long(dtiTxn.getEntityTO().getEntityId());
    String receiptMessage = EntityKey.getEntityReceipt(entityId);
    if (receiptMessage != null) {
      dtiResRespTO.setReceiptMessage(receiptMessage);
    }

    // Reservation
    String resCode = otMngResTO.getReservationCode();
    if (resCode != null) {
      ReservationTO dtiReservationTO = new ReservationTO();
      dtiReservationTO.setResCode(resCode);

      // Contract ID (lifted from payment section) (As of 2.15, JTL)
      for /* each */(PaymentTO aPaymentTO : /* in */dtiPmtList) {
        if (aPaymentTO.getInstallment() != null) {
          if (aPaymentTO.getInstallment().getContractId() != null) {
            String contractId = aPaymentTO.getInstallment()
                .getContractId();
            dtiReservationTO.setContractId(contractId);
            break; // Leave this loop when the condition is 1st satisfied.
          }
        }
      }

      dtiResRespTO.setReservation(dtiReservationTO);
    }

    // ClientData
    ClientDataTO dtiClientDataTO = new ClientDataTO();
    HkdOTClientDataTO otClientDataTO = otMngResTO.getClientData();
    if (otClientDataTO != null) {
      dtiClientDataTO.setClientId(otClientDataTO.getClientUniqueId()
          .toString());
      dtiResRespTO.setClientData(dtiClientDataTO);
    }

    return;
  }

  /**
   * If a type of transaction has a specific number of provider centric rules, implement them here, but if there are a very limited set of rules, mostly common to both providers, implement in the BusinessRules in the parent package.<BR>
   * Implements the following rules:<BR>
   * 
   * - n/a
   * 
   * @param dtiTxn
   *            The transaction object for this request.
   * @throws DTIException
   *             for any rules violation.
   */
  public static void applyWDWQueryReservationRules(DTITransactionTO dtiTxn) throws DTIException {

    return;

  }
  
}
