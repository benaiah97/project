package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO.OTTransactionType;
import pvt.disney.dti.gateway.provider.wdw.data.OTQueryTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.rules.BusinessRules;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for WDWQueryTicketRules.
 *
 * @author ARORT002
 */
public class WDWQueryTicketRulesTestCase extends CommonTestUtils {

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		BusinessRules.initBusinessRules(setConfigProperty());
		setMockProperty();
	}

	/**
	 * test case for transformRequest method.
	 */
	@Test
	public void testTransformRequest() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		QueryTicketRequestTO dtiQryTkt = new QueryTicketRequestTO();
		ArrayList<TicketTO> dtiTicketList = new ArrayList<TicketTO>();
		TicketTO ticketTOBarCode = new TicketTO();
		ticketTOBarCode.setBarCode("123");
		dtiTicketList.add(ticketTOBarCode);

		dtiQryTkt.setTktList(dtiTicketList);
		dtiTxn.getRequest().setCommandBody(dtiQryTkt);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		String xmlString = null;

		try {
			xmlString = WDWQueryTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		dtiTicketList.clear();
		TicketTO ticketTODssn = new TicketTO();
		ticketTODssn.setDssn(new GregorianCalendar(), "123", "456", "789");
		dtiTicketList.add(ticketTODssn);

		dtiQryTkt.setTktList(dtiTicketList);
		dtiTxn.getRequest().setCommandBody(dtiQryTkt);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		try {
			xmlString = WDWQueryTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		dtiTicketList.clear();
		TicketTO ticketTOTknId = new TicketTO();
		ticketTOTknId.setTktNID("123");
		dtiTicketList.add(ticketTOTknId);

		dtiQryTkt.setTktList(dtiTicketList);
		dtiTxn.getRequest().setCommandBody(dtiQryTkt);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		try {
			xmlString = WDWQueryTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		dtiTicketList.clear();
		TicketTO ticketTOMag = new TicketTO();
		ticketTOMag.setMag("123");
		dtiTicketList.add(ticketTOMag);

		dtiQryTkt.setTktList(dtiTicketList);
		dtiTxn.getRequest().setCommandBody(dtiQryTkt);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		try {
			xmlString = WDWQueryTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		dtiTicketList.clear();
		TicketTO ticketTOExtTicketCode = new TicketTO();
		ticketTOExtTicketCode.setExternal("456");
		dtiTicketList.add(ticketTOExtTicketCode);

		dtiQryTkt.setTktList(dtiTicketList);
		dtiTxn.getRequest().setCommandBody(dtiQryTkt);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		try {
			xmlString = WDWQueryTicketRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
	}

	/**
	 * Test case for transformResponseBody.
	 */
	@Test
	public void testTransformResponseBody() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		OTTransactionType txType = OTTransactionType.QUERYTICKET;
		OTCommandTO otCmdTO = new OTCommandTO(txType);
		// otCmdTO.set
		OTQueryTicketTO otQryTktTO = new OTQueryTicketTO();
		ArrayList<OTTicketInfoTO> ticketList = new ArrayList<OTTicketInfoTO>();
		OTTicketInfoTO otTicketInfoTO = new OTTicketInfoTO();
		ticketList.add(otTicketInfoTO);
		// setter for ticketList not present in otQryTktTO
		otCmdTO.setQueryTicketTO(otQryTktTO);

		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(queryReq);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		try {
			WDWQueryTicketRules.transformResponseBody(dtiTxn, otCmdTO,
					dtiRespTO);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

	}

	/**
	 * Test apply wdw associate media to account rules.
	 */
	@Test
	public void testApplyWDWAssociateMediaToAccountRules() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);

		QueryTicketRequestTO reqTO = new QueryTicketRequestTO();
		dtiTxn.getRequest().setCommandBody(reqTO);
		try {
			WDWQueryTicketRules.applyWDWQueryTicketRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.INVALID_TICKET_ID);
			assertEquals("In-bound WDW txn with <> 1 TktId: 0",
					dtie.getLogMessage());
		}

	}

}
