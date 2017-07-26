package pvt.disney.dti.gateway.dao.data;

import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;


// TODO: Auto-generated Javadoc
/**
 * The Class GuestProductTO.
 * 
 */
public class GuestProductTO {
	
	/** The dbproduct TO. */
	private DBProductTO dbproductTO;
	
	/** The OMNI ticket list. */
	private ArrayList<OTTicketInfoTO> otTicketList;
	
	/** The gw data resp TO. */
	private GWDataRequestRespTO gwDataRespTO;

	/**
	 * @return the dbproductTO
	 */
	public DBProductTO getDbproductTO() {
		return dbproductTO;
	}

	/**
	 * @param dbproductTO the dbproductTO to set
	 */
	public void setDbproductTO(DBProductTO dbproductTO) {
		this.dbproductTO = dbproductTO;
	}

	/**
	 * @return the otTicketList
	 */
	public ArrayList<OTTicketInfoTO> getOtTicketList() {
		return otTicketList;
	}

	/**
	 * @param otTicketList the otTicketList to set
	 */
	public void setOtTicketList(ArrayList<OTTicketInfoTO> otTicketList) {
		this.otTicketList = otTicketList;
	}

	/**
	 * @return the gwDataRespTO
	 */
	public GWDataRequestRespTO getGwDataRespTO() {
		return gwDataRespTO;
	}

	/**
	 * @param gwDataRespTO the gwDataRespTO to set
	 */
	public void setGwDataRespTO(GWDataRequestRespTO gwDataRespTO) {
		this.gwDataRespTO = gwDataRespTO;
	}
	
	
}