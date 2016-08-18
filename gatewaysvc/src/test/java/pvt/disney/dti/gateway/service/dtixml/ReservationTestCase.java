package pvt.disney.dti.gateway.service.dtixml;

import static org.junit.Assert.fail;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
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
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.ReservationResponseTO;
import pvt.disney.dti.gateway.data.common.AgencyTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.CreditCardTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.GiftCardTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.ProductTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TktStatusTO;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

import com.disney.context.ContextManager;
 
/**
 * Tests the reservation transformations.
 * 
 * @author lewit019
 */
public class ReservationTestCase extends ReservationXML {

  /**
   * Sets the up before class.
   * 
   * @throws Exception the exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  /**
   * Tear down after class.
   * 
   * @throws Exception the exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  /**
   * Sets the up.
   * 
   * @throws Exception the exception
   */
  @Before
  public void setUp() throws Exception {
  }

  /**
   * Tear down.
   * 
   * @throws Exception the exception
   */
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
    String fileName = new String("./config/Reservation/Reservation_01_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.RESERVATION;

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

    ReservationRequestTO reservationReqTO = (ReservationRequestTO) dtiTxn.getRequest()
        .getCommandBody();
    try {
      validateReservationReqTO(reservationReqTO);
    } catch (Exception e) {
      fail("Failure in validating reservation request: " + e.toString());
    }

    return;

  }

  /**
   * Test get to empty.
   */
  @Test
  public final void testGetTOEmpty() {

    // ***** Read the test file. *****
    String requestXML = null;
    String fileName = new String("./config/Reservation/Reservation_02_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.RESERVATION;

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
   * Test get to parse only.
   */
  @Test
  public final void testGetTOParseOnly() {

    // ***** Read the test file. *****
    String requestXML = null;
    String fileName = new String("./config/Reservation/Reservation_03_Req_XML.xml");

    try {
      requestXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = null;
    String tktBroker = new String(DTITestUtil.TKTBROKER);
    DTITransactionTO.TransactionType requestType = DTITransactionTO.TransactionType.RESERVATION;

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

    try {
      FileOutputStream fos = new FileOutputStream("C:\\var\\data\\ResReq.ser");
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(dtiTxn);
      oos.close();
    } catch (Exception e) {
      fail("Unable to write object to file: " + e.toString());
    }

    return;

  }

  /**
   * Validate reservation req to.
   * 
   * @param rReqTO the r req to
   * 
   * @throws Exception the exception
   */
  private static void validateReservationReqTO(ReservationRequestTO rReqTO) throws Exception {

    // RequestType
    if (rReqTO.getRequestType().compareTo(DTITestUtil.REQUESTTYPE) != 0)
      throw new Exception("Bad RequestType Ticket Value: " + rReqTO.getRequestType());

    // Ticket
    ArrayList<TicketTO> tktListTO = rReqTO.getTktList();
    if (tktListTO.size() != 2)
      throw new Exception("Invalid TktListTO.size(): " + tktListTO.size());

    for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

      BigInteger itemNumObj = aTicketTO.getTktItem();
      if (itemNumObj == null)
        throw new Exception("ItemNum is null.");
      int itemNum = itemNumObj.intValue();

      if (itemNum == 1) {

        if (aTicketTO.getProdCode().compareTo(DTITestUtil.PRODCODE) != 0)
          throw new Exception("Invalid ProdCode: " + aTicketTO.getProdCode());

        if (aTicketTO.getProdQty().compareTo(new BigInteger(DTITestUtil.PRODQTY)) != 0)
          throw new Exception("Invalid ProdQty: " + aTicketTO.getProdQty());

        if (aTicketTO.getProdPrice().compareTo(new BigDecimal(DTITestUtil.PRODPRICE)) != 0)
          throw new Exception("Invalid ProdPrice: " + aTicketTO.getProdPrice());
        
        if (aTicketTO.getExternal().compareTo(DTITestUtil.TKTEXTERNAL) != 0) 
          throw new Exception("Invalid TktID.External: " + aTicketTO.getExternal());
        
        if (aTicketTO.getTktStatusList().size() == 0) 
          throw new Exception("Invalid TktStatus (none provided).");
        
        TktStatusTO tktStatus = aTicketTO.getTktStatusList().get(0);
        if ((tktStatus.getStatusItem().compareTo(DTITestUtil.STATUSITEM)!= 0) ||
            (tktStatus.getStatusValue().compareTo(DTITestUtil.STATUSVALUE)!= 0))
          throw new Exception("Invalid StatusItem or StatusValue");

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

        if (DTITestUtil.TKTSECURITYLEVEL.compareTo(aTicketTO.getTktSecurityLevel()) != 0)
          throw new Exception("Invalid TktSecurity.");
        
        if (DTITestUtil.TKTSHELL.compareTo(aTicketTO.getTktShell())!= 0)
          throw new Exception("Invalid TktShell.");
        
        if (DTITestUtil.TKTMARKET.compareTo(aTicketTO.getTktMarket())!= 0)
          throw new Exception("Invalid TktMarket.");
        
        if (DTITestUtil.TKTNOTE.compareTo(aTicketTO.getTktNote())!= 0)
          throw new Exception("Invalid TktNote.");
        
      } else if (itemNum == 2) {

        if (aTicketTO.getProdCode().compareTo(DTITestUtil.PRODCODE) != 0)
          throw new Exception("Invalid ProdCode: " + aTicketTO.getProdCode());

        if (aTicketTO.getProdQty().compareTo(new BigInteger(DTITestUtil.PRODQTY)) != 0)
          throw new Exception("Invalid ProdQty: " + aTicketTO.getProdQty());

        if (aTicketTO.getProdPrice().compareTo(new BigDecimal(DTITestUtil.PRODPRICE)) != 0)
          throw new Exception("Invalid ProdPrice: " + aTicketTO.getProdPrice());

      } else
        throw new Exception("Invalid TktItem: " + itemNum);

    }

    // APPassInfo
    if (rReqTO.getAPPassInfo().compareTo(DTITestUtil.APPASSINFO) != 0)
      throw new Exception("Invalid APPassINfo: " + rReqTO.getAPPassInfo());

    // Payment
    ArrayList<PaymentTO> payListTO = rReqTO.getPaymentList();
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
    if (rReqTO.getEligibilityGroup().compareTo(DTITestUtil.ELIGGROUP) != 0)
      throw new Exception("Invalid Eligibility.Group: " + rReqTO.getEligibilityGroup());
    if (rReqTO.getEligibilityMember().compareTo(DTITestUtil.ELIGMEMBER) != 0)
      throw new Exception("Invalid Eligibility.Member: " + rReqTO.getEligibilityMember());

    // Reservation
    ReservationTO resTO = rReqTO.getReservation();
    if (resTO == null)
      throw new Exception("Missing Reservation section.");
    if (resTO.getResCode().compareTo(DTITestUtil.RESCODE)!= 0)
      throw new Exception("Invalid ResCode.");
    if (resTO.getResNumber().compareTo(DTITestUtil.RESNUMBER)!=0)
      throw new Exception("Invalid ResNumber.");
    GregorianCalendar gCalTestDate = CustomDataTypeConverter
    .parseYYYYMMDDDate(DTITestUtil.RESCREATEDATE);
    if (resTO.getResCreateDate().compareTo(gCalTestDate) != 0)
      throw new Exception("Invalid ResCreateDate.");
    gCalTestDate = CustomDataTypeConverter
    .parseYYYYMMDDDate(DTITestUtil.RESPICKUPDATE);
    if (resTO.getResPickupDate().compareTo(gCalTestDate)!= 0)
      throw new Exception("Invalid ResPickupDate.");
    if (resTO.getResPickupArea().compareTo(DTITestUtil.RESPICKUPAREA) != 0)
      throw new Exception("Invalid ResPickupArea.");
    if (resTO.getResSalesType().compareTo(DTITestUtil.RESSALESTYPE)!= 0)
      throw new Exception("Invalid ResSalesType.");
    
    
    // ClientData
    ClientDataTO cl = rReqTO.getClientData();
    if (cl.getClientId().compareTo(DTITestUtil.CLIENTID) !=0)
      throw new Exception("Invalid ClientId.");
    if (cl.getClientType().compareTo(DTITestUtil.CLIENTTYPE) != 0)
      throw new Exception("Invalid ClientType: " + cl.getClientType());
    if (cl.getClientCategory().compareTo(DTITestUtil.CLIENTCATEGORY) != 0)
      throw new Exception("Invalid ClientCategory: " + cl.getClientCategory());
    if (cl.getDemoLanguage().compareTo(DTITestUtil.DEMOLANGUAGE) != 0)
      throw new Exception("Invalid DemoLanguage: " + cl.getDemoLanguage());
    
    // Bill Info
    DemographicsTO bill = cl.getBillingInfo();
    if (bill.getAddr1().compareTo(DTITestUtil.BILLADDR1) != 0)
      throw new Exception("Invalid Bill Addr1: " + bill.getAddr1());
    if (bill.getAddr2().compareTo(DTITestUtil.BILLADDR2) != 0)
      throw new Exception("Invalid Bill Addr2: " + bill.getAddr2());
    if (bill.getCity().compareTo(DTITestUtil.BILLCITY) != 0)
      throw new Exception("Invalid Bill City: " + bill.getCity());
    if (bill.getCountry().compareTo(DTITestUtil.BILLCOUNTRY) != 0)
      throw new Exception("Invalid Bill Country: " + bill.getCountry());
    if (bill.getEmail().compareTo(DTITestUtil.BILLEMAIL) != 0)
      throw new Exception("Invalid Bill Email: " + bill.getEmail());
    if (bill.getFirstName().compareTo(DTITestUtil.BILLFIRSTNAME) != 0)
      throw new Exception("Invalid Bill FirstName: " + bill.getFirstName());
    if (bill.getLastName().compareTo(DTITestUtil.BILLLASTNAME) != 0)
      throw new Exception("Invalid Bill LastName: " + bill.getLastName());
    if (bill.getName().compareTo(DTITestUtil.BILLNAME) != 0)
      throw new Exception("Invalid Bill Name: " + bill.getName());
    if (bill.getState().compareTo(DTITestUtil.BILLSTATE) != 0)
      throw new Exception("Invalid Bill State: " + bill.getState());
    if (bill.getTelephone().compareTo(DTITestUtil.BILLTELEPHONE) != 0)
      throw new Exception("Invalid Bill Telephone: " + bill.getTelephone());
    if (bill.getZip().compareTo(DTITestUtil.BILLZIP) != 0)
      throw new Exception("Invalid Bill Zip: " + bill.getZip());
    // Ship Info
    DemographicsTO ship = cl.getShippingInfo();
    if (ship.getAddr1().compareTo(DTITestUtil.SHIPADDR1) != 0)
      throw new Exception("Invalid Ship Addr1: " + ship.getAddr1());
    if (ship.getAddr2().compareTo(DTITestUtil.SHIPADDR2) != 0)
      throw new Exception("Invalid Ship Addr2: " + ship.getAddr2());
    if (ship.getCity().compareTo(DTITestUtil.SHIPCITY) != 0)
      throw new Exception("Invalid Ship City: " + ship.getCity());
    if (ship.getCountry().compareTo(DTITestUtil.SHIPCOUNTRY) != 0)
      throw new Exception("Invalid Ship Country: " + ship.getCountry());
    if (ship.getEmail().compareTo(DTITestUtil.SHIPEMAIL) != 0)
      throw new Exception("Invalid Ship Email: " + ship.getEmail());
    if (ship.getFirstName().compareTo(DTITestUtil.SHIPFIRSTNAME) != 0)
      throw new Exception("Invalid Ship FirstName: " + ship.getFirstName());
    if (ship.getLastName().compareTo(DTITestUtil.SHIPLASTNAME) != 0)
      throw new Exception("Invalid Ship LastName: " + ship.getLastName());
    if (ship.getName().compareTo(DTITestUtil.SHIPNAME) != 0)
      throw new Exception("Invalid Ship Name: " + ship.getName());
    if (ship.getState().compareTo(DTITestUtil.SHIPSTATE) != 0)
      throw new Exception("Invalid Ship State: " + ship.getState());
    if (ship.getTelephone().compareTo(DTITestUtil.SHIPTELEPHONE) != 0)
      throw new Exception("Invalid Ship Telephone: " + ship.getTelephone());
    if (ship.getZip().compareTo(DTITestUtil.SHIPZIP) != 0)
      throw new Exception("Invalid Ship Zip: " + ship.getZip());

    // Agency
    AgencyTO agencyTO = rReqTO.getAgency();
    if (agencyTO.getIATA().compareTo(DTITestUtil.IATA) != 0)
      throw new Exception("Invalid Agency.IATA: " + agencyTO.getIATA());
    if (agencyTO.getAgent().compareTo(DTITestUtil.AGENT) != 0)
      throw new Exception("Invalid Agency.Agent: " + agencyTO.getAgent());

    // TaxExemptCode
    String taxExemptCode = rReqTO.getTaxExemptCode();
    if (taxExemptCode.compareTo(DTITestUtil.TAXEXEMPTCODE) != 0)
      throw new Exception("Invalid TaxExemptCode: " + taxExemptCode);
    
    // Note
    ArrayList<String> noteList = rReqTO.getNoteList();
    if (noteList.size() != 3)
      throw new Exception("Invalid length of note list array: " + noteList.size());
    for /* each */(String aNote : /* in */noteList) {

      if (aNote.compareTo(DTITestUtil.NOTE) != 0)
        throw new Exception("Invalid Note: " + aNote);
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
    String fileName = new String("./config/Reservation/Reservation_01_Rsp_XML.xml");

    try {
      baselineXML = DTITestUtilities.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    DTITransactionTO dtiTxn = createReservationRespTO();
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

    // Validate Reservation Response
    String resRespBaseline = DTITestUtilities.findTag(baselineXML, "ReservationResponse");
    String resRespNew = DTITestUtilities.findTag(newXML, "ReservationResponse");

    try {
    	DTITestUtilities.compareXML(resRespBaseline, resRespNew, "ReservationResponse");
    } catch (Exception e) {
      fail("Exception validating ReservationResponse section: " + e.toString());
    }

  }

  /**
   * Creates the reservation resp to.
   * 
   * @return the dTI transaction to
   */
  private static DTITransactionTO createReservationRespTO() {

    DTIResponseTO dtiRespTO = new DTIResponseTO();

    // Payload Header
    PayloadHeaderTO payHdrTO = DTITestUtil.createTestPayHeaderTO();
    dtiRespTO.setPayloadHeader(payHdrTO);

    // Command Header
    CommandHeaderTO cmdHdrTO = DTITestUtil.createTestCmdHeaderTO();
    dtiRespTO.setCommandHeader(cmdHdrTO);

    // ReservationResponse
    ReservationResponseTO rsRespTO = new ReservationResponseTO();
    rsRespTO.setResponseType(DTITestUtil.RESPONSETYPE);

    // Ticket 1
    TicketTO aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("1"));
    aTicketTO.setMag(DTITestUtil.TKTMAGTRACK1);
    aTicketTO.setBarCode(DTITestUtil.TKTBARCODE);
    GregorianCalendar dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTDSSNDATE);
    aTicketTO.setDssn(dssnDate, DTITestUtil.TKTDSSNSITE, DTITestUtil.TKTDSSNSTATION, DTITestUtil.TKTDSSNNBR);
    aTicketTO.setTktNID(DTITestUtil.TKTNID);
    aTicketTO.setExternal(DTITestUtil.TKTEXTERNAL);
    aTicketTO.setTktPrice(new BigDecimal(DTITestUtil.TKTPRICE));
    aTicketTO.setTktTax(new BigDecimal(DTITestUtil.TKTTAX));
    
    TicketTransactionTO tktTranIn = new TicketTransactionTO();
    tktTranIn.setTktProvider(DTITestUtil.TKTPROVIDER);
    dssnDate = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTTRANDATE);
    tktTranIn.setDssn(dssnDate, DTITestUtil.TKTTRANSITE, DTITestUtil.TKTTRANSTATION,
        DTITestUtil.TKTTRANNBR);
    tktTranIn.setTranNID(DTITestUtil.TKTTRANNID);
    aTicketTO.setTktTran(tktTranIn);
    
    TktStatusTO tktStatus = aTicketTO.new TktStatusTO();
    tktStatus.setStatusItem(DTITestUtil.STATUSITEM);
    tktStatus.setStatusValue(DTITestUtil.STATUSVALUE);
    aTicketTO.addTicketStatus(tktStatus);
    
    GregorianCalendar validStart = CustomDataTypeConverter
        .parseYYYYMMDDDate(DTITestUtil.TKTVALIDSTART);
    aTicketTO.setTktValidityValidStart(validStart);
    GregorianCalendar validEnd = CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.TKTVALIDEND);
    aTicketTO.setTktValidityValidEnd(validEnd);
    aTicketTO.setTktNote(DTITestUtil.TKTNOTE);
    rsRespTO.addTicket(aTicketTO);

    // Ticket 2
    aTicketTO = new TicketTO();
    aTicketTO.setTktItem(new BigInteger("2"));
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
    rsRespTO.addTicket(aTicketTO);
    
    // Product 1
    ProductTO aProduct = new ProductTO();
    aProduct.setProdItem(new BigInteger("1"));
    aProduct.setProdCode(DTITestUtil.PRODCODE);
    aProduct.setProdQty(new BigInteger(DTITestUtil.PRODQTY));
    aProduct.setProdTax1(new BigDecimal(DTITestUtil.PRODTAX1));
    aProduct.setProdTax2(new BigDecimal(DTITestUtil.PRODTAX2));
    aProduct.setProdPrice(new BigDecimal(DTITestUtil.PRODPRICE));
    aProduct.setProdParts(DTITestUtil.PRODPARTS);
    aProduct.setProdDescription(DTITestUtil.PRODDESC);
    aProduct.setProdReceiptMsg(DTITestUtil.PRODRECEIPTMSG);
    rsRespTO.addProduct(aProduct);

    // Payment 1
    PaymentTO aPaymentTO = new PaymentTO();
    aPaymentTO.setPayItem(new BigInteger("1"));
    CreditCardTO cc = new CreditCardTO();
    cc.setCcAuthCode(DTITestUtil.CCAUTHCODE);
    cc.setCcAuthNumber(DTITestUtil.CCAUTHNUMBER);
    cc.setCcAuthSysResponse(DTITestUtil.CCAUTHSYSRESPONSE);
    cc.setCcNumber(DTITestUtil.CCNUMBER);
    aPaymentTO.setCreditCard(cc);
    rsRespTO.addPayment(aPaymentTO);

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
    rsRespTO.addPayment(aPaymentTO);

    // Receipt Message
    rsRespTO.setReceiptMessage(DTITestUtil.RECEIPTMESSAGE);

    // Reservation
    ReservationTO rs = new ReservationTO();
    rs.setResCode(DTITestUtil.RESCODE);
    rs.setResNumber(DTITestUtil.RESNUMBER);
    rs.setResCreateDate(CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.RESCREATEDATE));
    rs.setResPickupDate(CustomDataTypeConverter.parseYYYYMMDDDate(DTITestUtil.RESPICKUPDATE));
    rs.setResPickupArea(DTITestUtil.RESPICKUPAREA);
    rs.setResSalesType(DTITestUtil.RESSALESTYPE);
    rsRespTO.setReservation(rs);

    // ClientData
    ClientDataTO cd = new ClientDataTO();
    cd.setClientId(DTITestUtil.CLIENTID);
    rsRespTO.setClientData(cd);

    dtiRespTO.setCommandBody(rsRespTO);

    // Complete the response
    DTIErrorTO tktErrorTO = new DTIErrorTO(new BigInteger(DTITestUtil.TKTERRORCODE), DTITestUtil.TKTERRORCLASS,
        DTITestUtil.TKTERRORTEXT, DTITestUtil.TKTERRORTYPE);
    dtiRespTO.setDtiError(tktErrorTO);
    DTITransactionTO dtiTxnTO = new DTITransactionTO(DTITransactionTO.TransactionType.RESERVATION);
    dtiTxnTO.setResponse(dtiRespTO);

    return dtiTxnTO;
  }

}
