package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.PaymentLookupTO;

import com.disney.exception.WrappedException;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from payment lookup queries.
 * 
 * @author lewit019
 * 
 */
public class PaymentLookupResult implements ResultSetProcessor {

	/** List of payment lookups. */
	private ArrayList<PaymentLookupTO> results = new ArrayList<PaymentLookupTO>();

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for PaymentLookupResult
	 */
	public PaymentLookupResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {
		return results;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		PaymentLookupTO aLookup = new PaymentLookupTO();

		aLookup.setLookupValue(rs.getString("LOOKUP_VALUE"));
		aLookup.setPymtCode(rs.getString("PYMT_CODE"));

		results.add(aLookup);

		this.recordsProcessed++;

		return;

	}

}
