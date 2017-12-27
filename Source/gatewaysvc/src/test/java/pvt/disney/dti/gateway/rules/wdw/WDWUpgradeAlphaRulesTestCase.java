package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO.OTTransactionType;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpgradeTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTransactionDSSNTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTUpgradeTicketInfoTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * JUnit TestCase for WDWUpgradeAlphaRules
 * @author RASTA006
 *
 */
public class WDWUpgradeAlphaRulesTestCase extends CommonTestUtils {
	@Before
	public void setUp() throws Exception {
		setMockProperty();
	}

	/**
	 * test case for transformRequest method
	 */
	@Test
	public void testTransformRequest() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPGRADEALPHA);
		createCommonRequest(dtiTxn);
		UpgradeAlphaRequestTO dtiUpgrdAlphaReq = new UpgradeAlphaRequestTO();
		dtiUpgrdAlphaReq.setTktList(getTicketList(TicketIdType.MAG_ID));
		dtiTxn.getRequest().setCommandBody(dtiUpgrdAlphaReq);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		String xmlString = null;
		dtiUpgrdAlphaReq.setEligibilityGroup("1");
		dtiUpgrdAlphaReq.setEligibilityMember("One");
		/* mocking prepareAndExecuteSql */
		DTIMockUtil.processMockprepareAndExecuteSql();
		/* Scenario:: 1 when paymentInfoList is empty*/
		try {
			xmlString = WDWUpgradeAlphaRules.transformRequest(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.INVALID_PAYMENT_AMOUNT,
					dtie.getDtiErrorCode());
		}
		/* Scenario:: 2 Mocking the paymentInfo List  */
		DTIMockUtil.mockGetPaymentInfoList();
		dtiTxn.getEntityTO().setDefSalesRepId(new Long(1));
		try {
			xmlString = WDWUpgradeAlphaRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		/* Scenario:: 3 passing the eligibility Group and eligibility member */
		dtiUpgrdAlphaReq.setEligibilityGroup("1");
		dtiUpgrdAlphaReq.setEligibilityMember("1");
		try {
			xmlString = WDWUpgradeAlphaRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
	}

	/**
	 * Test case for transformResponseBody
	 */
	@Test
	public void testTransformResponseBody() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPGRADEALPHA);
		createCommonRequest(dtiTxn);
		OTTransactionType txType = OTTransactionType.UPGRADETICKET;
		UpgradeAlphaRequestTO dtiUpgrdAlphaReq = new UpgradeAlphaRequestTO();
		dtiUpgrdAlphaReq.setTktList(getTicketList(TicketIdType.MAG_ID));
		dtiTxn.getRequest().setCommandBody(dtiUpgrdAlphaReq);
		OTCommandTO otCmdTO = new OTCommandTO(txType);
		OTUpgradeTicketTO upgradeTicketTO = new OTUpgradeTicketTO();
		ArrayList<OTUpgradeTicketInfoTO> upgradeTicketInfoList = new ArrayList<OTUpgradeTicketInfoTO>();
		upgradeTicketInfoList.add(new OTUpgradeTicketInfoTO());
		upgradeTicketInfoList.add(new OTUpgradeTicketInfoTO());
		upgradeTicketTO.setUpgradeTicketInfoList(upgradeTicketInfoList);
		otCmdTO.setUpgradeTicketTO(upgradeTicketTO);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		/* Scenario:: 1 when Ticket count and  upgradeTicketInfoList is not same*/
		try {
			WDWUpgradeAlphaRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.TP_INTERFACE_FAILURE,
					dtie.getDtiErrorCode());
		}
		/* Scenario:: 2 when Ticket count and  upgradeTicketInfoList is same */
		upgradeTicketTO.setUpgradeTicketInfoList(getOTUpgradeTicketInfo());
		OTTransactionDSSNTO transactionDSSN = new OTTransactionDSSNTO();
		transactionDSSN.setDate((GregorianCalendar) GregorianCalendar
				.getInstance());
		transactionDSSN.setSite("1");
		transactionDSSN.setStation("1");
		transactionDSSN.setTransactionId(new Integer(1));
		upgradeTicketTO.setTransactionDSSN(transactionDSSN);
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		dtiUpgrdAlphaReq.setIncludeVisualId(true);
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			WDWUpgradeAlphaRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
	}

	/**
	 * Test case for applyWDWUpgradeAlphaRules
	 */
	@Ignore
	@Test
	public void testApplyWDWUpgradeAlphaRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPGRADEALPHA);
		String mag1 = " AFTWU6SRSBUVRZSSUURYHGRS65RSRRRRRRRWFAWU6SRSBUVRZSSUURYHGRS65RSRRRRRRRW ";
		String mag2 = "01=222222";
		createCommonRequest(dtiTxn);
		UpgradeAlphaRequestTO dtiUpgrdAlphaReq = new UpgradeAlphaRequestTO();
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		for (TicketTO ticket : ticketList) {
			ticket.setMag(mag1, mag2);
		}
		dtiUpgrdAlphaReq.setTktList(ticketList);
		dtiTxn.getRequest().setCommandBody(dtiUpgrdAlphaReq);
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			WDWUpgradeAlphaRules.applyWDWUpgradeAlphaRules(dtiTxn);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
	}

	/**
	 * Method for fetching the list of upgradeTicketInfo
	 * 
	 * @return
	 */
	private ArrayList<OTUpgradeTicketInfoTO> getOTUpgradeTicketInfo() {
		ArrayList<OTUpgradeTicketInfoTO> upgradeTicketInfoList = new ArrayList<OTUpgradeTicketInfoTO>();
		OTUpgradeTicketInfoTO upgradeTicketinfo = new OTUpgradeTicketInfoTO();
		upgradeTicketinfo.setItem(new BigInteger("1"));
		upgradeTicketinfo.setTicket(getOTicket());
		upgradeTicketInfoList.add(upgradeTicketinfo);
		return upgradeTicketInfoList;
	}

	/**
	 * Common request for creation of ticket
	 * 
	 * @return
	 *//*
	private OTTicketTO getOTicket() {
		OTTicketTO ticket = new OTTicketTO();
		ticket.setTCOD("12000507111600050");
		ticket.setTDssn((GregorianCalendar) GregorianCalendar.getInstance(),
				"1", "1", "1");

		return ticket;
	}*/

}
