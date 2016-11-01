package pvt.disney.dti.gateway.dao.data;

/**
 * This class is the result object which is used by DAO classes to represent fields from a row of the INBOUND_TP_LOG table.
 * 
 * @author lewit019
 */
public class ITPLogEntry {

	/** Transaction identifier */
	private Integer transId;

	/** Payload ID */
	private String payloadId;

	/** Target System */
	private String targSys;

	/** XML Version */
	private String xmlVersion;

	/** Communications protocol */
	private String commProtocol;

	/** Communications method */
	private String commMethod;

	/** Inbound ticket seller identifier */
	private Long inboundTsId;

	/**
	 * @return the commMethod
	 */
	public String getCommMethod() {
		return commMethod;
	}

	/**
	 * @return the commProtocol
	 */
	public String getCommProtocol() {
		return commProtocol;
	}

	/**
	 * @return the inboundTsId
	 */
	public Long getInboundTsId() {
		return inboundTsId;
	}

	/**
	 * @return the payloadId
	 */
	public String getPayloadId() {
		return payloadId;
	}

	/**
	 * @return the targSys
	 */
	public String getTargSys() {
		return targSys;
	}

	/**
	 * @return the transId
	 */
	public Integer getTransId() {
		return transId;
	}

	/**
	 * @return the xmlVersion
	 */
	public String getXmlVersion() {
		return xmlVersion;
	}

	/**
	 * @param commMethod
	 *            the commMethod to set
	 */
	public void setCommMethod(String commMethod) {
		this.commMethod = commMethod;
	}

	/**
	 * @param commProtocol
	 *            the commProtocol to set
	 */
	public void setCommProtocol(String commProtocol) {
		this.commProtocol = commProtocol;
	}

	/**
	 * @param inboundTsId
	 *            the inboundTsId to set
	 */
	public void setInboundTsId(Long inboundTsId) {
		this.inboundTsId = inboundTsId;
	}

	/**
	 * @param payloadId
	 *            the payloadId to set
	 */
	public void setPayloadId(String payloadId) {
		this.payloadId = payloadId;
	}

	/**
	 * @param targSys
	 *            the targSys to set
	 */
	public void setTargSys(String targSys) {
		this.targSys = targSys;
	}

	/**
	 * @param transId
	 *            the transId to set
	 */
	public void setTransId(Integer transId) {
		this.transId = transId;
	}

	/**
	 * @param xmlVersion
	 *            the xmlVersion to set
	 */
	public void setXmlVersion(String xmlVersion) {
		this.xmlVersion = xmlVersion;
	}

}
