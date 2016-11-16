/* TransactionId - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package pvt.disney.dti.gateway.connection;

import java.util.StringTokenizer;

public class TransactionId
{
    private static final char _SEP = '-';
    private static final char _ERROR_SEP = '_';
    private static final String _DEFAULT = "UNKNOWN";
    private static final int byteArrayLength = 16;
    private String masterMsgType;
    private String componentId;
    private String eventType;
    private String currentMsgType;
    private String errorCode;
    
    public TransactionId(String masterMsgType, String componentId,
			 String eventType, String currentMsgType,
			 String errorCode) {
	this.masterMsgType = fixString(masterMsgType);
	this.componentId = fixString(componentId);
	this.eventType = fixString(eventType);
	this.currentMsgType = fixString(currentMsgType);
	this.errorCode = fixString(errorCode);
    }
    
    public static TransactionId parseTransactionId(String transId) {
	StringTokenizer st = new StringTokenizer(transId, "-");
	String masterMsgType = st.nextToken();
	String componentId = st.nextToken();
	String eventType = st.nextToken();
	String temp = st.nextToken();
	String currentMsgType = temp;
	String errorCode = "0000";
	int errorStart = temp.lastIndexOf('_');
	if (errorStart > 0) {
	    currentMsgType = temp.substring(0, errorStart);
	    errorCode = temp.substring(errorStart + 1);
	}
	return new TransactionId(masterMsgType, componentId, eventType,
				 currentMsgType, errorCode);
    }
    
    public String toString() {
	StringBuffer buff = new StringBuffer(toComparableString());
	buff.append('_');
	buff.append(errorCode);
	return buff.toString();
    }
    
    public String toComparableString() {
	StringBuffer buff = new StringBuffer();
	buff.append(masterMsgType);
	buff.append('-');
	buff.append(componentId);
	buff.append('-');
	buff.append(eventType);
	buff.append('-');
	buff.append(currentMsgType);
	return buff.toString();
    }
    
    public byte[] toByteArray() {
	StringBuffer id = new StringBuffer(toString());
	while (id.length() < 16)
	    id.append("?");
	return id.substring(0, 16).getBytes();
    }
    
    public String getComponentId() {
	return componentId;
    }
    
    public String getCurrentMsgType() {
	return currentMsgType;
    }
    
    public String getErrorCode() {
	return errorCode;
    }
    
    public String getEventType() {
	return eventType;
    }
    
    public String getMasterMsgType() {
	return masterMsgType;
    }
    
    private String fixString(String input) {
	if (input == null)
	    return null;
	String output = input.replace('-', ' ');
	output = output.trim().length() == 0 ? "UNKNOWN" : output.replace(' ',
									  '_');
	return output;
    }
}
