package pvt.disney.dti.gateway.service.dtixml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.ReservationResponseTO;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentDemographicsTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO.PaymentType;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.SpecifiedAccountTO;
import pvt.disney.dti.gateway.data.common.TicketIdTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.VoucherTO;
import pvt.disney.dti.gateway.request.xsd.ExternalReferenceType;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.ClientData.ClientGroupValidity;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.EntitlementAccount.SpecifiedAccount;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.EntitlementAccount.SpecifiedAccount.AccountDemoData;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.EntitlementAccount.SpecifiedAccount.ExistingAccount;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.EntitlementAccount.SpecifiedAccount.ExistingAccount.TktID;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.EntitlementAccount.SpecifiedAccount.NewMediaData;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Ticket.ProdDemoData;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Ticket.ProdDemoData.TktDemoData;
import pvt.disney.dti.gateway.request.xsd.ReservationRequest.Ticket.TktAssignment;
import pvt.disney.dti.gateway.response.xsd.ReservationResponse;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * 
 * @author lewit019
 * 
 */
public abstract class ReservationXML {

	private static final Class<ReservationXML> THISOBJECT = ReservationXML.class;
	private static final String CREDIT_CARD = "CREDIT CARD";
	private static final String DISNEY_GIFT_CARD = "DISNEY GIFT CARD";
	private static final String ORGANIZATIONAL_CHECK = "ORGANIZATIONAL CHECK";
	private static final String CASHIERS_CHECK = "CASHIER'S CHECK";
	private static final String MONEY_ORDER = "MONEY ORDER";
	private static final String WIRE_TRANSFER = "WIRE TRANSFER";
	private static final String PAY_AT_PICKUP = "PAY AT PICKUP";
	private static final String MASTER_ACCOUNT = "MASTER ACCOUNT";
	private static final String SAP_JOB_NUMBER = "SAP JOB NUMBER";
	private static final String TICKET_EXCHANGE = "TICKET EXCHANGE";
	public static final String ACCOUNT_PER_TICKET = "AccountPerTicket";
	public static final String ACCOUNT_PER_ORDER = "AccountPerOrder";
	private static final EventLogger logger = EventLogger
			.getLogger(ReservationXML.class.getCanonicalName());

	/**
	 * Gets the request transfer object from the parsed JAXB structure.
	 * 
	 * @param resReq
	 *            the Reservation Request from JAXB.
	 * 
	 * @return the properly formatted request transfer object.
	 * 
	 * @throws JAXBException
	 *             for any parsing failure.
	 */
	public static ReservationRequestTO getTO(ReservationRequest resReq) throws JAXBException {

		ReservationRequestTO resReqTO = new ReservationRequestTO();

		// Request Type
		resReqTO.setRequestType(resReq.getRequestType());

		// Ticket List
		List<ReservationRequest.Ticket> ticketList = resReq.getTicket();
		ArrayList<TicketTO> ticketListTO = new ArrayList<TicketTO>();
		for /* each */(ReservationRequest.Ticket aTicket : /* in */ticketList) {
			setTOTicketList(ticketListTO, aTicket);
		}
		resReqTO.setTktList(ticketListTO);

		// APPassInfo
		if (resReq.getAPPassInfo() != null) resReqTO.setAPPassInfo(resReq
				.getAPPassInfo());

		// Payment
		List<ReservationRequest.Payment> paymentList = resReq.getPayment();
		ArrayList<PaymentTO> paymentListTO = new ArrayList<PaymentTO>();
		for /* each */(ReservationRequest.Payment aPayment : /* in */paymentList) {
			setTOPaymentList(paymentListTO, aPayment);
		}
		resReqTO.setPaymentList(paymentListTO);

		// Eligibility
		if (resReq.getEligibility() != null) {
			ReservationRequest.Eligibility elig = resReq.getEligibility();
			resReqTO.setEligibilityGroup(elig.getGroup());
			resReqTO.setEligibilityMember(elig.getMember());
		}

		// Reservation
		if (resReq.getReservation() != null) {
			ReservationRequest.Reservation resv = resReq.getReservation();
			ReservationTO resvTO = new ReservationTO();
			List<JAXBElement<?>> aReservationList = resv
					.getResCodeOrResNumberOrResCreateDate();
			for /* each */(JAXBElement<?> aReservationEntry : /* in */aReservationList) {
				setTOReservation(resvTO, aReservationEntry);
			}
			resReqTO.setReservation(resvTO);
		}

		// ClientData
		if (resReq.getClientData() != null) {
			ReservationRequest.ClientData clientData = resReq.getClientData();
			ClientDataTO clientDataTO = new ClientDataTO();
			setTOClientData(clientData, clientDataTO);
			resReqTO.setClientData(clientDataTO);

		}

		// Agency
		if (resReq.getAgency() != null) {
			ReservationRequest.Agency agency = resReq.getAgency();
			AgencyTO agencyTO = new AgencyTO();

			List<JAXBElement<String>> anAgencyList = agency.getIATAOrAgent();

			for /* each */(JAXBElement<String> anAgencyEntry : /* in */anAgencyList) {
				QName fieldName = anAgencyEntry.getName();
				if (fieldName.getLocalPart().equalsIgnoreCase("IATA")) {
					agencyTO.setIATA(anAgencyEntry.getValue());
				}
				else if (fieldName.getLocalPart().equalsIgnoreCase("Agent")) {
					agencyTO.setAgent(anAgencyEntry.getValue());
				}
			}
			resReqTO.setAgency(agencyTO);
		}

		// TaxExemptCode
		if (resReq.getTaxExemptCode() != null) {
			resReqTO.setTaxExemptCode(resReq.getTaxExemptCode());
		}

		// Note
		if (resReq.getNote() != null) {
			ArrayList<String> noteListTO = new ArrayList<String>();
			List<String> noteList = resReq.getNote();

			for /* each */(String aNoteEntry : /* in */noteList)
				noteListTO.add(aNoteEntry);

			resReqTO.setNoteList(noteListTO);
		}

		// EntitlementAccount element
		ReservationRequest.EntitlementAccount entitlement = resReq
				.getEntitlementAccount();

		if (entitlement != null) {

			logger.sendEvent("ReservationXML: serach have entilement",
					EventType.DEBUG, THISOBJECT);

			if (entitlement.getDefaultAccount() != null) {

				if (ACCOUNT_PER_ORDER.equals(entitlement.getDefaultAccount()) || ACCOUNT_PER_TICKET
						.equals(entitlement.getDefaultAccount())) {
					resReqTO.setDefaultAccount(entitlement.getDefaultAccount());
				}
				else {
					throw new JAXBException(
							"Invalid <ReservationRequest><EntitlementAccount><DefaultAccount> element provided.",
							"ReservationXML.java: getTO");
				}

			}
			else if (entitlement.getSpecifiedAccount() != null) {

				logger.sendEvent(
						"ReservationXML: Not a default account, get account data",
						EventType.DEBUG, THISOBJECT);

				ArrayList<SpecifiedAccountTO> specifiedAccounts = resReqTO
						.getSpecifiedAccounts();

				for (ReservationRequest.EntitlementAccount.SpecifiedAccount specified : entitlement
						.getSpecifiedAccount()) {

					SpecifiedAccountTO specifiedTO = new SpecifiedAccountTO();

					logger.sendEvent(
							"ReservationXML: got an object from  specified",
							EventType.DEBUG, THISOBJECT);

					setTOSpecifiedAccount(specifiedTO, specified);

					logger.sendEvent(
							"ReservationXML: got specified accountID= " + specifiedTO
									.getAccountItem(), EventType.DEBUG,
							THISOBJECT);

					if (specifiedTO.getAccountItem() == null) {
						throw new JAXBException(
								"Missing required <ReservationRequest><EntitlementAccount><SpecifiedAccount><AccountItem> element.",
								"ReservationXML.java: getTO");
					}

					specifiedAccounts.add(specifiedTO);
				}
			}
			else {
				throw new JAXBException(
						"Invalid <ReservationRequest><EntitlementAccount> no account element provided.",
						"ReservationXML.java: getTO");
			}

		}

		return resReqTO;
	}

	/**
	 * Gets the jaxb structure from the response transfer object.
	 * 
	 * @param resRespTO
	 *            The response transfer object.
	 * 
	 * @return the populated jaxb object
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ReservationResponse getJaxb(ReservationResponseTO resRespTO,
			DTIErrorTO errorTO) throws JAXBException {

		ReservationResponse resResp = new ReservationResponse();

		// Response Type
		resResp.setResponseType(resRespTO.getResponseType());

		// Ticket
		if (resRespTO.getTicketList() != null) {
			List<ReservationResponse.Ticket> ticketList = resResp.getTicket();
			ArrayList<TicketTO> ticketListTO = resRespTO.getTicketList();
			setJaxbTicketList(ticketList, ticketListTO, errorTO);
		}

		// Product
		if (resRespTO.getProductList() != null) {

			List<ReservationResponse.Product> productList = resResp
					.getProduct();
			ArrayList<ProductTO> productListTO = resRespTO.getProductList();

			for /* each */(ProductTO aProductTO : /* in */productListTO) {

				ReservationResponse.Product aProduct = new ReservationResponse.Product();

				// Required fields
				aProduct.setProdItem(aProductTO.getProdItem());
				aProduct.setProdCode(aProductTO.getProdCode());
				aProduct.setProdQty(aProductTO.getProdQty());
				aProduct.setProdParts(aProductTO.getProdParts());
				aProduct.setProdPrice(aProductTO.getProdPrice());
				aProduct.setProdTax1(aProductTO.getProdTax1());

				// Optional fields
				if (aProductTO.getProdTax2() != null) {
					aProduct.setProdTax2(aProductTO.getProdTax2());
				}

				if (aProductTO.getProdDescription() != null) aProduct
						.setProdDescription(aProductTO.getProdDescription());

				if (aProductTO.getProdReceiptMsg() != null) aProduct
						.setProdReceiptMsg(aProductTO.getProdReceiptMsg());

				productList.add(aProduct);
			}
		}

		// Payment
		if (resRespTO.getPaymentList() != null) {
			List<ReservationResponse.Payment> paymentList = resResp
					.getPayment();
			ArrayList<PaymentTO> paymentListTO = resRespTO.getPaymentList();
			setJaxbPaymentList(paymentList, paymentListTO);
		}

		// Receipt
		if (resRespTO.getReceiptMessage() != null) {
			ReservationResponse.Receipt receipt = new ReservationResponse.Receipt();
			String message = resRespTO.getReceiptMessage();

			QName qName = new QName("Message");
			JAXBElement<String> rcpMsg = new JAXBElement(qName,
					message.getClass(), message);
			receipt.getTotalOrTaxTotalOrTax1().add(rcpMsg);

			resResp.setReceipt(receipt);
		}

		// Reservation
		if (resRespTO.getReservation() != null) {

			ReservationResponse.Reservation reservation = new ReservationResponse.Reservation();
			ReservationTO reservationTO = resRespTO.getReservation();

			// Optional fields
			if (reservationTO.getResCode() != null) reservation
					.setResCode(reservationTO.getResCode());
			if (reservationTO.getResNumber() != null) reservation
					.setResNumber(reservationTO.getResNumber());
			if (reservationTO.getResCreateDate() != null) reservation
					.setResCreateDate(UtilXML.convertToXML(reservationTO
							.getResCreateDate()));
			if (reservationTO.getResPickupDate() != null) reservation
					.setResPickupDate(UtilXML.convertToXML(reservationTO
							.getResPickupDate()));
			if (reservationTO.getResPickupArea() != null) reservation
					.setResPickupArea(reservationTO.getResPickupArea());
			if (reservationTO.getResSalesType() != null) reservation
					.setResSalesType(reservationTO.getResSalesType());

			// Contract ID (As of 2.15, JTL)
			if (reservationTO.getContractId() != null) {
				reservation.setContractId(reservationTO.getContractId());
			}
			resResp.setReservation(reservation);
		}

		// ClientData
		if (resRespTO.getClientData() != null) {

			ReservationResponse.ClientData clientData = new ReservationResponse.ClientData();

			// Optional field
			if (resRespTO.getClientData().getClientId() != null) clientData
					.setClientId(resRespTO.getClientData().getClientId());

			resResp.setClientData(clientData);
		}

		return resResp;
	}

	/**
	 * Sets the jaxb payment list.
	 * 
	 * @param paymentList
	 *            the payment list
	 * @param paymentListTO
	 *            the payment list to
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 */
	private static void setJaxbPaymentList(
			List<ReservationResponse.Payment> paymentList,
			ArrayList<PaymentTO> paymentListTO) throws JAXBException {

		for /* each */(PaymentTO aPaymentTO : /* in */paymentListTO) {

			// As of 2.15, JTL - Installment isn't returned in the payment clause.
			if (aPaymentTO.getPayType() == PaymentType.INSTALLMENT) continue; // Skip to the next pay type.

			ReservationResponse.Payment aPayment = new ReservationResponse.Payment();

			aPayment.setPayItem(aPaymentTO.getPayItem());

			ReservationResponse.Payment.PayType aPayType = new ReservationResponse.Payment.PayType();

			if (aPaymentTO.getCreditCard() != null) {

				ReservationResponse.Payment.PayType.CreditCard aCreditCard = new ReservationResponse.Payment.PayType.CreditCard();
				CreditCardTO aCreditCardTO = aPaymentTO.getCreditCard();

				// Required fields
				aCreditCard.setCCAuthCode(aCreditCardTO.getCcAuthCode());

				// Optional fields
				if (aCreditCardTO.getCcAuthNumber() != null) aCreditCard
						.setCCAuthNumber(aCreditCardTO.getCcAuthNumber());
				if (aCreditCardTO.getCcAuthSysResponse() != null) aCreditCard
						.setCCAuthSysResponse(aCreditCardTO
								.getCcAuthSysResponse());
				if (aCreditCardTO.getCcNumber() != null) {
					aCreditCard.setCCNumber(aCreditCardTO.getCcNumber());
				}

				aPayType.setCreditCard(aCreditCard);

			}
			else if (aPaymentTO.getGiftCard() != null) {

				ReservationResponse.Payment.PayType.GiftCard aGiftCard = new ReservationResponse.Payment.PayType.GiftCard();
				GiftCardTO aGiftCardTO = aPaymentTO.getGiftCard();

				// Required fields
				aGiftCard.setGCAuthCode(aGiftCardTO.getGcAuthCode());

				// Optional fields
				if (aGiftCardTO.getGcAuthNumber() != null) aGiftCard
						.setGCAuthNumber(aGiftCardTO.getGcAuthNumber());
				if (aGiftCardTO.getGcAuthSysResponse() != null) aGiftCard
						.setGCAuthSysResponse(aGiftCardTO
								.getGcAuthSysResponse());
				if (aGiftCardTO.getGcNumber() != null) aGiftCard
						.setGCNumber(aGiftCardTO.getGcNumber());
				if (aGiftCardTO.getGcRemainingBalance() != null) aGiftCard
						.setGCRemainingBalance(aGiftCardTO
								.getGcRemainingBalance());
				if (aGiftCardTO.getGcPromoExpDate() != null) {
					aGiftCard.setGCPromoExpDate(UtilXML
							.convertToXML(aGiftCardTO.getGcPromoExpDate()));
				}

				aPayType.setGiftCard(aGiftCard);

			}

			aPayment.setPayType(aPayType);

			paymentList.add(aPayment);
		}
	}

	/**
	 * Sets the jaxb ticket list.
	 * 
	 * @param ticketListTO
	 *            the transfer object ticket list
	 * @param ticketList
	 *            the JAXB Ticket list
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void setJaxbTicketList(
			List<ReservationResponse.Ticket> ticketList,
			ArrayList<TicketTO> ticketListTO, DTIErrorTO errorTO) throws JAXBException {

		for /* each */(TicketTO aTicketTO : /* in */ticketListTO) {

			ReservationResponse.Ticket aTicket = new ReservationResponse.Ticket();

			// Ticket Item
			BigInteger tktItemTO = aTicketTO.getTktItem();
			QName qName = new QName("TktItem");
			JAXBElement<BigInteger> tktItem = new JAXBElement(qName,
					tktItemTO.getClass(), tktItemTO);
			aTicket.getTktItemOrTktPartOrTktID().add(tktItem);

			// TktID
			ArrayList<TicketTO.TicketIdType> typeList = aTicketTO
					.getTicketTypes();

			if (typeList.size() > 0) {

				ReservationResponse.Ticket.TktID tktIdObj = new ReservationResponse.Ticket.TktID();

				// Mag Code?
				if (typeList.contains(TicketTO.TicketIdType.MAG_ID)) {
					String tktMagTO = aTicketTO.getMagTrack1();
					qName = new QName("Mag");
					JAXBElement<String> tktMag = new JAXBElement(qName,
							tktMagTO.getClass(), tktMagTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktMag);
				}

				// Bar Code?
				if (typeList.contains(TicketTO.TicketIdType.BARCODE_ID)) {
					String tktBarTO = aTicketTO.getBarCode();
					qName = new QName("Barcode");
					JAXBElement<String> tktBar = new JAXBElement(qName,
							tktBarTO.getClass(), tktBarTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktBar);
				}

				// Tkt DSSN?
				if (typeList.contains(TicketTO.TicketIdType.DSSN_ID)) {
					ReservationResponse.Ticket.TktID.TktDSSN tktDssn = new ReservationResponse.Ticket.TktID.TktDSSN();
					tktDssn.setTktDate(aTicketTO.getDssnDateString());
					tktDssn.setTktNbr(aTicketTO.getDssnNumber());
					tktDssn.setTktSite(aTicketTO.getDssnSite());
					tktDssn.setTktStation(aTicketTO.getDssnStation());
					qName = new QName("TktDSSN");
					JAXBElement<String> tktDssnElement = new JAXBElement(qName,
							tktDssn.getClass(), tktDssn);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktDssnElement);
				}

				// Tkt NID ?
				if (typeList.contains(TicketTO.TicketIdType.TKTNID_ID)) {
					String tktNidTO = aTicketTO.getTktNID();
					qName = new QName("TktNID");
					JAXBElement<String> tktNid = new JAXBElement(qName,
							tktNidTO.getClass(), tktNidTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktNid);

				}

				// External?
				if (typeList.contains(TicketTO.TicketIdType.EXTERNAL_ID)) {
					String tktExtTO = aTicketTO.getExternal();
					qName = new QName("External");
					JAXBElement<String> tktExt = new JAXBElement(qName,
							tktExtTO.getClass(), tktExtTO);
					tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktExt);
				}

				// Add whatever ticket versions were found to the response.
				qName = new QName("TktID");
				JAXBElement<String> tktId = new JAXBElement(qName,
						tktIdObj.getClass(), tktIdObj);
				aTicket.getTktItemOrTktPartOrTktID().add(tktId);

			}

			// TktTransaction
			if (aTicketTO.getTktTran() != null) {

				ReservationResponse.Ticket.TktTransaction tktTran = new ReservationResponse.Ticket.TktTransaction();
				TicketTransactionTO tktTranTO = aTicketTO.getTktTran();

				// TktProvider
				if (tktTranTO.getTktProvider() != null) tktTran
						.setTktProvider(tktTranTO.getTktProvider());

				// TranDSSN
				if (tktTranTO.getDssnDate() != null) {

					ReservationResponse.Ticket.TktTransaction.TranDSSN tranDssn = new ReservationResponse.Ticket.TktTransaction.TranDSSN();

					// Required Fields
					tranDssn.setTranDate(tktTranTO.getDssnDateString());
					tranDssn.setTranSite(tktTranTO.getDssnSite());
					tranDssn.setTranStation(tktTranTO.getDssnStation());
					tranDssn.setTranNbr(tktTranTO.getDssnNumber());

					tktTran.setTranDSSN(tranDssn);
				}

				// TranNID
				if (tktTranTO.getTranNID() != null) tktTran
						.setTranNID(tktTranTO.getTranNID());

				qName = new QName("TktTransaction");
				JAXBElement section = new JAXBElement(qName,
						tktTran.getClass(), tktTran);
				aTicket.getTktItemOrTktPartOrTktID().add(section);

			}

			// TktPrice?
			if (aTicketTO.getTktPrice() != null) {
				BigDecimal tktPriceTO = aTicketTO.getTktPrice();
				qName = new QName("TktPrice");
				JAXBElement<String> tktPrice = new JAXBElement(qName,
						tktPriceTO.getClass(), tktPriceTO);
				aTicket.getTktItemOrTktPartOrTktID().add(tktPrice);
			}

			// TktTax?
			if (aTicketTO.getTktTax() != null) {
				BigDecimal tktTaxTO = aTicketTO.getTktTax();
				qName = new QName("TktTax");
				JAXBElement<String> tktTax = new JAXBElement(qName,
						tktTaxTO.getClass(), tktTaxTO);
				aTicket.getTktItemOrTktPartOrTktID().add(tktTax);
			}

			// TktStatus?
			ArrayList<TktStatusTO> statusListTO = aTicketTO.getTktStatusList();
			if (statusListTO != null) {
				if (statusListTO.size() > 0) {
					for /* each */(TktStatusTO aStatusTO : /* in */statusListTO) {
						ReservationResponse.Ticket.TktStatus tktStatus = new ReservationResponse.Ticket.TktStatus();
						tktStatus.setStatusItem(aStatusTO.getStatusItem());
						tktStatus.setStatusValue(aStatusTO.getStatusValue());
						qName = new QName("TktStatus");
						JAXBElement<String> tktStatusElement = new JAXBElement(
								qName, tktStatus.getClass(), tktStatus);
						aTicket.getTktItemOrTktPartOrTktID().add(
								tktStatusElement);
					}
				}
			}

			// TktValidity?
			if ((aTicketTO.getTktValidityValidStart() != null) && (aTicketTO
					.getTktValidityValidEnd() != null)) {
				ReservationResponse.Ticket.TktValidity tktValidity = new ReservationResponse.Ticket.TktValidity();

				XMLGregorianCalendar xCalDate = UtilXML.convertToXML(aTicketTO
						.getTktValidityValidStart());
				tktValidity.setValidStart(xCalDate);

				xCalDate = UtilXML.convertToXML(aTicketTO
						.getTktValidityValidEnd());
				tktValidity.setValidEnd(xCalDate);

				qName = new QName("TktValidity");
				JAXBElement<String> tktValidityElement = new JAXBElement(qName,
						tktValidity.getClass(), tktValidity);
				aTicket.getTktItemOrTktPartOrTktID().add(tktValidityElement);

			}

			// TktNote
			if (aTicketTO.getTktNote() != null) {

				qName = new QName("TktNote");
				JAXBElement<String> tktValidityElement = new JAXBElement(qName,
						aTicketTO.getTktNote().getClass(),
						aTicketTO.getTktNote());
				aTicket.getTktItemOrTktPartOrTktID().add(tktValidityElement);

			}

			// Ticket Error
			if ((errorTO != null) && (errorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET)) {

				ReservationResponse.Ticket.TktError tktError = new ReservationResponse.Ticket.TktError();
				tktError.setTktErrorCode(errorTO.getErrorCode());
				tktError.setTktErrorType(errorTO.getErrorType());
				tktError.setTktErrorClass(errorTO.getErrorClass());
				tktError.setTktErrorText(errorTO.getErrorText());

				qName = new QName("TktError");
				JAXBElement<String> tktErrorElement = new JAXBElement(qName,
						tktError.getClass(), tktError);

				aTicket.getTktItemOrTktPartOrTktID().add(tktErrorElement);
			}

			ticketList.add(aTicket);
		}
	}

	/**
	 * Sets the to client data.
	 * 
	 * @param clientData
	 *            the client data parsed from JAXB
	 * @param clientDataTO
	 *            the client data transfer object
	 */
	private static void setTOClientData(
			ReservationRequest.ClientData clientData, ClientDataTO clientDataTO) {

		// Optional fields
		if (clientData.getClientId() != null) clientDataTO
				.setClientId(clientData.getClientId());
		if (clientData.getClientType() != null) clientDataTO
				.setClientType(clientData.getClientType());
		if (clientData.getClientCategory() != null) clientDataTO
				.setClientCategory(clientData.getClientCategory());
		if (clientData.getDemoLanguage() != null) clientDataTO
				.setDemoLanguage(clientData.getDemoLanguage());

		// Optional Demographics
		if (clientData.getDemoData() != null) {

			ReservationRequest.ClientData.DemoData demoData = clientData
					.getDemoData();

			if (demoData.getBill() != null) {
				DemographicsTO demoTO = new DemographicsTO();
				ReservationRequest.ClientData.DemoData.Bill billData = demoData
						.getBill();

				// Optional Attributes
				if (billData.getName() != null) demoTO.setName(billData
						.getName());
				if (billData.getLastName() != null) demoTO.setLastName(billData
						.getLastName());
				if (billData.getFirstName() != null) demoTO
						.setFirstName(billData.getFirstName());
				if (billData.getAddr1() != null) demoTO.setAddr1(billData
						.getAddr1());
				if (billData.getAddr2() != null) demoTO.setAddr2(billData
						.getAddr2());
				if (billData.getCity() != null) demoTO.setCity(billData
						.getCity());
				if (billData.getState() != null) demoTO.setState(billData
						.getState());
				if (billData.getZip() != null) demoTO.setZip(billData.getZip());
				if (billData.getCountry() != null) demoTO.setCountry(billData
						.getCountry());
				if (billData.getTelephone() != null) demoTO
						.setTelephone(billData.getTelephone());
				if (billData.getEmail() != null) demoTO.setEmail(billData
						.getEmail());
				if (billData.getSellerResNbr() != null) demoTO
						.setSellerResNbr(billData.getSellerResNbr());

				clientDataTO.setBillingInfo(demoTO);

			}

			if (demoData.getShip() != null) {

				DemographicsTO demoTO = new DemographicsTO();
				ReservationRequest.ClientData.DemoData.Ship shipData = demoData
						.getShip();

				// Optional Attributes
				if (shipData.getName() != null) demoTO.setName(shipData
						.getName());
				if (shipData.getLastName() != null) demoTO.setLastName(shipData
						.getLastName());
				if (shipData.getFirstName() != null) demoTO
						.setFirstName(shipData.getFirstName());
				if (shipData.getAddr1() != null) demoTO.setAddr1(shipData
						.getAddr1());
				if (shipData.getAddr2() != null) demoTO.setAddr2(shipData
						.getAddr2());
				if (shipData.getCity() != null) demoTO.setCity(shipData
						.getCity());
				if (shipData.getState() != null) demoTO.setState(shipData
						.getState());
				if (shipData.getZip() != null) demoTO.setZip(shipData.getZip());
				if (shipData.getCountry() != null) demoTO.setCountry(shipData
						.getCountry());
				if (shipData.getTelephone() != null) demoTO
						.setTelephone(shipData.getTelephone());
				if (shipData.getEmail() != null) demoTO.setEmail(shipData
						.getEmail());

				clientDataTO.setShippingInfo(demoTO);

			}

			if (demoData.getDelivery() != null) {

				DemographicsTO demoTO = new DemographicsTO();
				ReservationRequest.ClientData.DemoData.Delivery deliveryData = demoData
						.getDelivery();

				// Optional Attributes
				if (deliveryData.getName() != null) demoTO.setName(deliveryData
						.getName());
				if (deliveryData.getLastName() != null) demoTO
						.setLastName(deliveryData.getLastName());
				if (deliveryData.getFirstName() != null) demoTO
						.setFirstName(deliveryData.getFirstName());
				if (deliveryData.getAddr1() != null) demoTO
						.setAddr1(deliveryData.getAddr1());
				if (deliveryData.getAddr2() != null) demoTO
						.setAddr2(deliveryData.getAddr2());
				if (deliveryData.getCity() != null) demoTO.setCity(deliveryData
						.getCity());
				if (deliveryData.getState() != null) demoTO
						.setState(deliveryData.getState());
				if (deliveryData.getZip() != null) demoTO.setZip(deliveryData
						.getZip());
				if (deliveryData.getCountry() != null) demoTO
						.setCountry(deliveryData.getCountry());
				if (deliveryData.getTelephone() != null) demoTO
						.setTelephone(deliveryData.getTelephone());
				if (deliveryData.getEmail() != null) demoTO
						.setEmail(deliveryData.getEmail());

				clientDataTO.setDeliveryInfo(demoTO);

			}
		}

		// Client Payment Method
		if (clientData.getClientPaymentMethod() != null) {
			String paymentMethod = clientData.getClientPaymentMethod();

			if (paymentMethod.equalsIgnoreCase(CREDIT_CARD)) {
				clientDataTO
						.setClientPaymentMethod(ClientDataTO.PaymentMethod.CREDITCARD);
			}
			else if (paymentMethod.equalsIgnoreCase(DISNEY_GIFT_CARD)) {
				clientDataTO
						.setClientPaymentMethod(ClientDataTO.PaymentMethod.DISNEYGIFTCARD);
			}
			else if (paymentMethod.equalsIgnoreCase(ORGANIZATIONAL_CHECK)) {
				clientDataTO
						.setClientPaymentMethod(ClientDataTO.PaymentMethod.ORGANIZATIONALCHECK);
			}
			else if (paymentMethod.equalsIgnoreCase(CASHIERS_CHECK)) {
				clientDataTO
						.setClientPaymentMethod(ClientDataTO.PaymentMethod.CASHIERSCHECK);
			}
			else if (paymentMethod.equalsIgnoreCase(MONEY_ORDER)) {
				clientDataTO
						.setClientPaymentMethod(ClientDataTO.PaymentMethod.MONEYORDER);
			}
			else if (paymentMethod.equalsIgnoreCase(WIRE_TRANSFER)) {
				clientDataTO
						.setClientPaymentMethod(ClientDataTO.PaymentMethod.WIRETRANSFER);
			}
			else if (paymentMethod.equalsIgnoreCase(PAY_AT_PICKUP)) {
				clientDataTO
						.setClientPaymentMethod(ClientDataTO.PaymentMethod.PAYATPICKUP);
			}
			else if (paymentMethod.equalsIgnoreCase(MASTER_ACCOUNT)) {
				clientDataTO
						.setClientPaymentMethod(ClientDataTO.PaymentMethod.MASTERACCOUNT);
			}
			else if (paymentMethod.equalsIgnoreCase(SAP_JOB_NUMBER)) {
				clientDataTO
						.setClientPaymentMethod(ClientDataTO.PaymentMethod.SAPJOBNUMBER);
			}
			else if (paymentMethod.equalsIgnoreCase(TICKET_EXCHANGE)) {
				clientDataTO
						.setClientPaymentMethod(ClientDataTO.PaymentMethod.TICKETEXCHANGE);
			}
			else {
				clientDataTO
						.setClientPaymentMethod(ClientDataTO.PaymentMethod.UNDEFINED);
			}

		}

		// Client Fulfillment Method
		if (clientData.getClientFulfillmentMethod() != null) {
			clientDataTO.setClientFulfillmentMethod(clientData
					.getClientFulfillmentMethod());
		}

		// Client Delivery Instructions
		if (clientData.getClientDeliveryInstructions() != null) {
			clientDataTO.setClientDeliveryInstructions(clientData
					.getClientDeliveryInstructions());
		}

		// Client Group Validity
		if (clientData.getClientGroupValidity() != null) {

			ClientGroupValidity clientGroupValidity = clientData
					.getClientGroupValidity();

			List<JAXBElement<XMLGregorianCalendar>> aValidityList = clientGroupValidity
					.getValidStartOrValidEnd();

			for /* each */(JAXBElement<XMLGregorianCalendar> aValidityDate : /* in */aValidityList) {

				QName fieldName = aValidityDate.getName();

				if (fieldName.getLocalPart().equalsIgnoreCase("ValidStart")) {
					if (aValidityDate.getValue() != null) {

						XMLGregorianCalendar tXCal = aValidityDate.getValue();
						GregorianCalendar tempCalendar = new GregorianCalendar(
								tXCal.getEonAndYear().intValue(),
								(tXCal.getMonth() - 1), tXCal.getDay());
						clientDataTO.setGroupValidityValidStart(tempCalendar);

					}
				}

				if (fieldName.getLocalPart().equalsIgnoreCase("ValidEnd")) {

					XMLGregorianCalendar tXCal = aValidityDate.getValue();
					GregorianCalendar tempCalendar = new GregorianCalendar(
							tXCal.getEonAndYear().intValue(),
							(tXCal.getMonth() - 1), tXCal.getDay());
					clientDataTO.setGroupValidityValidEnd(tempCalendar);
				}

			}
		} // Client Group Validity

		// Client Sales Contact
		if (clientData.getClientSalesContact() != null) {
			clientDataTO.setClientSalesContact(clientData
					.getClientSalesContact());
		}

		// Time Requirement
		if (clientData.getTimeRequirement() != null) {
			clientDataTO.setTimeRequirement(clientData.getTimeRequirement());
		}

		return;
	}

	/**
	 * Sets the to reservation.
	 * 
	 * @param resvTO
	 *            the resv transfer object
	 * @param aReservationEntry
	 *            a JAXB reservation entry
	 */
	private static void setTOReservation(ReservationTO resvTO,
			JAXBElement<?> aReservationEntry) {

		QName fieldName = aReservationEntry.getName();

		if (fieldName.getLocalPart().equalsIgnoreCase("ResCode")) {
			resvTO.setResCode((String) aReservationEntry.getValue());
		}
		else if (fieldName.getLocalPart().equalsIgnoreCase("ResNumber")) {
			resvTO.setResNumber((String) aReservationEntry.getValue());
		}
		else if (fieldName.getLocalPart().equalsIgnoreCase("ResCreateDate")) {
			XMLGregorianCalendar tXCal = (XMLGregorianCalendar) aReservationEntry
					.getValue();
			GregorianCalendar tempCalendar = UtilXML.convertFromXML(tXCal);
			resvTO.setResCreateDate(tempCalendar);
		}
		else if (fieldName.getLocalPart().equalsIgnoreCase("ResPickupDate")) {
			XMLGregorianCalendar tXCal = (XMLGregorianCalendar) aReservationEntry
					.getValue();
			GregorianCalendar tempCalendar = UtilXML.convertFromXML(tXCal);
			resvTO.setResPickupDate(tempCalendar);
		}
		else if (fieldName.getLocalPart().equalsIgnoreCase("ResPickupArea")) {
			resvTO.setResPickupArea((String) aReservationEntry.getValue());
		}
		else if (fieldName.getLocalPart().equalsIgnoreCase("ResSalesType")) {
			resvTO.setResSalesType((String) aReservationEntry.getValue());
		}

		return;
	}

	/**
	 * Given the SpecifiedAccountTO to fill, and the JAXBElement representing the <SpecifiedAccount> element.
	 * 
	 * @param specifiedAccountTO
	 * @param specifiedAccountEntry
	 * @throws ParseException
	 */
	private static void setTOSpecifiedAccount(
			SpecifiedAccountTO specifiedAccountTO,
			SpecifiedAccount specifiedAccountEntry) throws JAXBException {

		logger.sendEvent("ReservationXML: setTOSpecifiedAccount()",
				EventType.DEBUG, THISOBJECT);

		SpecifiedAccount specific = specifiedAccountEntry;

		// set AccountID
		if (specific.getAccountItem() != null) {
			specifiedAccountTO.setAccountItem((BigInteger) specific
					.getAccountItem());
		}
		else {
			logger.sendEvent(
					"ReservationXML: setTOSpecifiedAccount invalid account item:" + specific
							.getAccountItem().toString(), EventType.DEBUG,
					THISOBJECT);
			throw new JAXBException(
					"Invalid <ReservationRequest><EntitlementAccount><SpecifiedAccount> no account item provided.",
					"CreateTicketXML.java: setTOSpecifiedAccount");
		}

		// [ ExistingAccount | NewAccount | NewMediaData ]
		if (specific.getExistingAccount() != null) {

			ExistingAccount existing = specific.getExistingAccount();

			logger.sendEvent(
					"ReservationXML: setTOSpecifiedAccount for:" + existing
							.getExistingMediaId() + ":" + (existing.getTktID() == null) + ":" + existing
							.getAccountId() + ":", EventType.DEBUG, THISOBJECT);

			if (existing.getExistingMediaId() != null) {

				specifiedAccountTO.setExistingMediaId(existing
						.getExistingMediaId());

			}
			else if (existing.getTktID() != null) {

				TktID tktId = (TktID) existing.getTktID();
				TicketIdTO ticketId = new TicketIdTO();

				if (tktId.getMag() != null) {
					ticketId.setMag(tktId.getMag());
				}
				else if (tktId.getBarcode() != null) {
					ticketId.setBarCode(tktId.getBarcode());
				}
				else if (tktId.getTktDSSN() != null) {

					try {
						ticketId.setDssn(tktId.getTktDSSN().getTktDate(), tktId
								.getTktDSSN().getTktSite(), tktId.getTktDSSN()
								.getTktStation(), tktId.getTktDSSN()
								.getTktNbr());
					}
					catch (ParseException parse) {
						parse.printStackTrace();
						throw (new JAXBException(
								"ReservationXML:setTOSpecifiedAccount: TicketIDTO.setDssn",
								parse));
					}
				}
				else if (tktId.getTktNID() != null) {

					ticketId.setTktNID(tktId.getTktNID());

				}
				else if (tktId.getExternal() != null) {

					ticketId.setExternal(tktId.getExternal());

				}
				specifiedAccountTO.setExistingTktID(ticketId);

			}
			else if (existing.getAccountId() != null) {

				specifiedAccountTO
						.setExistingAccountId(existing.getAccountId());

				logger.sendEvent(
						"ReservationXML: setTOSpecifiedAccount setAccountItem:" + specifiedAccountTO
								.getExistingAccountId(), EventType.DEBUG,
						THISOBJECT);
			}

		}
		else if (specific.getNewAccount() != null) {

			ExternalReferenceType newAccount = specific.getNewAccount();

			specifiedAccountTO.setNewExternalReferenceType(newAccount
					.getExternalReferenceType());

			specifiedAccountTO.setNewExternalReferenceValue(newAccount
					.getExternalReferenceValue());

		}

		// Pull account level demographic data.
		if (specific.getAccountDemoData() != null) { // New as of 2.11 (JTL)

			AccountDemoData acctDemoData = specific.getAccountDemoData();

			DemographicsTO demoTO = new DemographicsTO();

			// Optional Attributes
			// Title
			if (acctDemoData.getTitle() != null) demoTO.setTitle(acctDemoData
					.getTitle());

			// Fist Name
			if (acctDemoData.getFirstName() != null) demoTO
					.setFirstName(acctDemoData.getFirstName());

			// Middle Name
			if (acctDemoData.getMiddleName() != null) demoTO
					.setMiddleName(acctDemoData.getMiddleName());

			// Last Name
			if (acctDemoData.getLastName() != null) demoTO
					.setLastName(acctDemoData.getLastName());

			// Suffix
			if (acctDemoData.getSuffix() != null) demoTO.setSuffix(acctDemoData
					.getSuffix());

			// Date of Birth
			if (acctDemoData.getDateOfBirth() != null) {
				XMLGregorianCalendar tXCal = (XMLGregorianCalendar) acctDemoData
						.getDateOfBirth();
				GregorianCalendar tempCalendar = UtilXML.convertFromXML(tXCal);
				demoTO.setDateOfBirth(tempCalendar);
			}

			// Address 1
			if (acctDemoData.getAddr1() != null) demoTO.setAddr1(acctDemoData
					.getAddr1());

			// Address 2
			if (acctDemoData.getAddr2() != null) demoTO.setAddr2(acctDemoData
					.getAddr2());

			// City
			if (acctDemoData.getCity() != null) demoTO.setCity(acctDemoData
					.getCity());

			// State
			if (acctDemoData.getState() != null) demoTO.setState(acctDemoData
					.getState());

			// ZIP
			if (acctDemoData.getZip() != null) demoTO.setZip(acctDemoData
					.getZip());

			// Country
			if (acctDemoData.getCountry() != null) demoTO
					.setCountry(acctDemoData.getCountry());

			// eMail
			if (acctDemoData.getEmail() != null) demoTO.setEmail(acctDemoData
					.getEmail());

			// OptInSolocit (defaults to NO if not present)
			if (acctDemoData.getOptInSolicit() != null) {
				if (acctDemoData.getOptInSolicit().compareTo("YES") == 0) {
					demoTO.setOptInSolicit(new Boolean(true));
				}
				else {
					demoTO.setOptInSolicit(new Boolean(false));
				}
			}
			else {
				demoTO.setOptInSolicit(new Boolean(false));
			}

			specifiedAccountTO.setAccountDemo(demoTO);

		}

		// Is there new media? (Moved out of the prior if/then by JTL for 2.10)
		if (specific.getNewMediaData() != null) {

			List<NewMediaData> mediaList = specific.getNewMediaData();

			for (NewMediaData mediaItem : mediaList) {

				logger.sendEvent(
						"ReservationXML: setTOSpecifiedAccount for:" + mediaItem
								.getMediaId() + ":" + mediaItem.getMfrId() + ":" + mediaItem
								.getVisualId() + ":", EventType.DEBUG,
						THISOBJECT);

				NewMediaDataTO toMedia = new NewMediaDataTO();

				toMedia.setMediaId(mediaItem.getMediaId());
				toMedia.setMfrId(mediaItem.getMfrId());
				toMedia.setVisualId(mediaItem.getVisualId());

				specifiedAccountTO.addNewMediaData(toMedia);
			}
		}

		return;

	}

	/**
	 * Sets the transfer object payment list.
	 * 
	 * @param paymentListTO
	 *            the transfer object payment list.
	 * @param aPayment
	 *            the JAXB payment list.
	 */
	private static void setTOPaymentList(ArrayList<PaymentTO> paymentListTO,
			ReservationRequest.Payment aPayment) {

		PaymentTO aPaymentTO = new PaymentTO();

		// Required items
		// PayItem
		aPaymentTO.setPayItem(new BigInteger(aPayment.getPayItem()));

		// Optional Items
		// PayType
		ReservationRequest.Payment.PayType payType = aPayment.getPayType();

		if (payType.getCreditCard() != null) { // THIS IS A CREDIT CARD
			CreditCardTO creditCardTO = new CreditCardTO();
			ReservationRequest.Payment.PayType.CreditCard creditCard = payType
					.getCreditCard();

			if (creditCard.getCCManual() != null) {

				ReservationRequest.Payment.PayType.CreditCard.CCManual ccManual = creditCard
						.getCCManual();

				// Required fields
				creditCardTO.setCcNbr(ccManual.getCCNbr());
				creditCardTO.setCcExpiration(ccManual.getCCExpiration());

				// Optional fields
				if (ccManual.getCCVV() != null) creditCardTO.setCcVV(ccManual
						.getCCVV());
				if (ccManual.getCCName() != null) creditCardTO
						.setCcName(ccManual.getCCName());
				if (ccManual.getCCStreet() != null) creditCardTO
						.setCcStreet(ccManual.getCCStreet());
				if (ccManual.getCCZipcode() != null) creditCardTO
						.setCcZipCode(ccManual.getCCZipcode());
				if (ccManual.getCCCAVV() != null) creditCardTO
						.setCcCAVV(ccManual.getCCCAVV());
				if (ccManual.getCCEcommerce() != null) creditCardTO
						.setCcEcommerce(ccManual.getCCEcommerce());
				if (ccManual.getCCType() != null) creditCardTO
						.setCcType(ccManual.getCCType());

			}
			else if (creditCard.getCCSwipe() != null) {

				ReservationRequest.Payment.PayType.CreditCard.CCSwipe ccSwipe = creditCard
						.getCCSwipe();

				// Required fields
				creditCardTO.setCcTrack1(ccSwipe.getCCTrack1());
				creditCardTO.setCcTrack2(ccSwipe.getCCTrack2());

				// Optional fields
				if (ccSwipe.getCCVV() != null) creditCardTO.setCcVV(ccSwipe
						.getCCVV());
				if (ccSwipe.getPosTerminal() != null) // as of 2.12
				creditCardTO.setPosTermID(ccSwipe.getPosTerminal());
				if (ccSwipe.getExtnlDevSerial() != null) // as of 2.12
				creditCardTO.setExtnlDevSerial(ccSwipe.getExtnlDevSerial());
			}
			else if (creditCard.getCCWireless() != null) { // as of 2.12

				ReservationRequest.Payment.PayType.CreditCard.CCWireless ccWireless = creditCard
						.getCCWireless();
				creditCardTO.setWireless(true);

				// Required fields
				creditCardTO.setCcTrack1(ccWireless.getCCTrack1());
				creditCardTO.setCcTrack2(ccWireless.getCCTrack2());

				// Optional fields
				if (ccWireless.getCCVV() != null) creditCardTO
						.setCcVV(ccWireless.getCCVV());
				if (ccWireless.getPosTerminal() != null) // as of 2.12
				creditCardTO.setPosTermID(ccWireless.getPosTerminal());
				if (ccWireless.getExtnlDevSerial() != null) // as of 2.12
				creditCardTO.setExtnlDevSerial(ccWireless.getExtnlDevSerial());
			}

			aPaymentTO.setCreditCard(creditCardTO);

		}
		else if (payType.getVoucher() != null) { // THIS IS A VOUCHER
			VoucherTO voucherTO = new VoucherTO();

			// Required fields
			voucherTO.setMainCode(payType.getVoucher().getMainCode());

			// Optional fields
			if (payType.getVoucher().getUniqueCode() != null) voucherTO
					.setUniqueCode(payType.getVoucher().getUniqueCode());

			aPaymentTO.setVoucher(voucherTO);

		}
		else if (payType.getGiftCard() != null) { // THIS IS A GIFT CARD
			GiftCardTO giftCardTO = new GiftCardTO();

			if (payType.getGiftCard().getGCManual() != null) {
				ReservationRequest.Payment.PayType.GiftCard.GCManual gcManual = payType
						.getGiftCard().getGCManual();

				// Required fields
				giftCardTO.setGcNbr(gcManual.getGCNbr());

				// Optional fields
				if (gcManual.getGCStartDate() != null) {
					XMLGregorianCalendar tXCal = (XMLGregorianCalendar) gcManual
							.getGCStartDate();
					GregorianCalendar tempCalendar = UtilXML
							.convertFromXML(tXCal);
					giftCardTO.setGcStartDate(tempCalendar);
				}

				aPaymentTO.setGiftCard(giftCardTO);

			}
			else if (payType.getGiftCard().getGCSwipe() != null) {

				ReservationRequest.Payment.PayType.GiftCard.GCSwipe gcSwipe = payType
						.getGiftCard().getGCSwipe();

				// Required fields
				giftCardTO.setGcTrack2(gcSwipe.getGCTrack2());

				// Optional fields
				if (gcSwipe.getGCTrack1() != null) {
					giftCardTO.setGcTrack1(gcSwipe.getGCTrack1());
				}

				aPaymentTO.setGiftCard(giftCardTO);

			}
		}
		else if (payType.getInstallment() != null) { // (As of 2.15, JTL)

			InstallmentTO instTO = new InstallmentTO();
			ReservationRequest.Payment.PayType.Installment installment = payType
					.getInstallment();
			ReservationRequest.Payment.PayType.Installment.InstallmentCreditCard installCard = installment
					.getInstallmentCreditCard();

			InstallmentCreditCardTO installCCTO = instTO.getCreditCard();

			if (installCard.getCCManual() != null) {
				installCCTO.setCcNbr(installCard.getCCManual().getCCNbr());
				installCCTO.setCcExpiration(installCard.getCCManual()
						.getCCExpiration());
				installCCTO.setCcName(installCard.getCCManual().getCCName());
			}
			else {
				installCCTO.setCcTrack1(installCard.getCCSwipe().getCCTrack1());
				installCCTO.setCcTrack2(installCard.getCCSwipe().getCCTrack2());
			}

			ReservationRequest.Payment.PayType.Installment.InstallmentDemoData installDemo = installment
					.getInstallmentDemoData();
			InstallmentDemographicsTO installDemoTO = instTO.getInstllDemo();

			// First Name
			installDemoTO.setFirstName(installDemo.getFirstName());

			// Middle Name (opt)
			if (installDemo.getMiddleName() != null) {
				installDemoTO.setMiddleName(installDemo.getMiddleName());
			}

			// Last Name
			installDemoTO.setLastName(installDemo.getLastName());

			// DOB (opt)
			if (installDemo.getDateOfBirth() != null) {
				XMLGregorianCalendar tXCal = (XMLGregorianCalendar) installDemo
						.getDateOfBirth();
				GregorianCalendar tempCalendar = UtilXML.convertFromXML(tXCal);
				installDemoTO.setDateOfBirth(tempCalendar);
			}

			// Addr1
			installDemoTO.setAddr1(installDemo.getAddr1());

			// Addr2 (opt)
			if (installDemo.getAddr2() != null) {
				installDemoTO.setAddr2(installDemo.getAddr2());
			}

			// City
			installDemoTO.setCity(installDemo.getCity());

			// State
			installDemoTO.setState(installDemo.getState());

			// ZIP
			installDemoTO.setZip(installDemo.getZip());

			// Country
			installDemoTO.setCountry(installDemo.getCountry());

			// Telephone
			installDemoTO.setTelephone(installDemo.getTelephone());

			// AltTelephone (opt)
			if (installDemo.getAltTelephone() != null) {
				installDemoTO.setAltTelephone(installDemo.getAltTelephone());
			}

			// Email
			installDemoTO.setEmail(installDemo.getEmail());

			// Set the installment
			aPaymentTO.setInstallment(instTO);

		}

		// PayAmount
		if (aPayment.getPayAmount() != null) {
			String doubleString = aPayment.getPayAmount().toString();
			aPaymentTO.setPayAmount(new BigDecimal(doubleString));
		}

		// Add payment to list
		paymentListTO.add(aPaymentTO);

		return;

	}

	/**
	 * Sets the transfer object ticket list.
	 * 
	 * @param ticketListTO
	 *            the transfer object ticket list.
	 * @param aTicket
	 *            a JAXB parsed ticket.
	 */
	private static void setTOTicketList(ArrayList<TicketTO> ticketListTO,
			ReservationRequest.Ticket aTicket) {

		TicketTO aTicketTO = new TicketTO();

		// Required items
		// TktItem
		aTicketTO.setTktItem(aTicket.getTktItem());

		// ProdCode
		aTicketTO.setProdCode(aTicket.getProdCode());

		// ProdQty
		aTicketTO.setProdQty(aTicket.getProdQty());

		// Optional items
		// ProdDemoData (opt)
		if (aTicket.getProdDemoData() != null) {

			ProdDemoData aTicketDemo = aTicket.getProdDemoData();
			if (aTicketDemo.getTktDemoData() != null) {

				List<TktDemoData> tktDemoDataList = aTicketDemo
						.getTktDemoData();

				for /* each */(TktDemoData aTktDemographic : /* in */tktDemoDataList) {

					DemographicsTO aTicketDemoTO = new DemographicsTO();

					// NOTE: In this specific case, the XSD enforces minimum lengths,
					// so we know the length will never be zero.

					// FirstName
					aTicketDemoTO.setFirstName(aTktDemographic.getFirstName());

					// LastName
					aTicketDemoTO.setLastName(aTktDemographic.getLastName());

					// DateOfBirth
					XMLGregorianCalendar tXCal = (XMLGregorianCalendar) aTktDemographic
							.getDateOfBirth();
					GregorianCalendar tempCalendar = UtilXML
							.convertFromXML(tXCal);
					aTicketDemoTO.setDateOfBirth(tempCalendar);

					// Gender
					aTicketDemoTO.setGender(aTktDemographic.getGender());

					// Addr1
					aTicketDemoTO.setAddr1(aTktDemographic.getAddr1());

					// Addr2 (opt)
					if (aTktDemographic.getAddr2() != null) {
						aTicketDemoTO.setAddr2(aTktDemographic.getAddr2());
					}

					// City
					aTicketDemoTO.setCity(aTktDemographic.getCity());

					// State (opt)
					if (aTktDemographic.getState() != null) {
						aTicketDemoTO.setState(aTktDemographic.getState());
					}

					// ZIP
					aTicketDemoTO.setZip(aTktDemographic.getZip());

					// Country
					aTicketDemoTO.setCountry(aTktDemographic.getCountry());

					// Telephone (opt) as of 2.16.1 APMP (JTL)
					if (aTktDemographic.getTelephone() != null) {
						aTicketDemoTO.setTelephone(aTktDemographic
								.getTelephone());
					}

					// Email (opt)
					if (aTktDemographic.getEmail() != null) {
						aTicketDemoTO.setEmail(aTktDemographic.getEmail());
					}

					// OptInSolicit (2.10)
					if (aTktDemographic.getOptInSolicit().compareTo("YES") == 0) {
						aTicketDemoTO.setOptInSolicit(new Boolean(true));
					}
					else {
						aTicketDemoTO.setOptInSolicit(new Boolean(false));
					}

					aTicketTO.addTicketDemographic(aTicketDemoTO);

				}

			} // tktDemoDataList

		}

		// ProdPrice
		if (aTicket.getProdPrice() != null) aTicketTO
				.setProdPrice(new BigDecimal(aTicket.getProdPrice()));

		// TktValidity
		if (aTicket.getTktValidity() != null) {

			List<JAXBElement<XMLGregorianCalendar>> aValidityList = aTicket
					.getTktValidity().getValidStartOrValidEnd();

			for /* each */(JAXBElement<XMLGregorianCalendar> aValidityDate : /* in */aValidityList) {

				QName fieldName = aValidityDate.getName();

				if (fieldName.getLocalPart().equalsIgnoreCase("ValidStart")) {
					if (aValidityDate.getValue() != null) {

						XMLGregorianCalendar tXCal = aValidityDate.getValue();
						GregorianCalendar tempCalendar = new GregorianCalendar(
								tXCal.getEonAndYear().intValue(),
								(tXCal.getMonth() - 1), tXCal.getDay());
						aTicketTO.setTktValidityValidStart(tempCalendar);

					}
				}

				if (fieldName.getLocalPart().equalsIgnoreCase("ValidEnd")) {

					XMLGregorianCalendar tXCal = aValidityDate.getValue();
					GregorianCalendar tempCalendar = new GregorianCalendar(
							tXCal.getEonAndYear().intValue(),
							(tXCal.getMonth() - 1), tXCal.getDay());
					aTicketTO.setTktValidityValidEnd(tempCalendar);
				}

			}
		} // TktValidity

		// TktAssignment
		if (aTicket.getTktAssignment() != null) {
			for /* each */(TktAssignment assignment : /* in */aTicket
					.getTktAssignment()) {
				TicketTO.TktAssignmentTO assignTO = aTicketTO.new TktAssignmentTO();
				assignTO.setAccountItem(assignment.getAccountItem());
				assignTO.setProdQty(assignment.getProdQty());
				aTicketTO.getTicketAssignmets().add(assignTO);
			}
		}

		ticketListTO.add(aTicketTO);
	}

	/**
	 * @return the creditCard
	 */
	public static String getCreditCard() {
		return CREDIT_CARD;
	}

	/**
	 * @return the disneyGiftCard
	 */
	public static String getDisneyGiftCard() {
		return DISNEY_GIFT_CARD;
	}

	/**
	 * @return the organizationalCheck
	 */
	public static String getOrganizationalCheck() {
		return ORGANIZATIONAL_CHECK;
	}

	/**
	 * @return the cashiersCheck
	 */
	public static String getCashiersCheck() {
		return CASHIERS_CHECK;
	}

	/**
	 * @return the moneyOrder
	 */
	public static String getMoneyOrder() {
		return MONEY_ORDER;
	}

	/**
	 * @return the wireTransfer
	 */
	public static String getWireTransfer() {
		return WIRE_TRANSFER;
	}

	/**
	 * @return the payAtPickup
	 */
	public static String getPayAtPickup() {
		return PAY_AT_PICKUP;
	}

	/**
	 * @return the masterAccount
	 */
	public static String getMasterAccount() {
		return MASTER_ACCOUNT;
	}

	/**
	 * @return the sapJobNumber
	 */
	public static String getSapJobNumber() {
		return SAP_JOB_NUMBER;
	}

	/**
	 * @return the ticketExchange
	 */
	public static String getTicketExchange() {
		return TICKET_EXCHANGE;
	}

}
