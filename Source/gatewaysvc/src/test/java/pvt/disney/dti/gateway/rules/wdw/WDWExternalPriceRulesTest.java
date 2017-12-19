/**
 * 
 */
package pvt.disney.dti.gateway.rules.wdw;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.dao.ProductKey;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.common.DBProductTO;
import pvt.disney.dti.gateway.data.common.TicketTO;
import pvt.disney.dti.gateway.data.common.TicketTransactionTO.TransactionIDType;
import pvt.disney.dti.gateway.test.util.CommonTestUtils;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * @author manjrs
 *
 */

//@RunWith(PowerMockRunner.class)
@PrepareForTest({WDWExternalPriceRules.class, ProductKey.class})
public class WDWExternalPriceRulesTest extends CommonTestUtils{

   /**
    * Test method for
    * {@link pvt.disney.dti.gateway.rules.wdw.WDWExternalPriceRules#validateDeltaProducts(java.util.ArrayList)}.
    *
    * @throws Exception
    *            the exception
    */
   //@Test (expected=DTIException.class)
  /* public void testValidateDeltaProducts() throws Exception {
      DTIMockUtil.processMockprepareAndExecuteSql();
      DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.RESERVATION);
      ArrayList<TicketTO> tktListTOList= new ArrayList<>();
      TicketTO ticket = new TicketTO();
      ticket.setTktItem(new BigInteger("1"));
      ticket.setTktNID("12000507111600050");
      ticket.setProdCode("1");
      ticket.setExtrnlPrcd("T");
      tktListTOList.add(ticket);
      WDWExternalPriceRules.validateDeltaProducts(dtiTxn,tktListTOList);
   }*/
   
   /**
    * Test validate delta products 1.
    * @throws DTIException 
    *
    * @throws Exception the exception
    */
   //@Test
  /* public void testValidateDeltaProducts1() throws Exception {
      DTIMockUtil.processMockprepareAndExecuteSql();
      DTITransactionTO dtiTxn=new DTITransactionTO(TransactionType.RESERVATION);
      ArrayList<TicketTO> tktListTOList= new ArrayList<>();
      TicketTO ticket = new TicketTO();
      ticket.setTktItem(new BigInteger("1"));
      ticket.setTktNID("12000507111600050");
      ticket.setProdCode("1");
      ticket.setExtrnlPrcd("T");
      GregorianCalendar date = new GregorianCalendar(2017,12,27);
      GregorianCalendar date1 = new GregorianCalendar(2017,12,29);
      ticket.setTktValidityValidStart(date);
      ticket.setTktValidityValidEnd(date1);
      tktListTOList.add(ticket);
      WDWExternalPriceRules.validateDeltaProducts(dtiTxn,tktListTOList);
   }*/
   
   @Test
   public void testValidateDeltaProducts() throws DTIException{
      DTIMockUtil.processMockprepareAndExecuteSql();
      DTITransactionTO dtiTxn =new DTITransactionTO(TransactionType.RESERVATION);
      ArrayList<TicketTO> tktListTO=new ArrayList<>();
      DBProductTO dBProductTO=new DBProductTO();
      TicketTO ticketTO=new TicketTO();
      ArrayList<DBProductTO> dbProdListIn=new ArrayList<>();
      dBProductTO.setPdtCode("fGh92");
      dBProductTO.setDayCount(4);
      ticketTO.setProdCode("fGh92");
      ticketTO.setDayCount(4);
      ticketTO.setExtrnlPrcd("T");
      tktListTO.add(ticketTO);
      dbProdListIn.add(dBProductTO);
      dtiTxn.setDbProdList(dbProdListIn);
      WDWExternalPriceRules.validateDeltaProducts(dtiTxn,tktListTO);
   }
}
