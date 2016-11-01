package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;

import pvt.disney.dti.gateway.data.common.EntityTO;

import com.disney.exception.WrappedException;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;

/**
 * This class is used by the DAO to process the result sets from Entity queries.
 * 
 * @author lewit019
 * 
 */
public class EntityResult implements ResultSetProcessor {

	/** The entity information object. */
	private EntityTO entTO = null;

	/** The number of records processed by this result set processor. */
	private int recordsProcessed = 0;

	/**
	 * Constructor for EntityResult
	 */
	public EntityResult() {
		super();
	}

	/**
	 * Returns the processed object.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	public Object getProcessedObject() throws WrappedException {

		return entTO;
	}

	/**
	 * Processes the next (or only) value in the results set.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {

		entTO = new EntityTO();

		// Non-nullable fields
		entTO.setEntityId(rs.getInt("ENTITYID"));

		String activeIndString = rs.getString("ACTIVE_IND");
		if (activeIndString.compareToIgnoreCase("T") == 0) {
		  entTO.setActive(true);
		} else {
		  entTO.setActive(false);
		}

		entTO.setStartValidDate(rs.getDate("START_VALID_DATE"));
		entTO.setEndValidDate(rs.getDate("END_VALID_DATE"));
		entTO.setEntityCode(rs.getString("ENTITY_CODE"));

		String mismatchIndString = rs.getString("MISMATCH_IND");
		if (mismatchIndString.compareToIgnoreCase("T") == 0) entTO
				.setPriceMismatchAllowed(true);
		else entTO.setPriceMismatchAllowed(false);

		String iataReqIndString = rs.getString("IATA_REQ_IND");
		if (iataReqIndString.compareToIgnoreCase("T") == 0) entTO
				.setIataRequired(true);
		else entTO.setIataRequired(false);

		String pymtReqIndString = rs.getString("PYMT_REQ_IND");
		if (pymtReqIndString.compareToIgnoreCase("T") == 0) entTO
				.setPymtRequired(true);
		else entTO.setPymtRequired(false);

		String shellCheckIndString = rs.getString("SHELL_CHECK_IND");
		if (shellCheckIndString.compareToIgnoreCase("T") == 0) entTO
				.setTicketShellRequired(true);
		else entTO.setTicketShellRequired(false);

		// Nullable fields
		entTO.setStartValidTime(rs.getString("START_VALID_TIME"));
		entTO.setEndValidTime(rs.getString("END_VALID_TIME"));
		entTO.setCustomerId(rs.getString("CUSTOMERID"));
		entTO.setECommerceValue(rs.getString("ECOMMERCE"));
		entTO.setDefPymtData(rs.getString("DEF_PYMTDATA"));

		int macEntityId = rs.getInt("MAC_ENTITYID");
		if (macEntityId != 0) {
			entTO.setMacEntityId(macEntityId);
		}

		String setValidityDatesIndString = rs.getString("VALIDITY_DATES_IND");
		if (setValidityDatesIndString != null) {
			if (setValidityDatesIndString.compareToIgnoreCase("T") == 0) entTO
					.setValidityDateProductAllowed(true);
			else entTO.setValidityDateProductAllowed(false);
		}

		entTO.setDefIata(rs.getString("DEF_IATA"));

		int defPymtId = rs.getInt("DEF_PYMTID");
		if (defPymtId != 0) {
			entTO.setDefPymtId(defPymtId);
		}

		Long defaultSalesRepId = rs.getLong("DEF_SALESREPID");
		if (defaultSalesRepId != null) {
			entTO.setDefSalesRepId(defaultSalesRepId);
		}

		this.recordsProcessed++;

		return;

	}

}
