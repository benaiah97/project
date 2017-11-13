package pvt.disney.dti.gateway.rules;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTICalmException;
import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.DTITransactionTO;
import pvt.disney.dti.gateway.data.DTITransactionTO.ProviderType;
import pvt.disney.dti.gateway.data.DTITransactionTO.TransactionType;
import pvt.disney.dti.gateway.data.QueryTicketRequestTO;
import pvt.disney.dti.gateway.data.common.EntityTO;
import pvt.disney.dti.gateway.data.common.TicketTO.TicketIdType;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for Calm Rules
 * 
 * @author MISHP012
 * 
 */
public class CalmRulesTestCase extends CommonBusinessTest {
   private static CalmRules calmRules = null;
   /* CALM.QueryTicket.ReplyMACs property value */
   private static String tsMacWadm = "WDWADMIN";
   private static String tsMacWdp = "WDPRONA";

   /**
    * Unit test for CheckContingencyActionsLogicModule
    * 
    */
   @Test
   public void testCheckContingencyActionsLogicModule() throws DTIException, DTICalmException {

      calmRules = CalmRules.getInstance(setConfigProperty());
      super.setMockProperty();

      DTITransactionTO dtitxn = new DTITransactionTO(TransactionType.QUERYTICKET);
      EntityTO entityTO = new EntityTO();
      entityTO.setMacEntityId(0);
      entityTO.setEntityId(1);
      getDTITransactionTO(dtitxn);
      dtitxn.setEntityTO(entityTO);
      
      
      /*Barricade is not raised*/
      dtitxn.getRequest().getPayloadHeader().getTktSeller().setTsMac(tsMacWadm);
      dtitxn.setProvider(ProviderType.WDWNEXUS);

      try {
         DTIMockUtil.processMockprepareAndExecuteSql();
         calmRules.checkContingencyActionsLogicModule(dtitxn);
      } catch (DTIException dtie) {
         assertEquals("Unexpected exception", dtie.getLogMessage());
      }
      /*
       * Barricade tsmac and tsloc are null expected exception WDW Request
       * attempted when Barricade is raised.
       */
      DTIMockUtil.mockBarricade();

      dtitxn.getRequest().getPayloadHeader().getTktSeller().setTsMac(tsMacWadm);
      dtitxn.setProvider(ProviderType.WDWNEXUS);

      try {
         DTIMockUtil.processMockprepareAndExecuteSql();
         calmRules.checkContingencyActionsLogicModule(dtitxn);
      } catch (DTIException dtie) {
         assertEquals("WDW Request attempted when Barricade is raised.", dtie.getLogMessage());
      }
      /*
       * Barricade tsloc is null expected exception WDW Request attempted when
       * Barricade is raised.
       */
      DTIMockUtil.mockBarricade1();
      try {
         calmRules.checkContingencyActionsLogicModule(dtitxn);
      } catch (DTIException dtie) {
         assertEquals("WDW Request attempted when Barricade is raised.", dtie.getLogMessage());
      }
      /*
       * Barricade tsmac and tsloc are not null expected exception WDW Request
       * attempted when Barricade is raised.
       */
      DTIMockUtil.mockBarricade2();

      try {
         calmRules.checkContingencyActionsLogicModule(dtitxn);
      } catch (DTIException dtie) {
         assertEquals("WDW Request attempted when Barricade is raised.", dtie.getLogMessage());
      }
      /* Expected exception is DLR Request attempted when Barricade is raised. */
      dtitxn.getRequest().getPayloadHeader().getTktSeller().setTsMac(tsMacWadm);
      dtitxn.setProvider(ProviderType.DLRGATEWAY);
      try {
         calmRules.checkContingencyActionsLogicModule(dtitxn);
      } catch (DTIException dtie) {
         assertEquals("DLR Request attempted when Barricade is raised.", dtie.getLogMessage());

      }
      
      
     /*mock getPropSetValue returns as true*/
      DTIMockUtil.mockProperty();

      /* JUnit for executeWDWDownRules 
       * Expected Exception is WDW Request attempted when WDWDown outage wall
       * property is present in the database (CALM).
       */
      dtitxn.getRequest().getPayloadHeader().getTktSeller().setTsMac(tsMacWadm);
      dtitxn.setProvider(ProviderType.WDWNEXUS);

      try {
         DTIMockUtil.processMockprepareAndExecuteSql();
         calmRules.checkContingencyActionsLogicModule(dtitxn);
      } catch (DTIException dtie) {
         assertEquals("WDW Request attempted when WDWDown outage wall property is present in the database (CALM).",
                  dtie.getLogMessage());
      }

      /*
       * JUnit for createAPQueryWDWTicketResp
       * Expected Exception is WDW Request attempted when WDWDown outage wall
       * property is present in the database (CALM).
       */

      dtitxn.getRequest().getPayloadHeader().getTktSeller().setTsMac(tsMacWdp);

      try {
         calmRules.checkContingencyActionsLogicModule(dtitxn);
      } catch (DTIException dtie) {
         assertEquals("WDW Request attempted when WDWDown outage wall property is present in the database (CALM).",
                  dtie.getLogMessage());
      } catch (DTICalmException dtic) {
         assertEquals(DTICalmException.class, dtic.getClass());
      }

      /* JUnit for executeDLRDownRules 
       * Expected Exception is DLR Request attempted when DLRDown outage wall
       * property is present (CALM).
       */

      dtitxn.getRequest().getPayloadHeader().getTktSeller().setTsMac(tsMacWadm);
      dtitxn.setProvider(ProviderType.DLRGATEWAY);
      try {
         calmRules.checkContingencyActionsLogicModule(dtitxn);
      } catch (DTIException dtie) {
         assertEquals("DLR Request attempted when DLRDown outage wall property is present in the database (CALM).",
                  dtie.getLogMessage());
      }
      /* JUnit for createAPQueryDLRTicketResp */
      dtitxn.getRequest().getPayloadHeader().getTktSeller().setTsMac(tsMacWdp);

      try {
         calmRules.checkContingencyActionsLogicModule(dtitxn);
      }
      catch (DTIException dtie) {
         assertEquals("DLR Request attempted when DLRDown outage wall property is present in the database (CALM).",
                  dtie.getLogMessage());
      }
      catch (DTICalmException dtic) {
         assertEquals(DTICalmException.class, dtic.getClass());
      }

      /* JUnit for executeHKDDownRules */
      dtitxn.setProvider(ProviderType.HKDNEXUS);
      try {
         calmRules.checkContingencyActionsLogicModule(dtitxn);
      } catch (DTICalmException dtic) {
         assertEquals(DTICalmException.class, dtic.getClass());
      }
      
      
      dtitxn.getRequest().getPayloadHeader().getTktSeller().setTsMac("hkd");
      try {
         calmRules.checkContingencyActionsLogicModule(dtitxn);
      } catch (DTIException dtie) {
         assertEquals("HKD Request attempted when HKDDown outage wall file is present (CALM).",
                  dtie.getLogMessage());
      }
   }

   /**
    * Getting dtiTransaction
    * 
    * @param dTITransactionTO
    */
   private final void getDTITransactionTO(DTITransactionTO dtitxn) {
      createCommonRequest(dtitxn, true, true, TEST_TARGET, TPI_CODE_WDW, false);
      QueryTicketRequestTO queryTicketRequestTO = new QueryTicketRequestTO();
      queryTicketRequestTO.setTktList(getTicketList(TicketIdType.TKTNID_ID));
      dtitxn.getRequest().setCommandBody(queryTicketRequestTO);
   }

   /*
    * Property Configuration for calm Rules
    * 
    * @see pvt.disney.dti.gateway.rules.CommonBusinessTest#setConfigProperty()
    */
   public Properties setConfigProperty() {
      props = new Properties();
      props.setProperty("CALM.HKDDownFileName", "src/test/resources/jdbc.properties");
      props.setProperty("CALM.QueryTicket.ReplyMACs", "XGREETER");
      props.setProperty("CALM.QueryTicket.ReplyMACs", "WDPRONA,WDPRONADLR,DLRMerch,WDWMerch");
      return props;
   }

}
