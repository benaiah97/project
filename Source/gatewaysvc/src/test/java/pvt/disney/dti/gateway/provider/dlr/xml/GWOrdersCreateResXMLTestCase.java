package pvt.disney.dti.gateway.provider.dlr.xml;

import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWEnvelopeTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderContactTO;
//import pvt.disney.dti.gateway.provider.dlr.data.GWOrderLinePaymentTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderLineTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWOrdersRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.UtilityXML;

public class GWOrdersCreateResXMLTestCase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//DLRTestUtil2.init("TestOrders.properties");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test get xml.
	 */
	@Test
	public final void testGetXML() {
	    // ***** Read the test file. *****
	    String baselineXML = null;
	    String fileName = new String("./src/test/resources/xml/dti/DLR-ReservationRequest.xml");

		try {
		      baselineXML = DLRTestUtil.getXMLfromFile(fileName);
		    } catch (Exception e) {
		      fail("Unable to read test file: " + fileName);
		    }

		    GWEnvelopeTO dtiTxn = createOrdersReqTO();
		    String newXML = null;

		    try {
		      newXML = GWEnvelopeXML.getXML(dtiTxn);
		    } catch (DTIException dtie) {
		      fail("DTIException creating the gateway request XML: " + dtie.toString());
		    }

		    // Validate the XML Headers (Payload & Command)
		    try {
		      DLRTestUtil.validateXMLHeaders(baselineXML, newXML);
		    } catch (Exception e) {
		      fail("Exception validating gateway request XML headers: " + e.toString());
		    }

		    // Validate Order Response
		    String orderRespBaseline = DLRTestUtil.findTag(baselineXML, "Body");
		    String orderRespNew = DLRTestUtil.findTag(newXML, "Body");

		    Logger.getAnonymousLogger().info("FINALLY: orderRespNew:"+orderRespNew);
//		    try {
//		      DLRTestUtil.compareXML(orderRespBaseline, orderRespNew, "Orders");
//		    } catch (Exception e) {
//		      fail("Exception validating Orders section: " + e.toString());
//		    }
	}

	/**
	 * Test get to.
	 */
	@Test
	public final void testGetTO() {

		// ***** Read the test file. *****
		String inputXML = null;
		//fail("Not Implemented" + inputXML);
	}

	/**
	 * Test get to error.
	 */
	@Test
	public final void testGetTOErrorDuplicateOrder() {

		// ***** Read the test file. *****
		String inputXML = null;
		//fail("Not Implemented");

		String fileName = new String(
				"./src/test/resources/xml/egalaxy/OrdersResponse-Error107-AlreadyExists.xml");

		try {
			inputXML = DLRTestUtil.getXMLfromFile(fileName);
		} catch (Exception e) {
			fail("Unable to read test file: " + fileName);
		}

		GWEnvelopeTO parsedEnvTO = null;

		try {
			parsedEnvTO = GWEnvelopeXML.getTO(inputXML);
		} catch (DTIException dtie) {
			fail("DTIException parsing the gateway response XML: "
					+ dtie.toString());
		}

		try {
			validateOrdersRespTOError(parsedEnvTO,
					DLRTestUtil2.ORDERS_ALREADY_EXISTS_CODE);
		} catch (Exception e) {
			fail("Exception parsing the gateway response XML: " + e.toString());
		}
	}

	/**
	 * Creates the orders req to.
	 * 
	 * @return the gW envelope to
	 */
	 static GWEnvelopeTO createOrdersReqTO() {

	    GWEnvelopeTO gwEnvTO = new GWEnvelopeTO(GWEnvelopeTO.GWTransactionType.ORDERS);

	    // Header
	    GWHeaderTO gwHdrTO = new GWHeaderTO();
	    //gwHdrTO.setSourceID(DLRTestUtil2.SOURCEID);
	    gwHdrTO.setSourceID("WDPRONADLR");
	    
	    //gwHdrTO.setMessageID(DLRTestUtil2.MESSAGEID);
	    gwHdrTO.setMessageID(new BigInteger("13761182"));
	    
	    GregorianCalendar gCal =  new GregorianCalendar();
	    gwHdrTO.setTimeStamp(gCal);
	    
	    gwHdrTO.setMessageType(DLRTestUtil2.MESSAGETYPE_ORDERS);
	    gwEnvTO.setHeaderTO(gwHdrTO);

	    // Body
	    GWBodyTO gwBodyTO = new GWBodyTO();

	    // GWOrdersRqstTO
	    GWOrdersRqstTO gwOrdersReqTO = new GWOrdersRqstTO();
	    ArrayList<GWOrderTO> orderList = new ArrayList<GWOrderTO>();
	    
	    //populate orders based on stuff in test properties file
	    // iterate through invididual Order TOs
	    GWOrderTO orderTo = getBaseOrderInfo(gCal);
	   
	    // orderTo.setOrderTotal i think we will calculate this later
	    
	    //create and populate order contact Lists
	    ArrayList<GWOrderContactTO> orderContactList = new ArrayList<GWOrderContactTO>(); 	    
	    //add order contact   
	    orderContactList.add(getContactInfo());
	    // add shipto contact info   
	    orderContactList.add(getShipToInfo());
	    
	    // set shipping on Order
	    orderTo.setShipDeliveryMethod(DLRTestUtil2.DELIVERY_METHOD);
	    orderTo.setShipDeliveryDetails(DLRTestUtil2.DELIVERY_DETAILS);
	    
	    // set group visit info
	    orderTo.setGroupVisitDate(DLRTestUtil2.GROUPVISITDATE);
	    
	    // iterate through order line items
	    ArrayList<GWOrderLineTO> orderLineList = new ArrayList<GWOrderLineTO>();
////	    orderLineList.add(getOrderLine("1"));
////	    orderLineList.add(getOrderLine("2"));
////	    orderLineList.add(getOrderLine("3"));
////	    
////	    orderLineList.add(getPaymentOrderLine("1"));
////	    
////	    //set the order line list on the orders to
////	    orderTo.setOrderLineList(orderLineList);
//	    
	    
	    //set the order item list on the orders to
	   gwOrdersReqTO.addOrder(orderTo);
	    Logger.getAnonymousLogger().info("gwOrdersReqTO.orderList.size=" +gwOrdersReqTO.getOrderList().size());
	    // set the rqst TO on the Body to
	    gwBodyTO.setOrdersRqstTO(gwOrdersReqTO);
	    gwEnvTO.setBodyTO(gwBodyTO);

	    return gwEnvTO;
		
	}
	
	/**
	   * Validate orders resp to.
	   * 
	   * @param parsedEnvTO the parsed env to
	   * 
	   * @throws Exception the exception
	   */
	   static void validateOrdersRespTO(GWEnvelopeTO parsedEnvTO) throws Exception {
			String inputXML = null;
			fail("Not Implemented:" + inputXML);
			
	  }  
	  
	  /**
	   * Validate orders resp to error.
	   * 
	   * @param parsedEnvTO the parsed env to
	   * 
	   * @throws Exception the exception
	   */
	   static void validateOrderRespTOError(GWEnvelopeTO parsedEnvTO) throws Exception {
		  String inputXML = null;
			fail("Not Implemented:" + inputXML);
		}
	  
	  /**
	   * Validate resp header to.
	   * 
	   * @param gwHdrTO the gw hdr to
	   * 
	   * @throws Exception the exception
	   */
	   static void validateRespHeaderTO(GWHeaderTO gwHdrTO) throws Exception {
		  String inputXML = null;
			fail("Not Implemented:"+inputXML);
	
		}
	  
	  /**
	   * Helper method to get order contact info for test
	 * @return
	 */
	private static GWOrderContactTO getContactInfo() {
		    GWOrderContactTO orderContact = new GWOrderContactTO();

		    orderContact.setFirstName(DLRTestUtil2.CONTACT_FIRSTNAME);
		    orderContact.setLastName(DLRTestUtil2.CONTACT_LASTNAME);
		    orderContact.setStreet1(DLRTestUtil2.CONTACT_STREET1);
		    orderContact.setCity(DLRTestUtil2.CONTACT_CITY);
		    orderContact.setState(DLRTestUtil2.CONTACT_STATE);
		    orderContact.setCountry(DLRTestUtil2.CONTACT_COUNTRY);
		    orderContact.setPhone(DLRTestUtil2.CONTACT_PHONE);
		    orderContact.setEmail(DLRTestUtil2.CONTACT_EMAIL);
		    
		    return orderContact;
	  }
	  
	  /**
	   * Helper method to get shipto info for test
	   * get shipToInfo
	 * @return
	 */
	private static  GWOrderContactTO getShipToInfo() { 
		  GWOrderContactTO shipToContact = new GWOrderContactTO();
	    // populate ship to contact   
	    //SHAN: consider refactoring this into a helper method
	    //cause this is WAY ugly
	    shipToContact.setFirstName(DLRTestUtil2.SHIPTO_FIRSTNAME);
	    shipToContact.setLastName(DLRTestUtil2.SHIPTO_LASTNAME);
	    shipToContact.setStreet1(DLRTestUtil2.SHIPTO_STREET1);
	    shipToContact.setCity(DLRTestUtil2.SHIPTO_CITY);
	    shipToContact.setState(DLRTestUtil2.SHIPTO_STATE);
	    shipToContact.setCountry(DLRTestUtil2.SHIPTO_COUNTRY);
	    shipToContact.setPhone(DLRTestUtil2.SHIPTO_PHONE);
		  
	    return shipToContact;
	  }
	
	/**
	 * Helper method to get the base order to for tesing
	 * more goodies are added to it later
	 * @param gCal
	 * @return
	 */
	private static GWOrderTO getBaseOrderInfo(GregorianCalendar gCal) {
		GWOrderTO orderTo = new GWOrderTO();
	    orderTo.setOrderID(DLRTestUtil2.getDLROrderID());
	    orderTo.setCustomerID(DLRTestUtil2.CUSTOMERID);
	    orderTo.setOrderDate(UtilityXML.getEGalaxyDateFromGCal(gCal));
	    orderTo.setOrderStatus("2"); //per WMB: Constant '2' Open per Gateway Documentation
	    return orderTo;
	}
	
	/**
	 * Helper method to get an orderline item to for the test
	 * @return
	 */
	private static GWOrderLineTO getOrderLine(String number) {
		//ResourceBundle rb = ResourceBundle.getBundle("TestOrders");
		GWOrderLineTO orderLineTo = new GWOrderLineTO();
		//FIX ME I hate harcoding
		//orderLineTo.setDetailType(rb.getString("ORDERLINE " + number + "_DETAILTYPE"));
		orderLineTo.setDetailType("1");
		
		//orderLineTo.setPlu(rb.getString("ORDERLINE " + number + "_PLU"));
		orderLineTo.setPlu("21246PAH");
		
		//orderLineTo.setDescription(rb.getString("ORDERLINE " + number + "_DESCRIPTION"));
		orderLineTo.setDescription("2009 DLR Weddings 2DPH Child PAH");
		
		//orderLineTo.setTaxCode(rb.getString("ORDERLINE " + number + "_TAXCODE"));
		
		//orderLineTo.setQty(rb.getString("ORDERLINE " + number + "_QTY"));
		orderLineTo.setQty("2");
		
		//orderLineTo.setAmount(rb.getString("ORDERLINE " + number + "_AMOUNT"));
		orderLineTo.setAmount("103.00");
		
		orderLineTo.setTotal("206.00");
		
		return orderLineTo;
	}
	
	/**
	 * Helper method to get a payment orderline item to for the test
	 * @return
	 */
	private static GWOrderLineTO getPaymentOrderLine(String number) {
		
		GWOrderLineTO paymentTO = new GWOrderLineTO();
		//GWOrderLinePaymentTO paymentTO = new GWOrderLinePaymentTO();
		ResourceBundle rb = ResourceBundle.getBundle("TestOrders");
		//SET stup on the payment TO
		//paymentTO.setDetailType(rb.getString("PAYMENT_ORDERLINE_DETAILETYPE")); //TO DO is this always the same? where do we get it from
//		paymentTO.setPaymentCode(rb.getString("PAYMENT_ORDERLINE_PAYMENTCODE"));
//		
//		paymentTO.setDescription(rb.getString("PAYMENT_ORDERLINE_DESCRIPTION"));
//		
	
		// dont think i manually do this //<DetailType>2</DetailType>
		paymentTO.setPaymentCode("31"); //<PaymentCode>31</PaymentCode>

		
		//<PaymentDate>2009-07-25 13:39:55.20</PaymentDate>
		paymentTO.setPaymentDate("2009-09-22 22:40:38.00");// SHAN create a helper method to generate this hardcode==lazy bad coder
		// dont think i have to do this either
		
		//<Description>VI</Description>
		paymentTO.setDescription("VI"); //hard coded badness-ick

//		<Endorsement>4444855457062688</Endorsement>
		paymentTO.setEndorsement("4444855457062688"); //SHAN hard-code ick
		
//		<BillingZIP>76590</BillingZIP>	
//		<Amount>686.0</Amount>
//		<Total>686.0</Total>
//		<ExpDate>0512</ExpDate>
//		<CVN>529</CVN>
		paymentTO.setBillingZip("76590"); //SHAN do i enven need to say it? NO HARDCODE
		paymentTO.setAmount("686.00");
		paymentTO.setTotal("686.00");
		paymentTO.setExpDate("0512");
		paymentTO.setCvn("529");
		
		
//		PAYMENT_ORDERLINE_DETAILETYPE=2
//		PAYMENT_ORDERLINE_PAYMENTCODE=31
//		#PAYMENT_ORDERLINE_PAYMENT_DATE probably get this via some helper method 2009-07-25 13:39:55.20
//		PAYMENT_ORDERLINE_DESCRIPTION=VI
//		PAYMENT_ORDERLINE_ENDORSEMENT=4444855457062688
//		PAYMENT_ORDERLINE_BILLINGZIP=
//		#PAYMENT_ORDERLINE_AMOUNT= probably calc this from other order lines
//		#PAYMENT_ORDERLINE_TOTAL = probably calc this etc
//		PAYMENT_ORDERLINE=EXPDATE=0512
//		PAYMENT_ORDERLINE_CVN=529
//		
//		return paymentOrderLineTo;
		return null;
	}

	/**
 	 * Validates a gateway galaxy ticket activation (upgrade alpha) response transfer object error
 	 * 
 	 * @param envTo the env to
 	 * 
 	 * @throws Exception the exception
 	 */
 	private static void validateOrdersRespTOError(GWEnvelopeTO envTo, String orderErrorCode) throws Exception {
 		//TO DO
 		
 	}
}
