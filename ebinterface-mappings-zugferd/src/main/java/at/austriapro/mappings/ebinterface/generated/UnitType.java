//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.05 at 03:52:59 PM CEST 
//


package at.austriapro.mappings.ebinterface.generated;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for UnitType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="UnitType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.ebinterface.at/schema/4p1/>Decimal4Type">
 *       &lt;attribute ref="{http://www.ebinterface.at/schema/4p1/}Unit use="required""/>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UnitType", propOrder = {
    "value"
})
public class UnitType
    implements Serializable {

  @XmlValue
  protected BigDecimal value;
  @XmlAttribute(name = "Unit", namespace = "http://www.ebinterface.at/schema/4p1/", required = true)
  protected String unit;

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

  /**
   * Gets the value of the unit property.
   *
   * @return possible object is {@link String }
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Sets the value of the unit property.
   *
   * @param value allowed object is {@link String }
   */
  public void setUnit(String value) {
    this.unit = value;
  }

  public UnitType withValue(BigDecimal value) {
    setValue(value);
    return this;
  }

  public UnitType withUnit(String value) {
    setUnit(value);
    return this;
  }

}