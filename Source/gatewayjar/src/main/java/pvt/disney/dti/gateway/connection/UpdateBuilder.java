package pvt.disney.dti.gateway.connection;

import com.disney.exception.WrappedException;

/**
 * This interface allows an update SQL statement to be dynamically built based on run-time
 * parameters.
 */
public interface UpdateBuilder
{
    //~ Instance variables -------------------------------------------------------------------------

    String UPDATE = "UPDATE";

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * The method which dynamically builds the SQL statement.
     * 
     * @param propertiesFileUpdate the update from the properties file, or null if none exists
     * @param updateParameters the parameters to be used in building the statement, or null if none
     *        exist
     * 
     * @return the built SQL statement
     * 
     * @throws EAIException DOCUMENT ME!
     */
    String getUpdate(String propertiesFileUpdate, Object[] updateParameters)
        throws WrappedException;
}