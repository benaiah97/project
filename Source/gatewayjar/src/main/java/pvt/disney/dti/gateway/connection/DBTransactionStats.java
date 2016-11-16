/* DBTransactionStats - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package pvt.disney.dti.gateway.connection;

public class DBTransactionStats extends TransactionStats
{
    public DBTransactionStats(String name) {
	super(name);
    }
    
    void setValues(int count, long totalTime, long maxTime, long minTime) {
	this.count = count;
	this.totalTime = totalTime;
	this.maxTime = maxTime;
	this.minTime = minTime;
    }
}
