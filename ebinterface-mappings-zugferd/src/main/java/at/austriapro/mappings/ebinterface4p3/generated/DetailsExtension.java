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
 * <p>Java class for DetailsExtensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DetailsExtensionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p3/extensions/sv}DetailsExtension" minOccurs="0"/>
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
@XmlType(name = "DetailsExtensionType", namespace = "http://www.ebinterface.at/schema/4p3/extensions/ext", propOrder = {
    "detailsExtension",
    "custom"
})
@XmlRootElement(name = "DetailsExtension", namespace = "http://www.ebinterface.at/schema/4p3/extensions/ext")
public class DetailsExtension
    implements Serializable
{

    @XmlElement(name = "DetailsExtension", namespace = "http://www.ebinterface.at/schema/4p3/extensions/sv")
    protected DetailsExtension_sv detailsExtension;
    @XmlElement(name = "Custom")
    protected Custom custom;

    /**
     * Gets the value of the detailsExtension property.
     * 
     * @return
     *     possible object is
     *     {@link DetailsExtension_sv }
     *     
     */
    public DetailsExtension_sv getDetailsExtension() {
        return detailsExtension;
    }

    /**
     * Sets the value of the detailsExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailsExtension_sv }
     *     
     */
    public void setDetailsExtension(DetailsExtension_sv value) {
        this.detailsExtension = value;
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

    public DetailsExtension withDetailsExtension(DetailsExtension_sv value) {
        setDetailsExtension(value);
        return this;
    }

    public DetailsExtension withCustom(Custom value) {
        setCustom(value);
        return this;
    }

}
