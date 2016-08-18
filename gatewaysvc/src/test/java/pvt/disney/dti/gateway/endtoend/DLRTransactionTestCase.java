package pvt.disney.dti.gateway.endtoend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.dom4j.DocumentHelper;
//import org.dom4j.XPath;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;

import pvt.disney.dti.gateway.client.DLRHTTPClient;
import pvt.disney.dti.gateway.client.ProviderClient;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.rules.dlr.DLRBusinessRules;
import pvt.disney.dti.gateway.service.dtixml.TransmissionRqstXML;

public class DLRTransactionTestCase extends TestCase {

	private static DLRTestUtil dlrTestUtil;
	private static DLRHTTPClient dlrHttpClient;
	//private static DTIService dtiSvc;
	
	private static ArrayList<String> ticketProductList;
	private static ArrayList<String> ticketPriceList;
	private static ArrayList<String> ticketVisualIdList;
	private static final int NUMBER_OF_TICKETS_CREATED = 2;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		//create the DLRTestClient for sending messages
		dlrTestUtil = new DLRTestUtil();
		dlrHttpClient = new DLRHTTPClient();
		
		//we should really read this from a config file and not hardcode it
		//that way we can modify without recompiling
		ticketProductList = new ArrayList<String>();
		ticketPriceList = new ArrayList<String>();
		ticketVisualIdList = new ArrayList<String>();
		
		//add 2 ticket products
		ticketProductList.add("20876");
		ticketProductList.add("20876");
		//add the 2 ticket prices
		ticketPriceList.add("179.00");
		ticketPriceList.add("179.00");
		
		dlrCreateTicket();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Helper Method.
	 * Not a unit test, since DTI does not (YET) support create ticket
	 * for DLR. Just a Helper method for setting up tickets for
	 * query and void testing. This could easily be refactored into its 
	 * own stand alone test method if/when DTI supports create for DLR
	 */
	public static void dlrCreateTicket() throws Exception {
		Logger.getAnonymousLogger().info("Creating Tickets for later tests");
		
		//String createTicketsXML = TestUtilities.getXMLfromFile("./resources/xml/CreateTicketRequest-Multiple.xml");
		String reqXml =
				"<Envelope>" +
					"<Header>" +
						"<SourceID>WDPRONADLR</SourceID>" +
						"<MessageID>0</MessageID>" +
						"<MessageType>CreateTickets</MessageType>" +
						"<TimeStamp>" + DLRTestUtil.getDLRTimeStamp() + "</TimeStamp>" +
					"</Header>" +
					"<Body>" +
						"<TicketCreation>" +
							"<CustomerID>300000783</CustomerID>" +
							"<Tickets>" +
								"<Ticket>" +
									"<PLU>20876</PLU>" +
										"<Price>179.00</Price>" +
								"</Ticket>" +
								"<Ticket>" +
									"<PLU>20876</PLU>" +
									"<Price>179.00</Price>" +
								"</Ticket>" +
							"</Tickets>" +
							"<Payments>" +
								"<Payment>" +
									"<PaymentCode>42</PaymentCode>" +
										"<Description>Charge</Description>" +
										"<Endorsement>123456789</Endorsement>" +
										"<Amount>358.00</Amount>" +
								"</Payment>" +
							"</Payments>" +
						"</TicketCreation>" +
					"</Body>" +
				"</Envelope>";

		//org.dom4j.Document reqDoc = DocumentHelper.parseText(reqXml);
		
		//send the request and get the response from egalaxy
//		dlrHttpClient = new DLRHTTPClient();
//		String respXml = dlrHttpClient.sendRequest(reqXml);
//		
//		org.dom4j.Document response = DocumentHelper.parseText(respXml);
//		
//		//log the response
//		Logger.getAnonymousLogger().info("Response from egalaxy: " + response.asXML());
//		
//		//get the visualIds and set them in the list for query and void tests
//		org.dom4j.XPath xpathSelector = DocumentHelper.createXPath("//VisualID");
//	    List<org.dom4j.Element> results = xpathSelector.selectNodes(response);
//	    for ( Iterator<org.dom4j.Element> iter = results.iterator(); iter.hasNext(); ) {
//	      org.dom4j.Element element =  iter.next();
//	      ticketVisualIdList.add(element.getText());	      
//	      Logger.getAnonymousLogger().info("Found VisualId:" + element.getText());
//	    }
//		
//
//		// parse the response and grab the visuals ids
//		// add the visualid(s) to the ticketVisualIdList
//	    Assert.assertEquals("No VisualIds found in response", NUMBER_OF_TICKETS_CREATED, ticketVisualIdList.size());
	}
	
	@Test
	public void testQuery() throws Exception {
		
//		String queryMsg = this.buildDLRQuery();
//
//		dlrHttpClient = new DLRHTTPClient();
//		String resp = dlrHttpClient.sendRequest(queryMsg);
//		Logger.getAnonymousLogger().info("queryResp=" + resp);
	}

	
	//@Test
	public void testVoid() throws Exception {
//		String queryMsg = this.buildDLRQuery();
		String voidReq = this.buildDTIVoid();
		Logger.getAnonymousLogger().info("voidReq="+ voidReq);
//		dlrHttpClient = new DLRHTTPClient();
//		String voidResp = dlrHttpClient.sendRequest(voidReq);
		

		// Apply the business rules
		DTITransactionTO dtiTxn = TransmissionRqstXML.getTO(voidReq, TransactionType.VOIDTICKET, "DTIUK");
	    //  dtiTxn = BusinessRules.applyBusinessRules(dtiTxn);
		String xmlRequest = DLRBusinessRules.changeToDLRProviderFormat(dtiTxn);
		//String voidResp = DTIService.submitRequest(voidReq);
		String voidResp = ProviderClient.sendRequestToProvider(dtiTxn, xmlRequest);
		
		Logger.getAnonymousLogger().info("voidResp=" + voidResp);
		Assert.assertTrue("Response contains Error",voidResp.indexOf("Error") == -1);
	}

	/**
	 * 
	 */ 
//	@Test
//	public void testDLRQueryTicket() {
//		//TO DO
//		//TO DO read in the visual ids from the visualIdList
//		//TO DO use the appropriate classes to send the query, i think we can
//		//do this with DTIService
//		//TO DO validate no error in the response
//		
//		//TO DO other, should we do tests for negative?
//		//TO DO ie invalid id
//		
//		Assert.fail("Not Implemented");
//	}
//	
	/**
	 * 
	 */
//	@Test
//	public void testDLRVoidTicket() {
//		//TO DO
//		//should do a void on:
//		//a freshly created and voidable ticket
//		//a ticket that is already voided
//		//a made up ticket that doesn't exist
//		
//		Assert.fail("Not Implemented");
//	}
	
	/**
	 * 
	 */
//	@Test
//	public void testDlrReservationRequest() {
//		//TO DO
//		//TOOD figure out all the iterations of what should be
//		//tested here, different types of res requests, negative testing
//		//and error responses for invalid stuff
//		Assert.fail("Not Implemented");
//	}
	
	/**
	 * Helper method to get an egalxy Query message
	 * @return
	 */
	public String buildDLRQuery() {
		//TO DO should refactor this to read it from the resource directory
		//an update timestamp to now
		String msg = "<Envelope>" + "<Header>"
		+ "<SourceID>WDPRONADLR</SourceID>"
		+ "<MessageID>290118</MessageID>"
		+ "<TimeStamp>" + DLRTestUtil.getDLRTimeStamp() + "</TimeStamp>"
		+ "<MessageType>QueryTicket</MessageType>" + "</Header>"
		+ "<Body>" + "<QueryTicket>" + "<Query>"
		+ "<VisualID>"+ ticketVisualIdList.get(0)+ "</VisualID>" + "</Query>"
		+ "<DataRequest>" + "<Field>ItemKind</Field>"
		+ "<Field>Returnable</Field>" + "<Field>Status</Field>"
		+ "<Field>DateSold</Field>" + "<Field>TicketDate</Field>"
		+ "<Field>StartDateTime</Field>" + "<Field>DateOpened</Field>"
		+ "<Field>ExpirationDate</Field>"
		+ "<Field>EndDateTime</Field>" + "<Field>ValidUntil</Field>"
		+ "<Field>LockedOut</Field>" + "<Field>VisualID</Field>"
		+ "<Field>Price</Field>" + "<Field>UseCount</Field>"
		+ "<Field>Tax</Field>" + "</DataRequest>" + "</QueryTicket>"
		+ "</Body>" + "</Envelope>";
		
		return msg;
	}
	
	
	/**
	 * Helper method to get an DTI VOID message
	 * @return
	 */
	public String buildDTIVoid() {
		//TO DO should refactor this to read it from the resource directory
		//an update timestamp to now
		String voidReq=
		"<?xml version=\"1.0\"?>" +
		   "<Transmission xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"c:/var/void_ticket_request.xsd\">" +
		      "<Payload>" +
		         "<PayloadHeader>" +
		            "<PayloadID>" + DLRTestUtil.getDtiPayloadNumber() + "</PayloadID>" +
		            "<Target>Test-DLR</Target>" +
		            "<Version>1.0</Version>" +
		            "<Comm>" +
		               "<Protocol>IP</Protocol>" +
		               "<Method>Network</Method>" +
		            "</Comm>" +
		            "<TransmitDate>"+ DLRTestUtil.getDtiDate() + "</TransmitDate>" +
		            "<TransmitTime>"+ DLRTestUtil.getDtiTime() + "</TransmitTime>" +
		            "<TktSeller>" +
		               "<TSMAC>WDPRONADLR</TSMAC>" +
		               "<TSSystem>CC</TSSystem>" +
		               "<TSLocation>351</TSLocation>" +
		               "<TSSecurity>WaltDisney1</TSSecurity>" +
		            "</TktSeller>" +
		            "<CommandCount>1</CommandCount>" +
		         "</PayloadHeader>" +
		         "<Command>" +
		            "<CommandHeader>" +
		               "<CmdItem>1</CmdItem>" +
		               "<CmdTimeout>120</CmdTimeout>" +
		               "<CmdDate>" + DLRTestUtil.getDtiDate() +"</CmdDate>" +
		               "<CmdTime>" + DLRTestUtil.getDtiTime() + "</CmdTime>" +
		               "<CmdInvoice>1234 5678 90AB</CmdInvoice>" +
		               "<CmdDevice>WDPRO</CmdDevice>" +
		               "<CmdOperator>WDPRO</CmdOperator>" +
		       "<CmdActor>MGR</CmdActor>" +
		            "</CommandHeader>" +
		            "<VoidTicketRequest>" +
		               "<Ticket>" +
		                  "<TktItem>1</TktItem>" +
		                  "<TktID>" +
		                        "<External>" + ticketVisualIdList.get(0) +"</External>" +
		                  "</TktID>" +
		               "</Ticket>" +
		            "</VoidTicketRequest>" +
		         "</Command>" +
		      "</Payload>" +
		   "</Transmission>";
	
		return voidReq;
	}

}
