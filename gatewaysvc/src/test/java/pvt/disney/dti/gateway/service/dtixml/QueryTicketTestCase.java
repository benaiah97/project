package pvt.disney.dti.gateway.service.dtixml;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
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
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.QueryTicketResponseTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

import com.disney.context.ContextManager;

/**
 * Tests the query ticket transformations.
 * 
 * @author lewit019
 * 
 */
public class QueryTicketTestCase extends QueryTicketXML {

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
   * Tests getting the transfer object.
   */
  @Test
  public final void testGetTO() {

    
    
    // ***** Read the test file. *****
    String requestXML = null;
    String fileName = new String("./config/QueryTicket/QueryTicket_01_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.QUERYTICKET;

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

    QueryTicketRequestTO queryTicketReqTO = (QueryTicketRequestTO) dtiTxn.getRequest()
        .getCommandBody();
    try {
      validateQueryTicketReqTO(queryTicketReqTO);
    } catch (Exception e) {
      fail("Failure in validating query ticket request: " + e.toString());
    }

    return;
  }

  /**
   * Validate query ticket req to.
   * 
   * @param qReqTO
   *          the q req to
   * 
   * @throws Exception
   *           the exception
   */
  private static void validateQueryTicketReqTO(QueryTicketRequestTO qReqTO) throws Exception {

    ArrayList<TicketTO> tktListTO = qReqTO.getTktList();
    if (tktListTO.size() != 6)
      throw new Exception("Invalid TktListTO.size(): " + tktListTO.size());

    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      BigInteger itemNumObj = aTicketTO.getTktItem();
      if (itemNumObj == null)
        throw new Exception("ItemNum is null.");
      int itemNum = itemNumObj.intValue();

      if (itemNum == 1) { // Dssn
        DTITestUtil.validateDSSNTktId(aTicketTO);
      } else if (itemNum == 2) { // Mag Track (2 tracks)
        DTITestUtil.validateTwoMagTktId(aTicketTO);
      } else if (itemNum == 3) { // Barcode
        DTITestUtil.validateBarcodeTktId(aTicketTO);
      } else if (itemNum == 4) { // TktNID
        DTITestUtil.validateTktNIDTktId(aTicketTO);
      } else if (itemNum == 5) { // External
        DTITestUtil.validateExternalTktId(aTicketTO);
      } else if (itemNum == 6) { // Mag Track (1 track)
        DTITestUtil.validateOneMagTktId(aTicketTO);
      } else {
        throw new Exception("Invalid TktItem: " + itemNum);
      }

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
    String fileName = new String("./config/QueryTicket/QueryTicket_01_Rsp_XML.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = createQueryTicketRespTO();
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

    // Validate Query Ticket Response
    String qtRespBaseline = DTITestUtilities.findTag(baselineXML, "QueryTicketResponse");
    String qtRespNew = DTITestUtilities.findTag(newXML, "QueryTicketResponse");

    try {
    	DTITestUtilities.compareXML(qtRespBaseline, qtRespNew, "QueryTicketResponse");
    } catch (Exception e) {
      fail("Exception validating QueryTicketResponse section: " + e.toString());
    }

  }

  /**
   * Creates the query ticket resp to.
   * 
   * @return the dTI transaction to
   */
  private static DTITransactionTO createQueryTicketRespTO() {

    DTIResponseTO dtiRespTO = new DTIResponseTO();

    // Payload Header
    PayloadHeaderTO payHdrTO = DTITestUtil.createTestPayHeaderTO();
    dtiRespTO.setPayloadHeader(payHdrTO);

    // Command Header
    CommandHeaderTO cmdHdrTO = DTITestUtil.createTestCmdHeaderTO();
    dtiRespTO.setCommandHeader(cmdHdrTO);

    // QueryTicketResponse
    QueryTicketResponseTO qtRespTO = new QueryTicketResponseTO();
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("1"));
    aTicketTO.setMag(DTITestUtil.TKTMAGTRACK1);
    aTicketTO.setBarCode(DTITestUtil.TKTBARCODE);
    GregorianCalendar dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTDSSNDATE);
    aTicketTO.setDssn(dssnDate, DTITestUtil.TKTDSSNSITE, DTITestUtil.TKTDSSNSTATION,
        DTITestUtil.TKTDSSNNBR);
    aTicketTO.setTktNID(DTITestUtil.TKTNID);
    aTicketTO.setExternal(DTITestUtil.TKTEXTERNAL);
    aTicketTO.setTktPrice(new BigDecimal(DTITestUtil.TKTPRICE));
    aTicketTO.setTktTax(new BigDecimal(DTITestUtil.TKTTAX));
    TicketTO.TktStatusTO aStatus01TO = aTicketTO.new TktStatusTO();
    aStatus01TO.setStatusItem(DTITestUtil.STATUSITEM);
    aStatus01TO.setStatusValue(DTITestUtil.STATUSVALUE);
    aTicketTO.addTicketStatus(aStatus01TO);
    TicketTO.TktStatusTO aStatus02TO = aTicketTO.new TktStatusTO();
    aStatus02TO.setStatusItem(DTITestUtil.STATUSITEM);
    aStatus02TO.setStatusValue(DTITestUtil.STATUSVALUE);
    aTicketTO.addTicketStatus(aStatus02TO);
    GregorianCalendar validStart = CustomDataTypeConverter
        .parseYYYYMMDDDate(DTITestUtil.TKTVALIDSTART);
    aTicketTO.setTktValidityValidStart(validStart);
    GregorianCalendar validEnd = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTVALIDEND);
    aTicketTO.setTktValidityValidEnd(validEnd);
    aTicketTO.setAgeGroup(DTITestUtil.TKTAGEGROUP);
    aTicketTO.setMediaType(DTITestUtil.TKTMEDIATYPE);
    aTicketTO.setPassType(DTITestUtil.TKTPASSTYPE);
    aTicketTO.setPassClass(DTITestUtil.TKTPASSCLASS);
    aTicketTO.setPassRenew(DTITestUtil.TKTPASSRENEW);
    aTicketTO.setResident(DTITestUtil.TKTRESIDENT);
    qtRespTO.addTicket(aTicketTO);
    dtiRespTO.setCommandBody(qtRespTO);

    // Complete the response
    DTITransactionTO dtiTxnTO = new DTITransactionTO(DTITransactionTO.TransactionType.QUERYTICKET);
    DTIErrorTO tktErrorTO = new DTIErrorTO(new BigInteger(DTITestUtil.TKTERRORCODE),
        DTITestUtil.TKTERRORCLASS, DTITestUtil.TKTERRORTEXT, DTITestUtil.TKTERRORTYPE);
    dtiRespTO.setDtiError(tktErrorTO);
    dtiTxnTO.setResponse(dtiRespTO);

    return dtiTxnTO;
  }

}
