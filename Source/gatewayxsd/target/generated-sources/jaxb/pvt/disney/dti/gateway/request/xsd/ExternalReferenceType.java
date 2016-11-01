//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.01 at 03:55:33 PM CDT 
//


package pvt.disney.dti.gateway.request.xsd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExternalReferenceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExternalReferenceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExternalReferenceType">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="XBANDID"/>
 *               &lt;enumeration value="GXP_LINK_ID"/>
 *               &lt;enumeration value="XBAND_EXTERNAL_NUMBER"/>
 *               &lt;enumeration value="SWID"/>
 *               &lt;enumeration value="GUID"/>
 *               &lt;enumeration value="XBMS_LINK_ID"/>
 *               &lt;enumeration value="XPASSID"/>
 *               &lt;enumeration value="TRANSACTIONAL_GUEST_ID"/>
 *               &lt;enumeration value="ADMISSION_LINK_ID"/>
 *               &lt;enumeration value="PAYMENT_LINK_ID"/>
 *               &lt;enumeration value="MEDIA_LINK_ID"/>
 *               &lt;enumeration value="XID"/>
 *               &lt;enumeration value="DME_LINK_ID"/>
 *               &lt;enumeration value="SECURE_ID"/>
 *               &lt;enumeration value="TXN_GUID"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ExternalReferenceValue">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExternalReferenceType", propOrder = {
    "externalReferenceType",
    "externalReferenceValue"
})
public class ExternalReferenceType {

    @XmlElement(name = "ExternalReferenceType", required = true)
    protected String externalReferenceType;
    @XmlElement(name = "ExternalReferenceValue", required = true)
    protected String externalReferenceValue;

    /**
     * Gets the value of the externalReferenceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalReferenceType() {
        return externalReferenceType;
    }

    /**
     * Sets the value of the externalReferenceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalReferenceType(String value) {
        this.externalReferenceType = value;
    }

    /**
     * Gets the value of the externalReferenceValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalReferenceValue() {
        return externalReferenceValue;
    }

    /**
     * Sets the value of the externalReferenceValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalReferenceValue(String value) {
        this.externalReferenceValue = value;
    }

}
