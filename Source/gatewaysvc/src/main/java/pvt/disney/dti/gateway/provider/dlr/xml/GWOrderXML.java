package pvt.disney.dti.gateway.provider.dlr.xml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWMemberDemographicsTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderContactTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderLineTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrdersErrorsTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrdersRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrdersRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWPaymentContractTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.rules.TransformConstants;
import pvt.disney.dti.gateway.util.PCIControl;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This class is responsible for the marshalling and unmarshalling of the DLR order XML.
 * 
 * @author smoon
 * 
 */
public class GWOrderXML {

  public static final String PAYMENT_STATUS_APPROVED = "2";
  public static final String STATUS_CODE_SUCCESS = "0";
  public static final String STATUS_CODE_FAILURE = "3";
  public static final String STATUS_TEXT_OK = "OK";
  public static final String STATUS_TEXT_FAILURE = "Failure";

  /** The standard core logging mechanism. */
  private static EventLogger eventLogger = EventLogger
      .getLogger(GWOrderXML.class);

  /**
   * Adds the order element.
   * 
   * @param qtReqTO
   *            the qt req to
   * @param bodyElement
   *            the body element
   * 
   * @throws DTIException
   *             the DTI exception
   */
  public static void addOrderElement(GWOrdersRqstTO orderReqTO,
      Element bodyElement) throws DTIException {

    // Create the orders element
    Element ordersElement = bodyElement.addElement("Orders");

    // Iterate through individual order objects
    Iterator<GWOrderTO> orderIter = orderReqTO.getOrderList().iterator();
    if (!orderIter.hasNext()) {
      throw new DTIException(GWOrderXML.class,
          DTIErrorCode.INVALID_MSG_CONTENT,
          "DLR transaction missing order items");
    }

    while (orderIter.hasNext()) {
      GWOrderTO orderTO = orderIter.next();

      // create the order element and add it to the orders element
      Element orderElement = ordersElement.addElement("Order");

      // OrderID
      orderElement.addElement("OrderID").addText(orderTO.getOrderID());

      // CustomerID
      orderElement.addElement("CustomerID").addText(orderTO.getCustomerID());

      // Order Date
      orderElement.addElement("OrderDate").addText(orderTO.getOrderDate());

      // OrderStatus
      if (orderTO.getOrderStatus() != null && !orderTO.getOrderStatus()
          .equalsIgnoreCase("null") && orderTO.getOrderStatus()
          .length() > 0) orderElement.addElement("OrderStatus")
          .addText(orderTO.getOrderStatus());

      // SalesProgram
      if (orderTO.getSalesProgram() != null) {
        orderElement.addElement("SalesProgram").addText(orderTO.getSalesProgram());
      }

      // OrderTotal
      orderElement.addElement("OrderTotal").addText(orderTO.getOrderTotal().toString());

      // PaymentContracts (SECTION)
      if (orderTO.getPaymntContractList().size() > 0) {
        Element pmntCntrctsElement = orderElement
            .addElement("PaymentContracts");

        ArrayList<GWPaymentContractTO> paymentContractList = orderTO.getPaymntContractList();
        // There should be only one.
        GWPaymentContractTO gwPayContract = paymentContractList.get(0);

        Element pmntCntrctElement = pmntCntrctsElement.addElement("PaymentContract");
        Element recurElement = pmntCntrctElement.addElement("RecurrencePattern");

        recurElement.addElement("RecurrenceType").addText(gwPayContract.getRecurrenceType());
        recurElement.addElement("DayOfMonth").addText(gwPayContract.getDayOfMonth().toString());
        recurElement.addElement("Interval").addText(gwPayContract.getInterval().toString());
        recurElement.addElement("StartDate").addText(gwPayContract.getStartDate());
        recurElement.addElement("EndDate").addText(gwPayContract.getEndDate());

        pmntCntrctElement.addElement("PaymentPlanID").addText(gwPayContract.getPaymentPlanID());
        pmntCntrctElement.addElement("RenewContract").addText(gwPayContract.getRenewContract().toString());
        pmntCntrctElement.addElement("PaymentContractStatusID")
            .addText(gwPayContract.getPaymentContractStatusID());
        if (gwPayContract.getDownPaymentAmount() != null) {
          pmntCntrctElement.addElement("DownPaymentAmount").addText(gwPayContract.getDownPaymentAmount());
        }
        pmntCntrctElement.addElement("ContactMethod").addText(gwPayContract.getContactMethod());
      }

      // OrderReference
      if (orderTO.getOrderReference() != null && !orderTO
          .getOrderReference().equalsIgnoreCase("null") && orderTO
          .getOrderReference().length() > 0) orderElement.addElement(
          "OrderReference").addText(orderTO.getOrderReference());

      // OrderNote
      String orderNote = orderTO.getOrderNote();
      if (orderNote != null && orderNote.length() > 0) {
        orderElement.addElement("OrderNote").addText(orderNote);
      }

      // OrderContact (SECTION)
      addOrderContact(orderTO, orderElement);

      // ShipToContact (SECTION)
      addShipToContact(orderTO, orderElement);

      // Shipping (SECTION)
      addShipping(orderTO, orderElement);

      // OrderLines (SECTION)
      addOrderLines(orderTO, orderElement);

      // PCI Control -DO NOT REMOVE!!! (unless you removed the debug statement
      String cleanXML = PCIControl.overwritePciDataInXML(bodyElement.asXML());
      // END PCI Controls.
      eventLogger.sendEvent("GWOrderXML: XML Body Element: '" + cleanXML,
          EventType.DEBUG, Object.class);

      // END adding OrderLine elements to the Order element
    }
    // return bodyElement;
  }

  /**
   * @param orderTO
   * @param orderElement
   * @throws DTIException
   */
  private static void addOrderLines(GWOrderTO orderTO, Element orderElement) throws DTIException {

    // BEGIN adding OrderLine elements to the Order element
    Element orderLines = orderElement.addElement("OrderLines");

    Iterator<GWOrderLineTO> lineIter = orderTO.getOrderLineList()
        .iterator();
    while (lineIter.hasNext()) {

      GWOrderLineTO lineTO = lineIter.next();

      // OrderLine
      Element orderLineElement = orderLines.addElement("OrderLine");

      // DetailType
      String detailType = lineTO.getDetailType();
      orderLineElement.addElement("DetailType").addText(detailType);

      // Standard Ticket Product (Type 1 Detail)
      if (detailType
          .equals(TransformConstants.GW_ORDERS_TICKET_ORDER_LINE_ITEM)) {

        orderLineElement.addElement("PLU").addText(lineTO.getPlu());
        orderLineElement.addElement("Description").addText(
            lineTO.getDescription());

        // PaymentPlanID (optional for straight pay) (as of 2.16.1, JTL)
        if (lineTO.getPaymentPlanID() != null) {
          orderLineElement.addElement("PaymentPlanID").addText(
              lineTO.getPaymentPlanID());
        }

        if (lineTO.getTaxCode() != null && lineTO.getTaxCode().length() > 0) {
          orderLineElement.addElement("TaxCode").addText(
              lineTO.getTaxCode());
        }
        orderLineElement.addElement("Qty").addText(lineTO.getQty());
        orderLineElement.addElement("Amount").addText(
            lineTO.getAmount());
        orderLineElement.addElement("Total").addText(lineTO.getTotal());

        String ticketDate = lineTO.getTicketDate();
        if (ticketDate != null && ticketDate.length() > 0) {
          orderLineElement.addElement("TicketDate").addText(
              ticketDate);
        }

        // Payment (Type 2 Detail)
      }
      else if (detailType.equals(TransformConstants.GW_ORDERS_PAYMENT_ORDER_LINE_ITEM)) {
        orderLineElement.addElement("PaymentCode").addText(lineTO.getPaymentCode());
        orderLineElement.addElement("PaymentDate").addText(lineTO.getPaymentDate());
        orderLineElement.addElement("Description").addText(lineTO.getDescription());
        orderLineElement.addElement("Endorsement").addText(lineTO.getEndorsement());

        // Check for billing street (as of 2.16.2, JTL)
        String billingStreet = lineTO.getBillingStreet();
        if ( (billingStreet != null) && (billingStreet.length() > 0) ) {
             orderLineElement.addElement("BillingStreet").addText(lineTO.getBillingStreet());         
        }
        
        // null check for billing ZIP, cause gift cards and other payments
        // don't have it
        String billingZip = lineTO.getBillingZip();
        if (billingZip != null && billingZip.length() > 0) {
          orderLineElement.addElement("BillingZIP").addText(
              lineTO.getBillingZip());
        }
        
        orderLineElement.addElement("Amount").addText(
            lineTO.getAmount());
        orderLineElement.addElement("Total").addText(lineTO.getTotal());

        // null check for expirationDate, cause gift cards and other payments
        // don't have it
        String expDate = lineTO.getExpDate();
        if (expDate != null && expDate.length() > 0) {
          orderLineElement.addElement("ExpDate").addText(
              lineTO.getExpDate());
        }

        // null check for CVN, cause gift cards and other payments don't have
        // it
        String cvn = lineTO.getCvn();
        if (cvn != null && cvn.length() > 0) {
          orderLineElement.addElement("CVN").addText(lineTO.getCvn());
        }
      }
      else if (detailType
          .equals(TransformConstants.GW_ORDERS_PASS_ORDER_LINE_ITEM)) {

        // Amount
        orderLineElement.addElement("Amount").addText(
            lineTO.getAmount());

        // Description
        orderLineElement.addElement("Description").addText(
            lineTO.getDescription());

        // PaymentPlanID (optional for straight pay) (as of 2.16.1, JTL)
        if (lineTO.getPaymentPlanID() != null) {
          orderLineElement.addElement("PaymentPlanID").addText(
              lineTO.getPaymentPlanID());
        }

        // PLU
        orderLineElement.addElement("PLU").addText(lineTO.getPlu());

        // Qty
        orderLineElement.addElement("Qty").addText(lineTO.getQty());

        // Total
        orderLineElement.addElement("Total").addText(lineTO.getTotal());
        
        // Upgrade From Visual ID
        if (lineTO.getUpgradeFromVisualID() != null) {
           orderLineElement.addElement("UpgradeFromVisualID").addText(lineTO.getUpgradeFromVisualID());
        }

        // Are there demographics (there should be only one per line item,
        // thanks to flattening done prior during GW object creation.
        ArrayList<GWMemberDemographicsTO> memberList = lineTO.getMemberList();
        if ((memberList != null) && (memberList.size() > 0)) {

          GWMemberDemographicsTO gwDemoTO = memberList.get(0);

          // Pass (Section)
          Element passElement = orderLineElement.addElement("Pass");

          // FirstName
          if (gwDemoTO.getFirstName() != null) {
            passElement.addElement("FirstName").addText(gwDemoTO.getFirstName());
          }

          // LastName
          if (gwDemoTO.getLastName() != null) {
            passElement.addElement("LastName").addText(gwDemoTO.getLastName());
          }

          // Street1
          if (gwDemoTO.getStreet1() != null) {
            passElement.addElement("Street1").addText(gwDemoTO.getStreet1());
          }

          // Street2
          if (gwDemoTO.getStreet2() != null) {
            passElement.addElement("Street2").addText(gwDemoTO.getStreet2());
          }

          // City
          if (gwDemoTO.getCity() != null) {
            passElement.addElement("City").addText(gwDemoTO.getCity());
          }

          // State
          if (gwDemoTO.getState() != null) {
            passElement.addElement("State").addText(gwDemoTO.getState());
          }

          // ZIP
          if (gwDemoTO.getZip() != null) {
            passElement.addElement("ZIP").addText(gwDemoTO.getZip());
          }

          // CountryCode
          if (gwDemoTO.getCountryCode() != null) {
            passElement.addElement("CountryCode").addText(gwDemoTO.getCountryCode());
          }

          // Phone
          if (gwDemoTO.getPhone() != null) {
            passElement.addElement("Phone").addText(gwDemoTO.getPhone());

          }

          // Email (NOT INCLUDED ACCORDING TO JENNIFER OVERTURF)

          // DOB
          if (gwDemoTO.getDateOfBirth() != null) {
            passElement.addElement("DOB").addText(gwDemoTO.getDateOfBirth());
          }

          // Gender
          if (gwDemoTO.getGender() != null) {
            passElement.addElement("Gender").addText(gwDemoTO.getGender());
          }

          // VisualID
          if (gwDemoTO.getVisualID() != null) {
            passElement.addElement("VisualID").addText(gwDemoTO.getVisualID());
          }

        }

      }
      else {
        throw new DTIException(GWOrderXML.class,
            DTIErrorCode.INVALID_MSG_CONTENT, "Invalid OrderLine DetailType");
      }
    }
  }

  /**
   * @param orderTO
   * @param orderElement
   */
  private static void addShipping(GWOrderTO orderTO, Element orderElement) {
    Element shippingElement = orderElement.addElement("Shipping");
    shippingElement.addElement("DeliveryMethod").addText(
        orderTO.getShipDeliveryMethod());
    shippingElement.addElement("DeliveryDetails").addText(
        orderTO.getShipDeliveryDetails());

    // BEGIN add the GroupVisit Element to Order

    // add VisitDate to GroupVisit
    if (orderTO.getGroupVisitDate() != null && orderTO.getGroupVisitDate()
        .length() > 0 && !orderTO.getGroupVisitDate().equals("null")) {
      Element groupVisit = orderElement.addElement("GroupVisit");
      groupVisit.addElement("VisitDate").addText(
          orderTO.getGroupVisitDate());

      if (orderTO.getGroupVisitDescription() != null) {
        groupVisit.addElement("Description").addText(
            orderTO.getGroupVisitDescription());
      }

      // Code 2.11 to add Group Visit Reference
      if (orderTO.getGroupVisitReference() != null) {
        groupVisit.addElement("Reference").addText(
            orderTO.getGroupVisitReference());
      }

    }
  }

  /**
   * @param orderTO
   * @param orderElement
   */
  private static void addShipToContact(GWOrderTO orderTO, Element orderElement) {
    // BEGIN the ShipToContact element, add it to the Order element
    if (orderTO.getShipToContact() != null) {
      Element shipToElement = orderElement.addElement("ShipToContact");
      Element shipContact = shipToElement.addElement("Contact");
      GWOrderContactTO shipContactTo = orderTO.getShipToContact();

      String firstName = shipContactTo.getFirstName();
      String lastName = shipContactTo.getLastName();
      String street1 = shipContactTo.getStreet1();
      String street2 = shipContactTo.getStreet2();
      String street3 = shipContactTo.getStreet3();
      String city = shipContactTo.getCity();
      String state = shipContactTo.getState();
      String zip = shipContactTo.getZip();
      String country = shipContactTo.getCountry();
      String phone = shipContactTo.getPhone();
      String email = shipContactTo.getEmail();

      if (firstName != null && firstName.length() > 0 && !(firstName
          .equals("null"))) {
        shipContact.addElement("FirstName").addText(firstName);
      }
      if (lastName != null && lastName.length() > 0 && !(lastName
          .equals("null"))) {
        shipContact.addElement("LastName").addText(lastName);
      }

      if (street1 != null && street1.length() > 0 && !(street1
          .equals("null"))) {
        shipContact.addElement("Street1").addText(street1);
      }

      if (street2 != null && street2.length() > 0 && !(street2
          .equals("null"))) {
        shipContact.addElement("Street2").addText(street2);
      }

      if (street3 != null && street3.length() > 0 && !(street3
          .equals("null"))) {
        shipContact.addElement("Street3").addText(street3);
      }

      if (city != null && city.length() > 0 && !(city.equals("null"))) {
        shipContact.addElement("City").addText(city);
      }

      if (state != null && state.length() > 0 && !(state.equals("null"))) {
        shipContact.addElement("State").addText(state);
      }

      if (zip != null && zip.length() > 0 && !(zip.equals("null"))) {
        shipContact.addElement("Zip").addText(zip);
      }
      if (country != null && country.length() > 0 && !(country
          .equals("null"))) {
        shipContact.addElement("Country").addText(country);
      }
      if (phone != null && phone.length() > 0 && !(phone.equals("null"))) {
        shipContact.addElement("Phone").addText(phone);
      }
      if (email != null && email.length() > 0 && !(email.equals("null"))) {
        shipContact.addElement("Email").addText(email);
      }

    }
    // END the ShipToContact element, add it to the Order element
  }

  /**
   * @param orderTO
   * @param orderElement
   */
  private static void addOrderContact(GWOrderTO orderTO, Element orderElement) {
    // BEGIN the OrderContact element, add it to the Order element
    Element orderContact = orderElement.addElement("OrderContact");

    // add the Contact element to the OrderContact element
    Element contact = orderContact.addElement("Contact");
    GWOrderContactTO contactTo = orderTO.getOrderContact();
    contact.addElement("FirstName").addText(contactTo.getFirstName());
    contact.addElement("LastName").addText(contactTo.getLastName());
    contact.addElement("Street1").addText(contactTo.getStreet1());
    if (contactTo.getStreet2() != null && contactTo.getStreet2().length() > 0) {
      contact.addElement("Street2").addText(contactTo.getStreet2());
    }
    if (contactTo.getStreet3() != null && contactTo.getStreet3().length() > 0) {
      contact.addElement("Street3").addText(contactTo.getStreet3());
    }
    contact.addElement("City").addText(contactTo.getCity());
    // WDPROFIX
    if (contactTo.getState() != null && contactTo.getState().length() > 0 && !contactTo
        .getState().equals("null")) {
      contact.addElement("State").addText(contactTo.getState());
    }
    contact.addElement("Zip").addText(contactTo.getZip());
    contact.addElement("Country").addText(contactTo.getCountry());
    contact.addElement("Phone").addText(contactTo.getPhone());
    contact.addElement("Email").addText(contactTo.getEmail());
    // END the OrderContact element
  }

  /**
   * 
   * @param gwBodyTO
   * @param bodyElement
   * @throws DTIException
   */
  @SuppressWarnings("unchecked")
  public static void setRespBodyTO(GWBodyTO gwBodyTO, Element bodyElement) throws DTIException {

    GWOrdersRespTO gwOrdersRespTO = new GWOrdersRespTO();

    for (Iterator<org.dom4j.Element> bodyIterator = bodyElement
        .elementIterator(); bodyIterator.hasNext();) {
      Element element = bodyIterator.next();

      if (element.getName().compareTo("Status") == 0) {
        GWStatusTO statusTO = setStatusTO(element);
        gwBodyTO.setStatusTO(statusTO);
      }

      if (element.getName().compareTo("OrderStatusError") == 0) {
        GWOrdersErrorsTO errorsTO = setErrorsTO(element);
        gwBodyTO.setOrdersErrorsTO(errorsTO);
      }

      // Get the Galaxy Order ID, if present.
      if (element.getName().compareTo("GalaxyOrderID") == 0) {
        gwOrdersRespTO.setGalaxyOrderID(element.getText());
      }

      // Get the Order ID, if present
      if (element.getName().compareTo("OrderID") == 0) {
        gwOrdersRespTO.setOrderID(element.getText());
      }

      // Get the Authorization Code, if present
      if (element.getName().compareTo("AuthCode") == 0) {
        gwOrdersRespTO.setAuthCode(element.getText());
      }

      // Get the PaymentContracts section, if present (as of 2.16.1, JTL)
      if (element.getName().equalsIgnoreCase("PaymentContracts")) {

        // Locate the Products element
        for (Iterator<org.dom4j.Element> contractRespIter = element
            .elementIterator(); contractRespIter.hasNext();) {
          Element childElement = contractRespIter.next();

          if (childElement.getName().equalsIgnoreCase(
              "PaymentContractID")) {
            if (gwOrdersRespTO.getPaymentContractID() == null) {
              gwOrdersRespTO.setPaymentContractID(childElement
                  .getText());
            }
          }
        }
      }

      // Get the Products subsection, if present
      if (element.getName().compareTo("CreateTransactionResponse") == 0) {

        // Locate the Products element
        for (Iterator<org.dom4j.Element> crtTxnRespIter = element
            .elementIterator(); crtTxnRespIter.hasNext();) {
          Element childElement = crtTxnRespIter.next();

          if (childElement.getName().compareTo("Products") == 0) {
            setProducts(childElement, gwOrdersRespTO);
          }
        }
      }

      gwBodyTO.setOrdersRespTO(gwOrdersRespTO);

    }

    if ((gwBodyTO.getOrdersRespTO()) == null && (gwBodyTO
        .getOrdersErrorsTO() == null)) {
      throw new DTIException(GWBodyXML.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned OrdersResponse without a Error or Response clauses.");
    }

    return;
  }

  /**
   * Sets up the products in the response.  Note, excludes negative quantity items.
   * @param productsElement
   * @param gwOrdersRespTO
   */
  @SuppressWarnings({ "unchecked" })
  public static void setProducts(Element productsElement, GWOrdersRespTO gwOrdersRespTO) {

    eventLogger.sendEvent("Entering GWOrderXML.setProducts()", EventType.DEBUG, Object.class);

    ArrayList<GWOrdersRespTO.TicketRecord> gwTicketListTO = gwOrdersRespTO.getTicketArray();

    // Iterate through each product element
    for (Iterator<org.dom4j.Element> productListIter = productsElement
        .elementIterator(); productListIter.hasNext();) {

      Element productElement = productListIter.next();

      // Validate that this element is marked (attributed) as a Type="Ticket"
      boolean isATicket = false;
      for (Iterator<org.dom4j.Attribute> attrIter = productElement
          .attributeIterator(); attrIter.hasNext();) {
        Attribute anAttribute = attrIter.next();

        if (anAttribute.getName().compareTo("Type") == 0) {
          if (anAttribute.getText().compareTo("Ticket") == 0) {
            isATicket = true;
          }
        }
      }

      if (isATicket) {

        GWOrdersRespTO.TicketRecord gwTicketTO = gwOrdersRespTO.new TicketRecord();
        boolean isPositiveQuantity = true;

        for (Iterator<org.dom4j.Element> ticketIter = productElement.elementIterator(); ticketIter.hasNext();) {

          Element ticketElement = ticketIter.next();

          // Get the visualID
          if (ticketElement.getName().compareTo("VisualID") == 0) {
            gwTicketTO.setVisualID(ticketElement.getText());
          }

          // Get the PLU
          if (ticketElement.getName().compareTo("PLU") == 0) {
            gwTicketTO.setPlu(ticketElement.getText());
          }
          
          // Get the Qty (Quantity) to determine if this should be output.
          if (ticketElement.getName().compareTo("Qty") == 0) {
             String quantityString = ticketElement.getText();
             int qty = Integer.parseInt(quantityString);
             if (qty > 0) {
               isPositiveQuantity = true;
             } else {
               isPositiveQuantity = false;
             }
          }
          
          // Get the ItemDescription
          if (ticketElement.getName().compareTo("ItemSecription") == 0) {
            gwTicketTO.setDescription(ticketElement.getText());
          }

          // Get the price (Strip $, w/2.16.1, JTL)
          if (ticketElement.getName().compareTo("Price") == 0) {
            String formattedPriceString = ticketElement.getText();
            String priceString = formattedPriceString.replaceAll("[$,]", "");
            if (priceString.contains(".")) { 
               gwTicketTO.setPrice(new BigDecimal(priceString));
            } else {
               gwTicketTO.setPrice(new BigDecimal(priceString + ".00"));
            }
          }
        }

        // if the Visual ID is present, add the ticket to the array.
        if ((gwTicketTO.getVisualID() != null) && (isPositiveQuantity)) {
          gwTicketListTO.add(gwTicketTO);
        }
      } // is a ticket

    } // product for loop

    eventLogger.sendEvent(
            "GWOrderXML.setProducts() found this number of tickets: '" + gwTicketListTO
                .size(), EventType.DEBUG, Object.class);

    return;

  }

  /**
   * Marshalls the gateway status transfer object.
   * 
   * @param statusElement
   *            the response supplied by gateway.
   * @return the Gateway Status Transfer Object.
   * @throws DTIException
   *             should any unmarshalling error occur.
   */
  private static GWStatusTO setStatusTO(Element bodyElement) throws DTIException {

    GWStatusTO gwStatusTO = new GWStatusTO();
    String statusCode = null;
    String statusText = null;

    // get the status fields and set them in the status to
    org.dom4j.XPath statusCodeSelector = DocumentHelper
        .createXPath("//Status");
    statusCode = statusCodeSelector.selectSingleNode(bodyElement).getText();

    if (statusCode == null) throw new DTIException(GWBodyXML.class,
        DTIErrorCode.TP_INTERFACE_FAILURE,
        "Ticket provider returned Status clause missing a StatusCode.");

    // we need to manipulate status code and text here since order status and
    // errors behave completely differently than for voids, query, ticket
    // activation
    if (statusCode.equals(PAYMENT_STATUS_APPROVED)) {
      statusCode = STATUS_CODE_SUCCESS;
      statusText = STATUS_TEXT_OK;
    }
    else if (statusCode.equals(STATUS_CODE_FAILURE)) {
      // we know statusCode = STATUS_CODE_FAILED
      org.dom4j.XPath errorCodeSelector = DocumentHelper
          .createXPath("//OrderStatusError/ErrorCode");
      statusCode = errorCodeSelector.selectSingleNode(bodyElement)
          .getText();

      org.dom4j.XPath errorTextSelector = DocumentHelper
          .createXPath("//OrderStatusError/ErrorText");
      statusText = errorTextSelector.selectSingleNode(bodyElement)
          .getText();
    }

    gwStatusTO.setStatusCode(statusCode);
    gwStatusTO.setStatusText(statusText);

    return gwStatusTO;
  }

  /**
   * 
   * Unmarshall the errors element into the gateway orders errors transfer object.
   * 
   * @param errorsElement
   *            as provided by the parsed eGalaxy response.
   * @return the gateway ticket activations errors transfer object.
   * @throws DTIException
   *             should any unmarshalling problem occurs.
   */
  private static GWOrdersErrorsTO setErrorsTO(Element errorsElement) throws DTIException {
    GWOrdersErrorsTO errorsTO = new GWOrdersErrorsTO();
    // get the error fields and set them in the error to
    org.dom4j.XPath statusCodeSelector = DocumentHelper
        .createXPath("//OrderStatusError/ErrorCode");
    String statusCode = statusCodeSelector.selectSingleNode(errorsElement)
        .getText();

    org.dom4j.XPath statusTextSelector = DocumentHelper
        .createXPath("//OrderStatusError/ErrorText");
    String statusText = statusTextSelector.selectSingleNode(errorsElement)
        .getText();

    errorsTO.setErrorCode(statusCode);
    errorsTO.setErrorText(statusText);

    if (errorsTO.getErrorCode() == null) {
      throw new DTIException(
          GWHeaderTO.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned OrderStatusError.OrdersError.OrderStatusError clause without ErrorCode clause.");
    }

    if (errorsTO.getErrorText() == null) {
      throw new DTIException(
          GWHeaderTO.class,
          DTIErrorCode.TP_INTERFACE_FAILURE,
          "Ticket provider returned OrderStatusError.OrdersError.OrderStatusError clause without ErrorText clause.");
    }

    return errorsTO;
  }

}
