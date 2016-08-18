package com.disney.admin.notification;

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
 * Analogous to the TT_TYPE table used by the TroubleTicketAgent
 */
public class TroubleTicketTypeBean extends DataAccessBean {
    //~ Static variables/initializers ------------------------------------------

    public static final String TABLE = "TT_TYPE";
    public static final String ID = "TT_TYPE_ID";
    public static final String NAME = "TT_TYPE_NAME";
    public static final String VALUE = "TT_TYPE_VALUE";

    //~ Instance variables -----------------------------------------------------

    private int iID = 0;
    private String sName = "";
    private String sValue = "";

    //~ Constructors -----------------------------------------------------------


    /**
     * Creates a default instance of the TroubleTicketTypeBean
     */
    public TroubleTicketTypeBean() {
    }


    /**
     * Creates an instance of the TroubleTicketTypeBean from the database
     *
     * @param id id - tt_type_id from database
     * @param name name - tt_type_name from the database
     * @param value value - tt_type_value from the database
     */
    private TroubleTicketTypeBean(int id, String name, String value) {
        super();
        iID = id;
        sName = name;

        if ((null == value) || value.trim().equalsIgnoreCase("null")) {
            sValue = "";
        } else {
            sValue = value;
        }
    }


    /**
     * Creates a new instance of the TroubleTicketTypeBean from input
     *
     * @param name name - tt_type_name
     * @param value value - tt_type_value
     */
    public TroubleTicketTypeBean(String name, String value) {
        super();
        sName = name;
        sValue = value;
    }

    //~ Methods ----------------------------------------------------------------


    /**
     * DOCUMENT ME!
     *
     * @return int iID - primary key from database for tt_type
     */
    public int getID() {
        return iID;
    }


    /**
     * DOCUMENT ME!
     *
     * @return String sName- tt_type_name for this object, set from input or
     *         from database
     */
    public String getName() {
        return sName;
    }


    /**
     * DOCUMENT ME!
     *
     * @param arg arg - String value for tt_type_name of this object  use for
     *        update or insert only
     */
    public void setName(String arg) {
        sName = arg;
    }


    /**
     * DOCUMENT ME!
     *
     * @return String sValue - tt_type_value for this object, set from input or
     *         from database
     */
    public String getValue() {
        return sValue;
    }


    /**
     * DOCUMENT ME!
     *
     * @param arg arg - String value for the tt_type_value of this object use
     *        for update or insert only
     */
    public void setValue(String arg) {
        sValue = arg;
    }


    /**
     * get a vector of every instance of this type from the database
     *
     * @see DataAccessBean#getAll(String, String)
     */
    public Vector getAll(String dsName, String daoQuery)
        throws AdminException {
        Vector vAllTypes = new Vector();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //make sure i've got an open connection
            con = ConnectionManager.getInstance().getConnection(dsName);

            //prepare statement, it's a select all so no query parameters need to be set
            ps = con.prepareStatement(daoQuery);

            //execute query
            rs = ps.executeQuery();

            //create instances of this object and add them to the vector until i've gotten everything from the result set
            while (rs.next()) {
                vAllTypes.addElement(new TroubleTicketTypeBean(rs.getInt(ID),
                        rs.getString(NAME), rs.getString(VALUE)));
            }
        } catch (SQLException sqle) {
            String msg = "Encountered SQL Exception during " + TABLE
                + " Lookup";
            String type = EXCEPTION;
            this.log(msg, EventType.EXCEPTION,
                ErrorCode.DATABASE_SQL_EXCEPTION, sqle, this, type);
            throw (new AdminException(msg, sqle));
        } catch (ConnectionException ce) {
            String msg = "Encountered Connection Exception during " + TABLE
                + " Lookup";
            String type = EXCEPTION;
            this.log(msg, EventType.EXCEPTION,
                ErrorCode.DATABASE_CONNECTION_EXCEPTION, ce, this, type);
            throw (new AdminException(msg, ce));
        } finally {
            //defined in abstract class DataAccessBean
            cleanup(con, rs, ps);
        }

        return vAllTypes;
    }


    /**
     * Get a single instance of this type by id must be cast to
     * TroubleTicketTypeBean to be usable
     *
     * @see DataAccessBean#getById(String, String, int)
     */
    public Object getById(String dsName, String daoQuery, int id)
        throws AdminException {
        TroubleTicketTypeBean tttb = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // make sure i've got an open connection
            con = ConnectionManager.getInstance().getConnection(dsName);

            // prepare statment and populate query paramerter
            ps = con.prepareStatement(daoQuery);
            ps.setInt(1, id);

            // execute statement and populate result set
            rs = ps.executeQuery();

            if (rs.next()) {
                rs.first();
                tttb = new TroubleTicketTypeBean(rs.getInt(ID),
                        rs.getString(NAME), rs.getString(VALUE));
            }
        } catch (SQLException sqle) {
            String msg = "Encountered SQL Exception during " + TABLE
                + " Lookup";
            String type = EXCEPTION;
            this.log(msg, EventType.EXCEPTION,
                ErrorCode.DATABASE_SQL_EXCEPTION, sqle, this, type);
            throw (new AdminException(msg, sqle));
        } catch (ConnectionException ce) {
            String msg = "Encountered Connection Exception during " + TABLE
                + " Lookup";
            String type = EXCEPTION;
            this.log(msg, EventType.EXCEPTION,
                ErrorCode.DATABASE_CONNECTION_EXCEPTION, ce, this, type);
            throw (new AdminException(msg, ce));
        } finally {
            cleanup(con, rs, ps);
        }

        return (Object)tttb;
    }


    /**
     * @see DataAccessBean#insertNew(String, String)
     */
    public boolean insertNew(String dsName, String daoQuery)
        throws AdminException {
        boolean inserted = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // make sure i've got an open connection
            con = ConnectionManager.getInstance().getConnection(dsName);

            // prepare statment and populate query paramerter
            ps = con.prepareStatement(daoQuery);
            ps.setString(1, this.sName);
            ps.setString(2, this.sValue);

            // if anything was updated set the flag to true
            if (ps.executeUpdate() >= 1) {
                inserted = true;
            }
        } catch (SQLException sqle) {
            String msg = "Encountered SQL Exception during " + TABLE
                + " Insert";
            String type = EXCEPTION;
            this.log(msg, EventType.EXCEPTION,
                ErrorCode.DATABASE_SQL_EXCEPTION, sqle, this, type);
            throw (new AdminException(msg, sqle));
        } catch (ConnectionException ce) {
            String msg = "Encountered Connection Exception during " + TABLE
                + " Insert";
            String type = EXCEPTION;
            this.log(msg, EventType.EXCEPTION,
                ErrorCode.DATABASE_CONNECTION_EXCEPTION, ce, this, type);
            throw (new AdminException(msg, ce));
        } finally {
            cleanup(con, rs, ps);
        }

        return inserted;
    }


    /**
     * @see DataAccessBean#updateMe (String, String)}
     */
    public boolean updateMe(String dsName, String daoQuery)
        throws AdminException {
        boolean updated = false;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // make sure i've got an open connection
            con = ConnectionManager.getInstance().getConnection(dsName);

            // prepare statment and populate query paramerter
            ps = con.prepareStatement(daoQuery);
            ps.setString(1, this.sName);
            ps.setString(2, this.sValue);
            ps.setInt(3, this.iID);

            // if anything was updated set the flag to true
            if (ps.executeUpdate() >= 1) {
                updated = true;
            }
        } catch (SQLException sqle) {
            String msg = "Encountered SQL Exception during " + TABLE
                + " Update";
            String type = EXCEPTION;
            this.log(msg, EventType.EXCEPTION,
                ErrorCode.DATABASE_SQL_EXCEPTION, sqle, this, type);
            throw (new AdminException(msg, sqle));
        } catch (ConnectionException ce) {
            String msg = "Encountered Connection Exception during " + TABLE
                + " Update";
            String type = EXCEPTION;
            this.log(msg, EventType.EXCEPTION,
                ErrorCode.DATABASE_CONNECTION_EXCEPTION, ce, this, type);
            throw (new AdminException(msg, ce));
        } finally {
            cleanup(con, rs, ps);
        }

        return updated;
    }
}
