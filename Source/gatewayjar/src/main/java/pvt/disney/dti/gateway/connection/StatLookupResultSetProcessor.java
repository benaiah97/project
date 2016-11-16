/* StatLookupResultSetProcessor - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package pvt.disney.dti.gateway.connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.disney.exception.WrappedException;

public class StatLookupResultSetProcessor implements ResultSetProcessor
{
    private static final String sep = "-";
    private DBStatRecorder stats = null;
    
    public StatLookupResultSetProcessor() {
	stats = new DBStatRecorder();
    }
    
    public void processNextResultSet(ResultSet rs)
	throws SQLException, WrappedException {
	String msgType = rs.getString(1);
	String compId = rs.getString(2);
	String timingType = rs.getString(3);
	String currMsgType = rs.getString(4);
	String code = rs.getString(5);
	TransactionId transId = new TransactionId(msgType, compId, timingType,
						  currMsgType, code);
	int count = rs.getInt(6);
	long totalTime = rs.getLong(7);
	long maxTime = rs.getLong(8);
	long minTime = rs.getLong(9);
	DBTransactionStats tStats = new DBTransactionStats(transId.toString());
	tStats.setValues(count, totalTime, maxTime, minTime);
	stats.addTransactionStats(transId.toString(), tStats);
    }
    
    public Object getProcessedObject() throws WrappedException {
	return stats;
    }
}
