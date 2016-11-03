//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.03 at 08:39:01 AM CDT 
//


package pvt.disney.dti.gateway.request.xsd;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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
 *                 &lt;choice maxOccurs="unbounded" minOccurs="3">
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
 *                   &lt;element name="TktStatus" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="StatusItem" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="StatusValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="TktValidity" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;choice maxOccurs="unbounded">
 *                             &lt;element name="ValidStart" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                             &lt;element name="ValidEnd" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                           &lt;/choice>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="TktSecurity" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;choice maxOccurs="unbounded">
 *                             &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="Level" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="Template" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/choice>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="TktShell" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TktMarket" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="TktNote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "ticket"
})
@XmlRootElement(name = "UpdateTicketRequest")
public class UpdateTicketRequest {

    @XmlElement(name = "Ticket", required = true)
    protected List<UpdateTicketRequest.Ticket> ticket;

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
     * {@link UpdateTicketRequest.Ticket }
     * 
     * 
     */
    public List<UpdateTicketRequest.Ticket> getTicket() {
        if (ticket == null) {
            ticket = new ArrayList<UpdateTicketRequest.Ticket>();
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
     *       &lt;choice maxOccurs="unbounded" minOccurs="3">
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
     *         &lt;element name="TktStatus" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="StatusItem" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="StatusValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="TktValidity" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice maxOccurs="unbounded">
     *                   &lt;element name="ValidStart" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *                   &lt;element name="ValidEnd" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *                 &lt;/choice>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="TktSecurity" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice maxOccurs="unbounded">
     *                   &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="Level" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="Template" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                 &lt;/choice>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="TktShell" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TktMarket" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="TktNote" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "tktItemOrTktIDOrTktStatus"
    })
    public static class Ticket {

        @XmlElementRefs({
            @XmlElementRef(name = "TktValidity", type = JAXBElement.class),
            @XmlElementRef(name = "TktNote", type = JAXBElement.class),
            @XmlElementRef(name = "TktStatus", type = JAXBElement.class),
            @XmlElementRef(name = "TktID", type = JAXBElement.class),
            @XmlElementRef(name = "TktMarket", type = JAXBElement.class),
            @XmlElementRef(name = "TktShell", type = JAXBElement.class),
            @XmlElementRef(name = "TktSecurity", type = JAXBElement.class),
            @XmlElementRef(name = "TktItem", type = JAXBElement.class)
        })
        protected List<JAXBElement<?>> tktItemOrTktIDOrTktStatus;

        /**
         * Gets the value of the tktItemOrTktIDOrTktStatus property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the tktItemOrTktIDOrTktStatus property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTktItemOrTktIDOrTktStatus().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JAXBElement }{@code <}{@link UpdateTicketRequest.Ticket.TktValidity }{@code >}
         * {@link JAXBElement }{@code <}{@link String }{@code >}
         * {@link JAXBElement }{@code <}{@link UpdateTicketRequest.Ticket.TktStatus }{@code >}
         * {@link JAXBElement }{@code <}{@link UpdateTicketRequest.Ticket.TktID }{@code >}
         * {@link JAXBElement }{@code <}{@link String }{@code >}
         * {@link JAXBElement }{@code <}{@link String }{@code >}
         * {@link JAXBElement }{@code <}{@link UpdateTicketRequest.Ticket.TktSecurity }{@code >}
         * {@link JAXBElement }{@code <}{@link BigInteger }{@code >}
         * 
         * 
         */
        public List<JAXBElement<?>> getTktItemOrTktIDOrTktStatus() {
            if (tktItemOrTktIDOrTktStatus == null) {
                tktItemOrTktIDOrTktStatus = new ArrayList<JAXBElement<?>>();
            }
            return this.tktItemOrTktIDOrTktStatus;
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
            protected UpdateTicketRequest.Ticket.TktID.Mag mag;
            @XmlElement(name = "Barcode")
            protected String barcode;
            @XmlElement(name = "TktDSSN")
            protected UpdateTicketRequest.Ticket.TktID.TktDSSN tktDSSN;
            @XmlElement(name = "TktNID")
            protected String tktNID;
            @XmlElement(name = "External")
            protected String external;

            /**
             * Gets the value of the mag property.
             * 
             * @return
             *     possible object is
             *     {@link UpdateTicketRequest.Ticket.TktID.Mag }
             *     
             */
            public UpdateTicketRequest.Ticket.TktID.Mag getMag() {
                return mag;
            }

            /**
             * Sets the value of the mag property.
             * 
             * @param value
             *     allowed object is
             *     {@link UpdateTicketRequest.Ticket.TktID.Mag }
             *     
             */
            public void setMag(UpdateTicketRequest.Ticket.TktID.Mag value) {
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
             *     {@link UpdateTicketRequest.Ticket.TktID.TktDSSN }
             *     
             */
            public UpdateTicketRequest.Ticket.TktID.TktDSSN getTktDSSN() {
                return tktDSSN;
            }

            /**
             * Sets the value of the tktDSSN property.
             * 
             * @param value
             *     allowed object is
             *     {@link UpdateTicketRequest.Ticket.TktID.TktDSSN }
             *     
             */
            public void setTktDSSN(UpdateTicketRequest.Ticket.TktID.TktDSSN value) {
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


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;choice maxOccurs="unbounded">
         *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="Level" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="Template" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
            "typeOrLevelOrTemplate"
        })
        public static class TktSecurity {

            @XmlElementRefs({
                @XmlElementRef(name = "Level", type = JAXBElement.class),
                @XmlElementRef(name = "Type", type = JAXBElement.class),
                @XmlElementRef(name = "Template", type = JAXBElement.class)
            })
            protected List<JAXBElement<String>> typeOrLevelOrTemplate;

            /**
             * Gets the value of the typeOrLevelOrTemplate property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the typeOrLevelOrTemplate property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getTypeOrLevelOrTemplate().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link JAXBElement }{@code <}{@link String }{@code >}
             * {@link JAXBElement }{@code <}{@link String }{@code >}
             * {@link JAXBElement }{@code <}{@link String }{@code >}
             * 
             * 
             */
            public List<JAXBElement<String>> getTypeOrLevelOrTemplate() {
                if (typeOrLevelOrTemplate == null) {
                    typeOrLevelOrTemplate = new ArrayList<JAXBElement<String>>();
                }
                return this.typeOrLevelOrTemplate;
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
         *         &lt;element name="StatusItem" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="StatusValue" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
            "statusItem",
            "statusValue"
        })
        public static class TktStatus {

            @XmlElement(name = "StatusItem", required = true)
            protected String statusItem;
            @XmlElement(name = "StatusValue", required = true)
            protected String statusValue;

            /**
             * Gets the value of the statusItem property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStatusItem() {
                return statusItem;
            }

            /**
             * Sets the value of the statusItem property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStatusItem(String value) {
                this.statusItem = value;
            }

            /**
             * Gets the value of the statusValue property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStatusValue() {
                return statusValue;
            }

            /**
             * Sets the value of the statusValue property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStatusValue(String value) {
                this.statusValue = value;
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
         *       &lt;choice maxOccurs="unbounded">
         *         &lt;element name="ValidStart" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
         *         &lt;element name="ValidEnd" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
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
            "validStartOrValidEnd"
        })
        public static class TktValidity {

            @XmlElementRefs({
                @XmlElementRef(name = "ValidStart", type = JAXBElement.class),
                @XmlElementRef(name = "ValidEnd", type = JAXBElement.class)
            })
            protected List<JAXBElement<XMLGregorianCalendar>> validStartOrValidEnd;

            /**
             * Gets the value of the validStartOrValidEnd property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the validStartOrValidEnd property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getValidStartOrValidEnd().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
             * {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
             * 
             * 
             */
            public List<JAXBElement<XMLGregorianCalendar>> getValidStartOrValidEnd() {
                if (validStartOrValidEnd == null) {
                    validStartOrValidEnd = new ArrayList<JAXBElement<XMLGregorianCalendar>>();
                }
                return this.validStartOrValidEnd;
            }

        }

    }

}
