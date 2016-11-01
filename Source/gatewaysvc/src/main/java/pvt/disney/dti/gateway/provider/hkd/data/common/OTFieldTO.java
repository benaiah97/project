package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;

/**
 * This class is a transfer object which represents an Omni Ticket field.
 * 
 * @author lewit019
 * 
 */
public class OTFieldTO implements Serializable {

  /** Standard serial version UID. */
  final static long serialVersionUID = 9129231995L;

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

  /** Value for TKT_DEMO_LASTFIRST (1) */
  public static Integer TKT_DEMO_LASTFIRST = 1;

  /** Value for TKT_DEMO_DATE_OF_BIRTH (2) */
  public static Integer TKT_DEMO_DATE_OF_BIRTH = 2;

  /** Value for TKT_DEMO_GENDER (3) */
  public static Integer TKT_DEMO_GENDER = 3;

  /** Value for TKT_DEMO_ADDRESS_ONE (4) */
  public static Integer TKT_DEMO_ADDRESS_ONE = 4;

  /** Value for TKT_DEMO_ADDRESS_TWO (5) */
  public static Integer TKT_DEMO_ADDRESS_TWO = 5;

  /** Value for TKT_DEMO_CITY (6) */
  public static Integer TKT_DEMO_CITY = 6;

  /** Value for TKT_DEMO_STATE (7) */
  public static Integer TKT_DEMO_STATE = 7;

  /** Value for TKT_DEMO_ZIP (8) */
  public static Integer TKT_DEMO_ZIP = 8;

  /** Value for TKT_DEMO_COUNTRY (9) */
  public static Integer TKT_DEMO_COUNTRY = 9;

  /** Value for TKT_DEMO_TELEPHONE (10) */
  public static Integer TKT_DEMO_TELEPHONE = 10;

  /** Value for TKT_DEMO_EMAIL (12) */
  public static Integer TKT_DEMO_EMAIL = 12;

  /** Value for TKT_DEMO_OPTINSOLICIT (15) (as of 2.10) */
  public static Integer TKT_DEMO_OPTINSOLICIT = 15;

  // ***** ACCOUNT LEVEL DEMOGRAPHICS AS OF 2.12 FIELD ID *****

  /** Value for ACCT_DEMO_TITLE (1) */
  public static Integer ACCT_DEMO_TITLE = 1;

  /** Value for ACCT_DEMO_FIRSTNAME (2) */
  public static Integer ACCT_DEMO_FIRSTNAME = 2;

  /** Value for ACCT_DEMO_MIDDLENAME (3) */
  public static Integer ACCT_DEMO_MIDDLENAME = 3;

  /** Value for ACCT_DEMO_LASTNAME (4) */
  public static Integer ACCT_DEMO_LASTNAME = 4;

  /** Value for ACCT_DEMO_SUFFIX (5) */
  public static Integer ACCT_DEMO_SUFFIX = 5;

  /** Value for ACCT_DEMO_DOB (6) */
  public static Integer ACCT_DEMO_DOB = 6;

  /** Value for ACCT_DEMO_EMAIL (14) */
  public static Integer ACCT_DEMO_EMAIL = 14;

  /** Value for ACCT_DEMO_ADDRESS1 (7) */
  public static Integer ACCT_DEMO_ADDRESS1 = 7;

  /** Value for ACCT_DEMO_ADDRESS2 (8) */
  public static Integer ACCT_DEMO_ADDRESS2 = 8;

  /** Value for ACCT_DEMO_CITY (9) */
  public static Integer ACCT_DEMO_CITY = 9;

  /** Value for ACCT_DEMO_STATE (10) */
  public static Integer ACCT_DEMO_STATE = 10;

  /** Value for ACCT_DEMO_ZIP (11) */
  public static Integer ACCT_DEMO_ZIP = 11;

  /** Value for ACCT_DEMO_ZIP (12) */
  public static Integer ACCT_DEMO_COUNTRY = 12;

  /** Value for ACCT_DEMO_OKTOMAIL (15) */
  public static Integer ACCT_DEMO_OKTOMAIL = 15;

  // ***** END ACCOUNT LEVEL DEMOGRAPHICS AS OF 2.12 *****

  // ***** START INSTALLMENT LEVEL DEMOGRAPHICS AS OF 2.15 *****

  /** Value for INST_DEMO_FIRSTNAME (1) */
  public static Integer INST_DEMO_FIRSTNAME = 1;

  /** Value for INST_DEMO_MIDDLENAME (2) opt */
  public static Integer INST_DEMO_MIDLNAME = 2;

  /** Value for INST_DEMO_LASTNAME (3) */
  public static Integer INST_DEMO_LASTNAME = 3;

  /** Value for INST_DEMO_DOB (24) */
  public static Integer INST_DEMO_DOB = 24;

  /** Value for INST_DEMO_ADDR1 (6) */
  public static Integer INST_DEMO_ADDR1 = 6;

  /** Value for INST_DEMO_ADDR2 (7) */
  public static Integer INST_DEMO_ADDR2 = 7;

  /** Value for INST_DEMO_CITY (8) */
  public static Integer INST_DEMO_CITY = 8;

  /** Value for INST_DEMO_STATE (9) */
  public static Integer INST_DEMO_STATE = 9;

  /** Value for INST_DEMO_ZIP (10) */
  public static Integer INST_DEMO_ZIP = 10;

  /** Value for INST_DEMO_COUNTRY (11) */
  public static Integer INST_DEMO_COUNTRY = 11;

  /** Value for INST_DEMO_TELEPHONE (12) */
  public static Integer INST_DEMO_TELEPHONE = 12;

  /** Value for INST_DEMO_ALTPHONE (17) */
  public static Integer INST_DEMO_ALTPHONE = 17;

  /** Value for INST_DEMO_EMAIL (21) */
  public static Integer INST_DEMO_EMAIL = 21;

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
