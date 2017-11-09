package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.data.common.BarricadeTO;

import com.disney.exception.WrappedException;

/**
 * The Class BarricadeResult.
 */
public class BarricadeResult implements ResultSetProcessor{

   /** The barricade Tos. */
   ArrayList<BarricadeTO> barricadeTOs = new ArrayList<BarricadeTO>();

   /**
    * Constructor for BarricadeResult
    */
   public BarricadeResult() {
      super();
   }

   /** The number of records processed by this result set processor. */
   private int recordsProcessed = 0;
 
   /**
    * Gets the processed object.
    *
    * @return the processed object
    * @throws WrappedException the wrapped exception
    */
   public Object getProcessedObject() throws WrappedException {
      return barricadeTOs;
   }

   /**
    * Process next result set.
    *
    * @param rs the rs
    * @throws SQLException the SQL exception
    * @throws WrappedException the wrapped exception
    */
   public void processNextResultSet(ResultSet rs) throws SQLException, WrappedException {
      BarricadeTO anBarricade = new BarricadeTO();
      
      anBarricade.setBarricadeID(rs.getString("BARRICADEID"));
      anBarricade.setCosGrpID(rs.getInt("COSGRP_ID"));
      anBarricade.setCreatorID(rs.getString("CREATORID"));
      anBarricade.setOwnerID(rs.getString("OWNERID"));
      anBarricade.setTsLocID(rs.getString("TSLOCID"));
      anBarricade.setTsMacID(rs.getString("TSMACID"));
      barricadeTOs.add(anBarricade);
      this.recordsProcessed++;

      return;

   }
}
