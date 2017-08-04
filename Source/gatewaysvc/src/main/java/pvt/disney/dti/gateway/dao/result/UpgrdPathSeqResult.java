package pvt.disney.dti.gateway.dao.result;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.data.common.UpgrdPathSeqTO;

import com.disney.exception.WrappedException;

public class UpgrdPathSeqResult implements ResultSetProcessor{
	ArrayList<UpgrdPathSeqTO> pathSeqTOList =new ArrayList<UpgrdPathSeqTO>();
	/**
	 * Constructor for UpgrdPathSeqResult
	 */
	public UpgrdPathSeqResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {
		return pathSeqTOList;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 * @throws WrappedException the wrapped exception
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {
		UpgrdPathSeqTO pathSeqTO = new UpgrdPathSeqTO();
		
		// PATH_ID
		pathSeqTO.setPathId(new BigInteger(rs.getString("PATH_ID")));
		
		//PATH_ID_SEQ_NB
		pathSeqTO.setPathIdSeqNb(new BigInteger(rs.getString("PATH_ID_SEQ_NB")));
		
		//DAY_CLASS
		pathSeqTO.setDayClass(rs.getString("DAY_CLASS"));
		
		//DAY_SUBCLASS
		pathSeqTO.setDaySubclass(rs.getString("DAY_SUBCLASS"));
		
		pathSeqTOList.add(pathSeqTO);
		return;
	}
}
