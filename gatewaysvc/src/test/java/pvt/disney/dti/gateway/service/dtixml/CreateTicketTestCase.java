package pvt.disney.dti.gateway.service.dtixml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import pvt.disney.dti.gateway.test.util.*;
import javax.xml.bind.JAXBException;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.CreateTicketRequestTO;
import pvt.disney.dti.gateway.data.CreateTicketResponseTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

import com.disney.context.ContextManager;

/**
 * Validates the create ticket transformations.
 * 
 * @author lewit019
 *
 */
public class CreateTicketTestCase extends CreateTicketXML {

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
    String fileName = new String("./config/CreateTicket/CreateTicket_01_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.CREATETICKET;

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

    CreateTicketRequestTO createTicketReqTO = (CreateTicketRequestTO) dtiTxn.getRequest()
        .getCommandBody();
    try {
      validateCreateTicketReqTO(createTicketReqTO);
    } catch (Exception e) {
      fail("Failure in validating upgrade alpha request: " + e.toString());
    }

    return;

  }

  /**
   * Test get JAXB.
   */
  @Test
  public final void testGetJaxb() {
    // ***** Read the test file. *****
    String baselineXML = null;
    String fileName = new String("./config/CreateTicket/CreateTicket_01_Rsp_XML.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = createCreateTicketRespTO();
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
    
    // Validate Create Ticket Response 
    String ctRespBaseline = DTITestUtilities.findTag(baselineXML, "CreateTicketResponse");
    String ctRespNew = DTITestUtilities.findTag(newXML, "CreateTicketResponse");
    
    try {
      DTITestUtilities.compareXML(ctRespBaseline, ctRespNew, "CreateTicketResponse");
    } catch (Exception e) {
      fail("Exception validating CreateTicketResponse section: " + e.toString());
    }
    
    return;

  }

  /**
   * Validate create ticket req to.
   * 
   * @param ctReqTO the ct req to
   * 
   * @throws Exception the exception
   */
  private static void validateCreateTicketReqTO(CreateTicketRequestTO ctReqTO) throws Exception {

    // Ticket
    ArrayList<TicketTO> tktListTO = ctReqTO.getTktList();
    if (tktListTO.size() != 2)
      throw new Exception("Invalid TktListTO.size(): " + tktListTO.size());

    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      BigInteger itemNumObj = aTicketTO.getTktItem();
      if (itemNumObj == null)
        throw new Exception("ItemNum is null.");
      int itemNum = itemNumObj.intValue();

      // Check the individual tickets
      if ((itemNum == 1) || (itemNum == 2)) {

        if (aTicketTO.getProdCode().compareTo(DTITestUtil.PRODCODE) != 0)
          throw new Exception("Invalid Ticket.ProdCode: " + aTicketTO.getProdCode());

        if (aTicketTO.getProdQty().compareTo(new BigInteger(DTITestUtil.PRODQTY)) != 0)
          throw new Exception("Invalid Ticket.ProdQty: " + aTicketTO.getProdQty());

        if (aTicketTO.getProdPrice().compareTo(new BigDecimal(DTITestUtil.PRODPRICE)) != 0)
          throw new Exception("Invalid Ticket.ProdPrice: " + aTicketTO.getProdPrice());

        if (aTicketTO.getTktShell().compareTo(DTITestUtil.TKTSHELL) != 0)
          throw new Exception("Invalid Ticket.TktShell: " + aTicketTO.getTktShell());

        if (aTicketTO.getTktNote().compareTo(DTITestUtil.TKTNOTE) != 0)
          throw new Exception("Invalid Ticket.TktNote: " + aTicketTO.getTktNote());

      } else
        throw new Exception("Invalid TktItem: " + itemNum);

    }

    return;
  }

  /**
   * Creates the create ticket resp to.
   * 
   * @return the dTI transaction to
   */
  private static DTITransactionTO createCreateTicketRespTO() {
    DTIResponseTO dtiRespTO = new DTIResponseTO();

    // Payload Header
    PayloadHeaderTO payHdrTO = DTITestUtil.createTestPayHeaderTO();
    dtiRespTO.setPayloadHeader(payHdrTO);

    // Command Header
    CommandHeaderTO cmdHdrTO = DTITestUtil.createTestCmdHeaderTO();
    dtiRespTO.setCommandHeader(cmdHdrTO);

    // Create Ticket Response
    CreateTicketResponseTO ctRespTO = new CreateTicketResponseTO();

    // Ticket 1
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("1"));
    aTicketTO.setMag(DTITestUtil.TKTMAGTRACK1);
    aTicketTO.setBarCode(DTITestUtil.TKTBARCODE);
    GregorianCalendar dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTDSSNDATE);
    aTicketTO.setDssn(dssnDate, DTITestUtil.TKTDSSNSITE, DTITestUtil.TKTDSSNSTATION, DTITestUtil.TKTDSSNNBR);
    aTicketTO.setTktNID(DTITestUtil.TKTNID);
    aTicketTO.setTktPrice(new BigDecimal(DTITestUtil.TKTPRICE));
    aTicketTO.setTktTax(new BigDecimal(DTITestUtil.TKTTAX));
    GregorianCalendar validStart = CustomDataTypeConverter
        .parseYYYYMMDDDate(DTITestUtil.TKTVALIDSTART);
    aTicketTO.setTktValidityValidStart(validStart);
    GregorianCalendar validEnd = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTVALIDEND);
    aTicketTO.setTktValidityValidEnd(validEnd);
    aTicketTO.setTktNote(DTITestUtil.TKTNOTE);

    TicketTransactionTO tktTranTO = new TicketTransactionTO();
    tktTranTO.setTktProvider(DTITestUtil.TKTPROVIDER);
    dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTTRANDATE);
    tktTranTO.setDssn(dssnDate, DTITestUtil.TKTTRANSITE, DTITestUtil.TKTTRANSTATION, DTITestUtil.TKTTRANNBR);
    aTicketTO.setTktTran(tktTranTO);

    ctRespTO.addTicket(aTicketTO);

    // Ticket 2
    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("2"));
    aTicketTO.setMag(DTITestUtil.TKTMAGTRACK1);
    aTicketTO.setBarCode(DTITestUtil.TKTBARCODE);
    dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTDSSNDATE);
    aTicketTO.setDssn(dssnDate, DTITestUtil.TKTDSSNSITE, DTITestUtil.TKTDSSNSTATION, DTITestUtil.TKTDSSNNBR);
    aTicketTO.setTktNID(DTITestUtil.TKTNID);
    aTicketTO.setTktPrice(new BigDecimal(DTITestUtil.TKTPRICE));
    aTicketTO.setTktTax(new BigDecimal(DTITestUtil.TKTTAX));
    validStart = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTVALIDSTART);
    aTicketTO.setTktValidityValidStart(validStart);
    validEnd = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTVALIDEND);
    aTicketTO.setTktValidityValidEnd(validEnd);
    aTicketTO.setTktNote(DTITestUtil.TKTNOTE);

    tktTranTO = new TicketTransactionTO();
    tktTranTO.setTktProvider(DTITestUtil.TKTPROVIDER);
    dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTTRANDATE);
    tktTranTO.setDssn(dssnDate, DTITestUtil.TKTTRANSITE, DTITestUtil.TKTTRANSTATION, DTITestUtil.TKTTRANNBR);
    tktTranTO.setTranNID(DTITestUtil.TKTTRANNID);
    aTicketTO.setTktTran(tktTranTO);
    ctRespTO.addTicket(aTicketTO);

    // Product 1
    ProductTO aProductTO = new ProductTO();
    aProductTO.setProdItem(new BigInteger("1"));
    aProductTO.setProdCode(DTITestUtil.PRODCODE);
    aProductTO.setProdQty(new BigInteger(DTITestUtil.PRODQTY));
    aProductTO.setProdParts(DTITestUtil.PRODPARTS);
    aProductTO.setProdPrice(new BigDecimal(DTITestUtil.PRODPRICE));
    aProductTO.setProdTax1(new BigDecimal(DTITestUtil.PRODTAX1));
    aProductTO.setProdTax2(new BigDecimal(DTITestUtil.PRODTAX2));
    aProductTO.setProdDescription(DTITestUtil.PRODDESC);
    aProductTO.setProdReceiptMsg(DTITestUtil.PRODRECEIPTMSG);
    ctRespTO.addProduct(aProductTO);
    
    // Product 2
    aProductTO = new ProductTO();
    aProductTO.setProdItem(new BigInteger("2"));
    aProductTO.setProdCode(DTITestUtil.PRODCODE);
    aProductTO.setProdQty(new BigInteger(DTITestUtil.PRODQTY));
    aProductTO.setProdParts(DTITestUtil.PRODPARTS);
    aProductTO.setProdPrice(new BigDecimal(DTITestUtil.PRODPRICE));
    aProductTO.setProdTax1(new BigDecimal(DTITestUtil.PRODTAX1));
    aProductTO.setProdTax2(new BigDecimal(DTITestUtil.PRODTAX2));
    aProductTO.setProdDescription(DTITestUtil.PRODDESC);
    aProductTO.setProdReceiptMsg(DTITestUtil.PRODRECEIPTMSG);
    ctRespTO.addProduct(aProductTO);   
    
    dtiRespTO.setCommandBody(ctRespTO);
    
    DTIErrorTO tktErrorTO = new DTIErrorTO(new BigInteger(DTITestUtil.TKTERRORCODE), DTITestUtil.TKTERRORCLASS,
    DTITestUtil.TKTERRORTEXT, DTITestUtil.TKTERRORTYPE);
    dtiRespTO.setDtiError(tktErrorTO);
    
    // Complete the response
    DTITransactionTO dtiTxnTO = new DTITransactionTO(DTITransactionTO.TransactionType.CREATETICKET);
    dtiTxnTO.setResponse(dtiRespTO);

    return dtiTxnTO;

  }

}
