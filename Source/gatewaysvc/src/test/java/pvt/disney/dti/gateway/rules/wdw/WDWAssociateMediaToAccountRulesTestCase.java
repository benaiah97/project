package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.AssociateMediaToAccountRequestTO;
import pvt.disney.dti.gateway.data.AssociateMediaToAccountResponseTO;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO.OTTransactionType;
import pvt.disney.dti.gateway.provider.wdw.data.OTMultiEntitlementAccountTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTMediaDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;

/**
 * Tests the WDW specific business rules.
 * 
 * @author arort002
 * 
 */
public class WDWAssociateMediaToAccountRulesTestCase {

	/** The Constant TPREFNUM. */
	private static final Integer TPREFNUM = new Integer(123456);

	/** The Constant PAYLOADID. */
	private static final String PAYLOADID = "1234567890";

	/** The Constant TARGET. */
	private static final String TARGET = "TARGET";

	/** The Constant VERSION. */
	private static final String VERSION = "VERSION";

	/** The Constant METHOD. */
	private static final String METHOD = "METHOD";

	/** The Constant PROTOCOL. */
	private static final String PROTOCOL = "PROTOCOL";

	/** The Constant BROKER. */
	private static final String BROKER = "BROKER";

	/**
	 * Sets the up before class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Tear down after class.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test transform request.
	 */
	@Test
	public void testTransformRequest() {
		DTITransactionTO dtiTxn = getDTITransactionTO();
		DTIRequestTO dtiRqst = getDtiRequestTO();

		AssociateMediaToAccountRequestTO dtiAssociateMediaExistingMediaId = getAssociateMediaToAccountRequestTO();
		dtiAssociateMediaExistingMediaId.setExistingMediaId("123");
		dtiRqst.setCommandBody(dtiAssociateMediaExistingMediaId);
		dtiTxn.setRequest(dtiRqst);

		try {
			String actualXml = WDWAssociateMediaToAccountRules
					.transformRequest(dtiTxn);
			Assert.assertNotNull(actualXml);
		} catch (DTIException e) {
			Assert.fail("Exception in xml conversion");
		}

		AssociateMediaToAccountRequestTO dtiAssociateMediaAccountId = getAssociateMediaToAccountRequestTO();
		dtiAssociateMediaAccountId.setAccountId("456");
		dtiRqst.setCommandBody(dtiAssociateMediaAccountId);
		dtiTxn.setRequest(dtiRqst);

		try {
			String actualXml = WDWAssociateMediaToAccountRules
					.transformRequest(dtiTxn);
			Assert.assertNotNull(actualXml);
		} catch (DTIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AssociateMediaToAccountRequestTO dtiAssociateMediaMag = getAssociateMediaToAccountRequestTO();

		TicketTO ticketSearchModeMag = new TicketTO();
		ticketSearchModeMag.setMag("123");
		dtiAssociateMediaMag.setTicket(ticketSearchModeMag);
		dtiRqst.setCommandBody(dtiAssociateMediaMag);
		dtiTxn.setRequest(dtiRqst);

		try {
			String actualXml = WDWAssociateMediaToAccountRules
					.transformRequest(dtiTxn);
			Assert.assertNotNull(actualXml);
		} catch (DTIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AssociateMediaToAccountRequestTO dtiAssociateMediaBarCode = getAssociateMediaToAccountRequestTO();
		TicketTO ticketSearchModeBarCode = new TicketTO();
		ticketSearchModeBarCode.setBarCode("456");
		dtiAssociateMediaBarCode.setTicket(ticketSearchModeBarCode);
		dtiRqst.setCommandBody(dtiAssociateMediaBarCode);
		dtiTxn.setRequest(dtiRqst);

		try {
			String actualXml = WDWAssociateMediaToAccountRules
					.transformRequest(dtiTxn);
			Assert.assertNotNull(actualXml);
		} catch (DTIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AssociateMediaToAccountRequestTO dtiAssociateMediaTktNID = getAssociateMediaToAccountRequestTO();
		TicketTO ticketSearchModeTktNID = new TicketTO();
		ticketSearchModeTktNID.setTktNID("123");
		dtiAssociateMediaTktNID.setTicket(ticketSearchModeTktNID);
		dtiRqst.setCommandBody(dtiAssociateMediaTktNID);
		dtiTxn.setRequest(dtiRqst);

		try {
			String actualXml = WDWAssociateMediaToAccountRules
					.transformRequest(dtiTxn);
			Assert.assertNotNull(actualXml);
		} catch (DTIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AssociateMediaToAccountRequestTO dtiAssociateMediaExternal = getAssociateMediaToAccountRequestTO();
		TicketTO ticketSearchModeExternal = new TicketTO();
		ticketSearchModeExternal.setExternal("123");
		dtiAssociateMediaExternal.setTicket(ticketSearchModeExternal);
		dtiRqst.setCommandBody(dtiAssociateMediaExternal);
		dtiTxn.setRequest(dtiRqst);

		try {
			String actualXml = WDWAssociateMediaToAccountRules
					.transformRequest(dtiTxn);
			Assert.assertNotNull(actualXml);
		} catch (DTIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AssociateMediaToAccountRequestTO dtiAssociateMediaDssnSite = getAssociateMediaToAccountRequestTO();
		TicketTO ticketSearchModeDssnSite = new TicketTO();
		ticketSearchModeDssnSite.setDssn(new GregorianCalendar(), "123", "456",
				"789");
		dtiAssociateMediaDssnSite.setTicket(ticketSearchModeDssnSite);
		dtiRqst.setCommandBody(dtiAssociateMediaDssnSite);
		dtiTxn.setRequest(dtiRqst);

		try {
			String actualXml = WDWAssociateMediaToAccountRules
					.transformRequest(dtiTxn);
			Assert.assertNotNull(actualXml);
		} catch (DTIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test transform response body.
	 */
	@Test
	public void testTransformResponseBody() {
		OTCommandTO otCmdTO = new OTCommandTO(OTTransactionType.QUERYTICKET);
		AssociateMediaToAccountResponseTO dtiAssociateMediaResp = new AssociateMediaToAccountResponseTO();
		OTMultiEntitlementAccountTO otAssociateMediaToAccountTO = new OTMultiEntitlementAccountTO();
		otAssociateMediaToAccountTO.setAccountId("123");

		ArrayList<OTMediaDataTO> otMediaDataList = new ArrayList<OTMediaDataTO>();
		OTMediaDataTO otMediaDataTO = new OTMediaDataTO();
		otMediaDataTO.setMediaId("123");
		otMediaDataTO.setMfrId("456");
		otMediaDataTO.setVisualId("789");

		ArrayList<OTTicketInfoTO> otTktInfoList = getOtTktInfoList();
		otAssociateMediaToAccountTO.setTicketInfoList(otTktInfoList);
		otMediaDataList.add(otMediaDataTO);
		otAssociateMediaToAccountTO.setMediaData(otMediaDataList);

		otCmdTO.setMultiEntitlementAccountTO(otAssociateMediaToAccountTO);
		DTIResponseTO dtiRespTO = new DTIResponseTO();
		dtiRespTO.setCommandBody(dtiAssociateMediaResp);
		try {
			WDWAssociateMediaToAccountRules.transformResponseBody(
					getDTITransactionTO(), otCmdTO, dtiRespTO);
		} catch (DTIException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the ot tkt info list.
	 *
	 * @return the ot tkt info list
	 */
	private ArrayList<OTTicketInfoTO> getOtTktInfoList() {
		ArrayList<OTTicketInfoTO> otTktInfoList = new ArrayList<OTTicketInfoTO>();

		OTTicketInfoTO otTicketInfoTOBarCode = new OTTicketInfoTO();
		OTTicketTO otTktBarCode = new OTTicketTO();
		otTktBarCode.setBarCode(OTTicketTO.TicketIDType.BARCODE.toString());
		otTicketInfoTOBarCode.setTicket(otTktBarCode);
		otTktInfoList.add(otTicketInfoTOBarCode);

		OTTicketInfoTO otTicketInfoTOExtTicketCode = new OTTicketInfoTO();
		OTTicketTO otTktExtTicketCode = new OTTicketTO();
		otTktExtTicketCode
				.setExternalTicketCode(OTTicketTO.TicketIDType.EXTERNALTICKETCODE
						.toString());
		otTicketInfoTOExtTicketCode.setTicket(otTktExtTicketCode);
		otTktInfoList.add(otTicketInfoTOExtTicketCode);

		OTTicketInfoTO otTicketInfoTOMagTrack = new OTTicketInfoTO();
		OTTicketTO otTktMagTrack = new OTTicketTO();
		otTktMagTrack.setMagTrack(OTTicketTO.TicketIDType.MAGCODE.toString());
		otTicketInfoTOMagTrack.setTicket(otTktMagTrack);
		otTktInfoList.add(otTicketInfoTOMagTrack);

		OTTicketInfoTO otTicketInfoTOTcod = new OTTicketInfoTO();
		OTTicketTO otTktTcod = new OTTicketTO();
		otTktTcod.setTCOD(OTTicketTO.TicketIDType.TCOD.toString());
		otTicketInfoTOTcod.setTicket(otTktTcod);
		otTktInfoList.add(otTicketInfoTOTcod);

		OTTicketInfoTO otTicketInfoTOTdssn = new OTTicketInfoTO();
		OTTicketTO otTktTdssn = new OTTicketTO();
		otTktTdssn.setTDssn(new GregorianCalendar(), "123", "456", "789");
		otTicketInfoTOTdssn.setTicket(otTktTdssn);
		otTktInfoList.add(otTicketInfoTOTdssn);

		return otTktInfoList;
	}

	/**
	 * Test apply wdw associate media to account rules.
	 */
	@Test
	public void testApplyWDWAssociateMediaToAccountRules() {
		DTITransactionTO dtiTxn = getDTITransactionTO();
		DTIRequestTO dtiRqst = getDtiRequestTO();

		AssociateMediaToAccountRequestTO dtiAssociateMediaBarCode = getAssociateMediaToAccountRequestTO();
		TicketTO ticket = new TicketTO();
		
		ticket.setTktItem(new BigInteger("1"));
		ticket.setExternal("1");
		ticket.setProdCode("1");
		ticket.setProdPrice(new BigDecimal("1"));
		
		
		dtiAssociateMediaBarCode.setTicket(ticket);
		dtiRqst.setCommandBody(dtiAssociateMediaBarCode);
		dtiTxn.setRequest(dtiRqst);
		
		
		try {
			WDWAssociateMediaToAccountRules
					.applyWDWAssociateMediaToAccountRules(dtiTxn);
		} catch (DTIException dtie) {
			assertEquals(dtie.getDtiErrorCode(), DTIErrorCode.INVALID_TICKET_ID);
			assertEquals(
					"In-bound WDW txn with <> 1 TktId: 0",
					dtie.getLogMessage());
		}
		
		
	}

	/**
	 * Gets the DTI transaction to.
	 *
	 * @return the DTI transaction to
	 */
	private DTITransactionTO getDTITransactionTO() {
		DTITransactionTO dtiTxn = new DTITransactionTO(
				DTITransactionTO.TransactionType.QUERYTICKET);

		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = getAttributeTOMap();
		dtiTxn.setAttributeTOMap(aMap);
		dtiTxn.setTktBroker(BROKER);
		dtiTxn.setTpRefNum(TPREFNUM);
		return dtiTxn;
	}

	/**
	 * Gets the associate media to account request to.
	 *
	 * @return the associate media to account request to
	 */
	private AssociateMediaToAccountRequestTO getAssociateMediaToAccountRequestTO() {
		ArrayList<NewMediaDataTO> mediaDataList = getMediaDataList();
		AssociateMediaToAccountRequestTO dtiAssociateMediaExistingMediaId = new AssociateMediaToAccountRequestTO();
		dtiAssociateMediaExistingMediaId.setMediaData(mediaDataList);
		return dtiAssociateMediaExistingMediaId;
	}

	/**
	 * Gets the media data list.
	 *
	 * @return the media data list
	 */
	private ArrayList<NewMediaDataTO> getMediaDataList() {
		ArrayList<NewMediaDataTO> mediaDataList = new ArrayList<NewMediaDataTO>();
		NewMediaDataTO newMediaDataTO = new NewMediaDataTO();
		newMediaDataTO.setMediaId("123");
		newMediaDataTO.setMfrId("456");
		newMediaDataTO.setVisualId("789");
		mediaDataList.add(newMediaDataTO);
		return mediaDataList;
	}

	/**
	 * Gets the attribute to map.
	 *
	 * @return the attribute to map
	 */
	private HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> getAttributeTOMap() {
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> aMap = new HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>();
		AttributeTO opArea = new AttributeTO();
		opArea.setAttrValue("1");
		AttributeTO user = new AttributeTO();
		user.setAttrValue("2");
		AttributeTO pass = new AttributeTO();
		pass.setAttrValue("3");
		AttributeTO siteNumber = new AttributeTO();
		siteNumber.setAttrValue("4");
		aMap.put(AttributeTO.CmdAttrCodeType.OP_AREA, opArea);
		aMap.put(AttributeTO.CmdAttrCodeType.USER, user);
		aMap.put(AttributeTO.CmdAttrCodeType.PASS, pass);
		aMap.put(AttributeTO.CmdAttrCodeType.SITE_NUMBER, siteNumber);
		return aMap;
	}

	/**
	 * Gets the dti request to.
	 *
	 * @return the dti request to
	 */
	private DTIRequestTO getDtiRequestTO() {
		DTIRequestTO dtiRqst = new DTIRequestTO();
		PayloadHeaderTO rqstHeader = new PayloadHeaderTO();
		rqstHeader.setPayloadID(PAYLOADID);
		rqstHeader.setTarget(TARGET);
		rqstHeader.setVersion(VERSION);
		rqstHeader.setCommMethod(METHOD);
		rqstHeader.setCommProtocol(PROTOCOL);
		TktSellerTO tktSellerTO = new TktSellerTO();
		tktSellerTO.setTsMac("TDSNA");
		rqstHeader.setTktSeller(tktSellerTO);
		dtiRqst.setPayloadHeader(rqstHeader);
		return dtiRqst;
	}
}
