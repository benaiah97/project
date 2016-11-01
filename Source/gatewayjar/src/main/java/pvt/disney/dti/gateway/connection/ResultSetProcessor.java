package pvt.disney.dti.gateway.connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;


/**
 * Interface for processing ResultSet data from DAOHelper
 * 
 */
public interface ResultSetProcessor
{
    //~ Methods ------------------------------------------------------------------------------------

    /**
     * Process each result set record, called by DAOHelper. Typically used to
     * map RestulSet values to value object attributes.
     * 
     * @param rs ResultSet returned from DAOHelper query
     * 
     * @throws SQLException if a SQL exception is thrown by the query
     * @throws WrappedException If a non SQL excepton is thrown by the query
     */
    void processNextResultSet(ResultSet rs)
        throws SQLException, WrappedException;


    /**
     * The value object returned by the DAOHelper after all ResultSet records have been processed
     * 
     * @return The value object being populated by the result set
     * 
     * @throws WrappedException If ResultSet procesing results in an error
     */
    Object getProcessedObject() throws WrappedException;
}