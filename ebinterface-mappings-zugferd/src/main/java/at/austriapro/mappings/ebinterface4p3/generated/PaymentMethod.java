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
 * <p>Java class for PaymentMethodType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentMethodType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p3/}Comment" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element ref="{http://www.ebinterface.at/schema/4p3/}NoPayment"/>
 *           &lt;element ref="{http://www.ebinterface.at/schema/4p3/}DirectDebit"/>
 *           &lt;element ref="{http://www.ebinterface.at/schema/4p3/}SEPADirectDebit"/>
 *           &lt;element ref="{http://www.ebinterface.at/schema/4p3/}UniversalBankTransaction"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.ebinterface.at/schema/4p3/extensions/ext}PaymentMethodExtension" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentMethodType", propOrder = {
    "comment",
    "universalBankTransaction",
    "sepaDirectDebit",
    "directDebit",
    "noPayment",
    "paymentMethodExtension"
})
@XmlRootElement(name = "PaymentMethod")
public class PaymentMethod
    implements Serializable
{

    @XmlElement(name = "Comment")
    protected String comment;
    @XmlElement(name = "UniversalBankTransaction")
    protected UniversalBankTransaction universalBankTransaction;
    @XmlElement(name = "SEPADirectDebit")
    protected SEPADirectDebit sepaDirectDebit;
    @XmlElement(name = "DirectDebit")
    protected DirectDebit directDebit;
    @XmlElement(name = "NoPayment")
    protected NoPayment noPayment;
    @XmlElement(name = "PaymentMethodExtension", namespace = "http://www.ebinterface.at/schema/4p3/extensions/ext")
    protected PaymentMethodExtension paymentMethodExtension;

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the universalBankTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link UniversalBankTransaction }
     *     
     */
    public UniversalBankTransaction getUniversalBankTransaction() {
        return universalBankTransaction;
    }

    /**
     * Sets the value of the universalBankTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link UniversalBankTransaction }
     *     
     */
    public void setUniversalBankTransaction(UniversalBankTransaction value) {
        this.universalBankTransaction = value;
    }

    /**
     * Gets the value of the sepaDirectDebit property.
     * 
     * @return
     *     possible object is
     *     {@link SEPADirectDebit }
     *     
     */
    public SEPADirectDebit getSEPADirectDebit() {
        return sepaDirectDebit;
    }

    /**
     * Sets the value of the sepaDirectDebit property.
     * 
     * @param value
     *     allowed object is
     *     {@link SEPADirectDebit }
     *     
     */
    public void setSEPADirectDebit(SEPADirectDebit value) {
        this.sepaDirectDebit = value;
    }

    /**
     * Gets the value of the directDebit property.
     * 
     * @return
     *     possible object is
     *     {@link DirectDebit }
     *     
     */
    public DirectDebit getDirectDebit() {
        return directDebit;
    }

    /**
     * Sets the value of the directDebit property.
     * 
     * @param value
     *     allowed object is
     *     {@link DirectDebit }
     *     
     */
    public void setDirectDebit(DirectDebit value) {
        this.directDebit = value;
    }

    /**
     * Gets the value of the noPayment property.
     * 
     * @return
     *     possible object is
     *     {@link NoPayment }
     *     
     */
    public NoPayment getNoPayment() {
        return noPayment;
    }

    /**
     * Sets the value of the noPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link NoPayment }
     *     
     */
    public void setNoPayment(NoPayment value) {
        this.noPayment = value;
    }

    /**
     * Gets the value of the paymentMethodExtension property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentMethodExtension }
     *     
     */
    public PaymentMethodExtension getPaymentMethodExtension() {
        return paymentMethodExtension;
    }

    /**
     * Sets the value of the paymentMethodExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentMethodExtension }
     *     
     */
    public void setPaymentMethodExtension(PaymentMethodExtension value) {
        this.paymentMethodExtension = value;
    }

    public PaymentMethod withComment(String value) {
        setComment(value);
        return this;
    }

    public PaymentMethod withUniversalBankTransaction(UniversalBankTransaction value) {
        setUniversalBankTransaction(value);
        return this;
    }

    public PaymentMethod withSEPADirectDebit(SEPADirectDebit value) {
        setSEPADirectDebit(value);
        return this;
    }

    public PaymentMethod withDirectDebit(DirectDebit value) {
        setDirectDebit(value);
        return this;
    }

    public PaymentMethod withNoPayment(NoPayment value) {
        setNoPayment(value);
        return this;
    }

    public PaymentMethod withPaymentMethodExtension(PaymentMethodExtension value) {
        setPaymentMethodExtension(value);
        return this;
    }

}
