package pvt.disney.dti.gateway.test.util;

import java.sql.ResultSet;
import java.util.HashMap;

import mockit.Mock;
import mockit.MockUp;

import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;

import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.dao.result.AttributeResult;
import pvt.disney.dti.gateway.data.common.AttributeTO;

public class DTIMockUtil {
	static ResultSet attributeRs = null;
	static ResultSet attributeRs1 = null;
	static ResultSet attributeRs2 = null;
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
	
	
	private static void init()throws Exception{
		attributeRs = PowerMock.createMock(ResultSet.class);
		attributeRs1 = PowerMock.createMock(ResultSet.class);
		attributeRs2 = PowerMock.createMock(ResultSet.class);
		//setResultSet(rs);
		setAttributeRS();
		
		EasyMock.replay(attributeRs);
		EasyMock.replay(attributeRs1);
		EasyMock.replay(attributeRs2);
	}
	
	
	private static void setAttributeRS() throws Exception {
		setAttributeResultSet(attributeRs, 0);
		setAttributeResultSet(attributeRs1, 1);
		setAttributeResultSet(attributeRs2, 2);
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
