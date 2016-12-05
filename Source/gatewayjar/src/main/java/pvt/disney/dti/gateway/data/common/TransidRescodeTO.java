package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**
 * The Class TransidRescodeTO.
 * 
 * @author MOONS012
 * @since 2.16.3
 */
public class TransidRescodeTO  implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 7250181673022888799L;
  
  /** The ts transid. */
  private String tsTransid;
  
  /** The rescode. */
  private String rescode;
  
  /** The creation date. */
  private GregorianCalendar creationDate;
  

  /**
   * Instantiates a new transid rescode TO.
   */
  public TransidRescodeTO() {
    super();
  }

  /**
   * @return the tsTransid
   */
  public String getTsTransid() {
    return tsTransid;
  }

  /**
   * @param tsTransid the tsTransid to set
   */
  public void setTsTransid(String tsTransid) {
    this.tsTransid = tsTransid;
  }

  /**
   * @return the rescode
   */
  public String getRescode() {
    return rescode;
  }

  /**
   * @param rescode the rescode to set
   */
  public void setRescode(String rescode) {
    this.rescode = rescode;
  }

  /**
   * @return the creationDate
   */
  public GregorianCalendar getCreationDate() {
    return creationDate;
  }

  /**
   * @param creationDate the creationDate to set
   */
  public void setCreationDate(GregorianCalendar creationDate) {
    this.creationDate = creationDate;
  }

}

