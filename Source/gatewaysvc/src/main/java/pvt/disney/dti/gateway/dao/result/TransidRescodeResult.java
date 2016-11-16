package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import com.disney.exception.WrappedException;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.dao.data.TransidRescode;


/**
 * The Class TransidRescodeResult.
 * 
 * @author MOONS012
 * @since 2.16.3
 */
public class TransidRescodeResult implements ResultSetProcessor {

	/** The results. */
	private ArrayList<TransidRescode> results = new ArrayList<TransidRescode>();

	/** The number of records processed by this result set processor. */
	//private int recordsProcessed = 0;
	
	/**
	 * Instantiates a new transid rescode result.
	 */
	public TransidRescodeResult() {
		super();
	}

	/* (non-Javadoc)
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	@Override
	public void processNextResultSet(ResultSet rs) throws SQLException, WrappedException {
		TransidRescode transidRescodeTO = new TransidRescode();
		
		//TSTRANSID
		transidRescodeTO.setTsTransid( rs.getString("TS_TRANSID") );
		
		//RESCODE
		transidRescodeTO.setRescode( rs.getString("RESCODE") );
		
		//CREATION_DATE - check and convert to GregorianCalendar and set
		Date creationDate =  rs.getDate("CREATION_DATE");
		if (creationDate != null) {
			GregorianCalendar calCreationDate = (GregorianCalendar)GregorianCalendar.getInstance();
			calCreationDate.setTime(creationDate);
			transidRescodeTO.setCreationDate( calCreationDate);
		}

	}

	/* (non-Javadoc)
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	@Override
	public Object getProcessedObject() throws WrappedException {
		return results;
	}

}
