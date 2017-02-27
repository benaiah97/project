package pvt.disney.dti.gateway.test.util;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;

import mockit.Mock;
import mockit.MockUp;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.dao.data.DBTicketAttributes;
import pvt.disney.dti.gateway.dao.result.AttributeResult;
import pvt.disney.dti.gateway.dao.result.TicketAttributeResult;
import pvt.disney.dti.gateway.data.common.AttributeTO;

public class DTIMockUtil {
	static ResultSet attributeRs = null;
	static ResultSet attributeRs1 = null;
	static ResultSet attributeRs2 = null;
	static ResultSet rs = null;
	static ResultSetProcessor theProcessor=null;
	
	public static void mockAttributeKey() {
		try {
			init();
			new MockUp<DAOHelper>() {

				@SuppressWarnings("unchecked")
				@Mock
				protected Object processQuery(Object[] values) {

					HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeMap = null;
					theProcessor = new AttributeResult();
					try {
						theProcessor.processNextResultSet(attributeRs);
						theProcessor.processNextResultSet(attributeRs1);
						theProcessor.processNextResultSet(attributeRs2);
						attributeMap = (HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>) theProcessor
								.getProcessedObject();
					} catch (Exception e) {

					}

					return attributeMap;
				}
			};
		} catch (Exception e) {

		}

	}
	
	
	public static void mockTicketAttribute(){
		
		try{
			init();
			new MockUp<DAOHelper>() {

				@Mock
				protected Object processQuery(Object[] values) {

					DBTicketAttributes dbTicketAttributes = null;
					 theProcessor = new TicketAttributeResult();
					try {
						theProcessor.processNextResultSet(rs);
						dbTicketAttributes = (DBTicketAttributes) theProcessor
								.getProcessedObject();
					} catch (Exception e) {

					}

					return dbTicketAttributes;
				}
			};

		}catch(Exception e){
			
		}
	
	}
	
	
	private static void init()throws Exception{
		rs = PowerMock.createMock(ResultSet.class);
		attributeRs = PowerMock.createMock(ResultSet.class);
		attributeRs1 = PowerMock.createMock(ResultSet.class);
		attributeRs2 = PowerMock.createMock(ResultSet.class);
		setResultSet(rs);
		//setResultSet(rs);
		setAttributeRS();
		EasyMock.replay(rs);
		EasyMock.replay(attributeRs);
		EasyMock.replay(attributeRs1);
		EasyMock.replay(attributeRs2);
	}
	
	
	private static void setAttributeRS() throws Exception {
		setAttributeResultSet(attributeRs, 0);
		setAttributeResultSet(attributeRs1, 1);
		setAttributeResultSet(attributeRs2, 2);
	}
	
	
	private static void setResultSet(ResultSet rs) throws Exception {
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


	private static void setAttributeResultSet(ResultSet rs, int i) throws Exception {

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

	

}
