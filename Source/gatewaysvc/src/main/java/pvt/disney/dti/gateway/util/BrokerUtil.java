package pvt.disney.dti.gateway.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.disney.logging.EventLogger;
import com.disney.logging.audit.EventType;

/**
 * This class should contain anything related to the broker identity and 
 * broker identity management. 
 * @author lewit019
 *
 */
public class BrokerUtil {

   /** The object used for logging. */
   private static final BrokerUtil THISOBJ = new BrokerUtil();
   
   /** The event logger. */
   private static final EventLogger logger = EventLogger
         .getLogger("pvt.disney.dti.gateway.util.BrokerUtil");
   
   /** Constant for maximum length of broker. **/
   private static final int MAXBROKERLENGTH = 8;

   /** 
    * Basic constructor.
    */
   private BrokerUtil() {
      super();
   }
   
   /**
    * Get the broker name from the network interface. 
    * @return constructed TktBroker field to be used in the response
    */
   public static String getBrokerName()  {      
       
       String computerName = "DTIUNK";
       
       try {          
           computerName = InetAddress.getLocalHost().getHostName();
       } catch (UnknownHostException uhex) {
           logger.sendEvent("Not able to lookup server name from the network interface! Returning default: " 
               + computerName, 
               EventType.WARN, THISOBJ);
           return computerName;
       }
       
       char[] nameArray = computerName.toCharArray();
       StringBuffer suffixArray = new StringBuffer();
       boolean foundDigit = false;
         
       // Get only the numeric portion of the value...
       for (char aChar: nameArray) {
           
           if (Character.isDigit(aChar)) {
             foundDigit = true;  
           }
           
           if (foundDigit == true) {
             suffixArray.append(Character.toUpperCase(aChar));
             foundDigit = false;
           }        
       }

       // First part of the computerName field based on the numeric part of server name
       computerName = "" + suffixArray.toString();
       
       // Lookup JVM name using System properties (startup argument), if available
       String jvmName = System.getProperty("MW_NAME");
       
       // Ensure jvmName parameter is passed and its length is greater than 1
       if ((null != jvmName) && (!"".equals(jvmName)) && (jvmName.length() > 1)) {
         // Take the last 2 characters of the jvmName argument
         jvmName = jvmName.substring(jvmName.length() - 2, jvmName.length());
         // Second part of the computerName field based on the ending of the JVM name
         computerName = computerName + '_' + jvmName;
       }
       
       // Ensure that length of broker never exceeds DB maximums.
       if (computerName.length() > MAXBROKERLENGTH) {
          computerName = computerName.substring(computerName.length() - MAXBROKERLENGTH);
       }
       
       logger.sendEvent("Broker name returned: " + computerName, EventType.DEBUG, THISOBJ);

       return computerName;
   }
   
}
