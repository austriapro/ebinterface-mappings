//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.18 at 03:17:09 PM CET 
//


package at.austriapro.mappings.zugferd.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SupplyChainTradeLineItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SupplyChainTradeLineItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AssociatedDocumentLineDocument" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}DocumentLineDocumentType" minOccurs="0"/>
 *         &lt;element name="SpecifiedSupplyChainTradeAgreement" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}SupplyChainTradeAgreementType" minOccurs="0"/>
 *         &lt;element name="SpecifiedSupplyChainTradeDelivery" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}SupplyChainTradeDeliveryType" minOccurs="0"/>
 *         &lt;element name="SpecifiedSupplyChainTradeSettlement" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}SupplyChainTradeSettlementType" minOccurs="0"/>
 *         &lt;element name="SpecifiedTradeProduct" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}TradeProductType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SupplyChainTradeLineItemType", propOrder = {
    "associatedDocumentLineDocument",
    "specifiedSupplyChainTradeAgreement",
    "specifiedSupplyChainTradeDelivery",
    "specifiedSupplyChainTradeSettlement",
    "specifiedTradeProduct"
})
public class SupplyChainTradeLineItemType {

    @XmlElement(name = "AssociatedDocumentLineDocument")
    protected DocumentLineDocumentType associatedDocumentLineDocument;
    @XmlElement(name = "SpecifiedSupplyChainTradeAgreement")
    protected SupplyChainTradeAgreementType specifiedSupplyChainTradeAgreement;
    @XmlElement(name = "SpecifiedSupplyChainTradeDelivery")
    protected SupplyChainTradeDeliveryType specifiedSupplyChainTradeDelivery;
    @XmlElement(name = "SpecifiedSupplyChainTradeSettlement")
    protected SupplyChainTradeSettlementType specifiedSupplyChainTradeSettlement;
    @XmlElement(name = "SpecifiedTradeProduct")
    protected TradeProductType specifiedTradeProduct;

    /**
     * Gets the value of the associatedDocumentLineDocument property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentLineDocumentType }
     *     
     */
    public DocumentLineDocumentType getAssociatedDocumentLineDocument() {
        return associatedDocumentLineDocument;
    }

    /**
     * Sets the value of the associatedDocumentLineDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentLineDocumentType }
     *     
     */
    public void setAssociatedDocumentLineDocument(DocumentLineDocumentType value) {
        this.associatedDocumentLineDocument = value;
    }

    /**
     * Gets the value of the specifiedSupplyChainTradeAgreement property.
     * 
     * @return
     *     possible object is
     *     {@link SupplyChainTradeAgreementType }
     *     
     */
    public SupplyChainTradeAgreementType getSpecifiedSupplyChainTradeAgreement() {
        return specifiedSupplyChainTradeAgreement;
    }

    /**
     * Sets the value of the specifiedSupplyChainTradeAgreement property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupplyChainTradeAgreementType }
     *     
     */
    public void setSpecifiedSupplyChainTradeAgreement(SupplyChainTradeAgreementType value) {
        this.specifiedSupplyChainTradeAgreement = value;
    }

    /**
     * Gets the value of the specifiedSupplyChainTradeDelivery property.
     * 
     * @return
     *     possible object is
     *     {@link SupplyChainTradeDeliveryType }
     *     
     */
    public SupplyChainTradeDeliveryType getSpecifiedSupplyChainTradeDelivery() {
        return specifiedSupplyChainTradeDelivery;
    }

    /**
     * Sets the value of the specifiedSupplyChainTradeDelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupplyChainTradeDeliveryType }
     *     
     */
    public void setSpecifiedSupplyChainTradeDelivery(SupplyChainTradeDeliveryType value) {
        this.specifiedSupplyChainTradeDelivery = value;
    }

    /**
     * Gets the value of the specifiedSupplyChainTradeSettlement property.
     * 
     * @return
     *     possible object is
     *     {@link SupplyChainTradeSettlementType }
     *     
     */
    public SupplyChainTradeSettlementType getSpecifiedSupplyChainTradeSettlement() {
        return specifiedSupplyChainTradeSettlement;
    }

    /**
     * Sets the value of the specifiedSupplyChainTradeSettlement property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupplyChainTradeSettlementType }
     *     
     */
    public void setSpecifiedSupplyChainTradeSettlement(SupplyChainTradeSettlementType value) {
        this.specifiedSupplyChainTradeSettlement = value;
    }

    /**
     * Gets the value of the specifiedTradeProduct property.
     * 
     * @return
     *     possible object is
     *     {@link TradeProductType }
     *     
     */
    public TradeProductType getSpecifiedTradeProduct() {
        return specifiedTradeProduct;
    }

    /**
     * Sets the value of the specifiedTradeProduct property.
     * 
     * @param value
     *     allowed object is
     *     {@link TradeProductType }
     *     
     */
    public void setSpecifiedTradeProduct(TradeProductType value) {
        this.specifiedTradeProduct = value;
    }

    public SupplyChainTradeLineItemType withAssociatedDocumentLineDocument(DocumentLineDocumentType value) {
        setAssociatedDocumentLineDocument(value);
        return this;
    }

    public SupplyChainTradeLineItemType withSpecifiedSupplyChainTradeAgreement(SupplyChainTradeAgreementType value) {
        setSpecifiedSupplyChainTradeAgreement(value);
        return this;
    }

    public SupplyChainTradeLineItemType withSpecifiedSupplyChainTradeDelivery(SupplyChainTradeDeliveryType value) {
        setSpecifiedSupplyChainTradeDelivery(value);
        return this;
    }

    public SupplyChainTradeLineItemType withSpecifiedSupplyChainTradeSettlement(SupplyChainTradeSettlementType value) {
        setSpecifiedSupplyChainTradeSettlement(value);
        return this;
    }

    public SupplyChainTradeLineItemType withSpecifiedTradeProduct(TradeProductType value) {
        setSpecifiedTradeProduct(value);
        return this;
    }

}
