package pvt.disney.dti.gateway.dao.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



//@PowerMockIgnore({ "javax.management.*" })
//@RunWith(PowerMockRunner.class)
public class TestdbQueryBuilder {
	
	 DBQueryBuilder dbBuilder=null;
	 String sql=null;
	 Object[] strarray={"1"};
	

	  @Before
	  public void setUp(){
		   dbBuilder=new DBQueryBuilder();
		   sql="abc";
		   
	  }
	 @After 
	  public void tearDown() {
		  dbBuilder = null;
		  sql=null;
		  strarray=null;
	    }

	  
	
	@Test
	public void testgetQuery() throws Exception {
		assertNotNull(sql);
		assertNotNull(strarray);
	
		 String result=dbBuilder.getQuery(sql, strarray);
		 assertEquals(result,"abc");
	
		
	}

}
