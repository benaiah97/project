package pvt.disney.dti.gateway.provider.hkd.xml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.hkd.data.HkdOTManageReservationTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTAccountDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTAssociationInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTClientDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTDemographicData;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTDemographicInfo;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTExternalTransactionIDTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTFieldTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTMediaDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTProductTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTReservationDataTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketInfoTO;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTTicketTO.TicketIDType;
import pvt.disney.dti.gateway.util.UtilityXML;

/**
 * This class is responsible for transforming transfer objects into XML nodes and back again for the Omni Ticket Manage Reservation Command. Please note that all of its methods are either package or private level access.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class HkdOTManageReservationXML {

	/**
	 * Adds the manage reservation to the command XML stanza based on the transfer object that's passed in.
	 * 
	 * @param otMngResTO
	 *            The Omni Ticket Manage Reservation Transfer Object.
	 * @param commandStanza
	 *            the command XML stanza that needs the manage reservation section.
	 */
	static void addTxnBodyElement(HkdOTManageReservationTO otMngResTO,
			Element commandStanza) {

		// ManageReservation
		Element mngResStanza = commandStanza.addElement("ManageReservation");

		// WorkRules
		ArrayList<String> tagList = otMngResTO.getTagsList();
		HkdOTCommandXML.addWorkRules(mngResStanza, tagList);

		// SiteNumber
		mngResStanza.addElement("SiteNumber").addText(
				otMngResTO.getSiteNumber().toString());

		// CommandType
		mngResStanza.addElement("CommandType").addText(
				otMngResTO.getCommandType());

		// ReservationIdentifier
		if (otMngResTO.getReservationCode() != null) {
			Element resId = mngResStanza.addElement("ReservationIdentifier");
			resId.addElement("ReservationCode").addText(
					otMngResTO.getReservationCode());
		}
		else if (otMngResTO.getReservationUniqueId() != null) {
			Element resId = mngResStanza.addElement("ReservationIdentifier");
			resId.addElement("ReservationUniqueId").addText(
					otMngResTO.getReservationUniqueId());
		}

		// Product
		addProductStanza(mngResStanza, otMngResTO.getProductList());

		// PaymentInfo
		HkdOTCommandXML.addPaymentInfoStanza(mngResStanza,
				otMngResTO.getPaymentInfoList());

		// SellerId
		if (otMngResTO.getSellerId() != null) mngResStanza.addElement(
				"SellerId").addText(otMngResTO.getSellerId().toString());

		// AssociationInfo
		if (otMngResTO.getAssociationInfo() != null) {

		  HkdOTAssociationInfoTO otAsnInfoTO = otMngResTO.getAssociationInfo();

			Element asnInfoStanza = mngResStanza.addElement("AssociationInfo");

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

			// AllowMemberCreation
			if (otAsnInfoTO.isMemberCreationAllowed()) {
				asnInfoStanza.addElement("AllowMemberCreation").addText("true");
			}

			// DemographicData
			if (otAsnInfoTO.getDemographicData().size() > 0) {

				Element demoDataStanza = asnInfoStanza
						.addElement("DemographicData");
				ArrayList<HkdOTFieldTO> aFieldList = otAsnInfoTO
						.getDemographicData();

				HkdOTCommonXML.addOTDemoAsFieldId(demoDataStanza, aFieldList);

			}
		}

		// ZipCode (omitted)

		// IATA
		if (otMngResTO.getIATA() != null) mngResStanza.addElement("IATA")
				.addText(otMngResTO.getIATA());

		// TransactionNote (2.10)
		if (otMngResTO.getTransactionNote() != null) mngResStanza.addElement(
				"TransactionNote").addText(otMngResTO.getTransactionNote());

		// TaxExemptCode
		if (otMngResTO.getTaxExemptCode() != null) mngResStanza.addElement(
				"TaxExemptCode").addText(otMngResTO.getTaxExemptCode());

		// InTransactionAttribute (omitted)

		// ReservationNote
		if (otMngResTO.getReservationNote() != null) mngResStanza.addElement(
				"ReservationNote").addText(otMngResTO.getReservationNote());

		// Note Details
		if (otMngResTO.getNoteDetailsArray().size() > 0) {
			Element noteDtlStanza = mngResStanza.addElement("NoteDetails");

			ArrayList<String> noteDetails = otMngResTO.getNoteDetailsArray();

			for (int i = 0; i < otMngResTO.getNoteDetailsArray().size(); i++) {
				Element noteLineStanza = noteDtlStanza.addElement("NoteLine");
				String lineIdString = Integer.toString(i + 1);
				noteLineStanza.addElement("LineId").addText(lineIdString);
				noteLineStanza.addElement("Value").addText(noteDetails.get(i));
			}
		}

		// ReservationData
		addReservationDataStanza(mngResStanza, otMngResTO.getReservationData());

		// ClientData
		addClientDataStanza(mngResStanza, otMngResTO.getClientData());

		// BillTo (currently in WDW XSD)
		// RegistrantInfo (currently in WDW XSD)

		// ShowData (omitted)
		// VoidToOriginalMeansOfPaym (omitted)

		// ExplodeComplementary (currently in WDW XSD)
		// AllowVoidUsedTickets (currently in WDW XSD)

		// Entitlement Account
		addEntitlementAccountStanza(otMngResTO, mngResStanza);

		// GenerateEvent (Currently in WDW XSD)
		if (otMngResTO.getGenerateEvent() != null) {
			mngResStanza.addElement("GenerateEvent").addText(
					otMngResTO.getGenerateEvent().toString());
		}
		
		// External Transaction ID (optional as of 2.16.2, JTL)
		if (otMngResTO.getExternalTransactionID() != null) {
		  Element extTxnIdStanza = mngResStanza.addElement("ExternalTransactionID");
		  
		  HkdOTExternalTransactionIDTO otExtTxnIdTO = otMngResTO.getExternalTransactionID();
		  
		  extTxnIdStanza.addElement("ID").addText(otExtTxnIdTO.getId());
		  extTxnIdStanza.addElement("AlreadyEncrypted").addText(otExtTxnIdTO.isAlreadyEncrypted().toString());
		}
		

		return;
	}

	/**
	 * @since 2.9
	 * @param otMngResTO
	 * @param mngResStanza
	 */
	private static void addEntitlementAccountStanza(
	    HkdOTManageReservationTO otMngResTO, Element mngResStanza) {
		// EntitlementAccount
		if (otMngResTO.getEntitlementAccountCreationTypology() != null) {

			Element entitlementStanza = mngResStanza
					.addElement("EntitlementAccount");

			// Required by ATS rule set (JTL)
			entitlementStanza.addElement("AccountCreationTypology").addText(
					otMngResTO.getEntitlementAccountCreationTypology());

			for (HkdOTAccountDataTO account : otMngResTO.getAccountsData()) {

				Element accountStanza = entitlementStanza
						.addElement("AccountData");
				accountStanza.addElement("EntitlementAccountId").addText(
						account.getEntitlementAccountId().toString());

				// Ignore MediaData, only use ExternalReference, or SearchFilter
				if (account.getAccountExternalReferenceType() != null && account
						.getAccountExternalReference() != null) {

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

				// Ticket Demographic Data (as of 2.12 - JTL)
				if (account.getDemoData() != null) {
					Element demoDataStanza = accountStanza
							.addElement("DemographicData");
					ArrayList<HkdOTFieldTO> aFieldList = account.getDemoData()
							.getDemoDataList();

					HkdOTCommonXML.addOTDemoAsFieldId(demoDataStanza, aFieldList);
				}

				// Media Data
				if (account.getMediaDataList() != null) {

					// 2013.06.12 - MWH - logic for NewMediaData
					ArrayList<HkdOTMediaDataTO> aMediaList = account
							.getMediaDataList();

					for (HkdOTMediaDataTO mediaItem : aMediaList) {

						Element media = accountStanza.addElement("MediaData");

						media.addElement("MediaId").addText(
								mediaItem.getMediaId());
						media.addElement("MfrId").addText(mediaItem.getMfrId());
						media.addElement("VisualId").addText(
								mediaItem.getVisualId());

					}
				}

			} // for each AccountData element
		}// if EntitlementAccountCreationTypology

		return;
	}

	/**
	 * Adds the client data stanza to the XML transaction based on the transfer object provided.
	 * 
	 * @param txnStanza
	 *            The transaction stanza that needs the client data section.
	 * @param cliDataTO
	 *            The Omni Ticket Client Data Transfer Object.
	 */
	private static void addClientDataStanza(Element txnStanza,
	    HkdOTClientDataTO cliDataTO) {

		if (cliDataTO == null) return;

		Element cliDataStanza = txnStanza.addElement("ClientData");

		// ClientUniqueId
		cliDataStanza.addElement("ClientUniqueId").addText(
				cliDataTO.getClientUniqueId().toString());

		// ClientType
		cliDataStanza.addElement("ClientType").addText(
				cliDataTO.getClientType());

		// ClientSubtype
		if (cliDataTO.getClientSubtype() != null) cliDataStanza.addElement(
				"ClientSubtype").addText(
				cliDataTO.getClientSubtype().toString());

		// ClientCategory
		if (cliDataTO.getClientCategory() != null) cliDataStanza.addElement(
				"ClientCategory").addText(cliDataTO.getClientCategory());

		// ClientLanguage
		if (cliDataTO.getClientLanguage() != null) {
			DecimalFormat dnf = new DecimalFormat();
			dnf.applyPattern("000");
			String clientLanguage = dnf.format(cliDataTO.getClientLanguage()
					.longValue());
			cliDataStanza.addElement("ClientLanguage").addText(clientLanguage);
		}

		// Billing and Shipping Demographic Data (Old gateway sent all text in upper case, which is
		// emulated here.) JTL
		if ((cliDataTO.getDemographicData() != null) && (cliDataTO
				.getDemographicData().size() > 0)) {

			Element demoStanza = cliDataStanza.addElement("DemographicData");
			ArrayList<HkdOTFieldTO> aFieldList = cliDataTO.getDemographicData();

			HkdOTCommonXML.addOTDemoAsFieldId(demoStanza, aFieldList);

		}

		return;
	}

	/**
	 * Adds the reservation data section to the XML based on the transfer object provided.
	 * 
	 * @param mngResStanza
	 *            the manage reservation stanza that needs the reservation data section.
	 * @param resDataTO
	 *            The Omni Ticket Reservation Data Transfer Object.
	 */
	private static void addReservationDataStanza(Element mngResStanza,
	    HkdOTReservationDataTO resDataTO) {

		if (resDataTO == null) return;

		Element resStanza = mngResStanza.addElement("ReservationData");

		// Printed
		if (resDataTO.getPrinted() != null) resStanza.addElement("Printed")
				.addText(resDataTO.getPrinted().toString());

		// Encoded
		if (resDataTO.getEncoded() != null) resStanza.addElement("Encoded")
				.addText(resDataTO.getEncoded().toString());

		// Validated
		if (resDataTO.getValidated() != null) resStanza.addElement("Validated")
				.addText(resDataTO.getValidated().toString());

		// Deposit (omitted)

		// SalesType
		if (resDataTO.getSalesType() != null) {
			resStanza.addElement("SalesType").addText(
					resDataTO.getSalesType().toString());
		}

		// ResPickupDate
		if (resDataTO.getResPickupDate() != null) {
			resStanza.addElement("ResPickupDate").addText(
					UtilityXML.getNexusDateFromGCal(resDataTO
							.getResPickupDate()));
		}

		// ResPickupArea
		if (resDataTO.getResPickupArea() != null) {
			resStanza.addElement("ResPickupArea").addText(
					resDataTO.getResPickupArea().toString());
		}

		// ResPickupType
		if (resDataTO.getResPickupType() != null) {
			DecimalFormat dnf = new DecimalFormat();
			dnf.applyPattern("000");
			String resPickupType = dnf.format(resDataTO.getResPickupType()
					.longValue());
			resStanza.addElement("ResPickupType").addText(resPickupType);
		}

		// Batch (omitted)
		// BatchInfo (omitted)

		return;
	}

	/**
	 * Adds the product stanza to the XML transaction based on the list of transfer objects passed in.
	 * 
	 * @param txnStanza
	 *            the XML stanza that needs the product list.
	 * @param prodList
	 *            A list of Omni Ticket Product Transfer Objects.
	 */
	private static void addProductStanza(Element txnStanza,
			ArrayList<HkdOTProductTO> prodList) {

		if ((prodList == null) || (prodList.size() == 0)) return;

		for /* each */(HkdOTProductTO aProductTO : /* in */prodList) {

			Element pdtStanza = txnStanza.addElement("Product");

			// Item
			pdtStanza.addElement("Item").addText(
					aProductTO.getItem().toString());

			// ItemType
			pdtStanza.addElement("ItemType").addText(aProductTO.getItemType());

			// ItemIdentifier
			Element itemIdStanza = pdtStanza.addElement("ItemIdentifier");
			if (aProductTO.getItemNumCode() != null) itemIdStanza.addElement(
					"ItemNumCode").addText(
					aProductTO.getItemNumCode().toString());
			else itemIdStanza.addElement("ItemAlphaCode").addText(
					aProductTO.getItemAlphaCode());

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

			// Price
			if (aProductTO.getPrice() != null) {
				pdtStanza.addElement("Price").addText(
						aProductTO.getPrice().toString());
			}

			// TicketNote (omitted)
			// TicketAttribute (omitted)
			// GroupTicket (omitted)
			// BiometricLevel (omitted)
			// GroupCode (omitted)
			// PriceTableIndex (omitted)

			// Ticket DemographicInfo (As of 2.9, JTL)
			HkdOTDemographicInfo otDemoInfo = aProductTO.getDemographicInfo();
			if ((otDemoInfo != null) && (otDemoInfo.getDemoDataList().size() > 0)) {

				Element demoInfoStanza = pdtStanza
						.addElement("DemographicInfo");

				ArrayList<HkdOTDemographicData> otDemoDataList = otDemoInfo
						.getDemoDataList();

				for /* each */(HkdOTDemographicData oTDemoData : /* in */otDemoDataList) {

					Element demoDataStanza = demoInfoStanza
							.addElement("DemographicData");

					ArrayList<HkdOTFieldTO> oTFieldList = oTDemoData
							.getDemoDataList();

					HkdOTCommonXML.addOTDemoAsFieldId(demoDataStanza, oTFieldList);

				} // for each Demo Data in Demo Data List

			}

			// Workshop Info (omitted)
			// PrintAtTurnstile (omitted)

			// EntitlementAccountId
			for (String accountId : aProductTO.getEntitlementAccountId()) {
				pdtStanza.addElement("EntitlementAccountId").addText(accountId);
			}
		}

		return;
	}

	/**
	 * When provided with the manage reservation XML section of a parsed XML, fills out the transfer object.
	 * 
	 * @param manageResNode
	 *            The parsed section of the XML containing the
	 * @return The Omni Ticket Manage Reservation Transfer Object.
	 * @throws DTIException
	 *             for any parsing errors or missing required fields.
	 */
	@SuppressWarnings("unchecked")
	static HkdOTManageReservationTO getTO(Node manageResNode) throws DTIException {

	  HkdOTManageReservationTO otMngResTO = new HkdOTManageReservationTO();

		// Error
		Node errorNode = manageResNode.selectSingleNode("Error");
		if (errorNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ManageReservation,Error element.");
		else otMngResTO.setError(HkdOTCommandXML.setOTErrorTO(errorNode));

		// CommandType
		Node commandType = manageResNode.selectSingleNode("CommandType");
		if (commandType != null) {
			otMngResTO.setCommandType(commandType.getText());
		}

		// TicketInfo List
		List<Node> ticketInfoNodeList = manageResNode.selectNodes("TicketInfo");
		if ((ticketInfoNodeList != null) && (ticketInfoNodeList.size() > 0)) setOTTicketInfoTOList(
				otMngResTO.getTicketInfoList(), ticketInfoNodeList);

		// ProductInfo List
		List<Node> productInfoNodeList = manageResNode
				.selectNodes("ProductInfo");
		if (productInfoNodeList != null) setOTProductTOList(
				otMngResTO.getProductList(), productInfoNodeList);

		// PaymentInfo List
		List<Node> paymentInfoNodeList = manageResNode
				.selectNodes("PaymentInfo");
		if (paymentInfoNodeList != null) HkdOTCommandXML.setOTPaymentTOList(
				otMngResTO.getPaymentInfoList(), paymentInfoNodeList);

		// TransactionDSSN
		Node tranDssnNode = manageResNode.selectSingleNode("TransactionDSSN");
		if (tranDssnNode != null) otMngResTO.setTransactionDSSN(HkdOTCommandXML
				.setOTTransactionDSSNTO(tranDssnNode));

		// TransactionCOD
		Node tranCODNode = manageResNode.selectSingleNode("TransactionCOD");
		if (tranCODNode != null) otMngResTO.setTransactionCOD(tranCODNode
				.getText());

		// TransactionTime
		Node tranTimeNode = manageResNode.selectSingleNode("TransactionTime");
		if (tranTimeNode != null) otMngResTO.setTransactionTime(tranTimeNode
				.getText());

		// TotalAmount
		Node totalAmountNode = manageResNode.selectSingleNode("TotalAmount");
		if (totalAmountNode != null) {
			BigDecimal totalAmt = new BigDecimal(totalAmountNode.getText());
			otMngResTO.setTotalAmount(totalAmt);
		}

		// TotalTax
		Node totalTaxNode = manageResNode.selectSingleNode("TotalTax");
		if (totalTaxNode != null) {
			BigDecimal totalTax = new BigDecimal(totalTaxNode.getText());
			otMngResTO.setTotalTax(totalTax);
		}

		// TaxExempt
		Node taxExemptNode = manageResNode.selectSingleNode("TaxExempt");
		if (taxExemptNode != null) {
			String taxExemptValue = taxExemptNode.getText();
			if (taxExemptValue.compareTo("0") == 0) otMngResTO
					.setTaxExempt(new Boolean(false));
			else otMngResTO.setTaxExempt(new Boolean(true));
		}

		// ReservationCode
		Node reservationCodeNode = manageResNode
				.selectSingleNode("ReservationCode");
		if (reservationCodeNode != null) otMngResTO
				.setReservationCode(reservationCodeNode.getText());

		// ReservationId
		Node reservationIdNode = manageResNode
				.selectSingleNode("ReservationId");
		if (reservationIdNode != null) {
			String inText = reservationIdNode.getText();
			otMngResTO.setReservationId(new Integer(inText));
		}

		// ReservationNote
		Node reservationNoteNode = manageResNode
				.selectSingleNode("ReservationNote");
		if (reservationNoteNode != null) otMngResTO
				.setReservationNote(reservationNoteNode.getText());

		// ReservationData
		Node reservationDataNode = manageResNode
				.selectSingleNode("ReservationData");
		if (reservationDataNode != null) otMngResTO
				.setReservationData(setOTReservationDataTO(reservationDataNode));

		// ClientData
		Node clientDataNode = manageResNode.selectSingleNode("ClientData");
		if (clientDataNode != null) otMngResTO
				.setClientData(setOTClientDataTO(clientDataNode));

		return otMngResTO;
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
			ArrayList<HkdOTTicketInfoTO> otTktList, List<Node> ticketInfoNodeList) throws DTIException {

		List<Node> ticketList = (List<Node>) ticketInfoNodeList;

		for /* each */(Node aNode : /* in */ticketList) {

		  HkdOTTicketInfoTO otTktTO = new HkdOTTicketInfoTO();

			// Item
			Node itemNode = aNode.selectSingleNode("Item");
			if (itemNode == null) throw new DTIException(
					HkdOTManageReservationXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfo,Item element.");
			else {
				String inText = itemNode.getText();
				otTktTO.setItem(new BigInteger(inText));
			}

			// ItemStatus
			Node itemStatusNode = aNode.selectSingleNode("ItemStatus");
			if (itemStatusNode == null) throw new DTIException(
					HkdOTManageReservationXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a TicketInfo,ItemStatus element.");
			else {
				String inText = itemStatusNode.getText();
				otTktTO.setItemStatus(new Integer(inText));
			}

			// ItemType (ignored)
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

			// Ticket
			Node ticketNode = aNode.selectSingleNode("Ticket");
			if (ticketNode != null) otTktTO.setTicket(HkdOTCommandXML
					.setOTTicketTO(ticketNode));

			// Validity
			HkdOTCommandXML.setOTTicketInfoValidity(aNode, otTktTO);

			// Entitlement Account Id 2.16.1 BIEST001
			Node accountIdNode = aNode.selectSingleNode("AccountId");
			if (accountIdNode != null) {
				String inText = accountIdNode.getText();
				otTktTO.setAccountId(inText);
			}

			// TicketFlag (ignored)
			// TicketAttribute (ignored)
			// TktNote (ignored)
			// BiometricLevel (ignored)
			// ShowData (ignored)
			// SeatData (ignored)
			// Layout (ignored)

			otTktList.add(otTktTO);
		}

		return;
	}

	/**
	 * Sets the product transfer object list based upon the parsed XML provided.
	 * 
	 * @param otPdtList
	 *            The list of Omni Ticket Product Transfer Objects.
	 * @param pdtNodeList
	 *            The parsed XML containing the products.
	 * @throws DTIException
	 *             for any parsing errors or missing required fields.
	 */
	private static void setOTProductTOList(ArrayList<HkdOTProductTO> otPdtList,
			List<Node> pdtNodeList) throws DTIException {

		List<Node> pdtList = (List<Node>) pdtNodeList;

		for /* each */(Node aNode : /* in */pdtList) {

		  HkdOTProductTO otPdtTO = new HkdOTProductTO();

			// Item
			Node itemNode = aNode.selectSingleNode("Item");
			if (itemNode == null) throw new DTIException(
					HkdOTManageReservationXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a ProductInfo,Item element.");
			String inString = itemNode.getText();
			otPdtTO.setItem(new BigInteger(inString));

			// TicketType
			Node tktTypeNode = aNode.selectSingleNode("TicketType");
			if (tktTypeNode == null) throw new DTIException(
					HkdOTManageReservationXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a ProductInfo,TicketType element.");
			inString = tktTypeNode.getText();
			otPdtTO.setTicketType(new BigInteger(inString));

			// Price
			Node priceNode = aNode.selectSingleNode("Price");
			if (priceNode != null) {
				inString = priceNode.getText();
				otPdtTO.setPrice(new BigDecimal(inString));
			}

			// Qty
			Node qtyNode = aNode.selectSingleNode("Qty");
			if (qtyNode != null) {
				inString = qtyNode.getText();
				otPdtTO.setQuantity(new BigInteger(inString));
			}

			// Tax
			Node taxNode = aNode.selectSingleNode("Tax");
			if (taxNode != null) {
				inString = taxNode.getText();
				otPdtTO.setTax(new BigDecimal(inString));
			}

			// TicketName
			Node ticketNameNode = aNode.selectSingleNode("TicketName");
			if (ticketNameNode != null) otPdtTO.setTicketName(ticketNameNode
					.getText());

			// Description
			Node descNode = aNode.selectSingleNode("Description");
			if (descNode != null) otPdtTO.setDescription(descNode.getText());

			otPdtList.add(otPdtTO);
		}

		return;
	}

	/**
	 * Sets the reservation data transfer object based upon the parsed XML provided.
	 * 
	 * @param resDataNode
	 *            the parsed XML containing the reservation data section.
	 * @return The Omni Ticket Reservation Data Transfer Object.
	 * @throws DTIException
	 *             for any parsing errors or missing required fields.
	 */
	private static HkdOTReservationDataTO setOTReservationDataTO(Node resDataNode) throws DTIException {

	  HkdOTReservationDataTO otResData = new HkdOTReservationDataTO();

		// Paid
		Node paidNode = resDataNode.selectSingleNode("Paid");
		if (paidNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ReservationData,Paid element.");
		String inString = paidNode.getText();
		otResData.setPaid(inString);

		// Printed
		Node printedNode = resDataNode.selectSingleNode("Printed");
		if (printedNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ReservationData,Printed element.");
		inString = printedNode.getText();
		otResData.setPrinted(inString);

		// Encoded
		Node encodedNode = resDataNode.selectSingleNode("Encoded");
		if (encodedNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ReservationData,Encoded element.");
		inString = encodedNode.getText();
		otResData.setEncoded(inString);

		// Validated
		Node validatedNode = resDataNode.selectSingleNode("Validated");
		if (validatedNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ReservationData,Validated element.");
		inString = validatedNode.getText();
		otResData.setValidated(inString);

		// DepositAmount
		Node depositAmountNode = resDataNode.selectSingleNode("DepositAmount");
		if (depositAmountNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ReservationData,DepositAmount element.");
		inString = depositAmountNode.getText();
		otResData.setDepositAmount(new BigDecimal(inString));

		// SalesType
		Node salesTypeNode = resDataNode.selectSingleNode("SalesType");
		if (salesTypeNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ReservationData,SalesType element.");
		inString = salesTypeNode.getText();
		otResData.setSalesType(Integer.decode(inString));

		// ResStatus
		Node resStatusNode = resDataNode.selectSingleNode("ResStatus");
		if (resStatusNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ReservationData,ResStatus element.");
		otResData.setResStatus(resStatusNode.getText());

		// ResPickupDate
		Node resPickupDateNode = resDataNode.selectSingleNode("ResPickupDate");
		if (resPickupDateNode != null) {
			try {
				otResData.setResPickupDate(resPickupDateNode.getText());
			}
			catch (ParseException pe) {
				throw new DTIException(
						HkdOTManageReservationXML.class,
						DTIErrorCode.TP_INTERFACE_FAILURE,
						"Ticket provider returned XML with an invalid ReservationData,ResPickupDate element.");
			}
		}

		// ResPickupArea
		Node resPickupAreaNode = resDataNode.selectSingleNode("ResPickupArea");
		if (resPickupAreaNode != null) {
			inString = resPickupAreaNode.getText();
			otResData.setResPickupArea(Integer.decode(inString));
		}

		// ResPickupType
		Node resPickupTypeNode = resDataNode.selectSingleNode("ResPickupType");
		if (resPickupTypeNode != null) {
			inString = resPickupTypeNode.getText();
			otResData.setResPickupType(Integer.decode(inString));
		}

		return otResData;
	}

	/**
	 * Sets the transfer object for client data based on the parsed XML provided.
	 * 
	 * @param cliDataNode
	 *            the parsed section of the XML containing the client data node.
	 * @return The Omni Ticket Client Data Transfer Object.
	 * @throws DTIException
	 *             for any missing fields or parsing errors.
	 */
	private static HkdOTClientDataTO setOTClientDataTO(Node cliDataNode) throws DTIException {

	  HkdOTClientDataTO otClientData = new HkdOTClientDataTO();

		// ClientUniqueId
		Node clientUniIdNode = cliDataNode.selectSingleNode("ClientUniqueId");
		if (clientUniIdNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ClientData,ClientUniqueId element.");
		String inString = clientUniIdNode.getText();
		otClientData.setClientUniqueId(Integer.parseInt(inString));

		// ClientType
		Node clientTypeNode = cliDataNode.selectSingleNode("ClientType");
		if (clientTypeNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ClientData,ClientType element.");
		otClientData.setClientType(clientTypeNode.getText());

		// ClientSubtype
		Node clientSubTypeNode = cliDataNode.selectSingleNode("ClientSubtype");
		if (clientSubTypeNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ClientData,ClientSubtype element.");
		inString = clientSubTypeNode.getText();
		otClientData.setClientSubtype(Integer.parseInt(inString));

		// ClientCategory
		Node clientCatNode = cliDataNode.selectSingleNode("ClientCategory");
		if (clientCatNode != null) {
			otClientData.setClientCategory(clientCatNode.getText());
		}

		// ClientLanguage
		Node clientLangNode = cliDataNode.selectSingleNode("ClientLanguage");
		if (clientLangNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ClientData,ClientLanguage element.");
		inString = clientLangNode.getText();
		otClientData.setClientLanguage(Integer.parseInt(inString));

		// DemographicData
		Node demoDataNode = cliDataNode.selectSingleNode("DemographicData");
		if (demoDataNode == null) throw new DTIException(
				HkdOTManageReservationXML.class,
				DTIErrorCode.TP_INTERFACE_FAILURE,
				"Ticket provider returned XML without a ClientData,DemographicData element.");
		setDemoFieldList(otClientData.getDemographicData(), demoDataNode);

		return otClientData;
	}

	/**
	 * Sets the demographic field list in the transfer object based upon the parsed XML that is provided.
	 * 
	 * @param demoFieldList
	 *            the list of Omni Ticket Field Transfer Objects.
	 * @param demoDataNode
	 *            the parsed section of the XML containing the demographic data.
	 * @throws DTIException
	 *             for any missing required fields or parsing exceptions.
	 */
	@SuppressWarnings("unchecked")
	private static void setDemoFieldList(ArrayList<HkdOTFieldTO> otDemoFieldList,
			Node demoDataNode) throws DTIException {

		List<Node> fieldNodeList = demoDataNode.selectNodes("Field");

		for /* each */(Node aNode : /* in */fieldNodeList) {

			// FieldID
			Node fieldIdNode = aNode.selectSingleNode("FieldId");
			if (fieldIdNode == null) throw new DTIException(
					HkdOTManageReservationXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a Field,FieldId element.");
			String inString = fieldIdNode.getText();
			Integer fieldId = Integer.parseInt(inString);

			// FieldType
			Node fieldTypeNode = aNode.selectSingleNode("FieldType");
			if (fieldTypeNode == null) throw new DTIException(
					HkdOTManageReservationXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a Field,FieldType element.");
			inString = fieldTypeNode.getText();
			Integer fieldType = Integer.parseInt(inString);

			// FieldValue
			Node fieldValueNode = aNode.selectSingleNode("FieldValue");
			if (fieldValueNode == null) throw new DTIException(
					HkdOTManageReservationXML.class,
					DTIErrorCode.TP_INTERFACE_FAILURE,
					"Ticket provider returned XML without a Field,FieldValue element.");
			String fieldValue = fieldValueNode.getText();

			HkdOTFieldTO otField = new HkdOTFieldTO(fieldId, fieldType, fieldValue);
			otDemoFieldList.add(otField);

		}

		return;
	}

}