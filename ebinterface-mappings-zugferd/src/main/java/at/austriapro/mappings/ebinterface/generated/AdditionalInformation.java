//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.05 at 03:52:59 PM CEST 
//


package at.austriapro.mappings.ebinterface.generated;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdditionalInformationType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AdditionalInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}SerialNumber"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}ChargeNumber"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}Classification"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}AlternativeQuantity"
 * minOccurs="0"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}Size" minOccurs="0"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}Weight" minOccurs="0"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}Boxes" minOccurs="0"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}Color" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalInformationType", propOrder = {
    "serialNumbers",
    "chargeNumbers",
    "classifications",
    "alternativeQuantity",
    "size",
    "weight",
    "boxes",
    "color"
})
@XmlRootElement(name = "AdditionalInformation")
public class AdditionalInformation
    implements Serializable {

  @XmlElement(name = "SerialNumber")
  protected List<String> serialNumbers;
  @XmlElement(name = "ChargeNumber")
  protected List<String> chargeNumbers;
  @XmlElement(name = "Classification")
  protected List<Classification> classifications;
  @XmlElement(name = "AlternativeQuantity")
  protected UnitType alternativeQuantity;
  @XmlElement(name = "Size")
  protected String size;
  @XmlElement(name = "Weight")
  protected UnitType weight;
  @XmlElement(name = "Boxes")
  @XmlSchemaType(name = "positiveInteger")
  protected BigInteger boxes;
  @XmlElement(name = "Color")
  protected String color;

  /**
   * Gets the value of the serialNumbers property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the serialNumbers property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getSerialNumbers().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getSerialNumbers() {
    if (serialNumbers == null) {
      serialNumbers = new ArrayList<String>();
    }
    return this.serialNumbers;
  }

  /**
   * Gets the value of the chargeNumbers property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the chargeNumbers property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getChargeNumbers().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getChargeNumbers() {
    if (chargeNumbers == null) {
      chargeNumbers = new ArrayList<String>();
    }
    return this.chargeNumbers;
  }

  /**
   * Gets the value of the classifications property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the classifications property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getClassifications().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link Classification }
   */
  public List<Classification> getClassifications() {
    if (classifications == null) {
      classifications = new ArrayList<Classification>();
    }
    return this.classifications;
  }

  /**
   * Gets the value of the alternativeQuantity property.
   *
   * @return possible object is {@link UnitType }
   */
  public UnitType getAlternativeQuantity() {
    return alternativeQuantity;
  }

  /**
   * Sets the value of the alternativeQuantity property.
   *
   * @param value allowed object is {@link UnitType }
   */
  public void setAlternativeQuantity(UnitType value) {
    this.alternativeQuantity = value;
  }

  /**
   * Gets the value of the size property.
   *
   * @return possible object is {@link String }
   */
  public String getSize() {
    return size;
  }

  /**
   * Sets the value of the size property.
   *
   * @param value allowed object is {@link String }
   */
  public void setSize(String value) {
    this.size = value;
  }

  /**
   * Gets the value of the weight property.
   *
   * @return possible object is {@link UnitType }
   */
  public UnitType getWeight() {
    return weight;
  }

  /**
   * Sets the value of the weight property.
   *
   * @param value allowed object is {@link UnitType }
   */
  public void setWeight(UnitType value) {
    this.weight = value;
  }

  /**
   * Gets the value of the boxes property.
   *
   * @return possible object is {@link BigInteger }
   */
  public BigInteger getBoxes() {
    return boxes;
  }

  /**
   * Sets the value of the boxes property.
   *
   * @param value allowed object is {@link BigInteger }
   */
  public void setBoxes(BigInteger value) {
    this.boxes = value;
  }

  /**
   * Gets the value of the color property.
   *
   * @return possible object is {@link String }
   */
  public String getColor() {
    return color;
  }

  /**
   * Sets the value of the color property.
   *
   * @param value allowed object is {@link String }
   */
  public void setColor(String value) {
    this.color = value;
  }

  public AdditionalInformation withSerialNumbers(String... values) {
    if (values != null) {
      for (String value : values) {
        getSerialNumbers().add(value);
      }
    }
    return this;
  }

  public AdditionalInformation withSerialNumbers(Collection<String> values) {
    if (values != null) {
      getSerialNumbers().addAll(values);
    }
    return this;
  }

  public AdditionalInformation withChargeNumbers(String... values) {
    if (values != null) {
      for (String value : values) {
        getChargeNumbers().add(value);
      }
    }
    return this;
  }

  public AdditionalInformation withChargeNumbers(Collection<String> values) {
    if (values != null) {
      getChargeNumbers().addAll(values);
    }
    return this;
  }

  public AdditionalInformation withClassifications(Classification... values) {
    if (values != null) {
      for (Classification value : values) {
        getClassifications().add(value);
      }
    }
    return this;
  }

  public AdditionalInformation withClassifications(Collection<Classification> values) {
    if (values != null) {
      getClassifications().addAll(values);
    }
    return this;
  }

  public AdditionalInformation withAlternativeQuantity(UnitType value) {
    setAlternativeQuantity(value);
    return this;
  }

  public AdditionalInformation withSize(String value) {
    setSize(value);
    return this;
  }

  public AdditionalInformation withWeight(UnitType value) {
    setWeight(value);
    return this;
  }

  public AdditionalInformation withBoxes(BigInteger value) {
    setBoxes(value);
    return this;
  }

  public AdditionalInformation withColor(String value) {
    setColor(value);
    return this;
  }

}
