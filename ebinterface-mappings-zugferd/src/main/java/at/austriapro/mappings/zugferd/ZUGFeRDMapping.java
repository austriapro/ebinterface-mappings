package at.austriapro.mappings.zugferd;


import at.austriapro.mappings.ebinterface.generated.*;
import at.austriapro.mappings.zugferd.generated.*;
import at.austriapro.utils.ISO639ConversionUtil;

import com.google.common.base.Strings;

import com.google.common.collect.Iterables;


import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import at.austriapro.Mapping;
import at.austriapro.MappingException;
import at.austriapro.MappingFactory;
import at.austriapro.utils.DocumentTypeUtils;

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

    //Perform mapping
    CrossIndustryDocumentType zugferd = performMapping(invoice);

    return DocumentTypeUtils.writeZUGFeRD(zugferd);
  }


  /**
   * Map the ebInterface object to a ZUGFeRD object
   */
  private CrossIndustryDocumentType performMapping(Invoice invoice) {

    //Get an empty cross industry document type
    CrossIndustryDocumentType zugferd = getEmptyCrossIndustryDocumentType();

    //eb:ROOT element attributes
    mapRootAttributes(zugferd, invoice);

    //eb:Signature
    mapSignature(zugferd, invoice.getSignature());

    //eb:Cancelled original document
    //exdended: rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument
    //basic and comfort: IncludedNote
    mapCancelledOriginalDocument(zugferd, invoice.getCancelledOriginalDocument());

    //eb:Related documents
    //exdended: rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument
    //basic and comfort: IncludedNote
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

    //eb:Details
    mapDetails(zugferd, invoice.getDetails());

    //eb:ReductionAndSurchargeDetails
    mapReductionAndSurchargeDetails(zugferd, invoice.getReductionAndSurchargeDetails());

    //eb:Tax
    mapTax(zugferd, invoice.getTax());

    //eb:TotalGrossAmount
    mapTotalGrossAmount(zugferd, invoice.getTotalGrossAmount());

    //eb:PayableAmount
    mapPayableAmount(zugferd, invoice.getPayableAmount());

    //eb:PaymentMethod
    mapPaymentMethod(zugferd, invoice.getPaymentMethod());

    //eb:PaymentConditions
    mapPaymentConditions(zugferd, invoice.getPaymentConditions());

    //eb:PresentationDetails
    mapPresentationDetails(zugferd, invoice.getPresentationDetails());

    //eb:Comment
    mapComment(zugferd, invoice.getComment());

    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement

    //TODO - we need to refactor this - we always map from the ebInterface elements to the ZUGFeRD elements, by taking the
    //top level elements in ebInterface as a starting point
    //mapTradeSettlement(zugferd, invoice);

    return zugferd;
  }


  /**
   * Map details of ebInterface comments
   * @param zugferd
   * @param comment
   */
  private void mapComment(CrossIndustryDocumentType zugferd, String comment) {

  }


  /**
   * Map presentation details
   * @param zugferd
   * @param presentationDetails
   */
  private void mapPresentationDetails(CrossIndustryDocumentType zugferd,
                                      PresentationDetails presentationDetails) {

  }

  /**
   * Map the details of payment conditions
   * @param zugferd
   * @param paymentConditions
   */
  private void mapPaymentConditions(CrossIndustryDocumentType zugferd,
                                    PaymentConditions paymentConditions) {

  }


  /**
   * Map details of payment method
   * @param zugferd
   * @param paymentMethod
   */
  private void mapPaymentMethod(CrossIndustryDocumentType zugferd, PaymentMethod paymentMethod) {

  }

  /**
   * Map details of payable amount
   * @param zugferd
   * @param payableAmount
   */
  private void mapPayableAmount(CrossIndustryDocumentType zugferd, BigDecimal payableAmount) {

  }

  /**
   * ebInterface total gross details
   * @param zugferd
   * @param totalGrossAmount
   */
  private void mapTotalGrossAmount(CrossIndustryDocumentType zugferd, BigDecimal totalGrossAmount) {

  }


  /**
   * Map the tax details to the ZUGFeRD equivalent
   * @param zugferd
   * @param tax
   */
  private void mapTax(CrossIndustryDocumentType zugferd, Tax tax) {

  }


  /**
   * Map the different reductions and surcharges in ebInterface to the respective fields in ZUGFeRD
   * @param zugferd
   * @param reductionAndSurchargeDetails
   */
  private void mapReductionAndSurchargeDetails(CrossIndustryDocumentType zugferd,
                                               ReductionAndSurchargeDetails reductionAndSurchargeDetails) {

  }

  /**
   * Map the details section of ebInterace, containing the different line items, to the correct
   * fields in ZUGFeRD
   * @param zugferd
   * @param details
   */
  private void mapDetails(CrossIndustryDocumentType zugferd, Details details) {

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

    //eb:ManualProcessing
    //TODO not found in ZUGFeRD

    //eb:DocumentTitle
    //TODO not found in ZUGFeRD

    if (MappingFactory.MappingType.ZUGFeRD_COMFORT_1p0.equals(mappingType)) {
      //eb:Language
      //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:LanguageID
      zugferd.getHeaderExchangedDocument().getLanguageID()
          .add(new IDType().withValue(ISO639ConversionUtil.convertISO639_2ToISO639_1(
              invoice.getLanguage().value())));
    }

    if (MappingFactory.MappingType.ZUGFeRD_COMFORT_1p0.equals(mappingType)) {
      //eb:IsDuplicate
      //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:CopyIndicator
      if (BooleanUtils.isTrue(invoice.getIsDuplicate())) {
        zugferd.getHeaderExchangedDocument()
            .withCopyIndicator(new IndicatorType().withIndicator(Boolean.TRUE));
      }

      //eb:Invoice number
      //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:ID
      zugferd.getHeaderExchangedDocument().withID(
          new IDType().withValue(invoice.getInvoiceNumber()));

      //eb:Invoice date
      //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:IssueDateTime
      zugferd.getHeaderExchangedDocument().withIssueDateTime(new DateTimeType().withDateTimeString(
          new DateTimeType.DateTimeString().withFormat("102")
              .withValue(dateTimeFormatter.print(invoice.getInvoiceDate()))));
    }
  }

  /**
   * Map the signature from the ebInterface
   */
  private void mapSignature(CrossIndustryDocumentType zugferd, Signature signature) {
    //TODO not supported in ZUGFeRD
  }

  /**
   * Map the ordering party Target in ZUGFeRD: rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:ProductEndUserTradeParty
   */
  private void mapOrderingParty(CrossIndustryDocumentType zugferd, OrderingParty orderingParty) {
    if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)) {
      if (orderingParty == null) {
        LOG.debug("No biller element specified in ebInterface - continuing.");
        return;
      }

      //Create a trade party for the invoice recipient
      TradePartyType productEndUserTradeParty = new TradePartyType();
      SupplyChainTradeAgreementType
          supplyChainTradeAgreementType =
          getSupplyChainTradeAgreement(zugferd);
      supplyChainTradeAgreementType.withProductEndUserTradeParty(productEndUserTradeParty);

      //TODO - rest of the elements in orderingParty

      //Create a trade party for the invoice recipient
      ReferencedDocumentType buyerOrderReferencedDocument = new ReferencedDocumentType();
      supplyChainTradeAgreementType.withBuyerOrderReferencedDocument(buyerOrderReferencedDocument);

      //eb:OrderReference
      if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
        //eb:OrderID
        buyerOrderReferencedDocument
            .withIssueDateTime(
                dateTimeFormatter
                    .print(orderingParty.getOrderReference()
                               .getReferenceDate())) //IssueDateTime is a String
            .withID(new IDType().withValue(orderingParty.getOrderReference().getOrderID()));
      }
    }

  }

  /**
   * Map the invoice recipient Target in ZUGFeRD: rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:BuyerTradeParty
   */
  private void mapInvoiceRecipient(CrossIndustryDocumentType zugferd,
                                   InvoiceRecipient invoiceRecipient) {

    if (invoiceRecipient == null) {
      LOG.debug("No biller element specified in ebInterface - continuing.");
      return;
    }

    //Create a trade party for the invoice recipient
    TradePartyType invoiceRecipientTradePartyType = new TradePartyType();
    SupplyChainTradeAgreementType
        supplyChainTradeAgreementType =
        getSupplyChainTradeAgreement(zugferd);
    supplyChainTradeAgreementType.withBuyerTradeParty(invoiceRecipientTradePartyType);

    //eb:FurtherIdentification
    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
        && invoiceRecipient.getFurtherIdentifications() != null
        && invoiceRecipient.getFurtherIdentifications().size() > 0) {

      for (FurtherIdentification furtherIdentification : invoiceRecipient
          .getFurtherIdentifications()) {
        invoiceRecipientTradePartyType
            .withID(new IDType().withValue(furtherIdentification.getValue()).withSchemeID(
                furtherIdentification.getIdentificationType()));
      }

      //eb:InvoiceRecipient/Address/Addressidentifier/@AddressIdentifierType=(GLN, DUNS)
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/BuyerTradeParty/GlobalID
      if (invoiceRecipient.getAddress().getAddressIdentifiers().get(0) != null) {
        String schema = null;
        if (invoiceRecipient.getAddress().getAddressIdentifiers().get(0).getAddressIdentifierType()
            .equals(AddressIdentifierTypeType.DUNS)) {
          schema = "0060";
        } else if (invoiceRecipient.getAddress().getAddressIdentifiers().get(0)
            .getAddressIdentifierType()
            .equals(AddressIdentifierTypeType.GLN)) {
          schema = "0088";
        }
        if (schema != null) {
          invoiceRecipientTradePartyType.withGlobalID(
              new IDType().withValue(
                  invoiceRecipient.getAddress().getAddressIdentifiers().get(0).getValue())
                  .withSchemeID(schema));
        }
      }
    }

    //eb:BillersInvoiceRecipientID
    if (!Strings.isNullOrEmpty(invoiceRecipient.getBillersInvoiceRecipientID())) {
      invoiceRecipientTradePartyType.withID(
          new IDType().withValue(invoiceRecipient.getBillersInvoiceRecipientID()).withSchemeID(
              "Rechnungsempfänger-ID des Rechnungsstellers"));
    }

    //eb:AccountingArea
    //TODO - no respective field

    //eb:SubOrganizationID
    //TODO - no respective field

    //eb:Address
    String lineOne = "";
    if (invoiceRecipient.getAddress().getStreet() != null) {
      lineOne = invoiceRecipient.getAddress().getStreet();
    } else {
      lineOne = invoiceRecipient.getAddress().getPOBox();
    }

    invoiceRecipientTradePartyType.withName(
        new TextType().withValue(invoiceRecipient.getAddress().getName()));
    invoiceRecipientTradePartyType.withPostalTradeAddress(
        new TradeAddressType()
            .withPostcodeCode(
                new CodeType().withValue(invoiceRecipient.getAddress().getZIP()))
            .withLineOne(new TextType().withValue(lineOne))
            .withLineTwo(
                new TextType().withValue(invoiceRecipient.getAddress().getContact()))
            .withCityName(
                new TextType().withValue(invoiceRecipient.getAddress().getTown()))
            .withCountryID(new CountryIDType().withValue(
                invoiceRecipient.getAddress().getCountry().getCountryCode().value())));

    //eb:VATIdentification
    invoiceRecipientTradePartyType.withSpecifiedTaxRegistration(new TaxRegistrationType().withID(
        new IDType().withValue(invoiceRecipient.getVATIdentificationNumber()).withSchemeID("VA")));
  }

  /**
   * Map the biller Target in ZUGFeRD: //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement
   */
  private void mapTradeSettlement(CrossIndustryDocumentType zugferd, Invoice invoice) {
    SupplyChainTradeSettlementType
        sctst =
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement();

    //eb:PaymentReference
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:PaymentReference
    sctst.withPaymentReference(new TextType().withValue(
        invoice.getPaymentMethod().getUniversalBankTransaction().getPaymentReference().getValue()));

    //get zugferd currency
    String documentCurrency = sctst.getInvoiceCurrencyCode().getValue();

    TradeSettlementPaymentMeansType tspmt = new TradeSettlementPaymentMeansType();
    sctst.withSpecifiedTradeSettlementPaymentMeans(tspmt);

    //eb:?
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ID
    //TODO not in ebInvoice

    String paymentMethod = "";
    if (invoice.getPaymentMethod().getDirectDebit() != null) {
      paymentMethod = "49";
    } else if (invoice.getPaymentMethod().getUniversalBankTransaction() != null) {
      paymentMethod = "31";
    } else {
      paymentMethod = "1";
    }

    //eb:UniversalBankTransaction / eb:DirectDebit
    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/TypeCode
      tspmt.withTypeCode(new PaymentMeansCodeType().withValue(paymentMethod));

      //eb:Comment
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/Information
      tspmt.withInformation(new TextType().withValue(invoice.getPaymentMethod().getComment()));

      if (paymentMethod.equals("49")) {
        //eb:?
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/PayerPartyDebtorFinancialAccount
        //TODO not in ebInvoice

        //eb:?
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/PayerSpecifiedDebtorFinancialInstitution
        //TODO not in ebInvoice
      }
    }

    if (paymentMethod.equals("31")
        && invoice.getPaymentMethod().getUniversalBankTransaction().getBeneficiaryAccounts().size()
           > 1) {
      //eb:UniversalBankTransaction
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/PayeePartyCreditorFinancialAccount
      tspmt.withPayeePartyCreditorFinancialAccount(new CreditorFinancialAccountType()
                                                       .withIBANID(new IDType().withValue(
                                                           invoice.getPaymentMethod()
                                                               .getUniversalBankTransaction()
                                                               .getBeneficiaryAccounts().get(0)
                                                               .getIBAN()))
                                                       .withAccountName(new TextType().withValue(
                                                           invoice.getPaymentMethod()
                                                               .getUniversalBankTransaction()
                                                               .getBeneficiaryAccounts().get(0)
                                                               .getBankAccountOwner())));

      //eb:UniversalBankTransaction
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/PayeeSpecifiedCreditorFinancialInstitution
      tspmt.withPayeeSpecifiedCreditorFinancialInstitution(new CreditorFinancialInstitutionType()
                                                               .withBICID(new IDType().withValue(
                                                                   invoice.getPaymentMethod()
                                                                       .getUniversalBankTransaction()
                                                                       .getBeneficiaryAccounts()
                                                                       .get(0).getBIC()))
                                                               .withName(new TextType().withValue(
                                                                   invoice.getPaymentMethod()
                                                                       .getUniversalBankTransaction()
                                                                       .getBeneficiaryAccounts()
                                                                       .get(0).getBankName())));

    }

    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:ApplicableTradeTax
    if (invoice.getTax().getVAT().getVATItems().size() > 0) {

      for (VATItem vATItems : invoice.getTax().getVAT().getVATItems()) {

        TradeTaxType tradeTaxType = new TradeTaxType();

        //eb:Amount
        tradeTaxType.withCalculatedAmount(
            new AmountType().withValue(vATItems.getAmount())
                .withCurrencyID(documentCurrency));

        //Tax type - always VAT in this case
        tradeTaxType.withTypeCode(new TaxTypeCodeType().withValue("VAT"));

        //eb:TaxedAmount
        tradeTaxType.withBasisAmount(new AmountType().withValue(vATItems.getTaxedAmount())
                                         .withCurrencyID(documentCurrency));

        //eb:VATRate
        if (vATItems.getVATRate() != null) {
          tradeTaxType.withApplicablePercent(
              new PercentType()
                  .withValue(vATItems.getVATRate().getValue()));
        }
      }
    }

    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/?
    //TODO set OtherTaxes proper
    if (invoice.getTax().getOtherTaxes().size() > 0) {

      for (OtherTax otherTax : invoice.getTax().getOtherTaxes()) {
                /*sctst.withApplicableTradeTax(new TradeTaxType()
                        .withCalculatedAmount(new AmountType().withValue(vATItems.getAmount()).withCurrencyID(documentCurrency))
                        .withTypeCode(new TaxTypeCodeType().withValue("VAT"))
                        .withBasisAmount(new AmountType().withValue(vATItems.getTaxedAmount()).withCurrencyID(documentCurrency))
                        .withApplicablePercent(new PercentType().withValue(vATItems.getVATRate().getValue())));*/
      }
    }

  }

  /**
   * Map the biller Target in ZUGFeRD: CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:SellerTradeParty
   */
  private void mapBiller(CrossIndustryDocumentType zugferd, Biller biller) {
    if (biller == null) {
      LOG.debug("No blller element specified in ebInterface - continuing.");
      return;
    }

    SupplyChainTradeAgreementType
        supplyChainTradeAgreementType =
        getSupplyChainTradeAgreement(zugferd);
    //CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/SellerTradeParty
    TradePartyType sellerTradePartyType = new TradePartyType();
    supplyChainTradeAgreementType.withSellerTradeParty(sellerTradePartyType);

    //CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/BuyerReference
    //TODO not in ebInvoice
    //supplyChainTradeAgreementType.withBuyerReference(new TextType().withValue(""));

    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
        && biller.getInvoiceRecipientsBillerID() != null) {
      //eb:Biller/InvoiceRecipientsBillerID
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/SellerTradeParty/ID
      sellerTradePartyType.withID(new IDType().withValue(biller.getInvoiceRecipientsBillerID()));

      //eb:Biller/Address/Addressidentifier/@AddressIdentifierType=GLN
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/SellerTradeParty/GlobalID
      if (biller.getAddress().getAddressIdentifiers().get(0) != null) {
        String schema = null;
        if (biller.getAddress().getAddressIdentifiers().get(0).getAddressIdentifierType()
            .equals(AddressIdentifierTypeType.DUNS)) {
          schema = "0060";
        } else if (biller.getAddress().getAddressIdentifiers().get(0).getAddressIdentifierType()
            .equals(AddressIdentifierTypeType.GLN)) {
          schema = "0088";
        }
        if (schema != null) {
          sellerTradePartyType.withGlobalID(
              new IDType().withValue(biller.getAddress().getAddressIdentifiers().get(0).getValue())
                  .withSchemeID(schema));
        }
      }

      //eb:Address
      String lineOne = "";
      if (biller.getAddress().getStreet() != null) {
        lineOne = biller.getAddress().getStreet();
      } else {
        lineOne = biller.getAddress().getPOBox();
      }

      sellerTradePartyType.withName(new TextType().withValue(biller.getAddress().getName()));
      sellerTradePartyType.withPostalTradeAddress(
          new TradeAddressType()
              .withPostcodeCode(new CodeType().withValue(biller.getAddress().getZIP()))
              .withLineOne(new TextType().withValue(lineOne))
              .withLineTwo(new TextType().withValue(biller.getAddress().getContact()))
              .withCityName(new TextType().withValue(biller.getAddress().getTown()))
              .withCountryID(new CountryIDType().withValue(
                  biller.getAddress().getCountry().getCountryCode().value())));

      //eb:VATIdentification
      sellerTradePartyType.withSpecifiedTaxRegistration(new TaxRegistrationType().withID(
          new IDType().withValue(biller.getVATIdentificationNumber()).withSchemeID("VA")));
    }
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

      if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)) {
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
        //OI = Reference number identifying a previously issued invoice. (http://www.unece.org/trade/untdid/i98a/uncl/uncl1153.htm)
        referencedDocumentType.withTypeCode(
            new DocumentCodeType().withValue("OI"));

        //eb:Comment
        //TODO - not really an element in ZUGFeRD which fits here...
      } else {
        StringBuilder text = new StringBuilder();

        text.append("Zugehörige Rechnung:\n");

        if (relatedDocument.getInvoiceNumber() != null) {
          text.append(relatedDocument.getInvoiceNumber());
        }

        if (relatedDocument.getInvoiceDate() != null) {
          text.append(dateTimeFormatter.print(relatedDocument.getInvoiceDate())).append("\n");
        }
        if (relatedDocument.getComment() != null) {
          text.append(relatedDocument.getComment());
        }

        zugferd.getHeaderExchangedDocument().getIncludedNote()
            .add(new NoteType().withContent(new TextType().withValue(text.toString())));
      }
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

    if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)) {
      SupplyChainTradeAgreementType
          supplyChainTradeAgreementType =
          getSupplyChainTradeAgreement(zugferd);

      //Create a new related document type and assign it to the supply chain trade agreement
      ReferencedDocumentType referencedDocumentType = new ReferencedDocumentType();
      supplyChainTradeAgreementType.getAdditionalReferencedDocument().add(referencedDocumentType);

      //eb:InvoiceNumber
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument/ram:ID
      referencedDocumentType
          .withID(new IDType().withValue(cancelledOriginalDocument.getInvoiceNumber()));

      //eb:InvoiceDate
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument/ram:IssueDateTime
      referencedDocumentType.withIssueDateTime(
          dateTimeFormatter.print(cancelledOriginalDocument.getInvoiceDate()));

      //eb:Document type
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument/ram:TypeCode
      //ACW = Reference number assigned to the message which was previously issued (e.g. in the case of a cancellation,
      //      the primary reference of the message to be cancelled will be quoted in this element). (http://www.unece.org/trade/untdid/i98a/uncl/uncl1153.htm)
      referencedDocumentType.withTypeCode(
          new DocumentCodeType().withValue("ACW"));

      //eb:Comment
      //TODO - not really an element in ZUGFeRD which fits here...

    } else {
      StringBuilder text = new StringBuilder();

      text.append("Stornierte Rechnung:\n");

      if (cancelledOriginalDocument.getInvoiceNumber() != null) {
        text.append(cancelledOriginalDocument.getInvoiceNumber());
      }

      if (cancelledOriginalDocument.getInvoiceDate() != null) {
        text.append(dateTimeFormatter.print(cancelledOriginalDocument.getInvoiceDate()))
            .append("\n");
      }
      if (cancelledOriginalDocument.getComment() != null) {
        text.append(cancelledOriginalDocument.getComment());
      }

      zugferd.getHeaderExchangedDocument().getIncludedNote()
          .add(new NoteType().withContent(new TextType().withValue(text.toString())));
    }
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

    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
        && delivery.getDeliveryID() != null) {
      //eb:DeliveryID
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:DespatchAdviceReferencedDocument/ram:ID
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .withDeliveryNoteReferencedDocument(
              new ReferencedDocumentType()
                  .withID(new IDType().withValue(delivery.getDeliveryID())));
    }

    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ActualDeliverySupplyChainEvent/ram:OccurrenceDateTime/udt:DateTimeString
    if (delivery.getDate() != null) {
      //eb:Delivery/Date
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .withActualDeliverySupplyChainEvent(new SupplyChainEventType().withOccurrenceDateTime(
              new DateTimeType().withDateTimeString(new DateTimeType.DateTimeString().withValue(
                  dateTimeFormatter.print(delivery.getDate())).withFormat(
                  "102"))));
    }else if (delivery.getPeriod().getFromDate() != null){
      //eb:Delivery/Period/FromDate
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .withActualDeliverySupplyChainEvent(new SupplyChainEventType().withOccurrenceDateTime(
              new DateTimeType().withDateTimeString(new DateTimeType.DateTimeString().withValue(
                  dateTimeFormatter.print(delivery.getPeriod().getFromDate())).withFormat(
                  "102"))));
    }

    if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)) {
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
            .withTelephoneUniversalCommunication(
                new UniversalCommunicationType().withCompleteNumber(
                    new TextType().withValue(address.getPhone())));

        //eb:Email
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:DefinedTradeContact/ram:EmailURIUniversalCommunication/ram:CompleteNumber
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().getDefinedTradeContact().get(0)
            .withEmailURIUniversalCommunication(
                new UniversalCommunicationType().withCompleteNumber(
                    new TextType().withValue(address.getEmail())));

        //eb:Contact
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:DefinedTradeContact/ram:PersonName
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().getDefinedTradeContact().get(0)
            .setPersonName(new TextType().withValue(address.getContact()));

        //eb:Address extension
        //TODO - no field in ZUGFeRD for that
      }
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
