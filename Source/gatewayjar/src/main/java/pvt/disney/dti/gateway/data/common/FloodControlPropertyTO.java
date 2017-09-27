package pvt.disney.dti.gateway.data.common;

import java.util.HashMap;

/**
 * The Class FloodControlPropertyTO.
 * 
 * @author MISHP012
 */
public class FloodControlPropertyTO {

   /** The Constant serialVersionUID. */
   final static long serialVersionUID = 9129231995L;

   /** The key frequency limit WDWQT (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWQT = 6;

   /** The key frequency limit DLRVT. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRVT = 6;

   /** The key frequency limit WDWCT. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWCT = 6;

   /** The key frequency limit WDWUA. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWUA = 6;

   /** The key frequency limit WDWUT. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWUT = 6;

   /** The key frequency limit WDWUX. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWUX = 6;

   /** The key frequency limit WDWVT. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWVT = 6;

   /** The key frequency limit WDWRR. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWRR = 6;

   /** The key frequency limit WDWQR. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWQR = 6;

   /** The key frequency limit WDWUE. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWUE = 6;

   /** The key frequency limit WDWRE. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWRE = 6;

   /** The key frequency limit WDWAM. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWAM = 6;

   /** The key frequency limit WDWTE. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWTE = 6;

   /** The key frequency limit WDWVR. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWVR = 6;

   /** The key frequency limit DLRCT. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRCT = 6;

   /** The key frequency limit DLRUA. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRUA = 6;

   /** The key frequency limit DLRUT. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRUT = 6;

   /** The key frequency limit DLRUX. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRUX = 6;

   /** The key frequency limit DLRQT. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRQT = 6;

   /** The key frequency limit DLRRR. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRRR = 6;

   /** The key frequency limit DLRQR. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRQR = 6;

   /** The key frequency limit DLRUE. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRUE = 6;

   /** The key frequency limit DLRRE. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRRE = 6;

   /** The key frequency limit DLRAM. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRAM = 6;

   /** The key frequency limit DLRTE. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRTE = 6;

   /** The key frequency limit DLRVR. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRVR = 6;

   /** The key frequency limit HKDQT. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDQT = 6;

   /** The key frequency limit HKDCT. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDCT = 6;

   /** The key frequency limit HKDUA. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDUA = 6;

   /** The key frequency limit HKDUT. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDUT = 6;

   /** The key frequency limit HKDUX. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDUX = 6;

   /** The key frequency limit HKDVT. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDVT = 6;

   /** The key frequency limit HKDRR. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDRR = 6;

   /** The key frequency limit HKDQR. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDQR = 6;

   /** The key frequency limit HKDUE. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDUE = 6;

   /** The key frequency limit HKDRE. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDRE = 6;

   /** The key frequency limit HKDAM. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDAM = 6;

   /** The key frequency limit HKDTE. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDTE = 6;

   /** The key frequency limit HKDVR. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitHKDVR = 6;

   /** The key frequency limit WDWQP. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitWDWQP = 6;

   /** The key frequency limit DLRQP. (Default 6 occurrences). */
   private volatile int keyFrequencyLimitDLRQP = 6;

   /** The retrieved numeric property values from DB. */
   private volatile HashMap<String, String> retrievedPropertyValuesfromDB = new HashMap<String, String>();

   /**
    * Gets the key frequency limit WDWQT.
    * 
    * @return the key frequency limit WDWQT
    */
   public int getKeyFrequencyLimitWDWQT() {
      return keyFrequencyLimitWDWQT;
   }

   /**
    * This method will used for QueryTicket Transaction in WDW Provider for
    * Setting the key frequency limit .
    * 
    * @param keyFrequencyLimitWDWQT
    *           the new key frequency limit WDWQT
    */
   public void setKeyFrequencyLimitWDWQT(int keyFrequencyLimitWDWQT) {
      this.keyFrequencyLimitWDWQT = keyFrequencyLimitWDWQT;
   }

   /**
    * Gets the key frequency limit DLRVT.
    * 
    * @return the key frequency limit DLRVT
    */
   public int getKeyFrequencyLimitDLRVT() {
      return keyFrequencyLimitDLRVT;
   }

   /**
    * This method will used for VoidTicket Transaction in DLR Provider for
    * Setting the key frequency limit .
    * 
    * @param keyFrequencyLimitDLRVT
    *           the new key frequency limit DLRVT
    */
   public void setKeyFrequencyLimitDLRVT(int keyFrequencyLimitDLRVT) {
      this.keyFrequencyLimitDLRVT = keyFrequencyLimitDLRVT;
   }

   /**
    * Gets the key frequency limit WDWCT.
    * 
    * @return the key frequency limit WDWCT
    */
   public int getKeyFrequencyLimitWDWCT() {
      return keyFrequencyLimitWDWCT;
   }

   /**
    * This method will used for CreateTicket Transaction in WDW Provider for
    * Setting the key frequency limit .
    * 
    * @param keyFrequencyLimitWDWCT
    *           the new key frequency limit WDWCT
    */
   public void setKeyFrequencyLimitWDWCT(int keyFrequencyLimitWDWCT) {
      this.keyFrequencyLimitWDWCT = keyFrequencyLimitWDWCT;
   }

   /**
    * Gets the key frequency limit WDWUA.
    * 
    * @return the key frequency limit WDWUA
    */
   public int getKeyFrequencyLimitWDWUA() {
      return keyFrequencyLimitWDWUA;
   }

   /**
    * This method will used for UpgradeAlpha Transaction in WDW Provider for
    * Setting the key frequency limit .
    * 
    * @param keyFrequencyLimitWDWUA
    *           the new key frequency limit WDWUA
    */
   public void setKeyFrequencyLimitWDWUA(int keyFrequencyLimitWDWUA) {
      this.keyFrequencyLimitWDWUA = keyFrequencyLimitWDWUA;
   }

   /**
    * Gets the key frequency limit WDWUT.
    * 
    * @return the key frequency limit WDWUT
    */
   public int getKeyFrequencyLimitWDWUT() {
      return keyFrequencyLimitWDWUT;
   }

   /**
    * This method will used for UpdateTicket Transaction in WDW Provider for
    * Setting the key frequency limit .
    * 
    * @param keyFrequencyLimitWDWUT
    *           the new key frequency limit WDWUT
    */
   public void setKeyFrequencyLimitWDWUT(int keyFrequencyLimitWDWUT) {
      this.keyFrequencyLimitWDWUT = keyFrequencyLimitWDWUT;
   }

   /**
    * Gets the key frequency limit WDWUX.
    * 
    * @return the key frequency limit WDWUX
    */
   public int getKeyFrequencyLimitWDWUX() {
      return keyFrequencyLimitWDWUX;
   }

   /**
    * This method will used for UPDATETXNSTRING Transaction in WDW Provider for
    * Setting the key frequency limit .
    * 
    * @param keyFrequencyLimitWDWUX
    *           the new key frequency limit WDWUX
    */
   public void setKeyFrequencyLimitWDWUX(int keyFrequencyLimitWDWUX) {
      this.keyFrequencyLimitWDWUX = keyFrequencyLimitWDWUX;
   }

   /**
    * Gets the key frequency limit WDWVT.
    * 
    * @return the key frequency limit WDWVT
    */
   public int getKeyFrequencyLimitWDWVT() {
      return keyFrequencyLimitWDWVT;
   }

   /**
    * This method will used for VoidTickt Transaction in WDW Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitWDWVT
    *           the new key frequency limit WDWVT
    */
   public void setKeyFrequencyLimitWDWVT(int keyFrequencyLimitWDWVT) {
      this.keyFrequencyLimitWDWVT = keyFrequencyLimitWDWVT;
   }

   /**
    * Gets the key frequency limit WDWRR.
    * 
    * @return the key frequency limit WDWRR
    */
   public int getKeyFrequencyLimitWDWRR() {
      return keyFrequencyLimitWDWRR;
   }

   /**
    * This method will used for ReservationRequest Transaction in WDW Provider
    * for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitWDWRR
    *           the new key frequency limit WDWRR
    */
   public void setKeyFrequencyLimitWDWRR(int keyFrequencyLimitWDWRR) {
      this.keyFrequencyLimitWDWRR = keyFrequencyLimitWDWRR;
   }

   /**
    * Gets the key frequency limit WDWQR.
    * 
    * @return the key frequency limit WDWQR
    */
   public int getKeyFrequencyLimitWDWQR() {
      return keyFrequencyLimitWDWQR;
   }

   /**
    * This method will used for QueryTicket Transaction in WDW Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitWDWQR
    *           the new key frequency limit WDWQR
    */
   public void setKeyFrequencyLimitWDWQR(int keyFrequencyLimitWDWQR) {
      this.keyFrequencyLimitWDWQR = keyFrequencyLimitWDWQR;
   }

   /**
    * Gets the key frequency limit WDWUE.
    * 
    * @return the key frequency limit WDWUE
    */
   public int getKeyFrequencyLimitWDWUE() {
      return keyFrequencyLimitWDWUE;
   }

   /**
    * This method will used for UpgradeEntitlement Transaction in WDW Provider
    * for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitWDWUE
    *           the new key frequency limit WDWUE
    */
   public void setKeyFrequencyLimitWDWUE(int keyFrequencyLimitWDWUE) {
      this.keyFrequencyLimitWDWUE = keyFrequencyLimitWDWUE;
   }

   /**
    * Gets the key frequency limit WDWRE.
    * 
    * @return the key frequency limit WDWRE
    */
   public int getKeyFrequencyLimitWDWRE() {
      return keyFrequencyLimitWDWRE;
   }

   /**
    * This method will used for RenewEntitlement Transaction in WDW Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitWDWRE
    *           the new key frequency limit WDWRE
    */
   public void setKeyFrequencyLimitWDWRE(int keyFrequencyLimitWDWRE) {
      this.keyFrequencyLimitWDWRE = keyFrequencyLimitWDWRE;
   }

   /**
    * Gets the key frequency limit WDWAM.
    * 
    * @return the key frequency limit WDWAM
    */
   public int getKeyFrequencyLimitWDWAM() {
      return keyFrequencyLimitWDWAM;
   }

   /**
    * This method will used for AssosiateMedia Transaction in WDW Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitWDWAM
    *           the new key frequency limit WDWAM
    */
   public void setKeyFrequencyLimitWDWAM(int keyFrequencyLimitWDWAM) {
      this.keyFrequencyLimitWDWAM = keyFrequencyLimitWDWAM;
   }

   /**
    * Gets the key frequency limit WDWTE.
    * 
    * @return the key frequency limit WDWTE
    */
   public int getKeyFrequencyLimitWDWTE() {
      return keyFrequencyLimitWDWTE;
   }

   /**
    * This method will used for TICKERATEENTTLSTRING Transaction in WDW Provider
    * for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitWDWTE
    *           the new key frequency limit WDWTE
    */
   public void setKeyFrequencyLimitWDWTE(int keyFrequencyLimitWDWTE) {
      this.keyFrequencyLimitWDWTE = keyFrequencyLimitWDWTE;
   }

   /**
    * Gets the key frequency limit WDWVR.
    * 
    * @return the key frequency limit WDWVR
    */
   public int getKeyFrequencyLimitWDWVR() {
      return keyFrequencyLimitWDWVR;
   }

   /**
    * This method will used for VoidReservation Transaction in WDW Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitWDWVR
    *           the new key frequency limit WDWVR
    */
   public void setKeyFrequencyLimitWDWVR(int keyFrequencyLimitWDWVR) {
      this.keyFrequencyLimitWDWVR = keyFrequencyLimitWDWVR;
   }

   /**
    * Gets the key frequency limit DLRCT.
    * 
    * @return the key frequency limit DLRCT
    */
   public int getKeyFrequencyLimitDLRCT() {
      return keyFrequencyLimitDLRCT;
   }

   /**
    * This method will used for CreateTicket Transaction in DLR Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRCT
    *           the new key frequency limit DLRCT
    */
   public void setKeyFrequencyLimitDLRCT(int keyFrequencyLimitDLRCT) {
      this.keyFrequencyLimitDLRCT = keyFrequencyLimitDLRCT;
   }

   /**
    * Gets the key frequency limit DLRUA.
    * 
    * @return the key frequency limit DLRUA
    */
   public int getKeyFrequencyLimitDLRUA() {
      return keyFrequencyLimitDLRUA;
   }

   /**
    * This method will used for UpgradeAlpha Transaction in DLR Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRUA
    *           the new key frequency limit DLRUA
    */
   public void setKeyFrequencyLimitDLRUA(int keyFrequencyLimitDLRUA) {
      this.keyFrequencyLimitDLRUA = keyFrequencyLimitDLRUA;
   }

   /**
    * Gets the key frequency limit DLRUT.
    * 
    * @return the key frequency limit DLRUT
    */
   public int getKeyFrequencyLimitDLRUT() {
      return keyFrequencyLimitDLRUT;
   }

   /**
    * This method will used for UpdateTicket Transaction in DLR Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRUT
    *           the new key frequency limit DLRUT
    */
   public void setKeyFrequencyLimitDLRUT(int keyFrequencyLimitDLRUT) {
      this.keyFrequencyLimitDLRUT = keyFrequencyLimitDLRUT;
   }

   /**
    * Gets the key frequency limit DLRUX.
    * 
    * @return the key frequency limit DLRUX
    */
   public int getKeyFrequencyLimitDLRUX() {
      return keyFrequencyLimitDLRUX;
   }

   /**
    * This method will used for UPDATETXNSTRING Transaction in DLR Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRUX
    *           the new key frequency limit DLRUX
    */
   public void setKeyFrequencyLimitDLRUX(int keyFrequencyLimitDLRUX) {
      this.keyFrequencyLimitDLRUX = keyFrequencyLimitDLRUX;
   }

   /**
    * Gets the key frequency limit DLRQT.
    * 
    * @return the key frequency limit DLRQT
    */
   public int getKeyFrequencyLimitDLRQT() {
      return keyFrequencyLimitDLRQT;
   }

   /**
    * This method will used for QueryTicket Transaction in DLR Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRQT
    *           the new key frequency limit DLRQT
    */
   public void setKeyFrequencyLimitDLRQT(int keyFrequencyLimitDLRQT) {
      this.keyFrequencyLimitDLRQT = keyFrequencyLimitDLRQT;
   }

   /**
    * Gets the key frequency limit DLRRR.
    * 
    * @return the key frequency limit DLRRR
    */
   public int getKeyFrequencyLimitDLRRR() {
      return keyFrequencyLimitDLRRR;
   }

   /**
    * This method will used for ReservationRequest Transaction in DLR Provider
    * for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRRR
    *           the new key frequency limit DLRRR
    */
   public void setKeyFrequencyLimitDLRRR(int keyFrequencyLimitDLRRR) {
      this.keyFrequencyLimitDLRRR = keyFrequencyLimitDLRRR;
   }

   /**
    * Gets the key frequency limit DLRQR.
    * 
    * @return the key frequency limit DLRQR
    */
   public int getKeyFrequencyLimitDLRQR() {
      return keyFrequencyLimitDLRQR;
   }

   /**
    * This method will used for QueryReservation Transaction in DLR Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRQR
    *           the new key frequency limit DLRQR
    */
   public void setKeyFrequencyLimitDLRQR(int keyFrequencyLimitDLRQR) {
      this.keyFrequencyLimitDLRQR = keyFrequencyLimitDLRQR;
   }

   /**
    * Gets the key frequency limit DLRUE.
    * 
    * @return the key frequency limit DLRUE
    */
   public int getKeyFrequencyLimitDLRUE() {
      return keyFrequencyLimitDLRUE;
   }

   /**
    * This method will used for UpgradeEntitlement Transaction in DLR Provider
    * for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRUE
    *           the new key frequency limit DLRUE
    */
   public void setKeyFrequencyLimitDLRUE(int keyFrequencyLimitDLRUE) {
      this.keyFrequencyLimitDLRUE = keyFrequencyLimitDLRUE;
   }

   /**
    * Gets the key frequency limit DLRRE.
    * 
    * @return the key frequency limit DLRRE
    */
   public int getKeyFrequencyLimitDLRRE() {
      return keyFrequencyLimitDLRRE;
   }

   /**
    * This method will used for RenewEntitlement Transaction in DLR Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRRE
    *           the new key frequency limit DLRRE
    */
   public void setKeyFrequencyLimitDLRRE(int keyFrequencyLimitDLRRE) {
      this.keyFrequencyLimitDLRRE = keyFrequencyLimitDLRRE;
   }

   /**
    * Gets the key frequency limit DLRAM.
    * 
    * @return the key frequency limit DLRAM
    */
   public int getKeyFrequencyLimitDLRAM() {
      return keyFrequencyLimitDLRAM;
   }

   /**
    * This method will used for AssociateMedia Transaction in DLR Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRAM
    *           the new key frequency limit DLRAM
    */
   public void setKeyFrequencyLimitDLRAM(int keyFrequencyLimitDLRAM) {
      this.keyFrequencyLimitDLRAM = keyFrequencyLimitDLRAM;
   }

   /**
    * Gets the key frequency limit DLRTE.
    * 
    * @return the key frequency limit DLRTE
    */
   public int getKeyFrequencyLimitDLRTE() {
      return keyFrequencyLimitDLRTE;
   }

   /**
    * This method will used for TICKERATEENTTLSTRING Transaction in DLR Provider
    * for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRTE
    *           the new key frequency limit DLRTE
    */
   public void setKeyFrequencyLimitDLRTE(int keyFrequencyLimitDLRTE) {
      this.keyFrequencyLimitDLRTE = keyFrequencyLimitDLRTE;
   }

   /**
    * Gets the key frequency limit DLRVR.
    * 
    * @return the key frequency limit DLRVR
    */
   public int getKeyFrequencyLimitDLRVR() {
      return keyFrequencyLimitDLRVR;
   }

   /**
    * This method will used for VoidReservation Transaction in DLR Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRVR
    *           the new key frequency limit DLRVR
    */
   public void setKeyFrequencyLimitDLRVR(int keyFrequencyLimitDLRVR) {
      this.keyFrequencyLimitDLRVR = keyFrequencyLimitDLRVR;
   }

   /**
    * Gets the key frequency limit HKDQT.
    * 
    * @return the key frequency limit HKDQT
    */
   public int getKeyFrequencyLimitHKDQT() {
      return keyFrequencyLimitHKDQT;
   }

   /**
    * This method will used for QueryTicket Transaction in HKD Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDQT
    *           the new key frequency limit HKDQT
    */
   public void setKeyFrequencyLimitHKDQT(int keyFrequencyLimitHKDQT) {
      this.keyFrequencyLimitHKDQT = keyFrequencyLimitHKDQT;
   }

   /**
    * Gets the key frequency limit HKDCT.
    * 
    * @return the key frequency limit HKDCT
    */
   public int getKeyFrequencyLimitHKDCT() {
      return keyFrequencyLimitHKDCT;
   }

   /**
    * This method will used for CreateTicket Transaction in HKD Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDCT
    *           the new key frequency limit HKDCT
    */
   public void setKeyFrequencyLimitHKDCT(int keyFrequencyLimitHKDCT) {
      this.keyFrequencyLimitHKDCT = keyFrequencyLimitHKDCT;
   }

   /**
    * Gets the key frequency limit HKDUA.
    * 
    * @return the key frequency limit HKDUA
    */
   public int getKeyFrequencyLimitHKDUA() {
      return keyFrequencyLimitHKDUA;
   }

   /**
    * This method will used for QueryTicket Transaction in HKD Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDUA
    *           the new key frequency limit HKDUA
    */
   public void setKeyFrequencyLimitHKDUA(int keyFrequencyLimitHKDUA) {
      this.keyFrequencyLimitHKDUA = keyFrequencyLimitHKDUA;
   }

   /**
    * Gets the key frequency limit HKDUT.
    * 
    * @return the key frequency limit HKDUT
    */
   public int getKeyFrequencyLimitHKDUT() {
      return keyFrequencyLimitHKDUT;
   }

   /**
    * This method will used for UpdateTicket Transaction in HKD Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDUT
    *           the new key frequency limit HKDUT
    */
   public void setKeyFrequencyLimitHKDUT(int keyFrequencyLimitHKDUT) {
      this.keyFrequencyLimitHKDUT = keyFrequencyLimitHKDUT;
   }

   /**
    * Gets the key frequency limit HKDUX.
    * 
    * @return the key frequency limit HKDUX
    */
   public int getKeyFrequencyLimitHKDUX() {
      return keyFrequencyLimitHKDUX;
   }

   /**
    * This method will used for UPDATETXNSTRING Transaction in HKD Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDUX
    *           the new key frequency limit HKDUX
    */
   public void setKeyFrequencyLimitHKDUX(int keyFrequencyLimitHKDUX) {
      this.keyFrequencyLimitHKDUX = keyFrequencyLimitHKDUX;
   }

   /**
    * Gets the key frequency limit HKDVT.
    * 
    * @return the key frequency limit HKDVT
    */
   public int getKeyFrequencyLimitHKDVT() {
      return keyFrequencyLimitHKDVT;
   }

   /**
    * This method will used for VoidTicket Transaction in HKD Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDVT
    *           the new key frequency limit HKDVT
    */
   public void setKeyFrequencyLimitHKDVT(int keyFrequencyLimitHKDVT) {
      this.keyFrequencyLimitHKDVT = keyFrequencyLimitHKDVT;
   }

   /**
    * Gets the key frequency limit HKDRR.
    * 
    * @return the key frequency limit HKDRR
    */
   public int getKeyFrequencyLimitHKDRR() {
      return keyFrequencyLimitHKDRR;
   }

   /**
    * This method will used for ReservationRequest Transaction in HKD Provider
    * for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDRR
    *           the new key frequency limit HKDRR
    */
   public void setKeyFrequencyLimitHKDRR(int keyFrequencyLimitHKDRR) {
      this.keyFrequencyLimitHKDRR = keyFrequencyLimitHKDRR;
   }

   /**
    * Gets the key frequency limit HKDQR.
    * 
    * @return the key frequency limit HKDQR
    */
   public int getKeyFrequencyLimitHKDQR() {
      return keyFrequencyLimitHKDQR;
   }

   /**
    * This method will used for QueryReservation Transaction in HKD Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDQR
    *           the new key frequency limit HKDQR
    */
   public void setKeyFrequencyLimitHKDQR(int keyFrequencyLimitHKDQR) {
      this.keyFrequencyLimitHKDQR = keyFrequencyLimitHKDQR;
   }

   /**
    * Gets the key frequency limit HKDUE.
    * 
    * @return the key frequency limit HKDUE
    */
   public int getKeyFrequencyLimitHKDUE() {
      return keyFrequencyLimitHKDUE;
   }

   /**
    * This method will used for UPGRADEENTITLEMENTSTRING Transaction in HKD
    * Provider for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDUE
    *           the new key frequency limit HKDUE
    */
   public void setKeyFrequencyLimitHKDUE(int keyFrequencyLimitHKDUE) {
      this.keyFrequencyLimitHKDUE = keyFrequencyLimitHKDUE;
   }

   /**
    * Gets the key frequency limit HKDRE.
    * 
    * @return the key frequency limit HKDRE
    */
   public int getKeyFrequencyLimitHKDRE() {
      return keyFrequencyLimitHKDRE;
   }

   /**
    * This method will used for RENEWENTITLEMENTSTRING Transaction in HKD
    * Provider for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDRE
    *           the new key frequency limit HKDRE
    */
   public void setKeyFrequencyLimitHKDRE(int keyFrequencyLimitHKDRE) {
      this.keyFrequencyLimitHKDRE = keyFrequencyLimitHKDRE;
   }

   /**
    * Gets the key frequency limit HKDAM.
    * 
    * @return the key frequency limit HKDAM
    */
   public int getKeyFrequencyLimitHKDAM() {
      return keyFrequencyLimitHKDAM;
   }

   /**
    * This method will used for AssociateMedia Transaction in HKD Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDAM
    *           the new key frequency limit HKDAM
    */
   public void setKeyFrequencyLimitHKDAM(int keyFrequencyLimitHKDAM) {
      this.keyFrequencyLimitHKDAM = keyFrequencyLimitHKDAM;
   }

   /**
    * Gets the key frequency limit HKDTE.
    * 
    * @return the key frequency limit HKDTE
    */
   public int getKeyFrequencyLimitHKDTE() {
      return keyFrequencyLimitHKDTE;
   }

   /**
    * This method will used for TICKERATEENTTLSTRING Transaction in HKD Provider
    * for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDTE
    *           the new key frequency limit HKDTE
    */
   public void setKeyFrequencyLimitHKDTE(int keyFrequencyLimitHKDTE) {
      this.keyFrequencyLimitHKDTE = keyFrequencyLimitHKDTE;
   }

   /**
    * Gets the key frequency limit HKDVR.
    * 
    * @return the key frequency limit HKDVR
    */
   public int getKeyFrequencyLimitHKDVR() {
      return keyFrequencyLimitHKDVR;
   }

   /**
    * This method will used for VoidReservation Transaction in HKD Provider for
    * Setting the key frequency limit
    * 
    * @param keyFrequencyLimitHKDVR
    *           the new key frequency limit HKDVR
    */
   public void setKeyFrequencyLimitHKDVR(int keyFrequencyLimitHKDVR) {
      this.keyFrequencyLimitHKDVR = keyFrequencyLimitHKDVR;
   }

   /**
    * Gets the key frequency limit WDWQP.
    * 
    * @return the key frequency limit WDWQP
    */
   public int getKeyFrequencyLimitWDWQP() {
      return keyFrequencyLimitWDWQP;
   }

   /**
    * This method will used for QueryEligibleProduct Transaction in WDW Provider
    * for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitWDWQP
    *           the new key frequency limit WDWQP
    */
   public void setKeyFrequencyLimitWDWQP(int keyFrequencyLimitWDWQP) {
      this.keyFrequencyLimitWDWQP = keyFrequencyLimitWDWQP;
   }

   /**
    * Gets the key frequency limit DLRQP.
    * 
    * @return the key frequency limit DLRQP
    */
   public int getKeyFrequencyLimitDLRQP() {
      return keyFrequencyLimitDLRQP;
   }

   /**
    * This method will used for QueryEligibleProduct Transaction in DLR Provider
    * for Setting the key frequency limit
    * 
    * @param keyFrequencyLimitDLRQP
    *           the new key frequency limit DLRQP
    */
   public void setKeyFrequencyLimitDLRQP(int keyFrequencyLimitDLRQP) {
      this.keyFrequencyLimitDLRQP = keyFrequencyLimitDLRQP;
   }

   /**
    * Gets the retrieved property values from DB.
    * 
    * @return the retrieved property values from DB
    */
   public HashMap<String, String> getRetrievedPropertyValuesfromDB() {
      return retrievedPropertyValuesfromDB;
   }

   /**
    * Sets the retrieved property values from DB.
    * 
    * @param retrievedPropertyValuesfromDB
    *           the retrieved property values from DB
    */
   public void setRetrievedPropertyValuesfromDB(HashMap<String, String> retrievedPropertyValuesfromDB) {
      this.retrievedPropertyValuesfromDB = retrievedPropertyValuesfromDB;
   }

   /*
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString() {

      StringBuffer output = new StringBuffer();
      output.append("Flood Control properties are ");

      output.append("FloodControlPropertyTO [keyFrequencyLimitWDWQT=" + keyFrequencyLimitWDWQT
               + ", keyFrequencyLimitDLRVT=" + keyFrequencyLimitDLRVT + ", keyFrequencyLimitWDWCT="
               + keyFrequencyLimitWDWCT + ", keyFrequencyLimitWDWUA=" + keyFrequencyLimitWDWUA
               + ", keyFrequencyLimitWDWUT=" + keyFrequencyLimitWDWUT + ", keyFrequencyLimitWDWUX="
               + keyFrequencyLimitWDWUX + ", keyFrequencyLimitWDWVT=" + keyFrequencyLimitWDWVT
               + ", keyFrequencyLimitWDWRR=" + keyFrequencyLimitWDWRR + ", keyFrequencyLimitWDWQR="
               + keyFrequencyLimitWDWQR + ", keyFrequencyLimitWDWUE=" + keyFrequencyLimitWDWUE
               + ", keyFrequencyLimitWDWRE=" + keyFrequencyLimitWDWRE + ", keyFrequencyLimitWDWAM="
               + keyFrequencyLimitWDWAM + ", keyFrequencyLimitWDWTE=" + keyFrequencyLimitWDWTE
               + ", keyFrequencyLimitWDWVR=" + keyFrequencyLimitWDWVR + ", keyFrequencyLimitDLRCT="
               + keyFrequencyLimitDLRCT + ", keyFrequencyLimitDLRUA=" + keyFrequencyLimitDLRUA
               + ", keyFrequencyLimitDLRUT=" + keyFrequencyLimitDLRUT + ", keyFrequencyLimitDLRUX="
               + keyFrequencyLimitDLRUX + ", keyFrequencyLimitDLRQT=" + keyFrequencyLimitDLRQT
               + ", keyFrequencyLimitDLRRR=" + keyFrequencyLimitDLRRR + ", keyFrequencyLimitDLRQR="
               + keyFrequencyLimitDLRQR + ", keyFrequencyLimitDLRUE=" + keyFrequencyLimitDLRUE
               + ", keyFrequencyLimitDLRRE=" + keyFrequencyLimitDLRRE + ", keyFrequencyLimitDLRAM="
               + keyFrequencyLimitDLRAM + ", keyFrequencyLimitDLRTE=" + keyFrequencyLimitDLRTE
               + ", keyFrequencyLimitDLRVR=" + keyFrequencyLimitDLRVR + ", keyFrequencyLimitHKDQT="
               + keyFrequencyLimitHKDQT + ", keyFrequencyLimitHKDCT=" + keyFrequencyLimitHKDCT
               + ", keyFrequencyLimitHKDUA=" + keyFrequencyLimitHKDUA + ", keyFrequencyLimitHKDUT="
               + keyFrequencyLimitHKDUT + ", keyFrequencyLimitHKDUX=" + keyFrequencyLimitHKDUX
               + ", keyFrequencyLimitHKDVT=" + keyFrequencyLimitHKDVT + ", keyFrequencyLimitHKDRR="
               + keyFrequencyLimitHKDRR + ", keyFrequencyLimitHKDQR=" + keyFrequencyLimitHKDQR
               + ", keyFrequencyLimitHKDUE=" + keyFrequencyLimitHKDUE + ", keyFrequencyLimitHKDRE="
               + keyFrequencyLimitHKDRE + ", keyFrequencyLimitHKDAM=" + keyFrequencyLimitHKDAM
               + ", keyFrequencyLimitHKDTE=" + keyFrequencyLimitHKDTE + ", keyFrequencyLimitHKDVR="
               + keyFrequencyLimitHKDVR + ", keyFrequencyLimitWDWQP=" + keyFrequencyLimitWDWQP
               + ", keyFrequencyLimitDLRQP=" + keyFrequencyLimitDLRQP + "]");

      return output.toString();
   }
}
