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
 * <p>Java class for TradeAllowanceChargeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TradeAllowanceChargeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ChargeIndicator" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}IndicatorType" minOccurs="0"/>
 *         &lt;element name="SequenceNumeric" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}NumericType" minOccurs="0"/>
 *         &lt;element name="CalculationPercent" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}PercentType" minOccurs="0"/>
 *         &lt;element name="BasisAmount" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}AmountType" minOccurs="0"/>
 *         &lt;element name="BasisQuantity" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}QuantityType" minOccurs="0"/>
 *         &lt;element name="ActualAmount" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}AmountType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ReasonCode" type="{urn:un:unece:uncefact:data:standard:QualifiedDataType:12}AllowanceChargeReasonCodeType" minOccurs="0"/>
 *         &lt;element name="Reason" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}TextType" minOccurs="0"/>
 *         &lt;element name="CategoryTradeTax" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}TradeTaxType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TradeAllowanceChargeType", propOrder = {
    "chargeIndicator",
    "sequenceNumeric",
    "calculationPercent",
    "basisAmount",
    "basisQuantity",
    "actualAmount",
    "reasonCode",
    "reason",
    "categoryTradeTax"
})
public class TradeAllowanceChargeType {

    @XmlElement(name = "ChargeIndicator")
    protected IndicatorType chargeIndicator;
    @XmlElement(name = "SequenceNumeric")
    protected NumericType sequenceNumeric;
    @XmlElement(name = "CalculationPercent")
    protected PercentType calculationPercent;
    @XmlElement(name = "BasisAmount")
    protected AmountType basisAmount;
    @XmlElement(name = "BasisQuantity")
    protected QuantityType basisQuantity;
    @XmlElement(name = "ActualAmount")
    protected List<AmountType> actualAmount;
    @XmlElement(name = "ReasonCode")
    protected AllowanceChargeReasonCodeType reasonCode;
    @XmlElement(name = "Reason")
    protected TextType reason;
    @XmlElement(name = "CategoryTradeTax")
    protected List<TradeTaxType> categoryTradeTax;

    /**
     * Gets the value of the chargeIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link IndicatorType }
     *     
     */
    public IndicatorType getChargeIndicator() {
        return chargeIndicator;
    }

    /**
     * Sets the value of the chargeIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link IndicatorType }
     *     
     */
    public void setChargeIndicator(IndicatorType value) {
        this.chargeIndicator = value;
    }

    /**
     * Gets the value of the sequenceNumeric property.
     * 
     * @return
     *     possible object is
     *     {@link NumericType }
     *     
     */
    public NumericType getSequenceNumeric() {
        return sequenceNumeric;
    }

    /**
     * Sets the value of the sequenceNumeric property.
     * 
     * @param value
     *     allowed object is
     *     {@link NumericType }
     *     
     */
    public void setSequenceNumeric(NumericType value) {
        this.sequenceNumeric = value;
    }

    /**
     * Gets the value of the calculationPercent property.
     * 
     * @return
     *     possible object is
     *     {@link PercentType }
     *     
     */
    public PercentType getCalculationPercent() {
        return calculationPercent;
    }

    /**
     * Sets the value of the calculationPercent property.
     * 
     * @param value
     *     allowed object is
     *     {@link PercentType }
     *     
     */
    public void setCalculationPercent(PercentType value) {
        this.calculationPercent = value;
    }

    /**
     * Gets the value of the basisAmount property.
     * 
     * @return
     *     possible object is
     *     {@link AmountType }
     *     
     */
    public AmountType getBasisAmount() {
        return basisAmount;
    }

    /**
     * Sets the value of the basisAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link AmountType }
     *     
     */
    public void setBasisAmount(AmountType value) {
        this.basisAmount = value;
    }

    /**
     * Gets the value of the basisQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link QuantityType }
     *     
     */
    public QuantityType getBasisQuantity() {
        return basisQuantity;
    }

    /**
     * Sets the value of the basisQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *     
     */
    public void setBasisQuantity(QuantityType value) {
        this.basisQuantity = value;
    }

    /**
     * Gets the value of the actualAmount property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actualAmount property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActualAmount().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AmountType }
     * 
     * 
     */
    public List<AmountType> getActualAmount() {
        if (actualAmount == null) {
            actualAmount = new ArrayList<AmountType>();
        }
        return this.actualAmount;
    }

    /**
     * Gets the value of the reasonCode property.
     * 
     * @return
     *     possible object is
     *     {@link AllowanceChargeReasonCodeType }
     *     
     */
    public AllowanceChargeReasonCodeType getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the value of the reasonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link AllowanceChargeReasonCodeType }
     *     
     */
    public void setReasonCode(AllowanceChargeReasonCodeType value) {
        this.reasonCode = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setReason(TextType value) {
        this.reason = value;
    }

    /**
     * Gets the value of the categoryTradeTax property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the categoryTradeTax property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategoryTradeTax().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TradeTaxType }
     * 
     * 
     */
    public List<TradeTaxType> getCategoryTradeTax() {
        if (categoryTradeTax == null) {
            categoryTradeTax = new ArrayList<TradeTaxType>();
        }
        return this.categoryTradeTax;
    }

    public TradeAllowanceChargeType withChargeIndicator(IndicatorType value) {
        setChargeIndicator(value);
        return this;
    }

    public TradeAllowanceChargeType withSequenceNumeric(NumericType value) {
        setSequenceNumeric(value);
        return this;
    }

    public TradeAllowanceChargeType withCalculationPercent(PercentType value) {
        setCalculationPercent(value);
        return this;
    }

    public TradeAllowanceChargeType withBasisAmount(AmountType value) {
        setBasisAmount(value);
        return this;
    }

    public TradeAllowanceChargeType withBasisQuantity(QuantityType value) {
        setBasisQuantity(value);
        return this;
    }

    public TradeAllowanceChargeType withActualAmount(AmountType... values) {
        if (values!= null) {
            for (AmountType value: values) {
                getActualAmount().add(value);
            }
        }
        return this;
    }

    public TradeAllowanceChargeType withActualAmount(Collection<AmountType> values) {
        if (values!= null) {
            getActualAmount().addAll(values);
        }
        return this;
    }

    public TradeAllowanceChargeType withReasonCode(AllowanceChargeReasonCodeType value) {
        setReasonCode(value);
        return this;
    }

    public TradeAllowanceChargeType withReason(TextType value) {
        setReason(value);
        return this;
    }

    public TradeAllowanceChargeType withCategoryTradeTax(TradeTaxType... values) {
        if (values!= null) {
            for (TradeTaxType value: values) {
                getCategoryTradeTax().add(value);
            }
        }
        return this;
    }

    public TradeAllowanceChargeType withCategoryTradeTax(Collection<TradeTaxType> values) {
        if (values!= null) {
            getCategoryTradeTax().addAll(values);
        }
        return this;
    }

}
