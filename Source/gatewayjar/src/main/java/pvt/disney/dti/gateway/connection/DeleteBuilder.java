package pvt.disney.dti.gateway.connection;

import com.disney.exception.WrappedException;


/**
 * This interface allows a delete SQL statement to be dynamically built based on run-time
 * parameters.
 */
public interface DeleteBuilder
{
    //~ Instance variables -------------------------------------------------------------------------

    /** The name of the delete parameter from the dataaccess.properties */
    String DELETE = "DELETE";

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * The method which dynamically builds the SQL statement.
     * 
     * @param propertiesFileDelete the delete from the properties file, or null if none exists
     * @param deleteParameters the parameters to be used in building the statement, or null if none
     *        exist
     * 
     * @return the built SQL statement
     * 
     * @throws EAIException DOCUMENT ME!
     */
    String getDelete(String propertiesFileDelete, Object[] deleteParameters)
        throws WrappedException;
}