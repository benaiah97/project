package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
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
import pvt.disney.dti.gateway.provider.wdw.data.common.OTDemographicData;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
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
		OTTicketInfoTO otTicketInfoTO = new OTTicketInfoTO();
		otTicketInfoTO.setTicketAttribute(1);
		otTicketInfoTO.setAlreadyused(true);
		otTicketInfoTO.setDenyMultiEntitlementFunctions(true);
		otTicketInfoTO.setAccountId("123");
		otTicketInfoTO.setTicketType(BigInteger.valueOf(1));
		otTicketInfoTO.setValidityStartDate(new GregorianCalendar());
		otTicketInfoTO.setValidityEndDate(new GregorianCalendar());
		otTicketInfoTO.setVoidCode(0);
		OTDemographicData seasonPassDemo = new OTDemographicData();
		ArrayList<OTFieldTO> demoDataList = new ArrayList<OTFieldTO>();
		OTFieldTO anOTField = new OTFieldTO(1, "abc/def");
		OTFieldTO anOTFieldTwo = new OTFieldTO(2, "02/02/17");
		OTFieldTO anOTFieldThree = new OTFieldTO(3, "abc/def");
		OTFieldTO anOTFieldAddressOne = new OTFieldTO(4, "abc/def");
		OTFieldTO anOTFieldAddressTwo = new OTFieldTO(5, "abc/def");
		OTFieldTO anOTFieldSix = new OTFieldTO(6, "abc/def");
		OTFieldTO anOTFieldSeven = new OTFieldTO(7, "abc/def");
		OTFieldTO anOTFieldEight = new OTFieldTO(8, "abc/def");
		OTFieldTO anOTFieldNine = new OTFieldTO(9, "abc/def");
		OTFieldTO anOTFieldTen = new OTFieldTO(10, "abc/def");
		OTFieldTO anOTFieldTwelve = new OTFieldTO(12, "abc@gmail.com");
		demoDataList.add(anOTField);
		demoDataList.add(anOTFieldTwo);
		demoDataList.add(anOTFieldThree);
		demoDataList.add(anOTFieldAddressOne);
		demoDataList.add(anOTFieldAddressTwo);
		demoDataList.add(anOTFieldSix);
		demoDataList.add(anOTFieldSeven);
		demoDataList.add(anOTFieldEight);
		demoDataList.add(anOTFieldNine);
		demoDataList.add(anOTFieldTen);
		demoDataList.add(anOTFieldTwelve);
		seasonPassDemo.setDemoDataList(demoDataList);
		otTicketInfoTO.setSeasonPassDemo(seasonPassDemo);
		otQryTktTO.getTicketInfoList().add(otTicketInfoTO);
		
		 OTTicketTO otTkt = new OTTicketTO();
		 otTkt.setTDssn(new GregorianCalendar(), "123", "456", "789");
		 otTkt.setTCOD("123");
		 otTkt.setBarCode("123");
		 otTkt.setMagTrack("123");
		 otTkt.setExternalTicketCode("123");
		 otTicketInfoTO.setTicket(otTkt);
		otCmdTO.setQueryTicketTO(otQryTktTO);

		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		queryReq.setIncludeElectronicAttributes(true);
		queryReq.setIncludeEntitlementAccount(true);
		queryReq.setIncludeRenewalAttributes(true);
		dtiTxn.getRequest().setCommandBody(queryReq);
		DTIMockUtil.processMockprepareAndExecuteSql();
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
