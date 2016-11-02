//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.02 at 09:15:38 AM CDT 
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
 *         &lt;element name="Ticket" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="TktItem" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="TktID">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;choice>
 *                             &lt;element name="Mag">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="MagTrack1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="MagTrack2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="Barcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="TktDSSN">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="TktDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                                       &lt;element name="TktSite" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="TktStation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                       &lt;element name="TktNbr" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="TktNID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="External" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/choice>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="IncludeTktDemographics" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IncludePassAttributes" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IncludeElectronicAttributes" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IncludeTicketRedeemability" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IncludeEntitlementAccount" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IncludeRenewalAttributes" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
    "ticket",
    "includeTktDemographics",
    "includePassAttributes",
    "includeElectronicAttributes",
    "includeTicketRedeemability",
    "includeEntitlementAccount",
    "includeRenewalAttributes"
})
@XmlRootElement(name = "QueryTicketRequest")
public class QueryTicketRequest {

    @XmlElement(name = "Ticket", required = true)
    protected List<QueryTicketRequest.Ticket> ticket;
    @XmlElement(name = "IncludeTktDemographics")
    protected Boolean includeTktDemographics;
    @XmlElement(name = "IncludePassAttributes")
    protected Boolean includePassAttributes;
    @XmlElement(name = "IncludeElectronicAttributes")
    protected Boolean includeElectronicAttributes;
    @XmlElement(name = "IncludeTicketRedeemability")
    protected Boolean includeTicketRedeemability;
    @XmlElement(name = "IncludeEntitlementAccount")
    protected Boolean includeEntitlementAccount;
    @XmlElement(name = "IncludeRenewalAttributes")
    protected Boolean includeRenewalAttributes;

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
     * {@link QueryTicketRequest.Ticket }
     * 
     * 
     */
    public List<QueryTicketRequest.Ticket> getTicket() {
        if (ticket == null) {
            ticket = new ArrayList<QueryTicketRequest.Ticket>();
        }
        return this.ticket;
    }

    /**
     * Gets the value of the includeTktDemographics property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeTktDemographics() {
        return includeTktDemographics;
    }

    /**
     * Sets the value of the includeTktDemographics property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeTktDemographics(Boolean value) {
        this.includeTktDemographics = value;
    }

    /**
     * Gets the value of the includePassAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludePassAttributes() {
        return includePassAttributes;
    }

    /**
     * Sets the value of the includePassAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludePassAttributes(Boolean value) {
        this.includePassAttributes = value;
    }

    /**
     * Gets the value of the includeElectronicAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeElectronicAttributes() {
        return includeElectronicAttributes;
    }

    /**
     * Sets the value of the includeElectronicAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeElectronicAttributes(Boolean value) {
        this.includeElectronicAttributes = value;
    }

    /**
     * Gets the value of the includeTicketRedeemability property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeTicketRedeemability() {
        return includeTicketRedeemability;
    }

    /**
     * Sets the value of the includeTicketRedeemability property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeTicketRedeemability(Boolean value) {
        this.includeTicketRedeemability = value;
    }

    /**
     * Gets the value of the includeEntitlementAccount property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeEntitlementAccount() {
        return includeEntitlementAccount;
    }

    /**
     * Sets the value of the includeEntitlementAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeEntitlementAccount(Boolean value) {
        this.includeEntitlementAccount = value;
    }

    /**
     * Gets the value of the includeRenewalAttributes property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeRenewalAttributes() {
        return includeRenewalAttributes;
    }

    /**
     * Sets the value of the includeRenewalAttributes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeRenewalAttributes(Boolean value) {
        this.includeRenewalAttributes = value;
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
     *         &lt;element name="TktItem" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="TktID">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;element name="Mag">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="MagTrack1" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                             &lt;element name="MagTrack2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
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
    @XmlType(name = "", propOrder = {
        "tktItem",
        "tktID"
    })
    public static class Ticket {

        @XmlElement(name = "TktItem", required = true)
        protected BigInteger tktItem;
        @XmlElement(name = "TktID", required = true)
        protected QueryTicketRequest.Ticket.TktID tktID;

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
         *     {@link QueryTicketRequest.Ticket.TktID }
         *     
         */
        public QueryTicketRequest.Ticket.TktID getTktID() {
            return tktID;
        }

        /**
         * Sets the value of the tktID property.
         * 
         * @param value
         *     allowed object is
         *     {@link QueryTicketRequest.Ticket.TktID }
         *     
         */
        public void setTktID(QueryTicketRequest.Ticket.TktID value) {
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
         *         &lt;element name="Mag">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="MagTrack1" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                   &lt;element name="MagTrack2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
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
            protected QueryTicketRequest.Ticket.TktID.Mag mag;
            @XmlElement(name = "Barcode")
            protected String barcode;
            @XmlElement(name = "TktDSSN")
            protected QueryTicketRequest.Ticket.TktID.TktDSSN tktDSSN;
            @XmlElement(name = "TktNID")
            protected String tktNID;
            @XmlElement(name = "External")
            protected String external;

            /**
             * Gets the value of the mag property.
             * 
             * @return
             *     possible object is
             *     {@link QueryTicketRequest.Ticket.TktID.Mag }
             *     
             */
            public QueryTicketRequest.Ticket.TktID.Mag getMag() {
                return mag;
            }

            /**
             * Sets the value of the mag property.
             * 
             * @param value
             *     allowed object is
             *     {@link QueryTicketRequest.Ticket.TktID.Mag }
             *     
             */
            public void setMag(QueryTicketRequest.Ticket.TktID.Mag value) {
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
             *     {@link QueryTicketRequest.Ticket.TktID.TktDSSN }
             *     
             */
            public QueryTicketRequest.Ticket.TktID.TktDSSN getTktDSSN() {
                return tktDSSN;
            }

            /**
             * Sets the value of the tktDSSN property.
             * 
             * @param value
             *     allowed object is
             *     {@link QueryTicketRequest.Ticket.TktID.TktDSSN }
             *     
             */
            public void setTktDSSN(QueryTicketRequest.Ticket.TktID.TktDSSN value) {
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
             *         &lt;element name="MagTrack1" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;element name="MagTrack2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
                "magTrack1",
                "magTrack2"
            })
            public static class Mag {

                @XmlElement(name = "MagTrack1", required = true)
                protected String magTrack1;
                @XmlElement(name = "MagTrack2")
                protected String magTrack2;

                /**
                 * Gets the value of the magTrack1 property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getMagTrack1() {
                    return magTrack1;
                }

                /**
                 * Sets the value of the magTrack1 property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setMagTrack1(String value) {
                    this.magTrack1 = value;
                }

                /**
                 * Gets the value of the magTrack2 property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getMagTrack2() {
                    return magTrack2;
                }

                /**
                 * Sets the value of the magTrack2 property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setMagTrack2(String value) {
                    this.magTrack2 = value;
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

}
