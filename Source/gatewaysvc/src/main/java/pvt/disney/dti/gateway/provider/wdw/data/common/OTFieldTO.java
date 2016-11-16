package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;

/**
 * This class is a transfer object which represents a WDW Omni Ticket field.
 * 
 * @author lewit019
 * @since 2.16.3
 * 
 */
public class OTFieldTO implements Serializable {

  /** Standard serial version UID. */
  final static long serialVersionUID = 9129231995L;
  
  /** Value for UNTYPED field. */
  public static Integer UNTYPED_FIELD = 0;
  
  // WDW Reservation Order Client Demographics - FIELD ID

  /** Value for WDW_CLNT_BILL_NAME (1). */
  public static Integer WDW_CLNT_BILL_NAME = 1;

  /** Value for WDW_CLNT_BILL_LASTNAME (2) */
  public static Integer WDW_CLNT_BILL_LASTNAME = 2;

  /** Value for WDW_CLNT_BILL_FIRSTNAME (3). */
  public static Integer WDW_CLNT_BILL_FIRSTNAME = 3;

  /** Value for WDW_CLNT_BILL_ADDR1 (4). */
  public static Integer WDW_CLNT_BILL_ADDR1 = 4;

  /** Value for WDW_CLNT_BILL_ADDR2 (5). */
  public static Integer WDW_CLNT_BILL_ADDR2 = 5;

  /** Value for WDW_CLNT_BILL_CITY (6). */
  public static Integer WDW_CLNT_BILL_CITY = 6;

  /** Value for WDW_CLNT_BILL_STATE (7). */
  public static Integer WDW_CLNT_BILL_STATE = 7;

  /** Value for WDW_CLNT_BILL_ZIP (8). */
  public static Integer WDW_CLNT_BILL_ZIP = 8;

  /** Value for WDW_CLNT_BILL_COUNTRY (9). */
  public static Integer WDW_CLNT_BILL_COUNTRY = 9;

  /** Value for WDW_CLNT_BILL_TELEPHONE (10). */
  public static Integer WDW_CLNT_BILL_TELEPHONE = 10;

  /** Value for WDW_CLNT_SHIP_NAME (11). */
  public static Integer WDW_CLNT_SHIP_NAME = 11;

  /** Value for WDW_CLNT_SHIP_LASTNAME (12). */
  public static Integer WDW_CLNT_SHIP_LASTNAME = 12;

  /** Value for WDW_CLNT_SHIP_FIRSTNAME (13). */
  public static Integer WDW_CLNT_SHIP_FIRSTNAME = 13;

  /** Value for WDW_CLNT_SHIP_ADDR1 (14). */
  public static Integer WDW_CLNT_SHIP_ADDR1 = 14;

  /** Value for WDW_CLNT_SHIP_ADDR2 (15). */
  public static Integer WDW_CLNT_SHIP_ADDR2 = 15;

  /** Value for WDW_CLNT_SHIP_CITY (16). */
  public static Integer WDW_CLNT_SHIP_CITY = 16;

  /** Value for WDW_CLNT_SHIP_STATE (17). */
  public static Integer WDW_CLNT_SHIP_STATE = 17;

  /** Value for WDW_CLNT_SHIP_ZIP (18). */
  public static Integer WDW_CLNT_SHIP_ZIP = 18;

  /** Value for WDW_CLNT_SHIP_COUNTRY (19). */
  public static Integer WDW_CLNT_SHIP_COUNTRY = 19;

  /** Value for WDW_CLNT_SHIP_TELEPHONE (20). */
  public static Integer WDW_CLNT_SHIP_TELEPHONE = 20;

  /** Value for WDW_CLNT_SHIP_AGENT (21). */
  public static Integer WDW_CLNT_SHIP_AGENT = 21;

  /** Value for WDW_CLNT_BILL_EMAIL (26). */
  public static Integer WDW_CLNT_BILL_EMAIL = 26;

  /** Value for WDW_CLNT_BILL_SLR_RES_NBR (27) */
  public static Integer WDW_CLNT_BILL_SLR_RES_NBR = 27;
  
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

  // WDW Ticket Demographics FIELD IDs!
  // NOTE:  Cannot go to field types because of Nexus defect.
  // 11/3/2015 JTL 2.15

  /** Value for WDW_TKTDEMO_LASTFIRST (1) */
  public static Integer WDW_TKTDEMO_LASTFIRST = 1;

  /** Value for WDW_TKTDEMO_DATE_OF_BIRTH (2) */
  public static Integer WDW_TKTDEMO_DTE_OF_BIRTH = 2;

  /** Value for WDW_TKTDEMO_GENDER (3) */
  public static Integer WDW_TKTDEMO_GENDER = 3;

  /** Value for WDW_TKTDEMO_ADDRESS_ONE (4) */
  public static Integer WDW_TKTDEMO_ADDRESS_ONE = 4;

  /** Value for WDW_TKTDEMO_ADDRESS_TWO (5) */
  public static Integer WDW_TKTDEMO_ADDRESS_TWO = 5;

  /** Value for WDW_TKTDEMO_CITY (6) */
  public static Integer WDW_TKTDEMO_CITY = 6;

  /** Value for WDW_TKTDEMO_STATE (7) */
  public static Integer WDW_TKTDEMO_STATE = 7;

  /** Value for WDW_TKTDEMO_ZIP (8) */
  public static Integer WDW_TKTDEMO_ZIP = 8;

  /** Value for WDW_TKTDEMO_COUNTRY (9) */
  public static Integer WDW_TKTDEMO_COUNTRY = 9;

  /** Value for WDW_TKTDEMO_TELEPHONE (10) */
  public static Integer WDW_TKTDEMO_PHONE = 10;

  /** Value for WDW_TKTDEMO_EMAIL (12) */
  public static Integer WDW_TKTDEMO_EMAIL = 12;

  /** Value for WDW_TKTDEMO_OPTINSOLICIT (15) (as of 2.10) */
  public static Integer WDW_TKTDEMO_OPTINSOLICIT = 15;

  // HKD Ticket Demographics FIELD IDs!
  // NOTE:  Cannot go to field types because of Nexus defect.
  // 10/3/2015 JTL 2.15

  /** Value for HKD_TKTDEMO_LASTNAME (1) */
  public static Integer HKD_TKTDEMO_LASTNAME = 1;

  /** Value for HKD_TKTDEMO_FIRSTNAME (2) */
  public static Integer HKD_TKTDEMO_FIRSTNAME = 2;

  /** Value for HKD_TKTDEMO_GENDER (3) */
  public static Integer HKD_TKTDEMO_GENDER = 3;

  /** Value for HKD_TKTDEMO_EMAIL (4) */
  public static Integer HKD_TKTDEMO_EMAIL = 4;

  /** Value for HKD_TKTDEMO_RESIDENCE (5) */
  public static Integer HKD_TKTDEMO_RESIDENCE = 5;

  /** Value for HKD_TKTDEMO_ADDRESS_ONE (6) */
  public static Integer HKD_TKTDEMO_ADDRESS_ONE = 6;

  /** Value for HKD_TKTDEMO_ADDRESS_TWO (7) */
  public static Integer HKD_TKTDEMO_ADDRESS_TWO = 7;

  /** Value for HKD_TKTDEMO_STATE (8) */
  public static Integer HKD_TKTDEMO_STATE = 8;

  /** Value for HKD_TKTDEMO_POSTALCD (9) */
  public static Integer HKD_TKTDEMO_POSTALCD = 9;

  /** Value for HKD_TKTDEMO_DOBDD (10) */
  public static Integer HKD_TKTDEMO_DOBDD = 10;

  /** Value for HKD_TKTDEMO_DOBMM (11) */
  public static Integer HKD_TKTDEMO_DOBMM = 11;
  
  /** Value for HKD_TKTDEMO_DOBYYYY (12) */
  public static Integer HKD_TKTDEMO_DOBYYYY = 12;

  /** Value for HKD_TKTDEMO_MOBILEPHONE (13) */
  public static Integer HKD_TKTDEMO_MOBILEPHONE = 13;
  
  /** Value for HKD_TKTDEMO_OKAYFORNEWS (14) */
  public static Integer HKD_TKTDEMO_OKAYFORNEWS = 14;

  /** Value for HKD_TKTDEMO_AGREETANDC (15) */
  public static Integer HKD_TKTDEMO_AGREETANDC = 15;

  /** Value for HKD_TKTDEMO_PARENTOKAY (16) */
  public static Integer HKD_TKTDEMO_PARENTOKAY = 16;

  /** Value for HKD_TKTDEMO_TICNBR (17) */
  public static Integer HKD_TKTDEMO_TICNBR = 17;

  /** Value for HKD_TKTDEMO_GUESTID (18) */
  public static Integer HKD_TKTDEMO_GUESTID = 18;

  /** Value for HKD_TKTDEMO_DELIVDM (19) */
  public static Integer HKD_TKTDEMO_DELIVDM = 19;
  
  /** Value for HKD_TKTDEMO_PRICONTACT (20) */
  public static Integer HKD_TKTDEMO_PRICONTACT = 20;
  
  /** Value for HKD_TKTDEMO_PREFLANG (21) */
  public static Integer HKD_TKTDEMO_PREFLANG = 21;
  
  /** Value for HKD_TKTDEMO_NEWS (22) */
  public static Integer HKD_TKTDEMO_NEWS = 22;
  
  /** Value for HKD_TKTDEMO_APREF (23) */
  public static Integer HKD_TKTDEMO_APREF = 23;

  /** Value for HKD_TKTDEMO_HOMEPHONE (24) */
  public static Integer HKD_TKTDEMO_HOMEPHONE = 24;
  
  /** Value for HKD_TKTDEMO_SELLERREF (25) */
  public static Integer HKD_TKTDEMO_SELLERREF = 25;
  
  // ***** ACCOUNT LEVEL DEMOGRAPHICS AS OF 2.12 FIELD ID *****

  /** Value for WDW_ACCTDEMO_TITLE (1) */
  public static Integer WDW_ACCTDEMO_TITLE = 1;

  /** Value for WDW_ACCTDEMO_FIRSTNAME (2) */
  public static Integer WDW_ACCTDEMO_FIRSTNAME = 2;

  /** Value for WDW_ACCTDEMO_MIDDLENAME (3) */
  public static Integer WDW_ACCTDEMO_MIDDLENAME = 3;

  /** Value for WDW_ACCTDEMO_LASTNAME (4) */
  public static Integer WDW_ACCTDEMO_LASTNAME = 4;

  /** Value for WDW_ACCTDEMO_SUFFIX (5) */
  public static Integer WDW_ACCTDEMO_SUFFIX = 5;

  /** Value for WDW_ACCTDEMO_DOB (6) */
  public static Integer WDW_ACCTDEMO_DOB = 6;

  /** Value for WDW_ACCTDEMO_EMAIL (14) */
  public static Integer WDW_ACCTDEMO_EMAIL = 14;

  /** Value for WDW_ACCTDEMO_ADDRESS1 (7) */
  public static Integer WDW_ACCTDEMO_ADDRESS1 = 7;

  /** Value for WDW_ACCTDEMO_ADDRESS2 (8) */
  public static Integer WDW_ACCTDEMO_ADDRESS2 = 8;

  /** Value for WDW_ACCTDEMO_CITY (9) */
  public static Integer WDW_ACCTDEMO_CITY = 9;

  /** Value for WDW_ACCTDEMO_STATE (10) */
  public static Integer WDW_ACCTDEMO_STATE = 10;

  /** Value for WDW_ACCTDEMO_ZIP (11) */
  public static Integer WDW_ACCTDEMO_ZIP = 11;

  /** Value for WDW_ACCTDEMO_ZIP (12) */
  public static Integer WDW_ACCTDEMO_COUNTRY = 12;

  /** Value for WDW_ACCTDEMO_OKTOMAIL (15) */
  public static Integer WDW_ACCTDEMO_OKTOMAIL = 15;

  // ***** END ACCOUNT LEVEL DEMOGRAPHICS AS OF 2.12 *****

  // ***** START INSTALLMENT LEVEL DEMOGRAPHICS AS OF 2.15 *****

  /** Value for WDW_INSTDEMO_FIRSTNAME (1) */
  public static Integer WDW_INSTDEMO_FIRSTNAME = 1;

  /** Value for WDW_INSTDEMO_MIDDLENAME (2) opt */
  public static Integer WDW_INSTDEMO_MIDLNAME = 2;

  /** Value for WDW_INSTDEMO_LASTNAME (3) */
  public static Integer WDW_INSTDEMO_LASTNAME = 3;

  /** Value for WDW_INSTDEMO_DOB (24) */
  public static Integer WDW_INSTDEMO_DOB = 24;

  /** Value for WDW_INSTDEMO_ADDR1 (6) */
  public static Integer WDW_INSTDEMO_ADDR1 = 6;

  /** Value for WDW_INSTDEMO_ADDR2 (7) */
  public static Integer WDW_INSTDEMO_ADDR2 = 7;

  /** Value for WDW_INSTDEMO_CITY (8) */
  public static Integer WDW_INSTDEMO_CITY = 8;

  /** Value for WDW_INSTDEMO_STATE (9) */
  public static Integer WDW_INSTDEMO_STATE = 9;

  /** Value for WDW_INSTDEMO_ZIP (10) */
  public static Integer WDW_INSTDEMO_ZIP = 10;

  /** Value for WDW_INSTDEMO_COUNTRY (11) */
  public static Integer WDW_INSTDEMO_COUNTRY = 11;

  /** Value for WDW_INSTDEMO_TELEPHONE (12) */
  public static Integer WDW_INSTDEMO_TELEPHONE = 12;

  /** Value for WDW_INSTDEMO_ALTPHONE (17) */
  public static Integer WDW_INSTDEMO_ALTPHONE = 17;

  /** Value for WDW_INSTDEMO_EMAIL (21) */
  public static Integer WDW_INSTDEMO_EMAIL = 21;

  // ***** END INSTALLMENT LEVEL DEMOGRAPHICS AS OF 2.15 *****

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
  public OTFieldTO(Integer fieldIndexIn, Integer fieldTypeIn,
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
  public OTFieldTO(Integer fieldIndexIn, String fieldValueIn) {
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
