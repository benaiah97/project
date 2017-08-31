package pvt.disney.dti.gateway.data.common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Class MatchDemographicDataTO.
 */
public class MatchDemographicDataTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7492917339L;
	/** The field TO list. */
	private ArrayList<FieldTO> fieldTOList;

	/**
	 * Gets the field TO list.
	 *
	 * @return the field TO list
	 */
	public ArrayList<FieldTO> getFieldTOList() {
		return fieldTOList;
	}

	/**
	 * Sets the field TO list.
	 *
	 * @param fieldTOList the new field TO list
	 */
	public void setFieldTOList(ArrayList<FieldTO> fieldTOList) {
		this.fieldTOList = fieldTOList;
	}
	
	
}
