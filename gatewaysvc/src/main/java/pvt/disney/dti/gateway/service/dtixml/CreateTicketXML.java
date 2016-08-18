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
import pvt.disney.dti.gateway.constants.ValueConstants;
import pvt.disney.dti.gateway.data.CreateTicketRequestTO;
import pvt.disney.dti.gateway.data.CreateTicketResponseTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.SpecifiedAccountTO;
import pvt.disney.dti.gateway.data.common.TicketIdTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.VoucherTO;
import pvt.disney.dti.gateway.request.xsd.CreateTicketRequest;
import pvt.disney.dti.gateway.request.xsd.CreateTicketRequest.EntitlementAccount.SpecifiedAccount.ExistingAccount.TktID;
import pvt.disney.dti.gateway.request.xsd.CreateTicketRequest.Ticket.ProdDemoData;
import pvt.disney.dti.gateway.request.xsd.CreateTicketRequest.Ticket.ProdDemoData.TktDemoData;
import pvt.disney.dti.gateway.request.xsd.CreateTicketRequest.Ticket.TktAssignment;
import pvt.disney.dti.gateway.request.xsd.CreateTicketRequest.EntitlementAccount.SpecifiedAccount.NewMediaData;
import pvt.disney.dti.gateway.request.xsd.CreateTicketRequest.EntitlementAccount.SpecifiedAccount;
import pvt.disney.dti.gateway.request.xsd.CreateTicketRequest.EntitlementAccount.SpecifiedAccount.ExistingAccount;
import pvt.disney.dti.gateway.request.xsd.ExternalReferenceType;
import pvt.disney.dti.gateway.response.xsd.CreateTicketResponse;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This class is responsible for transforming the JAXB parsed request into a transfer object as well as transforming the transfer object response back into a JAXB structure.
 * 
 * @author lewit019
 * 
 */
public abstract class CreateTicketXML {

	private static final Class<CreateTicketXML> THISOBJECT = CreateTicketXML.class;
	public static final String ACCOUNT_PER_TICKET = "AccountPerTicket";
	public static final String ACCOUNT_PER_ORDER = "AccountPerOrder";
	private static final EventLogger logger = EventLogger
			.getLogger(CreateTicketXML.class.getCanonicalName());

	/**
	 * Gets the request transfer object from the parsed JAXB structure.
	 * 
	 * @param createReq
	 *            the create request JAXB parsed structure.
	 * 
	 * @return the properly formatted request transfer object.
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 * 
	 * @since 2.9
	 */
	public static CreateTicketRequestTO getTO(CreateTicketRequest createReq) throws JAXBException {

		CreateTicketRequestTO createReqTO = new CreateTicketRequestTO();

		// Ticket
		List<CreateTicketRequest.Ticket> ticketList = createReq.getTicket();
		ArrayList<TicketTO> ticketListTO = new ArrayList<TicketTO>();
		for /* each */(CreateTicketRequest.Ticket aTicket : /* in */ticketList) {
			setTOTicketList(ticketListTO, aTicket);
		}
		createReqTO.setTicketList(ticketListTO);

		// Payment
		List<CreateTicketRequest.Payment> paymentList = createReq.getPayment();
		ArrayList<PaymentTO> paymentListTO = new ArrayList<PaymentTO>();
		for /* each */(CreateTicketRequest.Payment aPayment : /* in */paymentList) {
			setTOPaymentList(paymentListTO, aPayment);
		}
		createReqTO.setPaymentList(paymentListTO);

		// Eligibility
		if (createReq.getEligibility() != null) {
			CreateTicketRequest.Eligibility elig = createReq.getEligibility();
			createReqTO.setEligibilityGroup(elig.getGroup());
			createReqTO.setEligibilityMember(elig.getMember());
		}

		// EntitlementAccount element
		CreateTicketRequest.EntitlementAccount entitlement = createReq
				.getEntitlementAccount();

		if (entitlement != null) {

			logger.sendEvent("CreateTicketXML: search have entilement",
					EventType.DEBUG, THISOBJECT);

			if (entitlement.getDefaultAccount() != null) {

				if (ACCOUNT_PER_ORDER.equals(entitlement.getDefaultAccount()) || ACCOUNT_PER_TICKET
						.equals(entitlement.getDefaultAccount())) {
					createReqTO.setDefaultAccount(entitlement
							.getDefaultAccount());
				}
				else {
					throw new JAXBException(
							"Invalid <CreateTicketRequest><EntitlementAccount><DefaultAccount> element provided.",
							"CreateTicketXML.java: getTO");
				}

			}
			else if (entitlement.getSpecifiedAccount() != null) {

				logger.sendEvent(
						"CreateTicketXML: Not a default account, get account data",
						EventType.DEBUG, THISOBJECT);

				ArrayList<SpecifiedAccountTO> specifiedAccounts = createReqTO
						.getSpecifiedAccounts();

				for (SpecifiedAccount specified : entitlement
						.getSpecifiedAccount()) {

					SpecifiedAccountTO specifiedTO = new SpecifiedAccountTO();

					logger.sendEvent(
							"CreateTicketXML: got an object from  specified",
							EventType.DEBUG, THISOBJECT);

					setTOSpecifiedAccount(specifiedTO, specified);

					logger.sendEvent(
							"CreateTicketXML: got specified accountID= " + specifiedTO
									.getAccountItem(), EventType.DEBUG,
							THISOBJECT);

					if (specifiedTO.getAccountItem() == null) {
						throw new JAXBException(
								"Missing required <CreateTicketRequest><EntitlementAccount><SpecifiedAccount><AccountItem> element.",
								"CreateTicketXML.java: getTO");
					}

					specifiedAccounts.add(specifiedTO);
				}
			}
			else {
				throw new JAXBException(
						"Invalid <CreateTicketRequest><EntitlementAccount> no account element provided.",
						"CreateTicketXML.java: getTO");
			}
		}

		// AuditNotation (as of 2.12)
		if (createReq.getAuditNotation() != null) {
			createReqTO.setAuditNotation(createReq.getAuditNotation());
		}

		return createReqTO;

	}

	/**
	 * Given the SpecifiedAccountTO to fill, and the JAXBElement representing the <SpecifiedAccount> element. NOTE: Minus logging, this is exactly the same code as in Reservation XML.
	 * 
	 * @since 2.9
	 * @param specifiedAccountTO
	 * @param specifiedAccountEntry
	 * @throws ParseException
	 */
	private static void setTOSpecifiedAccount(
			SpecifiedAccountTO specifiedAccountTO,
			SpecifiedAccount specifiedAccountEntry) throws JAXBException {

		logger.sendEvent("CreateTicketXML: setTOSpecifiedAccount()",
				EventType.DEBUG, THISOBJECT);

		SpecifiedAccount specific = specifiedAccountEntry;

		// set AccountID
		if (specific.getAccountItem() != null) {
			specifiedAccountTO.setAccountItem((BigInteger) specific
					.getAccountItem());
		}
		else {
			logger.sendEvent(
					"CreateTicketXML: setTOSpecifiedAccount invalid account item:" + specific
							.getAccountItem().toString(), EventType.DEBUG,
					THISOBJECT);
			throw new JAXBException(
					"Invalid <CreateTicketRequest><EntitlementAccount><SpecifiedAccount> no AccountItem provided.",
					"CreateTicketXML.java: setTOSpecifiedAccount");
		}

		// [ ExistingAccount | NewAccount | NewMediaData ]
		if (specific.getExistingAccount() != null) {

			ExistingAccount existing = specific.getExistingAccount();

			logger.sendEvent(
					"CreateTicketXML: setTOSpecifiedAccount for: " + existing
							.getExistingMediaId() + " : " + (existing
							.getTktID() == null) + " : " + existing
							.getAccountId() + " : ", EventType.DEBUG,
					THISOBJECT);

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
								"CreateTicketXML:setTOSpecifiedAccount: TicketIDTO.setDssn",
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
						"CreateTicketXML: setTOSpecifiedAccount setAccountItem:" + specifiedAccountTO
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

		// Is there new media data? (moved out of the if/then by JTL for 2.10)
		if (specific.getNewMediaData() != null) {

			List<NewMediaData> mediaList = specific.getNewMediaData();

			for (NewMediaData mediaItem : mediaList) {

				logger.sendEvent(
						"CreateTicketXML: setTOSpecifiedAccount for:" + mediaItem
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
			CreateTicketRequest.Payment aPayment) {

		PaymentTO aPaymentTO = new PaymentTO();

		// Required items
		// PayItem
		aPaymentTO.setPayItem(new BigInteger(aPayment.getPayItem()));

		// Optional Items
		// PayType
		CreateTicketRequest.Payment.PayType payType = aPayment.getPayType();

		if (payType.getCreditCard() != null) { // THIS IS A CREDIT CARD
			CreditCardTO creditCardTO = new CreditCardTO();
			CreateTicketRequest.Payment.PayType.CreditCard creditCard = payType
					.getCreditCard();

			if (creditCard.getCCManual() != null) {

				CreateTicketRequest.Payment.PayType.CreditCard.CCManual ccManual = creditCard
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

				CreateTicketRequest.Payment.PayType.CreditCard.CCSwipe ccSwipe = creditCard
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

				CreateTicketRequest.Payment.PayType.CreditCard.CCWireless ccWireless = creditCard
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
				CreateTicketRequest.Payment.PayType.GiftCard.GCManual gcManual = payType
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

				CreateTicketRequest.Payment.PayType.GiftCard.GCSwipe gcSwipe = payType
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
	 * Gets the JAXB structure for CreateTicketResponse from the Create Ticket Response transfer object.
	 * 
	 * @param createRespTO
	 *            The response transfer object.
	 * 
	 * @return the populated JAXB object
	 * 
	 * @throws JAXBException
	 *             for any parsing failure.
	 */
	public static CreateTicketResponse getJaxb(
			CreateTicketResponseTO createRespTO, DTIErrorTO errorTO) throws JAXBException {

		CreateTicketResponse ctResp = new CreateTicketResponse();

		// Ticket
		if (createRespTO.getTicketList() != null) {
			List<CreateTicketResponse.Ticket> ticketList = ctResp.getTicket();
			ArrayList<TicketTO> ticketListTO = createRespTO.getTicketList();
			setJaxbTicketList(ticketList, ticketListTO, errorTO);
		}

		// Payment
		if (createRespTO.getPaymentList() != null) {
			List<CreateTicketResponse.Payment> paymentList = ctResp
					.getPayment();
			ArrayList<PaymentTO> paymentListTO = createRespTO.getPaymentList();
			setJaxbPaymentList(paymentList, paymentListTO);
		}

		return ctResp;

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
			List<CreateTicketResponse.Payment> paymentList,
			ArrayList<PaymentTO> paymentListTO) throws JAXBException {
		for /* each */(PaymentTO aPaymentTO : /* in */paymentListTO) {

			CreateTicketResponse.Payment aPayment = new CreateTicketResponse.Payment();

			aPayment.setPayItem(aPaymentTO.getPayItem());

			CreateTicketResponse.Payment.PayType aPayType = new CreateTicketResponse.Payment.PayType();

			if (aPaymentTO.getCreditCard() != null) {

				CreateTicketResponse.Payment.PayType.CreditCard aCreditCard = new CreateTicketResponse.Payment.PayType.CreditCard();
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

				CreateTicketResponse.Payment.PayType.GiftCard aGiftCard = new CreateTicketResponse.Payment.PayType.GiftCard();
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
	 * Sets the JAXB ticket list.
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
			List<CreateTicketResponse.Ticket> ticketList,
			ArrayList<TicketTO> ticketListTO, DTIErrorTO errorTO) throws JAXBException {

		for /* each */(TicketTO aTicketTO : /* in */ticketListTO) {

			CreateTicketResponse.Ticket aTicket = new CreateTicketResponse.Ticket();

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

				CreateTicketResponse.Ticket.TktID tktIdObj = new CreateTicketResponse.Ticket.TktID();

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
					CreateTicketResponse.Ticket.TktID.TktDSSN tktDssn = new CreateTicketResponse.Ticket.TktID.TktDSSN();
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

				// Add whatever ticket versions were found to the response.
				qName = new QName("TktID");
				JAXBElement<String> tktId = new JAXBElement(qName,
						tktIdObj.getClass(), tktIdObj);
				aTicket.getTktItemOrTktPartOrTktID().add(tktId);

			}

			// TktTransaction
			if (aTicketTO.getTktTran() != null) {

				CreateTicketResponse.Ticket.TktTransaction tktTran = new CreateTicketResponse.Ticket.TktTransaction();
				TicketTransactionTO tktTranTO = aTicketTO.getTktTran();

				// TktProvider
				if (tktTranTO.getTktProvider() != null) tktTran
						.setTktProvider(tktTranTO.getTktProvider());

				// TranDSSN
				if (tktTranTO.getDssnDate() != null) {

					CreateTicketResponse.Ticket.TktTransaction.TranDSSN tranDssn = new CreateTicketResponse.Ticket.TktTransaction.TranDSSN();

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

			// TktValidity?
			if ((aTicketTO.getTktValidityValidStart() != null) && (aTicketTO
					.getTktValidityValidEnd() != null)) {
				CreateTicketResponse.Ticket.TktValidity tktValidity = new CreateTicketResponse.Ticket.TktValidity();

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

				CreateTicketResponse.Ticket.TktError tktError = new CreateTicketResponse.Ticket.TktError();
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
	 * Sets the transfer object ticket list.
	 * 
	 * @param ticketListTO
	 *            the transfer object ticket list.
	 * @param aTicket
	 *            a JAXB parsed ticket.
	 */
	private static void setTOTicketList(ArrayList<TicketTO> ticketListTO,
			CreateTicketRequest.Ticket aTicket) {

		TicketTO aTicketTO = new TicketTO();

		// Required items
		// TktItem
		aTicketTO.setTktItem(aTicket.getTktItem());

		// ProdCode
		aTicketTO.setProdCode(aTicket.getProdCode());

		// ProdQty
		aTicketTO.setProdQty(aTicket.getProdQty());

		if (aTicket.getProdDemoData() != null) {

			ProdDemoData aTicketDemo = aTicket.getProdDemoData();

			List<TktDemoData> tktDemoDataList = aTicketDemo.getTktDemoData();

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
				GregorianCalendar tempCalendar = UtilXML.convertFromXML(tXCal);
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

				// Telephone
				aTicketDemoTO.setTelephone(aTktDemographic.getTelephone());

				// Email (opt)
				if (aTktDemographic.getEmail() != null) {
					aTicketDemoTO.setEmail(aTktDemographic.getEmail());
				}

				// OptInSolicit (required)
				if (aTktDemographic.getOptInSolicit() != null) {
					if (aTktDemographic.getOptInSolicit().compareTo(
							ValueConstants.YES) == 0) {
						aTicketDemoTO.setOptInSolicit(new Boolean(true));
					}
					else {
						aTicketDemoTO.setOptInSolicit(new Boolean(false));
					}
				}
				else {
					aTicketDemoTO.setOptInSolicit(new Boolean(false));
				}

				aTicketTO.addTicketDemographic(aTicketDemoTO);

			}

		} // tktDemoDataList

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

		// Optional items
		// ProdPrice
		if (aTicket.getProdPrice() != null) aTicketTO
				.setProdPrice(new BigDecimal(aTicket.getProdPrice()));

		// TktValidity - Added as of DTI 2.16.1 APMP - JTL
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

		// TktShell
		if (aTicket.getTktShell() != null) aTicketTO.setTktShell(aTicket
				.getTktShell());

		// TktNote
		if (aTicket.getTktNote() != null) aTicketTO.setTktNote(aTicket
				.getTktNote());

		ticketListTO.add(aTicketTO);
	}

}
