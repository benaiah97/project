package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;

/**
 * The Class CosTpGrpCmdTO.
 * Returns an object for commands, including the class of service group a command is in, the provider/owwner,
 * the endpoint url for the cos group the command assigned to, the command code found in the dtix transaction xml and enum for command type.
 */
public class CosTpGrpCmdTO implements Serializable {

	/**  Standard serial Version UID. */
	private static final long serialVersionUID = 9133431995L;
	
	/** The cosxrefid. */
	private int cosxrefid;
	
	/** The cmdid. */
	private int cmdid;
	
	/** The ownerid. */
	private String ownerid;
	
	/** The cosgrpid. */
	private int cosgrpid;
	
	/** The endpointurl. */
	private String endpointurl;

	/** The grpownerid. */
	private String grpownerid;
	
	/** The groupname. */
	private String groupname;
	
	/** The cmdcode. */
	private String cmdcode;

	/**
	 * Instantiates a new cos tp grp cmd TO.
	 */
	public CosTpGrpCmdTO() {
	}

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
	 * Gets the cmdid.
	 *
	 * @return the cmdid
	 */
	public int getCmdid() {
		return cmdid;
	}

	/**
	 * Sets the cmdid.
	 *
	 * @param cmdid the new cmdid
	 */
	public void setCmdid(int cmdid) {
		this.cmdid = cmdid;
	}

	/**
	 * Gets the ownerid.
	 *
	 * @return the ownerid
	 */
	public String getOwnerid() {
		return ownerid;
	}

	/**
	 * Sets the ownerid.
	 *
	 * @param ownerid the new ownerid
	 */
	public void setOwnerid(String owenerid) {
		this.ownerid = owenerid;
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
	 * Gets the endpointurl.
	 *
	 * @return the endpointurl
	 */
	public String getEndpointurl() {
		return endpointurl;
	}

	/**
	 * Sets the endpointurl.
	 *
	 * @param endpointurl the new endpointurl
	 */
	public void setEndpointurl(String endpointurl) {
		this.endpointurl = endpointurl;
	}

	/**
	 * Gets the grpownerid.
	 *
	 * @return the grpownerid
	 */
	public String getGrpownerid() {
		return grpownerid;
	}

	/**
	 * Sets the grpownerid.
	 *
	 * @param grpownerid the new grpownerid
	 */
	public void setGrpownerid(String grpownerid) {
		this.grpownerid = grpownerid;
	}

	/**
	 * Gets the groupname.
	 *
	 * @return the groupname
	 */
	public String getGroupname() {
		return groupname;
	}

	/**
	 * Sets the groupname.
	 *
	 * @param groupname the new groupname
	 */
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	/**
	 * Gets the cmdcode.
	 *
	 * @return the cmdcode
	 */
	public String getCmdcode() {
		return cmdcode;
	}

	/**
	 * Sets the cmdcode.
	 *
	 * @param cmdcode the new cmdcode
	 */
	public void setCmdcode(String cmdcode) {
		this.cmdcode = cmdcode;
	}
	
}
