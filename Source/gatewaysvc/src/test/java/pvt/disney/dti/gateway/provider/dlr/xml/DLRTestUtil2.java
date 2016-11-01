package pvt.disney.dti.gateway.provider.dlr.xml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.HTMLWriter;

import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
 
/**
 * This class provides utilities to the gateway/DLR testing suite.
 * @author smoon
 *
 */
public class DLRTestUtil2 {
	//BEGIN CONSTANTS
	//message types
	  public final static String HEADER = "Header";
	  /** Message Type - Query Ticket Request */
	  public  final static String MESSAGETYPE_QRY = "QueryTicket";
	  /** Message Type - Query Ticket Response */
	  public  final static String MESSAGETYPE_QRYRSP = "QueryTicketResponse";
	  /** Message Type - Activate Ticket Request */
	  public  final static String MESSAGETYPE_TICKET_ACTIVATION = "ActivateTicket";
	  /** Message Type - Activate Ticket Response */
	  public  final static String MESSAGETYPE_TICKET_ACTIVATION_RSP = "ActivateTicketResponse";
	  /** Message Type - Orders  */
	  public  final static String MESSAGETYPE_ORDERS = "Orders";
	  /** Message Type - Orders Response */	  
	  public  final static String MESSAGETYPE_ORDERS_RSP = "OrdersResponse";
	  //END CONSTANTS
	  
    //values read from property file
	public static String SOURCEID;
	public static Long   MESSAGEID;
	public static String MESSAGETYPE;
	public static String ORDERID;
	public static String CUSTOMERID;
	public static String GROUPVISITDATE;
	public static String ORDER_PREFIX;
	
	//values read from property file, just for Orders msgs (ie DIT create res)
	//consider moving this to an TestOrder interface to make this class less ugly
	public static String CONTACT_FIRSTNAME;
	public static String CONTACT_LASTNAME;
	public static String CONTACT_STREET1;
	public static String CONTACT_CITY;
	public static String CONTACT_STATE;
	public static String CONTACT_ZIP;
	public static String CONTACT_COUNTRY;
	public static String CONTACT_PHONE;
	public static String CONTACT_EMAIL;
	public static String SHIPTO_FIRSTNAME;
	public static String SHIPTO_LASTNAME;
	public static String SHIPTO_STREET1;
	public static String SHIPTO_CITY;
	public static String SHIPTO_STATE;
	public static String SHIPTO_ZIP;
	public static String SHIPTO_COUNTRY;
	public static String SHIPTO_PHONE;
	public static String DELIVERY_METHOD;
	public static String DELIVERY_DETAILS;
  //making some of the test values non-final so we can ovveride them and test
  //cases don't have to used hard-coded values
  //public static  String CUSTOMERID = "300010434";
  
  //public static  String TIMESTAMPREQ = "2009-01-23 11:38:20.123";
  //public static  String TIMESTAMPRSP = "2009-01-23 11:38:20";
  public static  String VISUALID = "5555555555555555";

  
  public  static GWDataRequestRespTO.ItemKind ITEMKIND = GWDataRequestRespTO.ItemKind.PASS;
  public  static Boolean RETURNABLE = new Boolean(true);
  public  static GWDataRequestRespTO.Status STATUS = GWDataRequestRespTO.Status.VALID;
  public  static String DATESOLD = "2009-03-14 07:08:09";
  public  static String TICKETDATE = "2009-03-14 00:00:00";
  public  static String STARTDATETIME = "2009-03-14 00:00:00";
  public  static String DATEOPENED = "2009-03-14 00:00:00";
  public  static String EXPIRATIONDATE = "2009-03-14 00:00:00";
  public  static String ENDDATETIME = "2009-03-14 00:00:00";
  public  static String VALIDUNTIL = "2010-03-14 00:00:00";
  public  static Boolean LOCKEDOUT = new Boolean(false);
  public  static BigDecimal PRICE = new BigDecimal("134");
  public  static Integer USECOUNT = new Integer(0);
  public  static BigDecimal TAX = new BigDecimal("0");
  public  static String STATUSCODEOK = "0";
  public  static String STATUSTEXTOK = "OK";
  public  static String STATUSCODEERR = "1300";
  public  static String STATUSTEXTERR = "General request error";
  public  static String ERRORCODE = "999";
  public  static String ERRORTEXT = "An error occurred while processing the request.";
  
  ///some error constants used to validate error responses
	public static  String TICKET_ACTIVATION_FAILED_CODE = "1400";
	/** constant used for checking status code errors */
	public static  String TICKET_ACTIVATION_NOT_PERMITTED_CODE = "1403";
	/** constant used for checking status code errors */
	public static  String TICKET_ACTIVATION_DOES_NOT_EXIST_CODE = "1404";
	/** constant used for checking status code errors */
	public static  String TICKET_ACTIVATION_ALREADY_VOIDED_CODE = "1420";
	/** constant used for checking status code errors */
	public static  String TICKET_ACTIVATION_TICKET_ALREADY_ACTIVE_CODE = "1405";
	/** constant used for checking status code errors */
	public static  String TICKET_ACTIVATION_TICKET_PRICE_MISMATCH_CODE= "1409";
	/** constant used for checking status code errors */
	public static  String TICKET_ACTIVATION_PAYMENT_MISMATCH_CODE = "1414";

	public static String ORDERS_ALREADY_EXISTS_CODE = "107";
    public static String ORDERS_ARRIVAL_DATE_CODE = "114";
    public static String ORDERS_ORDER_OUT_OF_RANGE="118"; //bad prefix
    public static String ORDER_AVS_MISTMATCH="128";
	
	public static void init(String resourceBundle) {
		ResourceBundle rb = ResourceBundle.getBundle(resourceBundle);
		//refactor to iterate through and/or use reflection
		//just going to hard code now for expediency
		SOURCEID = rb.getString("SOURCEID");
		MESSAGEID= Long.valueOf(rb.getString("MESSAGEID"));
		MESSAGETYPE = rb.getString("MESSAGETYPE");
		ORDERID = rb.getString("ORDERID");
		CUSTOMERID = rb.getString("CUSTOMERID");
		ORDER_PREFIX =  rb.getString("ORDER_PREFIX");
		
		//SHAN this should really be refactored to contact obhects and
		//done in a helper method...this is too much ugliness going on in the fx()
		if (MESSAGETYPE.equals(MESSAGETYPE_ORDERS)) {
			initOrders(rb);
		
		}
		// think about how to iterate through orderline items, tokens or arrays?
		
		//TIMESTAMPREQ
//		     GWDataRequestRespTO.ItemKind ITEMKIND = GWDataRequestRespTO.ItemKind.PASS;
//		     Boolean RETURNABLE = new Boolean(true);
//		     GWDataRequestRespTO.Status STATUS = GWDataRequestRespTO.Status.VALID;
//		     String DATESOLD = "2009-03-14 07:08:09";
//		     String TICKETDATE = "2009-03-14 00:00:00";
//		     String STARTDATETIME = "2009-03-14 00:00:00";
//		     String DATEOPENED = "2009-03-14 00:00:00";
//		     String EXPIRATIONDATE = "2009-03-14 00:00:00";
//		     String ENDDATETIME = "2009-03-14 00:00:00";
//		     String VALIDUNTIL = "2010-03-14 00:00:00";
//		     Boolean LOCKEDOUT = new Boolean(false);
//		     BigDecimal PRICE = new BigDecimal("134");
//		     Integer USECOUNT = new Integer(0);
//		     BigDecimal TAX = new BigDecimal("0");
//		     String STATUSCODEOK = "0";
//		     String STATUSTEXTOK = "OK";
//		     String STATUSCODEERR = "1300";
//		     String STATUSTEXTERR = "General request error";
//		     String ERRORCODE = "999";
//		     String ERRORTEXT = "An error occurred while processing the request.";
	}
	
	public static void initOrders(ResourceBundle rb) {
		GROUPVISITDATE = rb.getString("GROUPVISITDATE");
		CONTACT_FIRSTNAME= rb.getString("CONTACT_FIRSTNAME");
		CONTACT_LASTNAME= rb.getString("CONTACT_LASTNAME");
		CONTACT_STREET1= rb.getString("CONTACT_STREET1");
		CONTACT_CITY= rb.getString("CONTACT_CITY");
		CONTACT_STATE= rb.getString("CONTACT_STATE");
		CONTACT_ZIP= rb.getString("CONTACT_ZIP");
		CONTACT_COUNTRY= rb.getString("CONTACT_COUNTRY");
		CONTACT_PHONE= rb.getString("CONTACT_PHONE");
		CONTACT_EMAIL= rb.getString("CONTACT_EMAIL");
		
		SHIPTO_FIRSTNAME= rb.getString("SHIPTO_FIRSTNAME");
		SHIPTO_LASTNAME= rb.getString("SHIPTO_LASTNAME");
		SHIPTO_STREET1= rb.getString("SHIPTO_STREET1");
		SHIPTO_CITY= rb.getString("SHIPTO_CITY");
		SHIPTO_STATE= rb.getString("SHIPTO_STATE");
		SHIPTO_ZIP= rb.getString("SHIPTO_ZIP");
		SHIPTO_COUNTRY= rb.getString("SHIPTO_COUNTRY");
		SHIPTO_PHONE= rb.getString("SOURCESHIPTO_PHONEID");
		
		DELIVERY_METHOD=rb.getString("DELIVERY_METHOD");
		DELIVERY_DETAILS=rb.getString("DELIVERY_DETAILS");
		

	}
  /**
   * Gets the xml from file.
   * 
   * @param fileName the file name
   * 
   * @return the XM lfrom file
   * 
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static String getXMLfromFile(String fileName) throws IOException {

    StringBuffer sb = new StringBuffer();

    try {

      FileReader file = new FileReader(fileName);
      BufferedReader buff = new BufferedReader(file);
      boolean eof = false;
      while (!eof) {
        String line = buff.readLine();
        if (line == null)
          eof = true;
        else
          sb.append(line);
      }

      buff.close();
      file.close();

    } catch (IOException ioe) {
      throw ioe;
    }

    String xml = sb.toString();

    return xml;

  }

  /**
   * Validate external tkt id.
   * 
   * @param aTicketTO the a ticket to
   * 
   * @throws Exception the exception
   */
  public static void validateExternalTktId(TicketTO aTicketTO) throws Exception {

    ArrayList<TicketTO.TicketIdType> ticketTypes = aTicketTO.getTicketTypes();
    boolean hasExternalTktId = false;

    for /* each */(TicketTO.TicketIdType aType : /* in */ticketTypes) {
      if (aType == TicketTO.TicketIdType.EXTERNAL_ID)
        hasExternalTktId = true;
    }

    if (!hasExternalTktId)
      throw new Exception("Ticket does not have expected External TktId.");

    if (aTicketTO.getExternal().compareTo(DLRTestUtil.VISUALID) != 0)
      throw new Exception("Invalid External Ticket Value: " + aTicketTO.getExternal());

    return;
  }

  /**
   * Validate xml headers.
   * 
   * @param baselineXML the baseline xml
   * @param newXML the new xml
   * 
   * @throws Exception the exception
   */
  public static void validateXMLHeaders(String baselineXML, String newXML) throws Exception {
    String basePayHdr = DLRTestUtil.findTag(baselineXML, DLRTestUtil.HEADER);
    String newPayHdr = DLRTestUtil.findTag(newXML, DLRTestUtil.HEADER);

    DLRTestUtil.compareXML(basePayHdr, newPayHdr, DLRTestUtil.HEADER);

    return;
  }
  
  
  /**
   * A simple routine to locate the value of a unique XML tag in an XML string.
   * returns empty string if tag is empty.
   * 
   * @param xml
   *          All of the XML to be searched as one string.
   * @param tagName
   *          Name of the tag being searched. Don't include greater than, less
   *          than, or slash.
   * @return the String value of the desired tag (empty string is returned for
   *         an empty XML tag or null if not present).
   */
  public static String findTag(String xml, String tagName) {

    // Locate the tag in the string.
    int startErrorTag = xml.indexOf("<" + tagName + ">");
    int endErrorTag = xml.indexOf("</" + tagName + ">");

    if ((startErrorTag == -1) || (endErrorTag == -1)) {
      if (xml.indexOf("<" + tagName + "/>") != -1)
        return "";
      else
        return null;
    }

    // Move pointer to start of the value.
    startErrorTag += tagName.length() + 2;

    if (startErrorTag == endErrorTag) {
      return "";
    }

    String tagString = xml.substring(startErrorTag, endErrorTag);

    return tagString;
  }

  /**
   * NOTE: This will remove trailing whitespace at the end of an XML value.
   * Please use another method to validate those tags.
   * 
   * @param inXML the in xml
   * 
   * @return the string
   */
  private static String clearXMLWhiteSpace(String inXML) {

    // Remove all white space between tags
    String workString = inXML.trim();
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < workString.length(); i++) {

      if (workString.charAt(i) == '<') {
        String tempString = sb.toString().trim();
        sb = new StringBuffer(tempString);
      }

      sb.append(workString.charAt(i));

    }

    String test = sb.toString();

    return test;
  }

  /**
   * Compare xml.
   * 
   * @param newXML the new xml
   * @param baselineXML the baseline xml
   * @param sectionName the section name
   * 
   * @throws Exception the exception
   */
  public static void compareXML(String baselineXML, String newXML, String sectionName) throws Exception {

     String trimBase = clearXMLWhiteSpace(baselineXML);
     String trimNew = clearXMLWhiteSpace(newXML);
     StringBuffer exceptionMessage = new StringBuffer();
     int compareLength = 0;
          
     if (trimBase.compareTo(trimNew) != 0) {
       
       if (trimBase.length() < trimNew.length())
          compareLength = trimBase.length();
       else
          compareLength = trimNew.length();
     
       for (int i = 0; i < compareLength; i++ ) {
         
         if (trimBase.charAt(i) != trimNew.charAt(i)) {
           
           exceptionMessage.append("Invalid XML generated in section " + sectionName + ": ");
           exceptionMessage.append("\nBaseline XML Version:\n");
           String baseVersion = null;
           
           if (trimBase.length() < 60)
             baseVersion = trimBase;
           else if ( ((i - 30) < 0) && ((i + 31) < trimBase.length()))   
             baseVersion = trimBase.subSequence(0, i + 30).toString();
           else if ( ((i - 30) >= 0) && ((i + 31) > trimBase.length()))
             baseVersion = trimBase.subSequence(i - 30, trimBase.length() -1).toString();
           else if ( ((i - 30) > 0) && ((i + 31) < trimBase.length()))
             baseVersion = trimBase.subSequence(i - 30, i + 30).toString();
  
           exceptionMessage.append(baseVersion);
           
           exceptionMessage.append("\nNew XML Version:\n");
           
           String newVersion = null;
           if (trimNew.length() < 60)
             newVersion = trimNew;
           else if ( ((i - 30) < 0) && ((i + 31) < trimNew.length()))   
             newVersion = trimNew.subSequence(0, i + 30).toString();
           else if ( ((i - 30) >= 0) && ((i + 31) > trimNew.length()))
             newVersion = trimNew.subSequence(i - 30, trimNew.length() -1).toString();
           else if ( ((i - 30) > 0) && ((i + 31) < trimNew.length()))
             newVersion = trimNew.subSequence(i - 30, i + 30).toString();
  
           exceptionMessage.append(newVersion);
           
           throw new Exception(exceptionMessage.toString());
           
         } 
                  
       }
       
     }
    
  }
  
	/**
	 * Helper method to get an egalaxy Query message, used by main() for
	 * demonstration purposes.
	 * @return
	 */
	public String buildDLRQuery() {
		//TO DO should refactor this to read it from the resource directory
		//an update timestamp to now
		String msg = "<Envelope>" + "<Header>"
		+ "<SourceID>WDPRONADLR</SourceID>"
		+ "<MessageID>290118</MessageID>"
		+ "<TimeStamp>" + getDLRTimeStamp() +"</TimeStamp>"
		+ "<MessageType>QueryTicket</MessageType>" + "</Header>"
		+ "<Body>" + "<QueryTicket>" + "<Query>"
		+ "<VisualID>210000050200001352</VisualID>" + "</Query>"
		+ "<DataRequest>" + "<Field>ItemKind</Field>"
		+ "<Field>Returnable</Field>" + "<Field>Status</Field>"
		+ "<Field>DateSold</Field>" + "<Field>TicketDate</Field>"
		+ "<Field>StartDateTime</Field>" + "<Field>DateOpened</Field>"
		+ "<Field>ExpirationDate</Field>"
		+ "<Field>EndDateTime</Field>" + "<Field>ValidUntil</Field>"
		+ "<Field>LockedOut</Field>" + "<Field>VisualID</Field>"
		+ "<Field>Price</Field>" + "<Field>UseCount</Field>"
		+ "<Field>Tax</Field>" + "</DataRequest>" + "</QueryTicket>"
		+ "</Body>" + "</Envelope>";
		
		return msg;
	}
	
	public static String prettyPrint(String xml) {
		StringWriter sw = new StringWriter();
	     try {
			
			 org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
			 // These are the default values for createPrettyPrint,
			 // so you needn't set them:
			 // format.setNewlines(true);
			 // format.setTrimText(true);</font>
			 format.setXHTML(true);
			 HTMLWriter writer = new HTMLWriter(sw, format);
			 org.dom4j.Document document = DocumentHelper.parseText(xml);
			 writer.write(document);
			 writer.flush();
			 
		} catch (DocumentException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	 }
	
	/**
	 * Hepler method.
	 * Gets a DLR style timestamp for sending a test message.
	 * @return
	 */
	public static String getDLRTimeStamp() {
		String dlrTS = null;
		//<TimeStamp>2004-02-04 18:01:09</TimeStamp>
		 Date date = new Date();
		 Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");

		 dlrTS = formatter.format(date);
		Logger.getAnonymousLogger().info("Timestamp="+dlrTS);
		return dlrTS;
		
	}

	/**
	 * Helper methods
	 * Get a DTI tag formatted date (i.e. CmdDate)
	 * @return
	 */
	public static String getDtiDate() {
		String dtiDate = null;
		Date date = new Date();
		 Format formatter = new SimpleDateFormat("yyyy-MM-dd");
		 dtiDate = formatter.format(date);
		 Logger.getAnonymousLogger().info("dtiDate="+dtiDate);
		 return dtiDate;
	}
	
	/**
	 * Get a DTI tag formatted time (i.e. CmdTime)
	 * @return
	 */
	public static String getDtiTime() {
		String dtiTime = null;
		 Date date = new Date();
		 Format formatter = new SimpleDateFormat("HH:MM:ss");

		 dtiTime = formatter.format(date)+ ".01";
		Logger.getAnonymousLogger().info("dtiTime="+dtiTime );
		return dtiTime;
	}
	
	/**
	 * Helper method...
	 * get a DTI payload number that is unique for testing
	 * @return
	 */
	public static String getDtiPayloadNumber() {
		String payload = null;
		 Date date = new Date();
		 Format formatter = new SimpleDateFormat("yyyyMMddHHMMssSS");

		 payload = formatter.format(date);
		Logger.getAnonymousLogger().info("payload="+payload);
		return payload;
	}
	
	/**
	 * Helper method
	 * generate a unique dlr order id, since egalaxy is smarter than ats
	 * and rejects duplicate order numbers
	 * @return
	 */
	public static String getDLROrderID() {
		return ORDER_PREFIX + getDtiPayloadNumber();
	}


}
