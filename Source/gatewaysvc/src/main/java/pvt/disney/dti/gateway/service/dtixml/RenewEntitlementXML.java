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
import pvt.disney.dti.gateway.data.RenewEntitlementRequestTO;
import pvt.disney.dti.gateway.data.RenewEntitlementResponseTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentCreditCardTO;
import pvt.disney.dti.gateway.data.common.InstallmentDemographicsTO;
import pvt.disney.dti.gateway.data.common.InstallmentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO.PaymentType;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.VoucherTO;
import pvt.disney.dti.gateway.request.xsd.RenewEntitlementRequest;
import pvt.disney.dti.gateway.request.xsd.RenewEntitlementRequest.Ticket.ProdDemoData;
import pvt.disney.dti.gateway.request.xsd.RenewEntitlementRequest.Ticket.ProdDemoData.TktDemoData;
import pvt.disney.dti.gateway.response.xsd.RenewEntitlementResponse;

/**
 * This class is responsible for transforming the JAXB parsed request into a transfer object as well as transforming the transfer object response back into a JAXB structure.
 * 
 * @author lewit019
 * 
 */
public abstract class RenewEntitlementXML {

  /**
   * Gets the request transfer object from the parsed JAXB structure.
   * 
   * @param renewEntReq
   *            the Reservation Request from JAXB.
   * 
   * @return the properly formatted request transfer object.
   * 
   * @throws JAXBException
   *             for any parsing failure.
   */
  public static RenewEntitlementRequestTO getTO(
      RenewEntitlementRequest renewEntReq) throws JAXBException {

    RenewEntitlementRequestTO renewEntReqTO = new RenewEntitlementRequestTO();

    // Ticket List
    List<RenewEntitlementRequest.Ticket> ticketList = renewEntReq
        .getTicket();
    ArrayList<TicketTO> ticketListTO = new ArrayList<TicketTO>();
    for /* each */(RenewEntitlementRequest.Ticket aTicket : /* in */ticketList) {
      setTOTicketList(ticketListTO, aTicket);
    }

    renewEntReqTO.setTktList(ticketListTO);

    // Eligibility
    if (renewEntReq.getEligibility() != null) {
      RenewEntitlementRequest.Eligibility elig = renewEntReq
          .getEligibility();
      renewEntReqTO.setEligibilityGroup(elig.getGroup());
      renewEntReqTO.setEligibilityMember(elig.getMember());
    }

    // Payment
    List<RenewEntitlementRequest.Payment> paymentList = renewEntReq
        .getPayment();
    ArrayList<PaymentTO> paymentListTO = new ArrayList<PaymentTO>();
    for /* each */(RenewEntitlementRequest.Payment aPayment : /* in */paymentList) {
      setTOPaymentList(paymentListTO, aPayment);
    }
    renewEntReqTO.setPaymentList(paymentListTO);

    // Reservation
    if (renewEntReq.getReservation() != null) {
      RenewEntitlementRequest.Reservation renewEnt = renewEntReq
          .getReservation();
      ReservationTO resvTO = new ReservationTO();
      List<JAXBElement<?>> aReservationList = renewEnt
          .getResCodeOrResCreateDateOrResPickupDate();
      for /* each */(JAXBElement<?> aReservationEntry : /* in */aReservationList) {
        setTOReservation(resvTO, aReservationEntry);
      }
      renewEntReqTO.setReservation(resvTO);
    }

    // ClientData
    if (renewEntReq.getClientData() != null) {
      RenewEntitlementRequest.ClientData clientData = renewEntReq
          .getClientData();
      ClientDataTO clientDataTO = new ClientDataTO();
      setTOClientData(clientData, clientDataTO);
      renewEntReqTO.setClientData(clientDataTO);

    }

    return renewEntReqTO;
  }

  /**
   * Gets the jaxb structure from the response transfer object.
   * 
   * @param renewRespTO
   *            The response transfer object.
   * 
   * @return the populated jaxb object
   * 
   * @throws JAXBException
   *             for any JAXB parsing issue.
   */
  public static RenewEntitlementResponse getJaxb(
      RenewEntitlementResponseTO renewRespTO, DTIErrorTO errorTO) throws JAXBException {

    RenewEntitlementResponse renewResp = new RenewEntitlementResponse();

    // Ticket
    if (renewRespTO.getTicketList() != null) {
      List<RenewEntitlementResponse.Ticket> ticketList = renewResp
          .getTicket();
      ArrayList<TicketTO> ticketListTO = renewRespTO.getTicketList();
      setJaxbTicketList(ticketList, ticketListTO, errorTO);
    }

    // Product
    if (renewRespTO.getProductList() != null) {

      List<RenewEntitlementResponse.Product> productList = renewResp
          .getProduct();
      ArrayList<ProductTO> productListTO = renewRespTO.getProductList();

      for /* each */(ProductTO aProductTO : /* in */productListTO) {

        RenewEntitlementResponse.Product aProduct = new RenewEntitlementResponse.Product();

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
    if (renewRespTO.getPaymentList() != null) {
      List<RenewEntitlementResponse.Payment> paymentList = renewResp
          .getPayment();
      ArrayList<PaymentTO> paymentListTO = renewRespTO.getPaymentList();
      setJaxbPaymentList(paymentList, paymentListTO);
    }

    // Renewal:ContractID
    RenewEntitlementResponse.Renewal renewal = new RenewEntitlementResponse.Renewal();
    ;
    if (renewRespTO.getContractId() != null) {
      renewal.setContractId(renewRespTO.getContractId());
    }

    // Renewal:TktTransaction
    if (renewRespTO.getTktTran() != null) {

      RenewEntitlementResponse.Renewal.TktTransaction tktTran = new RenewEntitlementResponse.Renewal.TktTransaction();
      TicketTransactionTO tktTranTO = renewRespTO.getTktTran();

      // TranDSSN
      if (tktTranTO.getDssnDate() != null) {

        RenewEntitlementResponse.Renewal.TktTransaction.TranDSSN tranDssn = new RenewEntitlementResponse.Renewal.TktTransaction.TranDSSN();

        // Required Fields
        tranDssn.setTranDate(tktTranTO.getDssnDateString());
        tranDssn.setTranSite(tktTranTO.getDssnSite());
        tranDssn.setTranStation(tktTranTO.getDssnStation());
        tranDssn.setTranNbr(tktTranTO.getDssnNumber());

        tktTran.setTranDSSN(tranDssn);
      }

      // TranNID
      if (tktTranTO.getTranNID() != null) tktTran.setTranNID(tktTranTO
          .getTranNID());

      renewal.setTktTransaction(tktTran);

    }

    if ((renewal.getContractId() != null) || (renewal.getTktTransaction() != null)) {
      renewResp.setRenewal(renewal);
    }

    // ClientData
    if (renewRespTO.getClientData() != null) {

      RenewEntitlementResponse.ClientData clientData = new RenewEntitlementResponse.ClientData();

      // Optional field
      if (renewRespTO.getClientData().getClientId() != null) clientData
          .setClientId(renewRespTO.getClientData().getClientId());

      renewResp.setClientData(clientData);
    }

    return renewResp;
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
      List<RenewEntitlementResponse.Payment> paymentList,
      ArrayList<PaymentTO> paymentListTO) throws JAXBException {

    for /* each */(PaymentTO aPaymentTO : /* in */paymentListTO) {

      // As of 2.15, JTL - Installment isn't returned in the payment clause.
      if (aPaymentTO.getPayType() == PaymentType.INSTALLMENT) continue; // Skip to the next pay type.

      RenewEntitlementResponse.Payment aPayment = new RenewEntitlementResponse.Payment();

      aPayment.setPayItem(aPaymentTO.getPayItem());

      RenewEntitlementResponse.Payment.PayType aPayType = new RenewEntitlementResponse.Payment.PayType();

      if (aPaymentTO.getCreditCard() != null) {

        RenewEntitlementResponse.Payment.PayType.CreditCard aCreditCard = new RenewEntitlementResponse.Payment.PayType.CreditCard();
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

        RenewEntitlementResponse.Payment.PayType.GiftCard aGiftCard = new RenewEntitlementResponse.Payment.PayType.GiftCard();
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
      List<RenewEntitlementResponse.Ticket> ticketList,
      ArrayList<TicketTO> ticketListTO, DTIErrorTO errorTO) throws JAXBException {

    for /* each */(TicketTO aTicketTO : /* in */ticketListTO) {

      RenewEntitlementResponse.Ticket aTicket = new RenewEntitlementResponse.Ticket();

      // Ticket Item
      BigInteger tktItemTO = aTicketTO.getTktItem();
      QName qName = new QName("TktItem");
      JAXBElement<BigInteger> tktItem = new JAXBElement(qName,
          tktItemTO.getClass(), tktItemTO);
      aTicket.getTktItemOrTktIDOrTktPrice().add(tktItem);

      // TktID
      ArrayList<TicketTO.TicketIdType> typeList = aTicketTO
          .getTicketTypes();

      if (typeList.size() > 0) {

        RenewEntitlementResponse.Ticket.TktID tktIdObj = new RenewEntitlementResponse.Ticket.TktID();

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
          RenewEntitlementResponse.Ticket.TktID.TktDSSN tktDssn = new RenewEntitlementResponse.Ticket.TktID.TktDSSN();
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
        RenewEntitlementResponse.Ticket.TktValidity tktValidity = new RenewEntitlementResponse.Ticket.TktValidity();

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

      // Ticket Error
      if ((errorTO != null) && (errorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET)) {

        RenewEntitlementResponse.Ticket.TktError tktError = new RenewEntitlementResponse.Ticket.TktError();
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

  /**
   * Sets the to client data.
   * 
   * @param clientData
   *            the client data parsed from JAXB
   * @param clientDataTO
   *            the client data transfer object
   */
  private static void setTOClientData(
      RenewEntitlementRequest.ClientData clientData,
      ClientDataTO clientDataTO) {

    // Optional fields
    if (clientData.getClientType() != null) clientDataTO
        .setClientType(clientData.getClientType());
    if (clientData.getClientCategory() != null) clientDataTO
        .setClientCategory(clientData.getClientCategory());
    if (clientData.getDemoLanguage() != null) clientDataTO
        .setDemoLanguage(clientData.getDemoLanguage());

    // Optional Demographics
    if (clientData.getDemoData() != null) {

      RenewEntitlementRequest.ClientData.DemoData demoData = clientData
          .getDemoData();

      if (demoData.getBill() != null) {
        DemographicsTO demoTO = new DemographicsTO();
        RenewEntitlementRequest.ClientData.DemoData.Bill billData = demoData
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
        RenewEntitlementRequest.ClientData.DemoData.Ship shipData = demoData
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
   * Sets the transfer object payment list.
   * 
   * @param paymentListTO
   *            the transfer object payment list.
   * @param aPayment
   *            the JAXB payment list.
   */
  private static void setTOPaymentList(ArrayList<PaymentTO> paymentListTO,
      RenewEntitlementRequest.Payment aPayment) {

    PaymentTO aPaymentTO = new PaymentTO();

    // Required items
    // PayItem
    aPaymentTO.setPayItem(new BigInteger(aPayment.getPayItem()));

    // Optional Items
    // PayType
    RenewEntitlementRequest.Payment.PayType payType = aPayment.getPayType();

    if (payType.getCreditCard() != null) { // THIS IS A CREDIT CARD
      CreditCardTO creditCardTO = new CreditCardTO();
      RenewEntitlementRequest.Payment.PayType.CreditCard creditCard = payType
          .getCreditCard();

      if (creditCard.getCCManual() != null) {

        RenewEntitlementRequest.Payment.PayType.CreditCard.CCManual ccManual = creditCard
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

        RenewEntitlementRequest.Payment.PayType.CreditCard.CCSwipe ccSwipe = creditCard
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

        RenewEntitlementRequest.Payment.PayType.CreditCard.CCWireless ccWireless = creditCard
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
        RenewEntitlementRequest.Payment.PayType.GiftCard.GCManual gcManual = payType
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

        RenewEntitlementRequest.Payment.PayType.GiftCard.GCSwipe gcSwipe = payType
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
    else if (payType.getInstallment() != null) { // THIS IS AN INSTALLMENT PAYMENT (As of 2.15, JTL)

      InstallmentTO instTO = new InstallmentTO();
      RenewEntitlementRequest.Payment.PayType.Installment installment = payType
          .getInstallment();
      RenewEntitlementRequest.Payment.PayType.Installment.InstallmentCreditCard installCard = installment
          .getInstallmentCreditCard();

      InstallmentCreditCardTO installCCTO = new InstallmentCreditCardTO();
      instTO.setForRenewal(true); // As of 2.16.2, JTL

      if (installCard.getCCManual() != null) {
        installCCTO.setCcNbr(installCard.getCCManual().getCCNbr());
        installCCTO.setCcExpiration(installCard.getCCManual()
            .getCCExpiration());
        installCCTO.setCcName(installCard.getCCManual().getCCName());

        if (installCard.getCCManual().getCCVV() != null) installCCTO
            .setCcVV(installCard.getCCManual().getCCVV());
        if (installCard.getCCManual().getCCStreet() != null) installCCTO
            .setCcStreet(installCard.getCCManual().getCCStreet());
        if (installCard.getCCManual().getCCZipcode() != null) installCCTO
            .setCcZipCode(installCard.getCCManual().getCCZipcode());
        if (installCard.getCCManual().getCCType() != null) installCCTO
            .setCcType(installCard.getCCManual().getCCType());

      }
      else {
        installCCTO.setCcTrack1(installCard.getCCSwipe().getCCTrack1());
        installCCTO.setCcTrack2(installCard.getCCSwipe().getCCTrack2());
      }

      instTO.setCreditCard(installCCTO);

      RenewEntitlementRequest.Payment.PayType.Installment.InstallmentDemoData installDemo = installment
          .getInstallmentDemoData();
      InstallmentDemographicsTO installDemoTO = new InstallmentDemographicsTO();

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

      instTO.setInstllDemo(installDemoTO);

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
      RenewEntitlementRequest.Ticket aTicket) {

    TicketTO aTicketTO = new TicketTO();

    // Required items
    // TktItem
    aTicketTO.setTktItem(aTicket.getTktItem());

    // ExistingTktID
    aTicketTO.setExistingTktID(translateExistingTktID(aTicket
        .getExistingTktID()));

    // Quantity (default to 1)
    aTicketTO.setProdQty(new BigInteger("1"));

    // ProdCode
    aTicketTO.setProdCode(aTicket.getProdCode());

    // ProdPrice
    aTicketTO.setProdPrice(new BigDecimal(aTicket.getProdPrice()));

    // Optional items
    // ProdDemoData (opt)
    if (aTicket.getProdDemoData() != null) {

      ProdDemoData aTicketDemo = aTicket.getProdDemoData();
      if (aTicketDemo.getTktDemoData() != null) {

        TktDemoData aTktDemographic = aTicketDemo.getTktDemoData();

        DemographicsTO aTicketDemoTO = new DemographicsTO();

        if (aTicketDemoTO != null) {

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

    // FromPassType
    if (aTicket.getFromPassType() != null) {
      aTicketTO.setFromProdCode(aTicket.getFromPassType());
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

    // TktNote
    if (aTicket.getTktNote() != null) {
      aTicketTO.setTktNote(aTicket.getTktNote());
    }

    ticketListTO.add(aTicketTO);
  }

  /**
   * Sets the transfer object existing ticket
   * 
   * @param tktId
   *            a JAXB parsed ticket.
   */
  private static TicketTO translateExistingTktID(
      RenewEntitlementRequest.Ticket.ExistingTktID tktId) {

    TicketTO aTicketTO = new TicketTO();

    // Required items
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
            RenewEntitlementRequest.Ticket.ExistingTktID.Mag tktMag = tktId
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
              RenewEntitlementRequest.Ticket.ExistingTktID.TktDSSN tktDssn = tktId
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

    return (aTicketTO);
  }

}
