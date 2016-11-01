package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import com.disney.logging.EventLogger;

/**
 * This class processes the result set for the transaction logged queries.
 * 
 * @author mansor7 lewit019
 * 
 */
public class TxnLoggedResult implements ResultSetProcessor {

	private Integer count = null;

	EventLogger evl = EventLogger.getLogger(this.getClass());

	private int recordsProcessed = 0;

	/**
	 * Constructor
	 */
	public TxnLoggedResult() {
		super();
	}

	/**
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {

		count = new Integer(this.recordsProcessed);

		return count;
	}

	/**
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		String tktSlrTxnId = rs.getString("TKT_SLR_TXN_ID");
		if (tktSlrTxnId != null) {
			recordsProcessed++;
		}

		return;
	}
}
