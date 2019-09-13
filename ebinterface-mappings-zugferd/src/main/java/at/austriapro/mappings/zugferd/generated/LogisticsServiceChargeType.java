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
 * <p>Java class for LogisticsServiceChargeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LogisticsServiceChargeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Description" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}TextType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AppliedAmount" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}AmountType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AppliedTradeTax" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}TradeTaxType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LogisticsServiceChargeType", propOrder = {
    "description",
    "appliedAmount",
    "appliedTradeTax"
})
public class LogisticsServiceChargeType {

    @XmlElement(name = "Description")
    protected List<TextType> description;
    @XmlElement(name = "AppliedAmount")
    protected List<AmountType> appliedAmount;
    @XmlElement(name = "AppliedTradeTax")
    protected List<TradeTaxType> appliedTradeTax;

    /**
     * Gets the value of the description property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the description property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescription().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TextType }
     * 
     * 
     */
    public List<TextType> getDescription() {
        if (description == null) {
            description = new ArrayList<TextType>();
        }
        return this.description;
    }

    /**
     * Gets the value of the appliedAmount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the appliedAmount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAppliedAmount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AmountType }
     * 
     * 
     */
    public List<AmountType> getAppliedAmount() {
        if (appliedAmount == null) {
            appliedAmount = new ArrayList<AmountType>();
        }
        return this.appliedAmount;
    }

    /**
     * Gets the value of the appliedTradeTax property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the appliedTradeTax property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAppliedTradeTax().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TradeTaxType }
     * 
     * 
     */
    public List<TradeTaxType> getAppliedTradeTax() {
        if (appliedTradeTax == null) {
            appliedTradeTax = new ArrayList<TradeTaxType>();
        }
        return this.appliedTradeTax;
    }

    public LogisticsServiceChargeType withDescription(TextType... values) {
        if (values!= null) {
            for (TextType value: values) {
                getDescription().add(value);
            }
        }
        return this;
    }

    public LogisticsServiceChargeType withDescription(Collection<TextType> values) {
        if (values!= null) {
            getDescription().addAll(values);
        }
        return this;
    }

    public LogisticsServiceChargeType withAppliedAmount(AmountType... values) {
        if (values!= null) {
            for (AmountType value: values) {
                getAppliedAmount().add(value);
            }
        }
        return this;
    }

    public LogisticsServiceChargeType withAppliedAmount(Collection<AmountType> values) {
        if (values!= null) {
            getAppliedAmount().addAll(values);
        }
        return this;
    }

    public LogisticsServiceChargeType withAppliedTradeTax(TradeTaxType... values) {
        if (values!= null) {
            for (TradeTaxType value: values) {
                getAppliedTradeTax().add(value);
            }
        }
        return this;
    }

    public LogisticsServiceChargeType withAppliedTradeTax(Collection<TradeTaxType> values) {
        if (values!= null) {
            getAppliedTradeTax().addAll(values);
        }
        return this;
    }

}
