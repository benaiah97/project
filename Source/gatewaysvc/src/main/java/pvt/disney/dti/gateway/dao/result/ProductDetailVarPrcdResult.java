package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.disney.exception.WrappedException;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from Product Detail Result queries.
 * 
 * @author lewit019
 * 
 */
public class ProductDetailVarPrcdResult implements ResultSetProcessor {

	/** List of database products. */
	private HashMap<String, String> results = new HashMap<>();

	/**
	 * Constructor for ProductDetailResult
	 */
	public ProductDetailVarPrcdResult() {
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
		results.put(rs.getString("PDT_CODE"), rs.getString("EXTRNL_PRCD"));
	}

}
