package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.math.BigInteger;

import pvt.disney.dti.gateway.constants.DTIErrorCode;

/**
 * This class represents an error in standard DTI format.
 * 
 * @author lewit019
 */
public class DTIErrorTO implements Serializable {

	/** Serial Version UID */
	final static long serialVersionUID = 9129231995L;

	/** The error code (number). */
	private BigInteger errorCode;
	/** The error criticality (warning, error, etc.). */
	private String errorType;
	/** The error class (application sending the error). */
	private String errorClass;
	/** Narrative error text. */
	private String errorText;
	/** Error scope (payload, command, or ticket. */
	private DTIErrorCode.ErrorScope errorScope;
	/** TP Error criticality */
	private String tpCriticality;

	/**
	 * Private constructor for DTI Error Transfer Object.
	 * 
	 */
	private DTIErrorTO() {
		super();
	}

	/**
	 * Constructor for the DTIErrorTO.
	 * 
	 * @param errorCodeIn
	 *            The error code.
	 * @param errorClassIn
	 *            The error class.
	 * @param errorTextIn
	 *            The error text.
	 * @param errorTypeIn
	 *            The error type.
	 */
	public DTIErrorTO(BigInteger errorCodeIn, String errorClassIn,
			String errorTextIn, String errorTypeIn) {

		errorCode = errorCodeIn;
		errorClass = errorClassIn;
		errorText = errorTextIn;
		errorType = errorTypeIn;

		if (errorCode != null) {
			errorScope = DTIErrorCode.getScopeFromCode(errorCodeIn.toString());
		}
	}

	/**
	 * Constructor for the DTIErrorTO, but this one include Ticket Provider criticality.
	 * 
	 * @param errorCodeIn
	 *            The error code.
	 * @param errorClassIn
	 *            The error class.
	 * @param errorTextIn
	 *            The error text.
	 * @param errorTypeIn
	 *            The error type.
	 * @param tpCriticalityIn
	 *            The ticket provider criticality.
	 */
	public DTIErrorTO(BigInteger errorCodeIn, String errorClassIn,
			String errorTextIn, String errorTypeIn, String tpCriticalityIn) {

		errorCode = errorCodeIn;
		errorClass = errorClassIn;
		errorText = errorTextIn;
		errorType = errorTypeIn;
		tpCriticality = tpCriticalityIn;

		if (errorCode != null) {
			errorScope = DTIErrorCode.getScopeFromCode(errorCodeIn.toString());
		}
	}

	/**
	 * Gets a copy of this object.
	 * 
	 * @return a copy of the current DTIErrorTO object.
	 */
	public DTIErrorTO getCopy() {

		DTIErrorTO errCopy = new DTIErrorTO();
		errCopy.setErrorScope(errorScope);
		errCopy.setErrorCode(errorCode);
		errCopy.setErrorClass(errorClass);
		errCopy.setErrorText(errorText);
		errCopy.setTpCriticality(tpCriticality);
		errCopy.setErrorType(errorType);

		return errCopy;
	}

	/**
	 * @return the errorClass
	 */
	public String getErrorClass() {
		return errorClass;
	}

	/**
	 * @return the errorType
	 */
	public String getErrorType() {
		return errorType;
	}

	/**
	 * @return the errorText
	 */
	public String getErrorText() {
		return errorText;
	}

	/**
	 * @return the errorCode
	 */
	public BigInteger getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorClass
	 *            the errorClass to set
	 */
	private void setErrorClass(String errorClass) {
		this.errorClass = errorClass;
	}

	/**
	 * @param errorType
	 *            the errorType to set
	 */
	private void setErrorType(String errorCriticality) {
		this.errorType = errorCriticality;
	}

	/**
	 * @param errorText
	 *            the errorText to set
	 */
	private void setErrorText(String errorName) {
		this.errorText = errorName;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	private void setErrorCode(BigInteger errorNumber) {
		this.errorCode = errorNumber;
	}

	/**
	 * Override of the nominal class "toString()" method to provide detail output about this object's contents.
	 */
	public String toString() {
		String output = "DTIErrorTO: code =[" + errorCode + "] type=[" + errorType + "] class=[" + errorClass + "] text=[" + errorText + "]";
		return output;
	}

	/**
	 * @return the errorScope
	 */
	public DTIErrorCode.ErrorScope getErrorScope() {
		return errorScope;
	}

	/**
	 * @param errorScope
	 *            the errorScope to set
	 */
	public void setErrorScope(DTIErrorCode.ErrorScope errorScope) {
		this.errorScope = errorScope;
	}

	/**
	 * Sets the error number.
	 * 
	 * @param errorNumberIn
	 *            the error number
	 * @param errorTypeIn
	 *            the error type
	 * @param errorClassIn
	 *            the error class
	 * @param errorTextIn
	 *            the error text
	 */
	public void setError(BigInteger errorNumberIn, String errorTypeIn,
			String errorClassIn, String errorTextIn) {
		errorCode = errorNumberIn;
		errorType = errorTypeIn;
		errorClass = errorClassIn;
		errorText = errorTextIn;

		return;
	}

	/**
	 * @return the tpCriticality
	 */
	public String getTpCriticality() {
		return tpCriticality;
	}

	/**
	 * @param tpCriticality
	 *            the tpCriticality to set
	 */
	public void setTpCriticality(String tpCriticality) {
		this.tpCriticality = tpCriticality;
	}

}
