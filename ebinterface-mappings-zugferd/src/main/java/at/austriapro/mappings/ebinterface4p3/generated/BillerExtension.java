//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.01.20 at 05:47:04 PM CET 
//


package at.austriapro.mappings.ebinterface4p3.generated;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BillerExtensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BillerExtensionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p3/extensions/sv}BillerExtension" minOccurs="0"/>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p3/extensions/ext}Custom" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BillerExtensionType", namespace = "http://www.ebinterface.at/schema/4p3/extensions/ext", propOrder = {
    "billerExtension",
    "custom"
})
@XmlRootElement(name = "BillerExtension", namespace = "http://www.ebinterface.at/schema/4p3/extensions/ext")
public class BillerExtension
    implements Serializable
{

    @XmlElement(name = "BillerExtension", namespace = "http://www.ebinterface.at/schema/4p3/extensions/sv")
    protected BillerExtension_sv billerExtension;
    @XmlElement(name = "Custom")
    protected Custom custom;

    /**
     * Gets the value of the billerExtension property.
     * 
     * @return
     *     possible object is
     *     {@link BillerExtension_sv }
     *     
     */
    public BillerExtension_sv getBillerExtension() {
        return billerExtension;
    }

    /**
     * Sets the value of the billerExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link BillerExtension_sv }
     *     
     */
    public void setBillerExtension(BillerExtension_sv value) {
        this.billerExtension = value;
    }

    /**
     * Gets the value of the custom property.
     * 
     * @return
     *     possible object is
     *     {@link Custom }
     *     
     */
    public Custom getCustom() {
        return custom;
    }

    /**
     * Sets the value of the custom property.
     * 
     * @param value
     *     allowed object is
     *     {@link Custom }
     *     
     */
    public void setCustom(Custom value) {
        this.custom = value;
    }

    public BillerExtension withBillerExtension(BillerExtension_sv value) {
        setBillerExtension(value);
        return this;
    }

    public BillerExtension withCustom(Custom value) {
        setCustom(value);
        return this;
    }

}
