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
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class TroubleTicketKeyBean extends DataAccessBean {
    //~ Static variables/initializers ------------------------------------------

    public static final String TABLE = "TT_KEY";
    public static final String ID = "TT_KEY_ID";
    public static final String COMPID = "COMP_ID";
    public static final String ERRCODE = "EAI_ERR_CD";
    public static final String EVENT = "EAI_EVENT_TYPE";

    //~ Instance variables -----------------------------------------------------

    private int iID = 0;
    private String sCompID = "";
    private String sErrCode = "";
    private String sEvent = "";

    //~ Constructors -----------------------------------------------------------


    /**
     * creates a default instance of the TroubleTickeyKeyBean class
     */
    public TroubleTicketKeyBean() {
    }


    /**
     * creates an instance of the TroubleTicketKeyBean class from the database
     *
     * @param id id - tt_key_id value from database
     * @param comp comp - comp_id value from database
     * @param err err - eai_err_cd value from database
     * @param evnt evnt - eai_event_type value from database
     */
    public TroubleTicketKeyBean(int id, String comp, String err, String evnt) {
        iID = id;

        if ((null == comp) || comp.trim().equalsIgnoreCase("null")) {
            sCompID = "";
        } else {
            sCompID = comp;
        }

        if ((null == err) || err.trim().equalsIgnoreCase("null")) {
            sErrCode = "";
        } else {
            sErrCode = err;
        }

        if ((null == evnt) || evnt.trim().equalsIgnoreCase("null")) {
            sEvent = "";
        } else {
            sEvent = evnt;
        }
    }


    /**
     * creates an instance of the TroubleTicketKeyBean class with values passed
     *
     * @param comp comp - comp_id
     * @param err err - eai_err_cd
     * @param evnt evnt - eai_event_type
     */
    public TroubleTicketKeyBean(String comp, String err, String evnt) {
        sCompID = comp;
        sErrCode = err;
        sEvent = evnt;
    }

    //~ Methods ----------------------------------------------------------------


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
    public int getID() {
        return iID;
    }


    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     */
    public void setCompId(String comp) {
        sCompID = comp;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCompId() {
        return sCompID;
    }


    /**
     * DOCUMENT ME!
     *
     * @param err DOCUMENT ME!
     */
    public void setErrCode(String err) {
        sErrCode = err;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getErrCode() {
        return sErrCode;
    }


    /**
     * DOCUMENT ME!
     *
     * @param evnt DOCUMENT ME!
     */
    public void setEvent(String evnt) {
        sEvent = evnt;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEvent() {
        return sEvent;
    }


    /**
     * @see DataAccessBean#getById(String, String, int)
     */
    public Object getById(String dsName, String daoQuery, int id)
        throws AdminException {
        TroubleTicketKeyBean ttkb = null;
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
                ttkb = new TroubleTicketKeyBean(rs.getInt(ID),
                        rs.getString(COMPID), rs.getString(ERRCODE),
                        rs.getString(EVENT));
            }
        } catch (SQLException sqle) {
            String msg = "Encountered SQL Exception during " + TABLE
                + " Lookup";
            String type = EXCEPTION;
            this.log(msg, EventType.EXCEPTION,
                ErrorCode.DATABASE_SQL_EXCEPTION, sqle, this, type);
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

        return ttkb;
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
                vAll.addElement(new TroubleTicketKeyBean(rs.getInt(ID),
                        rs.getString(COMPID), rs.getString(ERRCODE),
                        rs.getString(EVENT)));
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
            ps.setString(1, this.sCompID);
            ps.setString(2, this.sErrCode);
            ps.setString(3, this.sEvent);

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
            ps.setString(1, this.sCompID);
            ps.setString(2, this.sErrCode);
            ps.setString(3, this.sEvent);
            ps.setInt(4, this.iID);

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
