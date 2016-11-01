package pvt.disney.dti.gateway.service.dtixml;

import static org.junit.Assert.fail;

import java.util.GregorianCalendar;

import javax.xml.bind.JAXBException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.UpdateTransactionRequestTO;
import pvt.disney.dti.gateway.data.UpdateTransactionResponseTO;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

import com.disney.context.ContextManager;

/**
 * Tests the update transaction transformations.
 * 
 * @author lewit019
 *
 */
public class UpdateTransactionTestCase extends UpdateTransactionXML {

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
   * Test get to tran dssn.
   */
  @Test
  public final void testGetTOTranDSSN() {

    // ***** Read the test file. *****
    String requestXML = null;
    String fileName = new String("./config/UpdateTransaction/UpdateTransaction_01_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.UPDATETRANSACTION;

    // ***** Create the test object *****
    try {

      dtiTxn = TransmissionRqstXML.getTO(requestXML, requestType, tktBroker);

      if (dtiTxn == null)
        throw new DTIException("Null request transfer object returned from parser.");

    } catch (JAXBException je) {
      fail("JAXBException creating the request object: " + je.toString());
    } catch (DTIException qe) {
      ContextManager.getInstance().removeContext();
      fail("DTIException creating the request object: " + qe.toString());
    }

    // ***** Evaluate the test transfer object *****
    // Note: The payload and command headers are evaluated by this test as well.
    // Other tests can either ignore validation of these items or must make them
    // standard to what's validated here.

    PayloadHeaderTO payloadHdrTO = dtiTxn.getRequest().getPayloadHeader();
    try {
      DTITestUtil.validatePayloadHeaderReqTO(payloadHdrTO);
    } catch (Exception e) {
      fail("Failure in validating payload header: " + e.toString());
    }

    CommandHeaderTO commandHdrTO = dtiTxn.getRequest().getCommandHeader();
    try {
      DTITestUtil.validateCommandHeaderReqTO(commandHdrTO);
    } catch (Exception e) {
      fail("Failure in validating command header: " + e.toString());
    }

    UpdateTransactionRequestTO upgradeAlphaReqTO = (UpdateTransactionRequestTO) dtiTxn.getRequest()
        .getCommandBody();
    try {
      validateUpdateTransactionReqTO(upgradeAlphaReqTO);
    } catch (Exception e) {
      fail("Failure in validating upgrade alpha request: " + e.toString());
    }

    return;

  }

  /**
   * Test get to tran nid.
   */
  @Test
  public final void testGetTOTranNID() {

    // ***** Read the test file. *****
    String requestXML = null;
    String fileName = new String("./config/UpdateTransaction/UpdateTransaction_02_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.UPDATETRANSACTION;

    // ***** Create the test object *****
    try {

      dtiTxn = TransmissionRqstXML.getTO(requestXML, requestType, tktBroker);

      if (dtiTxn == null)
        throw new DTIException("Null request transfer object returned from parser.");

    } catch (JAXBException je) {
      fail("JAXBException creating the request object: " + je.toString());
    } catch (DTIException qe) {
      ContextManager.getInstance().removeContext();
      fail("DTIException creating the request object: " + qe.toString());
    }

    // ***** Evaluate the test transfer object *****
    // Note: The payload and command headers are evaluated by this test as well.
    // Other tests can either ignore validation of these items or must make them
    // standard to what's validated here.

    PayloadHeaderTO payloadHdrTO = dtiTxn.getRequest().getPayloadHeader();
    try {
      DTITestUtil.validatePayloadHeaderReqTO(payloadHdrTO);
    } catch (Exception e) {
      fail("Failure in validating payload header: " + e.toString());
    }

    CommandHeaderTO commandHdrTO = dtiTxn.getRequest().getCommandHeader();
    try {
      DTITestUtil.validateCommandHeaderReqTO(commandHdrTO);
    } catch (Exception e) {
      fail("Failure in validating command header: " + e.toString());
    }

    UpdateTransactionRequestTO updateTransactionReqTO = (UpdateTransactionRequestTO) dtiTxn
        .getRequest().getCommandBody();
    try {
      validateUpdateTransactionReqTO(updateTransactionReqTO);
    } catch (Exception e) {
      fail("Failure in validating Update Transaction request: " + e.toString());
    }

    return;

  }
  
  /**
   * Test get to tran id only.
   */
  @Test
  public final void testGetTOTranIDOnly() {

    // ***** Read the test file. *****
    String requestXML = null;
    String fileName = new String("./config/UpdateTransaction/UpdateTransaction_03_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.UPDATETRANSACTION;

    // ***** Create the test object *****
    try {

      dtiTxn = TransmissionRqstXML.getTO(requestXML, requestType, tktBroker);

      if (dtiTxn == null)
        throw new DTIException("Null request transfer object returned from parser.");

    } catch (JAXBException je) {
      fail("JAXBException creating the request object: " + je.toString());
    } catch (DTIException qe) {
      ContextManager.getInstance().removeContext();
      fail("DTIException creating the request object: " + qe.toString());
    }

    return;

  }

  /**
   * Test get jaxb.
   */
  @Test
  public final void testGetJaxb() {
    // ***** Read the test file. *****
    String baselineXML = null;
    String fileName = new String("./config/UpdateTransaction/UpdateTransaction_01_Rsp_XML.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = createUpdateTransactionRespTO();
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

    // There is no response portion to validate, beyond headers.

  }

  /**
   * Validate update transaction req to.
   * 
   * @param utReqTO the ut req to
   * 
   * @throws Exception the exception
   */
  private static void validateUpdateTransactionReqTO(UpdateTransactionRequestTO utReqTO)
      throws Exception {

    // Transaction
    TicketTransactionTO tktTranTO = utReqTO.getTransaction();

    if (tktTranTO.getTranNID() == null) {

      GregorianCalendar gCalDssnDateTO = tktTranTO.getDssnDate();
      GregorianCalendar gCalDssnDateTst = CustomDataTypeConverter
          .parseYYYYMMDDDate(DTITestUtil.TKTTRANDATE);
      if (gCalDssnDateTO.compareTo(gCalDssnDateTst) != 0)
        throw new Exception("Invalid TranDSSN.TranDate value: "
            + CustomDataTypeConverter.printCalToDTIDateFormat(gCalDssnDateTO));
      
      if (tktTranTO.getDssnSite().compareTo(DTITestUtil.TKTTRANSITE) != 0)
        throw new Exception("Invalid TranDSSN.TranSite value: " + tktTranTO.getDssnSite());

      if (tktTranTO.getDssnStation().compareTo(DTITestUtil.TKTTRANSTATION) != 0)
        throw new Exception("Invalid TranDSSN.TranStation value: " + tktTranTO.getDssnStation());

      if (tktTranTO.getDssnNumber().compareTo(DTITestUtil.TKTTRANNBR) != 0)
        throw new Exception("Invalid TranDSSN.TranNbr value: " + tktTranTO.getDssnNumber());

    } else {
      if (tktTranTO.getTranNID().compareTo(DTITestUtil.TKTTRANNID) != 0)
        throw new Exception("Invalid TranID.TranNID value: " + tktTranTO.getTranNID());
    }

    // SalesRep
    if (utReqTO.getSalesRep().compareTo(DTITestUtil.SALESREP) != 0)
      throw new Exception("Invalid SalesRep value: " + utReqTO.getSalesRep());

    // Agency
    AgencyTO agencyTO = utReqTO.getAgency();

    if (agencyTO.getIATA().compareTo(DTITestUtil.IATA) != 0)
      throw new Exception("Invalid Agency.IATA value: " + agencyTO.getIATA());

    return;
  }

  /**
   * Creates the update transaction resp to.
   * 
   * @return the dTI transaction to
   */
  private static DTITransactionTO createUpdateTransactionRespTO() {
    DTIResponseTO dtiRespTO = new DTIResponseTO();

    // Payload Header
    PayloadHeaderTO payHdrTO = DTITestUtil.createTestPayHeaderTO();
    dtiRespTO.setPayloadHeader(payHdrTO);

    // Command Header
    CommandHeaderTO cmdHdrTO = DTITestUtil.createTestCmdHeaderTO();
    dtiRespTO.setCommandHeader(cmdHdrTO);

    // Upgrade Alpha Response
    UpdateTransactionResponseTO utRespTO = new UpdateTransactionResponseTO();
    dtiRespTO.setCommandBody(utRespTO);

    // Complete the response
    DTITransactionTO dtiTxnTO = new DTITransactionTO(
        DTITransactionTO.TransactionType.UPDATETRANSACTION);
    dtiTxnTO.setResponse(dtiRespTO);

    return dtiTxnTO;

  }

}
