package pvt.disney.dti.gateway.provider.wdw.xml;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUsagesTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * Junit tests the Omni Ticket Query Ticket XML translations.
 * 
 * @author lewit019
 * 
 */
public class OTQueryTicketXMLTestCase {

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
    String fileName = new String("./config/QueryTicket/QueryTicket_01_Req_WDW.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    OTCommandTO otCommandTO = createQueryTicketRequest();

    String newXML = null;
    try {
      newXML = OTCommandXML.getXML(otCommandTO);
    } catch (DTIException dtie) {
      fail("DTI Exception getting string from transfer object: " + dtie.toString());
    }

    // Validate Reservation Response
    String resRespBaseline = DTITestUtilities.findTag(baselineXML, "QueryTicket");
    String resRespNew = DTITestUtilities.findTag(newXML, "QueryTicket");

    try {
      DTITestUtilities.compareXML(resRespBaseline, resRespNew, "QueryTicket");
    } catch (Exception e) {
      fail("Exception validating QueryTicket section: " + e.toString());
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
    String fileName = new String("./config/QueryTicket/QueryTicket_01_Rsp_WDW.xml");

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
      validateQueryTicketRspTO(otCommandTO);
    } catch (Exception e) {
      fail("Failure in validating query ticket response body: " + e.toString());
    }

    return;
  }

  /**
   * Creates the Omni Ticket request object for this type of transaction.
   * 
   * @return the Omni Ticket Command Transfer Object.
   */
  private static OTCommandTO createQueryTicketRequest() {

    OTCommandTO otCmdTO = new OTCommandTO(OTCommandTO.OTTransactionType.QUERYTICKET);

    // Header
    OTHeaderTO otHdrTO = OTTestUtil.createOTReqHeader(OTTestUtil.REQTYPE_QUERY,
        OTTestUtil.REQSUBTYPE_QUERYTICKET);

    otCmdTO.setHeaderTO(otHdrTO);

    OTQueryTicketTO otqryTktTO = new OTQueryTicketTO();

    // Work Rules
    ArrayList<String> workRules = otqryTktTO.getTagsList();
    workRules.add(OTTestUtil.TAGS_ALL);

    // Site Number
    otqryTktTO.setSiteNumber(Integer.decode(OTTestUtil.SITENUMBER));

    // TicketInfo
    OTTicketInfoTO otTktInfo = new OTTicketInfoTO();

    // Item
    otTktInfo.setItem(new BigInteger(OTTestUtil.ITEMONE));

    // Ticket
    OTTicketTO aTicket = new OTTicketTO();
    try {
      aTicket.setTDssn(OTTestUtil.TDSSNDATE_CCYY, OTTestUtil.TDSSNSITE, OTTestUtil.TDSSNSTATION,
          OTTestUtil.TDSSNTICKETID);
    } catch (ParseException pe) {

    }
    otTktInfo.setTicketSearchMode(aTicket);
    otqryTktTO.getTicketInfoList().add(otTktInfo);
    otCmdTO.setQueryTicketTO(otqryTktTO);

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
  private void validateQueryTicketRspTO(OTCommandTO otCommandTO) throws Exception {

    // Header
    OTHeaderTO otHdr = otCommandTO.getHeaderTO();
    if (otHdr == null)
      throw new Exception("OTCommandTO object had no OTHeaderTO object.");
    OTTestUtil.validateOTRspHeader(otHdr, OTTestUtil.REQTYPE_QUERY,
        OTTestUtil.REQSUBTYPE_QUERYTICKET);

    // Query Ticket
    OTQueryTicketTO otQryTkt = otCommandTO.getQueryTicketTO();
    if (otQryTkt == null)
      throw new Exception("OTCommandTO object had no OTQueryTicketTO object.");

    // Error
    OTErrorTO otErr = otQryTkt.getError();
    if (otErr == null)
      throw new Exception("OTQueryTicketTO object had no OTErrorTO object.");
    OTTestUtil.validateOTRspError(otErr);

    // TicketInfo
    ArrayList<OTTicketInfoTO> ticketInfoList = otQryTkt.getTicketInfoList();
    if ((ticketInfoList == null) || (ticketInfoList.size() == 0))
      throw new Exception("OTQueryTicketTO object had no or empty TicketInfo list.");
    validateTicketInfoList(ticketInfoList);

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

    OTTicketInfoTO tktInfTO = ticketInfoList.get(0);

    // Item
    OTTestUtil.validateValue("OTTicketInfoTO", "Item", OTTestUtil.ITEMONE, tktInfTO.getItem());

    // ItemStatus
    OTTestUtil.validateValue("OTTicketInfoTO", "ItemStatus", OTTestUtil.ITEMSTATUS, tktInfTO
        .getItemStatus());

    // TicketType
    OTTestUtil.validateValue("OTTicketInfoTO", "TicketType", OTTestUtil.TICKETTYPE, tktInfTO
        .getTicketType());

    // Price
    OTTestUtil.validateValue("OTTicketInfoTO", "Price", OTTestUtil.PRICE, tktInfTO.getPrice());

    // Tax
    OTTestUtil.validateValue("OTTicketInfoTO", "Tax", OTTestUtil.TAX, tktInfTO.getTax());

    // Ticket <c>
    OTTestUtil.validateTicket(tktInfTO.getTicket());

    // RemainingValue (ignored)

    // VoidCode
    OTTestUtil.validateValue("OTTicketInfoTO", "VoidCode", OTTestUtil.VOIDCODE, tktInfTO
        .getVoidCode());

    // Validity StartDate, EndDate
    OTTestUtil.validateValue("OTTicketInfoTO", "StartDate", OTTestUtil.STARTDATE, tktInfTO
        .getValidityStartDate());
    OTTestUtil.validateValue("OTTicketInfoTO", "EndDate", OTTestUtil.ENDDATE, tktInfTO
        .getValidityEndDate());

    // TicketFlag <c> (ignored)
    // Usages <c> (ignored)

    // TicketAttribute
    OTTestUtil.validateValue("OTTicketInfoTO", "TicketAttribute", OTTestUtil.TICKETATTRIBUTE,
        tktInfTO.getTicketAttribute());

    // TicketNote (ignored)
    // BiometricLevel (ignored)
    // TransactionDSSN <c> (ignored)
    // TransactionCOD (ignored)
    // GroupQuantity (ignored)
    // TaxExempt (ignored)

    // AlreadyUsed
    OTTestUtil.validateValue("OTTicketInfoTO", "AlreadyUsed", OTTestUtil.ALREADYUSED, tktInfTO
        .getAlreadyused());

    return;
  }

  /**
   * Not used at present for any testing.
   * 
   * @param usagesList
   * @throws Exception
   */
  public static void validateUsagesList(ArrayList<OTUsagesTO> usagesList) throws Exception {

    // Usages
    if ((usagesList == null) || (usagesList.size() != 2)) {
      throw new Exception("Object OTTicketInfoTO had a null or invalid size Usages list.");
    }

    // Usages
    int itemValue = 0;
    for /* each */(OTUsagesTO aUsage : /* in */usagesList) {

      // Item
      OTTestUtil.validateValue("OTUsagesTO", "Item", Integer.toString(++itemValue), aUsage
          .getItem());

      // UsageType
      OTTestUtil.validateValue("OTUsagesTO", "UsageType", OTTestUtil.USAGETYPE, aUsage
          .getUsageType());

      // SiteNumber
      OTTestUtil.validateValue("OTUsagesTO", "SiteNumber", OTTestUtil.SITENUMBER, aUsage
          .getSiteNumber());

      // Gate
      OTTestUtil.validateValue("OTUsagesTO", "Gate", OTTestUtil.GATE, aUsage.getGate());

      // Date
      OTTestUtil.validateValue("OTUsagesTO", "Date", OTTestUtil.DATE, aUsage.getDate());

      // Time
      OTTestUtil.validateValue("OTUsagesTO", "Time", OTTestUtil.TIME, aUsage.getTime());

    }

    return;

  }

}
