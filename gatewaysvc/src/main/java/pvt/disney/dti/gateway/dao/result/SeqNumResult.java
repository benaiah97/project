package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from sequence number queries.
 * 
 * @author lewit019
 * 
 */
public class SeqNumResult implements ResultSetProcessor {

	/** A sequence number result. */
	private Integer seqNum;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for SeqNumResult
	 */
	public SeqNumResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {

		if (this.recordsProcessed == 0) {
			throw new WrappedException(
					"Unable to obtain TpRefNumResult from database.");
		}

		return seqNum;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		int tpRefNumInt = rs.getInt("NEXTVAL");
		seqNum = Integer.valueOf(tpRefNumInt);

		this.recordsProcessed++;

		return;

	}

}
