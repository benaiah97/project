package pvt.disney.dti.gateway.dao;

import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import oracle.sql.CLOB;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.PropertyName;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.util.PCIControl;
import pvt.disney.dti.gateway.util.ResourceLoader;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;
import com.disney.util.AbstractInitializer;
import com.disney.util.PropertyHelper;

/**
 * This class, while not using the core DAO, handles the remaining queries concerning the DTI log tables. Since these tables use the Oracle CLOB, they cannot use the standard DAO classes.
 * 
 * @author Todd Lewis
 * @since 2.16.3
 * 
 */
public class LogTransaction {

	/** The standard core logging mechanism. */
	private EventLogger eventLogger = EventLogger.getLogger(this.getClass());

	/** Core properties management initializer. */
	private AbstractInitializer abstrInit = null;

	/** Properties variable to store properties from AbstractInitializer. */
	private Properties props = null;

	/**
	 * DBTransaction Constructor (default). Gets the properties instance.
	 * 
	 * @throws DTIException
	 *             on an initialization error.
	 */
	public LogTransaction() throws DTIException {

		try {
			
			ResourceBundle rb = ResourceLoader.getResourceBundle("dtiApp");
			props = ResourceLoader.convertResourceBundleToProperties(rb);
			
		}
		catch (Exception e) {
			eventLogger.sendEvent("THROWABLE initing props: " + e.toString(),
					EventType.FATAL, this);
			throw new DTIException(LogTransaction.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Unable to get properties for LogTransaction class.", e);
		}

	}

	/**
	 * Gets a connection to a persistent object, run the class internal query and return the result wrapped in an IValueSetCollection. At the end, release the connection to the persistent object.
	 * 
	 * This unique access method was added because of a known problem with utilizing Oracle CLOB objects under database connection pooling. We needed to use CLOB objects in order to pass XML data larger than 4000k.
	 * 
	 * @throws DTIException
	 *             on a DB error.
	 */
	public boolean insertITSLog(String payloadId, int entityId,
			String inXMLString, Integer transIdITS) throws DTIException {

		eventLogger.sendEvent(
				"Entering insertITSLog(DTITransactionTO, String)",
				EventType.DEBUG, this);

		CLOB xmlClob = null;
		Connection clobConn = null;
		PreparedStatement preparedStatement = null;
		String newXMLString = null;

		try {
			// PCI Control: DO NOT REMOVE
			newXMLString = PCIControl.overwritePciDataInXML(inXMLString);

			// Open a connection to the Oracle Database
			eventLogger.sendEvent(
					"DBTransaction: Connecting to DTIDataSource...",
					EventType.DEBUG, this);

			// Set Oracle JDBC Driver to be used for connection object
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

			// Get the properties.
			String dataSourceURL = PropertyHelper.readPropsValue(
					PropertyName.DATA_SOURCE_URL, props, null);
			String databaseUser = PropertyHelper.readPropsValue(
					PropertyName.DBUSER, props, null);
			String databasePassword = PropertyHelper.readPropsValue(
					PropertyName.DBPASSWORD, props, null);

			// Connect to the database
			clobConn = DriverManager.getConnection(dataSourceURL, databaseUser,
					databasePassword);

			clobConn.setAutoCommit(true);

			// Set and Get Oracle CLOB object
			xmlClob = getXMLCLOB(newXMLString, clobConn);

			String statement = "INSERT INTO INBOUND_TS_LOG (TRANS_ID, TRANS_DATE, TS_TRANSID, XML_DOC, ENTITYID) " + "VALUES (" + transIdITS
					.longValue() + ", SYSDATE, '" + payloadId + "', ? ," + entityId + ")";

			// Create the prepared statement with a SQL that doesn't contain any
			// parameter
			eventLogger.sendEvent(
					"DBTransaction: Creating prepared SQL statement...",
					EventType.DEBUG, this);
			preparedStatement = clobConn.prepareStatement(statement);

			// Executes the statement
			preparedStatement.setObject(1, xmlClob);

			eventLogger.sendEvent(
					"DBTransaction: Execute prepared SQL query...",
					EventType.DEBUG, this);

			if (preparedStatement.executeUpdate() == 1) {
				eventLogger
						.sendEvent(
								"Inserted new ticket seller request into INBOUND_TS_LOG!",
								EventType.DEBUG, this);
			}

			// Close the connection.
			preparedStatement.close();
			clobConn.close();
			eventLogger.sendEvent("Successfully closed CLOB DB connection.",
					EventType.DEBUG, this);

		}
		catch (SQLException exc) {

			eventLogger.sendEvent(
					"SQLException on inbound TS log insert: " + exc.toString(),
					EventType.WARN, this);
			exc.printStackTrace();

			try {
				// Attempt to shut down connections
				if (preparedStatement != null) preparedStatement.close();
				if (clobConn != null) clobConn.close();
				eventLogger.sendEvent(
						"Successfully closed CLOB DB connection.",
						EventType.DEBUG, this);
			}
			catch (SQLException exc2) {
				eventLogger
						.sendEvent(
								"Did NOT successfully close CLOB DB connection: " + exc2
										.toString(), EventType.WARN, this);
			}

			if (exc.getErrorCode() == 1) {
				eventLogger
						.sendEvent(
								"Recognized a duplicate request, did not insert to INBOUND_TS_LOG.",
								EventType.WARN, this);
				return false; // I was NOT able to insert this record (duplicate)
			}
			else {
				throw new DTIException(LogTransaction.class,
						DTIErrorCode.FAILED_DB_OPERATION_SVC,
						"Exception executing query in insertITSLog", exc);
			}

		}

		return true;
	}

	/**
	 * Inserts a row into the INBOUND_TP_LOG.
	 * 
	 * @param dtiTxn
	 *            The DTITransactionTO object containing the request.
	 * @param newXMLString
	 *            The request XML in provider format.
	 * @throws DTIException
	 *             on a DB error.
	 */
	public void insertITPLog(DTITransactionTO dtiTxn, String inXMLString) throws DTIException {

		eventLogger.sendEvent("Entering insertITPLog() via JDBC",
				EventType.DEBUG, this);
		CLOB xmlClob = null;
		Connection clobConn = null;
		PreparedStatement preparedStatement = null;
		String newXMLString = null;

		// Enrich the request with values required for logging.
		String errorCode;
		if (dtiTxn.isPriceMismatch()) errorCode = DTIErrorCode.PRICE_MISMATCH_WARNING
				.getErrorCode();
		else errorCode = " ";

		try {
			// PCI Control: DO NOT REMOVE
			newXMLString = PCIControl.overwritePciDataInXML(inXMLString);

			// Open a connection to the Oracle Database
			eventLogger.sendEvent(
					"DBTransaction: Connecting to DTIDataSource...",
					EventType.DEBUG, this);

			// Set Oracle JDBC Driver to be used for connection object
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

			// Get the properties.
			String dataSourceURL = PropertyHelper.readPropsValue(
					PropertyName.DATA_SOURCE_URL, props, null);
			String databaseUser = PropertyHelper.readPropsValue(
					PropertyName.DBUSER, props, null);
			String databasePassword = PropertyHelper.readPropsValue(
					PropertyName.DBPASSWORD, props, null);

			// Connect to the database
			clobConn = DriverManager.getConnection(dataSourceURL, databaseUser,
					databasePassword);

			clobConn.setAutoCommit(true);
			eventLogger.sendEvent("insertITPLog(): " + newXMLString,
					EventType.DEBUG, this);
			// Set and Get Oracle CLOB object
			xmlClob = getXMLCLOB(newXMLString, clobConn);

			String statement = "INSERT INTO INBOUND_TP_LOG (TRANS_ID, TRANS_DATE, TP_TRANSID, ERR_RETURN_CODE, " + "MQ_MSGID, REPLYTOQ, REPLYTOQMGR, PAYLOAD_ID, TARG_SYS, XML_VERSION, " + "COMM_PROTOCOL, COMM_METHOD, XML_DOC, INBOUND_TS_ID) " + "values (" + dtiTxn
					.getTransIdITP() + ", SYSDATE, " + dtiTxn.getTpRefNum()
					.longValue() + ", '" + errorCode + "', '00', 'NOREPLYQ', 'NOREPLYQMGR', '" + dtiTxn
					.getRequest().getPayloadHeader().getPayloadID() + "', '" + dtiTxn
					.getRequest().getPayloadHeader().getTarget() + "', '" + dtiTxn
					.getRequest().getPayloadHeader().getVersion() + "', '" + dtiTxn
					.getRequest().getPayloadHeader().getCommProtocol() + "', '" + dtiTxn
					.getRequest().getPayloadHeader().getCommMethod() + "', ?, " + dtiTxn
					.getTransIdITS() + ")";

			// Create the prepared statement with a SQL that doesn't contain any
			// parameter
			eventLogger.sendEvent(
					"DBTransaction: Creating prepared SQL statement...",
					EventType.DEBUG, this);
			preparedStatement = clobConn.prepareStatement(statement);

			// Executes the statement
			preparedStatement.setObject(1, xmlClob);

			eventLogger.sendEvent(
					"DBTransaction: Execute prepared SQL query...",
					EventType.DEBUG, this);

			if (preparedStatement.executeUpdate() == 1) {
				eventLogger
						.sendEvent(
								"Inserted new ticket seller request into INBOUND_TP_LOG!",
								EventType.DEBUG, this);
			}

			// Close the connection.
			preparedStatement.close();
			clobConn.close();
			eventLogger.sendEvent("Successfully closed CLOB DB connection.",
					EventType.DEBUG, this);

		}
		catch (SQLException exc) {

			eventLogger.sendEvent(
					"SQLException on inbound TS log insert: " + exc.toString(),
					EventType.WARN, this);
			exc.printStackTrace();

			try {
				// Attempt to shut down connections
				if (preparedStatement != null) preparedStatement.close();
				if (clobConn != null) clobConn.close();
				eventLogger.sendEvent(
						"Successfully closed CLOB DB connection.",
						EventType.DEBUG, this);
			}
			catch (SQLException exc2) {
				eventLogger
						.sendEvent(
								"Did NOT successfully close CLOB DB connection: " + exc2
										.toString(), EventType.WARN, this);
			}

			throw new DTIException(LogTransaction.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing query in insertITPLog", exc);

		}

		return;
	}

	/**
	 * Inserts a row into the OUTBOUND_TP_LOG table.
	 * 
	 * @param dtTxn
	 *            The DTITransactionTO associated with this request.
	 * @param newXMLString
	 *            The ticket provider response, in string form.
	 * @throws DTIException
	 *             on a DB error.
	 */
	public void insertOTPLog(DTITransactionTO dtiTxn, String inXMLString) throws DTIException {

		eventLogger.sendEvent("Entering insertOTPLog() via JDBC",
				EventType.DEBUG, this);
		CLOB xmlClob = null;
		Connection clobConn = null;
		PreparedStatement preparedStatement = null;
		String newXMLString = null;

		Integer otpLogTransId = dtiTxn.getTransIdOTP();
		Integer itpLogTransId = dtiTxn.getTransIdITP();
		Integer tpRefNum = dtiTxn.getTpRefNum();

		try {
			// PCI Control: DO NOT REMOVE
			newXMLString = PCIControl.overwritePciDataInXML(inXMLString);
			// PCI Control: DO NOT REMOVE

			// Open a connection to the Oracle Database
			eventLogger.sendEvent(
					"DBTransaction: Connecting to DTIDataSource...",
					EventType.DEBUG, this);

			// Set Oracle JDBC Driver to be used for connection object
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

			// Get the properties.
			String dataSourceURL = PropertyHelper.readPropsValue(
					PropertyName.DATA_SOURCE_URL, props, null);
			String databaseUser = PropertyHelper.readPropsValue(
					PropertyName.DBUSER, props, null);
			String databasePassword = PropertyHelper.readPropsValue(
					PropertyName.DBPASSWORD, props, null);

			eventLogger.sendEvent("Opening a CLOB DB connection.",
					EventType.DEBUG, this);

			// Connect to the database
			clobConn = DriverManager.getConnection(dataSourceURL, databaseUser,
					databasePassword);

			clobConn.setAutoCommit(true);

			// Set and Get Oracle CLOB object
			xmlClob = getXMLCLOB(newXMLString, clobConn);

			String statement = "INSERT INTO OUTBOUND_TP_LOG (TRANS_ID, TRANS_DATE, " + "TP_TRANSID, ERR_RETURN_CODE, XML_DOC, INBOUND_TP_ID)" + " values(" + otpLogTransId + ", SYSDATE, '" + tpRefNum + "', ' ', ?, " + itpLogTransId + ")";

			// Create the prepared statement with a SQL that doesn't contain any
			// parameter
			eventLogger.sendEvent(
					"DBTransaction: Creating prepared SQL statement...",
					EventType.DEBUG, this);
			preparedStatement = clobConn.prepareStatement(statement);

			// Executes the statement
			preparedStatement.setObject(1, xmlClob);

			eventLogger.sendEvent(
					"DBTransaction: Execute prepared SQL query...",
					EventType.DEBUG, this);

			if (preparedStatement.executeUpdate() == 1) {
				eventLogger
						.sendEvent(
								"Inserted new ticket seller request into OUTBOUND_TP_LOG!",
								EventType.DEBUG, this);
			}

			// Close the connection.
			preparedStatement.close();
			clobConn.close();
			eventLogger.sendEvent("Successfully closed CLOB DB connection.",
					EventType.DEBUG, this);

		}
		catch (SQLException exc) {

			eventLogger.sendEvent(
					"SQLException on inbound TS log insert: " + exc.toString(),
					EventType.WARN, this);
			exc.printStackTrace();

			try {
				// Attempt to shut down connections
				if (preparedStatement != null) preparedStatement.close();
				if (clobConn != null) clobConn.close();
				eventLogger.sendEvent(
						"Successfully closed CLOB DB connection.",
						EventType.DEBUG, this);
			}
			catch (SQLException exc2) {
				eventLogger
						.sendEvent(
								"Did NOT successfully close CLOB DB connection: " + exc2
										.toString(), EventType.WARN, this);
			}

			throw new DTIException(LogTransaction.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing query in insertOTPLog", exc);

		}

		return;
	}

	/**
	 * Inserts a record into the OUTBOUND_TS_LOG.
	 * 
	 * @param dtiTxn
	 *            The DTITransactionTO object representing this transaction
	 * @param newXMLString
	 *            The DTI response XML in string format.
	 * @throws DTIException
	 *             on a DB error.
	 */
	public void insertOTSLog(DTITransactionTO dtiTxn, String inXMLString) throws DTIException {

		eventLogger.sendEvent("Entering insertOTSLog() via JDBC",
				EventType.DEBUG, this);
		CLOB xmlClob = null;
		Connection clobConn = null;
		PreparedStatement preparedStatement = null;
		String newXMLString = null;

		try {
			Integer otsLogTransId = dtiTxn.getTransIdOTS();

			// PCI Control: DO NOT REMOVE
			newXMLString = PCIControl.overwritePciDataInXML(inXMLString);

			// Open a connection to the Oracle Database
			eventLogger.sendEvent(
					"DBTransaction: Connecting to DTIDataSource...",
					EventType.DEBUG, this);

			// Set Oracle JDBC Driver to be used for connection object
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

			// Get the properties.
			String dataSourceURL = PropertyHelper.readPropsValue(
					PropertyName.DATA_SOURCE_URL, props, null);
			String databaseUser = PropertyHelper.readPropsValue(
					PropertyName.DBUSER, props, null);
			String databasePassword = PropertyHelper.readPropsValue(
					PropertyName.DBPASSWORD, props, null);

			eventLogger.sendEvent("Opening a CLOB DB connection.",
					EventType.DEBUG, this);

			// Connect to the database
			clobConn = DriverManager.getConnection(dataSourceURL, databaseUser,
					databasePassword);

			clobConn.setAutoCommit(true);

			// Set and Get Oracle CLOB object
			xmlClob = getXMLCLOB(newXMLString, clobConn);

			// DYNAMICALLY BUILD THE SQL STATEMENT
			StringBuffer fieldsBuffer = new StringBuffer();
			StringBuffer valuesBuffer = new StringBuffer();

			// Create the insert fields that always appear
			fieldsBuffer
					.append("TRANS_ID, TRANS_DATE, TS_TRANSID, INBOUND_TS_ID, XML_DOC");
			valuesBuffer
					.append(otsLogTransId.toString() + ", SYSDATE, '" + dtiTxn
							.getResponse().getPayloadHeader().getTsPayloadID() + "', '" + dtiTxn
							.getTransIdITS().longValue() + "', ?");

			// Add the error field if it has a value
			if (dtiTxn.getResponse().getDtiError() != null) {
				fieldsBuffer.append(", ERR_RETURN_CODE");
				valuesBuffer.append(", '" + dtiTxn.getResponse().getDtiError()
						.getErrorCode().intValue() + "'");
			}

			if (dtiTxn.getTransIdITP() != null) {
				fieldsBuffer.append(", INBOUND_TP_ID");
				valuesBuffer.append(", " + dtiTxn.getTransIdITP().longValue());
			}

			if (dtiTxn.getTransIdOTP() != null) {
				fieldsBuffer.append(", OUTBOUND_TP_ID");
				valuesBuffer.append(", " + dtiTxn.getTransIdOTP().longValue());
			}

			String statement = "INSERT INTO OUTBOUND_TS_LOG (" + fieldsBuffer
					.toString() + ") values(" + valuesBuffer.toString() + ")";

			// Create the prepared statement with a SQL that doesn't contain any
			// parameter
			eventLogger.sendEvent(
					"DBTransaction: Creating prepared SQL statement...",
					EventType.DEBUG, this);
			preparedStatement = clobConn.prepareStatement(statement);

			// Executes the statement
			preparedStatement.setObject(1, xmlClob);

			eventLogger.sendEvent(
					"DBTransaction: Execute prepared SQL query...",
					EventType.DEBUG, this);

			if (preparedStatement.executeUpdate() == 1) {
				eventLogger
						.sendEvent(
								"Inserted new ticket seller request into INBOUND_TS_LOG!",
								EventType.DEBUG, this);
			}

			// Close the connection.
			preparedStatement.close();
			clobConn.close();
			eventLogger.sendEvent("Successfully closed CLOB DB connection.",
					EventType.DEBUG, this);

		}
		catch (SQLException exc) {

			eventLogger.sendEvent(
					"SQLException on inbound TS log insert: " + exc.toString(),
					EventType.WARN, this);
			exc.printStackTrace();

			try {
				// Attempt to shut down connections
				if (preparedStatement != null) preparedStatement.close();
				if (clobConn != null) clobConn.close();
				eventLogger.sendEvent(
						"Successfully closed CLOB DB connection.",
						EventType.DEBUG, this);
			}
			catch (SQLException exc2) {
				eventLogger
						.sendEvent(
								"Did NOT successfully close CLOB DB connection: " + exc2
										.toString(), EventType.WARN, this);
			}

			throw new DTIException(LogTransaction.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing query in insertOTSLog", exc);

		}

		return;
	}

	/**
	 * Creates CLOB object and assigns it a value. This method was added in an effort insert XMLData larger than 4000k. This is a known issue with sending string literals larger 4000k in an insert statement.
	 * 
	 * @since 01/16/04 16:23:00 PM
	 * 
	 * @param xmlData
	 *            - A string containing XML.
	 * @param c
	 *            - an instance of java.sql.Connection
	 * @return oracle.sql.CLOB;
	 * @throws DTIException
	 */
	private CLOB getXMLCLOB(String xmlData, java.sql.Connection c) throws DTIException {
    CLOB tempClob = null;
    Writer tempClobWriter = null;

    eventLogger.sendEvent("Entering getXMLCLOB(String,Connection)",
        EventType.DEBUG, this);

    try {
      // If the temporary CLOB has not yet been created, create new
      tempClob = CLOB.createTemporary(c, true, CLOB.DURATION_SESSION);

      // Open the temporary CLOB in read-write mode to enable writing
      tempClob.open(CLOB.MODE_READWRITE);

      // Get the output stream to write
      tempClobWriter = tempClob.getCharacterOutputStream();

      // Write the data into the temporary CLOB
      tempClobWriter.write(xmlData);

    }
    catch (Exception exc) {
      exc.printStackTrace();
      throw new DTIException("getXMLCLOB()", LogTransaction.class, 3,
          DTIErrorCode.UNABLE_TO_LOG_TRANSACTION_DB.getErrorCode(),
          "Exception in populating the CLOB", exc);
    }
    finally {
      try {
        // Flush and close the stream
        tempClobWriter.flush();
        tempClobWriter.close();

        // Close the temporary CLOB
        tempClob.close();
      }
      catch (IOException ioe) {
        ioe.printStackTrace();
        throw new DTIException("getXMLCLOB()", LogTransaction.class, 3,
            DTIErrorCode.UNABLE_TO_LOG_TRANSACTION_DB.getErrorCode(),
            "Exception flushing Writer object.", ioe);
      }
      catch (SQLException sqe) {
        sqe.printStackTrace();
        throw new DTIException("getXMLCLOB()", LogTransaction.class, 3,
            DTIErrorCode.UNABLE_TO_LOG_TRANSACTION_DB.getErrorCode(),
            "Exception closing CLOB instance.", sqe);
      }

    }

    return tempClob;
	}

}
