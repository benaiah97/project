package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.UpgradeEntitlementRequestTO;
import pvt.disney.dti.gateway.data.UpgradeEntitlementResponseTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpgradeTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicInfo;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO.PaymentType;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUpgradeTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVoucherTO;
import pvt.disney.dti.gateway.provider.wdw.xml.OTCommandXML;
import pvt.disney.dti.gateway.rules.PaymentRules;
import pvt.disney.dti.gateway.rules.ProductRules;

/**
 * This class is responsible for two main functions: 1. Taking the upgrade entitlement request and transforming it into an Omni upgrade ticket request. 2. Taking the Upgrade Ticket response and transforming it into an upgrade entitlement
 * response for DTI.
 * 
 * @author HUFFM017
 * @author LEWIT019
 * @since 2.16.3
 */

public class WDWUpgradeEntitlementRules {

  /** Request type header constant. */
  private final static String REQUEST_TYPE_UPGRADE_ENT = "Upgrade";

  /** Request sub-type header constant. */
  private final static String REQUEST_SUBTYPE_UPGRADE_ENT = "UpgradeTicket";

  /** Constant indicating all tags should be created. */
  private final static String[] UT_TAGS = { "All" };

  /** Penny voucher. */
  private final static String PRICE_BRIDGE_VOUCHER = "7800";

  /** Constant indicating the Update Transaction XSD. */
  private final static String NO_NAMESPACE_SCHEMA_LOCATION = "UpdateTicketRequest.xsd";

  /*
   * ----------------------------------------------------------------------------
   */

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
    UpgradeEntitlementRequestTO dtiUpgrdEntReq = (UpgradeEntitlementRequestTO) dtiCmdBody;

    // === Command Level ===
    OTCommandTO atsCommand = new OTCommandTO(
        OTCommandTO.OTTransactionType.UPGRADETICKET);
    atsCommand.setXmlSchemaInstance(WDWBusinessRules.XML_SCHEMA_INSTANCE);
    atsCommand.setNoNamespaceSchemaLocation(NO_NAMESPACE_SCHEMA_LOCATION);

    // === Header Level ===
    OTHeaderTO hdr = WDWBusinessRules.transformOTHeader(dtiTxn,
        REQUEST_TYPE_UPGRADE_ENT, REQUEST_SUBTYPE_UPGRADE_ENT);
    atsCommand.setHeaderTO(hdr);

    // === Update Ticket Level ===
    OTUpgradeTicketTO otUpgrdTktTO = new OTUpgradeTicketTO();

    // Tags
    ArrayList<String> tagList = new ArrayList<String>();

    for (int i = 0; i < UT_TAGS.length; i++) {
      tagList.add(UT_TAGS[i]);
    }

    otUpgrdTktTO.setTagsList(tagList);

    // SiteNumber
    HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = dtiTxn
        .getAttributeTOMap();

    AttributeTO anAttributeTO = aMap
        .get(AttributeTO.CmdAttrCodeType.SITE_NUMBER);

    if (anAttributeTO == null) {
      otUpgrdTktTO
          .setSiteNumber(WDWBusinessRules.getSiteNumberProperty());
    }
    else {
      otUpgrdTktTO.setSiteNumber(Integer.parseInt(anAttributeTO
          .getAttrValue()));
    }

    HashMap<String, DBProductTO> dtiProdMap = dtiTxn.getDbProdMap();

    // === UpgradeTicketInfo ===
    ArrayList<OTUpgradeTicketInfoTO> otUpgrdTktList = otUpgrdTktTO
        .getUpgradeTicketInfoList();
    ArrayList<TicketTO> dtiTktList = dtiUpgrdEntReq.getTicketList();

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
      if ((dtiProdMap != null) && (dtiTicket.getProdCode() != null)) {
        DBProductTO dtiProduct = dtiProdMap
            .get(dtiTicket.getProdCode());
        otTicketInfo
            .setTicketType(dtiProduct.getMappedProviderTktNbr());
      }

      // Price
      if (dtiTicket.getProdPrice() != null) otTicketInfo
          .setPrice(dtiTicket.getProdPrice());

      // Validity
      if (dtiTicket.getTktValidityValidStart() != null) otTicketInfo
          .setValidityStartDate(dtiTicket.getTktValidityValidStart());
      if (dtiTicket.getTktValidityValidEnd() != null) otTicketInfo
          .setValidityEndDate(dtiTicket.getTktValidityValidEnd());

      // TktNote
      if (dtiTicket.getTktNote() != null) otTicketInfo
          .setTicketNote(dtiTicket.getTktNote());

      // TicketAttribute (ignored)
      // GroupQuantity (ignored)
      // BiometricLevel (ignored)
      // AccessInfo (ignored)
      // IdOnMediaTicket (ignored)

      // DemographicData (as of 2.15, JTL)
      ArrayList<DemographicsTO> tktDemoList = dtiTicket
          .getTicketDemoList();
      if (tktDemoList.size() > 0) {
        OTDemographicInfo otDemoInfo = new OTDemographicInfo();
        WDWBusinessRules.transformTicketDemoData(tktDemoList,
            otDemoInfo);
        otTicketInfo.setDemographicInfo(otDemoInfo);
      }

      otUpgrdTktList.add(otTicketInfo);

    }

    // === Payment Info ===
    ArrayList<OTPaymentTO> otPaymentList = otUpgrdTktTO
        .getPaymentInfoList();
    ArrayList<PaymentTO> dtiPayList = dtiUpgrdEntReq.getPaymentList();
    EntityTO entityTO = dtiTxn.getEntityTO();

    WDWBusinessRules.createOTPaymentList(otPaymentList, dtiPayList,
        entityTO);

    // Note: On Upgrade Entitlement for WDW, payment list cannot be empty, default to a zero cost voucher.
    // Add the penny voucher if it's not already there.
    int payListSize = otPaymentList.size();
    boolean priceBridgeVoucherPresent = false;
    if (payListSize > 0) {
      for /* each */(OTPaymentTO aPaymentTO : /* in */otPaymentList) {
        if (aPaymentTO.getPayType() == PaymentType.VOUCHER) {
          OTVoucherTO otVoucherTO = aPaymentTO.getVoucher();
          if (otVoucherTO.getMasterCode().compareTo(
              PRICE_BRIDGE_VOUCHER) == 0) {
            priceBridgeVoucherPresent = true;
          }
        }
      }
    }

    // This voucher is now always added for bridging as of 2.12
    if (!priceBridgeVoucherPresent) {
      int itemNumber = payListSize + 1;
      OTPaymentTO otPaymentTO = new OTPaymentTO();
      otPaymentTO.setPayItem(BigInteger.valueOf(itemNumber));
      otPaymentTO.setPayType(PaymentType.VOUCHER);
      OTVoucherTO otVoucherTO = new OTVoucherTO();
      otVoucherTO.setMasterCode(PRICE_BRIDGE_VOUCHER);
      otPaymentTO.setVoucher(otVoucherTO);
      otPaymentList.add(otPaymentTO);
    }

    // Transaction note (as of 2.10 - Payload ID)
    // As of 2.12, override with Audit Notification if available.
    if (dtiUpgrdEntReq.getAuditNotation() == null) {
      otUpgrdTktTO.setTransactionNote(dtiRequest.getPayloadHeader()
          .getPayloadID());
    }
    else {
      otUpgrdTktTO.setTransactionNote(dtiUpgrdEntReq.getAuditNotation());
    }

    // Upgrade event always set to true. DTI 2.15.1, JTL
    otUpgrdTktTO.setGenerateEvent(true);

    // Set the upgrade ticket TO on the command
    atsCommand.setUpgradeTicketTO(otUpgrdTktTO);

    // Get the XML String
    xmlString = OTCommandXML.getXML(atsCommand);

    return xmlString;
  }

  /**
   * Transforms a update ticket response string from the WDW provider and updates the DTITransactionTO object with the response information. In these case, there are no fields which need to be transformed.
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

    UpgradeEntitlementResponseTO dtiUpgrdEntResp = new UpgradeEntitlementResponseTO();

    dtiRespTO.setCommandBody(dtiUpgrdEntResp);

    OTUpgradeTicketTO otUpgrdTktResp = otCmdTO.getUpgradeTicketTO();

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

        dtiUpgrdEntResp.addTicket(dtiTkt);

      }
    }

    // Payment List
    ArrayList<PaymentTO> dtiPmtList = dtiUpgrdEntResp.getPaymentList();
    ArrayList<OTPaymentTO> otPmtList = otUpgrdTktResp.getPaymentInfoList();
    WDWBusinessRules.setDTIPaymentList(dtiPmtList, otPmtList);

    return;

  }

  /**
   * These rules ensure that the upgrade being attempted is legal.
   * 
   * @param dtiTxn
   * @throws DTIException
   */
   public static void applyWDWUpgradeEntitlementRules(DTITransactionTO dtiTxn) throws DTIException {

      UpgradeEntitlementRequestTO upgrdEntReqTO = (UpgradeEntitlementRequestTO) dtiTxn.getRequest().getCommandBody();

      ArrayList<TicketTO> tktListTO = upgrdEntReqTO.getTicketList();

      for /* each */(TicketTO aTicketTO : /* in */tktListTO) {
         
         // Validate that WDW is not attempting to use DLR PLU specific fields.
         if (aTicketTO.getDlrPLU() != null) {
            throw new DTIException(WDWUpgradeEntitlementRules.class, DTIErrorCode.INVALID_PRODUCT_UPGRADE_PATH, 
                     "From product for a WDW entitlement upgrade cannot be a DLR PLU.  It must be a product code.");
         }

         // validate the TO price is greater than or equal to the FROM price
         if (aTicketTO.getProdPrice() != null) {

            BigDecimal fromProdPrice = aTicketTO.getFromPrice();
            BigDecimal toProdPrice = aTicketTO.getProdPrice();
            BigDecimal upgrdPrice = aTicketTO.getUpgrdPrice();

            String toProdCode = aTicketTO.getProdCode();
            String fromProdCode = aTicketTO.getFromProdCode();

            // RULE: Apply upgrade entitlement business rule, according to price
            ProductRules.validateUpgradePricing(fromProdCode, fromProdPrice, toProdCode, toProdPrice, upgrdPrice);

         }
      }

      // Validate that if other ticket demographics have been provided, phone
      // has been provided, as well.
      // As of 2.16.1 APMP JTL
      ProductRules.validateWdwTicketDemo(tktListTO);

      // RULE: Validate that if the "installment" type of payment is present,
      // Commenting out these lines to temporarily resolve INC4506020 -
      // ClassCastException on Upgrade from HERA
      ArrayList<TPLookupTO> tpLookups = dtiTxn.getTpLookupTOList();
      PaymentRules.validateUpgrdEntInstallDownpayment(dtiTxn, tpLookups);

   }

}
