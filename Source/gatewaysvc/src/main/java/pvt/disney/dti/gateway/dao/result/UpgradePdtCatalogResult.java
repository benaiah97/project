package pvt.disney.dti.gateway.dao.result;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.data.common.DBProductTO;

import com.disney.exception.WrappedException;

/**
 * Result set processor class for upgrade product catalog query.
 * @author lewit019
 * @since 2.17.3
 */
public class UpgradePdtCatalogResult implements ResultSetProcessor {

   /** List of DB products. */
   private ArrayList<DBProductTO> results = new ArrayList<DBProductTO>();

   /**
    * Constructor for ProductTktTypeResult
    */
   public UpgradePdtCatalogResult() {
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
   public void processNextResultSet(ResultSet rs) throws SQLException,
         WrappedException {

      DBProductTO aProduct = new DBProductTO();

      // PDT_CODE
      aProduct.setPdtCode((rs.getString("PDT_CODE")));

      // UNIT_PRICE
      aProduct.setUnitPrice(rs.getBigDecimal("UNIT_PRICE"));
      
      // TAX
      aProduct.setTax(rs.getBigDecimal("TAX"));
      
      // DAY_CLASS
      aProduct.setDayClass(rs.getString("DAY_CLASS"));
      
      // TODO DAY COUNT
      aProduct.setDayCount(rs.getInt("DAY_COUNT"));
      
      // DAY_SUBCLASS
      aProduct.setDaySubclass(rs.getString("DAY_SUBCLASS"));
      
      // TKT_NBR (Base system ID for WDW)
      aProduct.setMappedProviderTktNbr(new BigInteger(rs.getString("TKT_NBR")));
      
      // TKT_NAME (Base system ID for DLR - a.k.a. PLU)
      aProduct.setMappedProviderTktName(rs.getString("TKT_NAME"));

      results.add(aProduct);

      return;

   }
}