//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.09 at 02:31:37 PM CST 
//


package pvt.disney.dti.gateway.response.xsd;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TktError complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TktError">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TktErrorCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="TktErrorType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TktErrorClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TktErrorText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TktError", propOrder = {
    "tktErrorCode",
    "tktErrorType",
    "tktErrorClass",
    "tktErrorText"
})
public class TktError {

    @XmlElement(name = "TktErrorCode", required = true)
    protected BigInteger tktErrorCode;
    @XmlElement(name = "TktErrorType", required = true)
    protected String tktErrorType;
    @XmlElement(name = "TktErrorClass", required = true)
    protected String tktErrorClass;
    @XmlElement(name = "TktErrorText", required = true)
    protected String tktErrorText;

    /**
     * Gets the value of the tktErrorCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTktErrorCode() {
        return tktErrorCode;
    }

    /**
     * Sets the value of the tktErrorCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTktErrorCode(BigInteger value) {
        this.tktErrorCode = value;
    }

    /**
     * Gets the value of the tktErrorType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTktErrorType() {
        return tktErrorType;
    }

    /**
     * Sets the value of the tktErrorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTktErrorType(String value) {
        this.tktErrorType = value;
    }

    /**
     * Gets the value of the tktErrorClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTktErrorClass() {
        return tktErrorClass;
    }

    /**
     * Sets the value of the tktErrorClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTktErrorClass(String value) {
        this.tktErrorClass = value;
    }

    /**
     * Gets the value of the tktErrorText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTktErrorText() {
        return tktErrorText;
    }

    /**
     * Sets the value of the tktErrorText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTktErrorText(String value) {
        this.tktErrorText = value;
    }

}
