package pvt.disney.dti.gateway.provider.wdw.data.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class encapsulates the tag used by Omni (DemographicData).
 * 
 * @author LEWIT019
 * @since 2.9
 */
public class OTDemographicData implements Serializable {

	/** Serial version UID */
	private static final long serialVersionUID = 1L;

	/** The array of ticket demographic fields. */
	private ArrayList<OTFieldTO> demoDataList = new ArrayList<OTFieldTO>();

	/**
	 * @return the demoDataList
	 */
	public ArrayList<OTFieldTO> getDemoDataList() {
		return demoDataList;
	}

	/**
	 * @param demoDataList
	 *            the demoDataList to set
	 */
	public void setDemoDataList(ArrayList<OTFieldTO> demoDataList) {
		this.demoDataList = demoDataList;
	}

	/** Adds an OT Field to the demo data list. */
	public void addOTField(OTFieldTO aField) {
		demoDataList.add(aField);
	}

}
