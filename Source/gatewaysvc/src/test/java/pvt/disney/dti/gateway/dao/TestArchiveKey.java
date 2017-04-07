package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;
import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.result.TxnLoggedResult;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.UpgradeAlphaResponseTO;
import pvt.disney.dti.gateway.data.VoidTicketRequestTO;
import pvt.disney.dti.gateway.data.VoidTicketResponseTO;
import pvt.disney.dti.gateway.data.common.DTIErrorTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Writing test case for Archivekey
 * 
 * @author RASTA006
 *
 */
public class TestArchiveKey extends CommonTestDao {
	TxnLoggedResult resultSet;

	/**
	 * test Case for insertUpgradeAlphaRequest
	 * 
	 * @throws DTIException
	 * @throws ParseException
	 */
	@Test
	public void testInsertUpgradeAlphaRequest() throws DTIException,
			ParseException {
		final DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPGRADEALPHA);
		createCommonRequest(dtiTxn);
		UpgradeAlphaRequestTO upgradeUlpha = new UpgradeAlphaRequestTO();
		upgradeUlpha.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		dtiTxn.getRequest().setCommandBody(upgradeUlpha);
		dtiTxn.setProvider(ProviderType.DLRGATEWAY);
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBOrderList());
		/* Scenario :: 1 passing the record without mocking the insertion */
		try {
			ArchiveKey.insertUpgradeAlphaRequest(dtiTxn);
		} catch (Exception e) {
			Assert.fail("Unexpected Exception::" + e.getMessage());
		}
		/* Scenario :: 2 passing the record after mocking the insertion */
		DTIMockUtil.processMockprepareAndExecuteSql();
		;
		try {
			ArchiveKey.insertUpgradeAlphaRequest(dtiTxn);
		} catch (Exception e) {
			Assert.fail("Unexpected Exception::" + e.getMessage());
		}
	}

	/**
	 * Test Case for updateUpgradeAlphaResponse
	 * 
	 * @throws DTIException
	 * @throws ParseException
	 */
	@Test
	public void testUpdateUpgradeAlphaResponse() throws DTIException,
			ParseException {

		final DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPGRADEALPHA);
		createCommonRequest(dtiTxn);
		createCommonResponse(dtiTxn);
		UpgradeAlphaResponseTO upgradeAlphaResponseTO = new UpgradeAlphaResponseTO();
		upgradeAlphaResponseTO.addTicket(getTicketTO(true, true));
		dtiTxn.getResponse().setCommandBody(upgradeAlphaResponseTO);
		dtiTxn.setProvider(ProviderType.DLRGATEWAY);
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBOrderList());
		/*
		 * Scenario :: 1 passing the request through doesRecordExistInDatabase
		 * without mocking it
		 */
		try {
			ArchiveKey.updateUpgradeAlphaResponse(dtiTxn);
		} catch (DTIException dti) {
			assertEquals("Exception executing updateUpgradeAlphaResponse",
					dti.getLogMessage());
		}
		/* Scenario :: 2 creating the DTIErrorTO object */
		BigInteger errorCodeIn = new BigInteger("2345");
		DTIErrorTO dtiErrorIn = new DTIErrorTO(errorCodeIn, "errorClassIn",
				"errorTextIn", " errorTypeIn");
		dtiTxn.getResponse().setDtiError(dtiErrorIn);
		try {
			ArchiveKey.updateUpgradeAlphaResponse(dtiTxn);
		} catch (DTIException dti) {
			assertEquals("Exception executing DELETE_UPGRADE_ALPHA_RESPONSE",
					dti.getLogMessage());
		}

		/* Mocking for process Query */
		/*DTIMockUtil
				.mockResultProcessor("pvt.disney.dti.gateway.dao.result.TxnLoggedResult");*/

		dtiTxn.getResponse().getPayloadHeader()
				.setPayloadID("1234567890123456789");
		dtiTxn.getResponse().setDtiError(dtiErrorIn);
		/* Mocking for update */
		DTIMockUtil.processMockprepareAndExecuteSql();
		;
		/* Scenario :: 3 Mocking the object */
		try {
			ArchiveKey.updateUpgradeAlphaResponse(dtiTxn);
		} catch (DTIException dti) {
			Assert.fail("Unexpected Exception::" + dti.getLogMessage());
		}
	}

	/**
	 * Test case for insertVoidTicketRequest
	 * 
	 * @throws ParseException
	 * @throws DTIException
	 */
	@Test
	public void testInsertVoidTicketRequest() throws ParseException,
			DTIException {
		final DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.VOIDTICKET);
		createCommonRequest(dtiTxn);
		VoidTicketRequestTO voidTicketRequestTO = new VoidTicketRequestTO();
		voidTicketRequestTO.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		dtiTxn.getRequest().setCommandBody(voidTicketRequestTO);
		dtiTxn.setProvider(ProviderType.DLRGATEWAY);
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBOrderList());
		/* Scenario :: 1 passing the record without mocking the insertion */
		try {
			ArchiveKey.insertVoidTicketRequest(dtiTxn);
		} catch (Exception e) {
			Assert.fail("Unexpected Exception::" + e.getMessage());
		}
		/* Scenario :: 2 passing the record after mocking the insertion */
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			ArchiveKey.insertVoidTicketRequest(dtiTxn);
		} catch (Exception e) {
			Assert.fail("Unexpected Exception::" + e.getMessage());
		}
	}

	/**
	 * Test case for updateVoidTicketResponse
	 * 
	 * @throws ParseException
	 * @throws DTIException
	 */
	@Test
	public void testUpdateVoidTicketResponse() throws ParseException,
			DTIException {

		final DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.VOIDTICKET);
		createCommonRequest(dtiTxn);
		createCommonResponse(dtiTxn);
		TicketTO ticketTO = getTicketTO(false, false);
		VoidTicketResponseTO voidTicketResponseTO = new VoidTicketResponseTO();
		voidTicketResponseTO.addTicket(ticketTO);
		dtiTxn.getResponse().setCommandBody(voidTicketResponseTO);
		dtiTxn.setProvider(ProviderType.DLRGATEWAY);
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBOrderList());
		/*
		 * Scenario :: 1 Providing the ProviderTicketType as 0 and not mocking
		 * the delete method
		 */

		try {
			ArchiveKey.updateVoidTicketResponse(dtiTxn);
		} catch (DTIException dti) {
			assertEquals("Exception executing UPDATE_VOID_TICKET_RESPONSE",
					dti.getLogMessage());
		}
		/*
		 * Scenario :: 2 Providing the DTIErrorTO object
		 */
		BigInteger errorCodeIn = new BigInteger("2345");
		DTIErrorTO dtiErrorIn = new DTIErrorTO(errorCodeIn, "errorClassIn",
				"errorTextIn", " errorTypeIn");
		dtiTxn.getResponse().setDtiError(dtiErrorIn);
		try {
			ArchiveKey.updateVoidTicketResponse(dtiTxn);
		} catch (DTIException dti) {
			assertEquals("Exception executing DELETE_VOID_TICKET_RESPONSE",
					dti.getLogMessage());
		}
		/*
		 * Scenario :: 3 Providing the ProviderTicketType as 0 and not mocking
		 * the delete
		 */
		try {
			dtiTxn.getResponse().setDtiError(null);
			ArchiveKey.updateVoidTicketResponse(dtiTxn);
		} catch (DTIException dti) {
			assertEquals("Exception executing UPDATE_VOID_TICKET_RESPONSE",
					dti.getLogMessage());
		}
		/*
		 * Scenario :: 4 Mocking the Dao helper Class
		 */
		DTIMockUtil.processMockprepareAndExecuteSql();
		;
		dtiTxn.getRequest().getPayloadHeader().setTarget("TEST-DLR");
		dtiTxn.getResponse().setDtiError(null);
		voidTicketResponseTO = new VoidTicketResponseTO();
		voidTicketResponseTO.addTicket(getTicketTO(true, true));
		dtiTxn.getResponse().setCommandBody(voidTicketResponseTO);
		try {
			ArchiveKey.updateVoidTicketResponse(dtiTxn);
		} catch (DTIException dti) {
			Assert.fail("Unexpected Exception ::" + dti.getLogMessage());
		}
		
		 /* Scenario :: 5 Setting the target as TEST-DLR*/
		 
		// dtiTxn.setProvider(ProviderType.HKDNEXUS);
		DTIMockUtil.processMockprepareAndExecuteSql();
		;
		dtiTxn.getRequest().getPayloadHeader().setTarget("TEST-DLR");
		dtiTxn.getResponse().setDtiError(null);
		voidTicketResponseTO = new VoidTicketResponseTO();
		voidTicketResponseTO.addTicket(getTicketTO(true, false));
		dtiTxn.getResponse().setCommandBody(voidTicketResponseTO);
		
		try {
			ArchiveKey.updateVoidTicketResponse(dtiTxn);
		} catch (DTIException dti) {
			Assert.fail("Unexpected Exception ::" + dti.getLogMessage());
		}
		/*
		 * Scenario :: 6 Setting the target as ANIMEE
		 */
		// dtiTxn.setProvider(ProviderType.HKDNEXUS);
		dtiTxn.getRequest().getPayloadHeader().setTarget("ANIMEE");
		dtiTxn.getResponse().setDtiError(null);
		voidTicketResponseTO = new VoidTicketResponseTO();
		voidTicketResponseTO.addTicket(getTicketTO(true, false));
		dtiTxn.getResponse().setCommandBody(voidTicketResponseTO);
		try {
			ArchiveKey.updateVoidTicketResponse(dtiTxn);
		} catch (DTIException dti) {
			assertEquals("Exception executing UPDATE_VOID_TICKET_RESPONSE",
					dti.getLogMessage());
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC,
					dti.getDtiErrorCode());
		}
	}
}
