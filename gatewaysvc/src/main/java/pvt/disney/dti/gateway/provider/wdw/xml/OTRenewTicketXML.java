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
import pvt.disney.dti.gateway.provider.wdw.data.OTRenewTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO.TicketIDType;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

/**
<<<<<<< HEAD
 * This class is responsible for transforming transfer objects into XML nodes
 * and back again for the Omni Ticket Renew Ticket Command. Please note that
 * all of its methods are either package or private level access.
=======
 * This class is responsible for transforming transfer objects into XML nodes and back again for the Omni Ticket Renew Ticket Command. Please note that all of its methods are either package or private level access.
>>>>>>> develop
 * 
 * @author lewit019
 * @since 2.16.1
 */
public class OTRenewTicketXML {

<<<<<<< HEAD
  /**
   * Adds the transaction body of renew ticket to the command stanza when
   * provided with the appropriate transfer object.
   * 
   * @param otRenewTktTO
   *          The Omni Ticket Update Transaction Transfer Object.
   * @param commandStanza
   *          The command stanza needing the update transaction stanza.
   */
  static void addTxnBodyElement(OTRenewTicketTO otRenewTktTO, Element commandStanza) {

    // UpdateTransaction
    Element renewTicketStanza = commandStanza.addElement("RenewTicket");

    // WorkRules
    ArrayList<String> tagList = otRenewTktTO.getTagsList();
    OTCommandXML.addWorkRules(renewTicketStanza, tagList);

    // SiteNumber
    renewTicketStanza.addElement("SiteNumber").addText(otRenewTktTO.getSiteNumber().toString());

    // RenewTicketInfo
    ArrayList<OTTicketInfoTO> renewTicketTOList = otRenewTktTO.getRenewTicketInfoList();
    for (/* each */OTTicketInfoTO aRenewTicketTO : /* in */renewTicketTOList) {

      Element renewTicketInfoStanza = renewTicketStanza.addElement("RenewTicketInfo");

      // Item
      renewTicketInfoStanza.addElement("Item").addText(aRenewTicketTO.getItem().toString());

      // TicketSearchMode
      // aRenewTicketTO.getTicketSearchMode();
      // TicketSearchMode
      Element tktSrchModeStanza = renewTicketInfoStanza.addElement("TicketSearchMode");

      OTTicketTO otTicket = aRenewTicketTO.getTicketSearchMode();
      ArrayList<TicketIDType> otTicketIdTypeList = otTicket.getTktIdentityTypes();
      TicketIDType otTicketIdType = otTicketIdTypeList.get(0);

      switch (otTicketIdType) {

      case MAGCODE:
        if (otTicket.getMagTrack() != null)
          tktSrchModeStanza.addElement("MagCode").addText(otTicket.getMagTrack());
        break;

      case TDSSN:

        Element tdssnElement = tktSrchModeStanza.addElement("TDSSN");

        if (otTicket.getTdssnSite() != null)
          tdssnElement.addElement("Site").addText(otTicket.getTdssnSite());
        if (otTicket.getTdssnStation() != null)
          tdssnElement.addElement("Station").addText(otTicket.getTdssnStation());
        if (otTicket.getTdssnDate() != null) {
          GregorianCalendar tdssnDateCal = otTicket.getTdssnDate();
          String tdssnDate = CustomDataTypeConverter.printCalToDTIDateFormat(tdssnDateCal);
          tdssnElement.addElement("Date").addText(tdssnDate);
        }
        if (otTicket.getTdssnTicketId() != null)
          tdssnElement.addElement("TicketId").setText(otTicket.getTdssnTicketId());
        break;

      case TCOD:
        if (otTicket.getTCOD() != null)
          tktSrchModeStanza.addElement("TCOD").addText(otTicket.getTCOD());
        break;

      case BARCODE:
        if (otTicket.getBarCode() != null)
          tktSrchModeStanza.addElement("BarCode").addText(otTicket.getBarCode());
        break;

      case EXTERNALTICKETCODE:
        if (otTicket.getExternalTicketCode() != null)
          tktSrchModeStanza.addElement("ExternalTicketCode").addText(otTicket.getExternalTicketCode());
        break;

      case UNDEFINED:
        break;

      default:
        break;

      }

      // TicketType
      renewTicketInfoStanza.addElement("TicketType").addText(aRenewTicketTO.getTicketType().toString());

      // DemographicData (there should be only one line)
      OTDemographicData otDemoData = aRenewTicketTO.getSeasonPassDemo();
      ArrayList<OTFieldTO> otFieldList = otDemoData.getDemoDataList();
      
      if ((otDemoData != null) && (otDemoData.getDemoDataList().size() > 0)) {

         Element demoDataStanza = renewTicketInfoStanza.addElement("DemographicData");
         OTCommonXML.addOTDemoAsFieldId(demoDataStanza, otFieldList);

      }
    }

    // PaymentInfo
    OTCommandXML.addPaymentInfoStanza(renewTicketStanza, otRenewTktTO.getPaymentInfoList());

    // TransactionNote
    renewTicketStanza.addElement("TransactionNote").addText(otRenewTktTO.getTransactionNote());
    
    // OnSameMedia
    renewTicketStanza.addElement("OnSameMedia").addText("false"); // Force a entitlement number change.
    
    // GenerateEvent
    renewTicketStanza.addElement("GenerateEvent").addText(otRenewTktTO.getGenerateEvent().toString());

    return;
  }

  /**
   * When provided with the renew ticket XML section of a parsed XML,
   * fills out the transfer object.
   * 
   * @param renewTicketNode
   *          The parsed section of the XML containing the
   * @return The Omni Ticket Renew Ticket Transfer Object.
   * @throws DTIException
   *           for any parsing errors or missing required fields.
   */
  @SuppressWarnings("unchecked")
  public static OTRenewTicketTO getTO(Node renewTicketNode) throws DTIException {
    
    OTRenewTicketTO otRenewTktTO = new OTRenewTicketTO();
    
    // Error
    Node errorNode = renewTicketNode.selectSingleNode("Error");
    if (errorNode == null)
      throw new DTIException(OTManageReservationXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned XML without a RenewTicket,Error element.");
    else
      otRenewTktTO.setError(OTCommandXML.setOTErrorTO(errorNode));
    
    // RenewTicketInfo List (up to 8 items)
    List<Node> ticketInfoNodeList = renewTicketNode.selectNodes("RenewTicketInfo");
    if ((ticketInfoNodeList != null) && (ticketInfoNodeList.size() > 0))
      setOTTicketInfoTOList(otRenewTktTO.getRenewTicketInfoList(), ticketInfoNodeList);
    
    // PaymentInfo
    List<Node> paymentInfoNodeList = renewTicketNode.selectNodes("PaymentInfo");
    if (paymentInfoNodeList != null)
      OTCommandXML.setOTPaymentTOList(otRenewTktTO.getPaymentInfoList(), paymentInfoNodeList);
    
    // Transaction DSSN
    Node tranDssnNode = renewTicketNode.selectSingleNode("TransactionDSSN");
    if (tranDssnNode != null)
      otRenewTktTO.setTransactionDSSN(OTCommandXML.setOTTransactionDSSNTO(tranDssnNode));    
    
    // Transaction COD
    Node tranCODNode = renewTicketNode.selectSingleNode("TransactionCOD");
    if (tranCODNode != null)
      otRenewTktTO.setTransactionCOD(tranCODNode.getText());
    
    // Total Amount
    Node totalAmountNode = renewTicketNode.selectSingleNode("TotalAmount");
    if (totalAmountNode != null) {
      BigDecimal totalAmt = new BigDecimal(totalAmountNode.getText());
      otRenewTktTO.setTotalAmount(totalAmt);
    }
    
    // Total Tax
    Node totalTaxNode = renewTicketNode.selectSingleNode("TotalTax");
    if (totalTaxNode != null) {
      BigDecimal totalTax = new BigDecimal(totalTaxNode.getText());
      otRenewTktTO.setTotalTax(totalTax);
    }
    
    return otRenewTktTO;
  }
  
  /**
   * Sets the ticket info transfer object list based upon the parsed XML
   * provided.
   * 
   * @param otTktList
   *          The list of Omni Ticket Ticket Info Transfer Objects.
   * @param ticketInfoNodeList
   *          the parsed XML containing the ticket info list
   * @throws DTIException
   *           for any parsing errors or missing required fields.
   */
  private static void setOTTicketInfoTOList(ArrayList<OTTicketInfoTO> otTktList, List<Node> ticketInfoNodeList)
      throws DTIException {

    List<Node> ticketList = (List<Node>) ticketInfoNodeList;

    for /* each */(Node aNode : /* in */ ticketList) {

      OTTicketInfoTO otTktTO = new OTTicketInfoTO();

      // Item
      Node itemNode = aNode.selectSingleNode("Item");
      if (itemNode == null)
        throw new DTIException(OTManageReservationXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
            "Ticket provider returned XML without a TicketInfo,Item element.");
      else {
        String inText = itemNode.getText();
        otTktTO.setItem(new BigInteger(inText));
      }

      // ItemStatus
      Node itemStatusNode = aNode.selectSingleNode("ItemStatus");
      if (itemStatusNode == null)
        throw new DTIException(OTManageReservationXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
            "Ticket provider returned XML without a TicketInfo,ItemStatus element.");
      else {
        String inText = itemStatusNode.getText();
        otTktTO.setItemStatus(new Integer(inText));
      }
      
      // Ticket
      Node ticketNode = aNode.selectSingleNode("Ticket");
      if (ticketNode != null)
        otTktTO.setTicket(OTCommandXML.setOTTicketTO(ticketNode));

      // TicketType (ignored)
      // TicketCode (ignored)
      // ItemAlphaCode (ignored)
      // ItemNumCode (ignored)
      // TicketName (ignored)
      // Description (ignored)
      // PrintedPrice (ignored)

      // Price
      Node priceNode = aNode.selectSingleNode("Price");
      if (priceNode != null) {
        String inText = priceNode.getText();
        otTktTO.setPrice(new BigDecimal(inText));
      }

      // Tax
      Node taxNode = aNode.selectSingleNode("Tax");
      if (taxNode != null) {
        String inText = taxNode.getText();
        otTktTO.setTax(new BigDecimal(inText));
      }

      // Validity
      OTCommandXML.setOTTicketInfoValidity(aNode, otTktTO);

      otTktList.add(otTktTO);
    }

    return;
  }

  
=======
	/**
	 * Adds the transaction body of renew ticket to the command stanza when provided with the appropriate transfer object.
	 * 
	 * @param otRenewTktTO
	 *            The Omni Ticket Update Transaction Transfer Object.
	 * @param commandStanza
	 *            The command stanza needing the update transaction stanza.
	 */
	static void addTxnBodyElement(OTRenewTicketTO otRenewTktTO,
			Element commandStanza) {

		// UpdateTransaction
		Element renewTicketStanza = commandStanza.addElement("RenewTicket");

		// WorkRules
		ArrayList<String> tagList = otRenewTktTO.getTagsList();
		OTCommandXML.addWorkRules(renewTicketStanza, tagList);

		// SiteNumber
		renewTicketStanza.addElement("SiteNumber").addText(
				otRenewTktTO.getSiteNumber().toString());

		// RenewTicketInfo
		ArrayList<OTTicketInfoTO> renewTicketTOList = otRenewTktTO
				.getRenewTicketInfoList();
		for (/* each */OTTicketInfoTO aRenewTicketTO : /* in */renewTicketTOList) {

			Element renewTicketInfoStanza = renewTicketStanza
					.addElement("RenewTicketInfo");

			// Item
			renewTicketInfoStanza.addElement("Item").addText(
					aRenewTicketTO.getItem().toString());

			// TicketSearchMode
			// aRenewTicketTO.getTicketSearchMode();
			// TicketSearchMode
			Element tktSrchModeStanza = renewTicketInfoStanza
					.addElement("TicketSearchMode");

			OTTicketTO otTicket = aRenewTicketTO.getTicketSearchMode();
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
			renewTicketInfoStanza.addElement("TicketType").addText(
					aRenewTicketTO.getTicketType().toString());

			// DemographicData (there should be only one line)
			OTDemographicData otDemoData = aRenewTicketTO.getSeasonPassDemo();
			ArrayList<OTFieldTO> otFieldList = otDemoData.getDemoDataList();

			if ((otDemoData != null) && (otDemoData.getDemoDataList().size() > 0)) {

				Element demoDataStanza = renewTicketInfoStanza
						.addElement("DemographicData");
				OTCommonXML.addOTDemoAsFieldId(demoDataStanza, otFieldList);

			}
		}

		// PaymentInfo
		OTCommandXML.addPaymentInfoStanza(renewTicketStanza,
				otRenewTktTO.getPaymentInfoList());

		// TransactionNote
		renewTicketStanza.addElement("TransactionNote").addText(
				otRenewTktTO.getTransactionNote());

		// OnSameMedia
		renewTicketStanza.addElement("OnSameMedia").addText("false"); // Force a entitlement number change.

		// GenerateEvent
		renewTicketStanza.addElement("GenerateEvent").addText(
				otRenewTktTO.getGenerateEvent().toString());

		return;
	}

	/**
	 * When provided with the renew ticket XML section of a parsed XML, fills out the transfer object.
	 * 
	 * @param renewTicketNode
	 *            The parsed section of the XML containing the
	 * @return The Omni Ticket Renew Ticket Transfer Object.
	 * @throws DTIException
	 *             for any parsing errors or missing required fields.
	 */
	@SuppressWarnings("unchecked")
	public static OTRenewTicketTO getTO(Node renewTicketNode) throws DTIException {

		OTRenewTicketTO otRenewTktTO = new OTRenewTicketTO();

		// Error
		Node errorNode = renewTicketNode.selectSingleNode("Error");
		if (errorNode == null) throw new DTIException(
				OTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a RenewTicket,Error element.");
		else otRenewTktTO.setError(OTCommandXML.setOTErrorTO(errorNode));

		// RenewTicketInfo List (up to 8 items)
		List<Node> ticketInfoNodeList = renewTicketNode
				.selectNodes("RenewTicketInfo");
		if ((ticketInfoNodeList != null) && (ticketInfoNodeList.size() > 0)) setOTTicketInfoTOList(
				otRenewTktTO.getRenewTicketInfoList(), ticketInfoNodeList);

		// PaymentInfo
		List<Node> paymentInfoNodeList = renewTicketNode
				.selectNodes("PaymentInfo");
		if (paymentInfoNodeList != null) OTCommandXML.setOTPaymentTOList(
				otRenewTktTO.getPaymentInfoList(), paymentInfoNodeList);

		// Transaction DSSN
		Node tranDssnNode = renewTicketNode.selectSingleNode("TransactionDSSN");
		if (tranDssnNode != null) otRenewTktTO.setTransactionDSSN(OTCommandXML
				.setOTTransactionDSSNTO(tranDssnNode));

		// Transaction COD
		Node tranCODNode = renewTicketNode.selectSingleNode("TransactionCOD");
		if (tranCODNode != null) otRenewTktTO.setTransactionCOD(tranCODNode
				.getText());

		// Total Amount
		Node totalAmountNode = renewTicketNode.selectSingleNode("TotalAmount");
		if (totalAmountNode != null) {
			BigDecimal totalAmt = new BigDecimal(totalAmountNode.getText());
			otRenewTktTO.setTotalAmount(totalAmt);
		}

		// Total Tax
		Node totalTaxNode = renewTicketNode.selectSingleNode("TotalTax");
		if (totalTaxNode != null) {
			BigDecimal totalTax = new BigDecimal(totalTaxNode.getText());
			otRenewTktTO.setTotalTax(totalTax);
		}

		return otRenewTktTO;
	}

	/**
	 * Sets the ticket info transfer object list based upon the parsed XML provided.
	 * 
	 * @param otTktList
	 *            The list of Omni Ticket Ticket Info Transfer Objects.
	 * @param ticketInfoNodeList
	 *            the parsed XML containing the ticket info list
	 * @throws DTIException
	 *             for any parsing errors or missing required fields.
	 */
	private static void setOTTicketInfoTOList(
			ArrayList<OTTicketInfoTO> otTktList, List<Node> ticketInfoNodeList) throws DTIException {

		List<Node> ticketList = (List<Node>) ticketInfoNodeList;

		for /* each */(Node aNode : /* in */ticketList) {

			OTTicketInfoTO otTktTO = new OTTicketInfoTO();

			// Item
			Node itemNode = aNode.selectSingleNode("Item");
			if (itemNode == null) throw new DTIException(
					OTManageReservationXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfo,Item element.");
			else {
				String inText = itemNode.getText();
				otTktTO.setItem(new BigInteger(inText));
			}

			// ItemStatus
			Node itemStatusNode = aNode.selectSingleNode("ItemStatus");
			if (itemStatusNode == null) throw new DTIException(
					OTManageReservationXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfo,ItemStatus element.");
			else {
				String inText = itemStatusNode.getText();
				otTktTO.setItemStatus(new Integer(inText));
			}

			// Ticket
			Node ticketNode = aNode.selectSingleNode("Ticket");
			if (ticketNode != null) otTktTO.setTicket(OTCommandXML
					.setOTTicketTO(ticketNode));

			// TicketType (ignored)
			// TicketCode (ignored)
			// ItemAlphaCode (ignored)
			// ItemNumCode (ignored)
			// TicketName (ignored)
			// Description (ignored)
			// PrintedPrice (ignored)

			// Price
			Node priceNode = aNode.selectSingleNode("Price");
			if (priceNode != null) {
				String inText = priceNode.getText();
				otTktTO.setPrice(new BigDecimal(inText));
			}

			// Tax
			Node taxNode = aNode.selectSingleNode("Tax");
			if (taxNode != null) {
				String inText = taxNode.getText();
				otTktTO.setTax(new BigDecimal(inText));
			}

			// Validity
			OTCommandXML.setOTTicketInfoValidity(aNode, otTktTO);

			otTktList.add(otTktTO);
		}

		return;
	}
>>>>>>> develop

}
