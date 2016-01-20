//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.18 at 03:17:09 PM CET 
//


package at.austriapro.mappings.zugferd.generated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TradePartyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TradePartyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}IDType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="GlobalID" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}IDType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Name" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}TextType" minOccurs="0"/>
 *         &lt;element name="DefinedTradeContact" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}TradeContactType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PostalTradeAddress" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}TradeAddressType" minOccurs="0"/>
 *         &lt;element name="SpecifiedTaxRegistration" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}TaxRegistrationType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TradePartyType", propOrder = {
    "id",
    "globalID",
    "name",
    "definedTradeContact",
    "postalTradeAddress",
    "specifiedTaxRegistration"
})
public class TradePartyType {

    @XmlElement(name = "ID")
    protected List<IDType> id;
    @XmlElement(name = "GlobalID")
    protected List<IDType> globalID;
    @XmlElement(name = "Name")
    protected TextType name;
    @XmlElement(name = "DefinedTradeContact")
    protected List<TradeContactType> definedTradeContact;
    @XmlElement(name = "PostalTradeAddress")
    protected TradeAddressType postalTradeAddress;
    @XmlElement(name = "SpecifiedTaxRegistration")
    protected List<TaxRegistrationType> specifiedTaxRegistration;

    /**
     * Gets the value of the id property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the id property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IDType }
     * 
     * 
     */
    public List<IDType> getID() {
        if (id == null) {
            id = new ArrayList<IDType>();
        }
        return this.id;
    }

    /**
     * Gets the value of the globalID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the globalID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGlobalID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IDType }
     * 
     * 
     */
    public List<IDType> getGlobalID() {
        if (globalID == null) {
            globalID = new ArrayList<IDType>();
        }
        return this.globalID;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setName(TextType value) {
        this.name = value;
    }

    /**
     * Gets the value of the definedTradeContact property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the definedTradeContact property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDefinedTradeContact().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TradeContactType }
     * 
     * 
     */
    public List<TradeContactType> getDefinedTradeContact() {
        if (definedTradeContact == null) {
            definedTradeContact = new ArrayList<TradeContactType>();
        }
        return this.definedTradeContact;
    }

    /**
     * Gets the value of the postalTradeAddress property.
     * 
     * @return
     *     possible object is
     *     {@link TradeAddressType }
     *     
     */
    public TradeAddressType getPostalTradeAddress() {
        return postalTradeAddress;
    }

    /**
     * Sets the value of the postalTradeAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link TradeAddressType }
     *     
     */
    public void setPostalTradeAddress(TradeAddressType value) {
        this.postalTradeAddress = value;
    }

    /**
     * Gets the value of the specifiedTaxRegistration property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the specifiedTaxRegistration property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSpecifiedTaxRegistration().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaxRegistrationType }
     * 
     * 
     */
    public List<TaxRegistrationType> getSpecifiedTaxRegistration() {
        if (specifiedTaxRegistration == null) {
            specifiedTaxRegistration = new ArrayList<TaxRegistrationType>();
        }
        return this.specifiedTaxRegistration;
    }

    public TradePartyType withID(IDType... values) {
        if (values!= null) {
            for (IDType value: values) {
                getID().add(value);
            }
        }
        return this;
    }

    public TradePartyType withID(Collection<IDType> values) {
        if (values!= null) {
            getID().addAll(values);
        }
        return this;
    }

    public TradePartyType withGlobalID(IDType... values) {
        if (values!= null) {
            for (IDType value: values) {
                getGlobalID().add(value);
            }
        }
        return this;
    }

    public TradePartyType withGlobalID(Collection<IDType> values) {
        if (values!= null) {
            getGlobalID().addAll(values);
        }
        return this;
    }

    public TradePartyType withName(TextType value) {
        setName(value);
        return this;
    }

    public TradePartyType withDefinedTradeContact(TradeContactType... values) {
        if (values!= null) {
            for (TradeContactType value: values) {
                getDefinedTradeContact().add(value);
            }
        }
        return this;
    }

    public TradePartyType withDefinedTradeContact(Collection<TradeContactType> values) {
        if (values!= null) {
            getDefinedTradeContact().addAll(values);
        }
        return this;
    }

    public TradePartyType withPostalTradeAddress(TradeAddressType value) {
        setPostalTradeAddress(value);
        return this;
    }

    public TradePartyType withSpecifiedTaxRegistration(TaxRegistrationType... values) {
        if (values!= null) {
            for (TaxRegistrationType value: values) {
                getSpecifiedTaxRegistration().add(value);
            }
        }
        return this;
    }

    public TradePartyType withSpecifiedTaxRegistration(Collection<TaxRegistrationType> values) {
        if (values!= null) {
            getSpecifiedTaxRegistration().addAll(values);
        }
        return this;
    }

}
