package pvt.disney.dti.gateway.rules.wdw;

import java.util.ArrayList;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ArchiveKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.VoidTicketResponseTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTVoidTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVTicketTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;
import pvt.disney.dti.gateway.rules.PaymentRules;

/**
 * This class is responsible for three major functions for WDW void ticket:<BR>
 * 1. Defining the business rules specific to WDW void ticket.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer objects to the DTI transfer objects.<BR>
 * 
 * @author lewit019
 */
public class WDWVoidTicketRules {

  /** Request type header constant. */
  private final static String REQUEST_TYPE_VOID = "Void";

  /** Request subtype header constant. */
  private final static String REQUEST_SUBTYPE_VOIDTICKET = "VoidTicket";

  /** Constant indicating all tags should be created. */
  private final static String[] VOID_TAGS = { "TicketInfoVoid",
      "TransactionDSSN",
      "TransactionCOD",
      "PaymentInfo" };

  /** Constant indicating the Void Ticket XSD. */
  private final static String NO_NAMESPACE_SCHEMA_LOCATION = "VoidTicketRequest.xsd";

  /**
   * Transform the DTITransactionTO value object to the provider value objects and then pass those to XML Marshalling routines to create an XML string.
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
    VoidTicketRequestTO dtiVoidTkt = (VoidTicketRequestTO) dtiCmdBody;

    // === Command Level ===
    OTCommandTO atsCommand = new OTCommandTO(
        OTCommandTO.OTTransactionType.VOIDTICKET);
    atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

    // === Header Level ===
    OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
        REQUEST_TYPE_VOID, REQUEST_SUBTYPE_VOIDTICKET);
    atsCommand.setHeaderTO(hdr);

    // === Void Ticket Level ===
    OTVoidTicketTO otVoidTkt = new OTVoidTicketTO();

    // Tags
    ArrayList<String> tagList = new ArrayList<String>();
    for (int i = 0; i < VOID_TAGS.length; i++) {
      tagList.add(VOID_TAGS[i]);
    }
    otVoidTkt.setTagsList(tagList);

    // SiteNumber
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
        .getAttributeTOMap();
    AttributeTO anAttributeTO = aMap
        .get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
    if (anAttributeTO == null) {
      otVoidTkt.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
    }
    else {
      otVoidTkt.setSiteNumber(Integer.parseInt(anAttributeTO
          .getAttrValue()));
    }

    // VTicket
    // Ticket Information
    ArrayList<TicketTO> dtiTicketList = dtiVoidTkt.getTktList();
    TicketTO dtiTicket = dtiTicketList.get(0);

    // Item
    OTVTicketTO otVTicket = new OTVTicketTO();
    otVTicket.setItem(dtiTicket.getTktItem());

    // TicketSearchMode
    ArrayList<TicketIdType> dtiTicketTypeList = dtiTicket.getTicketTypes();
    TicketIdType dtiTicketType = dtiTicketTypeList.get(0);
    OTTicketTO otTicket = WDWBusinessRules.setOTTicketTO(dtiTicket,
        dtiTicketType);
    otVTicket.setTicketSearchMode(otTicket);

    // TicketNote (ignored)
    // VoidCode (ignored)
    // DeleteDemographic

    ArrayList<OTVTicketTO> tktList = otVoidTkt.getVTicketList();
    tktList.add(otVTicket);

    // PaymentInfo
    // Payment Processing
    // There are three alternatives: default payment, no payment, and payment.
    ArrayList<OTPaymentTO> otPaymentList = otVoidTkt.getPaymentInfoList();
    ArrayList<PaymentTO> dtiPayList = dtiVoidTkt.getPaymentList();
    EntityTO entityTO = dtiTxn.getEntityTO();
    WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
        entityTO);

    // TransactionNote (as of 2.10)
    otVoidTkt.setTransactionNote(dtiTxn.getRequest().getPayloadHeader()
        .getPayloadID());
    // String cmdInvoice = dtiRequest.getCommandHeader().getCmdInvoice();
    // if (cmdInvoice != null)
    // otVoidTkt.setTransactionNote(cmdInvoice);

    // AssociationInfo (omitted)
    /*
     * Note, while code was in the old gateway to populate VoidTicket.AssociationInfo.AssociationId and MemberId, it was being done from environment variables that, on first examination, where populated by a product SQL query. Upon
     * detailed examination of the code, no product query associated with void ticket could be located, so it is presumed that although the code was present in the old gateway, it was never executed. Hence, it has been removed here. JTL
     */

    // InTransactionAttributes
    ArrayList<OTInTransactionAttributeTO> inTxnAttrList = otVoidTkt
        .getInTxnAttrList();
    WDWBusinessRules.setOTInTransactionAttributes(dtiTxn, inTxnAttrList);

    // Generate Event - Voids affect entitlements and thus should generate an event.
    otVoidTkt.setGenerateEvent(true);

    // Set the void ticket TO on the command
    atsCommand.setVoidTicketTO(otVoidTkt);

    // Get the XML String
    xmlString = OTCommandXML.getXML(atsCommand);

    return xmlString;
  }

  /**
   * Transforms a void ticket response string from the WDW provider and updates the DTITransactionTO object with the response information.
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

    OTVTicketTO otVTkt = null;
    VoidTicketResponseTO dtiVoidTktResp = new VoidTicketResponseTO();
    dtiRespTO.setCommandBody(dtiVoidTktResp);

    // Is there any ticket on the response (position zero presumed).
    OTVoidTicketTO otVoidTktResp = otCmdTO.getVoidTicketTO();
    if ((otVoidTktResp.getVTicketList() != null) && (otVoidTktResp
        .getVTicketList().size() > 0)) {
      ArrayList<OTVTicketTO> otVTktList = otVoidTktResp.getVTicketList();
      if ((otVTktList != null) && (otVTktList.size() > 0)) {
        otVTkt = otVTktList.get(0);
      }
    }

    if (otVTkt == null) {
      return;
    }

    TicketTO dtiTkt = new TicketTO();
    dtiTkt.setTktItem(otVTkt.getItem());

    // Provider Ticket Type
    dtiTkt.setProviderTicketType(otVTkt.getProviderTicketType());

    // Ticket Price
    dtiTkt.setTktPrice(otVTkt.getPrice());

    // TktId.DSSN
    OTTicketTO otTkt = otVTkt.getTicket();
    if (otTkt.getTdssnDate() != null) {
      dtiTkt.setDssn(otTkt.getTdssnDate(), otTkt.getTdssnSite(),
          otTkt.getTdssnStation(), otTkt.getTdssnTicketId());
    }

    // TktId.TktNID
    if (otTkt.getTCOD() != null) {
      dtiTkt.setTktNID(otTkt.getTCOD());
    }

    // TktTransaction.TranDSSN
    if (otVoidTktResp.getTransactionDSSN() != null) {
      TicketTransactionTO dtiTktTxn = new TicketTransactionTO();
      OTTransactionDSSNTO otTxnDSSN = otVoidTktResp.getTransactionDSSN();
      String otTxnNid = otVoidTktResp.getTransactionCOD();
      dtiTktTxn.setDssn(otTxnDSSN.getDate(), otTxnDSSN.getSite(),
          otTxnDSSN.getStation(), otTxnDSSN.getTransactionId()
              .toString());
      dtiTktTxn.setTranNID(otTxnNid);
      dtiTkt.setTktTran(dtiTktTxn);
    }

    dtiVoidTktResp.addTicket(dtiTkt);

    // Always update upgrade alpha responses in reporting tables.
    // ArchiveKey.updateVoidTicketResponse(dtiTxn);

    return;
  }

  /**
   * If a type of transaction has a specific number of provider centric rules, implement them here, but if there are a very limited set of rules, mostly common to both providers, implement in the BusinessRules in the parent package.<BR>
   * Implements the following rules:<BR>
   * RULE: Validate that ticket is of valid format.<BR>
   * RULE (ACTION): Always log WDW Voids to reporting.
   * 
   * @param dtiTxn
   *            The transaction object for this request.
   * @throws DTIException
   *             for any rules violation or logging problem.
   */
  public static void applyWDWVoidTicketRules(DTITransactionTO dtiTxn) throws DTIException {

    VoidTicketRequestTO reqTO = (VoidTicketRequestTO) dtiTxn.getRequest()
        .getCommandBody();
    ArrayList<TicketTO> aTktList = reqTO.getTktList();

    // Validate that ticket presented is of valid format.
    WDWBusinessRules.validateInBoundWDWTickets(aTktList);

    // Validate that wdw void has at least one form of payment (real or
    // default).
    EntityTO entityTO = dtiTxn.getEntityTO();
    ArrayList<PaymentTO> payListTO = reqTO.getPaymentList();
    PaymentRules.validateReturnFormOfPayment(payListTO, entityTO);

    // RULE: Always log WDW Voids to reporting
    ArchiveKey.insertVoidTicketRequest(dtiTxn);

    return;
  }

}
