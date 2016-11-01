package pvt.disney.dti.gateway.service.dtixml;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;
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
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.VoidTicketResponseTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

import com.disney.context.ContextManager;

/**
 * Tests the void ticket transformations.
 * 
 * @author lewit019
 *
 */
public class VoidTicketTestCase extends VoidTicketXML{

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
   * Test get to.
   */
  @Test
  public final void testGetTO() {

    // ***** Read the test file. *****
    String requestXML = null;
    String fileName = new String("./config/VoidTicket/VoidTicket_01_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.VOIDTICKET;

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

    VoidTicketRequestTO voidTicketReqTO = (VoidTicketRequestTO) dtiTxn.getRequest()
        .getCommandBody();
    try {
      validateVoidTicketReqTO(voidTicketReqTO);
    } catch (Exception e) {
      fail("Failure in validating void ticket request: " + e.toString());
    }

    return;

  }

  /**
   * Test get to tickets only.
   */
  @Test
  public final void testGetTOTicketsOnly() {

    // ***** Read the test file. *****
    String requestXML = null;
    String fileName = new String("./config/VoidTicket/VoidTicket_02_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.VOIDTICKET;

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
    String fileName = new String("./config/VoidTicket/VoidTicket_01_Rsp_XML.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = createVoidTicketRespTO();
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

    // Validate Void Ticket Response
    String vtRespBaseline = DTITestUtilities.findTag(baselineXML, "VoidTicketResponse");
    String vtRespNew = DTITestUtilities.findTag(newXML, "VoidTicketResponse");

    try {
    	DTITestUtilities.compareXML(vtRespBaseline, vtRespNew, "VoidTicketResponse");
    } catch (Exception e) {
      fail("Exception validating VoidTicketResponse section: " + e.toString());
    }

  }

  /**
   * Validate void ticket req to.
   * 
   * @param vtReqTO the vt req to
   * 
   * @throws Exception the exception
   */
  private static void validateVoidTicketReqTO(VoidTicketRequestTO vtReqTO) throws Exception {

    ArrayList<TicketTO> tktListTO = vtReqTO.getTktList();
    if (tktListTO.size() != 6)
      throw new Exception("Invalid TktListTO.size(): " + tktListTO.size());

    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      BigInteger itemNumObj = aTicketTO.getTktItem();
      if (itemNumObj == null)
        throw new Exception("ItemNum is null.");
      int itemNum = itemNumObj.intValue();

      // Check the individual tickets
      if (itemNum == 1) { // Dssn

        DTITestUtil.validateDSSNTktId(aTicketTO);

      } else if (itemNum == 2) { // Mag Track

        DTITestUtil.validateTwoMagTktId(aTicketTO);

      } else if (itemNum == 3) { // Barcode

        DTITestUtil.validateBarcodeTktId(aTicketTO);

      } else if (itemNum == 4) { // TktNID

        DTITestUtil.validateTktNIDTktId(aTicketTO);

      } else if (itemNum == 5) { // External

        DTITestUtil.validateExternalTktId(aTicketTO);

      } else if (itemNum == 6) { // Single Mag Track
      
        DTITestUtil.validateOneMagTktId(aTicketTO);
        
      } else {
        throw new Exception("Invalid TktItem: " + itemNum);
      }

    }

    // Payment
    ArrayList<PaymentTO> payListTO = vtReqTO.getPaymentList();
    if (payListTO.size() != 5)
      throw new Exception("Invalid PayListTO.size(): " + tktListTO.size());

    for /* each */(PaymentTO aPaymentTO : /* in */payListTO) {

      BigInteger itemNumObj = aPaymentTO.getPayItem();
      if (itemNumObj == null)
        throw new Exception("ItemNum is null.");

      int itemNum = itemNumObj.intValue();

      if (itemNum == 1) { // CCManual

        DTITestUtil.validateCCManualPayment(aPaymentTO);

      } else if (itemNum == 2) { // CCSwipe

        DTITestUtil.validateCCSwipePayment(aPaymentTO);

      } else if (itemNum == 3) { // Voucher

        DTITestUtil.validateVoucherPayment(aPaymentTO);

      } else if (itemNum == 4) { // Gift Card Manual

        DTITestUtil.validateGCManualPayment(aPaymentTO);

      } else if (itemNum == 5) { // Gift Card Swipe

        DTITestUtil.validateGCSwipePayment(aPaymentTO);

      } else {
        throw new Exception("Invalid PayItem: " + itemNum);
      }
    }

    return;
  }

  /**
   * Creates the void ticket resp to.
   * 
   * @return the dTI transaction to
   */
  private static DTITransactionTO createVoidTicketRespTO() {
    DTIResponseTO dtiRespTO = new DTIResponseTO();

    // Payload Header
    PayloadHeaderTO payHdrTO = DTITestUtil.createTestPayHeaderTO();
    dtiRespTO.setPayloadHeader(payHdrTO);

    // Command Header
    CommandHeaderTO cmdHdrTO = DTITestUtil.createTestCmdHeaderTO();
    dtiRespTO.setCommandHeader(cmdHdrTO);

    // Void Ticket Response
    VoidTicketResponseTO vtRespTO = new VoidTicketResponseTO();

    // Ticket 1
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("1"));
    GregorianCalendar dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTDSSNDATE);
    aTicketTO.setDssn(dssnDate, DTITestUtil.TKTDSSNSITE, DTITestUtil.TKTDSSNSTATION, DTITestUtil.TKTDSSNNBR);
    aTicketTO.setTktNID(DTITestUtil.TKTNID);

    TicketTransactionTO tktTranTO = new TicketTransactionTO();
    tktTranTO.setTktProvider(DTITestUtil.TKTPROVIDER);
    dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTTRANDATE);
    tktTranTO.setDssn(dssnDate, DTITestUtil.TKTTRANSITE, DTITestUtil.TKTTRANSTATION, DTITestUtil.TKTTRANNBR);
    tktTranTO.setTranNID(DTITestUtil.TKTTRANNID);
    aTicketTO.setTktTran(tktTranTO);


    vtRespTO.addTicket(aTicketTO);

    // Ticket 2
    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("2"));
    aTicketTO.setExternal(DTITestUtil.TKTEXTERNAL);

    tktTranTO = new TicketTransactionTO();
    dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTTRANDATE);
    tktTranTO.setDssn(dssnDate, DTITestUtil.TKTTRANSITE, DTITestUtil.TKTTRANSTATION, DTITestUtil.TKTTRANNBR);
    tktTranTO.setTranNID(DTITestUtil.TKTTRANNID);
    aTicketTO.setTktTran(tktTranTO);

    vtRespTO.addTicket(aTicketTO);

    dtiRespTO.setCommandBody(vtRespTO);
    DTIErrorTO tktErrorTO = new DTIErrorTO(new BigInteger(DTITestUtil.TKTERRORCODE), DTITestUtil.TKTERRORCLASS,
        DTITestUtil.TKTERRORTEXT, DTITestUtil.TKTERRORTYPE);
    dtiRespTO.setDtiError(tktErrorTO);

    // Complete the response
    DTITransactionTO dtiTxnTO = new DTITransactionTO(DTITransactionTO.TransactionType.VOIDTICKET);
    dtiTxnTO.setResponse(dtiRespTO);

    return dtiTxnTO;

  }

}
