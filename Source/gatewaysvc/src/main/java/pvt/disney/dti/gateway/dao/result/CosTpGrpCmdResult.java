package pvt.disney.dti.gateway.dao.result;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.disney.exception.WrappedException;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.data.common.CosTpGrpCmdTO;


/**
 * The Class CosTpGrpCmdResult.
 */
public class CosTpGrpCmdResult implements ResultSetProcessor {

	/**  transfer object to hold result. */
	private ArrayList<CosTpGrpCmdTO> results = new ArrayList<CosTpGrpCmdTO>();

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Instantiates a new cos tp grp cmd result.
	 */
	public CosTpGrpCmdResult() {
		super();
	}
	
	 /**
 	 * Returns the result set.
 	 *
 	 * @param rs the rs
 	 * @throws SQLException the SQL exception
 	 * @throws WrappedException the wrapped exception
 	 */
		@Override
		public void processNextResultSet(ResultSet rs) throws SQLException, WrappedException {
			CosTpGrpCmdTO cosTpGrpCmdTO = new CosTpGrpCmdTO();
			
			cosTpGrpCmdTO.setCmdid(rs.getInt("CMD_ID"));
			cosTpGrpCmdTO.setCosgrpid(rs.getInt("COSGRP_ID"));
			cosTpGrpCmdTO.setCosxrefid(rs.getInt("COSXREF_ID"));
			cosTpGrpCmdTO.setEndpointurl(rs.getString("ENDPOINT_URL"));
			cosTpGrpCmdTO.setGrpownerid(rs.getString("GRP_OWNERID"));
			cosTpGrpCmdTO.setOwnerid(rs.getString("COS_GRP_OWNERID"));
			cosTpGrpCmdTO.setGroupname(rs.getString("GROUP_NAME"));
			cosTpGrpCmdTO.setCmdcode(rs.getString("CMD_CODE"));
		
			results.add(cosTpGrpCmdTO);
			
			this.recordsProcessed++;
		}

		/**
		 * Returns the process object.
		 *
		 * @return the processed object
		 * @throws WrappedException the wrapped exception
		 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
		 */
		@Override
		public Object getProcessedObject() throws WrappedException {
			return results;
		}
	
	
}
