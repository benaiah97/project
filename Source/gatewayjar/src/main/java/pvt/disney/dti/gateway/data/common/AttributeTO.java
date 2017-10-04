package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * This class represents entity command attributes. Entity command attributes are customizations in the database respective to a particular entity. Examples of this include the specific Nexus user ID and password to be used for that
 * entity's transactions.
 * 
 * @author lewit019
 * 
 */
public class AttributeTO implements Serializable {

  /** Standard serial Version UID */
  private static final long serialVersionUID = 9129227490L;

  /** The different types of command attributes supported by DTI. */
  public enum CmdAttrCodeType {
    OP_AREA,
    USER,
    PASS,
    PRNT_TKT_CNT_MAX,
    NO_PRNT_TKT_CNT_MAX,
    CC_NOT_PRESENT_IND,
    SITE_NUMBER,
    DEFAULT_ELECT_ENC,
    SELLER_RES_PREFIX,
    RACE_RES_OVERRIDE,
    UNDEFINED
  };

  /** Key of attribute key-value pair. */
  private CmdAttrCodeType cmdAttrCode;
  /** Value portion of attribute key-value pair. */
  private String attrValue;
  /** The alphanumeric text which identifies a command, such as "QueryTicket". */
  private String cmdCode;
  /** Command actor, if relevant. */
  private String actor;
  /** Determines if the attribute key value pair is active or not. */
  private String activeInd;

  /**
   * @return the activeInd
   */
  public String getActiveInd() {
    return activeInd;
  }

  /**
   * @return the actor
   */
  public String getActor() {
    return actor;
  }

  /**
   * @return the attrValue
   */
  public String getAttrValue() {
    return attrValue;
  }

  /**
   * @return the cmdCode
   */
  public String getCmdCode() {
    return cmdCode;
  }

  /**
   * @param activeInd
   *            the activeInd to set
   */
  public void setActiveInd(String activeInd) {
    this.activeInd = activeInd;
  }

  /**
   * @param actor
   *            the actor to set
   */
  public void setActor(String actor) {
    this.actor = actor;
  }

  /**
   * @param attrValue
   *            the attrValue to set
   */
  public void setAttrValue(String attrValue) {
    this.attrValue = attrValue;
  }

  /**
   * @return the cmdAttrCode
   */
  public CmdAttrCodeType getCmdAttrCode() {
    return cmdAttrCode;
  }

  /**
   * @param cmdAttrCode
   *            the cmdAttrCode to set
   */
  public void setCmdAttrCode(CmdAttrCodeType cmdAttrCode) {
    this.cmdAttrCode = cmdAttrCode;
  }

  /**
   * @param cmdCode
   *            the cmdCode to set
   */
  public void setCmdCode(String cmdCode) {
    this.cmdCode = cmdCode;
  }

	 /**
   * Override of the nominal class "toString()" method to provide detail output about this object's contents.
   */
  public String toString() {
    String output = "AttributeTO: cmdCode =[" + cmdCode + "] attrValue=[" + attrValue + "] cmdAttrCode=[" + cmdAttrCode + "] activeInd=[" + activeInd + "]";
    return output;
  }
	
}
