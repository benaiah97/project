package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.disney.exception.WrappedException;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.data.common.EnttlGuidTO;

public class EnttlGuidResult implements ResultSetProcessor {
	EnttlGuidTO enttlGuidTO = null;

	/**
	 * Constructor for EnttlGuidResult
	 */
	public EnttlGuidResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {
		return enttlGuidTO;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {
		enttlGuidTO = new EnttlGuidTO();
		enttlGuidTO.setGuId(rs.getString("GUID"));
	}
}
