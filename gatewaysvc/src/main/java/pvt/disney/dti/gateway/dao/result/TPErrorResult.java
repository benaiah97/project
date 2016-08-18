package pvt.disney.dti.gateway.dao.result;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import pvt.disney.dti.gateway.data.common.DTIErrorTO;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets of TP Error queries.
 * 
 * @author lewit019
 * 
 */
public class TPErrorResult implements ResultSetProcessor {

	/** DTI Error Object. */
	private DTIErrorTO result = null;

	private String tpsCriticality = "";

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for TPErrorResult
	 */
	public TPErrorResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {
		return result;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		String errorNumberString = rs.getString("ERR_NBR");
		BigInteger errorNumber = new BigInteger(errorNumberString);
		String errorCriticality = rs.getString("DTI_CRITICALITY");
		String errorClass = rs.getString("ERR_CLASS");
		String errorName = rs.getString("ERR_NAME");
		String tpCriticality = rs.getString("TPS_CRITICALITY");

		result = new DTIErrorTO(errorNumber, errorClass, errorName,
				errorCriticality, tpCriticality);

		this.recordsProcessed++;

		return;

	}

	/**
	 * @return the tpsCriticality
	 */
	public String getTpsCriticality() {
		return tpsCriticality;
	}

	/**
	 * @param tpsCriticality
	 *            the tpsCriticality to set
	 */
	public void setTpsCriticality(String tpsCriticality) {
		this.tpsCriticality = tpsCriticality;
	}

}
