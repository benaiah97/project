package pvt.disney.dti.gateway.provider.wdw.xml;

import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTHeaderTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpdateTransactionTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTErrorTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTInTransactionAttributeTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * Junit test for the Omni Ticket Update Transaction XML transformations.
 * 
 * @author lewit019
 * 
 */
public class OTUpdateTransactionXMLTestCase {

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
  public final void testGetXMLDSSN() {

    // ***** Read the test file. *****
    String baselineXML = null;
    String fileName = new String("./config/UpdateTransaction/UpdateTransaction_01_Req_WDW.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    OTCommandTO otCommandTO = createUpdateTransactionRequest();

    String newXML = null;
    try {
      newXML = OTCommandXML.getXML(otCommandTO);
    } catch (DTIException dtie) {
      fail("DTI Exception getting string from transfer object: " + dtie.toString());
    }

    // Validate Reservation Response
    String resRespBaseline = DTITestUtilities.findTag(baselineXML, "UpdateTransaction");
    String resRespNew = DTITestUtilities.findTag(newXML, "UpdateTransaction");

    try {
    	DTITestUtilities.compareXML(resRespBaseline, resRespNew, "UpdateTransaction");
    } catch (Exception e) {
      fail("Exception validating UpdateTransaction section: " + e.toString());
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
    String fileName = new String("./config/UpdateTransaction/UpdateTransaction_01_Rsp_WDW.xml");

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
      validateUpdateTransactionRspTO(otCommandTO);
    } catch (Exception e) {
      fail("Failure in validating UpdateTransaction body: " + e.toString());
    }

    return;
  }

  /**
   * Creates the Omni Ticket request object for this type of transaction.
   * 
   * @return the Omni Ticket Command Transfer Object.
   */
  private static OTCommandTO createUpdateTransactionRequest() {

    OTCommandTO otCmdTO = new OTCommandTO(OTCommandTO.OTTransactionType.UPDATETRANSACTION);

    // Header
    OTHeaderTO otHdrTO = OTTestUtil.createOTReqHeader(OTTestUtil.REQTYPE_UPDATE,
        OTTestUtil.REQSUBTYPE_UPDATETXN);

    otCmdTO.setHeaderTO(otHdrTO);

    OTUpdateTransactionTO otUpdtTxnTO = new OTUpdateTransactionTO();

    // Site Number
    otUpdtTxnTO.setSiteNumber(Integer.decode(OTTestUtil.SITENUMBER));

    // Transaction Item
    otUpdtTxnTO.setItem(new BigInteger(OTTestUtil.ITEMONE));

    // Transacation TransactionSearchMode TransactionCOD
    otUpdtTxnTO.setTransactionCOD(OTTestUtil.TRANCOD);

    // TransactionNote
    otUpdtTxnTO.setTransactionNote(OTTestUtil.TRANNOTE);

    // Seller
    otUpdtTxnTO.setSeller(OTTestUtil.SELLERID);

    // ZipCode
    otUpdtTxnTO.setZipCode(OTTestUtil.ZIPCODE);

    // IATA
    otUpdtTxnTO.setIata(OTTestUtil.IATA);

    // InTransactionAttribute (1)
    OTInTransactionAttributeTO inTxnAttr = new OTInTransactionAttributeTO();
    inTxnAttr.setAttributeCmd(OTTestUtil.ATTRIBUTECMD);
    inTxnAttr.setAttributeKey(new Integer(OTTestUtil.ATTRIBUTEKEY));
    inTxnAttr.setAttributeValue(OTTestUtil.ATTRIBUTEVALUE);
    inTxnAttr.setAttributeType(new Integer(OTTestUtil.ATTRIBUTETYPE));
    inTxnAttr.setAttributeFlag(OTTestUtil.ATTRIBUTEFLAG);
    otUpdtTxnTO.getInTxnAttrList().add(inTxnAttr);

    otCmdTO.setUpdateTransactionTO(otUpdtTxnTO);

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
  private void validateUpdateTransactionRspTO(OTCommandTO otCommandTO) throws Exception {

    // Header
    OTHeaderTO otHdr = otCommandTO.getHeaderTO();
    if (otHdr == null)
      throw new Exception("OTCommandTO object had no OTHeaderTO object.");
    OTTestUtil.validateOTRspHeader(otHdr, OTTestUtil.REQTYPE_UPDATE,
        OTTestUtil.REQSUBTYPE_UPDATETXN);

    // UpdateTransaction
    OTUpdateTransactionTO otUpdtTxn = otCommandTO.getUpdateTransactionTO();
    if (otUpdtTxn == null)
      throw new Exception("OTCommandTO object had no OTUpdateTransactionTO object.");

    // Error
    OTErrorTO otErr = otUpdtTxn.getError();
    if (otErr == null)
      throw new Exception("OTUpdateTransactionTO object had no OTErrorTO object.");
    OTTestUtil.validateOTRspError(otErr);

    return;

  }

}
