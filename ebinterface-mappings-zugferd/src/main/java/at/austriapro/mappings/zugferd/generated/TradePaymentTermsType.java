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
 * <p>Java class for TradePaymentTermsType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="TradePaymentTermsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Description" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}TextType"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="DueDateDateTime" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}DateTimeType"
 * minOccurs="0"/>
 *         &lt;element name="PartialPaymentAmount" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}AmountType"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ApplicableTradePaymentPenaltyTerms" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}TradePaymentPenaltyTermsType"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ApplicableTradePaymentDiscountTerms" type="{urn:un:unece:uncefact:data:standard:ReusableAggregateBusinessInformationEntity:12}TradePaymentDiscountTermsType"
 * maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TradePaymentTermsType", propOrder = {
    "description",
    "dueDateDateTime",
    "partialPaymentAmount",
    "applicableTradePaymentPenaltyTerms",
    "applicableTradePaymentDiscountTerms"
})
public class TradePaymentTermsType {

  @XmlElement(name = "Description")
  protected List<TextType> description;
  @XmlElement(name = "DueDateDateTime")
  protected DateTimeType dueDateDateTime;
  @XmlElement(name = "PartialPaymentAmount")
  protected List<AmountType> partialPaymentAmount;
  @XmlElement(name = "ApplicableTradePaymentPenaltyTerms")
  protected List<TradePaymentPenaltyTermsType> applicableTradePaymentPenaltyTerms;
  @XmlElement(name = "ApplicableTradePaymentDiscountTerms")
  protected List<TradePaymentDiscountTermsType> applicableTradePaymentDiscountTerms;

  /**
   * Gets the value of the description property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the description property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getDescription().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link TextType }
   */
  public List<TextType> getDescription() {
    if (description == null) {
      description = new ArrayList<TextType>();
    }
    return this.description;
  }

  /**
   * Gets the value of the dueDateDateTime property.
   *
   * @return possible object is {@link DateTimeType }
   */
  public DateTimeType getDueDateDateTime() {
    return dueDateDateTime;
  }

  /**
   * Sets the value of the dueDateDateTime property.
   *
   * @param value allowed object is {@link DateTimeType }
   */
  public void setDueDateDateTime(DateTimeType value) {
    this.dueDateDateTime = value;
  }

  /**
   * Gets the value of the partialPaymentAmount property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the partialPaymentAmount property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getPartialPaymentAmount().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link AmountType }
   */
  public List<AmountType> getPartialPaymentAmount() {
    if (partialPaymentAmount == null) {
      partialPaymentAmount = new ArrayList<AmountType>();
    }
    return this.partialPaymentAmount;
  }

  /**
   * Gets the value of the applicableTradePaymentPenaltyTerms property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the applicableTradePaymentPenaltyTerms property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getApplicableTradePaymentPenaltyTerms().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link
   * TradePaymentPenaltyTermsType }
   */
  public List<TradePaymentPenaltyTermsType> getApplicableTradePaymentPenaltyTerms() {
    if (applicableTradePaymentPenaltyTerms == null) {
      applicableTradePaymentPenaltyTerms = new ArrayList<TradePaymentPenaltyTermsType>();
    }
    return this.applicableTradePaymentPenaltyTerms;
  }

  /**
   * Gets the value of the applicableTradePaymentDiscountTerms property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the applicableTradePaymentDiscountTerms property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getApplicableTradePaymentDiscountTerms().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link
   * TradePaymentDiscountTermsType }
   */
  public List<TradePaymentDiscountTermsType> getApplicableTradePaymentDiscountTerms() {
    if (applicableTradePaymentDiscountTerms == null) {
      applicableTradePaymentDiscountTerms = new ArrayList<TradePaymentDiscountTermsType>();
    }
    return this.applicableTradePaymentDiscountTerms;
  }

  public TradePaymentTermsType withDescription(TextType... values) {
    if (values != null) {
      for (TextType value : values) {
        getDescription().add(value);
      }
    }
    return this;
  }

  public TradePaymentTermsType withDescription(Collection<TextType> values) {
    if (values != null) {
      getDescription().addAll(values);
    }
    return this;
  }

  public TradePaymentTermsType withDueDateDateTime(DateTimeType value) {
    setDueDateDateTime(value);
    return this;
  }

  public TradePaymentTermsType withPartialPaymentAmount(AmountType... values) {
    if (values != null) {
      for (AmountType value : values) {
        getPartialPaymentAmount().add(value);
      }
    }
    return this;
  }

  public TradePaymentTermsType withPartialPaymentAmount(Collection<AmountType> values) {
    if (values != null) {
      getPartialPaymentAmount().addAll(values);
    }
    return this;
  }

  public TradePaymentTermsType withApplicableTradePaymentPenaltyTerms(
      TradePaymentPenaltyTermsType... values) {
    if (values != null) {
      for (TradePaymentPenaltyTermsType value : values) {
        getApplicableTradePaymentPenaltyTerms().add(value);
      }
    }
    return this;
  }

  public TradePaymentTermsType withApplicableTradePaymentPenaltyTerms(
      Collection<TradePaymentPenaltyTermsType> values) {
    if (values != null) {
      getApplicableTradePaymentPenaltyTerms().addAll(values);
    }
    return this;
  }

  public TradePaymentTermsType withApplicableTradePaymentDiscountTerms(
      TradePaymentDiscountTermsType... values) {
    if (values != null) {
      for (TradePaymentDiscountTermsType value : values) {
        getApplicableTradePaymentDiscountTerms().add(value);
      }
    }
    return this;
  }

  public TradePaymentTermsType withApplicableTradePaymentDiscountTerms(
      Collection<TradePaymentDiscountTermsType> values) {
    if (values != null) {
      getApplicableTradePaymentDiscountTerms().addAll(values);
    }
    return this;
  }

}