//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.09.05 at 03:52:26 PM CEST 
//


package at.austriapro.mappings.zugferd.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DebtorFinancialInstitutionType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DebtorFinancialInstitutionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BICID" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}IDType"
 * minOccurs="0"/>
 *         &lt;element name="GermanBankleitzahlID" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}IDType"
 * minOccurs="0"/>
 *         &lt;element name="Name" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}TextType"
 * minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DebtorFinancialInstitutionType", propOrder = {
    "bicid",
    "germanBankleitzahlID",
    "name"
})
public class DebtorFinancialInstitutionType {

  @XmlElement(name = "BICID")
  protected IDType bicid;
  @XmlElement(name = "GermanBankleitzahlID")
  protected IDType germanBankleitzahlID;
  @XmlElement(name = "Name")
  protected TextType name;

  /**
   * Gets the value of the bicid property.
   *
   * @return possible object is {@link IDType }
   */
  public IDType getBICID() {
    return bicid;
  }

  /**
   * Sets the value of the bicid property.
   *
   * @param value allowed object is {@link IDType }
   */
  public void setBICID(IDType value) {
    this.bicid = value;
  }

  /**
   * Gets the value of the germanBankleitzahlID property.
   *
   * @return possible object is {@link IDType }
   */
  public IDType getGermanBankleitzahlID() {
    return germanBankleitzahlID;
  }

  /**
   * Sets the value of the germanBankleitzahlID property.
   *
   * @param value allowed object is {@link IDType }
   */
  public void setGermanBankleitzahlID(IDType value) {
    this.germanBankleitzahlID = value;
  }

  /**
   * Gets the value of the name property.
   *
   * @return possible object is {@link TextType }
   */
  public TextType getName() {
    return name;
  }

  /**
   * Sets the value of the name property.
   *
   * @param value allowed object is {@link TextType }
   */
  public void setName(TextType value) {
    this.name = value;
  }

  public DebtorFinancialInstitutionType withBICID(IDType value) {
    setBICID(value);
    return this;
  }

  public DebtorFinancialInstitutionType withGermanBankleitzahlID(IDType value) {
    setGermanBankleitzahlID(value);
    return this;
  }

  public DebtorFinancialInstitutionType withName(TextType value) {
    setName(value);
    return this;
  }

}
