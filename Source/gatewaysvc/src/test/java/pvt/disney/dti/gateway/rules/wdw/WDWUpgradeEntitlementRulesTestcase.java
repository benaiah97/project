package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.UpgradeAlphaRequestTO;
import pvt.disney.dti.gateway.data.UpgradeEntitlementRequestTO;
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
 * jUnit Test Case for WDWUpgradeEntitlementRules
 * @author RASTA006
 *
 */
public class WDWUpgradeEntitlementRulesTestcase extends CommonTestUtils {
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
				TransactionType.UPGRADEENTITLEMENT);
		createCommonRequest(dtiTxn);
		UpgradeEntitlementRequestTO dtiUpgrdEntReq = new UpgradeEntitlementRequestTO();
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		for (TicketTO ticket : ticketList) {
			ticket.addTicketDemographic(getBillingInfo());
			ticket.setTicketAssignmets(getTicketAssingment(ticket));
			ticket.setTktNote("1");
			ticket.setTktShell("1");
			ticket.setTktValidityValidStart(new GregorianCalendar());
			ticket.setTktValidityValidEnd(new GregorianCalendar());
		}
		dtiUpgrdEntReq.setTicketList(ticketList);
		dtiTxn.getRequest().setCommandBody(dtiUpgrdEntReq);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		String xmlString = null;
		/* mocking prepareAndExecuteSql */
		DTIMockUtil.processMockprepareAndExecuteSql();
		/* Scenario:: 1 Passing the complete dtiTxn object */
		try {
			xmlString = WDWUpgradeEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		dtiUpgrdEntReq.setAuditNotation("1");
		try {
			xmlString = WDWUpgradeEntitlementRules.transformRequest(dtiTxn);
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
				TransactionType.UPGRADEENTITLEMENT);
		createCommonRequest(dtiTxn);
		OTTransactionType txType = OTTransactionType.UPGRADETICKET;
		UpgradeAlphaRequestTO dtiUpgrdAlphaReq = new UpgradeAlphaRequestTO();
		dtiUpgrdAlphaReq.setTktList(getTicketList(TicketIdType.MAG_ID));
		dtiTxn.getRequest().setCommandBody(dtiUpgrdAlphaReq);
		OTCommandTO otCmdTO = new OTCommandTO(txType);
		OTUpgradeTicketTO upgradeTicketTO = new OTUpgradeTicketTO();
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		/* Scenario:: 1 when Passing the dtiTxn Object */
		upgradeTicketTO.setUpgradeTicketInfoList(getOTUpgradeTicketInfo());
		OTTransactionDSSNTO transactionDSSN = new OTTransactionDSSNTO();
		transactionDSSN.setDate((GregorianCalendar) GregorianCalendar
				.getInstance());
		transactionDSSN.setSite("1");
		transactionDSSN.setStation("1");
		transactionDSSN.setTransactionId(new Integer(1));
		upgradeTicketTO.setTransactionDSSN(transactionDSSN);
		otCmdTO.setUpgradeTicketTO(upgradeTicketTO);
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		dtiUpgrdAlphaReq.setIncludeVisualId(true);
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			WDWUpgradeEntitlementRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
	}

	/**
	 * Test case for applyWDWUpgradeEntitlementRules
	 */
	// @Test
	public void testApplyWDWUpgradeEntitlementRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.UPGRADEENTITLEMENT);
		String mag1 = " AFTWU6SRSBUVRZSSUURYHGRS65RSRRRRRRRWFAWU6SRSBUVRZSSUURYHGRS65RSRRRRRRRW ";
		String mag2 = "01=222222";
		createCommonRequest(dtiTxn);
		UpgradeEntitlementRequestTO upgrdEntReqTO = new UpgradeEntitlementRequestTO();
		ArrayList<TicketTO> ticketList = getTicketList(TicketIdType.MAG_ID);
		for (TicketTO ticket : ticketList) {
			ticket.setMag(mag1, mag2);
		}
		upgrdEntReqTO.setTicketList(ticketList);
		dtiTxn.getRequest().setCommandBody(upgrdEntReqTO);
		dtiTxn.setDbProdList(DTIMockUtil.fetchDBTicketTypeList());
		DTIMockUtil.processMockprepareAndExecuteSql();
		try {
			WDWUpgradeEntitlementRules.applyWDWUpgradeEntitlementRules(dtiTxn);
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
		
}
