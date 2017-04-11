package pvt.disney.dti.gateway.rules;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.ResourceBundle;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.RenewEntitlementRequestTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.EnvironmentType;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * @author rasta006 Common Class for keeping the common method
 */
public class CommonBusinessTest extends CommonTestUtils {
	@Mocked
	ResourceBundle resourceBundle;
	/** Properties */
	Properties props = null;
	/** Constant value for Test WDW */
	protected final static String TESTWDW = "TEST-WDW";
	/** Environment variable for */
	protected static String TESTENV = null;
	/** PRODUCTION WDW */
	protected final static String PRODWDW = "PROD-WDW";
	/** TEST TARGET */
	protected final static String TEST_TARGET = "TEST";
	/** PRODUCTION TARGET */
	protected final static String PROD_TARGET = "PROD";
	/** HKD TARGET */
	protected final static String PROD_HKD_TARGET = "PROD-HKD";
	/** The TPI Code for NEX. */
	protected static final String TPI_CODE_WDW = "NEX01";
	/** The TPI Code for DLR. */
	protected static final String TPI_CODE_DLR = "DLR01";
	/** The TPI Code for HKD. */
	protected static final String TPI_CODE_HKD = "HKD01";
	/** The CREATETAG. */
	protected final static String CREATETAG = "Create";

	/**
	 * Common method for populating the medial List
	 * 
	 * @return
	 */
	protected ArrayList<NewMediaDataTO> getMediaDataList() {
		ArrayList<NewMediaDataTO> aMediaList = new ArrayList<NewMediaDataTO>();
		NewMediaDataTO amedia = new NewMediaDataTO();
		amedia.setMediaId("MDIA20160719000");
		amedia.setMfrId("MUID201607190003");
		amedia.setVisualId("VSUL201607190003");
		aMediaList.add(amedia);
		return aMediaList;
	}

	/**
	 * Common method for creation of common request , (without any transaction)
	 * 
	 * @param dtiTxn
	 * @param active
	 * @param actor
	 * @param target
	 * @param ticketType
	 * @param inactiveDate
	 * 
	 */
	protected void createCommonRequest(DTITransactionTO dtiTxn, boolean active,
			boolean actor, String target, String ticketType,
			boolean inactiveDate) {
		DTIRequestTO dtiRequestTO = new DTIRequestTO();
		dtiTxn.setRequest(dtiRequestTO);
		PayloadHeaderTO payHeaderTO = new PayloadHeaderTO();
		dtiRequestTO.setPayloadHeader(payHeaderTO);
		CommandHeaderTO cmdHeaderTO = new CommandHeaderTO();
		dtiRequestTO.setCommandHeader(cmdHeaderTO);
		QueryTicketRequestTO queryTicketRequestTO = new QueryTicketRequestTO();
		dtiRequestTO.setCommandBody(queryTicketRequestTO);
		payHeaderTO.setPayloadID("1234567890");
		switch (target) {
		case "TEST":
			payHeaderTO.setTarget("TEST");
			break;
		case "PROD":
			payHeaderTO.setTarget("PROD");
			break;
		case "PROD-HKD":
			payHeaderTO.setTarget("PROD-HKD");
			break;
		}
		payHeaderTO.setVersion("1.0");
		payHeaderTO.setCommProtocol("IP");
		payHeaderTO.setCommMethod("Network");
		payHeaderTO.setTransmitDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		TktSellerTO tktSellerTo = new TktSellerTO();
		tktSellerTo.setTsMac("DA6626");
		tktSellerTo.setTsLocation("001");
		tktSellerTo.setTsSystem("CC");
		tktSellerTo.setTsSecurity("WaltDisney1");
		payHeaderTO.setTktSeller(tktSellerTo);
		payHeaderTO.setCommandCount(new BigInteger("1"));
		payHeaderTO.setTransmitDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		cmdHeaderTO.setCmdItem(new BigInteger("1"));
		cmdHeaderTO.setCmdTimeout(new BigInteger("45"));
		cmdHeaderTO.setCmdDateTime((GregorianCalendar) GregorianCalendar
				.getInstance());
		cmdHeaderTO.setCmdInvoice("2018895220000011272.");
		if (actor) {
			cmdHeaderTO.setCmdActor("SYS");
		} else {
			cmdHeaderTO.setCmdActor(null);
		}
		EntityTO entityTO = new EntityTO();
		dtiTxn.setEntityTO(entityTO);
		if (active) {
			entityTO.setActive(true);
		} else {
			entityTO.setActive(false);
		}
		dtiTxn.setEntityTO(entityTO);
		entityTO.setEntityId(1);
		entityTO.setDefPymtId(1);
		entityTO.setDefPymtData("1");
		if (inactiveDate) {
			entityTO.setStartValidDate(new Date(System.currentTimeMillis()));
			entityTO.setEndValidDate(new Date(System.currentTimeMillis()));
		} else {
			entityTO.setStartValidDate(null);
			entityTO.setEndValidDate(null);
		}
		cmdHeaderTO.setCmdDevice("PAULH");
		cmdHeaderTO.setCmdOperator("PAULH");
	}

	/**
	 * For creating QueryTicketRequestTO;
	 * 
	 * @param dtiTxn
	 * @param active
	 * @param actor
	 * @param target
	 * @param ticketType
	 * 
	 *            QueryTicketRequestTO object
	 */
	protected void createQueryTicketRequest(DTITransactionTO dtiTxn,
			String ticketType) {

		QueryTicketRequestTO queryReq = new QueryTicketRequestTO();
		ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();
		if (ticketType.compareTo("NEX01") == 0) {
			tktList = getTicketList(TicketIdType.TKTNID_ID);
		} else {
			tktList = getTicketList(TicketIdType.EXTERNAL_ID);
		}
		queryReq.setTktList(tktList);
		dtiTxn.getRequest().setCommandBody(queryReq);
	}

	/**
	 * For reservation request
	 * 
	 * @param resReqTO
	 */
	protected void getReservationRequest(ReservationRequestTO resReqTO) {
		resReqTO.setRequestType(CREATETAG);
		resReqTO.setTktList(getTicketList(TicketIdType.MAG_ID));
		resReqTO.setEligibilityGroup("1");
		resReqTO.setEligibilityMember("1");
		ClientDataTO clientData = new ClientDataTO();
		getClientData(clientData);
		resReqTO.setClientData(clientData);
		ReservationTO reserv = new ReservationTO();
		reserv.setResPickupArea("ABC");
		reserv.setResSalesType("123");
		resReqTO.setReservation(reserv);

	}
	/**
	 * RenewEntitlementRequestTO request
	 * 
	 * @param renewEntReqTO
	 */
	protected void getRenewEntitlementRequestTO(
			RenewEntitlementRequestTO renewEntReqTO) {
		renewEntReqTO.setTktList(getTicketList(TicketIdType.EXTERNAL_ID));
		renewEntReqTO.setEligibilityGroup("1");
		renewEntReqTO.setEligibilityMember("1");
		ClientDataTO clientData = new ClientDataTO();
		getClientData(clientData);
		renewEntReqTO.setClientData(clientData);
		ReservationTO reserv = new ReservationTO();
		reserv.setResPickupArea("ABC");
		reserv.setResSalesType("123");
		reserv.setResCode("ABCD");
		renewEntReqTO.setReservation(reserv);
	}
	/**
	 * For providing a common place for Mock Util methods
	 */
	protected void mockUtilMethods() {
		DTIMockUtil.mockGetOrderProduct();
		DTIMockUtil.mockGetEntityProducts();
		DTIMockUtil.mockGetEntityProductGroups();
		DTIMockUtil.mockGetOrderEligibility();
		DTIMockUtil.mockGetProductTicketTypes();
		DTIMockUtil.mockGetTPCommandLookup();
		DTIMockUtil.mockGetWordCollection();
		DTIMockUtil.processMockInsert();
		DTIMockUtil.mockGetGWTPCommandLookup();
		DTIMockUtil.mockGetOrderProductWithParam();
		DTIMockUtil.mockGetEntityProductsWithParam();
		DTIMockUtil.mockGetEntityProductGroupsthreeParam();
	}
	/**
	 * For setting up the environment, TEST / PROD , to achieve the value of
	 * tpiCode
	 * 
	 * @param dtiTxn
	 * @param env
	 * @return
	 */
	public DTITransactionTO mockValidateProviderTarget(
			DTITransactionTO dtiTxn, String env) {
		TESTENV = env;
		new MockUp<ContentRules>() {
			@Mock
			protected DTITransactionTO validateProviderTarget(
					DTITransactionTO dtiTxn) {
				if (TESTENV.compareTo("TEST") == 0) {
					dtiTxn.setProvider(ProviderType.WDWNEXUS);
					dtiTxn.setEnvironment(EnvironmentType.TEST);
					dtiTxn.getRequest().getPayloadHeader().setTarget(TESTWDW);
				} else if (TESTENV.compareTo("PROD") == 0) {
					dtiTxn.setProvider(ProviderType.DLRGATEWAY);
					dtiTxn.setEnvironment(EnvironmentType.PRODUCTION);
					dtiTxn.getRequest().getPayloadHeader().setTarget(PRODWDW);
				} else {
					dtiTxn.setProvider(ProviderType.HKDNEXUS);
					dtiTxn.setEnvironment(EnvironmentType.PRODUCTION);
					dtiTxn.getRequest().getPayloadHeader().setTarget(PRODWDW);
				}
				return dtiTxn;
			}
		};
		return dtiTxn;
	}


}
