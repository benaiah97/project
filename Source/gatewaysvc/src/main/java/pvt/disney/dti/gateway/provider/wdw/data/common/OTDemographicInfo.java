package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class encapsulates the Omni XML tag "DemographicInfo".
 * 
 * @author LEWIT019
 * @since 2.9
 */
public class OTDemographicInfo implements Serializable {

	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	/** The list of demographics applied to tickets. */
	private ArrayList<OTDemographicData> demoDataList = new ArrayList<OTDemographicData>();

	/**
	 * @return the demoDataList
	 */
	public ArrayList<OTDemographicData> getDemoDataList() {
		return demoDataList;
	}

	/**
	 * @param demoDataList
	 *            the demoDataList to set
	 */
	public void setDemoDataList(ArrayList<OTDemographicData> demoDataList) {
		this.demoDataList = demoDataList;
	}

	/**
	 * Adds a new OTDemographicData to the list.
	 */
	public void addOTDemographicData(OTDemographicData newData) {
		demoDataList.add(newData);
	}

}
