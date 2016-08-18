package pvt.disney.dti.gateway.provider.dlr.xml;

import static org.junit.Assert.fail;

import java.io.File;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWPaymentTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWTicketActivationRqstTO;
import pvt.disney.dti.gateway.rules.TransformConstants;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;
import pvt.disney.dti.gateway.util.UtilityXML;

/**
 * Junit test case for transforming to and from gateway egalaxy xml and gw
 * transfer objects.
 * 
 * @author smoon
 * 
 */
public class GWTicketActivationUAXMLTestCase {
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DLRTestUtil.VISUALID = "210000050200001352";
		DLRTestUtil.CUSTOMERID = "300010434";
		DLRTestUtil.SOURCEID = "VLINK";
		DLRTestUtil.TIMESTAMPREQ="2009-06-25 17:17:15.49";
		DLRTestUtil.MESSAGEID= new BigInteger("12345678");
		DLRTestUtil.TIMESTAMPRSP="2009-06-25 08:17:10";
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// override the default hardcoded values in DLRTestUtil, if we used
		//spring autowiring this would be a perfect use for dependency injection
		//shan no like brittle code
		DLRTestUtil.VISUALID = "6051039879902575";
		DLRTestUtil.CUSTOMERID = "300010434";
		DLRTestUtil.SOURCEID = "VLINK";
		DLRTestUtil.TIMESTAMPREQ="2009-06-25 17:17:15.49";
		DLRTestUtil.MESSAGEID= new BigInteger("12702847");
		

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test getting xml from an sample egalaxy xml file for a ticket activate
	 * request that originates as a dti upgrade alpha for dlr.
	 */
	@Test
	public final void testGetXML() {
		// ***** Read the test file. *****
		String baselineXML = null;
		File file = new File("filename");
		Logger.getAnonymousLogger().info(file.getAbsolutePath());
		// harcoding is incredibly bad..refactor this to use a property file
		String fileName = new String(
				"./src/test/resources/xml/egalaxy/TicketActivationRequest-UpgradeAlpha-VLINK.xml");

		try {
			baselineXML = DLRTestUtil.getXMLfromFile(fileName);
		} catch (Exception e) {
			fail("Unable to read test file: " + fileName);
		}

		GWEnvelopeTO dtiTxn = createUATicketActivationReqTO();
		String newXML = null;

		try {
			newXML = GWEnvelopeXML.getXML(dtiTxn);
		} catch (DTIException dtie) {
			fail("DTIException creating the gateway request XML: "
					+ dtie.toString());
		}

		// Validate the XML Headers (Payload & Command)
		try {
			DLRTestUtil.validateXMLHeaders(baselineXML, newXML);
		} catch (Exception e) {
			fail("Exception validating gateway request XML headers: "
					+ e.toString());
		}

		// Validate Ticket Activation (void) Response
		String taRespBaseline = DLRTestUtil.findTag(baselineXML, "Body");
		String taRespNew = DLRTestUtil.findTag(newXML, "Body");

		try {
			DLRTestUtil.compareXML(taRespBaseline, taRespNew, "Tickets");
		} catch (Exception e) {
			fail("Exception validating TicketActivation section: "
					+ e.toString());
		}

		return;
	}

	/**
	 * Test get transfer object for a successful TicketActivation.Command =
	 * Activate, aka a dlr upgrade alpha
	 */
	@Test
	public final void testGetSucessTO() {

		// ***** Read the test file. *****
		String inputXML = null;
		String fileName = new String(
				"./src/test/resources/xml/egalaxy/TicketActivationResponse-UpgradeAlpha-VLINK.xml");

		try {
			inputXML = DLRTestUtil.getXMLfromFile(fileName);
		} catch (Exception e) {
			fail("Unable to read test file: " + fileName);
		}

		GWEnvelopeTO parsedEnvTO = null;

		try {
			Logger.getAnonymousLogger().info("getting TO from envelope");
			parsedEnvTO = GWEnvelopeXML.getTO(inputXML);
		} catch (DTIException dtie) {
			fail("DTIException parsing the gateway response XML: "
					+ dtie.toString());
		}

		try {
			Logger.getAnonymousLogger().info("validating reponse TO");
			validateUATicketRespTO(parsedEnvTO);

		} catch (Exception e) {
			fail("Exception parsing the gateway response XML: " + e.toString());
		}
	}

	/**
	 * Test get transfer object for an error reponse, error TBD. TO DO there will
	 * be multiple error TO tests, this is just a placeholder for now.
	 */
	@Test
	public final void testGetTOPayment1414Error() {
		

		// ***** Read the test file. *****
		String inputXML = null;
		String fileName = new String(
				"./src/test/resources/xml/egalaxy/TicketActivationResponse-UpgradeAlpha-VLINK-MultipleItems-Error1414-PaymentAmount.xml");

		try {
			inputXML = DLRTestUtil.getXMLfromFile(fileName);
		} catch (Exception e) {
			fail("Unable to read test file: " + fileName);
		}

		GWEnvelopeTO parsedEnvTO = null;

		try {
			parsedEnvTO = GWEnvelopeXML.getTO(inputXML);
		} catch (DTIException dtie) {
			fail("DTIException parsing the gateway response XML: "
					+ dtie.toString());
		}

		try {
			validateUATicketRespTOError(parsedEnvTO,
					DLRTestUtil.TICKET_ACTIVATION_PAYMENT_MISMATCH_CODE);
		} catch (Exception e) {
			fail("Exception parsing the gateway response XML: " + e.toString());
		}

	}

	/**
	 * Validates a gateway galaxy TicketActivation.Command = Activate (upgrade
	 * alpha) response transfer object.
	 * 
	 * @param envTo
	 *            the gateway envelop transfer object
	 * 
	 * @throws Exception
	 *             the exception
	 */
	static void validateUATicketRespTO(GWEnvelopeTO envTo)
			throws Exception {
		GWHeaderTO gwHdrTO = envTo.getHeaderTO();

		validateRespHeaderTO(gwHdrTO);

		// Body
		GWBodyTO gwBodyTO = envTo.getBodyTO();
		if (gwBodyTO == null) {
			throw new Exception("Null Body in parsed transfer object.");
		}

		// Status
		GWStatusTO gwStatusTO = gwBodyTO.getStatusTO();
		if (gwStatusTO == null) {
			throw new Exception("Null Body/Status in parsed transfer object.");
		}

		String statusCode = gwStatusTO.getStatusCode();
		if (statusCode == null) {
			throw new Exception(
					"Null Body/Status/StatusCode in parsed transfer object.");
		}

		// we could get a status of non-zero back for error testing
		// if (statusCode.compareTo(DLRTestUtil.STATUSCODEOK) != 0)
		// throw new Exception("Unexpected value of " + statusCode +
		// " in Body/Status/StatusCode.");

		String statusText = gwStatusTO.getStatusText();
		if (statusText == null) {
			throw new Exception(
					"Null Body/Status/statusText in parsed transfer object.");
		}

		// we could get a non-ok status in error testing
		// if (statusText.compareTo(DLRTestUtil.STATUSTEXTOK) != 0) {
		// throw new Exception("Unexpected value of " + statusText +
		// " in Body/Status/StatusCode.");
		// }
	}

	/**
 	 * Validates a gateway galaxy ticket activation (upgrade alpha) response transfer object error
 	 * 
 	 * @param envTo the env to
 	 * 
 	 * @throws Exception the exception
 	 */
 	private static void validateUATicketRespTOError(GWEnvelopeTO envTo, String ticketErrorCode) throws Exception {
		GWHeaderTO gwHdrTO = envTo.getHeaderTO();
		validateRespHeaderTO(gwHdrTO);
		// Body
		GWBodyTO gwBodyTO = envTo.getBodyTO();
		if (gwBodyTO == null) {
			throw new Exception("Null Body in parsed transfer object.");
		}

		// Status
		GWStatusTO gwStatusTO = gwBodyTO.getStatusTO();
		if (gwStatusTO == null) {
			throw new Exception("Null Body/Status in parsed transfer object.");
		}

		String statusCode = gwStatusTO.getStatusCode();
		if (statusCode == null) {
			throw new Exception(
					"Null Body/Status/StatusCode in parsed transfer object.");
		}

		// we could get a status of non-zero back for error testing
		if (statusCode.length() == 0
				|| !statusCode.equals(DLRTestUtil.TICKET_ACTIVATION_FAILED_CODE)) {
			throw new Exception("Unexpected value of " + statusCode
					+ " in Body/Status/StatusCode.");
		}
		String statusText = gwStatusTO.getStatusText();
		if (statusText == null) {
			throw new Exception(
					"Null Body/Status/statusText in parsed transfer object.");
		}

		// TO DO visualID should be added to errors TO
		// String visualID = gwBodyTO.getTicketActivationtErrorsTO().g
		String ticketStatusCode = gwBodyTO.getTicketActivationtErrorsTO()
				.getErrorCode();
		String ticketStatusText = gwBodyTO.getTicketActivationtErrorsTO()
				.getErrorText();

		if (ticketStatusText.length() == 0 || ticketStatusText == null) {
			throw new Exception(
					"Null /TicketActivationErrors/TicketActivationError/StatusText in parsed transfer object.");
		}

		if (ticketStatusCode.length() == 0
				|| !ticketStatusCode.equals(ticketErrorCode)) {
			throw new Exception("Expected ticket status code "
					+ ticketErrorCode + " but received " + ticketStatusCode);
		}
 		}

	/**
	 * TO DO move this to a parent class, maybe a dlr test utils helper, since
	 * this duplicates functionality in the void ticket test case. Validates a
	 * gateway galaxy response header transfer object.
	 * 
	 * @param hdrTo
	 *            the hdr to
	 * 
	 * @throws Exception
	 *             the exception
	 */
	 private static void validateRespHeaderTO(GWHeaderTO hdrTo) throws Exception {
		    if (hdrTo == null) {
		        throw new Exception("Null Header in parsed transfer object.");
		      }

		      // MessageID
              BigInteger messageId = hdrTo.getMessageID();
		      if (messageId == null) {
		        throw new Exception("Null MessageID on parsed XML.");
		      }
		      if (messageId.compareTo(DLRTestUtil.MESSAGEID) != 0) {
		    	  Logger.getAnonymousLogger().info("messageid" + messageId);
		        throw new Exception("Unexpected value of " + messageId.toString() + " in Header/MessageID");
		      }
		        
		      // MessageType
		      String msgType = hdrTo.getMessageType();
		      if (msgType == null) {
		        throw new Exception("Null MessageType in parsed transfer object.");
		      }
		      if (msgType.compareTo(DLRTestUtil.MESSAGETYPE_TICKET_ACTIVATION_RSP) != 0)
		        throw new Exception("Unexpected value of " + msgType + " in Header/MessageType.");

		      // SourceID
		      String sourceId = hdrTo.getSourceID();
		      if (sourceId == null) {
		        throw new Exception("Null SourceID on parsed XML.");
		      }
		      if (sourceId.compareTo(DLRTestUtil.SOURCEID) != 0) {
		        throw new Exception("Unexpected value of " + sourceId + " in Header/SourceID.");
		      }
		      
		      // TimeStamp
		      GregorianCalendar gCalTO = hdrTo.getTimeStamp();
		      if (gCalTO == null) {
		        throw new Exception("Null TimeStamp in parsed transfer object.");
		      }
		      GregorianCalendar gCalBase = UtilityXML.getGCalFromEGalaxyDate(DLRTestUtil.TIMESTAMPRSP);
		      if (gCalTO.compareTo(gCalBase) != 0) {
		        throw new Exception("Unexpected value of " + gCalTO.toString() + " in Header/TimeStamp.");
		      }	
	}
	 
		/**
		 * Helper method.
		 * Creates an ticket activation activate (upgrade alpha)request transfer object.
		 * 
		 * @return the gW envelope to
		 */
		static GWEnvelopeTO createUATicketActivationReqTO() {
			GWEnvelopeTO gwEnvTO = new GWEnvelopeTO(
					GWEnvelopeTO.GWTransactionType.TICKETACTIVATION);

			// Header
			GWHeaderTO gwHdrTO = new GWHeaderTO();
			gwHdrTO.setSourceID(DLRTestUtil.SOURCEID);
			gwHdrTO.setMessageID(DLRTestUtil.MESSAGEID);
			GregorianCalendar gCal = CustomDataTypeConverter
					.parseDateTimeStamp(DLRTestUtil.TIMESTAMPREQ);
			gwHdrTO.setTimeStamp(gCal);
			gwHdrTO.setMessageType(DLRTestUtil.MESSAGETYPE_TICKET_ACTIVATION);
			gwEnvTO.setHeaderTO(gwHdrTO);

			// Body
			GWBodyTO gwBodyTO = new GWBodyTO();

			// GWQueryTicketRqstTO
			GWTicketActivationRqstTO gwTktActTO = new GWTicketActivationRqstTO();
			//TO DO refactor this to read from a properties file, hardcoding is bad
			//visualID, ItemCode, Price
			gwTktActTO.addTicket("6051039879902575", "21353", "65.00");
			gwTktActTO.setCustomerID(DLRTestUtil.CUSTOMERID);
			gwTktActTO.setCommand(TransformConstants.GW_COMMAND_ACTIVATE);
			//TO DO refactor this to read from a properties file, hardcoding is bad
			//paymentCode, Description, Amount
			GWPaymentTO payment = new GWPaymentTO("64", "MrchTktSls", "65.00");
			gwTktActTO.addPayment(payment);
			
			gwBodyTO.setTicketActivationRqstTO(gwTktActTO);
			gwEnvTO.setBodyTO(gwBodyTO);

			return gwEnvTO;
		}
}
