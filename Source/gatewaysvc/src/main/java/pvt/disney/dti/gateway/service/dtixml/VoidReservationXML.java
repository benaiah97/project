package pvt.disney.dti.gateway.service.dtixml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.data.VoidReservationRequestTO;
import pvt.disney.dti.gateway.data.VoidReservationResponseTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.request.xsd.VoidReservationRequest;
import pvt.disney.dti.gateway.request.xsd.VoidReservationRequest.VoidedReservation;
import pvt.disney.dti.gateway.response.xsd.VoidReservationResponse;

/**
 * 
 * @author lewit019
 *
 */
public abstract class VoidReservationXML {

  /**
   * When passed the JAXB object, return the DTI application object.
   * 
   * @param queryReq
   *            the JAXB version of the object
   * @return the DTI application object
   * @throws JAXBException
   *             should any parsing errors occur.
   */
  public static VoidReservationRequestTO getTO(
      VoidReservationRequest voidResReq) throws JAXBException {

    // Create the void reservation request transfer object and set request type.
    VoidReservationRequestTO queryResReqTO = new VoidReservationRequestTO();

    queryResReqTO.setRequestType(voidResReq.getRequestType());

    // Get JAXB parsed object.
    VoidedReservation reservation = voidResReq.getVoidedReservation();

    // Get key fields
    
    // ResCode
    if (reservation.getResCode() != null) {
      queryResReqTO.setResCode(reservation.getResCode());
    }
    
    // Payload ID
    if (reservation.getPayloadID() != null) {
      queryResReqTO.setPayloadID(reservation.getPayloadID());
    }

    return queryResReqTO;

  }

  /**
   * Gets the JAXB structure from the response transfer object.
   * 
   * @param voidResRespTO
   *            The response transfer object.
   * 
   * @return the populated JAXB object
   * 
   * @throws JAXBException
   *             for any JAXB parsing issue.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static VoidReservationResponse getJaxb(
      VoidReservationResponseTO voidResRespTO, DTIErrorTO errorTO) throws JAXBException {

    VoidReservationResponse resResp = new VoidReservationResponse();

    // Response Type
    resResp.setResponseType(voidResRespTO.getResponseType()); // TODO NPE!!!!

    // Ticket
    if (voidResRespTO.getTicketList() != null) {
      List<VoidReservationResponse.Ticket> ticketList = resResp
          .getTicket();
      ArrayList<TicketTO> ticketListTO = voidResRespTO.getTicketList();
      setJaxbTicketList(ticketList, ticketListTO, errorTO);
    }

    // Product
    if (voidResRespTO.getProductList() != null) {

      List<VoidReservationResponse.Product> productList = resResp
          .getProduct();
      ArrayList<ProductTO> productListTO = voidResRespTO.getProductList();

      for /* each */(ProductTO aProductTO : /* in */productListTO) {

        VoidReservationResponse.Product aProduct = new VoidReservationResponse.Product();

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

        if (aProductTO.getProdDescription() != null) {
           aProduct.setProdDescription(aProductTO.getProdDescription());
        }

        if (aProductTO.getProdReceiptMsg() != null) {
           aProduct.setProdReceiptMsg(aProductTO.getProdReceiptMsg());
        }

        productList.add(aProduct);
      }
    }

    // Payment
    if (voidResRespTO.getPaymentList() != null) {
      List<VoidReservationResponse.Payment> paymentList = resResp
          .getPayment();
      ArrayList<PaymentTO> paymentListTO = voidResRespTO.getPaymentList();
      setJaxbPaymentList(paymentList, paymentListTO);
    }

    // Receipt
    if (voidResRespTO.getReceiptMessage() != null) {
      VoidReservationResponse.Receipt receipt = new VoidReservationResponse.Receipt();
      String message = voidResRespTO.getReceiptMessage();

      QName qName = new QName("Message");
      JAXBElement<String> rcpMsg = new JAXBElement(qName,message.getClass(), message);
      receipt.getTotalOrTaxTotalOrTax1().add(rcpMsg);

      resResp.setReceipt(receipt);
    }

    // Reservation
    if (voidResRespTO.getReservation() != null) {

      VoidReservationResponse.Reservation reservation = new VoidReservationResponse.Reservation();
      ReservationTO reservationTO = voidResRespTO.getReservation();

      // Optional fields
      if (reservationTO.getResCode() != null) {
         reservation.setResCode(reservationTO.getResCode());
      }
      if (reservationTO.getResNumber() != null) {
         reservation.setResNumber(reservationTO.getResNumber());
      }
      if (reservationTO.getResCreateDate() != null) {
         reservation.setResCreateDate(UtilXML.convertToXML(reservationTO.getResCreateDate()));
      }
      if (reservationTO.getResPickupDate() != null) {
         reservation.setResPickupDate(UtilXML.convertToXML(reservationTO.getResPickupDate()));
      }
      if (reservationTO.getResPickupArea() != null) {
         reservation.setResPickupArea(reservationTO.getResPickupArea());
      }
      if (reservationTO.getResSalesType() != null) {
         reservation.setResSalesType(reservationTO.getResSalesType());
      }

      resResp.setReservation(reservation);
    }

    // ClientData
    if (voidResRespTO.getClientData() != null) {

      VoidReservationResponse.ClientData clientData = new VoidReservationResponse.ClientData();

      // Optional field
      if (voidResRespTO.getClientData().getClientId() != null) {
         clientData.setClientId(voidResRespTO.getClientData().getClientId());
      }

      resResp.setClientData(clientData);
    }

    return resResp;
  }

  /**
   * Sets the JAXB payment list.
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
      List<VoidReservationResponse.Payment> paymentList,
      ArrayList<PaymentTO> paymentListTO) throws JAXBException {
     
    for /* each */(PaymentTO aPaymentTO : /* in */paymentListTO) {

      VoidReservationResponse.Payment aPayment = new VoidReservationResponse.Payment();

      aPayment.setPayItem(aPaymentTO.getPayItem());

      VoidReservationResponse.Payment.PayType aPayType = new VoidReservationResponse.Payment.PayType();

      if (aPaymentTO.getCreditCard() != null) {

        VoidReservationResponse.Payment.PayType.CreditCard aCreditCard = new VoidReservationResponse.Payment.PayType.CreditCard();
        CreditCardTO aCreditCardTO = aPaymentTO.getCreditCard();

        // Required fields
        aCreditCard.setCCAuthCode(aCreditCardTO.getCcAuthCode());

        // Optional fields
        if (aCreditCardTO.getCcAuthNumber() != null) {
           aCreditCard.setCCAuthNumber(aCreditCardTO.getCcAuthNumber());
        }
        if (aCreditCardTO.getCcAuthSysResponse() != null) {
           aCreditCard.setCCAuthSysResponse(aCreditCardTO.getCcAuthSysResponse());
        }
        if (aCreditCardTO.getCcNumber() != null) {
          aCreditCard.setCCNumber(aCreditCardTO.getCcNumber());
        }

        aPayType.setCreditCard(aCreditCard);

      }
      else if (aPaymentTO.getGiftCard() != null) {

        VoidReservationResponse.Payment.PayType.GiftCard aGiftCard = new VoidReservationResponse.Payment.PayType.GiftCard();
        GiftCardTO aGiftCardTO = aPaymentTO.getGiftCard();

        // Required fields
        aGiftCard.setGCAuthCode(aGiftCardTO.getGcAuthCode());

        // Optional fields
        if (aGiftCardTO.getGcAuthNumber() != null) {
           aGiftCard.setGCAuthNumber(aGiftCardTO.getGcAuthNumber());
        }
        if (aGiftCardTO.getGcAuthSysResponse() != null) {
           aGiftCard.setGCAuthSysResponse(aGiftCardTO.getGcAuthSysResponse());
        }
        if (aGiftCardTO.getGcNumber() != null) {
           aGiftCard.setGCNumber(aGiftCardTO.getGcNumber());
        }
        if (aGiftCardTO.getGcRemainingBalance() != null) {
           aGiftCard.setGCRemainingBalance(aGiftCardTO.getGcRemainingBalance());
        }
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
      List<VoidReservationResponse.Ticket> ticketList,
      ArrayList<TicketTO> ticketListTO, DTIErrorTO errorTO) throws JAXBException {

    for /* each */(TicketTO aTicketTO : /* in */ticketListTO) {

      VoidReservationResponse.Ticket aTicket = new VoidReservationResponse.Ticket();

      // Ticket Item
      BigInteger tktItemTO = aTicketTO.getTktItem();
      QName qName = new QName("TktItem");
      JAXBElement<BigInteger> tktItem = new JAXBElement(qName,tktItemTO.getClass(), tktItemTO);
      aTicket.getTktItemOrTktIDOrTktPrice().add(tktItem);

      // TktID
      ArrayList<TicketTO.TicketIdType> typeList = aTicketTO.getTicketTypes();

      if (typeList.size() > 0) {

        VoidReservationResponse.Ticket.TktID tktIdObj = new VoidReservationResponse.Ticket.TktID();

        // Magnetic Code?
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

        // Ticket DSSN?
        if (typeList.contains(TicketTO.TicketIdType.DSSN_ID)) {
          VoidReservationResponse.Ticket.TktID.TktDSSN tktDssn = new VoidReservationResponse.Ticket.TktID.TktDSSN();
          tktDssn.setTktDate(aTicketTO.getDssnDateString());
          tktDssn.setTktNbr(aTicketTO.getDssnNumber());
          tktDssn.setTktSite(aTicketTO.getDssnSite());
          tktDssn.setTktStation(aTicketTO.getDssnStation());
          qName = new QName("TktDSSN");
          JAXBElement<String> tktDssnElement = new JAXBElement(qName,
              tktDssn.getClass(), tktDssn);
          tktIdObj.getMagOrBarcodeOrTktDSSN().add(tktDssnElement);
        }

        // Ticket NID ?
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
        aTicket.getTktItemOrTktIDOrTktPrice().add(tktId);

      }

      // TktPrice?
      if (aTicketTO.getTktPrice() != null) {
        BigDecimal tktPriceTO = aTicketTO.getTktPrice();
        qName = new QName("TktPrice");
        JAXBElement<String> tktPrice = new JAXBElement(qName,
            tktPriceTO.getClass(), tktPriceTO);
        aTicket.getTktItemOrTktIDOrTktPrice().add(tktPrice);
      }

      // TktTax?
      if (aTicketTO.getTktTax() != null) {
        BigDecimal tktTaxTO = aTicketTO.getTktTax();
        qName = new QName("TktTax");
        JAXBElement<String> tktTax = new JAXBElement(qName,
            tktTaxTO.getClass(), tktTaxTO);
        aTicket.getTktItemOrTktIDOrTktPrice().add(tktTax);
      }

      // TktValidity?
      if ((aTicketTO.getTktValidityValidStart() != null) && (aTicketTO
          .getTktValidityValidEnd() != null)) {
        VoidReservationResponse.Ticket.TktValidity tktValidity = new VoidReservationResponse.Ticket.TktValidity();

        XMLGregorianCalendar xCalDate = UtilXML.convertToXML(aTicketTO
            .getTktValidityValidStart());
        tktValidity.setValidStart(xCalDate);

        xCalDate = UtilXML.convertToXML(aTicketTO
            .getTktValidityValidEnd());
        tktValidity.setValidEnd(xCalDate);

        qName = new QName("TktValidity");
        JAXBElement<String> tktValidityElement = new JAXBElement(qName,
            tktValidity.getClass(), tktValidity);
        aTicket.getTktItemOrTktIDOrTktPrice().add(tktValidityElement);

      }

      // TktProdCode ?
      if (aTicketTO.getProdCode() != null) {
        qName = new QName("TktProdCode");
        JAXBElement<String> tktProdCodeElement = new JAXBElement(qName,
            aTicketTO.getProdCode().getClass(),
            aTicketTO.getProdCode());
        aTicket.getTktItemOrTktIDOrTktPrice().add(tktProdCodeElement);
      }

      // Entitlement Account Id? 2.15.1 BIEST001
      if (aTicketTO.getAccountId() != null) {
        String tktAccountId = aTicketTO.getAccountId();
        qName = new QName("AccountId");
        JAXBElement<String> accountId = new JAXBElement(qName,
            tktAccountId.getClass(), tktAccountId);
        aTicket.getTktItemOrTktIDOrTktPrice().add(accountId);
      }

      // Ticket Error ?
      if ((errorTO != null) && (errorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET)) {

        VoidReservationResponse.Ticket.TktError tktError = new VoidReservationResponse.Ticket.TktError();
        tktError.setTktErrorCode(errorTO.getErrorCode());
        tktError.setTktErrorType(errorTO.getErrorType());
        tktError.setTktErrorClass(errorTO.getErrorClass());
        tktError.setTktErrorText(errorTO.getErrorText());

        qName = new QName("TktError");
        JAXBElement<String> tktErrorElement = new JAXBElement(qName,
            tktError.getClass(), tktError);

        aTicket.getTktItemOrTktIDOrTktPrice().add(tktErrorElement);
      }

      ticketList.add(aTicket);
    }
  }

}
