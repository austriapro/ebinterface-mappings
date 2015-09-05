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
 * <p>Java class for ProductCharacteristicType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ProductCharacteristicType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TypeCode" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}CodeType"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Description" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}TextType"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ValueMeasure" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}MeasureType"
 * maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Value" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}TextType"
 * maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProductCharacteristicType", propOrder = {
    "typeCode",
    "description",
    "valueMeasure",
    "value"
})
public class ProductCharacteristicType {

  @XmlElement(name = "TypeCode")
  protected List<CodeType> typeCode;
  @XmlElement(name = "Description")
  protected List<TextType> description;
  @XmlElement(name = "ValueMeasure")
  protected List<MeasureType> valueMeasure;
  @XmlElement(name = "Value")
  protected List<TextType> value;

  /**
   * Gets the value of the typeCode property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the typeCode property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getTypeCode().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link CodeType }
   */
  public List<CodeType> getTypeCode() {
    if (typeCode == null) {
      typeCode = new ArrayList<CodeType>();
    }
    return this.typeCode;
  }

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
   * Gets the value of the valueMeasure property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the valueMeasure property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getValueMeasure().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link MeasureType }
   */
  public List<MeasureType> getValueMeasure() {
    if (valueMeasure == null) {
      valueMeasure = new ArrayList<MeasureType>();
    }
    return this.valueMeasure;
  }

  /**
   * Gets the value of the value property.
   *
   * <p> This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the value property.
   *
   * <p> For example, to add a new item, do as follows:
   * <pre>
   *    getValue().add(newItem);
   * </pre>
   *
   *
   * <p> Objects of the following type(s) are allowed in the list {@link TextType }
   */
  public List<TextType> getValue() {
    if (value == null) {
      value = new ArrayList<TextType>();
    }
    return this.value;
  }

  public ProductCharacteristicType withTypeCode(CodeType... values) {
    if (values != null) {
      for (CodeType value : values) {
        getTypeCode().add(value);
      }
    }
    return this;
  }

  public ProductCharacteristicType withTypeCode(Collection<CodeType> values) {
    if (values != null) {
      getTypeCode().addAll(values);
    }
    return this;
  }

  public ProductCharacteristicType withDescription(TextType... values) {
    if (values != null) {
      for (TextType value : values) {
        getDescription().add(value);
      }
    }
    return this;
  }

  public ProductCharacteristicType withDescription(Collection<TextType> values) {
    if (values != null) {
      getDescription().addAll(values);
    }
    return this;
  }

  public ProductCharacteristicType withValueMeasure(MeasureType... values) {
    if (values != null) {
      for (MeasureType value : values) {
        getValueMeasure().add(value);
      }
    }
    return this;
  }

  public ProductCharacteristicType withValueMeasure(Collection<MeasureType> values) {
    if (values != null) {
      getValueMeasure().addAll(values);
    }
    return this;
  }

  public ProductCharacteristicType withValue(TextType... values) {
    if (values != null) {
      for (TextType value : values) {
        getValue().add(value);
      }
    }
    return this;
  }

  public ProductCharacteristicType withValue(Collection<TextType> values) {
    if (values != null) {
      getValue().addAll(values);
    }
    return this;
  }

}
