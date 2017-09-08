package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DemographicsInfoTO;

/**
 * The Class UpdateDemographicsRequestTO.
 */
public class UpdateDemographicsRequestTO extends CommandBodyTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The demographics info list. */
	//1 to 8
	private ArrayList<DemographicsInfoTO> demographicsInfoList;
	

	/**
	 * The Enum DemographicType.
	 * C=Season passes (default) M=Major clients
       P=Private clients G=Associations S=Sellers D=Employees U=Users
       R=Registrant
       I=IB Payor O=Coupons H=Vouchers
	 */
	public enum DemographicType {
		
		/**  SEASON PASSES (DEFAULT). */
		C,
		
		/**  MAJOR CLIENTS. */
		M,
		
		/**  PRIVATE CLIENTS. */
		P,
		
		/**  ASSSOCIATIONS. */
		G,
		
		/**  SELLERS. */
		S,
		
		/**  EMPLOYEES. */
		D,
		
		/**  USERS. */
		U,
		
		/**  REGISTRANT. */
		R,
		
		/**  IB PAYOR. */
		I,
		
		/**  COUPONS. */
		O,
		
		/**  VOUCHERS. */
		H,
		
		/** The l. */
		L,
		
		/** The undefined. */
		UNDEFINED
	};
	
	
	/** The demographic type. */
	private DemographicType demographicType = DemographicType.UNDEFINED;


	/**
	 * Gets the demographics info list.
	 *
	 * @return the demographics info list
	 */
	public ArrayList<DemographicsInfoTO> getDemographicsInfoList() {
		return demographicsInfoList;
	}


	/**
	 * Sets the demographics info list.
	 *
	 * @param demographicsInfoList the new demographics info list
	 */
	public void setDemographicsInfoList(ArrayList<DemographicsInfoTO> demographicsInfoList) {
		this.demographicsInfoList = demographicsInfoList;
	}


	/**
	 * Gets the demographic type.
	 *
	 * @return the demographic type
	 */
	public DemographicType getDemographicType() {
		return demographicType;
	}


	/**
	 * Sets the demographic type.
	 *
	 * @param demographicType the new demographic type
	 */
	public void setDemographicType(DemographicType demographicType) {
		this.demographicType = demographicType;
	}
	
	
	/**
	 * Convenience method to add a TO to the demographicsInfoTO list
	 *
	 * @param to the to
	 */
	public void addDemographicsInfoTO(DemographicsInfoTO to) {
		demographicsInfoList.add(to);
	}
}
