package at.austriapro.mappings.zugferd;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.ebinterface.builder.EbInterfaceReader;
import com.helger.ebinterface.v42.*;
import com.helger.xsds.xmldsig.SignatureType;

import at.austriapro.Mapping;
import at.austriapro.MappingException;
import at.austriapro.MappingFactory;
import at.austriapro.mappings.zugferd.generated.*;
import at.austriapro.utils.DocumentTypeUtils;
import at.austriapro.utils.ISO639Util;

/**
 * Performs mappings from/to ZUGFeRD
 */
public class ZUGFeRDMappingFromEbInterface4p2 extends Mapping {
  private static final Logger LOG = LoggerFactory.getLogger(ZUGFeRDMappingFromEbInterface4p2.class);

  private final MappingFactory.ZugferdMappingType zugferdMappingType;
  private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern ("uuuuMMdd");
  private final DateTimeFormatter issueDateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

  public ZUGFeRDMappingFromEbInterface4p2(final MappingFactory.ZugferdMappingType zugferdMappingType) {
    this.zugferdMappingType = zugferdMappingType;
  }

  /**
   * Perform a mapping from ebInterface to ZUGFeRD
   */
  @Override
  public byte[] mapFromebInterface(final byte[] ebinterface) throws MappingException {

    //Retrieve an Invoice object
    final Ebi42InvoiceType invoice = EbInterfaceReader.ebInterface42 ().read(ebinterface);

    //Perform mapping
    final CrossIndustryDocumentType zugferd = performMapping(invoice);

    return DocumentTypeUtils.writeZUGFeRD(zugferd);
  }


  /**
   * Map the ebInterface object to a ZUGFeRD object
   */
  private CrossIndustryDocumentType performMapping(final Ebi42InvoiceType invoice) {

    //Get an empty cross industry document type
    final CrossIndustryDocumentType zugferd = getEmptyCrossIndustryDocumentType();

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
    mapRelatedDocuments(zugferd, invoice.getRelatedDocument());

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
  private void mapComment(final CrossIndustryDocumentType zugferd, final String comment) {
    //ebInterface: /Invoice/Comment
    if (comment != null) {
      zugferd.getHeaderExchangedDocument()
          .withIncludedNote(new NoteType().withContent(new TextType().withValue(comment)));
    }
  }


  /**
   * Map presentation details
   */
  private void mapPresentationDetails(final CrossIndustryDocumentType zugferd,
                                      final Ebi42PresentationDetailsType presentationDetails) {
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

      if (presentationDetails.isSuppressZero() != null) {
        //ebInterface: /Invoice/PresentationDetails/SuppressZero
        //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
        zugferd.getHeaderExchangedDocument().withIncludedNote(
            new NoteType()
                .withContentCode(new CodeType().withValue("PresentationDetails/SuppressZero"))
                .withContent(new TextType().withValue(
                    presentationDetails.isSuppressZero().toString ())));
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
  private void mapPaymentConditions(final CrossIndustryDocumentType zugferd,
                                    final Ebi42PaymentConditionsType paymentConditions) {
    if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)
        && paymentConditions != null) {
      final String
          documentCurrency =
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

      TradePaymentTermsType stpt;

      if (MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0.equals(zugferdMappingType)
          && paymentConditions.getDiscount() != null
          && paymentConditions.getDiscount().size() > 0) {
        //ebInterface: /Invoice/PaymentConditions/Discount
        for (final Ebi42DiscountType discount : paymentConditions.getDiscount()) {
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
                                                     dateFormatter
                                                         .format(
                                                             paymentConditions.getDueDate()))
                                                 .withFormat(
                                                     "102")));
          }

          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/ApplicableTradePaymentDiscountTerms
          final TradePaymentDiscountTermsType atpd = new TradePaymentDiscountTermsType();
          stpt.withApplicableTradePaymentDiscountTerms(atpd);

          if (discount.getPaymentDate() != null) {
            //ebInterface: /Invoice/PaymentConditions/Discount/PaymentDate
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradePaymentTerms/ApplicableTradePaymentDiscountTerms/BasisDateTime
            atpd.withBasisDateTime(new DateTimeType()
                                       .withDateTimeString(
                                           new DateTimeType.DateTimeString()
                                               .withValue(
                                                   dateFormatter
                                                       .format(
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
          } else {
            //fill with blanks
            stpt.withDescription(new TextType().withValue(" "));
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
                                                   dateFormatter
                                                       .format(
                                                           paymentConditions.getDueDate()))
                                               .withFormat(
                                                   "102")));
        }

        if (MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0.equals(zugferdMappingType)
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
        } else {
          //fill with blanks
          stpt.withDescription(new TextType().withValue(" "));
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
  private void mapPaymentMethod(final CrossIndustryDocumentType zugferd, final Ebi42PaymentMethodType paymentMethod) {
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
                  .withContent(new TextType().withValue(
                      paymentMethod.getSEPADirectDebit().getType().value())));
          mLog.add(
              "SEPADirectDebit/Typec does not exist in ZUGFeRD, mapped to IncludedNote",
              "/Invoice/PaymentMethod/SEPADirectDebit/Type",
              "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
        }

        if (paymentMethod.getSEPADirectDebit().getBankAccountOwner() != null) {
          //ebInterface: /Invoice/PaymentMethod/SEPADirectDebit/BankAccountOwner
          //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
          zugferd.getHeaderExchangedDocument().withIncludedNote(
              new NoteType()
                  .withContentCode(new CodeType().withValue("SEPADirectDebit/BankAccountOwner"))
                  .withContent(new TextType().withValue(
                      paymentMethod.getSEPADirectDebit().getType().value())));
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

        if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)) {
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
                                                                            dateFormatter
                                                                                .format(
                                                                                    paymentMethod
                                                                                        .getSEPADirectDebit()
                                                                                        .getDebitCollectionDate()))
                                                                        .withFormat(
                                                                            "102")))
                    .withEndDateTime(new DateTimeType()
                                         .withDateTimeString(
                                             new DateTimeType.DateTimeString()
                                                 .withValue(
                                                     dateFormatter
                                                         .format(
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
        for (final Ebi42AccountType ba : paymentMethod.getUniversalBankTransaction()
            .getBeneficiaryAccount()) {
          tspmt = new TradeSettlementPaymentMeansType();

          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/TypeCode
          tspmt.withTypeCode(new PaymentMeansCodeType().withValue("31"));

          if (paymentMethod.getComment() != null) {
            //ebInterface: /Invoice/PaymentMethod/Comment
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/Information
            tspmt.withInformation(new TextType().withValue(paymentMethod.getComment()));
          }

          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementPaymentMeans/PayeePartyCreditorFinancialAccount
          final CreditorFinancialAccountType cfa = new CreditorFinancialAccountType();
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
          final CreditorFinancialInstitutionType cft = new CreditorFinancialInstitutionType();
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
  private void mapPayableAmount(final CrossIndustryDocumentType zugferd, final BigDecimal payableAmount) {
    //ebInterface: /Invoice/PayableAmount
    if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)
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
  private void mapTotalGrossAmount(final CrossIndustryDocumentType zugferd, final BigDecimal totalGrossAmount) {
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

      final String documentCurrency = zugferd.getSpecifiedSupplyChainTradeTransaction()
          .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeSettlementMonetarySummation/GrandTotalAmount
      stsms.withGrandTotalAmount(new AmountType().withValue(totalGrossAmount).withCurrencyID(
          documentCurrency));

      BigDecimal totalLineAmount = new BigDecimal(0);
      BigDecimal totalChargeAmount = new BigDecimal(0);
      BigDecimal totalAllowanceAmount = new BigDecimal(0);
      BigDecimal totalTaxBasisAmount = new BigDecimal(0);
      BigDecimal totalTaxAmount = new BigDecimal(0);

      if (zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getIncludedSupplyChainTradeLineItem() != null && zugferd.getSpecifiedSupplyChainTradeTransaction()
                                                                    .getIncludedSupplyChainTradeLineItem().size() > 0) {
        for (final SupplyChainTradeLineItemType items : zugferd.getSpecifiedSupplyChainTradeTransaction()
            .getIncludedSupplyChainTradeLineItem()) {
          if (items.getSpecifiedSupplyChainTradeSettlement() != null
              && items.getSpecifiedSupplyChainTradeSettlement()
                     .getSpecifiedTradeSettlementMonetarySummation() != null) {
            final TradeSettlementMonetarySummationType
                monSum =
                items.getSpecifiedSupplyChainTradeSettlement()
                    .getSpecifiedTradeSettlementMonetarySummation();

            if (monSum.getLineTotalAmount() != null
                && monSum.getLineTotalAmount().size() > 0) {
              totalLineAmount = totalLineAmount.add(monSum.getLineTotalAmount().get(0).getValue());
            }

            if (items.getSpecifiedSupplyChainTradeSettlement()
                    .getApplicableTradeTax() != null
                && items.getSpecifiedSupplyChainTradeSettlement()
                       .getApplicableTradeTax().size() > 0) {
              for (final TradeTaxType tt : items.getSpecifiedSupplyChainTradeSettlement()
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

          if (items.getSpecifiedSupplyChainTradeAgreement()
                  .getGrossPriceProductTradePrice().get(0).getAppliedTradeAllowanceCharge()
              != null
              && items.getSpecifiedSupplyChainTradeAgreement()
                     .getGrossPriceProductTradePrice().get(0).getAppliedTradeAllowanceCharge()
                     .size()
                 > 0) {
            for (final TradeAllowanceChargeType tac : items.getSpecifiedSupplyChainTradeAgreement()
                .getGrossPriceProductTradePrice().get(0).getAppliedTradeAllowanceCharge()) {
              if (tac.getChargeIndicator().getIndicator().booleanValue ()) {
                totalChargeAmount =
                    totalChargeAmount.add(tac.getActualAmount().get(0).getValue());
              } else {
                totalAllowanceAmount =
                    totalAllowanceAmount.add(tac.getActualAmount().get(0).getValue());
              }
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
        for (final TradeAllowanceChargeType tac : zugferd.getSpecifiedSupplyChainTradeTransaction()
            .getApplicableSupplyChainTradeSettlement().getSpecifiedTradeAllowanceCharge()) {
          if (tac.getChargeIndicator().getIndicator().booleanValue ()) {
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
  private void mapTax(final CrossIndustryDocumentType zugferd, final Ebi42TaxType tax) {
    //ebInterface: /Invoice/Tax
    if (tax != null) {
      final String
          documentCurrency =
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

      //ebInterface: /Invoice/Tax/VAT
      if (tax.getVAT() != null && tax.getVAT().getVATItem().size() > 0) {

        //ebInterface: /Invoice/Tax/VAT/VATItem
        for (final Ebi42VATItemType vATItems : tax.getVAT().getVATItem()) {
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/ApplicableTradeTax
          final TradeTaxType tradeTaxType = new TradeTaxType();
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

          if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)) {
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
      if (tax.getOtherTax() != null && tax.getOtherTax().size() > 0) {

        final SupplyChainTradeSettlementType
            ascts =
            zugferd.getSpecifiedSupplyChainTradeTransaction()
                .getApplicableSupplyChainTradeSettlement();

        for (final Ebi42OtherTaxType otherTax : tax.getOtherTax()) {
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
  private void mapReductionAndSurchargeDetails(final CrossIndustryDocumentType zugferd,
                                               final Ebi42ReductionAndSurchargeDetailsType reductionAndSurchargeDetails) {
    if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)
        && reductionAndSurchargeDetails != null) {
      //ebInterface: /Invoice/ReductionAndSurchargeDetails
      if (reductionAndSurchargeDetails.getReductionOrSurchargeOrOtherVATableTax () != null
          && !reductionAndSurchargeDetails.getReductionOrSurchargeOrOtherVATableTax ()
          .isEmpty()) {

        final String
            documentCurrency =
            zugferd.getSpecifiedSupplyChainTradeTransaction()
                .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

        final SupplyChainTradeSettlementType
            ascts =
            zugferd.getSpecifiedSupplyChainTradeTransaction()
                .getApplicableSupplyChainTradeSettlement();

        for (final JAXBElement<?> rSVItem : reductionAndSurchargeDetails
            .getReductionOrSurchargeOrOtherVATableTax ()) {
          boolean chargeIndicator;
          BigDecimal baseAmount = null;
          BigDecimal percentage = null;
          BigDecimal amount = null;
          String comment = null;
          final Object aValue = rSVItem.getValue ();

          //Create TradeAllowanceCharge
          //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/SpecifiedTradeAllowanceCharge
          TradeAllowanceChargeType stac;

          if (aValue instanceof Ebi42OtherVATableTaxType) {
            //ebInterface: /Invoice/ReductionAndSurchargeDetails/OtherVATableTax
            final Ebi42OtherVATableTaxType
                oVatItem =
                (Ebi42OtherVATableTaxType) aValue;

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
                                        amount, comment);

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
            final Ebi42ReductionAndSurchargeType
                rsItem =
                (Ebi42ReductionAndSurchargeType) aValue;

            //Reduction (ReductionListLineItem) => chargeIndicator: false
            //Surcharge (SurchargeListLineItem) => chargeIndicator: true
            chargeIndicator =
                rSVItem.getName().getLocalPart()
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
  private void mapDetails(final CrossIndustryDocumentType zugferd, final Ebi42DetailsType details) {
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

      if (details.getItemList() != null && details.getItemList().size() > 0) {
        final String
            documentCurrency =
            zugferd.getSpecifiedSupplyChainTradeTransaction()
                .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

        //Create a collection of SupplyChainTradeLineItems
        final List<SupplyChainTradeLineItemType>
            listSCTLI =
            new ArrayList<>();
        final TreeSet<BigInteger> posNr = new TreeSet<>();

        int iList = 0;

        //ebInterface: loop all /Invoice/Details/ItemList
        for (final Ebi42ItemListType itemList : details.getItemList()) {
          NoteType listHeaderDescription = null;
          NoteType listFooterDescription = null;

          if (itemList.getHeaderDescription() != null) {
            //ebInterface: /Invoice/Detail/ItemList/HeaderDescription
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
            listHeaderDescription =
                new NoteType()
                    .withContentCode(new CodeType().withValue("ItemList/HeaderDescription"))
                    .withContent(new TextType().withValue(itemList.getHeaderDescription()));
            mLog.add(
                "Detail/ItemList/HeaderDescription does not exist in ZUGFeRD, mapped to IncludedNote",
                "/Invoice/Detail/ItemList/HeaderDescription",
                "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
          }

          if (itemList.getFooterDescription() != null) {
            //ebInterface: /Invoice/Detail/ItemList/FooterDescription
            //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
            listFooterDescription =
                new NoteType()
                    .withContentCode(new CodeType().withValue("ItemList/FooterDescription"))
                    .withContent(new TextType().withValue(itemList.getFooterDescription()));
            mLog.add(
                "Detail/ItemList/FooterDescription does not exist in ZUGFeRD, mapped to IncludedNote",
                "/Invoice/Detail/ItemList/FooterDescription",
                "/CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote");
          }

          if (itemList.getListLineItem() != null && itemList.getListLineItem().size() > 0) {
            int iItems = 0;

            //ebInterface: loop all /Invoice/Details/ItemLists/ListLineItem
            for (final Ebi42ListLineItemType item : itemList.getListLineItem()) {
              //Create a SupplyChainTradeLineItem for a Detail
              final SupplyChainTradeLineItemType sctli = new SupplyChainTradeLineItemType();

              //create a SpecifiedTradeProduct
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct
              final TradeProductType stp = new TradeProductType();
              sctli.withSpecifiedTradeProduct(stp);

              //create a AssociatedDocumentLineDocument
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument
              final DocumentLineDocumentType adld = new DocumentLineDocumentType();
              sctli.withAssociatedDocumentLineDocument(adld);

              //Add listHeaderDescription
              if (listHeaderDescription != null) {
                adld.withIncludedNote(listHeaderDescription);
              }
              //Add listHeaderDescription
              if (listFooterDescription != null) {
                adld.withIncludedNote(listFooterDescription);
              }

              if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)) {
                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/PositionNumber
                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/LineID
                if (item.getPositionNumber() != null) {
                  adld.withLineID(
                      new IDType().withValue(item.getPositionNumber().toString()));
                  posNr.add(item.getPositionNumber());
                } else {
                  final BigInteger tPosNr = posNr.last().add(new BigInteger("100"));
                  adld.withLineID(
                      new IDType().withValue(tPosNr.toString()));
                  posNr.add(tPosNr);
                }
              }

              //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Description
              if (item.getDescription() != null && item.getDescription().size() > 0) {
                final StringBuilder zugDesc = new StringBuilder();

                int i = 0;

                //the first description entry will be used for ZUGFeRD.name, the other entries are ZUGFeRD.description
                for (final String ebDesc : item.getDescription()) {
                  if (i == 0) {
                    stp.withName(new TextType().withValue(ebDesc));
                  } else {
                    if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(
                        zugferdMappingType)) {
                      zugDesc.append(ebDesc).append("\n");
                    }
                  }

                  i++;
                }

                if (zugDesc.toString().trim().length() > 0) {
                  stp.withDescription(new TextType().withValue(zugDesc.toString().trim()));
                }
              }

              if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)) {
                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ArticleNumber
                if (item.getArticleNumber() != null && item.getArticleNumber().size() > 0) {
                  int iArt = 0;

                  for (final Ebi42ArticleNumberType art : item.getArticleNumber()) {
                    if (art.getArticleNumberType().value().equals("GTIN")) {
                      //GTIN
                      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/GlobalID
                      stp.withGlobalID(
                          new IDType().withValue(art.getValue()).withSchemeID("0160"));
                    } else if (art.getArticleNumberType().value()
                        .equals("InvoiceRecipientsArticleNumber")) {
                      //InvoiceRecipientsArticleNumber
                      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/BuyerAssignedID
                      stp.withBuyerAssignedID(new IDType().withValue(art.getValue()));
                    } else if (art.getArticleNumberType().value().equals("BillersArticleNumber")) {
                      //BillersArticleNumber
                      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/SellerAssignedID
                      stp.withSellerAssignedID(new IDType().withValue(art.getValue()));
                    } else if (art.getArticleNumberType().value().equals("PZN")) {
                      //PZN
                      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
                      adld.withIncludedNote(new NoteType().withContentCode(
                          new CodeType().withValue("ArticleNumber/PZN"))
                                                .withContent(new TextType().withValue(
                                                    art.getValue())));
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
              final SupplyChainTradeDeliveryType ssctd = new SupplyChainTradeDeliveryType();
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
              final SupplyChainTradeAgreementType scta = new SupplyChainTradeAgreementType();
              sctli.withSpecifiedSupplyChainTradeAgreement(scta);

              if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)) {
                if (item.getUnitPrice() != null) {
                  //Create NetPriceProductTradePrice and add it to ZUGFeRD
                  final TradePriceType npptp = new TradePriceType();
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
              final SupplyChainTradeSettlementType scts = new SupplyChainTradeSettlementType();
              sctli.withSpecifiedSupplyChainTradeSettlement(scts);

              if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)) {
                //Create ApplicableTradeTax and add it to ZUGFeRD
                final TradeTaxType att = new TradeTaxType();
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
              final TradePriceType gpptp = new TradePriceType();
              scta.withGrossPriceProductTradePrice(gpptp);

              //ebInterface: [/Invoice/Details/ItemLists/ListLineItem/] (Quantity/nvl(BaseQuantity, 1)*UnitPrice)
              final BigDecimal quantity = item.getQuantity().getValue();
              BigDecimal baseQuantity;
              if (item.getUnitPrice().getBaseQuantity() != null) {
                baseQuantity = item.getUnitPrice().getBaseQuantity();
              } else {
                baseQuantity = new BigDecimal(1);
              }
              final BigDecimal unitPrice = item.getUnitPrice().getValue();
              //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/ChargeAmount
              gpptp.withChargeAmount(
                  new AmountType().withValue(quantity.divide(baseQuantity).multiply(unitPrice))
                      .withCurrencyID(documentCurrency));

              if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)) {

                if (item.isDiscountFlag() != null) {
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/DiscountFlag
                  //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/AssociatedDocumentLineDocument/IncludedNote
                  adld.withIncludedNote(new NoteType().withContentCode(
                      new CodeType().withValue("DiscountFlag"))
                                            .withContent(new TextType().withValue(
                                                item.isDiscountFlag().toString ())));
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
                                                                                      .getReductionListLineItemOrSurchargeListLineItemOrOtherVATableTaxListLineItem ()
                                                                                  != null && item
                                                                                                 .getReductionAndSurchargeListLineItemDetails()
                                                                                                 .getReductionListLineItemOrSurchargeListLineItemOrOtherVATableTaxListLineItem()
                                                                                                 .size()
                                                                                             > 0) {
                  for (final JAXBElement<?> rSVItem : item
                      .getReductionAndSurchargeListLineItemDetails()
                      .getReductionListLineItemOrSurchargeListLineItemOrOtherVATableTaxListLineItem ()) {
                    boolean chargeIndicator;
                    BigDecimal baseAmount = null;
                    BigDecimal percentage = null;
                    BigDecimal amount = null;
                    String comment = null;

                    if (rSVItem.getName().getLocalPart().equals("ReductionListLineItem") || rSVItem
                        .getName().getLocalPart().equals("SurchargeListLineItem")) {
                      //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/ReductionListLineItem
                      //ebInterface: /Invoice/Details/ItemLists/ListLineItem/ReductionAndSurchargeListLineItemDetails/SurchargeListLineItem
                      final Ebi42ReductionAndSurchargeBaseType
                          rsItem =
                          (Ebi42ReductionAndSurchargeBaseType) rSVItem.getValue();

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
                      final Ebi42OtherVATableTaxBaseType
                          otherTaxItem =
                          (Ebi42OtherVATableTaxBaseType) rSVItem.getValue();

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
                                                amount, comment));
                  }
                }
              }

              //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery
              if (MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0.equals(zugferdMappingType)
                  && item.getDelivery() != null) {
                //Create the necessary elements in ZUGFeRD
                final TradePartyType stttp = new TradePartyType();
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
                                  dateFormatter.format(item.getDelivery().getDate())).withFormat(
                                  "102"))));
                } else if (item.getDelivery().getPeriod().getFromDate() != null) {
                  //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Period/FromDate
                  ssctd.withActualDeliverySupplyChainEvent(new SupplyChainEventType()
                                                               .withOccurrenceDateTime(
                                                                   new DateTimeType()
                                                                       .withDateTimeString(
                                                                           new DateTimeType.DateTimeString()
                                                                               .withValue(
                                                                                   dateFormatter
                                                                                       .format(
                                                                                           item.getDelivery()
                                                                                               .getPeriod()
                                                                                               .getFromDate()))
                                                                               .withFormat(
                                                                                   "102"))));
                }

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address
                if (item.getDelivery().getAddress() != null) {

                  if (item.getDelivery().getAddress() != null) {
                    String name = new String();

                    if (item.getDelivery().getAddress().getName() != null) {
                      name = item.getDelivery().getAddress().getName();

                      if(item.getDelivery().getAddress().getSalutation() != null){
                        name = item.getDelivery().getAddress().getSalutation() + " " + name;
                      }
                    }

                    if (!name.equals("")){
                      stttp.withName(new TextType().withValue(name));
                    }

                    stttp.withPostalTradeAddress(getTradeAddressType(item.getDelivery().getAddress()));

                    if (getTradeContactType(item.getDelivery().getAddress()) != null){
                      stttp.withDefinedTradeContact(getTradeContactType(item.getDelivery().getAddress()));
                    }
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
                                                    .format(
                                                        item.getBillersOrderReference()
                                                            .getReferenceDateLocal().atStartOfDay ()))));
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
              if (MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0.equals(zugferdMappingType)
                  && item.getInvoiceRecipientsOrderReference() != null) {

                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/BuyerOrderReferencedDocument
                final ReferencedDocumentType bor = new ReferencedDocumentType();
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
                                            .format(
                                                item.getInvoiceRecipientsOrderReference()
                                                    .getReferenceDateLocal().atStartOfDay ()));
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
              if (MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0.equals(zugferdMappingType)
                  && item.getAdditionalInformation() != null) {
                final Ebi42AdditionalInformationType ai = item.getAdditionalInformation();

                String typeCode, description, unitCode, value;
                BigDecimal valueMeasure;

                //ebInterface: /Invoice/Details/ItemLists/ListLineItem/AdditionalInformation/SerialNumber
                if (ai.getSerialNumber() != null && ai.getSerialNumber().size() > 0) {
                  for (final String sn : ai.getSerialNumber()) {
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
                if (ai.getChargeNumber() != null && ai.getChargeNumber().size() > 0) {
                  for (final String ch : ai.getChargeNumber()) {
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
                if (ai.getClassification() != null && ai.getClassification().size() > 0) {
                  for (final Ebi42ClassificationType cl : ai.getClassification()) {
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

              if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)
                  && item.getLineItemAmount() != null) {
                //ebInterface: ebInterface: /Invoice/Details/ItemLists/ListLineItem/LineItemAmount
                //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeSettlement/SpecifiedTradeSettlementMonetarySummation/LineTotalAmount
                scts.withSpecifiedTradeSettlementMonetarySummation(
                    new TradeSettlementMonetarySummationType().withLineTotalAmount(
                        new AmountType().withValue(item.getLineItemAmount())
                            .withCurrencyID(documentCurrency)));

                if (MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0.equals(
                    zugferdMappingType)
                    && gpptp.getAppliedTradeAllowanceCharge() != null
                    && gpptp.getAppliedTradeAllowanceCharge().size() > 0) {
                  BigDecimal sum = BigDecimal.ZERO;

                  for (final TradeAllowanceChargeType ch : gpptp.getAppliedTradeAllowanceCharge()) {
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
                      if (ch.getChargeIndicator().getIndicator().booleanValue ()) {
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
  private void mapRootAttributes(final CrossIndustryDocumentType zugferd, final Ebi42InvoiceType invoice) {
    //ZUGFeRD type
    final String zugFeRDType = getZUGfeRDType();
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
    final String documentType = getDocumentType(invoice);
    zugferd.getHeaderExchangedDocument().withName(new TextType().withValue(documentType));

    //ebInterface: Document type code
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/TypeCode
    final String typeCode = getDocumentTypeCode(invoice);
    zugferd.getHeaderExchangedDocument().withTypeCode(new DocumentCodeType().withValue(typeCode));

    //ebInterface: /Invoice/@InvoiceCurrency
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeSettlement/InvoiceCurrencyCode
    final String documentCurrency = invoice.getInvoiceCurrency();
    zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement()
        .withInvoiceCurrencyCode(new CodeType().withValue(documentCurrency));

    //ebInterface: /Invoice/@ManualProcessing
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/IncludedNote
    if (invoice.isManualProcessing() != null) {
      zugferd.getHeaderExchangedDocument().withIncludedNote(
          new NoteType().withContentCode(new CodeType().withValue("SEPADirectDebit/Type"))
              .withContent(
                  new TextType().withValue(invoice.isManualProcessing().toString ())));
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
    if (MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0.equals(zugferdMappingType)) {
      //Attention: convert from ISO639-2 to ISO639-1!
      zugferd.getHeaderExchangedDocument().getLanguageID()
          .add(new IDType()
                   .withValue(ISO639Util.convertISO639_2ToISO639_1(invoice.getLanguage())));
    }

    //ebInterface: /Invoice/@IsDuplicate
    //ZUGFeRD: /CrossIndustryDocument/HeaderExchangedDocument/CopyIndicator
    if (MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0.equals(zugferdMappingType)) {
      if (invoice.isIsDuplicate() != null && invoice.isIsDuplicate ().booleanValue ()) {
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
            .withValue(dateFormatter.format(invoice.getInvoiceDate()))));
  }

  /**
   * Map the signature from the ebInterface
   */
  private void mapSignature(@SuppressWarnings ("unused") final CrossIndustryDocumentType zugferd, final SignatureType signature) {
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
  private void mapOrderingParty(final CrossIndustryDocumentType zugferd, final Ebi42OrderingPartyType orderingParty) {
    //ebInterface: /Invoice/OrderingParty
    if (MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0.equals(zugferdMappingType)) {
      if (orderingParty == null) {
        LOG.debug("No ordering party element specified in ebInterface - continuing.");
        return;
      }

      //Create a trade party for the invoice recipient
      final TradePartyType productEndUserTradeParty = new TradePartyType();
      final SupplyChainTradeAgreementType
          supplyChainTradeAgreementType =
          getSupplyChainTradeAgreement(zugferd);
      supplyChainTradeAgreementType.withProductEndUserTradeParty(productEndUserTradeParty);

      //ebInterface: /Invoice/OrderingParty/VATIdentification
      productEndUserTradeParty.withSpecifiedTaxRegistration(new TaxRegistrationType().withID(
          new IDType().withValue(orderingParty.getVATIdentificationNumber()).withSchemeID("VA")));

      if (orderingParty.getFurtherIdentification() != null
          && orderingParty.getFurtherIdentification().size() > 0) {

        //ebInterface: /Invoice/OrderingParty/FurtherIdentification
        for (final Ebi42FurtherIdentificationType furtherIdentification : orderingParty
            .getFurtherIdentification()) {

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

      if (orderingParty.getAddress().getAddressIdentifier() != null
          && orderingParty.getAddress().getAddressIdentifier().size() > 0) {
        int i = 0;

        for (final Ebi42AddressIdentifierType aId : orderingParty.getAddress().getAddressIdentifier()) {
          //ebInterface: /Invoice/OrderingParty/Address/AddressIdentifier
          String schema;
          if (aId.getAddressIdentifierType()
              .equals(Ebi42AddressIdentifierTypeType.DUNS)) {
            schema = "0060";
          } else if (aId.getAddressIdentifierType()
              .equals(Ebi42AddressIdentifierTypeType.GLN)) {
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
                                                   .format(
                                                       orderingParty.getOrderReference()
                                                           .getReferenceDateLocal().atStartOfDay ()))));
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

      if (orderingParty.getAddress() != null) {

        if (orderingParty.getAddress() != null) {
          String name = new String();

          if (orderingParty.getAddress().getName() != null) {
            name = orderingParty.getAddress().getName();

            if(orderingParty.getAddress().getSalutation() != null){
              name = orderingParty.getAddress().getSalutation() + " " + name;
            }
          }

          if (!name.equals("")){
            productEndUserTradeParty.withName(new TextType().withValue(name));
          }

          productEndUserTradeParty.withPostalTradeAddress(getTradeAddressType(orderingParty.getAddress()));

          if (getTradeContactType(orderingParty.getAddress()) != null){
            productEndUserTradeParty.withDefinedTradeContact(getTradeContactType(orderingParty.getAddress()));
          }
        }
      }

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
  private void mapInvoiceRecipient(final CrossIndustryDocumentType zugferd,
                                   final Ebi42InvoiceRecipientType invoiceRecipient) {
    //ebInterface: /Invoice/InvoiceRecipient

    if (invoiceRecipient == null) {
      LOG.debug("No biller element specified in ebInterface - continuing.");
      return;
    }

    //Create a trade party for the invoice recipient
    final TradePartyType buyerTradeParty = new TradePartyType();
    final SupplyChainTradeAgreementType
        supplyChainTradeAgreementType =
        getSupplyChainTradeAgreement(zugferd);
    supplyChainTradeAgreementType.withBuyerTradeParty(buyerTradeParty);

    //ebInterface: /Invoice/InvoiceRecipient/VATIdentification
    buyerTradeParty.withSpecifiedTaxRegistration(new TaxRegistrationType().withID(
        new IDType().withValue(invoiceRecipient.getVATIdentificationNumber()).withSchemeID("VA")));

    //ebInterface: /Invoice/InvoiceRecipient/FurtherIdentification
    if (invoiceRecipient.getFurtherIdentification() != null
        && invoiceRecipient.getFurtherIdentification().size() > 0) {

      //ebInterface: /Invoice/InvoiceRecipient/FurtherIdentification
      for (final Ebi42FurtherIdentificationType furtherIdentification : invoiceRecipient
          .getFurtherIdentification()) {

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
    if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)
        && invoiceRecipient.getAddress().getAddressIdentifier() != null
        && invoiceRecipient.getAddress().getAddressIdentifier().size() > 0) {
      int i = 0;

      for (final Ebi42AddressIdentifierType aId : invoiceRecipient.getAddress().getAddressIdentifier()) {
        //ebInterface: /Invoice/InvoiceRecipient/Address/AddressIdentifier
        String schema;
        if (aId.getAddressIdentifierType()
            .equals(Ebi42AddressIdentifierTypeType.DUNS)) {
          schema = "0060";
        } else if (aId.getAddressIdentifierType()
            .equals(Ebi42AddressIdentifierTypeType.GLN)) {
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
                                                 .format(
                                                     invoiceRecipient.getOrderReference()
                                                         .getReferenceDateLocal().atStartOfDay ()))));
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

    if (invoiceRecipient.getAddress() != null) {

      if (invoiceRecipient.getAddress() != null) {
        String name = new String();

        if (invoiceRecipient.getAddress().getName() != null) {
          name = invoiceRecipient.getAddress().getName();

          if(invoiceRecipient.getAddress().getSalutation() != null){
            name = invoiceRecipient.getAddress().getSalutation() + " " + name;
          }
        }

        if (!name.equals("")){
          buyerTradeParty.withName(new TextType().withValue(name));
        }

        buyerTradeParty.withPostalTradeAddress(getTradeAddressType(invoiceRecipient.getAddress()));

        if (getTradeContactType(invoiceRecipient.getAddress()) != null){
          buyerTradeParty.withDefinedTradeContact(getTradeContactType(invoiceRecipient.getAddress()));
        }
      }
    }

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
  private void mapBiller(final CrossIndustryDocumentType zugferd, final Ebi42BillerType biller) {
    //ebInterface: /Invoice/Biller
    if (biller == null) {
      LOG.debug("No blller element specified in ebInterface - continuing.");
      return;
    }

    final SupplyChainTradeAgreementType
        supplyChainTradeAgreementType =
        getSupplyChainTradeAgreement(zugferd);
    //CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/SellerTradeParty
    final TradePartyType sellerTradePartyType = new TradePartyType();
    supplyChainTradeAgreementType.withSellerTradeParty(sellerTradePartyType);

    //ebInterface: /Invoice/Biller/VATIdentification
    sellerTradePartyType.withSpecifiedTaxRegistration(new TaxRegistrationType().withID(
        new IDType().withValue(biller.getVATIdentificationNumber()).withSchemeID("VA")));

    //ebInterface: /Invoice/Biller/FurtherIdentification
    if (biller.getFurtherIdentification() != null
        && biller.getFurtherIdentification().size() > 0) {

      //ebInterface: /Invoice/InvoiceRecipient/FurtherIdentification
      for (final Ebi42FurtherIdentificationType furtherIdentification : biller
          .getFurtherIdentification()) {

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
    if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)
        && biller.getAddress().getAddressIdentifier() != null
        && biller.getAddress().getAddressIdentifier().size() > 0) {
      for (final Ebi42AddressIdentifierType aId : biller.getAddress().getAddressIdentifier()) {
        //ebInterface: /Invoice/Biller/Address/AddressIdentifier
        String schema;
        if (aId.getAddressIdentifierType()
            .equals(Ebi42AddressIdentifierTypeType.DUNS)) {
          schema = "0060";
        } else if (aId.getAddressIdentifierType()
            .equals(Ebi42AddressIdentifierTypeType.GLN)) {
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

    if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)) {
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
                                                   .format(
                                                       biller.getOrderReference()
                                                           .getReferenceDateLocal().atStartOfDay ()))));
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

    if (biller.getAddress() != null) {

      if (biller.getAddress() != null) {
        String name = new String();

        if (biller.getAddress().getName() != null) {
          name = biller.getAddress().getName();

          if(biller.getAddress().getSalutation() != null){
            name = biller.getAddress().getSalutation() + " " + name;
          }
        }

        if (!name.equals("")){
          sellerTradePartyType.withName(new TextType().withValue(name));
        }

        sellerTradePartyType.withPostalTradeAddress(getTradeAddressType(biller.getAddress()));

        if (getTradeContactType(biller.getAddress()) != null){
          sellerTradePartyType.withDefinedTradeContact(getTradeContactType(biller.getAddress()));
        }
      }
    }

    //ebInterface: /Invoice/Biller/InvoiceRecipientsBillerID
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/SellerTradeParty/ID
    if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)) {
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
                      .withValue(biller.getBillerExtension().getBillerExtension()
                                     .getBillersContractPartnerNumber())));
      mLog.add(
          "BillersContractPartnerNumber does not exist in ZUGFeRD, mapped to IncludedNote",
          "/Invoice/Biller/BillerExtension/BillerExtension/BillersContractPartnerNumber",
          "/CrossIndustryDocument/HeaderExchangedDocument/IncludedNote");
    }
  }

  /**
   * Map the details of related documents Target in ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument
   */
  private void mapRelatedDocuments(final CrossIndustryDocumentType zugferd,
                                   final List<Ebi42RelatedDocumentType> relatedDocuments) {
    //ebInterface: /Invoice/RelatedDocuments
    if (relatedDocuments.isEmpty ()) {
      LOG.debug("No related documents specified in ebInterface - continuing");
      return;
    }

    final SupplyChainTradeAgreementType
        supplyChainTradeAgreementType =
        getSupplyChainTradeAgreement(zugferd);

    int i = 0;

    for (final Ebi42RelatedDocumentType relatedDocument : relatedDocuments) {

      if (MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0.equals(zugferdMappingType)) {
        //Create a new related document type and assign it to the supply chain trade agreement

        final ReferencedDocumentType referencedDocumentType = new ReferencedDocumentType();
        supplyChainTradeAgreementType.getAdditionalReferencedDocument().add(referencedDocumentType);

        //ebInterface: /Invoice/RelatedDocument/InvoiceNumber
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument/ID
        referencedDocumentType.withID(new IDType().withValue(relatedDocument.getInvoiceNumber()));

        //ebInterface: /Invoice/RelatedDocument/InvoiceDate
        //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument/IssueDateTime
        referencedDocumentType.withIssueDateTime(
            issueDateTimeFormatter.format(relatedDocument.getInvoiceDateLocal().atStartOfDay ()));

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
        final StringBuilder text = new StringBuilder();

        text.append("Zugehörige Rechnung:\n");

        //ebInterface: /Invoice/RelatedDocument/InvoiceNumber
        if (relatedDocument.getInvoiceNumber() != null) {
          text.append(relatedDocument.getInvoiceNumber());
        }

        //ebInterface: /Invoice/RelatedDocument/InvoiceDate
        if (relatedDocument.getInvoiceDate() != null) {
          text.append(dateFormatter.format(relatedDocument.getInvoiceDate())).append("\n");
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
  private void mapCancelledOriginalDocument(final CrossIndustryDocumentType zugferd,
                                            final Ebi42CancelledOriginalDocumentType cancelledOriginalDocument) {
    //ebInterface: /Invoice/CancelledOriginalDocument
    if (cancelledOriginalDocument == null) {
      LOG.debug("No cancelled original document specified in ebInterface - continuing");
      return;
    }

    if (MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0.equals(zugferdMappingType)) {
      final SupplyChainTradeAgreementType
          supplyChainTradeAgreementType =
          getSupplyChainTradeAgreement(zugferd);

      //Create a new related document type and assign it to the supply chain trade agreement
      final ReferencedDocumentType referencedDocumentType = new ReferencedDocumentType();
      supplyChainTradeAgreementType.getAdditionalReferencedDocument().add(referencedDocumentType);

      //ebInterface: /Invoice/CancelledOriginalDocument/InvoiceNumber
      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument/ID
      referencedDocumentType
          .withID(new IDType().withValue(cancelledOriginalDocument.getInvoiceNumber()));

      //ebInterface: /Invoice/CancelledOriginalDocument/InvoiceDate
      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/ApplicableSupplyChainTradeAgreement/AdditionalReferencedDocument/IssueDateTime
      referencedDocumentType.withIssueDateTime(
          issueDateTimeFormatter.format(cancelledOriginalDocument.getInvoiceDateLocal().atStartOfDay ()));

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
      final StringBuilder text = new StringBuilder();

      text.append("Stornierte Rechnung:\n");

      //ebInterface: /Invoice/CancelledOriginalDocument/InvoiceNumber
      if (cancelledOriginalDocument.getInvoiceNumber() != null) {
        text.append(cancelledOriginalDocument.getInvoiceNumber());
      }

      //ebInterface: /Invoice/CancelledOriginalDocument/InvoiceDate
      if (cancelledOriginalDocument.getInvoiceDate() != null) {
        text.append(dateFormatter.format(cancelledOriginalDocument.getInvoiceDate()))
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
  private void mapDelivery(final CrossIndustryDocumentType zugferd, final Ebi42DeliveryType delivery) {
    //ebInterface: /Invoice/Delivery
    if (delivery == null) {
      LOG.debug("No delivery element specified in ebInterface - continuing.");
      return;
    }

    //Create the necessary elements in ZUGFeRD
    zugferd.getSpecifiedSupplyChainTradeTransaction().withApplicableSupplyChainTradeDelivery(
        new SupplyChainTradeDeliveryType());

    if (!MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)
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
                  dateFormatter.format(delivery.getDate())).withFormat(
                  "102"))));
    } else if (delivery.getPeriod().getFromDate() != null) {
      //ebInterface: /Invoice/Delivery/Period/FromDate
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
          .withActualDeliverySupplyChainEvent(new SupplyChainEventType().withOccurrenceDateTime(
              new DateTimeType().withDateTimeString(new DateTimeType.DateTimeString().withValue(
                  dateFormatter.format(delivery.getPeriod().getFromDate())).withFormat(
                  "102"))));
    }

    if (MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0.equals(zugferdMappingType)) {
      if (delivery.getAddress() != null) {
        final TradePartyType sttp = new TradePartyType();

        //Create the necessary elements in ZUGFeRD
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery().withShipToTradeParty(
            sttp);

        if (delivery.getAddress() != null) {

          if (delivery.getAddress() != null) {
            String name = new String();

            if (delivery.getAddress().getName() != null) {
              name = delivery.getAddress().getName();

              if(delivery.getAddress().getSalutation() != null){
                name = delivery.getAddress().getSalutation() + " " + name;
              }
            }

            if (!name.equals("")){
              sttp.withName(new TextType().withValue(name));
            }

            sttp.withPostalTradeAddress(getTradeAddressType(delivery.getAddress()));

            if (getTradeContactType(delivery.getAddress()) != null){
              sttp.withDefinedTradeContact(getTradeContactType(delivery.getAddress()));
            }
          }
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
    final CrossIndustryDocumentType zugferd = new CrossIndustryDocumentType();

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
  private String getDocumentType(final Ebi42InvoiceType invoice) {

    if (Ebi42DocumentTypeType.SELF_BILLING.equals(invoice.getDocumentType())) {
      return "BELASTUNGSANZEIGE";
    } else if (Ebi42DocumentTypeType.CREDIT_MEMO.equals(invoice.getDocumentType())) {
      return "GUTSCHRIFT";
    } else {
      return "RECHNUNG";
    }

  }

  /**
   * Return the correct document type code. Valid document type codes in ZUGFeRD are 380, 84, 389
   */
  private String getDocumentTypeCode(final Ebi42InvoiceType invoice) {

    //Code 84 has no equivalent in ebInterface
    if (Ebi42DocumentTypeType.SELF_BILLING.equals(invoice.getDocumentType())) {
      return "389";
    }
    return "380";
  }


  /**
   * Return the correct identifier scheme of the given ZUGFeRD type
   */
  private String getZUGfeRDType() {

    if (MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(zugferdMappingType)) {
      return "urn:ferd:CrossIndustryDocument:invoice:1p0:basic";
    } else if (MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0.equals(zugferdMappingType)) {
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
      final CrossIndustryDocumentType zugferd) {

    if (zugferd.getSpecifiedSupplyChainTradeTransaction()
                              .getApplicableSupplyChainTradeAgreement().isEmpty ()) {
      final SupplyChainTradeAgreementType
          supplyChainTradeAgreementType =
          new SupplyChainTradeAgreementType();
      zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeAgreement()
          .add(
              supplyChainTradeAgreementType);
      return supplyChainTradeAgreementType;
    }
    return zugferd.getSpecifiedSupplyChainTradeTransaction()
          .getApplicableSupplyChainTradeAgreement().get(0);
  }

  private TradeAllowanceChargeType getTradeAllowanceCharge(final boolean chargeIndicator,
                                                           final BigDecimal baseAmount,
                                                           final String documentCurrency,
                                                           final BigDecimal percentage,
                                                           final BigDecimal amount, final String comment) {
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge
    final TradeAllowanceChargeType atac = new TradeAllowanceChargeType();

    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/ChargeIndicator
    //surcharge: true
    //reduction: false
    atac.withChargeIndicator(new IndicatorType().withIndicator(Boolean.valueOf (chargeIndicator)));

    if (MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0.equals(zugferdMappingType)) {
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

    if (comment != null && comment.trim().length() != 0) {
      //ebInterface: .../Comment
      //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedSupplyChainTradeAgreement/GrossPriceProductTradePrice/AppliedTradeAllowanceCharge/Reason
      atac.withReason(new TextType().withValue(comment));
    }

    return atac;
  }

  private ProductCharacteristicType getApplicableProductCharacteristic(final String typeCode,
                                                                       final String description,
                                                                       final BigDecimal valueMeasure,
                                                                       final String unitCode,
                                                                       final String value) {
    //ZUGFeRD: /CrossIndustryDocument/SpecifiedSupplyChainTradeTransaction/IncludedSupplyChainTradeLineItem/SpecifiedTradeProduct/ApplicableProductCharacteristic
    final ProductCharacteristicType pc = new ProductCharacteristicType();

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
      final MeasureType m = new MeasureType();

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

  private TradeAddressType getTradeAddressType(final Ebi42AddressType address) {
    final TradeAddressType tat = new TradeAddressType();

    //ebInterface: /Invoice/*/Address/Street
    //ebInterface: /Invoice/*/Address/POBox
    //ebInterface: /Invoice/*/Address/Name
    //ebInterface: /Invoice/*/Address/ZIP
    //ebInterface: /Invoice/*/Address/Contact
    //ebInterface: /Invoice/*/Address/Town
    //ebInterface: /Invoice/*/Address/CountryCode
    String lineOne = "";
    if (address.getStreet() != null) {
      lineOne = address.getStreet();
    } else {
      lineOne = address.getPOBox();
    }

    if (address.getZIP() != null) {
      tat.withPostcodeCode(new CodeType().withValue(address.getZIP()));
    }

    if (lineOne != null) {
      tat.withLineOne(new TextType().withValue(lineOne));
    }

    if (address.getContact() != null) {
      tat.withLineTwo(new TextType().withValue(address.getContact()));
    }

    if (address.getTown() != null) {
      tat.withCityName(new TextType().withValue(address.getTown()));
    }

    if (address.getCountry() != null && address.getCountry().getCountryCode() != null) {
      tat.withCountryID(new CountryIDType().withValue(
          address.getCountry().getCountryCode()));
    } else if (address.getCountry() != null){
      tat.withCountryID(new CountryIDType().withValue(
          address.getCountry().getValue()));
    }

    if (address.getAddressExtension() != null) {
      //ebInterface: /Invoice/Details/ItemLists/ListLineItem/Delivery/Address/AddressExtension
      //TODO - not in ZUGFeRD
      mLog.add(
          "AddressExtensions not mapped to ZUGFeRD: no element in ZUGFeRD",
          "/Invoice/Details/ItemList/ListLineItem/Delivery/Address/AddressExtensions",
          "No Element in ZUGFeRD");
    }

    return tat;
  }

  private TradeContactType getTradeContactType(final Ebi42AddressType address) {
    TradeContactType tct = null;

    if (address.getContact() != null || address.getPhone() != null || address.getEmail() != null) {
      tct = new TradeContactType();

      if (address.getContact() != null) {
        tct.withPersonName(new TextType().withValue(address.getContact()));
      }

      if (address.getPhone() != null) {
        tct.withTelephoneUniversalCommunication(new UniversalCommunicationType().withCompleteNumber(
            new TextType().withValue(address.getPhone())));
      }

      if (address.getEmail() != null) {
        tct.withEmailURIUniversalCommunication(
            new UniversalCommunicationType().withURIID(
                new IDType().withValue(address.getEmail())));
      }
    }

    return tct;
  }
}