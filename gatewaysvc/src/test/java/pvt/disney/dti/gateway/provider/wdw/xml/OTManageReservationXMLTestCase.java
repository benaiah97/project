package pvt.disney.dti.gateway.provider.wdw.xml;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTManageReservationTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.*;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTCreditCardTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTProductTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTReservationDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * Junit test for the Omni Ticket Manage Reservation XML translations.
 * 
 * @author lewit019
 * 
 */
public class OTManageReservationXMLTestCase {

  /**
   * Sets the up before class.
   * 
   * @throws Exception
   *           the exception 
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * Tear down after class.
   * 
   * @throws Exception
   *           the exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * Sets the up.
   * 
   * @throws Exception
   *           the exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * Tear down.
   * 
   * @throws Exception
   *           the exception
   */
  @After
  public void tearDown() throws Exception {
  }

  /**
   * Tests the ability of the system to create XML from a properly constructed
   * transfer object.
   */
  @Test
  public final void testGetXML() {

    // ***** Read the test file. *****
    String baselineXML = null;
    String fileName = new String("./config/Reservation/Reservation_01_Req_WDW.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    OTCommandTO otCommandTO = createManageResRequest();

    String newXML = null;
    try {
      newXML = OTCommandXML.getXML(otCommandTO);
    } catch (DTIException dtie) {
      fail("DTI Exception getting string from transfer object: " + dtie.toString());
    }

    // Validate Reservation Response
    String resRespBaseline = DTITestUtilities.findTag(baselineXML, "ManageReservation");
    String resRespNew = DTITestUtilities.findTag(newXML, "ManageReservation");

    try {
      DTITestUtilities.compareXML(resRespBaseline, resRespNew, "ManageReservation");
    } catch (Exception e) {
      fail("Exception validating ReservationResponse section: " + e.toString());
    }

    return;
  }

  /**
   * Tests the ability of the system to create a properly constructed transfer
   * object from XML responses.
   */
  @Test
  public final void testGetTO() {

    // ***** Read the test file. *****
    String baselineXML = null;
    String fileName = new String("./config/Reservation/Reservation_01_Rsp_WDW.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    OTCommandTO otCommandTO = null;

    try {
      otCommandTO = OTCommandXML.getTO(baselineXML);
    } catch (DTIException dtie) {
      fail("DTI Exception getting transfer object from XML string: " + dtie.toString());
    }

    try {
      validateManageReservationRspTO(otCommandTO);
    } catch (Exception e) {
      fail("Failure in validating command header: " + e.toString());
    }

    return;
  }

  /**
   * Validates the transaction response transfer object to determine if it was
   * formed properly.
   * 
   * @param otCommandTO
   *          the Omni Ticket Command Transfer Object containing the response.
   * @throws Exception
   *           for any validation failures.
   */
  private void validateManageReservationRspTO(OTCommandTO otCommandTO) throws Exception {

    // Header
    OTHeaderTO otHdr = otCommandTO.getHeaderTO();
    if (otHdr == null)
      throw new Exception("OTCommandTO object had no OTHeaderTO object.");
    OTTestUtil.validateOTRspHeader(otHdr, OTTestUtil.REQTYPE_MANAGE,
        OTTestUtil.REQSUBTYPE_MANAGERES);

    // ManageReservation
    OTManageReservationTO otMngRes = otCommandTO.getManageReservationTO();
    if (otMngRes == null)
      throw new Exception("OTCommandTO object had no OTManageReservationTO object.");

    // Error
    OTErrorTO otErr = otMngRes.getError();
    if (otErr == null)
      throw new Exception("OTManageReservationTO object had no OTErrorTO object.");
    OTTestUtil.validateOTRspError(otErr);

    String mngResNode = "OTManageReservationTO";

    // CommandType
    OTTestUtil.validateValue(mngResNode, "CommandType", OTTestUtil.CMDTYPE, otMngRes
        .getCommandType());

    // TicketInfo
    ArrayList<OTTicketInfoTO> ticketInfoList = otMngRes.getTicketInfoList();
    if ((ticketInfoList == null) || (ticketInfoList.size() == 0))
      throw new Exception("OTManageReservationTO object had no or empty TicketInfo list.");
    validateTicketInfoList(ticketInfoList);

    // ProductInfo
    ArrayList<OTProductTO> productList = otMngRes.getProductList();
    if ((productList == null) || (productList.size() == 0))
      throw new Exception("OTManageReservationTO object had no or empty ProductInfo list.");
    validateProductList(productList);

    // PaymentInfo
    ArrayList<OTPaymentTO> paymentList = otMngRes.getPaymentInfoList();
    OTTestUtil.validatePaymentList(paymentList);

    // TransactionDSSN
    OTTestUtil.validateTranDSSN(otMngRes.getTransactionDSSN());

    // TransactionCOD
    OTTestUtil.validateValue(mngResNode, "TransactionCOD", OTTestUtil.TRANCOD, otMngRes
        .getTransactionCOD());

    // TransactionTime
    OTTestUtil.validateValue(mngResNode, "TransactionTime", OTTestUtil.TRANTIME, otMngRes
        .getTransactionTime());

    // TotalAmount
    OTTestUtil.validateValue(mngResNode, "TotalAmount", OTTestUtil.TOTALAMT, otMngRes
        .getTotalAmount());

    // TotalTax
    OTTestUtil.validateValue(mngResNode, "TotalTax", OTTestUtil.TOTALTAX, otMngRes.getTotalTax());

    // TaxExempt
    OTTestUtil
        .validateValue(mngResNode, "TaxExempt", OTTestUtil.TAXEXEMPT, otMngRes.getTaxExempt());

    // ReservationCode
    OTTestUtil.validateValue(mngResNode, "ReservationCode", OTTestUtil.RESCODE, otMngRes
        .getReservationCode());

    // ReservationId
    OTTestUtil.validateValue(mngResNode, "ReservationId", OTTestUtil.RESERVATIONID, otMngRes
        .getReservationId());

    // ReservationData
    OTReservationDataTO otResData = otMngRes.getReservationData();
    if (otResData == null)
      throw new Exception("OTManageReservationTO object had no ReservationData object.");
    validateReservationData(otResData);

    // ClientData
    OTClientDataTO otClientData = otMngRes.getClientData();
    if (otClientData == null)
      throw new Exception("OTManageReservationTO object had no ClientData object.");
    validateClientData(otClientData);

    return;
  }

  /**
   * Validates the ticket info section of the response.
   * 
   * @param tktInfoList
   *          the list of Omni Ticket Ticket Info Transfer Objects.
   * @throws Exception
   *           for any validation error
   */
  private static void validateTicketInfoList(ArrayList<OTTicketInfoTO> ticketInfoList)
      throws Exception {

    String nodeName = "OTTicketInfoTO";

    if (ticketInfoList.size() != 2)
      throw new Exception("OTTicketInfoTO list did not have 2 members, as expected.");

    int itemNum = 0;

    for /* each */(OTTicketInfoTO aTicketInfo : /* in */ticketInfoList) {

      // Item
      BigInteger item = aTicketInfo.getItem();
      if (item == null)
        throw new Exception("OTTicketInfoTO object had no Item");
      BigInteger validItem = BigInteger.valueOf(++itemNum);
      if (validItem.compareTo(item) != 0)
        throw new Exception("OTTicketInfoTO object had invalid Item");

      // ItemStatus
      OTTestUtil.validateValue("OTTicketInfoTO", "ItemStatus", OTTestUtil.ITEMSTATUS, aTicketInfo
          .getItemStatus());

      // ItemType (ignored)
      // ItemAlphaCode (ignored)
      // ItemNumCode (ignored)
      // TicketName (ignored)
      // Description (ignored)
      // PrintedPrice (ignored)
      // TicketFlag (ignored)
      // TicketAttribute (ignored)
      // TktNote (ignored)
      // BiometricLevel (ignored)
      // ShowData (ignored)
      // SeatData (ignored)
      // Layout (ignored)

      // Price
      OTTestUtil.validateValue(nodeName, "Price", OTTestUtil.PRICE, aTicketInfo.getPrice());

      // Tax
      OTTestUtil.validateValue(nodeName, "Tax", OTTestUtil.TAX, aTicketInfo.getTax());

      // Ticket (obj)
      OTTestUtil.validateTicket(aTicketInfo.getTicket());

      // Validity StartDate
      OTTestUtil.validateValue(nodeName, "Validity.StartDate", OTTestUtil.STARTDATE, aTicketInfo
          .getValidityStartDate());

      // Validity EndDate
      OTTestUtil.validateValue(nodeName, "Validity.EndDate", OTTestUtil.ENDDATE, aTicketInfo
          .getValidityEndDate());

    } // TicketInfo Loop

    return;
  }

  /**
   * Validates the product list on the response.
   * 
   * @param productList
   *          list of Omni Ticket Product Transfer Objects
   * @throws Exception
   *           for any validation error.
   */
  private static void validateProductList(ArrayList<OTProductTO> productList) throws Exception {

    if (productList.size() != 2)
      throw new Exception("ProductInfo list did not have 2 members, as expected.");

    String nodeName = "OTProductTO";

    int itemNum = 0;

    for /* each */(OTProductTO aProduct : /* in */productList) {

      // Item
      BigInteger item = aProduct.getItem();
      if (item == null)
        throw new Exception("OTProductTO object had no Item");
      BigInteger validItem = BigInteger.valueOf(++itemNum);
      if (validItem.compareTo(item) != 0)
        throw new Exception("OTProductTO object had invalid Item");

      // TicketType
      OTTestUtil.validateValue(nodeName, "TicketType", OTTestUtil.TICKETTYPE, aProduct
          .getTicketType());

      // Price
      OTTestUtil.validateValue(nodeName, "Price", OTTestUtil.PRICE, aProduct.getPrice());

      // Qty
      OTTestUtil.validateValue(nodeName, "Qty", OTTestUtil.QUANTITY, aProduct.getQuantity());

      // Tax
      OTTestUtil.validateValue(nodeName, "Tax", OTTestUtil.TAX, aProduct.getTax());

      // TicketName
      OTTestUtil.validateValue(nodeName, "TicketName", OTTestUtil.TICKETNAME, aProduct
          .getTicketName());

      // Description
      OTTestUtil.validateValue(nodeName, "Description", OTTestUtil.DESCRPTION, aProduct
          .getDescription());

    }

    return;
  }

  /**
   * Validates the reservation data section of the response.
   * 
   * @param otResData
   *          the Omni Ticket Reservation Data Transfer Object.
   * @throws Exception
   *           for any validation error.
   */
  private static void validateReservationData(OTReservationDataTO otResData) throws Exception {

    String nodeName = "ReservationData";

    // Paid
    OTTestUtil.validateValue(nodeName, "Paid", OTTestUtil.PAID, otResData.getPaid());

    // Printed
    OTTestUtil.validateValue(nodeName, "Printed", OTTestUtil.PRINTED, otResData.getPrinted());

    // Encoded
    OTTestUtil.validateValue(nodeName, "Encoded", OTTestUtil.ENCODED, otResData.getEncoded());

    // Validated
    OTTestUtil.validateValue(nodeName, "Validated", OTTestUtil.VALIDATED, otResData.getValidated());

    // DepositAmount
    OTTestUtil.validateValue(nodeName, "DepositAmount", OTTestUtil.DEPOSITAMOUNT, otResData
        .getDepositAmount());

    // SalesType
    OTTestUtil.validateValue(nodeName, "SalesType", OTTestUtil.SALESTYPE, otResData.getSalesType());

    // ResStatus
    OTTestUtil.validateValue(nodeName, "ResStatus", OTTestUtil.RESSTATUS, otResData.getResStatus());

    // ResPickupDate
    OTTestUtil.validateValue(nodeName, "ResPickupDate", OTTestUtil.RSPRESPICKUPDATE, otResData
        .getResPickupDate());

    // ResPickupArea
    OTTestUtil.validateValue(nodeName, "ResPickupArea", OTTestUtil.RESPICKUPAREA, otResData
        .getResPickupArea());

    // ResPickupType
    OTTestUtil.validateValue(nodeName, "ResPickupType", OTTestUtil.RESPICKUPTYPE, otResData
        .getResPickupType());

    return;
  }

  /**
   * Validates the client data section of the response.
   * 
   * @param otClientData
   *          the Omni Ticket Client Data Transfer Object.
   * @throws Exception
   *           for any validation error.
   */
  private static void validateClientData(OTClientDataTO otClientData) throws Exception {

    String nodeName = "ClientData";

    // ClientUniqueId
    OTTestUtil.validateValue(nodeName, "ClientUniqueId", OTTestUtil.CLIENTUNIQUEID, otClientData
        .getClientUniqueId());

    // ClientType
    OTTestUtil.validateValue(nodeName, "ClientType", OTTestUtil.CLIENTTYPE, otClientData
        .getClientType());

    // ClientSubtype
    OTTestUtil.validateValue(nodeName, "ClientSubtype", OTTestUtil.CLIENTSUBTYPE, otClientData
        .getClientSubtype());

    // ClientCategory
    OTTestUtil.validateValue(nodeName, "ClientCategory", OTTestUtil.CLIENTCATEGORY, otClientData
        .getClientCategory());

    // ClientLanguage
    OTTestUtil.validateValue(nodeName, "ClientLanguage", OTTestUtil.RSPCLIENTLANGUAGE, otClientData
        .getClientLanguage());

    // DemographicData
    ArrayList<OTFieldTO> demoList = otClientData.getDemographicData();
    if ((demoList == null) || (demoList.size() == 0))
      throw new Exception("ClientData object had no or empty DemographicData list.");
    validateDemoList(demoList);

    return;
  }

  /**
   * Validates the demographic data list from the response.
   * 
   * @param demoList
   *          the list of Omni Ticket Field Transfer Objects that contain
   *          demographic data
   * @throws Exception
   *           for any validation error.
   */
  private static void validateDemoList(ArrayList<OTFieldTO> demoList) throws Exception {

    if (demoList.size() != 2)
      throw new Exception("DemographicData list did not have 2 members, as expected.");

    String nodeName = "DemographicData.Field";

    int itemNum = 0;

    for /* each */(OTFieldTO aField : /* in */demoList) {

      // Item
      Integer item = aField.getFieldIndex();
      if (item == null)
        throw new Exception("DemographicData.Field object had no Item");
      Integer validItem = Integer.valueOf(++itemNum);
      if (validItem.compareTo(item) != 0)
        throw new Exception("DemographicData.Field object had invalid Item");

      // FieldType
      OTTestUtil.validateValue(nodeName, "FieldType", OTTestUtil.FIELDTYPE, aField.getFeildType());

      // FieldValue
      OTTestUtil.validateValue(nodeName, "FieldValue", OTTestUtil.FIELDVALUE, aField
          .getFieldValue());
    }

    return;
  }

  /**
   * Creates the Omni Ticket request object for this type of transaction.
   * 
   * @return the Omni Ticket Command Transfer Object.
   */
  private static OTCommandTO createManageResRequest() {

    OTCommandTO otCmdTO = new OTCommandTO(OTCommandTO.OTTransactionType.MANAGERESERVATION);

    // Header
    OTHeaderTO otHdrTO = OTTestUtil.createOTReqHeader(OTTestUtil.REQTYPE_MANAGE,
        OTTestUtil.REQSUBTYPE_MANAGERES);

    otCmdTO.setHeaderTO(otHdrTO);

    OTManageReservationTO otMngTO = new OTManageReservationTO();

    // Work Rules
    ArrayList<String> workRules = otMngTO.getTagsList();
    workRules.add(OTTestUtil.TAGS_ALL);

    // Site Number
    otMngTO.setSiteNumber(Integer.decode(OTTestUtil.SITENUMBER));

    // Command Type
    otMngTO.setCommandType(OTTestUtil.CMDTYPE);

    // Reservation Code
    otMngTO.setReservationCode(OTTestUtil.RESCODE);

    // Products
    ArrayList<OTProductTO> productList = otMngTO.getProductList();
    populateProductArray(productList);

    // Payments
    ArrayList<OTPaymentTO> paymentList = otMngTO.getPaymentInfoList();
    populatePaymentInfoArray(paymentList);

    // SellerId
    otMngTO.setSellerId(Long.decode(OTTestUtil.SELLERID));

    // AssociationInfo
    OTAssociationInfoTO assnInfoTO = new OTAssociationInfoTO();
    assnInfoTO.setAssociationId(Integer.decode(OTTestUtil.ASSOCIATIONID));
    assnInfoTO.setMemberId(Integer.decode(OTTestUtil.MEMBERID));
    otMngTO.setAssociationInfo(assnInfoTO);

    // IATA
    otMngTO.setIATA(OTTestUtil.IATA);

    // ReservationData
    OTReservationDataTO otResTO = new OTReservationDataTO();
    otResTO.setPrinted(Boolean.parseBoolean(OTTestUtil.TRUE));
    otResTO.setValidated(Boolean.parseBoolean(OTTestUtil.TRUE));
    otResTO.setSalesType(Integer.decode(OTTestUtil.SALESTYPE));
    GregorianCalendar resPickupDate = (GregorianCalendar) GregorianCalendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date aDate = null;
    try {
      aDate = sdf.parse(OTTestUtil.REQRESPICKUPDATE);
    } catch (Exception e) {
      fail("Invalid REQRESPICKUPDATE format in OTTestUtil.");
    }
    resPickupDate.setTime(aDate);
    otResTO.setResPickupDate(resPickupDate);
    otResTO.setResPickupArea(Integer.decode(OTTestUtil.RESPICKUPAREA));
    otResTO.setResPickupType(Integer.decode(OTTestUtil.REQRESPICKUPTYPE));
    otMngTO.setReservationData(otResTO);

    // Client Data
    OTClientDataTO otClient = new OTClientDataTO();
    otClient.setClientUniqueId(Integer.decode(OTTestUtil.CLIENTUNIQUEID));
    otClient.setClientType(OTTestUtil.CLIENTTYPE);
    otClient.setClientCategory(OTTestUtil.CLIENTCATEGORY);
    otClient.setClientLanguage(Integer.decode(OTTestUtil.REQCLIENTLANGUAGE));
    ArrayList<OTFieldTO> demoList = otClient.getDemographicData();
    OTFieldTO otField1 = new OTFieldTO(OTFieldTO.RES_BILL_NAME,
        OTTestUtil.FIELDVALUE);
    demoList.add(otField1);
    OTFieldTO otField2 = new OTFieldTO(OTFieldTO.RES_BILL_NAME,
        OTTestUtil.FIELDVALUE);
    demoList.add(otField2);
    otMngTO.setClientData(otClient);

    otCmdTO.setManageReservationTO(otMngTO);

    return otCmdTO;
  }

  /**
   * Creates the payment information array for the test request.
   * 
   * @param paymentList
   *          the pointer to the payment list in the transaction object.
   */
  private static void populatePaymentInfoArray(ArrayList<OTPaymentTO> paymentList) {

    // Payment 1
    OTPaymentTO payment1 = new OTPaymentTO();
    payment1.setPayItem(new BigInteger(OTTestUtil.ITEMONE));
    payment1.setPayType(OTPaymentTO.PaymentType.CREDITCARD);
    payment1.setPayAmount(new BigDecimal(OTTestUtil.PAYAMOUNT));
    OTCreditCardTO ccard = new OTCreditCardTO();
    ccard.setCardNumber(OTTestUtil.PAYCARDNUM);
    ccard.setCardExpDate(OTTestUtil.PAYCARDEXP);
    ccard.setAvs_AvsZipCode(OTTestUtil.PAYAVSZIPCODE);
    ccard.setAvs_AvsStreet(OTTestUtil.PAYAVSSTREET);

    payment1.setCreditCard(ccard);
    paymentList.add(payment1);

    // Payment 2
    OTPaymentTO payment2 = new OTPaymentTO();
    payment2.setPayItem(new BigInteger(OTTestUtil.ITEMTWO));
    payment2.setPayType(OTPaymentTO.PaymentType.CREDITCARD);
    payment2.setPayAmount(new BigDecimal(OTTestUtil.PAYAMOUNT));
    ccard = new OTCreditCardTO();
    ccard.setCardNumber(OTTestUtil.PAYCARDNUM);
    ccard.setCardExpDate(OTTestUtil.PAYCARDEXP);
    ccard.setAvs_AvsZipCode(OTTestUtil.PAYAVSZIPCODE);
    ccard.setAvs_AvsStreet(OTTestUtil.PAYAVSSTREET);

    payment2.setCreditCard(ccard);
    paymentList.add(payment2);

    return;
  }

  /**
   * Populates the product array in the request object.
   * 
   * @param productList
   *          the (empty) list of Omni Ticket Product transfer objects
   */
  private static void populateProductArray(ArrayList<OTProductTO> productList) {

    OTProductTO prod1 = new OTProductTO();

    prod1.setItem(new BigInteger(OTTestUtil.ITEMONE));
    prod1.setItemType(OTTestUtil.ITEMTYPE);
    prod1.setItemNumCode(new BigInteger(OTTestUtil.ITEMNUMCODE));
    prod1.setQuantity(new BigInteger(OTTestUtil.QUANTITY));
    prod1.setPrice(new BigDecimal(OTTestUtil.PRICE));

    productList.add(prod1);

    OTProductTO prod2 = new OTProductTO();

    prod2.setItem(new BigInteger(OTTestUtil.ITEMTWO));
    prod2.setItemType(OTTestUtil.ITEMTYPE);
    prod2.setItemNumCode(new BigInteger(OTTestUtil.ITEMNUMCODE));
    prod2.setQuantity(new BigInteger(OTTestUtil.QUANTITY));
    prod2.setPrice(new BigDecimal(OTTestUtil.PRICE));

    productList.add(prod2);

    return;
  }

}
