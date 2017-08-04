package pvt.disney.dti.gateway.test.util;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import mockit.Mock;
import mockit.MockUp;

import org.apache.xerces.parsers.DOMParser;
import org.easymock.EasyMock;
import org.powermock.api.easymock.PowerMock;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pvt.disney.dti.gateway.connection.DAOHelper;
import pvt.disney.dti.gateway.connection.QueryBuilder;
import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIErrorCode.ErrorScope;
import pvt.disney.dti.gateway.dao.AttributeKey;
import pvt.disney.dti.gateway.dao.EligibilityKey;
import pvt.disney.dti.gateway.dao.EntityKey;
import pvt.disney.dti.gateway.dao.LookupKey;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.dao.TransidRescodeKey;
import pvt.disney.dti.gateway.dao.data.DBTicketAttributes;
import pvt.disney.dti.gateway.dao.result.AttributeResult;
import pvt.disney.dti.gateway.dao.result.MultipleSeqNumResult;
import pvt.disney.dti.gateway.dao.result.ProductDetailResult;
import pvt.disney.dti.gateway.dao.result.ProductIdListResult;
import pvt.disney.dti.gateway.dao.result.ProductTktTypeResult;
import pvt.disney.dti.gateway.dao.result.ShellTypeResult;
import pvt.disney.dti.gateway.dao.result.TicketAttributeResult;
import pvt.disney.dti.gateway.dao.result.TransidRescodeResult;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TransidRescodeTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTUpgradeTicketTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTPaymentTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketTO;
import pvt.disney.dti.gateway.rules.race.utility.WordCipher;

import com.disney.exception.WrappedException;
import com.disney.util.Loader;

/**
 * Utility class for Mocked methods.
 *
 * @author rasta006
 */
public class DTIMockUtil extends CommonTestUtils {

	Text to make compilation fail for CICD TESTING!
		
	/** The attribute rs. */
	static ResultSet attributeRs = null;

	/** The attribute rs1. */
	static ResultSet attributeRs1 = null;

	/** The attribute rs2. */
	static ResultSet attributeRs2 = null;

	/** The attribute rs3. */
	static ResultSet attributeRs3 = null;

	/** The rs. */
	static ResultSet rs = null;

	/** The processor. */
	static ResultSetProcessor theProcessor = null;

	/** The prod list. */
	public static ArrayList<DBProductTO> prodList = new ArrayList<DBProductTO>();

	/** the TicketList. */
	public static OTTicketTO ticket = getOTicket();

	/** The mocking. */
	public static boolean mocking = false;

	/** The mock parse. */
	static boolean mockParse = false;
	private static String errorScope=null; 

	/**
	 * For Mocking AttributeKey.
	 */
	public static void mockEntAttribute() {
		try {
			init();
			new MockUp<AttributeKey>() {
				@SuppressWarnings("unchecked")
				@Mock
				public HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> getEntAttribtues(
						DTITransactionTO dtiTxn, String tpiCode, long entityId,
						String actor) {
					HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeMap = null;
					theProcessor = new AttributeResult();
					try {
						theProcessor.processNextResultSet(attributeRs);
						theProcessor.processNextResultSet(attributeRs1);
						theProcessor.processNextResultSet(attributeRs2);
						attributeMap = (HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>) theProcessor
								.getProcessedObject();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return attributeMap;
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * For Mocking DAOHelper processQuery.
	 */
	public static void mockTicketAttribute() {
		try {
			init();
			new MockUp<DAOHelper>() {
				@Mock
				protected Object processQuery(Object[] values) {
					DBTicketAttributes dbTicketAttributes = null;
					theProcessor = new TicketAttributeResult();
					try {
						theProcessor.processNextResultSet(rs);
						dbTicketAttributes = (DBTicketAttributes) theProcessor
								.getProcessedObject();
					} catch (Exception e) {
					}
					return dbTicketAttributes;
				}
			};
		} catch (Exception e) {
		}
	}

	/**
	 * Inits the.
	 *
	 * @throws Exception
	 *             the exception
	 */
	private static void init() throws Exception {
		rs = PowerMock.createMock(ResultSet.class);
		attributeRs = PowerMock.createMock(ResultSet.class);
		attributeRs1 = PowerMock.createMock(ResultSet.class);
		attributeRs2 = PowerMock.createMock(ResultSet.class);
		attributeRs3 = PowerMock.createMock(ResultSet.class);
		setResultSet(rs);
		setAttributeRS();
		EasyMock.replay(rs);
		EasyMock.replay(attributeRs);
		EasyMock.replay(attributeRs1);
		EasyMock.replay(attributeRs2);
		EasyMock.replay(attributeRs3);
	}

	/**
	 * Sets the attribute rs.
	 *
	 * @throws Exception
	 *             the exception
	 */
	private static void setAttributeRS() throws Exception {
		setAttributeResultSet(attributeRs, 0);
		setAttributeResultSet(attributeRs1, 1);
		setAttributeResultSet(attributeRs2, 2);
		setAttributeResultSet(attributeRs3, 3);
	}

	/**
	 * Sets the result set.
	 *
	 * @param rs
	 *            the new result set
	 * @throws Exception
	 *             the exception
	 */
	private static void setResultSet(ResultSet rs) throws Exception {
		EasyMock.expect(rs.getString(EasyMock.contains("ACTIVE_IND")))
				.andReturn("T").anyTimes();
		EasyMock.expect(rs.getString(EasyMock.contains("SOLD_OUT")))
				.andReturn(null).anyTimes();
		EasyMock.expect(rs.getString(EasyMock.anyObject(String.class)))
				.andReturn("1").anyTimes();
		EasyMock.expect(rs.getLong(EasyMock.anyObject(String.class)))
				.andReturn(1L).anyTimes();
		EasyMock.expect(rs.getDouble(EasyMock.anyObject(String.class)))
				.andReturn(1.0).anyTimes();
		EasyMock.expect(rs.getInt(EasyMock.anyObject(String.class)))
				.andReturn(1).anyTimes();
		EasyMock.expect(rs.getTimestamp(EasyMock.anyObject(String.class)))
				.andReturn(new Timestamp(System.currentTimeMillis()))
				.anyTimes();
		EasyMock.expect(rs.getDate(EasyMock.anyObject(String.class)))
				.andReturn(new Date(System.currentTimeMillis())).anyTimes();
		EasyMock.expect(rs.getBoolean(EasyMock.anyObject(String.class)))
				.andReturn(true).anyTimes();
		EasyMock.expect(rs.getBigDecimal(EasyMock.anyObject(String.class)))
				.andReturn(new BigDecimal("1.0")).anyTimes();
	}

	/**
	 * Sets the attribute result set.
	 *
	 * @param rs
	 *            the rs
	 * @param i
	 *            the i
	 * @throws Exception
	 *             the exception
	 */
	private static void setAttributeResultSet(ResultSet rs, int i)
			throws Exception {
		EasyMock.expect(rs.getString("ATTR_VALUE")).andReturn("11111")
				.anyTimes();
		EasyMock.expect(rs.getString("ACTOR")).andReturn("MGR").anyTimes();
		if (i == 0) {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("OpArea")
					.anyTimes();
		} else if (i == 1) {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("User")
					.anyTimes();
		} else if (i == 2) {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE"))
					.andReturn("SellerResPrefix").anyTimes();
		} else {
			EasyMock.expect(rs.getString("CMD_ATTR_CODE")).andReturn("Pass")
					.anyTimes();
		}
		EasyMock.expect(rs.getString("ACTIVE_IND")).andReturn("T").anyTimes();
		EasyMock.expect(rs.getString("CMD_CODE")).andReturn("QueryReservation")
				.anyTimes();
	}

	/**
	 * For Mocking DAOHelper processInsert.
	 */
	public static void processMockInsert() {
		new MockUp<DAOHelper>() {
			@Mock
			public int processInsert(Object[] inputValues) {
				return 1;
			}
		};
	}

	/**
	 * Process mocking.
	 */
	public static void processMocking() {
		mocking = true;
	}

	/**
	 * For Mocking DAOHelper prepareAndExecuteSql.
	 */
	@SuppressWarnings("unused")
	public static void processMockprepareAndExecuteSql() {

		new MockUp<DAOHelper>() {
			@Mock
			protected int prepareAndExecuteSql(Object[] inputValues,
					String sql, boolean query, ResultSetProcessor theProcessor) {

				Object obj = new Object();
				try {
					init();
					if (theProcessor != null) {
						if (theProcessor instanceof MultipleSeqNumResult) {
							return 1;
						}
						theProcessor.processNextResultSet(rs);
						// obj =
						// (Object)theProcessor.getProcessedObject();
						if (theProcessor instanceof TransidRescodeResult) {
							ArrayList<TransidRescodeTO> list = new ArrayList<TransidRescodeTO>();
							TransidRescodeTO transCode = new TransidRescodeTO();
							transCode
									.setCreationDate((GregorianCalendar) GregorianCalendar
											.getInstance());
							transCode.setRescode("1");
							transCode.setTsTransid("1");
							list.add(transCode);
							obj = list;
						}
					}
				} catch (Exception e) {
				}

				mocking = true;
				return 1;
			}
		};

	}

	/**
	 * For Mocking DAOHelper processQuery for mockResultProcessor.
	 *
	 * @param resultSetProcessor
	 *            the result set processor
	 */
	public static void mockResultProcessor(String resultSetProcessor) {

		try {
			Object obj = null;
			obj = Loader.loadClass(resultSetProcessor).newInstance();
			if (resultSetProcessor != null) {
				if (obj instanceof ResultSetProcessor) {
					theProcessor = (ResultSetProcessor) obj;
				} else {
					fail("given String does not belong to ResultSetProcessor");
				}
				init();
			}

			new MockUp<DAOHelper>() {
				@Mock
				protected Object processQuery(Object[] values) throws Exception {
					Object obj = new Object();
					{
						try {
							theProcessor.processNextResultSet(rs);
							obj = (Object) theProcessor.getProcessedObject();
							if (theProcessor instanceof TransidRescodeResult) {
								ArrayList<TransidRescodeTO> list = new ArrayList<TransidRescodeTO>();
								TransidRescodeTO transCode = new TransidRescodeTO();
								transCode
										.setCreationDate((GregorianCalendar) GregorianCalendar
												.getInstance());
								transCode.setRescode("1");
								transCode.setTsTransid("1");
								list.add(transCode);
								obj = list;
							}
						} catch (Exception e) {
						}
					}

					return obj;
				}
			};

		} catch (Exception e) {
			fail("Not able to create object for given String "
					+ resultSetProcessor);
		}

	}

	/**
	 * Mock result processor.
	 *
	 * @param resultSetProcessor
	 *            the result set processor
	 * @param inputValues
	 *            the input values
	 * @param queryParameters
	 *            the query parameters
	 * @param theQueryBuilder
	 *            the the query builder
	 */
	public static void mockResultProcessor(String resultSetProcessor,
			Object[] inputValues, Object[] queryParameters,
			QueryBuilder theQueryBuilder) {
		try {
			Object obj = null;
			obj = Loader.loadClass(resultSetProcessor).newInstance();
			if (obj instanceof ResultSetProcessor) {
				theProcessor = (ResultSetProcessor) obj;
			} else {
				fail("given String does not belong to ResultSetProcessor");
			}
			init();
			new MockUp<DAOHelper>() {
				@Mock
				protected Object processQuery(Object[] inputValues,
						Object[] queryParameters, QueryBuilder theQueryBuilder)
						throws WrappedException {
					Object obj = null;
					try {
						theProcessor.processNextResultSet(rs);
						obj = theProcessor.getProcessedObject();
						if (theProcessor instanceof TransidRescodeResult) {
							ArrayList<TransidRescodeTO> list = new ArrayList<TransidRescodeTO>();
							TransidRescodeTO transCode = new TransidRescodeTO();
							transCode
									.setCreationDate((GregorianCalendar) GregorianCalendar
											.getInstance());
							transCode.setRescode("1");
							transCode.setTsTransid("1");
							list.add(transCode);
							obj = list;
						}
					} catch (Exception e) {
						throw new WrappedException("" + e);
					}
					return obj;
				}
			};
		} catch (Exception e) {
			fail("Not able to create object for given String "
					+ resultSetProcessor);
		}
	}

	/**
	 * Mock null result processor.
	 *
	 * @param resultSetProcessor
	 *            the result set processor
	 */
	public static void mockNullResultProcessor(String resultSetProcessor) {
		try {
			new MockUp<DAOHelper>() {
				@Mock
				protected Object processQuery(Object[] values) throws Exception {
					try {
					} catch (Exception e) {
					}
					return null;
				}
			};
		} catch (Exception e) {
		}
	}

	/**
	 * Mock null result processor.
	 *
	 * @param resultSetProcessor
	 *            the result set processor
	 */
	public static void mockExceptionResultProcessor(String resultSetProcessor) {
		try {
			new MockUp<DAOHelper>() {
				@Mock
				protected Object processQuery(Object[] values) throws Exception {
					throw new Exception("");
					// return null;
				}
			};
		} catch (Exception e) {
		}
	}

	/**
	 * For Mocking PaymentKey getPaymentLookup.
	 */
	public static void mockPaymentLookUp() {
		processMockprepareAndExecuteSql();
	}

	/**
	 * For Mocking DBProductTO getOrderProducts.
	 */
	public static void mockGetOrderProduct() {
		try {
			new MockUp<ProductKey>() {
				@SuppressWarnings("unchecked")
				@Mock
				protected ArrayList<DBProductTO> getOrderProducts(
						ArrayList<TicketTO> tktListTO) {
					ArrayList<DBProductTO> dbProduct = null;
					theProcessor = new ProductDetailResult();
					try {
						theProcessor.processNextResultSet(rs);
						dbProduct = (ArrayList<DBProductTO>) theProcessor
								.getProcessedObject();

					} catch (Exception e) {
						e.printStackTrace();
					}
					return dbProduct;
				}
			};
		} catch (Exception e) {

		}

	}

	/**
	 * Method for getting the DBProduct List.
	 *
	 * @return the array list
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<DBProductTO> fetchDBOrderList() {
		ArrayList<DBProductTO> dbProduct = null;
		try {
			init();
			theProcessor = new ProductDetailResult();
			theProcessor.processNextResultSet(rs);
			dbProduct = (ArrayList<DBProductTO>) theProcessor
					.getProcessedObject();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbProduct;
	}

	/**
	 * Method for getting the DBProduct List.
	 *
	 * @return the hash map
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> fetchAttributeTOMapList() {
		HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeMap = null;

		try {
			init();
			theProcessor = new AttributeResult();
			theProcessor.processNextResultSet(attributeRs);
			theProcessor.processNextResultSet(attributeRs1);
			theProcessor.processNextResultSet(attributeRs2);
			theProcessor.processNextResultSet(attributeRs3);
			attributeMap = (HashMap<AttributeTO.CmdAttrCodeType, AttributeTO>) theProcessor
					.getProcessedObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attributeMap;

	}

	/**
	 * For Mocking DBProductTO getOrderProducts.
	 */
	public static void mockGetOrderProductWithParam() {
		processMockprepareAndExecuteSql();
	}

	/**
	 * For Mocking EntityKey getEntityProducts.
	 */
	public static void mockGetEntityProducts() {
		processMockprepareAndExecuteSql();
	}

	/**
	 * For Mocking EntityKey getEntityProducts.
	 */
	public static void mockGetEntityProductsWithParam() {
		processMockprepareAndExecuteSql();
	}

	/**
	 * For Mocking EntityKey getEntityProductGroups.
	 */
	public static void mockGetEntityProductGroups() {
		processMockprepareAndExecuteSql();
	}

	/**
	 * For Mocking EntityKey getEntityProductGroups.
	 */
	public static void mockGetEntityProductGroupsthreeParam() {
		try {
			new MockUp<EntityKey>() {
				@SuppressWarnings("unchecked")
				@Mock
				protected ArrayList<BigInteger> getEntityProductGroups(
						EntityTO entityTO, ArrayList<DBProductTO> dbProdList,
						String typeCode) {
					ArrayList<BigInteger> entityProd = null;
					theProcessor = new ProductIdListResult();
					try {
						theProcessor.processNextResultSet(rs);
						entityProd = (ArrayList<BigInteger>) theProcessor
								.getProcessedObject();

					} catch (Exception e) {
					}
					return entityProd;
				}
			};

		} catch (Exception e) {
		}
	}

	/**
	 * For Mocking EligibilityKey getOrderEligibility.
	 */
	public static void mockGetOrderEligibility() {
		processMockprepareAndExecuteSql();
	}

	/**
	 * For Mocking ProductKey getProductTicketTypes.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public static void mockGetProductTicketTypes() {
		try {
			new MockUp<ProductKey>() {
				@Mock
				protected ArrayList<DBProductTO> getProductTicketTypes(
						ArrayList<DBProductTO> dbProdList) {
					ArrayList<DBProductTO> dbProd = null;
					theProcessor = new ProductTktTypeResult();
					try {
						theProcessor.processNextResultSet(rs);
						dbProd = (ArrayList<DBProductTO>) theProcessor
								.getProcessedObject();
						for (DBProductTO prodDb : dbProdList) {
							prodDb.setMappedProviderTktName("1");
							prodDb.setMappedProviderTktActive(true);
							prodDb.setDayClass("SHIP");
						}
					} catch (Exception e) {
					}
					prodList = dbProdList;
					return dbProdList;
				}
			};
		} catch (Exception e) {
		}
	}

	/**
	 * For Mocking ProductKey getActiveShells.
	 */
	public static void mockGetActiveShells() {
		try {
			new MockUp<ProductKey>() {
				@SuppressWarnings("unchecked")
				@Mock
				protected ArrayList<Integer> getActiveShells(
						HashSet<Integer> shellSet) {
					ArrayList<Integer> dbProd = null;
					theProcessor = new ShellTypeResult();
					try {
						theProcessor.processNextResultSet(rs);
						dbProd = (ArrayList<Integer>) theProcessor
								.getProcessedObject();
					} catch (Exception e) {
					}
					return dbProd;
				}
			};

		} catch (Exception e) {
		}
	}

	/**
	 * For Mocking TPLookupTO getTPCommandLookup.
	 */
	public static void mockGetTPCommandLookup() {
		try {
			new MockUp<LookupKey>() {
				@Mock
				protected ArrayList<TPLookupTO> getTPCommandLookup(
						String tpiCode,
						DTITransactionTO.TransactionType txnType,
						String language, String clientType,
						String resPickupArea, String resSalesType) {
					ArrayList<TPLookupTO> dbProd = new ArrayList<TPLookupTO>();
					for (int i = 0; i < 7; i++) {
						TPLookupTO tpLookupTO = new TPLookupTO();
						if (i % 2 == 0) {
							tpLookupTO
									.setLookupType(TPLookupTO.TPLookupType.MAX_LIMIT);
							tpLookupTO.setLookupValue("1");
						} else if (i % 3 == 0) {
							tpLookupTO
									.setLookupType(TPLookupTO.TPLookupType.CLIENT_TYPE);
							tpLookupTO.setLookupValue("2");
						} else {
							tpLookupTO
									.setLookupType(TPLookupTO.TPLookupType.LANGUAGE);
							tpLookupTO.setLookupValue("3");
						}
						tpLookupTO.setCmdCode("" + i);
						tpLookupTO.setCmdId(new BigInteger("" + i));
						dbProd.add(tpLookupTO);
					}
					return dbProd;
				}
			};
		} catch (Exception e) {
		}
	}

	/**
	 * For Mocking TPLookupTO getGWTPCommandLookup.
	 */
	public static void mockGetGWTPCommandLookup() {
		try {
			new MockUp<LookupKey>() {
				@Mock
				protected ArrayList<TPLookupTO> getGWTPCommandLookup(
						String tpiCode,
						DTITransactionTO.TransactionType txnType,
						String shipMethod, String shipDetail) {
					ArrayList<TPLookupTO> dbProd = new ArrayList<TPLookupTO>();
					for (int i = 0; i < 7; i++) {
						TPLookupTO tpLookupTO = new TPLookupTO();
						if (i % 2 == 0) {
							tpLookupTO
									.setLookupType(TPLookupTO.TPLookupType.MAX_LIMIT);
							tpLookupTO.setLookupValue("1");
						} else if (i % 3 == 0) {
							tpLookupTO
									.setLookupType(TPLookupTO.TPLookupType.CLIENT_TYPE);
							tpLookupTO.setLookupValue("2");
						} else {
							tpLookupTO
									.setLookupType(TPLookupTO.TPLookupType.LANGUAGE);
							tpLookupTO.setLookupValue("3");
						}
						tpLookupTO.setCmdCode("" + i);
						tpLookupTO.setCmdId(new BigInteger("" + i));
						dbProd.add(tpLookupTO);
					}
					return dbProd;
				}
			};
		} catch (Exception e) {
		}
	}

	/**
	 * For Mocking Cipher.
	 */
	public static void mockGetWordCollection() {
		try {
			new MockUp<WordCipher>() {
				@Mock
				protected Collection<String> getWordCollection(String fileName) {
					List<String> decodedWordCollection = new ArrayList<String>();
					String str1 = "ABCD";
					String str2 = "EFG";
					decodedWordCollection.add(str1);
					decodedWordCollection.add(str2);
					return decodedWordCollection;
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * For Mocking the getTransidRescodeFromDB , value obtained from.
	 */
	public static void mockGetTransidRescodeFromDB() {
		try {
			new MockUp<TransidRescodeKey>() {
				@Mock
				protected ArrayList<TransidRescodeTO> getTransidRescodeFromDB(
						String transid) {
					ArrayList<TransidRescodeTO> list = new ArrayList<TransidRescodeTO>();
					TransidRescodeTO transCode = new TransidRescodeTO();
					transCode
							.setCreationDate((GregorianCalendar) GregorianCalendar
									.getInstance());
					transCode.setRescode("1");
					transCode.setTsTransid("1");
					list.add(transCode);
					return list;
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * For Mocking File exists.
	 */
	public static void mockFileExists() {
		new MockUp<File>() {
			@Mock
			public boolean exists() {
				return true;
			}
		};
	}

	/**
	 * For Mocking ElectronicEntitlementKey insertVoidedEntitlement.
	 */
	public static void mockInsertVoidedEntitlement() {
		processMockprepareAndExecuteSql();
	}

	/**
	 * For Mocking ElectronicEntitlementKey insertUpgradedEntitlement.
	 */
	public static void mockInsertUpgradedEntitlement() {
		processMockprepareAndExecuteSql();
	}

	/**
	 * Fetch db ticket type list.
	 *
	 * @return the array list
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<DBProductTO> fetchDBTicketTypeList() {
		ArrayList<DBProductTO> dbProduct = null;
		try {
			init();
			dbProduct = DTIMockUtil.fetchDBOrderList();
			theProcessor = new ProductTktTypeResult();
			theProcessor.processNextResultSet(rs);
			ArrayList<DBProductTO> dbProductTemp = (ArrayList<DBProductTO>) theProcessor
					.getProcessedObject();
			for (DBProductTO dbProductTo : dbProductTemp) {
				for (DBProductTO dbProductFinal : dbProduct) {
					dbProductFinal.setMappedProviderTktNbr(dbProductTo
							.getMappedProviderTktNbr());
					dbProductFinal.setMappedProviderTktName(dbProductTo
							.getMappedProviderTktName());
					dbProductFinal.setValidityDateInfoRequired(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbProduct;
	}

	/**
	 * Mocking getPaymentInfoList.
	 */
	public static void mockGetPaymentInfoList() {
		new MockUp<OTUpgradeTicketTO>() {
			@Mock
			public ArrayList<OTPaymentTO> getPaymentInfoList() {
				ArrayList<OTPaymentTO> otPaymentList = new ArrayList<OTPaymentTO>();
				OTPaymentTO oTpayment = new OTPaymentTO();
				otPaymentList.add(oTpayment);
				return otPaymentList;
			}
		};
	}

	/**
	 * For mocking the client parser.
	 *
	 * @param mock
	 *            the mock
	 */
	public static void mockParseProcess(boolean mock) {

		if (mock) {
			mockParse = false;
			new MockUp<DocumentBuilder>() {
				@Mock
				public Document parse(InputStream is) throws SAXException,
						IOException, Exception {
					Document doc = null;
					DOMParser domParser = new DOMParser();
					if (is == null) {
						throw new IllegalArgumentException(
								"InputStream cannot be null");
					}
					URL url = this.getClass().getResource(
							"/xml/iagoTestSuccess.xml");
					File file = new File(url.toURI());
					InputStream inStream = new FileInputStream(file);
					InputSource in = new InputSource(inStream);

					domParser.parse(in);
					doc = domParser.getDocument();
					domParser.dropDocumentReferences();
					return doc;
				}
			};

		}
	}

	/**
	 * Mock get product code from tkt nbr.
	 */
	public static void mockGetProductCodeFromTktNbr() {
		try {
			new MockUp<ProductKey>() {
				@Mock
				protected ArrayList<DBProductTO> getProductCodeFromTktNbr(
						BigInteger itemNumCode) throws Exception {
					ArrayList<DBProductTO> dbProdArray = null;
					try {
						dbProdArray = new ArrayList<DBProductTO>();
						DBProductTO dbProductTO = new DBProductTO();
						dbProductTO.setPdtCode("abc");
						dbProdArray.add(dbProductTO);

					} catch (Exception e) {
					}
					return dbProdArray;
				}
			};
		} catch (Exception e) {
		}
	}

	/**
	 * For Mocking TPLookupTO getTPCommandLookup.
	 */
	public static void mockGetTPCommandLookupForException() {
		try {
			new MockUp<LookupKey>() {
				@Mock
				protected ArrayList<TPLookupTO> getTPCommandLookup(
						String tpiCode,
						DTITransactionTO.TransactionType txnType,
						String language, String clientType,
						String resPickupArea, String resSalesType) {
					ArrayList<TPLookupTO> dbProd = new ArrayList<TPLookupTO>();
					for (int i = 0; i < 6; i++) {
						TPLookupTO tpLookupTO = new TPLookupTO();
						if (i % 2 == 0) {
							tpLookupTO
									.setLookupType(TPLookupTO.TPLookupType.MAX_LIMIT);
							tpLookupTO.setLookupValue("1");
						} else if (i % 3 == 0) {
							tpLookupTO
									.setLookupType(TPLookupTO.TPLookupType.CLIENT_TYPE);
							tpLookupTO.setLookupValue("2");
						} else {
							tpLookupTO
									.setLookupType(TPLookupTO.TPLookupType.LANGUAGE);
							tpLookupTO.setLookupValue("3");
						}
						tpLookupTO.setCmdCode("" + i);
						tpLookupTO.setCmdId(new BigInteger("" + i));
						dbProd.add(tpLookupTO);
					}
					return dbProd;
				}
			};
		} catch (Exception e) {
		}
	}

	/**
	 * Mock get eligibility assoc id.
	 */
	public static void mockGetEligibilityAssocId() {
		try {
			new MockUp<EligibilityKey>() {
				@Mock
				protected Integer getEligibilityAssocId(String itemNumCode)
						throws Exception {

					return new Integer(1);
				}
			};
		} catch (Exception e) {
		}
	}
	/**
	 * Mock for method getScopeFromCode of DTIErrorCode
	 */
	public static void mockDtiErrorCodeScope(String errScope) {
		errorScope=errScope;
		try {
			new MockUp<DTIErrorCode>() {
				@Mock
				public DTIErrorCode.ErrorScope getScopeFromCode(
						String dtiErrorCode) {
					return ErrorScope.valueOf(errorScope);
				}
			};
		} catch (Exception e) {
		}
	}

}
