package pvt.disney.dti.gateway.connection;

import com.disney.exception.WrappedException;


/**
 * This interface allows an insert SQL statement to be dynamically built based on run-time
 * parameters.
 */
public interface InsertBuilder
{
    //~ Instance variables -------------------------------------------------------------------------

    String INSERT = "INSERT";

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * The method which dynamically builds the SQL statement.
     * 
     * @param propertiesFileInsert the insert from the properties file, or null if none exists
     * @param insertParameters the parameters to be used in building the statement, or null if none
     *        exist
     * 
     * @return the built SQL statement
     * 
     * @throws EAIException DOCUMENT ME!
     */
    String getInsert(String propertiesFileInsert, Object[] insertParameters)
        throws WrappedException;
}