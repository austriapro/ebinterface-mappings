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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OtherTaxType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="OtherTaxType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}Comment"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}Amount"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OtherTaxType", propOrder = {
    "comment",
    "amount"
})
@XmlRootElement(name = "OtherTax")
public class OtherTax
    implements Serializable {

  @XmlElement(name = "Comment", required = true)
  protected String comment;
  @XmlElement(name = "Amount", required = true)
  protected BigDecimal amount;

  /**
   * Gets the value of the comment property.
   *
   * @return possible object is {@link String }
   */
  public String getComment() {
    return comment;
  }

  /**
   * Sets the value of the comment property.
   *
   * @param value allowed object is {@link String }
   */
  public void setComment(String value) {
    this.comment = value;
  }

  /**
   * Gets the value of the amount property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getAmount() {
    return amount;
  }

  /**
   * Sets the value of the amount property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setAmount(BigDecimal value) {
    this.amount = value;
  }

  public OtherTax withComment(String value) {
    setComment(value);
    return this;
  }

  public OtherTax withAmount(BigDecimal value) {
    setAmount(value);
    return this;
  }

}
