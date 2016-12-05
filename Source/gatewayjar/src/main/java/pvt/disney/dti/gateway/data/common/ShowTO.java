package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * @author lewit019
 * @since 2.16.3
 * 
 */
public class ShowTO implements Serializable {

  /** Serial Version UID */
  private static final long serialVersionUID = 9129231996L;

  /** Show Group */
  private String showGroup;
  
  /** Show Performance */
  private String showPerformance;
  
  /** Show Quota */
  private String showQuota;

  /**
   * @return the showGroup
   */
  public String getShowGroup() {
    return showGroup;
  }

  /**
   * @param showGroup the showGroup to set
   */
  public void setShowGroup(String showGroup) {
    this.showGroup = showGroup;
  }

  /**
   * @return the showPerformance
   */
  public String getShowPerformance() {
    return showPerformance;
  }

  /**
   * @param showPerformance the showPerformance to set
   */
  public void setShowPerformance(String showPerformance) {
    this.showPerformance = showPerformance;
  }

  /**
   * @return the showQuota
   */
  public String getShowQuota() {
    return showQuota;
  }

  /**
   * @param showQuota the showQuota to set
   */
  public void setShowQuota(String showQuota) {
    this.showQuota = showQuota;
  }
  
  
  
}
