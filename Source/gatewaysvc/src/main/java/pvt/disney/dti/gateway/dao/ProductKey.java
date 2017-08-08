package pvt.disney.dti.gateway.dao;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.constants.ValueConstants;
import pvt.disney.dti.gateway.dao.data.DBQueryBuilder;
import pvt.disney.dti.gateway.dao.data.DBUtil;
import pvt.disney.dti.gateway.dao.data.UpgradeCatalogTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.UpgrdPathSeqTO;
import com.disney.logging.EventLogger;
import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.EventType;

/**
 * This DAO class is responsible for product database lookups such as for
 * products, ticket types, shell types, and the product shell cross reference.
 * 
 * @author lewit019
 * 
 */
public class ProductKey {

	/** Object instance used for logging. */
	private static final ProductKey THISINSTANCE = new ProductKey();

 
	/** Event logger. */
	private static final EventLogger logger = EventLogger
			.getLogger(ProductKey.class.getCanonicalName());


	/** Constant representing the get products query. */
	private static final String GET_PRODUCTS = "GET_PRODUCTS";

  
	/** Constant representing the get product ticket type query. */
	private static final String GET_PDT_TKT_TYPE = "GET_PDT_TKT_TYPE";

	/** Constant representing the get product shell type query. */
	private static final String GET_PDT_SHELL_TYPE = "GET_PDT_SHELL_TYPE";

	/** Constant representing the get product shell cross reference query. */
	private static final String GET_PDT_SHELL_XREF = "GET_PDT_SHELL_XREF";

	/** Constant representing the insert product detail query. */
	private static final String INS_PDT_DTL = "INS_PDT_DTL";

	/** Constant representing the get product receipt query. */
	private static final String GET_PDT_RECEIPT = "GET_PDT_RECEIPT";

	/** Constant representing the get ticket to product query. */
	private static final String GET_TKT_TO_PRODUCT = "GET_TKT_TO_PRODUCT";
  

	/** Constant representing the get product from ticket number query . */
	private static final String GET_PRD_FROM_TKTNBR = "GET_PRD_FROM_TKTNBR";

	/** Constant representing the get AP upgrade product products query by Name. */
	private static final String GET_PRODUCTS_FROM_NAME = "GET_PRODUCTS_FROM_NAME";

	/** Constant representing the get AP upgrade product products query by NBR */
	private static final String GET_PRODUCTS_FROM_NBR = "GET_PRODUCTS_FROM_NBR";
	
	 /** Constant representing the get product catalog query. */
	  private static final String GET_UPGRD_PRD_CATALOG = "GET_UPGRD_PRD_CATALOG";
	  
	  /** Constant indicating the Annual Pass day class. */
	  private static final String AP_DAY_SUBCLASS = "AP";

	/**
	 * Constant representing the get AP upgrade product products query by
	 * TypeCode
	 */
	private static final String GET_PRODUCTS_FROM_TYPCODE = "GET_PRODUCTS_FROM_TYPCODE";

	 /** Constant indicating the Day sub class. */
	  private static final String GET_DAY_SUB_CLASS = "GET_DAY_SUB_CLASS";

	/**
	 * Returns an array of products found on the order.
	 * 
	 * @param tktListTO
	 *            The ticket list from the order
	 * @return an array of products found on the order.
	 * @throws DTIException
	 *             in the case of a DB or DAO error.
	 */

	public static final ArrayList<DBProductTO> getOrderProducts(
			ArrayList<TicketTO> tktListTO) throws DTIException {

		return getOrderProducts(tktListTO, ValueConstants.TYPE_CODE_SELL);

	}

	/**
	 * Gets the order products from the database.
	 * 
	 * @param tktListTO
	 * @param typeCode
	 * @return
	 * @throws DTIException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<DBProductTO> getOrderProducts(
			ArrayList<TicketTO> tktListTO, String typeCode) throws DTIException {

		logger.sendEvent("Entering getOrderProducts()", EventType.DEBUG,
				THISINSTANCE);

		ArrayList<DBProductTO> result = null;

		// Retrieve and validate the parameters
		if ((tktListTO == null) || (tktListTO.size() == 0)) {
			throw new DTIException(ProductKey.class,
					DTIErrorCode.INVALID_PRODUCT_CODE,
					"getOrderProducts DB routine found an empty ticket list.");
		}

		// Create a set of unique product code strings
		HashSet<String> productCodeSet = new HashSet<String>();
		for /* each */(TicketTO aTicketTO : /* in */tktListTO) {

			if (typeCode.equals(ValueConstants.TYPE_CODE_SELL)) {
				if (aTicketTO.getProdCode() != null) {
					productCodeSet.add(aTicketTO.getProdCode());
				}
			}
			if (typeCode.equals(ValueConstants.TYPE_CODE_UPGRADE)) {
				if (aTicketTO.getFromProdCode() != null) {
					productCodeSet.add(aTicketTO.getFromProdCode());
				}
			}
		}

		Object[] queryParms = { DBUtil.createSQLInList(productCodeSet) };

		// Replaces "?"
		Object[] values = {};

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PRODUCTS);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PRODUCTS",
					EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<DBProductTO>) helper.processQuery(values,
					queryParms, qBuilder);

			// Debug
			logger.sendEvent("getOrderProducts found products.",
					EventType.DEBUG, THISINSTANCE, result, null);

		} catch (Exception e) {
			logger.sendEvent(
					"Exception executing getOrderProducts: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getOrderProducts", e);
		}

		// Add quantity of products to results
		if ((result != null) && (result.size() > 0)) {

			for /* each */(DBProductTO aProduct : /* in */result) {

				String matchProduct = aProduct.getPdtCode();
				int quantity = 0;

				for /* each */(TicketTO aTicket : /* in */tktListTO) {

					if (aTicket.getProdCode() != null) {

						if (aTicket.getProdCode().compareToIgnoreCase(
								matchProduct) == 0) {
							if (aTicket.getProdQty() == null) { // 2.10, implied
																// quantity is 1
																// on upgrade
								quantity = quantity + 1;
							} else {
								quantity = aTicket.getProdQty().intValue()
										+ quantity;
							}
						}

					}

				} // tktListTO loop
					// Make sure there are no un-printable characters; must
					// match the xml
					// encoding of UTF-8
				aProduct.setQuantity(BigInteger.valueOf(quantity));
				try {
					String utf8 = new String(aProduct.getPdtDesc().getBytes(
							"UTF-8"));
					aProduct.setPdtDesc(utf8);
				} catch (UnsupportedEncodingException e) {
					logger.sendException(EventType.EXCEPTION,
							ErrorCode.APPLICATION_EXCEPTION, e, THISINSTANCE);
				}

			} // result loop

		}

		return result;

	}

	/**
	 * Returns the mapping of products to tickets.
	 * 
	 * @param dbProdList
	 *            list of products on the order
	 * @return the mapping of products to tickets in the provider systems
	 * @throws DTIException
	 *             in the event of a DAO or DB error.
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<DBProductTO> getProductTicketTypes(
			ArrayList<DBProductTO> dbProdList) throws DTIException {

		logger.sendEvent("Entering getProductTicketTypes()", EventType.DEBUG,
				THISINSTANCE);

		ArrayList<DBProductTO> resultList = null;

		// Retrieve and validate the parameters
		if ((dbProdList == null) || (dbProdList.size() == 0)) {
			throw new DTIException(ProductKey.class,
					DTIErrorCode.INVALID_PRODUCT_CODE,
					"getProductTicketTypes DB routine found an empty db product list.");
		}

		// Create a set of unique product code strings
		HashSet<String> productIdSet = new HashSet<String>();
		for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {
			if (aDBProduct.getPdtid() != null)
				productIdSet.add(aDBProduct.getPdtid().toString());
		}

		Object[] queryParms = { DBUtil.createSQLInList(productIdSet) };

		// Replaces "?"
		Object[] values = {};

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PDT_TKT_TYPE);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PDT_TKT_TYPE",
					EventType.DEBUG, THISINSTANCE);
			resultList = (ArrayList<DBProductTO>) helper.processQuery(values,
					queryParms, qBuilder);

			// Debug
			logger.sendEvent("getOrderProducts found products.",
					EventType.DEBUG, THISINSTANCE, resultList, null);

		} catch (Exception e) {
			logger.sendEvent(
					"Exception executing getProductTicketTypes: "
							+ e.toString(), EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getProductTicketTypes", e);
		}

		// Enrich the existing DB Product List with the ticket type information.
		// It is more efficient to run the product query separate from the tkt
		// map
		// query
		HashMap<BigInteger, DBProductTO> pdtTktIdMapList = new HashMap<BigInteger, DBProductTO>();
		for /* each */(DBProductTO aPdtTktMap : /* in */resultList) {
			pdtTktIdMapList.put(aPdtTktMap.getPdtid(), aPdtTktMap);
		}

		for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {

			DBProductTO aPdtTktMap = pdtTktIdMapList.get(aDBProduct.getPdtid());
			if (pdtTktIdMapList.get(aDBProduct.getPdtid()) != null) {

				aDBProduct.setMappedProviderTktActive(aPdtTktMap
						.isMappedProviderTktActive());
				aDBProduct.setMappedProviderTktNbr(aPdtTktMap
						.getMappedProviderTktNbr());
				aDBProduct.setMappedProviderTktName(aPdtTktMap
						.getMappedProviderTktName());

			}
		}

		return dbProdList;
	}

	/**
	 * Returns the active shells in the database, based on the those provided on
	 * the order.
	 * 
	 * @param shellSet
	 *            the set of ticket shells on the order.
	 * @return an arraylist of active shells.
	 * @throws DTIException
	 *             for a DAO or DB failure.
	 */
	@SuppressWarnings("unchecked")
	public static final ArrayList<Integer> getActiveShells(
			HashSet<Integer> shellSet) throws DTIException {

		logger.sendEvent("Entering getActiveShells()", EventType.DEBUG,
				THISINSTANCE);

		ArrayList<Integer> result = null;

		// Retrieve and validate the parameters
		if ((shellSet == null) || (shellSet.size() == 0)) {
			throw new DTIException(ProductKey.class,
					DTIErrorCode.INVALID_PRODUCT_CODE,
					"getActiveShells DB routine found an empty ticket list.");
		}

		Object[] queryParms = { DBUtil.createSQLInList(shellSet) };

		// Replaces "?"
		Object[] values = {};

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PDT_SHELL_TYPE);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PDT_SHELL_TYPE",
					EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<Integer>) helper.processQuery(values,
					queryParms, qBuilder);

			// Debug
			logger.sendEvent("getActiveShells found products.",
					EventType.DEBUG, THISINSTANCE, result, null);

		} catch (Exception e) {
			logger.sendEvent(
					"Exception executing getActiveShells: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getActiveShells", e);
		}

		return result;
	}

	/**
	 * Returns the product to ticket shell cross reference.
	 * 
	 * @param dbProdList
	 *            the products on the order
	 * @return the product to ticket shell cross reference.
	 * @throws DTIException
	 *             on a DAO or DB failure.
	 */
	@SuppressWarnings("unchecked")
	public static final HashMap<String, ArrayList<Integer>> getProductShellXref(
			ArrayList<DBProductTO> dbProdList) throws DTIException {

		logger.sendEvent("Entering getProductShellXref()", EventType.DEBUG,
				THISINSTANCE);

		HashMap<String, ArrayList<Integer>> result = null;

		// Retrieve and validate the parameters
		if ((dbProdList == null) || (dbProdList.size() == 0)) {
			throw new DTIException(AttributeKey.class,
					DTIErrorCode.INVALID_PRODUCT_CODE,
					"getProductShellXref DB routine found an empty db product list.");
		}

		// Create a set of unique product code strings
		HashSet<String> productIdSet = new HashSet<String>();
		for /* each */(DBProductTO aDBProduct : /* in */dbProdList) {
			if (aDBProduct.getPdtid() != null)
				productIdSet.add(aDBProduct.getPdtid().toString());
		}

		Object[] queryParms = { DBUtil.createSQLInList(productIdSet) };

		// Replaces "?"
		Object[] values = {};

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PDT_SHELL_XREF);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PDT_SHELL_XREF",
					EventType.DEBUG, THISINSTANCE);
			result = (HashMap<String, ArrayList<Integer>>) helper.processQuery(
					values, queryParms, qBuilder);

			// Debug
			logger.sendEvent("getProductShellXref found products.",
					EventType.DEBUG, THISINSTANCE, result, null);

		} catch (Exception e) {
			logger.sendEvent(
					"Exception executing getProductShellXref: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getProductShellXref", e);
		}

		return result;
	}

	/**
	 * Inserts the product detail into the product detail table.
	 * 
	 * @param dtiTxn
	 *            The DTI Transaction Object
	 * @throws DTIException
	 *             on any DB or DAO failure.
	 */
	public static final void insertProductDetail(DTITransactionTO dtiTxn)
			throws DTIException {

		logger.sendEvent("Entering insertProductDetail()", EventType.DEBUG,
				THISINSTANCE);

		ArrayList<DBProductTO> prodList = dtiTxn.getDbProdList();

		if ((prodList == null) || (prodList.size() == 0)) {
			return;
		}

		/*
		 * In the old gateway, although code was present to log reservation and
		 * create ticket information in the product detail table, it never
		 * functioned because of a comparison that was always false. When an
		 * attempt to fix this was done in the new gateway, any quantity over 9
		 * failed because the PRODUCT_DETAIL_LOG table's quantity field is
		 * limited to a type of "number(1)".
		 * 
		 * Therefore, the reservation command was dropped (and for the same
		 * reason create ticket) because of their incompatibility with the
		 * quantity limitation.
		 */
		if ((dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.CREATETICKET)
				|| (dtiTxn.getTransactionType() == DTITransactionTO.TransactionType.RESERVATION)) {
			return;
		}

		int counter = 0;

		try {
			// Loop through the product list for insert
			// Note: DAO can't handle BigInteger and BigDecimal
			for /* each */(DBProductTO aProduct : /* in */prodList) {

				// PRODUCT_CODE
				String productCode = aProduct.getPdtCode();

				// PRODUCT_DESC
				String productDesc = aProduct.getPdtDesc();

				// PRODUCT_QTY
				BigInteger productQtyBI = aProduct.getQuantity();
				Long productQty = productQtyBI.longValue();

				// UNIT_PRICE
				// Although Unit Price is indicated by column name,
				// PrintedPrice is what's used throughout the DTI gateway.
				// Per Craig Stuart, 09/25/2009
				BigDecimal unitPriceBD = aProduct.getPrintedPrice();
				Double unitPrice = unitPriceBD.doubleValue();

				// TAX1
				BigDecimal tax1BD = aProduct.getTax1();
				Double tax1 = tax1BD.doubleValue();

				// TAX2
				BigDecimal tax2BD = aProduct.getTax2();
				Double tax2 = tax2BD.doubleValue();

				// INBOUND_TP_ID
				Integer inboundTpId = dtiTxn.getTransIdITP();

				Object[] values = { productCode, productDesc, productQty,
						unitPrice, tax1, tax2, inboundTpId };
				DAOHelper helper = DAOHelper.getInstance(INS_PDT_DTL);
				counter += helper.processInsert(values);
			}
			logger.sendEvent("INS_PDT_DTL inserted " + counter + " rows.",
					EventType.DEBUG, THISINSTANCE);

		} catch (Exception e) {
			logger.sendEvent(
					"Exception executing insertProductDetail: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing insertProductDetail", e);
		}

		return;
	}

	/**
	 * Returns the product receipt information when provided with a product
	 * code.
	 * 
	 * @param pdtCode
	 *            the product code
	 * @return String the entity receipt text
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	public static final String getProductReceipt(String pdtCode)
			throws DTIException {

		logger.sendEvent("Entering getProductReceipt()", EventType.DEBUG,
				THISINSTANCE);

		String result = null;

		// Retrieve and validate the parameters
		if (pdtCode == null) {
			throw new DTIException(ProductKey.class,
					DTIErrorCode.INVALID_ENTITY,
					"Insufficient parameters to execute getProductReceipt.");
		}

		try {
			// Retrieve and validate the parameters
			Object[] values = { pdtCode };

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PDT_RECEIPT);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PDT_RECEIPT",
					EventType.DEBUG, THISINSTANCE);
			result = (String) helper.processQuery(values);

		} catch (Exception e) {
			logger.sendEvent(
					"Exception executing getProductReceipt: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getProductReceipt", e);
		}

		if (result != null) {
			logger.sendEvent(
					"getProductReceipt found message:  " + result.toString(),
					EventType.DEBUG, THISINSTANCE, result, null);
		}

		return result;
	}

	/**
	 * Returns the product receipt information when provided with a product
	 * code.
	 * 
	 * @param pdtCode
	 *            the product code
	 * @return String the entity receipt text
	 * @throws DTIException
	 *             on a DB or DAO problem.
	 */
	public static final String getProductFromTicketType(
			String providerTicketType) throws DTIException {

		logger.sendEvent("Entering getProductFromTicketType()",
				EventType.DEBUG, THISINSTANCE);

		String result = null;

		// Retrieve and validate the parameters
		if (providerTicketType == null) {
			throw new DTIException(ProductKey.class,
					DTIErrorCode.MISSING_TICKET_TYPE,
					"Insufficient parameters to execute getProductFromTicketType.");
		}

		try {
			// Retrieve and validate the parameters
			Object[] values = { providerTicketType };

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_TKT_TO_PRODUCT);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_TKT_TO_PRODUCT",
					EventType.DEBUG, THISINSTANCE);
			result = (String) helper.processQuery(values);

		} catch (Exception e) {
			logger.sendEvent("Exception executing getProductFromTicketType: "
					+ e.toString(), EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getProductFromTicketType", e);
		}

		if (result != null) {
			logger.sendEvent("getProductFromTicketType found product:  "
					+ result, EventType.DEBUG, THISINSTANCE, result, null);
		}

		return result;
	}

	/**
	 * Gets the order products from the database.
	 * 
	 * @param tktListTO
	 * @param typeCode
	 * @return
	 * @throws DTIException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<DBProductTO> getProductCodeFromTktNbr(
			BigInteger tktNbr) throws DTIException {

		logger.sendEvent("Entering getProductCodeFromTktNbr()",
				EventType.DEBUG, THISINSTANCE);

		ArrayList<DBProductTO> result = null;

		// Retrieve and validate the parameters
		if ((tktNbr == null) || (tktNbr.intValue() == 0)) {
			throw new DTIException(ProductKey.class,
					DTIErrorCode.INVALID_PRODUCT_CODE,
					"getOrderProducts DB routine found an empty ticket list.");
		}
  

		try {
			// Retrieve and validate the parameters
			Long aLongNumber = Long.valueOf(tktNbr.longValue());
			Object[] values = { aLongNumber };

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PRD_FROM_TKTNBR);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PRD_FROM_TKTNBR",
					EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<DBProductTO>) helper.processQuery(values);

		} catch (Exception e) {
			logger.sendEvent("Exception executing getProductCodeFromTktNbr: "
					+ e.toString(), EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getProductCodeFromTktNbr", e);
		}

		if (result != null) {
			logger.sendEvent("getProductCodeFromTktNbr found product:  "
					+ result, EventType.DEBUG, THISINSTANCE, result, null);
		}

		return result;

	}

	/**
	 * Gets the AP upgrade products from tktName from the database.
	 * 
	 * @param tktName
	 * @return
	 * @throws DTIException
	 */
	@SuppressWarnings("unchecked")
	public static DBProductTO getProductsByTktName(ArrayList<String> tktName) throws DTIException {

		DBProductTO result = null;

		logger.sendEvent("Entering getProductsByTktName()", EventType.DEBUG, THISINSTANCE);

		// Retrieve and validate the parameters
		if (tktName == null || tktName.isEmpty()) {
			throw new DTIException(ProductKey.class, DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
						"getProductByTktName DB routine is found missing parameters");
		}

		// Create a set of unique product code strings
		HashSet<String> tktNameSet = new HashSet<String>();
		for /* each */(String tktNameString : /* in */tktName) {

			tktNameSet.add(tktNameString);
		}

		Object[] queryParms = { DBUtil.createSQLInList(tktNameSet) };
		

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {

			// Set tktName (PLU Number) as a parameter for query
			Object[] values = {};

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper", EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PRODUCTS_FROM_NAME);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PRODUCTS_FROM_NAME", EventType.DEBUG, THISINSTANCE);
			ArrayList<DBProductTO> resultSet = (ArrayList<DBProductTO>) helper.processQuery(values,
						queryParms, qBuilder);
			if (resultSet != null) {
				logger.sendEvent("getProductsByTktName() found " + resultSet.size() + " products", EventType.INFO,
							THISINSTANCE);
				// taking the first product if multiple are found as per the
				// requirements
				result = resultSet.get(0);
			}

		} catch (Exception e) {
			logger.sendEvent("Exception executing getProductsByTktName: " + e.toString(), EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class, DTIErrorCode.FAILED_DB_OPERATION_SVC,
						"Exception executing getProductsByTktName", e);
		}

		if (result != null) {
			logger.sendEvent("getProductsByTktName() picking up Product with " + result.getPdtCode() + " product code",
						EventType.INFO, THISINSTANCE);
		}

		return result;
	}

	/**
	 * Gets the AP upgrade products from TktNbr from the database.
	 * 
	 * @param tktNbr
	 * @return
	 * @throws DTIException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<DBProductTO> getProductsfromTktNbr(
			ArrayList<BigInteger> tktNbr) throws DTIException {

		logger.sendEvent("Entering getProductsTktNbr()", EventType.DEBUG,
				THISINSTANCE);

		ArrayList<DBProductTO> result = null;

		// Retrieve and validate the parameters
		if ((tktNbr == null)) {
			throw new DTIException("getProductsTktNbr tktNbr is null.");
		}
		// Create a set of unique product code strings
		HashSet<BigInteger> tktNbrSet = new HashSet<BigInteger>();
		for /* each */(BigInteger tktNbrString : /* in */tktNbr) {

			tktNbrSet.add(tktNbrString);
		}

		Object[] queryParms = { DBUtil.createSQLInList(tktNbrSet) };
		Object[] values = {};

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PRODUCTS_FROM_NBR);

			// Run the SQL
			logger.sendEvent("About to processQuery:  GET_PRODUCTS_FROM_NBR",
					EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<DBProductTO>) helper.processQuery(values,
					queryParms, qBuilder);

			// Debug
			logger.sendEvent("getProductsTktNbr found products.",
					EventType.DEBUG, THISINSTANCE, result, null);

		} catch (Exception e) {
			logger.sendEvent(
					"Exception executing getProductsTktNbr: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getProductsTktNbr", e);
		}

		return result;
	}

	
	/**Gets the AP upgrade products from TYPE CODE from the database.
	 * @param upgrdTypCode
	 * @return
	 * @throws DTIException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<DBProductTO> getProductsForSeller(
			ArrayList<String> upgrdTypCode,Date startSaleDate) throws DTIException {

		logger.sendEvent("Entering getProductsForSeller()", EventType.DEBUG,
				THISINSTANCE);

		ArrayList<DBProductTO> result = null;

		// Retrieve and validate the parameters
		if ((upgrdTypCode == null)) {
			throw new DTIException("getProductsForSeller upgrdTypCode is null.");
		}
		// Create a set of unique product code strings
		HashSet<String> upgrdTypSet = new HashSet<String>();
		for /* each */(String upgrdCodeString : /* in */upgrdTypCode) {
			upgrdTypSet.add(upgrdCodeString);
		}

		Object[] queryParms = { DBUtil.createSQLInList(upgrdTypSet) };
		// Replaces "?"
		Object[] values = {startSaleDate};

		// Get instance of Query Builder (Replaces "%")
		DBQueryBuilder qBuilder = new DBQueryBuilder();

		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper.getInstance(GET_PRODUCTS_FROM_TYPCODE);

			// Run the SQL
			logger.sendEvent(
					"About to processQuery:  GET_PRODUCTS_FROM_TYPCODE",
					EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<DBProductTO>) helper.processQuery(values,
					queryParms, qBuilder);

			// Debug
			logger.sendEvent("getProductsForSeller found products.",
					EventType.DEBUG, THISINSTANCE, result, null);

		} catch (Exception e) {
			logger.sendEvent(
					"Exception executing getProductsForSeller: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getProductsForSeller", e);
		}
		return result;
	}

	
	/**Gets the AP upgrade products from the database.
	 * @return
	 * @throws DTIException
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<UpgrdPathSeqTO> getSubClassesForPathId(Integer pathId)
			throws DTIException {

		logger.sendEvent("Entering getSubClassesForAp()", EventType.DEBUG,
				THISINSTANCE);

		ArrayList<UpgrdPathSeqTO> result = null;

		Object[] values = {pathId};
		try {

			// Prepare query
			logger.sendEvent("About to getInstance from DAOHelper",
					EventType.DEBUG, THISINSTANCE);
			DAOHelper helper = DAOHelper
					.getInstance(GET_DAY_SUB_CLASS);

			// Run the SQL
			logger.sendEvent(
					"About to processQuery:  GET_DAY_SUB_CLASS",
					EventType.DEBUG, THISINSTANCE);
			result = (ArrayList<UpgrdPathSeqTO>) helper.processQuery(values);

			// Debug
			logger.sendEvent("getSubClassesForAp found products.",
					EventType.DEBUG, THISINSTANCE, result, null);

		} catch (Exception e) {
			logger.sendEvent(
					"Exception executing getSubClassesForAp: " + e.toString(),
					EventType.WARN, THISINSTANCE);
			throw new DTIException(ProductKey.class,
					DTIErrorCode.FAILED_DB_OPERATION_SVC,
					"Exception executing getSubClassesForAp", e);
		}
		return result;
	}
	
	/**
	   * GET_PRD_CATALOG_ARGUMENTS=1 (ENTITYID) + 1 (DAY_CLASS) +  1 (TPS_CODE) AP_DAY_SUBCLASS
	   * @param entityTO
	   * @param tpsCode
	   * @return
	   * @throws DTIException
	   */
	  public static UpgradeCatalogTO getAPUpgradeCatalog(EntityTO entityTO, String tpsCode) throws DTIException {
	     
	     UpgradeCatalogTO result = new UpgradeCatalogTO(); 

	     logger.sendEvent("Entering getAPUpgradeCatalog()", EventType.DEBUG, THISINSTANCE);

	     // Retrieve and validate the parameters
	     if ((entityTO == null) || (tpsCode == null)) {
	       throw new DTIException(ProductKey.class, DTIErrorCode.UNDEFINED_CRITICAL_ERROR,
	           "getAPUpgradeCatalog DB routine found missing parameters.");
	     }

	     try {
	       // Retrieve and validate the parameters
	       // 1 (ENTITYID) + 1 (DAY_CLASS) +  1 (TPS_CODE) 
	       Long entityIdLong = Long.valueOf(entityTO.getEntityId());
	       Object[] values = { entityIdLong, AP_DAY_SUBCLASS, tpsCode};

	       // Prepare query
	       logger.sendEvent("About to getInstance from DAOHelper", EventType.DEBUG, THISINSTANCE);
	       DAOHelper helper = DAOHelper.getInstance(GET_UPGRD_PRD_CATALOG);

	       // Run the SQL
	       logger.sendEvent("About to processQuery:  GET_UPGRD_PRD_CATALOG", EventType.DEBUG, THISINSTANCE);
	       @SuppressWarnings("unchecked")
	       ArrayList<DBProductTO> resultSet = (ArrayList<DBProductTO>) helper.processQuery(values);
	       if (resultSet != null) {
	          result.setProductList(resultSet);
	       }

	     } catch (Exception e) {
	       logger.sendEvent("Exception executing getAPUpgradeCatalog: " + e.toString(), EventType.WARN, THISINSTANCE);
	       throw new DTIException(ProductKey.class, DTIErrorCode.FAILED_DB_OPERATION_SVC,
	           "Exception executing getAPUpgradeCatalog", e);
	     }

	     if (result != null) {
	       logger.sendEvent("getAPUpgradeCatalog found " + result.getProductListCount() + " products.", EventType.DEBUG, THISINSTANCE, result,
	           null);
	     }

	     return result;
	 
	  }  
	  
	}
