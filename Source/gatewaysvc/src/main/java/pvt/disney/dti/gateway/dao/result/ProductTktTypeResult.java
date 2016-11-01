package pvt.disney.dti.gateway.dao.result;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.DBProductTO;

import com.disney.exception.WrappedException;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from Product Tkt Type Result.
 * 
 * @author lewit019
 * 
 */
public class ProductTktTypeResult implements ResultSetProcessor {

	/** List of db products. */
	private ArrayList<DBProductTO> results = new ArrayList<DBProductTO>();

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for ProductTktTypeResult
	 */
	public ProductTktTypeResult() {
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

		DBProductTO aProduct = new DBProductTO();

		// PDTID
		aProduct.setPdtid(new BigInteger(rs.getString("PDTID")));

		// TKT_NBR
		aProduct.setMappedProviderTktNbr(new BigInteger(rs.getString("TKT_NBR")));

		// TKT_NAME
		aProduct.setMappedProviderTktName(rs.getString("TKT_NAME"));

		// ACTIVE_IND
		String activeIndString = rs.getString("ACTIVE_IND");
		if (activeIndString.compareToIgnoreCase("T") == 0) aProduct
				.setMappedProviderTktActive(true);
		else aProduct.setMappedProviderTktActive(false);

		results.add(aProduct);

		this.recordsProcessed++;

		return;

	}
}
