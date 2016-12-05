package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;

/**
 * The Omni-Ticket centric version of the show data.
 * @author lewit019
 *
 */
public class HkdOTShowDataTO implements Serializable {

  /** Standard serial version UID. */
  public final static long serialVersionUID = 9129231995L;
  
  private String groupCode;
  
  private String performanceId;
  
  private String quota;

  /**
   * @return the groupCode
   */
  public String getGroupCode() {
    return groupCode;
  }

  /**
   * @param groupCode the groupCode to set
   */
  public void setGroupCode(String groupCode) {
    this.groupCode = groupCode;
  }

  /**
   * @return the performanceId
   */
  public String getPerformanceId() {
    return performanceId;
  }

  /**
   * @param performanceId the performanceId to set
   */
  public void setPerformanceId(String performanceId) {
    this.performanceId = performanceId;
  }

  /**
   * @return the quota
   */
  public String getQuota() {
    return quota;
  }

  /**
   * @param quota the quota to set
   */
  public void setQuota(String quota) {
    this.quota = quota;
  }
  
  
}
