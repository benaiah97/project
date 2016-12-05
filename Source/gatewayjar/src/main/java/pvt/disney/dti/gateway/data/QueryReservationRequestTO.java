package pvt.disney.dti.gateway.data;

import java.io.Serializable;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;

/**
 * 
 * @author biest001
 * @since 2.16.3
 */
public class QueryReservationRequestTO extends CommandBodyTO implements Serializable {

  private static final long serialVersionUID = 5930876491753661660L;

  private String requestType;

  private String resCode;

  private String resNumber;

  private String externalResCode;
  
  private String payloadID;

  /**
   * An optional value that determines if reservation demographics should be returned in the response.
   * As of 2.16.3, JTL
   */
  private boolean includeResDemographics = false;
  
  /**
   * @return the requestType
   */
  public String getRequestType() {
    return requestType;
  }

  /**
   * @param requestType the requestType to set
   */
  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  /**
   * @return the resCode
   */
  public String getResCode() {
    return resCode;
  }

  /**
   * @param resCode the resCode to set
   */
  public void setResCode(String resCode) {
    this.resCode = resCode;
  }

  /**
   * @return the resNumber
   */
  public String getResNumber() {
    return resNumber;
  }

  /**
   * @param resNumber the resNumber to set
   */
  public void setResNumber(String resNumber) {
    this.resNumber = resNumber;
  }

  /**
   * @return the externalResCode
   */
  public String getExternalResCode() {
    return externalResCode;
  }

  /**
   * @param externalResCode the externalResCode to set
   */
  public void setExternalResCode(String externalResCode) {
    this.externalResCode = externalResCode;
  }

  /**
   * @return the payloadID
   */
  public String getPayloadID() {
    return payloadID;
  }

  /**
   * @param payloadID the payloadID to set
   */
  public void setPayloadID(String payloadID) {
    this.payloadID = payloadID;
  }

  /**
   * @return the includeResDemographics
   */
  public boolean isIncludeResDemographics() {
    return includeResDemographics;
  }

  /**
   * @param includeResDemographics the includeResDemographics to set
   */
  public void setIncludeResDemographics(boolean includeResDemographics) {
    this.includeResDemographics = includeResDemographics;
  }

}