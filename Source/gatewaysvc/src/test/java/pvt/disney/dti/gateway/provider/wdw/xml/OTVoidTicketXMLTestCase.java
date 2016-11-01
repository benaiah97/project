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
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTVoidTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVoucherTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * Junit test for the Omni Ticket Void Ticket XML transformations.
 * 
 * @author lewit019
 * 
 */
public class OTVoidTicketXMLTestCase {

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
    String fileName = new String("./config/VoidTicket/VoidTicket_01_Req_WDW.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    OTCommandTO otCommandTO = createVoidTicketRequest();

    String newXML = null;
    try {
      newXML = OTCommandXML.getXML(otCommandTO);
    } catch (DTIException dtie) {
      fail("DTI Exception getting string from transfer object: " + dtie.toString());
    }

    // Validate Reservation Response
    String resRespBaseline = DTITestUtilities.findTag(baselineXML, "VoidTicket");
    String resRespNew = DTITestUtilities.findTag(newXML, "VoidTicket");

    try {
    	DTITestUtilities.compareXML(resRespBaseline, resRespNew, "VoidTicket");
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
    String fileName = new String("./config/VoidTicket/VoidTicket_01_Rsp_WDW.xml");

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
      validateVoidTicketRspTO(otCommandTO);
    } catch (Exception e) {
      fail("Failure in validating void ticket response body: " + e.toString());
    }

    return;
  }

  /**
   * Creates the Omni Ticket request object for this type of transaction.
   * 
   * @return the Omni Ticket Command Transfer Object.
   */
  private static OTCommandTO createVoidTicketRequest() {

    OTCommandTO otCmdTO = new OTCommandTO(OTCommandTO.OTTransactionType.VOIDTICKET);

    // Header
    OTHeaderTO otHdrTO = OTTestUtil.createOTReqHeader(OTTestUtil.REQTYPE_VOID,
        OTTestUtil.REQSUBTYPE_VOIDTICKET);

    otCmdTO.setHeaderTO(otHdrTO);

    OTVoidTicketTO otVoidTktTO = new OTVoidTicketTO();

    // Work Rules
    ArrayList<String> workRules = otVoidTktTO.getTagsList();
    workRules.add(OTTestUtil.TAGS_TICKETINFOVOID);
    workRules.add(OTTestUtil.TAGS_TRANSACTIONDSSN);
    workRules.add(OTTestUtil.TAGS_TRANSACTIONCOD);
    workRules.add(OTTestUtil.TAGS_PAYMENTINFO);

    // Site Number
    otVoidTktTO.setSiteNumber(Integer.decode(OTTestUtil.SITENUMBER));

    // VTicket
    OTVTicketTO voidTkt = new OTVTicketTO();
    voidTkt.setItem(new BigInteger(OTTestUtil.ITEMONE));
    OTTicketTO aTicket = new OTTicketTO();
    aTicket.setMagTrack(OTTestUtil.MAGCODE);
    voidTkt.setTicketSearchMode(aTicket);
    otVoidTktTO.getVTicketList().add(voidTkt);

    // PaymentInfo
    OTPaymentTO aPayment = new OTPaymentTO();
    aPayment.setPayItem(new BigInteger(OTTestUtil.ITEMONE));
    aPayment.setPayType(OTPaymentTO.PaymentType.VOUCHER);
    OTVoucherTO aVoucher = new OTVoucherTO();
    aVoucher.setMasterCode(OTTestUtil.MASTERCODE);
    aPayment.setVoucher(aVoucher);
    otVoidTktTO.getPaymentInfoList().add(aPayment);

    // TransactionNote
    otVoidTktTO.setTransactionNote(OTTestUtil.TRANNOTE);

    // InTransactionAttribute (1)
    OTInTransactionAttributeTO inTxnAttr = new OTInTransactionAttributeTO();
    inTxnAttr.setAttributeCmd(OTTestUtil.ATTRIBUTECMD);
    inTxnAttr.setAttributeKey(new Integer(OTTestUtil.ATTRIBUTEKEY));
    inTxnAttr.setAttributeValue(OTTestUtil.ATTRIBUTEVALUE);
    inTxnAttr.setAttributeType(new Integer(OTTestUtil.ATTRIBUTETYPE));
    inTxnAttr.setAttributeFlag(OTTestUtil.ATTRIBUTEFLAG);
    otVoidTktTO.getInTxnAttrList().add(inTxnAttr);

    // InTransactionAttribute (2)
    inTxnAttr = new OTInTransactionAttributeTO();
    inTxnAttr.setAttributeCmd(OTTestUtil.ATTRIBUTECMD);
    inTxnAttr.setAttributeKey(new Integer(OTTestUtil.ATTRIBUTEKEY));
    inTxnAttr.setAttributeValue(OTTestUtil.ATTRIBUTEVALUE);
    inTxnAttr.setAttributeType(new Integer(OTTestUtil.ATTRIBUTETYPE));
    inTxnAttr.setAttributeFlag(OTTestUtil.ATTRIBUTEFLAG);
    otVoidTktTO.getInTxnAttrList().add(inTxnAttr);

    otCmdTO.setVoidTicketTO(otVoidTktTO);

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
  private void validateVoidTicketRspTO(OTCommandTO otCommandTO) throws Exception {

    // Header
    OTHeaderTO otHdr = otCommandTO.getHeaderTO();
    if (otHdr == null)
      throw new Exception("OTCommandTO object had no OTHeaderTO object.");
    OTTestUtil
        .validateOTRspHeader(otHdr, OTTestUtil.REQTYPE_VOID, OTTestUtil.REQSUBTYPE_VOIDTICKET);

    // Void Ticket
    OTVoidTicketTO otVoidTkt = otCommandTO.getVoidTicketTO();
    if (otVoidTkt == null)
      throw new Exception("OTCommandTO object had no OTVoidTicketTO object.");

    // Error
    OTErrorTO otErr = otVoidTkt.getError();
    if (otErr == null)
      throw new Exception("OTVoidTicketTO object had no OTErrorTO object.");
    OTTestUtil.validateOTRspError(otErr);

    // TicketInfoVoid <c>
    validateTicketInfoVoidTO(otVoidTkt.getVTicketList());

    // PaymentInfo (ignored)
    // ArrayList<OTPaymentTO> paymentList = otVoidTkt.getPaymentInfoList();
    // OTTestUtil.validatePaymentList(paymentList);

    // TransactionDSSN <c>
    OTTestUtil.validateTranDSSN(otVoidTkt.getTransactionDSSN());

    // TransactionCOD
    OTTestUtil.validateValue("OTVoidTicketTO", "TransactionCOD", OTTestUtil.TRANCOD, otVoidTkt
        .getTransactionCOD());

    return;
  }

  /**
   * Validates the ticket info section of the response.
   * 
   * @param vTktList
   *          the list of Omni Ticket Void Ticket Info Transfer Objects.
   * @throws Exception
   *           for any validation error
   */
  private static void validateTicketInfoVoidTO(ArrayList<OTVTicketTO> vTktList) throws Exception {

    if ((vTktList == null) || (vTktList.size() != 1)) {
      throw new Exception(
          "OTVoidTicketTO object does not have a valid (single) TicketInfoVoid as expected.");
    }

    OTVTicketTO aVoidTkt = vTktList.get(0);

    // Item
    OTTestUtil.validateValue("OTVTicketTO", "Item", OTTestUtil.ITEMONE, aVoidTkt.getItem());

    // ItemStatus
    OTTestUtil.validateValue("OTVTicketTO", "ItemStatus", OTTestUtil.ITEMSTATUS, aVoidTkt
        .getItemStatus());

    // TicketType (ignored)
    // Price (ignored)
    // Tax (ignored)
    // RemainingValue (ignored)
    // VoidCode (ignored)

    // Ticket <c>
    OTTestUtil.validateTicket(aVoidTkt.getTicket());

    // LastUsageDate (ignored)
    // TaxExempt (ignored)

    return;
  }

}
