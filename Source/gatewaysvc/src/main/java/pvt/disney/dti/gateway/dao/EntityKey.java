package pvt.disney.dti.gateway.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.ValueConstants;
import pvt.disney.dti.gateway.dao.data.DBQueryBuilder;
import pvt.disney.dti.gateway.dao.data.DBUtil;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;

import pvt.disney.dti.gateway.connection.DAOHelper;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This DAO class provides access to the ticket seller entity tables.
 * 
 * @author lewit019
 * 
 */
public class EntityKey {

	// Constants
	/** Object instance used for logging. */
	private static final EntityKey THISINSTANCE = new EntityKey();

	/** Event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger(EntityKey.class.getCanonicalName());

	/** Constant value for the get entity query. */
	private static final String GET_ENT = "GET_ENT";

	/** Constant value for the get entity active query. */
	private static final String GET_ENT_ACTIVE = "GET_ENT_ACTIVE";

	/** Constant value for the get entity product query. */
	private static final String GET_ENTITY_PDT = "GET_ENTITY_PDT";

	/** Constant value for the get entity product query with upgrade. */
	private static final String GET_ENTITY_PDT_UPGRD = "GET_ENTITY_PDT_UPGRD";

	/** Constant value for the get entity product group query. */
	private static final String GET_ENTITY_PDT_GRP = "GET_ENTITY_PDT_GRP";

	/** Constant value for the get entity product group query with upgrade. */
	private static final String GET_ENTITY_PDT_GRP_UPGRD = "GET_ENTITY_PDT_GRP_UPGRD";

	/** Constant value for the get entity receipt query. */
	private static final String GET_ENTITY_RECEIPT = "GET_ENTITY_RECEIPT";

	/**
	 * Constructor for EntityKey
	 */
	private EntityKey() {
		super();
	}

	/**
	 * Returns an entity object when provided with a valid tsMac and tsLocation.
	 * 
	 * @param tsMac
	 *            The ticket seller master agreement code
	 * @param tsLocation
	 *            The ticket seller location
	 * @return EntityTO result object filled with appropriate values
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	public static final EntityTO getEntity(String tsMac, String tsLocation) throws DTIException {

		logger.sendEvent("Entering getEntity()", EventType.DEBUG, THISINSTANCE);

		EntityTO result = null;

		// Retrieve and validate the parameters
		if ((tsMac == null) || (tsLocation == null)) {
			throw new DTIException(EntityKey.class,
					DTIErrorCode.INVALID_ENTITY,
					"Insufficient parameters to execute getEntity.");
		}

		try {
			// Retrieve and validate the parameters
			Object[] values = { tsMac, tsLocation };

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ENT);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ENT",
					EventType.DEBUG, THISINSTANCE);
			result = (EntityTO) helper.processQuery(values);

		}
		catch (Exception e) {
			logger.sendEvent("Exception executing getEntity: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(EntityKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getEntity", e);
		}

		if (result != null) {
			logger.sendEvent(
					"getEntity found entity:  " + result.getEntityCode(),
					EventType.DEBUG, THISINSTANCE, result, null);
			// Populate the fields passed in.
			result.setTsMac(tsMac);
			result.setTsLocation(tsLocation);
		}
		else {
			result = new EntityTO();
			result.setEntityId(0);
			result.setTsMac(tsMac);
			result.setTsLocation(tsLocation);
		}

		return result;
	}

	/**
	 * Returns true/false if the entity provided is active or not.
	 * 
	 * @param tsMac
	 *            The ticket seller master agreement code
	 * @param tsLocation
	 *            The ticket seller location
	 * @return true if the entity is active, false if not.
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	public static final Boolean getEntityActive(String tsMac, String tsLocation) throws DTIException {

		logger.sendEvent("Entering getEntityActive()", EventType.DEBUG,
				THISINSTANCE);

		Boolean result = null;

		// Retrieve and validate the parameters
		if ((tsMac == null) || (tsLocation == null)) {
			throw new DTIException(EntityKey.class,
					DTIErrorCode.INVALID_ENTITY,
					"Insufficient parameters to execute getEntityActive.");
		}

		try {

			// Retrieve and validate the parameters
			Object[] values = { tsMac, tsLocation };

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ENT_ACTIVE);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ENT_ACTIVE",
					EventType.DEBUG, THISINSTANCE);
			result = (Boolean) helper.processQuery(values);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getEntityActive: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(EntityKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getEntityActive", e);
		}

		if (result == null) {
			throw new DTIException(
					EntityKey.class,
					DTIErrorCode.INVALID_ENTITY,
					"No entity retrieved for tsMac " + tsMac + " tsLocation " + tsLocation);
		}

		logger.sendEvent("getEntityActive found entity:  " + result.toString(),
				EventType.DEBUG, THISINSTANCE, result, null);

		return result;
	}

	/**
	 * Gets the products that can be sold by this entity.
	 * 
	 * @param entityTO
	 *            Entity Transfer Object
	 * @param dbProdList
	 *            the database product list
	 * @return array list of sellable products
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<BigInteger> getEntityProducts(
			EntityTO entityTO, ArrayList<DBProductTO> dbProdList) throws DTIException {

		logger.sendEvent("Entering getEntityProducts()", EventType.DEBUG,
				THISINSTANCE);

		ArrayList<BigInteger> result = new ArrayList<BigInteger>();

		// Retrieve and validate the parameters
		if ((entityTO == null) || (dbProdList == null)) {
			throw new DTIException(EntityKey.class,
					DTIErrorCode.INVALID_ENTITY,
					"Insufficient parameters to execute getEntityProducts.");
		}

		// Create a set of unique product code strings
		HashSet<BigInteger> pdtIdSet = new HashSet<BigInteger>();
		for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {
			if (aDBProduct.getPdtid() != null) pdtIdSet.add(aDBProduct
					.getPdtid());
		}

		// Ensure the product list is not empty. (as of 2.10)
		if (pdtIdSet.size() == 0) {
			logger.sendEvent(
					"getEntityProducts passed no valid products in dbProdList.",
					EventType.EXCEPTION, THISINSTANCE);
			throw new DTIException(AttributeKey.class,
					DTIErrorCode.INVALID_PRODUCT_CODE,
					"Exception executing getEntityProducts");
		}

		Object[] queryParms = { DBUtil.createSQLInList(pdtIdSet) };

		// Replaces "?"
		Long entityIdLong = Long.valueOf(entityTO.getEntityId());
		Object[] values = { entityIdLong };

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ENTITY_PDT);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ENTITY_PDT",
					EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<BigInteger>) helper.processQuery(values,
					queryParms, qBuilder);

			// Debug
			logger.sendEvent("getEntityProducts found products.",
					EventType.DEBUG, THISINSTANCE, result, null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getEntityProducts: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(AttributeKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getEntityProducts", e);
		}

		return result;

	}

	/**
	 * Gets the products that can be sold by this entity.
	 * 
	 * @param entityTO
	 *            Entity Transfer Object
	 * @param dbProdList
	 *            the database product list
	 * @param typeCode
	 *            the typeCode to narrow down the search with
	 * @return array list of sellable products
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<BigInteger> getEntityProducts(
			EntityTO entityTO, ArrayList<DBProductTO> dbProdList,
			String typeCode) throws DTIException {

		logger.sendEvent("Entering getEntityProducts() with upgrade",
				EventType.DEBUG, THISINSTANCE);

		ArrayList<BigInteger> result = new ArrayList<BigInteger>();

		// Retrieve and validate the parameters
		if ((entityTO == null) || (dbProdList == null) || (typeCode == null)) {
			throw new DTIException(EntityKey.class,
					DTIErrorCode.INVALID_ENTITY,
					"Insufficient parameters to execute getEntityProducts with upgrade.");
		}

		// Create a set of unique product code strings
		HashSet<BigInteger> pdtIdSet = new HashSet<BigInteger>();
		for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {
			if (aDBProduct.getPdtid() != null) pdtIdSet.add(aDBProduct
					.getPdtid());
		}

		// Ensure the product list is not empty. (as of 2.10)
		if (pdtIdSet.size() == 0) {
			logger.sendEvent(
					"getEntityProducts passed no valid products in dbProdList.",
					EventType.EXCEPTION, THISINSTANCE);
			throw new DTIException(AttributeKey.class,
					DTIErrorCode.INVALID_PRODUCT_CODE,
					"Exception executing getEntityProducts");
		}

		// Upgrade logic: on the from, we can come from a "sellable" group, or
		// a "upgrade-only" group
		if (typeCode.equals(ValueConstants.TYPE_CODE_SELL)) return getEntityProducts(
				entityTO, dbProdList);

		Object[] queryParms = { DBUtil.createSQLInList(pdtIdSet) };

		// Replaces "?"
		Long entityIdLong = Long.valueOf(entityTO.getEntityId());
		Object[] values = { entityIdLong };

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ENTITY_PDT_UPGRD);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ENTITY_PDT_UPGRD",
					EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<BigInteger>) helper.processQuery(values,
					queryParms, qBuilder);

			// Debug
			logger.sendEvent("getEntityProducts with upgrade found products.",
					EventType.DEBUG, THISINSTANCE, result, null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getEntityProducts with upgrade: " + e
							.toString(), EventType.WARN, THISINSTANCE);
			throw new DTIException(AttributeKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getEntityProducts with upgrade", e);
		}

		return result;

	}

	/**
	 * Returns the products in product groups that the entity can sell.
	 * 
	 * @param entityTO
	 *            The Entity Transfer Object.
	 * @param dbProdList
	 *            the database product list
	 * @return the array list of product ID's that the entity can sell.
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<BigInteger> getEntityProductGroups(
			EntityTO entityTO, ArrayList<DBProductTO> dbProdList) throws DTIException {

		logger.sendEvent("Entering getEntityProductGroups()", EventType.DEBUG,
				THISINSTANCE);

		ArrayList<BigInteger> result = new ArrayList<BigInteger>();

		// Retrieve and validate the parameters
		if ((entityTO == null) || (dbProdList == null)) {
			throw new DTIException(EntityKey.class,
					DTIErrorCode.INVALID_ENTITY,
					"Insufficient parameters to execute getEntityProductGroups.");
		}

		// Create a set of unique product code strings
		HashSet<BigInteger> pdtIdSet = new HashSet<BigInteger>();
		for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {
			if (aDBProduct.getPdtid() != null) pdtIdSet.add(aDBProduct
					.getPdtid());
		}

		// Ensure the product list is not empty. (as of 2.10)
		if (pdtIdSet.size() == 0) {
			logger.sendEvent(
					"getEntityProductGroups passed no valid products in dbProdList.",
					EventType.EXCEPTION, THISINSTANCE);
			throw new DTIException(AttributeKey.class,
					DTIErrorCode.INVALID_PRODUCT_CODE,
					"Exception executing getEntityProductGroups");
		}

		Object[] queryParms = { DBUtil.createSQLInList(pdtIdSet) };

		// Replaces "?"
		Long entityIdLong = Long.valueOf(entityTO.getEntityId());
		Object[] values = { entityIdLong };

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ENTITY_PDT_GRP);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ENTITY_PDT_GRP",
					EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<BigInteger>) helper.processQuery(values,
					queryParms, qBuilder);

			// Debug
			logger.sendEvent("getOrderProducts found products.",
					EventType.DEBUG, THISINSTANCE, result, null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getEntityProductGroups: " + e
							.toString(), EventType.WARN, THISINSTANCE);
			throw new DTIException(AttributeKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getEntityProductGroups", e);
		}

		return result;
	}

	/**
	 * Returns the products in product groups that the entity can sell.
	 * 
	 * @param entityTO
	 *            The Entity Transfer Object.
	 * @param dbProdList
	 *            the database product list
	 * @param typeCode
	 *            the typeCode to narrow down the search with
	 * @return the array list of product ID's that the entity can sell.
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<BigInteger> getEntityProductGroups(
			EntityTO entityTO, ArrayList<DBProductTO> dbProdList,
			String typeCode) throws DTIException {

		logger.sendEvent("Entering getEntityProductGroups() with upgrade",
				EventType.DEBUG, THISINSTANCE);

		ArrayList<BigInteger> result = new ArrayList<BigInteger>();

		// Retrieve and validate the parameters
		if ((entityTO == null) || (dbProdList == null) || (typeCode == null)) {
			throw new DTIException(EntityKey.class,
					DTIErrorCode.INVALID_ENTITY,
					"Insufficient parameters to execute getEntityProductGroups with upgrade.");
		}

		// Create a set of unique product code strings
		HashSet<BigInteger> pdtIdSet = new HashSet<BigInteger>();
		for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {
			if (aDBProduct.getPdtid() != null) pdtIdSet.add(aDBProduct
					.getPdtid());
		}

		// Ensure the product list is not empty. (as of 2.10)
		if (pdtIdSet.size() == 0) {
			logger.sendEvent(
					"getEntityProductGroups passed no valid products in dbProdList.",
					EventType.EXCEPTION, THISINSTANCE);
			throw new DTIException(AttributeKey.class,
					DTIErrorCode.INVALID_PRODUCT_CODE,
					"Exception executing getEntityProductGroups");
		}

		// Upgrade logic: on the From product, we can come from a "sellable" group, or
		// a "upgrade-only" group
		if (typeCode.equals(ValueConstants.TYPE_CODE_SELL)) return getEntityProductGroups(
				entityTO, dbProdList);

		Object[] queryParms = { DBUtil.createSQLInList(pdtIdSet) };

		// Replaces "?"
		Long entityIdLong = Long.valueOf(entityTO.getEntityId());
		Object[] values = { entityIdLong };

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ENTITY_PDT_GRP_UPGRD);

			// Run the SQL
			logger.sendEvent(
					"About to processQuery:  GET_ENTITY_PDT_GRP_UPGRD",
					EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<BigInteger>) helper.processQuery(values,
					queryParms, qBuilder);

			// Debug
			logger.sendEvent("getOrderProducts with upgrade found products.",
					EventType.DEBUG, THISINSTANCE, result, null);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getEntityProductGroups with upgrade: " + e
							.toString(), EventType.WARN, THISINSTANCE);
			throw new DTIException(AttributeKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getEntityProductGroups with upgrade",
					e);
		}

		return result;

	}

	/**
	 * Returns an entity receipt text when provided the entityId.
	 * 
	 * @param entityId
	 *            The ticket seller entity
	 * @return String the entity receipt text
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	public static final String getEntityReceipt(Long entityId) throws DTIException {

		logger.sendEvent("Entering getEntityReceipt()", EventType.DEBUG,
				THISINSTANCE);

		String result = null;

		// Retrieve and validate the parameters
		if (entityId == null) {
			throw new DTIException(EntityKey.class,
					DTIErrorCode.INVALID_ENTITY,
					"Insufficient parameters to execute getEntityReceipt.");
		}

		try {
			// Retrieve and validate the parameters
			Object[] values = { entityId };

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_ENTITY_RECEIPT);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_ENTITY_RECEIPT",
					EventType.DEBUG, THISINSTANCE);
			result = (String) helper.processQuery(values);

		}
		catch (Exception e) {
			logger.sendEvent(
					"Exception executing getEntityReceipt: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(EntityKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getEntityReceipt", e);
		}

		if (result != null) {
			logger.sendEvent(
					"getEntityReceipt found message:  " + result.toString(),
					EventType.DEBUG, THISINSTANCE, result, null);
		}

		return result;
	}

}
