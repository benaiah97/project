package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.data.common.BarricadeTO;

import com.disney.exception.WrappedException;

/**
 * The Class BarricadeResult.
 * @author MISHP012
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
      BarricadeTO barricadeTO = new BarricadeTO();
      
      barricadeTO.setBarricadeID(rs.getInt("BARRICADEID"));
      barricadeTO.setCosGrpID(rs.getInt("COSGRP_ID"));
      barricadeTO.setCreatorID(rs.getInt("CREATORID"));
      barricadeTO.setOwnerID(rs.getString("OWNERID"));
      barricadeTO.setTsLocID(rs.getInt("TSLOCID"));
      barricadeTO.setTsMacID(rs.getInt("TSMACID"));
      barricadeTOs.add(barricadeTO);
      this.recordsProcessed++;

      return;

   }
}
