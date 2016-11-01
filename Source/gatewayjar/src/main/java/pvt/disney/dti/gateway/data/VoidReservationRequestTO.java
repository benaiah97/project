package pvt.disney.dti.gateway.data;

import java.io.Serializable;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;

/**
 * Void Reservation Transfer Object
 * 
 * @author LEWIT019
 * @since 2.16.2
 * 
 */
public class VoidReservationRequestTO extends CommandBodyTO implements Serializable {

  private static final long serialVersionUID = 5930876491753661660L;

  /** Request type */
  private String requestType;

  /** Reservation Code */
  private String resCode;

  /** Reservation Number */
  private String resNumber;

  /** External Reservation Code */
  private String externalResCode;

  /**
   * Gets the request type
   * @return
   */
  public String getRequestType() {
    return requestType;
  }

  /**
   * Sets the request type
   * @param requestType
   */
  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  /** 
   * Gets the reservation code
   * @return
   */
  public String getResCode() {
    return resCode;
  }

  /**
   * Sets the reservation code
   * @param resCode
   */
  public void setResCode(String resCode) {
    this.resCode = resCode;
  }

  /** 
   * Gets the reservation number
   * @return
   */
  public String getResNumber() {
    return resNumber;
  }

  /**
   * Sets the reservation number
   * @param resNumber
   */
  public void setResNumber(String resNumber) {
    this.resNumber = resNumber;
  }

  /** 
   * Gets the external reservation code
   * @return
   */
  public String getExternalResCode() {
    return externalResCode;
  }

  /**
   * Sets the external reservation code
   * @param externalResCode
   */
  public void setExternalResCode(String externalResCode) {
    this.externalResCode = externalResCode;
  }

}
