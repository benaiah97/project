package pvt.disney.dti.gateway.dao.data;

import java.util.ArrayList;

import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.provider.dlr.data.GWDataRequestRespTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTTicketInfoTO;

/**
 * The Class GuestProductTO.
 */
public class GuestProductTO {

	/** The dbproduct TO. */
	private DBProductTO dbproductTO;

	/** The OMNI ticket list. */
	private ArrayList<OTTicketInfoTO> otTicketInfoList=new ArrayList<OTTicketInfoTO>();
	
	private OTTicketInfoTO otTicketInfo;

	/** The gw data resp TO. */
	private GWDataRequestRespTO gwDataRespTO;
	
	private boolean sourcedFromDB = true;

	/**
	 * @return the dbproductTO
	 */
	public DBProductTO getDbproductTO() {
		return dbproductTO;
	}

	/**
	 * @param dbproductTO
	 *           the dbproductTO to set
	 */
	public void setDbproductTO(DBProductTO dbproductTO) {
		this.dbproductTO = dbproductTO;
	}

	/**
	 * @return the otTicketInfoList
	 */
	public ArrayList<OTTicketInfoTO> getOtTicketInfoList() {
		return otTicketInfoList;
	}

	/**
	 * @param otTicketInfoList the otTicketInfoList to set
	 */
	public void setOtTicketInfoList(ArrayList<OTTicketInfoTO> otTicketInfoList) {
		this.otTicketInfoList = otTicketInfoList;
	}

	/**
	 * @return the otTicketInfo
	 */
	public OTTicketInfoTO getOtTicketInfo() {
		return otTicketInfo;
	}

	/**
	 * @param otTicketInfo the otTicketInfo to set
	 */
	public void setOtTicketInfo(OTTicketInfoTO otTicketInfo) {
		this.otTicketInfo = otTicketInfo;
	}

	/**
	 * @return the gwDataRespTO
	 */
	public GWDataRequestRespTO getGwDataRespTO() {
		return gwDataRespTO;
	}

	/**
	 * @param gwDataRespTO
	 *           the gwDataRespTO to set
	 */
	public void setGwDataRespTO(GWDataRequestRespTO gwDataRespTO) {
		this.gwDataRespTO = gwDataRespTO;
	}
	

	/**
    * @return the sourcedFromDB
    */
   public boolean isSourcedFromDB() {
      return sourcedFromDB;
   }

   /**
    * @param sourcedFromDB the sourcedFromDB to set
    */
   public void setSourcedFromDB(boolean sourcedFromDB) {
      this.sourcedFromDB = sourcedFromDB;
   }

   /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GuestProductTO [dbproductTO=" + dbproductTO + "]";
	}
	

}