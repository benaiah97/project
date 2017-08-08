package pvt.disney.dti.gateway.client;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;

import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

import org.apache.xerces.parsers.DOMParser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;
import pvt.disney.dti.gateway.util.ResourceLoader;

public class ProviderClientTestcase extends CommonTestUtils{
	
	@Before
	public void setUp(){
		setMockProperty();
		
      
      	}
	//@Test
	public void testSendRequestToProvider(){
		
		String xmlRequest =
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
		
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.RENEWENTITLEMENT);
		DTIMockUtil.mockParseProcess(true);
		dtiTxn.setProvider(ProviderType.DLRGATEWAY);
		try{
			
			String result=ProviderClient.sendRequestToProvider(dtiTxn, xmlRequest);
			assertNotNull(result);
		}catch(DTIException dtie){
			//assertEquals(DTIErrorCode.TRANSACTION_TIME_OUT, dtie.getDtiErrorCode());
		}
		dtiTxn.setProvider(ProviderType.HKDNEXUS);
		try{
			//DTIMockUtil.mockParseProcess(true);
			String result=ProviderClient.sendRequestToProvider(dtiTxn, xmlRequest);
			assertNotNull(result);
		}catch(DTIException dtie){
			//assertEquals(DTIErrorCode.TP_INTERFACE_FAILURE, dtie.getDtiErrorCode());
		}
		dtiTxn.setProvider(ProviderType.WDWNEXUS);
		try{
			//DTIMockUtil.mockParseProcess(true);
			String result=ProviderClient.sendRequestToProvider(dtiTxn, xmlRequest);
			assertNotNull(result);
		}catch(DTIException dtie){
			//assertEquals(DTIErrorCode.TP_INTERFACE_FAILURE, dtie.getDtiErrorCode());
		}
		
	}

}
