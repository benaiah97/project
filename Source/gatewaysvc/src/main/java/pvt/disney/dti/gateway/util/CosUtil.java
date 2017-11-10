package pvt.disney.dti.gateway.util;

import com.disney.logging.EventLogger;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.CosGrpKey;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.common.CosGrpTO;


/**
 * The Class CosUtil. Can be used to lookup a class of service Group for a DTI Transaction
 */
public class CosUtil {

	  /** Event logger. */
	  private static final EventLogger logger = EventLogger
	      .getLogger("pvt.disney.dti.gateway.util.CosUtil");
	
	/**
	 * Lookup cos grp.
	 * 
	 * String private is the DTITransactionTO.Provider
	 * String transType id the DTITransactionTO.TransactionType
	 *
	 * @param provider the provider
	 * @param transType the trans type
	 * @return the cos grp TO
	 */
	private static  CosGrpTO lookupCosGrp(String transProvider, String transType ) throws DTIException {
		CosGrpTO cosGrp = new CosGrpTO();
		//uses first three characters of the DTITransactionTO.Provider
		cosGrp = CosGrpKey.getTsCmdCosGrp(transProvider.substring(0, 3), transType);
		
		return cosGrp;
	}
	
	/**
	 * Lookup cos grp.
	 *
	 * @param txnTO the txn TO
	 * @return the cos grp TO
	 * @throws DTIException the DTI exception
	 */
	public static CosGrpTO lookupCosGrp(DTITransactionTO txnTO) throws DTIException {
		return lookupCosGrp(txnTO.getProvider().toString(), txnTO.getTransactionType().toString());	
	}
	
}
