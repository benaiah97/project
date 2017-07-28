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
import pvt.disney.dti.gateway.data.TickerateEntitlementRequestTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO.OTTransactionType;
import pvt.disney.dti.gateway.provider.wdw.data.OTMultiEntitlementAccountTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMediaDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.rules.BusinessRules;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for WDWTickerateEntitlementRules.
 *
 * @author ARORT002
 */
public class WDWTickerateEntitlementRulesTestCase extends CommonTestUtils {

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
	 * Test transform request.
	 */
	@Test
	public void testTransformRequest() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		TickerateEntitlementRequestTO dtiTickerateEntitlement = new TickerateEntitlementRequestTO();
		ArrayList<TicketTO> dtiTicketList = new ArrayList<TicketTO>();
		TicketTO ticketTOBarCode = new TicketTO();
		ticketTOBarCode.setBarCode("123");
		dtiTicketList.add(ticketTOBarCode);

		dtiTickerateEntitlement.setTickets(dtiTicketList);
		dtiTickerateEntitlement.setExistingMediaId("123");

		dtiTickerateEntitlement.setMediaData(getNewMediaDataToList());
		dtiTxn.getRequest().setCommandBody(dtiTickerateEntitlement);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		String xmlString = null;

		try {
			xmlString = WDWTickerateEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		dtiTicketList.clear();
		TicketTO ticketTOmagTrack1 = new TicketTO();
		ticketTOmagTrack1.setMag("123");
		dtiTicketList.add(ticketTOmagTrack1);

		dtiTickerateEntitlement.setTickets(dtiTicketList);
		dtiTickerateEntitlement.setExistingMediaId(null);
		dtiTickerateEntitlement.setAccountId("123");

		dtiTickerateEntitlement.setMediaData(getNewMediaDataToList());
		dtiTxn.getRequest().setCommandBody(dtiTickerateEntitlement);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());

		try {
			xmlString = WDWTickerateEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiTicketList.clear();
		TicketTO ticketTOTknId = new TicketTO();
		ticketTOTknId.setTktNID("123");
		dtiTicketList.add(ticketTOTknId);

		dtiTickerateEntitlement.setTickets(dtiTicketList);
		dtiTickerateEntitlement.setExistingMediaId(null);
		dtiTickerateEntitlement.setAccountId(null);
		TicketTO ticketSearchMode =  new TicketTO();
		ticketSearchMode.setMag("123");
		dtiTickerateEntitlement.setTicket(ticketSearchMode);

		dtiTickerateEntitlement.setMediaData(getNewMediaDataToList());
		dtiTxn.getRequest().setCommandBody(dtiTickerateEntitlement);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());

		try {
			xmlString = WDWTickerateEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}

		dtiTicketList.clear();
		TicketTO ticketTOExt = new TicketTO();
		ticketTOExt.setExternal("123");
		dtiTicketList.add(ticketTOExt);

		dtiTickerateEntitlement.setTickets(dtiTicketList);
		dtiTickerateEntitlement.setExistingMediaId(null);
		dtiTickerateEntitlement.setAccountId(null);
		TicketTO ticketBarCode =  new TicketTO();
		ticketBarCode.setBarCode("123");
		dtiTickerateEntitlement.setTicket(ticketBarCode);

		dtiTickerateEntitlement.setMediaData(getNewMediaDataToList());
		dtiTxn.getRequest().setCommandBody(dtiTickerateEntitlement);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());

		try {
			xmlString = WDWTickerateEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		dtiTicketList.clear();
		TicketTO ticketTODssn = new TicketTO();
		ticketTODssn.setDssn(new GregorianCalendar(), "123", "456", "789");
		dtiTicketList.add(ticketTODssn);

		dtiTickerateEntitlement.setTickets(dtiTicketList);
		dtiTickerateEntitlement.setExistingMediaId(null);
		dtiTickerateEntitlement.setAccountId(null);
		TicketTO ticketTknId =  new TicketTO();
		ticketTknId.setTktNID("123");
		dtiTickerateEntitlement.setTicket(ticketTknId);

		dtiTickerateEntitlement.setMediaData(getNewMediaDataToList());
		dtiTxn.getRequest().setCommandBody(dtiTickerateEntitlement);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());

		try {
			xmlString = WDWTickerateEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		
		dtiTickerateEntitlement.setTickets(dtiTicketList);
		dtiTickerateEntitlement.setExistingMediaId(null);
		dtiTickerateEntitlement.setAccountId(null);
		TicketTO ticketExt =  new TicketTO();
		ticketExt.setExternal("123");
		dtiTickerateEntitlement.setTicket(ticketExt);

		dtiTickerateEntitlement.setMediaData(getNewMediaDataToList());
		dtiTxn.getRequest().setCommandBody(dtiTickerateEntitlement);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());

		try {
			xmlString = WDWTickerateEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
		
		
		TicketTO ticketDssn =  new TicketTO();
		ticketDssn.setDssn(new GregorianCalendar(), "123", "456", "789");
		dtiTickerateEntitlement.setTicket(ticketDssn);

		dtiTickerateEntitlement.setMediaData(getNewMediaDataToList());
		dtiTxn.getRequest().setCommandBody(dtiTickerateEntitlement);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());

		try {
			xmlString = WDWTickerateEntitlementRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		} catch (DTIException dtie) {
			Assert.fail("Unexepected Exception " + dtie.getLogMessage());
		}
	}

	/**
	 * Gets the new media data to list.
	 *
	 * @return the new media data to list
	 */
	private ArrayList<NewMediaDataTO> getNewMediaDataToList() {
		ArrayList<NewMediaDataTO> newMediaDataToList = new ArrayList<NewMediaDataTO>();
		NewMediaDataTO newMediaDataTO = new NewMediaDataTO();
		newMediaDataTO.setMediaId("123");
		newMediaDataTO.setMfrId("456");
		newMediaDataTO.setVisualId("123");
		newMediaDataToList.add(newMediaDataTO);
		return newMediaDataToList;
	}

	/**
	 * Test transform response body.
	 */
	@Test
	public void testTransformResponseBody() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.QUERYTICKET);
		createCommonRequest(dtiTxn);
		OTTransactionType txType = OTTransactionType.QUERYTICKET;
		OTCommandTO otCmdTO = new OTCommandTO(txType);
		OTMultiEntitlementAccountTO otTickerateEntitlementTO = new OTMultiEntitlementAccountTO();
		otTickerateEntitlementTO.setAccountId("123");
		otCmdTO.setMultiEntitlementAccountTO(otTickerateEntitlementTO);

		ArrayList<OTTicketInfoTO> ticketList = new ArrayList<OTTicketInfoTO>();
		OTTicketInfoTO otTicketInfoTO = new OTTicketInfoTO();
		OTTicketTO otTkt = new OTTicketTO();
		otTkt.setTDssn(new GregorianCalendar(), "123", "456", "789");
		otTkt.setTCOD("123");
		otTkt.setBarCode("123");
		otTkt.setBarCode("123");
		otTkt.setMagTrack("123");
		otTkt.setExternalTicketCode("123");
		otTicketInfoTO.setTicket(otTkt);
		ticketList.add(otTicketInfoTO);
		otTickerateEntitlementTO.setTicketInfoList(ticketList);
		
		ArrayList<OTMediaDataTO> otMediaDataTOList = new ArrayList<OTMediaDataTO>();
		OTMediaDataTO otMediaDataTO = new OTMediaDataTO();
		otMediaDataTO.setMediaId("123");
		otMediaDataTO.setMfrId("123");
		otMediaDataTO.setVisualId("123");
		otMediaDataTOList.add(otMediaDataTO);
		otTickerateEntitlementTO.setMediaData(otMediaDataTOList);
		
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		try {
			WDWTickerateEntitlementRules.transformResponseBody(dtiTxn, otCmdTO,
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

		TickerateEntitlementRequestTO reqTO = new TickerateEntitlementRequestTO();
		TicketTO searchTicket  = new TicketTO();
		searchTicket.setTktNID("12345678912345678");
		reqTO.setTicket(searchTicket);
		
		ArrayList<TicketTO> ticketList = new ArrayList<TicketTO>();
		ticketList.add(searchTicket);
		reqTO.setTickets(ticketList);
		dtiTxn.getRequest().setCommandBody(reqTO);
		try {
			WDWTickerateEntitlementRules
					.applyWDWTickerateEntitlementRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.INVALID_TICKET_ID);
			assertEquals("In-bound WDW txn with <> 1 TktId: 0",
					dtie.getLogMessage());
		}

	}

}
