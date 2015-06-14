package at.austriapro.mappings.zugferd;


import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import at.austriapro.Mapping;
import at.austriapro.MappingException;
import at.austriapro.MappingFactory;
import at.austriapro.mappings.ebinterface.generated.DocumentTypeType;
import at.austriapro.mappings.zugferd.generated.CodeType;
import at.austriapro.mappings.zugferd.generated.DateTimeType;
import at.austriapro.mappings.zugferd.generated.DocumentCodeType;
import at.austriapro.mappings.zugferd.generated.DocumentContextParameterType;
import at.austriapro.mappings.zugferd.generated.ExchangedDocumentContextType;
import at.austriapro.mappings.zugferd.generated.ExchangedDocumentType;
import at.austriapro.mappings.zugferd.generated.IDType;
import at.austriapro.mappings.zugferd.generated.IndicatorType;
import at.austriapro.mappings.zugferd.generated.SupplyChainTradeSettlementType;
import at.austriapro.mappings.zugferd.generated.SupplyChainTradeTransactionType;
import at.austriapro.mappings.zugferd.generated.TextType;
import at.austriapro.utils.DocumentTypeUtils;
import at.austriapro.mappings.ebinterface.generated.Invoice;
import at.austriapro.mappings.zugferd.generated.CrossIndustryDocumentType;

/**
 * Performs mappings from/to ZUGFeRD
 */
public class ZUGFeRDMapping extends Mapping {

  private MappingFactory.MappingType mappingType;
  private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");


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

    return zugferd;
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
   * <xs:enumeration value="FinalSettlement"/> RECHNUNG <xs:enumeration value="Invoice"/>
   *        RECHNUNG <xs:enumeration value="InvoiceForAdvancePayment"/> RECHNUNG <xs:enumeration
   * value="InvoiceForPartialDelivery"/> RECHNUNG <xs:enumeration value="SelfBilling"/>
   *  BELASTUNGSANZEIGE <xs:enumeration value="SubsequentCredit"/>          RECHNUNG <xs:enumeration
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
