package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets for entity actor queries.
 * 
 * @author lewit019
 * 
 */
public class EntityActorResult implements ResultSetProcessor {

	/** The result of the SQL. */
	private String result;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for EntityActorResult
	 */
	public EntityActorResult() {
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

		String activeString = rs.getString("ACTOR");

		if (activeString == null) return;
		else result = activeString;

		this.recordsProcessed++;

		return;

	}

}
