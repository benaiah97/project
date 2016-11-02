//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.02 at 09:15:38 AM CDT 
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
 *         &lt;element name="ExistingAccount" type="{}EntitlementAccount" minOccurs="0"/>
 *         &lt;element name="MediaData" type="{}MediaData" maxOccurs="250"/>
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
    "existingAccount",
    "mediaData"
})
@XmlRootElement(name = "AssociateMediaToAccountRequest")
public class AssociateMediaToAccountRequest {

    @XmlElement(name = "ExistingAccount")
    protected EntitlementAccount existingAccount;
    @XmlElement(name = "MediaData", required = true)
    protected List<MediaData> mediaData;

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

}
