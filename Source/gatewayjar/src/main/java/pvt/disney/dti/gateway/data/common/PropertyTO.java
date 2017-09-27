package pvt.disney.dti.gateway.data.common;

/**
 * 
 * @author GANDV005
 *
 */
public class PropertyTO {

   private String section;
   private String propSetKey;
   private String propSetValue;

   /**
    * @return the section
    */
   public String getSection() {
      return section;
   }

   /**
    * @param section
    *           the section to set
    */
   public void setSection(String section) {
      this.section = section;
   }

   /**
    * @return the propSetKey
    */
   public String getPropSetKey() {
      return propSetKey;
   }

   /**
    * @param propSetKey
    *           the propSetKey to set
    */
   public void setPropSetKey(String propSetKey) {
      this.propSetKey = propSetKey;
   }

   /**
    * @return the propSetValue
    */
   public String getPropSetValue() {
      return propSetValue;
   }

   /**
    * @param propSetValue
    *           the propSetValue to set
    */
   public void setPropSetValue(String propSetValue) {
      this.propSetValue = propSetValue;
   }

}