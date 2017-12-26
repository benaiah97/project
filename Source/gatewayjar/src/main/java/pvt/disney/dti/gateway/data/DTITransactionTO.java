package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import pvt.disney.dti.gateway.data.common.AttributeTO;
import pvt.disney.dti.gateway.data.common.CosGrpTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.PaymentLookupTO;
import pvt.disney.dti.gateway.data.common.TPLookupTO;
import pvt.disney.dti.gateway.data.dlr.GWTPLookupTO;

// TODO: Auto-generated Javadoc
/**
 * Class that represents all of the data associated with a DTI transaction as it passes through the system.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class DTITransactionTO implements Serializable {

  /** Standard serial version UID. */
  final static long serialVersionUID = 9129231995L;

  /** The enumeration of possible transaction types in DTI. */
  public enum TransactionType {
    
    /** The queryticket. */
    QUERYTICKET,
    
    /** The upgradealpha. */
    UPGRADEALPHA,
    
    /** The voidticket. */
    VOIDTICKET,
    
    /** The reservation. */
    RESERVATION,
    
    /** The createticket. */
    CREATETICKET,
    
    /** The updateticket. */
    UPDATETICKET,
    
    /** The updatetransaction. */
    UPDATETRANSACTION,
    
    /** The queryreservation. */
    QUERYRESERVATION,
    
    /** The upgradeentitlement. */
    UPGRADEENTITLEMENT, 
 /** The renewentitlement. */
 // 2.10
    RENEWENTITLEMENT, 
 /** The associatemediatoaccount. */
 // As of 2.16.1, JTL
    ASSOCIATEMEDIATOACCOUNT, 
 /** The tickerateentitlement. */
 // 2.16.1 BIEST001
    TICKERATEENTITLEMENT, 
 /** The voidreservation. */
 // 2.16.1 BIEST001
    VOIDRESERVATION, 
 /** The queryeligproducts. */
 // 2.16.3, JTL
    QUERYELIGPRODUCTS, 
 /** The undefined. */
 // 2.17.3 RASTA006, refactored moons012 to match ENTITY_COMMAND.COMMAND_CODE in database
    UNDEFINED
  };

  /** The enumeration of ticket providers. */
  public enum ProviderType {
    
    /** The wdwnexus. */
    WDWNEXUS,
    
    /** The dlrgateway. */
    DLRGATEWAY,
    
    /** The hkdnexus. */
    HKDNEXUS,  
  /** The undefined. */
  // As of 2.16.2 (JTL)
    UNDEFINED
  };

  /** The enumeration of possible deployment environments. */
  public enum EnvironmentType {
    
    /** The production. */
    PRODUCTION,
    
    /** The test. */
    TEST,
    
    /** The undefined. */
    UNDEFINED
  }

  /** The TPI Code for WDW. */
  public static final String TPI_CODE_WDW = "NEX01";

  /** The TPI Code for DLR. */
  public static final String TPI_CODE_DLR = "DLR01";
  
  /** The TPI Code for HKD. (As of 2.16.2, JTL) */
  public static final String TPI_CODE_HKD = "HKD01";

  /** The type of price mismatch. */
  private boolean priceMismatch;

  /** The type of transaction this represents. */
  private TransactionType txnType = TransactionType.UNDEFINED;

  /** The provider this transaction is intended for. */
  private ProviderType provider = ProviderType.UNDEFINED;

  /** The environment this transaction is intended for. */
  private EnvironmentType environment = EnvironmentType.UNDEFINED;

  /** The TPI Code of this transaction. */
  private String tpiCode;

  /** The request object associated with this transaction. */
  private DTIRequestTO request = null;

  /** The response object associated with this transaction. */
  private DTIResponseTO response = null;

  /** The ticket provider reference number assigned to this transaction by DTI. */
  private Integer tpRefNum;

  /** The primary database table key for the transaction in Inbound_TS_LOG. */
  private Integer transIdITS;

  /** The primary database table key for the transaction in Inbound_TP_LOG. */
  private Integer transIdITP;

  /** The primary database table key for the transaction in Outbound_TP_LOG. */
  private Integer transIdOTP;

  /** The primary database table key for the transaction in Outbound_TS_LOG. */
  private Integer transIdOTS;

  /** Boolean indicating if the out-bound ticket provider log was saved. */
  // JTL: Since all log files indexes are chosen up front to support
  // the index logging table, another indicator is required to ensure
  // that the OTP log was saved. If not, a FK constraint occurs in the database.
  boolean loggedOTP = false;

  /** The object representing the entity of the ticket seller. */
  private EntityTO entityTO;

  /** The array of attributes associated with this entity. */
  private HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeTOMap;

  /** The array of payment information associated with this entity. */
  private ArrayList<PaymentLookupTO> paymentLookupTOList;

  /**
   * The array of ticket provider lookup items associated with this request. (optional)
   */
  private ArrayList<TPLookupTO> tpLookupTOList;

  /**
   * The array of gw ticket provider lookup items associated with this request. (optional)
   */
  private ArrayList<GWTPLookupTO> gwTpLookupTOList;

  /**
   * The read only HashMap of ticket provider lookup items associated with this request.
   */
  private HashMap<TPLookupTO.TPLookupType, TPLookupTO> tpLookupTOMap;

  /**
   * The read only HashMap of gw ticket provider lookup items associated with this request.
   */
  private HashMap<String, String> paymentCardMap = new HashMap<String, String>();

  /** The identity of the ticket broker processing this transaction. */
  private String tktBroker;

  /** Determines when this object was created. */
  private Date createTime = new Date();

  /** List of products on the DB request. */
  private ArrayList<DBProductTO> dbProdList;

  /**  Map of products on the DB request, by DB Code. */
  private HashMap<String, DBProductTO> dbProdMap;
  
  /**  Is this transaction a "rework"?. */
  private boolean isRework = false;
  
  /** The class of service TO. */
  private CosGrpTO cosGrpTO;

  /**
   * Gets the payment card map.
   *
   * @return the payment card map
   */
  public HashMap<String, String> getPaymentCardMap() {
    return paymentCardMap;
  }

  /**
   * Gets the db prod list.
   *
   * @return the dbProdList
   */
  public ArrayList<DBProductTO> getDbProdList() {
    return dbProdList;
  }

  /**
   * Sets the dbProdList and the dbProdMap.
   *
   * @param dbProdListIn the new db prod list
   */
  public void setDbProdList(ArrayList<DBProductTO> dbProdListIn) {
    dbProdList = dbProdListIn;

    dbProdMap = new HashMap<String, DBProductTO>();

    for /* each */(DBProductTO aProductTO : /* in */dbProdList) {
      String pdtCode = aProductTO.getPdtCode();
      dbProdMap.put(pdtCode, aProductTO);
    }

    return;
  }

  /**
   * Gets the creates the time.
   *
   * @return the createTime
   */
  public Date getCreateTime() {
    return createTime;
  }

  /**
   * Gets the tkt broker.
   *
   * @return the tktBroker
   */
  public String getTktBroker() {
    return tktBroker;
  }

  /**
   * Sets the tkt broker.
   *
   * @param tktBroker            the tktBroker to set
   */
  public void setTktBroker(String tktBroker) {
    this.tktBroker = tktBroker;
  }

  /**
   * Instantiates a new DTI transaction TO.
   *
   * @param typeIn the type in
   */
  public DTITransactionTO(TransactionType typeIn) {
    txnType = typeIn;
  }

  /**
   * Renders this object as a string for easy logging.
   *
   * @return the string
   */
  public String toString() {
    StringBuffer output = new StringBuffer();
    output.append("DTITransactionTO: [" + txnType.toString() + "]\n");

    if (request != null) {
      output.append(request.toString());
    }

    if (response != null) {
      output.append(response.toString());
    }

    return output.toString();
  }

  /**
   * Gets the request.
   *
   * @return the request
   */
  public DTIRequestTO getRequest() {
    return request;
  }

  /**
   * Sets the request.
   *
   * @param request            the request to set
   */
  public void setRequest(DTIRequestTO request) {
    this.request = request;
  }

  /**
   * Gets the response.
   *
   * @return the response
   */
  public DTIResponseTO getResponse() {
    return response;
  }

  /**
   * Sets the response.
   *
   * @param response            the response to set
   */
  public void setResponse(DTIResponseTO response) {
    this.response = response;
  }

  /**
   * Gets the transaction type.
   *
   * @return the commandType
   */
  public TransactionType getTransactionType() {
    return txnType;
  }

  /**
   * Gets the tp ref num.
   *
   * @return the tpRefNum
   */
  public Integer getTpRefNum() {
    return tpRefNum;
  }

  /**
   * Gets the trans id ITP.
   *
   * @return the transIdITP
   */
  public Integer getTransIdITP() {
    return transIdITP;
  }

  /**
   * Gets the trans id ITS.
   *
   * @return the transIdITS
   */
  public Integer getTransIdITS() {
    return transIdITS;
  }

  /**
   * Gets the trans id OTP.
   *
   * @return the transIdOTP
   */
  public Integer getTransIdOTP() {
    return transIdOTP;
  }

  /**
   * Sets the tp ref num.
   *
   * @param tpRefNum            the tpRefNum to set
   */
  public void setTpRefNum(Integer tpRefNum) {
    this.tpRefNum = tpRefNum;
  }

  /**
   * Sets the trans id ITP.
   *
   * @param transIdITP            the transIdITP to set
   */
  public void setTransIdITP(Integer transIdITP) {
    this.transIdITP = transIdITP;
  }

  /**
   * Sets the trans id ITS.
   *
   * @param transIdITS            the transIdITS to set
   */
  public void setTransIdITS(Integer transIdITS) {
    this.transIdITS = transIdITS;
  }

  /**
   * Sets the trans id OTP.
   *
   * @param transIdOTP            the transIdOTP to set
   */
  public void setTransIdOTP(Integer transIdOTP) {
    this.transIdOTP = transIdOTP;
  }

  /**
   * Gets the provider.
   *
   * @return the provider
   */
  public ProviderType getProvider() {
    return provider;
  }

  /**
   * Sets the provider enumeration and the "tpiCode" used as a reference in the database as the string "name" of the provider. i.e. NEX01 or DLR01
   * 
   * @param provider
   *            the provider to set
   */
  public void setProvider(ProviderType provider) {
    this.provider = provider;
    if (provider == ProviderType.DLRGATEWAY) { 
      this.tpiCode = DTITransactionTO.TPI_CODE_DLR;
    } else if (provider == ProviderType.WDWNEXUS) {
      this.tpiCode = DTITransactionTO.TPI_CODE_WDW;
    } else if (provider == ProviderType.HKDNEXUS) {
      this.tpiCode = DTITransactionTO.TPI_CODE_HKD;
    }
  }

  /**
   * Gets the environment.
   *
   * @return the environment
   */
  public EnvironmentType getEnvironment() {
    return environment;
  }

  /**
   * Sets the environment.
   *
   * @param environment            the environment to set
   */
  public void setEnvironment(EnvironmentType environment) {
    this.environment = environment;
  }

  /**
   * Gets the entity TO.
   *
   * @return the entityTO
   */
  public EntityTO getEntityTO() {
    return entityTO;
  }

  /**
   * Sets the entity TO.
   *
   * @param entityTO            the entityTO to set
   */
  public void setEntityTO(EntityTO entityTO) {
    this.entityTO = entityTO;
  }

  /**
   * Gets the attribute TO map.
   *
   * @return the attribute TO map
   */
  public HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> getAttributeTOMap() {
    return attributeTOMap;
  }

  /**
   * Sets the attribute TO map.
   *
   * @param attributeTOMap the attribute TO map
   */
  public void setAttributeTOMap(
      HashMap<AttributeTO.CmdAttrCodeType, AttributeTO> attributeTOMap) {
    this.attributeTOMap = attributeTOMap;
  }

  /**
   * Gets the payment lookup TO list.
   *
   * @return the paymentLookupTOList
   */
  public ArrayList<PaymentLookupTO> getPaymentLookupTOList() {
    return paymentLookupTOList;
  }

  /**
   * Sets the payment lookup TO list.
   *
   * @param paymentLookupTOList            the paymentLookupTOList to set
   */
  public void setPaymentLookupTOList(
      ArrayList<PaymentLookupTO> paymentLookupTOList) {
    this.paymentLookupTOList = paymentLookupTOList;
  }

  /**
   * Gets the tpi code.
   *
   * @return the tpiCode
   */
  public String getTpiCode() {
    return tpiCode;
  }

  /**
   * Gets the tp lookup TO list.
   *
   * @return the tpLookupTOList
   */
  public ArrayList<TPLookupTO> getTpLookupTOList() {
    return tpLookupTOList;
  }

  /**
   * Gets the gw tp lookup TO list.
   *
   * @return the gwTpLookupTOList
   */
  public ArrayList<GWTPLookupTO> getGwTpLookupTOList() {
    return gwTpLookupTOList;
  }

  /**
   * Sets the tp lookup TO list.
   *
   * @param tpLookupTOListIn the new tp lookup TO list
   */
  public void setTpLookupTOList(ArrayList<TPLookupTO> tpLookupTOListIn) {
    this.tpLookupTOList = tpLookupTOListIn;

    tpLookupTOMap = new HashMap<TPLookupTO.TPLookupType, TPLookupTO>();

    for /* each */(TPLookupTO aTPLookup : /* in */tpLookupTOList) {
      TPLookupTO.TPLookupType type = aTPLookup.getLookupType();

      // Break out look-ups that have a different cardinality then one type to
      // one value.
      if (type == TPLookupTO.TPLookupType.CC_TYPE) {
        paymentCardMap.put(aTPLookup.getLookupValue(), aTPLookup.getLookupDesc());
      }
      else {
        tpLookupTOMap.put(type, aTPLookup);
      }
    }
    return;
  }

  /**
   * Gets the db prod map.
   *
   * @return the HashMap of DBProductTO's.
   */
  public HashMap<String, DBProductTO> getDbProdMap() {
    return dbProdMap;
  }

  /**
   * Gets the tp lookup TO map.
   *
   * @return the HashMap of TPLookupTO's.
   */
  public HashMap<TPLookupTO.TPLookupType, TPLookupTO> getTpLookupTOMap() {
    return tpLookupTOMap;
  }

  /**
   * Checks if is price mismatch.
   *
   * @return if the transaction is a price mismatch transaction
   */
  public boolean isPriceMismatch() {
    return priceMismatch;
  }

  /**
   * Sets if the transaction is price mismatch or not.
   * 
   * @param priceMismatch
   *            true or false value
   */
  public void setPriceMismatch(boolean priceMismatch) {
    this.priceMismatch = priceMismatch;
  }

  /**
   * Gets the trans id OTS.
   *
   * @return the trans id OTS
   */
  public Integer getTransIdOTS() {
    return transIdOTS;
  }

  /**
   * Sets the trans id OTS.
   *
   * @param transIdOTS the new trans id OTS
   */
  public void setTransIdOTS(Integer transIdOTS) {
    this.transIdOTS = transIdOTS;
  }

  /**
   * Checks if is logged OTP.
   *
   * @return true, if is logged OTP
   */
  public boolean isLoggedOTP() {
    return loggedOTP;
  }

  /**
   * Sets the logged OTP.
   *
   * @param loggedOTP the new logged OTP
   */
  public void setLoggedOTP(boolean loggedOTP) {
    this.loggedOTP = loggedOTP;
  }

  /**
   * Checks if is rework.
   *
   * @return the isRework
   */
  public boolean isRework() {
    return isRework;
  }

  /**
   * Sets the rework.
   *
   * @param isRework the isRework to set
   */
  public void setRework(boolean isRework) {
    this.isRework = isRework;
  }

	/**
	 * Gets the cos grp TO.
	 *
	 * @return the cos grp TO
	 */
	public CosGrpTO getCosGrpTO() {
		return cosGrpTO;
	}

	/**
	 * Sets the cos grp TO.
	 *
	 * @param cosGrpTO the new cos grp TO
	 */
	public void setCosGrpTO(CosGrpTO cosGrpTO) {
		this.cosGrpTO = cosGrpTO;
	}

}
