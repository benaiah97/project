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
import pvt.disney.dti.gateway.provider.wdw.data.OTUpdateTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * Junit test for the Omni Ticket Update Ticket XML translations.
 * 
 * @author lewit019
 * 
 */
public class OTUpdateTicketXMLTestCase {

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
    String fileName = new String("./config/UpdateTicket/UpdateTicket_01_Req_WDW.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    OTCommandTO otCommandTO = createUpdateTicketRequest();

    String newXML = null;
    try {
      newXML = OTCommandXML.getXML(otCommandTO);
    } catch (DTIException dtie) {
      fail("DTI Exception getting string from transfer object: " + dtie.toString());
    }

    // Validate Reservation Response
    String resRespBaseline = DTITestUtilities.findTag(baselineXML, "UpdateTicket");
    String resRespNew = DTITestUtilities.findTag(newXML, "UpdateTicket");

    try {
    	DTITestUtilities.compareXML(resRespBaseline, resRespNew, "UpdateTicket");
    } catch (Exception e) {
      fail("Exception validating UpdateTicket section: " + e.toString());
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
    String fileName = new String("./config/UpdateTicket/UpdateTicket_01_Rsp_WDW.xml");

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
      validateUpdateTicketRspTO(otCommandTO);
    } catch (Exception e) {
      fail("Failure in validating update ticket command body: " + e.toString());
    }

    return;
  }

  /**
   * Creates the Omni Ticket request object for this type of transaction.
   * 
   * @return the Omni Ticket Command Transfer Object.
   */
  private static OTCommandTO createUpdateTicketRequest() {

    OTCommandTO otCmdTO = new OTCommandTO(OTCommandTO.OTTransactionType.UPDATETICKET);

    // Header
    OTHeaderTO otHdrTO = OTTestUtil.createOTReqHeader(OTTestUtil.REQTYPE_UPDATE,
        OTTestUtil.REQSUBTYPE_UPDATETICKET);

    otCmdTO.setHeaderTO(otHdrTO);

    OTUpdateTicketTO otupdtTktTO = new OTUpdateTicketTO();

    // Work Rules
    ArrayList<String> workRules = otupdtTktTO.getTagsList();
    workRules.add(OTTestUtil.TAGS_UPDATETICKETINFO);

    // Site Number
    otupdtTktTO.setSiteNumber(Integer.decode(OTTestUtil.SITENUMBER));

    // TicketInfo
    OTTicketInfoTO otTktInfo = new OTTicketInfoTO();

    // Item
    otTktInfo.setItem(new BigInteger(OTTestUtil.ITEMONE));

    // Ticket
    OTTicketTO aTicket = new OTTicketTO();
    aTicket.setTCOD(OTTestUtil.TCOD);

    otTktInfo.setTicketSearchMode(aTicket);
    otupdtTktTO.getTktInfoList().add(otTktInfo);

    otCmdTO.setUpdateTicketTO(otupdtTktTO);

    // Validity
    try {
      otTktInfo.setValidityStartDate(OTTestUtil.STARTDATE);
      otTktInfo.setValidityEndDate(OTTestUtil.ENDDATE);
    } catch (ParseException pe) {

    }

    // TicketNote
    otTktInfo.setTicketNote(OTTestUtil.TICKETNOTE);

    // VoidCode
    otTktInfo.setVoidCode(Integer.decode(OTTestUtil.VOIDCODE));

    // ZipCode
    otTktInfo.setZipCode(OTTestUtil.ZIPCODE);

    // BiometricLevel
    otTktInfo.setBiometricLevel(Integer.decode(OTTestUtil.BIOMETRICLEVEL));

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
  private void validateUpdateTicketRspTO(OTCommandTO otCommandTO) throws Exception {

    // Header
    OTHeaderTO otHdr = otCommandTO.getHeaderTO();
    if (otHdr == null)
      throw new Exception("OTCommandTO object had no OTHeaderTO object.");
    OTTestUtil.validateOTRspHeader(otHdr, OTTestUtil.REQTYPE_UPDATE,
        OTTestUtil.REQSUBTYPE_UPDATETICKET);

    // Update Ticket
    OTUpdateTicketTO otUpdateTkt = otCommandTO.getUpdateTicketTO();
    if (otUpdateTkt == null)
      throw new Exception("OTCommandTO object had no OTUpdateTicketTO object.");

    // Error
    OTErrorTO otErr = otUpdateTkt.getError();
    if (otErr == null)
      throw new Exception("OTUpdateTicketTO object had no OTErrorTO object.");
    OTTestUtil.validateOTRspError(otErr);

    return;
  }

}
