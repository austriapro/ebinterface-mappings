//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.05 at 03:52:26 PM CEST 
//


package at.austriapro.mappings.zugferd.generated;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for NumericType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NumericType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>decimal">
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NumericType", namespace = "urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15", propOrder = {
    "value"
})
public class NumericType {

  @XmlValue
  protected BigDecimal value;

  /**
   * Gets the value of the value property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getValue() {
    return value;
  }

  /**
   * Sets the value of the value property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public NumericType withValue(BigDecimal value) {
    setValue(value);
    return this;
  }

}
