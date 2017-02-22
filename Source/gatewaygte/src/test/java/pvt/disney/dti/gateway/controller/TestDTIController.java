package pvt.disney.dti.gateway.controller;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;
import java.util.ResourceBundle;






import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;

import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.LogTransaction;
import pvt.disney.dti.gateway.dao.result.AttributeResult;
import pvt.disney.dti.gateway.dao.result.EntityResult;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.rules.ContentRules;
import pvt.disney.dti.gateway.test.util.DTITestUtilities;
import pvt.disney.dti.gateway.util.ResourceLoader;

public class TestDTIController {
	@Mocked
	ResourceBundle resourceBundle;
	Properties prop = null;
	ResultSet rs = null;
	ResultSet entityRs = null;
	ResultSet attributeRs = null;
	ResultSet attributeRs1 = null;
	ResultSet attributeRs2 = null;
	int i = 0;

	@Before
	public void setUp() throws Exception {
		rs = PowerMock.createMock(ResultSet.class);
		entityRs = PowerMock.createMock(ResultSet.class);
		attributeRs = PowerMock.createMock(ResultSet.class);
		attributeRs1 = PowerMock.createMock(ResultSet.class);
		attributeRs2 = PowerMock.createMock(ResultSet.class);

		setEntityResultSet(entityRs);
		setResultSet(rs);

		setAttributeRS();

		try {
			prop = new Properties();
			InputStream inStream = this.getClass().getResourceAsStream(
					"/dtiApp.properties");
			InputStream inStream1 = this.getClass().getResourceAsStream(
					"/IAGO.properties");

			prop.load(inStream);
			prop.load(inStream1);

		} catch (Exception e) {

		}

		new MockUp<ContentRules>() {
			@Mock
			public String getProperty(String Key) {
				return "test";

			}
		};

		new MockUp<ResourceLoader>() {
			@Mock
			public Properties convertResourceBundleToProperties(
					ResourceBundle Key) {
				return prop;
			}
		};

		new MockUp<ResourceLoader>() {
			@Mock
			public ResourceBundle getResourceBundle(String prop) {

				return resourceBundle;
			}
		};

		new MockUp<DAOHelper>() {
			@Mock
			public int prepareAndExecuteSql(Object[] inputValues, String sql,
					boolean query, ResultSetProcessor theProcessor)
					throws Exception {

				if (theProcessor != null) {
					if (theProcessor instanceof EntityResult) {
						theProcessor.processNextResultSet(entityRs);
					} else if (theProcessor instanceof AttributeResult) {

						theProcessor.processNextResultSet(attributeRs);
						theProcessor.processNextResultSet(attributeRs1);
						theProcessor.processNextResultSet(attributeRs2);
					} else {

						theProcessor.processNextResultSet(rs);
					}

				}
				return 1;

			}

		};

		new MockUp<LogTransaction>() {
			@Mock
			boolean insertITSLog(String payloadId, int entityId,
					String inXMLString, Integer transIdITS) throws DTIException {

				return true;

			}

		};

		new MockUp<DAOHelper>() {
			@Mock
			public int processInsert(Object[] inputValues) {

				return 1;

			}

		};

		new MockUp<DAOHelper>() {
			@Mock
			public int processUpdate(Object[] inputValues) {

				return 1;

			}

		};

		new MockUp<DAOHelper>() {
			@Mock
			public int processDelete(Object[] inputValues) {

				return 1;

			}

		};

	}

	private void setResultSet(ResultSet rs) throws Exception {
		EasyMock.expect(rs.getString(EasyMock.anyObject(String.class)))
				.andReturn("1").anyTimes();

		EasyMock.expect(rs.getLong(EasyMock.anyObject(String.class)))
				.andReturn(1L).anyTimes();

		EasyMock.expect(rs.getDouble(EasyMock.anyObject(String.class)))
				.andReturn(1.0).anyTimes();

		EasyMock.expect(rs.getInt(EasyMock.anyObject(String.class)))
				.andReturn(1).anyTimes();

		EasyMock.expect(rs.getTimestamp(EasyMock.anyObject(String.class)))
				.andReturn(new Timestamp(System.currentTimeMillis()))
				.anyTimes();

		EasyMock.expect(rs.getDate(EasyMock.anyObject(String.class)))
				.andReturn(new Date(System.currentTimeMillis())).anyTimes();
		EasyMock.expect(rs.getBoolean(EasyMock.anyObject(String.class)))
				.andReturn(true).anyTimes();

	}

	// /Entity result set
	private void setEntityResultSet(ResultSet rs) throws Exception {

		EasyMock.expect(rs.getInt("ENTITYID")).andReturn(9153).anyTimes();

		EasyMock.expect(rs.getString("ACTIVE_IND")).andReturn("T").anyTimes();

		EasyMock.expect(rs.getDate("START_VALID_DATE"))
				.andReturn(new Date(System.currentTimeMillis())).anyTimes();
		EasyMock.expect(rs.getDate("END_VALID_DATE")).andReturn(addDate())
				.anyTimes();

		EasyMock.expect(rs.getString("ENTITY_CODE")).andReturn("001")
				.anyTimes();

		EasyMock.expect(rs.getString("MISMATCH_IND")).andReturn("F").anyTimes();

		EasyMock.expect(rs.getString("IATA_REQ_IND")).andReturn("T").anyTimes();

		EasyMock.expect(rs.getString("PYMT_REQ_IND")).andReturn("F").anyTimes();

		EasyMock.expect(rs.getString("SHELL_CHECK_IND")).andReturn("T")
				.anyTimes();

		EasyMock.expect(rs.getString("START_VALID_TIME")).andReturn("11:05")
				.anyTimes();

		EasyMock.expect(rs.getString("END_VALID_TIME")).andReturn("07:00")
				.anyTimes();

		EasyMock.expect(rs.getString("CUSTOMERID")).andReturn("379").anyTimes();

		EasyMock.expect(rs.getString("ECOMMERCE")).andReturn(" ").anyTimes();

		EasyMock.expect(rs.getString("DEF_PYMTDATA")).andReturn("6626")
				.anyTimes();

		EasyMock.expect(rs.getString("DEF_IATA")).andReturn(" ").anyTimes();

		EasyMock.expect(rs.getInt("MAC_ENTITYID")).andReturn(9152).anyTimes();

		EasyMock.expect(rs.getString("VALIDITY_DATES_IND")).andReturn("F")
				.anyTimes();

		EasyMock.expect(rs.getInt("DEF_PYMTID")).andReturn(3).anyTimes();

		EasyMock.expect(rs.getLong("DEF_SALESREPID")).andReturn(new Long(10))
				.anyTimes();

	}

	private void setAttributeResultSet(ResultSet rs, int i) throws Exception {

		EasyMock.expect(rs.getString("ATTR_VALUE")).andReturn("11111").times(3);

		EasyMock.expect(rs.getString("ACTOR")).andReturn("MGR").times(3);
		if (i == 0) {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("OpArea")
					.times(3);

		} else if (i == 1) {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("User")
					.times(3);
		} else {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("Pass")
					.times(3);
		}

		EasyMock.expect(rs.getString("ACTIVE_IND")).andReturn("T").times(3);
		EasyMock.expect(rs.getString("CMD_CODE")).andReturn("QueryReservation")
				.times(3);

	}

	private Date addDate() {
		Date dt = new Date(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		dt.setTime(cal.getTimeInMillis());
		return dt;

	}

	private void setAttributeRS() throws Exception {
		setAttributeResultSet(attributeRs, 0);
		setAttributeResultSet(attributeRs1, 1);
		setAttributeResultSet(attributeRs2, 2);
	}

	@Test
	public void testProcessRequest() throws Exception {
		String requestXML = null;
		String fileName = new String(
				"./config/QueryTicket/QueryTicket_01_Req_XML.xml");
		EasyMock.replay(rs);
		EasyMock.replay(entityRs);
		EasyMock.replay(attributeRs);
		EasyMock.replay(attributeRs1);
		EasyMock.replay(attributeRs2);
		try {

			requestXML = DTITestUtilities.getXMLfromFile(fileName);
		} catch (Exception e) {
			e.printStackTrace();

		}
		DTIController dTIController = new DTIController();

		InputStream inputStream = new ByteArrayInputStream(
				requestXML.getBytes());

		dTIController.processRequest(inputStream);
	}

}
