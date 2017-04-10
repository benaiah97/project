package pvt.disney.dti.gateway.test.util;

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
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO.GenderType;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TktSellerTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.data.common.TicketTO.TktAssignmentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.util.ResourceLoader;

/**
 * The Class CommonTestUtils.
 * 
 * @author rasta006
 */
public class CommonTestUtils {

	/** The resource bundle. */
	@Mocked
	ResourceBundle resourceBundle;

	/** Properties. */
	Properties props = null;
	//public static final String hkdXMLURls="/xml/config/Reservation/Reservation_01_Rsp_WDW.xml";
	public static final String hkdXMLURls="/xml/hkd/Test_01_HKD_01.xml";

	/* for setting up the properties from dtiapp.properties */
	/**
	 * Sets the config property.
	 *
	 * @return the properties
	 */
	public Properties setConfigProperty() {
		props = new Properties();
		props.setProperty("DtiApp.TSMACExclusion", "WDWADMIN");
		props.setProperty("DtiApp.FloodControlExceptionTsLoc",
				"mkl2,VoidStore,97016000002");
		props.setProperty("POS.target", "Test");
		props.setProperty("POS.tktBroker", "DTIDV");
		props.setProperty("ATS.SiteNumber", "120");
		props.setProperty("ATS.MaxEncodeAllCount", "41");
		props.setProperty("iago.endpoint", "http://aa.com");
		props.setProperty("iago.socketTimeout", "40000");
		props.setProperty("iago.connectionTimeout", "8000");
		props.setProperty("iago.socketTimeout.renewal", "100000");
		props.setProperty("ACTIVECONN", "DEFAULTURL");
		props.setProperty("DEFAULTURL", "http://aa.com");
		props.setProperty("CONNECT_TIMEOUT_MILLIS", "5000");
		props.setProperty("READ_TIMEOUT_MILLIS", "35000");
		props.setProperty("READ_TIMEOUT_MILLIS.RENEWAL", "60000");
		return props;
	}

	/* Mocking the ResourceBundle getResourceBundle */
	/**
	 * Sets the mock property.
	 */
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
	 * Common code for Ticket Creation.
	 *
	 * @param type
	 *            the type
	 * @return the ticket list
	 */
	public ArrayList<TicketTO> getTicketList(TicketIdType type) {
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
		default:
			break;
		}

		ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();
		tktList.add(ticket);
		return tktList;
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
	protected DemographicsTO getBillingInfo() {
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
		bill.setDateOfBirth(new GregorianCalendar());
		bill.setGender(GenderType.MALE);
		bill.setOptInSolicit(true);
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
	 * Common Request for DTITransactionTO
	 * 
	 * @param dtiTxn
	 */
	public void createCommonRequest(DTITransactionTO dtiTxn) {
		DTIRequestTO dtiRequestTO = new DTIRequestTO();
		dtiTxn.setRequest(dtiRequestTO);
		PayloadHeaderTO payHeaderTO = new PayloadHeaderTO();
		dtiRequestTO.setPayloadHeader(payHeaderTO);
		CommandHeaderTO cmdHeaderTO = new CommandHeaderTO();
		dtiRequestTO.setCommandHeader(cmdHeaderTO);
		payHeaderTO.setPayloadID("1234567890");
		payHeaderTO.setTarget("TEST");
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
		cmdHeaderTO.setCmdActor("SYS");
		EntityTO entityTO = new EntityTO();
		entityTO.setDefPymtId(3);
		entityTO.setDefPymtData("1");
		entityTO.setEntityId(1);
		
		entityTO.setActive(true);
		dtiTxn.setEntityTO(entityTO);
		entityTO.setEntityId(1);
		entityTO.setDefPymtId(1);
		entityTO.setDefPymtData("1");
		entityTO.setStartValidDate(new Date(System.currentTimeMillis()));
		entityTO.setEndValidDate(new Date(System.currentTimeMillis()));
		dtiTxn.setEntityTO(entityTO);
		cmdHeaderTO.setCmdDevice("PAULH");
		cmdHeaderTO.setCmdOperator("PAULH");
	}

	/**
	 * Common Response for DTITransactionTO
	 * 
	 * @param dtiTxn
	 */
	public void createCommonResponse(DTITransactionTO dtiTxn) {

		DTIResponseTO dtiResponseTO = new DTIResponseTO();
		dtiTxn.setResponse(dtiResponseTO);
		PayloadHeaderTO payHeaderTO = new PayloadHeaderTO();
		dtiResponseTO.setPayloadHeader(payHeaderTO);
		CommandHeaderTO cmdHeaderTO = new CommandHeaderTO();
		dtiResponseTO.setCommandHeader(cmdHeaderTO);
		/*
		 * QueryTicketRequestTO queryTicketRequestTO = new
		 * QueryTicketRequestTO();
		 * dtiRequestTO.setCommandBody(queryTicketRequestTO);
		 */
		payHeaderTO.setPayloadID("1234567890");
		payHeaderTO.setTarget("TEST");

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
		cmdHeaderTO.setCmdActor("SYS");
		EntityTO entityTO = new EntityTO();
		dtiTxn.setEntityTO(entityTO);
		entityTO.setActive(true);
		dtiTxn.setEntityTO(entityTO);
		entityTO.setEntityId(1);
		entityTO.setDefPymtId(1);
		entityTO.setDefPymtData("1");
		entityTO.setStartValidDate(new Date(System.currentTimeMillis()));
		entityTO.setEndValidDate(new Date(System.currentTimeMillis()));
		cmdHeaderTO.setCmdDevice("PAULH");
		cmdHeaderTO.setCmdOperator("PAULH");
	}

	/**
	 * Gets the ticket assingment.
	 *
	 * @param ticket
	 *            the ticket
	 * @return the ticket assingment
	 */
	protected ArrayList<TktAssignmentTO> getTicketAssingment(TicketTO ticket) {
		ArrayList<TktAssignmentTO> ticketAssignmets = new ArrayList<TktAssignmentTO>();
		TktAssignmentTO ticketassignment = ticket.new TktAssignmentTO();
		ticketassignment.setAccountItem(new BigInteger("1"));
		ticketassignment.setProdQty(new BigInteger("1"));
		ticketAssignmets.add(ticketassignment);
		return ticketAssignmets;

	}

	/**
	 * Common request for creation of ticket
	 * 
	 * @return
	 */
	public static OTTicketTO getOTicket() {
		OTTicketTO ticket = new OTTicketTO();
		ticket.setTCOD("12000507111600050");
		ticket.setTDssn((GregorianCalendar) GregorianCalendar.getInstance(),
				"1", "1", "1");

		return ticket;
	}
	
}
