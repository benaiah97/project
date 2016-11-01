//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.01 at 03:55:33 PM CDT 
//


package pvt.disney.dti.gateway.request.xsd;

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
 *         &lt;element name="Ticket" type="{}Ticket" maxOccurs="250"/>
 *         &lt;element name="NewAccount" type="{}ExternalReferenceType" minOccurs="0"/>
 *         &lt;element name="ExistingAccount" type="{}EntitlementAccount" minOccurs="0"/>
 *         &lt;element name="NewMediaData" type="{}MediaData" maxOccurs="250" minOccurs="0"/>
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
    "newAccount",
    "existingAccount",
    "newMediaData"
})
@XmlRootElement(name = "TickerateEntitlementRequest")
public class TickerateEntitlementRequest {

    @XmlElement(name = "Ticket", required = true)
    protected List<Ticket> ticket;
    @XmlElement(name = "NewAccount")
    protected ExternalReferenceType newAccount;
    @XmlElement(name = "ExistingAccount")
    protected EntitlementAccount existingAccount;
    @XmlElement(name = "NewMediaData")
    protected List<MediaData> newMediaData;

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
     * {@link Ticket }
     * 
     * 
     */
    public List<Ticket> getTicket() {
        if (ticket == null) {
            ticket = new ArrayList<Ticket>();
        }
        return this.ticket;
    }

    /**
     * Gets the value of the newAccount property.
     * 
     * @return
     *     possible object is
     *     {@link ExternalReferenceType }
     *     
     */
    public ExternalReferenceType getNewAccount() {
        return newAccount;
    }

    /**
     * Sets the value of the newAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExternalReferenceType }
     *     
     */
    public void setNewAccount(ExternalReferenceType value) {
        this.newAccount = value;
    }

    /**
     * Gets the value of the existingAccount property.
     * 
     * @return
     *     possible object is
     *     {@link EntitlementAccount }
     *     
     */
    public EntitlementAccount getExistingAccount() {
        return existingAccount;
    }

    /**
     * Sets the value of the existingAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntitlementAccount }
     *     
     */
    public void setExistingAccount(EntitlementAccount value) {
        this.existingAccount = value;
    }

    /**
     * Gets the value of the newMediaData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the newMediaData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNewMediaData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MediaData }
     * 
     * 
     */
    public List<MediaData> getNewMediaData() {
        if (newMediaData == null) {
            newMediaData = new ArrayList<MediaData>();
        }
        return this.newMediaData;
    }

}
