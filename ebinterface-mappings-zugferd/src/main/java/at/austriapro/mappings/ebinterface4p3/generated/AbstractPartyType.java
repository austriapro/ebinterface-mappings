//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.20 at 05:47:04 PM CET 
//


package at.austriapro.mappings.ebinterface4p3.generated;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AbstractPartyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractPartyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p3/}VATIdentificationNumber"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p3/}FurtherIdentification" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p3/}OrderReference" minOccurs="0"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p3/}Address"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractPartyType", propOrder = {
    "vatIdentificationNumber",
    "furtherIdentifications",
    "orderReference",
    "address"
})
@XmlSeeAlso({
    OrderingParty.class,
    InvoiceRecipient.class,
    Biller.class
})
public class AbstractPartyType
    implements Serializable
{

    @XmlElement(name = "VATIdentificationNumber", required = true)
    protected String vatIdentificationNumber;
    @XmlElement(name = "FurtherIdentification")
    protected List<FurtherIdentification> furtherIdentifications;
    @XmlElement(name = "OrderReference")
    protected OrderReferenceType orderReference;
    @XmlElement(name = "Address", required = true)
    protected Address address;

    /**
     * Gets the value of the vatIdentificationNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVATIdentificationNumber() {
        return vatIdentificationNumber;
    }

    /**
     * Sets the value of the vatIdentificationNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVATIdentificationNumber(String value) {
        this.vatIdentificationNumber = value;
    }

    /**
     * Gets the value of the furtherIdentifications property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the furtherIdentifications property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFurtherIdentifications().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FurtherIdentification }
     * 
     * 
     */
    public List<FurtherIdentification> getFurtherIdentifications() {
        if (furtherIdentifications == null) {
            furtherIdentifications = new ArrayList<FurtherIdentification>();
        }
        return this.furtherIdentifications;
    }

    /**
     * Gets the value of the orderReference property.
     * 
     * @return
     *     possible object is
     *     {@link OrderReferenceType }
     *     
     */
    public OrderReferenceType getOrderReference() {
        return orderReference;
    }

    /**
     * Sets the value of the orderReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderReferenceType }
     *     
     */
    public void setOrderReference(OrderReferenceType value) {
        this.orderReference = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link Address }
     *     
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link Address }
     *     
     */
    public void setAddress(Address value) {
        this.address = value;
    }

    public AbstractPartyType withVATIdentificationNumber(String value) {
        setVATIdentificationNumber(value);
        return this;
    }

    public AbstractPartyType withFurtherIdentifications(FurtherIdentification... values) {
        if (values!= null) {
            for (FurtherIdentification value: values) {
                getFurtherIdentifications().add(value);
            }
        }
        return this;
    }

    public AbstractPartyType withFurtherIdentifications(Collection<FurtherIdentification> values) {
        if (values!= null) {
            getFurtherIdentifications().addAll(values);
        }
        return this;
    }

    public AbstractPartyType withOrderReference(OrderReferenceType value) {
        setOrderReference(value);
        return this;
    }

    public AbstractPartyType withAddress(Address value) {
        setAddress(value);
        return this;
    }

}
