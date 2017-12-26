package pvt.disney.dti.gateway.provider.wdw.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpgradeTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicInfo;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUpgradeTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO.TicketIDType;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

/**
 * This class is responsible for transforming transfer objects into XML nodes and back again for the Omni Ticket Upgrade Ticket Command. Please note that all of its methods are either package or private level access.
 * 
 * @author lewit019
 */
public class OTUpgradeTicketXML {

  /**
   * Adds the transaction body element to the command stanza XML based upon the transfer object provided.
   * 
   * @param otUpgrdTktTO
   *            The Omni Ticket Upgrade Ticket Transfer Object.
   * @param commandStanza
   *            the command stanza XML that needs the upgrade ticket section.
   */
  static void addTxnBodyElement(OTUpgradeTicketTO otUpgrdTktTO,
      Element commandStanza) {

    // UpgradeTicket
    Element upgrdTktStanza = commandStanza.addElement("UpgradeTicket");

    // WorkRules
    ArrayList<String> tagList = otUpgrdTktTO.getTagsList();
    OTCommandXML.addWorkRules(upgrdTktStanza, tagList);

    // SiteNumber
    upgrdTktStanza.addElement("SiteNumber").addText(
        otUpgrdTktTO.getSiteNumber().toString());

    // UpgradeTicketInfo
    ArrayList<OTUpgradeTicketInfoTO> tktList = otUpgrdTktTO
        .getUpgradeTicketInfoList();
    for /* each */(OTUpgradeTicketInfoTO otTicketInfo : /* in */tktList) {

      Element upgrdTicketInfoStanza = upgrdTktStanza
          .addElement("UpgradeTicketInfo");

      // Item
      upgrdTicketInfoStanza.addElement("Item").addText(
          otTicketInfo.getItem().toString());

      // TicketSearchMode
      Element tktSrchModeStanza = upgrdTicketInfoStanza
          .addElement("TicketSearchMode");

      OTTicketTO otTicket = otTicketInfo.getTicketSearchMode();
      ArrayList<TicketIDType> otTicketIdTypeList = otTicket
          .getTktIdentityTypes();
      TicketIDType otTicketIdType = otTicketIdTypeList.get(0);

      switch (otTicketIdType) {

      case MAGCODE:
        if (otTicket.getMagTrack() != null) tktSrchModeStanza
            .addElement("MagCode").addText(otTicket.getMagTrack());
        break;

      case TDSSN:

        Element tdssnElement = tktSrchModeStanza.addElement("TDSSN");

        if (otTicket.getTdssnSite() != null) tdssnElement.addElement(
            "Site").addText(otTicket.getTdssnSite());
        if (otTicket.getTdssnStation() != null) tdssnElement
            .addElement("Station").addText(
                otTicket.getTdssnStation());
        if (otTicket.getTdssnDate() != null) {
          GregorianCalendar tdssnDateCal = otTicket.getTdssnDate();
          String tdssnDate = CustomDataTypeConverter
              .printCalToDTIDateFormat(tdssnDateCal);
          tdssnElement.addElement("Date").addText(tdssnDate);
        }
        if (otTicket.getTdssnTicketId() != null) tdssnElement
            .addElement("TicketId").setText(
                otTicket.getTdssnTicketId());
        break;

      case TCOD:
        if (otTicket.getTCOD() != null) tktSrchModeStanza.addElement(
            "TCOD").addText(otTicket.getTCOD());
        break;

      case BARCODE:
        if (otTicket.getBarCode() != null) tktSrchModeStanza
            .addElement("BarCode").addText(otTicket.getBarCode());
        break;

      case EXTERNALTICKETCODE:
        if (otTicket.getExternalTicketCode() != null) tktSrchModeStanza
            .addElement("ExternalTicketCode").addText(
                otTicket.getExternalTicketCode());
        break;

      case UNDEFINED:
        break;

      default:
        break;

      }

      // TicketType
      upgrdTicketInfoStanza.addElement("TicketType").addText(
          otTicketInfo.getTicketType().toString());
      
      // QuoteToken
      if (null != otTicketInfo.getProdPriceToken()) {
            upgrdTicketInfoStanza.addElement("QuoteToken").addText(
                     otTicketInfo.getProdPriceToken().toString());
         }
      
      // Price (ignored)

      // Validity
      if ((otTicketInfo.getValidityStartDate() != null) || (otTicketInfo
          .getValidityEndDate() != null)) {
        Element validityElement = upgrdTicketInfoStanza
            .addElement("Validity");

        if (otTicketInfo.getValidityStartDate() != null) {
          GregorianCalendar startDateCal = otTicketInfo
              .getValidityStartDate();
          String startDateString = CustomDataTypeConverter
              .printCalToDTIDateFormat(startDateCal);
          validityElement.addElement("StartDate").addText(
              startDateString);
        }

        if (otTicketInfo.getValidityEndDate() != null) {
          GregorianCalendar endDateCal = otTicketInfo
              .getValidityEndDate();
          String endDateString = CustomDataTypeConverter
              .printCalToDTIDateFormat(endDateCal);
          validityElement.addElement("EndDate")
              .addText(endDateString);
        }

      }

      // PriceOverride (ignored)

      // TktNote
      if (otTicketInfo.getTicketNote() != null) upgrdTicketInfoStanza
          .addElement("TicketNote").addText(
              otTicketInfo.getTicketNote());

      // TicketAttribute (ignored)
      // GroupQuantity (ignored)
      // BiometricLevel (ignored)
      // AccessInfo (ignored)
      // IdOnMediaTicket (ignored)

      // TICKET DemographicData (As of 2.15, JTL)
      OTDemographicInfo otDemoInfo = otTicketInfo.getDemographicInfo();
      if ((otDemoInfo != null) && (otDemoInfo.getDemoDataList().size() > 0)) {

        ArrayList<OTDemographicData> otDemoDataList = otDemoInfo
            .getDemoDataList();

        for /* each */(OTDemographicData oTDemoData : /* in */otDemoDataList) {

          Element demoDataStanza = upgrdTicketInfoStanza
              .addElement("DemographicData");

          ArrayList<OTFieldTO> oTFieldList = oTDemoData
              .getDemoDataList();

          OTCommonXML.addOTDemoAsFieldId(demoDataStanza, oTFieldList);

        } // for each Demo Data in Demo Data List (for upgrade, there should
          // only be one)

      }

    } // UpgradeTicketInfo loop

    // PaymentInfo
    if (otUpgrdTktTO.getPaymentInfoList() != null) OTCommandXML
        .addPaymentInfoStanza(upgrdTktStanza,
            otUpgrdTktTO.getPaymentInfoList());

    // SellerId
    if (otUpgrdTktTO.getSellerId() != null) upgrdTktStanza.addElement(
        "SellerId").addText(otUpgrdTktTO.getSellerId().toString());

    // AssociationInfo
    if (otUpgrdTktTO.getAssociationId() != null) {

      Element assnInfoStanza = upgrdTktStanza
          .addElement("AssociationInfo");

      assnInfoStanza.addElement("AssociationId").addText(
          otUpgrdTktTO.getAssociationId().toString());

      Element memberInfo = assnInfoStanza.addElement("MemberInfo");

      if (otUpgrdTktTO.getMemberId() != null) memberInfo.addElement(
          "MemberId").addText(otUpgrdTktTO.getMemberId().toString());
      else memberInfo.addElement("MemberField").addText(
          otUpgrdTktTO.getMemberField());
    }

    // IATA
    if (otUpgrdTktTO.getIATA() != null) upgrdTktStanza.addElement("IATA")
        .addText(otUpgrdTktTO.getIATA());

    // TransactionNote
    if (otUpgrdTktTO.getTransactionNote() != null) upgrdTktStanza
        .addElement("TransactionNote").addText(
            otUpgrdTktTO.getTransactionNote());

    // InTransaction Attribute
    ArrayList<OTInTransactionAttributeTO> inTxnAttrList = otUpgrdTktTO
        .getInTxnAttrList();

    if (inTxnAttrList != null) OTCommandXML
        .addInTransactionAttributeStanzas(upgrdTktStanza, inTxnAttrList);

    // DenyUpgradeOnUpgrade
    if (otUpgrdTktTO.getDenyUpgradeOnUpgrade() != null) upgrdTktStanza
        .addElement("DenyUpgradeOnUpgrade").addText(
            otUpgrdTktTO.getDenyUpgradeOnUpgrade().toString());

    // GenerateEvent
    if (otUpgrdTktTO.getGenerateEvent() != null) {
      upgrdTktStanza.addElement("GenerateEvent").addText(
          otUpgrdTktTO.getGenerateEvent().toString());
    }

    return;
  }

  /**
   * When provided with the parsed XML, returns the transfer object for the upgrade ticket.
   * 
   * @param upgrdTktNode
   *            the parsed XML containing the upgrade ticket.
   * @return The Omni Ticket Upgrade Ticket Transfer Object.
   * @throws DTIException
   *             for any missing required fields or other parsing errors.
   */
  @SuppressWarnings("unchecked")
  static OTUpgradeTicketTO getTO(Node upgrdTktNode) throws DTIException {

    OTUpgradeTicketTO otUpgrdTktTO = new OTUpgradeTicketTO();

    // Set error object
    OTErrorTO otErrorTO = OTCommandXML.getErrorTO(upgrdTktNode);
    otUpgrdTktTO.setError(otErrorTO);

    // UpgradeTicketInfo
    List<Node> upgradeTicketInfoNodeList = upgrdTktNode
        .selectNodes("UpgradeTicketInfo");
    if ((upgradeTicketInfoNodeList != null) && (upgradeTicketInfoNodeList
        .size() > 0)) setOTUpgradeTicketInfoTOList(
        otUpgrdTktTO.getUpgradeTicketInfoList(),
        upgradeTicketInfoNodeList);

    // PaymentInfo
    List<Node> paymentInfoNodeList = upgrdTktNode
        .selectNodes("PaymentInfo");
    if (paymentInfoNodeList != null) OTCommandXML.setOTPaymentTOList(
        otUpgrdTktTO.getPaymentInfoList(), paymentInfoNodeList);

    // TransactionDSSN
    Node tranDssnNode = upgrdTktNode.selectSingleNode("TransactionDSSN");
    if (tranDssnNode != null) otUpgrdTktTO.setTransactionDSSN(OTCommandXML
        .setOTTransactionDSSNTO(tranDssnNode));

    // TransactionCOD
    Node tranCODNode = upgrdTktNode.selectSingleNode("TransactionCOD");
    if (tranCODNode != null) otUpgrdTktTO.setTransactionCOD(tranCODNode
        .getText());

    // TotalAmount (ignored)
    // TotalTax (ignored)
    // TaxExempt (ignored)

    return otUpgrdTktTO;
  }

  /**
   * Sets the transfer object for upgrade ticket info based upon the parsed XML provided.
   * 
   * @param otTktList
   *            The list of Omni Ticket Upgrade Ticket Info Transfer Objects.
   * @param ticketInfoNodeList
   *            The parsed XML containing the upgrade ticket info sections.
   * @throws DTIException
   *             for any missing required fields or other parsing errors.
   */
  private static void setOTUpgradeTicketInfoTOList(
      ArrayList<OTUpgradeTicketInfoTO> otTktList,
      List<Node> ticketInfoNodeList) throws DTIException {

    List<Node> ticketList = ticketInfoNodeList;

    for /* each */(Node aNode : /* in */ticketList) {

      OTUpgradeTicketInfoTO otTktTO = new OTUpgradeTicketInfoTO();

      // Item
      Node itemNode = aNode.selectSingleNode("Item");
      if (itemNode == null) throw new DTIException(
          OTUpgradeTicketXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned XML without a TicketInfo,Item element.");
      else {
        String inText = itemNode.getText();
        otTktTO.setItem(new BigInteger(inText));
      }

      // ItemStatus
      Node itemStatusNode = aNode.selectSingleNode("ItemStatus");
      if (itemStatusNode == null) throw new DTIException(
          OTUpgradeTicketXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned XML without a TicketInfo,ItemStatus element.");
      else {
        String inText = itemStatusNode.getText();
        otTktTO.setItemStatus(new Integer(inText));
      }

      // TicketType (ignored)
      // RemainingValue(ignored)

      // Price
      Node priceNode = aNode.selectSingleNode("Price");
      if (priceNode != null) {
        String inText = priceNode.getText();
        otTktTO.setPrice(new BigDecimal(inText));
      }

      // VoidCode (ignored)

      // Tax
      Node taxNode = aNode.selectSingleNode("Tax");
      if (taxNode != null) {
        String inText = taxNode.getText();
        otTktTO.setTax(new BigDecimal(inText));
      }

      
      // QuoteToken
      Node quoteNode = aNode.selectSingleNode("QuoteToken");
      if (quoteNode != null) {
        otTktTO.setProdPriceToken(quoteNode.getText());
      }
      
      // Ticket
      Node ticketNode = aNode.selectSingleNode("Ticket");
      if (ticketNode != null) otTktTO.setTicket(OTCommandXML
          .setOTTicketTO(ticketNode));

      // TicketFlag (ignored)

      otTktList.add(otTktTO);
    }

    return;
  }

}
