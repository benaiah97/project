package pvt.disney.dti.gateway.dao;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import mockit.Mock;
import mockit.MockUp;
import oracle.sql.CLOB;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.util.ResourceLoader;

public class TestLogTransaction extends CommonTestDao{
	@Test
	public void testLogTransaction(){
		LogTransaction logTransaction=null;
		try{
			logTransaction=new LogTransaction();
		}catch(DTIException dtie){
			assertEquals(DTIErrorCode.FAILED_DB_OPERATION_SVC, dtie.getDtiErrorCode());
			assertEquals("Unable to get properties for LogTransaction class.",
					dtie.getLogMessage());
		}
		setMockProperty();
		try{
			logTransaction=new LogTransaction();
		}catch(DTIException dtie){
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
		
	}
public void testInsertITSLog() {
		String payloadId = "tsPayload";
		int entityId = 1;
		String inXMLString = "<Envelope>" + "<Header>"
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
				+ "</TicketCreation>" + "</Body>" + "</Envelope>";;
		Integer transIdITS = new Integer(1);

		new MockUp<DriverManager>() {
			@Mock
			public void registerDriver(java.sql.Driver driver)
			        throws SQLException {
			}
		};
		/* Mocking the ResourceBundle convertResourceBundleToProperties */
		new MockUp<DriverManager>() {
			@Mock
			 public  Connection getConnection(String url,
				        String user, String password) throws SQLException {
				Connection con=EasyMock.createMock(Connection.class);
				return con;
			}
		};
			new MockUp<LogTransaction>() {
			@Mock
			private CLOB getXMLCLOB(String xmlData, java.sql.Connection c){
				CLOB clob=EasyMock.createMock(CLOB.class);
				return clob;
			}
		};
		/*new MockUp<Connection>() {
			@Mock
			PreparedStatement prepareStatement(String sql)
			        throws SQLException{
				PreparedStatement ps=EasyMock.createMock(PreparedStatement.class);
				return ps;
			}
		};*/
		
		setMockProperty();
		try{
			LogTransaction logTransaction=new LogTransaction();
			DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.QUERYTICKET);
			createCommonRequest(dtiTxn);
			dtiTxn.setTpRefNum(new Integer(1));
			logTransaction.insertITPLog(dtiTxn, inXMLString);
		}catch(DTIException dtie){
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	
	}
}
