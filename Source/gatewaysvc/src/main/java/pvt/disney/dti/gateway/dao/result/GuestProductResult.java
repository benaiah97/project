/*
 * 
 */
package pvt.disney.dti.gateway.dao.result;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import pvt.disney.dti.gateway.connection.ResultSetProcessor;
import pvt.disney.dti.gateway.data.common.GuestProductTO;

import com.disney.exception.WrappedException;

// TODO: Auto-generated Javadoc
/**
 * The Class GuestProductResult.
 */
public class GuestProductResult implements ResultSetProcessor {
	
	/** The List of database products for Guest Product. . */
	private ArrayList<GuestProductTO> results = new ArrayList<GuestProductTO>();
	
	/**
	 * Instantiates a new guest product result.
	 */
	GuestProductResult(){
		super();
	}


		
	/**
	 * Returns the processed object.
	 * 
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#getProcessedObject()
	 */
	@Override
	public Object getProcessedObject() throws WrappedException {
		
		return results;
	}
	
	/**
	 * Processes the next (or only) value in the results set.
	 *
	 * @param rs the rs
	 * @throws SQLException the SQL exception
	 * @throws WrappedException the wrapped exception
	 * @see pvt.disney.dti.gateway.connection.ResultSetProcessor#processNextResultSet(java.sql.ResultSet)
	 */
	@Override
	public void processNextResultSet(ResultSet rs) throws SQLException,
			WrappedException {
		
		GuestProductTO aProduct = new GuestProductTO();

		// PDT_CODE
		aProduct.setPdtCode(rs.getString("PDT_CODE"));
		
		// TICKET NAME
		aProduct.setTktSysId(rs.getString("TKTSYSID"));
		
		// PDTID
		aProduct.setPdtid(new BigInteger(rs.getString("PDTID")));

		// ELIG_GRPID
		String eligGrpidString = rs.getString("ELIG_GRPID");
		if (eligGrpidString != null) aProduct.setEligGrpid(new BigInteger(
				eligGrpidString));

		// PDT_DESC
		aProduct.setPdtDesc(rs.getString("PDT_DESC"));

		// TAX
		aProduct.setTax(rs.getBigDecimal("TAX"));

		// TAX1
		aProduct.setTax1(rs.getBigDecimal("TAX1"));

		// TAX2
		aProduct.setTax2(rs.getBigDecimal("TAX2"));

		// PRINTED_PRICE
		aProduct.setPrintedPrice(rs.getBigDecimal("PRINTED_PRICE"));

		// UNIT_PRICE
		aProduct.setUnitPrice(rs.getBigDecimal("UNIT_PRICE"));

		// ACTIVE_IND
		String activeIndString = rs.getString("ACTIVE_IND");
		if (activeIndString.compareToIgnoreCase("T") == 0) aProduct
				.setActiveInd(true);
		else aProduct.setActiveInd(false);

		// SOLD_OUT
		String soldOutString = rs.getString("SOLD_OUT");
		if (soldOutString == null) aProduct.setSoldOut(false);
		else aProduct.setSoldOut(true);

		// MISMATCH_IND
		String mismatchIndString = rs.getString("MISMATCH_IND");
		if (mismatchIndString.compareToIgnoreCase("T") == 0) aProduct
				.setMisMatchInd(true);
		else aProduct.setMisMatchInd(false);

		// MISMATCH_TOL_TYPE
		String mismatchTolTypeString = rs.getString("MISMATCH_TOL_TYPE");
		
		if (mismatchTolTypeString != null) {
			if (mismatchTolTypeString.compareTo("Amount") == 0) aProduct
					.setMisMatchTolType(GuestProductTO.MismatchToleranceType.AMOUNT);
			if (mismatchTolTypeString.compareTo("Percent") == 0) aProduct
					.setMisMatchTolType(GuestProductTO.MismatchToleranceType.PERCENT);
		} else {
			aProduct.setMisMatchTolType(GuestProductTO.MismatchToleranceType.UNDEFINED);
		}
		
		// MISMATCH_TOL
		BigDecimal mismatchTol = rs.getBigDecimal("MISMATCH_TOL");
		if (mismatchTol != null) aProduct.setMisMatchTol(mismatchTol);

		// START_SALE_DATE
		Date startSaleDate = rs.getDate("START_SALE_DATE");
		if (startSaleDate != null) {
			GregorianCalendar gCal = (GregorianCalendar) GregorianCalendar
					.getInstance();
			gCal.setTime(startSaleDate);
			aProduct.setStartSaleDate(gCal);
		}

		// END_SALE_DATE
		Date endSaleDate = rs.getDate("END_SALE_DATE");
		if (endSaleDate != null) {
			GregorianCalendar gCal = (GregorianCalendar) GregorianCalendar
					.getInstance();
			gCal.setTime(endSaleDate);
			aProduct.setEndSaleDate(gCal);
		}

		// VALIDITY_DATES_IND
		String validityDatesInd = rs.getString("VALIDITY_DATES_IND");
		aProduct.setValiditydatesInd(false);
		if (validityDatesInd != null) {
			if (validityDatesInd.compareToIgnoreCase("T") == 0) aProduct
					.setValiditydatesInd(true);
		}

		// START_SALE_DATE
		Date startValidDate = rs.getDate("START_VALID_DATE");
		if (startValidDate != null) {
			GregorianCalendar gCal = (GregorianCalendar) GregorianCalendar
					.getInstance();
			gCal.setTime(startValidDate);
			aProduct.setStartValidDate(gCal);
		}

		// END_SALE_DATE
		Date endValidDate = rs.getDate("END_VALID_DATE");
		if (endValidDate != null) {
			GregorianCalendar gCal = (GregorianCalendar) GregorianCalendar
					.getInstance();
			gCal.setTime(endValidDate);
			aProduct.setEndValidDate(gCal);
		}

		// CONSUMABLE_IND
		String consumableIndString = rs.getString("CONSUMABLE_IND");
		if (consumableIndString.compareToIgnoreCase("T") == 0) aProduct
				.setConsumableInd(true);
		else aProduct.setConsumableInd(false);

		// DEMOGRAPHIC_IND (As of 2.9)
		String demographicIndString = rs.getString("DEMOGRAPHIC_IND");
		if (demographicIndString == null) {
			aProduct.setDemographicInd(false);
		}
		else {
			if (demographicIndString.compareToIgnoreCase("T") == 0) aProduct
					.setDemographicInd(true);
			else aProduct.setDemographicInd(false);
		}

		// DAY_CLASS (As of 2.12)
		String dayClassString = rs.getString("DAY_CLASS");
		if (dayClassString != null) {
			aProduct.setDayClass(dayClassString);
		}
		else {
			aProduct.setDayClass("NONE");
		}

		// DAY_SUBCLASS (As of 2.16.1)
		String daySubclassString = rs.getString("DAY_SUBCLASS");
		if (daySubclassString != null) {
			aProduct.setDaySubclass(daySubclassString);
		}
		else {
			aProduct.setDaySubclass("NONE");
		}

		// STANDARD_RETAIL_PRICE (As of 2.12)
		BigDecimal standardRetailPrice = rs
				.getBigDecimal("STANDARD_RETAIL_PRICE");
		if (standardRetailPrice != null) {
			aProduct.setStandardRetailPrice(standardRetailPrice);
		}
		
        //RESIDENT_IND
		String residentIndString = rs.getString("RESIDENT_IND");
		if(residentIndString.compareToIgnoreCase("T")==0){
			aProduct.setResidentInd(true);
		}else{
			aProduct.setResidentInd(false);
		}
		
		
		//STANDARD_RETAIL_TAX
		String standardRetailTax = String.valueOf(rs.getBigDecimal("STANDARD_RETAIL_TAX"));
		if (standardRetailTax != null) {
			aProduct.setStandardRetailTax(standardRetailTax);
		}
		
		//UPGRD_PATH_ID
		BigDecimal upGrdPathId=rs.getBigDecimal("UPGRD_PATH_ID");
		aProduct.setUpgradePathId(String.valueOf(upGrdPathId));
		
		/* Adding the result set to result*/
		results.add(aProduct);

		return;

	}

}
