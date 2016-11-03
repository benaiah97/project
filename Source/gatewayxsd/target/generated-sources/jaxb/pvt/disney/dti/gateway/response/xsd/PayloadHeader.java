//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.03 at 09:34:20 AM CDT 
//


package pvt.disney.dti.gateway.response.xsd;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PayloadID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TSPayloadID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Target" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ProcMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Comm">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Protocol" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Method" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="TransmitDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="TransmitTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TktBroker" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CommandCount" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="PayloadNote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PayloadError" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="HdrErrorCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="HdrErrorType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="HdrErrorClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="HdrErrorText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
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
@XmlType(name = "", propOrder = {
    "payloadID",
    "tsPayloadID",
    "target",
    "version",
    "priority",
    "procMethod",
    "comm",
    "transmitDate",
    "transmitTime",
    "tktBroker",
    "commandCount",
    "payloadNote",
    "payloadError"
})
@XmlRootElement(name = "PayloadHeader")
public class PayloadHeader {

    @XmlElement(name = "PayloadID", required = true)
    protected String payloadID;
    @XmlElement(name = "TSPayloadID", required = true)
    protected String tsPayloadID;
    @XmlElement(name = "Target", required = true)
    protected String target;
    @XmlElement(name = "Version", required = true)
    protected String version;
    @XmlElement(name = "Priority")
    protected String priority;
    @XmlElement(name = "ProcMethod")
    protected String procMethod;
    @XmlElement(name = "Comm", required = true)
    protected PayloadHeader.Comm comm;
    @XmlElement(name = "TransmitDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar transmitDate;
    @XmlElement(name = "TransmitTime", required = true)
    protected String transmitTime;
    @XmlElement(name = "TktBroker", required = true)
    protected String tktBroker;
    @XmlElement(name = "CommandCount", required = true)
    protected BigInteger commandCount;
    @XmlElement(name = "PayloadNote")
    protected String payloadNote;
    @XmlElement(name = "PayloadError")
    protected PayloadHeader.PayloadError payloadError;

    /**
     * Gets the value of the payloadID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayloadID() {
        return payloadID;
    }

    /**
     * Sets the value of the payloadID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayloadID(String value) {
        this.payloadID = value;
    }

    /**
     * Gets the value of the tsPayloadID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTSPayloadID() {
        return tsPayloadID;
    }

    /**
     * Sets the value of the tsPayloadID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTSPayloadID(String value) {
        this.tsPayloadID = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTarget(String value) {
        this.target = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Gets the value of the procMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcMethod() {
        return procMethod;
    }

    /**
     * Sets the value of the procMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcMethod(String value) {
        this.procMethod = value;
    }

    /**
     * Gets the value of the comm property.
     * 
     * @return
     *     possible object is
     *     {@link PayloadHeader.Comm }
     *     
     */
    public PayloadHeader.Comm getComm() {
        return comm;
    }

    /**
     * Sets the value of the comm property.
     * 
     * @param value
     *     allowed object is
     *     {@link PayloadHeader.Comm }
     *     
     */
    public void setComm(PayloadHeader.Comm value) {
        this.comm = value;
    }

    /**
     * Gets the value of the transmitDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTransmitDate() {
        return transmitDate;
    }

    /**
     * Sets the value of the transmitDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTransmitDate(XMLGregorianCalendar value) {
        this.transmitDate = value;
    }

    /**
     * Gets the value of the transmitTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransmitTime() {
        return transmitTime;
    }

    /**
     * Sets the value of the transmitTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransmitTime(String value) {
        this.transmitTime = value;
    }

    /**
     * Gets the value of the tktBroker property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTktBroker() {
        return tktBroker;
    }

    /**
     * Sets the value of the tktBroker property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTktBroker(String value) {
        this.tktBroker = value;
    }

    /**
     * Gets the value of the commandCount property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCommandCount() {
        return commandCount;
    }

    /**
     * Sets the value of the commandCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCommandCount(BigInteger value) {
        this.commandCount = value;
    }

    /**
     * Gets the value of the payloadNote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayloadNote() {
        return payloadNote;
    }

    /**
     * Sets the value of the payloadNote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayloadNote(String value) {
        this.payloadNote = value;
    }

    /**
     * Gets the value of the payloadError property.
     * 
     * @return
     *     possible object is
     *     {@link PayloadHeader.PayloadError }
     *     
     */
    public PayloadHeader.PayloadError getPayloadError() {
        return payloadError;
    }

    /**
     * Sets the value of the payloadError property.
     * 
     * @param value
     *     allowed object is
     *     {@link PayloadHeader.PayloadError }
     *     
     */
    public void setPayloadError(PayloadHeader.PayloadError value) {
        this.payloadError = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Protocol" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Method" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "protocol",
        "method"
    })
    public static class Comm {

        @XmlElement(name = "Protocol", required = true)
        protected String protocol;
        @XmlElement(name = "Method", required = true)
        protected String method;

        /**
         * Gets the value of the protocol property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProtocol() {
            return protocol;
        }

        /**
         * Sets the value of the protocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProtocol(String value) {
            this.protocol = value;
        }

        /**
         * Gets the value of the method property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMethod() {
            return method;
        }

        /**
         * Sets the value of the method property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMethod(String value) {
            this.method = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="HdrErrorCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="HdrErrorType" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="HdrErrorClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="HdrErrorText" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "hdrErrorCode",
        "hdrErrorType",
        "hdrErrorClass",
        "hdrErrorText"
    })
    public static class PayloadError {

        @XmlElement(name = "HdrErrorCode", required = true)
        protected BigInteger hdrErrorCode;
        @XmlElement(name = "HdrErrorType", required = true)
        protected String hdrErrorType;
        @XmlElement(name = "HdrErrorClass", required = true)
        protected String hdrErrorClass;
        @XmlElement(name = "HdrErrorText", required = true)
        protected String hdrErrorText;

        /**
         * Gets the value of the hdrErrorCode property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getHdrErrorCode() {
            return hdrErrorCode;
        }

        /**
         * Sets the value of the hdrErrorCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setHdrErrorCode(BigInteger value) {
            this.hdrErrorCode = value;
        }

        /**
         * Gets the value of the hdrErrorType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHdrErrorType() {
            return hdrErrorType;
        }

        /**
         * Sets the value of the hdrErrorType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHdrErrorType(String value) {
            this.hdrErrorType = value;
        }

        /**
         * Gets the value of the hdrErrorClass property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHdrErrorClass() {
            return hdrErrorClass;
        }

        /**
         * Sets the value of the hdrErrorClass property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHdrErrorClass(String value) {
            this.hdrErrorClass = value;
        }

        /**
         * Gets the value of the hdrErrorText property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getHdrErrorText() {
            return hdrErrorText;
        }

        /**
         * Sets the value of the hdrErrorText property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setHdrErrorText(String value) {
            this.hdrErrorText = value;
        }

    }

}
