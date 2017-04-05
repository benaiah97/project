package pvt.disney.dti.gateway.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;

public class IAGOCLientTestCase extends CommonTestUtils {

	@Mocked
	ResourceBundle resourceBundle;
	@Mocked
	URLConnection con;
	IagoClient client = null;
	Properties props = null;
	String url = "";

	@Before
	public void setUp() {

		setMockProperty();
		new MockUp<DocumentBuilder>() {
			@Mock
			public Document parse(InputStream is) throws SAXException,
					IOException, Exception {
				Document doc = null;
				DOMParser domParser = new DOMParser();
				if (is == null) {
					throw new IllegalArgumentException(
							"InputStream cannot be null");
				}
				URL url = this.getClass().getResource("/xml/iagoTestSuccess.xml");
				File file = new File(url.toURI());
				InputStream inStream = new FileInputStream(file);
				InputSource in = new InputSource(inStream);

				domParser.parse(in);
				doc = domParser.getDocument();
				domParser.dropDocumentReferences();

				return doc;
			}
		};

		client = new IagoClient();
	}

	@Test
	public void testSendRequest() {
		String reqXml = "<Envelope>" + "<Header>"
				+ "<SourceID>WDPRONADLR</SourceID>"
				+ "<MessageID>0</MessageID>"
				+ "<MessageType>CreateTickets</MessageType>" + "<TimeStamp>"
				+ DLRTestUtil.getDLRTimeStamp()
				+ "</TimeStamp>"
				+ "</Header>"
				+ "<Body>"
				+ "<TicketCreation>"
				+ "<CustomerID>300000783</CustomerID>"
				+ "<Tickets>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "<Ticket>"
				+ "<PLU>20876</PLU>"
				+ "<Price>179.00</Price>"
				+ "</Ticket>"
				+ "</Tickets>"
				+ "<Payments>"
				+ "<Payment>"
				+ "<PaymentCode>42</PaymentCode>"
				+ "<Description>Charge</Description>"
				+ "<Endorsement>123456789</Endorsement>"
				+ "<Amount>358.00</Amount>"
				+ "</Payment>"
				+ "</Payments>"
				+ "</TicketCreation>" + "</Body>" + "</Envelope>";

		DTITransactionTO dtiTxn = new DTITransactionTO(
				TransactionType.RENEWENTITLEMENT);

		IagoClient client = new IagoClient();
		try {

			String  xmlResponse = client.sendRequest(dtiTxn, reqXml);
			assertNotNull(xmlResponse);
		} catch (DTIException dtie) {
			assertEquals(DTIErrorCode.TP_INTERFACE_FAILURE,
					dtie.getDtiErrorCode());
		}

	}

}
