/* TimedEvent - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package pvt.disney.dti.gateway.connection;
import pvt.disney.dti.gateway.connection.TransactionId;

public class TimedEvent
{
    private TransactionId transId;
    private long startTime = 0L;
    private long elapsedTime = 0L;
    
    public long getElapsedTime() {
	return elapsedTime;
    }
    
    public void setElapsedTime(long elapsedTime) {
	this.elapsedTime = elapsedTime;
    }
    
    public long getStartTime() {
	return startTime;
    }
    
    public void setStartTime(long startTime) {
	this.startTime = startTime;
    }
    
    public TransactionId getTransactionId() {
	return transId;
    }
    
    public void setTransactionId(TransactionId id) {
	transId = id;
    }
    
    public String getType() {
	return transId.getEventType();
    }
    
    public String getComponentId() {
	return transId.getComponentId();
    }
    
    public String toString() {
	StringBuffer buff = new StringBuffer("TimedEvent:[startTime=");
	buff.append(startTime);
	buff.append(", elapsedTime=");
	buff.append(elapsedTime);
	buff.append(", transactionId=");
	buff.append(transId);
	buff.append("]");
	return buff.toString();
    }
}
