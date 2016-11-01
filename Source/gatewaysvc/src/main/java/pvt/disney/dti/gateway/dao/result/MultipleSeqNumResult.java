package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import pvt.disney.dti.gateway.dao.data.TransactionSequences;

import com.disney.exception.WrappedException;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import java.sql.ResultSetMetaData;

/**
 * This class is used by the DAO to process the result sets from sequence number queries.
 * 
 * @author lewit019
 * 
 */
public class MultipleSeqNumResult implements ResultSetProcessor {

	/** A sequence number result. */
	private TransactionSequences sequences;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for SeqNumResult
	 */
	public MultipleSeqNumResult() {
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
					"Unable to obtain sequences (ITP,OTP,OTS) from database.");
		}

		return sequences;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		ResultSetMetaData rsmd = rs.getMetaData();
		rsmd.getColumnCount();

		int result = rs.getInt(1);
		sequences.setTransIdITP(Integer.valueOf(result));
		result = rs.getInt(2);
		sequences.setTransIdOTP(Integer.valueOf(result));

		result = rs.getInt(3);
		sequences.setTransIdOTS(Integer.valueOf(result));

		this.recordsProcessed++;

		return;

	}

}
