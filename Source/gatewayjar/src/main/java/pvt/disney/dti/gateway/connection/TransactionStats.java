/* TransactionStats - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package pvt.disney.dti.gateway.connection;

import java.util.Date;

public class TransactionStats
{
    private final int defaultMaxResponseListSize = 50;
    private int nextPosition = 0;
    private String name = "";
    protected int count = 0;
    protected long totalTime = 0L;
    protected long minTime = 9223372036854775807L;
    protected long maxTime = -9223372036854775808L;
    private Date timestamp = null;
    private long[] latestTimes = new long[50];
    
    public TransactionStats(String name) {
	this.name = name;
	for (int i = 0; i < 50; i++)
	    latestTimes[i] = 0L;
    }
    
    public void bumpTransaction(long transactionElapsedTime,
				Date transactionCompletedTime) {
	timestamp = transactionCompletedTime;
	count++;
	totalTime += transactionElapsedTime;
	if (transactionElapsedTime > maxTime)
	    maxTime = transactionElapsedTime;
	if (transactionElapsedTime < minTime)
	    minTime = transactionElapsedTime;
	if (nextPosition >= 50)
	    nextPosition = 0;
	latestTimes[nextPosition++] = transactionElapsedTime;
    }
    
    public String toPropList() {
	String propListString
	    = (name + "={" + "totalTime=\"" + totalTime + "\";" + "count=\""
	       + count + "\";" + "timestamp=\"" + timestamp.toString() + "\";"
	       + "latestTimes=(" + getLatestTimesString() + ");" + "};");
	return propListString;
    }
    
    private String getLatestTimesString() {
	int index = nextPosition - 1;
	String list = "";
	for (int i = 1; i < latestTimes.length; i++) {
	    if (index < 0)
		index = latestTimes.length - 1;
	    if (latestTimes[index] != 0L)
		list += "\"" + latestTimes[index] + "\",";
	    index--;
	}
	int length = list.length();
	if (length > 0)
	    return list.substring(0, list.length() - 1);
	return list;
    }
    
    public long getTotalTime() {
	return totalTime;
    }
    
    public int getCount() {
	return count;
    }
    
    public long[] getLatestTimes() {
	return latestTimes;
    }
    
    public int getNextPosition() {
	return nextPosition;
    }
    
    public long getMinTime() {
	return minTime;
    }
    
    public long getMaxTime() {
	return maxTime;
    }
}
