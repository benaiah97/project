package pvt.disney.dti.gateway.dao;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mockit.Mock;
import mockit.MockUp;
import oracle.sql.CLOB;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.LogTransaction;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.xml.DLRTestUtil;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;

/**
 * @author MISHP012
 *  JUnit for LogTransaction
 */
public class TestLogTransaction extends CommonTestUtils {
	private static Connection con = null;
	private static CLOB clob = null;
	private static ResultSet rs = null;
	private static DTITransactionTO dtiTxn = null;
	private static LogTransaction logTransaction = null;
	private static String INXMLSTRING = "<Envelope>" + "<Header>"
			+ "<SourceID>WDPRONADLR</SourceID>" + "<MessageID>0</MessageID>"
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

	public static void mockConnection() throws SQLException {
		initMon();
		new MockUp<DriverManager>() {
			@Mock
			public Connection getConnection(String url, String user,
					String password) {
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
				return con;
			}
		};
		new MockUp<LogTransaction>() {
			@Mock
			private CLOB getXMLCLOB(String xmlData, java.sql.Connection c) {
				return clob;
			}
		};
	}

	public static void initMon() throws SQLException {
		clob = PowerMock.createMock(CLOB.class);
		con = PowerMock.createMock(Connection.class);
		rs = PowerMock.createMock(ResultSet.class);
		Clob iClob = PowerMock.createMock(Clob.class);
		PreparedStatement stmt = PowerMock.createMock(PreparedStatement.class);
		try {
			EasyMock.expect(
					con.prepareStatement(EasyMock.anyObject(String.class)))
					.andReturn(stmt).anyTimes();
			EasyMock.expect(stmt.executeUpdate()).andReturn(1).anyTimes();
			EasyMock.expect(stmt.executeQuery()).andReturn(rs).anyTimes();
			EasyMock.expect(rs.next()).andReturn(true).once();
			EasyMock.expect(rs.next()).andReturn(false).once();
			EasyMock.expect(rs.getObject(1)).andReturn(new Object()).anyTimes();
			EasyMock.expect(rs.getClob(1)).andReturn(iClob).anyTimes();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.setAutoCommit(true);
		stmt.setObject(1, clob);
		stmt.close();
		con.close();
		rs.close();
		EasyMock.expectLastCall();
		EasyMock.replay(con);
		EasyMock.replay(stmt);
		EasyMock.replay(rs);
	}

	/**
	 * @throws SQLException
	 *             SetUP
	 */
	@Before
	public void setUp() throws SQLException {
		setMockProperty();
		mockConnection();
	}

	/**
	 * JUnit for InsertITSLog
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testInsertITSLog() throws SQLException {
		try {
			logTransaction = new LogTransaction();
			dtiTxn = new DTITransactionTO(TransactionType.QUERYTICKET);
			logTransaction.insertITSLog("1", 1, INXMLSTRING, 1);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}

	/**
	 * JUnit for InsertITPLog
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testInsertITPLog() throws SQLException {
		try {
			logTransaction = new LogTransaction();
			dtiTxn = new DTITransactionTO(TransactionType.QUERYTICKET);
			createCommonRequest(dtiTxn);
			dtiTxn.setTpRefNum(new Integer(1));
			logTransaction.insertITPLog(dtiTxn, INXMLSTRING);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}

	/**
	 * @throws SQLException
	 *             JUnit for InsertOTPLog
	 */
	@Test
	public void testInsertOTPLog() throws SQLException {
		try {
			logTransaction = new LogTransaction();
			dtiTxn = new DTITransactionTO(TransactionType.QUERYTICKET);
			createCommonRequest(dtiTxn);
			dtiTxn.setTpRefNum(new Integer(1));
			logTransaction.insertOTPLog(dtiTxn, INXMLSTRING);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}

	/**
	 * @throws SQLException
	 *             JUnit for InsertOTSLog
	 */
	@Test
	public void testInsertOTSLog() throws SQLException {
		try {
			logTransaction = new LogTransaction();
			dtiTxn = new DTITransactionTO(TransactionType.QUERYTICKET);
			createCommonRequest(dtiTxn);
			dtiTxn.setTpRefNum(new Integer(1));
			dtiTxn.setTransIdOTS(1);
			dtiTxn.setTransIdITS(1);
			DTIResponseTO dtiResponseTO = new DTIResponseTO();
			PayloadHeaderTO payloadHeaderTO = new PayloadHeaderTO();
			payloadHeaderTO.setTsPayloadID("1");
			dtiResponseTO.setPayloadHeader(payloadHeaderTO);
			dtiTxn.setResponse(dtiResponseTO);
			logTransaction.insertOTSLog(dtiTxn, INXMLSTRING);
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}

	/**
	 * @throws SQLException
	 *             JUnit for SelectOTSLog
	 */
	@Test
	public void testSelectOTSLog() throws SQLException {
		try {
			logTransaction = new LogTransaction();
			logTransaction.selectOTSLog("1");
		} catch (DTIException dtie) {
			Assert.fail("Unexpected Exception::" + dtie.getLogMessage());
		}
	}
}