package pvt.disney.dti.gateway.data;

import java.io.Serializable;
import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.CommandBodyTO;
import pvt.disney.dti.gateway.data.common.DemographicDataTO;
import pvt.disney.dti.gateway.data.common.DemographicsInfoTO;

/**
 * The Class UpdateDemographicsResponseTO.
 */
public class UpdateDemographicsResponseTO extends CommandBodyTO implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4826284944L;

	/** The demographics info TO list. */
	private ArrayList<DemographicsInfoTO> demographicsInfoTOList;

	private DemographicDataTO demographicsDataTO;
	
	/**
	 * Gets the demographics info TO list.
	 *
	 * @return the demographics info TO list
	 */
	public ArrayList<DemographicsInfoTO> getDemographicsInfoTOList() {
		return demographicsInfoTOList;
	}

	/**
	 * Sets the demographics info TO list.
	 *
	 * @param demographicsInfoTOList the new demographics info TO list
	 */
	public void setDemographicsInfoTOList(ArrayList<DemographicsInfoTO> demographicsInfoTOList) {
		this.demographicsInfoTOList = demographicsInfoTOList;
	}

	/**
	 * @return the demographicsDataTO
	 */
	public DemographicDataTO getDemographicsDataTO() {
		return demographicsDataTO;
	}

	/**
	 * @param demographicsDataTO the demographicsDataTO to set
	 */
	public void setDemographicsDataTO(DemographicDataTO demographicsDataTO) {
		this.demographicsDataTO = demographicsDataTO;
	}
	
	
}
