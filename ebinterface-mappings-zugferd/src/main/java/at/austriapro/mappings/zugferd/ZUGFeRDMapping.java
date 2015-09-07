package at.austriapro.mappings.zugferd;


import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import at.austriapro.Mapping;
import at.austriapro.MappingException;
import at.austriapro.MappingFactory;
import at.austriapro.mappings.ebinterface.generated.Address;
import at.austriapro.mappings.ebinterface.generated.Biller;
import at.austriapro.mappings.ebinterface.generated.CancelledOriginalDocument;
import at.austriapro.mappings.ebinterface.generated.Delivery;
import at.austriapro.mappings.ebinterface.generated.DocumentTypeType;
import at.austriapro.mappings.ebinterface.generated.InvoiceRecipient;
import at.austriapro.mappings.ebinterface.generated.OrderingParty;
import at.austriapro.mappings.ebinterface.generated.RelatedDocument;
import at.austriapro.mappings.zugferd.generated.CodeType;
import at.austriapro.mappings.zugferd.generated.CountryIDType;
import at.austriapro.mappings.zugferd.generated.DateTimeType;
import at.austriapro.mappings.zugferd.generated.DocumentCodeType;
import at.austriapro.mappings.zugferd.generated.DocumentContextParameterType;
import at.austriapro.mappings.zugferd.generated.ExchangedDocumentContextType;
import at.austriapro.mappings.zugferd.generated.ExchangedDocumentType;
import at.austriapro.mappings.zugferd.generated.IDType;
import at.austriapro.mappings.zugferd.generated.IndicatorType;
import at.austriapro.mappings.zugferd.generated.ReferencedDocumentType;
import at.austriapro.mappings.zugferd.generated.SupplyChainEventType;
import at.austriapro.mappings.zugferd.generated.SupplyChainTradeAgreementType;
import at.austriapro.mappings.zugferd.generated.SupplyChainTradeDeliveryType;
import at.austriapro.mappings.zugferd.generated.SupplyChainTradeSettlementType;
import at.austriapro.mappings.zugferd.generated.SupplyChainTradeTransactionType;
import at.austriapro.mappings.zugferd.generated.TextType;
import at.austriapro.mappings.zugferd.generated.TradeAddressType;
import at.austriapro.mappings.zugferd.generated.TradeContactType;
import at.austriapro.mappings.zugferd.generated.TradePartyType;
import at.austriapro.mappings.zugferd.generated.UniversalCommunicationType;
import at.austriapro.utils.DocumentTypeUtils;
import at.austriapro.mappings.ebinterface.generated.Invoice;
import at.austriapro.mappings.zugferd.generated.CrossIndustryDocumentType;

/**
 * Performs mappings from/to ZUGFeRD
 */
public class ZUGFeRDMapping extends Mapping {

  private MappingFactory.MappingType mappingType;
  private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");

  private static final Logger LOG = LoggerFactory.getLogger(ZUGFeRDMapping.class.getName());


  public ZUGFeRDMapping(MappingFactory.MappingType mappingType) {
    this.mappingType = mappingType;
  }

  /*
  Hide parameterless constructor
   */
  private ZUGFeRDMapping() {
  }


  /**
   * Perform a mapping from ebInterface to ZUGFeRD
   */
  @Override
  public byte[] mapFromebInterface(String ebinterface) throws MappingException {

    //Retrieve an Invoice object
    Invoice invoice = DocumentTypeUtils.parseebInterface(ebinterface);

    //Perform mapping - currently we only consider ZUGFeRD Basic by default
    CrossIndustryDocumentType zugferd = null;

    if (MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
      zugferd = performBasicMapping(invoice);
    } else if (MappingFactory.MappingType.ZUGFeRD_COMFORT_1p0.equals(mappingType)) {
      throw new UnsupportedOperationException("ZUGFeRD Comfort is not supported at the moment.");
    } else {
      throw new UnsupportedOperationException("ZUGFeRD Extended is not supported at the moment");
    }

    return DocumentTypeUtils.writeZUGFeRD(zugferd);
  }


  /**
   * Map the ebInterface object to a ZUGFeRD object
   */
  private CrossIndustryDocumentType performBasicMapping(Invoice invoice) {

    //Get an empty cross industry document type
    CrossIndustryDocumentType zugferd = getEmptyCrossIndustryDocumentType();

    //eb:ROOT element attributes
    mapRootAttributes(zugferd, invoice);

    //eb:Related documents
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument
    mapRelatedDocuments(zugferd, invoice.getRelatedDocuments());

    //eb:Delivery
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty
    mapDelivery(zugferd, invoice.getDelivery());

    //eb:Biller
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:SellerTradeParty
    mapBiller(zugferd, invoice.getBiller());

    //eb:Invoice Recipient
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:BuyerTradeParty
    mapInvoiceRecipient(zugferd, invoice.getInvoiceRecipient());

    //eb:Ordering Party
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:ProductEndUserTradeParty
    mapOrderingParty(zugferd, invoice.getOrderingParty());

    return zugferd;
  }

  /**
   * Map the attributes from the ebInterface ROOT element
   */
  private void mapRootAttributes(CrossIndustryDocumentType zugferd, Invoice invoice) {
    //ZUGFeRD type
    String zugFeRDType = getZUGfeRDType();
    zugferd.getSpecifiedExchangedDocumentContext().withGuidelineSpecifiedDocumentContextParameter(
        new DocumentContextParameterType().withID(new IDType().withValue(zugFeRDType)));

    //eb:Generating system
    //TODO

    //eb:Document type
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:Name
    String documentType = getDocumentType(invoice);
    zugferd.getHeaderExchangedDocument().withName(new TextType().withValue(documentType));

    //eb:Document type code
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:TypeCode
    String typeCode = getDocumentTypeCode(invoice);
    zugferd.getHeaderExchangedDocument().withTypeCode(new DocumentCodeType().withValue(typeCode));

    //eb:Currency
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:InvoiceCurrencyCode
    String documentCurrency = invoice.getInvoiceCurrency().value();
    zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement()
        .withInvoiceCurrencyCode(new CodeType().withValue(documentCurrency));

    //eb:Language
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:LanguageID
    zugferd.getHeaderExchangedDocument().getLanguageID()
        .add(new IDType().withValue(invoice.getLanguage().value()));

    //eb:IsDuplicate
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:CopyIndicator
    if (BooleanUtils.isTrue(invoice.getIsDuplicate())) {
      zugferd.getHeaderExchangedDocument()
          .withCopyIndicator(new IndicatorType().withIndicator(Boolean.TRUE));
    }

    //eb:Invoice number
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:ID
    zugferd.getHeaderExchangedDocument().withID(new IDType().withValue(invoice.getInvoiceNumber()));

    //eb:Invoice date
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:IssueDateTime
    zugferd.getHeaderExchangedDocument().withIssueDateTime(new DateTimeType().withDateTimeString(
        new DateTimeType.DateTimeString().withFormat("102")
            .withValue(dateTimeFormatter.print(invoice.getInvoiceDate()))));

    //eb:Cancelled original document
    //TODO - we may put this one in an IncludedNote in the header
    mapCancelledOriginalDocument(zugferd, invoice.getCancelledOriginalDocument());
  }

  /**
   * Map the ordering party Target in ZUGFeRD: rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:ProductEndUserTradeParty
   */
  private void mapOrderingParty(CrossIndustryDocumentType zugferd, OrderingParty orderingParty) {
    //TODO
  }

  /**
   * Map the invoice recipient Target in ZUGFeRD: rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:BuyerTradeParty
   */
  private void mapInvoiceRecipient(CrossIndustryDocumentType zugferd,
                                   InvoiceRecipient invoiceRecipient) {
    //TODO
  }

  /**
   * Map the biller Target in ZUGFeRD: CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:SellerTradeParty
   */
  private void mapBiller(CrossIndustryDocumentType zugferd, Biller biller) {
    //TODO
  }

  /**
   * Map the details of related documents Target in ZUGFeRD: rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument
   */
  private void mapRelatedDocuments(CrossIndustryDocumentType zugferd,
                                   List<RelatedDocument> relatedDocuments) {
    if (Iterables.isEmpty(relatedDocuments)) {
      LOG.debug("No related documents specified in ebInterface - continuing");
      return;
    }

    SupplyChainTradeAgreementType
        supplyChainTradeAgreementType =
        getSupplyChainTradeAgreement(zugferd);

    for (RelatedDocument relatedDocument : relatedDocuments) {

      //Create a new related document type and assign it to the supply chain trade agreement

      ReferencedDocumentType referencedDocumentType = new ReferencedDocumentType();
      supplyChainTradeAgreementType.getAdditionalReferencedDocument().add(referencedDocumentType);

      //eb:InvoiceNumber
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument/ram:ID
      referencedDocumentType.withID(new IDType().withValue(relatedDocument.getInvoiceNumber()));

      //eb:InvoiceDate
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument/ram:IssueDateTime
      referencedDocumentType.withIssueDateTime(
          dateTimeFormatter.print(relatedDocument.getInvoiceDate()));

      //eb:Document type
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument/ram:TypeCode
      referencedDocumentType.withTypeCode(
          new DocumentCodeType().withValue(relatedDocument.getDocumentType().value()));

      //eb:Comment
      //TODO - not really an element in ZUGFeRD which fits here...

    }


  }

  /**
   * Map the details of the cancelled document Target in ZUGFeRD: TODO
   */
  private void mapCancelledOriginalDocument(CrossIndustryDocumentType zugferd,
                                            CancelledOriginalDocument cancelledOriginalDocument) {

    if (cancelledOriginalDocument == null) {
      LOG.debug("No cancelled original document specified in ebInterface - continuing");
      return;
    }

    //TODO

  }


  /**
   * Map the details of an ebInterface delivery element Target in ZUGFeRD:
   * rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty
   */
  private void mapDelivery(CrossIndustryDocumentType zugferd, Delivery delivery) {

    if (delivery == null) {
      LOG.debug("No delivery element specified in ebInterface - continuing.");
      return;
    }

    //Create the necessary elements in ZUGFeRD
    zugferd.getSpecifiedSupplyChainTradeTransaction().withApplicableSupplyChainTradeDelivery(
        new SupplyChainTradeDeliveryType().withShipToTradeParty(new TradePartyType()));

    //eb:Delivery/Date
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ActualDeliverySupplyChainEvent/ram:OccurrenceDateTime/udt:DateTimeString
    if (delivery.getDate() != null) {
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .withActualDeliverySupplyChainEvent(new SupplyChainEventType().withOccurrenceDateTime(
              new DateTimeType().withDateTimeString(new DateTimeType.DateTimeString().withValue(
                  dateTimeFormatter.print(delivery.getDate())).withFormat(
                  "102"))));
    }

    //eb:DeliveryID
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:DespatchAdviceReferencedDocument/ram:ID
    zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
        .withDespatchAdviceReferencedDocument(
            new ReferencedDocumentType().withID(new IDType().withValue(delivery.getDeliveryID())));

    //Process address details, in case there's one
    if (delivery.getAddress() != null) {

      //Create the necessary elements in ZUGFeRD
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipToTradeParty().withDefinedTradeContact(new TradeContactType());
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipToTradeParty().withPostalTradeAddress(new TradeAddressType());

      Address address = delivery.getAddress();

      String partyName = "";
      //Build the person name string
      if (!Strings.isNullOrEmpty(address.getSalutation())) {
        partyName += address.getSalutation() + " ";
      }
      if (!Strings.isNullOrEmpty(address.getName())) {
        partyName += address.getName();
      }

      //eb:Salutation and Name
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:Name
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipToTradeParty().withName(new TextType().withValue(partyName));

      //eb:Street
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:PostalTradeAddress/ram:LineOne
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipToTradeParty().getPostalTradeAddress()
          .setLineOne(new TextType().withValue(address.getStreet()));

      //eb:Town
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:PostalTradeAddress/ram:CityName
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipToTradeParty().getPostalTradeAddress()
          .setCityName(new TextType().withValue(address.getTown()));

      //eb:ZIP
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:PostalTradeAddress/ram:PostcodeCode
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipToTradeParty().getPostalTradeAddress().getPostcodeCode()
          .add(new CodeType().withValue(address.getZIP()));

      //eb:Country Code
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:PostalTradeAddress/ram:CountryID
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipToTradeParty().getPostalTradeAddress().setCountryID(
          new CountryIDType().withValue(address.getCountry().getCountryCode().value()));

      //eb:Phone
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:DefinedTradeContact/ram:TelephoneUniversalCommunication/ram:CompleteNumber
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipToTradeParty().getDefinedTradeContact().get(0)
          .withTelephoneUniversalCommunication(new UniversalCommunicationType().withCompleteNumber(
              new TextType().withValue(address.getPhone())));

      //eb:Email
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:DefinedTradeContact/ram:EmailURIUniversalCommunication/ram:CompleteNumber
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipToTradeParty().getDefinedTradeContact().get(0)
          .withEmailURIUniversalCommunication(new UniversalCommunicationType().withCompleteNumber(
              new TextType().withValue(address.getEmail())));

      //eb:Contact
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:DefinedTradeContact/ram:PersonName
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipToTradeParty().getDefinedTradeContact().get(0)
          .setPersonName(new TextType().withValue(address.getContact()));

      //eb:Address extension
      //TODO - no field in ZUGFeRD for that

    }

    //eb:Description
    //TODO - no field in ZUGFeRD for that

  }


  /**
   * Create an empty cross industry invoice, with the most important elements already added with
   * empty content
   */
  private CrossIndustryDocumentType getEmptyCrossIndustryDocumentType() {
    CrossIndustryDocumentType zugferd = new CrossIndustryDocumentType();

    zugferd.withSpecifiedExchangedDocumentContext(new ExchangedDocumentContextType());
    zugferd.withHeaderExchangedDocument(new ExchangedDocumentType());
    zugferd.withSpecifiedSupplyChainTradeTransaction(new SupplyChainTradeTransactionType()
                                                         .withApplicableSupplyChainTradeSettlement(
                                                             new SupplyChainTradeSettlementType()));

    return zugferd;
  }

  /**
   * Depending on the set document type in ebInterface, return the correct ZUGFeRD document type
   *
   * Valid values in ZUGFeRD are RECHNUNG, GUTSCHRIFT, BELASTUNGSANZEIGE, PROFORMARECHNUNG
   *
   * The following mapping scheme is applied <xs:enumeration value="CreditMemo"/> GUTSCHRIFT
   * <xs:enumeration value="FinalSettlement"/> RECHNUNG <xs:enumeration value="Invoice"/> RECHNUNG
   * <xs:enumeration value="InvoiceForAdvancePayment"/> RECHNUNG <xs:enumeration
   * value="InvoiceForPartialDelivery"/> RECHNUNG <xs:enumeration value="SelfBilling"/>
   * BELASTUNGSANZEIGE <xs:enumeration value="SubsequentCredit"/>          RECHNUNG <xs:enumeration
   * value="SubsequentDebit"/> RECHNUNG
   */
  private String getDocumentType(Invoice invoice) {

    if (DocumentTypeType.SELF_BILLING.equals(invoice.getDocumentType())) {
      return "BELASTUNGSANZEIGE";
    } else if (DocumentTypeType.CREDIT_MEMO.equals(invoice.getDocumentType())) {
      return "GUTSCHRIFT";
    } else {
      return "RECHNUNG";
    }

  }

  /**
   * Return the correct document type code. Valid document type codes in ZUGFeRD are 380, 84, 389
   */
  private String getDocumentTypeCode(Invoice invoice) {

    //Code 84 has no equivalent in ebInterface
    if (DocumentTypeType.SELF_BILLING.equals(invoice.getDocumentType())) {
      return "389";
    } else {
      return "380";
    }
  }


  /**
   * Return the correct identifier scheme of the given ZUGFeRD type
   */
  private String getZUGfeRDType() {

    if (MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
      return "urn:ferd:CrossIndustryDocument:invoice:1p0:basic";
    } else if (MappingFactory.MappingType.ZUGFeRD_COMFORT_1p0.equals(mappingType)) {
      return "urn:ferd:CrossIndustryDocument:invoice:1p0:comfort";
    } else {
      return "urn:ferd:CrossIndustryDocument:invoice:1p0:extended";
    }
  }

  /**
   * Get the supply chain trade agreement from ZUGFeRD. In case there's one, return the first one
   * found (we won't use multiple supply chain trade agreements in this mapping). Otherwise create a
   * new one and return it.
   */
  private SupplyChainTradeAgreementType getSupplyChainTradeAgreement(
      CrossIndustryDocumentType zugferd) {

    if (Iterables.isEmpty(zugferd.getSpecifiedSupplyChainTradeTransaction()
                              .getApplicableSupplyChainTradeAgreement())) {
      SupplyChainTradeAgreementType
          supplyChainTradeAgreementType =
          new SupplyChainTradeAgreementType();
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeAgreement()
          .add(
              supplyChainTradeAgreementType);
      return supplyChainTradeAgreementType;
    } else {
      return zugferd.getSpecifiedSupplyChainTradeTransaction()
          .getApplicableSupplyChainTradeAgreement().get(0);
    }

  }
}
