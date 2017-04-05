package pvt.disney.dti.gateway.rules.wdw;

import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTIResponseTO;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.VoidReservationRequestTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO;
import pvt.disney.dti.gateway.provider.wdw.data.OTCommandTO.OTTransactionType;
import pvt.disney.dti.gateway.provider.wdw.data.OTManageReservationTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTClientDataTO;
import pvt.disney.dti.gateway.provider.wdw.data.common.OTProductTO;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Unit Tests case for WDWVoidReservationRules
 * @author RASTA006
 *
 */
public class WDWVoidReservationRulesTestCase extends CommonTestUtils{
	@Before
	public void setUp() throws Exception {
				setMockProperty();
	}
	/**
	 * test case for transformRequest method
	 */
	@Test
	public void testTransformRequest(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.VOIDRESERVATION);
		createCommonRequest(dtiTxn);
		VoidReservationRequestTO dtiVoidRes=new VoidReservationRequestTO();
		dtiVoidRes.setResNumber("1");
		//dtiVoidRes.setResCode("1");
		dtiVoidRes.setExternalResCode("1");
		dtiTxn.getRequest().setCommandBody(dtiVoidRes);
		dtiTxn.setTpRefNum(new Integer(1));
		dtiTxn.setAttributeTOMap(DTIMockUtil.fetchAttributeTOMapList());
		String xmlString=null;
		DTIMockUtil.mockTicketInfoList();
		/*Scenario:: 1 passing the dtTxn Object*/
		try{
			xmlString=WDWVoidReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		}catch(DTIException dtie){
			Assert.fail("Unexepected Exception "+dtie.getLogMessage());
		}
		dtiVoidRes.setResCode("1");
		try{
			xmlString=WDWVoidReservationRules.transformRequest(dtiTxn);
			assertNotNull(xmlString);
		}catch(DTIException dtie){
			Assert.fail("Unexepected Exception "+dtie.getLogMessage());
		}
	}
	/**
	 * Test case for transformResponseBody
	 */
	@Test
	public void testTransformResponseBody(){
		DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.VOIDRESERVATION);
	
		OTManageReservationTO oManageReser=new OTManageReservationTO();
		oManageReser.setCommandType("1");
		oManageReser.setProductList(getOTProductList());
		oManageReser.setReservationCode("1");
		OTClientDataTO clientData=new OTClientDataTO();
		clientData.setClientUniqueId(new Integer(1));
		oManageReser.setClientData(clientData);
		OTTransactionType txType=OTTransactionType.UNDEFINED;
		OTCommandTO otCmdTO=new OTCommandTO(txType);
		otCmdTO.setManageReservationTO(oManageReser);
		DTIResponseTO dtiRespTO=new DTIResponseTO();
		EntityTO entityTO = new EntityTO();
		entityTO.setDefPymtId(3);
		entityTO.setDefPymtData("1");
		entityTO.setEntityId(1);
		dtiTxn.setEntityTO(entityTO);
		DTIMockUtil.processMockprepareAndExecuteSql();
		
		/*Scenario:: 1 when passing the required parameter */
		try{
		WDWVoidReservationRules.transformResponseBody(dtiTxn, otCmdTO, dtiRespTO);
		}catch(DTIException dtie){
			Assert.fail("Unexepected Exception "+dtie.getLogMessage());
		}
	}
	
	/**
	 * fetch OTProductList;
	 * @return
	 */
	private ArrayList<OTProductTO> getOTProductList(){
		ArrayList<OTProductTO> products=new ArrayList<OTProductTO>();
		OTProductTO otProduct=new OTProductTO();
		otProduct.setItem(new BigInteger("1"));
		//otProduct
		products.add(otProduct);
		return products;
	}

}
