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
 * Analogous to the TT_KEY_TYPE_XREF table used by the TroubleTicketAgent also
 * includes field references to TT_KEY, TT_TYPE, TT_GROUP for display purposes
 */
public class TroubleTicketKeyTypeXRefBean extends DataAccessBean {
    //~ Static variables/initializers ------------------------------------------

    public static final String TABLE = "TT_KEY_TYPE_XREF";
    public static final String ID = "TT_KT_XREF_ID";
    public static final String KEYID = "TT_KEY_ID";
    public static final String MAXALLOWED = "MAX_ALLOWED";
    public static final String INTERVAL = "INTERVAL";
    public static final String FREQUENCY = "FREQUENCY";
    public static final String TYPEID = "TT_TYPE_ID";
    public static final String EAINODE = "EAI_NODE";
    public static final String SVCTYPE = "SVC_TYPE";

    //OTHER FIELDS
    public static final String KEYCOMP = "COMP_ID";
    public static final String KEYERRCD = "EAI_ERR_CD";
    public static final String KEYEVENT = "EAI_EVENT_TYPE";
    public static final String TYPENAME = "TT_TYPE_NAME";
    public static final String TYPEVALUE = "TT_TYPE_VALUE";

    //~ Instance variables -----------------------------------------------------

    private int iID = 0;
    private int iKeyID = 0;
    private int iMaxAllowed = 0;
    private int iInterval = 0;
    private int iFrequency = 0;
    private int iTypeID = 0;
    private String sEAINode = "";
    private String sSVCType = "";
    private String sKeyCompId = "";
    private String sKeyEaiErr = "";
    private String sKeyEvent = "";
    private String sTypeName = "";
    private String sTypeValue = "";

    //~ Constructors -----------------------------------------------------------


    /**
     * Creates a default instance of the TroubleTicketKeyTypeXRefBean
     */
    public TroubleTicketKeyTypeXRefBean() {
    }


    /**
     * Creates an instance of the TroubleTicketKeyTypeXRefBean
     *
     * @param iKey iKey - tt_key_id
     * @param iMax iMax - max_allowed
     * @param iInt iInt - interval
     * @param iFreq iFreq - frequency
     * @param iType iType - tt_type_id
     * @param sNode sNode - eai_node
     * @param sType sType - svc_type
     */
    public TroubleTicketKeyTypeXRefBean(int iKey, int iMax, int iInt,
        int iFreq, int iType, String sNode, String sType) {
        iKeyID = iKey;
        iMaxAllowed = iMax;
        iInterval = iInt;
        iFrequency = iFreq;
        iTypeID = iType;
        sEAINode = (sNode != null) ? sNode : "";
        sSVCType = (sType != null) ? sType : "";
    }


    /**
     * Creates an instance of the TroubleTicketKeyTypeXRefBean from the
     * database
     *
     * @param iId iID - tt_kt_xref_id value from database
     * @param iKey iKey - tt_key_id value from database
     * @param iMax iMax - max_allowed value from database
     * @param iInt iInt - interval value from database
     * @param iFreq iFreq - frequency value from database
     * @param iType iType - tt_type_id value from database
     * @param sNode sNode - eai_node value from database
     * @param sType sType - svc_type value from database
     */
    private TroubleTicketKeyTypeXRefBean(int iId, int iKey, int iMax, int iInt,
        int iFreq, int iType, String sNode, String sType) {
        iID = iId;
        iKeyID = iKey;
        iMaxAllowed = iMax;
        iInterval = iInt;
        iFrequency = iFreq;
        iTypeID = iType;
        sEAINode = (sNode != null) ? sNode : "";
        sSVCType = (sType != null) ? sType : "";
    }


    /**
     * Creates an instance of the TroubleTicketKeyTypeXRefBean from the
     * database including external fields for display
     *
     * @param id id - TT_KT_XREF_ID value from database
     * @param key key - TT_KEY_ID value from database
     * @param max max - MAX_ALLOWED value from database
     * @param inter inter - INTERVAL value from database
     * @param freq freq - FREQUENCY value from database
     * @param type type - TT_TYPE_ID value from database
     * @param node node - EAI_NODE value from database
     * @param svc svc - SVC_TYPE value from database
     * @param comp comp - COMP_ID value from database
     * @param err err - EAI_ERR_CD value from database
     * @param event event - EAI_EVENT_TYPE value from database
     * @param name name - TT_TYPE_NAME value from database
     * @param value value - TT_TYPE_VALUE value from database
     */
    public TroubleTicketKeyTypeXRefBean(int id, int key, int max, int inter,
        int freq, int type, String node, String svc, String comp, String err,
        String event, String name, String value) {
        iID = id;
        iKeyID = key;
        iMaxAllowed = max;
        iInterval = inter;
        iFrequency = freq;
        iTypeID = type;
        sEAINode = (node != null) ? node : "";
        sSVCType = (svc != null) ? svc : "";
        sKeyCompId = (comp != null) ? comp : "";
        sKeyEaiErr = (err != null) ? err : "";
        sKeyEvent = (event != null) ? event : "";
        sTypeName = (name != null) ? name : "";
        sTypeValue = (value != null) ? value : "";
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
    public void setKeyId(int kId) {
        iKeyID = kId;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKeyId() {
        return iKeyID;
    }


    /**
     * DOCUMENT ME!
     *
     * @param max DOCUMENT ME!
     */
    public void setMaxAllowed(int max) {
        iMaxAllowed = max;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxAllowed() {
        return iMaxAllowed;
    }


    /**
     * DOCUMENT ME!
     *
     * @param inter DOCUMENT ME!
     */
    public void setInterval(int inter) {
        iInterval = inter;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getInterval() {
        return iInterval;
    }


    /**
     * DOCUMENT ME!
     *
     * @param freq DOCUMENT ME!
     */
    public void setFrequency(int freq) {
        iFrequency = freq;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFrequency() {
        return iFrequency;
    }


    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setTypeId(int type) {
        iTypeID = type;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTypeId() {
        return iTypeID;
    }


    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    public void setEaiNode(String node) {
        sEAINode = node;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getEaiNode() {
        return sEAINode;
    }


    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setSvcType(String type) {
        sSVCType = type;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSvcType() {
        return sSVCType;
    }


    /**
     * DOCUMENT ME!
     *
     * @param keyComp DOCUMENT ME!
     */
    public void setKeyCompId(String keyComp) {
        sKeyCompId = keyComp;
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
     * @param keyEaiErr DOCUMENT ME!
     */
    public void setKeyEaiErr(String keyEaiErr) {
        sKeyEaiErr = keyEaiErr;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKeyEaiErr() {
        return sKeyEaiErr;
    }


    /**
     * DOCUMENT ME!
     *
     * @param keyEvent DOCUMENT ME!
     */
    public void setKeyEvent(String keyEvent) {
        sKeyEvent = keyEvent;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKeyEvent() {
        return sKeyEvent;
    }


    /**
     * DOCUMENT ME!
     *
     * @param typeName DOCUMENT ME!
     */
    public void setTypeName(String typeName) {
        sTypeName = typeName;
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
     * @param typeValue DOCUMENT ME!
     */
    public void setTypeValue(String typeValue) {
        sTypeValue = typeValue;
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
        boolean bOpenedHere = false;

        try {
            //make sure i've got an open connection
            con = ConnectionManager.getInstance().getConnection(dsName);

            //prepare statement, it's a select all so no query parameters need to be set
            ps = con.prepareStatement(daoQuery);

            //execute query
            rs = ps.executeQuery();

            //create instances of this object and add them to the vector until i've gotten everything from the result set
            while (rs.next()) {
                vAllTypes.addElement(new TroubleTicketKeyTypeXRefBean(rs.getInt(
                            ID), rs.getInt(KEYID), rs.getInt(MAXALLOWED),
                        rs.getInt(INTERVAL), rs.getInt(FREQUENCY),
                        rs.getInt(TYPEID), rs.getString(EAINODE),
                        rs.getString(SVCTYPE), rs.getString(KEYCOMP),
                        rs.getString(KEYERRCD), rs.getString(KEYEVENT),
                        rs.getString(TYPENAME), rs.getString(TYPEVALUE)));
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
     * TroubleTicketKeyTypeXRefBean to be usable
     *
     * @see DataAccessBean#getById(String, String, int)
     */
    public Object getById(String dsName, String daoQuery, int id)
        throws AdminException {
        TroubleTicketKeyTypeXRefBean ttktxb = null;
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
                ttktxb = new TroubleTicketKeyTypeXRefBean(rs.getInt(ID),
                        rs.getInt(KEYID), rs.getInt(MAXALLOWED),
                        rs.getInt(INTERVAL), rs.getInt(FREQUENCY),
                        rs.getInt(TYPEID), rs.getString(EAINODE),
                        rs.getString(SVCTYPE));
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

        return (Object)ttktxb;
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

            // prepare statment and populate query paramerters
            ps = con.prepareStatement(daoQuery);
            ps.setInt(1, this.iKeyID);
            ps.setString(2, this.sEAINode);
            ps.setString(3, this.sSVCType);
            ps.setInt(4, this.iMaxAllowed);
            ps.setInt(5, this.iInterval);
            ps.setInt(6, this.iFrequency);
            ps.setInt(7, this.iTypeID);

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
            ps.setString(2, this.sEAINode);
            ps.setString(3, this.sSVCType);
            ps.setInt(4, this.iMaxAllowed);
            ps.setInt(5, this.iInterval);
            ps.setInt(6, this.iFrequency);
            ps.setInt(7, this.iTypeID);
            ps.setInt(8, this.iID);

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
