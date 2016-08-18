package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from Shell Type result queries.
 * 
 * @author lewit019
 * 
 */
public class ShellTypeResult implements ResultSetProcessor {

	/** List of Shell Types. */
	private ArrayList<Integer> results = new ArrayList<Integer>();

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for ShellTypeResult
	 */
	public ShellTypeResult() {
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

		Integer anInt = new Integer(rs.getInt("SHELL_NBR"));

		results.add(anInt);

		this.recordsProcessed++;

		return;

	}

}
