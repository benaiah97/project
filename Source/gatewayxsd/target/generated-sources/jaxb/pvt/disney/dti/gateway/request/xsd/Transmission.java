//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.15 at 04:42:01 PM CST 
//


package pvt.disney.dti.gateway.request.xsd;

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
 *         &lt;element name="Payload">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{}PayloadHeader"/>
 *                   &lt;element name="Command">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element ref="{}CommandHeader"/>
 *                             &lt;element ref="{}QueryTicketRequest" minOccurs="0"/>
 *                             &lt;element ref="{}UpgradeAlphaRequest" minOccurs="0"/>
 *                             &lt;element ref="{}VoidTicketRequest" minOccurs="0"/>
 *                             &lt;element ref="{}ReservationRequest" minOccurs="0"/>
 *                             &lt;element ref="{}CreateTicketRequest" minOccurs="0"/>
 *                             &lt;element ref="{}UpdateTicketRequest" minOccurs="0"/>
 *                             &lt;element ref="{}UpdateTransactionRequest" minOccurs="0"/>
 *                             &lt;element ref="{}UpgradeEntitlementRequest" minOccurs="0"/>
 *                             &lt;element ref="{}QueryReservationRequest" minOccurs="0"/>
 *                             &lt;element ref="{}RenewEntitlementRequest" minOccurs="0"/>
 *                             &lt;element ref="{}AssociateMediaToAccountRequest" minOccurs="0"/>
 *                             &lt;element ref="{}TickerateEntitlementRequest" minOccurs="0"/>
 *                             &lt;element ref="{}VoidReservationRequest" minOccurs="0"/>
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
    "payload"
})
@XmlRootElement(name = "Transmission")
public class Transmission {

    @XmlElement(name = "Payload", required = true)
    protected Transmission.Payload payload;

    /**
     * Gets the value of the payload property.
     * 
     * @return
     *     possible object is
     *     {@link Transmission.Payload }
     *     
     */
    public Transmission.Payload getPayload() {
        return payload;
    }

    /**
     * Sets the value of the payload property.
     * 
     * @param value
     *     allowed object is
     *     {@link Transmission.Payload }
     *     
     */
    public void setPayload(Transmission.Payload value) {
        this.payload = value;
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
     *         &lt;element ref="{}PayloadHeader"/>
     *         &lt;element name="Command">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element ref="{}CommandHeader"/>
     *                   &lt;element ref="{}QueryTicketRequest" minOccurs="0"/>
     *                   &lt;element ref="{}UpgradeAlphaRequest" minOccurs="0"/>
     *                   &lt;element ref="{}VoidTicketRequest" minOccurs="0"/>
     *                   &lt;element ref="{}ReservationRequest" minOccurs="0"/>
     *                   &lt;element ref="{}CreateTicketRequest" minOccurs="0"/>
     *                   &lt;element ref="{}UpdateTicketRequest" minOccurs="0"/>
     *                   &lt;element ref="{}UpdateTransactionRequest" minOccurs="0"/>
     *                   &lt;element ref="{}UpgradeEntitlementRequest" minOccurs="0"/>
     *                   &lt;element ref="{}QueryReservationRequest" minOccurs="0"/>
     *                   &lt;element ref="{}RenewEntitlementRequest" minOccurs="0"/>
     *                   &lt;element ref="{}AssociateMediaToAccountRequest" minOccurs="0"/>
     *                   &lt;element ref="{}TickerateEntitlementRequest" minOccurs="0"/>
     *                   &lt;element ref="{}VoidReservationRequest" minOccurs="0"/>
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
        "payloadHeader",
        "command"
    })
    public static class Payload {

        @XmlElement(name = "PayloadHeader", required = true)
        protected PayloadHeader payloadHeader;
        @XmlElement(name = "Command", required = true)
        protected Transmission.Payload.Command command;

        /**
         * Gets the value of the payloadHeader property.
         * 
         * @return
         *     possible object is
         *     {@link PayloadHeader }
         *     
         */
        public PayloadHeader getPayloadHeader() {
            return payloadHeader;
        }

        /**
         * Sets the value of the payloadHeader property.
         * 
         * @param value
         *     allowed object is
         *     {@link PayloadHeader }
         *     
         */
        public void setPayloadHeader(PayloadHeader value) {
            this.payloadHeader = value;
        }

        /**
         * Gets the value of the command property.
         * 
         * @return
         *     possible object is
         *     {@link Transmission.Payload.Command }
         *     
         */
        public Transmission.Payload.Command getCommand() {
            return command;
        }

        /**
         * Sets the value of the command property.
         * 
         * @param value
         *     allowed object is
         *     {@link Transmission.Payload.Command }
         *     
         */
        public void setCommand(Transmission.Payload.Command value) {
            this.command = value;
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
         *         &lt;element ref="{}CommandHeader"/>
         *         &lt;element ref="{}QueryTicketRequest" minOccurs="0"/>
         *         &lt;element ref="{}UpgradeAlphaRequest" minOccurs="0"/>
         *         &lt;element ref="{}VoidTicketRequest" minOccurs="0"/>
         *         &lt;element ref="{}ReservationRequest" minOccurs="0"/>
         *         &lt;element ref="{}CreateTicketRequest" minOccurs="0"/>
         *         &lt;element ref="{}UpdateTicketRequest" minOccurs="0"/>
         *         &lt;element ref="{}UpdateTransactionRequest" minOccurs="0"/>
         *         &lt;element ref="{}UpgradeEntitlementRequest" minOccurs="0"/>
         *         &lt;element ref="{}QueryReservationRequest" minOccurs="0"/>
         *         &lt;element ref="{}RenewEntitlementRequest" minOccurs="0"/>
         *         &lt;element ref="{}AssociateMediaToAccountRequest" minOccurs="0"/>
         *         &lt;element ref="{}TickerateEntitlementRequest" minOccurs="0"/>
         *         &lt;element ref="{}VoidReservationRequest" minOccurs="0"/>
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
            "commandHeader",
            "queryTicketRequest",
            "upgradeAlphaRequest",
            "voidTicketRequest",
            "reservationRequest",
            "createTicketRequest",
            "updateTicketRequest",
            "updateTransactionRequest",
            "upgradeEntitlementRequest",
            "queryReservationRequest",
            "renewEntitlementRequest",
            "associateMediaToAccountRequest",
            "tickerateEntitlementRequest",
            "voidReservationRequest"
        })
        public static class Command {

            @XmlElement(name = "CommandHeader", required = true)
            protected CommandHeader commandHeader;
            @XmlElement(name = "QueryTicketRequest")
            protected QueryTicketRequest queryTicketRequest;
            @XmlElement(name = "UpgradeAlphaRequest")
            protected UpgradeAlphaRequest upgradeAlphaRequest;
            @XmlElement(name = "VoidTicketRequest")
            protected VoidTicketRequest voidTicketRequest;
            @XmlElement(name = "ReservationRequest")
            protected ReservationRequest reservationRequest;
            @XmlElement(name = "CreateTicketRequest")
            protected CreateTicketRequest createTicketRequest;
            @XmlElement(name = "UpdateTicketRequest")
            protected UpdateTicketRequest updateTicketRequest;
            @XmlElement(name = "UpdateTransactionRequest")
            protected UpdateTransactionRequest updateTransactionRequest;
            @XmlElement(name = "UpgradeEntitlementRequest")
            protected UpgradeEntitlementRequest upgradeEntitlementRequest;
            @XmlElement(name = "QueryReservationRequest")
            protected QueryReservationRequest queryReservationRequest;
            @XmlElement(name = "RenewEntitlementRequest")
            protected RenewEntitlementRequest renewEntitlementRequest;
            @XmlElement(name = "AssociateMediaToAccountRequest")
            protected AssociateMediaToAccountRequest associateMediaToAccountRequest;
            @XmlElement(name = "TickerateEntitlementRequest")
            protected TickerateEntitlementRequest tickerateEntitlementRequest;
            @XmlElement(name = "VoidReservationRequest")
            protected VoidReservationRequest voidReservationRequest;

            /**
             * Gets the value of the commandHeader property.
             * 
             * @return
             *     possible object is
             *     {@link CommandHeader }
             *     
             */
            public CommandHeader getCommandHeader() {
                return commandHeader;
            }

            /**
             * Sets the value of the commandHeader property.
             * 
             * @param value
             *     allowed object is
             *     {@link CommandHeader }
             *     
             */
            public void setCommandHeader(CommandHeader value) {
                this.commandHeader = value;
            }

            /**
             * Gets the value of the queryTicketRequest property.
             * 
             * @return
             *     possible object is
             *     {@link QueryTicketRequest }
             *     
             */
            public QueryTicketRequest getQueryTicketRequest() {
                return queryTicketRequest;
            }

            /**
             * Sets the value of the queryTicketRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link QueryTicketRequest }
             *     
             */
            public void setQueryTicketRequest(QueryTicketRequest value) {
                this.queryTicketRequest = value;
            }

            /**
             * Gets the value of the upgradeAlphaRequest property.
             * 
             * @return
             *     possible object is
             *     {@link UpgradeAlphaRequest }
             *     
             */
            public UpgradeAlphaRequest getUpgradeAlphaRequest() {
                return upgradeAlphaRequest;
            }

            /**
             * Sets the value of the upgradeAlphaRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link UpgradeAlphaRequest }
             *     
             */
            public void setUpgradeAlphaRequest(UpgradeAlphaRequest value) {
                this.upgradeAlphaRequest = value;
            }

            /**
             * Gets the value of the voidTicketRequest property.
             * 
             * @return
             *     possible object is
             *     {@link VoidTicketRequest }
             *     
             */
            public VoidTicketRequest getVoidTicketRequest() {
                return voidTicketRequest;
            }

            /**
             * Sets the value of the voidTicketRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link VoidTicketRequest }
             *     
             */
            public void setVoidTicketRequest(VoidTicketRequest value) {
                this.voidTicketRequest = value;
            }

            /**
             * Gets the value of the reservationRequest property.
             * 
             * @return
             *     possible object is
             *     {@link ReservationRequest }
             *     
             */
            public ReservationRequest getReservationRequest() {
                return reservationRequest;
            }

            /**
             * Sets the value of the reservationRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link ReservationRequest }
             *     
             */
            public void setReservationRequest(ReservationRequest value) {
                this.reservationRequest = value;
            }

            /**
             * Gets the value of the createTicketRequest property.
             * 
             * @return
             *     possible object is
             *     {@link CreateTicketRequest }
             *     
             */
            public CreateTicketRequest getCreateTicketRequest() {
                return createTicketRequest;
            }

            /**
             * Sets the value of the createTicketRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link CreateTicketRequest }
             *     
             */
            public void setCreateTicketRequest(CreateTicketRequest value) {
                this.createTicketRequest = value;
            }

            /**
             * Gets the value of the updateTicketRequest property.
             * 
             * @return
             *     possible object is
             *     {@link UpdateTicketRequest }
             *     
             */
            public UpdateTicketRequest getUpdateTicketRequest() {
                return updateTicketRequest;
            }

            /**
             * Sets the value of the updateTicketRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link UpdateTicketRequest }
             *     
             */
            public void setUpdateTicketRequest(UpdateTicketRequest value) {
                this.updateTicketRequest = value;
            }

            /**
             * Gets the value of the updateTransactionRequest property.
             * 
             * @return
             *     possible object is
             *     {@link UpdateTransactionRequest }
             *     
             */
            public UpdateTransactionRequest getUpdateTransactionRequest() {
                return updateTransactionRequest;
            }

            /**
             * Sets the value of the updateTransactionRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link UpdateTransactionRequest }
             *     
             */
            public void setUpdateTransactionRequest(UpdateTransactionRequest value) {
                this.updateTransactionRequest = value;
            }

            /**
             * Gets the value of the upgradeEntitlementRequest property.
             * 
             * @return
             *     possible object is
             *     {@link UpgradeEntitlementRequest }
             *     
             */
            public UpgradeEntitlementRequest getUpgradeEntitlementRequest() {
                return upgradeEntitlementRequest;
            }

            /**
             * Sets the value of the upgradeEntitlementRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link UpgradeEntitlementRequest }
             *     
             */
            public void setUpgradeEntitlementRequest(UpgradeEntitlementRequest value) {
                this.upgradeEntitlementRequest = value;
            }

            /**
             * Gets the value of the queryReservationRequest property.
             * 
             * @return
             *     possible object is
             *     {@link QueryReservationRequest }
             *     
             */
            public QueryReservationRequest getQueryReservationRequest() {
                return queryReservationRequest;
            }

            /**
             * Sets the value of the queryReservationRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link QueryReservationRequest }
             *     
             */
            public void setQueryReservationRequest(QueryReservationRequest value) {
                this.queryReservationRequest = value;
            }

            /**
             * Gets the value of the renewEntitlementRequest property.
             * 
             * @return
             *     possible object is
             *     {@link RenewEntitlementRequest }
             *     
             */
            public RenewEntitlementRequest getRenewEntitlementRequest() {
                return renewEntitlementRequest;
            }

            /**
             * Sets the value of the renewEntitlementRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link RenewEntitlementRequest }
             *     
             */
            public void setRenewEntitlementRequest(RenewEntitlementRequest value) {
                this.renewEntitlementRequest = value;
            }

            /**
             * Gets the value of the associateMediaToAccountRequest property.
             * 
             * @return
             *     possible object is
             *     {@link AssociateMediaToAccountRequest }
             *     
             */
            public AssociateMediaToAccountRequest getAssociateMediaToAccountRequest() {
                return associateMediaToAccountRequest;
            }

            /**
             * Sets the value of the associateMediaToAccountRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link AssociateMediaToAccountRequest }
             *     
             */
            public void setAssociateMediaToAccountRequest(AssociateMediaToAccountRequest value) {
                this.associateMediaToAccountRequest = value;
            }

            /**
             * Gets the value of the tickerateEntitlementRequest property.
             * 
             * @return
             *     possible object is
             *     {@link TickerateEntitlementRequest }
             *     
             */
            public TickerateEntitlementRequest getTickerateEntitlementRequest() {
                return tickerateEntitlementRequest;
            }

            /**
             * Sets the value of the tickerateEntitlementRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link TickerateEntitlementRequest }
             *     
             */
            public void setTickerateEntitlementRequest(TickerateEntitlementRequest value) {
                this.tickerateEntitlementRequest = value;
            }

            /**
             * Gets the value of the voidReservationRequest property.
             * 
             * @return
             *     possible object is
             *     {@link VoidReservationRequest }
             *     
             */
            public VoidReservationRequest getVoidReservationRequest() {
                return voidReservationRequest;
            }

            /**
             * Sets the value of the voidReservationRequest property.
             * 
             * @param value
             *     allowed object is
             *     {@link VoidReservationRequest }
             *     
             */
            public void setVoidReservationRequest(VoidReservationRequest value) {
                this.voidReservationRequest = value;
            }

        }

    }

}
