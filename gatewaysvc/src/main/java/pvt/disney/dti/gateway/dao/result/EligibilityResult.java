package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from Eligibility Queries.
 * 
 * @author lewit019
 * 
 */
public class EligibilityResult implements ResultSetProcessor {

	/** The result of the SQL. */
	private Boolean result = null;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for EligibilityResult
	 */
	public EligibilityResult() {
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

		String activeString = rs.getString("ACTIVE_IND");

		if (activeString == null) return;
		else {
			if (activeString.compareToIgnoreCase("T") == 0) result = new Boolean(
					true);
			else result = new Boolean(false);
		}

		this.recordsProcessed++;

		return;

	}

}
