package pvt.disney.dti.gateway.dao;

import org.junit.Assert;
import org.junit.Test;

import pvt.disney.dti.gateway.constants.DTIException;
import pvt.disney.dti.gateway.test.util.DTIMockUtil;

/**
 * Test Case for PropertyKey
 * 
 * @author GANDV005
 *
 */
public class TestPropertyKey extends CommonTestDao {

   private String application;
   private String environment;
   private String section;
   Integer tpoId =0;
   @Test
   public void testGetProperties() {
      try {
         // Get Exception for passing application as null
         PropertyKey.getProperties(application, tpoId,environment, section);
      } catch (DTIException e) {
         // TODO Add Exception
      }
      application = "DTIGateway";
      try {
         // Get Exception for passing environment as null
         PropertyKey.getProperties(application,tpoId, environment, section);
      } catch (DTIException e) {
         // TODO Add Exception
      }
      environment = "Test";
      try {
         // Get Exception for section application as null
         PropertyKey.getProperties(application, tpoId,environment, section);
      } catch (DTIException e) {
         // TODO Add Exception
      }
      section = "None";
      try {
         // Get Exception as DB is not mocked yet
         PropertyKey.getProperties(application,tpoId, environment, section);
      } catch (DTIException e) {
         // TODO Add Exception
      }
      try {
         // Mock the DB
         DTIMockUtil.mockResultProcessor("pvt.disney.dti.gateway.dao.result.PropertyResult");
         Assert.assertNotNull(PropertyKey.getProperties(application,tpoId, environment, section).get(0));
      } catch (DTIException e) {
         // TODO Add Exception
      }
   }

}