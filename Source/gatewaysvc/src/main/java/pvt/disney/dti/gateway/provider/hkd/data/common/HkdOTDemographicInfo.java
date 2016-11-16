package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class encapsulates the Omni XML tag "DemographicInfo".
 * 
 * @author LEWIT019
 * @since 2.16.3
 */
public class HkdOTDemographicInfo implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	/** The list of demographics applied to tickets. */
	private ArrayList<HkdOTDemographicData> demoDataList = new ArrayList<HkdOTDemographicData>();

	/**
	 * @return the demoDataList
	 */
	public ArrayList<HkdOTDemographicData> getDemoDataList() {
		return demoDataList;
	}

	/**
	 * @param demoDataList
	 *            the demoDataList to set
	 */
	public void setDemoDataList(ArrayList<HkdOTDemographicData> demoDataList) {
		this.demoDataList = demoDataList;
	}

	/**
	 * Adds a new OTDemographicData to the list.
	 */
	public void addOTDemographicData(HkdOTDemographicData newData) {
		demoDataList.add(newData);
	}

}
