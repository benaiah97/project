package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.assertEquals;
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
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
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
	 * Test validate pay hdr fields.
	 */
	@Test
	public void testValidatePayHdrFields() {
		/*Testing for valid Exceptions : START */
		DTITransactionTO dtiTxn = null;
		PayloadHeaderTO payHeaderTO = new PayloadHeaderTO();
		/*Scenario::1 when Payload Id is missing: Expected Exception*/
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected Exception : PayloadId was null.");		
		} catch (DTIException dti) {
			assertEquals(dti.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dti.getLogMessage(), "PayloadId was null.");
		}

		/*Scenario::2 when Payload Id is of invalid length: Expected Exception*/
		payHeaderTO.setPayloadID("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception: PayloadId invalid length too short.");
		} catch (DTIException dti) {
			assertEquals(dti.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dti.getLogMessage(),
					"PayloadId was of invalid length.");
		}
		/*Scenario:: 3: PayloadId invalid length too long: Expected Exception*/
		payHeaderTO.setPayloadID("123456789012345678901");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception:  PayloadId invalid length too long.");
		} catch (DTIException dti) {
			assertEquals(dti.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dti.getLogMessage(),
					"PayloadId was of invalid length.");
		}
		/*Scenario:: 4: Target missing: Expected Exception*/
		payHeaderTO.setPayloadID("1234567890");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 4:  Target missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(), "Target was null.");

		}
		/*Scenario:: 5: Target too short: Expected Exception*/
		payHeaderTO.setTarget("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 5:  Target too short.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(), "Target was of zero length.");
		}
		/*Scenario:: 6: Version missing: Expected Exception*/
		payHeaderTO.setTarget("Target");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 6:  Version missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(), "Version was null.");
		}
		/*Scenario:: 7: Version too short: Expected Exception*/
		payHeaderTO.setVersion("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 7:  Version too short.");
		} catch (DTIException dti) {
			assertEquals(dti.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dti.getLogMessage(), "Version was of zero length.");
		}
		/*Scenario:: 8: Comm Protocol missing: Expected Exception*/
		payHeaderTO.setVersion("Version");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 8:  Comm Protocol missing.");
		} catch (DTIException dti) {
			assertEquals(dti.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dti.getLogMessage(), "Comm Protocol was null.");
		}
		/*Scenario:: 9: Comm Protocol too short: Expected Exception*/
		payHeaderTO.setCommProtocol("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 9:  Comm Protocol too short.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(),
					"Comm Protocol was of zero length.");
		}
		/*Scenario:: 10: Comm Method missing: Expected Exception*/
		payHeaderTO.setCommProtocol("Protocol");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 10:  Comm Method missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(),
					"Comm Method was null.");
		}
		/*Scenario:: 11: Comm Method too short: Expected Exception*/
		payHeaderTO.setCommMethod("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 11:  Comm Method too short.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(),
					"Comm Method was of zero length.");
		}
		/*Scenario:: 12: TransmitDate/Time missing: Expected Exception*/
		payHeaderTO.setCommMethod("Method");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 12:  TransmitDate/Time missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(),
					"Trans Date or Time was null.");
		}
		/*Scenario:: 13: TktSeller missing: Expected Exception*/
		payHeaderTO.setTransmitDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 13:  TktSeller missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(),
					"TktSeller object was null.");
		}
		/*Scenario:: 14: TSMAC missing: Expected Exception*/
		TktSellerTO tktSellerTO = new TktSellerTO();
		payHeaderTO.setTktSeller(tktSellerTO);
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 14:  TSMAC missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			}
		/*Scenario:: 15: TSMAC too short: Expected Exception*/
		tktSellerTO.setTsMac("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 15:  TSMAC to short.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(),
					"TSMAC was of zero length.");
		}
		/*Scenario:: 16: TSLocation missing: Expected Exception*/
		tktSellerTO.setTsMac("TSMAC");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 16:  TSLocation missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
		}
		/*Scenario:: 17: TSLocation too short: Expected Exception*/
		tktSellerTO.setTsLocation("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 17:  TSLocation too short.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(),
					"TSLocation was of zero length.");
		}
		/*Scenario:: 18: TSSystem missing: Expected Exception*/
		tktSellerTO.setTsLocation("TsLocation");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 18:  TSSystem missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
		}
		/*Scenario:: 19: TSSystem too short: Expected Exception*/
		tktSellerTO.setTsSystem("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 19:  TSSystem too short.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(),
					"TSSystem was of zero length.");
		}
		/*Scenario:: 20: TsSecurity missing: Expected Exception*/
		tktSellerTO.setTsSystem("TsSystem");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 20:  TsSecurity missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
		}
		/*Scenario:: 21: TsSecurity too short: Expected Exception*/
		tktSellerTO.setTsSecurity("");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 21:  TsSecurity too short.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(),
					"TSSecurity was of zero length.");
		}
		/*Scenario:: 22: CommandCount missing: Expected Exception*/
		tktSellerTO.setTsSecurity("TsSecurity");
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 22:  CommandCount missing.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
		}
		/*Scenario:: 23: CommandCount invalid value: Expected Exception*/
		BigInteger count = new BigInteger("-1");
		payHeaderTO.setCommandCount(count);
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
			Assert.fail("Expected exception in Test 23:  CommandCount invalid value.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_PAYLOAD_HDR);
			assertEquals(dtie.getLogMessage(),
					"CommandCount was of zero or negative value.");
		}
		/*Testing for valid Exceptions : END */
		/*Testing for SUCCESS */
		/*Scenario:: 24: Valid payload header: Expecting SUCCESS*/
		payHeaderTO.setCommandCount(new BigInteger("1"));
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected exception in Test 24:  Valid payload header.");
		}
		/*Scenario:: 25: Passing the entire request :Expecting SUCCESS*/
		/* Test case for applyQueryTicketRules */
		dtiTxn = new DTITransactionTO(TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		 payHeaderTO = dtiTxn.getRequest().getPayloadHeader();
		try {
			ContentRules.validatePayHdrFields(payHeaderTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*****************TEST CASE END ****************/
	}

	/**
	 * Test validate cmd hdr fields.
	 */
	@Test
	public final void testValidateCmdHdrFields() {

		CommandHeaderTO cmdHeaderTO = new CommandHeaderTO();
		/*Testing for valid Exceptions : START */
		/* Scenario :: 1 CmdItem is missing: Expected Exception */
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			Assert.fail("Expected Exception: CmdItem was null.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_COMMAND_HDR);
			assertEquals("CmdItem was null.", dtie.getLogMessage());
		}
		/* Scenario :: 2 CmdItem invalid value: Expected Exception */
		BigInteger cmdItem = new BigInteger("-1");
		cmdHeaderTO.setCmdItem(cmdItem);
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			Assert.fail("Expected exception: CmdItem was of zero value.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_COMMAND_HDR);
			assertEquals("CmdItem was of zero value.", dtie.getLogMessage());
		}
		/* Scenario :: 3 CmdTimeout is missing: Expected Exception */
		cmdHeaderTO.setCmdItem(new BigInteger("1"));
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			Assert.fail("Expected exception: CommandTimeout was null.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_COMMAND_HDR);
			assertEquals("CommandTimeout was null.", dtie.getLogMessage());
		}
		/* Scenario :: 4: CmdTimeout invalid value: Expected Exception */
		BigInteger cmdTimeout = new BigInteger("-1");
		cmdHeaderTO.setCmdTimeout(cmdTimeout);
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			Assert.fail("Expected exception: CmdItem was of zero value.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_COMMAND_HDR);
			assertEquals("CmdItem was of zero value.", dtie.getLogMessage());
		}
		/* Scenario :: 5 CmdDateTime is missing:Expected Exception */
		cmdHeaderTO.setCmdTimeout(new BigInteger("1"));
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			Assert.fail("Expected exception:  Cmd Date or Time was null");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_COMMAND_HDR);
			assertEquals("Cmd Date or Time was null.", dtie.getLogMessage());
		}
		/* Scenario :: 6: CmdInvoice is missing:Expected Exception */
		cmdHeaderTO.setCmdDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			Assert.fail("Expected exception:  CmdInvoice was null.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_COMMAND_HDR);
			assertEquals("CmdInvoice was null.", dtie.getLogMessage());
		}
		/* Scenario :: 7: CmdInvoice too short :Expected Exception */
		cmdHeaderTO.setCmdInvoice(new String(""));
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			Assert.fail("Expected exception:  CmdInvoice was of zero length.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_COMMAND_HDR);
			assertEquals("CmdInvoice was of zero length.", dtie.getLogMessage());
		}
		/* Scenario :: 8: CmdDevice is missing :Expected Exception */
		cmdHeaderTO.setCmdInvoice("AnInvoice.");
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			Assert.fail("Expected exception:  CmdDevice was null.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_COMMAND_HDR);
			assertEquals("CmdDevice was null.", dtie.getLogMessage());
		}
		/* Scenario :: 9: CmdDevice is too short :Expected Exception */
		cmdHeaderTO.setCmdDevice(new String(""));
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			Assert.fail("Expected exception:  CmdDevice was of zero length.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_COMMAND_HDR);
			assertEquals("CmdDevice was of zero length.", dtie.getLogMessage());
		}
		/* Scenario :: 10: CmdOperator is missing :Expected Exception */
		cmdHeaderTO.setCmdDevice("CmdDevice");
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			Assert.fail("Expected exception in Test 10:  CmdOperator was null.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_COMMAND_HDR);
			assertEquals("CmdOperator was null.", dtie.getLogMessage());
		}
		/* Scenario :: 11: CmdOperator is too short :Expected Exception */
		cmdHeaderTO.setCmdOperator(new String(""));
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
			Assert.fail("Expected exception in Test 11:  CmdOperator is too short.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_COMMAND_HDR);
			assertEquals("CmdOperator was of zero length.", dtie.getLogMessage());
		}
		/*Testing for valid Exceptions : END */
		/*Testing for SUCCESS */
		/* Scenario :: 12: Valid command header :Expected SUCCESS */
		cmdHeaderTO.setCmdOperator("Bob");
		try {
			ContentRules.validateCmdHdrFields(cmdHeaderTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected exception :"+dtie.getLocalizedMessage());
		}
		/******************TEST CASE END *********************/

	}

	/**
	 * Test validate dti request struct.
	 */
	@Test
	public final void testValidateDTIRequestStructure() {
		/* Validates the DTITransactionTo */
		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.QUERYTICKET);
		/*
		 * /*Testing for valid Exceptions : START 
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
		 * Scenario :: 2 When Payload Header is missing: Exception Expected
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
		/*
		 * Scenario :: 3 When Command header is missing: Exception Expected
		 */
		try {
			ContentRules.validateDTIRequestStructure(dtiTxn);
			Assert.fail("Expected Exception: DTIRequestTO has no CommandHeaderTO object associated.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_ELEMENT);
			assertEquals(
					"DTIRequestTO has no CommandHeaderTO object associated.",
					dtie.getLogMessage());
		}
		CommandHeaderTO cmdHeaderTO = new CommandHeaderTO();
		dtiRequestTO.setCommandHeader(cmdHeaderTO);
		/*
		 * Scenario :: 4 When Command Body is missing: Exception Expected
		 */
		try {
			ContentRules.validateDTIRequestStructure(dtiTxn);
			Assert.fail("Expected Exception: DTIRequestTO has no CommandBodyTO object associated.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_ELEMENT);
			assertEquals(
					"DTIRequestTO has no CommandBodyTO object associated.",
					dtie.getLogMessage());
		}
		QueryTicketRequestTO queryTicketRequestTO = new QueryTicketRequestTO();
		dtiRequestTO.setCommandBody(queryTicketRequestTO);
		/*Testing for valid Exceptions : END */
		/*Expecting SUCCESS */
		/*
		 * Scenario :: 5 When Passing the complete request: Expected SUCCESS
		 */
		try {
			ContentRules.validateDTIRequestStructure(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Expected Exception: DTIRequestTO has no CommandBodyTO object associated.");
		}
		/******************TEST CASE END *********************/
	}

	/**
	 *Test case for validateProviderTarget
	 */
	@Test
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
			//Assert.fail("Expected Exception: Null target of sent to DTI.");
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
		/*	Assert.fail("Expected Exception: Invalid target of " + target
					+ " sent to DTI.");*/
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
		/*	Assert.fail("Expected Exception: Invalid target of " + target
					+ " sent to DTI.");*/
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
		/*	Assert.fail("Expected Exception: Invalid target of " + target
					+ " sent to DTI.");*/
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
		/*	Assert.fail("Expected Exception: Invalid target of " + target
					+ " sent to DTI.");*/
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
			/*Assert.fail("Expected Exception: Invalid target of " + target
					+ " sent to DTI.");*/
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
	/**
	 * Test Case for isTargetWDW , will give the target result of the environment 
	 */
	@Test
	public void testIsTargetWDW(){
		/*Scenario:: 1 Passing the TEST Expecting true*/
		String target="TEST";
		boolean result=false;
		result=ContentRules.isTargetWDW(target);
		assertEquals(true,result);
		/*Scenario:: 2 Passing the PROD-WDW Expecting true*/
		target="PROD-WDW";
		result=ContentRules.isTargetWDW(target);
		assertEquals(true,result);
		/*Scenario:: 3 Passing the PROD Expecting true*/
		target="PROD";
		result=ContentRules.isTargetWDW(target);
		assertEquals(true,result);
		/*Scenario:: 4 Passing the TEST-WDW Expecting true*/
		target="TEST-WDW";
		result=ContentRules.isTargetWDW(target);
		assertEquals(true,result);
		/*Scenario:: 4 Passing the UNDEFINED Expecting false*/
		target="UNDEFINED";
		result=ContentRules.isTargetWDW(target);
		assertEquals(false,result);
		
		
	}
	
	/**
	 * For testing  isTargetDLR , gives the scenario based value for DLR
	 */
	@Test
	public void testIsTargetDLR(){
		/*Scenario:: 1 Passing the PROD-DLR Expecting true*/
		String target="PROD-DLR";
		boolean result=false;
		result=ContentRules.isTargetDLR(target);
		assertEquals(true,result);
		/*Scenario:: 2 Passing the TEST-DLR Expecting true*/
		target="TEST-DLR";
		result=ContentRules.isTargetDLR(target);
		assertEquals(true,result);
		/*Scenario:: 3 Passing the TEST Expecting false*/
		target="TEST";
		result=ContentRules.isTargetDLR(target);
		assertEquals(false,result);
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
