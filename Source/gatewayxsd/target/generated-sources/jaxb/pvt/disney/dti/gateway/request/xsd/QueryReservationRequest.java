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
 *         &lt;element name="RequestType">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="EntitlementProduct"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="QueriedReservation">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="ResCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ResNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ExternalResCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "requestType",
    "queriedReservation"
})
@XmlRootElement(name = "QueryReservationRequest")
public class QueryReservationRequest {

    @XmlElement(name = "RequestType", required = true)
    protected String requestType;
    @XmlElement(name = "QueriedReservation", required = true)
    protected QueryReservationRequest.QueriedReservation queriedReservation;

    /**
     * Gets the value of the requestType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Sets the value of the requestType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestType(String value) {
        this.requestType = value;
    }

    /**
     * Gets the value of the queriedReservation property.
     * 
     * @return
     *     possible object is
     *     {@link QueryReservationRequest.QueriedReservation }
     *     
     */
    public QueryReservationRequest.QueriedReservation getQueriedReservation() {
        return queriedReservation;
    }

    /**
     * Sets the value of the queriedReservation property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryReservationRequest.QueriedReservation }
     *     
     */
    public void setQueriedReservation(QueryReservationRequest.QueriedReservation value) {
        this.queriedReservation = value;
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
     *         &lt;element name="ResCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="ResNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="ExternalResCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "resCode",
        "resNumber",
        "externalResCode"
    })
    public static class QueriedReservation {

        @XmlElement(name = "ResCode")
        protected String resCode;
        @XmlElement(name = "ResNumber")
        protected String resNumber;
        @XmlElement(name = "ExternalResCode")
        protected String externalResCode;

        /**
         * Gets the value of the resCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getResCode() {
            return resCode;
        }

        /**
         * Sets the value of the resCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setResCode(String value) {
            this.resCode = value;
        }

        /**
         * Gets the value of the resNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getResNumber() {
            return resNumber;
        }

        /**
         * Sets the value of the resNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setResNumber(String value) {
            this.resNumber = value;
        }

        /**
         * Gets the value of the externalResCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getExternalResCode() {
            return externalResCode;
        }

        /**
         * Sets the value of the externalResCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setExternalResCode(String value) {
            this.externalResCode = value;
        }

    }

}
