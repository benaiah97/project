/* MessageStats - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package pvt.disney.dti.gateway.connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.disney.logging.audit.Event;

public class MessageStats
{
    private String serviceType = null;
    private String messageId = null;
    private String conversationId = null;
    private String node = null;
    private String appName = null;
    private List timedEvents = null;
    private boolean shouldSort = true;
    private List events = null;
    
    public MessageStats() {
	timedEvents = new ArrayList();
	events = new ArrayList();
    }
    
    public String getConversationId() {
	return conversationId;
    }
    
    public void setConversationId(String conversationId) {
	this.conversationId = conversationId;
    }
    
    public String getMessageId() {
	return messageId;
    }
    
    public void setMessageId(String messageId) {
	this.messageId = messageId;
    }
    
    public String getServiceType() {
	return serviceType;
    }
    
    public void setServiceType(String serviceType) {
	this.serviceType = serviceType;
    }
    
    public String getAppName() {
	return appName;
    }
    
    public void setAppName(String appName) {
	this.appName = appName;
    }
    
    public String getNode() {
	return node;
    }
    
    public void setNode(String node) {
	this.node = node;
    }
    
    public void addTimedEvent(TransactionId transId, long startTime,
			      long elapsedTime) {
	TimedEvent te = new TimedEvent();
	te.setTransactionId(transId);
	te.setStartTime(startTime);
	te.setElapsedTime(elapsedTime);
	timedEvents.add(te);
	shouldSort = true;
    }
    
    public void addEvent(Event e) {
	events.add(e);
    }
    
    public String toString() {
	StringBuffer buf = new StringBuffer("MessageStats:[");
	buf.append("conversationId=" + conversationId);
	buf.append(",messageId=" + messageId);
	buf.append(",serviceType=" + serviceType);
	buf.append(",appName=" + appName);
	buf.append(",node=" + node);
	buf.append(",timedEvents=" + timedEvents.toString());
	buf.append(",events=" + events.toString());
	buf.append("]");
	return buf.toString();
    }
    
    public List getTimedEvents() {
	if (shouldSort)
	    sortTimedEvents();
	return timedEvents;
    }
    
    public List getEvents() {
	return events;
    }
    
    private void sortTimedEvents() {
	TimedEventComparator comparator = new TimedEventComparator();
	Collections.sort(timedEvents, comparator);
	shouldSort = false;
    }
}
