//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.03 at 09:34:19 AM CDT 
//


package pvt.disney.dti.gateway.request.xsd;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="CmdItem" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="CmdProcRule" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CmdTimeout" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="CmdDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="CmdTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CmdInvoice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CmdDevice" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CmdOperator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CmdActor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CmdSecurity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CmdNote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CmdMarket" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CmdAttribute" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="AttribName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="AttribValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "cmdItem",
    "cmdProcRule",
    "cmdTimeout",
    "cmdDate",
    "cmdTime",
    "cmdInvoice",
    "cmdDevice",
    "cmdOperator",
    "cmdActor",
    "cmdSecurity",
    "cmdNote",
    "cmdMarket",
    "cmdAttribute"
})
@XmlRootElement(name = "CommandHeader")
public class CommandHeader {

    @XmlElement(name = "CmdItem", required = true)
    protected BigInteger cmdItem;
    @XmlElement(name = "CmdProcRule")
    protected List<CommandHeader.CmdProcRule> cmdProcRule;
    @XmlElement(name = "CmdTimeout")
    protected BigInteger cmdTimeout;
    @XmlElement(name = "CmdDate", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar cmdDate;
    @XmlElement(name = "CmdTime", required = true)
    protected String cmdTime;
    @XmlElement(name = "CmdInvoice", required = true)
    protected String cmdInvoice;
    @XmlElement(name = "CmdDevice", required = true)
    protected String cmdDevice;
    @XmlElement(name = "CmdOperator", required = true)
    protected String cmdOperator;
    @XmlElement(name = "CmdActor")
    protected String cmdActor;
    @XmlElement(name = "CmdSecurity")
    protected String cmdSecurity;
    @XmlElement(name = "CmdNote")
    protected String cmdNote;
    @XmlElement(name = "CmdMarket")
    protected String cmdMarket;
    @XmlElement(name = "CmdAttribute")
    protected List<CommandHeader.CmdAttribute> cmdAttribute;

    /**
     * Gets the value of the cmdItem property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCmdItem() {
        return cmdItem;
    }

    /**
     * Sets the value of the cmdItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCmdItem(BigInteger value) {
        this.cmdItem = value;
    }

    /**
     * Gets the value of the cmdProcRule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cmdProcRule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCmdProcRule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CommandHeader.CmdProcRule }
     * 
     * 
     */
    public List<CommandHeader.CmdProcRule> getCmdProcRule() {
        if (cmdProcRule == null) {
            cmdProcRule = new ArrayList<CommandHeader.CmdProcRule>();
        }
        return this.cmdProcRule;
    }

    /**
     * Gets the value of the cmdTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCmdTimeout() {
        return cmdTimeout;
    }

    /**
     * Sets the value of the cmdTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCmdTimeout(BigInteger value) {
        this.cmdTimeout = value;
    }

    /**
     * Gets the value of the cmdDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCmdDate() {
        return cmdDate;
    }

    /**
     * Sets the value of the cmdDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCmdDate(XMLGregorianCalendar value) {
        this.cmdDate = value;
    }

    /**
     * Gets the value of the cmdTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmdTime() {
        return cmdTime;
    }

    /**
     * Sets the value of the cmdTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmdTime(String value) {
        this.cmdTime = value;
    }

    /**
     * Gets the value of the cmdInvoice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmdInvoice() {
        return cmdInvoice;
    }

    /**
     * Sets the value of the cmdInvoice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmdInvoice(String value) {
        this.cmdInvoice = value;
    }

    /**
     * Gets the value of the cmdDevice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmdDevice() {
        return cmdDevice;
    }

    /**
     * Sets the value of the cmdDevice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmdDevice(String value) {
        this.cmdDevice = value;
    }

    /**
     * Gets the value of the cmdOperator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmdOperator() {
        return cmdOperator;
    }

    /**
     * Sets the value of the cmdOperator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmdOperator(String value) {
        this.cmdOperator = value;
    }

    /**
     * Gets the value of the cmdActor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmdActor() {
        return cmdActor;
    }

    /**
     * Sets the value of the cmdActor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmdActor(String value) {
        this.cmdActor = value;
    }

    /**
     * Gets the value of the cmdSecurity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmdSecurity() {
        return cmdSecurity;
    }

    /**
     * Sets the value of the cmdSecurity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmdSecurity(String value) {
        this.cmdSecurity = value;
    }

    /**
     * Gets the value of the cmdNote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmdNote() {
        return cmdNote;
    }

    /**
     * Sets the value of the cmdNote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmdNote(String value) {
        this.cmdNote = value;
    }

    /**
     * Gets the value of the cmdMarket property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmdMarket() {
        return cmdMarket;
    }

    /**
     * Sets the value of the cmdMarket property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmdMarket(String value) {
        this.cmdMarket = value;
    }

    /**
     * Gets the value of the cmdAttribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cmdAttribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCmdAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CommandHeader.CmdAttribute }
     * 
     * 
     */
    public List<CommandHeader.CmdAttribute> getCmdAttribute() {
        if (cmdAttribute == null) {
            cmdAttribute = new ArrayList<CommandHeader.CmdAttribute>();
        }
        return this.cmdAttribute;
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
     *         &lt;element name="AttribName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="AttribValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "attribName",
        "attribValue"
    })
    public static class CmdAttribute {

        @XmlElement(name = "AttribName", required = true)
        protected String attribName;
        @XmlElement(name = "AttribValue", required = true)
        protected String attribValue;

        /**
         * Gets the value of the attribName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAttribName() {
            return attribName;
        }

        /**
         * Sets the value of the attribName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAttribName(String value) {
            this.attribName = value;
        }

        /**
         * Gets the value of the attribValue property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAttribValue() {
            return attribValue;
        }

        /**
         * Sets the value of the attribValue property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAttribValue(String value) {
            this.attribValue = value;
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
     *         &lt;element name="Rule" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "rule",
        "value"
    })
    public static class CmdProcRule {

        @XmlElement(name = "Rule", required = true)
        protected String rule;
        @XmlElement(name = "Value", required = true)
        protected String value;

        /**
         * Gets the value of the rule property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRule() {
            return rule;
        }

        /**
         * Sets the value of the rule property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRule(String value) {
            this.rule = value;
        }

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

    }

}
