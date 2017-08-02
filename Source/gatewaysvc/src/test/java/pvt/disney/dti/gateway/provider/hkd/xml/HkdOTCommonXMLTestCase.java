package pvt.disney.dti.gateway.provider.hkd.xml;

import java.util.ArrayList;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTFieldTO;

/**
 * @author MISHP012 JUnit HkdOTCommonXML
 *
 */
public class HkdOTCommonXMLTestCase {

	/**
	 * JUnit for addOTDemoAsFieldType
	 */
	@Test
	public void testAddOTDemoAsFieldType() {
		Document document = DocumentHelper.createDocument();		
		Integer fieldIndexIn = 5;
		String fieldValueIn = "test";
		HkdOTFieldTO hkdOTFieldTO = new HkdOTFieldTO(fieldIndexIn, fieldValueIn);
		ArrayList<HkdOTFieldTO> aFieldList = new ArrayList<>();
		aFieldList.add(hkdOTFieldTO);
		Element demoDataStanza = document.addElement("");
		HkdOTCommonXML.addOTDemoAsFieldType(demoDataStanza, aFieldList);
	}
	/**
	 * JUnit for addOTDemoAsFieldId
	 */
	@Test
	public void testAddOTDemoAsFieldId() {
		Document document = DocumentHelper.createDocument();
		Integer fieldIndexIn = 5;
		String fieldValueIn = "test";
		HkdOTFieldTO hkdOTFieldTO = new HkdOTFieldTO(fieldIndexIn, fieldValueIn);
		ArrayList<HkdOTFieldTO> aFieldList = new ArrayList<>();
		aFieldList.add(hkdOTFieldTO);
		Element demoDataStanza = document.addElement("");
		HkdOTCommonXML.addOTDemoAsFieldId(demoDataStanza, aFieldList);
	}
}
