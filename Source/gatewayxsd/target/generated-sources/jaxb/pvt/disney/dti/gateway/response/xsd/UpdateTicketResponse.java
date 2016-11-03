//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.03 at 09:34:20 AM CDT 
//


package pvt.disney.dti.gateway.response.xsd;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="Ticket" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="TktItem" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="TktError" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="TktErrorCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                             &lt;element name="TktErrorType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="TktErrorClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="TktErrorText" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
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
    "ticket"
})
@XmlRootElement(name = "UpdateTicketResponse")
public class UpdateTicketResponse {

    @XmlElement(name = "Ticket")
    protected List<UpdateTicketResponse.Ticket> ticket;

    /**
     * Gets the value of the ticket property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ticket property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTicket().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UpdateTicketResponse.Ticket }
     * 
     * 
     */
    public List<UpdateTicketResponse.Ticket> getTicket() {
        if (ticket == null) {
            ticket = new ArrayList<UpdateTicketResponse.Ticket>();
        }
        return this.ticket;
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
     *         &lt;element name="TktItem" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="TktError" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="TktErrorCode" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *                   &lt;element name="TktErrorType" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="TktErrorClass" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="TktErrorText" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "tktItem",
        "tktError"
    })
    public static class Ticket {

        @XmlElement(name = "TktItem", required = true)
        protected String tktItem;
        @XmlElement(name = "TktError")
        protected UpdateTicketResponse.Ticket.TktError tktError;

        /**
         * Gets the value of the tktItem property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTktItem() {
            return tktItem;
        }

        /**
         * Sets the value of the tktItem property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTktItem(String value) {
            this.tktItem = value;
        }

        /**
         * Gets the value of the tktError property.
         * 
         * @return
         *     possible object is
         *     {@link UpdateTicketResponse.Ticket.TktError }
         *     
         */
        public UpdateTicketResponse.Ticket.TktError getTktError() {
            return tktError;
        }

        /**
         * Sets the value of the tktError property.
         * 
         * @param value
         *     allowed object is
         *     {@link UpdateTicketResponse.Ticket.TktError }
         *     
         */
        public void setTktError(UpdateTicketResponse.Ticket.TktError value) {
            this.tktError = value;
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
        @XmlType(name = "", propOrder = {
            "tktErrorCode",
            "tktErrorType",
            "tktErrorClass",
            "tktErrorText"
        })
        public static class TktError {

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

    }

}
