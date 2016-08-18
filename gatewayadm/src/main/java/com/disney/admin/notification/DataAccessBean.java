package com.disney.admin.notification;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.admin.AdminException;
import com.disney.jdbc.ConnectionException;
import com.disney.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;


/**
 * Abstract Class DataAccessBean give me a core set of methods to be used by any data access bean
 * that works against a database object
 * 
 * @author Ric Alvarez
 */
public abstract class DataAccessBean
{
    //~ Static variables/initializers --------------------------------------------------------------

    protected static final String EXCEPTION = "EXCEPTION";
    protected static final EventLogger EVL = EventLogger.getLogger ("ADMIN");

    //~ Methods ------------------------------------------------------------------------------------

    /**
     * log an error encountered by an object that extends me
     * 
     * @param message - message to be included in log entry
     * @param et - event type to be logged
     * @param ec - error code encountered
     * @param th - throwable object encountered
     * @param o - object that encountered problem
     * @param type - type of error to log, assumes UNEXPECTED_EXCEPTION if a supported type is not
     *        passed
     */
    protected final void log(String message, EventType et, ErrorCode ec, Throwable th, Object o, 
                             String type)
    {
        if (type.trim ().equalsIgnoreCase (EXCEPTION))
        {
            EVL.sendException (message, et, ec, th, o);
        }
        else
        {
            EVL.sendException (message, et, ErrorCode.APPLICATION_EXCEPTION, th, o);
        }
    }


    /**
     * log an error encountered by an object that extends me, assumes that anything logged through
     * this signature is an UNEXPECTED_EXCEPTION
     * 
     * @param message - message to be included in log entry
     * @param et - event type to be logged
     * @param ec - error code encountered
     * @param th - throwable object encountered
     * @param o - object that encountered problem
     */
    protected final void log(String message, EventType et, ErrorCode ec, Throwable th, Object o) throws AdminException
    {
        EVL.sendException (message, et, ec, th, o);
        //i want to create and throw an EAIException here just so that there is
        //something to bubble to the user.
        AdminException ae = new AdminException(message, th);
        throw ae;
    }


    /**
     * Cleanup my database objects, should be called in the finally of any method for any object
     * that extends me and makes a database call
     * 
     * @param con - used connection to be closed and marked for GC
     * @param rs - used resultset to be closed and marked for GC
     * @param ps - used preparedstatement to be closed and marked for GC
     */
    protected final void cleanup(Connection con, ResultSet rs, PreparedStatement ps) throws AdminException
    {
        String message = "Encountered error cleanup database objects";

        //try to close the resultset
        if (rs != null)
        {
            try
            {
                rs.close ();
            }
            catch (Throwable t)
            {
                this.log (message, EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, t, rs);
            }
            finally
            {
                //make it null regardless
                rs = null;
            }
        }

        //try to close the prepared statement
        if (ps != null)
        {
            try
            {
                ps.close ();
            }
            catch (Throwable t)
            {
                this.log (message, EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, t, ps);
            }
            finally
            { //make it null regardless
                ps = null;
            }
        }

        //try to close the connection
        if (con != null)
        {
            try
            {
                con.close ();
            }
            catch (Throwable t)
            {
                this.log (message, EventType.EXCEPTION, ErrorCode.APPLICATION_EXCEPTION, t, con);
            }
            finally
            { //make it null regardless
                con = null;
            }
        }
    }


    /**
     * Used to get all instances of this object from the database. Construct object as appropriate
     * and add as an element in the return vector Any complete query can be passed through this
     * method, as long as the return is of a single declared type, denormalizations and views
     * requre their own access beans.
     * 
     * @param dsName dsName - datasource name to use for this lookup
     * @param daoQuery daoQuery - Query string to use for this lookup
     * 
     * @return Vector - a vector of all objects returned by this query
     * 
     * @throws SQLException DOCUMENT ME!
     * @throws ConnectionException DOCUMENT ME!
     */
    public abstract Vector getAll(String dsName, String daoQuery)
        throws SQLException, ConnectionException, AdminException;


    /**
     * Used to get a single instance of this object from the database Construct object as
     * appropriate cast to and return as object, cast back to appropriate type for use
     * 
     * @param dsName dsName - datasource name to use for this lookup
     * @param daoQuery daoQuery - query string to use for this lookup
     * @param id id - primary key for object to be retreived from database
     * 
     * @return Object - the object retrieved by the lookup
     * 
     * @throws SQLException DOCUMENT ME!
     * @throws ConnectionException DOCUMENT ME!
     */
    public abstract Object getById(String dsName, String daoQuery, int id)
        throws SQLException, ConnectionException, AdminException;


    /**
     * Used to insert a new instance of this object into the database
     * 
     * @param dsName dsName - datasource name to use for this insert
     * @param daoQuery daoQuery - query string to use for his insert
     * 
     * @return boolean - has the insert successfully occurred or not?
     * 
     * @throws SQLException DOCUMENT ME!
     * @throws ConnectionException DOCUMENT ME!
     */
    public abstract boolean insertNew(String dsName, String daoQuery)
        throws SQLException, ConnectionException, AdminException;


    /**
     * Used to delete this instance of this object from the database
     * 
     * @param dsName dsName - datasource name to use for this update
     * @param daoQuery daoQuery - query string to use for his update
     * @param id id - primary key value identifying this instance
     * @param table table - table name containing objects of this type
     * 
     * @return boolean - has the update successfully occurred or not?
     * 
     * @throws SQLException DOCUMENT ME!
     * @throws ConnectionException DOCUMENT ME!
     */
    public boolean deleteMe(String dsName, String daoQuery, int id, String table)
        throws SQLException, ConnectionException, AdminException
    {
        boolean deleted = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            // make sure i've got an open connection
            con = ConnectionManager.getInstance ().getConnection (dsName);


            // prepare statment and populate query paramerter
            ps = con.prepareStatement (daoQuery);
            ps.setInt (1, id);

            // if anything was updated set the flag to true
            if (ps.executeUpdate () >= 1)
            {
                deleted = true;
            }
        }
        catch (SQLException sqle)
        {
            String msg = "Encountered SQL Exception during " + table + " Delete";
            String type = EXCEPTION;
            this.log (msg, EventType.EXCEPTION, ErrorCode.DATABASE_SQL_EXCEPTION, sqle, this, type);
            throw sqle;
        }
        catch (ConnectionException ce)
        {
            String msg = "Encountered Connection Exception during " + table + " Delete";
            String type = EXCEPTION;
            this.log (msg, EventType.EXCEPTION, ErrorCode.DATABASE_SQL_EXCEPTION, ce, this, type);
            throw ce;
        }
        finally
        {
            cleanup (con, rs, ps);
        }

        return deleted;
    }


    /**
     * Used to update an existing instance of this object in the database
     * 
     * @param dsName dsName - datasource name to use for this update
     * @param daoQuery daoQuery - query string to use for this update
     * 
     * @return boolean - has the update successfully occurred or not?
     * 
     * @throws SQLException DOCUMENT ME!
     * @throws ConnectionException DOCUMENT ME!
     */
    public abstract boolean updateMe(String dsName, String daoQuery)
        throws SQLException, ConnectionException, AdminException;
}