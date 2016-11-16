package pvt.disney.dti.gateway.provider.hkd.xml;

import java.util.ArrayList;

import org.dom4j.Element;

import pvt.disney.dti.gateway.provider.hkd.data.common.HkdOTFieldTO;

/**
 * 
 * @author lewit019
 * @since 2.16.3
 * 
 */
public class HkdOTCommonXML {

	/**
	 * 
	 * @param demoDataStanza
	 * @param aFieldList
	 */
	public static void addOTDemoAsFieldType(Element demoDataStanza,
			ArrayList<HkdOTFieldTO> aFieldList) {
		for /* each */(HkdOTFieldTO aField : /* in */aFieldList) {

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
			ArrayList<HkdOTFieldTO> aFieldList) {
		for /* each */(HkdOTFieldTO aField : /* in */aFieldList) {

			Element fieldStanza = demoDataStanza.addElement("Field");

			fieldStanza.addElement("FieldId").addText(
					aField.getFieldIndex().toString());
			fieldStanza.addElement("FieldValue").addText(
					aField.getFieldValue().toUpperCase());
		}
	}

}
