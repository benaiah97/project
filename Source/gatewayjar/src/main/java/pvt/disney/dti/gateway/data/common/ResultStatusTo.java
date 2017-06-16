/**
 * 
 */
package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * @author RASTA006
 *
 */
public class ResultStatusTo implements Serializable {

	private static final long serialVersionUID = 5053301391682911498L;

	public enum ResultType {
		INELIGIBLE, 
		NOPRODUCTS, 
		ELIGIBLE
	};

	private ResultType resultType = ResultType.INELIGIBLE;

	public ResultStatusTo(ResultType resultType) {
		this.resultType = resultType;
	}

	/**
	 * @return the resultType
	 */
	public ResultType getResultType() {
		return resultType;
	}

	/**
	 * @param resultType
	 *            the resultType to set
	 */
	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

}
