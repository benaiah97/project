package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from Input Ticket Seller Log queries.
 * 
 * @author lewit019
 * 
 */
public class ITSLogResult implements ResultSetProcessor {

	/** The Input Ticket Seller primary key. */
	private Integer itsTransId = null;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for ITSLogResult
	 */
	public ITSLogResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {

		return itsTransId;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		int transId = rs.getInt("TRANS_ID");
		itsTransId = Integer.valueOf(transId);

		this.recordsProcessed++;

		return;

	}

}
