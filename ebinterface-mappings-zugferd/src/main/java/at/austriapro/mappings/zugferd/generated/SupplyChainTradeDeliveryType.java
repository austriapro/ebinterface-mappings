//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.05 at 03:52:26 PM CEST 
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
 * <p>Java class for SupplyChainTradeDeliveryType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SupplyChainTradeDeliveryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BilledQuantity" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}QuantityType"
 * minOccurs="0"/>
 *         &lt;element name="ChargeFreeQuantity" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}QuantityType"
 * minOccurs="0"/>
 *         &lt;element name="PackageQuantity" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}QuantityType"
 * minOccurs="0"/>
 *         &lt;element name="RelatedSupplyChainConsignment" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}SupplyChainConsignmentType"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ShipToTradeParty" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}TradePartyType"
 * minOccurs="0"/>
 *         &lt;element name="UltimateShipToTradeParty" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}TradePartyType"
 * minOccurs="0"/>
 *         &lt;element name="ShipFromTradeParty" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}TradePartyType"
 * minOccurs="0"/>
 *         &lt;element name="ActualDeliverySupplyChainEvent" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}SupplyChainEventType"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DespatchAdviceReferencedDocument" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}ReferencedDocumentType"
 * minOccurs="0"/>
 *         &lt;element name="ReceivingAdviceReferencedDocument" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}ReferencedDocumentType"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DeliveryNoteReferencedDocument" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}ReferencedDocumentType"
 * minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SupplyChainTradeDeliveryType", propOrder = {
    "billedQuantity",
    "chargeFreeQuantity",
    "packageQuantity",
    "relatedSupplyChainConsignment",
    "shipToTradeParty",
    "ultimateShipToTradeParty",
    "shipFromTradeParty",
    "actualDeliverySupplyChainEvent",
    "despatchAdviceReferencedDocument",
    "receivingAdviceReferencedDocument",
    "deliveryNoteReferencedDocument"
})
public class SupplyChainTradeDeliveryType {

  @XmlElement(name = "BilledQuantity")
  protected QuantityType billedQuantity;
  @XmlElement(name = "ChargeFreeQuantity")
  protected QuantityType chargeFreeQuantity;
  @XmlElement(name = "PackageQuantity")
  protected QuantityType packageQuantity;
  @XmlElement(name = "RelatedSupplyChainConsignment")
  protected List<SupplyChainConsignmentType> relatedSupplyChainConsignment;
  @XmlElement(name = "ShipToTradeParty")
  protected TradePartyType shipToTradeParty;
  @XmlElement(name = "UltimateShipToTradeParty")
  protected TradePartyType ultimateShipToTradeParty;
  @XmlElement(name = "ShipFromTradeParty")
  protected TradePartyType shipFromTradeParty;
  @XmlElement(name = "ActualDeliverySupplyChainEvent")
  protected List<SupplyChainEventType> actualDeliverySupplyChainEvent;
  @XmlElement(name = "DespatchAdviceReferencedDocument")
  protected ReferencedDocumentType despatchAdviceReferencedDocument;
  @XmlElement(name = "ReceivingAdviceReferencedDocument")
  protected List<ReferencedDocumentType> receivingAdviceReferencedDocument;
  @XmlElement(name = "DeliveryNoteReferencedDocument")
  protected ReferencedDocumentType deliveryNoteReferencedDocument;

  /**
   * Gets the value of the billedQuantity property.
   *
   * @return possible object is {@link QuantityType }
   */
  public QuantityType getBilledQuantity() {
    return billedQuantity;
  }

  /**
   * Sets the value of the billedQuantity property.
   *
   * @param value allowed object is {@link QuantityType }
   */
  public void setBilledQuantity(QuantityType value) {
    this.billedQuantity = value;
  }

  /**
   * Gets the value of the chargeFreeQuantity property.
   *
   * @return possible object is {@link QuantityType }
   */
  public QuantityType getChargeFreeQuantity() {
    return chargeFreeQuantity;
  }

  /**
   * Sets the value of the chargeFreeQuantity property.
   *
   * @param value allowed object is {@link QuantityType }
   */
  public void setChargeFreeQuantity(QuantityType value) {
    this.chargeFreeQuantity = value;
  }

  /**
   * Gets the value of the packageQuantity property.
   *
   * @return possible object is {@link QuantityType }
   */
  public QuantityType getPackageQuantity() {
    return packageQuantity;
  }

  /**
   * Sets the value of the packageQuantity property.
   *
   * @param value allowed object is {@link QuantityType }
   */
  public void setPackageQuantity(QuantityType value) {
    this.packageQuantity = value;
  }

  /**
   * Gets the value of the relatedSupplyChainConsignment property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the relatedSupplyChainConsignment property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getRelatedSupplyChainConsignment().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link SupplyChainConsignmentType
   * }
   */
  public List<SupplyChainConsignmentType> getRelatedSupplyChainConsignment() {
    if (relatedSupplyChainConsignment == null) {
      relatedSupplyChainConsignment = new ArrayList<SupplyChainConsignmentType>();
    }
    return this.relatedSupplyChainConsignment;
  }

  /**
   * Gets the value of the shipToTradeParty property.
   *
   * @return possible object is {@link TradePartyType }
   */
  public TradePartyType getShipToTradeParty() {
    return shipToTradeParty;
  }

  /**
   * Sets the value of the shipToTradeParty property.
   *
   * @param value allowed object is {@link TradePartyType }
   */
  public void setShipToTradeParty(TradePartyType value) {
    this.shipToTradeParty = value;
  }

  /**
   * Gets the value of the ultimateShipToTradeParty property.
   *
   * @return possible object is {@link TradePartyType }
   */
  public TradePartyType getUltimateShipToTradeParty() {
    return ultimateShipToTradeParty;
  }

  /**
   * Sets the value of the ultimateShipToTradeParty property.
   *
   * @param value allowed object is {@link TradePartyType }
   */
  public void setUltimateShipToTradeParty(TradePartyType value) {
    this.ultimateShipToTradeParty = value;
  }

  /**
   * Gets the value of the shipFromTradeParty property.
   *
   * @return possible object is {@link TradePartyType }
   */
  public TradePartyType getShipFromTradeParty() {
    return shipFromTradeParty;
  }

  /**
   * Sets the value of the shipFromTradeParty property.
   *
   * @param value allowed object is {@link TradePartyType }
   */
  public void setShipFromTradeParty(TradePartyType value) {
    this.shipFromTradeParty = value;
  }

  /**
   * Gets the value of the actualDeliverySupplyChainEvent property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the actualDeliverySupplyChainEvent property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getActualDeliverySupplyChainEvent().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link SupplyChainEventType }
   */
  public List<SupplyChainEventType> getActualDeliverySupplyChainEvent() {
    if (actualDeliverySupplyChainEvent == null) {
      actualDeliverySupplyChainEvent = new ArrayList<SupplyChainEventType>();
    }
    return this.actualDeliverySupplyChainEvent;
  }

  /**
   * Gets the value of the despatchAdviceReferencedDocument property.
   *
   * @return possible object is {@link ReferencedDocumentType }
   */
  public ReferencedDocumentType getDespatchAdviceReferencedDocument() {
    return despatchAdviceReferencedDocument;
  }

  /**
   * Sets the value of the despatchAdviceReferencedDocument property.
   *
   * @param value allowed object is {@link ReferencedDocumentType }
   */
  public void setDespatchAdviceReferencedDocument(ReferencedDocumentType value) {
    this.despatchAdviceReferencedDocument = value;
  }

  /**
   * Gets the value of the receivingAdviceReferencedDocument property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the receivingAdviceReferencedDocument property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getReceivingAdviceReferencedDocument().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link ReferencedDocumentType }
   */
  public List<ReferencedDocumentType> getReceivingAdviceReferencedDocument() {
    if (receivingAdviceReferencedDocument == null) {
      receivingAdviceReferencedDocument = new ArrayList<ReferencedDocumentType>();
    }
    return this.receivingAdviceReferencedDocument;
  }

  /**
   * Gets the value of the deliveryNoteReferencedDocument property.
   *
   * @return possible object is {@link ReferencedDocumentType }
   */
  public ReferencedDocumentType getDeliveryNoteReferencedDocument() {
    return deliveryNoteReferencedDocument;
  }

  /**
   * Sets the value of the deliveryNoteReferencedDocument property.
   *
   * @param value allowed object is {@link ReferencedDocumentType }
   */
  public void setDeliveryNoteReferencedDocument(ReferencedDocumentType value) {
    this.deliveryNoteReferencedDocument = value;
  }

  public SupplyChainTradeDeliveryType withBilledQuantity(QuantityType value) {
    setBilledQuantity(value);
    return this;
  }

  public SupplyChainTradeDeliveryType withChargeFreeQuantity(QuantityType value) {
    setChargeFreeQuantity(value);
    return this;
  }

  public SupplyChainTradeDeliveryType withPackageQuantity(QuantityType value) {
    setPackageQuantity(value);
    return this;
  }

  public SupplyChainTradeDeliveryType withRelatedSupplyChainConsignment(
      SupplyChainConsignmentType... values) {
    if (values != null) {
      for (SupplyChainConsignmentType value : values) {
        getRelatedSupplyChainConsignment().add(value);
      }
    }
    return this;
  }

  public SupplyChainTradeDeliveryType withRelatedSupplyChainConsignment(
      Collection<SupplyChainConsignmentType> values) {
    if (values != null) {
      getRelatedSupplyChainConsignment().addAll(values);
    }
    return this;
  }

  public SupplyChainTradeDeliveryType withShipToTradeParty(TradePartyType value) {
    setShipToTradeParty(value);
    return this;
  }

  public SupplyChainTradeDeliveryType withUltimateShipToTradeParty(TradePartyType value) {
    setUltimateShipToTradeParty(value);
    return this;
  }

  public SupplyChainTradeDeliveryType withShipFromTradeParty(TradePartyType value) {
    setShipFromTradeParty(value);
    return this;
  }

  public SupplyChainTradeDeliveryType withActualDeliverySupplyChainEvent(
      SupplyChainEventType... values) {
    if (values != null) {
      for (SupplyChainEventType value : values) {
        getActualDeliverySupplyChainEvent().add(value);
      }
    }
    return this;
  }

  public SupplyChainTradeDeliveryType withActualDeliverySupplyChainEvent(
      Collection<SupplyChainEventType> values) {
    if (values != null) {
      getActualDeliverySupplyChainEvent().addAll(values);
    }
    return this;
  }

  public SupplyChainTradeDeliveryType withDespatchAdviceReferencedDocument(
      ReferencedDocumentType value) {
    setDespatchAdviceReferencedDocument(value);
    return this;
  }

  public SupplyChainTradeDeliveryType withReceivingAdviceReferencedDocument(
      ReferencedDocumentType... values) {
    if (values != null) {
      for (ReferencedDocumentType value : values) {
        getReceivingAdviceReferencedDocument().add(value);
      }
    }
    return this;
  }

  public SupplyChainTradeDeliveryType withReceivingAdviceReferencedDocument(
      Collection<ReferencedDocumentType> values) {
    if (values != null) {
      getReceivingAdviceReferencedDocument().addAll(values);
    }
    return this;
  }

  public SupplyChainTradeDeliveryType withDeliveryNoteReferencedDocument(
      ReferencedDocumentType value) {
    setDeliveryNoteReferencedDocument(value);
    return this;
  }

}