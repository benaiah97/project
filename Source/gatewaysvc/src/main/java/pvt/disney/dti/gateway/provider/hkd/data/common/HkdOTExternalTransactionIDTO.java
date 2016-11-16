package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;

/**
 * Represents the Omni Ticket construction for the external transaction ID
 * @author lewit019
 * @since 2.16.3
 */
public class HkdOTExternalTransactionIDTO implements Serializable {

  private static final long serialVersionUID = 1L;
  
  private String id;
  
  private Boolean alreadyEncrypted = false;

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * @return the alreadyEncrypted
   */
  public Boolean isAlreadyEncrypted() {
    return alreadyEncrypted;
  }

  /**
   * @param alreadyEncrypted the alreadyEncrypted to set
   */
  public void setAlreadyEncrypted(Boolean alreadyEncrypted) {
    this.alreadyEncrypted = alreadyEncrypted;
  }
  
}
