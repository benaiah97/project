package pvt.disney.dti.gateway.connection;

import com.disney.exception.WrappedException;

/**
 * This interface allows a delete SQL statement to be dynamically built based on run-time
 * parameters.
 */
public interface QueryBuilder
{
    //~ Instance variables -------------------------------------------------------------------------

    String QUERY = "QUERY";

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * The method which dynamically builds the SQL statement.
     * 
     * @param propertiesFileQuery the query from the properties file, or null if none exists
     * @param queryValues the parameters to be used in building the statement, or null if none
     *        exist
     * 
     * @return the built SQL statement
     * 
     * @throws EAIException DOCUMENT ME!
     */
    String getQuery(String propertiesFileQuery, Object[] queryValues)
        throws WrappedException;
}