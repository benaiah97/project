package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Properties;

import mockit.Mock;
import mockit.MockUp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.EnvironmentType;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;

/**
 * Tests the content rules.
 * @author lewit019
 *
 */
public class ContentRulesTestCase extends CommonBusinessTest {

	@Before
	public void setUp() {
		Properties props=setConfigProperty();
		setMockProperty();
		mockProperty();

	}
	/**
	 * 
	 */
	
	public void testValidatePayHdrFieldsforException() {
		PayloadHeaderTO payHeaderTO = new PayloadHeaderTO();
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
		} catch (DTIException dti) {
			assertEquals(dti.getLogMessage(), "PayloadId was null.");
		}

		// Test 2
		payHeaderTO.setPayloadID("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 2: PayloadId invalid length too short.");
		} catch (DTIException dti) {
			assertEquals(dti.getLogMessage(),
					"PayloadId was of invalid length.");
		}
		// Test 3: PayloadId invalid length too long
		payHeaderTO.setPayloadID("123456789012345678901");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 3:  PayloadId invalid length too long.");
		} catch (DTIException dti) {
			assertEquals(dti.getLogMessage(),
					"PayloadId was of invalid length.");
		}

		// Test 4: Target missing
		payHeaderTO.setPayloadID("1234567890");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 4:  Target missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getLogMessage(), "Target was null.");

		}

		// Test 5: Target too short
		payHeaderTO.setTarget("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 5:  Target too short.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getLogMessage(), "Target was of zero length.");
		}

		// Test 6: Version missing
		payHeaderTO.setTarget("Target");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 6:  Version missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getLogMessage(), "Version was null.");
		}

		// Test 7: Version too short
		payHeaderTO.setVersion("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 7:  Version too short.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getLogMessage(), "Version was of zero length.");
		}

		// Test 8: Comm Protocol missing
		payHeaderTO.setVersion("Version");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 8:  Comm Protocol missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getLogMessage(), "Comm Protocol was null.");
		}

		// Test 9: Comm Protocol too short
		payHeaderTO.setCommProtocol("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 9:  Comm Protocol too short.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getLogMessage(),
					"Comm Protocol was of zero length.");
		}

		// Test 10: Comm Method missing
		payHeaderTO.setCommProtocol("Protocol");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 10:  Comm Method missing.");
		} catch (DTIException dtie) {
		}

		// Test 11: Comm Method too short
		payHeaderTO.setCommMethod("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 11:  Comm Method too short.");
		} catch (DTIException dtie) {
		}

		// Test 12: TransmitDate/Time missing
		payHeaderTO.setCommMethod("Method");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 12:  TransmitDate/Time missing.");
		} catch (DTIException dtie) {
		}

		// Test 13: TktSeller missing
		payHeaderTO.setTransmitDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 13:  TktSeller missing.");
		} catch (DTIException dtie) {
		}

		// Test 14: TSMAC missing
		TktSellerTO tktSellerTO = new TktSellerTO();
		payHeaderTO.setTktSeller(tktSellerTO);
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 14:  TSMAC missing.");
		} catch (DTIException dtie) {
		}

		// Test 15: TSMAC too short

		tktSellerTO.setTsMac("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 15:  TSMAC to short.");
		} catch (DTIException dtie) {
		}

		// Test 16: TSLocation missing
		tktSellerTO.setTsMac("TSMAC");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 16:  TSLocation missing.");
		} catch (DTIException dtie) {
		}

		// Test 17: TSLocation too short
		tktSellerTO.setTsLocation("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 17:  TSLocation too short.");
		} catch (DTIException dtie) {
		}

		// Test 18: TSSystem missing
		tktSellerTO.setTsLocation("TsLocation");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 18:  TSSystem missing.");
		} catch (DTIException dtie) {
		}

		// Test 19: TSSystem too short
		tktSellerTO.setTsSystem("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 19:  TSSystem too short.");
		} catch (DTIException dtie) {
		}

		// Test 20: TsSecurity missing
		tktSellerTO.setTsSystem("TsSystem");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 20:  TsSecurity missing.");
		} catch (DTIException dtie) {
		}

		// Test 21: TsSecurity too short
		tktSellerTO.setTsSecurity("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 21:  TsSecurity too short.");
		} catch (DTIException dtie) {
		}

		// Test 22: CommandCount missing
		tktSellerTO.setTsSecurity("TsSecurity");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 22:  CommandCount missing.");
		} catch (DTIException dtie) {
		}

		// Test 23: CommandCount invalid value
		BigInteger count = new BigInteger("-1");
		payHeaderTO.setCommandCount(count);
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			fail("Expected exception in Test 23:  CommandCount invalid value.");
		} catch (DTIException dtie) {
		}

		// Test 24: Valid payload header
		payHeaderTO.setCommandCount(new BigInteger("1"));
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
		} catch (DTIException dtie) {
			fail("Unexpected exception in Test 24:  Valid payload header.");
		}

	}

	/**
	 * Test validate pay hdr fields.
	 */
	
	public void testValidatePayHdrFields() {

		testValidatePayHdrFieldsforException();
		DTITransactionTO dtiTxn = null;
		/* Test case for applyQueryTicketRules */
		dtiTxn = new DTITransactionTO(TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		PayloadHeaderTO payHeaderTO = dtiTxn.getRequest().getPayloadHeader();
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
	}

	/**
	 * Test validate cmd hdr fields.
	 */
	
	public final void testValidateCmdHdrFields() {

		CommandHeaderTO cmdHeaderTO = new CommandHeaderTO();

		// Test 1: CmdItem is missing
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			fail("Expected exception in Test 1: CmdItem missing.");
		} catch (DTIException dtie) {
			/*
			 * if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_MSG_ELEMENT)
			 * //
			 * fail("Expected INVALID_MSG_ELEMENT in Test 1:  CmdItem is missing."
			 * );
			 */}

		// Test 2: CmdItem invalid value
		BigInteger cmdItem = new BigInteger("-1");
		cmdHeaderTO.setCmdItem(cmdItem);
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			fail("Expected exception in Test 2:  CmdItem invalid value.");
		} catch (DTIException dtie) {
		}

		// Test 3: CmdTimeout is missing
		cmdHeaderTO.setCmdItem(new BigInteger("1"));
		;
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			fail("Expected exception in Test 3: CmdTimeout is missing.");
		} catch (DTIException dtie) {
		}

		// Test 4: CmdTimeout invalid value
		BigInteger cmdTimeout = new BigInteger("-1");
		cmdHeaderTO.setCmdTimeout(cmdTimeout);
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			fail("Expected exception in Test 4:  CmdTimeout invalid value.");
		} catch (DTIException dtie) {
		}

		// Test 5: CmdDateTime is missing
		cmdHeaderTO.setCmdTimeout(new BigInteger("1"));
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			fail("Expected exception in Test 5:  CmdDateTime is missing.");
		} catch (DTIException dtie) {
		}

		// Test 6: CmdInvoice is missing
		cmdHeaderTO.setCmdDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			fail("Expected exception in Test 6:  CmdInvoice is missing.");
		} catch (DTIException dtie) {
		}

		// Test 7: CmdInvoice too short
		cmdHeaderTO.setCmdInvoice(new String(""));
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			fail("Expected exception in Test 7:  CmdInvoice too short.");
		} catch (DTIException dtie) {
		}

		// Test 8: CmdDevice is missing
		cmdHeaderTO.setCmdInvoice("AnInvoice.");
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			fail("Expected exception in Test 8:  CmdDevice is missing.");
		} catch (DTIException dtie) {
		}

		// Test 9: CmdDevice is too short
		cmdHeaderTO.setCmdDevice(new String(""));
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			fail("Expected exception in Test 9:  CmdDevice is too short.");
		} catch (DTIException dtie) {
		}

		// Test 10: CmdOperator is missing
		cmdHeaderTO.setCmdDevice("CmdDevice");
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			fail("Expected exception in Test 10:  CmdOperator is missing.");
		} catch (DTIException dtie) {
		}

		// Test 11: CmdOperator is too short
		cmdHeaderTO.setCmdOperator(new String(""));
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			fail("Expected exception in Test 11:  CmdOperator is too short.");
		} catch (DTIException dtie) {
		}

		// Test 12: Valid command header
		cmdHeaderTO.setCmdOperator("Bob");
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
		} catch (DTIException dtie) {
			fail("Unexpected exception in Test 12:  Valid command header.");
		}

	}

	/**
	 * Test validate dti request struct.
	 */
	@Test
	public final void testValidateDTIRequestStructure() {
		/*Validates the DTITransactionTo */
		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.QUERYTICKET);
		/*
		 * Scenario :: 1 When no DTIRequestTO is associated with
		 * DTITransactionTO: Exception Expected
		 */
		try {
			ContentRules.validateDTIRequestStructure(dtiTxn);
			Assert.fail("Expected Exception: DTITransactionTO has no DTIRequestTO object associated.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_ELEMENT);
			assertEquals(
					"DTITransactionTO has no DTIRequestTO object associated.",
					dtie.getLogMessage());
		}

		DTIRequestTO dtiRequestTO = new DTIRequestTO();
		dtiTxn.setRequest(dtiRequestTO);
		/*
		 * Scenario :: 1 When DTIRequestTO is associated with
		 * DTITransactionTO: Exception Expected
		 */
		try {
			ContentRules.validateDTIRequestStructure(dtiTxn);
			Assert.fail("Expected Exception: DTIRequestTO has no PayloadHeaderTO object associated.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_ELEMENT);
			assertEquals(
					"DTIRequestTO has no PayloadHeaderTO object associated.",
					dtie.getLogMessage());
		}

		PayloadHeaderTO payHeaderTO = new PayloadHeaderTO();
		dtiRequestTO.setPayloadHeader(payHeaderTO);

		// Test 3: DTIRequestTO missing cmdHdr and Body
		try {
			ContentRules.validateDTIRequestStructure(dtiTxn);
			fail("Test 3:  DTIRequestTO missing cmdHdr and Body should have failed.");
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_MSG_ELEMENT)
				fail("Test 3:  DTIRequestTO missing cmdHdr and Body should have triggered INVALID_MSG_ELEMENT");
		}

		CommandHeaderTO cmdHeaderTO = new CommandHeaderTO();
		dtiRequestTO.setCommandHeader(cmdHeaderTO);

		// Test 4: DTIRequestTO missing Body
		try {
			ContentRules.validateDTIRequestStructure(dtiTxn);
			fail("Test 4:  DTIRequestTO missing Body should have failed.");
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_MSG_ELEMENT)
				fail("Test 4:  DTIRequestTO missing Body should have triggered INVALID_MSG_ELEMENT");
		}

		QueryTicketRequestTO queryTicketRequestTO = new QueryTicketRequestTO();
		dtiRequestTO.setCommandBody(queryTicketRequestTO);

		// Test 5: DTIRequestTO complete
		try {
			ContentRules.validateDTIRequestStructure(dtiTxn);

		} catch (DTIException dtie) {
			fail("Test 5:  DTIRequestTO complete should have passed.");
		}

		return;

	}

	/**
	 *Test case for validateProviderTarget
	 */
	//@Test
	public void testValidateProviderTarget() {
		String target = null;
		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.QUERYTICKET);
		DTIRequestTO dtiRequest = new DTIRequestTO();
		PayloadHeaderTO payHeaderTO = new PayloadHeaderTO();
		dtiRequest.setPayloadHeader(payHeaderTO);
		dtiTxn.setRequest(dtiRequest);
		/* Scenario :: 1 putting the target as null , expecting Exception */
		payHeaderTO.setTarget(target);
		try {
			ContentRules.validateProviderTarget(dtiTxn);
			Assert.fail("Expected Exception: Null target of sent to DTI.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_TARGET_VERSION);
			assertEquals("Null target of sent to DTI.", dtie.getLogMessage());
		}
		/*
		 * Scenario :: 2 putting the target of length less than 4 , expecting
		 * Exception
		 */
		target = "TES";
		payHeaderTO.setTarget(target);
		try {
			ContentRules.validateProviderTarget(dtiTxn);
			Assert.fail("Expected Exception: Invalid target of " + target
					+ " sent to DTI.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_TARGET_VERSION);
			assertEquals("Invalid target of " + target + " sent to DTI.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario :: 3 putting the target of invalid value , expecting
		 * Exception
		 */
		target = "ABUMBLEBEE";
		payHeaderTO.setTarget(target);
		try {
			ContentRules.validateProviderTarget(dtiTxn);
			Assert.fail("Expected Exception: Invalid target of " + target
					+ " sent to DTI.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_TARGET_VERSION);
			assertEquals("Invalid target of " + target + " sent to DTI.",
					dtie.getLogMessage());
		}
		/* Scenario :: 4 putting the target of different environment */
		target = "PROD";
		payHeaderTO.setTarget(target);
		try {
			ContentRules.validateProviderTarget(dtiTxn);
			Assert.fail("Expected Exception: Invalid target of " + target
					+ " sent to DTI.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_TARGET_VERSION);
			assertEquals("Invalid target of " + target + " sent to DTI.",
					dtie.getLogMessage());
		}
		assertEquals(dtiTxn.getTpiCode(),TPI_CODE_WDW);
		assertEquals(dtiTxn.getProvider(),ProviderType.WDWNEXUS);
		assertEquals(dtiTxn.getEnvironment(),EnvironmentType.PRODUCTION);
		/* Scenario :: 4.1 putting the target of different environment */
		target = "PROD-DLR";
		payHeaderTO.setTarget(target);
		try {
			ContentRules.validateProviderTarget(dtiTxn);
			Assert.fail("Expected Exception: Invalid target of " + target
					+ " sent to DTI.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_TARGET_VERSION);
			assertEquals("Invalid target of " + target + " sent to DTI.",
					dtie.getLogMessage());
		}
		assertEquals(dtiTxn.getTpiCode(),TPI_CODE_DLR);
		assertEquals(dtiTxn.getProvider(),ProviderType.DLRGATEWAY);
		assertEquals(dtiTxn.getEnvironment(),EnvironmentType.PRODUCTION);
		/* Scenario :: 4.2 putting the target of different environment */
		target = "PROD-HKD";
		payHeaderTO.setTarget(target);
		try {
			ContentRules.validateProviderTarget(dtiTxn);
			Assert.fail("Expected Exception: Invalid target of " + target
					+ " sent to DTI.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_TARGET_VERSION);
			assertEquals("Invalid target of " + target + " sent to DTI.",
					dtie.getLogMessage());
		}
		assertEquals(dtiTxn.getTpiCode(),TPI_CODE_HKD);
		assertEquals(dtiTxn.getProvider(),ProviderType.HKDNEXUS);
		assertEquals(dtiTxn.getEnvironment(),EnvironmentType.PRODUCTION);
		/* Scenario :: 5 putting the target of same environment */
		target = "TEST";
		payHeaderTO.setTarget(target);
		try {
			ContentRules.validateProviderTarget(dtiTxn);
			assertEquals(dtiTxn.getTpiCode(),TPI_CODE_WDW);
			assertEquals(dtiTxn.getProvider(),ProviderType.WDWNEXUS);
			assertEquals(dtiTxn.getEnvironment(),EnvironmentType.TEST);
			
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario :: 5.1 putting the target of same environment */
		target = "TEST-DLR";
		payHeaderTO.setTarget(target);
		try {
			ContentRules.validateProviderTarget(dtiTxn);
			assertEquals(dtiTxn.getTpiCode(),TPI_CODE_DLR);
			assertEquals(dtiTxn.getProvider(),ProviderType.DLRGATEWAY);
			assertEquals(dtiTxn.getEnvironment(),EnvironmentType.TEST);
			
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		/* Scenario :: 5.1 putting the target of same environment */
		target = "TEST-HKD";
		payHeaderTO.setTarget(target);
		try {
			ContentRules.validateProviderTarget(dtiTxn);
			assertEquals(dtiTxn.getTpiCode(),TPI_CODE_HKD);
			assertEquals(dtiTxn.getProvider(),ProviderType.HKDNEXUS);
			assertEquals(dtiTxn.getEnvironment(),EnvironmentType.TEST);
			
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		

	}

	
	public final void testValidateProviderTarget1() {
		Properties props = new Properties();

		// Create enough of a transactional structure to test with.
		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.QUERYTICKET);
		DTIRequestTO dtiRequest = new DTIRequestTO();
		PayloadHeaderTO payHeaderTO = new PayloadHeaderTO();
		dtiRequest.setPayloadHeader(payHeaderTO);
		dtiTxn.setRequest(dtiRequest);
		InputStream inStream = null;
		// Test 1: Invalid target string

		new MockUp<ContentRules>() {
			@Mock
			String getProperty(String key) {
				return "Test";
			}

		};

		/*
		 * try{ inStream =
		 * this.getClass().getResourceAsStream("/dtiApp.properties");
		 * props.load(inStream); }catch(Exception e){
		 * 
		 * }
		 */
		try {

			ContentRules rules = new ContentRules();
			try {
				rules.validatePayHdrFields(payHeaderTO);
			} catch (Exception e) {

			}

			payHeaderTO.setTarget("ABUMBLEBEE");
			ContentRules.validateProviderTarget(dtiTxn);
			fail("Test 1:  Invalid target string should have triggered an error.");
		} catch (DTIException dtie) {
			if (dtie.getDtiErrorCode() != DTIErrorCode.INVALID_TARGET_VERSION)
				fail("Test 1:  Invalid target string should have triggered INVALID_TARGET_VERSION");
		}

		// Test 2: Target strings of PROD, PROD-WDW
		try {
			payHeaderTO.setTarget("PROD");
			ContentRules.validateProviderTarget(dtiTxn);
			if (dtiTxn.getProvider() != DTITransactionTO.ProviderType.WDWNEXUS)
				fail("Test 2:  Target strings of PROD should have resulted in WDWNEXUS ProviderType.");
			if (dtiTxn.getEnvironment() != DTITransactionTO.EnvironmentType.PRODUCTION)
				fail("Test 2:  Target strings of PROD should have resulted in PRODUCTION EnvironmentType.");

			payHeaderTO.setTarget("Prod-WDW");
			ContentRules.validateProviderTarget(dtiTxn);
			if (dtiTxn.getProvider() != DTITransactionTO.ProviderType.WDWNEXUS)
				fail("Test 2:  Target strings of PROD-WDW should have resulted in WDWNEXUS ProviderType.");
			if (dtiTxn.getEnvironment() != DTITransactionTO.EnvironmentType.PRODUCTION)
				fail("Test 2:  Target strings of PROD-WDW should have resulted in PRODUCTION EnvironmentType.");
			fail("Test 2:  Target strings of PROD, PROD-WDW should passed");
		} catch (DTIException dtie) {
			//if(!"Invalid target of Prod sent to DTI".equals(dtie.getLogMessage()))
			//fail("Test 2:  Target strings of PROD, PROD-WDW should passed");
		}

		// Test 3: Target strings of TEST, TEST-WDW
		try {
			payHeaderTO.setTarget("Test");
			ContentRules.validateProviderTarget(dtiTxn);
			if (dtiTxn.getProvider() != DTITransactionTO.ProviderType.WDWNEXUS)
				fail("Test 3:  Target strings of Test should have resulted in WDWNEXUS ProviderType.");
			if (dtiTxn.getEnvironment() != DTITransactionTO.EnvironmentType.TEST)
				fail("Test 3:  Target strings of Test should have resulted in TEST EnvironmentType.");

			payHeaderTO.setTarget("Test-WDW");
			ContentRules.validateProviderTarget(dtiTxn);
			if (dtiTxn.getProvider() != DTITransactionTO.ProviderType.WDWNEXUS)
				fail("Test 3:  Target strings of TEST-WDW should have resulted in WDWNEXUS ProviderType.");
			if (dtiTxn.getEnvironment() != DTITransactionTO.EnvironmentType.TEST)
				fail("Test 3:  Target strings of TEST-WDW should have resulted in TEST EnvironmentType.");

		} catch (DTIException dtie) {
			fail("Test 3:  Target strings of TEST, TEST-WDW should passed");
		}

		// Test 4: Target strings of PROD-DLR
		try {
			payHeaderTO.setTarget("Prod-DLR");
			ContentRules.validateProviderTarget(dtiTxn);
			if (dtiTxn.getProvider() != DTITransactionTO.ProviderType.DLRGATEWAY)
				fail("Test 4:  Target strings of PROD-DLR should have resulted in DLRGATEWAY ProviderType.");
			if (dtiTxn.getEnvironment() != DTITransactionTO.EnvironmentType.PRODUCTION)
				fail("Test 4:  Target strings of PROD-DLR should have resulted in PRODUCTION EnvironmentType.");

		} catch (DTIException dtie) {
			// fail("Test 4:  Target strings of PROD-DLR should passed");
		}

		// Test 5: Target strings of TEST-DLR
		try {
			payHeaderTO.setTarget("Test-DLR");
			ContentRules.validateProviderTarget(dtiTxn);
			if (dtiTxn.getProvider() != DTITransactionTO.ProviderType.DLRGATEWAY)
				fail("Test 5:  Target strings of TEST-DLR should have resulted in DLRGATEWAY ProviderType.");
			if (dtiTxn.getEnvironment() != DTITransactionTO.EnvironmentType.TEST)
				fail("Test 5:  Target strings of TEST-DLR should have resulted in TEST EnvironmentType.");

		} catch (DTIException dtie) {
			fail("Test 5:  Target strings of TEST-DLR should passed");
		}

	}
	
	/**
	 * for mocking getProperty
	 */
	private void mockProperty(){
		new MockUp<ContentRules>() {
			@Mock
			String getProperty(String key) {
				return "Test";
			}

		};
	}

}
