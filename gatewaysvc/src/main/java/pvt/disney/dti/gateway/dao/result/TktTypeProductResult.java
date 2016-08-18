package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * Returns a single DTI Product code (the first located) that matches the ticket type.
 * 
 * @author lewit019
 * 
 */
public class TktTypeProductResult implements ResultSetProcessor {

	/** Active DTI Product. */
	private String result = null;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for ProductTktTypeResult
	 */
	public TktTypeProductResult() {
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

		// PDTID
		if (result == null) {
			result = rs.getString("PDT_CODE");
		}

		this.recordsProcessed++;

		return;

	}
}
