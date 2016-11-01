package pvt.disney.dti.gateway.rules;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIRequestTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.ReservationRequestTO;
import pvt.disney.dti.gateway.data.common.ClientDataTO;
import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.CommandHeaderTO;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.DemographicsTO;
import pvt.disney.dti.gateway.data.common.PayloadHeaderTO;
import pvt.disney.dti.gateway.rules.dlr.DLRBusinessRules;
import pvt.disney.dti.gateway.rules.hkd.HKDBusinessRules;
import pvt.disney.dti.gateway.rules.wdw.WDWBusinessRules;
import pvt.disney.dti.gateway.service.dtixml.ReservationXML;
import pvt.disney.dti.gateway.util.CustomDataTypeConverter;

/**
 * This class has the responsibility to transform between DTITransactionTO
 * objects and the provider's specific request string format.
 * 
 * @author lewit019
 */
public abstract class TransformRules {

  /** Default constant for zero duration. */
  private final static BigDecimal ZERO_DURATION = new BigDecimal("0.0");
  
  /** Default constant for command count of one. */
  private final static BigInteger CMD_COUNT = new BigInteger("1");
  
  private final static String SALES_CONTACT = "SALES CONTACT: ";

  private final static String USE_VALIDITY_DATES = "USE VALIDITY DATES ";
  
  /** Constant representing the note carriage return. */
  private final static String NOTECR = "^";
  
  private final static String UNKNOWN_PAYMENT = "UNKNOWN";
  
  /**
   * This method changes from the DTITransaction to any provider's request
   * string format. It determines the specific provider to use based on the
   * tpiCode stored in the dtiTxn object.
   * 
   * @param dtiTxn
   *          The dtiTxn object containing the request.
   * @return The provider's request format.
   * @throws DTIException
   *           for any error. Contains enough detail to formulate an error
   *           response to the seller.
   */
  public static String changeToProviderFormat(DTITransactionTO dtiTxn) throws DTIException {

    String tpiCode = dtiTxn.getTpiCode();
    if (tpiCode.equals(DTITransactionTO.TPI_CODE_WDW)) {

      return WDWBusinessRules.changeToWDWProviderFormat(dtiTxn);

    } else if (tpiCode.equals(DTITransactionTO.TPI_CODE_DLR)) {

      return DLRBusinessRules.changeToDLRProviderFormat(dtiTxn);

    } else if (tpiCode.equals(DTITransactionTO.TPI_CODE_HKD)) {
      
      return HKDBusinessRules.changeToHKDProviderFormat(dtiTxn);
      
    } else {

      throw new DTIException(TransformRules.class, DTIErrorCode.DTI_PROCESS_ERROR,
          "Invalid ticket provider sent to queryTicket() in Java version of DTI Gateway.");

    }

  }

  /**
   * Parses the xmlResponse string from any provider into the DTITransactionTO
   * object.
   * 
   * @param dtiTxn
   *          The dtiTxn object for this transaction.
   * @param xmlResponse
   *          The response string returned by the provider.
   * @return The dtiTxn object enhanced with the response.
   * @throws DTIException
   *           for any error. Contains enough detail to formulate an error
   *           response to the seller.
   */
  public static DTITransactionTO changeToDtiFormat(DTITransactionTO dtiTxn, String xmlResponse)
      throws DTIException {

    String tpiCode = dtiTxn.getTpiCode();
    if (tpiCode.equals(DTITransactionTO.TPI_CODE_WDW)) {

      return WDWBusinessRules.changeWDWProviderFormatToDti(dtiTxn, xmlResponse);

    } else if (tpiCode.equals(DTITransactionTO.TPI_CODE_DLR)) {

      return DLRBusinessRules.changeDLRProviderFormatToDti(dtiTxn, xmlResponse);

    } else if (tpiCode.equals(DTITransactionTO.TPI_CODE_HKD)) {
      
      return HKDBusinessRules.changeHKDProviderFormatToDti(dtiTxn, xmlResponse);
      
    } else {

      throw new DTIException(TransformRules.class, DTIErrorCode.INVALID_TICKET_PROVIDER,
          "Invalid ticket provider sent to Java version of DTI Gateway.");

    }

  }

  /**
   * Creates the response payload header.
   * 
   * @param dtiTxn
   *          The dtiTxn object containing the request.
   * @return The PayloadHeaderTO object supplied with correct attributes.
   */
  public static PayloadHeaderTO createRespPayloadHdr(DTITransactionTO dtiTxn) {

    PayloadHeaderTO respHeader = new PayloadHeaderTO();
    PayloadHeaderTO rqstHeader = dtiTxn.getRequest().getPayloadHeader();

    respHeader.setPayloadID(dtiTxn.getTpRefNum().toString());
    respHeader.setTsPayloadID(rqstHeader.getPayloadID());
    respHeader.setTarget(rqstHeader.getTarget());
    respHeader.setVersion(rqstHeader.getVersion());
    respHeader.setCommProtocol(rqstHeader.getCommProtocol());
    respHeader.setCommMethod(rqstHeader.getCommMethod());
    respHeader.setCommandCount(CMD_COUNT);

    respHeader.setTktBroker(dtiTxn.getTktBroker());

    // Set the TransmitDateTime to the present moment, GMT.
    GregorianCalendar gCal = DateTimeRules.getGMTDateNow();
    respHeader.setTransmitDateTime(gCal);

    return respHeader;
  }

  /**
   * Creates the response command header.
   * 
   * @param dtiTxn
   *          The dtiTxn object containing the request.
   * @return The CommandHeaderTO filled with appropriate attributes.
   */
  public static CommandHeaderTO createRespCmdHdr(DTITransactionTO dtiTxn) {

    CommandHeaderTO respHeader = new CommandHeaderTO();
    CommandHeaderTO rqstHeader = dtiTxn.getRequest().getCommandHeader();

    respHeader.setCmdItem(rqstHeader.getCmdItem());
    respHeader.setCmdDuration(ZERO_DURATION);

    // Set the CmdDateTime to the present moment, GMT.
    GregorianCalendar gCal = DateTimeRules.getGMTDateNow();
    respHeader.setCmdDateTime(gCal);

    return respHeader;
  }
  
  /**
   * Creates a highly formatted note field, mostly for groups fulfillment.
   * @param dtiTxn The DTI Transaction transfer object
   * @return the formatted string or null if nothing was formatted.
   */
  public static String setFulfillmentNoteDetails(DTITransactionTO dtiTxn) {

    DTIRequestTO dtiRequest = dtiTxn.getRequest();
    CommandBodyTO dtiCmdBody = dtiRequest.getCommandBody();
    ReservationRequestTO dtiResReq = (ReservationRequestTO) dtiCmdBody;
    ClientDataTO dtiClientData = dtiResReq.getClientData();

    StringBuffer sb = new StringBuffer();

    // Section 1: Payment method
    if (dtiClientData.getClientPaymentMethod() != ClientDataTO.PaymentMethod.UNDEFINED) {
      sb.append("PMT: ");

      ClientDataTO.PaymentMethod payMethod = dtiClientData
          .getClientPaymentMethod();

      switch (payMethod) {

      case CREDITCARD:
        sb.append(ReservationXML.getCreditCard() + NOTECR);
        break;
      case DISNEYGIFTCARD:
        sb.append(ReservationXML.getDisneyGiftCard() + NOTECR);
        break;
      case ORGANIZATIONALCHECK:
        sb.append(ReservationXML.getOrganizationalCheck() + NOTECR);
        break;
      case CASHIERSCHECK:
        sb.append(ReservationXML.getCashiersCheck() + NOTECR);
        break;
      case MONEYORDER:
        sb.append(ReservationXML.getMoneyOrder() + NOTECR);
        break;
      case WIRETRANSFER:
        sb.append(ReservationXML.getWireTransfer() + NOTECR);
        break;
      case PAYATPICKUP:
        sb.append(ReservationXML.getPayAtPickup() + NOTECR);
        break;
      case MASTERACCOUNT:
        sb.append(ReservationXML.getMasterAccount() + NOTECR);
        break;
      case SAPJOBNUMBER:
        sb.append(ReservationXML.getSapJobNumber() + NOTECR);
        break;
      case TICKETEXCHANGE:
        sb.append(ReservationXML.getTicketExchange() + NOTECR);
        break;
      default:
        sb.append(UNKNOWN_PAYMENT + NOTECR);
        break;

      }
    }

    // Section 2: Tax Exempt Label
    if (dtiResReq.getTaxExemptCode() != null) {
      sb.append("TAX EXEMPT ");
      if(dtiClientData.getClientDeliveryInstructions() == null) {
    	  sb.append(NOTECR);
      }
    }
    
    //RM13528962 - Client Delivery Instructions should be kept after TAX Exempt
    //Section 6 is moved here for this requirement
    if (dtiClientData.getClientDeliveryInstructions() != null) {
        sb.append(dtiClientData.getClientDeliveryInstructions() + NOTECR);
    }
    
    // Section 3: Fulfillment Method
    if (dtiClientData.getClientFulfillmentMethod() != null) {
      sb.append(dtiClientData.getClientFulfillmentMethod() + NOTECR);
    }

    // Section 4: Time Requirement
    if (dtiClientData.getTimeRequirement() != null) {
      sb.append(dtiClientData.getTimeRequirement() + NOTECR);
    }

    // Section 5: Shipping Address
    if (dtiClientData.getDeliveryInfo() != null) {
      sb.append("SHIP TO ");

      DemographicsTO dlvAddr = dtiClientData.getDeliveryInfo();

      if (dlvAddr.getFirstName() != null) {
        sb.append(dlvAddr.getFirstName());
      }

      if (dlvAddr.getLastName() != null) {
        sb.append(" " + dlvAddr.getLastName());
      }

      if (dlvAddr.getName() != null) {
        sb.append(NOTECR + dlvAddr.getName());
      }

      if (dlvAddr.getAddr1() != null) {
        sb.append(NOTECR + dlvAddr.getAddr1());
      }

      if (dlvAddr.getAddr2() != null) {
        sb.append(NOTECR + dlvAddr.getAddr2());
      }

      if (dlvAddr.getCity() != null) {
        sb.append(NOTECR + dlvAddr.getCity());
      }

      if (dlvAddr.getState() != null) {
        sb.append(" " + dlvAddr.getState());
      }

      if (dlvAddr.getZip() != null) {
        sb.append(" " + dlvAddr.getZip());
      }

      if (dlvAddr.getCountry() != null) {
        sb.append(NOTECR + dlvAddr.getCountry());
      }

      if (dlvAddr.getTelephone() != null) {
        sb.append(NOTECR + dlvAddr.getTelephone());
      }

      if (dlvAddr.getEmail() != null) {
        sb.append(NOTECR + dlvAddr.getEmail());
      }

      sb.append(NOTECR);
    }

    // Section 7: Product validity dates
    // to facilitate displaying blank valid start/end dates, if needed,
    // check for products that need validity if any of the delivery note
    // fields are provided.
    if ((dtiClientData.getGroupValidityValidStart() != null)
        || (dtiClientData.getGroupValidityValidEnd() != null)
        || sb.length() > 0 || dtiClientData.getClientSalesContact() != null) {
      boolean productsNeedValidity = false;

      ArrayList<DBProductTO> dbProdList = dtiTxn.getDbProdList();

      for /* each */(DBProductTO dbProd : /* in */dbProdList) {
        if (dbProd.isValidityDateInfoRequired()) {
          productsNeedValidity = true;
          break;
        }
      }

      if (productsNeedValidity) {
        sb.append(USE_VALIDITY_DATES);
        if (dtiClientData.getGroupValidityValidStart() != null) {
          sb.append(CustomDataTypeConverter
              .printCalToStandardDateFormat(dtiClientData
                  .getGroupValidityValidStart()));
        }
        sb.append(" - ");
        if (dtiClientData.getGroupValidityValidEnd() != null) {
          sb.append(CustomDataTypeConverter
              .printCalToStandardDateFormat(dtiClientData
                  .getGroupValidityValidEnd()));
        }
        sb.append(NOTECR);
      }
    }

    // Section 8: Sales Contact
    if (dtiClientData.getClientSalesContact() != null) {
      sb.append(SALES_CONTACT + dtiClientData.getClientSalesContact());
    }

    if (sb.length() > 0) {
      return sb.toString();
    } else {
      return null;
    }
  }

}
