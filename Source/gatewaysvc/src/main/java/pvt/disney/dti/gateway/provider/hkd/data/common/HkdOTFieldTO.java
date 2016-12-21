package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;

/**
 * This class is a transfer object which represents an HKD Omni Ticket field.
 * 
 * @author lewit019
 * @since 2.16.3
 */
public class HkdOTFieldTO implements Serializable {

  /** Standard serial version UID. */
  final static long serialVersionUID = 9129231995L;

  // HKD Reservation Order Client Demographics - FIELD ID

  /** Value for HKD_CLNT_BILL_NAME (1). */
  public static Integer HKD_CLNT_BILL_NAME = 1;

  /** Value for HKD_CLNT_BILL_LASTNAME (2) */
  public static Integer HKD_CLNT_BILL_LASTNAME = 2;

  /** Value for HKD_CLNT_BILL_FIRSTNAME_CH (3). */
  public static Integer HKD_CLNT_BILL_FIRSTNAME_CH = 3;

  /** Value for HKD_CLNT_BILL_FIRSTNAME_ENG (4) */
  public static Integer HKD_CLNT_BILL_FIRSTNAME_ENG = 4;
  
  /** Value for HKD_CLNT_BILL_ADDR1 (5). */
  public static Integer HKD_CLNT_BILL_ADDR1 = 5;

  /** Value for HKD_CLNT_BILL_ADDR2 (6). */
  public static Integer HKD_CLNT_BILL_ADDR2 = 6;

  /** Value for HKD_CLNT_BILL_CITY (7). */
  public static Integer HKD_CLNT_BILL_CITY = 7;

  /** Value for HKD_CLNT_BILL_STATE (8). */
  public static Integer HKD_CLNT_BILL_STATE = 8;

  /** Value for HKD_CLNT_BILL_ZIP (9). */
  public static Integer HKD_CLNT_BILL_ZIP = 9;

  /** Value for HKD_CLNT_BILL_COUNTRY (10). */
  public static Integer HKD_CLNT_BILL_COUNTRY = 10;

  /** Value for HKD_CLNT_BILL_TELEPHONE (11). */
  public static Integer HKD_CLNT_BILL_TELEPHONE = 11;
  
  /** Value for HKD_CLNT_BILL_TELEPHONE (30). */
  public static Integer HKD_CLNT_TRANSACTION_NO = 30;

  /** Value for HKD_CLNT_SHIP_NAME (12). */
  public static Integer HKD_CLNT_SHIP_NAME = 12;

  /** Value for HKD_CLNT_SHIP_LASTNAME (13). */
  public static Integer HKD_CLNT_SHIP_LASTNAME = 13;

  /** Value for HKD_CLNT_SHIP_FIRSTNAME (14). */
  public static Integer HKD_CLNT_SHIP_FIRSTNAME_CH = 14;  
  
  /** Value for HKD_CLNT_SHIP_FIRSTNAME (15). */
  public static Integer HKD_CLNT_SHIP_FIRSTNAME_ENG = 15;

  /** Value for HKD_CLNT_SHIP_ADDR1 (16). */
  public static Integer HKD_CLNT_SHIP_ADDR1 = 16;

  /** Value for HKD_CLNT_SHIP_ADDR2 (17). */
  public static Integer HKD_CLNT_SHIP_ADDR2 = 17;

  /** Value for HKD_CLNT_SHIP_CITY (18). */
  public static Integer HKD_CLNT_SHIP_CITY = 18;

  /** Value for HKD_CLNT_SHIP_STATE (19). */
  public static Integer HKD_CLNT_SHIP_STATE = 19;

  /** Value for HKD_CLNT_SHIP_SFA_REV (20). */
  public static Integer HKD_CLNT_SFA_REV = 20;

  /** Value for HKD_CLNT_SHIP_COUNTRY (21). */
  public static Integer HKD_CLNT_SHIP_COUNTRY = 21;

  /** Value for HKD_CLNT_SHIP_TELEPHONE (22). */
  public static Integer HKD_CLNT_SHIP_TELEPHONE = 22;

  /** Value for HKD_CLNT_SHIP_CREDIT_INFO (23). */
  public static Integer HKD_CLNT_SHIP_CREDIT_INFO = 23;
  
  /** Value for HKD_CLNT_SHIP_SALES (24). */
  public static Integer HKD_CLNT_BILL_EMAIL = 24;
  
  /** Value for HKD_CLNT_SHIP_EMAIL (25). */
  public static Integer HKD_CLNT_SHIP_EMAIL = 25;
  
  /** Value for HKD_CLNT_SHIP_NOTE (26). */
  public static Integer HKD_CLNT_SHIP_NOTE = 26;
  
  /** Value for HKD_CLNT_SHIP_FAX (27) */
  public static Integer HKD_CLNT_SHIP_FAX = 27;
  
  /** Value for HKD_CLNT_SHIP_MEP_TYPE (28) */
  public static Integer HKD_CLNT_SHIP_MEP_TYPE = 28;
  
  // Reservation Order Demographics - FIELD ID

  /** Value for UNTYPED field. */
  public static Integer UNTYPED_FIELD = 0;

  /** Value for RES_BILL_NAME (1). */
  public static Integer RES_BILL_NAME = 1;

  /** Value for RES_BILL_LASTNAME (2) */
  public static Integer RES_BILL_LASTNAME = 2;

  /** Value for RES_BILL_FIRSTNAME (3). */
  public static Integer RES_BILL_FIRSTNAME = 3;

  /** Value for RES_BILL_ADDR1 (4). */
  public static Integer RES_BILL_ADDR1 = 4;

  /** Value for RES_BILL_ADDR2 (5). */
  public static Integer RES_BILL_ADDR2 = 5;

  /** Value for RES_BILL_CITY (6). */
  public static Integer RES_BILL_CITY = 6;

  /** Value for RES_BILL_STATE (7). */
  public static Integer RES_BILL_STATE = 7;

  /** Value for RES_BILL_ZIP (8). */
  public static Integer RES_BILL_ZIP = 8;

  /** Value for RES_BILL_COUNTRY (9). */
  public static Integer RES_BILL_COUNTRY = 9;

  /** Value for RES_BILL_TELEPHONE (10). */
  public static Integer RES_BILL_TELEPHONE = 10;

  /** Value for RES_SHIP_NAME (11). */
  public static Integer RES_SHIP_NAME = 11;

  /** Value for RES_SHIP_LASTNAME (12). */
  public static Integer RES_SHIP_LASTNAME = 12;

  /** Value for RES_SHIP_FIRSTNAME (13). */
  public static Integer RES_SHIP_FIRSTNAME = 13;

  /** Value for RES_SHIP_ADDR1 (14). */
  public static Integer RES_SHIP_ADDR1 = 14;

  /** Value for RES_SHIP_ADDR2 (15). */
  public static Integer RES_SHIP_ADDR2 = 15;

  /** Value for RES_SHIP_CITY (16). */
  public static Integer RES_SHIP_CITY = 16;

  /** Value for RES_SHIP_STATE (17). */
  public static Integer RES_SHIP_STATE = 17;

  /** Value for RES_SHIP_ZIP (18). */
  public static Integer RES_SHIP_ZIP = 18;

  /** Value for RES_SHIP_COUNTRY (19). */
  public static Integer RES_SHIP_COUNTRY = 19;

  /** Value for RES_SHIP_TELEPHONE (20). */
  public static Integer RES_SHIP_TELEPHONE = 20;

  /** Value for RES_SHIP_AGENT (21). */
  public static Integer RES_SHIP_AGENT = 21;

  /** Value for RES_BILL_EMAIL (26). */
  public static Integer RES_BILL_EMAIL = 26;

  /** Value for RES_BILL_SLR_RES_NBR (27) */
  public static Integer RES_BILL_SLR_RES_NBR = 27;

  // Ticket Demographics FIELD IDs!
  // Cannot go to field types because of Nexus defect.
  // 11/3/2015 JTL 2.15

  /** Value for HKD_TKTDEMO_LASTNAME (1) */
  public static Integer HKD_TKTDEMO_LASTNAME = 1;
  
  /** Value for HKD_TKTDEMO_FIRSTNAME (2) */
  public static Integer HKD_TKTDEMO_FIRSTNAME = 2;

  /** Value for HKD_TKTDEMO_GENDER (3) */
  public static Integer HKD_TKTDEMO_GENDER = 3;

  /** Value for HKD_TKTDEMO_EMAIL (4) */
  public static Integer HKD_TKTDEMO_EMAIL = 4;
  
  /** Value for HKD_TKTDEMO_CITY (5) */
  public static Integer HKD_TKTDEMO_CITY = 5;

  /** Value for HKD_TKTDEMO_ADDRESS_ONE (6) */
  public static Integer HKD_TKTDEMO_ADDRESS_ONE = 6;

  /** Value for HKD_TKTDEMO_ADDRESS_TWO (7) */
  public static Integer HKD_TKTDEMO_ADDRESS_TWO = 7;

  /** Value for HKD_TKTDEMO_STATE (8) */
  public static Integer HKD_TKTDEMO_STATE = 8;

  /** Value for HKD_TKTDEMO_ZIP (9) */
  public static Integer HKD_TKTDEMO_ZIP = 9;

  /** Value for HKD_TKTDEMO_DOB_DD (10) */
  public static Integer HKD_TKTDEMO_DOB_DD = 10;
  
  /** Value for HKD_TKTDEMO_DOB_MM (11) */
  public static Integer HKD_TKTDEMO_DOB_MM = 11;

  /** Value for HKD_TKTDEMO_DOB_YYYY (12) */
  public static Integer HKD_TKTDEMO_DOB_YYYY = 12;
  
  /** Value for HKD_TKTDEMO_MOBILEPHONE (13). */
  public static Integer HKD_TKTDEMO_MOBILEPHONE = 13;
  
  /** Value for HKD_TKTDEMO_OKAYFORNEWS (14). */
  public static Integer HKD_TKTDEMO_OKAYFORNEWS = 14;
  
  /** Value for HKD_TKTDEMO_AGREETANDC (15). */
  public static Integer HKD_TKTDEMO_AGREETANDC = 15;
  
  /** Value for HKD_TKTDEMO_PARENTAPPRV (16). */
  public static Integer HKD_TKTDEMO_PARENTAPPRV = 16;

  /** Value for HKD_TKTDEMO_TICNUMBER (17). */
  public static Integer HKD_TKTDEMO_TICNUMBER = 17;

  /** Value for HKD_TKTDEMO_GUESTID (18). */
  public static Integer HKD_TKTDEMO_GUESTID = 18;
  
  /** Value for HKD_TKTDEMO_DELIVDM (19). */
  public static Integer HKD_TKTDEMO_DELIVDM = 19;
 
  /** Value for HKD_TKTDEMO_PRIMRYCNCT (20). */
  public static Integer HKD_TKTDEMO_PRIMRYCNCT = 20;
  
  /** Value for HKD_TKTDEMO_PREFLANG (21). */
  public static Integer HKD_TKTDEMO_PREFLANG = 21;  
  
  /** Value for HKD_TKTDEMO_NEWS (22). */
  public static Integer HKD_TKTDEMO_NEWS = 22;
  
  /** Value for HKD_TKTDEMO_AP_REFERRAL (23). */
  public static Integer HKD_TKTDEMO_AP_REFERRAL = 23;
  
  /** Value for HKD_TKTDEMO_HOMEPHONE (24). */
  public static Integer HKD_TKTDEMO_HOMEPHONE = 24;
  
  /** Value for HKD_TKTDEMO_SELLERREF (25). */
  public static Integer HKD_TKTDEMO_SELLERREF = 25;
  
  /** The field identifier. */
  private Integer fieldIndex;

  /** The field type. */
  private Integer fieldType;

  /** The field value. */
  private String fieldValue;

  /**
   * Constructor for an Omni Ticket Field Transfer Object
   * 
   * @param fieldIdIn
   *            the feild ID, such as item 1, 2, 3, etc.
   * @param fieldTypeIn
   *            the field type, represented by one of the public constants set in this class.
   * @param fieldValueIn
   *            the value of the field.
   */
  public HkdOTFieldTO(Integer fieldIndexIn, Integer fieldTypeIn,
      String fieldValueIn) {
    fieldIndex = fieldIndexIn;
    fieldType = fieldTypeIn;
    fieldValue = fieldValueIn;
  }

  /**
   * Constructor for an Omni Ticket Field Transfer Object - REQUEST ONLY
   * 
   * @param fieldIdIn
   *            the feild ID, such as item 1, 2, 3, etc.
   * @param fieldValueIn
   *            the value of the field.
   */
  public HkdOTFieldTO(Integer fieldIndexIn, String fieldValueIn) {
    fieldIndex = fieldIndexIn;
    fieldType = UNTYPED_FIELD;
    fieldValue = fieldValueIn;
  }

  /**
   * @return the feildType
   */
  public Integer getFeildType() {
    return fieldType;
  }

  /**
   * @return the fieldId
   */
  public Integer getFieldIndex() {
    return fieldIndex;
  }

  /**
   * @return the fieldValue
   */
  public String getFieldValue() {
    return fieldValue;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String dataString = "OTFieldTO:  fieldIndex(" + fieldIndex + "), fieldType(" + fieldType + "), fieldValue(" + fieldValue + ")";
    return dataString;
  }

}
