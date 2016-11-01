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
import java.sql.Timestamp;

import java.util.Vector;


/**
 * Analogous to the TT_HISTORY table used by the TroubleTicketAgent also
 * includes field references to TT_TYPE, TT_KEY, and TT_GROUP for  display
 * purposes
 */
public class TroubleTicketHistoryBean extends DataAccessBean {
    //~ Static variables/initializers ------------------------------------------

    public static final String TABLE = "TT_HISTORY";
    public static final String ID = "TT_HISTORY_ID";
    public static final String KEYID = "TT_KEY_ID";
    public static final String TYPEID = "TT_TYPE_ID";
    public static final String EAINODE = "EAI_NODE";
    public static final String SENDTIME = "TT_SEND_TIME";
    public static final String GROUPID = "TT_GROUP_ID";

    //OTHER FIELDS
    public static final String TYPENAME = "TT_TYPE_NAME";
    public static final String TYPEVALUE = "TT_TYPE_VALUE";
    public static final String KEYCOMPID = "COMP_ID";
    public static final String KEYERRCD = "EAI_ERR_CD";
    public static final String KEYEVENTCD = "EAI_EVENT_TYPE";
    public static final String GROUPNAME = "TT_GROUP_NAME";
    public static final String GROUPDESC = "DESCRIPTION";

    //~ Instance variables -----------------------------------------------------

    private int iID = 0;
    private int iKeyID = 0;
    private String sKeyCompId = "";
    private String sKeyErrCd = "";
    private String sKeyEvntType = "";
    private int iTypeID = 0;
    private String sTypeName = "";
    private String sTypeValue = "";
    private String sEaiNode = "";
    private int iGroupID = 0;
    private String sGroupName = "";
    private String sDescription = "";
    private Timestamp dSendTime = new Timestamp(System.currentTimeMillis());

    //~ Constructors -----------------------------------------------------------


    /**
     * Create a default instance of the TroubleTicketHistoryBean class
     */
    public TroubleTicketHistoryBean() {
    }


    /**
     * Create an instance of the TroubleTicketHistoryBean class from database
     * data
     *
     * @param id id - tt_history_id value from database
     * @param key key - tt_key_id value from database
     * @param type type - tt_type_id value from database
     * @param group node - eai_node value fromdatabase
     * @param node sent - tt_send_time value from database
     * @param sent DOCUMENT ME!
     */
    public TroubleTicketHistoryBean(int id, int key, int type, int group,
        String node, Timestamp sent) {
        iID = id;
        iKeyID = key;
        iTypeID = type;
        iGroupID = group;
        sEaiNode = node;
        dSendTime = sent;
    }


    /**
     * Create an instance of the TroubleTicketHistoryBean class from database
     * data include data from related tables
     *
     * @param id id - tt_history_id value from database
     * @param kId kId - tt_key_id
     * @param tId tId - tt_type_id
     * @param gId gId - tt_group_id
     * @param kComp kComp - comp_id
     * @param kErr kErr - eai_err_cd
     * @param kEvnt kEvnt - eai_event_type
     * @param tName tName - tt_type_name
     * @param tVal tVal - tt_type_value
     * @param eaiNode eaiNode - eai_node
     * @param gName gName - tt_group_name
     * @param desc desc - Description
     * @param tsSend DOCUMENT ME!
     */
    public TroubleTicketHistoryBean(int id, int kId, int tId, int gId,
        String kComp, String kErr, String kEvnt, String tName, String tVal,
        String eaiNode, String gName, String desc, Timestamp tsSend) {
        iID = id;
        iKeyID = kId;
        iTypeID = tId;
        iGroupID = gId;

        //make sure that the strings are not null
        if ((kComp != null) && !kComp.trim().equalsIgnoreCase("null")) {
            sKeyCompId = kComp;
        }

        if ((kErr != null) && !kErr.trim().equalsIgnoreCase("null")) {
            sKeyErrCd = kErr;
        }

        if ((kEvnt != null) && !kEvnt.trim().equalsIgnoreCase("null")) {
            sKeyEvntType = kEvnt;
        }

        if ((tName != null) && !tName.trim().equalsIgnoreCase("null")) {
            sTypeName = tName;
        }

        if ((tVal != null) && !tVal.trim().equalsIgnoreCase("null")) {
            sTypeValue = tVal;
        }

        if ((eaiNode != null) && !eaiNode.trim().equalsIgnoreCase("null")) {
            sEaiNode = eaiNode;
        }

        if ((gName != null) && !gName.trim().equalsIgnoreCase("null")) {
            sGroupName = gName;
        }

        if ((desc != null) && !desc.trim().equalsIgnoreCase("null")) {
            sDescription = desc;
        }

        dSendTime = tsSend;
    }


    /**
     * Create an instance of the TroubleTicketHistoryBean class
     *
     * @param key key - tt_key_id
     * @param type type - tt_type_id
     * @param group node - eai_node
     * @param node sent - tt_send_time
     * @param sent DOCUMENT ME!
     */
    public TroubleTicketHistoryBean(int key, int type, int group, String node,
        Timestamp sent) {
        iKeyID = key;
        iTypeID = type;
        iGroupID = group;
        sEaiNode = node;
        dSendTime = sent;
    }


    /**
     * Create an instance of the TroubleTicketHistoryBean class from database
     * data
     *
     * @param id id - tt_history_id value from database
     * @param key key - tt_key_id value from database
     * @param type type - tt_type_id value from database
     * @param group node - eai_node value from database
     * @param node sent - tt_send_time value from database as long, will be
     *        translated to Date
     * @param sent DOCUMENT ME!
     */
    public TroubleTicketHistoryBean(int id, int key, int type, int group,
        String node, long sent) {
        iID = id;
        iKeyID = key;
        iTypeID = type;
        iGroupID = group;
        sEaiNode = node;
        dSendTime = new Timestamp(sent);
    }


    /**
     * Create an instance of the TroubleTicketHistoryBean class
     *
     * @param key key - tt_key_id
     * @param type type - tt_type_id
     * @param group node - eai_node
     * @param node sent - tt_send_time as long, will be translated to Date
     * @param sent DOCUMENT ME!
     */
    public TroubleTicketHistoryBean(int key, int type, int group, String node,
        long sent) {
        iKeyID = key;
        iTypeID = type;
        iGroupID = group;
        sEaiNode = node;
        dSendTime = new Timestamp(sent);
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
     * @param kId DOCUMENT ME!
     */
    public void setKeyID(int kId) {
        iKeyID = kId;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKeyID() {
        return iKeyID;
    }


    /**
     * DOCUMENT ME!
     *
     * @param kcId DOCUMENT ME!
     */
    public void setKeyCompId(String kcId) {
        sKeyCompId = kcId;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKeyCompId() {
        return sKeyCompId;
    }


    /**
     * DOCUMENT ME!
     *
     * @param kErrCd DOCUMENT ME!
     */
    public void setKeyErrCd(String kErrCd) {
        sKeyErrCd = kErrCd;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKeyErrCd() {
        return sKeyErrCd;
    }


    /**
     * DOCUMENT ME!
     *
     * @param kEvnt DOCUMENT ME!
     */
    public void setKeyEvntType(String kEvnt) {
        sKeyEvntType = kEvnt;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKeyEvntType() {
        return sKeyEvntType;
    }


    /**
     * DOCUMENT ME!
     *
     * @param tId DOCUMENT ME!
     */
    public void setTypeID(int tId) {
        iTypeID = tId;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTypeID() {
        return iTypeID;
    }


    /**
     * DOCUMENT ME!
     *
     * @param tName DOCUMENT ME!
     */
    public void setTypeName(String tName) {
        sTypeName = tName;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTypeName() {
        return sTypeName;
    }


    /**
     * DOCUMENT ME!
     *
     * @param tVal DOCUMENT ME!
     */
    public void setTypeValue(String tVal) {
        sTypeValue = tVal;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTypeValue() {
        return sTypeValue;
    }


    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    public void setEaiNode(String node) {
        sEaiNode = node;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEaiNode() {
        return sEaiNode;
    }


    /**
     * DOCUMENT ME!
     *
     * @param gId DOCUMENT ME!
     */
    public void setGroupId(int gId) {
        iGroupID = gId;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGroupId() {
        return iGroupID;
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
    public String getGroupName() {
        return sGroupName;
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
     * @param sent DOCUMENT ME!
     */
    public void setSendTime(Timestamp sent) {
        dSendTime = sent;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Timestamp getSendTime() {
        return dSendTime;
    }


    /**
     * @see DataAccessBean#getById(String, String, int)
     */
    public Object getById(String dsName, String daoQuery, int id)
        throws AdminException {
        TroubleTicketHistoryBean tthb = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // make sure i've got an open connection
            con = ConnectionManager.getInstance().getConnection(dsName);

            // prepare statment and populate query parameters
            ps = con.prepareStatement(daoQuery);
            ps.setInt(1, id);

            // execute statement and populate result set
            rs = ps.executeQuery();

            // get the first element from the resultset and create an instance of this object
            if (rs.next()) {
                rs.first();
                tthb = new TroubleTicketHistoryBean(rs.getInt(ID),
                        rs.getInt(KEYID), rs.getInt(TYPEID),
                        rs.getInt(GROUPID), rs.getString(EAINODE),
                        rs.getTimestamp(SENDTIME));
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

        return tthb;
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

            // prepare statment, no parameters to populate this should return all records
            ps = con.prepareStatement(daoQuery);

            // execute statement and populate result set
            rs = ps.executeQuery();

            // get the first element from the resultset and create an instance of this object
            while (rs.next()) {
                vAll.addElement(new TroubleTicketHistoryBean(rs.getInt(ID),
                        rs.getInt(KEYID), rs.getInt(TYPEID),
                        rs.getInt(GROUPID), rs.getString(EAINODE),
                        rs.getTimestamp(SENDTIME)));
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
     * Gets all objects from the database within a certain date range
     *
     * @param dsName startDate in format yyyy-mm-dd HH24:mi:ss
     * @param daoQuery endDate in format yyyy-mm-dd HH24:mi:ss or null for
     *        present
     * @param startDate DOCUMENT ME!
     * @param endDate DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getAllForRange(String dsName, String daoQuery,
        String startDate, String endDate)
        throws AdminException {
        Vector vAll = new Vector();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //if the endDate is not specified, set it to now
        if ((endDate == null) || endDate.trim().equalsIgnoreCase("")) {
            endDate = (new Timestamp(System.currentTimeMillis())).toString();
        }

        //strip the millis from the end of the start/end dates
        startDate = startDate.substring(0, startDate.lastIndexOf("."));
        endDate = endDate.substring(0, endDate.lastIndexOf("."));

        try {
            // make sure i've got an open connection
            con = ConnectionManager.getInstance().getConnection(dsName);

            // prepare statment, no parameters to populate this should return all records
            ps = con.prepareStatement(daoQuery);
            ps.setString(1, startDate);
            ps.setString(2, endDate);

            // execute statement and populate result set
            rs = ps.executeQuery();

            // get the first element from the resultset and create an instance of this object
            while (rs.next()) {
                vAll.addElement(new TroubleTicketHistoryBean(rs.getInt(ID),
                        rs.getInt(KEYID), rs.getInt(TYPEID),
                        rs.getInt(GROUPID), rs.getString(KEYCOMPID),
                        rs.getString(KEYERRCD), rs.getString(KEYEVENTCD),
                        rs.getString(TYPENAME), rs.getString(TYPEVALUE),
                        rs.getString(EAINODE), rs.getString(GROUPNAME),
                        rs.getString(GROUPDESC), rs.getTimestamp(SENDTIME)));
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
            ps.setInt(1, this.iKeyID);
            ps.setInt(2, this.iTypeID);
            ps.setString(3, this.sEaiNode);
            ps.setTimestamp(4, this.dSendTime);

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
            ps.setInt(1, this.iKeyID);
            ps.setInt(2, this.iTypeID);
            ps.setString(3, this.sEaiNode);
            ps.setTimestamp(4, this.dSendTime);
            ps.setInt(5, this.iID);

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
