package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from Product Shell XRef queries.
 * 
 * @author lewit019
 * 
 */
public class ProductShellXRefResult implements ResultSetProcessor {

	/** Map product to shell cross references. */
	private HashMap<String, ArrayList<Integer>> results = new HashMap<String, ArrayList<Integer>>();

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for ProductShellXRefResult
	 */
	public ProductShellXRefResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {
		return results;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		String aProductID = rs.getString("PDT_CODE");
		Integer aShellID = new Integer(rs.getInt("SHELL_NBR"));

		if (results.get(aProductID) == null) {

			ArrayList<Integer> arrayList = new ArrayList<Integer>();
			arrayList.add(aShellID);
			results.put(aProductID, arrayList);

		}
		else {

			ArrayList<Integer> arrayList = results.get(aProductID);
			arrayList.add(aShellID);

		}

		this.recordsProcessed++;

		return;

	}

}
