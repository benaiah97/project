package pvt.disney.dti.gateway.client;



import static org.junit.Assert.*;
import mockit.Mock;
import mockit.MockUp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

public class ClientUtilityTestCase {
	ClientUtility client=null;
	String response=null;
	String request=null;
	@Before
	public void setUp(){
		client=new ClientUtility();
		
	}
	@Test
	public void testConvertResponse(){
		String xmlresponse="<Header><ReferenceNumber>12345678</ReferenceNumber><RequestNumber>7654321</RequestNumber><SessionId>7777777</SessionId>"
+ "<RequestType>Create</RequestType><RequestSubType>CreateTransaction</RequestSubType><ValueMsg><ValuedQty>10</ValuedQty>"
+ "<NonValuedQty>0</NonValuedQty></ValueMsg>	</Header>	<CreateTransaction><Error><ErrorCode>21</ErrorCode>"
+ "<ErrorShortDescription>OP_FAIL_CC_PAYM_NOT_APP</ErrorShortDescription><ErrorDescription>CREDIT CARD PAYMENT NOT APPROVED</ErrorDescription>"
+ "</Error><TicketInfo><Item>1</Item><ItemStatus>0</ItemStatus><TicketType>25834</TicketType><Price>239.63</Price><Tax>14.63</Tax>"
+ "<Ticket><TDSSN><Site>WDW</Site><Station>CAS999</Station><Date>09-06-15</Date><TicketId>303</TicketId></TDSSN><TCOD>99999999999999999</TCOD>"
+ "<BarCode>77777777777777777777</BarCode><MagCode> FFFFFFFFFFFFFFFFFFFFFFFF </MagCode><ExternalTicketCode>ABCDEFGHIJKLMNOPQRSTUVWXYZ</ExternalTicketCode>"
+ "</Ticket><Validity><StartDate>09-06-15</StartDate><EndDate>30-12-31</EndDate></Validity><TicketFlag><TicketFlagNumber>0</TicketFlagNumber>"
+ "</TicketFlag><TicketAttribute>0</TicketAttribute><TicketNote>I AM A TICKET NOTE</TicketNote><BiometricLevel>0</BiometricLevel></TicketInfo>"
+ "<TicketInfo><Item>2</Item><ItemStatus>0</ItemStatus><TicketType>25834</TicketType><Price>239.63</Price><Tax>14.63</Tax>"
+ "<Ticket><TDSSN><Site>WDW</Site><Station>CAS999</Station><Date>09-06-15</Date><TicketId>303</TicketId></TDSSN><TCOD>99999999999999999</TCOD>"
+ "<BarCode>77777777777777777777</BarCode><MagCode> FFFFFFFFFFFFFFFFFFFFFFFF </MagCode><ExternalTicketCode>ABCDEFGHIJKLMNOPQRSTUVWXYZ</ExternalTicketCode></Ticket>"
+ "<Validity><StartDate>09-06-15</StartDate><EndDate>30-12-31</EndDate></Validity><TicketFlag><TicketFlagNumber>0</TicketFlagNumber></TicketFlag>"
+ "<TicketAttribute>0</TicketAttribute><TicketNote>I AM A TICKET NOTE</TicketNote><BiometricLevel>0</BiometricLevel>"
+ "</TicketInfo><TransactionDSSN><Site>WDW</Site><Station>CAS999</Station><Date>09-06-15</Date><TransactionId>123</TransactionId></TransactionDSSN>"
+ "<TransactionCOD>99999999999999999</TransactionCOD></CreateTransaction></Command>";
		String response=null;
		try{
			response=ClientUtility.convertResponse(xmlresponse);
			assertNotNull(response);
		}catch(Exception dtie){
			Assert.fail("Unexpected Exception :"+dtie.getMessage());
		}
		
	}
	/**
	 * 
	 */
	@Test
	public void testConvertRequest() {
		String xmlrequest =	"<Envelope>" +
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
		String response = null;
		try {
			response = ClientUtility.convertRequest(xmlrequest);
			assertNotNull(response);
		} catch (Exception dtie) {
			Assert.fail("Unexpected Exception :" + dtie.getMessage());
		}
		
		
	}
	


}
