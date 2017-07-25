package pvt.disney.dti.gateway.provider.dlr.xml;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.Node;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

import pvt.disney.dti.gateway.constants.DTIErrorCode;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.provider.dlr.data.GWBodyTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWHeaderTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketErrorsTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRespTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWQueryTicketRqstTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWStatusTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO.LineageRecord;
import pvt.disney.dti.gateway.util.UtilityXML;

public class GWQueryEligibleProductsTicketXML {
	
	 /** Event logger. */
	  private static final EventLogger logger = EventLogger
	      .getLogger(GWQueryTicketXML.class.getCanonicalName());
	  private static final GWQueryEligibleProductsTicketXML logInstance = new GWQueryEligibleProductsTicketXML();
	  
	  private static final int DLR_TEMP_ENTITLEMENT_LENGTH = 19;

	/**
	   * Adds the query ticket element.
	   * 
	   * @param qtReqTO
	   *            the Query Ticket request transfer object.
	   * @param bodyElement
	   *            the body element which needs the query ticket section added.
	   * @throws DTIException
	   *             should any marshaling error occur.
	   */
	  public static void addQueryTicketElement(GWQueryTicketRqstTO qtReqTO,
	      Element bodyElement) throws DTIException {

	    Element queryTicketElement = bodyElement.addElement("QueryTicket");

	    String visualID = qtReqTO.getVisualID();
	    if (visualID == null) throw new DTIException(GWQueryEligibleProductsTicketXML.class,
	        DTIErrorCode.INVALID_MSG_CONTENT,
	        "QueryTicket request did not have a visual ID specified.");

	    Element queryStanza = queryTicketElement.addElement("Query");
	    queryStanza.addElement("VisualID").addText(visualID);

	     Element dataRequestStanza = queryTicketElement
	        .addElement("DataRequest");
	     dataRequestStanza.addElement("Field").addText("ItemKind");
	     dataRequestStanza.addElement("Field").addText("Returnable");
	     dataRequestStanza.addElement("Field").addText("Status");
	     dataRequestStanza.addElement("Field").addText("DateSold");
	     dataRequestStanza.addElement("Field").addText("TicketDate");
	     dataRequestStanza.addElement("Field").addText("StartDateTime");
	     dataRequestStanza.addElement("Field").addText("DateOpened");
	     dataRequestStanza.addElement("Field").addText("ExpirationDate");
	     dataRequestStanza.addElement("Field").addText("EndDateTime");
	     dataRequestStanza.addElement("Field").addText("ValidUntil");
	     dataRequestStanza.addElement("Field").addText("LockedOut");
	     dataRequestStanza.addElement("Field").addText("VisualID");
	     dataRequestStanza.addElement("Field").addText("Price");
	     dataRequestStanza.addElement("Field").addText("UseCount");
	     dataRequestStanza.addElement("Field").addText("Tax");
	     dataRequestStanza.addElement("Field").addText("RemainingUse"); // As of DTI 2.14

	     // As of 2.11 for DLR Evergreen
	    dataRequestStanza.addElement("Field").addText("Kind");
	  
	    // Demographics Data
	    if (qtReqTO.isIncludeTktDemographics()) {
	          dataRequestStanza.addElement("Field").addText("FirstName");
	          dataRequestStanza.addElement("Field").addText("LastName");
	          dataRequestStanza.addElement("Field").addText("Street1");
	          dataRequestStanza.addElement("Field").addText("Street2");
	          dataRequestStanza.addElement("Field").addText("City");
	          dataRequestStanza.addElement("Field").addText("State");
	          dataRequestStanza.addElement("Field").addText("ZIP");
	          dataRequestStanza.addElement("Field").addText("CountryCode");
	          dataRequestStanza.addElement("Field").addText("Phone");
	          dataRequestStanza.addElement("Field").addText("Email");
	        }
	    dataRequestStanza.addElement("Field").addText("DOB");
	    dataRequestStanza.addElement("Field").addText("Gender");
	    
	    if (visualID.length() == DLR_TEMP_ENTITLEMENT_LENGTH) {
	        Element lineageStanza = dataRequestStanza
	            .addElement("LineageRecords");
	        lineageStanza.addElement("Field").addText("Amount");
	        lineageStanza.addElement("Field").addText("Valid");
	        lineageStanza.addElement("Field").addText("Status");
	        lineageStanza.addElement("Field").addText("VisualID");
	        lineageStanza.addElement("Field").addText("ExpirationDate");
	      }
	    
	    // Adding new field for the QEP Command
	    dataRequestStanza.addElement("Field").addText("UpgradePLUList");
	    dataRequestStanza.addElement("Field").addText("Contact");
	    dataRequestStanza.addElement("Field").addText("HasPicture");
	    return;
	  }
	  /**
	   * Sets the response body transfer object with the parsed data from the body element.
	   * 
	   * @param gwBodyTO
	   *            The body transfer object which needs to have values set.
	   * @param bodyElement
	   *            the body element from the response.
	   * @throws DTIException
	   *             should any unmarshalling exception occur.
	   */
	  @SuppressWarnings("unchecked")
	  public static void setRespBodyTO(GWBodyTO gwBodyTO, Element bodyElement) throws DTIException {

	    //
	    for (Iterator<org.dom4j.Element> i = bodyElement.elementIterator(); i
	        .hasNext();) {
	      Element element = i.next();

	      if (element.getName().compareTo("Status") == 0) {
	        GWStatusTO statusTO = setStatusTO(element);
	        gwBodyTO.setStatusTO(statusTO);
	      }

	      if (element.getName().compareTo("QueryTicketResponse") == 0) {
	        GWQueryTicketRespTO respTO = setRespTO(element);
	        gwBodyTO.setQueryTicketResponseTO(respTO);
	      }

	      if (element.getName().compareTo("QueryTicketErrors") == 0) {
	        GWQueryTicketErrorsTO errorsTO = setErrorsTO(element);
	        gwBodyTO.setQueryTicketErrorsTO(errorsTO);
	      }

	    }

	    if (gwBodyTO.getStatusTO() == null) throw new DTIException(
	        GWBodyXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
	        "Ticket provider returned QueryTicketResponse without Status clause.");

	    if ((gwBodyTO.getQueryTicketErrorsTO() == null) && (gwBodyTO
	        .getQueryTicketResponseTO() == null)) throw new DTIException(
	        GWBodyXML.class,
	        DTIErrorCode.TP_INTERFACE_FAILURE,
	        "Ticket provider returned QueryTicketResponse without Error or Response clauses.");

	    return;
	  }

	  /**
	   * Marshals the gateway status transfer object.
	   * 
	   * @param statusElement
	   *            the response supplied by gateway.
	   * @return the Gateway Status Transfer Object.
	   * @throws DTIException
	   *             should any unmarshalling error occur.
	   */
	  @SuppressWarnings("unchecked")
	  private static GWStatusTO setStatusTO(Element statusElement) throws DTIException {

	    GWStatusTO gwStatusTO = new GWStatusTO();

	    for (Iterator<org.dom4j.Element> i = statusElement.elementIterator(); i
	        .hasNext();) {
	      Element element = i.next();

	      if (element.getName().compareTo("StatusCode") == 0) {
	        gwStatusTO.setStatusCode(element.getText());
	      }

	      if (element.getName().compareTo("StatusText") == 0) {
	        gwStatusTO.setStatusText(element.getText());
	      }

	    }

	    if (gwStatusTO.getStatusCode() == null) throw new DTIException(
	        GWBodyXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
	        "Ticket provider returned Status clause missing a StatusCode.");

	    if (gwStatusTO.getStatusText() == null) throw new DTIException(
	        GWBodyXML.class, DTIErrorCode.TP_INTERFACE_FAILURE,
	        "Ticket provider returned Status clause missing a StatusText.");

	    return gwStatusTO;
	  }

	  /**
	   * Unmarshall the query response element into the gateway query ticket response transfer object.
	   * 
	   * @param qryRespElement
	   *            the query ticket response element provided in the parsed response.
	   * @return the gateway query ticket response transfer object.
	   * @throws DTIException
	   *             should any unmarshalling problem occur.
	   */
	  @SuppressWarnings("unchecked")
	  private static GWQueryTicketRespTO setRespTO(Element qryRespElement) throws DTIException {

		    GWQueryTicketRespTO qtRespTO = new GWQueryTicketRespTO();

		    Element dataRespElement = null;
		    Element upgradePluElement=null;

		    for (Iterator<org.dom4j.Element> i = qryRespElement.elementIterator(); i
		        .hasNext();) {
		      Element element = i.next();

		      if (element.getName().compareTo("DataRequestResponse") == 0) {
		        dataRespElement = element;
		        break;
		      }

		    }

		    GWDataRequestRespTO dataRespTO = new GWDataRequestRespTO();

		    if (dataRespElement == null) throw new DTIException(
		        GWBodyXML.class,
		        DTIErrorCode.TP_INTERFACE_FAILURE,
		        "Ticket provider returned QueryTicketResponse without a DataRequestResponse clause.");

		    for (Iterator<org.dom4j.Element> i = dataRespElement.elementIterator(); i
		        .hasNext();) {
		      Element element = i.next();

		      Integer itemKind = 0;

		      // Integer itemKind
		      if (element.getName().compareTo("ItemKind") == 0) {
		        String itemKindString = element.getText();
		        try {
		          itemKind = new Integer(itemKindString);
		          dataRespTO.setItemKind(itemKind);
		        }
		        catch (NumberFormatException nfe) {
		          throw new DTIException(GWQueryTicketXML.class,
		              DTIErrorCode.INVALID_MSG_CONTENT,
		              "Response GW XML DataRequestResp has non-numeric ItemKind.");
		        }
		      }

		      // Boolean returnable
		      if (element.getName().compareTo("Returnable") == 0) {
		        String returnableString = element.getText();
		        if (returnableString.equalsIgnoreCase("YES")) dataRespTO
		            .setReturnable(Boolean.valueOf(true));
		        else if (returnableString.equalsIgnoreCase("NO")) dataRespTO
		            .setReturnable(Boolean.valueOf(false));
		        else throw new DTIException(GWQueryTicketXML.class,
		            DTIErrorCode.INVALID_MSG_CONTENT,
		            "Response GW XML DataRequestResp has invalid value for Returnable.");
		      }

		      // Boolean renewable (As of 2.16.1, JTL)
		      if (element.getName().compareTo("Renewable") == 0) {
		        String renewableString = element.getText();
		        if (renewableString.equalsIgnoreCase("YES")) dataRespTO
		            .setRenewable(Boolean.valueOf(true));
		        else if (renewableString.equalsIgnoreCase("NO")) dataRespTO
		            .setRenewable(Boolean.valueOf(false));
		        else throw new DTIException(GWQueryTicketXML.class,
		            DTIErrorCode.INVALID_MSG_CONTENT,
		            "Response GW XML DataRequestResp has invalid value for Renewable.");
		      }

		      // Integer status
		      if (element.getName().compareTo("Status") == 0) {
		        String statusString = element.getText();
		        try {
		          Integer status = new Integer(statusString);
		          dataRespTO.setStatus(status);
		        }
		        catch (NumberFormatException nfe) {
		          throw new DTIException(GWQueryTicketXML.class,
		              DTIErrorCode.INVALID_MSG_CONTENT,
		              "Response GW XML DataRequestResp has non-numeric Status.");
		        }
		      }

		      // GregorianCalendar dateSold
		      if (element.getName().compareTo("DateSold") == 0) {
		        String dateSoldString = element.getText();
		        GregorianCalendar dateSold = UtilityXML
		            .getGCalFromEGalaxyDate(dateSoldString);
		        if (dateSold == null) {
		          throw new DTIException(
		              GWQueryTicketXML.class,
		              DTIErrorCode.INVALID_MSG_CONTENT,
		              "Response GW XML DataRequestResp has unparsable DateSold: " + dateSoldString);
		        }
		        dataRespTO.setDateSold(dateSold);
		      }

		      // GregorianCalendar ticketDate
		      if (element.getName().compareTo("TicketDate") == 0) {
		        String ticketDateString = element.getText();
		        if ((ticketDateString != null) && (ticketDateString.length() > 0)) {
		          GregorianCalendar ticketDate = UtilityXML
		              .getGCalFromEGalaxyDate(ticketDateString);
		          if (ticketDate == null) {
		            throw new DTIException(
		                GWQueryTicketXML.class,
		                DTIErrorCode.INVALID_MSG_CONTENT,
		                "Response GW XML DataRequestResp has unparsable TicketDate: " + ticketDateString);
		          }
		          dataRespTO.setTicketDate(ticketDate);
		        }
		      }

		      // GregorianCalendar startDateTime
		      if (element.getName().compareTo("StartDateTime") == 0) {
		        String startDateTimeString = element.getText();

		        if ((startDateTimeString != null) && (startDateTimeString
		            .length() > 0)) {
		          GregorianCalendar startDateTime = UtilityXML
		              .getGCalFromEGalaxyDate(startDateTimeString);
		          if (startDateTime == null) {
		            throw new DTIException(
		                GWQueryTicketXML.class,
		                DTIErrorCode.INVALID_MSG_CONTENT,
		                "Response GW XML DataRequestResp has unparsable StartDateTime: " + startDateTimeString);
		          }
		          dataRespTO.setStartDateTime(startDateTime);
		        }
		      }

		      // GregorianCalendar dateOpened
		      if (element.getName().compareTo("DateOpened") == 0) {
		        String dateOpenedString = element.getText();
		        GregorianCalendar dateOpened = UtilityXML
		            .getGCalFromEGalaxyDate(dateOpenedString);
		        if (dateOpened == null) {
		          throw new DTIException(
		              GWQueryTicketXML.class,
		              DTIErrorCode.INVALID_MSG_CONTENT,
		              "Response GW XML DataRequestResp has unparsable DateOpened: " + dateOpenedString);
		        }
		        dataRespTO.setDateOpened(dateOpened);
		      }

		      // GregorianCalendar expirationDate;
		      if (element.getName().compareTo("ExpirationDate") == 0) {
		        String expirationDateString = element.getText();
		        GregorianCalendar expirationDate = UtilityXML
		            .getGCalFromEGalaxyDate(expirationDateString);
		        if (expirationDate != null) {
		          dataRespTO.setExpirationDate(expirationDate);
		        }

		      }

		      // GregorianCalendar endDateTime
		      // Since 2.9 - Modified to prevent empty tags from causing failures.
		      if (element.getName().compareTo("EndDateTime") == 0) {
		        String endDateTimeString = element.getText();

		        if ((endDateTimeString != null) && (endDateTimeString.length() > 0)) {

		          GregorianCalendar endDateTime = UtilityXML
		              .getGCalFromEGalaxyDate(endDateTimeString);
		          if (endDateTime == null) {
		            throw new DTIException(
		                GWQueryTicketXML.class,
		                DTIErrorCode.INVALID_MSG_CONTENT,
		                "Response GW XML DataRequestResp has unparsable EndDateTime: " + endDateTimeString);
		          }
		          dataRespTO.setEndDateTime(endDateTime);
		        }

		      }

		      // GregorianCalendar validUntil
		      if (element.getName().compareTo("ValidUntil") == 0) {
		        String validUntilString = element.getText();
		        GregorianCalendar validUntil = UtilityXML
		            .getGCalFromEGalaxyDate(validUntilString);
		        if (validUntil == null) {
		          throw new DTIException(
		              GWQueryTicketXML.class,
		              DTIErrorCode.INVALID_MSG_CONTENT,
		              "Response GW XML DataRequestResp has unparsable ValidUntil: " + validUntilString);
		        }
		        dataRespTO.setValidUntil(validUntil);
		      }

		      // Boolean lockedOut
		      if (element.getName().compareTo("LockedOut") == 0) {
		        String lockedOutString = element.getText();
		        if (lockedOutString.equalsIgnoreCase("YES")) dataRespTO
		            .setLockedOut(Boolean.valueOf(true));
		        else if (lockedOutString.equalsIgnoreCase("NO")) dataRespTO
		            .setLockedOut(Boolean.valueOf(false));
		        else throw new DTIException(GWQueryTicketXML.class,
		            DTIErrorCode.INVALID_MSG_CONTENT,
		            "Response GW XML DataRequestResp has invalid value for LockedOut.");
		      }

		      // String visualID
		      if (element.getName().compareTo("VisualID") == 0) {
		        String visualID = element.getText();
		        dataRespTO.setVisualID(visualID);
		      }

		      // Use Count
		      if (element.getName().compareTo("UseCount") == 0) {
		        String useCountString = element.getText();
		        try {
		          Integer useCount = new Integer(useCountString);
		          dataRespTO.setUseCount(useCount);
		        }
		        catch (NumberFormatException nfe) {
		          throw new DTIException(GWQueryTicketXML.class,
		              DTIErrorCode.INVALID_MSG_CONTENT,
		              "Response GW XML DataRequestResp has non-numeric UseCount.");
		        }
		      }

		      // BigDecimal price
		      if (element.getName().compareTo("Price") == 0) {
		        String priceString = element.getText();
		        if (priceString.contains(".")) dataRespTO
		            .setPrice(new BigDecimal(priceString));
		        else dataRespTO.setPrice(new BigDecimal(priceString + ".00"));
		      }

		      // BigDecimal tax
		      if (element.getName().compareTo("Tax") == 0) {
		        String taxString = element.getText();
		        if (taxString.contains(".")) dataRespTO.setTax(new BigDecimal(
		            taxString));
		        else dataRespTO.setTax(new BigDecimal(taxString + ".00"));
		      }

		      // String First Name
		      if (element.getName().compareTo("FirstName") == 0) {
		        String firstName = element.getText();
		        dataRespTO.setFirstName(firstName);
		      }

		      // String Last Name
		      if (element.getName().compareTo("LastName") == 0) {
		        String lastName = element.getText();
		        dataRespTO.setLastName(lastName);
		      }

		      // String Street1
		      if (element.getName().compareTo("Street1") == 0) {
		        String street1 = element.getText();
		        dataRespTO.setStreet1(street1);
		      }

		      // String Street2
		      if (element.getName().compareTo("Street2") == 0) {
		        String street2 = element.getText();
		        dataRespTO.setStreet2(street2);
		      }

		      // String City
		      if (element.getName().compareTo("City") == 0) {
		        String city = element.getText();
		        dataRespTO.setCity(city);
		      }

		      // String State
		      if (element.getName().compareTo("State") == 0) {
		        String state = element.getText();
		        dataRespTO.setState(state);
		      }

		      // String ZIP
		      if (element.getName().compareTo("ZIP") == 0) {
		        String zip = element.getText();
		        dataRespTO.setZip(zip);
		      }

		      // String CountryCode
		      if (element.getName().compareTo("CountryCode") == 0) {
		        String countryCode = element.getText();
		        dataRespTO.setCountryCode(countryCode);
		      }

		      // String Phone
		      if (element.getName().compareTo("Phone") == 0) {
		        String phone = element.getText();
		        dataRespTO.setPhone(phone);
		      }

		      // String Email
		      if (element.getName().compareTo("Email") == 0) {
		        String email = element.getText();
		        dataRespTO.setEmail(email);
		      }

		      // Gregorian Calendar DOB (Date of Birth) As of 2.16.1, JTL
		      if (element.getName().equalsIgnoreCase("DOB")) {
		        String dobString = element.getText();
		        if ((dobString != null) && (dobString != "")) {
		          GregorianCalendar dateOfBirth = UtilityXML
		              .getGCalFromEGalaxyDate(dobString);
		          if (dateOfBirth == null) {
		            throw new DTIException(
		                GWQueryTicketXML.class,
		                DTIErrorCode.INVALID_MSG_CONTENT,
		                "Response GW XML DataRequestResp has unparsable DateUsed: " + dobString);
		          }
		          dataRespTO.setDateOfBirth(dateOfBirth);
		        }
		      }

		      // Gender (As of 2.16.1, JTL)
		      if (element.getName().equalsIgnoreCase("Gender")) {
		        String genderString = element.getText();
		        dataRespTO.setGenderRespString(genderString);
		      }

		      // GregorianCalendar DateUsed
		      if (element.getName().compareTo("DateUsed") == 0) {
		        String dateUsedString = element.getText();
		        if ((dateUsedString != null) && (dateUsedString != "")) {
		          GregorianCalendar dateUsed = UtilityXML
		              .getGCalFromEGalaxyDate(dateUsedString);
		          if (dateUsed == null) {
		            throw new DTIException(
		                GWQueryTicketXML.class,
		                DTIErrorCode.INVALID_MSG_CONTENT,
		                "Response GW XML DataRequestResp has unparsable DateUsed: " + dateUsedString);
		          }
		          dataRespTO.setDateUsed(dateUsed);
		        }
		      }

		      // String Kind (as of 2.11)
		      if (element.getName().compareTo("Kind") == 0) {
		        String kind = element.getText();
		        dataRespTO.setKind(kind);
		      }

		      // Integer Remaining Use (as of 2.14)
		      if (element.getName().compareTo("RemainingUse") == 0) {
		        String useString = element.getText();
		        if ((useString != null) && (useString
		            .equalsIgnoreCase("Unlimited"))) {
		          useString = "999";
		        }
		        try {
		          Integer remainingUse = new Integer(useString);
		          dataRespTO.setRemainingUse(remainingUse);
		        }
		        catch (NumberFormatException nfe) {
		          throw new DTIException(GWQueryTicketXML.class,
		              DTIErrorCode.INVALID_MSG_CONTENT,
		              "Response GW XML DataRequestResp has non-numeric RemainingUse.");
		        }
		      }

		      // String PassKindName
		      if (element.getName().compareTo("PassKindName") == 0) {
		        String passKindName = element.getText();
		        dataRespTO.setPassKindName(passKindName);
		      }

		      // Compound Tags (LineageRecords)
		      if (element.getName().compareTo("LineageRequestResponse") == 0) {
		        extractLineageInfo(dataRespTO, i, element);
		      }

		      // PM17616 - For item kind 1 end date time is not required from eGalaxy.
		      if ((itemKind == 1) && (dataRespTO.getEndDateTime() == null)) {
		        GregorianCalendar endDateTime = UtilityXML
		            .getGCalFromEGalaxyDate("2031-12-31 00:00:00");
		        dataRespTO.setEndDateTime(endDateTime);
		      }

		      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      Date dateobj = new Date();
		      String strFormatedDate = df.format(dateobj);

		      if ((itemKind == 1) && (dataRespTO.getStartDateTime() == null)) {
		        GregorianCalendar startDateTime = UtilityXML
		            .getGCalFromEGalaxyDate(strFormatedDate);
		        dataRespTO.setStartDateTime(startDateTime);
		      }

		      if ((itemKind == 1) && (dataRespTO.getTicketDate() == null)) {
		        GregorianCalendar ticketDate = UtilityXML
		            .getGCalFromEGalaxyDate(strFormatedDate);
		        dataRespTO.setTicketDate(ticketDate);
		      }
		      
		      // Adding new Tag UpgradePLUList
		      if(element.getName().compareTo("UpgradePLUList")==0){
		    	  extractUplgradePlU(dataRespTO, i, element);
		      }

		      // Adding new Tag Contact
		      if(element.getName().compareTo("Contact")==0){
		    	  extractContact(dataRespTO, i, element);
		      }

		    }

		    qtRespTO.setDataRespTO(dataRespTO);

		    return qtRespTO;
		  }
	  
	 /**
	 * It contains the list of Upgraded PLU Object 
	 * @param dataRespTO
	 * @param i
	 * @param lineageRequestResponse
	 * @throws DTIException
	 */
	private static void extractUplgradePlU(GWDataRequestRespTO dataRespTO,
	      Iterator<org.dom4j.Element> i, Element lineageRequestResponse) throws DTIException{
		  
		    // LineageRequestResponse
		    Node linReqResp = lineageRequestResponse;

		   

		    List linRecordList = linReqResp.selectNodes("Item");

		    for (int index = 0; index < linRecordList.size(); index++) {

		      Node linRecord = (Node) linRecordList.get(index);

		      // Create the inner class
		      GWDataRequestRespTO.UpgradePLUList uplUpgradePLUList = dataRespTO.new UpgradePLUList();
		      
		      // PLU
		      Node pluNode = linRecord.selectSingleNode("PLU");
		      if (pluNode != null) {
		    	  uplUpgradePLUList.setPLU(pluNode.getText());
		      }

		      // Price
		      Node priceNode = linRecord.selectSingleNode("Price");
		      if (priceNode != null) {
		        String inText = priceNode.getText();
		        //TODO add price section
		      if (inText.contains(".")) uplUpgradePLUList
		            .setPrice(new BigDecimal(inText));
		        else uplUpgradePLUList.setPrice(new BigDecimal(inText + ".00"));
		      }
		      
		      // Upgraded Price
		      Node upgdpriceNode = linRecord.selectSingleNode("UpgradePrice");
		      if (upgdpriceNode != null) {
		        String inText = upgdpriceNode.getText();
		        //TODO add price section
		      if (inText.contains(".")) uplUpgradePLUList
		            .setUpgradePrice(new BigDecimal(inText));
		        else uplUpgradePLUList.setUpgradePrice(new BigDecimal(inText + ".00"));
		      }
		      // Save it to the array
		      dataRespTO.addUpgradePLUList(uplUpgradePLUList);

		    }

		  
		  
	  }


	private static void extractContact(GWDataRequestRespTO dataRespTO,
		      Iterator<org.dom4j.Element> i, Element lineageRequestResponse) throws DTIException{
		
		 // LineageRequestResponse
	    Node linReqResp = lineageRequestResponse;
		
	    List linRecordList = linReqResp.selectNodes("Contact");

	    for (int index = 0; index < linRecordList.size(); index++) {

	      Node linRecord = (Node) linRecordList.get(index);
	      
	      // Create the inner class
	      GWDataRequestRespTO.Contact contact = dataRespTO.new Contact();
	      
	      // FirstName
	      Node firstNameNode = linRecord.selectSingleNode("FirstName");
	      if (firstNameNode != null) {
	    	  contact.setFirstName(firstNameNode.getText());
	      }
	      
	      // MiddleName
	      Node middleNameNode = linRecord.selectSingleNode("MiddleName");
	      if (middleNameNode != null) {
	    	  contact.setMiddleName(middleNameNode.getText());
	      }
	      
	      // LastName
	      Node lastNameNode = linRecord.selectSingleNode("LastName");
	      if (lastNameNode != null) {
	    	  contact.setLastName(lastNameNode.getText());
	      } 
	      
	      // IdentificationNo
	      Node identificatioNo = linRecord.selectSingleNode("IdentificationNo");
	      if (identificatioNo != null) {
	    	  contact.setIdentificationNo(identificatioNo.getText());
	      }
	      
	      // Street1
	      Node street1 = linRecord.selectSingleNode("Street1");
	      if (street1 != null) {
	    	  contact.setStreet1(street1.getText());
	      }
	      
	      // Street2
	      Node street2 = linRecord.selectSingleNode("Street2");
	      if (street2 != null) {
	    	  contact.setStreet2(street2.getText());
	      } 
	      
	      // Street3
	      Node street3 = linRecord.selectSingleNode("Street3");
	      if (street3 != null) {
	    	  contact.setStreet3(street3.getText());
	      }
	      
	      // City
	      Node cityNode = linRecord.selectSingleNode("City");
	      if (cityNode != null) {
	    	  contact.setCity(cityNode.getText());
	      }
	      
	      // State
	      Node stateNode = linRecord.selectSingleNode("State");
	      if (stateNode != null) {
	    	  contact.setState(stateNode.getText());
	      }
	      
	      //ZIP
	      Node zipNode = linRecord.selectSingleNode("ZIP");
	      if (zipNode != null) {
	    	  contact.setZip(zipNode.getText());
	      }
	      
	      //CountryCode
	      Node countryCodeNode = linRecord.selectSingleNode("CountryCode");
	      if (countryCodeNode != null) {
	    	  contact.setCountryCode(countryCodeNode.getText());
	      }
	      
	      //Phone
	      Node phoneNode = linRecord.selectSingleNode("Phone");
	      if (phoneNode != null) {
	    	  contact.setPhone(phoneNode.getText());
	      }
	      
	      //Fax
	      Node faxNode = linRecord.selectSingleNode("FaxNode");
	      if (faxNode != null) {
	    	  contact.setFax(faxNode.getText());
	      }
	      
	      //Cell
	      Node cellNode = linRecord.selectSingleNode("Cell");
	      if (cellNode != null) {
	    	  contact.setCell(cellNode.getText());
	      }
	      
	      //Email
	      Node emailNode = linRecord.selectSingleNode("Email");
	      if (emailNode != null) {
	    	  contact.setEmail(emailNode.getText());
	      }
	      
	      //ExternalID
	      Node externalIDNode = linRecord.selectSingleNode("ExternalID");
	      if (externalIDNode != null) {
	    	  contact.setExternalId(externalIDNode.getText());
	      }
	      
	      //ContactGUID
	      Node contactGUIDNode = linRecord.selectSingleNode("ContactGUID");
	      if (contactGUIDNode != null) {
	    	  contact.setContactGUID(contactGUIDNode.getText());
	      } 
	      
	      //GalaxyContactID
	      Node galaxyContactIDNode = linRecord.selectSingleNode("GalaxyContactID");
	      if (galaxyContactIDNode != null) {
	    	  contact.setGalaxyContactId(galaxyContactIDNode.getText());
	      } 
	      
	      //JobTitle
	      Node jobTitleNode = linRecord.selectSingleNode("JobTitle");
	      if (jobTitleNode != null) {
	    	  contact.setJobTitle(jobTitleNode.getText());
	      }  
	      
	      //Primary
	      Node pimaryNode = linRecord.selectSingleNode("Primary");
	      if (pimaryNode != null) {
	    	  contact.setPrimary(pimaryNode.getText());
	      } 
	      
	      //ContactNote
	      Node contactNoteNode = linRecord.selectSingleNode("ContactNote");
	      if (contactNoteNode != null) {
	    	  contact.setContactNote(contactNoteNode.getText());
	      }  
	      
	      //NameTitleID
	      Node nameTitleIDNode = linRecord.selectSingleNode("NameTitleID");
	      if (nameTitleIDNode != null) {
	    	  contact.setNameTitleId(nameTitleIDNode.getText());
	      }  
	      
	      //NameSuffixID
	      Node nameSuffixIDNode = linRecord.selectSingleNode("NameSuffixID");
	      if (nameSuffixIDNode != null) {
	    	  contact.setNameSuffixId(nameSuffixIDNode.getText());
	      }  
	      
	      //TotalPaymentContracts
	      Node totalPaymentContractsNode = linRecord.selectSingleNode("TotalPaymentContracts");
	      if (totalPaymentContractsNode != null) {
	    	  contact.setTotalPaymentContracts(totalPaymentContractsNode.getText());
	      } 
	      
	      //AllowEmail
	      Node allowEmailNode = linRecord.selectSingleNode("AllowEmail");
	      if (allowEmailNode != null) {
	    	  contact.setAllowMail(allowEmailNode.getText());
	      }  
	      
	      //AllowMailings
	      Node allowMailingsNode = linRecord.selectSingleNode("AllowMailings");
	      if (allowMailingsNode != null) {
	    	  contact.setAllowMailing(allowMailingsNode.getText());
	      }  
	      
	      //DOB
	      Node DOBNode = linRecord.selectSingleNode("DOB");
	      if (DOBNode != null) {
	    	  contact.setDob(DOBNode.getText());
	      }  
	      
	      //AgeGroup
	      Node ageGroupNode = linRecord.selectSingleNode("AgeGroup");
	      if (ageGroupNode != null) {
	    	  contact.setAgeGroup(ageGroupNode.getText());
	      }
	      
	      
	      //Gender
	      Node genderNode = linRecord.selectSingleNode("Gender");
	      if (genderNode != null) {
	    	  contact.setGender(genderNode.getText());
	      }
	      
	      // Save it to the array
	      dataRespTO.addContactList(contact);

	    }
	    }
	    
	  /**
	   * Extracts the lineage info (this structure is complex).
	   * 
	   * @param dataRespTO
	   * @param i
	   * @param linReqResp
	   * @throws DTIException
	   * 
	   *             <LineageRequestResponse> <LineageRecords> <LineageRecord> <Amount>199</Amount> <ExpirationDate>2012-08-24 00:00:00</ExpirationDate> <Status>7</Status> <Valid>NO</Valid> <VisualID>2937555200149073829</VisualID>
	   *             </LineageRecord>
	   * 
	   */
	  @SuppressWarnings("rawtypes")
	  private static void extractLineageInfo(GWDataRequestRespTO dataRespTO,
	      Iterator<org.dom4j.Element> i, Element lineageRequestResponse) throws DTIException {

	    // LineageRequestResponse
	    Node linReqResp = lineageRequestResponse;

	    // LineageRecords
	    Node linRecords = linReqResp.selectSingleNode("LineageRecords");
	    if (linRecords == null) {
	      return;
	    }

	    List linRecordList = linRecords.selectNodes("LineageRecord");

	    for (int index = 0; index < linRecordList.size(); index++) {

	      Node linRecord = (Node) linRecordList.get(index);

	      // Create the inner class
	      GWDataRequestRespTO.LineageRecord lineageRecordTO = dataRespTO.new LineageRecord();

	      // Amount
	      Node amountNode = linRecord.selectSingleNode("Amount");
	      if (amountNode != null) {
	        String inText = amountNode.getText();
	        if (inText.contains(".")) lineageRecordTO
	            .setAmount(new BigDecimal(inText));
	        else lineageRecordTO.setAmount(new BigDecimal(inText + ".00"));
	      }

	      // Expiration Date
	      Node expireNode = linRecord.selectSingleNode("ExpirationDate");
	      if (expireNode != null) {
	        String inText = expireNode.getText();
	        if ((inText != null) && (inText != "")) {
	          GregorianCalendar expDate = UtilityXML
	              .getGCalFromEGalaxyDate(inText);
	          if (expDate == null) {
	            throw new DTIException(
	                GWQueryTicketXML.class,
	                DTIErrorCode.INVALID_MSG_CONTENT,
	                "Response GW XML LineageRecord has unparsable ExpirationDate: " + inText);
	          }
	          lineageRecordTO.setExpirationDate(expDate);
	        }
	      }

	      // Status
	      Node statusNode = linRecord.selectSingleNode("Status");
	      if (statusNode != null) {
	        String inText = statusNode.getText();
	        try {
	          lineageRecordTO.setStatus(Integer.decode(inText));
	        }
	        catch (NumberFormatException nfe) {
	          throw new DTIException(GWQueryTicketXML.class,
	              DTIErrorCode.INVALID_MSG_CONTENT,
	              "Response GW XML LineageRecord has non-numeric Status.");
	        }
	      }

	      // Valid
	      Node validNode = linRecord.selectSingleNode("Valid");
	      if (validNode != null) {
	        String validString = validNode.getText();
	        if (validString.equalsIgnoreCase("YES")) lineageRecordTO
	            .setValid(Boolean.valueOf(true));
	        else if (validString.equalsIgnoreCase("NO")) lineageRecordTO
	            .setValid(Boolean.valueOf(false));
	        else {
	          throw new DTIException(GWQueryTicketXML.class,
	              DTIErrorCode.INVALID_MSG_CONTENT,
	              "Response GW XML LineageRecord has invalid value for Valid.");
	        }
	      }

	      // VisualID
	      Node visualIDNode = linRecord.selectSingleNode("VisualID");
	      if (visualIDNode != null) {
	        String inText = visualIDNode.getText();
	        lineageRecordTO.setVisualID(inText);
	      }

	      // Save it to the array
	      dataRespTO.addLineageRecord(lineageRecordTO);

	    }

	  }

	  /**
	   * Unmarshall the errors element into the gateway query ticket errors transfer object.
	   * 
	   * @param errorsElement
	   *            as provided by the parsed eGalaxy response.
	   * @return into the gateway query ticket errors transfer object.
	   * @throws DTIException
	   *             should any unmarshalling problem occurs.
	   */
	  private static GWQueryTicketErrorsTO setErrorsTO(Element errorsElement) throws DTIException {

	    // System.err.println("GWQueryTiketXML.setErrorsTO()");
	    // System.err.println("incoming errorsElement:\n'" + errorsElement.asXML() +
	    // "'");
	    GWQueryTicketErrorsTO errorsTO = new GWQueryTicketErrorsTO();

	    // the errorCode we will hand back after checking for special errors
	    // if no special errors found, it is the statusCode
	    String errorCode = null;
	    // the error text for an errorCode (if found)...grabbed for logging
	    String errorText = null;

	    // response document to check for errors

	    // if all went well we can start hunting for errors
	    if (errorsElement != null) {
	      // System.err.println("errsElement not null:" + errorsElement);
	      logger.sendEvent(
	          "QueryTicket Errors element:" + errorsElement.asXML(),
	          EventType.DEBUG, logInstance);
	      // }

	      // and try to grab //QueryTicketErrors/ERror/Error/ErrorCode
	      org.dom4j.Node queryTickerErrorCode = errorsElement
	          .selectSingleNode("//Errors/Error/ErrorCode");

	      // if we found one....
	      if (queryTickerErrorCode != null && queryTickerErrorCode.getText() != null && queryTickerErrorCode
	          .getText().length() > 0) {
	        // System.err.println("found errrorCode:" +
	        // queryTickerErrorCode.asXML());
	        // grab the details for the error
	        errorCode = queryTickerErrorCode.getText();
	        // System.err.println("qte errorCode:" + errorCode);
	        // System.err.println("seeking errorText:");
	        errorText = errorsElement.selectSingleNode(
	            "//Errors/Error/ErrorText").getText();

	        // System.err.println("qte errorText:" + errorText);
	        // and log it
	        logger.sendEvent(
	            "GWQueryTicketXML.setErrorsTO() found a QueryTicketError: " + errorCode + ":" + errorText,
	            EventType.DEBUG, logInstance);
	      }
	      // if we didnt find QueryTicketErrors/Errors/Error/ErrorCode we need to
	      // check for
	      // QueryTicketErrors/DataRequestErrors
	      else if (errorsElement
	          .selectSingleNode("//DataRequestErrors/DataRequestError/ErrorCode") != null) {
	        // System.err.println("seeking datarequesterrors");
	        // if we have a DataRequestError, grab its details
	        errorCode = errorsElement.selectSingleNode(
	            "//DataRequestErrors/DataRequestError/ErrorCode")
	            .getText();
	        // System.err.println("datarequest error errorCode:" + errorCode);
	        errorText = errorsElement.selectSingleNode(
	            "//DataRequestErrors/DataRequestError/ErrorText")
	            .getText();
	        // System.err.println("datarequest error errorText:" + errorText);
	        // and log it
	        logger.sendEvent(
	            "GWQueryTicketXML.setErrorTO() found a DataRequestError: " + errorCode + ":" + errorText,
	            EventType.DEBUG, logInstance);
	      }

	    }

	    // check for bad xml construct
	    if (errorCode == null) {
	      throw new DTIException(
	          GWHeaderTO.class,
	          DTIErrorCode.TP_INTERFACE_FAILURE,
	          "Ticket provider returned QueryTicketError,Errors,Error clause without ErrorCode clause.");
	    }

	    // check for bad xml construct
	    if (errorText == null) {
	      throw new DTIException(
	          GWHeaderTO.class,
	          DTIErrorCode.TP_INTERFACE_FAILURE,
	          "Ticket provider returned QueryTicketError,Errors,Error clause without ErrorText clause.");
	    }

	    errorsTO.setErrorCode(errorCode);
	    errorsTO.setErrorText(errorText);

	    return errorsTO;

	  }



}
