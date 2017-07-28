package pvt.disney.dti.gateway.dao.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDBTicketAttributes {
	DBTicketAttributes dbticketAttributes=null;
	  @Before
	  public void setUp(){
		  dbticketAttributes=new DBTicketAttributes();
		  dbticketAttributes.setAgeGroup("A");
		  dbticketAttributes.setMediaType("S");
		  dbticketAttributes.setPassClass("PH");
		  dbticketAttributes.setPassRenew("PASSRENEW");
		  dbticketAttributes.setPassType("SEASONAL");
		  dbticketAttributes.setResident("Resident");
		  dbticketAttributes.setTktCode("1");
		 
	  }
	  @After
	    public void tearDown() {
		  dbticketAttributes = null;
	    }
	  @Test
	  public void testDBTicketAttributes(){
		  assertNotNull(dbticketAttributes);
		  assertEquals(dbticketAttributes.getAgeGroup(),"A");
		  assertEquals(dbticketAttributes.getMediaType(),"S");
		  assertEquals(dbticketAttributes.getPassClass(),"PH");
		  assertEquals(dbticketAttributes.getPassRenew(),"PASSRENEW");
		  assertEquals(dbticketAttributes.getPassType(),"SEASONAL");
		  assertEquals(dbticketAttributes.getResident(),"Resident");
		  assertEquals(dbticketAttributes.getTktCode(),"1");
		  
	  }
	  
}
