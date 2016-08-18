package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets for entity active queries.
 * 
 * @author lewit019
 * 
 */
public class EntityActiveResult implements ResultSetProcessor {

	/** True if the entity is active, false if not. */
	private Boolean result;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for EntityActiveResult
	 */
	public EntityActiveResult() {
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

		if (activeString.equalsIgnoreCase("T")) {
			result = Boolean.valueOf(true);
		}

		if (activeString.equalsIgnoreCase("F")) {
			result = Boolean.valueOf(false);
		}

		this.recordsProcessed++;

		return;

	}

}
