package pvt.disney.dti.gateway.dao.result;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.TPLookupTO;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from the TP Lookup Result queries.
 * 
 * @author lewit019
 * 
 */
public class TPLookupResult implements ResultSetProcessor {

	private final static String LANGUAGE_STRING = "Language";
	private final static String CLIENT_TYPE_STRING = "ClientType";
	private final static String PICKUP_AREA_STRING = "PickupArea";
	private final static String PICKUP_TYPE_STRING = "PickupType";
	private final static String SALES_TYPE_STRING = "SalesType";
	private final static String SHIP_METHOD_STRING = "ShipMethod";
	private final static String SHIP_DETAIL_STRING = "ShipDetail";
	private final static String MAX_LIMIT_STRING = "MaxLimit";
	private final static String CC_TYPE_STRING = "CCType";
	private final static String INSTALLMENT_STRING = "Installmnt";

	/** The list if TP lookup objects. */
	private ArrayList<TPLookupTO> tpLookupList = new ArrayList<TPLookupTO>();

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for TPLookupResult
	 */
	public TPLookupResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {

		return tpLookupList;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see com.disney.jdbc.dao.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		TPLookupTO tpLookupTO = new TPLookupTO();

		String cmdCodeString = rs.getString("CMD_CODE");
		if (cmdCodeString != null) tpLookupTO.setCmdCode(cmdCodeString);

		String cmdIdString = rs.getString("CMDID");
		if (cmdIdString != null) tpLookupTO
				.setCmdId(new BigInteger(cmdIdString));

		String lookupTypeString = rs.getString("LOOKUP_TYPE");
		if (lookupTypeString != null) {

			if (lookupTypeString.compareTo(LANGUAGE_STRING) == 0) {
				tpLookupTO.setLookupType(TPLookupTO.TPLookupType.LANGUAGE);
			}
			else if (lookupTypeString.compareTo(CLIENT_TYPE_STRING) == 0) {
				tpLookupTO.setLookupType(TPLookupTO.TPLookupType.CLIENT_TYPE);
			}
			else if (lookupTypeString.compareTo(PICKUP_AREA_STRING) == 0) {
				tpLookupTO.setLookupType(TPLookupTO.TPLookupType.PICKUP_AREA);
			}
			else if (lookupTypeString.compareTo(PICKUP_TYPE_STRING) == 0) {
				tpLookupTO.setLookupType(TPLookupTO.TPLookupType.PICKUP_TYPE);
			}
			else if (lookupTypeString.compareTo(SALES_TYPE_STRING) == 0) {
				tpLookupTO.setLookupType(TPLookupTO.TPLookupType.SALES_TYPE);
			}
			else if (lookupTypeString.compareTo(SHIP_METHOD_STRING) == 0) {
				tpLookupTO.setLookupType(TPLookupTO.TPLookupType.SHIP_METHOD);
			}
			else if (lookupTypeString.compareTo(SHIP_DETAIL_STRING) == 0) {
				tpLookupTO.setLookupType(TPLookupTO.TPLookupType.SHIP_DETAIL);
			}
			else if (lookupTypeString.compareTo(MAX_LIMIT_STRING) == 0) {
				tpLookupTO.setLookupType(TPLookupTO.TPLookupType.MAX_LIMIT);
			}
			else if (lookupTypeString.compareTo(CC_TYPE_STRING) == 0) {
				tpLookupTO.setLookupType(TPLookupTO.TPLookupType.CC_TYPE);
			}
			else if (lookupTypeString.compareTo(INSTALLMENT_STRING) == 0) {
				tpLookupTO.setLookupType(TPLookupTO.TPLookupType.INSTALLMENT);
			}
			else {
				tpLookupTO.setLookupType(TPLookupTO.TPLookupType.UNDEFINED);
			}
		}
		else {
			tpLookupTO.setLookupType(TPLookupTO.TPLookupType.UNDEFINED);
		}

		String lookupValueString = rs.getString("LOOKUP_VALUE");
		if (lookupValueString != null) {
			tpLookupTO.setLookupValue(lookupValueString);
		}

		String lookupDescString = rs.getString("LOOKUP_DESC");
		if (lookupDescString != null) {
			tpLookupTO.setLookupDesc(lookupDescString);
		}

		// Capture Link ID (as of 2.16.1)
<<<<<<< HEAD
    String linkIdString = rs.getString("LINKID");
    if (linkIdString != null)
      tpLookupTO.setLinkId(new BigInteger(cmdIdString));
		
=======
		String linkIdString = rs.getString("LINKID");
		if (linkIdString != null) tpLookupTO.setLinkId(new BigInteger(
				cmdIdString));

>>>>>>> develop
		tpLookupList.add(tpLookupTO);

		this.recordsProcessed++;

		return;

	}

}
