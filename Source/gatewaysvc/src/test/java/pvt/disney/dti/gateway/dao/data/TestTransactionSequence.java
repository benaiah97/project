package pvt.disney.dti.gateway.dao.data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTransactionSequence {
	TransactionSequences seq=null;
	 @Before
	  public void setUp(){
		 seq=new TransactionSequences();
		 seq.setTransIdITP(new Integer(1));
		 seq.setTransIdOTP(new Integer(12));
		 seq.setTransIdOTS(new Integer(123));
	 }
	 @After
	    public void tearDown() {
		 seq = null;
	    }
	 @Test
	 public void testTransactionSequences(){
		 assertNotNull(seq);
		 assertEquals(seq.getTransIdITP(),new Integer(1));
		 assertEquals(seq.getTransIdOTP(),new Integer(12));
		 assertEquals(seq.getTransIdOTS(),new Integer(123));
	 }
}
