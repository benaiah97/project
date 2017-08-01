package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import com.disney.exception.WrappedException;

public class ProductCatalogResult implements ResultSetProcessor{

	/** List of database products. */
	private ArrayList<DBProductTO> results = new ArrayList<DBProductTO>();

	/**
	 * Constructor for ProductDetailResult
	 */
	public ProductCatalogResult() {
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

		// PDT_CODE
		aProduct.setPdtCode(rs.getString("PDT_CODE"));
		
		// UNIT_PRICE
		aProduct.setUnitPrice(rs.getBigDecimal("UNIT_PRICE"));
		
		// TAX
		aProduct.setTax(rs.getBigDecimal("TAX"));

		// DAY_CLASS (As of 2.12)
		String dayClassString = rs.getString("DAY_CLASS");
		if (dayClassString != null) {
			aProduct.setDayClass(dayClassString);
		}
		else {
			aProduct.setDayClass("NONE");
		}

		// DAY_SUBCLASS (As of 2.16.1)
		String daySubclassString = rs.getString("DAY_SUBCLASS");
		if (daySubclassString != null) {
			aProduct.setDaySubclass(daySubclassString);
		}
		else {
			aProduct.setDaySubclass("NONE");
		}

		return;

	}

}
