/* DBStatRecorder - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package pvt.disney.dti.gateway.connection;
import java.util.HashMap;


public class DBStatRecorder extends StatsRecorder
{
    void addTransactionStats(String transId, TransactionStats tStats) {
	stats.put(transId, tStats);
    }
    
    public HashMap getStats() {
	return stats;
    }
}
