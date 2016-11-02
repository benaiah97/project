//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.02 at 09:15:39 AM CDT 
//


package pvt.disney.dti.gateway.response.xsd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EntitlementAccount complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EntitlementAccount">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccountId" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="17"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Ticket" type="{}Ticket" maxOccurs="250" minOccurs="0"/>
 *         &lt;element name="MediaData" type="{}MediaData" maxOccurs="250" minOccurs="0"/>
 *         &lt;element name="TicketsOnAccount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="MediaOnAccount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntitlementAccount", propOrder = {
    "accountId",
    "ticket",
    "mediaData",
    "ticketsOnAccount",
    "mediaOnAccount"
})
public class EntitlementAccount {

    @XmlElement(name = "AccountId")
    protected String accountId;
    @XmlElement(name = "Ticket")
    protected List<Ticket> ticket;
    @XmlElement(name = "MediaData")
    protected List<MediaData> mediaData;
    @XmlElement(name = "TicketsOnAccount")
    protected Integer ticketsOnAccount;
    @XmlElement(name = "MediaOnAccount")
    protected Integer mediaOnAccount;

    /**
     * Gets the value of the accountId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Sets the value of the accountId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountId(String value) {
        this.accountId = value;
    }

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
     * Gets the value of the mediaData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mediaData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMediaData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MediaData }
     * 
     * 
     */
    public List<MediaData> getMediaData() {
        if (mediaData == null) {
            mediaData = new ArrayList<MediaData>();
        }
        return this.mediaData;
    }

    /**
     * Gets the value of the ticketsOnAccount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTicketsOnAccount() {
        return ticketsOnAccount;
    }

    /**
     * Sets the value of the ticketsOnAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTicketsOnAccount(Integer value) {
        this.ticketsOnAccount = value;
    }

    /**
     * Gets the value of the mediaOnAccount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getMediaOnAccount() {
        return mediaOnAccount;
    }

    /**
     * Sets the value of the mediaOnAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setMediaOnAccount(Integer value) {
        this.mediaOnAccount = value;
    }

}
