package pvt.disney.dti.gateway.provider.hkd.data.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class encapsulates the tag used by Omni (DemographicData).
 * 
 * @author LEWIT019
 * @since 2.16.3
 */
public class HkdOTDemographicData implements Serializable {

	/** Serial version UID */
	private static final long serialVersionUID = 1L;

	/** The array of ticket demographic fields. */
	private ArrayList<HkdOTFieldTO> demoDataList = new ArrayList<HkdOTFieldTO>();

	/**
	 * @return the demoDataList
	 */
	public ArrayList<HkdOTFieldTO> getDemoDataList() {
		return demoDataList;
	}

	/**
	 * @param demoDataList
	 *            the demoDataList to set
	 */
	public void setDemoDataList(ArrayList<HkdOTFieldTO> demoDataList) {
		this.demoDataList = demoDataList;
	}

	/** Adds an OT Field to the demo data list. */
	public void addOTField(HkdOTFieldTO aField) {
		demoDataList.add(aField);
	}

}
