package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import pvt.disney.dti.gateway.dao.data.DBTicketAttributes;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from Ticket Attribute result queries.
 * 
 * @author lewit019
 * 
 */
public class TicketAttributeResult implements ResultSetProcessor {

	/** DB Ticket Attributes */
	private DBTicketAttributes result = null;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for TicketAttributeResult
	 */
	public TicketAttributeResult() {
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

		result = new DBTicketAttributes();

		// TKT_CODE
		result.setTktCode(rs.getString("TKT_CODE"));

		// AGE_GROUPID
		result.setAgeGroup(rs.getString("AGE_GROUPID"));

		// MEDIA_TYPEID
		result.setMediaType(rs.getString("MEDIA_TYPEID"));

		// PASS_TYPEID
		result.setPassType(rs.getString("PASS_TYPEID"));

		// PASS_RENEWID
		result.setPassRenew(rs.getString("PASS_RENEWID"));

		// PASS_CLASSID
		result.setPassClass(rs.getString("PASS_CLASSID"));

		// RESIDENTID
		result.setResident(rs.getString("RESIDENTID"));

		this.recordsProcessed++;

		return;

	}

}
