package pvt.disney.dti.gateway.dao;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.data.common.BarricadeTO;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * The Class TestBarricadeKey.
 * 
 * @author MISHP012
 */
public class TestBarricadeKey {

   /**
    * Test get barricade lookup.
    */
   @Test
   public void testgetBarricadeLookup() {
      Integer cosgrpId = 1;
      String ownerId = "DLR";
      try {
         DTIMockUtil.processMockprepareAndExecuteSql();
         List<BarricadeTO> barricadeTOs = BarricadeKey.getBarricadeLookup(cosgrpId, ownerId);
         Assert.assertNotNull(barricadeTOs);
      } catch (DTIException dtie) {
         Assert.fail("Unexpected Massage ." + dtie.getLogMessage()); 
      }
   }
}
