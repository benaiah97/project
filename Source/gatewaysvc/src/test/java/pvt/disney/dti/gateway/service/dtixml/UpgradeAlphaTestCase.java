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
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaResponseTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

import com.disney.context.ContextManager;

/**
 * Tests the upgrade alpha transformations.
 * 
 * @author lewit019
 *
 */
public class UpgradeAlphaTestCase extends UpgradeAlphaXML {

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
    String fileName = new String("./config/UpgradeAlpha/UpgradeAlpha_01_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.UPGRADEALPHA;

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

    UpgradeAlphaRequestTO upgradeAlphaReqTO = (UpgradeAlphaRequestTO) dtiTxn.getRequest()
        .getCommandBody();
    try {
      validateUpgradeAlphaReqTO(upgradeAlphaReqTO);
    } catch (Exception e) {
      fail("Failure in validating upgrade alpha request: " + e.toString());
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
    String fileName = new String("./config/UpgradeAlpha/UpgradeAlpha_02_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.UPGRADEALPHA;

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

    /* Other tests validate that the fields get to the right places in the TO.
     * This test just validates that parsing can occur. 
     */

    return;

  }


  /**
   * Test get jaxb.
   */
  @Test
  public final void testGetJaxb() {
    // ***** Read the test file. *****
    String baselineXML = null;
    String fileName = new String("./config/UpgradeAlpha/UpgradeAlpha_01_Rsp_XML.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = createUpgradeAlphaRespTO();
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

    // Validate Upgrade Alpha Response
    String uaRespBaseline = DTITestUtilities.findTag(baselineXML, "UpgradeAlphaResponse");
    String uaRespNew = DTITestUtilities.findTag(newXML, "UpgradeAlphaResponse");

    try {
    	DTITestUtilities.compareXML(uaRespBaseline, uaRespNew, "UpgradeAlphaResponse");
    } catch (Exception e) {
      fail("Exception validating UpgradeAlphaResponse section: " + e.toString());
    }

  }

  /**
   * Validate upgrade alpha req to.
   * 
   * @param uaReqTO the ua req to
   * 
   * @throws Exception the exception
   */
  private static void validateUpgradeAlphaReqTO(UpgradeAlphaRequestTO uaReqTO) throws Exception {

    ArrayList<TicketTO> tktListTO = uaReqTO.getTktList();
    if (tktListTO.size() != 6)
      throw new Exception("Invalid TktListTO.size(): " + tktListTO.size());

    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      BigInteger itemNumObj = aTicketTO.getTktItem();
      if (itemNumObj == null)
        throw new Exception("ItemNum is null.");
      int itemNum = itemNumObj.intValue();

      // Check fields common to all tickets.
      if (aTicketTO.getProdCode().compareTo(DTITestUtil.PRODCODE) != 0)
        throw new Exception("Invalid Ticket.ProdCode: " + aTicketTO.getProdCode());
      if (aTicketTO.getProdPrice().compareTo(new BigDecimal(DTITestUtil.PRODPRICE)) != 0)
        throw new Exception("Invalid Ticket.ProdPrice: " + aTicketTO.getProdPrice());
      if (aTicketTO.getTktShell().compareTo(DTITestUtil.TKTSHELL) != 0)
        throw new Exception("Invalid Ticket.TktShell: " + aTicketTO.getTktShell());

      // Check the individual tickets
      if (itemNum == 1) { // Dssn

        DTITestUtil.validateDSSNTktId(aTicketTO);
        
        ArrayList<TktStatusTO> tktStatusList = aTicketTO.getTktStatusList();
        if (tktStatusList.size() == 0)
          throw new Exception("TktStatus list was empty.");
        TktStatusTO tktStat = tktStatusList.get(0);
        if (tktStat.getStatusItem().compareTo(DTITestUtil.STATUSITEM)!= 0)
          throw new Exception("Invalid TktStatus.StatusItem.");
        if (tktStat.getStatusValue().compareTo(DTITestUtil.STATUSVALUE)!= 0)
          throw new Exception("Invalid TktStatus.StatusValue.");  
        
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

      } else if (itemNum == 2) { // Mag Track

        DTITestUtil.validateTwoMagTktId(aTicketTO);

      } else if (itemNum == 3) { // Barcode

        DTITestUtil.validateBarcodeTktId(aTicketTO);

      } else if (itemNum == 4) { // TktNID

        DTITestUtil.validateTktNIDTktId(aTicketTO);

      } else if (itemNum == 5) { // External

        DTITestUtil.validateExternalTktId(aTicketTO);

      } else if (itemNum == 6) { // Mag Track (1)

        DTITestUtil.validateOneMagTktId(aTicketTO);

      } else {
        throw new Exception("Invalid TktItem: " + itemNum);
      }

    }

    // Payment
    ArrayList<PaymentTO> payListTO = uaReqTO.getPaymentList();
    if (payListTO.size() != 5)
      throw new Exception("Invalid PayListTO.size(): " + tktListTO.size());

    for /* each */(PaymentTO aPaymentTO : /* in */payListTO) {

      BigInteger itemNumObj = aPaymentTO.getPayItem();
      if (itemNumObj == null)
        throw new Exception("ItemNum is null.");

      int itemNum = itemNumObj.intValue();

      if (itemNum == 1) { // CCManual

        DTITestUtil.validateCCManualPayment(aPaymentTO);

        if (aPaymentTO.getCreditCard().getCcType().compareTo(DTITestUtil.CCTYPE) != 0)
          throw new Exception("Invalid CCType: " + aPaymentTO.getCreditCard().getCcType());

      } else if (itemNum == 2) { // CCSwipe

        DTITestUtil.validateCCSwipePayment(aPaymentTO);

      } else if (itemNum == 3) { // Voucher

        DTITestUtil.validateVoucherPayment(aPaymentTO);

      } else if (itemNum == 4) { // Gift Card Manual

        DTITestUtil.validateGCManualPayment(aPaymentTO);

      } else if (itemNum == 5) { // Gift Card Swipe

        DTITestUtil.validateGCSwipePayment(aPaymentTO);

      } else
        throw new Exception("Invalid PayItem: " + itemNum);
    }

    // Eligibility
    if (uaReqTO.getEligibilityGroup().compareTo(DTITestUtil.ELIGGROUP) != 0)
      throw new Exception("Invalid Eligibility.Group: " + uaReqTO.getEligibilityGroup());
    if (uaReqTO.getEligibilityMember().compareTo(DTITestUtil.ELIGMEMBER) != 0)
      throw new Exception("Invalid Eligibility.Member: " + uaReqTO.getEligibilityMember());

    return;
  }

  /**
   * Creates the upgrade alpha resp to.
   * 
   * @return the dTI transaction to
   */
  private static DTITransactionTO createUpgradeAlphaRespTO() {
    DTIResponseTO dtiRespTO = new DTIResponseTO();

    // Payload Header
    PayloadHeaderTO payHdrTO = DTITestUtil.createTestPayHeaderTO();
    dtiRespTO.setPayloadHeader(payHdrTO);

    // Command Header
    CommandHeaderTO cmdHdrTO = DTITestUtil.createTestCmdHeaderTO();
    dtiRespTO.setCommandHeader(cmdHdrTO);

    // Upgrade Alpha Response
    UpgradeAlphaResponseTO uaRespTO = new UpgradeAlphaResponseTO();

    // Ticket 1
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("1"));
    aTicketTO.setMag(DTITestUtil.TKTMAGTRACK1);
    aTicketTO.setBarCode(DTITestUtil.TKTBARCODE);
    GregorianCalendar dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTDSSNDATE);
    aTicketTO.setDssn(dssnDate, DTITestUtil.TKTDSSNSITE, DTITestUtil.TKTDSSNSTATION, DTITestUtil.TKTDSSNNBR);
    aTicketTO.setTktNID(DTITestUtil.TKTNID);

    TicketTransactionTO aTranTO = new TicketTransactionTO();
    aTranTO.setTktProvider(DTITestUtil.TKTPROVIDER);
    dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTTRANDATE);
    aTranTO.setDssn(dssnDate, DTITestUtil.TKTTRANSITE, DTITestUtil.TKTTRANSTATION, DTITestUtil.TKTTRANNBR);
    aTranTO.setTranNID(DTITestUtil.TKTTRANNID);
    aTicketTO.setTktTran(aTranTO);

    uaRespTO.addTicket(aTicketTO);

    // Ticket 2
    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("2"));
    aTicketTO.setExternal(DTITestUtil.TKTEXTERNAL);

    aTranTO = new TicketTransactionTO();
    dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTTRANDATE);
    aTranTO.setDssn(dssnDate, DTITestUtil.TKTTRANSITE, DTITestUtil.TKTTRANSTATION, DTITestUtil.TKTTRANNBR);
    aTranTO.setTranNID(DTITestUtil.TKTTRANNID);
    aTicketTO.setTktTran(aTranTO);

    uaRespTO.addTicket(aTicketTO);

    // Product
    ProductTO aProductTO = new ProductTO();
    aProductTO.setProdItem(new BigInteger("1"));
    aProductTO.setProdCode(DTITestUtil.PRODCODE);
    aProductTO.setProdQty(new BigInteger(DTITestUtil.PRODQTY));
    aProductTO.setProdParts(DTITestUtil.PRODPARTS);
    aProductTO.setProdPrice(new BigDecimal(DTITestUtil.PRODPRICE));
    aProductTO.setProdTax1(new BigDecimal(DTITestUtil.PRODTAX1));
    aProductTO.setProdReceiptMsg(DTITestUtil.PRODRECEIPTMSG);
    uaRespTO.addProduct(aProductTO);

    dtiRespTO.setCommandBody(uaRespTO);
    DTIErrorTO tktErrorTO = new DTIErrorTO(new BigInteger(DTITestUtil.TKTERRORCODE), DTITestUtil.TKTERRORCLASS,
        DTITestUtil.TKTERRORTEXT, DTITestUtil.TKTERRORTYPE);
    dtiRespTO.setDtiError(tktErrorTO);
    
  
    // Payment 1
    PaymentTO aPaymentTO = new PaymentTO();
    aPaymentTO.setPayItem(new BigInteger("1"));
    CreditCardTO cc = new CreditCardTO();
    cc.setCcAuthCode(DTITestUtil.CCAUTHCODE);
    cc.setCcAuthNumber(DTITestUtil.CCAUTHNUMBER);
    cc.setCcAuthSysResponse(DTITestUtil.CCAUTHSYSRESPONSE);
    cc.setCcNumber(DTITestUtil.CCNUMBER);
    aPaymentTO.setCreditCard(cc);
    uaRespTO.addPayment(aPaymentTO);

    // Payment 2
    aPaymentTO = new PaymentTO();
    aPaymentTO.setPayItem(new BigInteger("2"));
    GiftCardTO gc = new GiftCardTO();
    gc.setGcAuthCode(DTITestUtil.GCAUTHCODE);
    gc.setGcAuthNumber(DTITestUtil.GCAUTHNUMBER);
    gc.setGcAuthSysResponse(DTITestUtil.GCAUTHSYSRESPONSE);
    gc.setGcNumber(DTITestUtil.GCNUMBER);
    gc.setGcRemainingBalance(new BigDecimal(DTITestUtil.GCREMAININGBALANCE));
    gc.setGcPromoExpDate(CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.GCPROMOEXPDATE));
    aPaymentTO.setGiftCard(gc);
    uaRespTO.addPayment(aPaymentTO);
    
    // Receipt Message
    uaRespTO.setReceiptMessage(DTITestUtil.RECEIPTMESSAGE);
    
    // Complete the response
    DTITransactionTO dtiTxnTO = new DTITransactionTO(DTITransactionTO.TransactionType.UPGRADEALPHA);
    dtiTxnTO.setResponse(dtiRespTO);

    return dtiTxnTO;

  }

}
