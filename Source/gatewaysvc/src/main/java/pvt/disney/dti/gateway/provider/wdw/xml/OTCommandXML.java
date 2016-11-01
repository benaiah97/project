package pvt.disney.dti.gateway.provider.wdw.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.tree.FlyweightAttribute;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCreateTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTManageReservationTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTMultiEntitlementAccountTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTRenewTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpdateTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpdateTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpgradeTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTVoidTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTCreditCardTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTCreditCardTO.CreditCardEntryType;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInstallmentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVoucherTO;
import pvt.disney.dti.gateway.rules.TransformConstants;

/**
 * This class is responsible for marshalling Omni Ticket transfer objects into XML and unmarshalling XML response strings back into transfer objects. The class contains the only two public methods: getTO (for get transfer object) and
 * getXML. All marshalling and unmarshalling are accomplished through these two methods.
 * 
 * @author lewit019
 */
public class OTCommandXML implements TransformConstants {

  /** No namespace schema location value required on Omni Ticket XML. */
  private final static String XSINONSSCHLOC = "xsi:noNamespaceSchemaLocation";

  /**
   * Constant value of a generic error message for partially successful transactions.
   */
  public final static int GENERIC_OPER_PARTIALLY_SUCCESSFUL = 1;

  /** Constant value of a generic error message for failed transactions. */
  public final static int GENERIC_OPER_FAILED = 2;

  /**
   * When provided a complete transfer object, renders the XML for a request to the provider system.
   * 
   * @param otCommandTO
   *            The Omni Ticket Command Transfer Object.
   * @return the XML string version of the request.
   * @throws DTIException
   *             for any parsing errors or missing fields.
   */
  public static String getXML(OTCommandTO otCommandTO) throws DTIException {

    String xmlString = null;

    Document document = DocumentHelper.createDocument();
    Element commandStanza = document.addElement("Command");
    ArrayList<Attribute> attribList = new ArrayList<Attribute>();

    // Set xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    Attribute xmlns = new FlyweightAttribute("xmlns:xsi",
        "http://www.w3.org/2001/XMLSchema-instance");
    attribList.add(xmlns);

    OTCommandTO.OTTransactionType txnType = otCommandTO.getTxnType();

    OTHeaderTO otHeader = otCommandTO.getHeaderTO();
    OTHeaderXML.addHeaderElement(otHeader, commandStanza);

    // Set xsi:noNamespaceSchemaLocation="ManageReservationRequest.xsd" or
    // "Upgrade..."
    Attribute xsi = null;

    switch (txnType) {

    case MANAGERESERVATION:
      xsi = new FlyweightAttribute(XSINONSSCHLOC,
          "ManageReservationRequest.xsd");
      OTManageReservationTO otMngResTO = otCommandTO
          .getManageReservationTO();
      OTManageReservationXML.addTxnBodyElement(otMngResTO, commandStanza);
      break;

    case QUERYTICKET:
      xsi = new FlyweightAttribute(XSINONSSCHLOC,
          "QueryTicketRequest.xsd");
      OTQueryTicketTO otQryTktTO = otCommandTO.getQueryTicketTO();
      OTQueryTicketXML.addTxnBodyElement(otQryTktTO, commandStanza);
      break;

    case CREATETRANSACTION:
      xsi = new FlyweightAttribute(XSINONSSCHLOC,
          "CreateTransactionRequest.xsd");
      OTCreateTransactionTO otCrtTxnTO = otCommandTO
          .getCreateTransactionTO();
      OTCreateTransactionXML.addTxnBodyElement(otCrtTxnTO, commandStanza);
      break;

    case VOIDTICKET:
      xsi = new FlyweightAttribute(XSINONSSCHLOC, "VoidTicketRequest.xsd");
      OTVoidTicketTO otVoidTktTO = otCommandTO.getVoidTicketTO();
      OTVoidTicketXML.addTxnBodyElement(otVoidTktTO, commandStanza);
      break;

    case UPGRADETICKET:
      xsi = new FlyweightAttribute(XSINONSSCHLOC,
          "UpgradeTicketRequest.xsd");
      OTUpgradeTicketTO otUpgrdTktTO = otCommandTO.getUpgradeTicketTO();
      OTUpgradeTicketXML.addTxnBodyElement(otUpgrdTktTO, commandStanza);
      break;

    case UPDATETICKET:
      xsi = new FlyweightAttribute(XSINONSSCHLOC,
          "UpdateTicketRequest.xsd");
      OTUpdateTicketTO otUpdtTktTO = otCommandTO.getUpdateTicketTO();
      OTUpdateTicketXML.addTxnBodyElement(otUpdtTktTO, commandStanza);
      break;

    case UPDATETRANSACTION:
      xsi = new FlyweightAttribute(XSINONSSCHLOC,
          "UpdateTransactionRequest.xsd");
      OTUpdateTransactionTO otUpdtTxnTO = otCommandTO
          .getUpdateTransactionTO();
      OTUpdateTransactionXML
          .addTxnBodyElement(otUpdtTxnTO, commandStanza);
      break;

    case MULTIENTITLEMENTACCOUNT:
      xsi = new FlyweightAttribute(XSINONSSCHLOC,
          "MultiEntitlementAccountRequest.xsd");
      OTMultiEntitlementAccountTO otMultiEntitlementAccountTxnTO = otCommandTO
          .getMultiEntitlementAccountTO();
      OTMultiEntitlementAccountXML.addTxnBodyElement(
          otMultiEntitlementAccountTxnTO, commandStanza);
      break;

    case RENEWTICKET: // as of 2.16.1, JTL
      xsi = new FlyweightAttribute(XSINONSSCHLOC,
          "RenewTicketRequest.xsd");
      OTRenewTicketTO otRenewTktTO = otCommandTO.getRenewTicketTO();
      OTRenewTicketXML.addTxnBodyElement(otRenewTktTO, commandStanza);
      break;

    default:
      throw new DTIException(OTCommandXML.class,
          DTIErrorCode.INVALID_COMMAND,
          "Internal Error:  Unknown enumeration on OTCommandTO class.");

    }

    attribList.add(xsi);
    commandStanza.setAttributes(attribList);

    xmlString = document.asXML();

    return xmlString;
  }

  /**
   * When passed an XML response from the provider, parses it into a transfer object.
   * 
   * @param xmlResponse
   *            the string version of the XML response.
   * @return The Omni Ticket Command Transfer Object.
   * @throws DTIException
   *             for any parsing errors that are discovered.
   */
  public static OTCommandTO getTO(String xmlResponse) throws DTIException {

    Document document = null;
    OTCommandTO otCmdTO = new OTCommandTO(
        OTCommandTO.OTTransactionType.UNDEFINED);
    OTHeaderTO otHdrTO = null;
    boolean bodyElementFound = false;

    try {
      document = DocumentHelper.parseText(xmlResponse);
    }
    catch (DocumentException de) {
      throw new DTIException(OTCommandXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Unable to parse XML response from provider: " + de
              .toString());
    }

    // Command
    Node commandStanza = document.getRootElement();

    if (commandStanza.getName().compareTo("Command") != 0) throw new DTIException(
        OTCommandXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned XML without a Command element.");

    // Header
    Node headerNode = commandStanza.selectSingleNode("Header");

    if (headerNode == null) {
      throw new DTIException(OTCommandXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned XML without a Header element.");
    }
    else {
      otHdrTO = OTHeaderXML.getTO(headerNode);
      otCmdTO.setHeaderTO(otHdrTO);

      if (otHdrTO.getRequestSubType().compareTo(OT_MANAGE_RES_TYPE) == 0) {
        otCmdTO.setTxnType(OTCommandTO.OTTransactionType.MANAGERESERVATION);
      }
      else if (otHdrTO.getRequestSubType().compareTo(OT_UPGRADE_TICKET) == 0) {
        otCmdTO.setTxnType(OTCommandTO.OTTransactionType.UPGRADETICKET);
      }
      else if (otHdrTO.getRequestSubType().compareTo(OT_VOID_TICKET) == 0) {
        otCmdTO.setTxnType(OTCommandTO.OTTransactionType.VOIDTICKET);
      }
      else if (otHdrTO.getRequestSubType().compareTo(OT_QUERY_TICKET) == 0) {
        otCmdTO.setTxnType(OTCommandTO.OTTransactionType.QUERYTICKET);
      }
      else if (otHdrTO.getRequestSubType().compareTo(OT_UPDATE_TICKET) == 0) {
        otCmdTO.setTxnType(OTCommandTO.OTTransactionType.UPDATETICKET);
      }
      else if (otHdrTO.getRequestSubType().compareTo(
          OT_UPDATE_TRANSACTION) == 0) {
        otCmdTO.setTxnType(OTCommandTO.OTTransactionType.UPDATETRANSACTION);
      }
      else if (otHdrTO.getRequestSubType().compareTo(
          OT_CREATE_TRANSACTION) == 0) {
        otCmdTO.setTxnType(OTCommandTO.OTTransactionType.CREATETRANSACTION);
      }
      else if (otHdrTO.getRequestSubType().compareTo(OT_RENEW_TICKET) == 0) {
        otCmdTO.setTxnType(OTCommandTO.OTTransactionType.RENEWTICKET);
      }
      else if (otHdrTO.getRequestSubType().compareTo(
          OT_MULTI_ENTITLEMENT_ACCOUNT) == 0) {
        otCmdTO.setTxnType(OTCommandTO.OTTransactionType.MULTIENTITLEMENTACCOUNT);
      }
      else {
        StringBuffer errorBuffer = new StringBuffer(
            "Ticket provider returned unknown Header,Message type: ");
        if (otHdrTO.getRequestSubType() == null) errorBuffer
            .append("null");
        else errorBuffer.append(otHdrTO.getRequestSubType());
        throw new DTIException(OTCommandXML.class,
            DTIErrorCode.TP_INTERFACE_FAILURE,
            errorBuffer.toString());
      }

      // ==== Body Sections ====
      // ManageReservation
      Node manageResNode = commandStanza
          .selectSingleNode("ManageReservation");
      if (manageResNode != null) {
        OTManageReservationTO otMngRes = OTManageReservationXML
            .getTO(manageResNode);
        otCmdTO.setManageReservationTO(otMngRes);
        bodyElementFound = true;
      }

      // UpgradeTicket
      Node upgradeTicketNode = commandStanza
          .selectSingleNode("UpgradeTicket");
      if (upgradeTicketNode != null) {
        OTUpgradeTicketTO otUpgrdTktRes = OTUpgradeTicketXML
            .getTO(upgradeTicketNode);
        otCmdTO.setUpgradeTicketTO(otUpgrdTktRes);
        bodyElementFound = true;
      }

      // VoidTicket
      Node voidTicketNode = commandStanza.selectSingleNode("VoidTicket");
      if (voidTicketNode != null) {
        OTVoidTicketTO otVoidTktRes = OTVoidTicketXML
            .getTO(voidTicketNode);
        otCmdTO.setVoidTicketTO(otVoidTktRes);
        bodyElementFound = true;
      }

      // QueryTicket
      Node queryTicketNode = commandStanza
          .selectSingleNode("QueryTicket");
      if (queryTicketNode != null) {
        OTQueryTicketTO otQueryTktRes = OTQueryTicketXML
            .getTO(queryTicketNode);
        otCmdTO.setQueryTicketTO(otQueryTktRes);
        bodyElementFound = true;
      }

      // UpdateTicket
      Node updateTicketNode = commandStanza
          .selectSingleNode("UpdateTicket");
      if (updateTicketNode != null) {
        OTUpdateTicketTO otUpdtTktRes = OTUpdateTicketXML
            .getTO(updateTicketNode);
        otCmdTO.setUpdateTicketTO(otUpdtTktRes);
        bodyElementFound = true;
      }

      // UpdateTransaction
      Node updateTransactionNode = commandStanza
          .selectSingleNode("UpdateTransaction");
      if (updateTransactionNode != null) {
        OTUpdateTransactionTO otUpdtTxnRes = OTUpdateTransactionXML
            .getTO(updateTransactionNode);
        otCmdTO.setUpdateTransactionTO(otUpdtTxnRes);
        bodyElementFound = true;
      }

      // CreateTransaction
      Node createTransactionNode = commandStanza
          .selectSingleNode("CreateTransaction");
      if (createTransactionNode != null) {
        OTCreateTransactionTO otCrtTxnRes = OTCreateTransactionXML
            .getTO(createTransactionNode);
        otCmdTO.setCreateTransactionTO(otCrtTxnRes);
        bodyElementFound = true;
      }

      // MultiEntitlementAccount (encompasses all NGE signatures)
      Node multiEntitlementAccountNode = commandStanza
          .selectSingleNode("MultiEntitlementAccount");
      if (multiEntitlementAccountNode != null) {
        OTMultiEntitlementAccountTO otMultiEntitlementAccountRes = OTMultiEntitlementAccountXML
            .getTO(multiEntitlementAccountNode);
        otCmdTO.setMultiEntitlementAccountTO(otMultiEntitlementAccountRes);
        bodyElementFound = true;
      }

      // RenewTicket (as of 2.16.1, JTL)
      Node renewTicketNode = commandStanza
          .selectSingleNode("RenewTicket");
      if (renewTicketNode != null) {
        OTRenewTicketTO otRenewTktRes = OTRenewTicketXML
            .getTO(renewTicketNode);
        otCmdTO.setRenewTicketTO(otRenewTktRes);
        bodyElementFound = true;
      }

      // if no body found, error out.
      if (!bodyElementFound) throw new DTIException(OTCommandXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned XML without a Body element.");

    }

    return otCmdTO;
  }

  /**
   * Adds the payment information stanzas to the XML document based on the transfer object provided. Package level access is intentional on this method (absence of public, private, protected). Note, that by the time this code is called,
   * the request should have already been edited for missing fields.
   * 
   * @param txnStanza
   *            the transaction stanza which should be populated with the payment info.
   * @param payList
   *            A list of Omni Ticket Payment Transfer Objects.
   */
  static void addPaymentInfoStanza(Element txnStanza,
      ArrayList<OTPaymentTO> payList) {

    if ((payList == null) || (payList.size() == 0)) return;

    for /* each */(OTPaymentTO aPaymentTO : /* in */payList) {

      Element payStanza = txnStanza.addElement("PaymentInfo");

      // Item
      if (aPaymentTO.getPayItem() != null) {
        payStanza.addElement("Item").addText(
            aPaymentTO.getPayItem().toString());
      }

      // PaymentType
      payStanza.addElement("PaymentType").addText(
          aPaymentTO.getPayTypeInt().toString());

      // PaymentAmount
      if (aPaymentTO.getPayAmount() != null) payStanza.addElement(
          "PaymentAmount").addText(
          aPaymentTO.getPayAmount().toString());

      // CreditCard
      if (aPaymentTO.getCreditCard() != null) {
        addCreditCardStanza(payStanza, aPaymentTO.getCreditCard());
      }

      // Voucher
      if (aPaymentTO.getVoucher() != null) {
        addVoucherStanza(payStanza, aPaymentTO.getVoucher());
      }

      // Installment (as of 2.15, JTL)
      if (aPaymentTO.getInstallment() != null) {
        addInstallmentStanza(payStanza, aPaymentTO.getInstallment());
      }

      // ForeignCurrency (omitted)
      // Invoice (omitted)
      // Convention (omitted)

    }

    return;
  }

  /**
   * Adds the credit card stanza to the payment stanza XML based on the transfer object provided. Package level access is intentional on this method (absence of public, private, protected).
   * 
   * @param payStanza
   *            XML stanza that needs the credit card information.
   * @param ccardTO
   *            The Omni Ticket Credit Card Transfer Object.
   */
  static void addCreditCardStanza(Element payStanza, OTCreditCardTO ccardTO) {

    Element ccardStanza = payStanza.addElement("CreditCard");

    Element ccInfoStanza = ccardStanza.addElement("CardInfo");

    if (ccardTO.getCcManualOrSwiped() == OTCreditCardTO.CreditCardEntryType.SWIPED) {
      Element ccSwipedStanza = ccInfoStanza.addElement("Swiped");

      // Track1 (optional)
      if (ccardTO.getTrack1() != null) ccSwipedStanza
          .addElement("Track1").addText(ccardTO.getTrack1());

      // Track2 (required)
      ccSwipedStanza.addElement("Track2").addText(ccardTO.getTrack2());

      // PosTerminalId (optional - as of 2.12)
      if (ccardTO.getPosTerminalId() != null) ccSwipedStanza.addElement(
          "PosTerminalId").addText(ccardTO.getPosTerminalId());

      // External Device Serial Id (optional - as of 2.12)
      if (ccardTO.getExternalDeviceSerialId() != null) ccSwipedStanza
          .addElement("ExternalDeviceSerialId").addText(
              ccardTO.getExternalDeviceSerialId());

    }
    else {
      Element ccManualStanza = ccInfoStanza.addElement("Manual");

      // CardNumber
      ccManualStanza.addElement("CardNumber").addText(
          ccardTO.getCardNumber());
      
      // SHA-1
      if (ccardTO.getSha1() != null) {
         ccManualStanza.addElement("SHA-1").addText(ccardTO.getSha1());
      }
      
      // SubCode
      if (ccardTO.getSubCode() != null) {
        ccManualStanza.addElement("SubCode").addText(ccardTO.getSubCode());
      }

      // CardExpDate
      if (ccardTO.getCardExpDate() != null) ccManualStanza.addElement(
          "CardExpDate").addText(ccardTO.getCardExpDate());

      // CVV
      if (ccardTO.getCVV() != null) ccManualStanza.addElement("CVV")
          .addText(ccardTO.getCVV());

      // AVS
      if ((ccardTO.getAvs_AvsStreet() != null) || (ccardTO
          .getAvs_AvsZipCode() != null)) {
        Element avsStanza = ccManualStanza.addElement("AVS");

        if (ccardTO.getAvs_AvsZipCode() != null) avsStanza.addElement(
            "AVSZipCode").addText(ccardTO.getAvs_AvsZipCode());

        if (ccardTO.getAvs_AvsStreet() != null) avsStanza.addElement(
            "AVSStreet").addText(ccardTO.getAvs_AvsStreet());
      }

      // IssueCode (omitted)
      // CardStartDate (omitted)

      // CAVVFormat
      if (ccardTO.getCAVVFormat() != null) ccManualStanza.addElement(
          "CAVVFormat").addText(ccardTO.getCAVVFormat());

      // CAVVValue
      if (ccardTO.getCAVVValue() != null) ccManualStanza.addElement(
          "CAVVValue").addText(ccardTO.getCAVVValue());

      // GiftCardStartDate (omitted)
      if (ccardTO.getECommerceValue() != null) ccManualStanza.addElement(
          "ECommerceValue").addText(ccardTO.getECommerceValue());

    }

    // CardType
    ccardStanza.addElement("CardType").addText(
        ccardTO.getCardTypeInteger().toString());

    // PreApproved 
    if (ccardTO.getIsPreApproved() != null) {
       ccardStanza.addElement("PreApproved").addText(ccardTO.getIsPreApproved().toString());
    }
    
    // AuthCode
    if (ccardTO.getAuthCode() != null) {
      ccardStanza.addElement("AuthCode").addText(ccardTO.getAuthCode());
    }

    return;
  }

  /**
   * Adds the voucher XML nodes based on the voucher transfer object that is passed in. Note that, at this point, these nodes would have already been validated for missing fields, values, etc. Package level access is intentional on this
   * method (absence of public, private, protected).
   * 
   * @param payStanza
   *            The XML stanza which will contain the voucher information.
   * @param voucherTO
   *            The Omni Ticket Voucher Transfer Object.
   */
  static void addVoucherStanza(Element payStanza, OTVoucherTO voucherTO) {

    Element voucherStanza = payStanza.addElement("Voucher");

    voucherStanza.addElement("MasterCode").addText(
        voucherTO.getMasterCode());

    if (voucherTO.getUniqueCode() != null) voucherStanza.addElement(
        "UniqueCode").addText(voucherTO.getUniqueCode());

    return;
  }

  /**
   * Adds the installment XML nodes based on the installment transfer object that is passed in. Note that, at this point, these nodes would have already been validated for missing fields, values, etc. Package level access is intentional
   * on this method (absence of public, private, protected).
   * 
   * @param payStanza
   *            The XML stanza which will contain the voucher information.
   * @param installmentTO
   *            The Omni Ticket Installment Transfer Object.
   */
  static void addInstallmentStanza(Element payStanza,
      OTInstallmentTO installTO) {

    // Installment
    Element installStanza = payStanza.addElement("Installment");

    installStanza.addElement("ContractAlphaCode").addText(
        Integer.toString(installTO.getContractAlphaCode()));

    // Credit Card Stanza
    Element ccardStanza = installStanza.addElement("CreditCard");
    Element cInfoStanza = ccardStanza.addElement("CardInfo");

    if (installTO.getCcManualOrSwiped() == CreditCardEntryType.SWIPED) {

      Element swipedStanza = cInfoStanza.addElement("Swiped");
      swipedStanza.addElement("Track1").addText(installTO.getTrack1());
      swipedStanza.addElement("Track2").addText(installTO.getTrack2());

    }
    else { // must be manual

      Element manualStanza = cInfoStanza.addElement("Manual");
      manualStanza.addElement("CardNumber").addText(
          installTO.getCardNumber());
      manualStanza.addElement("CardExpDate").addText(
          installTO.getCardExpDate());
      manualStanza.addElement("CardHolderName").addText(
          installTO.getCardHolderName());

    }

    // Installment Payor Info
    Element payorStanza = installStanza.addElement("PayorInfo");
    payorStanza.addElement("ClientUniqueId").addText(
        Integer.toString(OTInstallmentTO.getClientuniqueid()));
    Element demoDataStanza = payorStanza.addElement("DemographicData");
    ArrayList<OTFieldTO> oTFieldList = installTO.getDemographicData();

    OTCommonXML.addOTDemoAsFieldType(demoDataStanza, oTFieldList);

    return;
  }

  /**
   * Adds the in-transaction attributes to the XML based on the transaction attribute list provided. Package level access is intentional on this method (absence of public, private, protected).
   * 
   * @param tktStanza
   *            XML ticket stanza that requires the ticket attributes.
   * @param inTxnAttrList
   *            List of Omni Ticket In-Transaction Attribute Transfer Objects
   */
  static void addInTransactionAttributeStanzas(Element tktStanza,
      ArrayList<OTInTransactionAttributeTO> inTxnAttrList) {

    if (inTxnAttrList.size() > 0) {

      for /* each */(OTInTransactionAttributeTO otAttr : /* in */inTxnAttrList) {

        Element inTxnAttr = tktStanza
            .addElement("InTransactionAttribute");

        inTxnAttr.addElement("AttributeCmd").addText(
            otAttr.getAttributeCmd());
        inTxnAttr.addElement("AttributeKey").addText(
            otAttr.getAttributeKey().toString());
        inTxnAttr.addElement("AttributeValue").addText(
            otAttr.getAttributeValue());
        inTxnAttr.addElement("AttributeType").addText(
            otAttr.getAttributeType().toString());
        inTxnAttr.addElement("AttributeFlag").addText(
            otAttr.getAttributeFlag());

      }

    }
    return;
  }

  /**
   * Sets the transfer object for the error based on the parsed XML that is passed in. Package level access is intentional on this method (absence of public, private, protected). Also note that while this is not a section found in the
   * command section of the response XML, it is uniform in every response body, hence its inclusion here (it should have been in the XML's command header).
   * 
   * @param errorNode
   *            The parsed XML section containing the error section of the XML.
   * @return The Omni Ticket Error Transfer Object.
   * @throws DTIException
   *             for any parsing errors or missing mandatory fields.
   */
  static OTErrorTO setOTErrorTO(Node errorNode) throws DTIException {

    OTErrorTO errTO = new OTErrorTO();

    // ErrorCode
    Node errorCodeNode = errorNode.selectSingleNode("ErrorCode");
    if (errorCodeNode == null) throw new DTIException(
        OTManageReservationXML.class,
        DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned XML without a Error,ErrorCode element.");
    else {
      String inText = errorCodeNode.getText();
      errTO.setErrorCode(new Integer(inText));
    }

    // ErrorShortDescription
    Node errShortDescNode = errorNode
        .selectSingleNode("ErrorShortDescription");
    if (errShortDescNode != null) errTO
        .setErrorShortDescription(errShortDescNode.getText());

    // ErrorDescription
    Node errDescNode = errorNode.selectSingleNode("ErrorDescription");
    if (errDescNode != null) errTO.setErrorDescription(errDescNode
        .getText());

    return errTO;
  }

  /**
   * Sets the transfer object of a ticket based on the parsed XML that is passed in. Package level access is intentional on this method (absence of public, private, protected).
   * 
   * @param ticketNode
   *            The section of the XML document containing the ticket.
   * @return The Omni Ticket Ticket Transfer Object.
   * @throws DTIException
   *             for any missing fields or other parsing errors.
   */
  static OTTicketTO setOTTicketTO(Node ticketNode) throws DTIException {

    OTTicketTO otTicket = new OTTicketTO();

    // TDSSN
    Node tdssnNode = ticketNode.selectSingleNode("TDSSN");
    if (tdssnNode != null) {
      // throw new DTIException(OTCommandXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
      // "Ticket provider returned XML without a Ticket,TDSSN element.");

      // Site, Station, Date, TicketId
      Node siteNode = tdssnNode.selectSingleNode("Site");
      Node stationNode = tdssnNode.selectSingleNode("Station");
      Node dateNode = tdssnNode.selectSingleNode("Date");
      Node ticketIdNode = tdssnNode.selectSingleNode("TicketId");
      if ((siteNode == null) || (stationNode == null) || (dateNode == null) || (ticketIdNode == null)) throw new DTIException(
          OTManageReservationXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned XML with incomplete TDSSN element.");

      String dateString = dateNode.getText();
      String siteString = siteNode.getText();
      String stationString = stationNode.getText();
      String ticketIdString = ticketIdNode.getText();

      try {
        otTicket.setTDssn(dateString, siteString, stationString,
            ticketIdString);
      }
      catch (ParseException pe) {
        throw new DTIException(OTCommandXML.class,
            DTIErrorCode.TP_INTERFACE_FAILURE,
            "Ticket provider returned XML with invalid TDSSN element.");
      }

    }

    // TCOD
    Node tcodNode = ticketNode.selectSingleNode("TCOD");
    if (tcodNode != null) {
      otTicket.setTCOD(tcodNode.getText());
    }

    // BarCode
    Node barCodeNode = ticketNode.selectSingleNode("BarCode");
    if (barCodeNode != null) {
      otTicket.setBarCode(barCodeNode.getText());
    }

    // MagCode
    Node magCodeNode = ticketNode.selectSingleNode("MagCode");
    if (magCodeNode != null) {
      otTicket.setMagTrack(magCodeNode.getText());
    }

    // ExternalTicketCode (ignored)
    Node externalNode = ticketNode.selectSingleNode("ExternalTicketCode");
    if (externalNode != null) {
      otTicket.setExternalTicketCode(externalNode.getText());
    }

    return otTicket;
  }

  /**
   * Sets the transfer object for ticket availability bsed on the parsed XML provided. Package level access is intentional on this method (absence of public, private, protected).
   * 
   * @param aNode
   *            parsed XML document section containing ticket validity.
   * @param otTktTO
   *            The Omni Ticket Ticket Info Transfer Object that needs to be populated.
   * @throws DTIException
   *             for any missing fields or parsing exceptions.
   */
  static void setOTTicketInfoValidity(Node aNode, OTTicketInfoTO otTktTO) throws DTIException {

    // Ticket
    Node ticketNode = aNode.selectSingleNode("Ticket");
    if (ticketNode != null) otTktTO.setTicket(OTCommandXML
        .setOTTicketTO(ticketNode));

    // Validity
    Node validityNode = aNode.selectSingleNode("Validity");
    if (validityNode != null) {

      Node startDateNode = validityNode.selectSingleNode("StartDate");
      Node endDateNode = validityNode.selectSingleNode("EndDate");

      if ((startDateNode == null) || (endDateNode == null)) throw new DTIException(
          OTManageReservationXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned XML with an incomplete Validity element.");

      String startDateString = startDateNode.getText();
      String endDateString = endDateNode.getText();

      try {
        otTktTO.setValidityStartDate(startDateString);
        otTktTO.setValidityEndDate(endDateString);
      }
      catch (ParseException pe) {
        throw new DTIException(
            OTManageReservationXML.class,
            DTIErrorCode.TP_INTERFACE_FAILURE,
            "Ticket provider returned XML with an invalid Validity element: " + pe
                .toString());
      }
    }
  }

  /**
   * Sets up the transfer object from the DSSN node provided from a parsed XML document. Package level access is intentional on this method (absence of public, private, protected).
   * 
   * @param dssnNode
   *            The XML node containing the dssn section of the XML.
   * @return a completed Omni Ticket Transaction DSSN Transfer Object.
   * @throws DTIException
   *             for any parsing errors, generally the absence of required fields.
   */
  static OTTransactionDSSNTO setOTTransactionDSSNTO(Node dssnNode) throws DTIException {

    OTTransactionDSSNTO otTranDssn = new OTTransactionDSSNTO();

    // Site
    Node siteNode = dssnNode.selectSingleNode("Site");
    if (siteNode == null) throw new DTIException(
        OTManageReservationXML.class,
        DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned XML without a TransactionDSSN,Site element.");
    otTranDssn.setSite(siteNode.getText());

    // Station
    Node stationNode = dssnNode.selectSingleNode("Station");
    if (stationNode == null) throw new DTIException(
        OTManageReservationXML.class,
        DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned XML without a TransactionDSSN,Station element.");
    otTranDssn.setStation(stationNode.getText());

    // Date
    Node dateNode = dssnNode.selectSingleNode("Date");
    if (dateNode == null) throw new DTIException(
        OTManageReservationXML.class,
        DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned XML without a TransactionDSSN,Date element.");
    try {
      otTranDssn.setDate(dateNode.getText());
    }
    catch (ParseException pe) {
      throw new DTIException(OTManageReservationXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned XML with an invalid TransactionDSSN,Date element.");
    }

    // TransactionId
    Node tranIdNode = dssnNode.selectSingleNode("TransactionId");
    if (tranIdNode == null) throw new DTIException(
        OTManageReservationXML.class,
        DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned XML without a TransactionDSSN,TransactionId element.");
    String inString = tranIdNode.getText();
    otTranDssn.setTransactionId(new Integer(inString));

    return otTranDssn;
  }

  /**
   * Sets up the transfer object from the Omni Ticket payment section of a response XML. Package level access is intentional (the absence of public, private, or protected).
   * 
   * @param otPayList
   *            ArrayList of Omni Ticket Payment Transfer Objects.
   * @param payNodeList
   *            List of XML nodes from a parsed document
   * @throws DTIException
   *             for any parse errors
   */
  static void setOTPaymentTOList(ArrayList<OTPaymentTO> otPayList,
      List<Node> payNodeList) throws DTIException {

    List<Node> payList = (List<Node>) payNodeList;

    for /* each */(Node aNode : /* in */payList) {

      OTPaymentTO otPayTO = new OTPaymentTO();

      // Item
      Node itemNode = aNode.selectSingleNode("Item");
      if (itemNode == null) throw new DTIException(
          OTManageReservationXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned XML without a PaymentInfo,Item element.");
      String inString = itemNode.getText();
      otPayTO.setPayItem(new BigInteger(inString));

      // PaymentType
      Node payTypeNode = aNode.selectSingleNode("PaymentType");
      if (payTypeNode == null) throw new DTIException(
          OTManageReservationXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned XML without a PaymentInfo,PaymentType element.");
      inString = payTypeNode.getText();
      otPayTO.setPayTypeInt(Integer.parseInt(inString));

      // PaymentAmount
      Node payAmountNode = aNode.selectSingleNode("PaymentAmount");
      if (payAmountNode == null) throw new DTIException(
          OTManageReservationXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned XML without a PaymentInfo,PaymentAmount element.");
      inString = payAmountNode.getText();
      otPayTO.setPayAmount(new BigDecimal(inString));

      // CreditCard
      Node creditCardNode = aNode.selectSingleNode("CreditCard");
      if (creditCardNode != null) otPayTO
          .setCreditCard(setOTCreditCardTO(creditCardNode));

      // Voucher
      Node voucherNode = aNode.selectSingleNode("Voucher");
      if (voucherNode != null) otPayTO
          .setVoucher(setOTVoucherTO(voucherNode));

      // Installment (new with 2.15, JTL)
      Node installmentNode = aNode.selectSingleNode("Installment");
      if (installmentNode != null) otPayTO
          .setInstallment(setOTInstallmentTO(installmentNode));

      otPayList.add(otPayTO);
    }

    return;
  }

  /**
   * Sets the transfer object for credit card based on the response that is passed in.
   * 
   * @param ccNode
   *            the parsed XML containing the credit card.
   * @return The Omni Ticket Credit Card Transfer Object
   * @throws DTIException
   *             for any missing mandatory fields or other parsing errors.
   */
  private static OTCreditCardTO setOTCreditCardTO(Node ccNode) throws DTIException {

    OTCreditCardTO otCreditCard = new OTCreditCardTO();

    // CardType
    Node cardTypeNode = ccNode.selectSingleNode("CardType");
    if (cardTypeNode == null) throw new DTIException(
        OTManageReservationXML.class,
        DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned XML without a CreditCard,CardType element.");
    String inString = cardTypeNode.getText();
    otCreditCard.setGiftCardIndicator(Integer.parseInt(inString));

    // AuthErrorCode
    Node authErrCodeNode = ccNode.selectSingleNode("AuthErrorCode");
    if (authErrCodeNode == null) throw new DTIException(
        OTManageReservationXML.class,
        DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned XML without a CreditCard,AuthErrorCode element.");
    otCreditCard.setAuthErrorCode(authErrCodeNode.getText());

    // AuthNumber
    Node authNumNode = ccNode.selectSingleNode("AuthNumber");
    if (authNumNode != null) otCreditCard.setAuthNumber(authNumNode
        .getText());

    // AuthMessage
    Node authMsgNode = ccNode.selectSingleNode("AuthMessage");
    if (authMsgNode != null) otCreditCard.setAuthMessage(authMsgNode
        .getText());

    // CCNumber
    Node ccNumNode = ccNode.selectSingleNode("CCNumber");
    if (ccNumNode != null) otCreditCard.setCcNumber(ccNumNode.getText());

    // CCExpiration (ignored)

    // RemainingBalance
    Node remBalNode = ccNode.selectSingleNode("RemainingBalance");
    if (remBalNode != null) {
      inString = remBalNode.getText();
      otCreditCard.setRemainingBalance(new BigDecimal(inString));
    }

    // PromoExpDate
    Node promoExpNode = ccNode.selectSingleNode("PromoExpDate");
    if (promoExpNode != null) {
      inString = promoExpNode.getText();
      try {
        otCreditCard.setPromoExpDate(inString);
      }
      catch (ParseException pe) {
        throw new DTIException(
            OTManageReservationXML.class,
            DTIErrorCode.TP_INTERFACE_FAILURE,
            "Ticket provider returned XML with an invalid CreditCard,PromoExpDate element: " + pe
                .toString());
      }
    }

    // RRN (ignored)

    return otCreditCard;
  }

  /**
   * Sets the transfer object for the voucher based on parsed XML that is passed in.
   * 
   * @param voucherNode
   *            the parsed XML document containing a voucher.
   * @return The Omni Ticket Voucher Transfer Object.
   * @throws DTIException
   *             for any missing required fields or other parsing errors.
   */
  private static OTVoucherTO setOTVoucherTO(Node voucherNode) throws DTIException {

    OTVoucherTO otVoucher = new OTVoucherTO();

    // MasterCode
    Node masterCodeNode = voucherNode.selectSingleNode("MasterCode");
    if (masterCodeNode == null) throw new DTIException(
        OTManageReservationXML.class,
        DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned XML without a Voucher,MasterCode element.");
    otVoucher.setMasterCode(masterCodeNode.getText());

    Node uniqueCodeNode = voucherNode.selectSingleNode("UniqueCode");
    if (uniqueCodeNode != null) otVoucher.setUniqueCode(uniqueCodeNode
        .getText());

    return otVoucher;
  }

  /**
   * Sets the transfer object for credit card based on the response that is passed in.
   * 
   * @since 2.15
   * @author lewit019
   * @param instNode
   *            the parsed XML containing the credit card.
   * @return The Omni Ticket Credit Card Transfer Object
   * @throws DTIException
   *             for any missing mandatory fields or other parsing errors.
   */
  private static OTInstallmentTO setOTInstallmentTO(Node instNode) throws DTIException {

    OTInstallmentTO otInstallmentTO = new OTInstallmentTO();

    // ContractID
    Node contractIdNode = instNode.selectSingleNode("ContractId");
    if (contractIdNode == null) throw new DTIException(
        OTManageReservationXML.class,
        DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned XML without an Installment,ContractId element.");
    String inString = contractIdNode.getText();
    otInstallmentTO.setContractId(inString);

    // PayorId (ignored)

    return otInstallmentTO;
  }

  /**
   * Adds the work rules to the transaction XML clause provided in the transfer object list. Package level access is intentional (the absence of public, private, or protected).
   * 
   * @param txnStanza
   *            the transaction stanza which needs to have the work rules added.
   * @param tagTOList
   *            A list of work rule strings from the Omni Ticket transfer objects.
   */
  static void addWorkRules(Element txnStanza, ArrayList<String> tagTOList) {

    if ((tagTOList != null) && (tagTOList.size() > 0)) {
      Element workRulesStanza = txnStanza.addElement("WorkRules");

      for /* each */(String aTagValue : /* in */tagTOList) {
        workRulesStanza.addElement("Tags").addText(aTagValue);
      }

    }

    return;
  }

  /**
   * Gets the error node, adjusting for generic errors (replacing generic error codes, if present, with ItemStatus, which has more detail.
   * 
   * @param bodyNode
   *            The body response node containing the error clause.
   * @return The Omni Ticket Error Transport Object
   * @throws DTIException
   *             for a missing "Error" element.
   */
  public static OTErrorTO getErrorTO(Node bodyNode) throws DTIException {

    // Error
    Node errorNode = bodyNode.selectSingleNode("Error");
    if (errorNode == null) throw new DTIException(OTCommandXML.class,
        DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned XML without a Error element.");

    // ItemStatus replacement for generic errors, if required.
    OTErrorTO otErrorTO = OTCommandXML.setOTErrorTO(errorNode);
    if ((otErrorTO.getErrorCode().intValue() == OTCommandXML.GENERIC_OPER_PARTIALLY_SUCCESSFUL) || (otErrorTO
        .getErrorCode().intValue() == OTCommandXML.GENERIC_OPER_FAILED)) {
      Node itemStatusNode = bodyNode.selectSingleNode("//ItemStatus");
      if (itemStatusNode != null) {
        String inText = itemStatusNode.getText();
        otErrorTO.setErrorCode(new Integer(inText));
      }
    }

    return otErrorTO;
  }

}
