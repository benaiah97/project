package pvt.disney.dti.gateway.rules.dlr;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import com.ibm.icu.util.Calendar;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ErrorKey;
import pvt.disney.dti.gateway.dao.LookupKey;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.ReservationResponseTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.PaymentTO.PaymentType;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO.TPLookupType;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWMemberDemographicsTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderContactTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderLineTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrdersRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrdersRespTO.TicketRecord;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrdersRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWPaymentContractTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.provider.dlr.data.UtilityXML;
import pvt.disney.dti.gateway.provider.dlr.xml.GWEnvelopeXML;
import pvt.disney.dti.gateway.rules.DateTimeRules;
import pvt.disney.dti.gateway.rules.PaymentRules;
import pvt.disney.dti.gateway.rules.ProductRules;
import pvt.disney.dti.gateway.rules.TransformConstants;
import pvt.disney.dti.gateway.rules.TransformRules;

/**
 * The Class DLRReservationRules.
 * @author lewit019
 * @since 2.16.3
 */
public class DLRReservationRules implements TransformConstants {

  /**
   * Transforms a DTI transaction into a eGalaxy XML request.
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @return the string
   * 
   * @throws DTIException
   *             the DTI exception
   */
  static String transformRequest(DTITransactionTO dtiTxn) throws DTIException {

    String xmlRequest;
    GWEnvelopeTO envelopeTO = new GWEnvelopeTO(
        GWEnvelopeTO.GWTransactionType.ORDERS);

    GWHeaderTO headerTO = envelopeTO.getHeaderTO();
    GWBodyTO bodyTO = envelopeTO.getBodyTO();
    GWOrdersRqstTO ordersReqTO = new GWOrdersRqstTO();

    ReservationRequestTO resReq = (ReservationRequestTO) dtiTxn
        .getRequest().getCommandBody();

    // CREATE the ORDER TO
    GWOrderTO orderTO = createOrderTO(dtiTxn);

    // Installment Fields - Set Contract clause and sales program in the Order clause
    if (resReq.isInstallmentResRequest()) {

      GWPaymentContractTO contract = new GWPaymentContractTO();

      // Set Constants
      contract.setRecurrenceType(GW_ORDERS_CONTRACT_MONTHLY_RECURRENCE);
      contract.setInterval(GW_ORDERS_CONTRACT_RECURRENCE_INTERVAL);
      contract.setRenewContract(GW_ORDERS_CONTRACT_RENEW_CONTRACT);
      contract.setPaymentContractStatusID(GW_ORDERS_CONTRACT_STATUS_ID);
      if (resReq.getInstallmentDownpayment() != null) {
        String downPaymentAmount = resReq.getInstallmentDownpayment()
            .toString();
        contract.setDownPaymentAmount(downPaymentAmount);
      }
      contract.setContactMethod(GW_ORDERS_CONTRACT_CONTACT_METHOD);

      // Get TP Lookups - note, must be list used here, not hash as
      // several installment values use the same key...
      ArrayList<TPLookupTO> tpLookupList = dtiTxn.getTpLookupTOList();

      // Loop through the tpList and build an "installment" hash map.
      HashMap<String, String> instLookupMap = new HashMap<String, String>();
      for (/* each */TPLookupTO aTPLookup : /* in */tpLookupList) {
        if (aTPLookup.getLookupType() == TPLookupType.INSTALLMENT) {
          instLookupMap.put(aTPLookup.getLookupDesc(),
              aTPLookup.getLookupValue());
        }
      }

      // Sales Program (order level, not contract level)
      String salesProgram = instLookupMap
          .get(GW_ORDERS_SALESPROGRAM_DBKEY);
      if (salesProgram == null) {
        throw new DTIException(TransformRules.class,
            DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
            "Internal Error:  DLR Installmnt SalesProgram not in TP_LOOKUP Table.");
      }
      orderTO.setSalesProgram(salesProgram);

      GregorianCalendar gCal = new GregorianCalendar();

      // Day of Month
      contract.setDayOfMonth(Integer.valueOf(gCal
          .get(Calendar.DAY_OF_MONTH)));

      // Start Date (Today +1 month)
      gCal.add(Calendar.MONTH, 1);
      String startDate = UtilityXML.getEGalaxyDateFromGCalNoTime(gCal);
      contract.setStartDate(startDate);

      // End Date (Start date + 10 months (11 months from today))
      gCal.add(Calendar.MONTH, 10);
      String endDate = UtilityXML.getEGalaxyDateFromGCalNoTime(gCal);
      contract.setEndDate(endDate);

      // PaymentPlan
      String eligMember = resReq.getEligibilityMember();

      if (eligMember.equalsIgnoreCase(SOCA_RES)) {
        String paymentPlan = instLookupMap.get(GW_ORDERS_SOCAPURCHPLAN);
        if (paymentPlan == null) {
          throw new DTIException(
              TransformRules.class,
              DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
              "Internal Error:  DLR Installmnt paymentPlan not in TP_LOOKUP Table for " + GW_ORDERS_SOCAPURCHPLAN + ".");
        }
        contract.setPaymentPlanID(paymentPlan);
      }
      else if (eligMember.equalsIgnoreCase(CA_RES)) {
        String paymentPlan = instLookupMap.get(GW_ORDERS_CAPURCHPLAN);
        if (paymentPlan == null) {
          throw new DTIException(
              TransformRules.class,
              DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
              "Internal Error:  DLR Installmnt paymentPlan not in TP_LOOKUP Table for " + GW_ORDERS_CAPURCHPLAN + ".");
        }
        contract.setPaymentPlanID(paymentPlan);
      }

      // Add the contract to the order
      orderTO.addPaymentContract(contract);

    }

    // Set OrderLine(s), product info
    ArrayList<TicketTO> dtiTktList = resReq.getTktList();

    // Get the product map so for various product related value lookups
    HashMap<String, DBProductTO> dtiProductMap = dtiTxn.getDbProdMap();

    // Pre-processing to "flatten" the DTI order out if
    // there is an annual pass with a quantity > 1. (As of 2.16.1, JTL)
    ArrayList<TicketTO> dtiFlatTktList = flattenApQuantities(dtiTktList,
        dtiProductMap, resReq);

    // Set the order lines, returning the order total (required by the header)
    BigDecimal orderTotal = setProductOrderLines(resReq, orderTO, dtiProductMap, dtiFlatTktList);

    // Order>OrderTotal: Set the order total based on the products
    orderTO.setOrderTotal(orderTotal);

    // Set OrderLine(s), payments info
    // use this to get order line item that are payments
    HashMap<String, String> payCardMap = dtiTxn.getPaymentCardMap();
    boolean missingCvv = setPaymentOrderLines(resReq, orderTO, orderTotal, payCardMap);

    // Set the source ID to the TS MAC
    String sourceID = dtiTxn.getRequest().getPayloadHeader().getTktSeller()
        .getTsMac();
    if (missingCvv) {
      // credit card w/o CVV, and from WDPro send to secondary DLR source
      if (GW_WDPRO.compareTo(sourceID) == 0) sourceID = GW_WDPRO_NO_CVV;
    }
    headerTO.setSourceID(sourceID);

    // Set MessageID to DTI Payload ID (to TpRefNum as of 2.16.1)
    headerTO.setMessageID(new BigInteger(dtiTxn.getTpRefNum().toString()));

    // Set the echo data to the DTI payload ID (as of 2.16.1, JTL)
    headerTO.setEchoData(dtiTxn.getRequest().getPayloadHeader()
        .getPayloadID());

    // Set the time stamp to the GMT date/time now. (as of 2.17.2, JTL)
    headerTO.setTimeStamp(DateTimeRules.getPTDateNow());

    // Set the message type to a fixed value
    headerTO.setMessageType(GW_ORDERS_MESSAGE_TYPE);

    ordersReqTO.addOrder(orderTO);

    bodyTO.setOrdersRqstTO(ordersReqTO);
    envelopeTO.setHeaderTO(headerTO);
    envelopeTO.setBodyTO(bodyTO);

    xmlRequest = GWEnvelopeXML.getXML(envelopeTO);

    return xmlRequest;
  }

  /**
   * Sets the payment order lines, based on the DTI input.
   * 
   * @param resReq
   * @param orderTO
   * @param orderTotal
   * @param payCardMap
   * @return true or false if the CVV was found or not.
   */
  private static boolean setPaymentOrderLines(ReservationRequestTO resReq,
      GWOrderTO orderTO, BigDecimal orderTotal,
      HashMap<String, String> payCardMap) {
    String paymentCode = null;
    boolean missingCvv = false;

    Iterator<PaymentTO> paymentIter = resReq.getPaymentList().iterator();
    while (paymentIter.hasNext()) {
      // lookup TO is used to setting paymentCode on the order line to
      PaymentTO paymentTO = paymentIter.next();

      GWOrderLineTO gwOrderLinePayTO = new GWOrderLineTO();
      gwOrderLinePayTO
          .setDetailType(TransformConstants.GW_ORDERS_PAYMENT_ORDER_LINE_ITEM);

      // CREDITCARD, VOUCHER, GIFTCARD, or INSTALLMENT/UNSUPPORTED
      if (paymentTO.getPayType() == PaymentType.CREDITCARD) {
        // set the credit card stuff
        CreditCardTO cCardTO = paymentTO.getCreditCard();
        gwOrderLinePayTO.setEndorsement(cCardTO.getCcNbr());
        gwOrderLinePayTO.setDescription(cCardTO.getCcType());
        gwOrderLinePayTO.setBillingZip(cCardTO.getCcZipCode());

        if (cCardTO.getCcStreet() != null) {
           gwOrderLinePayTO.setBillingStreet(cCardTO.getCcStreet());
        }
        
        // eGalaxy requires MMYY
        gwOrderLinePayTO.setExpDate(cCardTO.getCcExpiration());
        if (cCardTO.getCcVV() != null && cCardTO.getCcVV().length() > 0) {
          gwOrderLinePayTO.setCvn(cCardTO.getCcVV());
        }
        else {
          missingCvv = true;
        }
        // 3DS CCCAVV
        if (cCardTO.getCcCAVV() != null) {
          gwOrderLinePayTO.setcCCAVV(cCardTO.getCcCAVV());
        }

        // ccEcommerce 
        if (cCardTO.getCcEcommerce() != null) {
          gwOrderLinePayTO.setcCEcommerce(cCardTO.getCcEcommerce());
        }

        // 3DSXID
        if (cCardTO.getXid() != null) {
          gwOrderLinePayTO.setXid3DS(cCardTO.getXid());
        }

        // use the map to set payment type,get card type to get
        // paymentCode,
        // set after if conditions below
        paymentCode = payCardMap.get(cCardTO.getCcType());
        paymentTO.setCreditCard(cCardTO);
      }

      if (paymentTO.getPayType() == PaymentType.GIFTCARD) {
        // set the gift card stuff
        GiftCardTO gCardTO = paymentTO.getGiftCard();
        gwOrderLinePayTO
            .setDescription(TransformConstants.GW_ORDERS_ORDER_LINE_ITEM_DESCRIPTION_GIFT_CARD);
        gwOrderLinePayTO.setEndorsement(paymentTO.getGiftCard()
            .getGcNbr());
        // Hard-coded (as was done in WMB)
        paymentCode = payCardMap
            .get(TransformConstants.GW_ORDERS_ORDER_LINE_ITEM_DESCRIPTION_GIFT_CARD);
        paymentTO.setGiftCard(gCardTO);
      }

      // Don't process a DLR installment payment type. They don't
      // have a place for it. Everything not paid for will go on
      // the installment plan.
      if (paymentTO.getPayType() == PaymentType.INSTALLMENT) {
        continue;
      }

      // set the payment code
      gwOrderLinePayTO.setPaymentCode(paymentCode);

      // set payment date
      gwOrderLinePayTO.setPaymentDate(UtilityXML.getCurrentEgalaxyDate());

      // set pay total
      gwOrderLinePayTO.setTotal(String.valueOf(orderTotal));

      // set pay amount
      gwOrderLinePayTO
          .setAmount(String.valueOf(paymentTO.getPayAmount()));

      // add back to the order
      orderTO.addOrderLine(gwOrderLinePayTO);
    }
    // END OrderLine(s), payments info
    return missingCvv;
  }

  /**
   * For any item that is an annual pass, flattens the quantity of products to one and assigns one of the demographics rows to each of the newly created line items.
   * 
   * @param dtiTktList
   * @param dtiProductMap
   * @return the "flattened" ticket list
   * @since 2.16.1 (JTL)
   */
  private static ArrayList<TicketTO> flattenApQuantities(
      ArrayList<TicketTO> dtiTktList,
      HashMap<String, DBProductTO> dtiProductMap,
      ReservationRequestTO resReq) {
    
    ArrayList<TicketTO> dtiFlatTktList = new ArrayList<TicketTO>();
    for (/* each */TicketTO inTicketTO : /* in */dtiTktList) {

      // Does it need to be flattened? (pushed to quantity 1 with 1 demo)
      String prodCode = inTicketTO.getProdCode();
      DBProductTO dbProduct = dtiProductMap.get(prodCode);
      if (dbProduct.getDaySubclass().equalsIgnoreCase(GW_PASS)) {

        if (inTicketTO.getProdQty().intValue() > 1) {

          int quantity = inTicketTO.getProdQty().intValue();
          for (int i = 0; i < quantity; i++) {

            // Clone the entitlement.
            TicketTO flatTicketTO = null;
            try {
              flatTicketTO = inTicketTO.clone();
            }
            catch (CloneNotSupportedException e) {
              // While not expected, this could be handled better.
              System.out
                  .println("CloneNotSupportedException - " + e
                      .toString());
              e.printStackTrace();
            }

            // Set the quantity to 1.
            flatTicketTO.setProdQty(new BigInteger("1"));

            // Choose the demographics
            ArrayList<DemographicsTO> oneDemoList = new ArrayList<DemographicsTO>();
            DemographicsTO singleTicketDemo = inTicketTO
                .getTicketDemoList().get(i);
            oneDemoList.add(singleTicketDemo);
            flatTicketTO.setTicketDemoList(oneDemoList);

            dtiFlatTktList.add(flatTicketTO);
          }

        }
        else {
          dtiFlatTktList.add(inTicketTO);
        }

      }
      else {
        // If this is a SHIP product on an installment order for DLR, drop
        // the shipping code - it's not compatible. (can't be on a payment plan)
        if ((resReq.isInstallmentResRequest()) && (dbProduct
            .getDayClass().equalsIgnoreCase(ProductRules.SHIPPING))) {
          // Suppress the shipping code
        }
        else {
          dtiFlatTktList.add(inTicketTO);
        }
      }

    } // Flatten ticket loop for APs Only (as of 2.16.1, JTL)
    return dtiFlatTktList;
  }

  /**
   * Sets the product-based order lines on the out-going request.
   * 
   * @param resReq
   * @param orderTO
   * @param dtiProductMap
   * @param dtiFlatTktList
   * @return the order total (needed in the GW header)
   */
  private static BigDecimal setProductOrderLines(ReservationRequestTO resReq,
      GWOrderTO orderTO, HashMap<String, DBProductTO> dtiProductMap,
      ArrayList<TicketTO> dtiFlatTktList) {

    BigDecimal orderTotal = new BigDecimal("0.00");

    // iterate through the DTI tickets
    Iterator<TicketTO> iter = dtiFlatTktList.iterator();
    while (iter.hasNext()) {

      TicketTO dtiTicket = iter.next();
      DBProductTO dtiProduct = dtiProductMap.get(dtiTicket.getProdCode());

      GWOrderLineTO gwLineItemTO = new GWOrderLineTO();
      gwLineItemTO.setAmount(String.valueOf(dtiTicket.getProdPrice()));

      gwLineItemTO.setDescription(dtiProduct.getPdtDesc());

      // Set the detail type based on the type of product
      if (dtiProduct.getDaySubclass().equalsIgnoreCase(GW_PASS)) {
        gwLineItemTO
            .setDetailType(TransformConstants.GW_ORDERS_PASS_ORDER_LINE_ITEM);
      }
      else {
        gwLineItemTO
            .setDetailType(TransformConstants.GW_ORDERS_TICKET_ORDER_LINE_ITEM);
      }

      // PLU (mapped from Ticket Name, not ticket code because of length)
      DBProductTO dtiDbProduct = dtiProductMap.get(dtiTicket.getProdCode());
      gwLineItemTO.setPlu(dtiDbProduct.getMappedProviderTktName());

      // PaymentPlanID
      if (resReq.isInstallmentResRequest()) {
        GWPaymentContractTO contract = orderTO.getPaymntContractList()
            .get(0);
        String paymentPlanID = contract.getPaymentPlanID();
        gwLineItemTO.setPaymentPlanID(paymentPlanID);
      }

      // Qty
      gwLineItemTO.setQty(String.valueOf(dtiTicket.getProdQty()));
      gwLineItemTO.setTaxCode(""); // FUTURE EXPANSION, TaxCode not used

      // Eligibility group
      if (resReq.getEligibilityGroup() != null) {

        if ((resReq.getEligibilityGroup()
            .equalsIgnoreCase(TransformConstants.GW_ORDERS_DLR_MTGS_CONV_ELIGIBILITY_GROUP)) || (resReq
            .getEligibilityGroup()
            .equalsIgnoreCase(TransformConstants.GW_ORDERS_DLR_WEDDINGS_ELIGIBILITY_GROUP)) || (resReq
            .getEligibilityGroup()
            .equalsIgnoreCase(TransformConstants.GW_ORDERS_DLR_BOLT_GROUP))) {

          if (dtiTicket.getTktValidityValidStart() != null) {
            gwLineItemTO.setTicketDate(UtilityXML
                .getEGalaxyDateFromGCal(dtiTicket
                    .getTktValidityValidStart()));
          }
        }
      }

      // figure out the product total
      BigDecimal prodCost = dtiTicket.getProdPrice();
      BigDecimal productTotal = prodCost.multiply(new BigDecimal(
          dtiTicket.getProdQty()));

      // add it to the total cost of the order
      orderTotal = orderTotal.add(productTotal);

      // set the total on the line item
      gwLineItemTO.setTotal(String.valueOf(productTotal));

      // Add demographics, if present (note: because of activity above,
      // only one expected per line). (As of 2.16.1, JTL)
      if (dtiTicket.getTicketDemoList().size() != 0) {

        DemographicsTO dtiDemo = dtiTicket.getTicketDemoList().get(0); // One and only one.

        GWMemberDemographicsTO gwDemo = new GWMemberDemographicsTO();

        // First Name
        if (dtiDemo.getFirstName() != null) {
          gwDemo.setFirstName(dtiDemo.getFirstName());
        }

        // Last Name
        if (dtiDemo.getLastName() != null) {
          gwDemo.setLastName(dtiDemo.getLastName());
        }

        // Street 1
        if (dtiDemo.getAddr1() != null) {
          gwDemo.setStreet1(dtiDemo.getAddr1());
        }

        // Street 2
        if (dtiDemo.getAddr2() != null) {
          gwDemo.setStreet2(dtiDemo.getAddr2());
        }

        // City
        if (dtiDemo.getCity() != null) {
          gwDemo.setCity(dtiDemo.getCity());
        }

        // State
        if (dtiDemo.getState() != null) {
          gwDemo.setState(dtiDemo.getState());
        }

        // ZIP
        if (dtiDemo.getZip() != null) {
          gwDemo.setZip(dtiDemo.getZip());
        }

        // CountryCode
        if (dtiDemo.getCountry() != null) {
          gwDemo.setCountryCode(dtiDemo.getCountry());
        }

        // Phone Number
        if (dtiDemo.getTelephone() != null) {
          gwDemo.setPhone(dtiDemo.getTelephone());
        }

        // E-mail
        if (dtiDemo.getEmail() != null) {
          gwDemo.seteMail(dtiDemo.getEmail());
        }

        // Date of Birth
        if (dtiDemo.getDateOfBirth() != null) {
          String dateOfBirth = UtilityXML
              .getEGalaxyDateFromGCal(dtiDemo.getDateOfBirth());
          gwDemo.setDateOfBirth(dateOfBirth);
        }

        // Gender (Added default for unspecified, as of 2.16.1)
        if (dtiDemo.getGenderType() != DemographicsTO.GenderType.NOTPRESENT) {
          if (dtiDemo.getGenderType() == DemographicsTO.GenderType.MALE) {
            gwDemo.setGender("1");
          }
          else if (dtiDemo.getGenderType() == DemographicsTO.GenderType.FEMALE) {
            gwDemo.setGender("2");
          }
          else {
            gwDemo.setGender("0");
          }

          gwLineItemTO.addMember(gwDemo);
        }

      }

      // add to orderTO
      orderTO.addOrderLine(gwLineItemTO);
    }
    // END OrderLine(s), product info
    return orderTotal;
  }

  /**
   * Transforms a orders response string from the eGalaxy DLR provider and updates the DTITransactionTO object with the response information.
   * 
   * @param dtiTxn
   *            The transaction object for this request.
   * @param xmlResponse
   *            The DLR provider's response in string format.
   * @return The DTITransactionTO object, enriched with the response information.
   * @throws DTIException
   *             for any error. Contains enough detail to formulate an error response to the seller.
   */
  static DTITransactionTO transformResponse(DTITransactionTO dtiTxn,
      String xmlResponse) throws DTIException {

    GWEnvelopeTO gwEnvRespTO = GWEnvelopeXML.getTO(xmlResponse);

    DTIResponseTO dtiRespTO = new DTIResponseTO();

    ReservationResponseTO dtiResRespTO = new ReservationResponseTO();
    dtiResRespTO.setResponseType("Create");

    // Set up the Payload and Command Header Responses.
    PayloadHeaderTO payloadHdrTO = TransformRules
        .createRespPayloadHdr(dtiTxn);
    CommandHeaderTO commandHdrTO = TransformRules.createRespCmdHdr(dtiTxn);

    dtiRespTO.setPayloadHeader(payloadHdrTO);
    dtiRespTO.setCommandHeader(commandHdrTO);

    // get the body and order response TOs
    GWBodyTO gwBodyTO = gwEnvRespTO.getBodyTO();

    // BEGIN ERROR CHECKING
    // search for blatant error first
    if (gwBodyTO == null) {
      // throw bad provider response error
      throw new DTIException(TransformRules.class,
          DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
          "Internal Error:  Gateway XML allowed a response with null body.");
    }

    // check status related errors
    GWStatusTO gwStatusTO = gwBodyTO.getStatusTO();
    if (gwStatusTO == null) {
      // throw bad provider response error
      throw new DTIException(TransformRules.class,
          DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
          "Internal Error:  Gateway XML allowed a response with null status.");
    }

    String statusString = gwStatusTO.getStatusCode();
    if (statusString == null) {
      // throw bad provider response error
      throw new DTIException(TransformRules.class,
          DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
          "Internal Error:  Gateway XML allowed a response with null status code.");
    }

    // Get the provider response status code
    int statusCode = -1;

    try {
      statusCode = Integer.parseInt(statusString);
      dtiRespTO.setProviderErrCode(statusString);
    }
    catch (NumberFormatException e) {
      throw new DTIException(TransformRules.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Provider responded with a non-numeric status code.");
    }

    // If the provider had an error, map it and generate the response.
    // Copy the ticket identity and default the TktStatus Voidable to No
    if (statusCode != 0) {
      dtiTxn = transformError(dtiTxn, dtiRespTO, statusString,
          xmlResponse);
      return dtiTxn; // NOTE: multiple return points is an anti-pattern,
      // this
      // should be re-factored
    }

    // Price mismatch warning
    if (dtiTxn.isPriceMismatch()) {
      DTIErrorTO mismatchWarn = ErrorKey
          .getErrorDetail(DTIErrorCode.PRICE_MISMATCH_WARNING);
      dtiRespTO.setDtiError(mismatchWarn);
    }

    // END ERROR CHECKING

    GWOrdersRespTO gwOrdResp = gwEnvRespTO.getBodyTO().getOrdersRespTO();

    String galaxyOrderID = gwOrdResp.getGalaxyOrderID();
    String orderID = gwOrdResp.getOrderID();

    // don't be confused by the use of authorization code here...
    // it is used to set CCAuthNumber on the DTI response, not CCAuthCode
    String authNumber = gwOrdResp.getAuthCode();

    PaymentTO paymentTO = new PaymentTO();

    // PER WMB, if authorization code == 1
    if (authNumber != null) {
      paymentTO.setPayItem(new BigInteger("1"));
      CreditCardTO dtiCredCardTO = new CreditCardTO();
      dtiCredCardTO.setCcAuthCode("0"); // Hard-coded as in WMB
      dtiCredCardTO.setCcAuthNumber(authNumber);
      paymentTO.setCreditCard(dtiCredCardTO);
    }
    dtiResRespTO.addPayment(paymentTO);

    // set ResCode
    ReservationTO dtiReservationTO = new ReservationTO();
    dtiReservationTO.setResCode(orderID);

    // set contract (if there) as of 2.16.1, JTL
    if (gwOrdResp.getPaymentContractID() != null) {
      dtiReservationTO.setContractId(gwOrdResp.getPaymentContractID());
    }
    dtiResRespTO.setReservation(dtiReservationTO);

    // set client data client ID from galaxyOrderID
    ClientDataTO clientDataTO = new ClientDataTO();
    clientDataTO.setClientId(galaxyOrderID);

    dtiResRespTO.setClientData(clientDataTO);

    // Add tickets if present
    ArrayList<TicketRecord> gwTicketArray = gwOrdResp.getTicketArray();
    if (gwTicketArray.size() > 0) {

      // ArrayList<TicketTO> dtiTicketArray =
      // dtiResRespTO.getTicketList();
      for (int i = 0; gwTicketArray.size() > i; i++) {

        TicketRecord gwTicket = gwTicketArray.get(i);

        TicketTO dtiTicket = new TicketTO();

        // TktItem
        BigInteger tktItem = new BigInteger(Integer.toString((i + 1)));
        dtiTicket.setTktItem(tktItem);

        // Visual ID to BarCode
        dtiTicket.setBarCode(gwTicket.getVisualID());

        dtiResRespTO.addTicket(dtiTicket);

      }

    }

    // set TOs back on various places
    dtiRespTO.setCommandBody(dtiResRespTO);
    dtiTxn.setResponse(dtiRespTO);

    return dtiTxn;
  }

  /**
   * Transforms a DLR Orders error
   * 
   * @param dtiTxn
   *            The DTITransacationTO object representing this transaction.
   * @param dtiRespTO
   *            The parsed version of the received response.
   * @param statusString
   *            get to get the TP provider error information.
   * @throws DTIException
   *             Should the routine be unable to find the TP error lookup.
   */
  static DTITransactionTO transformError(DTITransactionTO dtiTxn,
      DTIResponseTO dtiRespTO, String statusString, String xmlResponse) throws DTIException {

    String errorCode = DLRErrorRules.processStatusError(
        dtiTxn.getTransactionType(), statusString, xmlResponse);

    DTIErrorTO dtiErrorTO = ErrorKey.getTPErrorMap(errorCode);

    DTIErrorCode.populateDTIErrorResponse(dtiErrorTO, dtiTxn, dtiRespTO);

    // Copy over key ticket values for error response to match
    // existing format.
    if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.COMMAND) {

      if (dtiErrorTO.getErrorScope() == DTIErrorCode.ErrorScope.TICKET) {
        ReservationRequestTO resReq = (ReservationRequestTO) dtiTxn
            .getRequest().getCommandBody();
        ReservationResponseTO resResp = new ReservationResponseTO();

        TicketTO dtiTktTO = new TicketTO();

        // Ticket Item
        dtiTktTO.setTktItem(new BigInteger(ITEM_1));

        // Ticket Identity
        if (resReq.getTktList().size() != 0) {
          TicketTO dtiTktTOReq = resReq.getTktList().get(0);
          if (dtiTktTOReq != null) dtiTktTO.setExternal(dtiTktTOReq
              .getExternal());
        }

        // Add the ticket to the response
        resResp.addTicket(dtiTktTO);

        dtiRespTO.setCommandBody(resReq);
      }
    }

    dtiTxn.setResponse(dtiRespTO);

    return dtiTxn;
  }

  /**
   * Apply DLR reservation rules. RULE: Validate that resCode is not null and not 0 length. RULE: Validate that billing demographics are present and complete RULE: Validate that if the "installment" type of payment is present, the
   * down-payment matches the required amount. (As of 2.16.1, JTL)
   * 
   * @param dtiTxn
   *            the DTI transaction
   * 
   * @throws DTIException
   *             the DTI exception
   */
  public static void applyDLRReservationRules(DTITransactionTO dtiTxn) throws DTIException {
    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    ReservationRequestTO dtiResReq = (ReservationRequestTO) dtiCmdBody;

    // RULE: Is there one shipping product on the order? (as of 2.12)
    // Moved to both WDW and DLR as a part of 2.16.3, JTL
    ArrayList<DBProductTO>dbProdList = dtiTxn.getDbProdList();
    ArrayList<TicketTO> tktListTO = dtiResReq.getTktList();
    ProductRules.validateOneShipProduct(tktListTO, dbProdList);
    
    // Validate that resCode is not null and not 0 length
    if (dtiResReq.getReservation() != null) {
      String resCode = dtiResReq.getReservation().getResCode();

      if (resCode == null || resCode.length() == 0 || resCode.length() > 20) {
        throw new DTIException(DLRReservationRules.class,
            DTIErrorCode.INVALID_MSG_CONTENT,
            "ResCode is not between required lengths (1 - 20) for DLR.");
      }
    }

    // Validate that billing demographics are present
    // FirstName, LastName, Street1, City, ZIP, Country, Phone, Email
    DemographicsTO billingTO = dtiResReq.getClientData().getBillingInfo();
    String firstName = billingTO.getFirstName();
    String lastName = billingTO.getLastName();
    String street1 = billingTO.getAddr1();
    String city = billingTO.getCity();
    String zip = billingTO.getZip();
    String country = billingTO.getCountry();
    String phone = billingTO.getTelephone();
    String email = billingTO.getEmail();

    if ((firstName == null) || (firstName.length() == 0)) {
      throw new DTIException(DLRReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "DLR Reservation missing required billing first name.");
    }
    if ((lastName == null) || (lastName.length() == 0)) {
      throw new DTIException(DLRReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "DLR Reservation missing required billing last name.");
    }
    if ((street1 == null) || (street1.length() == 0)) {
      throw new DTIException(DLRReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "DLR Reservation missing required billing street address 1.");
    }
    if ((city == null) || (city.length() == 0)) {
      throw new DTIException(DLRReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "DLR Reservation missing required billing city.");
    }
    if ((zip == null) || (zip.length() == 0)) {
      throw new DTIException(DLRReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "DLR Reservation missing required billing ZIP.");
    }
    if ((country == null) || (country.length() == 0)) {
      throw new DTIException(DLRReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "DLR Reservation missing required billing country.");
    }
    if ((phone == null) || (phone.length() == 0)) {
      throw new DTIException(DLRReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "DLR Reservation missing required billing phone.");
    }
    if ((email == null) || (email.length() == 0)) {
      throw new DTIException(DLRReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "DLR Reservation missing required billing email.");
    }

    // max limit is enforced in TicketRules
    String tpiCode = dtiTxn.getTpiCode();
    TransactionType txnType = dtiTxn.getTransactionType();
    ReservationTO dtiResTO = dtiResReq.getReservation();
    String shipDetail = dtiResTO.getResSalesType();
    String shipMethod = dtiResTO.getResSalesType();

    if (shipDetail == null) {
      throw new DTIException(DLRReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "Reservation getResSalesType cannot be null.");
    }
    if (shipMethod == null) {
      throw new DTIException(DLRReservationRules.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "Reservation getResSalesType cannot be null.");
    }

    // Get the TP Lookups
    ArrayList<TPLookupTO> tpLookups = LookupKey.getGWTPCommandLookup(
        tpiCode, txnType, shipMethod, shipDetail);

    if (tpLookups.size() == 0) {
      throw new DTIException(
          DLRReservationRules.class,
          DTIErrorCode.FAILED_DB_OPERATION_SVC,
          "TPCommandLookup query on reservation did not return with responses as expected (set-up incomplete): " + tpLookups
              .size());
    }
    dtiTxn.setTpLookupTOList(tpLookups);

    // RULE: Validate that if the "installment" type of payment is present,
    // the down-payment matches the required amount. (As of 2.16.1, JTL)
    PaymentRules.validateResInstallDownpayment(dtiTxn, tpLookups);

    // RULE: Validate ticket level demographics for DLR. (As of 2.16.3, JTL)
    ProductRules.validateDlrTicketDemo(tktListTO);

    return;

  }

  /**
   * 
   * @param dtiTxn
   * @return
   * @throws DTIException
   */
  private static GWOrderTO createOrderTO(DTITransactionTO dtiTxn) throws DTIException {

    GWOrderTO orderTO = new GWOrderTO();
    // get a map of various provider values (we will need to use this later
    // for some shipping and payment details)
    HashMap<TPLookupTO.TPLookupType, TPLookupTO> gwTPLookupMap = dtiTxn
        .getTpLookupTOMap();

    ReservationRequestTO resReq = (ReservationRequestTO) dtiTxn
        .getRequest().getCommandBody();

    // OrderID
    orderTO.setOrderID(resReq.getReservation().getResCode());

    // CustomerID
    // if the override customer ID is present, use it, otherwise use what's
    // in the database
    if (resReq.getEligibilityGroup() != null && resReq
        .getEligibilityGroup().equalsIgnoreCase(
            TransformConstants.GW_ORDERS_DLR_ELIGIBILITY_GROUP)) {
      orderTO.setCustomerID(resReq.getEligibilityMember());
    }
    else {
      orderTO.setCustomerID(dtiTxn.getEntityTO().getCustomerId());
    }

    // OrderDate (Now PT, as of 2.17.2.1 JTL)
    orderTO.setOrderDate(UtilityXML.getEGalaxyDateFromGCal(DateTimeRules.getPTDateNow()));

    // OrderStatus ( 1 if unpaid, 2 if paid )
    ArrayList<PaymentTO> payListTO = resReq.getPaymentList();
    if (payListTO.size() > 0) {
      orderTO.setOrderStatus(TransformConstants.GW_ORDERS_PAID_ORDER_STATUS);
    }
    else {
      orderTO.setOrderStatus(TransformConstants.GW_ORDERS_UNPAID_ORDER_STATUS);
    }

    // SalesProgram (include if the order is installment) as of 2.16.1, JTL
    // Also, since values will be required later, put together a map of keys
    // and values for ready access.
    HashMap<String, String> instTPLookupMap = new HashMap<String, String>();
    if (resReq.isInstallmentResRequest()) {

      ArrayList<TPLookupTO> tpLookupList = dtiTxn.getTpLookupTOList();
      String salesProgram = null;
      for (/* each */TPLookupTO aTPLookup : /* in */tpLookupList) {

        if ((aTPLookup.getLookupDesc().equalsIgnoreCase("SalesProgram")) && (aTPLookup
            .getLookupType() == TPLookupTO.TPLookupType.INSTALLMENT)) {
          salesProgram = aTPLookup.getLookupValue();
        }

        instTPLookupMap.put(aTPLookup.getLookupDesc(),
            aTPLookup.getLookupValue());

      } // for loop

      if (salesProgram == null) {
        throw new DTIException(DLRReservationRules.class,
            DTIErrorCode.UNDEFINED_FAILURE,
            "GWTPLookup for SalesProgram is missing in the database.");
      }
      else {
        orderTO.setSalesProgram(salesProgram);
      }

    } // If is installment request

    // BEGIN OrderNote
    String orderNote = null;

    // check for AP pass note
    if (resReq.getAPPassInfo() != null) {
      orderNote = resReq.getAPPassInfo();
      orderTO.setOrderNote(orderNote);
    }
    // check for other notes
    Iterator<String> resNoteIter = resReq.getNoteList().iterator();
    while (resNoteIter.hasNext()) {
      String resNote = resNoteIter.next();
      if (resNote.startsWith("Personal Message")) {
        orderTO.setOrderNote(resNote);
        orderNote = resNote;
      }
      else if (resNote.startsWith("Personnel Number")) {
        orderTO.setOrderReference(resNote); // calling out separate from
        // default
      }
      else { // default
        orderTO.setOrderReference(resNote);
      }
    }

    // Format the DLR note field exactly like the ATS one for fulfillment.
    // Since 2.9 JTL
    if (orderNote == null) {
      if (resReq.getClientData() != null) {
        if (resReq.getClientData().getClientPaymentMethod() != null) {
          orderNote = TransformRules
              .setFulfillmentNoteDetails(dtiTxn);
          orderTO.setOrderNote(orderNote);
        }
      }
    }

    // END OrderNote

    // OrderTotal (as of 2.16.1, JTL)
    if (resReq.isInstallmentResRequest()) {
      orderTO.setOrderTotal(resReq.getTotalOrderAmount());
    }

    // OrderReference
    // If the order is a "BOLT" order, then put the LMS number in Order
    // Reference.
    if (resReq.getEligibilityGroup() != null) {
      if (resReq.getEligibilityGroup().equalsIgnoreCase(
          GW_ORDERS_DLR_BOLT_GROUP)) {
        if (resReq.getEligibilityMember() != null) {
          orderTO.setOrderReference(resReq.getEligibilityMember());
        }
      }
    }

    // PaymentContracts (as of 2.16.1, JTL)
    if (resReq.isInstallmentResRequest()) {

      GWPaymentContractTO payContract = new GWPaymentContractTO();

      // RecurrenceType
      payContract.setRecurrenceType(GW_ORDERS_CONTRACT_MONTHLY_RECURRENCE);

      // DayOfMonth (Set to today's day).
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
      String dayString = dateFormat.format(new Date());
      payContract.setDayOfMonth(new Integer(dayString));

      // Interval
      payContract.setInterval(GW_ORDERS_CONTRACT_RECURRENCE_INTERVAL);

      // StartDate (+1 month from today including last day of month logic)
      GregorianCalendar startCal = new GregorianCalendar();
      startCal.add(Calendar.MONTH, 1);
      payContract.setStartDate(UtilityXML
          .getEGalaxyDateFromGCal(startCal));

      // EndDate (+11 months from today)
      GregorianCalendar endCal = new GregorianCalendar();
      startCal.add(Calendar.MONTH, 11);
      payContract.setEndDate(UtilityXML.getEGalaxyDateFromGCal(endCal));

      // PaymentPlanID
      String paymentPlan = null;
      if (resReq.getEligibilityMember().equalsIgnoreCase(SOCA_RES)) {
        paymentPlan = instTPLookupMap.get(GW_ORDERS_SOCAPURCHPLAN);
      } else {
        paymentPlan = instTPLookupMap.get(GW_ORDERS_CAPURCHPLAN);
      } 
      if (paymentPlan == null) {
        throw new DTIException(
            DLRReservationRules.class,
            DTIErrorCode.UNDEFINED_FAILURE,
            "GWTPLookup for " + GW_ORDERS_SOCAPURCHPLAN + " or " + GW_ORDERS_CAPURCHPLAN + " is missing in the database.");
      }
      payContract.setPaymentPlanID(paymentPlan);

      // Renew Contract
      payContract.setRenewContract(GW_ORDERS_CONTRACT_RENEW_CONTRACT);

      // PaymentContractStatusID
      payContract
          .setPaymentContractStatusID(GW_ORDERS_CONTRACT_STATUS_ID);

      // ContactMethod
      payContract.setContactMethod(GW_ORDERS_CONTRACT_CONTACT_METHOD);

    }

    // BEGIN OrderContactInfo field
    // set order contact info using billing demographics info
    DemographicsTO demoTO = resReq.getClientData().getBillingInfo();
    GWOrderContactTO orderContactTO = new GWOrderContactTO();
    orderContactTO.setFirstName(demoTO.getFirstName());
    orderContactTO.setLastName(demoTO.getLastName());
    orderContactTO.setStreet1(demoTO.getAddr1());
    if (demoTO.getAddr2() != null) {
      orderContactTO.setStreet2(demoTO.getAddr2());
    }

    // Since 2.11 - If group is "BOLT", then "org name" goes to Street 3.
    if (resReq.getEligibilityGroup() != null) {
      if (resReq.getEligibilityGroup().equalsIgnoreCase(
          GW_ORDERS_DLR_BOLT_GROUP)) {
        if (demoTO.getName() != null) {
          orderContactTO.setStreet3(demoTO.getName());
        }
      }
    }

    orderContactTO.setCity(demoTO.getCity());
    // WDPROX FIX
    if (demoTO.getState() != null && demoTO.getState().length() > 0) {
      orderContactTO.setState(demoTO.getState());
    }
    orderContactTO.setZip(demoTO.getZip());
    orderContactTO.setCountry(demoTO.getCountry());
    orderContactTO.setPhone(demoTO.getTelephone());
    orderContactTO.setEmail(demoTO.getEmail());
    // add the contact on the order
    orderTO.setOrderContact(orderContactTO);
    // END OrderContactInfo field

    // BEGIN ShipToContact field
    // set ship to contact info using shipping info
    GWOrderContactTO shipContactTO = new GWOrderContactTO();
    DemographicsTO shippingTO = resReq.getClientData().getShippingInfo();

    if (shippingTO != null) { // As of 2.12 , permit Shipping to be absent.
      if (shippingTO.getFirstName() != null) {
        shipContactTO.setFirstName(shippingTO.getFirstName());
      }
      if (shippingTO.getLastName() != null) {
        shipContactTO.setLastName(shippingTO.getLastName());
      }
      shipContactTO.setStreet1(shippingTO.getAddr1());
      if (shippingTO.getAddr2() != null) {
        shipContactTO.setStreet2(shippingTO.getAddr2());
      }

      // Since 2.11 - If group is "BOLT", then "org name" goes to Street
      // 3.
      if (resReq.getEligibilityGroup() != null) {
        if (resReq.getEligibilityGroup().equalsIgnoreCase(
            GW_ORDERS_DLR_BOLT_GROUP)) {
          if (shippingTO.getName() != null) {
            shipContactTO.setStreet3(shippingTO.getName());
          }
        }
      }

      shipContactTO.setCity(shippingTO.getCity());
      // WDPROFIX
      if (shippingTO.getState() != null && shippingTO.getState().length() > 0) {
        shipContactTO.setState(shippingTO.getState());
      }
      shipContactTO.setZip(shippingTO.getZip());
      shipContactTO.setCountry(shippingTO.getCountry());
      shipContactTO.setPhone(shippingTO.getTelephone());
      shipContactTO.setEmail(shippingTO.getEmail());
      // add the contact on the order
      orderTO.setShipToContact(shipContactTO);
      // END ShipToContact field
    }
    else { // as of 2.16.1, JTL
      if (resReq.isInstallmentResRequest()) {
        shipContactTO.setSameAsOrderContact(true);
      }
    }

    // BEGIN Shipping field & children, ShipDetails, ShipMethod
    // use the lookup map for details & lookup our various values
    try {
      // look for ship details
      TPLookupTO shipDetailslookupTO = gwTPLookupMap
          .get(TPLookupTO.TPLookupType.SHIP_DETAIL);

      if (shipDetailslookupTO == null) {
        throw new DTIException(DLRReservationRules.class,
            DTIErrorCode.UNDEFINED_FAILURE,
            "GWTPLookup for ShipDetails is missing in the database.");
      }
      else {
        orderTO.setShipDeliveryDetails(shipDetailslookupTO
            .getLookupValue());
      }

      // look for ship method
      TPLookupTO shipMethodlookupTO = gwTPLookupMap
          .get(TPLookupTO.TPLookupType.SHIP_METHOD);
      if (shipMethodlookupTO == null) {
        throw new DTIException(DLRReservationRules.class,
            DTIErrorCode.UNDEFINED_FAILURE,
            "GWTPLookup for ShipMethod is missing in the database.");
      }
      else {
        orderTO.setShipDeliveryMethod(shipMethodlookupTO.getLookupValue());
      }

      String resPickup = UtilityXML.getEGalaxyDateFromGCalNoTime(resReq
          .getReservation().getResPickupDate());
      orderTO.setGroupVisitDate(resPickup + "00:00:00");

      // Since 2.9 Put bill name in group description (per Art Wightman)
      // Change for 2.12 - No longer check to see if shipping method is 1,
      // 2, or
      // 6.
      // No longer require a payment clause to be present on the in-bound
      // request.
      if ((resReq.getClientData().getBillingInfo() != null) && (resReq
          .getClientData().getBillingInfo().getName() != null)) {

        orderTO.setGroupVisitDescription(resReq.getClientData()
            .getBillingInfo().getName());
        
        if (resReq.getEligibilityGroup() == null) {
          throw new DTIException(
              DLRReservationRules.class,
              DTIErrorCode.INVALID_ELIGIBILITY_NBR,
              "Eligibility Group not provided as expected on order.");
        }

        // Populate Group Visit Reference only for BOLT style orders.
        if (resReq.getEligibilityGroup().equalsIgnoreCase(
            GW_ORDERS_DLR_BOLT_GROUP)) {
          orderTO.setGroupVisitReference(resReq.getClientData()
              .getBillingInfo().getName());
        }

      }

    }
    catch (NumberFormatException nfe) {
      throw new DTIException(
          DLRReservationRules.class,
          DTIErrorCode.UNDEFINED_FAILURE,
          "TPLookup value ShipType or ShipDetail was not a valid integer in the database.");
    }

    return orderTO;
  }

}
