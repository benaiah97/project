package pvt.disney.dti.gateway.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.or.ObjectRenderer;
import org.apache.log4j.spi.LoggingEvent;

import com.disney.logging.audit.ErrorCode;
import com.disney.logging.audit.Event;
import com.disney.logging.audit.EventType;
import com.disney.logging.audit.MessageInfo;
import com.disney.logging.audit.TraceInfo;

/**
 * Renders the log messages from DTI in an easy-to-read format. Borrowed and slighly modified from another project.
 * 
 * @author ctl2
 * @version %version: %
 */
public final class FileRenderer implements ObjectRenderer {
	private static final int DEFAULT_STRINGBUFFER_SIZE = 5000;
	private static final String NEWLINE = System.getProperty("line.separator");
	private static final SimpleDateFormat sdfFormat = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss.SSS zzz yyyy");

	/**
	 * Creates easy to ready output based on the log object passed in.
	 * 
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(Object)
	 */
	public String doRender(Object obj) {
		/*
		 * Example output:
		 * 
		 * **** BEGIN EXCEPTION EVENT ***** CODE: err2001 (Only for error events) WHEN: Wed Oct 01 12:16:12 EDT 2003 MESSAGE: General Exception TRANSACTION_INFO: <TransactionInfo ... > (Optional) TRACE_INFO: <TraceInfo ... > (Optional)
		 * STACK_TRACE: com.disney.ir.dispatcher.exception.ApplicationException at com.disney.ir.dispatcher.util.SendMessageUtilTest.testPublishErrorResponse(SendMessageUtilTest.java:86) at java.lang.reflect.Method.invoke(Native Method) at
		 * junit.framework.TestCase.runTest(TestCase.java:166) at junit.framework.TestCase.runBare(TestCase.java:140) at junit.framework.TestResult$1.protect(TestResult.java:106) at
		 * junit.framework.TestResult.runProtected(TestResult.java:124) at junit.framework.TestResult.run(TestResult.java:109) at junit.framework.TestCase.run(TestCase.java:131) at junit.framework.TestSuite.runTest(TestSuite.java:173) at
		 * junit.framework.TestSuite.run(TestSuite.java:168) at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.runTests(RemoteTestRunner.java:329) at
		 * org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.run(RemoteTestRunner.java:218) at org.eclipse.jdt.internal.junit.runner.RemoteTestRunner.main(RemoteTestRunner.java:151)**** END EXCEPTION EVENT *****
		 * 
		 * **** BEGIN DEBUG EVENT ***** WHEN: Wed Oct 01 12:16:12 EDT 2003 MESSAGE: START ExceptionTransformer:mapToMessage:: TRANSACTION_INFO: <TransactionInfo ... > TRACE_INFO: <TraceInfo ... >**** END DEBUG EVENT *****
		 */
		StringBuffer output = new StringBuffer(DEFAULT_STRINGBUFFER_SIZE);
		if (obj instanceof Event) {
			Event ev = (Event) obj;

			EventType evType = ev.getType();

			String evTypeString = "UNKNOWN";

			if (evType != null) {
				evTypeString = evType.toString();
			}

			output.append(NEWLINE + NEWLINE + "***** BEGIN " + evTypeString + " EVENT *****" + NEWLINE);

			ErrorCode errorCodeObject = ev.getCode();

			if (errorCodeObject != null) {
				String errorCode = errorCodeObject.toString();
				output.append("CODE: " + errorCode + NEWLINE);
			}

			Date creationDate = ev.getCreationDate();
			// Date creationDate = new Date();
			String whenString = sdfFormat.format(creationDate);

			output.append("WHEN: " + whenString + NEWLINE);
			output.append("MESSAGE: " + ev.getMessage() + NEWLINE);

			MessageInfo txInfo = ev.getMessageInfo();

			if (txInfo != null) {
				output.append("TRANSACTION_INFO: " + txInfo.toString() + NEWLINE);
			}

			TraceInfo tcInfo = ev.getTraceInfo();

			if (tcInfo != null) {
				output.append("TRACE_INFO: " + tcInfo.toString() + NEWLINE);
			}

			String stackTrace = ev.getStackTrace();

			if (stackTrace != null) {
				output.append("STACK_TRACE: " + stackTrace + NEWLINE);
			}

			output.append("***** END " + evTypeString + " EVENT *****");

		}
		else if (obj instanceof LoggingEvent) {

			LoggingEvent le = (LoggingEvent) obj;

			output.append(NEWLINE + NEWLINE + "***** BEGIN " + le
					.getLoggerName() + " EVENT *****" + NEWLINE);
			output.append("MESSAGE: " + le.getMessage() + NEWLINE);
			output.append("WHEN: " + new Date(le.timeStamp) + NEWLINE);

			if (le.getThrowableInformation() != null) {
				String stackTrace = le.getThrowableInformation().toString();
				output.append("STACK_TRACE: " + stackTrace + NEWLINE);
			}
			output.append("***** END " + le.getLoggerName() + " EVENT *****");

		}
		else if (obj != null) {
			output.append(NEWLINE + NEWLINE + "***** BEGIN UNKNOWN EVENT *****" + NEWLINE);
			output.append("MESSAGE: " + obj.toString() + NEWLINE);
			output.append("WHEN: " + new Date() + NEWLINE);
			output.append("***** END UNKNOWN EVENT *****");
		}
		return output.toString();
	}
}
