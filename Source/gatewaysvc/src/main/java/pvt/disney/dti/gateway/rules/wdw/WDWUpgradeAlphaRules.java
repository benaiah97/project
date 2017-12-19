package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import pvt.disney.dti.gateway.client.WdwIagoClient;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ArchiveKey;
import pvt.disney.dti.gateway.dao.ElectronicEntitlementKey;
import pvt.disney.dti.gateway.dao.EligibilityKey;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaResponseTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpgradeTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUpgradeTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;

/**
 * This class is responsible for three major functions for WDW upgrade alpha:<BR>
 * 1. Defining the business rules specific to WDW upgrade alpha.<BR>
 * 2. Defining the rules for transforming requests from the DTI transfer objects to the provider transfer objects.<BR>
 * 3. Defining the rules for transforming responses from the provider transfer objects to the DTI transfer objects.<BR>
 * 
 * @author lewit019
 * 
 */
public class WDWUpgradeAlphaRules {

  /** Request type header constant. */
  private final static String REQUEST_TYPE_UPGRADE = "Upgrade";

  /** Request sub-type header constant. */
  private final static String REQUEST_SUBTYPE_UPGRADETICKET = "UpgradeTicket";

  /** Constant indicating all tags should be created. */
  private final static String[] UA_TAGS = { "UpgradeTicketInfo",
      "TransactionDSSN",
      "TransactionCOD",
      "PaymentInfo" };

  /** Constant indicating the Update Ticket XSD. */
  private final static String NO_NAMESPACE_SCHEMA_LOCATION = "UpgradeTicketRequest.xsd";

  private final static String PROD_PARTS_ONE = "1";

  private final static Boolean TRUE = new Boolean("true");

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
    UpgradeAlphaRequestTO dtiUpgrdAlphaReq = (UpgradeAlphaRequestTO) dtiCmdBody;

    // === Command Level ===
    OTCommandTO atsCommand = new OTCommandTO(
        OTCommandTO.OTTransactionType.UPGRADETICKET);
    atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

    // === Header Level ===
    OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
        REQUEST_TYPE_UPGRADE, REQUEST_SUBTYPE_UPGRADETICKET);
    atsCommand.setHeaderTO(hdr);

    // === Upgrade Ticket Level ===
    OTUpgradeTicketTO otUpgrdTkt = new OTUpgradeTicketTO();

    // Tags
    ArrayList<String> tagList = new ArrayList<String>();
    for (int i = 0; i < UA_TAGS.length; i++) {
      tagList.add(UA_TAGS[i]);
    }
    otUpgrdTkt.setTagsList(tagList);

    // SiteNumber
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
        .getAttributeTOMap();
    AttributeTO anAttributeTO = aMap
        .get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
    if (anAttributeTO == null) {
      otUpgrdTkt.setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
    }
    else {
      otUpgrdTkt.setSiteNumber(Integer.parseInt(anAttributeTO
          .getAttrValue()));
    }

    HashMap<String, DBProductTO> dtiProdMap = dtiTxn.getDbProdMap();

    // UpgradeTicketInfo
    ArrayList<OTUpgradeTicketInfoTO> otUpgrdTktList = otUpgrdTkt
        .getUpgradeTicketInfoList();
    ArrayList<TicketTO> dtiTktList = dtiUpgrdAlphaReq.getTktList();
    for /* each */(TicketTO dtiTicket : /* in */dtiTktList) {

      OTUpgradeTicketInfoTO otTicketInfo = new OTUpgradeTicketInfoTO();

      // Item
      otTicketInfo.setItem(dtiTicket.getTktItem());

      // TicketSearchMode
      ArrayList<TicketIdType> dtiTicketTypeList = dtiTicket
          .getTicketTypes();
      TicketIdType dtiTicketType = dtiTicketTypeList.get(0);
      OTTicketTO otTicket = WDWBusinessRules.setOTTicketTO(dtiTicket,
          dtiTicketType);
      otTicketInfo.setTicketSearchMode(otTicket);

      // TicketType
      DBProductTO dtiProduct = dtiProdMap.get(dtiTicket.getProdCode());
      otTicketInfo.setTicketType(dtiProduct.getMappedProviderTktNbr());

      // Price (ignored)

      // Validity
      if (dtiTicket.getTktValidityValidStart() != null) otTicketInfo
          .setValidityStartDate(dtiTicket.getTktValidityValidStart());
      if (dtiTicket.getTktValidityValidEnd() != null) otTicketInfo
          .setValidityEndDate(dtiTicket.getTktValidityValidEnd());

      // PriceOverride (ignored)

      // TktNote
      if (dtiTicket.getTktNote() != null) otTicketInfo
          .setTicketNote(dtiTicket.getTktNote());
      
      //Price quote token for variably priced products
      if ((null != dtiTicket.getExtrnlPrcd()) && (dtiTicket.getExtrnlPrcd().equalsIgnoreCase("T"))
                  && (null != dtiTicket.getProdPriceQuoteToken())) {
            otTicketInfo.setProdPriceToken(dtiTicket.getProdPriceQuoteToken());
         }

      // TicketAttribute (ignored)
      // GroupQuantity (ignored)
      // BiometricLevel (ignored)
      // AccessInfo (ignored)
      // IdOnMediaTicket (ignored)
      // DemographicData (ignored)

      otUpgrdTktList.add(otTicketInfo);
    }

    // PaymentInfo
    ArrayList<OTPaymentTO> otPaymentList = otUpgrdTkt.getPaymentInfoList();
    ArrayList<PaymentTO> dtiPayList = dtiUpgrdAlphaReq.getPaymentList();
    EntityTO entityTO = dtiTxn.getEntityTO();
    WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
        entityTO);

    // Note: On Upgrade Alpha for WDW, payment list cannot be empty.
    if (otPaymentList.size() == 0) {
      throw new DTIException(
          WDWBusinessRules.class,
          DTIErrorCode.INVALID_PAYMENT_AMOUNT,
          dtiTxn.getEntityTO().getTsMac() + "/" + dtiTxn
              .getEntityTO().getTsLocation() + " did not provide any payments for upgrade alpha or did not have a valid " + "default paytype for upgrade alpha.");
    }

    // SellerId
    EntityTO entTO = dtiTxn.getEntityTO();
    if (entTO.getDefSalesRepId() != 0) otUpgrdTkt.setSellerId(BigInteger
        .valueOf(entTO.getDefSalesRepId()));

    // AssociationInfo
    if (dtiUpgrdAlphaReq.getEligibilityGroup() != null) {

      String eligGroup = dtiUpgrdAlphaReq.getEligibilityGroup();
      String eligMember = dtiUpgrdAlphaReq.getEligibilityMember();

      Integer associationId = EligibilityKey
          .getEligibilityAssocId(eligGroup);
      otUpgrdTkt.setAssociationId(associationId);

      boolean isMemberNumeric = true;
      Integer memberInteger = null;
      try {
        memberInteger = Integer.decode(eligMember);
      }
      catch (NumberFormatException nfe) {
        isMemberNumeric = false;
      }

      if ((eligGroup.compareTo(WDWBusinessRules.DVC_STRING) == 0) || (isMemberNumeric == false)) {
        otUpgrdTkt.setMemberField(eligMember);
      }
      else {
        otUpgrdTkt.setMemberId(memberInteger);
      }

    }

    // IATA
    if (entTO.getDefIata() != null) otUpgrdTkt.setIATA(entTO.getDefIata());

    // TransactionNote
    // Replace cmdInvoice with payload ID - Since 2.9
    // String cmdInvoice = dtiRequest.getCommandHeader().getCmdInvoice();
    // if (cmdInvoice != null)
    // otUpgrdTkt.setTransactionNote(cmdInvoice);
    otUpgrdTkt.setTransactionNote(dtiRequest.getPayloadHeader()
        .getPayloadID());

    // InTransaction Attribute
    ArrayList<OTInTransactionAttributeTO> inTxnAttrList = otUpgrdTkt
        .getInTxnAttrList();
    WDWBusinessRules.setOTInTransactionAttributes(dtiTxn, inTxnAttrList);

    // DenyUpgradeOnUpgrade
    otUpgrdTkt.setDenyUpgradeOnUpgrade(TRUE);

    // //Generate Event - Upgrade Alpha affects entitlements and thus should generate an event.
    otUpgrdTkt.setGenerateEvent(true);

    // Set the Query Ticket TO on the command
    atsCommand.setUpgradeTicketTO(otUpgrdTkt);

    // Get the XML String
    xmlString = OTCommandXML.getXML(atsCommand);

    return xmlString;
  }

  /**
   * Transforms a upgrade ticket response string from the WDW provider and updates the DTITransactionTO object with the response information. NOTE: As of 2.12, validates that the number of tickets coming back in match what was sent.
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

    UpgradeAlphaResponseTO dtiUpgrdTktResp = new UpgradeAlphaResponseTO();
    dtiRespTO.setCommandBody(dtiUpgrdTktResp);
    OTUpgradeTicketTO otUpgrdTktResp = otCmdTO.getUpgradeTicketTO();

    // As of 2.12, do the number of tickets returned on a success match the number sent?
    UpgradeAlphaRequestTO uaReqTO = (UpgradeAlphaRequestTO) dtiTxn
        .getRequest().getCommandBody();
    int inCount = uaReqTO.getTktList().size();
    int outCount = otUpgrdTktResp.getUpgradeTicketInfoList().size();
    if ((inCount != outCount) && (outCount > 0)) {
      throw new DTIException(
          WdwIagoClient.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Number of tickets returned  (" + outCount + ") don't match number sent (" + inCount + ").");
    }

    // Ticket
    ArrayList<OTUpgradeTicketInfoTO> otUpgrdTktInfoList = otUpgrdTktResp
        .getUpgradeTicketInfoList();

    if (otUpgrdTktInfoList.size() > 0) {

      for /* each */(OTUpgradeTicketInfoTO otUpgrdTktInfo : /* in */otUpgrdTktInfoList) {

        TicketTO dtiTkt = new TicketTO();

        // Item
        dtiTkt.setTktItem(otUpgrdTktInfo.getItem());

        // TktID.TktDSSN
        OTTicketTO otTkt = otUpgrdTktInfo.getTicket();
        dtiTkt.setDssn(otTkt.getTdssnDate(), otTkt.getTdssnSite(),
            otTkt.getTdssnStation(), otTkt.getTdssnTicketId());

        // TktID.TkTNID
        dtiTkt.setTktNID(otTkt.getTCOD());

        // TktTransaction.TranDSSN
        TicketTransactionTO dtiTktTxn = new TicketTransactionTO();
        OTTransactionDSSNTO otTxnDSSN = otUpgrdTktResp
            .getTransactionDSSN();
        String otTxnNid = otUpgrdTktResp.getTransactionCOD();
        dtiTktTxn.setDssn(otTxnDSSN.getDate(), otTxnDSSN.getSite(),
            otTxnDSSN.getStation(), otTxnDSSN.getTransactionId()
                .toString());
        dtiTktTxn.setTranNID(otTxnNid);
        dtiTkt.setTktTran(dtiTktTxn);

        // Add visual ID to response if required and available
        // As of 2.15 JTL
        if (uaReqTO.isIncludeVisualId()) {
          String visualId = ElectronicEntitlementKey.queryEntitlementVisualId(dtiTkt);
          if (visualId != null) {
            dtiTkt.setVisualId(visualId);
          }
        }

        dtiUpgrdTktResp.addTicket(dtiTkt);

      }
    }

    // Product
    ArrayList<DBProductTO> dbProdList = dtiTxn.getDbProdList();
    ArrayList<ProductTO> dtiProdList = dtiUpgrdTktResp.getProductList();
    int counter = 0;
    for /* each */(DBProductTO dbProd : /* in */dbProdList) {

      ProductTO dtiProd = new ProductTO();

      // ProdItem
      dtiProd.setProdItem(BigInteger.valueOf(++counter));

      // ProdCode
      dtiProd.setProdCode(dbProd.getPdtCode());

      // ProdQty
      dtiProd.setProdQty(dbProd.getQuantity());

      // ProdParts (defaults)
      dtiProd.setProdParts(PROD_PARTS_ONE);

      // ProdPrice
      // Per Craig Stuart, Printed Price is evaluated on inbound, but unit price
      // was being reported in the old gateway (which was inconsistent). Therefore,
      // printed price will now be reported back going forward. (9/25/2009)
      dtiProd.setProdPrice(dbProd.getPrintedPrice());

      // ProdTax1
      dtiProd.setProdTax1(dbProd.getTax1());

      // ProdReceiptMsg
      String prodReceiptMsg = ProductKey.getProductReceipt(dbProd
          .getPdtCode());
      if (prodReceiptMsg != null) {
        dtiProd.setProdReceiptMsg(prodReceiptMsg);
      }

      dtiProdList.add(dtiProd);

    } // DB Product Loop

    // Payment List
    // Note: Carryover from old system. Payment type of "Voucher" cannot be
    // returned on the response. Not supported in the XSD (RE: JTL 09/15/2008)
    ArrayList<PaymentTO> dtiPmtList = dtiUpgrdTktResp.getPaymentList();
    ArrayList<OTPaymentTO> otPmtList = otUpgrdTktResp.getPaymentInfoList();
    WDWBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);

    // Receipt.Message
    long entityId = dtiTxn.getEntityTO().getEntityId();
    String entityReceiptMsg = EntityKey.getEntityReceipt(entityId);
    if (entityReceiptMsg != null) {
      dtiUpgrdTktResp.setReceiptMessage(entityReceiptMsg);
    }

    // Always update upgrade alpha responses in reporting tables.
    // ArchiveKey.updateUpgradeAlphaResponse(dtiTxn);

    return;
  }

  /**
   * If a type of transaction has a specific number of provider centric rules, implement them here, but if there are a very limited set of rules, mostly common to both providers, implement in the BusinessRules in the parent package.<BR>
   * Implements the following rules: <BR>
   * RULE: Are WDW tickets presented in a valid format?<BR>
   * RULE: Are ticket shells provided on order active? RULE: Are ticket shells provided on order associated to the right product? <BR>
   * RULE (ACTION): Always log WDW Upgrades to reporting.<BR>
   * 
   * @param dtiTxn
   *            The transaction object for this request.
   * @throws DTIException
   *             for any rules violation or logging problem.
   */
  public static void applyWDWUpgradeAlphaRules(DTITransactionTO dtiTxn) throws DTIException {

    UpgradeAlphaRequestTO reqTO = (UpgradeAlphaRequestTO) dtiTxn
        .getRequest().getCommandBody();
    ArrayList<TicketTO> aTktListTO = reqTO.getTktList();

    // RULE: Are WDW tickets presented in a valid format?
    WDWBusinessRules.validateInBoundWDWTickets(aTktListTO);

    ArrayList<DBProductTO> dbProdList = dtiTxn.getDbProdList();

    // RULE: Are ticket shells provided on order active?
    HashSet<Integer> orderShellSet = WDWBusinessRules
        .validateTicketOrderShells(aTktListTO);
    if (orderShellSet.size() > 0) {
      ArrayList<Integer> activeShells = ProductKey
          .getActiveShells(orderShellSet);
      WDWBusinessRules.validateTicketShellActive(orderShellSet,
          activeShells);

      // RULE: Are ticket shells provided on order associated to the right
      // product?
      HashMap<String, ArrayList<Integer>> prodShellsXRef = ProductKey
          .getProductShellXref(dbProdList);
      WDWBusinessRules.validateTicketShellToProduct(aTktListTO,
          prodShellsXRef);
    }

    // RULE: Always log WDW Upgrades to reporting
    ArchiveKey.insertUpgradeAlphaRequest(dtiTxn);
    
    // validate variably priced product
    WDWExternalPriceRules.validateDeltaProducts(dtiTxn,aTktListTO);
    return;
  }

}
