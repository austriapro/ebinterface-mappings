package at.austriapro.mappings.zugferd;

import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.xml.bind.JAXBElement;

import at.austriapro.Mapping;
import at.austriapro.MappingException;
import at.austriapro.MappingFactory;
import at.austriapro.mappings.ebinterface.generated.AdditionalInformation;
import at.austriapro.mappings.ebinterface.generated.Address;
import at.austriapro.mappings.ebinterface.generated.AddressIdentifier;
import at.austriapro.mappings.ebinterface.generated.AddressIdentifierTypeType;
import at.austriapro.mappings.ebinterface.generated.ArticleNumber;
import at.austriapro.mappings.ebinterface.generated.BeneficiaryAccount;
import at.austriapro.mappings.ebinterface.generated.Biller;
import at.austriapro.mappings.ebinterface.generated.CancelledOriginalDocument;
import at.austriapro.mappings.ebinterface.generated.Classification;
import at.austriapro.mappings.ebinterface.generated.Delivery;
import at.austriapro.mappings.ebinterface.generated.Details;
import at.austriapro.mappings.ebinterface.generated.Discount;
import at.austriapro.mappings.ebinterface.generated.DocumentTypeType;
import at.austriapro.mappings.ebinterface.generated.FurtherIdentification;
import at.austriapro.mappings.ebinterface.generated.Invoice;
import at.austriapro.mappings.ebinterface.generated.InvoiceRecipient;
import at.austriapro.mappings.ebinterface.generated.ItemList;
import at.austriapro.mappings.ebinterface.generated.ListLineItem;
import at.austriapro.mappings.ebinterface.generated.OrderingParty;
import at.austriapro.mappings.ebinterface.generated.OtherTax;
import at.austriapro.mappings.ebinterface.generated.OtherVATableTax;
import at.austriapro.mappings.ebinterface.generated.OtherVATableTaxBaseType;
import at.austriapro.mappings.ebinterface.generated.PaymentConditions;
import at.austriapro.mappings.ebinterface.generated.PaymentMethod;
import at.austriapro.mappings.ebinterface.generated.PresentationDetails;
import at.austriapro.mappings.ebinterface.generated.ReductionAndSurchargeBaseType;
import at.austriapro.mappings.ebinterface.generated.ReductionAndSurchargeDetails;
import at.austriapro.mappings.ebinterface.generated.ReductionAndSurchargeType;
import at.austriapro.mappings.ebinterface.generated.RelatedDocument;
import at.austriapro.mappings.ebinterface.generated.Signature;
import at.austriapro.mappings.ebinterface.generated.Tax;
import at.austriapro.mappings.ebinterface.generated.VATItem;
import at.austriapro.mappings.zugferd.generated.AmountType;
import at.austriapro.mappings.zugferd.generated.CodeType;
import at.austriapro.mappings.zugferd.generated.CountryIDType;
import at.austriapro.mappings.zugferd.generated.CreditorFinancialAccountType;
import at.austriapro.mappings.zugferd.generated.CreditorFinancialInstitutionType;
import at.austriapro.mappings.zugferd.generated.CrossIndustryDocumentType;
import at.austriapro.mappings.zugferd.generated.DateTimeType;
import at.austriapro.mappings.zugferd.generated.DebtorFinancialAccountType;
import at.austriapro.mappings.zugferd.generated.DebtorFinancialInstitutionType;
import at.austriapro.mappings.zugferd.generated.DocumentCodeType;
import at.austriapro.mappings.zugferd.generated.DocumentContextParameterType;
import at.austriapro.mappings.zugferd.generated.DocumentLineDocumentType;
import at.austriapro.mappings.zugferd.generated.ExchangedDocumentContextType;
import at.austriapro.mappings.zugferd.generated.ExchangedDocumentType;
import at.austriapro.mappings.zugferd.generated.IDType;
import at.austriapro.mappings.zugferd.generated.IndicatorType;
import at.austriapro.mappings.zugferd.generated.MeasureType;
import at.austriapro.mappings.zugferd.generated.NoteType;
import at.austriapro.mappings.zugferd.generated.PaymentMeansCodeType;
import at.austriapro.mappings.zugferd.generated.PercentType;
import at.austriapro.mappings.zugferd.generated.ProductCharacteristicType;
import at.austriapro.mappings.zugferd.generated.QuantityType;
import at.austriapro.mappings.zugferd.generated.ReferencedDocumentType;
import at.austriapro.mappings.zugferd.generated.SpecifiedPeriodType;
import at.austriapro.mappings.zugferd.generated.SupplyChainEventType;
import at.austriapro.mappings.zugferd.generated.SupplyChainTradeAgreementType;
import at.austriapro.mappings.zugferd.generated.SupplyChainTradeDeliveryType;
import at.austriapro.mappings.zugferd.generated.SupplyChainTradeLineItemType;
import at.austriapro.mappings.zugferd.generated.SupplyChainTradeSettlementType;
import at.austriapro.mappings.zugferd.generated.SupplyChainTradeTransactionType;
import at.austriapro.mappings.zugferd.generated.TaxCategoryCodeType;
import at.austriapro.mappings.zugferd.generated.TaxRegistrationType;
import at.austriapro.mappings.zugferd.generated.TaxTypeCodeType;
import at.austriapro.mappings.zugferd.generated.TextType;
import at.austriapro.mappings.zugferd.generated.TradeAddressType;
import at.austriapro.mappings.zugferd.generated.TradeAllowanceChargeType;
import at.austriapro.mappings.zugferd.generated.TradeContactType;
import at.austriapro.mappings.zugferd.generated.TradePartyType;
import at.austriapro.mappings.zugferd.generated.TradePaymentDiscountTermsType;
import at.austriapro.mappings.zugferd.generated.TradePaymentTermsType;
import at.austriapro.mappings.zugferd.generated.TradePriceType;
import at.austriapro.mappings.zugferd.generated.TradeProductType;
import at.austriapro.mappings.zugferd.generated.TradeSettlementMonetarySummationType;
import at.austriapro.mappings.zugferd.generated.TradeSettlementPaymentMeansType;
import at.austriapro.mappings.zugferd.generated.TradeTaxType;
import at.austriapro.mappings.zugferd.generated.UniversalCommunicationType;
import at.austriapro.utils.DocumentTypeUtils;
import at.austriapro.utils.ISO639Util;

/**
 * Performs mappings from/to ZUGFeRD
 */
public class ZUGFeRDMapping extends Mapping {

  private MappingFactory.MappingType mappingType;
  private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");
  private DateTimeFormatter
      issueDateTimeFormatter =
      DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

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

    //ebInterface: ROOT element attributes
    mapRootAttributes(zugferd, invoice);

    //ebInterface: Signature
    mapSignature(zugferd, invoice.getSignature());

    //ebInterface: Cancelled original document
    //ZUGFeRD exdended: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument
    //ZUGFeRD basic and comfort: IncludedNote
    mapCancelledOriginalDocument(zugferd, invoice.getCancelledOriginalDocument());

    //ebInterface: Related documents
    //ZUGFeRD exdended: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument
    //ZUGFeRD basic and comfort: IncludedNote
    mapRelatedDocuments(zugferd, invoice.getRelatedDocuments());

    //ebInterface: Delivery
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/ShipToTradeParty
    mapDelivery(zugferd, invoice.getDelivery());

    //ebInterface: Biller
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/SellerTradeParty
    mapBiller(zugferd, invoice.getBiller());

    //ebInterface: Invoice Recipient
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/BuyerTradeParty
    mapInvoiceRecipient(zugferd, invoice.getInvoiceRecipient());

    //ebInterface: Ordering Party
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/ProductEndUserTradeParty
    mapOrderingParty(zugferd, invoice.getOrderingParty());

    //ebInterface: Details
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem
    mapDetails(zugferd, invoice.getDetails());

    //ebInterface: ReductionAndSurchargeDetails
    mapReductionAndSurchargeDetails(zugferd, invoice.getReductionAndSurchargeDetails());

    //ebInterface: Tax
    mapTax(zugferd, invoice.getTax());

    //ebInterface: TotalGrossAmount
    mapTotalGrossAmount(zugferd, invoice.getTotalGrossAmount());

    //ebInterface: PayableAmount
    mapPayableAmount(zugferd, invoice.getPayableAmount());

    //ebInterface: PaymentMethod
    mapPaymentMethod(zugferd, invoice.getPaymentMethod());

    //ebInterface: PaymentConditions
    mapPaymentConditions(zugferd, invoice.getPaymentConditions());

    //ebInterface: PresentationDetails
    mapPresentationDetails(zugferd, invoice.getPresentationDetails());

    //ebInterface: Comment
    mapComment(zugferd, invoice.getComment());

    return zugferd;
  }


  /**
   * Map details of ebInterface comments
   */
  private void mapComment(CrossIndustryDocumentType zugferd, String comment) {
    //ebInterface: /Invoice/Comment
    if (comment != null) {
      zugferd.getHeaderExchangedDocument()
          .withIncludedNote(new NoteType().withContent(new TextType().withValue(comment)));
    }
  }


  /**
   * Map presentation details
   */
  private void mapPresentationDetails(CrossIndustryDocumentType zugferd,
                                      PresentationDetails presentationDetails) {
    //ebInterface: /Invoice/PresentationDetails
    if (presentationDetails != null) {
      if (presentationDetails.getURL() != null) {
        //ebInterface: /Invoice/PresentationDetails/URL
        //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(new CodeType().withValue("PresentationDetails/URL"))
                .withContent(new TextType().withValue(presentationDetails.getURL())));
        mLog.add(
            "PresentationDetails/URL does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/PresentationDetails/URL",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }

      if (presentationDetails.getLogoURL() != null) {
        //ebInterface: /Invoice/PresentationDetails/LogoURL
        //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(new CodeType().withValue("PresentationDetails/LogoURL"))
                .withContent(new TextType().withValue(presentationDetails.getLogoURL())));
        mLog.add(
            "PresentationDetails/LogoURL does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/PresentationDetails/LogoURL",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }

      if (presentationDetails.getLayoutID() != null) {
        //ebInterface: /Invoice/PresentationDetails/LayoutID
        //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(new CodeType().withValue("PresentationDetails/LayoutID"))
                .withContent(new TextType().withValue(presentationDetails.getLayoutID())));
        mLog.add(
            "PresentationDetails/LayoutID does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/PresentationDetails/LayoutID",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }

      if (presentationDetails.getSuppressZero() != null) {
        //ebInterface: /Invoice/PresentationDetails/SuppressZero
        //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(new CodeType().withValue("PresentationDetails/SuppressZero"))
                .withContent(new TextType().withValue(presentationDetails.getSuppressZero() ? "true" : "false")));
        mLog.add(
            "PresentationDetails/SuppressZero does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/PresentationDetails/SuppressZero",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }

      if (presentationDetails.getPresentationDetailsExtension() != null) {
        //ebInterface: /Invoice/PresentationDetails/PresentationDetailsExtension
        //TODO
        mLog.add(
            "PresentationDetailsExtension not mapped to ZUGFeRD: no definition given",
            "/Invoice/PresentationDetails/SuppressZero",
            "");
      }

    }
  }

  /**
   * Map the details of payment conditions
   */
  private void mapPaymentConditions(CrossIndustryDocumentType zugferd,
                                    PaymentConditions paymentConditions) {
    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
        && paymentConditions != null) {
      String
          documentCurrency =
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

      TradePaymentTermsType stpt;

      if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)
          && paymentConditions.getDiscounts() != null
          && paymentConditions.getDiscounts().size() > 0) {
        //ebInterface: /Invoice/PaymentConditions/Discount
        for (Discount discount : paymentConditions.getDiscounts()) {
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms
          stpt = new TradePaymentTermsType();
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().withSpecifiedTradePaymentTerms(stpt);

          if (paymentConditions.getDueDate() != null) {
            //ebInterface: /Invoice/PaymentConditions/DueDate
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/DueDateDateTime
            stpt.withDueDateDateTime(new DateTimeType()
                                         .withDateTimeString(
                                             new DateTimeType.DateTimeString()
                                                 .withValue(
                                                     dateTimeFormatter
                                                         .print(
                                                             paymentConditions.getDueDate()))
                                                 .withFormat(
                                                     "102")));
          }

          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/ApplicableTradePaymentDiscountTerms
          TradePaymentDiscountTermsType atpd = new TradePaymentDiscountTermsType();
          stpt.withApplicableTradePaymentDiscountTerms(atpd);

          if (discount.getPaymentDate() != null) {
            //ebInterface: /Invoice/PaymentConditions/Discount/PaymentDate
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/ApplicableTradePaymentDiscountTerms/BasisDateTime
            atpd.withBasisDateTime(new DateTimeType()
                                       .withDateTimeString(
                                           new DateTimeType.DateTimeString()
                                               .withValue(
                                                   dateTimeFormatter
                                                       .print(
                                                           discount.getPaymentDate()))
                                               .withFormat(
                                                   "102")));
          }

          if (discount.getBaseAmount() != null) {
            //ebInterface: /Invoice/PaymentConditions/Discount/BaseAmount
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/ApplicableTradePaymentDiscountTerms/BasisAmount
            atpd.withBasisAmount(new AmountType().withValue(discount.getBaseAmount())
                                     .withCurrencyID(documentCurrency));
          }

          if (discount.getPercentage() != null) {
            //ebInterface: /Invoice/PaymentConditions/Discount/Percentage
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/ApplicableTradePaymentDiscountTerms/CalculationPercent
            atpd.withCalculationPercent(new PercentType().withValue(discount.getPercentage()));
          }

          if (discount.getAmount() != null) {
            //ebInterface: /Invoice/PaymentConditions/Discount/Amount
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/ApplicableTradePaymentDiscountTerms/ActualDiscountAmount
            atpd.withActualDiscountAmount(new AmountType().withValue(discount.getAmount())
                                              .withCurrencyID(documentCurrency));
          }

          if (paymentConditions.getMinimumPayment() != null) {
            //ebInterface: /Invoice/PaymentConditions/MinimumPayment
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/PartialPaymentAmount
            stpt.withPartialPaymentAmount(
                new AmountType().withValue(paymentConditions.getMinimumPayment())
                    .withCurrencyID(documentCurrency));
          }

          if (paymentConditions.getComment() != null) {
            //ebInterface: /Invoice/PaymentConditions/Comment
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/Description
            stpt.withDescription(new TextType().withValue(paymentConditions.getComment()));
          }

          if (paymentConditions.getPaymentConditionsExtension() != null) {
            //ebInterface: /Invoice/PaymentConditions/PaymentConditionsExtension
            //TODO
            mLog.add(
                "PaymentConditionsExtension not mapped to ZUGFeRD: no definition given",
                "/Invoice/PaymentConditions/PaymentConditionsExtension",
                "");
          }
        }
      } else {
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms
        stpt = new TradePaymentTermsType();
        zugferd.getSpecifiedSupplyChainTradeTransaction()
            .getApplicableSupplyChainTradeSettlement().withSpecifiedTradePaymentTerms(stpt);

        if (paymentConditions.getDueDate() != null) {
          //ebInterface: /Invoice/PaymentConditions/DueDate
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/DueDateDateTime
          stpt.withDueDateDateTime(new DateTimeType()
                                       .withDateTimeString(
                                           new DateTimeType.DateTimeString()
                                               .withValue(
                                                   dateTimeFormatter
                                                       .print(
                                                           paymentConditions.getDueDate()))
                                               .withFormat(
                                                   "102")));
        }

        if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)
            && paymentConditions.getMinimumPayment() != null) {
          //ebInterface: /Invoice/PaymentConditions/MinimumPayment
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/PartialPaymentAmount
          stpt.withPartialPaymentAmount(
              new AmountType().withValue(paymentConditions.getMinimumPayment())
                  .withCurrencyID(documentCurrency));
        }

        if (paymentConditions.getComment() != null) {
          //ebInterface: /Invoice/PaymentConditions/Comment
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/Description
          stpt.withDescription(new TextType().withValue(paymentConditions.getComment()));
        }

        if (paymentConditions.getPaymentConditionsExtension() != null) {
          //ebInterface: /Invoice/PaymentConditions/PaymentConditionsExtension
          //TODO
          mLog.add(
              "PaymentConditionsExtension not mapped to ZUGFeRD: no definition given",
              "/Invoice/PaymentConditions/PaymentConditionsExtension",
              "");
        }
      }
    }
  }

  /**
   * Map details of payment method
   */
  private void mapPaymentMethod(CrossIndustryDocumentType zugferd, PaymentMethod paymentMethod) {
    if (paymentMethod != null) {
      TradeSettlementPaymentMeansType tspmt = new TradeSettlementPaymentMeansType();

      if (paymentMethod.getDirectDebit() != null) {
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/TypeCode
        tspmt.withTypeCode(new PaymentMeansCodeType().withValue("49"));

        if (paymentMethod.getComment() != null) {
          //ebInterface: /Invoice/PaymentMethod/Comment
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/Information
          tspmt.withInformation(new TextType().withValue(paymentMethod.getComment()));
        }

        if (paymentMethod.getPaymentMethodExtension() != null) {
          //ebInterface: /Invoice/PaymentMethod/PaymentMethodExtension
          //TODO
          mLog.add(
              "PaymentMethodExtension not mapped to ZUGFeRD: no definition given",
              "/Invoice/PaymentMethod/PaymentMethodExtension",
              "");
        }

        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement()
            .withSpecifiedTradeSettlementPaymentMeans(
                tspmt);
      } else if (paymentMethod.getSEPADirectDebit() != null) {
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/TypeCode
        tspmt.withTypeCode(new PaymentMeansCodeType().withValue("49"));

        if (paymentMethod.getComment() != null) {
          //ebInterface: /Invoice/PaymentMethod/Comment
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/Information
          tspmt.withInformation(new TextType().withValue(paymentMethod.getComment()));
        }

        if (paymentMethod.getSEPADirectDebit().getType() != null) {
          //ebInterface: /Invoice/PaymentMethod/SEPADirectDebit/Type
          //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
          zugferd.getHeaderExchangedDocument().withIncludedNote(
              new NoteType().withContentCode(new CodeType().withValue("SEPADirectDebit/Type"))
                  .withContent(new TextType().withValue(paymentMethod.getSEPADirectDebit().getType().value())));
          mLog.add(
              "SEPADirectDebit/Typec does not exist in ZUGFeRD, mapped to IncludedNote",
              "/Invoice/PaymentMethod/SEPADirectDebit/Type",
              "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
        }

        if (paymentMethod.getSEPADirectDebit().getBankAccountOwner() != null) {
          //ebInterface: /Invoice/PaymentMethod/SEPADirectDebit/BankAccountOwner
          //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
          zugferd.getHeaderExchangedDocument().withIncludedNote(
              new NoteType().withContentCode(new CodeType().withValue("SEPADirectDebit/BankAccountOwner"))
                  .withContent(new TextType().withValue(paymentMethod.getSEPADirectDebit().getType().value())));
          mLog.add(
              "SEPADirectDebit/BankAccountOwner does not exist in ZUGFeRD, mapped to IncludedNote",
              "/Invoice/PaymentMethod/SEPADirectDebit/BankAccountOwner",
              "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
        }

        if (paymentMethod.getSEPADirectDebit().getMandateReference() != null) {
          //ebInterface: /Invoice/PaymentMethod/SEPADirectDebit/MandateReference
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/ID
          tspmt.withID(
              new IDType().withValue(paymentMethod.getSEPADirectDebit().getMandateReference()));
        }

        if (paymentMethod.getSEPADirectDebit().getCreditorID() != null) {
          //ebInterface: /Invoice/PaymentMethod/SEPADirectDebit/IBAN
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayeePartyCreditorFinancialAccount
          tspmt
              .withPayeePartyCreditorFinancialAccount(new CreditorFinancialAccountType().withIBANID(
                  new IDType().withValue(paymentMethod.getSEPADirectDebit().getCreditorID())));
        }

        if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
          if (paymentMethod.getSEPADirectDebit().getIBAN() != null) {
            //ebInterface: /Invoice/PaymentMethod/SEPADirectDebit/IBAN
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayerPartyDebtorFinancialAccount
            tspmt.withPayerPartyDebtorFinancialAccount(new DebtorFinancialAccountType().withIBANID(
                new IDType().withValue(paymentMethod.getSEPADirectDebit().getIBAN())));
          }

          if (paymentMethod.getSEPADirectDebit().getBIC() != null) {
            //ebInterface: /Invoice/PaymentMethod/SEPADirectDebit/BIC
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayerSpecifiedDebtorFinancialInstitution
            tspmt.withPayerSpecifiedDebtorFinancialInstitution(new DebtorFinancialInstitutionType()
                                                                   .withBICID(
                                                                       new IDType().withValue(
                                                                           paymentMethod
                                                                               .getSEPADirectDebit()
                                                                               .getBIC())));
          }

          if (paymentMethod.getSEPADirectDebit().getDebitCollectionDate() != null) {
            //ebInterface: /Invoice/PaymentMethod/SEPADirectDebit/DebitCollectionDate
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/BillingSpecifiedPeriod
            zugferd.getSpecifiedSupplyChainTradeTransaction()
                .getApplicableSupplyChainTradeSettlement().withBillingSpecifiedPeriod(
                new SpecifiedPeriodType().withStartDateTime(new DateTimeType()
                                                                .withDateTimeString(
                                                                    new DateTimeType.DateTimeString()
                                                                        .withValue(
                                                                            dateTimeFormatter
                                                                                .print(
                                                                                    paymentMethod
                                                                                        .getSEPADirectDebit()
                                                                                        .getDebitCollectionDate()))
                                                                        .withFormat(
                                                                            "102")))
                    .withEndDateTime(new DateTimeType()
                                         .withDateTimeString(
                                             new DateTimeType.DateTimeString()
                                                 .withValue(
                                                     dateTimeFormatter
                                                         .print(
                                                             paymentMethod
                                                                 .getSEPADirectDebit()
                                                                 .getDebitCollectionDate()))
                                                 .withFormat(
                                                     "102"))));
          }
        }

        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement()
            .withSpecifiedTradeSettlementPaymentMeans(
                tspmt);
      } else if (paymentMethod.getUniversalBankTransaction() != null) {
        for (BeneficiaryAccount ba : paymentMethod.getUniversalBankTransaction()
            .getBeneficiaryAccounts()) {
          tspmt = new TradeSettlementPaymentMeansType();

          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/TypeCode
          tspmt.withTypeCode(new PaymentMeansCodeType().withValue("31"));

          if (paymentMethod.getComment() != null) {
            //ebInterface: /Invoice/PaymentMethod/Comment
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/Information
            tspmt.withInformation(new TextType().withValue(paymentMethod.getComment()));
          }

          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayeePartyCreditorFinancialAccount
          CreditorFinancialAccountType cfa = new CreditorFinancialAccountType();
          tspmt
              .withPayeePartyCreditorFinancialAccount(cfa);

          if (ba.getIBAN() != null) {
            //ebInterface: /Invoice/PaymentMethod/UniversalBankTransaction/BeneficiaryAccount/IBAN
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayeePartyCreditorFinancialAccount/IBANID
            cfa.withIBANID(
                new IDType().withValue(ba.getIBAN()));
          }

          if (ba.getBankAccountOwner() != null) {
            //ebInterface: /Invoice/PaymentMethod/UniversalBankTransaction/BeneficiaryAccount/BankAccountOwner
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayeePartyCreditorFinancialAccount/AccountName
            cfa.withAccountName(new TextType().withValue(ba.getBankAccountOwner()));
          }

          if (ba.getBankAccountNr() != null) {
            //ebInterface: /Invoice/PaymentMethod/UniversalBankTransaction/BeneficiaryAccount/BankAccountNr
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayeePartyCreditorFinancialAccount/ProprietaryID
            cfa.withProprietaryID(new IDType().withValue(ba.getBankAccountNr()));
          }

          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayeePartyCreditorFinancialInstitution
          CreditorFinancialInstitutionType cft = new CreditorFinancialInstitutionType();
          tspmt
              .withPayeeSpecifiedCreditorFinancialInstitution(cft);

          if (ba.getBIC() != null) {
            //ebInterface: /Invoice/PaymentMethod/UniversalBankTransaction/BeneficiaryAccount/BIC
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayeePartyCreditorFinancialInstitution/BICID
            cft.withBICID(new IDType().withValue(ba.getBIC()));
          }

          /*
          if (ba.getBankCode() != null) {
            //ebInterface: /Invoice/PaymentMethod/UniversalBankTransaction/BeneficiaryAccount/BankCode
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayeePartyCreditorFinancialInstitution/GermanBankleitzahlID
            cft.withGermanBankleitzahlID(new IDType().withValue(ba.getBankCode().getValue())));
          }
          */

          if (ba.getBankName() != null) {
            //ebInterface: /Invoice/PaymentMethod/UniversalBankTransaction/BeneficiaryAccount/BankName
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayeePartyCreditorFinancialInstitution/Name
            cft.withName(new TextType().withValue(ba.getBankName()));
          }

          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement()
              .withSpecifiedTradeSettlementPaymentMeans(
                  tspmt);
        }

        if (paymentMethod.getUniversalBankTransaction().getPaymentReference().getValue() != null) {
          //ebInterface: /Invoice/PaymentMethod/UniversalBankTransaction/PaymentReference
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/PaymentReference
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement()
              .withPaymentReference(new TextType().withValue(
                  paymentMethod.getUniversalBankTransaction().getPaymentReference().getValue()));
        }
      }
    }
  }

  /**
   * Map details of payable amount
   */
  private void mapPayableAmount(CrossIndustryDocumentType zugferd, BigDecimal payableAmount) {
    //ebInterface: /Invoice/PayableAmount
    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
        && payableAmount != null) {
      TradeSettlementMonetarySummationType stsms;
      if (zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement()
              .getSpecifiedTradeSettlementMonetarySummation() == null) {
        stsms = new TradeSettlementMonetarySummationType();
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement()
            .withSpecifiedTradeSettlementMonetarySummation(stsms);
      } else {
        stsms =
            zugferd.getSpecifiedSupplyChainTradeTransaction()
                .getApplicableSupplyChainTradeSettlement()
                .getSpecifiedTradeSettlementMonetarySummation();
      }

      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementMonetarySummation/DuePayableAmount
      stsms.withDuePayableAmount(new AmountType().withValue(payableAmount).withCurrencyID(
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue()));
    }

  }

  /**
   * ebInterface total gross details
   */
  private void mapTotalGrossAmount(CrossIndustryDocumentType zugferd, BigDecimal totalGrossAmount) {
    //ebInterface: /Invoice/TotalGrossAmount
    if (totalGrossAmount != null) {
      TradeSettlementMonetarySummationType stsms;
      if (zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement()
              .getSpecifiedTradeSettlementMonetarySummation() == null) {
        stsms = new TradeSettlementMonetarySummationType();
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement()
            .withSpecifiedTradeSettlementMonetarySummation(stsms);
      } else {
        stsms =
            zugferd.getSpecifiedSupplyChainTradeTransaction()
                .getApplicableSupplyChainTradeSettlement()
                .getSpecifiedTradeSettlementMonetarySummation();
      }

      String documentCurrency = zugferd.getSpecifiedSupplyChainTradeTransaction()
          .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementMonetarySummation/GrandTotalAmount
      stsms.withGrandTotalAmount(new AmountType().withValue(totalGrossAmount).withCurrencyID(
          documentCurrency));

      BigDecimal totalLineAmount = new BigDecimal(0);
      BigDecimal totalChargeAmount = new BigDecimal(0);
      BigDecimal totalAllowanceAmount = new BigDecimal(0);
      BigDecimal totalTaxBasisAmount = new BigDecimal(0);
      BigDecimal totalTaxAmount = new BigDecimal(0);

      for (SupplyChainTradeLineItemType items : zugferd.getSpecifiedSupplyChainTradeTransaction()
          .getIncludedSupplyChainTradeLineItem()) {
        TradeSettlementMonetarySummationType
            monSum =
            items.getSpecifiedSupplyChainTradeSettlement()
                .getSpecifiedTradeSettlementMonetarySummation();
        if (monSum.getLineTotalAmount() != null && monSum.getLineTotalAmount().size() > 0) {
          totalLineAmount = totalLineAmount.add(monSum.getLineTotalAmount().get(0).getValue());
        }

        if (items.getSpecifiedSupplyChainTradeAgreement()
                .getGrossPriceProductTradePrice().get(0).getAppliedTradeAllowanceCharge() != null
            && items.getSpecifiedSupplyChainTradeAgreement()
                   .getGrossPriceProductTradePrice().get(0).getAppliedTradeAllowanceCharge().size()
               > 0) {
          for (TradeAllowanceChargeType tac : items.getSpecifiedSupplyChainTradeAgreement()
              .getGrossPriceProductTradePrice().get(0).getAppliedTradeAllowanceCharge()) {
            if (tac.getChargeIndicator().getIndicator()) {
              totalChargeAmount =
                  totalChargeAmount.add(tac.getActualAmount().get(0).getValue());
            } else {
              totalAllowanceAmount =
                  totalAllowanceAmount.add(tac.getActualAmount().get(0).getValue());
            }
          }
        }

        if (items.getSpecifiedSupplyChainTradeSettlement()
                .getApplicableTradeTax() != null && items.getSpecifiedSupplyChainTradeSettlement()
                                                        .getApplicableTradeTax().size() > 0) {
          for (TradeTaxType tt : items.getSpecifiedSupplyChainTradeSettlement()
              .getApplicableTradeTax()) {
            if (!tt.getCategoryCode().getValue().equals("E")) {
              totalTaxBasisAmount =
                  totalTaxBasisAmount.add(monSum.getLineTotalAmount().get(
                      0).getValue());
              totalTaxAmount = totalTaxAmount.add(monSum.getLineTotalAmount().get(
                  0).getValue().divide(new BigDecimal(100))
                                                      .multiply(
                                                          tt.getApplicablePercent().getValue())
                                                      .setScale(2, BigDecimal.ROUND_HALF_UP));
            }
          }
        }
      }

      if (zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().getSpecifiedTradeAllowanceCharge() != null
          && zugferd.getSpecifiedSupplyChainTradeTransaction()
                 .getApplicableSupplyChainTradeSettlement().getSpecifiedTradeAllowanceCharge()
                 .size()
             > 0) {
        for (TradeAllowanceChargeType tac : zugferd.getSpecifiedSupplyChainTradeTransaction()
            .getApplicableSupplyChainTradeSettlement().getSpecifiedTradeAllowanceCharge()) {
          if (tac.getChargeIndicator().getIndicator()) {
            totalChargeAmount =
                totalChargeAmount.add(tac.getActualAmount().get(0).getValue());
          } else {
            totalAllowanceAmount =
                totalAllowanceAmount.add(tac.getActualAmount().get(0).getValue());
          }
        }
      }

      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementMonetarySummation/LineTotalAmount
      stsms.withLineTotalAmount(
          new AmountType().withValue(totalLineAmount).withCurrencyID(documentCurrency
          ));

      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementMonetarySummation/ChargeTotalAmount
      stsms.withChargeTotalAmount(
          new AmountType().withValue(totalChargeAmount).withCurrencyID(documentCurrency
          ));

      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementMonetarySummation/AllowanceTotalAmount
      stsms.withAllowanceTotalAmount(
          new AmountType().withValue(totalAllowanceAmount).withCurrencyID(documentCurrency
          ));

      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementMonetarySummation/TaxBasisTotalAmount
      stsms.withTaxBasisTotalAmount(
          new AmountType().withValue(totalTaxBasisAmount).withCurrencyID(documentCurrency
          ));

      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementMonetarySummation/TaxTotalAmount
      stsms.withTaxTotalAmount(
          new AmountType().withValue(totalTaxAmount).withCurrencyID(documentCurrency
          ));
    }
  }


  /**
   * Map the tax details to the ZUGFeRD equivalent
   */
  private void mapTax(CrossIndustryDocumentType zugferd, Tax tax) {
    //ebInterface: /Invoice/Tax
    if (tax != null) {
      String
          documentCurrency =
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

      //ebInterface: /Invoice/Tax/VAT
      if (tax.getVAT() != null && tax.getVAT().getVATItems().size() > 0) {

        //ebInterface: /Invoice/Tax/VAT/VATItem
        for (VATItem vATItems : tax.getVAT().getVATItems()) {
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/ApplicableTradeTax
          TradeTaxType tradeTaxType = new TradeTaxType();
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().withApplicableTradeTax(tradeTaxType);

          //ebInterface: /Invoice/Tax/VAT/VATItem/TaxedAmount
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/ApplicableTradeTax/BasisAmount
          if (vATItems.getTaxedAmount() != null) {
            tradeTaxType.withBasisAmount(new AmountType().withValue(vATItems.getTaxedAmount())
                                             .withCurrencyID(documentCurrency));
          }

          //ebInterface: /Invoice/Tax/VAT/VATItem/VATRate
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/ApplicableTradeTax/ApplicablePercent
          if (vATItems.getVATRate() != null) {
            tradeTaxType.withApplicablePercent(
                new PercentType()
                    .withValue(vATItems.getVATRate().getValue()));
          }

          if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/ApplicableTradeTax/ExcemptionReason
            if (vATItems.getTaxExemption() != null) {
              String addCode = "";

              //ebInterface: /Invoice/Tax/TaxException/TaxExemptionCode
              if (vATItems.getTaxExemption().getTaxExemptionCode() != null) {
                addCode = vATItems.getTaxExemption().getTaxExemptionCode();
              }

              //ebInterface: /Invoice/Tax/TaxException
              tradeTaxType.withExemptionReason(
                  new TextType().withValue(addCode + " " + vATItems.getTaxExemption().getValue()));
            }
          }

          //ebInterface: /Invoice/Tax/VAT/VATItem/Amount
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/ApplicableTradeTax/CalculatedAmount
          if (vATItems.getAmount() != null) {
            tradeTaxType.withCalculatedAmount(
                new AmountType().withValue(vATItems.getAmount())
                    .withCurrencyID(documentCurrency));
          }

          //Tax type - always VAT in this case
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/ApplicableTradeTax/TypeCode
          tradeTaxType.withTypeCode(new TaxTypeCodeType().withValue("VAT"));
        }
      }

      //ebInterface: /Invoice/Tax/OtherTax
      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/?
      if (tax.getOtherTaxes() != null && tax.getOtherTaxes().size() > 0) {

        SupplyChainTradeSettlementType
            ascts =
            zugferd.getSpecifiedSupplyChainTradeTransaction()
                .getApplicableSupplyChainTradeSettlement();

        for (OtherTax otherTax : tax.getOtherTaxes()) {
          boolean chargeIndicator;
          BigDecimal amount = null;
          String comment = null;

          //Taxes are surcharge => chargeIndicator: true
          chargeIndicator = true;

          if (otherTax.getAmount() != null) {
            //ebInterface: /Invoice/Tax/OtherTax/Amount
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/ActualAmount
            amount = otherTax.getAmount();
          }

          if (otherTax.getComment() != null) {
            //ebInterface: /Invoice/Tax/OtherTax/Comment
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/Reason
            comment = otherTax.getComment();
          }

          //Create TradeAllowanceCharge and add it to ZUGFeRD
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge
          ascts.withSpecifiedTradeAllowanceCharge(
              getTradeAllowanceCharge(chargeIndicator, null, documentCurrency,
                                      null,
                                      amount, comment));
        }
      }
    }
  }

  /**
   * Map the different reductions and surcharges in ebInterface to the respective fields in ZUGFeRD
   */
  private void mapReductionAndSurchargeDetails(CrossIndustryDocumentType zugferd,
                                               ReductionAndSurchargeDetails reductionAndSurchargeDetails) {
    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
        && reductionAndSurchargeDetails != null) {
      //ebInterface: /Invoice/ReductionAndSurchargeDetails
      if (reductionAndSurchargeDetails.getReductionsAndSurchargesAndOtherVATableTaxes() != null
          && !reductionAndSurchargeDetails.getReductionsAndSurchargesAndOtherVATableTaxes()
          .isEmpty()) {

        String
            documentCurrency =
            zugferd.getSpecifiedSupplyChainTradeTransaction()
                .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

        SupplyChainTradeSettlementType
            ascts =
            zugferd.getSpecifiedSupplyChainTradeTransaction()
                .getApplicableSupplyChainTradeSettlement();

        for (Serializable rSVItem : reductionAndSurchargeDetails
            .getReductionsAndSurchargesAndOtherVATableTaxes()) {
          boolean chargeIndicator;
          BigDecimal baseAmount = null;
          BigDecimal percentage = null;
          BigDecimal amount = null;
          String comment = null;

          //Create TradeAllowanceCharge
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge
          TradeAllowanceChargeType stac;

          if (rSVItem instanceof OtherVATableTax) {
            //ebInterface: /Invoice/ReductionAndSurchargeDetails/OtherVATableTax
            OtherVATableTax
                oVatItem =
                (OtherVATableTax) rSVItem;

            //Taxes are surcharges => chargeIndicator: true
            chargeIndicator = true;

            if (oVatItem.getBaseAmount() != null) {
              //ebInterface: /Invoice/ReductionAndSurchargeDetails/OtherVATableTax/BaseAmount
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/BasisAmount
              baseAmount = oVatItem.getBaseAmount();
            }

            if (oVatItem.getPercentage() != null) {
              //ebInterface: /Invoice/ReductionAndSurchargeDetails/OtherVATableTax/Percentage
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/CalculationPercent
              percentage = oVatItem.getPercentage();
            }

            if (oVatItem.getAmount() != null) {
              //ebInterface: /Invoice/ReductionAndSurchargeDetails/OtherVATableTax/Amount
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/ActualAmount
              amount = oVatItem.getAmount();
            }

            if (oVatItem.getComment() != null) {
              //ebInterface: /Invoice/ReductionAndSurchargeDetails/OtherVATableTax/TaxID
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/Reason
              comment = oVatItem.getComment() + "\n";
            }

            if (oVatItem.getComment() != null) {
              //ebInterface: /Invoice/ReductionAndSurchargeDetails/OtherVATableTax/Comment
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/Reason
              comment += oVatItem.getComment();
            }

            stac =
                getTradeAllowanceCharge(chargeIndicator, baseAmount, documentCurrency,
                                        percentage,
                                        amount, comment.trim());

            if (oVatItem.getVATRate() != null) {
              //ebInterface: /Invoice/ReductionAndSurchargeDetails/OtherVATableTax/VATRate
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/CategoryTradeTax
              stac.withCategoryTradeTax(
                  new TradeTaxType().withTypeCode(new TaxTypeCodeType().withValue("VAT"))
                      .withCategoryCode(new TaxCategoryCodeType().withValue("S"))
                      .withApplicablePercent(
                          new PercentType().withValue(oVatItem.getVATRate().getValue())));
            }
          } else {
            //ebInterface: /Invoice/ReductionAndSurchargeDetails/Reduction
            //and
            //ebInterface: /Invoice/ReductionAndSurchargeDetails/Surcharge
            JAXBElement<? extends Serializable>
                jabxItem =
                (JAXBElement<? extends Serializable>) rSVItem;

            ReductionAndSurchargeType
                rsItem =
                (ReductionAndSurchargeType) jabxItem.getValue();

            //Reduction (ReductionListLineItem) => chargeIndicator: false
            //Surcharge (SurchargeListLineItem) => chargeIndicator: true
            chargeIndicator =
                jabxItem.getName().getLocalPart()
                    .equals("Surcharge");

            if (rsItem.getBaseAmount() != null) {
              //ebInterface: /Invoice/ReductionAndSurchargeDetails/.../BaseAmount
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/BasisAmount
              baseAmount = rsItem.getBaseAmount();
            }

            if (rsItem.getPercentage() != null) {
              //ebInterface: /Invoice/ReductionAndSurchargeDetails/.../Percentage
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/CalculationPercent
              percentage = rsItem.getPercentage();
            }

            if (rsItem.getAmount() != null) {
              //ebInterface: /Invoice/ReductionAndSurchargeDetails/.../Amount
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/ActualAmount
              amount = rsItem.getAmount();
            }

            if (rsItem.getComment() != null) {
              //ebInterface: /Invoice/ReductionAndSurchargeDetails/.../Comment
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/Reason
              comment = rsItem.getComment();
            }

            stac =
                getTradeAllowanceCharge(chargeIndicator, baseAmount, documentCurrency,
                                        percentage,
                                        amount, comment);

            if (rsItem.getVATRate() != null) {
              //ebInterface: /Invoice/ReductionAndSurchargeDetails/.../VATRate
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge/CategoryTradeTax
              stac.withCategoryTradeTax(
                  new TradeTaxType().withTypeCode(new TaxTypeCodeType().withValue("VAT"))
                      .withCategoryCode(new TaxCategoryCodeType().withValue("S"))
                      .withApplicablePercent(new PercentType().withValue(rsItem.getVATRate()
                                                                             .getValue())));
            }
          }

          //add SpecifiedTradeAllowanceCharge to ZUGFeRD
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge
          ascts.withSpecifiedTradeAllowanceCharge(stac);
        }
      }
    }
  }

  /**
   * Map the details section of ebInterace, containing the different line items, to the correct
   * fields in ZUGFeRD from: ebInterface: Details to: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem
   */
  private void mapDetails(CrossIndustryDocumentType zugferd, Details details) {
    //ebInterface: /Invoice/Details
    if (details != null) {
      if (details.getHeaderDescription() != null) {
        //ebInterface: /Invoice/Detail/HeaderDescription
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(new CodeType().withValue("Detail/HeaderDescription"))
                .withContent(new TextType().withValue(details.getHeaderDescription())));
        mLog.add(
            "Detail/HeaderDescription does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/Detail/HeaderDescription",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }

      if (details.getFooterDescription() != null) {
        //ebInterface: /Invoice/Detail/FooterDescription
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(new CodeType().withValue("Detail/FooterDescription"))
                .withContent(new TextType().withValue(details.getFooterDescription())));
        mLog.add(
            "Detail/FooterDescription does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/Detail/FooterDescription",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }

      if (details.getItemLists() != null && details.getItemLists().size() > 0) {
        String
            documentCurrency =
            zugferd.getSpecifiedSupplyChainTradeTransaction()
                .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

        //Create a collection of SupplyChainTradeLineItems
        List<SupplyChainTradeLineItemType>
            listSCTLI =
            new ArrayList<SupplyChainTradeLineItemType>();
        TreeSet<BigInteger> posNr = new TreeSet();

        int iList = 0;

        //ebInterface: loop all /Invoice/Details/ItemList
        for (ItemList itemList : details.getItemLists()) {
          NoteType listHeaderDescription = null;
          NoteType listFooterDescription = null;

          if (itemList.getHeaderDescription() != null) {
            //ebInterface: /Invoice/Detail/ItemList/HeaderDescription
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
            listHeaderDescription = new NoteType().withContentCode(new CodeType().withValue("ItemList/HeaderDescription"))
                .withContent(new TextType().withValue(itemList.getHeaderDescription()));
            mLog.add(
                "Detail/ItemList/HeaderDescription does not exist in ZUGFeRD, mapped to IncludedNote",
                "/Invoice/Detail/ItemList/HeaderDescription",
                "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
          }

          if (itemList.getFooterDescription() != null) {
            //ebInterface: /Invoice/Detail/ItemList/FooterDescription
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
            listFooterDescription = new NoteType().withContentCode(new CodeType().withValue("ItemList/FooterDescription"))
                .withContent(new TextType().withValue(itemList.getFooterDescription()));
            mLog.add(
                "Detail/ItemList/FooterDescription does not exist in ZUGFeRD, mapped to IncludedNote",
                "/Invoice/Detail/ItemList/FooterDescription",
                "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
          }

          if (itemList.getListLineItems() != null && itemList.getListLineItems().size() > 0) {
            int iItems = 0;

            //ebInterface: loop all /Invoice/Details/ItemLists/ListLineItem
            for (ListLineItem item : itemList.getListLineItems()) {
              //Create a SupplyChainTradeLineItem for a Detail
              SupplyChainTradeLineItemType sctli = new SupplyChainTradeLineItemType();

              //create a SpecifiedTradeProduct
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct
              TradeProductType stp = new TradeProductType();
              sctli.withSpecifiedTradeProduct(stp);

              //create a AssociatedDocumentLineDocument
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument
              DocumentLineDocumentType adld = new DocumentLineDocumentType();
              sctli.withAssociatedDocumentLineDocument(adld);

              //Add listHeaderDescription
              if (listHeaderDescription != null){
                adld.withIncludedNote(listHeaderDescription);
              }
              //Add listHeaderDescription
              if (listFooterDescription != null){
                adld.withIncludedNote(listFooterDescription);
              }

              if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/PositionNumber
                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/LineID
                if (item.getPositionNumber() != null) {
                  adld.withLineID(
                      new IDType().withValue(item.getPositionNumber().toString()));
                  posNr.add(item.getPositionNumber());
                } else {
                  BigInteger tPosNr = posNr.last().add(new BigInteger("100"));
                  adld.withLineID(
                      new IDType().withValue(tPosNr.toString()));
                  posNr.add(tPosNr);
                }
              }

              //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Description
              if (item.getDescriptions() != null && item.getDescriptions().size() > 0) {
                StringBuilder zugDesc = new StringBuilder();

                int i = 0;

                //the first description entry will be used for ZUGFeRD.name, the other entries are ZUGFeRD.description
                for (String ebDesc : item.getDescriptions()) {
                  if (i == 0) {
                    stp.withName(new TextType().withValue(ebDesc));
                  } else {
                    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
                      zugDesc.append(ebDesc).append("\n");
                    }
                  }

                  i++;
                }

                if (zugDesc.toString().trim().length() > 0) {
                  stp.withDescription(new TextType().withValue(zugDesc.toString().trim()));
                }
              }

              if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ArticleNumber
                if (item.getArticleNumbers() != null && item.getArticleNumbers().size() > 0) {
                  int iArt = 0;

                  for (ArticleNumber art : item.getArticleNumbers()) {
                    if (art.getArticleNumberType().value().equals("GTIN")) {
                      //GTIN
                      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/GlobalID
                      stp.withGlobalID(
                          new IDType().withValue(art.getContent()).withSchemeID("0160"));
                    } else if (art.getArticleNumberType().value()
                        .equals("InvoiceRecipientsArticleNumber")) {
                      //InvoiceRecipientsArticleNumber
                      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/BuyerAssignedID
                      stp.withBuyerAssignedID(new IDType().withValue(art.getContent()));
                    } else if (art.getArticleNumberType().value().equals("BillersArticleNumber")) {
                      //BillersArticleNumber
                      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/SellerAssignedID
                      stp.withSellerAssignedID(new IDType().withValue(art.getContent()));
                    } else if (art.getArticleNumberType().value().equals("PZN")) {
                      //PZN
                      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
                      adld.withIncludedNote(new NoteType().withContentCode(
                          new CodeType().withValue("ArticleNumber/PZN"))
                                                .withContent(new TextType().withValue(
                                                    art.getContent())));
                      mLog.add(
                          "ArticleNumber 'PZN' does not exist in ZUGFeRD, mapped to IncludedNote",
                          "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                          + "]/ArticleNumber[" + iArt + "]",
                          "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
                    }

                    iArt++;
                  }
                }
              }

              //Create SupplyChainTradeDelivery and add it to ZUGFeRD
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery
              SupplyChainTradeDeliveryType ssctd = new SupplyChainTradeDeliveryType();
              sctli.withSpecifiedSupplyChainTradeDelivery(ssctd);

              //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Quantity
              //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Quantity/@Unit
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery/BilledQuantity
              if (item.getQuantity() != null) {
                ssctd.withBilledQuantity(new QuantityType().withValue(item.getQuantity().getValue())
                                             .withUnitCode(item.getQuantity().getUnit()));
              }

              //Create SpecifiedSupplyChainTradeAgreement and add it to ZUGFeRD
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement
              SupplyChainTradeAgreementType scta = new SupplyChainTradeAgreementType();
              sctli.withSpecifiedSupplyChainTradeAgreement(scta);

              if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
                if (item.getUnitPrice() != null) {
                  //Create NetPriceProductTradePrice and add it to ZUGFeRD
                  TradePriceType npptp = new TradePriceType();
                  scta.withNetPriceProductTradePrice(npptp);

                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/UnitPrice
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/NetPriceProductTradePrice
                  npptp.withChargeAmount(new AmountType().withValue(item.getUnitPrice().getValue())
                                             .withCurrencyID(documentCurrency));

                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/UnitPrice/@BaseQuantity
                  if (item.getUnitPrice().getBaseQuantity() != null) {
                    npptp.withBasisQuantity(
                        new QuantityType().withValue(item.getUnitPrice().getBaseQuantity())
                            .withUnitCode(item.getQuantity().getUnit()));
                  }
                }
              }

              //Create SpecifiedSupplyChainTradeSettlement and add it to ZUGFeRD
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeSettlement
              SupplyChainTradeSettlementType scts = new SupplyChainTradeSettlementType();
              sctli.withSpecifiedSupplyChainTradeSettlement(scts);

              if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
                //Create ApplicableTradeTax and add it to ZUGFeRD
                TradeTaxType att = new TradeTaxType();
                scts.withApplicableTradeTax(att);

                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeSettlement/ApplicableTradeTax/TypeCode
                att.withTypeCode(new TaxTypeCodeType().withValue("VAT"));

                if (item.getTaxExemption() != null) {
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeSettlement/ApplicableTradeTax/ExemptionReason
                  att.withExemptionReason(
                      new TextType().withValue(item.getTaxExemption().getValue()));

                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeSettlement/ApplicableTradeTax/CategoryCode
                  att.withCategoryCode(new TaxCategoryCodeType().withValue("E"));

                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeSettlement/ApplicableTradeTax/ApplicablePercent
                  att.withApplicablePercent(
                      new PercentType().withValue(new BigDecimal(0)));
                } else {
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeSettlement/ApplicableTradeTax/CategoryCode
                  att.withCategoryCode(new TaxCategoryCodeType().withValue("S"));

                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeSettlement/ApplicableTradeTax/ApplicablePercent
                  att.withApplicablePercent(
                      new PercentType().withValue(item.getVATRate().getValue()));
                }
              }

              //Create GrossPriceProductTradePrice and add it to ZUGFeRD
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice
              TradePriceType gpptp = new TradePriceType();
              scta.withGrossPriceProductTradePrice(gpptp);

              //ebInterface: [/Invoice/Details/ItemLists/ListLineItem/] (Quantity/nvl(BaseQuantity, 1)*UnitPrice)
              BigDecimal quantity = item.getQuantity().getValue();
              BigDecimal baseQuantity;
              if (item.getUnitPrice().getBaseQuantity() != null) {
                baseQuantity = item.getUnitPrice().getBaseQuantity();
              } else {
                baseQuantity = new BigDecimal(1);
              }
              BigDecimal unitPrice = item.getUnitPrice().getValue();
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/ChargeAmount
              gpptp.withChargeAmount(
                  new AmountType().withValue(quantity.divide(baseQuantity).multiply(unitPrice))
                      .withCurrencyID(documentCurrency));

              if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {

                if (item.getDiscountFlag() != null) {
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/DiscountFlag
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
                  adld.withIncludedNote(new NoteType().withContentCode(
                      new CodeType().withValue("DiscountFlag"))
                                            .withContent(new TextType().withValue(
                                                item.getDiscountFlag() ? "true" : "false")));
                  mLog.add(
                      "DiscountFlag does not exist in ZUGFeRD, mapped to IncludedNote",
                      "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                      + "]/DiscountFlag",
                      "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails
                if (item
                        .getReductionAndSurchargeListLineItemDetails() != null && item
                                                                                      .getReductionAndSurchargeListLineItemDetails()
                                                                                      .getReductionListLineItemsAndSurchargeListLineItemsAndOtherVATableTaxListLineItems()
                                                                                  != null && item
                                                                                                 .getReductionAndSurchargeListLineItemDetails()
                                                                                                 .getReductionListLineItemsAndSurchargeListLineItemsAndOtherVATableTaxListLineItems()
                                                                                                 .size()
                                                                                             > 0) {
                  for (JAXBElement<? extends Serializable> rSVItem : item
                      .getReductionAndSurchargeListLineItemDetails()
                      .getReductionListLineItemsAndSurchargeListLineItemsAndOtherVATableTaxListLineItems()) {
                    boolean chargeIndicator;
                    BigDecimal baseAmount = null;
                    BigDecimal percentage = null;
                    BigDecimal amount = null;
                    String comment = null;

                    if (rSVItem.getName().getLocalPart().equals("ReductionListLineItem") || rSVItem
                        .getName().getLocalPart().equals("SurchargeListLineItem")) {
                      //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/ReductionListLineItem
                      //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/SurchargeListLineItem
                      ReductionAndSurchargeBaseType
                          rsItem =
                          (ReductionAndSurchargeBaseType) rSVItem.getValue();

                      //Reduction (ReductionListLineItem) => chargeIndicator: false
                      //Surcharge (SurchargeListLineItem) => chargeIndicator: true
                      chargeIndicator =
                          rSVItem.getName().getLocalPart().equals("SurchargeListLineItem");

                      if (rsItem.getBaseAmount() != null) {
                        //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/.../BaseAmount
                        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/BasisAmount
                        baseAmount = rsItem.getBaseAmount();
                      }

                      if (rsItem.getPercentage() != null) {
                        //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/.../Percentage
                        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/CalculationPercent
                        percentage = rsItem.getPercentage();
                      }

                      if (rsItem.getAmount() != null) {
                        //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/.../Amount
                        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/ActualAmount
                        amount = rsItem.getAmount();
                      }

                      if (rsItem.getComment() != null) {
                        //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/.../Comment
                        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/Reason
                        comment = rsItem.getComment();
                      }
                    } else { //rSVItem.getName().getLocalPart().equals("OtherVATableTaxListLineItem")
                      //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/OtherVATableTaxListLineItem
                      OtherVATableTaxBaseType
                          otherTaxItem =
                          (OtherVATableTaxBaseType) rSVItem.getValue();

                      //Taxes are surcharges => chargeIndicator: true
                      chargeIndicator = true;

                      if (otherTaxItem.getBaseAmount() != null) {
                        //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/OtherVATableTaxListLineItem/BaseAmount
                        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/BasisAmount
                        baseAmount = otherTaxItem.getBaseAmount();
                      }

                      if (otherTaxItem.getPercentage() != null) {
                        //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/OtherVATableTaxListLineItem/Percentage
                        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/CalculationPercent
                        percentage = otherTaxItem.getPercentage();
                      }

                      if (otherTaxItem.getAmount() != null) {
                        //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/OtherVATableTaxListLineItem/Amount
                        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/ActualAmount
                        amount = otherTaxItem.getAmount();
                      }

                      if (otherTaxItem.getTaxID() != null) {
                        //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/OtherVATableTaxListLineItem/TaxID
                        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/Reason
                        comment = otherTaxItem.getTaxID() + "\n";
                      }

                      if (otherTaxItem.getComment() != null) {
                        //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/OtherVATableTaxListLineItem/Comment
                        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/Reason
                        comment += otherTaxItem.getComment();
                      }
                    }

                    //Create TradeAllowanceCharge and add it to ZUGFeRD
                    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge
                    gpptp.withAppliedTradeAllowanceCharge(
                        getTradeAllowanceCharge(chargeIndicator, baseAmount, documentCurrency,
                                                percentage,
                                                amount, comment.trim()));
                  }
                }
              }

              //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery
              if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)
                  && item.getDelivery() != null) {
                //Create the necessary elements in ZUGFeRD
                TradePartyType stttp = new TradePartyType();
                ssctd.withShipToTradeParty(stttp);

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/DeliveryID
                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery/DeliveryNoteReferencedDocument/ID
                ssctd.withDeliveryNoteReferencedDocument(new ReferencedDocumentType()
                                                             .withID(new IDType().withValue(
                                                                 item.getDelivery()
                                                                     .getDeliveryID())));

                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery/ActualDeliverySupplyChainEvent/OccurrenceDateTime/udt:DateTimeString
                if (item.getDelivery().getDate() != null) {
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Date
                  ssctd.withActualDeliverySupplyChainEvent(
                      new SupplyChainEventType().withOccurrenceDateTime(
                          new DateTimeType()
                              .withDateTimeString(new DateTimeType.DateTimeString().withValue(
                                  dateTimeFormatter.print(item.getDelivery().getDate())).withFormat(
                                  "102"))));
                } else if (item.getDelivery().getPeriod().getFromDate() != null) {
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Period/FromDate
                  ssctd.withActualDeliverySupplyChainEvent(new SupplyChainEventType()
                                                               .withOccurrenceDateTime(
                                                                   new DateTimeType()
                                                                       .withDateTimeString(
                                                                           new DateTimeType.DateTimeString()
                                                                               .withValue(
                                                                                   dateTimeFormatter
                                                                                       .print(
                                                                                           item.getDelivery()
                                                                                               .getPeriod()
                                                                                               .getFromDate()))
                                                                               .withFormat(
                                                                                   "102"))));
                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address
                if (item.getDelivery().getAddress() != null) {

                  //Create the necessary elements in ZUGFeRD
                  stttp.withDefinedTradeContact(new TradeContactType());
                  stttp.withPostalTradeAddress(new TradeAddressType());

                  Address address = item.getDelivery().getAddress();

                  String partyName = "";
                  //Build the person name string
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address/Salutation
                  if (!Strings.isNullOrEmpty(address.getSalutation())) {
                    partyName += address.getSalutation() + " ";
                  }
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address/Name
                  if (!Strings.isNullOrEmpty(address.getName())) {
                    partyName += address.getName();
                  }

                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery/ShipToTradeParty/Name
                  stttp.withName(new TextType().withValue(partyName));

                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address/Street
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery/ShipToTradeParty/PostalTradeAddress/LineOne
                  stttp.getPostalTradeAddress()
                      .setLineOne(new TextType().withValue(address.getStreet()));

                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address/Town
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery/ShipToTradeParty/PostalTradeAddress/CityName
                  stttp.getPostalTradeAddress()
                      .setCityName(new TextType().withValue(address.getTown()));

                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address/ZIP
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery/ShipToTradeParty/PostalTradeAddress/PostcodeCode
                  stttp.getPostalTradeAddress().getPostcodeCode()
                      .add(new CodeType().withValue(address.getZIP()));

                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address/Country/@CountryCode
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery/ShipToTradeParty/PostalTradeAddress/CountryID
                  stttp.getPostalTradeAddress().setCountryID(
                      new CountryIDType().withValue(address.getCountry().getCountryCode().value()));

                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address/Phone
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery/ShipToTradeParty/DefinedTradeContact/TelephoneUniversalCommunication/CompleteNumber
                  stttp.getDefinedTradeContact().get(0)
                      .withTelephoneUniversalCommunication(
                          new UniversalCommunicationType().withCompleteNumber(
                              new TextType().withValue(address.getPhone())));

                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address/Email
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery/ShipToTradeParty/DefinedTradeContact/EmailURIUniversalCommunication/CompleteNumber
                  stttp.getDefinedTradeContact().get(0)
                      .withEmailURIUniversalCommunication(
                          new UniversalCommunicationType().withURIID(
                              new IDType().withValue(address.getEmail())));

                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address/Contact
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeDelivery/ShipToTradeParty/DefinedTradeContact/PersonName
                  stttp.getDefinedTradeContact().get(0)
                      .setPersonName(new TextType().withValue(address.getContact()));

                  if (item.getDelivery().getAddress().getAddressExtensions() != null) {
                    //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address/AddressExtension
                    //TODO - not in ZUGFeRD
                    mLog.add(
                        "AddressExtensions not mapped to ZUGFeRD: no element in ZUGFeRD",
                        "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                        + "]/Delivery/Address/AddressExtensions",
                        "???");
                  }
                }

                if (item.getDelivery().getDescription() != null) {
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Description
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
                  adld.withIncludedNote(new NoteType().withContentCode(
                      new CodeType().withValue("Description"))
                                            .withContent(new TextType().withValue(
                                                item.getDelivery().getDescription())));
                  mLog.add(
                      "Description does not exist in ZUGFeRD, mapped to IncludedNote",
                      "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                      + "]/Delivery/Description",
                      "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
                }
              }

              //ebInterface: /Invoice/Details/ItemLists/ListLineItem/BillersOrderReference
              if (item.getBillersOrderReference() != null) {
                if (item.getBillersOrderReference().getOrderID() != null) {
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/BillersOrderReference/OrderID
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
                  adld.withIncludedNote(new NoteType().withContentCode(
                      new CodeType().withValue("BillersOrderReference/OrderID"))
                                            .withContent(new TextType().withValue(
                                                item.getBillersOrderReference()
                                                    .getOrderPositionNumber())));
                  mLog.add(
                      "BillersOrderReference/OrderID does not exist in ZUGFeRD, mapped to IncludedNote",
                      "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                      + "]/BillersOrderReference/OrderID",
                      "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
                }

                if (item.getBillersOrderReference().getReferenceDate() != null) {
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/BillersOrderReference/ReferenceDate
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
                  adld.withIncludedNote(new NoteType().withContentCode(
                      new CodeType().withValue("BillersOrderReference/ReferenceDate"))
                                            .withContent(new TextType().withValue(
                                                issueDateTimeFormatter
                                                    .print(
                                                        item.getBillersOrderReference()
                                                            .getReferenceDate()))));
                  mLog.add(
                      "BillersOrderReference/ReferenceDate does not exist in ZUGFeRD, mapped to IncludedNote",
                      "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                      + "]/BillersOrderReference/ReferenceDate",
                      "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
                }

                if (item.getBillersOrderReference().getDescription() != null) {
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/BillersOrderReference/Description
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
                  adld.withIncludedNote(new NoteType().withContentCode(
                      new CodeType().withValue("BillersOrderReference/Description"))
                                            .withContent(new TextType().withValue(
                                                item.getBillersOrderReference().getDescription())));
                  mLog.add(
                      "BillersOrderReference/Description does not exist in ZUGFeRD, mapped to IncludedNote",
                      "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                      + "]/BillersOrderReference/Description",
                      "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
                }

                if (item.getBillersOrderReference().getOrderPositionNumber() != null) {
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/BillersOrderReference/OrderPositionNumber
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
                  adld.withIncludedNote(new NoteType().withContentCode(
                      new CodeType().withValue("BillersOrderReference/OrderPositionNumber"))
                                            .withContent(new TextType().withValue(
                                                item.getBillersOrderReference()
                                                    .getOrderPositionNumber())));
                  mLog.add(
                      "BillersOrderReference/OrderPositionNumber does not exist in ZUGFeRD, mapped to IncludedNote",
                      "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                      + "]/BillersOrderReference/Description",
                      "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
                }
              }

              //ebInterface: /Invoice/Details/ItemLists/ListLineItem/InvoiceRecipientsOrderReference
              if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)
                  && item.getInvoiceRecipientsOrderReference() != null) {

                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/BuyerOrderReferencedDocument
                ReferencedDocumentType bor = new ReferencedDocumentType();
                scta.withBuyerOrderReferencedDocument(bor);

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/InvoiceRecipientsOrderReference/OrderID
                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/BuyerOrderReferencedDocument/OrderID
                if (item.getInvoiceRecipientsOrderReference().getOrderID() != null) {
                  bor.withID(
                      new IDType()
                          .withValue(item.getInvoiceRecipientsOrderReference().getOrderID()));
                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/InvoiceRecipientsOrderReference/OrderPositionNumber
                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/BuyerOrderReferencedDocument/LineID
                if (item.getInvoiceRecipientsOrderReference().getOrderPositionNumber() != null) {
                  bor.withLineID(
                      new IDType().withValue(
                          item.getInvoiceRecipientsOrderReference().getOrderPositionNumber()));
                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/InvoiceRecipientsOrderReference/ReferenceDate
                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/BuyerOrderReferencedDocument/IssueDateTime
                if (item.getInvoiceRecipientsOrderReference().getReferenceDate() != null) {
                  bor.withIssueDateTime(issueDateTimeFormatter
                                            .print(
                                                item.getInvoiceRecipientsOrderReference()
                                                    .getReferenceDate()));
                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/InvoiceRecipientsOrderReference/Description
                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
                if (item.getInvoiceRecipientsOrderReference().getDescription() != null) {
                  adld.withIncludedNote(new NoteType().withContentCode(
                      new CodeType().withValue("InvoiceRecipientsOrderReference/Description"))
                                            .withContent(new TextType().withValue(
                                                item.getInvoiceRecipientsOrderReference()
                                                    .getDescription())));
                  mLog.add(
                      "InvoiceRecipientsOrderReference/Description does not exist in ZUGFeRD, mapped to IncludedNote",
                      "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                      + "]/InvoiceRecipientsOrderReference/Description",
                      "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
                }
              }

              //ebInterface: /Invoice/Details/ItemLists/ListLineItem/AdditionalInformation
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/ApplicableProductCharacteristic
              if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)
                  && item.getAdditionalInformation() != null) {
                AdditionalInformation ai = item.getAdditionalInformation();

                String typeCode, description, unitCode, value;
                BigDecimal valueMeasure = null;

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/AdditionalInformation/SerialNumber
                if (ai.getSerialNumbers() != null && ai.getSerialNumbers().size() > 0) {
                  for (String sn : ai.getSerialNumbers()) {
                    typeCode = "SERIAL_NUMBER";
                    description = "Seriennummer";
                    valueMeasure = null;
                    unitCode = null;
                    value = sn;

                    stp.withApplicableProductCharacteristic(
                        getApplicableProductCharacteristic(typeCode, description, valueMeasure,
                                                           unitCode, value));
                  }
                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/AdditionalInformation/ChargeNumber
                if (ai.getChargeNumbers() != null && ai.getChargeNumbers().size() > 0) {
                  for (String ch : ai.getChargeNumbers()) {
                    typeCode = "LOT_NUMBER";
                    description = "Chargennummer";
                    valueMeasure = null;
                    unitCode = null;
                    value = ch;

                    stp.withApplicableProductCharacteristic(
                        getApplicableProductCharacteristic(typeCode, description, valueMeasure,
                                                           unitCode, value));
                  }
                }

                //ebInterface: loop all /Invoice/Details/ItemLists/ListLineItem/AdditionalInformation/Classification
                if (ai.getClassifications() != null && ai.getClassifications().size() > 0) {
                  for (Classification cl : ai.getClassifications()) {
                    //TODO - Classifications can't be mapped to a typeCode, OTHER is not documented and a placeholder for now
                    typeCode = "OTHER";
                    description = cl.getClassificationSchema();
                    valueMeasure = null;
                    unitCode = null;
                    value = cl.getValue();

                    stp.withApplicableProductCharacteristic(
                        getApplicableProductCharacteristic(typeCode, description, valueMeasure,
                                                           unitCode, value));
                  }
                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/AdditionalInformation/AlternativeQuantity
                if (ai.getAlternativeQuantity() != null) {
                  //TODO - AlternativeQuantity can't be mapped to a typeCode, ALTERNATIVE_QUANTITY is not documented and a placeholder for now
                  typeCode = "ALTERNATIVE_QUANTITY";
                  description = "Alternative Quantity";
                  valueMeasure = ai.getAlternativeQuantity().getValue();
                  unitCode = ai.getAlternativeQuantity().getUnit();
                  value = null;

                  stp.withApplicableProductCharacteristic(
                      getApplicableProductCharacteristic(typeCode, description, valueMeasure,
                                                         unitCode, value));

                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/AdditionalInformation/Size
                if (ai.getSize() != null) {
                  typeCode = "SIZE_TEXT";
                  description = "Größenbezeichnung";
                  valueMeasure = null;
                  unitCode = null;
                  value = ai.getSize();

                  stp.withApplicableProductCharacteristic(
                      getApplicableProductCharacteristic(typeCode, description, valueMeasure,
                                                         unitCode, value));

                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/AdditionalInformation/Weight
                if (ai.getWeight() != null) {
                  typeCode = "WEIGHT_NET";
                  description = "Netto-Gewicht";
                  valueMeasure = ai.getWeight().getValue();
                  if (ai.getWeight().getUnit() != null) {
                    unitCode = ai.getWeight().getUnit();
                  } else {
                    unitCode = null;
                  }
                  value = null;

                  stp.withApplicableProductCharacteristic(
                      getApplicableProductCharacteristic(typeCode, description, valueMeasure,
                                                         unitCode, value));

                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/AdditionalInformation/Boxes
                if (ai.getColor() != null) {
                  //TODO - Boxes can't be mapped to a typeCode, BOXES_QUANTITY is not documented and a placeholder for now
                  typeCode = "BOXES_QUANTITY";
                  description = "Quantity boxes/container";
                  valueMeasure = null;
                  unitCode = null;
                  value = ai.getBoxes().toString();

                  stp.withApplicableProductCharacteristic(
                      getApplicableProductCharacteristic(typeCode, description, valueMeasure,
                                                         unitCode, value));

                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/AdditionalInformation/Color
                if (ai.getColor() != null) {
                  typeCode = "COLOR_TEXT";
                  description = "Farbe als Text";
                  valueMeasure = null;
                  unitCode = null;
                  value = ai.getColor();

                  stp.withApplicableProductCharacteristic(
                      getApplicableProductCharacteristic(typeCode, description, valueMeasure,
                                                         unitCode, value));

                }
              }

              if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
                  && item.getLineItemAmount() != null) {
                //ebInterface: ebInterface: /Invoice/Details/ItemLists/ListLineItem/LineItemAmount
                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeSettlement/SpecifiedTradeSettlementMonetarySummation/LineTotalAmount
                scts.withSpecifiedTradeSettlementMonetarySummation(
                    new TradeSettlementMonetarySummationType().withLineTotalAmount(
                        new AmountType().withValue(item.getLineItemAmount())
                            .withCurrencyID(documentCurrency)));

                if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)
                    && gpptp.getAppliedTradeAllowanceCharge() != null
                    && gpptp.getAppliedTradeAllowanceCharge().size() > 0) {
                  BigDecimal sum = BigDecimal.ZERO;

                  for (TradeAllowanceChargeType ch : gpptp.getAppliedTradeAllowanceCharge()) {
                    BigDecimal am;

                    if (ch.getActualAmount() != null && ch.getActualAmount().size() > 0) {
                      am = ch.getActualAmount().get(0).getValue();
                    } else if (ch.getCalculationPercent() != null && ch.getBasisAmount() != null) {
                      am =
                          ch.getBasisAmount().getValue()
                              .multiply(ch.getCalculationPercent().getValue())
                              .divide(new BigDecimal(100));
                    } else {
                      am = null;
                    }

                    if (am != null) {
                      if (ch.getChargeIndicator().getIndicator()) {
                        sum = sum.subtract(am);
                      } else {
                        sum = sum.add(am);
                      }
                    }
                  }

                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem[/SpecifiedSupplyChainTradeSettlement/SpecifiedTradeSettlementMonetarySummation/TotalAllowanceChargeAmount
                  scts.getSpecifiedTradeSettlementMonetarySummation()
                      .withTotalAllowanceChargeAmount(
                          new AmountType().withValue(sum).withCurrencyID(documentCurrency));
                }
              }

              if (item.getListLineItemExtension() != null
                  && item.getListLineItemExtension().getListLineItemExtension() != null &&
                  item.getListLineItemExtension().getListLineItemExtension()
                      .getBeneficiarySocialInsuranceNumber() != null) {
                //ebInterface: ebInterface: /Invoice/Details/ItemLists/ListLineItem/ListLineItemExtension/ListLineItemExtension/BeneficiarySocialInsuranceNumber
                //TODO - not in ZUGFeRD
                mLog.add(
                    "BeneficiarySocialInsuranceNumber not mapped to ZUGFeRD: no element in ZUGFeRD",
                    "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                    + "]/ListLineItemExtension/ListLineItemExtension/BeneficiarySocialInsuranceNumber",
                    "???");
              }

              //Add SupplyChainTradeLineItem to SupplyChainTradeLineItem list
              listSCTLI.add(sctli);

              iItems++;
            }
          }

          iList++;
        }

        //Add SupplyChainTradeLineItems (List) to ZUGFeRD
        zugferd.getSpecifiedSupplyChainTradeTransaction()
            .withIncludedSupplyChainTradeLineItem(listSCTLI);
      }
    }
  }

  /**
   * Map the attributes from the ebInterface ROOT element
   */
  private void mapRootAttributes(CrossIndustryDocumentType zugferd, Invoice invoice) {
    //ZUGFeRD type
    String zugFeRDType = getZUGfeRDType();
    zugferd.getSpecifiedExchangedDocumentContext().withGuidelineSpecifiedDocumentContextParameter(
        new DocumentContextParameterType().withID(new IDType().withValue(zugFeRDType)));

    //ebInterface: /Invoice/@GeneratingSystem
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
    if (invoice.getGeneratingSystem() != null) {
      zugferd.getHeaderExchangedDocument().withIncludedNote(
          new NoteType().withContentCode(new CodeType().withValue("GeneratingSystem"))
              .withContent(new TextType().withValue(invoice.getGeneratingSystem())));
      mLog.add(
          "GeneratingSystem does not exist in ZUGFeRD, mapped to IncludedNote",
          "/Invoice/@GeneratingSystem",
          "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
    }

    //ebInterface: ebInterface: /Invoice/@DocumentType
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/Name
    String documentType = getDocumentType(invoice);
    zugferd.getHeaderExchangedDocument().withName(new TextType().withValue(documentType));

    //ebInterface: Document type code
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/TypeCode
    String typeCode = getDocumentTypeCode(invoice);
    zugferd.getHeaderExchangedDocument().withTypeCode(new DocumentCodeType().withValue(typeCode));

    //ebInterface: /Invoice/@InvoiceCurrency
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/InvoiceCurrencyCode
    String documentCurrency = invoice.getInvoiceCurrency().value();
    zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement()
        .withInvoiceCurrencyCode(new CodeType().withValue(documentCurrency));

    //ebInterface: /Invoice/@ManualProcessing
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
    if (invoice.getManualProcessing() != null) {
      zugferd.getHeaderExchangedDocument().withIncludedNote(
          new NoteType().withContentCode(new CodeType().withValue("SEPADirectDebit/Type"))
              .withContent(new TextType().withValue(invoice.getManualProcessing() ? "true" : "false")));
      mLog.add(
          "ManualProcessing does not exist in ZUGFeRD, mapped to IncludedNote",
          "/Invoice/ManualProcessing",
          "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
    }

    //ebInterface: /Invoice/@DocumentTitle
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
    if (invoice.getDocumentTitle() != null) {
      zugferd.getHeaderExchangedDocument().withIncludedNote(
          new NoteType().withContentCode(new CodeType().withValue("DocumentTitle"))
              .withContent(new TextType().withValue(invoice.getDocumentTitle())));
      mLog.add(
          "DocumentTitle does not exist in ZUGFeRD, mapped to IncludedNote",
          "/Invoice/@DocumentTitle",
          "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
    }

    //ebInterface: /Invoice/@Language
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/LanguageID
    if (MappingFactory.MappingType.ZUGFeRD_COMFORT_1p0.equals(mappingType)) {
      //Attention: convert from ISO639-2 to ISO639-1!
      zugferd.getHeaderExchangedDocument().getLanguageID()
          .add(new IDType()
                   .withValue(ISO639Util.convertISO639_2ToISO639_1(invoice.getLanguage().value())));
    }

    //ebInterface: /Invoice/@IsDuplicate
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/CopyIndicator
    if (MappingFactory.MappingType.ZUGFeRD_COMFORT_1p0.equals(mappingType)) {
      if (BooleanUtils.isTrue(invoice.getIsDuplicate())) {
        zugferd.getHeaderExchangedDocument()
            .withCopyIndicator(new IndicatorType().withIndicator(Boolean.TRUE));
      }

    }
    //ebInterface: /Invoice/InvoiceNumber
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/ID
    zugferd.getHeaderExchangedDocument().withID(
        new IDType().withValue(invoice.getInvoiceNumber()));

    //ebInterface: /Invoice/InvoiceDate
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IssueDateTime
    zugferd.getHeaderExchangedDocument().withIssueDateTime(new DateTimeType().withDateTimeString(
        new DateTimeType.DateTimeString().withFormat("102")
            .withValue(dateTimeFormatter.print(invoice.getInvoiceDate()))));
  }

  /**
   * Map the signature from the ebInterface
   */
  private void mapSignature(CrossIndustryDocumentType zugferd, Signature signature) {
    //TODO not supported in ZUGFeRD
    //ebInterface: /Invoice/Signature
    if (signature != null) {
      //TODO - no field in ZUGFeRD for that
      mLog.add(
          "Signature not mapped to ZUGFeRD: no element in ZUGFeRD",
          "/Invoice/Signature",
          "???");
    }
  }

  /**
   * Map the ordering party Target in ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/ProductEndUserTradeParty
   */
  private void mapOrderingParty(CrossIndustryDocumentType zugferd, OrderingParty orderingParty) {
    //ebInterface: /Invoice/OrderingParty
    if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)) {
      if (orderingParty == null) {
        LOG.debug("No ordering party element specified in ebInterface - continuing.");
        return;
      }

      //Create a trade party for the invoice recipient
      TradePartyType productEndUserTradeParty = new TradePartyType();
      SupplyChainTradeAgreementType
          supplyChainTradeAgreementType =
          getSupplyChainTradeAgreement(zugferd);
      supplyChainTradeAgreementType.withProductEndUserTradeParty(productEndUserTradeParty);

      //ebInterface: /Invoice/OrderingParty/VATIdentification
      productEndUserTradeParty.withSpecifiedTaxRegistration(new TaxRegistrationType().withID(
          new IDType().withValue(orderingParty.getVATIdentificationNumber()).withSchemeID("VA")));

      if (orderingParty.getFurtherIdentifications() != null
          && orderingParty.getFurtherIdentifications().size() > 0) {

        //ebInterface: /Invoice/OrderingParty/FurtherIdentification
        for (FurtherIdentification furtherIdentification : orderingParty
            .getFurtherIdentifications()) {

          //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
          zugferd.getHeaderExchangedDocument().withIncludedNote(
              new NoteType().withContentCode(
                  new CodeType()
                      .withValue("OrderingParty/" + furtherIdentification.getIdentificationType()))
                  .withContent(new TextType().withValue(furtherIdentification.getValue())));
          mLog.add(
              "FurtherIdentification does not exist in ZUGFeRD, mapped to IncludedNote",
              "/Invoice/OrderingParty/FurtherIdentification",
              "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
        }
      }

      if (orderingParty.getAddress().getAddressIdentifiers() != null
          && orderingParty.getAddress().getAddressIdentifiers().size() > 0) {
        int i = 0;

        for (AddressIdentifier aId : orderingParty.getAddress().getAddressIdentifiers()) {
          //ebInterface: /Invoice/OrderingParty/Address/AddressIdentifier
          String schema;
          if (aId.getAddressIdentifierType()
              .equals(AddressIdentifierTypeType.DUNS)) {
            schema = "0060";
          } else if (aId.getAddressIdentifierType()
              .equals(AddressIdentifierTypeType.GLN)) {
            schema = "0088";
          } else /* (aId.getAddressIdentifierType() == ProprietaryAddressID) */ {
            schema = null;
          }

          if (schema != null) {
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/ProductEndUserTradeParty/GlobalID
            productEndUserTradeParty.withGlobalID(
                new IDType().withValue(
                    aId.getValue())
                    .withSchemeID(schema));
          } else {
            //The first ProprietaryAddressID is ID, if there are more, they become IncludedNotes
            if (i == 0) {
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/ProductEndUserTradeParty/ID
              productEndUserTradeParty
                  .withID(new IDType().withValue(aId.getValue()));
            } else {
              //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
              zugferd.getHeaderExchangedDocument().withIncludedNote(
                  new NoteType().withContentCode(
                      new CodeType().withValue("OrderingParty/ProprietaryAddressID"))
                      .withContent(new TextType().withValue(aId.getValue())));
              mLog.add(
                  "More than one OrderingParty/ProprietaryAddressID found, mapped to IncludedNote",
                  "/Invoice/OrderingParty/Address/AddressIdentifier/@AddressIdentifierType=ProprietaryAddressID",
                  "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
            }
            i++;
          }
        }
      }

      //ebInterface: /Invoice/OrderingParty/OrderReference
      if (orderingParty.getOrderReference() != null) {
        if (orderingParty.getOrderReference().getOrderID() != null) {
          //ebInterface: /Invoice/OrderingParty/OrderReference/OrderID
          //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
          zugferd.getHeaderExchangedDocument().withIncludedNote(
              new NoteType().withContentCode(
                  new CodeType().withValue("OrderingParty/OrderReference/OrderID"))
                  .withContent(
                      new TextType().withValue(orderingParty.getOrderReference().getOrderID())));
          mLog.add(
              "OrderReference/OrderID does not exist in ZUGFeRD, mapped to IncludedNote",
              "/Invoice/OrderingParty/OrderReference/OrderID",
              "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
        }

        if (orderingParty.getOrderReference().getReferenceDate() != null) {
          //ebInterface: /Invoice/OrderingParty/OrderReference/ReferenceDate
          //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
          zugferd.getHeaderExchangedDocument().withIncludedNote(
              new NoteType().withContentCode(
                  new CodeType().withValue("OrderingParty/OrderReference/ReferenceDate"))
                  .withContent(
                      new TextType().withValue(issueDateTimeFormatter
                                                   .print(
                                                       orderingParty.getOrderReference()
                                                           .getReferenceDate()))));
          mLog.add(
              "OrderReference/ReferenceDate does not exist in ZUGFeRD, mapped to IncludedNote",
              "/Invoice/OrderingParty/OrderReference/ReferenceDate",
              "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
        }

        if (orderingParty.getOrderReference().getDescription() != null) {
          //ebInterface: /Invoice/OrderingParty/OrderReference/Description
          //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
          zugferd.getHeaderExchangedDocument().withIncludedNote(
              new NoteType().withContentCode(
                  new CodeType().withValue("OrderingParty/OrderReference/Description"))
                  .withContent(
                      new TextType()
                          .withValue(orderingParty.getOrderReference().getDescription())));
          mLog.add(
              "OrderReference/Description does not exist in ZUGFeRD, mapped to IncludedNote",
              "/Invoice/OrderingParty/OrderReference/Description",
              "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
        }
      }

      //ebInterface: /Invoice/OrderingParty/Address
      //ebInterface: /Invoice/OrderingParty/Address/Street
      //ebInterface: /Invoice/OrderingParty/Address/POBox
      //ebInterface: /Invoice/OrderingParty/Address/Name
      //ebInterface: /Invoice/OrderingParty/Address/ZIP
      //ebInterface: /Invoice/OrderingParty/Address/Contact
      //ebInterface: /Invoice/OrderingParty/Address/Town
      //ebInterface: /Invoice/OrderingParty/Address/CountryCode
      String lineOne = "";
      if (orderingParty.getAddress().getStreet() != null) {
        lineOne = orderingParty.getAddress().getStreet();
      } else {
        lineOne = orderingParty.getAddress().getPOBox();
      }

      productEndUserTradeParty.withName(
          new TextType().withValue(orderingParty.getAddress().getName()));
      productEndUserTradeParty.withPostalTradeAddress(
          new TradeAddressType()
              .withPostcodeCode(
                  new CodeType().withValue(orderingParty.getAddress().getZIP()))
              .withLineOne(new TextType().withValue(lineOne))
              .withLineTwo(
                  new TextType().withValue(orderingParty.getAddress().getContact()))
              .withCityName(
                  new TextType().withValue(orderingParty.getAddress().getTown()))
              .withCountryID(new CountryIDType().withValue(
                  orderingParty.getAddress().getCountry().getCountryCode().value())));

      if (orderingParty.getBillersOrderingPartyID() != null) {
        //ebInterface: /Invoice/OrderingParty/BillersInvoiceRecipientID
        //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(
                new CodeType().withValue("OrderingParty/BillersInvoiceRecipientID"))
                .withContent(
                    new TextType()
                        .withValue(orderingParty.getBillersOrderingPartyID())));
        mLog.add(
            "BillersInvoiceRecipientID does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/OrderingParty/BillersInvoiceRecipientID",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }
    }
  }

  /**
   * Map the invoice recipient Target in ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/BuyerTradeParty
   */
  private void mapInvoiceRecipient(CrossIndustryDocumentType zugferd,
                                   InvoiceRecipient invoiceRecipient) {
    //ebInterface: /Invoice/InvoiceRecipient

    if (invoiceRecipient == null) {
      LOG.debug("No biller element specified in ebInterface - continuing.");
      return;
    }

    //Create a trade party for the invoice recipient
    TradePartyType buyerTradeParty = new TradePartyType();
    SupplyChainTradeAgreementType
        supplyChainTradeAgreementType =
        getSupplyChainTradeAgreement(zugferd);
    supplyChainTradeAgreementType.withBuyerTradeParty(buyerTradeParty);

    //ebInterface: /Invoice/InvoiceRecipient/VATIdentification
    buyerTradeParty.withSpecifiedTaxRegistration(new TaxRegistrationType().withID(
        new IDType().withValue(invoiceRecipient.getVATIdentificationNumber()).withSchemeID("VA")));

    //ebInterface: /Invoice/InvoiceRecipient/FurtherIdentification
    if (invoiceRecipient.getFurtherIdentifications() != null
        && invoiceRecipient.getFurtherIdentifications().size() > 0) {

      //ebInterface: /Invoice/InvoiceRecipient/FurtherIdentification
      for (FurtherIdentification furtherIdentification : invoiceRecipient
          .getFurtherIdentifications()) {

        //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(
                new CodeType()
                    .withValue("InvoiceRecipient/" + furtherIdentification.getIdentificationType()))
                .withContent(new TextType().withValue(furtherIdentification.getValue())));
        mLog.add(
            "FurtherIdentification does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/InvoiceRecipient/FurtherIdentification",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }
    }

    //ebInterface: /Invoice/InvoiceRecipient/Address/AddressIdentifier/@AddressIdentifierType
    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
        && invoiceRecipient.getAddress().getAddressIdentifiers() != null
        && invoiceRecipient.getAddress().getAddressIdentifiers().size() > 0) {
      int i = 0;

      for (AddressIdentifier aId : invoiceRecipient.getAddress().getAddressIdentifiers()) {
        //ebInterface: /Invoice/InvoiceRecipient/Address/AddressIdentifier
        String schema;
        if (aId.getAddressIdentifierType()
            .equals(AddressIdentifierTypeType.DUNS)) {
          schema = "0060";
        } else if (aId.getAddressIdentifierType()
            .equals(AddressIdentifierTypeType.GLN)) {
          schema = "0088";
        } else /* (aId.getAddressIdentifierType() == ProprietaryAddressID) */ {
          schema = null;
        }

        if (schema != null) {
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/BuyerTradeParty/GlobalID
          buyerTradeParty.withGlobalID(
              new IDType().withValue(
                  aId.getValue())
                  .withSchemeID(schema));
        } else {
          //The first ProprietaryAddressID is ID, if there are more, they become IncludedNotes
          if (i == 0) {
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/BuyerTradeParty/ID
            buyerTradeParty
                .withID(new IDType().withValue(aId.getValue()));
          } else {
            //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
            zugferd.getHeaderExchangedDocument().withIncludedNote(
                new NoteType().withContentCode(
                    new CodeType().withValue("InvoiceRecipient/ProprietaryAddressID"))
                    .withContent(new TextType().withValue(aId.getValue())));
            mLog.add(
                "More than one InvoiceRecipient/ProprietaryAddressID found, mapped to IncludedNote",
                "/Invoice/InvoiceRecipient/Address/AddressIdentifier/@AddressIdentifierType=ProprietaryAddressID",
                "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
          }
          i++;
        }
      }
    }

    //ebInterface: /Invoice/InvoiceRecipient/OrderReference
    if (invoiceRecipient.getOrderReference() != null) {
      if (invoiceRecipient.getOrderReference().getOrderID() != null) {
        //ebInterface: /Invoice/InvoiceRecipient/OrderReference/OrderID
        //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(
                new CodeType().withValue("InvoiceRecipient/OrderReference/OrderID"))
                .withContent(
                    new TextType().withValue(invoiceRecipient.getOrderReference().getOrderID())));
        mLog.add(
            "OrderReference/OrderID does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/InvoiceRecipient/OrderReference/OrderID",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }

      if (invoiceRecipient.getOrderReference().getReferenceDate() != null) {
        //ebInterface: /Invoice/InvoiceRecipient/OrderReference/ReferenceDate
        //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(
                new CodeType().withValue("InvoiceRecipient/OrderReference/ReferenceDate"))
                .withContent(
                    new TextType().withValue(issueDateTimeFormatter
                                                 .print(
                                                     invoiceRecipient.getOrderReference()
                                                         .getReferenceDate()))));
        mLog.add(
            "OrderReference/ReferenceDate does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/InvoiceRecipient/OrderReference/ReferenceDate",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }

      if (invoiceRecipient.getOrderReference().getDescription() != null) {
        //ebInterface: /Invoice/OrderingParty/InvoiceRecipient/Description
        //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(
                new CodeType().withValue("InvoiceRecipient/OrderReference/Description"))
                .withContent(
                    new TextType()
                        .withValue(invoiceRecipient.getOrderReference().getDescription())));
        mLog.add(
            "OrderReference/Description does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/InvoiceRecipient/OrderReference/Description",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }
    }

    //ebInterface: /Invoice/InvoiceRecipient/Address
    //ebInterface: /Invoice/InvoiceRecipient/Address/Street
    //ebInterface: /Invoice/InvoiceRecipient/Address/POBox
    //ebInterface: /Invoice/InvoiceRecipient/Address/Name
    //ebInterface: /Invoice/InvoiceRecipient/Address/ZIP
    //ebInterface: /Invoice/InvoiceRecipient/Address/Contact
    //ebInterface: /Invoice/InvoiceRecipient/Address/Town
    //ebInterface: /Invoice/InvoiceRecipient/Address/CountryCode
    String lineOne = "";
    if (invoiceRecipient.getAddress().getStreet() != null) {
      lineOne = invoiceRecipient.getAddress().getStreet();
    } else {
      lineOne = invoiceRecipient.getAddress().getPOBox();
    }

    buyerTradeParty.withName(
        new TextType().withValue(invoiceRecipient.getAddress().getName()));
    buyerTradeParty.withPostalTradeAddress(
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

    if (invoiceRecipient.getBillersInvoiceRecipientID() != null) {
      //ebInterface: /Invoice/InvoiceRecipient/BillersInvoiceRecipientID
      //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
      zugferd.getHeaderExchangedDocument().withIncludedNote(
          new NoteType().withContentCode(
              new CodeType().withValue("InvoiceRecipient/BillersInvoiceRecipientID"))
              .withContent(
                  new TextType()
                      .withValue(invoiceRecipient.getBillersInvoiceRecipientID())));
      mLog.add(
          "BillersInvoiceRecipientID does not exist in ZUGFeRD, mapped to IncludedNote",
          "/Invoice/InvoiceRecipient/BillersInvoiceRecipientID",
          "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
    }

    if (invoiceRecipient.getAccountingArea() != null) {
      //ebInterface: /Invoice/InvoiceRecipient/AccountingArea
      //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
      zugferd.getHeaderExchangedDocument().withIncludedNote(
          new NoteType().withContentCode(
              new CodeType().withValue("InvoiceRecipient/AccountingArea"))
              .withContent(
                  new TextType()
                      .withValue(invoiceRecipient.getAccountingArea())));
      mLog.add(
          "AccountingArea does not exist in ZUGFeRD, mapped to IncludedNote",
          "/Invoice/InvoiceRecipient/AccountingArea",
          "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
    }

    if (invoiceRecipient.getSubOrganizationID() != null) {
      //ebInterface: /Invoice/InvoiceRecipient/SubOrganizationID
      //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
      zugferd.getHeaderExchangedDocument().withIncludedNote(
          new NoteType().withContentCode(
              new CodeType().withValue("InvoiceRecipient/SubOrganizationID"))
              .withContent(
                  new TextType()
                      .withValue(invoiceRecipient.getSubOrganizationID())));
      mLog.add(
          "SubOrganizationID does not exist in ZUGFeRD, mapped to IncludedNote",
          "/Invoice/InvoiceRecipient/SubOrganizationID",
          "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
    }
  }

  /**
   * Map the biller Target in ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/SellerTradeParty
   */
  private void mapBiller(CrossIndustryDocumentType zugferd, Biller biller) {
    //ebInterface: /Invoice/Biller
    if (biller == null) {
      LOG.debug("No blller element specified in ebInterface - continuing.");
      return;
    }

    SupplyChainTradeAgreementType
        supplyChainTradeAgreementType =
        getSupplyChainTradeAgreement(zugferd);
    //CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/SellerTradeParty
    TradePartyType sellerTradePartyType = new TradePartyType();
    supplyChainTradeAgreementType.withSellerTradeParty(sellerTradePartyType);

    //ebInterface: /Invoice/Biller/VATIdentification
    sellerTradePartyType.withSpecifiedTaxRegistration(new TaxRegistrationType().withID(
        new IDType().withValue(biller.getVATIdentificationNumber()).withSchemeID("VA")));

    //ebInterface: /Invoice/Biller/FurtherIdentification
    if (biller.getFurtherIdentifications() != null
        && biller.getFurtherIdentifications().size() > 0) {

      //ebInterface: /Invoice/InvoiceRecipient/FurtherIdentification
      for (FurtherIdentification furtherIdentification : biller
          .getFurtherIdentifications()) {

        //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType().withContentCode(
                new CodeType()
                    .withValue("Biller/" + furtherIdentification.getIdentificationType()))
                .withContent(new TextType().withValue(furtherIdentification.getValue())));
        mLog.add(
            "FurtherIdentification does not exist in ZUGFeRD, mapped to IncludedNote",
            "/Invoice/Biller/FurtherIdentification",
            "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
      }
    }

    //ebInterface: /Invoice/Biller/Address/AddressIdentifier/@AddressIdentifierType
    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
        && biller.getAddress().getAddressIdentifiers() != null
        && biller.getAddress().getAddressIdentifiers().size() > 0) {
      for (AddressIdentifier aId : biller.getAddress().getAddressIdentifiers()) {
        //ebInterface: /Invoice/Biller/Address/AddressIdentifier
        String schema;
        if (aId.getAddressIdentifierType()
            .equals(AddressIdentifierTypeType.DUNS)) {
          schema = "0060";
        } else if (aId.getAddressIdentifierType()
            .equals(AddressIdentifierTypeType.GLN)) {
          schema = "0088";
        } else /* (aId.getAddressIdentifierType() == ProprietaryAddressID) */ {
          schema = null;
        }

        if (schema != null) {
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/SellerTradeParty/GlobalID
          sellerTradePartyType.withGlobalID(
              new IDType().withValue(
                  aId.getValue())
                  .withSchemeID(schema));
        } else {
          //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
          zugferd.getHeaderExchangedDocument().withIncludedNote(
              new NoteType().withContentCode(
                  new CodeType().withValue("Biller/ProprietaryAddressID"))
                  .withContent(new TextType().withValue(aId.getValue())));
          mLog.add(
              "Biller/ProprietaryAddressID found ID is already in use, mapped to IncludedNote",
              "/Invoice/Biller/Address/AddressIdentifier/@AddressIdentifierType=ProprietaryAddressID",
              "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
        }
      }
    }

    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
      if (biller.getOrderReference() != null) {
        //ebInterface: /Invoice/Biller/OrderReference
        if (biller.getOrderReference().getOrderID() != null) {
          //ebInterface: /Invoice/Biller/OrderReference/OrderID
          //CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/BuyerReference
          supplyChainTradeAgreementType
              .withBuyerReference(
                  new TextType().withValue(biller.getOrderReference().getOrderID()));

          //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
          zugferd.getHeaderExchangedDocument().withIncludedNote(
              new NoteType().withContentCode(
                  new CodeType().withValue("Biller/OrderReference/OrderID"))
                  .withContent(
                      new TextType().withValue(biller.getOrderReference().getOrderID())));
        }

        if (biller.getOrderReference().getReferenceDate() != null) {
          //ebInterface: /Invoice/Biller/OrderReference/ReferenceDate
          //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
          zugferd.getHeaderExchangedDocument().withIncludedNote(
              new NoteType().withContentCode(
                  new CodeType().withValue("Biller/OrderReference/ReferenceDate"))
                  .withContent(
                      new TextType().withValue(issueDateTimeFormatter
                                                   .print(
                                                       biller.getOrderReference()
                                                           .getReferenceDate()))));
          mLog.add(
              "OrderReference/ReferenceDate does not exist in ZUGFeRD, mapped to IncludedNote",
              "/Invoice/Biller/OrderReference/ReferenceDate",
              "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
        }

        if (biller.getOrderReference().getDescription() != null) {
          //ebInterface: /Invoice/Biller/OrderReference/Description
          //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
          zugferd.getHeaderExchangedDocument().withIncludedNote(
              new NoteType().withContentCode(
                  new CodeType().withValue("Biller/OrderReference/Description"))
                  .withContent(
                      new TextType()
                          .withValue(biller.getOrderReference().getDescription())));
          mLog.add(
              "OrderReference/Description does not exist in ZUGFeRD, mapped to IncludedNote",
              "/Invoice/Biller/OrderReference/Description",
              "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
        }
      }
    }

    //ebInterface: /Invoice/Biller/Address
    //ebInterface: /Invoice/Biller/Address/Street
    //ebInterface: /Invoice/Biller/Address/POBox
    //ebInterface: /Invoice/Biller/Address/Name
    //ebInterface: /Invoice/Biller/Address/ZIP
    //ebInterface: /Invoice/Biller/Address/Contact
    //ebInterface: /Invoice/Biller/Address/Town
    //ebInterface: /Invoice/Biller/Address/CountryCode
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

    //ebInterface: /Invoice/Biller/InvoiceRecipientsBillerID
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/SellerTradeParty/ID
    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
      if (biller.getInvoiceRecipientsBillerID() != null) {
        sellerTradePartyType.withID(new IDType().withValue(biller.getInvoiceRecipientsBillerID()));
      }
    }

    if (biller.getBillerExtension() != null
        && biller.getBillerExtension().getBillerExtension() != null
        && biller.getBillerExtension().getBillerExtension().getBillersContractPartnerNumber()
           != null) {
      //ebInterface: /Invoice/Biller/BillerExtension/BillerExtension/BillersContractPartnerNumber
      //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
      zugferd.getHeaderExchangedDocument().withIncludedNote(
          new NoteType().withContentCode(
              new CodeType().withValue("BillersContractPartnerNumber"))
              .withContent(
                  new TextType()
                      .withValue(biller.getBillerExtension().getBillerExtension().getBillersContractPartnerNumber())));
      mLog.add(
          "BillersContractPartnerNumber does not exist in ZUGFeRD, mapped to IncludedNote",
          "/Invoice/Biller/BillerExtension/BillerExtension/BillersContractPartnerNumber",
          "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
    }
  }

  /**
   * Map the details of related documents Target in ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument
   */
  private void mapRelatedDocuments(CrossIndustryDocumentType zugferd,
                                   List<RelatedDocument> relatedDocuments) {
    //ebInterface: /Invoice/RelatedDocuments
    if (Iterables.isEmpty(relatedDocuments)) {
      LOG.debug("No related documents specified in ebInterface - continuing");
      return;
    }

    SupplyChainTradeAgreementType
        supplyChainTradeAgreementType =
        getSupplyChainTradeAgreement(zugferd);

    int i = 0;

    for (RelatedDocument relatedDocument : relatedDocuments) {

      if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)) {
        //Create a new related document type and assign it to the supply chain trade agreement

        ReferencedDocumentType referencedDocumentType = new ReferencedDocumentType();
        supplyChainTradeAgreementType.getAdditionalReferencedDocument().add(referencedDocumentType);

        //ebInterface: /Invoice/RelatedDocument/InvoiceNumber
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument/ID
        referencedDocumentType.withID(new IDType().withValue(relatedDocument.getInvoiceNumber()));

        //ebInterface: /Invoice/RelatedDocument/InvoiceDate
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument/IssueDateTime
        referencedDocumentType.withIssueDateTime(
            issueDateTimeFormatter.print(relatedDocument.getInvoiceDate()));

        //ebInterface: /Invoice/RelatedDocument/DocumentType
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument/TypeCode
        //OI = Reference number identifying a previously issued invoice. (http://www.unece.org/trade/untdid/i98a/uncl/uncl1153.htm)
        referencedDocumentType.withTypeCode(
            new DocumentCodeType().withValue("OI"));

        if (relatedDocument.getComment() != null) {
          //ebInterface: /Invoice/RelatedDocument/Comment
          //TODO - not really an element in ZUGFeRD which fits here...
          mLog.add(
              "OrderReference not mapped to ZUGFeRD: no element in ZUGFeRD",
              "/Invoice/RelatedDocument[" + i + "]/Comment",
              "???");
        }
      } else {
        StringBuilder text = new StringBuilder();

        text.append("Zugehörige Rechnung:\n");

        //ebInterface: /Invoice/RelatedDocument/InvoiceNumber
        if (relatedDocument.getInvoiceNumber() != null) {
          text.append(relatedDocument.getInvoiceNumber());
        }

        //ebInterface: /Invoice/RelatedDocument/InvoiceDate
        if (relatedDocument.getInvoiceDate() != null) {
          text.append(dateTimeFormatter.print(relatedDocument.getInvoiceDate())).append("\n");
        }

        //ebInterface: /Invoice/RelatedDocument/Comment
        if (relatedDocument.getComment() != null) {
          text.append(relatedDocument.getComment());
        }

        zugferd.getHeaderExchangedDocument().getIncludedNote()
            .add(new NoteType().withContent(new TextType().withValue(text.toString())));
      }

      i++;
    }
  }

  /**
   * Map the details of the cancelled document Target in ZUGFeRD:
   */
  private void mapCancelledOriginalDocument(CrossIndustryDocumentType zugferd,
                                            CancelledOriginalDocument cancelledOriginalDocument) {
    //ebInterface: /Invoice/CancelledOriginalDocument
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

      //ebInterface: /Invoice/CancelledOriginalDocument/InvoiceNumber
      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument/ID
      referencedDocumentType
          .withID(new IDType().withValue(cancelledOriginalDocument.getInvoiceNumber()));

      //ebInterface: /Invoice/CancelledOriginalDocument/InvoiceDate
      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument/IssueDateTime
      referencedDocumentType.withIssueDateTime(
          issueDateTimeFormatter.print(cancelledOriginalDocument.getInvoiceDate()));

      //ebInterface: /Invoice/CancelledOriginalDocument/DocumentType
      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument/TypeCode
      //ACW = Reference number assigned to the message which was previously issued (e.g. in the case of a cancellation,
      //      the primary reference of the message to be cancelled will be quoted in this element). (http://www.unece.org/trade/untdid/i98a/uncl/uncl1153.htm)
      referencedDocumentType.withTypeCode(
          new DocumentCodeType().withValue("ACW"));

      if (cancelledOriginalDocument.getComment() != null) {
        //ebInterface: /Invoice/CancelledOriginalDocument/Comment
        //TODO - not really an element in ZUGFeRD which fits here...
        mLog.add(
            "CancelledOriginalDocument not mapped to ZUGFeRD: no element in ZUGFeRD",
            "/Invoice/CancelledOriginalDocument/Comment",
            "???");
      }

    } else {
      StringBuilder text = new StringBuilder();

      text.append("Stornierte Rechnung:\n");

      //ebInterface: /Invoice/CancelledOriginalDocument/InvoiceNumber
      if (cancelledOriginalDocument.getInvoiceNumber() != null) {
        text.append(cancelledOriginalDocument.getInvoiceNumber());
      }

      //ebInterface: /Invoice/CancelledOriginalDocument/InvoiceDate
      if (cancelledOriginalDocument.getInvoiceDate() != null) {
        text.append(dateTimeFormatter.print(cancelledOriginalDocument.getInvoiceDate()))
            .append("\n");
      }

      //ebInterface: /Invoice/CancelledOriginalDocument/Comment
      if (cancelledOriginalDocument.getComment() != null) {
        text.append(cancelledOriginalDocument.getComment());
      }

      zugferd.getHeaderExchangedDocument().getIncludedNote()
          .add(new NoteType().withContent(new TextType().withValue(text.toString())));
    }
  }

  /**
   * Map the details of an ebInterface delivery element Target in ZUGFeRD:
   * /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/ShipToTradeParty
   */
  private void mapDelivery(CrossIndustryDocumentType zugferd, Delivery delivery) {
    //ebInterface: /Invoice/Delivery
    if (delivery == null) {
      LOG.debug("No delivery element specified in ebInterface - continuing.");
      return;
    }

    //Create the necessary elements in ZUGFeRD
    zugferd.getSpecifiedSupplyChainTradeTransaction().withApplicableSupplyChainTradeDelivery(
        new SupplyChainTradeDeliveryType().withShipToTradeParty(new TradePartyType()));

    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
        && delivery.getDeliveryID() != null) {
      //ebInterface: /Invoice/Delivery/DeliveryID
      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/DespatchAdviceReferencedDocument/ID
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .withDeliveryNoteReferencedDocument(
              new ReferencedDocumentType()
                  .withID(new IDType().withValue(delivery.getDeliveryID())));
    }

    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/ActualDeliverySupplyChainEvent/OccurrenceDateTime/udt:DateTimeString
    if (delivery.getDate() != null) {
      //ebInterface: /Invoice/Delivery/Date
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .withActualDeliverySupplyChainEvent(new SupplyChainEventType().withOccurrenceDateTime(
              new DateTimeType().withDateTimeString(new DateTimeType.DateTimeString().withValue(
                  dateTimeFormatter.print(delivery.getDate())).withFormat(
                  "102"))));
    } else if (delivery.getPeriod().getFromDate() != null) {
      //ebInterface: /Invoice/Delivery/Period/FromDate
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .withActualDeliverySupplyChainEvent(new SupplyChainEventType().withOccurrenceDateTime(
              new DateTimeType().withDateTimeString(new DateTimeType.DateTimeString().withValue(
                  dateTimeFormatter.print(delivery.getPeriod().getFromDate())).withFormat(
                  "102"))));
    }

    if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)) {
      if (delivery.getAddress() != null) {

        //Create the necessary elements in ZUGFeRD
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().withDefinedTradeContact(new TradeContactType());
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().withPostalTradeAddress(new TradeAddressType());

        //ebInterface: /Invoice/Delivery/Address
        Address address = delivery.getAddress();

        String partyName = "";
        //Build the person name string
        //ebInterface: /Invoice/Delivery/Address/Salutation
        if (!Strings.isNullOrEmpty(address.getSalutation())) {
          partyName += address.getSalutation() + " ";
        }
        //ebInterface: /Invoice/Delivery/Address/Name
        if (!Strings.isNullOrEmpty(address.getName())) {
          partyName += address.getName();
        }

        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/ShipToTradeParty/Name
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().withName(new TextType().withValue(partyName));

        //ebInterface: /Invoice/Delivery/Address/Street
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/ShipToTradeParty/PostalTradeAddress/LineOne
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().getPostalTradeAddress()
            .setLineOne(new TextType().withValue(address.getStreet()));

        //ebInterface: /Invoice/Delivery/Address/Town
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/ShipToTradeParty/PostalTradeAddress/CityName
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().getPostalTradeAddress()
            .setCityName(new TextType().withValue(address.getTown()));

        //ebInterface: /Invoice/Delivery/Address/ZIP
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/ShipToTradeParty/PostalTradeAddress/PostcodeCode
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().getPostalTradeAddress().getPostcodeCode()
            .add(new CodeType().withValue(address.getZIP()));

        //ebInterface: /Invoice/Delivery/Address/CountryCode
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/ShipToTradeParty/PostalTradeAddress/CountryID
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().getPostalTradeAddress().setCountryID(
            new CountryIDType().withValue(address.getCountry().getCountryCode().value()));

        //ebInterface: /Invoice/Delivery/Address/Phone
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/ShipToTradeParty/DefinedTradeContact/TelephoneUniversalCommunication/CompleteNumber
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().getDefinedTradeContact().get(0)
            .withTelephoneUniversalCommunication(
                new UniversalCommunicationType().withCompleteNumber(
                    new TextType().withValue(address.getPhone())));

        //ebInterface: /Invoice/Delivery/Address/Email
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/ShipToTradeParty/DefinedTradeContact/EmailURIUniversalCommunication/CompleteNumber
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().getDefinedTradeContact().get(0)
            .withEmailURIUniversalCommunication(
                new UniversalCommunicationType().withURIID(
                    new IDType().withValue(address.getEmail())));

        //ebInterface: /Invoice/Delivery/Address/Contact
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeDelivery/ShipToTradeParty/DefinedTradeContact/PersonName
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().getDefinedTradeContact().get(0)
            .setPersonName(new TextType().withValue(address.getContact()));

        if (address.getAddressExtensions() != null) {
          //ebInterface: /Invoice/Delivery/Address/AddressExtension
          //TODO - no field in ZUGFeRD for that
          mLog.add(
              "AddressExtensions not mapped to ZUGFeRD: no element in ZUGFeRD",
              "/Invoice/Delivery/Address/AddressExtensions",
              "???");
        }
      }
    }

    if (delivery.getDescription() != null) {
      //ebInterface: /Invoice/Delivery/Description
      //TODO - no field in ZUGFeRD for that
      mLog.add(
          "Description not mapped to ZUGFeRD: no element in ZUGFeRD",
          "/Invoice/Delivery/Description",
          "???");
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

  private TradeAllowanceChargeType getTradeAllowanceCharge(boolean chargeIndicator,
                                                           BigDecimal baseAmount,
                                                           String documentCurrency,
                                                           BigDecimal percentage,
                                                           BigDecimal amount, String comment) {
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge
    TradeAllowanceChargeType atac = new TradeAllowanceChargeType();

    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/ChargeIndicator
    //surcharge: true
    //reduction: false
    atac.withChargeIndicator(new IndicatorType().withIndicator(chargeIndicator));

    if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)) {
      if (baseAmount != null) {
        //ebInterface: .../BaseAmount
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/BasisAmount
        atac.withBasisAmount(
            new AmountType().withValue(baseAmount)
                .withCurrencyID(
                    documentCurrency));
      }

      if (percentage != null) {
        //ebInterface: .../Percentage
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/CalculationPercent
        atac.withCalculationPercent(
            new PercentType().withValue(percentage));
      }
    }

    if (amount != null) {
      //ebInterface: .../Amount
      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/ActualAmount
      atac.withActualAmount(new AmountType().withValue(amount)
                                .withCurrencyID(
                                    documentCurrency));
    }

    if (comment != null) {
      //ebInterface: .../Comment
      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/Reason
      atac.withReason(new TextType().withValue(comment));
    }

    return atac;
  }

  private ProductCharacteristicType getApplicableProductCharacteristic(String typeCode,
                                                                       String description,
                                                                       BigDecimal valueMeasure,
                                                                       String unitCode,
                                                                       String value) {
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/ApplicableProductCharacteristic
    ProductCharacteristicType pc = new ProductCharacteristicType();

    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/ApplicableProductCharacteristic/TypeCode
    if (typeCode != null) {
      pc.withTypeCode(new CodeType().withValue(typeCode));
    }

    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/ApplicableProductCharacteristic/Description
    if (description != null) {
      pc.withDescription(new TextType().withValue(description));
    }

    if (valueMeasure != null) {
      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/ApplicableProductCharacteristic/ValueMeasure
      MeasureType m = new MeasureType();

      m.withValue(valueMeasure);

      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/ApplicableProductCharacteristic/ValueMeasure/unitCode
      if (unitCode != null) {
        m.withUnitCode(unitCode);
      }

      pc.withValueMeasure(m);
    }

    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/ApplicableProductCharacteristic/Value
    if (value != null) {
      pc.withValue(new TextType().withValue(value));
    }

    return pc;
  }
}