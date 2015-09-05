package at.austriapro.mappings.zugferd;


import com.google.common.base.Strings;

import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.austriapro.Mapping;
import at.austriapro.MappingException;
import at.austriapro.MappingFactory;
import at.austriapro.mappings.ebinterface.generated.Address;
import at.austriapro.mappings.ebinterface.generated.Delivery;
import at.austriapro.mappings.ebinterface.generated.DocumentTypeType;
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
      throw new UnsupportedOperationException("Comfort is not supported at the moment.");
    } else {
      throw new UnsupportedOperationException("Extended is not supported at the moment");
    }

    return DocumentTypeUtils.writeZUGFeRD(zugferd);
  }


  /**
   * Map the ebInterface object to a ZUGFeRD object
   */
  private CrossIndustryDocumentType performBasicMapping(Invoice invoice) {

    //Get an empty cross industry document type
    CrossIndustryDocumentType zugferd = getEmptyCrossIndustryDocumentType();

    //ZUGFeRD type
    String zugFeRDType = getZUGfeRDType();
    zugferd.getSpecifiedExchangedDocumentContext().withGuidelineSpecifiedDocumentContextParameter(
        new DocumentContextParameterType().withID(new IDType().withValue(zugFeRDType)));

    //Generating system @TODO

    //Document type
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:Name
    String documentType = getDocumentType(invoice);
    zugferd.getHeaderExchangedDocument().withName(new TextType().withValue(documentType));

    //Document type code
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:TypeCode
    String typeCode = getDocumentTypeCode(invoice);
    zugferd.getHeaderExchangedDocument().withTypeCode(new DocumentCodeType().withValue(typeCode));

    //Currency
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:InvoiceCurrencyCode
    String documentCurrency = invoice.getInvoiceCurrency().value();
    zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement()
        .withInvoiceCurrencyCode(new CodeType().withValue(documentCurrency));

    //Language
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:LanguageID
    zugferd.getHeaderExchangedDocument().getLanguageID()
        .add(new IDType().withValue(invoice.getLanguage().value()));

    //IsDuplicate
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:CopyIndicator
    if (BooleanUtils.isTrue(invoice.getIsDuplicate())) {
      zugferd.getHeaderExchangedDocument()
          .withCopyIndicator(new IndicatorType().withIndicator(Boolean.TRUE));
    }

    //Invoice number
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:ID
    zugferd.getHeaderExchangedDocument().withID(new IDType().withValue(invoice.getInvoiceNumber()));

    //Invoice date
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:IssueDateTime
    zugferd.getHeaderExchangedDocument().withIssueDateTime(new DateTimeType().withDateTimeString(
        new DateTimeType.DateTimeString().withFormat("102")
            .withValue(dateTimeFormatter.print(invoice.getInvoiceDate()))));

    //Map ebInterface delivery details
    mapDelivery(zugferd, invoice.getDelivery());

    return zugferd;
  }


  /**
   * Map the details of an ebInterface delivery element
   */
  private void mapDelivery(CrossIndustryDocumentType zugferd, Delivery delivery) {

    if (delivery == null) {
      LOG.debug("No delivery element specified in ebInterface - continuing.");
      return;
    }

    //Create the necessary elements in ZUGFeRD
    zugferd.getSpecifiedSupplyChainTradeTransaction().withApplicableSupplyChainTradeDelivery(
        new SupplyChainTradeDeliveryType().withShipFromTradeParty(new TradePartyType()));

    //Delivery/Date
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ActualDeliverySupplyChainEvent/ram:OccurrenceDateTime/udt:DateTimeString
    if (delivery.getDate() != null) {
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .withActualDeliverySupplyChainEvent(new SupplyChainEventType().withOccurrenceDateTime(
              new DateTimeType().withDateTimeString(new DateTimeType.DateTimeString().withValue(
                  dateTimeFormatter.print(delivery.getDate())).withFormat(
                  "102"))));
    }

    //DeliveryID
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:DespatchAdviceReferencedDocument/ram:ID
    zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
        .withDespatchAdviceReferencedDocument(
            new ReferencedDocumentType().withID(new IDType().withValue(delivery.getDeliveryID())));

    //Process address details, in case there's one
    if (delivery.getAddress() != null) {

      //Create the necessary elements in ZUGFeRD
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipFromTradeParty().withDefinedTradeContact(new TradeContactType());
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipFromTradeParty().withPostalTradeAddress(new TradeAddressType());

      Address address = delivery.getAddress();

      String partyName = "";
      //Build the person name string
      if (!Strings.isNullOrEmpty(address.getSalutation())) {
        partyName += address.getSalutation() + " ";
      }
      if (!Strings.isNullOrEmpty(address.getName())) {
        partyName += address.getName();
      }

      //Salutation and Name
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipFromTradeParty/ram:Name
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipFromTradeParty().withName(new TextType().withValue(partyName));

      //Street
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipFromTradeParty/ram:PostalTradeAddress/ram:LineOne
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipFromTradeParty().getPostalTradeAddress()
          .setLineOne(new TextType().withValue(address.getStreet()));

      //Town
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipFromTradeParty/ram:PostalTradeAddress/ram:CityName
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipFromTradeParty().getPostalTradeAddress()
          .setCityName(new TextType().withValue(address.getTown()));

      //ZIP
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipFromTradeParty/ram:PostalTradeAddress/ram:PostcodeCode
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipFromTradeParty().getPostalTradeAddress().getPostcodeCode()
          .add(new CodeType().withValue(address.getZIP()));

      //Country Code
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipFromTradeParty/ram:PostalTradeAddress/ram:CountryID
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipFromTradeParty().getPostalTradeAddress().setCountryID(
          new CountryIDType().withValue(address.getCountry().getCountryCode().value()));

      //Phone
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipFromTradeParty/ram:DefinedTradeContact/ram:TelephoneUniversalCommunication/ram:CompleteNumber
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipFromTradeParty().getDefinedTradeContact().get(0)
          .withTelephoneUniversalCommunication(new UniversalCommunicationType().withCompleteNumber(
              new TextType().withValue(address.getPhone())));

      //Email
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipFromTradeParty/ram:DefinedTradeContact/ram:EmailURIUniversalCommunication/ram:CompleteNumber
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipFromTradeParty().getDefinedTradeContact().get(0)
          .withEmailURIUniversalCommunication(new UniversalCommunicationType().withCompleteNumber(
              new TextType().withValue(address.getEmail())));

      //Contact
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipFromTradeParty/ram:DefinedTradeContact/ram:PersonName
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .getShipFromTradeParty().getDefinedTradeContact().get(0)
          .setPersonName(new TextType().withValue(address.getContact()));

    }

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

}
