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
public class TroubleTicketKeyGroupXRefBean extends DataAccessBean {
    //~ Static variables/initializers ------------------------------------------

    //my fields
    public static final String TABLE = "TT_KEY_GRP_XREF";
    public static final String ID = "TT_KG_XREF_ID";
    public static final String KEYID = "TT_KEY_ID";
    public static final String SERVICE = "SVC_TYPE";
    public static final String GROUPID = "TT_GROUP_ID";

    //other fields
    public static final String COMPID = "COMP_ID";
    public static final String EAIERRCD = "EAI_ERR_CD";
    public static final String EAIEVNTTYPE = "EAI_EVENT_TYPE";
    public static final String NAME = "TT_GROUP_NAME";
    public static final String DESCRIPTION = "DESCRIPTION";

    //~ Instance variables -----------------------------------------------------

    private int iId = 0;
    private int iKeyId = 0;
    private int iGroupId = 0;
    private String sService = "";
    private String sCompId = "";
    private String sErrCd = "";
    private String sEvnt = "";
    private String sName = "";
    private String sDescription = "";

    //~ Constructors -----------------------------------------------------------


    /**
     * Create default instance of TroubleTicketKeyGroupXRefBean class
     */
    public TroubleTicketKeyGroupXRefBean() {
    }


    /**
     * Create an instance of the TroubleTicketKeyGroupXRefBean class from
     * database
     *
     * @param id id - tt_kg_xref_id value from database
     * @param kId kId - tt_key_id value from database
     * @param svc svc - svc_type value from database
     * @param gId gId - tt_group_id value from database
     */
    public TroubleTicketKeyGroupXRefBean(int id, int kId, String svc, int gId) {
        iId = id;
        iKeyId = kId;
        sService = (svc != null) ? svc : "";
        iGroupId = gId;
    }


    /**
     * Creates a new TroubleTicketKeyGroupXRefBean object.
     *
     * @param id DOCUMENT ME!
     * @param key DOCUMENT ME!
     * @param svc DOCUMENT ME!
     * @param group DOCUMENT ME!
     * @param comp DOCUMENT ME!
     * @param err DOCUMENT ME!
     * @param evnt DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param desc DOCUMENT ME!
     */
    public TroubleTicketKeyGroupXRefBean(int id, int key, String svc,
        int group, String comp, String err, String evnt, String name,
        String desc) {
        iId = id;
        iKeyId = key;
        sService = (svc != null) ? svc : "";
        iGroupId = group;
        sCompId = (comp != null) ? comp : "";
        sErrCd = (err != null) ? err : "";
        sEvnt = (evnt != null) ? evnt : "";
        sName = (name != null) ? name : "";
        sDescription = (desc != null) ? desc : "";
    }


    /**
     * Create an instance of the TroubleTicketKeyGroupXRefBean class from
     * values passed
     *
     * @param kId kId - tt_key_id
     * @param svc svc - svc_type
     * @param gId gId - tt_group_id
     */
    public TroubleTicketKeyGroupXRefBean(int kId, String svc, int gId) {
        iKeyId = kId;
        sService = (svc != null) ? svc : "";
        iGroupId = gId;
    }

    //~ Methods ----------------------------------------------------------------


    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setId(int id) {
        iId = id;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getId() {
        return iId;
    }


    /**
     * DOCUMENT ME!
     *
     * @param kId DOCUMENT ME!
     */
    public void setKeyId(int kId) {
        iKeyId = kId;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKeyId() {
        return iKeyId;
    }


    /**
     * DOCUMENT ME!
     *
     * @param svc DOCUMENT ME!
     */
    public void setService(String svc) {
        sService = svc;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getService() {
        return sService;
    }


    /**
     * DOCUMENT ME!
     *
     * @param gId DOCUMENT ME!
     */
    public void setGroupId(int gId) {
        iGroupId = gId;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGroupId() {
        return iGroupId;
    }


    /**
     * @see DataAccessBean#getById(String, String, int)
     */
    public Object getById(String dsName, String daoQuery, int id)
        throws AdminException {
        TroubleTicketKeyGroupXRefBean ttkgxrb = null;
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
                ttkgxrb = new TroubleTicketKeyGroupXRefBean(rs.getInt(ID),
                        rs.getInt(KEYID), rs.getString(SERVICE),
                        rs.getInt(GROUPID));
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

        return ttkgxrb;
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
                vAll.addElement(new TroubleTicketKeyGroupXRefBean(rs.getInt(ID),
                        rs.getInt(KEYID), rs.getString(SERVICE),
                        rs.getInt(GROUPID), rs.getString(COMPID),
                        rs.getString(EAIERRCD), rs.getString(EAIEVNTTYPE),
                        rs.getString(NAME), rs.getString(DESCRIPTION)));
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
            ps.setInt(1, this.iKeyId);
            ps.setString(2, this.sService);
            ps.setInt(3, this.iGroupId);

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
            ps.setInt(1, this.iKeyId);
            ps.setString(2, this.sService);
            ps.setInt(3, this.iGroupId);
            ps.setInt(4, this.iId);

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


    /**
     * Gets the compId
     *
     * @return Returns a String
     */
    public String getCompId() {
        return sCompId;
    }


    /**
     * Sets the compId
     *
     * @param compId The compId to set
     */
    public void setCompId(String compId) {
        sCompId = compId;
    }


    /**
     * Gets the description
     *
     * @return Returns a String
     */
    public String getDescription() {
        return sDescription;
    }


    /**
     * Sets the description
     *
     * @param description The description to set
     */
    public void setDescription(String description) {
        sDescription = description;
    }


    /**
     * Gets the errCd
     *
     * @return Returns a String
     */
    public String getErrCd() {
        return sErrCd;
    }


    /**
     * Sets the errCd
     *
     * @param errCd The errCd to set
     */
    public void setErrCd(String errCd) {
        sErrCd = errCd;
    }


    /**
     * Gets the evnt
     *
     * @return Returns a String
     */
    public String getEvnt() {
        return sEvnt;
    }


    /**
     * Sets the evnt
     *
     * @param evnt The evnt to set
     */
    public void setEvnt(String evnt) {
        sEvnt = evnt;
    }


    /**
     * Gets the name
     *
     * @return Returns a String
     */
    public String getName() {
        return sName;
    }


    /**
     * Sets the name
     *
     * @param name The name to set
     */
    public void setName(String name) {
        sName = name;
    }
}
