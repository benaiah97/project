package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from Visual ID queries.
 * 
 * @since DTI Gateway 2.15
 * @author lewit019
 * 
 */
public class VisualIdResult implements ResultSetProcessor {

	/** The visual ID string object. */
	private String visualId = null;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for EntityResult
	 */
	public VisualIdResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {
		return visualId;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		// Non-nullable fields
		visualId = rs.getString("MEDIA_VSUAL_ID");

		this.recordsProcessed++;

		return;

	}

	/**
	 * Returns the number of records processed.
	 * 
	 * @return
	 */
	public int getRecordsProcessed() {
		return recordsProcessed;
	}

}
