package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.util.ArrayList;

public class GWProductsTO implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;

	private ArrayList<GWProductTO> gwProductList = new ArrayList<GWProductTO>();

	public ArrayList<GWProductTO> getGwProductList() {
		return gwProductList;
	}

}
