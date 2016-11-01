package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;

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
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {
		return result;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
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
