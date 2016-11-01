package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.TicketTO;

/**
 * This class represents the request parameters of a Query Ticket Request.
 * 
 * @author lewit019
 * 
 */
public class QueryTicketRequestTO extends CommandBodyTO implements Serializable {

  /** Standard serial version UID. */
  final static long serialVersionUID = 9129231995L;

  // /** Defines the ticket demographics request type. */
  // public enum TktDemographicsRequestType {
  // TRUE, FALSE, RENEWAL
  // };

  // /** Defines what the string value of true is. */
  // private final static String TRUESTRING = "TRUE";
  //
  // /** Defines what the string value of all is. */
  // private final static String RENEWALSTRING = "RENEWAL";

  /**
   * An array list of tickets (although generally there should only be one, this matches the XML specification.
   */
  private ArrayList<TicketTO> tktList = new ArrayList<TicketTO>();

  /**
   * An optional value that determines if ticket demographics should be returned in the response.
   */
  private boolean includeTktDemographics = false;

  /**
   * An optional value that determines if pass attributes should be returned in the response (in addition to the normal ticket attributes).
   */
  private boolean includePassAttributes = false;

  /**
   * An optional value that determines if Electronic Attributes should be returned in the response (in addition to then normal ticket attributes). currently limited to ELECTRONICELIGIBLE and ELECTRONICENTITILEMENT
   */
  private boolean includeElectronicAttributes = false;

  /**
   * An optional value that determines if the ticket redeemability will be returned in the response.
   */
  private boolean includeTicketRedeemability = false;

  /**
   * An optional value that determines if the renewal attributes should be returned in the response.
   */
  private boolean includeRenewalAttributes = false;

  /**
   * An optional flag that indicates the Electronic Account Id should be returned in the response.
   */
  private boolean includeEntitlementAccount = false;

  /**
   * @return the tktList
   */
  public ArrayList<TicketTO> getTktList() {
    return tktList;
  }

  /**
   * @param tktList
   *            the tktList to set
   */
  public void setTktList(ArrayList<TicketTO> tktList) {
    this.tktList = tktList;
  }

  /**
   * 
   * @return
   */
  public boolean isIncludePassAttributes() {
    return includePassAttributes;
  }

  /**
   * 
   * @param includePassAttributes
   */
  public void setIncludePassAttributes(boolean includePassAttributes) {
    this.includePassAttributes = includePassAttributes;
  }

  /**
   * 
   * @return
   */
  public boolean isIncludeElectronicAttributes() {
    return this.includeElectronicAttributes;
  }

  /**
   * 
   * @param includeElectronicAttributes
   */
  public void setIncludeElectronicAttributes(
      boolean includeElectronicAttributes) {
    this.includeElectronicAttributes = includeElectronicAttributes;
  }

  /**
   * @return the includeTicketRedeemability
   */
  public boolean isIncludeTicketRedeemability() {
    return includeTicketRedeemability;
  }

  /**
   * @param includeTicketRedeemability
   *            the includeTicketRedeemability to set
   */
  public void setIncludeTicketRedeemability(boolean includeTicketRedeemability) {
    this.includeTicketRedeemability = includeTicketRedeemability;
  }

  /**
   * @return the includeTktDemographics
   */
  public boolean isIncludeTktDemographics() {
    return includeTktDemographics;
  }

  /**
   * @param includeTktDemographics
   *            the includeTktDemographics to set
   */
  public void setIncludeTktDemographics(boolean includeTktDemographics) {
    this.includeTktDemographics = includeTktDemographics;
  }

  // /**
  // * @param String the includeTktDemographics to set
  // */
  // public void setIncludeTktDemographics(String inString) {
  //
  // if (inString.equalsIgnoreCase(RENEWALSTRING)) {
  // this.includeTktDemographics = TktDemographicsRequestType.RENEWAL;
  // } else if (inString.equalsIgnoreCase(TRUESTRING)) {
  // this.includeTktDemographics = TktDemographicsRequestType.TRUE;
  // } else {
  // this.includeTktDemographics = TktDemographicsRequestType.FALSE;
  // }
  // }

  /**
   * @return the includeRenewalAttributes
   */
  public boolean isIncludeRenewalAttributes() {
    return includeRenewalAttributes;
  }

  /**
   * @param includeRenewalAttributes
   *            the includeRenewalAttributes to set
   */
  public void setIncludeRenewalAttributes(boolean includeRenewalAttributes) {
    this.includeRenewalAttributes = includeRenewalAttributes;
  }

  /**
   * @return includeEntitlementAccount
   */
  public boolean isIncludeEntitlementAccount() {
    return includeEntitlementAccount;
  }

  /**
   * @param includeEntitlementAccount
   *            (the value to "set")
   */
  public void setIncludeEntitlementAccount(boolean includeEntitlementAccount) {
    this.includeEntitlementAccount = includeEntitlementAccount;
  }

}
