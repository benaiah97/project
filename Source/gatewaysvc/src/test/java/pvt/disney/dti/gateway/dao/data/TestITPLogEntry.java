package pvt.disney.dti.gateway.dao.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestITPLogEntry {
	ITPLogEntry itplog=null;
	 @Before
	  public void setUp(){
		 itplog=new ITPLogEntry();
		 itplog.setCommMethod("Network");
		 itplog.setCommProtocol("IP");
		 itplog.setInboundTsId(new Long(1));
		 itplog.setPayloadId("99999999999999999999");
		 itplog.setTargSys("PROD-WDW");
		 itplog.setXmlVersion("1.0");
		 itplog.setTransId(new Integer(1));
	 }
	 @After
	    public void tearDown() {
		 itplog = null;
	    }
	 @Test
	 public void testITPLogEntry(){
		 assertNotNull(itplog);
		 assertEquals(itplog.getCommMethod(),"Network");
		 assertEquals(itplog.getCommProtocol(),"IP");
		 assertEquals(itplog.getInboundTsId(),new Long(1));
		 assertEquals(itplog.getPayloadId(),"99999999999999999999");
		 assertEquals(itplog.getTargSys(),"PROD-WDW");
		 assertEquals(itplog.getXmlVersion(),"1.0");
		 assertEquals(itplog.getTransId(),new Integer(1));
	 }

}
