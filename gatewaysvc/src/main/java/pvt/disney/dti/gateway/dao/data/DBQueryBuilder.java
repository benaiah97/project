package pvt.disney.dti.gateway.dao.data;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.rules.BusinessRules;

import com.disney.exception.WrappedException;
import com.disney.jdbc.dao.QueryBuilder;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.Converter;

/**
 * This class assembles portions of SQL where multiple values are being concatenated (such as in a statement "select * from table X where value in (1,2,3,4,5)".
 * 
 * @author lewit019
 */
public class DBQueryBuilder implements QueryBuilder {

	/** The object instance used for logging. */
	private static final DBQueryBuilder THISINSTANCE = new DBQueryBuilder();

	/** The event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger(DBQueryBuilder.class.getCanonicalName());

	/**
	 * Gets the complex query.
	 * 
	 * @param sql
	 *            the SQL string
	 * @param inStringArray
	 *            the string of variable parameters.
	 * @throws WrappedException
	 *             for any issue.
	 * @see com.disney.jdbc.dao.QueryBuilder#getQuery(java.lang.String, java.lang.Object[])
	 */
	public String getQuery(String sql, Object[] inStringArray) throws WrappedException {
		try {
			// Validate parameters
			if ((sql == null) || sql.equals("") || (inStringArray == null) || (inStringArray.length != 1)) {

				DTIException dtiEx = new DTIException(BusinessRules.class,
						DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
						"DBQueryBuilder.getQuery found bad sql or inStringArray.  SQL will fail.");
				logger.sendEvent(
						"Exception executing DBQueryBuilder.getQuery: " + dtiEx
								.toString(), EventType.WARN, THISINSTANCE);
				throw new WrappedException(
						"DBQueryBuilder.getQuery found bad sql or inStringArray.  SQL will fail.",
						dtiEx);

			}

			// Retrieve the string from the array list, there will only be one
			String inString = (String) inStringArray[0];

			// Replace wild character with the in string
			sql = Converter.findAndReplace(sql, "%", inString);

			// Validate response
			if ((sql == null) || sql.equals("")) {
				throw (new Exception(
						"Converter.findAndReplace() " + "encountered an error and returned empty string."));
			}
		}
		catch (Throwable t) {
			logger.sendEvent(
					"Exception executing DBQueryBuilder.getQuery: " + t
							.toString(), EventType.WARN, THISINSTANCE);

			// Log exception if not already and then throw
			if (t instanceof WrappedException) {
				throw (WrappedException) t;
			}
			else {
				throw new WrappedException(
						"DBQueryBuilder.getQuery encounter SQL error.", t);
			}
		}

		return sql;
	}

}
