package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.data.ITPLogEntry;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

public class TestLogKey extends CommonTestDao {
	@Before
	public void setUp() throws Exception {
		setMockProperty();
	}

	/**
	 * Test Case for getITPLogEntry
	 */
	@Test
	public void testGetITPLogEntry() {
		ITPLogEntry result = null;
		long tpTransId = -1;

		/* Scenario::1 Passing tpTransId as 0 */
		try {
			LogKey.getITPLogEntry(tpTransId);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_FAILURE, dtie.getDtiErrorCode());
			assertEquals("Invalid tpTransId passed to getITPLogEntry",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		tpTransId = 1;
		try {
			LogKey.getITPLogEntry(tpTransId);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("Exception executing getITPLogEntry",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}
		try {
			result = LogKey.getITPLogEntry(tpTransId);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

	}

	/**
	 * Test Case for getITSLogTransId
	 */
	@Test
	public void testGetITSLogTransId() {
		Integer result = null;
		String payloadId = null;

		/* Scenario::1 Passing tpTransId as 0 */
		try {
			LogKey.getITSLogTransId(payloadId);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.UNDEFINED_FAILURE, dtie.getDtiErrorCode());
			assertEquals("Null payloadId passed to getITSLogTransId",
					dtie.getLogMessage());
		}
		/* Scenario::2 Passing object without mocking DB */
		payloadId = "1";
		try {
			LogKey.getITSLogTransId(payloadId);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("Exception executing getITSLogTransId",
					dtie.getLogMessage());
		}
		/* Scenario::3 Passing object after mocking DB */
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}
		try {
			result = LogKey.getITSLogTransId(payloadId);
			assertNotNull(result);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

	}

	/**
	 * Test Case for insertITSLog
	 */
	@Test
	public void testInsertITSLog() {
		String payloadId = null;
		EntityTO entityTO = new EntityTO();
		entityTO.setEntityId(2);
		Integer transIdITS = null;
		String inXMLString = "<Envelope>" + "<Header>"
				+ "<SourceID>WDPRONADLR</SourceID>"
				+ "<MessageID>0</MessageID>"
				+ "<MessageType>CreateTickets</MessageType>" + "<TimeStamp>"
				+ DLRTestUtil.getDLRTimeStamp()
				+ "</TimeStamp>"
				+ "</Header>"
				+ "<Body>"
				+ "<TicketCreation>"
				+ "<CustomerID>300000783</CustomerID>"
				+ "<Tickets>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "</Tickets>"
				+ "<Payments>"
				+ "<Payment>"
				+ "<PaymentCode>42</PaymentCode>"
				+ "<Description>Charge</Description>"
				+ "<Endorsement>123456789</Endorsement>"
				+ "<Amount>358.00</Amount>"
				+ "</Payment>"
				+ "</Payments>"
				+ "</TicketCreation>" + "</Body>" + "</Envelope>";
		/* Scenario::1 Passing object without mocking DB */
		try {
			LogKey.insertITSLog(payloadId, entityTO, transIdITS, inXMLString);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("Exception executing insertITSLog",
					dtie.getLogMessage());
		}

		/* Scenario::2 Passing object after mocking DB */
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}
		try {
			LogKey.insertITSLog(payloadId, entityTO, transIdITS, inXMLString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());

		}
	}

	/**
	 * Test Case for getLogTableKeys
	 */
	@Test
	public void testGetLogTableKeys() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			LogKey.getLogTableKeys(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

	}

	/**
	 * Test case for insertOTSLog
	 */
	@Test
	public void testInsertOTSLog() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		createCommonResponse(dtiTxn);
		BigInteger errorCodeIn = new BigInteger("2345");
		DTIErrorTO dtiErrorIn = new DTIErrorTO(errorCodeIn, "errorClassIn",
				"errorTextIn", " errorTypeIn");
		dtiTxn.getResponse().setDtiError(dtiErrorIn);
		String inXMLString = "<Envelope>" + "<Header>"
				+ "<SourceID>WDPRONADLR</SourceID>"
				+ "<MessageID>0</MessageID>"
				+ "<MessageType>CreateTickets</MessageType>" + "<TimeStamp>"
				+ DLRTestUtil.getDLRTimeStamp()
				+ "</TimeStamp>"
				+ "</Header>"
				+ "<Body>"
				+ "<TicketCreation>"
				+ "<CustomerID>300000783</CustomerID>"
				+ "<Tickets>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "</Tickets>"
				+ "<Payments>"
				+ "<Payment>"
				+ "<PaymentCode>42</PaymentCode>"
				+ "<Description>Charge</Description>"
				+ "<Endorsement>123456789</Endorsement>"
				+ "<Amount>358.00</Amount>"
				+ "</Payment>"
				+ "</Payments>"
				+ "</TicketCreation>" + "</Body>" + "</Envelope>";
		try {
			LogKey.insertOTSLog(dtiTxn, inXMLString);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
		}
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}

		try {
			LogKey.insertOTSLog(dtiTxn, inXMLString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		dtiTxn.setTransIdOTP(new Integer(1));
		dtiTxn.setTransIdITP(new Integer(1));
		try {
			LogKey.insertOTSLog(dtiTxn, inXMLString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		dtiTxn.setTransIdOTP(new Integer(1));
		dtiTxn.setTransIdITP(new Integer(1));
		dtiTxn.setLoggedOTP(true);
		try {
			LogKey.insertOTSLog(dtiTxn, inXMLString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		dtiTxn.getResponse().setDtiError(null);

		dtiTxn.setTransIdITP(null);
		try {
			LogKey.insertOTSLog(dtiTxn, inXMLString);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("Exception executing insertOTSLog",
					dtie.getLogMessage());
		}
		dtiTxn.setTransIdITP(new Integer(1));
		dtiTxn.setTransIdOTP(null);
		try {
			LogKey.insertOTSLog(dtiTxn, inXMLString);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
			assertEquals("Exception executing insertOTSLog",
					dtie.getLogMessage());
		}

	}

	@Test
	public void testInsertITPLog() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		createCommonResponse(dtiTxn);
		BigInteger errorCodeIn = new BigInteger("2345");
		DTIErrorTO dtiErrorIn = new DTIErrorTO(errorCodeIn, "errorClassIn",
				"errorTextIn", " errorTypeIn");
		dtiTxn.getResponse().setDtiError(dtiErrorIn);
		dtiTxn.setTpRefNum(new Integer(1));
		String inXMLString = "<Envelope>" + "<Header>"
				+ "<SourceID>WDPRONADLR</SourceID>"
				+ "<MessageID>0</MessageID>"
				+ "<MessageType>CreateTickets</MessageType>" + "<TimeStamp>"
				+ DLRTestUtil.getDLRTimeStamp()
				+ "</TimeStamp>"
				+ "</Header>"
				+ "<Body>"
				+ "<TicketCreation>"
				+ "<CustomerID>300000783</CustomerID>"
				+ "<Tickets>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "</Tickets>"
				+ "<Payments>"
				+ "<Payment>"
				+ "<PaymentCode>42</PaymentCode>"
				+ "<Description>Charge</Description>"
				+ "<Endorsement>123456789</Endorsement>"
				+ "<Amount>358.00</Amount>"
				+ "</Payment>"
				+ "</Payments>"
				+ "</TicketCreation>" + "</Body>" + "</Envelope>";
		try {
			LogKey.insertITPLog(dtiTxn, inXMLString);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
		}
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}

		try {
			LogKey.insertITPLog(dtiTxn, inXMLString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}

	/**
	 * Test Case for insertOTPLog
	 */
	@Test
	public void testInsertOTPLog() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		createCommonResponse(dtiTxn);
		BigInteger errorCodeIn = new BigInteger("2345");
		DTIErrorTO dtiErrorIn = new DTIErrorTO(errorCodeIn, "errorClassIn",
				"errorTextIn", " errorTypeIn");
		dtiTxn.getResponse().setDtiError(dtiErrorIn);
		dtiTxn.setTpRefNum(new Integer(1));
		String inXMLString = "<Envelope>" + "<Header>"
				+ "<SourceID>WDPRONADLR</SourceID>"
				+ "<MessageID>0</MessageID>"
				+ "<MessageType>CreateTickets</MessageType>" + "<TimeStamp>"
				+ DLRTestUtil.getDLRTimeStamp()
				+ "</TimeStamp>"
				+ "</Header>"
				+ "<Body>"
				+ "<TicketCreation>"
				+ "<CustomerID>300000783</CustomerID>"
				+ "<Tickets>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "</Tickets>"
				+ "<Payments>"
				+ "<Payment>"
				+ "<PaymentCode>42</PaymentCode>"
				+ "<Description>Charge</Description>"
				+ "<Endorsement>123456789</Endorsement>"
				+ "<Amount>358.00</Amount>"
				+ "</Payment>"
				+ "</Payments>"
				+ "</TicketCreation>" + "</Body>" + "</Envelope>";
		try {
			LogKey.insertOTPLog(dtiTxn, inXMLString);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
		}
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}

		try {
			LogKey.insertOTPLog(dtiTxn, inXMLString);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

	}

	/**
	 * Test Case for insertDTITransLog
	 */
	@Test
	public void testInsertDTITransLog() {
		Integer tpRefNumber = new Integer(1);
		String payloadId = "payload";
		EntityTO entityTO = new EntityTO();
		String target = "TEST_DLR";
		String broker = "broker";
		Integer transIdITS = new Integer(1);
		boolean isFirstAttempt = false;
		try {
			LogKey.insertDTITransLog(tpRefNumber, payloadId, entityTO, target,
					broker, transIdITS, isFirstAttempt);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
		}
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}

		try {
			LogKey.insertDTITransLog(tpRefNumber, payloadId, entityTO, target,
					broker, transIdITS, isFirstAttempt);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}

	/**
	 * Test case for updateDTITransLogITP
	 */
	@Test
	public void testUpdateDTITransLogITP() {
		Integer tpRefNumber = new Integer(1);
		Date trans_date = new Date();
		Integer cmdid = new Integer(1);
		String cmdInvoice = "cmd";
		Integer transIdITS = new Integer(1);
		try {
			LogKey.updateDTITransLogITP(tpRefNumber, trans_date, cmdid,
					cmdInvoice, transIdITS);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
		}
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}

		try {
			LogKey.updateDTITransLogITP(tpRefNumber, trans_date, cmdid,
					cmdInvoice, transIdITS);
		} catch (DTIException dtie) {
			//Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}

	/**
	 * Test case for updateDTITransLogOTP
	 */
	@Test
	public void testUpdateDTITransLogOTP() {
		Integer tpRefNumber = new Integer(1);
		Integer transIdOTP = new Integer(1);
		try {
			LogKey.updateDTITransLogOTP(tpRefNumber, transIdOTP);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
		}
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}
		try {
			LogKey.updateDTITransLogOTP(tpRefNumber, transIdOTP);
		} catch (DTIException dtie) {
			//Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

	}

	/**
	 * Test case for insertOTSLogError
	 */
	@Test
	public void testInsertOTSLogError() {
		Integer otsLogKey = new Integer(1);
		String tsPayloadId = "tsPayload";
		String inXMLString = "<Envelope>" + "<Header>"
				+ "<SourceID>WDPRONADLR</SourceID>"
				+ "<MessageID>0</MessageID>"
				+ "<MessageType>CreateTickets</MessageType>" + "<TimeStamp>"
				+ DLRTestUtil.getDLRTimeStamp()
				+ "</TimeStamp>"
				+ "</Header>"
				+ "<Body>"
				+ "<TicketCreation>"
				+ "<CustomerID>300000783</CustomerID>"
				+ "<Tickets>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "</Tickets>"
				+ "<Payments>"
				+ "<Payment>"
				+ "<PaymentCode>42</PaymentCode>"
				+ "<Description>Charge</Description>"
				+ "<Endorsement>123456789</Endorsement>"
				+ "<Amount>358.00</Amount>"
				+ "</Payment>"
				+ "</Payments>"
				+ "</TicketCreation>" + "</Body>" + "</Envelope>";
		String errorCode = "errCode";
		Integer transIdITS = new Integer(1);
		try {
			LogKey.insertOTSLogError(otsLogKey, tsPayloadId, inXMLString,
					errorCode, transIdITS);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
		}
		if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}
		try {
			LogKey.insertOTSLogError(otsLogKey, tsPayloadId, inXMLString,
					errorCode, transIdITS);
		} catch (DTIException dtie) {
			//Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}

	/**
	 * Test case for updateDTITransLogOTS
	 */
	@Test
	public void testUpdateDTITransLogOTS() {
		Integer tpRefNumber = new Integer(1);
		Integer transIdOTP = new Integer(1);
		String errReturnCode = "errorReturnCode";
		String errName = "errName";
		String providerErrCode = "providerErrCode";
		String providerErrName = "providerErrName";
		Date trans_date = new Date();
		TransactionType txnType = TransactionType.CREATETICKET;
		String cmdInvoice = "cmdInvoice";

		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dtie.getDtiErrorCode());
		}

		/*if(!DTIMockUtil.mocking){
			DTIMockUtil.processMockprepareAndExecuteSql();	
		}*/
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

		txnType = TransactionType.QUERYTICKET;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

		txnType = TransactionType.UPGRADEALPHA;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

		txnType = TransactionType.VOIDTICKET;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

		txnType = TransactionType.RESERVATION;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

		txnType = TransactionType.UPDATETICKET;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		txnType = TransactionType.UPDATETRANSACTION;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		txnType = TransactionType.QUERYRESERVATION;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		txnType = TransactionType.UPGRADEENTITLEMENT;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		txnType = TransactionType.RENEWENTITLEMENT;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

		txnType = TransactionType.ASSOCIATEMEDIATOACCOUNT;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

		txnType = TransactionType.TICKERATEENTITLEMENT;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}

		txnType = TransactionType.VOIDRESERVATION;
		try {
			LogKey.updateDTITransLogOTS(tpRefNumber, transIdOTP, errReturnCode,
					errName, providerErrCode, providerErrName, trans_date,
					txnType, cmdInvoice);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
}
