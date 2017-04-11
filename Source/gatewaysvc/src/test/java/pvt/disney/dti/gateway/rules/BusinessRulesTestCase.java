package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;
import java.util.ArrayList;

import mockit.Mock;
import mockit.MockUp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTICalmException;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.AssociateMediaToAccountRequestTO;
import pvt.disney.dti.gateway.data.CreateTicketRequestTO;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.EnvironmentType;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryReservationRequestTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.RenewEntitlementRequestTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.TickerateEntitlementRequestTO;
import pvt.disney.dti.gateway.data.UpdateTicketRequestTO;
import pvt.disney.dti.gateway.data.UpdateTransactionRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.UpgradeEntitlementRequestTO;
import pvt.disney.dti.gateway.data.VoidReservationRequestTO;
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.common.PaymentTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for Business Rules
 * 
 * @author rasto006
 *
 */
public class BusinessRulesTestCase extends CommonBusinessTest {
	private static boolean MOCK_INIT = false;
	private static boolean MOCK_COMMON_RULE = false;

	// CalmRules calmRules=null;

	/**
	 * Initializing the properties and initializing Business Rule.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		BusinessRules.initBusinessRules(setConfigProperty());
		setMockProperty();
		/* mocking the paymentLookup */
		//DTIMockUtil.mockPaymentLookUp();
		DTIMockUtil.processMockprepareAndExecuteSql();
		DTIMockUtil.mockEntAttribute();
		mockCalmRules();
	}

	/**
	 * Unit Test For applyBusinessRules. * * This will cover the method for
	 * applyCommonRules
	 * 
	 * @throws DTICalmException
	 * @throws DTIException
	 */
	@Test
	public void testApplyBusinessRules() throws DTICalmException, DTIException {
		DTITransactionTO dtiTxn = null;
		/* Test case for applyCommonRules :: START(private method) */
		dtiTxn = new DTITransactionTO(TransactionType.QUERYTICKET);

		MOCK_COMMON_RULE = true;
		createCommonRequest(dtiTxn, false, false, TEST_TARGET, TPI_CODE_WDW,
				false);
		/* Mocking the validateProviderTarget method to set the tpiCode as TEST */
		/* Scenario :: 1 Expecting a exception when entity is not active */
		mockValidateProviderTarget(dtiTxn, TEST_TARGET);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Expected Exception: "
					+ "Entity tsmac null tslocation null is not active in the DTI database.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.ENTITY_NOT_ACTIVE);
			assertEquals(
					"Entity tsmac null tslocation null is not active in the DTI database.",
					dtie.getLogMessage());
		}
		/* Making the entity as active. Actor value is null */
		createCommonRequest(dtiTxn, true, false, TEST_TARGET, TPI_CODE_WDW,
				true);
		/*
		 * Scenario :: 2 Expecting a exception attributeTOMap is null
		 * (attributeTOMap is value returned from AttributeKey.getEntAttribtues)
		 */
		/* Mocking the attributekey so as to return the null value */
		DTIMockUtil
				.mockNullResultProcessor("pvt.disney.dti.gateway.dao.result.AttributeResult");
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Expected Exception: "
					+ "Client requesting transaction did not have tags required by attributes.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.COMMAND_NOT_AUTHORIZED);
			assertEquals(
					"Client requesting transaction did not have tags required by attributes.",
					dtie.getLogMessage());
			assertNull(dtiTxn.getAttributeTOMap());
		}
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW, true);
		/*
		 * Scenario :: 3 Expecting a exception :: Request attempted outside of
		 * configured time windows mocking the attributeKey to populate the
		 * attributeTOMap Mocking is there active only for
		 */
		DTIMockUtil.mockEntAttribute();
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Expected Exception: Request attempted outside of configured time windows.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_SALES_DATE_TIME);
			assertEquals(
					"Request attempted outside of configured time windows.",
					dtie.getLogMessage());
		}
		/* Scenario :: 4 Passing the NEX01 for validation */
		/* Making the entity as active making the actor as SYS */
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		createQueryTicketRequest(dtiTxn, TPI_CODE_WDW);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/* For Asserting the records fetched */
		assertNotNull(dtiTxn.getAttributeTOMap());
		/*assertNotNull(dtiTxn.getPaymentLookupTOList());*/
		assertEquals(TPI_CODE_WDW, dtiTxn.getTpiCode());
		/*-------------TEST END FOR TPICODE NEX01--------------------*/
		/* Scenario :: 5 Passing the DLR01 for validation */
		mockValidateProviderTarget(dtiTxn, PROD_TARGET);
		createQueryTicketRequest(dtiTxn, TPI_CODE_DLR);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		assertEquals(TPI_CODE_DLR, dtiTxn.getTpiCode());
		/*-------------TEST END FOR TPICODE DLR01--------------------*/
		/* Scenario :: 6 Passing the HKD01 for validation */
		mockValidateProviderTarget(dtiTxn, PROD_HKD_TARGET);
		createQueryTicketRequest(dtiTxn, TPI_CODE_DLR);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		assertEquals(TPI_CODE_HKD, dtiTxn.getTpiCode());
		/*-------------TEST END FOR TPICODE HKD01--------------------*/

		/* Scenario :: 7: Testing for exception for default */
		dtiTxn = new DTITransactionTO(TransactionType.UNDEFINED);
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);

		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.COMMAND_NOT_AUTHORIZED);
			assertEquals(
					"Transaction not supported in Java version of DTI Gateway.",
					dtie.getLogMessage());
		}
		/*-------------TEST CASE END applyBusinessRules--------------------*/
	}

	/**
	 * Test Case for applyQueryTicketRules:
	 * 
	 * @throws DTICalmException
	 * @throws DTIException
	 */
	@Test
	public void testApplyQueryTicketRules() throws DTICalmException,
			DTIException {
		DTITransactionTO dtiTxn = null;
		/* Test case for applyQueryTicketRules */
		dtiTxn = new DTITransactionTO(TransactionType.QUERYTICKET);
		/* Creating a common request universal to all */
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		VoidTicketRequestTO voidTicket = new VoidTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(voidTicket);
		DTIMockUtil.mockEntAttribute();
		/* Scenario 1:: Expecting a exception , wrong Command Body passed */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Expecting Exception::Internal Error:  Non-query class passed to validate.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-query class passed to validate.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario 2:: Expecting a success , NEX01 is passed as tpiCode and
		 * making the environment as TEST
		 */
		createQueryTicketRequest(dtiTxn, TPI_CODE_WDW);
		mockValidateProviderTarget(dtiTxn, TEST_TARGET);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/* Scenario 3:: Expecting a success , DLR01 is passed as tpiCode */
		createQueryTicketRequest(dtiTxn, TPI_CODE_DLR);
		mockValidateProviderTarget(dtiTxn, PROD_TARGET);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED-------------------------------------- */
		/*-------------TEST CASE END applyQueryTicketRules--------------------*/
	}

	/**
	 * Unit Test for applyUpgradeAlphaRules
	 * 
	 * @throws DTICalmException
	 * @throws DTIException
	 */
	@Test
	public void testApplyUpgradeAlphaRules() throws DTICalmException,
			DTIException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPGRADEALPHA);
		/* Creating a common request universal to all */
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		VoidTicketRequestTO voidTicket = new VoidTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(voidTicket);

		/* Scenario 1:: fetching the DTI_PROCESS_ERROR , wrong request passed */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Excpecting Exception:: Internal Error:  Non-upgrade alpha request class passed to applyUpgradeAlphaRules.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-upgrade alpha request class passed to applyUpgradeAlphaRules.",
					dtie.getLogMessage());
		}
		/* Scenario 2:: Expecting a success , NEX01 is passed as tpiCode */
		UpgradeAlphaRequestTO uaReqTO = new UpgradeAlphaRequestTO();
		uaReqTO.setTktList(getTicketList(TicketIdType.MAG_ID));
		uaReqTO.setEligibilityGroup("1");
		uaReqTO.setEligibilityMember("1");
		/* For Mocking the required objects */
		if (!MOCK_INIT) {
			mockUtilMethods();
			MOCK_INIT = true;
		}
		dtiTxn.getRequest().setCommandBody(uaReqTO);
		mockValidateProviderTarget(dtiTxn, TEST_TARGET);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/* Scenario 3:: Expecting a success , DLR01 is passed as tpiCode */
		uaReqTO = new UpgradeAlphaRequestTO();
		uaReqTO.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		uaReqTO.setEligibilityGroup("1");
		uaReqTO.setEligibilityMember("1");
		dtiTxn.getRequest().setCommandBody(uaReqTO);
		/* Making the environment to PROD to pass tpicode as DLR01 */
		mockValidateProviderTarget(dtiTxn, PROD_TARGET);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED---------------------------------------*/
		/*-------------TEST CASE END aplyUpgradeAlphaRules--------------------*/
	}

	/**
	 * For testing ApplyVoidTicketRules
	 */
	@Test
	public void testApplyVoidTicketRules() throws DTICalmException,
			DTIException {
		/* ---------For Provider Type WDWNEXUS tpicode-NEX01------------------ */
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.VOIDTICKET);
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		/*
		 * Scenario ::1 for fetching DTI_PROCESS_ERROR exception.Passing the
		 * invalid command body
		 */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Excepted Exception :: Internal Error:  Non-void ticket class passed to applyVoidTicketRules.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-void ticket class passed to applyVoidTicketRules.",
					dtie.getLogMessage());
		}
		/* Scenario 2:: Expecting a success , NEX01 is passed as tpiCode */
		VoidTicketRequestTO voidTicketRequest = new VoidTicketRequestTO();
		voidTicketRequest.setTktList(getTicketList(TicketIdType.TKTNID_ID));
		PaymentTO payment = new PaymentTO();
		ArrayList<PaymentTO> payListTO = new ArrayList<PaymentTO>();
		payListTO.add(payment);
		voidTicketRequest.setPaymentList(payListTO);
		// request.setCommandBody(upgrade);
		dtiTxn.getRequest().setCommandBody(voidTicketRequest);
		/* For Provider Type WDWNEXUS tpicode-NEX01 */
		mockValidateProviderTarget(dtiTxn, TEST_TARGET);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/* ---------For Provider Type DLRGATEWAY tpicode-DLR01------------------ */
		dtiTxn.setProvider(ProviderType.DLRGATEWAY);

		/* Scenario 3:: Expecting a success , DLR01 is passed as tpiCode */
		voidTicketRequest.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		dtiTxn.getRequest().setCommandBody(voidTicketRequest);
		mockValidateProviderTarget(dtiTxn, PROD_TARGET);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED-------------------------------------------- */
		/*-------------TEST CASE END applyVoidTicketRules--------------------*/
	}

	/**
	 * Unit Test case for applyUpdateTicketRules.
	 * 
	 * @throws DTICalmException
	 * @throws DTIException
	 */
	@Test
	public void testApplyUpdateTicketRules() throws DTICalmException,
			DTIException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPDATETICKET);
		/*
		 * Scenario ::1 for fetching DTI_PROCESS_ERROR exception.Passing the
		 * invalid command body
		 */
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		// request.setCommandBody(queryReq);
		dtiTxn.getRequest().setCommandBody(queryReq);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Expected Exception :: Internal Error:  Non-update ticket class passed to applyUpdateTicketRules.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-update ticket class passed to applyUpdateTicketRules.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario ::2 for Success Scenario
		 */
		UpdateTicketRequestTO updateTicket = new UpdateTicketRequestTO();
		updateTicket.setTicketList(getTicketList(TicketIdType.TKTNID_ID));
		dtiTxn.getRequest().setCommandBody(updateTicket);
		mockValidateProviderTarget(dtiTxn, TEST_TARGET);
		// DTIMockUtil.mockEntAttribute();

		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED-------------------------------------------- */
		/*-------------TEST CASE END applyUpdateTicketRules--------------------*/
	}

	/**
	 * Unit Test case for applyUpdateTransactionRules
	 */
	@Test
	public void testApplyUpdateTransactionRules() throws DTICalmException,
			DTIException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPDATETRANSACTION);
		DTIRequestTO request = new DTIRequestTO();
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);

		dtiTxn.getRequest().setCommandBody(queryReq);
		/*
		 * Scenario ::1 for fetching DTI_PROCESS_ERROR exception.Passing the
		 * invalid command body
		 */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Expected Exception :: Internal Error:  Non-update transaction class passed to applyUpdateTransactionRules");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-update transaction class passed to applyUpdateTransactionRules.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario ::2 for Success Scenario
		 */
		UpdateTransactionRequestTO updateTransaction = new UpdateTransactionRequestTO();
		/* Setting up only 1 ticket */
		TicketTO ticket = new TicketTO();
		ticket.setTktItem(new BigInteger("1"));
		ticket.setTktNID("12000507111600050");
		updateTransaction.setTicket(ticket);
		request.setCommandBody(updateTransaction);
		dtiTxn.getRequest().setCommandBody(updateTransaction);
		mockValidateProviderTarget(dtiTxn, TEST_TARGET);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED-------------------------------------------- */
		/*-------------TEST CASE END applyUpdateTransactionRules--------------------*/
	}

	/**
	 * Unit Test for applyQueryReservationRules
	 */
	@Test
	public void testApplyQueryReservationRules() throws DTICalmException,
			DTIException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYRESERVATION);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		dtiTxn.getRequest().setCommandBody(queryReq);
		/*
		 * Scenario ::1 for fetching DTI_PROCESS_ERROR exception.Passing the
		 * invalid command body
		 */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Expected Exception :: Non-query reservation transaction class passed to applyQueryReservationRules.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-query reservation transaction class passed to applyQueryReservationRules.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario ::2 for fetching Exception attempted with illegal
		 * reservation identifier type
		 */
		QueryReservationRequestTO queryReservation = new QueryReservationRequestTO();
		queryReservation.setResNumber("1");
		dtiTxn.getRequest().setCommandBody(queryReservation);
		mockValidateProviderTarget(dtiTxn, TEST_TARGET);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Expected Exception :: Query Reservation attempted with illegal reservation identifier type.  Must be ResCode.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
			assertEquals(
					"Query Reservation attempted with illegal reservation identifier type.  Must be ResCode.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario ::2 for SUCCESS
		 */
		queryReservation = new QueryReservationRequestTO();
		queryReservation.setResCode("1");
		queryReservation.setPayloadID("123");
		dtiTxn.getRequest().setCommandBody(queryReservation);
		mockValidateProviderTarget(dtiTxn, PROD_HKD_TARGET);
		DTIMockUtil.mockGetTransidRescodeFromDB();
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED-------------------------------------------- */
		/*-------------TEST CASE END applyQueryReservationRules--------------------*/
	}

	/**
	 * Test Case for ApplyVoidReservationRules
	 * 
	 * @throws DTICalmException
	 * @throws DTIException
	 */
	@Test
	public void testApplyVoidReservationRules() throws DTICalmException,
			DTIException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.VOIDRESERVATION);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		dtiTxn.getRequest().setCommandBody(queryReq);
		/*
		 * Scenario ::1 for fetching DTI_PROCESS_ERROR exception.Passing the
		 * invalid command body
		 */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Expected Exception :: Non-void reservation transaction class passed to applyVoidReservationRules.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-void reservation transaction class passed to applyVoidReservationRules.",
					dtie.getLogMessage());
		}
		/*
		 * Scenario ::2 for fetching Exception : Void Reservation attempted with
		 * illegal reservation identifier type. Must be ResCode.
		 */
		VoidReservationRequestTO queryResReqTO = new VoidReservationRequestTO();
		queryResReqTO.setExternalResCode("1");
		// queryResReqTO.setResCode("1");
		dtiTxn.getRequest().setCommandBody(queryResReqTO);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Excepted Exception :: Void Reservation attempted with illegal reservation identifier type.  Must be ResCode.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_CONTENT);
			assertEquals(
					dtie.getLogMessage(),
					"Void Reservation attempted with illegal reservation identifier type.  Must be ResCode.");
		}
		/*
		 * Scenario ::3 for SUCCESS
		 */
		queryResReqTO = new VoidReservationRequestTO();
		dtiTxn.getRequest().setCommandBody(queryResReqTO);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED-------------------------------------------- */
		/*-------------TEST CASE END applyVoidReservationRules--------------------*/
	}

	/**
	 * Test case for applyUpgradeEntitlementRules
	 * 
	 * @throws DTICalmException
	 * @throws DTIException
	 */
	@Test
	public void testApplyUpgradeEntitlementRules() throws DTICalmException,
			DTIException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPGRADEENTITLEMENT);
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		/* Scenario ::1 Expecting Exception Invalid Command Object */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Excepted Exception :: Internal Error:  Non-upgrade entitlement class passed to applyUpgradeEntitlementRules.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-upgrade entitlement class passed to applyUpgradeEntitlementRules.",
					dtie.getLogMessage());
		}
		UpgradeEntitlementRequestTO upgrdEntReqTO = new UpgradeEntitlementRequestTO();
		upgrdEntReqTO.setTicketList(getTicketList(TicketIdType.MAG_ID));
		dtiTxn.getRequest().setCommandBody(upgrdEntReqTO);
		mockValidateProviderTarget(dtiTxn, TEST_TARGET);
		/* For Mocking the required objects */
		if (!MOCK_INIT) {
			/* mockUtilMethods(); */
			MOCK_INIT = true;
		}

		/* Scenario ::2 Expecting the SUCCESS : Passing the required parameters */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED-------------------------------------------- */
		/*-------------TEST CASE END applyUpgradeEntitlementRules--------------------*/
	}

	/**
	 * Test Case for applyRenewEntitlementRules
	 * 
	 * @throws DTICalmException
	 * @throws DTIException
	 */
	@Test
	public void testApplyRenewEntitlementRules() throws DTICalmException,
			DTIException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RENEWENTITLEMENT);
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		/* Scenario ::1 Expecting Exception Invalid Command Object */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Excepted Exception :: Internal Error:  Non-renew entitlement transaction class passed to applyRenewEntitlementRules.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-renew entitlement transaction class passed to applyRenewEntitlementRules.",
					dtie.getLogMessage());
		}
		/* Passing the correct request */
		RenewEntitlementRequestTO renewEntReqTO = new RenewEntitlementRequestTO();
		renewEntReqTO.setTktList(getTicketList(TicketIdType.MAG_ID));
		renewEntReqTO.setEligibilityGroup("1");
		renewEntReqTO.setEligibilityMember("1");
		dtiTxn.getRequest().setCommandBody(renewEntReqTO);
		/* For setting up the test environment so as to pass NEX01 */
		mockValidateProviderTarget(dtiTxn, TEST_TARGET);
		/* For Mocking the required objects */
		if (!MOCK_INIT) {
			mockUtilMethods();
			MOCK_INIT = true;
		}
		/* Scenario 2:: Passing the piCode as NEX01 */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		renewEntReqTO = new RenewEntitlementRequestTO();
		getRenewEntitlementRequestTO(renewEntReqTO);
		dtiTxn.getRequest().setCommandBody(renewEntReqTO);
		mockValidateProviderTarget(dtiTxn, PROD_TARGET);

		/* Scenario ::3 Passing the piCode as DLR01 */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED-------------------------------------------- */
		/*-------------TEST CASE END applyRenewEntitlementRules--------------------*/
	}

	/**
	 * Test Case for applyCreateTicketRules
	 * 
	 * @throws DTICalmException
	 * @throws DTIException
	 */
	@Test
	public void testApplyCreateTicketRules() throws DTICalmException,
			DTIException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.CREATETICKET);
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		/* Scenario ::1 Expecting Exception Invalid Command Object */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Excepted Exception :: Internal Error:  Non-createticket class passed to applyCreateTicketRules.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-createticket class passed to applyCreateTicketRules.",
					dtie.getLogMessage());
		}
		CreateTicketRequestTO createTktTO = new CreateTicketRequestTO();
		createTktTO.setTicketList(getTicketList(TicketIdType.MAG_ID));
		createTktTO.setEligibilityGroup("1");
		createTktTO.setEligibilityMember("1");
		createTktTO.setAuditNotation("1");
		createTktTO.setDefaultAccount("1");
		dtiTxn.getRequest().setCommandBody(createTktTO);
		mockValidateProviderTarget(dtiTxn, TEST_TARGET);
		/* For Mocking the required objects */
		if (!MOCK_INIT) {
			mockUtilMethods();
			MOCK_INIT = true;
		}
		/* Scenario ::6 Success passing required parameters */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED-------------------------------------------- */
		/*-------------TEST CASE END applyCreateTicketRules--------------------*/
	}

	/**
	 * Test Case for apply Reservation
	 * 
	 * @throws DTICalmException
	 * @throws DTIException
	 */
	@Test
	public void testApplyReservationRules() throws DTICalmException,
			DTIException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RESERVATION);
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		/* Scenario ::1 Expecting Exception Invalid Command Object */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Excepted Exception :: Internal Error:  Non-reservation class passed to applyReservationRules.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-reservation class passed to applyReservationRules.",
					dtie.getLogMessage());
		}
		/* Scenario ::2 Expecting Exception ,No Request Type */
		ReservationRequestTO resReqTO = new ReservationRequestTO();
		dtiTxn.getRequest().setCommandBody(resReqTO);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_ELEMENT);
			assertEquals("No RequestType on reservation.", dtie.getLogMessage());

		}
		/* Scenario ::3 Expecting Exception ,Invalid Request Type */
		resReqTO = new ReservationRequestTO();
		resReqTO.setRequestType("1");
		dtiTxn.getRequest().setCommandBody(resReqTO);
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(),
					DTIErrorCode.INVALID_MSG_ELEMENT);
			assertEquals(
					"Invalid RequestType on reservation:"
							+ resReqTO.getRequestType(), dtie.getLogMessage());
		}
		resReqTO = new ReservationRequestTO();
		getReservationRequest(resReqTO);
		dtiTxn.getRequest().setCommandBody(resReqTO);
		mockValidateProviderTarget(dtiTxn, TEST_TARGET);
		/* For Mocking the required objects */
		if (!MOCK_INIT) {
			mockUtilMethods();
			MOCK_INIT = true;
		}
		/* Scenario ::4 Success passing NEX01 as tpiCode */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			//Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		mockValidateProviderTarget(dtiTxn, PROD_TARGET);
		/* Scenario ::5 Success passing DLR01 as tpiCode */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		mockValidateProviderTarget(dtiTxn, PROD_HKD_TARGET);
		/* Scenario ::6 Success passing HKD01 as tpiCode */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}

		/*-------------TEST CASE PASSED-------------------------------------------- */
		/*-------------TEST CASE END applyReservationRules--------------------*/
	}

	/**
	 * Test Case for applyTickerateEntitlementRules
	 */
	@Test
	public void testApplyTickerateEntitlementRules() throws DTICalmException,
			DTIException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.TICKERATEENTITLEMENT);
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		/* Scenario ::1 Expecting Exception Invalid Command Object */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
			Assert.fail("Expected Exception:: Internal Error:  Non-TickerateEntitlement class passed to applyTickerateEntitlementRules.");
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-TickerateEntitlement class passed to applyTickerateEntitlementRules.",
					dtie.getLogMessage());
		}
		TickerateEntitlementRequestTO tickerateEntitlementReq = new TickerateEntitlementRequestTO();
		tickerateEntitlementReq
				.setTickets(getTicketList(TicketIdType.TKTNID_ID));
		tickerateEntitlementReq.setMediaData(getMediaDataList());
		dtiTxn.getRequest().setCommandBody(tickerateEntitlementReq);
		/* Scenario ::2 Expecting Success passing all required parameters */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED-------------------------------------------- */
		/*-------------TEST CASE END applyTickerateEntitlementRules--------------------*/

	}

	/* Test Case for applyAssociateMediaToAccountRules */
	@Test
	public void testApplyAssociateMediaToAccountRules()
			throws DTICalmException, DTIException {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.ASSOCIATEMEDIATOACCOUNT);
		createCommonRequest(dtiTxn, true, true, TEST_TARGET, TPI_CODE_WDW,
				false);
		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		/* Scenario ::1 Expecting Exception Invalid Command Object */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.DTI_PROCESS_ERROR);
			assertEquals(
					"Internal Error:  Non-AssociateMedia class passed to applyAssociateMediaToAccountRules.",
					dtie.getLogMessage());
		}
		AssociateMediaToAccountRequestTO associateMediaReq = new AssociateMediaToAccountRequestTO();
		associateMediaReq.setMediaData(getMediaDataList());
		dtiTxn.getRequest().setCommandBody(associateMediaReq);
		/* Scenario ::2 Expecting Success passing all required parameters */
		try {
			BusinessRules.applyBusinessRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getLogMessage());
		}
		/*-------------TEST CASE PASSED-------------------------------------------- */
		/*-------------TEST CASE END applyAssociateMediaToAccountRules--------------------*/
	}

	
	

	/**
	 * For Mocking the calm Rules
	 */
	public void mockCalmRules() {
		new MockUp<CalmRules>() {
			@Mock
			public void checkContingencyActionsLogicModule(
					DTITransactionTO dtiTxn) {

			}
		};
	}
}
