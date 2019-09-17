//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.18 at 03:17:09 PM CET 
//


package at.austriapro.mappings.zugferd.generated;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReferencedDocumentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReferencedDocumentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IssueDateTime" type="{urn:un:unece:uncefact:data:standard:QualifiedDataType:12}DateMandatoryDateTimeType" minOccurs="0"/>
 *         &lt;element name="LineID" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}IDType" minOccurs="0"/>
 *         &lt;element name="TypeCode" type="{urn:un:unece:uncefact:data:standard:QualifiedDataType:12}DocumentCodeType" minOccurs="0"/>
 *         &lt;element name="ID" type="{urn:un:unece:uncefact:data:standard:UnqualifiedDataType:15}IDType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ReferenceTypeCode" type="{urn:un:unece:uncefact:data:standard:QualifiedDataType:12}ReferenceCodeType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReferencedDocumentType", propOrder = {
    "issueDateTime",
    "lineID",
    "typeCode",
    "id",
    "referenceTypeCode"
})
public class ReferencedDocumentType {

    @XmlElement(name = "IssueDateTime")
    protected String issueDateTime;
    @XmlElement(name = "LineID")
    protected IDType lineID;
    @XmlElement(name = "TypeCode")
    protected DocumentCodeType typeCode;
    @XmlElement(name = "ID")
    protected List<IDType> id;
    @XmlElement(name = "ReferenceTypeCode")
    protected ReferenceCodeType referenceTypeCode;

    /**
     * Gets the value of the issueDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssueDateTime() {
        return issueDateTime;
    }

    /**
     * Sets the value of the issueDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssueDateTime(String value) {
        this.issueDateTime = value;
    }

    /**
     * Gets the value of the lineID property.
     * 
     * @return
     *     possible object is
     *     {@link IDType }
     *     
     */
    public IDType getLineID() {
        return lineID;
    }

    /**
     * Sets the value of the lineID property.
     * 
     * @param value
     *     allowed object is
     *     {@link IDType }
     *     
     */
    public void setLineID(IDType value) {
        this.lineID = value;
    }

    /**
     * Gets the value of the typeCode property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentCodeType }
     *     
     */
    public DocumentCodeType getTypeCode() {
        return typeCode;
    }

    /**
     * Sets the value of the typeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentCodeType }
     *     
     */
    public void setTypeCode(DocumentCodeType value) {
        this.typeCode = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the id property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IDType }
     * 
     * 
     */
    public List<IDType> getID() {
        if (id == null) {
            id = new ArrayList<IDType>();
        }
        return this.id;
    }

    /**
     * Gets the value of the referenceTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link ReferenceCodeType }
     *     
     */
    public ReferenceCodeType getReferenceTypeCode() {
        return referenceTypeCode;
    }

    /**
     * Sets the value of the referenceTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReferenceCodeType }
     *     
     */
    public void setReferenceTypeCode(ReferenceCodeType value) {
        this.referenceTypeCode = value;
    }

    public ReferencedDocumentType withIssueDateTime(String value) {
        setIssueDateTime(value);
        return this;
    }

    public ReferencedDocumentType withLineID(IDType value) {
        setLineID(value);
        return this;
    }

    public ReferencedDocumentType withTypeCode(DocumentCodeType value) {
        setTypeCode(value);
        return this;
    }

    public ReferencedDocumentType withID(IDType... values) {
        if (values!= null) {
            for (IDType value: values) {
                getID().add(value);
            }
        }
        return this;
    }

    public ReferencedDocumentType withID(Collection<IDType> values) {
        if (values!= null) {
            getID().addAll(values);
        }
        return this;
    }

    public ReferencedDocumentType withReferenceTypeCode(ReferenceCodeType value) {
        setReferenceTypeCode(value);
        return this;
    }

}
