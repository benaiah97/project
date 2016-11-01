package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from Void Code Lookup queries.
 * 
 * @author lewit019
 * 
 */
public class VoidCodeLookupResult implements ResultSetProcessor {

	/** A VoidCodeLookup result. */
	private Integer lookupValue;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for VoidCodeLookup
	 */
	public VoidCodeLookupResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {

		if (this.recordsProcessed == 0) {
			throw new WrappedException(
					"Unable to obtain TpRefNumResult from database.");
		}

		return lookupValue;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		lookupValue = rs.getInt("LOOKUP_VALUE");

		this.recordsProcessed++;

		return;

	}

}
