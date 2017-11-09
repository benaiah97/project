package pvt.disney.dti.gateway.dao.result;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;

import com.disney.exception.WrappedException;

/**
 * The Class CommandResult.
 *
 * @author MISHP012
 */
public class CommandResult implements ResultSetProcessor{
   
   
   /** The results. */
   List<String> results = new ArrayList<String>();
   
   /** The number of records processed by this result set processor. */
   @SuppressWarnings("unused")
   private int recordsProcessed = 0;

  
   /**
    * Instantiates a new command result.
    */
   public CommandResult() {
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
    * @param rs the rs
    * @throws SQLException the SQL exception
    * @throws WrappedException the wrapped exception
    * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
    */
   public void processNextResultSet(ResultSet rs) throws SQLException, WrappedException {

      results.add(rs.getString("CMD_CODE"));

      this.recordsProcessed++;

      return;

   }
}
