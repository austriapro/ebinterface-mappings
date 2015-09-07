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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RetrievalMethodType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RetrievalMethodType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Transforms" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="URI" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetrievalMethodType", namespace = "http://www.w3.org/2000/09/xmldsig#", propOrder = {
    "transforms"
})
@XmlRootElement(name = "RetrievalMethod", namespace = "http://www.w3.org/2000/09/xmldsig#")
public class RetrievalMethod implements Serializable {

  @XmlElement(name = "Transforms")
  protected Transforms transforms;
  @XmlAttribute(name = "URI")
  @XmlSchemaType(name = "anyURI")
  protected String uri;
  @XmlAttribute(name = "Type")
  @XmlSchemaType(name = "anyURI")
  protected String type;

  /**
   * Gets the value of the transforms property.
   *
   * @return possible object is {@link Transforms }
   */
  public Transforms getTransforms() {
    return transforms;
  }

  /**
   * Sets the value of the transforms property.
   *
   * @param value allowed object is {@link Transforms }
   */
  public void setTransforms(Transforms value) {
    this.transforms = value;
  }

  /**
   * Gets the value of the uri property.
   *
   * @return possible object is {@link String }
   */
  public String getURI() {
    return uri;
  }

  /**
   * Sets the value of the uri property.
   *
   * @param value allowed object is {@link String }
   */
  public void setURI(String value) {
    this.uri = value;
  }

  /**
   * Gets the value of the type property.
   *
   * @return possible object is {@link String }
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the value of the type property.
   *
   * @param value allowed object is {@link String }
   */
  public void setType(String value) {
    this.type = value;
  }

  public RetrievalMethod withTransforms(Transforms value) {
    setTransforms(value);
    return this;
  }

  public RetrievalMethod withURI(String value) {
    setURI(value);
    return this;
  }

  public RetrievalMethod withType(String value) {
    setType(value);
    return this;
  }

}