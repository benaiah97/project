package pvt.disney.dti.gateway.data.common;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import pvt.disney.dti.gateway.data.common.AttributeTO.CmdAttrCodeType;

/**
 * @author AGARS017 JUnit for AttributeTO
 */
public class AttributeTOTestCase {
	
	AttributeTO attributeTO = new AttributeTO();
	
	/**
	 * TestCase for setUp
	 */
	@Before
	public void setUP(){
		attributeTO.setCmdAttrCode(CmdAttrCodeType.USER);
		attributeTO.setAttrValue("attrValue");
		attributeTO.setCmdCode("Cmdcode");
		attributeTO.setActor("Actor");
		attributeTO.setActiveInd("ActiveInd");
	}
	
	/**
	 * TestCase for AttributeTOGetterMethods
	 */
	@Test
	public void testAttributeTO(){
		assertEquals(attributeTO.getCmdAttrCode(), CmdAttrCodeType.USER);
		assertEquals(attributeTO.getAttrValue(), "attrValue");
		assertEquals(attributeTO.getCmdCode(), "Cmdcode");
		assertEquals(attributeTO.getActor(), "Actor");
		assertEquals(attributeTO.getActiveInd(), "ActiveInd");
	}
	
	/**
	 * TestCase for toString
	 */
	@Test
	public void testToString() {
		attributeTO.toString();
	}

}
