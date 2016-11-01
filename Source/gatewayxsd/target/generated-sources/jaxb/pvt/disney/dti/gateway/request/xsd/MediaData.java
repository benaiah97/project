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
 * <p>Java class for MediaData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MediaData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MediaId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="32"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MfrId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="VisualId">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="32"/>
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
@XmlType(name = "MediaData", propOrder = {
    "mediaId",
    "mfrId",
    "visualId"
})
public class MediaData {

    @XmlElement(name = "MediaId", required = true)
    protected String mediaId;
    @XmlElement(name = "MfrId", required = true)
    protected String mfrId;
    @XmlElement(name = "VisualId", required = true)
    protected String visualId;

    /**
     * Gets the value of the mediaId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMediaId() {
        return mediaId;
    }

    /**
     * Sets the value of the mediaId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMediaId(String value) {
        this.mediaId = value;
    }

    /**
     * Gets the value of the mfrId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMfrId() {
        return mfrId;
    }

    /**
     * Sets the value of the mfrId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMfrId(String value) {
        this.mfrId = value;
    }

    /**
     * Gets the value of the visualId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVisualId() {
        return visualId;
    }

    /**
     * Sets the value of the visualId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVisualId(String value) {
        this.visualId = value;
    }

}
