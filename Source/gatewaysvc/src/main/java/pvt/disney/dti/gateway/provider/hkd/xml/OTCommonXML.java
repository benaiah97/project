package pvt.disney.dti.gateway.provider.hkd.xml;

import java.util.ArrayList;

import org.dom4j.Element;

import pvt.disney.dti.gateway.provider.wdw.data.common.OTFieldTO;

/**
 * 
 * @author lewit019
 * 
 */
public class OTCommonXML {

	/**
	 * 
	 * @param demoDataStanza
	 * @param aFieldList
	 */
	public static void addOTDemoAsFieldType(Element demoDataStanza,
			ArrayList<OTFieldTO> aFieldList) {
		for /* each */(OTFieldTO aField : /* in */aFieldList) {

			Element fieldStanza = demoDataStanza.addElement("Field");

			fieldStanza.addElement("FieldType").addText(
					aField.getFieldIndex().toString());
			fieldStanza.addElement("FieldValue").addText(
					aField.getFieldValue().toUpperCase());
		}
	}

	/**
	 * 
	 * @param demoDataStanza
	 * @param aFieldList
	 */
	public static void addOTDemoAsFieldId(Element demoDataStanza,
			ArrayList<OTFieldTO> aFieldList) {
		for /* each */(OTFieldTO aField : /* in */aFieldList) {

			Element fieldStanza = demoDataStanza.addElement("Field");

			fieldStanza.addElement("FieldId").addText(
					aField.getFieldIndex().toString());
			fieldStanza.addElement("FieldValue").addText(
					aField.getFieldValue().toUpperCase());
		}
	}

}
