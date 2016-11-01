package pvt.disney.dti.gateway.service.dtixml;

import static org.junit.Assert.fail;

import java.math.BigInteger;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;

/**
 * Tests the headers.
 * 
 * @author lewit019
 *
 */
public class HeadersTestCase extends PayloadHeaderXML {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test get jaxb payload header error.
   */
  @Test
  public final void testGetJaxbPayloadHeaderError() {
    
    // ***** Read the test file. *****
    String baselineXML = null;
    String fileName = new String("./config/Headers/QueryTicket_01_Rsp_XML.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = createPayHeaderErrorTO();
    String newXML = null;

    try {
      newXML = TransmissionRespXML.getXML(dtiTxn);
    } catch (JAXBException je) {
      fail("JAXBException creating the response XML: " + je.toString());
    }

    // Validate the XML Headers (Payload & Command)
    try {
      DTITestUtil.validateXMLPayloadHeader(baselineXML, newXML);
    } catch (Exception e) {
      fail("Exception validating response XML headers: " + e.toString());
    }

    return;
  }

  /**
   * Test get jaxb command header error.
   */
  @Test
  public final void testGetJaxbCommandHeaderError() {
    // ***** Read the test file. *****
    String baselineXML = null;
    String fileName = new String("./config/Headers/QueryTicket_02_Rsp_XML.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = createCmdHeaderErrorTO();
    String newXML = null;

    try {
      newXML = TransmissionRespXML.getXML(dtiTxn);
    } catch (JAXBException je) {
      fail("JAXBException creating the response XML: " + je.toString());
    }

    // Validate the XML Headers (Payload & Command)
    try {
      DTITestUtil.validateXMLHeaders(baselineXML, newXML);
    } catch (Exception e) {
      fail("Exception validating response XML headers: " + e.toString());
    }

    return;
  }

  /**
   * Creates the pay header error to.
   * 
   * @return the dTI transaction to
   */
  private static DTITransactionTO createPayHeaderErrorTO() {

    DTIResponseTO dtiRespTO = new DTIResponseTO();

    // Payload Header Error
    DTIErrorTO payloadErrorTO = new DTIErrorTO(new BigInteger("310"), "Servelet",
        "Transaction Time-Out", "Critical");
    dtiRespTO.setDtiError(payloadErrorTO);
    
    // Payload Header
    PayloadHeaderTO payHdrTO = DTITestUtil.createTestPayHeaderTO();
    dtiRespTO.setPayloadHeader(payHdrTO);

    // Command Header
    CommandHeaderTO cmdHdrTO = DTITestUtil.createTestCmdHeaderTO();
    dtiRespTO.setCommandHeader(cmdHdrTO);

    // Complete the response
    DTITransactionTO dtiTxnTO = new DTITransactionTO(DTITransactionTO.TransactionType.QUERYTICKET);
    dtiTxnTO.setResponse(dtiRespTO);

    return dtiTxnTO;
  }

  /**
   * Creates the cmd header error to.
   * 
   * @return the dTI transaction to
   */
  private static DTITransactionTO createCmdHeaderErrorTO() {

    DTIResponseTO dtiRespTO = new DTIResponseTO();
    
    // Command Header Error
    DTIErrorTO commandErrorTO = new DTIErrorTO(new BigInteger("500"), "Product",
        "Invalid Product Code", "Error");
    dtiRespTO.setDtiError(commandErrorTO);

    // Payload Header
    PayloadHeaderTO payHdrTO = DTITestUtil.createTestPayHeaderTO();
    dtiRespTO.setPayloadHeader(payHdrTO);

    // Command Header
    CommandHeaderTO cmdHdrTO = DTITestUtil.createTestCmdHeaderTO();
    dtiRespTO.setCommandHeader(cmdHdrTO);

    // Complete the response
    DTITransactionTO dtiTxnTO = new DTITransactionTO(DTITransactionTO.TransactionType.QUERYTICKET);
    dtiTxnTO.setResponse(dtiRespTO);

    return dtiTxnTO;
  }

}
