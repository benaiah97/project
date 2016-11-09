//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.09 at 02:31:35 PM CST 
//


package pvt.disney.dti.gateway.request.xsd;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Ticket complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Ticket">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TktItem" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="TktID">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="Mag" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="Barcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="TktDSSN">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="TktDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                             &lt;element name="TktSite" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="TktStation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="TktNbr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="TktNID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="External" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/choice>
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
@XmlType(name = "Ticket", propOrder = {
    "tktItem",
    "tktID"
})
public class Ticket {

    @XmlElement(name = "TktItem", required = true)
    protected BigInteger tktItem;
    @XmlElement(name = "TktID", required = true)
    protected Ticket.TktID tktID;

    /**
     * Gets the value of the tktItem property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTktItem() {
        return tktItem;
    }

    /**
     * Sets the value of the tktItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTktItem(BigInteger value) {
        this.tktItem = value;
    }

    /**
     * Gets the value of the tktID property.
     * 
     * @return
     *     possible object is
     *     {@link Ticket.TktID }
     *     
     */
    public Ticket.TktID getTktID() {
        return tktID;
    }

    /**
     * Sets the value of the tktID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ticket.TktID }
     *     
     */
    public void setTktID(Ticket.TktID value) {
        this.tktID = value;
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
     *       &lt;choice>
     *         &lt;element name="Mag" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="Barcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="TktDSSN">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="TktDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *                   &lt;element name="TktSite" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="TktStation" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="TktNbr" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="TktNID" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="External" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "mag",
        "barcode",
        "tktDSSN",
        "tktNID",
        "external"
    })
    public static class TktID {

        @XmlElement(name = "Mag")
        protected String mag;
        @XmlElement(name = "Barcode")
        protected String barcode;
        @XmlElement(name = "TktDSSN")
        protected Ticket.TktID.TktDSSN tktDSSN;
        @XmlElement(name = "TktNID")
        protected String tktNID;
        @XmlElement(name = "External")
        protected String external;

        /**
         * Gets the value of the mag property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMag() {
            return mag;
        }

        /**
         * Sets the value of the mag property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMag(String value) {
            this.mag = value;
        }

        /**
         * Gets the value of the barcode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getBarcode() {
            return barcode;
        }

        /**
         * Sets the value of the barcode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setBarcode(String value) {
            this.barcode = value;
        }

        /**
         * Gets the value of the tktDSSN property.
         * 
         * @return
         *     possible object is
         *     {@link Ticket.TktID.TktDSSN }
         *     
         */
        public Ticket.TktID.TktDSSN getTktDSSN() {
            return tktDSSN;
        }

        /**
         * Sets the value of the tktDSSN property.
         * 
         * @param value
         *     allowed object is
         *     {@link Ticket.TktID.TktDSSN }
         *     
         */
        public void setTktDSSN(Ticket.TktID.TktDSSN value) {
            this.tktDSSN = value;
        }

        /**
         * Gets the value of the tktNID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTktNID() {
            return tktNID;
        }

        /**
         * Sets the value of the tktNID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTktNID(String value) {
            this.tktNID = value;
        }

        /**
         * Gets the value of the external property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getExternal() {
            return external;
        }

        /**
         * Sets the value of the external property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setExternal(String value) {
            this.external = value;
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
         *         &lt;element name="TktDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
         *         &lt;element name="TktSite" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="TktStation" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="TktNbr" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
            "tktDate",
            "tktSite",
            "tktStation",
            "tktNbr"
        })
        public static class TktDSSN {

            @XmlElement(name = "TktDate", required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar tktDate;
            @XmlElement(name = "TktSite", required = true)
            protected String tktSite;
            @XmlElement(name = "TktStation", required = true)
            protected String tktStation;
            @XmlElement(name = "TktNbr", required = true)
            protected String tktNbr;

            /**
             * Gets the value of the tktDate property.
             * 
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public XMLGregorianCalendar getTktDate() {
                return tktDate;
            }

            /**
             * Sets the value of the tktDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *     
             */
            public void setTktDate(XMLGregorianCalendar value) {
                this.tktDate = value;
            }

            /**
             * Gets the value of the tktSite property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTktSite() {
                return tktSite;
            }

            /**
             * Sets the value of the tktSite property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTktSite(String value) {
                this.tktSite = value;
            }

            /**
             * Gets the value of the tktStation property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTktStation() {
                return tktStation;
            }

            /**
             * Sets the value of the tktStation property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTktStation(String value) {
                this.tktStation = value;
            }

            /**
             * Gets the value of the tktNbr property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTktNbr() {
                return tktNbr;
            }

            /**
             * Sets the value of the tktNbr property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTktNbr(String value) {
                this.tktNbr = value;
            }

        }

    }

}
