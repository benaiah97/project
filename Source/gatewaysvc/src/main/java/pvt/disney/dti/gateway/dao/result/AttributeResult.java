package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import pvt.disney.dti.gateway.data.common.AttributeTO;

import com.disney.exception.WrappedException;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from attribute queries.
 * 
 * @author lewit019
 * 
 */
public class AttributeResult implements ResultSetProcessor {

	private static final String OPAREA_STRING = "OpArea";
	private static final String USER_STRING = "User";
	private static final String PASS_STRING = "Pass";
	private static final String PRNTTKTCNTMAX_STRING = "PrntTktCntMax";
	private static final String NOPRNTTKTCNTMAX_STRING = "NoPrntTktCntMax";
	private static final String CCNOTPRESENTIND_STRING = "CCNotPresentInd";
	private static final String SITENUMBER_STRING = "SiteNumber";
	private static final String DEFAULTELECTENC_STRING = "DefaultElectEnc";

	/** Map of attributes and values. */
	private HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> results = new HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>();

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for AttributeResult
	 */
	public AttributeResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {
		return results;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		AttributeTO anAttribute = new AttributeTO();

		anAttribute.setAttrValue(rs.getString("ATTR_VALUE"));
		anAttribute.setActiveInd(rs.getString("ACTOR"));

		String cmdAttrCodeString = rs.getString("CMD_ATTR_CODE");

		if (cmdAttrCodeString.compareTo(OPAREA_STRING) == 0) {
			anAttribute.setCmdAttrCode(AttributeTO.CmdAttrCodeType.OP_AREA);
		}
		else if (cmdAttrCodeString.compareTo(USER_STRING) == 0) {
			anAttribute.setCmdAttrCode(AttributeTO.CmdAttrCodeType.USER);
		}
		else if (cmdAttrCodeString.compareTo(PASS_STRING) == 0) {
			anAttribute.setCmdAttrCode(AttributeTO.CmdAttrCodeType.PASS);
		}
		else if (cmdAttrCodeString.compareTo(PRNTTKTCNTMAX_STRING) == 0) {
			anAttribute
					.setCmdAttrCode(AttributeTO.CmdAttrCodeType.PRNT_TKT_CNT_MAX);
		}
		else if (cmdAttrCodeString.compareTo(NOPRNTTKTCNTMAX_STRING) == 0) {
			anAttribute
					.setCmdAttrCode(AttributeTO.CmdAttrCodeType.NO_PRNT_TKT_CNT_MAX);
		}
		else if (cmdAttrCodeString.compareTo(CCNOTPRESENTIND_STRING) == 0) {
			anAttribute
					.setCmdAttrCode(AttributeTO.CmdAttrCodeType.CC_NOT_PRESENT_IND);
		}
		else if (cmdAttrCodeString.compareTo(DEFAULTELECTENC_STRING) == 0) {
			anAttribute
					.setCmdAttrCode(AttributeTO.CmdAttrCodeType.DEFAULT_ELECT_ENC);
		}
		else if (cmdAttrCodeString.compareTo(SITENUMBER_STRING) == 0) {
			anAttribute.setCmdAttrCode(AttributeTO.CmdAttrCodeType.SITE_NUMBER);
		}
		else anAttribute.setCmdAttrCode(AttributeTO.CmdAttrCodeType.UNDEFINED);

		anAttribute.setActiveInd(rs.getString("ACTIVE_IND"));
		anAttribute.setCmdCode(rs.getString("CMD_CODE"));

		results.put(anAttribute.getCmdAttrCode(), anAttribute);

		this.recordsProcessed++;

		return;

	}

}
