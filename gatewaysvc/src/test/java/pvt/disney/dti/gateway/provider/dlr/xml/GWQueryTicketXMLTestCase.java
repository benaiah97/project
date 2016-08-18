package pvt.disney.dti.gateway.provider.dlr.xml;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketErrorsTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;
import pvt.disney.dti.gateway.util.UtilityXML;

/**
 * This class is responsible for validating the transformations for the gateway query ticket XML.
 * @author lewit019
 *
 */
public class GWQueryTicketXMLTestCase {

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
   * Test get xml.
   */
  @Test
  public final void testGetXML() {
    // ***** Read the test file. *****
    String baselineXML = null;
    String fileName = new String("./config/QueryTicket/QueryTicket_01_Req_DLR.xml");

    try {
      baselineXML = DLRTestUtil.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    GWEnvelopeTO dtiTxn = createQueryTicketReqTO();
    String newXML = null;

    try {
      newXML = GWEnvelopeXML.getXML(dtiTxn);
    } catch (DTIException dtie) {
      fail("DTIException creating the gateway request XML: " + dtie.toString());
    }

    // Validate the XML Headers (Payload & Command)
    try {
      DLRTestUtil.validateXMLHeaders(baselineXML, newXML);
    } catch (Exception e) {
      fail("Exception validating gateway request XML headers: " + e.toString());
    }

    // Validate Query Ticket Response
    String qtRespBaseline = DLRTestUtil.findTag(baselineXML, "Body");
    String qtRespNew = DLRTestUtil.findTag(newXML, "Body");

    try {
      DLRTestUtil.compareXML(qtRespBaseline, qtRespNew, "QueryTicket");
    } catch (Exception e) {
      fail("Exception validating QueryTicket section: " + e.toString());
    }

    return;
  }

  /**
   * Test get to.
   */
  @Test
  public final void testGetTO() {

    // ***** Read the test file. *****
    String inputXML = null;
    String fileName = new String("./config/QueryTicket/QueryTicket_01_Rsp_DLR.xml");

    try {
      inputXML = DLRTestUtil.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    GWEnvelopeTO parsedEnvTO = null;

    try {
      parsedEnvTO = GWEnvelopeXML.getTO(inputXML);
    } catch (DTIException dtie) {
      fail("DTIException parsing the gateway response XML: " + dtie.toString());
    }

    try {
      validateQueryTicketRespTO(parsedEnvTO);
    } catch (Exception e) {
      fail("Exception parsing the gateway response XML: " + e.toString());
    }

  }

  /**
   * Test get to error.
   */
  @Test
  public final void testGetTOError() {

    // ***** Read the test file. *****
    String inputXML = null;
    String fileName = new String("./config/QueryTicket/QueryTicket_02_Rsp_DLR.xml");

    try {
      inputXML = DLRTestUtil.getXMLfromFile(fileName);
    } catch (Exception e) {
      fail("Unable to read test file: " + fileName);
    }

    GWEnvelopeTO parsedEnvTO = null;

    try {
      parsedEnvTO = GWEnvelopeXML.getTO(inputXML);
    } catch (DTIException dtie) {
      fail("DTIException parsing the gateway response XML: " + dtie.toString());
    }

    try {
      validateQueryTicketRespTOError(parsedEnvTO);
    } catch (Exception e) {
      fail("Exception parsing the gateway response XML: " + e.toString());
    }

  }

  /**
   * Creates the query ticket req to.
   * 
   * @return the gW envelope to
   */
  private static GWEnvelopeTO createQueryTicketReqTO() {

    GWEnvelopeTO gwEnvTO = new GWEnvelopeTO(GWEnvelopeTO.GWTransactionType.QUERYTICKET);

    // Header
    GWHeaderTO gwHdrTO = new GWHeaderTO();
    gwHdrTO.setSourceID(DLRTestUtil.SOURCEID);
    gwHdrTO.setMessageID(DLRTestUtil.MESSAGEID);
    GregorianCalendar gCal = CustomDataTypeConverter.parseDateTimeStamp(DLRTestUtil.TIMESTAMPREQ);
    gwHdrTO.setTimeStamp(gCal);
    gwHdrTO.setMessageType(DLRTestUtil.MESSAGETYPE_QRY);
    gwEnvTO.setHeaderTO(gwHdrTO);

    // Body
    GWBodyTO gwBodyTO = new GWBodyTO();

    // GWQueryTicketRqstTO
    GWQueryTicketRqstTO gwQryTktTO = new GWQueryTicketRqstTO();
    gwQryTktTO.setVisualID(DLRTestUtil.VISUALID);
    gwBodyTO.setQueryTicketRqstTO(gwQryTktTO);
    gwEnvTO.setBodyTO(gwBodyTO);

    return gwEnvTO;

  }

  /**
   * Validate query ticket resp to.
   * 
   * @param parsedEnvTO the parsed env to
   * 
   * @throws Exception the exception
   */
  private static void validateQueryTicketRespTO(GWEnvelopeTO parsedEnvTO) throws Exception {

    GWHeaderTO gwHdrTO = parsedEnvTO.getHeaderTO();

    validateRespHeaderTO(gwHdrTO);

    // Body
    GWBodyTO gwBodyTO = parsedEnvTO.getBodyTO();
    if (gwBodyTO == null) {
      throw new Exception("Null Body in parsed transfer object.");
    }

    // Status
    GWStatusTO gwStatusTO = gwBodyTO.getStatusTO();
    if (gwStatusTO == null) {
      throw new Exception("Null Body/Status in parsed transfer object.");
    }

    String statusCode = gwStatusTO.getStatusCode();
    if (statusCode == null)
      throw new Exception("Null Body/Status/StatusCode in parsed transfer object.");
    if (statusCode.compareTo(DLRTestUtil.STATUSCODEOK) != 0)
      throw new Exception("Unexpected value of " + statusCode + " in Body/Status/StatusCode.");

    String statusText = gwStatusTO.getStatusText();
    if (statusText == null)
      throw new Exception("Null Body/Status/statusText in parsed transfer object.");
    if (statusText.compareTo(DLRTestUtil.STATUSTEXTOK) != 0)
      throw new Exception("Unexpected value of " + statusText + " in Body/Status/StatusCode.");

    // Query Ticket Resp
    GWQueryTicketRespTO qtRespTO = gwBodyTO.getQueryTicketResponseTO();
    if (qtRespTO == null)
      throw new Exception("Null Body/QueryTicketResponse in parsed transfer object.");

    // DataRequestResponse
    GWDataRequestRespTO dataRespTO = qtRespTO.getDataRespTO();
    if (dataRespTO == null)
      throw new Exception("Null QueryTicketResponse/DataRequestResponse in parsed transfer object.");

    // DateOpened
    GregorianCalendar dateOpen = dataRespTO.getDateOpened();
    if (dateOpen == null)
      throw new Exception("Null DateOpened in parsed transfer object.");
    GregorianCalendar dateOpenBase = UtilityXML.getGCalFromEGalaxyDate(DLRTestUtil.DATEOPENED);
    if (dateOpen.compareTo(dateOpenBase) != 0)
      throw new Exception("Unexpected value of " + dateOpen.toString()
          + " in DataRequestResponse/DateOpened.");

    // ItemKind
    GWDataRequestRespTO.ItemKind itemKind = dataRespTO.getItemKind();
    if (itemKind == null)
      throw new Exception("Null DataRequestResponse/ItemKind in parsed transfer object.");
    if (itemKind != DLRTestUtil.ITEMKIND)
      throw new Exception("Unexpected value of " + itemKind + " in DataRequestResponse/ItemKind.");

    // LockedOut
    Boolean lockedOut = dataRespTO.getLockedOut();
    if (lockedOut == null)
      throw new Exception("Null DataRequestResponse/LockedOut in parsed transfer object.");
    if (lockedOut.compareTo(DLRTestUtil.LOCKEDOUT) != 0)
      throw new Exception("Unexpected value of " + lockedOut + " in DataRequestResponse/LockedOut.");

    // Price
    BigDecimal price = dataRespTO.getPrice();
    if (price == null)
      throw new Exception("Null DataRequestResponse/Price in parsed transfer object.");
    if (price.compareTo(DLRTestUtil.PRICE) != 0)
      throw new Exception("Unexpected value of " + price + " in DataRequestResponse/Price.");

    // Returnable
    Boolean returnable = dataRespTO.getReturnable();
    if (returnable == null)
      throw new Exception("Null DataRequestResponse/Returnable in parsed transfer object.");
    if (returnable.compareTo(DLRTestUtil.RETURNABLE) != 0)
      throw new Exception("Unexpected value of " + returnable
          + " in DataRequestResponse/Returnable.");

    // Status
    GWDataRequestRespTO.Status status = dataRespTO.getStatus();
    if (status == null)
      throw new Exception("Null DataRequestResponse/Status in parsed transfer object.");
    if (status != DLRTestUtil.STATUS)
      throw new Exception("Unexpected value of " + status + " in DataRequestResponse/Status.");

    // Tax
    BigDecimal tax = dataRespTO.getTax();
    if (tax == null)
      throw new Exception("Null DataRequestResponse/Tax in parsed transfer object.");
    if (tax.compareTo(DLRTestUtil.TAX) != 0)
      throw new Exception("Unexpected value of " + tax + " in DataRequestResponse/Tax.");

    // UseCount
    Integer useCount = dataRespTO.getUseCount();
    if (useCount == null)
      throw new Exception("Null DataRequestResponse/UseCount in parsed transfer object.");
    if (useCount.compareTo(DLRTestUtil.USECOUNT) != 0)
      throw new Exception("Unexpected value of " + useCount + " in DataRequestResponse/UseCount.");

    // ValidUntil
    GregorianCalendar validUntil = dataRespTO.getValidUntil();
    if (validUntil == null)
      throw new Exception("Null DataRequestResponse/ValidUntil in parsed transfer object.");
    GregorianCalendar gCalBase = UtilityXML.getGCalFromEGalaxyDate(DLRTestUtil.VALIDUNTIL);
    if (validUntil.compareTo(gCalBase) != 0)
      throw new Exception("Unexpected value of " + validUntil.toString()
          + " in DataRequestResponse/ValidUntil.");

    // VisualID
    String visualId = dataRespTO.getVisualID();
    if (visualId == null)
      throw new Exception("Null DataRequestResponse/VisualID in parsed transfer object.");
    if (visualId.compareTo(DLRTestUtil.VISUALID) != 0)
      throw new Exception("Unexpected value of " + visualId + " in DataRequestResponse/VisualID.");

    // DateSold
    GregorianCalendar dateSold = dataRespTO.getDateSold();
    if (dateSold == null)
      throw new Exception("Null DateSold in parsed transfer object.");
    GregorianCalendar dateSoldBase = UtilityXML.getGCalFromEGalaxyDate(DLRTestUtil.DATESOLD);
    if (dateSold.compareTo(dateSoldBase) != 0)
      throw new Exception("Unexpected value of " + dateSold.toString()
          + " in DataRequestResponse/DateSold.");

    // Awaiting test values
    // dataRespTO.getTicketDate();
    // dataRespTO.getStartDateTime();
    // dataRespTO.getExpirationDate();
    // dataRespTO.getEndDateTime();

    return;
  }

  /**
   * Validate query ticket resp to error.
   * 
   * @param parsedEnvTO the parsed env to
   * 
   * @throws Exception the exception
   */
  private static void validateQueryTicketRespTOError(GWEnvelopeTO parsedEnvTO) throws Exception {

    GWHeaderTO gwHdrTO = parsedEnvTO.getHeaderTO();

    validateRespHeaderTO(gwHdrTO);

    // Body
    GWBodyTO gwBodyTO = parsedEnvTO.getBodyTO();
    if (gwBodyTO == null) {
      throw new Exception("Null Body in parsed transfer object.");
    }

    // Status
    GWStatusTO gwStatusTO = gwBodyTO.getStatusTO();
    if (gwStatusTO == null) {
      throw new Exception("Null Body/Status in parsed transfer object.");
    }

    String statusCode = gwStatusTO.getStatusCode();
    if (statusCode == null)
      throw new Exception("Null Body/Status/StatusCode in parsed transfer object.");
    if (statusCode.compareTo(DLRTestUtil.STATUSCODEERR) != 0)
      throw new Exception("Unexpected value of " + statusCode + " in Body/Status/StatusCode.");

    String statusText = gwStatusTO.getStatusText();
    if (statusText == null)
      throw new Exception("Null Body/Status/statusText in parsed transfer object.");
    if (statusText.compareTo(DLRTestUtil.STATUSTEXTERR) != 0)
      throw new Exception("Unexpected value of " + statusText + " in Body/Status/StatusCode.");

    // QueryTicketErrors
    GWQueryTicketErrorsTO qtErrTO = gwBodyTO.getQueryTicketErrorsTO();
    if (qtErrTO == null)
      throw new Exception("Null Body/QueryTicketErrors in parsed transfer object.");

    // Errors
    String errorCode = qtErrTO.getErrorCode();
    if (errorCode == null)
      throw new Exception("Null Error/ErrorCode in parsed transfer object.");
    if (errorCode.compareTo(DLRTestUtil.ERRORCODE) != 0)
      throw new Exception("Unexpected value of " + errorCode + " in Error/ErrorCode.");

    String errorText = qtErrTO.getErrorText();
    if (errorText == null)
      throw new Exception("Null Error/ErrorText in parsed transfer object.");
    if (errorText.compareTo(DLRTestUtil.ERRORTEXT) != 0)
      throw new Exception("Unexpected value of " + errorText + " in Error/ErrorText.");

    return;
  }

  /**
   * Validate resp header to.
   * 
   * @param gwHdrTO the gw hdr to
   * 
   * @throws Exception the exception
   */
  private static void validateRespHeaderTO(GWHeaderTO gwHdrTO) throws Exception {
    if (gwHdrTO == null) {
      throw new Exception("Null Header in parsed transfer object.");
    }

    // MessageID
    BigInteger messageId = gwHdrTO.getMessageID();
    if (messageId == null)
      throw new Exception("Null MessageID on parsed XML.");
    if (messageId.compareTo(DLRTestUtil.MESSAGEID) != 0)
      throw new Exception("Unexpected value of " + messageId.toString() + " in Header/MessageID");

    // MessageType
    String msgType = gwHdrTO.getMessageType();
    if (msgType == null)
      throw new Exception("Null MessageType in parsed transfer object.");
    if (msgType.compareTo(DLRTestUtil.MESSAGETYPE_QRYRSP) != 0)
      throw new Exception("Unexpected value of " + msgType + " in Header/MessageType.");

    // SourceID
    String sourceId = gwHdrTO.getSourceID();
    if (sourceId == null)
      throw new Exception("Null SourceID on parsed XML.");
    if (sourceId.compareTo(DLRTestUtil.SOURCEID) != 0)
      throw new Exception("Unexpected value of " + sourceId + " in Header/SourceID.");

    // TimeStamp
    GregorianCalendar gCalTO = gwHdrTO.getTimeStamp();
    if (gCalTO == null)
      throw new Exception("Null TimeStamp in parsed transfer object.");
    GregorianCalendar gCalBase = UtilityXML.getGCalFromEGalaxyDate(DLRTestUtil.TIMESTAMPRSP);
    if (gCalTO.compareTo(gCalBase) != 0)
      throw new Exception("Unexpected value of " + gCalTO.toString() + " in Header/TimeStamp.");

    return;
  }

}
