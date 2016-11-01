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
import pvt.disney.dti.gateway.provider.wdw.data.OTUpgradeTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUpgradeTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTVoucherTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * Junit test for the Omni Ticket Upgrade Ticket XML transformations.
 * 
 * @author lewit019
 * 
 */
public class OTUpgradeTicketXMLTestCase {

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
    String fileName = new String("./config/UpgradeAlpha/UpgradeAlpha_01_Req_WDW.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    OTCommandTO otCommandTO = createUpgradeTicketRequest();

    String newXML = null;
    try {
      newXML = OTCommandXML.getXML(otCommandTO);
    } catch (DTIException dtie) {
      fail("DTI Exception getting string from transfer object: " + dtie.toString());
    }

    // Validate Reservation Response
    String resRespBaseline = DTITestUtilities.findTag(baselineXML, "UpgradeTicket");
    String resRespNew = DTITestUtilities.findTag(newXML, "UpgradeTicket");

    try {
    	DTITestUtilities.compareXML(resRespBaseline, resRespNew, "UpgradeTicket");
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
    String fileName = new String("./config/UpgradeAlpha/UpgradeAlpha_01_Rsp_WDW.xml");

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
      validateUpgradeTicketRspTO(otCommandTO);
    } catch (Exception e) {
      fail("Failure in validating upgrade ticket response body: " + e.toString());
    }

    return;
  }

  /**
   * Creates the Omni Ticket request object for this type of transaction.
   * 
   * @return the Omni Ticket Command Transfer Object.
   */
  private static OTCommandTO createUpgradeTicketRequest() {

    OTCommandTO otCmdTO = new OTCommandTO(OTCommandTO.OTTransactionType.UPGRADETICKET);

    // Header
    OTHeaderTO otHdrTO = OTTestUtil.createOTReqHeader(OTTestUtil.REQTYPE_UPGRADE,
        OTTestUtil.REQSUBTYPE_UPGRADETICKET);

    otCmdTO.setHeaderTO(otHdrTO);

    OTUpgradeTicketTO otUpgrdTktTO = new OTUpgradeTicketTO();

    // Work Rules
    ArrayList<String> workRules = otUpgrdTktTO.getTagsList();
    workRules.add(OTTestUtil.TAGS_UPGRADETICKETINFO);
    workRules.add(OTTestUtil.TAGS_TRANSACTIONDSSN);
    workRules.add(OTTestUtil.TAGS_TRANSACTIONCOD);
    workRules.add(OTTestUtil.TAGS_PAYMENTINFO);

    // Site Number
    otUpgrdTktTO.setSiteNumber(Integer.decode(OTTestUtil.SITENUMBER));

    // UpgradeTicketInfo (1)
    OTUpgradeTicketInfoTO upgrdTktInfo = new OTUpgradeTicketInfoTO();
    upgrdTktInfo.setItem(new BigInteger(OTTestUtil.ITEMONE));
    OTTicketTO aTicket = new OTTicketTO();
    aTicket.setMagTrack(OTTestUtil.MAGCODE);
    upgrdTktInfo.setTicketSearchMode(aTicket);
    upgrdTktInfo.setTicketType(new BigInteger(OTTestUtil.TICKETTYPE));
    otUpgrdTktTO.getUpgradeTicketInfoList().add(upgrdTktInfo);

    // UpgradeTicketInfo (2)
    upgrdTktInfo = new OTUpgradeTicketInfoTO();
    upgrdTktInfo.setItem(new BigInteger(OTTestUtil.ITEMTWO));
    aTicket = new OTTicketTO();
    aTicket.setMagTrack(OTTestUtil.MAGCODE);
    upgrdTktInfo.setTicketSearchMode(aTicket);
    upgrdTktInfo.setTicketType(new BigInteger(OTTestUtil.TICKETTYPE));
    otUpgrdTktTO.getUpgradeTicketInfoList().add(upgrdTktInfo);

    // PaymentInfo
    OTPaymentTO aPayment = new OTPaymentTO();
    aPayment.setPayItem(new BigInteger(OTTestUtil.ITEMONE));
    aPayment.setPayType(OTPaymentTO.PaymentType.VOUCHER);
    OTVoucherTO aVoucher = new OTVoucherTO();
    aVoucher.setMasterCode(OTTestUtil.MASTERCODE);
    aPayment.setVoucher(aVoucher);
    otUpgrdTktTO.getPaymentInfoList().add(aPayment);

    // TransactionNote
    otUpgrdTktTO.setTransactionNote(OTTestUtil.TRANNOTE);

    // InTransactionAttribute (1)
    OTInTransactionAttributeTO inTxnAttr = new OTInTransactionAttributeTO();
    inTxnAttr.setAttributeCmd(OTTestUtil.ATTRIBUTECMD);
    inTxnAttr.setAttributeKey(new Integer(OTTestUtil.ATTRIBUTEKEY));
    inTxnAttr.setAttributeValue(OTTestUtil.ATTRIBUTEVALUE);
    inTxnAttr.setAttributeType(new Integer(OTTestUtil.ATTRIBUTETYPE));
    inTxnAttr.setAttributeFlag(OTTestUtil.ATTRIBUTEFLAG);
    otUpgrdTktTO.getInTxnAttrList().add(inTxnAttr);

    // InTransactionAttribute (2)
    inTxnAttr = new OTInTransactionAttributeTO();
    inTxnAttr.setAttributeCmd(OTTestUtil.ATTRIBUTECMD);
    inTxnAttr.setAttributeKey(new Integer(OTTestUtil.ATTRIBUTEKEY));
    inTxnAttr.setAttributeValue(OTTestUtil.ATTRIBUTEVALUE);
    inTxnAttr.setAttributeType(new Integer(OTTestUtil.ATTRIBUTETYPE));
    inTxnAttr.setAttributeFlag(OTTestUtil.ATTRIBUTEFLAG);
    otUpgrdTktTO.getInTxnAttrList().add(inTxnAttr);

    // DenyUpgradeOnUpgrade
    otUpgrdTktTO.setDenyUpgradeOnUpgrade(new Boolean(true));

    otCmdTO.setUpgradeTicketTO(otUpgrdTktTO);

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
  private void validateUpgradeTicketRspTO(OTCommandTO otCommandTO) throws Exception {

    // Header
    OTHeaderTO otHdr = otCommandTO.getHeaderTO();
    if (otHdr == null)
      throw new Exception("OTCommandTO object had no OTHeaderTO object.");
    OTTestUtil.validateOTRspHeader(otHdr, OTTestUtil.REQTYPE_UPGRADE,
        OTTestUtil.REQSUBTYPE_UPGRADETICKET);

    // Upgrade Ticket
    OTUpgradeTicketTO otUpgrdTkt = otCommandTO.getUpgradeTicketTO();
    if (otUpgrdTkt == null)
      throw new Exception("OTCommandTO object had no OTUpgradeTicketTO object.");

    // Error
    OTErrorTO otErr = otUpgrdTkt.getError();
    if (otErr == null)
      throw new Exception("OTUpgradeTicketTO object had no OTErrorTO object.");
    OTTestUtil.validateOTRspError(otErr);

    // UpgradeTicketInfo <c>
    validateUpgradeTicketInfo(otUpgrdTkt.getUpgradeTicketInfoList());

    // PaymentInfo
    ArrayList<OTPaymentTO> paymentList = otUpgrdTkt.getPaymentInfoList();
    OTTestUtil.validatePaymentList(paymentList);

    // TransactionDSSN <c>
    OTTestUtil.validateTranDSSN(otUpgrdTkt.getTransactionDSSN());

    // TransactionCOD
    OTTestUtil.validateValue("OTUpgradeTicketTO", "TransactionCOD", OTTestUtil.TRANCOD, otUpgrdTkt
        .getTransactionCOD());

    return;
  }

  /**
   * Validates the ticket info section of the response.
   * 
   * @param tktInfoList
   *          the list of Omni Ticket Upgrade Ticket Info Transfer Objects.
   * @throws Exception
   *           for any validation error
   */
  private static void validateUpgradeTicketInfo(ArrayList<OTUpgradeTicketInfoTO> tktInfoList)
      throws Exception {

    // UpgradeTicketInfo
    if ((tktInfoList == null) || (tktInfoList.size() == 0)) {
      throw new Exception("OTUpgradeTicketTO object has empty or null UpgradeTicketInfo list.");
    }

    String parentNode = "OTUpgradeTicketInfoTO";

    int itemNum = 0;

    for /* each */(OTUpgradeTicketInfoTO aTktInfo : /* in */tktInfoList) {

      // Item
      BigInteger item = aTktInfo.getItem();
      if (item == null)
        throw new Exception("OTPaymentTO object had no Item");
      BigInteger validItem = BigInteger.valueOf(++itemNum);
      if (validItem.compareTo(item) != 0)
        throw new Exception("OTPaymentTO object had invalid Item");

      // ItemStatus
      OTTestUtil.validateValue(parentNode, "ItemStatus", OTTestUtil.ITEMSTATUS, aTktInfo
          .getItemStatus());

      // TicketType (ignored)

      // RemainingValue (ignored)

      // Price
      OTTestUtil.validateValue(parentNode, "Price", OTTestUtil.PRICE, aTktInfo.getPrice());

      // VoidCode (ignored)

      // Tax
      OTTestUtil.validateValue(parentNode, "Tax", OTTestUtil.TAX, aTktInfo.getTax());

      // Ticket
      OTTestUtil.validateTicket(aTktInfo.getTicket());

      // Validity StartDate, EndDate (ignored)
      // TicketFlag <c> (ignored)
      // TicketAttribute (ignored)
      // TicketNote (ignored)
      // BiometricLevel (ignored)
      // Layout (ignored)

    }

    return;
  }

}
