package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.data.common.CosGrpTO;
/**
 * Result Set process for class of service
 * @author moons012
 *
 */
public class CosGrpResult implements ResultSetProcessor {
	
	/** transfer object to hold result	 */
	private CosGrpTO cosGrpTO = null;
	
	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;
	
	public CosGrpResult() {
		super();
	}
	
   /**
    * Returns the result set
    */
	@Override
	public void processNextResultSet(ResultSet rs) throws SQLException, WrappedException {
		cosGrpTO = new CosGrpTO();
		
		cosGrpTO.setCosgrpid( rs.getInt("COSGRP_ID") );
		cosGrpTO.setGroupName( rs.getString("GROUP_NAME") );
		cosGrpTO.setEndpointUrl( rs.getString("ENDPOINT_URL") );
		cosGrpTO.setOwnerId( rs.getString("OWNERID") );
		
		this.recordsProcessed++;
	}

	/**
	 * Returns the process object
	 *  @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	@Override
	public Object getProcessedObject() throws WrappedException {
		return cosGrpTO;
	}

}
