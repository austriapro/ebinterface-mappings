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
 * <p>Java class for BelowTheLineItemType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BelowTheLineItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}Description"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}LineItemAmount"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/}Reason" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BelowTheLineItemType", propOrder = {
    "description",
    "lineItemAmount",
    "reason"
})
@XmlRootElement(name = "BelowTheLineItem")
public class BelowTheLineItem
    implements Serializable {

  @XmlElement(name = "Description", required = true)
  protected String description;
  @XmlElement(name = "LineItemAmount", required = true)
  protected BigDecimal lineItemAmount;
  @XmlElement(name = "Reason")
  protected Reason reason;

  /**
   * Gets the value of the description property.
   *
   * @return possible object is {@link String }
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the value of the description property.
   *
   * @param value allowed object is {@link String }
   */
  public void setDescription(String value) {
    this.description = value;
  }

  /**
   * Gets the value of the lineItemAmount property.
   *
   * @return possible object is {@link BigDecimal }
   */
  public BigDecimal getLineItemAmount() {
    return lineItemAmount;
  }

  /**
   * Sets the value of the lineItemAmount property.
   *
   * @param value allowed object is {@link BigDecimal }
   */
  public void setLineItemAmount(BigDecimal value) {
    this.lineItemAmount = value;
  }

  /**
   * Gets the value of the reason property.
   *
   * @return possible object is {@link Reason }
   */
  public Reason getReason() {
    return reason;
  }

  /**
   * Sets the value of the reason property.
   *
   * @param value allowed object is {@link Reason }
   */
  public void setReason(Reason value) {
    this.reason = value;
  }

  public BelowTheLineItem withDescription(String value) {
    setDescription(value);
    return this;
  }

  public BelowTheLineItem withLineItemAmount(BigDecimal value) {
    setLineItemAmount(value);
    return this;
  }

  public BelowTheLineItem withReason(Reason value) {
    setReason(value);
    return this;
  }

}
