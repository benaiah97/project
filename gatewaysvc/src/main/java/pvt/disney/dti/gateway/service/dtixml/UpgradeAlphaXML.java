package pvt.disney.dti.gateway.service.dtixml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaResponseTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.VoucherTO;
import pvt.disney.dti.gateway.request.xsd.UpgradeAlphaRequest;
import pvt.disney.dti.gateway.response.xsd.UpgradeAlphaResponse;

/**
 * This class is responsible for transforming the JAXB parsed request into a transfer object as well as transforming the transfer object response back into a JAXB structure.
 * 
 * @author lewit019
 * 
 */
public class UpgradeAlphaXML {

	/**
	 * Gets the request transfer object from the parsed JAXB structure.
	 * 
	 * @param uaReq
	 *            the parsed JAXB structure.
	 * 
	 * @return the properly formatted request transfer object.
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 */
	public static UpgradeAlphaRequestTO getTO(UpgradeAlphaRequest uaReq) throws JAXBException {

		UpgradeAlphaRequestTO uaReqTO = new UpgradeAlphaRequestTO();

		// Ticket List
		List<UpgradeAlphaRequest.Ticket> ticketList = uaReq.getTicket();
		ArrayList<TicketTO> ticketListTO = new ArrayList<TicketTO>();
		for /* each */(UpgradeAlphaRequest.Ticket aTicket : /* in */ticketList) {
			addTicketToTOList(ticketListTO, aTicket);
		}
		uaReqTO.setTktList(ticketListTO);

		// Payment
		List<UpgradeAlphaRequest.Payment> paymentList = uaReq.getPayment();
		ArrayList<PaymentTO> paymentListTO = new ArrayList<PaymentTO>();
		for /* each */(UpgradeAlphaRequest.Payment aPayment : /* in */paymentList) {
			setTOPaymentList(paymentListTO, aPayment);
		}
		uaReqTO.setPaymentList(paymentListTO);

		// Eligibility
		if (uaReq.getEligibility() != null) {
			UpgradeAlphaRequest.Eligibility elig = uaReq.getEligibility();
			uaReqTO.setEligibilityGroup(elig.getGroup());
			uaReqTO.setEligibilityMember(elig.getMember());
		}

		// IncludeVisualId (as of 2.15 JTL)
		Boolean isVisualId = uaReq.isIncludeVisualId();
		if (isVisualId != null) {
			if (isVisualId.booleanValue()) {
				uaReqTO.setIncludeVisualId(true);
			}
		}

		return uaReqTO;
	}

	/**
	 * Gets the jaxb structure from the response transfer object.
	 * 
	 * @param uaRespTO
	 *            The response transfer object.
	 * 
	 * @return the populated jaxb object
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static UpgradeAlphaResponse getJaxb(UpgradeAlphaResponseTO uaRespTO,
			DTIErrorTO errorTO) throws JAXBException {

		UpgradeAlphaResponse uaResp = new UpgradeAlphaResponse();

		// Ticket
		if (uaRespTO.getTktList() != null) {
			List<UpgradeAlphaResponse.Ticket> ticketList = uaResp.getTicket();
			ArrayList<TicketTO> ticketListTO = uaRespTO.getTktList();
			setJaxbTicketList(ticketList, ticketListTO, errorTO);
		}

		// Product
		if (uaRespTO.getProductList() != null) {

			List<UpgradeAlphaResponse.Product> productList = uaResp
					.getProduct();
			ArrayList<ProductTO> productListTO = uaRespTO.getProductList();

			for /* each */(ProductTO aProductTO : /* in */productListTO) {

				UpgradeAlphaResponse.Product aProduct = new UpgradeAlphaResponse.Product();

				// Required fields
				aProduct.setProdItem(aProductTO.getProdItem());
				aProduct.setProdCode(aProductTO.getProdCode());
				aProduct.setProdQty(aProductTO.getProdQty());
				aProduct.setProdParts(aProductTO.getProdParts());
				aProduct.setProdPrice(aProductTO.getProdPrice());
				aProduct.setProdTax1(aProductTO.getProdTax1());

				// Optional fields
				if (aProductTO.getProdReceiptMsg() != null) aProduct
						.setProdReceiptMsg(aProductTO.getProdReceiptMsg());

				productList.add(aProduct);
			}
		}

		// Payment
		if (uaRespTO.getPaymentList() != null) {
			List<UpgradeAlphaResponse.Payment> paymentList = uaResp
					.getPayment();
			ArrayList<PaymentTO> paymentListTO = uaRespTO.getPaymentList();
			setJaxbPaymentList(paymentList, paymentListTO);
		}

		// Receipt
		if (uaRespTO.getReceiptMessage() != null) {
			UpgradeAlphaResponse.Receipt receipt = new UpgradeAlphaResponse.Receipt();
			String message = uaRespTO.getReceiptMessage();

			QName qName = new QName("Message");
			JAXBElement<String> rcpMsg = new JAXBElement(qName,
					message.getClass(), message);
			receipt.getTotalOrTaxTotalOrTax1().add(rcpMsg);

			uaResp.setReceipt(receipt);
		}

		return uaResp;
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
			List<UpgradeAlphaResponse.Payment> paymentList,
			ArrayList<PaymentTO> paymentListTO) throws JAXBException {

		for /* each */(PaymentTO aPaymentTO : /* in */paymentListTO) {

			UpgradeAlphaResponse.Payment aPayment = new UpgradeAlphaResponse.Payment();

			aPayment.setPayItem(aPaymentTO.getPayItem());

			UpgradeAlphaResponse.Payment.PayType aPayType = new UpgradeAlphaResponse.Payment.PayType();

			if (aPaymentTO.getCreditCard() != null) {

				UpgradeAlphaResponse.Payment.PayType.CreditCard aCreditCard = new UpgradeAlphaResponse.Payment.PayType.CreditCard();
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

				UpgradeAlphaResponse.Payment.PayType.GiftCard aGiftCard = new UpgradeAlphaResponse.Payment.PayType.GiftCard();
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
			List<UpgradeAlphaResponse.Ticket> ticketList,
			ArrayList<TicketTO> ticketListTO, DTIErrorTO errorTO) throws JAXBException {

		for /* each */(TicketTO aTicketTO : /* in */ticketListTO) {

			UpgradeAlphaResponse.Ticket aTicket = new UpgradeAlphaResponse.Ticket();

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

				UpgradeAlphaResponse.Ticket.TktID tktIdObj = new UpgradeAlphaResponse.Ticket.TktID();

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
					UpgradeAlphaResponse.Ticket.TktID.TktDSSN tktDssn = new UpgradeAlphaResponse.Ticket.TktID.TktDSSN();
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

				UpgradeAlphaResponse.Ticket.TktTransaction tktTran = new UpgradeAlphaResponse.Ticket.TktTransaction();
				TicketTransactionTO tktTranTO = aTicketTO.getTktTran();

				// TktProvider
				if (tktTranTO.getTktProvider() != null) tktTran
						.setTktProvider(tktTranTO.getTktProvider());

				// TranDSSN
				if (tktTranTO.getDssnDate() != null) {

					UpgradeAlphaResponse.Ticket.TktTransaction.TranDSSN tranDssn = new UpgradeAlphaResponse.Ticket.TktTransaction.TranDSSN();

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

			// VisualId ?
			if (aTicketTO.getVisualId() != null) {
				String visualId = aTicketTO.getVisualId();
				qName = new QName("VisualId");
				JAXBElement<String> visId = new JAXBElement(qName,
						visualId.getClass(), visualId);
				aTicket.getTktItemOrTktPartOrTktID().add(visId);
			}

			// Ticket Error
			if ((errorTO != null) && (errorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET)) {

				UpgradeAlphaResponse.Ticket.TktError tktError = new UpgradeAlphaResponse.Ticket.TktError();
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
	 * Sets the transfer object payment list.
	 * 
	 * @param paymentListTO
	 *            the transfer object payment list.
	 * @param aPayment
	 *            the JAXB payment list.
	 */
	private static void setTOPaymentList(ArrayList<PaymentTO> paymentListTO,
			UpgradeAlphaRequest.Payment aPayment) {

		PaymentTO aPaymentTO = new PaymentTO();

		// Required items
		// PayItem
		aPaymentTO.setPayItem(new BigInteger(aPayment.getPayItem()));

		// Optional Items
		// PayType
		UpgradeAlphaRequest.Payment.PayType payType = aPayment.getPayType();

		if (payType.getCreditCard() != null) { // THIS IS A CREDIT CARD
			CreditCardTO creditCardTO = new CreditCardTO();
			UpgradeAlphaRequest.Payment.PayType.CreditCard creditCard = payType
					.getCreditCard();

			if (creditCard.getCCManual() != null) {

				UpgradeAlphaRequest.Payment.PayType.CreditCard.CCManual ccManual = creditCard
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

				UpgradeAlphaRequest.Payment.PayType.CreditCard.CCSwipe ccSwipe = creditCard
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

				UpgradeAlphaRequest.Payment.PayType.CreditCard.CCWireless ccWireless = creditCard
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
				UpgradeAlphaRequest.Payment.PayType.GiftCard.GCManual gcManual = payType
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

				UpgradeAlphaRequest.Payment.PayType.GiftCard.GCSwipe gcSwipe = payType
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
		if (aPayment.getPayAmount() != null) aPaymentTO
				.setPayAmount(new BigDecimal(aPayment.getPayAmount()));

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
	private static void addTicketToTOList(ArrayList<TicketTO> ticketListTO,
			UpgradeAlphaRequest.Ticket aTicket) {

		TicketTO aTicketTO = new TicketTO();

		// Required items
		// TktItem
		aTicketTO.setTktItem(aTicket.getTktItem());
		// ProdCode
		aTicketTO.setProdCode(aTicket.getProdCode());
		// Implicit: Product Quantity (always 1 for upgrade alpha).
		aTicketTO.setProdQty(new BigInteger("1"));

		// TktID
		UpgradeAlphaRequest.Ticket.TktID tktId = aTicket.getTktID();
		if (tktId.getBarcode() != null) {
			aTicketTO.setBarCode(tktId.getBarcode());
		}
		else {
			if (tktId.getExternal() != null) {
				aTicketTO.setExternal(tktId.getExternal());
			}
			else {
				if (tktId.getTktNID() != null) {
					aTicketTO.setTktNID(tktId.getTktNID());
				}
				else {
					if (tktId.getMag() != null) {
						UpgradeAlphaRequest.Ticket.TktID.Mag tktMag = tktId
								.getMag();
						String mag1 = tktMag.getMagTrack1();
						String mag2 = tktMag.getMagTrack2();

						if (mag2 != null) {
							aTicketTO.setMag(mag1, mag2);
						}
						else {
							aTicketTO.setMag(mag1);
						}
					}
					else {
						if (tktId.getTktDSSN() != null) {
							UpgradeAlphaRequest.Ticket.TktID.TktDSSN tktDssn = tktId
									.getTktDSSN();
							GregorianCalendar tempCalendar = UtilXML
									.convertFromXML(tktDssn.getTktDate());
							aTicketTO.setDssn(tempCalendar,
									tktDssn.getTktSite(),
									tktDssn.getTktStation(),
									tktDssn.getTktNbr());
						}
					}
				}
			}
		}

		// Optional items
		// ProdPrice
		if (aTicket.getProdPrice() != null) aTicketTO
				.setProdPrice(new BigDecimal(aTicket.getProdPrice()));

		// TktStatus
		if (aTicket.getTktStatus() != null) {
			ArrayList<TicketTO.TktStatusTO> tktStatusListTO = aTicketTO
					.getTktStatusList();
			List<UpgradeAlphaRequest.Ticket.TktStatus> tktStatusList = aTicket
					.getTktStatus();
			for /* each */(UpgradeAlphaRequest.Ticket.TktStatus aTktStatus : /* in */tktStatusList) {
				TicketTO.TktStatusTO aTktStatusTO = aTicketTO.new TktStatusTO();
				aTktStatusTO.setStatusItem(aTktStatus.getStatusItem());
				aTktStatusTO.setStatusValue(aTktStatus.getStatusValue());
				tktStatusListTO.add(aTktStatusTO);
			}
		}

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

		// TktShell
		if (aTicket.getTktShell() != null) aTicketTO.setTktShell(aTicket
				.getTktShell());

		// TktMarket
		if (aTicket.getTktMarket() != null) aTicketTO.setTktMarket(aTicket
				.getTktMarket());

		// TktNote
		if (aTicket.getTktNote() != null) aTicketTO.setTktNote(aTicket
				.getTktNote());

		ticketListTO.add(aTicketTO);
	}

}
