package com.disney.admin.notification;

import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;
import com.disney.admin.AdminException;
import pvt.disney.dti.gateway.connection.ConnectionException;

import pvt.disney.dti.gateway.connection.ConnectionManager;
//import com.disney.jdbc.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Vector;


/**
 * Analogous to the TT_GROUP table used by the TroubleTicketAgent
 */
public class TroubleTicketGroupBean extends DataAccessBean {
    //~ Static variables/initializers ------------------------------------------

    public static final String TABLE = "TT_GROUP";
    public static final String ID = "TT_GROUP_ID";
    public static final String GROUPNAME = "TT_GROUP_NAME";
    public static final String DESCRIPTION = "DESCRIPTION";

    //~ Instance variables -----------------------------------------------------

    private int iID = 0;
    private String sGroupName = "";
    private String sDescription = "";

    //~ Constructors -----------------------------------------------------------


    /**
     * Create a default instance of the TroubleTicketGroupBean
     */
    public TroubleTicketGroupBean() {
    }


    /**
     * Create an instance of the TroubleTicketGroupBean with data from the
     * database
     *
     * @param id id - tt_group_id value from database
     * @param gName gName - tt_group_name value from database
     * @param desc desc - description value from database
     */
    public TroubleTicketGroupBean(int id, String gName, String desc) {
        iID = id;
        sGroupName = gName;

        if ((null == desc) || desc.trim().equalsIgnoreCase("null")) {
            sDescription = "";
        } else {
            sDescription = desc;
        }
    }


    /**
     * Create an instance of the TroubleTicketGroupBean
     *
     * @param gName gName - tt_group_name
     * @param desc desc - description
     */
    public TroubleTicketGroupBean(String gName, String desc) {
        sGroupName = gName;
        sDescription = desc;
    }

    //~ Methods ----------------------------------------------------------------


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getID() {
        return iID;
    }


    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setID(int id) {
        iID = id;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getGroupName() {
        return sGroupName;
    }


    /**
     * DOCUMENT ME!
     *
     * @param gName DOCUMENT ME!
     */
    public void setGroupName(String gName) {
        sGroupName = gName;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return sDescription;
    }


    /**
     * DOCUMENT ME!
     *
     * @param desc DOCUMENT ME!
     */
    public void setDescription(String desc) {
        sDescription = desc;
    }


    /**
     * @see DataAccessBean#getById(String, String, int)
     */
    public Object getById(String dsName, String daoQuery, int id)
        throws AdminException {
        TroubleTicketGroupBean ttgb = null;
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

            // get the first element from the resultset and create an instance of this object
            if (rs.next()) {
                rs.first();
                ttgb = new TroubleTicketGroupBean(rs.getInt(ID),
                        rs.getString(GROUPNAME), rs.getString(DESCRIPTION));
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

        return (Object)ttgb;
    }


    /**
     * @see DataAccessBean#getAll(String, String)
     */
    public Vector getAll(String dsName, String daoQuery)
        throws AdminException {
        Vector vAll = new Vector();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // make sure i've got an open connection
            con = ConnectionManager.getInstance().getConnection(dsName);

            // prepare statment and populate query paramerter
            ps = con.prepareStatement(daoQuery);

            // execute statement and populate result set
            rs = ps.executeQuery();

            // parse resultset and add objects to vector for return
            while (rs.next()) {
                vAll.addElement(new TroubleTicketGroupBean(rs.getInt(ID),
                        rs.getString(GROUPNAME), rs.getString(DESCRIPTION)));
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

        return vAll;
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
            ps.setString(1, this.sGroupName);
            ps.setString(2, this.sDescription);

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
            ps.setString(1, this.sGroupName);
            ps.setString(2, this.sDescription);
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
