package pvt.disney.dti.gateway.rules.wdw;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.VoidReservationRequestTO;
import pvt.disney.dti.gateway.data.VoidReservationResponseTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTManageReservationTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTClientDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTProductTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;

import com.disney.logging.EventLogger;

/**
 * This class is responsible for three major functions for WDW query reservation:<BR>
 * 1. Defining the business rules specific to WDW query reservation.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer objects to the DTI transfer objects.<BR>
 * 
 * @author JTL
 * @since 2.16.3
 * 
 */
public class WDWVoidReservationRules {

  /** Request type header constant. */
  private final static String REQUEST_TYPE_MANAGE = "Manage";

  /** Request sub-type header constant. */
  private final static String REQUEST_SUBTYPE_MANAGERES = "ManageReservation";

  /** Constant indicating all tags should be created. */
  private final static String ALL_TAGS = "All";

  /** Constant indicating the Manage Reservation XSD. */
  private final static String NO_NAMESPACE_SCHEMA_LOCATION = "ManageReservationRequest.xsd";

  /**
   * Constant indicating the Void variant of the Manage Reservation command.
   */
  private final static String COMMAND_TYPE = "Void";

  @SuppressWarnings("unused")
  private static final EventLogger logger = EventLogger
      .getLogger(WDWVoidReservationRules.class.getCanonicalName());

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
    VoidReservationRequestTO dtiResReq = (VoidReservationRequestTO) dtiCmdBody;

    // === Command Level ===
    OTCommandTO atsCommand = new OTCommandTO(
        OTCommandTO.OTTransactionType.MANAGERESERVATION);
    atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

    // === Header Level ===
    OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
        REQUEST_TYPE_MANAGE, REQUEST_SUBTYPE_MANAGERES);
    atsCommand.setHeaderTO(hdr);

    // === Manage Reservation Level ===
    OTManageReservationTO otManageRes = new OTManageReservationTO();

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
      otManageRes.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
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
    
    // Experimental Void to original means of payment.
    otManageRes.setVoidToOrigPayment(new Boolean("true"));

    // Generate Event - Querying a reservation shouldn't trigger an event
    otManageRes.setGenerateEvent(false);

    // Set the manage reservation TO on the command
    atsCommand.setManageReservationTO(otManageRes);

    // Get the XML String
    xmlString = OTCommandXML.getXML(atsCommand);

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
      OTCommandTO otCmdTO, DTIResponseTO dtiRespTO) throws DTIException {

    VoidReservationResponseTO dtiResRespTO = new VoidReservationResponseTO();
    OTManageReservationTO otMngResTO = otCmdTO.getManageReservationTO();
    dtiRespTO.setCommandBody(dtiResRespTO);

    // Price mismatch warning (invalid for void res - omitted)    

    // ResponseType
    dtiResRespTO.setResponseType(otMngResTO.getCommandType());

    // Ticket List
    ArrayList<TicketTO> dtiTktList = dtiResRespTO.getTicketList();
    ArrayList<OTTicketInfoTO> otTicketList = otMngResTO.getTicketInfoList();
    if ((otTicketList != null) && (otTicketList.size() > 0)) {

      for (OTTicketInfoTO otTicketInfo : otTicketList) {

        TicketTO dtiTicketTO = new TicketTO();
        OTTicketTO otTicketTO = otTicketInfo.getTicket();

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
    ArrayList<OTProductTO> otProductList = otMngResTO.getProductList();
    if ((otProductList != null) && (otProductList.size() > 0)) {

      for (OTProductTO otProduct : otProductList) {
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
    ArrayList<OTPaymentTO> otPmtList = otMngResTO.getPaymentInfoList();
    WDWBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);
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
    OTClientDataTO otClientDataTO = otMngResTO.getClientData();
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
  public static void applyWDWVoidReservationRules(DTITransactionTO dtiTxn) throws DTIException {

    return;

  }
}
