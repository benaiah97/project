package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;

/**
 * The transfer object class for Omni Ticket's "MultiEntitlementAccount". This particular signature is rather diverse in the functionality that it offers, and the constructs below assist in navigating the physical message orchestrations
 * that are possible.
 * 
 * 2.16.1
 * 
 * @author biest001
 */

public class OTMultiEntitlementAccountTO implements Serializable {

	/**
	 * Auto-generated Serial Number
	 */
	private static final long serialVersionUID = -5430286184537349365L;

	/** Defines possible variations of Command Types */
	public enum CommandType {
		CreateEntitlement,
		TransferEntitlement,
		OrderEntitlement,
		CreateAccount,
		QueryAccount,
		MergeAccount,
		SetAccountStatus,
		ManageMedia
	};

	/** Defines possible variations of SubCommand Types */
	public enum SubCommandType {
		Associate,
		Deassociate,
		ChangeStatus
	};

	/** Defines possible variations of Media Status */
	public interface MediaStatus {
		static final Integer OK = 0;
		static final Integer VOID = 1;
		static final Integer STOLEN = 2;
		static final Integer LOST = 3;
	}
}