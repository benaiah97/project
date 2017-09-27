package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.disney.exception.WrappedException;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.data.common.PropertyTO;

public class PropertyResult implements ResultSetProcessor {

   /** List of properties. */
   private ArrayList<PropertyTO> results = new ArrayList<PropertyTO>();

   /** The number of records processed by this result set processor. */
   @SuppressWarnings("unused")
   private int recordsProcessed = 0;

   /**
    * Constructor for PropertyResult
    */
   public PropertyResult() {
      super();
   }

   /**
    * Returns the processed object.
    * 
    * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
    */
   public Object getProcessedObject() throws WrappedException {
      return results;
   }

   /**
    * Processes the next (or only) value in the results set.
    * 
    * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
    */
   public void processNextResultSet(ResultSet rs) throws SQLException, WrappedException {

      PropertyTO aProduct = new PropertyTO();

      // SECTION
      aProduct.setSection(rs.getString("SECTION"));

      // PSDTL_KEY
      aProduct.setPropSetKey(rs.getString("PSDTL_KEY"));

      // PSDTL_VALUE
      String propSetValue=rs.getString("PSDTL_VALUE");
      if(propSetValue.equals("T") || propSetValue.equals("True")){
    	  aProduct.setPropSetValue("true");
      }else{
    	  aProduct.setPropSetValue(propSetValue);
      }
      
      results.add(aProduct);

      this.recordsProcessed++;

      return;

   }

}