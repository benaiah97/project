package pvt.disney.dti.gateway.provider.wdw.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO.TicketIDType;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

/**
 * This class is responsible for transforming transfer objects into XML nodes and back again for the Omni Ticket Query Ticket Command. Please note that all of its methods are either package or private level access.
 * 
 * @author lewit019
 */
public class OTQueryTicketXML {

	/** The prefix of a CAS station, sometimes missing in requests. */
	public final static String CASPREFIX = "CAS";
	private static EventLogger logger = EventLogger
			.getLogger(OTQueryTicketXML.class);

	/**
	 * Adds the transaction body element of a query ticket to the XML of the command stanza.
	 * 
	 * @param otQryTktTO
	 *            The Omni Ticket Query Ticket Transfer Object.
	 * @param commandStanza
	 *            The XML command stanza.
	 */
	static void addTxnBodyElement(OTQueryTicketTO otQryTktTO,
			Element commandStanza) {

		// QueryTicket
		Element qryTktStanza = commandStanza.addElement("QueryTicket");

		// WorkRules
		ArrayList<String> tagList = otQryTktTO.getTagsList();
		OTCommandXML.addWorkRules(qryTktStanza, tagList);

		// SiteNumber
		qryTktStanza.addElement("SiteNumber").addText(
				otQryTktTO.getSiteNumber().toString());

		// TicketInfo
		ArrayList<OTTicketInfoTO> tktList = otQryTktTO.getTicketInfoList();
		for /* each */(OTTicketInfoTO otTicketInfo : /* in */tktList) {

			Element ticketInfoStanza = qryTktStanza.addElement("TicketInfo");

			// Item
			ticketInfoStanza.addElement("Item").addText(
					otTicketInfo.getItem().toString());

			// TicketSearchMode
			Element tktSrchModeStanza = ticketInfoStanza
					.addElement("TicketSearchMode");

			OTTicketTO otTicket = otTicketInfo.getTicketSearchMode();
			ArrayList<TicketIDType> otTicketIdTypeList = otTicket
					.getTktIdentityTypes();
			TicketIDType otTicketIdType = otTicketIdTypeList.get(0);

			switch (otTicketIdType) {

			case MAGCODE:
				tktSrchModeStanza.addElement("MagCode").addText(
						otTicket.getMagTrack());
				break;

			case TDSSN:
				Element tdssnElement = tktSrchModeStanza.addElement("TDSSN");
				tdssnElement.addElement("Site")
						.setText(otTicket.getTdssnSite());
				String station = otTicket.getTdssnStation();
				if (station.length() > 2) {
					String prefix = station.substring(0, 3);
					if (prefix.compareTo("CAS") == 0) tdssnElement.addElement(
							"Station").setText(otTicket.getTdssnStation());
					else tdssnElement.addElement("Station").setText(
							CASPREFIX + otTicket.getTdssnStation());
				}
				else tdssnElement.addElement("Station").setText(
						CASPREFIX + otTicket.getTdssnStation());

				GregorianCalendar tdssnDateCal = otTicket.getTdssnDate();
				String tdssnDate = CustomDataTypeConverter
						.printCalToDTIDateFormat(tdssnDateCal);
				tdssnElement.addElement("Date").setText(tdssnDate);
				tdssnElement.addElement("TicketId").setText(
						otTicket.getTdssnTicketId());
				break;

			case TCOD:
				tktSrchModeStanza.addElement("TCOD")
						.addText(otTicket.getTCOD());
				break;

			case BARCODE:
				tktSrchModeStanza.addElement("BarCode").addText(
						otTicket.getBarCode());
				break;

			case EXTERNALTICKETCODE:
				tktSrchModeStanza.addElement("ExternalTicketCode").addText(
						otTicket.getExternalTicketCode());
				break;
			}

		}

		return;
	}

	/**
	 * Gets the transfer object for query ticket based on the parsed XML passed in.
	 * 
	 * @param queryTktNode
	 *            the parsed XML containing query ticket.
	 * @return The Omni Ticket Query Ticket Transfer Object.
	 * @throws DTIException
	 *             for any parsing errors or missing required fields.
	 */
	static OTQueryTicketTO getTO(Node queryTktNode) throws DTIException {

		OTQueryTicketTO qryTktTO = new OTQueryTicketTO();

		// Set error object
		OTErrorTO otErrorTO = OTCommandXML.getErrorTO(queryTktNode);
		qryTktTO.setError(otErrorTO);

		// TicketInfo
		@SuppressWarnings("unchecked")
		List<Node> tktInfoNodeList = queryTktNode.selectNodes("TicketInfo");
		setOTTicketInfoTOList(tktInfoNodeList, qryTktTO.getTicketInfoList());

		return qryTktTO;
	}

	/**
	 * Sets the ticket info list transfer objects based upon the parsed XML that's passed in.
	 * 
	 * @param ticketInfoNodeList
	 *            the parsed XML section containing the ticket info list.
	 * @param otTktList
	 *            The list of Omni Ticket Ticket Info Transfer Objects.
	 * @throws DTIException
	 *             for any missing required fields or other parsing errors.
	 */
	@SuppressWarnings("unchecked")
	private static void setOTTicketInfoTOList(List<Node> ticketInfoNodeList,
			ArrayList<OTTicketInfoTO> otTktList) throws DTIException {

		List<Node> ticketList = (List<Node>) ticketInfoNodeList;

		for /* each */(Node aNode : /* in */ticketList) {

			OTTicketInfoTO otTktInfoTO = new OTTicketInfoTO();

			// Item
			Node itemNode = aNode.selectSingleNode("Item");
			if (itemNode == null) throw new DTIException(
					OTQueryTicketXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfo,Item element.");
			else {
				String inText = itemNode.getText();
				otTktInfoTO.setItem(new BigInteger(inText));
			}

			// ItemStatus
			Node itemStatusNode = aNode.selectSingleNode("ItemStatus");
			if (itemStatusNode == null) throw new DTIException(
					OTQueryTicketXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfo,ItemStatus element.");
			else {
				String inText = itemStatusNode.getText();
				otTktInfoTO.setItemStatus(new Integer(inText));
			}

			// TicketType
			Node ticketTypeNode = aNode.selectSingleNode("TicketType");
			if (ticketTypeNode != null) {
				String inText = ticketTypeNode.getText();
				otTktInfoTO.setTicketType(new BigInteger(inText));
			}

			// Price
			Node priceNode = aNode.selectSingleNode("Price");
			if (priceNode != null) {
				String inText = priceNode.getText();
				otTktInfoTO.setPrice(new BigDecimal(inText));
			}

			// Tax
			Node taxNode = aNode.selectSingleNode("Tax");
			if (taxNode != null) {
				String inText = taxNode.getText();
				otTktInfoTO.setTax(new BigDecimal(inText));
			}

			// Ticket
			Node ticketNode = aNode.selectSingleNode("Ticket");
			if (ticketNode != null) otTktInfoTO.setTicket(OTCommandXML
					.setOTTicketTO(ticketNode));

			// RemainingValue (ignored)

			// VoidCode
			Node voidCodeNode = aNode.selectSingleNode("VoidCode");
			if (voidCodeNode != null) {
				String inText = voidCodeNode.getText();
				otTktInfoTO.setVoidCode(Integer.decode(inText));
			}

			// Validity
			OTCommandXML.setOTTicketInfoValidity(aNode, otTktInfoTO);

			// TicketFlag (ignored)
			// Usages (ignored)

			// TicketAttribute
			Node ticketAttrNode = aNode.selectSingleNode("TicketAttribute");
			if (ticketAttrNode != null) {
				String inText = ticketAttrNode.getText();
				otTktInfoTO.setTicketAttribute(Integer.decode(inText));
			}

			// TicketNote (ignored)
			// BiometricLevel (ignored)
			// TransactionDSSN (ignored)
			// TransactionCOD (ignored)
			// GroupQuantity (ignored)
			// TaxExempt (ignored)

			// AlreadyUsed
			Node alreadyUsedNode = aNode.selectSingleNode("AlreadyUsed");
			if (alreadyUsedNode != null) {
				String inText = alreadyUsedNode.getText();
				otTktInfoTO.setAlreadyused(inText);
			}

			// DebitCardAuth (ignored)
			// BiometricInfo (ignored)

			// SeasonPassInfo (as of 2.16.1)
			Node seasonPassInfoNode = aNode.selectSingleNode("SeasonPassInfo");
			if (seasonPassInfoNode != null) {

				Node demoDataNode = seasonPassInfoNode
						.selectSingleNode("DemographicData");
				if (demoDataNode != null) {

					OTDemographicData otDemoData = new OTDemographicData();

					List<Node> aFieldList = demoDataNode.selectNodes("Field");
					for (/* each */Node aFieldNode : /* in */aFieldList) {

						String fieldIdString = aFieldNode.selectSingleNode(
								"FieldId").getText();
						String fieldValueString = aFieldNode.selectSingleNode(
								"FieldValue").getText();

						Integer fieldId = null;

						try {
							fieldId = new Integer(fieldIdString);
						}
						catch (NumberFormatException nfe) {
							logger.sendEvent(
									"Invalid FieldId sent by Iago.  Check standard err logs.",
									EventType.WARN, new OTQueryTicketXML());
							nfe.printStackTrace();
						}

						OTFieldTO newOTField = new OTFieldTO(fieldId,
								fieldValueString);

						otDemoData.addOTField(newOTField);

					}

					otTktInfoTO.setSeasonPassDemo(otDemoData); // Done
				}

			}

			// WorkShopInfo (ignored)
			// ExternalDiscountClass (ignored)
			// ComplementaryInfo (ignored)

			Node denyMultiEntitlementFunctionsNode = aNode
					.selectSingleNode("DenyMultiEntitlementFunctions");
			if (denyMultiEntitlementFunctionsNode != null) {
				String inText = denyMultiEntitlementFunctionsNode.getText();
				otTktInfoTO.setDenyMultiEntitlementFunctions(inText);
			}

			// Entitlement Account Id 2.16.1 BIEST001
			Node accountIdNode = aNode.selectSingleNode("AccountId");
			if (accountIdNode != null) {
				String inText = accountIdNode.getText();
				otTktInfoTO.setAccountId(inText);
			}

			otTktList.add(otTktInfoTO);
		}

		return;
	}

}
