package pvt.disney.dti.gateway.provider.wdw.xml;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCreateTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTProductTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVoucherTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * Junit test for the Omni Ticket Create Transaction XML translations.
 * 
 * @author lewit019
 * 
 */
public class OTCreateTransactionXMLTestCase {

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
    String fileName = new String("./config/CreateTicket/CreateTicket_01_Req_WDW.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    OTCommandTO otCommandTO = createTransactionRequest();

    String newXML = null;
    try {
      newXML = OTCommandXML.getXML(otCommandTO);
    } catch (DTIException dtie) {
      fail("DTI Exception getting string from transfer object: " + dtie.toString());
    }

    // Validate Reservation Response
    String resRespBaseline = DTITestUtilities.findTag(baselineXML, "CreateTransaction");
    String resRespNew = DTITestUtilities.findTag(newXML, "CreateTransaction");

    try {
      DTITestUtilities.compareXML(resRespBaseline, resRespNew, "CreateTransaction");
    } catch (Exception e) {
      fail("Exception validating CreateTransaction section: " + e.toString());
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
    String fileName = new String("./config/CreateTicket/CreateTicket_01_Rsp_WDW.xml");

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
      validateCreateTxnRspTO(otCommandTO);
    } catch (Exception e) {
      fail("Failure in validating create ticket response: " + e.toString());
    }

    return;
  }

  /**
   * Creates the Omni Ticket request object for this type of transaction.
   * 
   * @return the Omni Ticket Command Transfer Object.
   */
  private static OTCommandTO createTransactionRequest() {

    OTCommandTO otCmdTO = new OTCommandTO(OTCommandTO.OTTransactionType.CREATETRANSACTION);

    // Header
    OTHeaderTO otHdrTO = OTTestUtil.createOTReqHeader(OTTestUtil.REQTYPE_CREATE,
        OTTestUtil.REQSUBTYPE_CREATETXN);

    otCmdTO.setHeaderTO(otHdrTO);

    OTCreateTransactionTO otCrtTxnTO = new OTCreateTransactionTO();

    // Work Rules
    ArrayList<String> workRules = otCrtTxnTO.getTagsList();
    workRules.add(OTTestUtil.TAGS_TICKETINFO);
    workRules.add(OTTestUtil.TAGS_TRANSACTIONCOD);
    workRules.add(OTTestUtil.TAGS_TRANSACTIONDSSN);

    // Site Number
    otCrtTxnTO.setSiteNumber(Integer.decode(OTTestUtil.SITENUMBER));

    // Product
    OTProductTO aProduct = new OTProductTO();
    aProduct.setItem(new BigInteger(OTTestUtil.ITEMONE));
    aProduct.setTicketType(new BigInteger(OTTestUtil.TICKETTYPE));
    aProduct.setQuantity(new BigInteger(OTTestUtil.QUANTITY));
    aProduct.setTicketNote(OTTestUtil.TICKETNOTE);
    aProduct.setTicketAttribute(new BigInteger(OTTestUtil.TICKETATTRIBUTE));
    otCrtTxnTO.getProductList().add(aProduct);

    // PaymentInfo
    OTPaymentTO aPayment = new OTPaymentTO();
    aPayment.setPayItem(new BigInteger(OTTestUtil.ITEMONE));
    aPayment.setPayType(OTPaymentTO.PaymentType.VOUCHER);
    OTVoucherTO aVoucher = new OTVoucherTO();
    aVoucher.setMasterCode(OTTestUtil.MASTERCODE);
    aPayment.setVoucher(aVoucher);
    otCrtTxnTO.getPaymentInfoList().add(aPayment);

    // InTransactionAttribute (1)
    OTInTransactionAttributeTO inTxnAttr = new OTInTransactionAttributeTO();
    inTxnAttr.setAttributeCmd(OTTestUtil.ATTRIBUTECMD);
    inTxnAttr.setAttributeKey(new Integer(OTTestUtil.ATTRIBUTEKEY));
    inTxnAttr.setAttributeValue(OTTestUtil.ATTRIBUTEVALUE);
    inTxnAttr.setAttributeType(new Integer(OTTestUtil.ATTRIBUTETYPE));
    inTxnAttr.setAttributeFlag(OTTestUtil.ATTRIBUTEFLAG);
    otCrtTxnTO.getInTxnAttrList().add(inTxnAttr);

    otCmdTO.setCreateTransactionTO(otCrtTxnTO);

    return otCmdTO;
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
  private void validateCreateTxnRspTO(OTCommandTO otCommandTO) throws Exception {

    // Header
    OTHeaderTO otHdr = otCommandTO.getHeaderTO();
    if (otHdr == null)
      throw new Exception("OTCommandTO object had no OTHeaderTO object.");
    OTTestUtil.validateOTRspHeader(otHdr, OTTestUtil.REQTYPE_CREATE,
        OTTestUtil.REQSUBTYPE_CREATETXN);

    // Create Transaction
    OTCreateTransactionTO otCreatTxn = otCommandTO.getCreateTransactionTO();
    if (otCreatTxn == null)
      throw new Exception("OTCommandTO object had no OTCreateTransactionTO object.");

    // Error
    OTErrorTO otErr = otCreatTxn.getError();
    if (otErr == null)
      throw new Exception("OTCreateTransactionTO object had no OTErrorTO object.");
    OTTestUtil.validateOTRspError(otErr);

    // TicketInfo <c>
    validateTicketInfoTO(otCreatTxn.getTktInfoList());

    // TransactionDSSN <c>
    OTTestUtil.validateTranDSSN(otCreatTxn.getTransactionDSSN());

    // TransactionCOD
    OTTestUtil.validateValue("OTCreateTransactionTO", "TransactionCOD", OTTestUtil.TRANCOD,
        otCreatTxn.getTransactionCOD());

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
  private static void validateTicketInfoTO(ArrayList<OTTicketInfoTO> tktInfoList) throws Exception {

    if ((tktInfoList == null) || (tktInfoList.size() != 2)) {
      throw new Exception(
          "OTCreateTransactionTO object does not have a valid two TicketInfo clauses as expected.");
    }

    int itemNum = 0;

    for /* each */(OTTicketInfoTO aTicketInfo : /* in */tktInfoList) {

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

      // TicketType
      OTTestUtil.validateValue("OTTicketInfoTO", "TicketType", OTTestUtil.TICKETTYPE, aTicketInfo
          .getTicketType());

      // Price
      OTTestUtil.validateValue("OTTicketInfoTO", "Price", OTTestUtil.PRICE, aTicketInfo.getPrice());

      // Tax
      OTTestUtil.validateValue("OTTicketInfoTO", "Tax", OTTestUtil.TAX, aTicketInfo.getTax());

      // Ticket <c>
      OTTestUtil.validateTicket(aTicketInfo.getTicket());

      // Validity StartDate
      OTTestUtil.validateValue("OTTicketInfoTO", "Validity.StartDate", OTTestUtil.STARTDATE,
          aTicketInfo.getValidityStartDate());

      // Validity EndDate
      OTTestUtil.validateValue("OTTicketInfoTO", "Validity.EndDate", OTTestUtil.ENDDATE,
          aTicketInfo.getValidityEndDate());

      // TicketNote
      OTTestUtil.validateValue("OTTicketInfoTO", "TicketNote", OTTestUtil.TICKETNOTE, aTicketInfo
          .getTicketNote());

    }

    return;
  }

}
