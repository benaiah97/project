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

import org.apache.xerces.dom.ElementNSImpl;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.VoidTicketResponseTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.VoucherTO;
import pvt.disney.dti.gateway.request.xsd.VoidTicketRequest;
import pvt.disney.dti.gateway.response.xsd.VoidTicketResponse;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

/**
 * This class is responsible for transforming the JAXB parsed request into a transfer object as well as transforming the transfer object response back into a JAXB structure.
 * 
 * @author lewit019
 * 
 */
public abstract class VoidTicketXML {

	/**
	 * Gets the request transfer object from the parsed JAXB structure.
	 * 
	 * @param vtReq
	 *            the parsed JAXB structure.
	 * 
	 * @return the properly formatted request transfer object.
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 */
	public static VoidTicketRequestTO getTO(VoidTicketRequest vtReq) throws JAXBException {

		VoidTicketRequestTO vtReqTO = new VoidTicketRequestTO();

		// Ticket
		// Ticket List
		List<VoidTicketRequest.Ticket> ticketList = vtReq.getTicket();
		ArrayList<TicketTO> ticketListTO = new ArrayList<TicketTO>();
		for /* each */(VoidTicketRequest.Ticket aTicket : /* in */ticketList) {
			setTOTicketList(ticketListTO, aTicket);
		}
		vtReqTO.setTktList(ticketListTO);

		// Payment
		List<VoidTicketRequest.Payment> paymentList = vtReq.getPayment();
		ArrayList<PaymentTO> paymentListTO = new ArrayList<PaymentTO>();
		for /* each */(VoidTicketRequest.Payment aPayment : /* in */paymentList) {
			setTOPaymentList(paymentListTO, aPayment);
		}
		vtReqTO.setPaymentList(paymentListTO);

		return vtReqTO;
	}

	/**
	 * Gets the jaxb structure from the response transfer object.
	 * 
	 * @param vtRespTO
	 *            The response transfer object.
	 * 
	 * @return the populated jaxb object
	 * 
	 * @throws JAXBException
	 *             for any JAXB parsing issue.
	 */
	public static VoidTicketResponse getJaxb(VoidTicketResponseTO vtRespTO,
			DTIErrorTO errorTO) throws JAXBException {

		VoidTicketResponse vtResp = new VoidTicketResponse();

		// Ticket
		if (vtRespTO.getTktList() != null) {
			List<VoidTicketResponse.Ticket> ticketList = vtResp.getTicket();
			ArrayList<TicketTO> ticketListTO = vtRespTO.getTktList();
			setJaxbTicketList(ticketList, ticketListTO, errorTO);
		}

		return vtResp;
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
			List<VoidTicketResponse.Ticket> ticketList,
			ArrayList<TicketTO> ticketListTO, DTIErrorTO errorTO) throws JAXBException {

		for /* each */(TicketTO aTicketTO : /* in */ticketListTO) {

			VoidTicketResponse.Ticket aTicket = new VoidTicketResponse.Ticket();

			// Ticket Item
			BigInteger tktItemTO = aTicketTO.getTktItem();
			QName qName = new QName("TktItem");
			JAXBElement<BigInteger> tktItem = new JAXBElement(qName,
					tktItemTO.getClass(), tktItemTO);
			aTicket.getTktItemOrTktIDOrTktTransaction().add(tktItem);

			// TktID
			ArrayList<TicketTO.TicketIdType> typeList = aTicketTO.getTicketTypes();

			if (typeList.size() > 0) {

				VoidTicketResponse.Ticket.TktID tktIdObj = new VoidTicketResponse.Ticket.TktID();

				// Tkt DSSN?
				if (typeList.contains(TicketTO.TicketIdType.DSSN_ID)) {
					VoidTicketResponse.Ticket.TktID.TktDSSN tktDssn = new VoidTicketResponse.Ticket.TktID.TktDSSN();
					tktDssn.setTktDate(UtilXML.convertToXML(aTicketTO
							.getDssnDate()));
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
				aTicket.getTktItemOrTktIDOrTktTransaction().add(tktId);

			}

			// TktTransaction
			if (aTicketTO.getTktTran() != null) {

				VoidTicketResponse.Ticket.TktTransaction tktTran = new VoidTicketResponse.Ticket.TktTransaction();
				TicketTransactionTO tktTranTO = aTicketTO.getTktTran();

				// TktProvider
				if (tktTranTO.getTktProvider() != null) {
				   tktTran.setTktProvider(tktTranTO.getTktProvider());
				}

				// TranDSSN
				if (tktTranTO.getDssnDate() != null) {

					VoidTicketResponse.Ticket.TktTransaction.TranDSSN tranDssn = new VoidTicketResponse.Ticket.TktTransaction.TranDSSN();

					// Required Fields
					tranDssn.setTranDate(tktTranTO.getDssnDateString());
					tranDssn.setTranSite(tktTranTO.getDssnSite());
					tranDssn.setTranStation(tktTranTO.getDssnStation());
					tranDssn.setTranNbr(tktTranTO.getDssnNumber());

					tktTran.setTranDSSN(tranDssn);
				}

				// TranNID
				if (tktTranTO.getTranNID() != null) {
				   tktTran.setTranNID(tktTranTO.getTranNID());
				}

				qName = new QName("TktTransaction");
				JAXBElement section = new JAXBElement(qName, tktTran.getClass(), tktTran);
				aTicket.getTktItemOrTktIDOrTktTransaction().add(section);

			}

			// Ticket Error
			if ((errorTO != null) && (errorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET)) {

				VoidTicketResponse.Ticket.TktError tktError = new VoidTicketResponse.Ticket.TktError();
				tktError.setTktErrorCode(errorTO.getErrorCode());
				tktError.setTktErrorType(errorTO.getErrorType());
				tktError.setTktErrorClass(errorTO.getErrorClass());
				tktError.setTktErrorText(errorTO.getErrorText());

				qName = new QName("TktError");
				JAXBElement<String> tktErrorElement = new JAXBElement(qName, tktError.getClass(), tktError);

				aTicket.getTktItemOrTktIDOrTktTransaction().add(tktErrorElement);
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
			VoidTicketRequest.Payment aPayment) {

		PaymentTO aPaymentTO = new PaymentTO();

		// Required items
		// PayItem
		aPaymentTO.setPayItem(new BigInteger(aPayment.getPayItem()));

		// Optional Items
		// PayType
		VoidTicketRequest.Payment.PayType payType = aPayment.getPayType();

		if (payType.getCreditCard() != null) { // THIS IS A CREDIT CARD
			CreditCardTO creditCardTO = new CreditCardTO();
			VoidTicketRequest.Payment.PayType.CreditCard creditCard = payType.getCreditCard();

			if (creditCard.getCCManual() != null) {

				VoidTicketRequest.Payment.PayType.CreditCard.CCManual ccManual = creditCard.getCCManual();

				// Required fields
				creditCardTO.setCcNbr(ccManual.getCCNbr());
				creditCardTO.setCcExpiration(ccManual.getCCExpiration());

				// Optional fields
				if (ccManual.getCCVV() != null) {
				   creditCardTO.setCcVV(ccManual.getCCVV());
				}
				if (ccManual.getCCName() != null) {
				   creditCardTO.setCcName(ccManual.getCCName());
				}
				if (ccManual.getCCStreet() != null) {
				   creditCardTO.setCcStreet(ccManual.getCCStreet());
				}
				if (ccManual.getCCZipcode() != null) {
				   creditCardTO.setCcZipCode(ccManual.getCCZipcode());
				}

				if (ccManual.getCCCAVV() != null) {
					ElementNSImpl nsElement = (ElementNSImpl) ccManual.getCCCAVV();
					String cccavv = nsElement.getFirstChild().getNodeValue();
					creditCardTO.setCcCAVV(cccavv);
				}

				if (ccManual.getCCEcommerce() != null) {
					ElementNSImpl nsElement = (ElementNSImpl) ccManual.getCCEcommerce();
					String eComm = nsElement.getFirstChild().getNodeValue();
					creditCardTO.setCcEcommerce(eComm);
				}

			}
			else if (creditCard.getCCSwipe() != null) {

				VoidTicketRequest.Payment.PayType.CreditCard.CCSwipe ccSwipe = creditCard.getCCSwipe();

				// Required fields
				creditCardTO.setCcTrack1(ccSwipe.getCCTrack1());
				creditCardTO.setCcTrack2(ccSwipe.getCCTrack2());

				// Optional fields
				if (ccSwipe.getCCVV() != null) {
				   creditCardTO.setCcVV(ccSwipe.getCCVV());
				}
				if (ccSwipe.getPosTerminal() != null) {
				   creditCardTO.setPosTermID(ccSwipe.getPosTerminal());
				}
				if (ccSwipe.getExtnlDevSerial() != null) {
				   creditCardTO.setExtnlDevSerial(ccSwipe.getExtnlDevSerial());
				}

			}
			else if (creditCard.getCCWireless() != null) { // as of 2.12

				VoidTicketRequest.Payment.PayType.CreditCard.CCWireless ccWireless = creditCard.getCCWireless();
				creditCardTO.setWireless(true);

				// Required fields
				creditCardTO.setCcTrack1(ccWireless.getCCTrack1());
				creditCardTO.setCcTrack2(ccWireless.getCCTrack2());

				// Optional fields
				if (ccWireless.getCCVV() != null) {
				   creditCardTO.setCcVV(ccWireless.getCCVV());
				}
				if (ccWireless.getPosTerminal() != null) {
				   creditCardTO.setPosTermID(ccWireless.getPosTerminal());
				}
				if (ccWireless.getExtnlDevSerial() != null) {
				   creditCardTO.setExtnlDevSerial(ccWireless.getExtnlDevSerial());
				}
			}

			aPaymentTO.setCreditCard(creditCardTO);

		}
		else if (payType.getVoucher() != null) { // THIS IS A VOUCHER
			VoucherTO voucherTO = new VoucherTO();

			// Required fields
			voucherTO.setMainCode(payType.getVoucher().getMainCode());

			// Optional fields
			if (payType.getVoucher().getUniqueCode() != null) {
			   voucherTO.setUniqueCode(payType.getVoucher().getUniqueCode());
			}

			aPaymentTO.setVoucher(voucherTO);

		}
		else if (payType.getGiftCard() != null) { // THIS IS A GIFT CARD
			GiftCardTO giftCardTO = new GiftCardTO();

			if (payType.getGiftCard().getGCManual() != null) {
				VoidTicketRequest.Payment.PayType.GiftCard.GCManual gcManual = payType
						.getGiftCard().getGCManual();

				// Required fields
				giftCardTO.setGcNbr(gcManual.getGCNbr());

				// Optional fields:
				if (gcManual.getGCStartDate() != null) {
					XMLGregorianCalendar tXCal = (XMLGregorianCalendar) gcManual.getGCStartDate();
					GregorianCalendar tempCalendar = UtilXML.convertFromXML(tXCal);
					giftCardTO.setGcStartDate(tempCalendar);
				}

				aPaymentTO.setGiftCard(giftCardTO);

			}
			else if (payType.getGiftCard().getGCSwipe() != null) {

				VoidTicketRequest.Payment.PayType.GiftCard.GCSwipe gcSwipe = payType.getGiftCard().getGCSwipe();

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
		   aPaymentTO.setPayAmount(new BigDecimal(aPayment.getPayAmount()));
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
			VoidTicketRequest.Ticket aTicket) {

		TicketTO aTicketTO = new TicketTO();

		// Required items
		// TktItem
		aTicketTO.setTktItem(new BigInteger(aTicket.getTktItem()));

		// TktID
		VoidTicketRequest.Ticket.TktID tktId = aTicket.getTktID();
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
						VoidTicketRequest.Ticket.TktID.Mag tktMag = tktId
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
							VoidTicketRequest.Ticket.TktID.TktDSSN tktDssn = tktId
									.getTktDSSN();
							String dssnDateString = tktDssn.getTktDate();
							GregorianCalendar tempCalendar = CustomDataTypeConverter
									.parseYYYYMMDDDate(dssnDateString);
							aTicketTO.setDssn(tempCalendar,
									tktDssn.getTktSite(),
									tktDssn.getTktStation(),
									tktDssn.getTktNbr());
						}
					}
				}
			}
		}

		ticketListTO.add(aTicketTO);

		return;
	}

}
