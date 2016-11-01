package pvt.disney.dti.gateway.constants;

import java.util.Date;

/**
 * Extends the Exception class to include DTI specific logic. Although several variations are used in the front part (GATE, DTIController, etc.), the forms DTIErrorCode are what should be used, since they provide enough information back to
 * the catch clauses to generate XML responses for the failing request. *
 * 
 * @author Andy Anon & Todd Lewis
 * @version %version: 2 %
 * 
 */
@SuppressWarnings("rawtypes")
public class DTIException extends Exception implements java.io.Serializable {

	final static long serialVersionUID = 9129231995L;

	/** The method having the failure. */
	private String methodName;

	/** The class where the failure occurred. */

	private Class className;

	/** The date of the failure (includes time). */
	private Date date;

	/** A message to be passed back. */
	private String message;

	/** The TP reference number of the request. */
	private Integer tpRefNumber;

	/** A message to be logged. */
	private String logMessage;

	/**
	 * The exception (wrapped) that may have caused the throwing of this DTIException.
	 */
	private Exception externalException;

	/** The error code, if appropriate. */
	private String code;

	/**
	 * The DTIErrorCode which can be used to generate an appropriate error response back to the client.
	 */
	private DTIErrorCode dtiErrorCode;

	private final static String DEFAULT_MESSAGE = "default message";

	/**
	 * Initializes a WDWException using an error code message.
	 * 
	 */
	public DTIException() {
		super();
		date = java.util.Calendar.getInstance().getTime();

		return;
	}

	/**
	 * Initializes a WDWException using an error code message.
	 * 
	 * @param messageKey
	 *            A string Message
	 */
	public DTIException(String messageKey) {
		date = java.util.Calendar.getInstance().getTime();
		message = getMessage(messageKey);
		code = messageKey;

		return;
	}

	/**
	 * Initializes a WDWException using a DTI Error Code.
	 * 
	 * @param dtiErrorCodeIn
	 *            A string Message
	 */
	public DTIException(DTIErrorCode dtiErrorCodeIn) {
		dtiErrorCode = dtiErrorCodeIn;

		return;
	}

	/**
	 * Initializes a DTIException using an error code message, an error code and a description.
	 */
	public DTIException(Class classNameIn, DTIErrorCode errorIn,
			String logMessageIn) {
		super();

		date = java.util.Calendar.getInstance().getTime();
		className = classNameIn;
		dtiErrorCode = errorIn;
		code = errorIn.getErrorCode();
		logMessage = logMessageIn;

		return;
	}

	/**
	 * Initializes a DTIException using an error code message, an error code and a description.
	 */
	public DTIException(Class classNameIn, DTIErrorCode errorIn,
			String logMessageIn, Exception wrappedExceptionIn) {
		super();

		date = java.util.Calendar.getInstance().getTime();
		className = classNameIn;
		dtiErrorCode = errorIn;
		code = errorIn.getErrorCode();
		logMessage = logMessageIn;
		externalException = wrappedExceptionIn;

		return;
	}

	/**
	 * Initializes a DTIException using an error code message, an error code and a description.
	 * 
	 * @param methodNameIn
	 * @param classNameIn
	 * @param tpRefNumberIn
	 * @param messageKeyIn
	 * @param logMessageIn
	 * @param wrappedExceptionIn
	 */
	public DTIException(String methodNameIn, Class classNameIn,
			Integer tpRefNumberIn, String messageKeyIn, String logMessageIn,
			Exception wrappedExceptionIn) {
		super();

		date = java.util.Calendar.getInstance().getTime();
		methodName = methodNameIn;
		className = classNameIn;
		tpRefNumber = tpRefNumberIn;

		if (messageKeyIn == null) {
			messageKeyIn = DEFAULT_MESSAGE;
		}

		code = messageKeyIn;
		message = getMessage(messageKeyIn);
		logMessage = logMessageIn + " " + getMessage(messageKeyIn);
		externalException = wrappedExceptionIn;

		return;
	}

	/**
	 * Returns the classname.
	 * 
	 * @return java.lang.Class
	 */
	public Class getClassName() {
		return className;
	}

	/**
	 * @return the dtiErrorCode
	 */
	public DTIErrorCode getDtiErrorCode() {
		return dtiErrorCode;
	}

	/**
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * 
	 * @return Exception
	 */
	public Exception getExternalException() {
		return externalException;
	}

	/**
	 * 
	 * @return java.lang.String
	 */
	public String getLogMessage() {
		return logMessage;
	}

	/**
	 * 
	 * @return java.lang.String
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @return java.lang.String
	 * @param messageKey
	 *            java.lang.String
	 */
	private String getMessage(String messageKey) {
		String message = "";
		return message;
	}

	/**
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getMethodName() {
		return methodName;
	}

	/**
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String toString() {
		StringBuffer message = new StringBuffer();

		message.append("Class: ");
		if (getClassName() != null) {
			message.append(className.getName());
		}
		else {
			message.append("no class supplied");
		}
		message.append(" Method: " + methodName);
		message.append(" Time: " + date);
		message.append(" TP Reference Number: " + tpRefNumber);
		message.append(" Exception: ");
		if (getExternalException() != null) {
			message.append(externalException.toString());
		}
		else {
			message.append("no external exception");
		}
		message.append(" Log Message: " + logMessage);

		return message.toString();
	}

	/**
	 * Gets the code
	 * 
	 * @return Returns a String
	 */
	public String getCode() {
		return code;
	}

	public Integer getTpRefNumber() {
		return tpRefNumber;
	}

	public void setTpRefNumber(Integer tpRefNumber) {
		this.tpRefNumber = tpRefNumber;
	}
}
