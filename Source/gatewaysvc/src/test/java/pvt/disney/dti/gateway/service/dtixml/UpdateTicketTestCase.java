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
import pvt.disney.dti.gateway.data.UpdateTicketRequestTO;
import pvt.disney.dti.gateway.data.UpdateTicketResponseTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

import com.disney.context.ContextManager;

/**
 * Tests the update ticket transformations.
 * 
 * @author lewit019
 * 
 */
public class UpdateTicketTestCase extends UpdateTicketXML {

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
    String fileName = new String("./config/UpdateTicket/UpdateTicket_01_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.UPDATETICKET;

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

    UpdateTicketRequestTO updateTicketReqTO = (UpdateTicketRequestTO) dtiTxn.getRequest()
        .getCommandBody();
    try {
      validateUpgradeTicketReqTO(updateTicketReqTO);
    } catch (Exception e) {
      fail("Failure in validating upgrade alpha request: " + e.toString());
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
    String fileName = new String("./config/UpdateTicket/UpdateTicket_01_Rsp_XML.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = createUpdateTicketRespTO();
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

    // Validate Update Ticket Response
    String utRespBaseline = DTITestUtilities.findTag(baselineXML, "UpdateTicketResponse");
    String utRespNew = DTITestUtilities.findTag(newXML, "UpdateTicketResponse");

    try {
    	DTITestUtilities.compareXML(utRespBaseline, utRespNew, "UpdateTicketResponse");
    } catch (Exception e) {
      fail("Exception validating UpdateTicketResponse section: " + e.toString());
    }

  }

  /**
   * Validate upgrade ticket req to.
   * 
   * @param utReqTO
   *          the ut req to
   * 
   * @throws Exception
   *           the exception
   */
  private static void validateUpgradeTicketReqTO(UpdateTicketRequestTO utReqTO) throws Exception {

    ArrayList<TicketTO> tktListTO = utReqTO.getTktList();
    if (tktListTO.size() != 6)
      throw new Exception("Invalid TktListTO.size(): " + tktListTO.size());

    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      BigInteger itemNumObj = aTicketTO.getTktItem();
      if (itemNumObj == null)
        throw new Exception("ItemNum is null.");
      int itemNum = itemNumObj.intValue();

      if (itemNum == 1) { // Dssn

        DTITestUtil.validateDSSNTktId(aTicketTO);

        // TktStatus
        ArrayList<TicketTO.TktStatusTO> tStatTOArray = aTicketTO.getTktStatusList();
        if (tStatTOArray.size() != 1)
          throw new Exception("Invalid Ticket.TktStatusTO.size(): " + tStatTOArray);

        for /* each */(TicketTO.TktStatusTO aStatusTO : /* in */tStatTOArray) {

          if (aStatusTO.getStatusItem().compareTo(DTITestUtil.STATUSITEM) != 0)
            throw new Exception("Invalid TktStatus.StatusItem: " + aStatusTO.getStatusItem());

          if (aStatusTO.getStatusValue().compareTo(DTITestUtil.STATUSVALUE) != 0)
            throw new Exception("Invalid TktStatus.StatusValue: " + aStatusTO.getStatusValue());

        }

        // TktValidity
        GregorianCalendar gCalValidStartTO = aTicketTO.getTktValidityValidStart();
        GregorianCalendar gCalValidStartTst = CustomDataTypeConverter
            .parseYYYYMMDDDate(DTITestUtil.TKTVALIDSTART);
        if (gCalValidStartTO.compareTo(gCalValidStartTst) != 0)
          throw new Exception("Invalid TktValidity.ValidStart value: "
              + CustomDataTypeConverter.printCalToDTIDateFormat(gCalValidStartTO));

        GregorianCalendar gCalValidEndTO = aTicketTO.getTktValidityValidEnd();
        GregorianCalendar gCalValidEndTst = CustomDataTypeConverter
            .parseYYYYMMDDDate(DTITestUtil.TKTVALIDEND);
        if (gCalValidEndTO.compareTo(gCalValidEndTst) != 0)
          throw new Exception("Invalid TktValidity.ValidEnd value: "
              + CustomDataTypeConverter.printCalToDTIDateFormat(gCalValidEndTO));

        // TktSecurity
        if (aTicketTO.getTktSecurityLevel().compareTo(DTITestUtil.TKTSECURITYLEVEL) != 0)
          throw new Exception("Invalid TktSecurity.Level value: " + aTicketTO.getTktSecurityLevel());

        // TktMarket
        if (aTicketTO.getTktMarket().compareTo(DTITestUtil.TKTMARKET) != 0)
          throw new Exception("Invalid TktMarket value: " + aTicketTO.getTktMarket());

        // TktNote
        if (aTicketTO.getTktNote().compareTo(DTITestUtil.TKTNOTE) != 0)
          throw new Exception("Invalid TktNote value: " + aTicketTO.getTktNote());

      } else if (itemNum == 2) { // Mag Track

        DTITestUtil.validateTwoMagTktId(aTicketTO);

        // TktNote
        if (aTicketTO.getTktNote().compareTo(DTITestUtil.TKTNOTE) != 0)
          throw new Exception("Invalid TktNote value: " + aTicketTO.getTktNote());

      } else if (itemNum == 3) { // Barcode

        DTITestUtil.validateBarcodeTktId(aTicketTO);

        // TktNote
        if (aTicketTO.getTktNote().compareTo(DTITestUtil.TKTNOTE) != 0)
          throw new Exception("Invalid TktNote value: " + aTicketTO.getTktNote());

      } else if (itemNum == 4) { // TktNID

        DTITestUtil.validateTktNIDTktId(aTicketTO);

        // TktNote
        if (aTicketTO.getTktNote().compareTo(DTITestUtil.TKTNOTE) != 0)
          throw new Exception("Invalid TktNote value: " + aTicketTO.getTktNote());

      } else if (itemNum == 5) { // External

        DTITestUtil.validateExternalTktId(aTicketTO);

        // TktNote
        if (aTicketTO.getTktNote().compareTo(DTITestUtil.TKTNOTE) != 0)
          throw new Exception("Invalid TktNote value: " + aTicketTO.getTktNote());

      } else if (itemNum == 6) { // Mag track (one value)

        DTITestUtil.validateOneMagTktId(aTicketTO);

      } else {
        throw new Exception("Invalid TktItem: " + itemNum);
      }

    }

    return;

  }

  /**
   * Creates the update ticket resp to.
   * 
   * @return the dTI transaction to
   */
  private static DTITransactionTO createUpdateTicketRespTO() {
    DTIResponseTO dtiRespTO = new DTIResponseTO();

    // Payload Header
    PayloadHeaderTO payHdrTO = DTITestUtil.createTestPayHeaderTO();
    dtiRespTO.setPayloadHeader(payHdrTO);

    // Command Header
    CommandHeaderTO cmdHdrTO = DTITestUtil.createTestCmdHeaderTO();
    dtiRespTO.setCommandHeader(cmdHdrTO);

    // Upgrade Alpha Response
    UpdateTicketResponseTO utRespTO = new UpdateTicketResponseTO();

    // Ticket 1
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("1"));

    utRespTO.addTicket(aTicketTO);

    // Ticket 2
    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("2"));
    utRespTO.addTicket(aTicketTO);

    dtiRespTO.setCommandBody(utRespTO);

    // Complete the response
    DTIErrorTO tktErrorTO = new DTIErrorTO(new BigInteger(DTITestUtil.TKTERRORCODE),
        DTITestUtil.TKTERRORCLASS, DTITestUtil.TKTERRORTEXT, DTITestUtil.TKTERRORTYPE);
    dtiRespTO.setDtiError(tktErrorTO);
    DTITransactionTO dtiTxnTO = new DTITransactionTO(DTITransactionTO.TransactionType.UPDATETICKET);
    dtiTxnTO.setResponse(dtiRespTO);

    return dtiTxnTO;

  }

}
