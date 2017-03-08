package pvt.disney.dti.gateway.rules;

import java.math.BigDecimal;
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
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.NewMediaDataTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.ReservationTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.util.ResourceLoader;

/**
 * @author rasta006
 *  Common Class for keeping the common method
 */
public class CommonBusinessTest {
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

	/* for setting up the properties from dtiapp.properties */
	public Properties setConfigProperty() {
		props = new Properties();
		props.setProperty("DtiApp.TSMACExclusion", "WDWADMIN");
		props.setProperty("DtiApp.FloodControlExceptionTsLoc",
				"mkl2,VoidStore,97016000002");
		props.setProperty("POS.target", "Test");
		props.setProperty("POS.tktBroker", "DTIDV");
		props.setProperty("ATS.SiteNumber", "120");
		props.setProperty("ATS.MaxEncodeAllCount", "41");
		return props;
	}

	/* Mocking the ResourceBundle getResourceBundle */
	public void setMockProperty() {
		new MockUp<ResourceLoader>() {
			@Mock
			public ResourceBundle getResourceBundle(String props) {
				return resourceBundle;
			}
		};
		/* Mocking the ResourceBundle convertResourceBundleToProperties */
		new MockUp<ResourceLoader>() {
			@Mock
			public Properties convertResourceBundleToProperties(
					ResourceBundle Key) {
				/* Returning the values of property from property */
				return setConfigProperty();
			}
		};
	}

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
	 * Common code for Ticket Creation
	 * 
	 * @return
	 */
	protected ArrayList<TicketTO> getTicketList(TicketIdType type) {
		TicketTO ticket = new TicketTO();
		switch (type) {
		case MAG_ID:
			ticket.setTktItem(new BigInteger("1"));
			ticket.setProdQty(new BigInteger("1"));
			// ticket.set
			ticket.setMag("AFTWU6SRSBUVRZSSUURYHGRS65RSRRRRRRRWFFWU6SRSBUVRZSSUURYHGRS65RSRRRRRRRW12");
			ticket.setProdCode("1");
			ticket.setProdPrice(new BigDecimal("1"));
			ticket.setFromPrice(new BigDecimal("1"));
			ticket.setFromProdCode("1");
			ticket.setUpgrdPrice(new BigDecimal("0"));
			break;
		case TKTNID_ID:
			ticket.setTktItem(new BigInteger("1"));
			ticket.setTktNID("12000507111600050");
			break;
		case EXTERNAL_ID:
			ticket.setTktItem(new BigInteger("1"));
			ticket.setExternal("1");
			ticket.setProdCode("1");
			ticket.setProdPrice(new BigDecimal("1"));
			break;
		}

		ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();
		tktList.add(ticket);
		return tktList;
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
	 * Shipping Information
	 * 
	 * @return
	 */
	private DemographicsTO getShippingInfo() {
		DemographicsTO ship = new DemographicsTO();
		ship.setAddr1("1");
		ship.setAddr2("2");
		return ship;
	}

	/**
	 * Billing Information
	 * 
	 * @return
	 */
	private DemographicsTO getBillingInfo() {
		DemographicsTO bill = new DemographicsTO();
		bill.setAddr1("1");
		bill.setAddr2("2");
		bill.setFirstName("FIRST");
		bill.setLastName("LAST");
		bill.setCity("TEST");
		bill.setZip("560067");
		bill.setCountry("US");
		bill.setTelephone("9876543210");
		bill.setEmail("test@test.com");
		return bill;
	}

	/**
	 * populating the clientData
	 * 
	 * @param clientData
	 */
	protected void getClientData(ClientDataTO clientData) {
		clientData.setClientType("Private");
		clientData.setClientCategory("WW");
		clientData.setDemoLanguage("en");
		clientData.setBillingInfo(getBillingInfo());
		clientData.setShippingInfo(getShippingInfo());

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

}
