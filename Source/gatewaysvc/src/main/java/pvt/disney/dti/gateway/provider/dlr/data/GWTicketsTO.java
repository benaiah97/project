package pvt.disney.dti.gateway.provider.dlr.data;

import java.io.Serializable;
import java.util.ArrayList;

public class GWTicketsTO implements Serializable {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;

	private ArrayList<GWTicketTO> gwTicketList = new ArrayList<GWTicketTO>();

	public ArrayList<GWTicketTO> getGwTicketList() {
		return gwTicketList;
	}

}
