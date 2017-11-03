package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * The Class CosGrpCmdXrefTO.
 */
public class CosGrpCmdXrefTO implements Serializable {
	
	/**  Standard serial Version UID. */
	private static final long serialVersionUID = 9429431996L;

	/** The cosxrefid. */
	private int cosxrefid;
	
	/** The cosgrpid. */
	private int cosgrpid;
	
	/** The cosgrpownerid. */
	private String cosgrpownerid;
	
	/** The command code. */
	private String commandCode;

	/**
	 * Gets the cosxrefid.
	 *
	 * @return the cosxrefid
	 */
	public int getCosxrefid() {
		return cosxrefid;
	}

	/**
	 * Sets the cosxrefid.
	 *
	 * @param cosxrefid the new cosxrefid
	 */
	public void setCosxrefid(int cosxrefid) {
		this.cosxrefid = cosxrefid;
	}

	/**
	 * Gets the cosgrpid.
	 *
	 * @return the cosgrpid
	 */
	public int getCosgrpid() {
		return cosgrpid;
	}

	/**
	 * Sets the cosgrpid.
	 *
	 * @param cosgrpid the new cosgrpid
	 */
	public void setCosgrpid(int cosgrpid) {
		this.cosgrpid = cosgrpid;
	}

	/**
	 * Gets the cosgrpownerid.
	 *
	 * @return the cosgrpownerid
	 */
	public String getCosgrpownerid() {
		return cosgrpownerid;
	}

	/**
	 * Sets the cosgrpownerid.
	 *
	 * @param cosgrpownerid the new cosgrpownerid
	 */
	public void setCosgrpownerid(String cosgrpownerid) {
		this.cosgrpownerid = cosgrpownerid;
	}

	/**
	 * Gets the command code.
	 *
	 * @return the command code
	 */
	public String getCommandCode() {
		return commandCode;
	}

	/**
	 * Sets the command code.
	 *
	 * @param commandCode the new command code
	 */
	public void setCommandCode(String commandCode) {
		this.commandCode = commandCode;
	}
	
	
}
