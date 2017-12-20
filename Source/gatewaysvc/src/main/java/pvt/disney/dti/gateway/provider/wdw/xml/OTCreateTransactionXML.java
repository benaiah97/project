package pvt.disney.dti.gateway.provider.wdw.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTCreateTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTAccountDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTAssociationInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicInfo;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMediaDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTProductTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO.TicketIDType;
import pvt.disney.dti.gateway.util.UtilityXML;

/**
 * This class is responsible for transforming transfer objects into XML nodes and back again for the Omni Ticket Create Transaction Command. Please note that all of its methods are either package or private level access.
 * 
 * @author lewit019
 * 
 */
public class OTCreateTransactionXML {

	/**
	 * Adds the transaction body of create ticket to the command stanza when provided with the appropriate transfer object.
	 * 
	 * @param otCrtTxnTO
	 *            The Omni Ticket Create Transaction Transfer Object.
	 * @param commandStanza
	 *            The command stanza needing the create transaction stanza.
	 */
	static void addTxnBodyElement(OTCreateTransactionTO otCrtTxnTO,
			Element commandStanza) {

		// CreateTransaction
		Element crtTxnStanza = commandStanza.addElement("CreateTransaction");

		// WorkRules
		ArrayList<String> tagList = otCrtTxnTO.getTagsList();
		OTCommandXML.addWorkRules(crtTxnStanza, tagList);

		// SiteNumber
		crtTxnStanza.addElement("SiteNumber").addText(
				otCrtTxnTO.getSiteNumber().toString());

		// Product
		addProductStanza(crtTxnStanza, otCrtTxnTO.getProductList());

		// AssociationInfo (since 2.9)
		if (otCrtTxnTO.getAssociationInfo() != null) {

			OTAssociationInfoTO otAsnInfoTO = otCrtTxnTO.getAssociationInfo();

			Element asnInfoStanza = crtTxnStanza.addElement("AssociationInfo");

			asnInfoStanza.addElement("AssociationId").addText(
					otAsnInfoTO.getAssociationId().toString());

			// MemberInfo
			Element memberInfoStanza = asnInfoStanza.addElement("MemberInfo");

			if (otAsnInfoTO.getMemberId() != null) {
				memberInfoStanza.addElement("MemberId").addText(
						otAsnInfoTO.getMemberId().toString());
			}
			else {
				memberInfoStanza.addElement("MemberField").addText(
						otAsnInfoTO.getMemberField());
			}

		}

		// PaymentInfo
		OTCommandXML.addPaymentInfoStanza(crtTxnStanza,
				otCrtTxnTO.getPaymentInfoList());

		// TransactionNote (2.10)
		if (otCrtTxnTO.getTransactionNote() != null) crtTxnStanza.addElement(
				"TransactionNote").addText(otCrtTxnTO.getTransactionNote());

		// InTransactionAttibute
		ArrayList<OTInTransactionAttributeTO> inTxnAttrList = otCrtTxnTO
				.getInTxnAttrList();
		OTCommandXML.addInTransactionAttributeStanzas(crtTxnStanza,
				inTxnAttrList);

		// Entitlement Account
		addEntitlementAccountStanza(otCrtTxnTO, crtTxnStanza);

		// GenerateEvent
		if (otCrtTxnTO.getGenerateEvent() != null) crtTxnStanza.addElement(
				"GenerateEvent").addText(
				otCrtTxnTO.getGenerateEvent().toString());

		return;
	}

	/**
	 * @since 2.9
	 * @param otCrtTxnTO
	 * @param crtTxnStanza
	 */
	private static void addEntitlementAccountStanza(
			OTCreateTransactionTO otCrtTxnTO, Element crtTxnStanza) {

		// EntitlementAccount
		if (otCrtTxnTO.getEntitlementAccountCreationTypology() != null) {

			Element entitlementStanza = crtTxnStanza
					.addElement("EntitlementAccount");

			// Required by ATS rule set (JTL)
			entitlementStanza.addElement("AccountCreationTypology").addText(
					otCrtTxnTO.getEntitlementAccountCreationTypology());

			for (OTAccountDataTO account : otCrtTxnTO.getAccountsData()) {

				Element accountStanza = entitlementStanza
						.addElement("AccountData");
				accountStanza.addElement("EntitlementAccountId").addText(
						account.getEntitlementAccountId().toString());

				// Ignore MediaData, only use ExternalReference, or SearchFilter
				if ((account.getAccountExternalReferenceType() != null) && (account
						.getAccountExternalReference() != null)) {

					Element external = accountStanza
							.addElement("ExternalReference");
					external.addElement("AccountExternalReference").addText(
							account.getAccountExternalReference());
					external.addElement("AccountExternalReferenceType")
							.addText(
									account.getAccountExternalReferenceType()
											.toString());

				}
				else {

					Element search = accountStanza.addElement("SearchFilter");

					if (account.getSearchExistingMediaId() != null) {
						search.addElement("ExistingMediaId").addText(
								account.getSearchExistingMediaId());
					}
					else if (account.getSearchAccountId() != null) {
						search.addElement("AccountId").addText(
								account.getSearchAccountId().toString());
					}
					else if (account.getTicketSearchMode() != null) {
						Element ticket = search.addElement("TicketSearchMode");
						ArrayList<TicketIDType> type = account
								.getTicketSearchMode().getTktIdentityTypes();
						switch (type.get(0)) {

						case BARCODE:
							ticket.addElement("BarCode").addText(
									account.getTicketSearchMode().getBarCode());
							break;

						case TDSSN:
							Element dssn = ticket.addElement("TDSSN");
							dssn.addElement("Site").addText(
									account.getTicketSearchMode()
											.getTdssnSite());
							dssn.addElement("Station").addText(
									account.getTicketSearchMode()
											.getTdssnStation());
							dssn.addElement("Date").addText(
									UtilityXML.getNexusDateFromGCal(account
											.getTicketSearchMode()
											.getTdssnDate()));
							dssn.addElement("TicketId").addText(
									account.getTicketSearchMode()
											.getTdssnTicketId());
							break;

						case TCOD:
							ticket.addElement("TCOD").addText(
									account.getTicketSearchMode().getTCOD());
							break;

						case MAGCODE:
							ticket.addElement("MagCode")
									.addText(
											account.getTicketSearchMode()
													.getMagTrack());
							break;

						case EXTERNALTICKETCODE:
							ticket.addElement("ExternalTicketCode").addText(
									account.getTicketSearchMode()
											.getExternalTicketCode());
							break;

						default:
							break;
						}
					}
				} // External or SearchFilter

				if (account.getMediaDataList() != null) {

					// 2013.06.12 - MWH - logic for NewMediaData
					ArrayList<OTMediaDataTO> aMediaList = account
							.getMediaDataList();

					for (/* each */OTMediaDataTO mediaItem : /* in */aMediaList) {

						Element media = accountStanza.addElement("MediaData");

						media.addElement("MediaId").addText(
								mediaItem.getMediaId());
						media.addElement("MfrId").addText(mediaItem.getMfrId());
						media.addElement("VisualId").addText(
								mediaItem.getVisualId());

					}
				}

			} // for each AccountData element

		} // if EntitlementAccountCreationTypology

		return;
	}

	/**
	 * Adds the product stanza to the transaction stanza.
	 * 
	 * @param txnStanza
	 *            the DOM4J element the product stanza is to be added to.
	 * @param prodList
	 *            the list of Omni Ticket Product Transfer Objects.
	 */
	private static void addProductStanza(Element txnStanza,
			ArrayList<OTProductTO> prodList) {

		for /* each */(OTProductTO aProductTO : /* in */prodList) {

			Element pdtStanza = txnStanza.addElement("Product");

			// Item
			pdtStanza.addElement("Item").addText(
					aProductTO.getItem().toString());

			// TicketType
			pdtStanza.addElement("TicketType").addText(
					aProductTO.getTicketType().toString());

			// Quantity
			pdtStanza.addElement("Quantity").addText(
					aProductTO.getQuantity().toString());

			// Validity
			if ((aProductTO.getValidity_StartDate() != null) || (aProductTO
					.getValidity_EndDate() != null)) {

				Element vldStanza = pdtStanza.addElement("Validity");

				if (aProductTO.getValidity_StartDate() != null) {
					String startDate = UtilityXML
							.getNexusDateFromGCal(aProductTO
									.getValidity_StartDate());
					vldStanza.addElement("StartDate").addText(startDate);
				}

				if (aProductTO.getValidity_EndDate() != null) {
					String endDate = UtilityXML.getNexusDateFromGCal(aProductTO
							.getValidity_EndDate());
					vldStanza.addElement("EndDate").addText(endDate);
				}
			}

			// External Code Token for variably price products
			if(null != aProductTO.getProdPriceToken()){
			   pdtStanza.addElement("QuoteToken").addText(
	                  aProductTO.getProdPriceToken());
			}
			
			// Price
			if (aProductTO.getPrice() != null) {
				pdtStanza.addElement("Price").addText(
						aProductTO.getPrice().toString());
			}

			// TicketNote
			if (aProductTO.getTicketNote() != null) {
				pdtStanza.addElement("TicketNote").addText(
						aProductTO.getTicketNote());
			}

			// TicketAttribute
			if (aProductTO.getTicketAttribute() != null) {
				pdtStanza.addElement("TicketAttribute").addText(
						aProductTO.getTicketAttribute().toString());
			}

			// Ticket demographicInfo (As of 2.9)
			OTDemographicInfo otDemoInfo = aProductTO.getDemographicInfo();
			if ((otDemoInfo != null) && (otDemoInfo.getDemoDataList().size() > 0)) {

				Element demoInfoStanza = pdtStanza
						.addElement("DemographicInfo");

				ArrayList<OTDemographicData> otDemoDataList = otDemoInfo
						.getDemoDataList();

				for /* each */(OTDemographicData oTDemoData : /* in */otDemoDataList) {

					Element demoDataStanza = demoInfoStanza
							.addElement("DemographicData");

					ArrayList<OTFieldTO> oTFieldList = oTDemoData
							.getDemoDataList();

					OTCommonXML.addOTDemoAsFieldId(demoDataStanza, oTFieldList);

				} // for each Demo Data in Demo Data List

			}

			// EntitlementAccountId
			for (String accountId : aProductTO.getEntitlementAccountId()) {
				pdtStanza.addElement("EntitlementAccountId").addText(accountId);
			}

		}

		return;
	}

	/**
	 * When provided with the parsed XML, creates the transfer object for the create transaction.
	 * 
	 * @param createTxnResStanza
	 *            the parsed XML containing the create transaction response.
	 * @return The Omni Ticket Create Transaction Transfer Object.
	 * @throws DTIException
	 *             for any missing required fields or other parsing errors.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	static OTCreateTransactionTO getTO(Node createTxnNode) throws DTIException {

		OTCreateTransactionTO otCrtTxnTO = new OTCreateTransactionTO();

		// Set error object
		OTErrorTO otErrorTO = OTCommandXML.getErrorTO(createTxnNode);
		otCrtTxnTO.setError(otErrorTO);

		// TicketInfo List

		List ticketInfoNodeList = createTxnNode.selectNodes("TicketInfo");
		if ((ticketInfoNodeList != null) && (ticketInfoNodeList.size() > 0)) {
			setOTTicketInfoTOList(otCrtTxnTO.getTktInfoList(),
					ticketInfoNodeList);
		}

		// ProductInfo (ignored)

		// PaymentInfo List
		List<Node> paymentInfoNodeList = createTxnNode
				.selectNodes("PaymentInfo");
		if (paymentInfoNodeList != null) {
			OTCommandXML.setOTPaymentTOList(otCrtTxnTO.getPaymentInfoList(),
					paymentInfoNodeList);
		}

		// TransactionDSSN
		Node tranDssnNode = createTxnNode.selectSingleNode("TransactionDSSN");
		if (tranDssnNode != null) {
			otCrtTxnTO.setTransactionDSSN(OTCommandXML
					.setOTTransactionDSSNTO(tranDssnNode));
		}

		// TransactionCOD
		Node tranCODNode = createTxnNode.selectSingleNode("TransactionCOD");
		if (tranCODNode != null) otCrtTxnTO.setTransactionCOD(tranCODNode
				.getText());

		// TotalAmount (ignored)
		// TotalTax (ignored)
		// TaxExempt (ignored)
		// ReservedSeatsData (ignored)

		return otCrtTxnTO;
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
	@SuppressWarnings("rawtypes")
	private static void setOTTicketInfoTOList(
			ArrayList<OTTicketInfoTO> otTktList, List ticketInfoNodeList) throws DTIException {

		@SuppressWarnings("unchecked")
		List<Node> ticketList = ticketInfoNodeList;

		for /* each */(Node aNode : /* in */ticketList) {

			OTTicketInfoTO otTktTO = new OTTicketInfoTO();

			// Item
			Node itemNode = aNode.selectSingleNode("Item");
			if (itemNode == null) throw new DTIException(
					OTCreateTransactionXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfo,Item element.");
			else {
				String inText = itemNode.getText();
				otTktTO.setItem(new BigInteger(inText));
			}

			// ItemStatus
			Node itemStatusNode = aNode.selectSingleNode("ItemStatus");
			if (itemStatusNode == null) throw new DTIException(
					OTCreateTransactionXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfo,ItemStatus element.");
			else {
				String inText = itemStatusNode.getText();
				otTktTO.setItemStatus(Integer.decode(inText));
			}

			// TicketType
			Node ticketTypeNode = aNode.selectSingleNode("TicketType");
			if (ticketTypeNode == null) throw new DTIException(
					OTCreateTransactionXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfo,TicketType element.");
			else {
				String inText = ticketTypeNode.getText();
				otTktTO.setTicketType(new BigInteger(inText));
			}

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

			// Ticket
			Node ticketNode = aNode.selectSingleNode("Ticket");
			if (ticketNode != null) {
				otTktTO.setTicket(OTCommandXML.setOTTicketTO(ticketNode));
			}

			// TicketNote
			Node ticketNoteNode = aNode.selectSingleNode("TicketNote");
			if (ticketNoteNode != null) {
				otTktTO.setTicketNote(ticketNoteNode.getText());
			}

			// Validity
			OTCommandXML.setOTTicketInfoValidity(aNode, otTktTO);

			// TktNote
			Node tktNoteNode = aNode.selectSingleNode("TktNote");
			if (tktNoteNode != null) {
				otTktTO.setTicketNote(tktNoteNode.getText());
			}

			otTktList.add(otTktTO);
		}

		return;

	}

}
