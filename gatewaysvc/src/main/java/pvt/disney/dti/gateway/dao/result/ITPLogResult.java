package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import pvt.disney.dti.gateway.dao.data.ITPLogEntry;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from Input Ticket Provider log queries.
 * 
 * @author lewit019
 * 
 */
public class ITPLogResult implements ResultSetProcessor {

	/** An ITPLogEntry result object. */
	private ITPLogEntry itpLogEntry = null;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for ITPLogResult
	 */
	public ITPLogResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {
		return itpLogEntry;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		itpLogEntry = new ITPLogEntry();

		itpLogEntry.setTransId(rs.getInt("TRANS_ID"));
		itpLogEntry.setPayloadId(rs.getString("PAYLOAD_ID"));
		itpLogEntry.setTargSys(rs.getString("TARG_SYS"));
		itpLogEntry.setXmlVersion(rs.getString("XML_VERSION"));
		itpLogEntry.setCommProtocol(rs.getString("COMM_PROTOCOL"));
		itpLogEntry.setCommMethod(rs.getString("COMM_METHOD"));
		itpLogEntry.setInboundTsId(rs.getLong("INBOUND_TS_ID"));

		this.recordsProcessed++;

		return;

	}

}
