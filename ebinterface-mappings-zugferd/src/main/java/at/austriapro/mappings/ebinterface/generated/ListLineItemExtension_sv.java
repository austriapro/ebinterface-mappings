//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.05 at 03:52:59 PM CEST 
//


package at.austriapro.mappings.ebinterface.generated;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ListLineItemExtensionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ListLineItemExtensionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p1/extensions/sv}BeneficiarySocialInsuranceNumber"
 * minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListLineItemExtensionType", namespace = "http://www.ebinterface.at/schema/4p1/extensions/sv", propOrder = {
    "beneficiarySocialInsuranceNumber"
})
@XmlRootElement(name = "ListLineItemExtension", namespace = "http://www.ebinterface.at/schema/4p1/extensions/sv")
public class ListLineItemExtension_sv
    implements Serializable {

  @XmlElement(name = "BeneficiarySocialInsuranceNumber")
  protected String beneficiarySocialInsuranceNumber;

  /**
   * Gets the value of the beneficiarySocialInsuranceNumber property.
   *
   * @return possible object is {@link String }
   */
  public String getBeneficiarySocialInsuranceNumber() {
    return beneficiarySocialInsuranceNumber;
  }

  /**
   * Sets the value of the beneficiarySocialInsuranceNumber property.
   *
   * @param value allowed object is {@link String }
   */
  public void setBeneficiarySocialInsuranceNumber(String value) {
    this.beneficiarySocialInsuranceNumber = value;
  }

  public ListLineItemExtension_sv withBeneficiarySocialInsuranceNumber(String value) {
    setBeneficiarySocialInsuranceNumber(value);
    return this;
  }

}
