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
import at.austriapro.utils.DocumentTypeUtils;

/**
 * Performs mappings from/to ZUGFeRD
 */
public class ZUGFeRDMapping extends Mapping {

  private MappingFactory.MappingType mappingType;
  private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd");
  private DateTimeFormatter issueDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

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
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem
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

    return zugferd;
  }


  /**
   * Map details of ebInterface comments
   * @param zugferd
   * @param comment
   */
  private void mapComment(CrossIndustryDocumentType zugferd, String comment) {
    if(comment != null){
      zugferd.getHeaderExchangedDocument().withIncludedNote(new NoteType().withContent(new TextType().withValue(comment)));
    }
  }


  /**
   * Map presentation details
   * @param zugferd
   * @param presentationDetails
   */
  private void mapPresentationDetails(CrossIndustryDocumentType zugferd,
                                      PresentationDetails presentationDetails) {
    //TODO - not in ZUGFeRD
    if (presentationDetails != null) {
      //eb:PresentationDetails
      //TODO - no field in ZUGFeRD for that
      mLog.add(
          "PresentationDetails not mapped to ZUGFeRD: no proper element in ZUGFeRD",
          "/Invoice/PresentationDetails",
          "???");
    }
  }

  /**
   * Map the details of payment conditions
   * @param zugferd
   * @param paymentConditions
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
        //eb:Discount
        for (Discount discount : paymentConditions.getDiscounts()) {
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms
          stpt = new TradePaymentTermsType();
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().withSpecifiedTradePaymentTerms(stpt);

          if (paymentConditions.getDueDate() != null) {
            //eb:DueDate
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms/ram:DueDateDateTime
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

          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms/ram:ApplicableTradePaymentDiscountTerms
          TradePaymentDiscountTermsType atpd = new TradePaymentDiscountTermsType();
          stpt.withApplicableTradePaymentDiscountTerms(atpd);

          if (discount.getPaymentDate() != null) {
            //eb:Discount/eb:PaymentDate
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms/ram:ApplicableTradePaymentDiscountTerms/ram:BasisDateTime
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
            //eb:Discount/eb:BaseAmount
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms/ram:ApplicableTradePaymentDiscountTerms/ram:BasisAmount
            atpd.withBasisAmount(new AmountType().withValue(discount.getBaseAmount())
                                     .withCurrencyID(documentCurrency));
          }

          if (discount.getPercentage() != null) {
            //eb:Discount/eb:Percentage
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms/ram:ApplicableTradePaymentDiscountTerms/ram:CalculationPercent
            atpd.withCalculationPercent(new PercentType().withValue(discount.getPercentage()));
          }

          if (discount.getAmount() != null) {
            //eb:Discount/eb:Amount
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms/ram:ApplicableTradePaymentDiscountTerms/ram:ActualDiscountAmount
            atpd.withActualDiscountAmount(new AmountType().withValue(discount.getAmount())
                                              .withCurrencyID(documentCurrency));
          }

          if (paymentConditions.getMinimumPayment() != null) {
            //eb:MinimumPayment
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms/ram:PartialPaymentAmount
            stpt.withPartialPaymentAmount(
                new AmountType().withValue(paymentConditions.getMinimumPayment())
                    .withCurrencyID(documentCurrency));
          }

          if (paymentConditions.getComment() != null) {
            //eb:Comment
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms/ram:Description
            stpt.withDescription(new TextType().withValue(paymentConditions.getComment()));
          }

          if (paymentConditions.getPaymentConditionsExtension() != null) {
            //eb:PaymentConditionsExtension
            //TODO - no field in ZUGFeRD for that
            mLog.add(
                "PaymentConditionsExtension not mapped to ZUGFeRD: no proper element in ZUGFeRD",
                "/Invoice/PaymentConditions/PaymentConditionsExtension",
                "???");
          }
        }
      } else {
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms
        stpt = new TradePaymentTermsType();
        zugferd.getSpecifiedSupplyChainTradeTransaction()
            .getApplicableSupplyChainTradeSettlement().withSpecifiedTradePaymentTerms(stpt);

        if (paymentConditions.getDueDate() != null) {
          //eb:DueDate
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms/ram:DueDateDateTime
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
          //eb:MinimumPayment
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms/ram:PartialPaymentAmount
          stpt.withPartialPaymentAmount(
              new AmountType().withValue(paymentConditions.getMinimumPayment())
                  .withCurrencyID(documentCurrency));
        }

        if (paymentConditions.getComment() != null) {
          //eb:Comment
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradePaymentTerms/ram:Description
          stpt.withDescription(new TextType().withValue(paymentConditions.getComment()));
        }

        if (paymentConditions.getPaymentConditionsExtension() != null) {
          //eb:PaymentConditionsExtension
          //TODO - no field in ZUGFeRD for that
          mLog.add(
              "PaymentConditionsExtension not mapped to ZUGFeRD: no proper element in ZUGFeRD",
              "/Invoice/PaymentConditions/PaymentConditionsExtension",
              "???");
        }
      }
    }
  }

  /**
   * Map details of payment method
   * @param zugferd
   * @param paymentMethod
   */
  private void mapPaymentMethod(CrossIndustryDocumentType zugferd, PaymentMethod paymentMethod) {
    if (paymentMethod != null) {
      TradeSettlementPaymentMeansType tspmt = new TradeSettlementPaymentMeansType();

      if (paymentMethod.getDirectDebit() != null) {
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/TypeCode
        tspmt.withTypeCode(new PaymentMeansCodeType().withValue("49"));

        if (paymentMethod.getComment() != null) {
          //eb:Comment
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/Information
          tspmt.withInformation(new TextType().withValue(paymentMethod.getComment()));
        }

        if (paymentMethod.getPaymentMethodExtension() != null) {
          //eb:PaymentMethodExtension/eb:Custom
          //TODO - all other information is in custom extensions
          mLog.add(
              "PaymentMethodExtension/Custom not mapped to ZUGFeRD: no proper information to map to ZUGFeRD",
              "/Invoice/PaymentMethod/PaymentMethodExtension/Custom",
              "???");
        }

        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement()
            .withSpecifiedTradeSettlementPaymentMeans(
                tspmt);
      } else if (paymentMethod.getSEPADirectDebit() != null) {
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/TypeCode
        tspmt.withTypeCode(new PaymentMeansCodeType().withValue("49"));

        if (paymentMethod.getComment() != null) {
          //eb:Comment
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/Information
          tspmt.withInformation(new TextType().withValue(paymentMethod.getComment()));
        }

        if (paymentMethod.getSEPADirectDebit().getType() != null) {
          //eb:SEPADirectDebit/eb:Type
          //TODO - not in Zugferd
          mLog.add(
              "SEPADirectDebit/Type not mapped to ZUGFeRD: no proper element in ZUGFeRD",
              "/Invoice/PaymentMethod/SEPADirectDebit/Type",
              "???");
        }

        if (paymentMethod.getSEPADirectDebit().getBankAccountOwner() != null) {
          //eb:SEPADirectDebit/eb:BankAccountOwner
          //TODO - not in Zugferd
          mLog.add(
              "SEPADirectDebit/BankAccountOwner not mapped to ZUGFeRD: no proper element in ZUGFeRD",
              "/Invoice/PaymentMethod/SEPADirectDebit/BankAccountOwner",
              "???");
        }

        if (paymentMethod.getSEPADirectDebit().getMandateReference() != null) {
          //eb:SEPADirectDebit/eb:MandateReference
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ID
          tspmt.withID(
              new IDType().withValue(paymentMethod.getSEPADirectDebit().getMandateReference()));
        }

        if (paymentMethod.getSEPADirectDebit().getCreditorID() != null) {
          //eb:SEPADirectDebit/eb:IBAN
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:PayeePartyCreditorFinancialAccount
          tspmt
              .withPayeePartyCreditorFinancialAccount(new CreditorFinancialAccountType().withIBANID(
                  new IDType().withValue(paymentMethod.getSEPADirectDebit().getCreditorID())));
        }

        if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
          if (paymentMethod.getSEPADirectDebit().getIBAN() != null) {
            //eb:SEPADirectDebit/eb:IBAN
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:PayerPartyDebtorFinancialAccount
            tspmt.withPayerPartyDebtorFinancialAccount(new DebtorFinancialAccountType().withIBANID(
                new IDType().withValue(paymentMethod.getSEPADirectDebit().getIBAN())));
          }

          if (paymentMethod.getSEPADirectDebit().getBIC() != null) {
            //eb:SEPADirectDebit/eb:BIC
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:PayerSpecifiedDebtorFinancialInstitution
            tspmt.withPayerSpecifiedDebtorFinancialInstitution(new DebtorFinancialInstitutionType()
                                                                   .withBICID(
                                                                       new IDType().withValue(
                                                                           paymentMethod
                                                                               .getSEPADirectDebit()
                                                                               .getBIC())));
          }

          if (paymentMethod.getSEPADirectDebit().getDebitCollectionDate() != null) {
            //eb:SEPADirectDebit/eb:DebitCollectionDate
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:BillingSpecifiedPeriod
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
        for (BeneficiaryAccount ba : paymentMethod.getUniversalBankTransaction().getBeneficiaryAccounts()){
          tspmt = new TradeSettlementPaymentMeansType();

          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:TypeCode
          tspmt.withTypeCode(new PaymentMeansCodeType().withValue("31"));

          if (paymentMethod.getComment() != null) {
            //eb:Comment
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/Information
            tspmt.withInformation(new TextType().withValue(paymentMethod.getComment()));
          }

          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:PayeePartyCreditorFinancialAccount
          CreditorFinancialAccountType cfa = new CreditorFinancialAccountType();
          tspmt
              .withPayeePartyCreditorFinancialAccount(cfa);

          if (ba.getIBAN() != null) {
            //eb:UniversalBankTransaction/eb:BeneficiaryAccount/eb:IBAN
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:PayeePartyCreditorFinancialAccount/ram:IBANID
            cfa.withIBANID(
                new IDType().withValue(ba.getIBAN()));
          }

          if (ba.getBankAccountOwner() != null) {
            //eb:UniversalBankTransaction/eb:BeneficiaryAccount/eb:BankAccountOwner
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:PayeePartyCreditorFinancialAccount/ram:AccountName
            cfa.withAccountName(new TextType().withValue(ba.getBankAccountOwner()));
          }

          if (ba.getBankAccountNr() != null) {
            //eb:UniversalBankTransaction/eb:BeneficiaryAccount/eb:BankAccountNr
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:PayeePartyCreditorFinancialAccount/ram:ProprietaryID
            cfa.withProprietaryID(new IDType().withValue(ba.getBankAccountNr()));
          }

          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:PayeePartyCreditorFinancialInstitution
          CreditorFinancialInstitutionType cft = new CreditorFinancialInstitutionType();
          tspmt
              .withPayeeSpecifiedCreditorFinancialInstitution(cft);

          if (ba.getBIC() != null) {
            //eb:UniversalBankTransaction/eb:BeneficiaryAccount/eb:BIC
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:PayeePartyCreditorFinancialInstitution/ram:BICID
            cft.withBICID(new IDType().withValue(ba.getBIC()));
          }

          /*
          if (ba.getBankCode() != null) {
            //eb:UniversalBankTransaction/eb:BeneficiaryAccount/eb:BankCode
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:PayeePartyCreditorFinancialInstitution/ram:GermanBankleitzahlID
            cft.withGermanBankleitzahlID(new IDType().withValue(ba.getBankCode().getValue())));
          }
          */

          if (ba.getBankName() != null) {
            //eb:UniversalBankTransaction/eb:BeneficiaryAccount/eb:BankName
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementPaymentMeans/ram:PayeePartyCreditorFinancialInstitution/ram:Name
            cft.withName(new TextType().withValue(ba.getBankName()));
          }

          zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement()
              .withSpecifiedTradeSettlementPaymentMeans(
                  tspmt);
        }

        if (paymentMethod.getUniversalBankTransaction().getPaymentReference().getValue() != null) {
          //eb:UniversalBankTransaction/eb:PaymentReference
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:PaymentReference
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
   * @param zugferd
   * @param payableAmount
   */
  private void mapPayableAmount(CrossIndustryDocumentType zugferd, BigDecimal payableAmount) {
    //eb:PayableAmount
    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType) && payableAmount != null) {
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

      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:DuePayableAmount
      stsms.withDuePayableAmount(new AmountType().withValue(payableAmount).withCurrencyID(
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue()));
    }

  }

  /**
   * ebInterface total gross details
   * @param zugferd
   * @param totalGrossAmount
   */
  private void mapTotalGrossAmount(CrossIndustryDocumentType zugferd, BigDecimal totalGrossAmount) {
    //eb:TotalGrossAmount
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

      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:GrandTotalAmount
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

      if (zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement().getSpecifiedTradeAllowanceCharge() != null
          && zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement().getSpecifiedTradeAllowanceCharge().size()
             > 0) {
        for (TradeAllowanceChargeType tac : zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeSettlement().getSpecifiedTradeAllowanceCharge()) {
          if (tac.getChargeIndicator().getIndicator()) {
            totalChargeAmount =
                totalChargeAmount.add(tac.getActualAmount().get(0).getValue());
          } else {
            totalAllowanceAmount =
                totalAllowanceAmount.add(tac.getActualAmount().get(0).getValue());
          }
        }
      }

      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:LineTotalAmount
      stsms.withLineTotalAmount(
          new AmountType().withValue(totalLineAmount).withCurrencyID(documentCurrency
          ));

      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:ChargeTotalAmount
      stsms.withChargeTotalAmount(
          new AmountType().withValue(totalChargeAmount).withCurrencyID(documentCurrency
          ));

      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:AllowanceTotalAmount
      stsms.withAllowanceTotalAmount(
          new AmountType().withValue(totalAllowanceAmount).withCurrencyID(documentCurrency
          ));

      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:TaxBasisTotalAmount
      stsms.withTaxBasisTotalAmount(
          new AmountType().withValue(totalTaxBasisAmount).withCurrencyID(documentCurrency
          ));

      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:TaxTotalAmount
      stsms.withTaxTotalAmount(
          new AmountType().withValue(totalTaxAmount).withCurrencyID(documentCurrency
          ));
    }
  }


  /**
   * Map the tax details to the ZUGFeRD equivalent
   * @param zugferd
   * @param tax
   */
  private void mapTax(CrossIndustryDocumentType zugferd, Tax tax) {
    //eb:UniversalBankTransaction / eb:DirectDebit
    if (tax != null) {
      String
          documentCurrency =
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

      if (tax.getVAT() != null && tax.getVAT().getVATItems().size() > 0) {

        for (VATItem vATItems : tax.getVAT().getVATItems()) {
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:ApplicableTradeTax
          TradeTaxType tradeTaxType = new TradeTaxType();

          //eb:TaxedAmount
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:BasisAmount
          if (vATItems.getTaxedAmount() != null) {
            tradeTaxType.withBasisAmount(new AmountType().withValue(vATItems.getTaxedAmount())
                                             .withCurrencyID(documentCurrency));
          }

          //eb:VATRate
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:ApplicablePercent
          if (vATItems.getVATRate() != null) {
            tradeTaxType.withApplicablePercent(
                new PercentType()
                    .withValue(vATItems.getVATRate().getValue()));
          }

          if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:ExcemptionReason
            if (vATItems.getTaxExemption() != null) {
              String addCode = "";

              //eb:TaxException//eb:TaxExemptionCode
              if (vATItems.getTaxExemption().getTaxExemptionCode() != null) {
                addCode = vATItems.getTaxExemption().getTaxExemptionCode();
              }

              //eb:TaxException
              tradeTaxType.withExemptionReason(
                  new TextType().withValue(addCode + " " + vATItems.getTaxExemption().getValue()));
            }
          }

          //eb:Amount
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:CalculatedAmount
          if (vATItems.getAmount() != null) {
            tradeTaxType.withCalculatedAmount(
                new AmountType().withValue(vATItems.getAmount())
                    .withCurrencyID(documentCurrency));
          }

          //Tax type - always VAT in this case
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:TypeCode
          tradeTaxType.withTypeCode(new TaxTypeCodeType().withValue("VAT"));
        }
      }

      //eb:OtherTax
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/?
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
            //eb:Amount
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:ActualAmount
            amount = otherTax.getAmount();
          }

          if (otherTax.getComment() != null) {
            //eb:Comment
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:Reason
            comment = otherTax.getComment();
          }

          //Create TradeAllowanceCharge and add it to ZUGFeRD
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge
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
   * @param zugferd
   * @param reductionAndSurchargeDetails
   */
  private void mapReductionAndSurchargeDetails(CrossIndustryDocumentType zugferd,
                                               ReductionAndSurchargeDetails reductionAndSurchargeDetails) {
    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
        && reductionAndSurchargeDetails != null) {
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
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge
          TradeAllowanceChargeType stac;

          if (rSVItem instanceof OtherVATableTax) {
            OtherVATableTax
                oVatItem =
                (OtherVATableTax) rSVItem;

            //Taxes are surcharges => chargeIndicator: true
            chargeIndicator = true;

            if (oVatItem.getBaseAmount() != null) {
              //eb:BaseAmount
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:BasisAmount
              baseAmount = oVatItem.getBaseAmount();
            }

            if (oVatItem.getPercentage() != null) {
              //eb:Percentage
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:CalculationPercent
              percentage = oVatItem.getPercentage();
            }

            if (oVatItem.getAmount() != null) {
              //eb:Amount
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:ActualAmount
              amount = oVatItem.getAmount();
            }

            if (oVatItem.getComment() != null) {
              //eb:TaxID
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:Reason
              comment = oVatItem.getComment() + "\n";
            }

            if (oVatItem.getComment() != null) {
              //eb:Comment
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:Reason
              comment += oVatItem.getComment();
            }

            stac =
                getTradeAllowanceCharge(chargeIndicator, baseAmount, documentCurrency,
                                        percentage,
                                        amount, comment.trim());

            if (oVatItem.getVATRate() != null) {
              //eb:VATRate
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:CategoryTradeTax
              stac.withCategoryTradeTax(
                  new TradeTaxType().withTypeCode(new TaxTypeCodeType().withValue("VAT"))
                      .withCategoryCode(new TaxCategoryCodeType().withValue("S"))
                      .withApplicablePercent(
                          new PercentType().withValue(oVatItem.getVATRate().getValue())));
            }
          } else {
            JAXBElement<? extends Serializable> jabxItem = (JAXBElement<? extends Serializable>) rSVItem;

            ReductionAndSurchargeType
                rsItem =
                (ReductionAndSurchargeType) jabxItem.getValue();

            //Surcharge (SurchargeListLineItem) => chargeIndicator: true
            //Reduction (ReductionListLineItem) => chargeIndicator: false
            chargeIndicator =
                jabxItem.getName().getLocalPart()
                    .equals("Surcharge");

            if (rsItem.getBaseAmount() != null) {
              //eb:BaseAmount
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:BasisAmount
              baseAmount = rsItem.getBaseAmount();
            }

            if (rsItem.getPercentage() != null) {
              //eb:Percentage
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:CalculationPercent
              percentage = rsItem.getPercentage();
            }

            if (rsItem.getAmount() != null) {
              //eb:Amount
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:ActualAmount
              amount = rsItem.getAmount();
            }

            if (rsItem.getComment() != null) {
              //eb:Comment
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:Reason
              comment = rsItem.getComment();
            }

            stac =
                getTradeAllowanceCharge(chargeIndicator, baseAmount, documentCurrency,
                                        percentage,
                                        amount, comment);

            if (rsItem.getVATRate() != null) {
              //eb:VATRate
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeSettlement/ram:SpecifiedTradeAllowanceCharge/ram:CategoryTradeTax
              stac.withCategoryTradeTax(
                  new TradeTaxType().withTypeCode(new TaxTypeCodeType().withValue("VAT"))
                      .withCategoryCode(new TaxCategoryCodeType().withValue("S"))
                      .withApplicablePercent(new PercentType().withValue(rsItem.getVATRate()
                                                                             .getValue())));
            }
          }

          //add SpecifiedTradeAllowanceCharge to ZUGFeRD
          //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge
          ascts.withSpecifiedTradeAllowanceCharge(stac);
        }
      }
    }
  }

  /**
   * Map the details section of ebInterace, containing the different line items, to the correct
   * fields in ZUGFeRD
   * from: eb:Details
   * to: rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem
   * @param zugferd
   * @param details
   */
  private void mapDetails(CrossIndustryDocumentType zugferd, Details details) {
    if (details.getItemLists() != null && details.getItemLists().size() > 0) {
      //TODO Elemente für Header und Footer nicht in ZUGFeRD verfügbar
      //eb:HeaderDescription
      //eb:ItemList/HeaderDescription
      //eb:ItemList/FooterDescription
      //eb:FooterDescription

      String
          documentCurrency =
          zugferd.getSpecifiedSupplyChainTradeTransaction()
              .getApplicableSupplyChainTradeSettlement().getInvoiceCurrencyCode().getValue();

      //Create a collection of SupplyChainTradeLineItems
      List<SupplyChainTradeLineItemType> listSCTLI = new ArrayList<SupplyChainTradeLineItemType>();
      TreeSet<BigInteger> posNr = new TreeSet();

      int iList = 0;

      //loop all eb:ItemLists
      for (ItemList itemList : details.getItemLists()) {
        if (itemList.getListLineItems() != null && itemList.getListLineItems().size() > 0) {
          int iItems = 0;

          //loop all eb:ListLineItems
          for (ListLineItem item : itemList.getListLineItems()) {
            //Create a SupplyChainTradeLineItem for a Detail
            SupplyChainTradeLineItemType sctli = new SupplyChainTradeLineItemType();

            //create a SpecifiedTradeProduct
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedTradeProduct
            TradeProductType stp = new TradeProductType();
            sctli.withSpecifiedTradeProduct(stp);

            if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
              //eb:PositionNumber
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:AssociatedDocumentLineDocument/ram:LineID
              if (item.getPositionNumber() != null) {
                sctli.withAssociatedDocumentLineDocument(new DocumentLineDocumentType().withLineID(
                    new IDType().withValue(item.getPositionNumber().toString())));
                posNr.add(item.getPositionNumber());
              } else {
                BigInteger tPosNr = posNr.last().add(new BigInteger("100"));
                sctli.withAssociatedDocumentLineDocument(new DocumentLineDocumentType().withLineID(
                    new IDType().withValue(tPosNr.toString())));
                posNr.add(tPosNr);
              }
            }

            //eb:Description
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
              //eb:ArticleNumber
              if (item.getArticleNumbers() != null && item.getArticleNumbers().size() > 0) {
                int iArt = 0;

                for (ArticleNumber art : item.getArticleNumbers()) {
                  if (art.getArticleNumberType().value().equals("GTIN")) {
                    //GTIN
                    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedTradeProduct/ram:GlobalID
                    stp.withGlobalID(new IDType().withValue(art.getContent()).withSchemeID("0160"));
                  } else if (art.getArticleNumberType().value()
                      .equals("InvoiceRecipientsArticleNumber")) {
                    //InvoiceRecipientsArticleNumber
                    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedTradeProduct/ram:BuyerAssignedID
                    stp.withBuyerAssignedID(new IDType().withValue(art.getContent()));
                  } else if (art.getArticleNumberType().value().equals("BillersArticleNumber")) {
                    //BillersArticleNumber
                    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedTradeProduct/ram:SellerAssignedID
                    stp.withSellerAssignedID(new IDType().withValue(art.getContent()));
                  } else if (art.getArticleNumberType().value().equals("PZN")) {
                    //PZN
                    //TODO - not in Zugferd
                    mLog.add(
                        "ArticleNumber 'PZN' not mapped to ZUGFeRD: no proper element in ZUGFeRD",
                        "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                        + "]/ArticleNumber[" + iArt + "]",
                        "???");

                  }

                  iArt++;
                }
              }
            }

            //Create SupplyChainTradeDelivery and add it to ZUGFeRD
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery
            SupplyChainTradeDeliveryType ssctd = new SupplyChainTradeDeliveryType();
            sctli.withSpecifiedSupplyChainTradeDelivery(ssctd);

            //eb:Quantity
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery/ram:BilledQuantity
            if (item.getQuantity() != null) {
              ssctd.withBilledQuantity(new QuantityType().withValue(item.getQuantity().getValue())
                                           .withUnitCode(item.getQuantity().getUnit()));
            }

            //Create SpecifiedSupplyChainTradeAgreement and add it to ZUGFeRD
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement
            SupplyChainTradeAgreementType scta = new SupplyChainTradeAgreementType();
            sctli.withSpecifiedSupplyChainTradeAgreement(scta);

            if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
              if (item.getUnitPrice() != null) {
                //Create NetPriceProductTradePrice and add it to ZUGFeRD
                TradePriceType npptp = new TradePriceType();
                scta.withNetPriceProductTradePrice(npptp);

                //eb:UnitPrice
                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:NetPriceProductTradePrice
                npptp.withChargeAmount(new AmountType().withValue(item.getUnitPrice().getValue())
                                           .withCurrencyID(documentCurrency));

                if (item.getUnitPrice().getBaseQuantity() != null) {
                  npptp.withBasisQuantity(
                      new QuantityType().withValue(item.getUnitPrice().getBaseQuantity())
                          .withUnitCode(item.getQuantity().getUnit()));
                }
              }
            }

            //Create SpecifiedSupplyChainTradeSettlement and add it to ZUGFeRD
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeSettlement
            SupplyChainTradeSettlementType scts = new SupplyChainTradeSettlementType();
            sctli.withSpecifiedSupplyChainTradeSettlement(scts);

            if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
              //Create ApplicableTradeTax and add it to ZUGFeRD
              TradeTaxType att = new TradeTaxType();
              scts.withApplicableTradeTax(att);

              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:TypeCode
              att.withTypeCode(new TaxTypeCodeType().withValue("VAT"));

              if (item.getTaxExemption() != null) {
                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:ExemptionReason
                att.withExemptionReason(
                    new TextType().withValue(item.getTaxExemption().getValue()));

                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:CategoryCode
                att.withCategoryCode(new TaxCategoryCodeType().withValue("E"));

                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:ApplicablePercent
                att.withApplicablePercent(
                    new PercentType().withValue(new BigDecimal(0)));
              } else {
                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:CategoryCode
                att.withCategoryCode(new TaxCategoryCodeType().withValue("S"));

                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeSettlement/ram:ApplicableTradeTax/ram:ApplicablePercent
                att.withApplicablePercent(
                    new PercentType().withValue(item.getVATRate().getValue()));
              }
            }

            //Create GrossPriceProductTradePrice and add it to ZUGFeRD
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice
            TradePriceType gpptp = new TradePriceType();
            scta.withGrossPriceProductTradePrice(gpptp);

            //(eb:Quantity/nvl(eb:BaseQuantity, 1)*eb:UnitPrice)
            BigDecimal quantity = item.getQuantity().getValue();
            BigDecimal baseQuantity;
            if (item.getUnitPrice().getBaseQuantity() != null) {
              baseQuantity = item.getUnitPrice().getBaseQuantity();
            } else {
              baseQuantity = new BigDecimal(1);
            }
            BigDecimal unitPrice = item.getUnitPrice().getValue();
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:ChargeAmount
            gpptp.withChargeAmount(
                new AmountType().withValue(quantity.divide(baseQuantity).multiply(unitPrice))
                    .withCurrencyID(documentCurrency));

            if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {

              if (item.getDiscountFlag() != null) {
                //eb:DiscountFlag
                //TODO - not in ZUGFeRD
                mLog.add(
                    "DiscountFlag not mapped to ZUGFeRD: no proper element in ZUGFeRD",
                    "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                    + "]/DiscountFlag",
                    "???");
              }

              //eb:ReductionAndSurchargeListLineItemDetails
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
                    ReductionAndSurchargeBaseType
                        rsItem =
                        (ReductionAndSurchargeBaseType) rSVItem.getValue();

                    //Surcharge (SurchargeListLineItem) => chargeIndicator: true
                    //Reduction (ReductionListLineItem) => chargeIndicator: false
                    chargeIndicator =
                        rSVItem.getName().getLocalPart().equals("SurchargeListLineItem");

                    if (rsItem.getBaseAmount() != null) {
                      //eb:BaseAmount
                      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:BasisAmount
                      baseAmount = rsItem.getBaseAmount();
                    }

                    if (rsItem.getPercentage() != null) {
                      //eb:Percentage
                      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:CalculationPercent
                      percentage = rsItem.getPercentage();
                    }

                    if (rsItem.getAmount() != null) {
                      //eb:Amount
                      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:ActualAmount
                      amount = rsItem.getAmount();
                    }

                    if (rsItem.getComment() != null) {
                      //eb:Comment
                      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:Reason
                      comment = rsItem.getComment();
                    }
                  } else { //rSVItem.getName().getLocalPart().equals("OtherVATableTaxListLineItem")
                    OtherVATableTaxBaseType
                        otherTaxItem =
                        (OtherVATableTaxBaseType) rSVItem.getValue();

                    //Taxes are surcharges => chargeIndicator: true
                    chargeIndicator = true;

                    if (otherTaxItem.getBaseAmount() != null) {
                      //eb:BaseAmount
                      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:BasisAmount
                      baseAmount = otherTaxItem.getBaseAmount();
                    }

                    if (otherTaxItem.getPercentage() != null) {
                      //eb:Percentage
                      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:CalculationPercent
                      percentage = otherTaxItem.getPercentage();
                    }

                    if (otherTaxItem.getAmount() != null) {
                      //eb:Amount
                      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:ActualAmount
                      amount = otherTaxItem.getAmount();
                    }

                    if (otherTaxItem.getTaxID() != null) {
                      //eb:TaxID
                      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:Reason
                      comment = otherTaxItem.getTaxID() + "\n";
                    }

                    if (otherTaxItem.getComment() != null) {
                      //eb:Comment
                      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:Reason
                      comment += otherTaxItem.getComment();
                    }
                  }

                  //Create TradeAllowanceCharge and add it to ZUGFeRD
                  //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge
                  gpptp.withAppliedTradeAllowanceCharge(
                      getTradeAllowanceCharge(chargeIndicator, baseAmount, documentCurrency,
                                              percentage,
                                              amount, comment.trim()));
                }
              }
            }

            //eb:Delivery
            if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)
                && item.getDelivery() != null) {
              //Create the necessary elements in ZUGFeRD
              TradePartyType stttp = new TradePartyType();
              ssctd.withShipToTradeParty(stttp);

              //eb:DeliveryID
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery/ram:DeliveryNoteReferencedDocument/ram:ID
              ssctd.withDeliveryNoteReferencedDocument(new ReferencedDocumentType()
                                                           .withID(new IDType().withValue(
                                                               item.getDelivery()
                                                                   .getDeliveryID())));

              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery/ram:ActualDeliverySupplyChainEvent/ram:OccurrenceDateTime/udt:DateTimeString
              if (item.getDelivery().getDate() != null) {
                //eb:Delivery/Date
                ssctd.withActualDeliverySupplyChainEvent(
                    new SupplyChainEventType().withOccurrenceDateTime(
                        new DateTimeType()
                            .withDateTimeString(new DateTimeType.DateTimeString().withValue(
                                dateTimeFormatter.print(item.getDelivery().getDate())).withFormat(
                                "102"))));
              } else if (item.getDelivery().getPeriod().getFromDate() != null) {
                //eb:Delivery/Period/FromDate
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

              if (item.getDelivery().getAddress() != null) {

                //Create the necessary elements in ZUGFeRD
                stttp.withDefinedTradeContact(new TradeContactType());
                stttp.withPostalTradeAddress(new TradeAddressType());

                Address address = item.getDelivery().getAddress();

                String partyName = "";
                //Build the person name string
                if (!Strings.isNullOrEmpty(address.getSalutation())) {
                  partyName += address.getSalutation() + " ";
                }
                if (!Strings.isNullOrEmpty(address.getName())) {
                  partyName += address.getName();
                }

                //eb:Salutation and Name
                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:Name
                stttp.withName(new TextType().withValue(partyName));

                //eb:Street
                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:PostalTradeAddress/ram:LineOne
                stttp.getPostalTradeAddress()
                    .setLineOne(new TextType().withValue(address.getStreet()));

                //eb:Town
                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:PostalTradeAddress/ram:CityName
                stttp.getPostalTradeAddress()
                    .setCityName(new TextType().withValue(address.getTown()));

                //eb:ZIP
                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:PostalTradeAddress/ram:PostcodeCode
                stttp.getPostalTradeAddress().getPostcodeCode()
                    .add(new CodeType().withValue(address.getZIP()));

                //eb:Country Code
                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:PostalTradeAddress/ram:CountryID
                stttp.getPostalTradeAddress().setCountryID(
                    new CountryIDType().withValue(address.getCountry().getCountryCode().value()));

                //eb:Phone
                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:DefinedTradeContact/ram:TelephoneUniversalCommunication/ram:CompleteNumber
                stttp.getDefinedTradeContact().get(0)
                    .withTelephoneUniversalCommunication(
                        new UniversalCommunicationType().withCompleteNumber(
                            new TextType().withValue(address.getPhone())));

                //eb:Email
                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:DefinedTradeContact/ram:EmailURIUniversalCommunication/ram:CompleteNumber
                stttp.getDefinedTradeContact().get(0)
                    .withEmailURIUniversalCommunication(
                        new UniversalCommunicationType().withURIID(
                            new IDType().withValue(address.getEmail())));

                //eb:Contact
                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:DefinedTradeContact/ram:PersonName
                stttp.getDefinedTradeContact().get(0)
                    .setPersonName(new TextType().withValue(address.getContact()));

                if (item.getDelivery().getAddress().getAddressExtensions() != null) {
                  //eb:Address extension
                  //TODO - not in ZUGFeRD
                  mLog.add(
                      "AddressExtensions not mapped to ZUGFeRD: no proper element in ZUGFeRD",
                      "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                      + "]/Delivery/Address/AddressExtensions",
                      "???");
                }
              }

              if (item.getDelivery().getDescription() != null) {
                //eb:Description
                //TODO - no field in ZUGFeRD for that
                mLog.add(
                    "Description not mapped to ZUGFeRD: no proper element in ZUGFeRD",
                    "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                    + "]/Delivery/Description",
                    "???");
              }
            }

            if (item.getBillersOrderReference() != null) {
              //eb:BillersOrderReference
              //TODO - no field in ZUGFeRD for that
              mLog.add(
                  "BillersOrderReference not mapped to ZUGFeRD: no proper element in ZUGFeRD",
                  "/Invoice/Details/ItemList[" + iList + "]/ListLineItem[" + iItems
                  + "]/BillersOrderReference",
                  "???");
            }

            //eb:InvoiceRecipientsOrderReference
            if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)
                && item.getInvoiceRecipientsOrderReference() != null) {

              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:BuyerOrderReferencedDocument
              ReferencedDocumentType bor = new ReferencedDocumentType();
              scta.withBuyerOrderReferencedDocument(bor);

              //eb:OrderID
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:BuyerOrderReferencedDocument/ram:OrderID
              if (item.getInvoiceRecipientsOrderReference().getOrderID() != null) {
                bor.withID(
                    new IDType().withValue(item.getInvoiceRecipientsOrderReference().getOrderID()));
              }

              //eb:OrderPositionNumber
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:BuyerOrderReferencedDocument/ram:LineID
              if (item.getInvoiceRecipientsOrderReference().getOrderPositionNumber() != null) {
                bor.withLineID(
                    new IDType().withValue(
                        item.getInvoiceRecipientsOrderReference().getOrderPositionNumber()));
              }

              //eb:ReferenceDate
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:BuyerOrderReferencedDocument/ram:IssueDateTime
              if (item.getInvoiceRecipientsOrderReference().getReferenceDate() != null) {
                bor.withIssueDateTime(issueDateTimeFormatter
                                          .print(
                                              item.getInvoiceRecipientsOrderReference()
                                                  .getReferenceDate()));
              }
            }

            //eb:AdditionalInformation
            //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedTradeProduct/ram:ApplicableProductCharacteristic
            if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)
                && item.getAdditionalInformation() != null) {
              AdditionalInformation ai = item.getAdditionalInformation();

              String typeCode, description, unitCode, value;
              BigDecimal valueMeasure = null;

              //ed:SerialNumber
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

              //ed:ChargeNumber
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

              //ed:Classifications
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

              //ed:AlternativeQuantity
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

              //ed:Size
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

              //ed:Weight
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

              //ed:Boxes
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

              //ed:Color
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
              //eb:LineItemAmount
              //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:LineTotalAmount
              scts.withSpecifiedTradeSettlementMonetarySummation(
                  new TradeSettlementMonetarySummationType().withLineTotalAmount(
                      new AmountType().withValue(item.getLineItemAmount())
                          .withCurrencyID(documentCurrency)));

              if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)
                  && gpptp.getAppliedTradeAllowanceCharge() != null
                  && gpptp.getAppliedTradeAllowanceCharge().size() > 0) {
                BigDecimal sum = new BigDecimal(0);

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

                  if (am != null && ch.getChargeIndicator().getIndicator()) {
                    sum = sum.subtract(am);
                  } else {
                    sum = sum.add(am);
                  }
                }

                //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem[/ram:SpecifiedSupplyChainTradeSettlement/ram:SpecifiedTradeSettlementMonetarySummation/ram:TotalAllowanceChargeAmount
                scts.getSpecifiedTradeSettlementMonetarySummation().withTotalAllowanceChargeAmount(
                    new AmountType().withValue(sum).withCurrencyID(documentCurrency));
              }
            }

            if (item.getListLineItemExtension() != null
                && item.getListLineItemExtension().getListLineItemExtension() != null &&
                item.getListLineItemExtension().getListLineItemExtension()
                    .getBeneficiarySocialInsuranceNumber() != null) {
              //eb:ListLineItemExtension//eb:ListLineItemExtension//eb:BeneficiarySocialInsuranceNumber
              //TODO - not in ZUGFeRD
              mLog.add(
                  "BeneficiarySocialInsuranceNumber not mapped to ZUGFeRD: no proper element in ZUGFeRD",
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

  /**
   * Map the attributes from the ebInterface ROOT element
   */
  private void mapRootAttributes(CrossIndustryDocumentType zugferd, Invoice invoice) {
    //ZUGFeRD type
    String zugFeRDType = getZUGfeRDType();
    zugferd.getSpecifiedExchangedDocumentContext().withGuidelineSpecifiedDocumentContextParameter(
        new DocumentContextParameterType().withID(new IDType().withValue(zugFeRDType)));

    if (invoice.getGeneratingSystem() != null) {
      //eb:GeneratingSystem
      //TODO - no field in ZUGFeRD for that
      mLog.add(
          "GeneratingSystem not mapped to ZUGFeRD: no proper element in ZUGFeRD",
          "/Invoice/GeneratingSystem",
          "???");
    }

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

    if (invoice.getManualProcessing() != null) {
      //eb:ManualProcessing
      //TODO - no field in ZUGFeRD for that
      mLog.add(
          "ManualProcessing not mapped to ZUGFeRD: no proper element in ZUGFeRD",
          "/Invoice/ManualProcessing",
          "???");
    }

    if (invoice.getDocumentTitle() != null) {
      //eb:DocumentTitle
      //TODO - no field in ZUGFeRD for that
      mLog.add(
          "DocumentTitle not mapped to ZUGFeRD: no proper element in ZUGFeRD",
          "/Invoice/DocumentTitle",
          "???");
    }

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

    }
    //eb:Invoice number
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:ID
    zugferd.getHeaderExchangedDocument().withID(
        new IDType().withValue(invoice.getInvoiceNumber()));

    //eb:Invoice date
    //rsm:CrossIndustryDocument/rsm:HeaderExchangedDocument/ram:IssueDateTime
    zugferd.getHeaderExchangedDocument().withIssueDateTime(new DateTimeType().withDateTimeString(
        new DateTimeType.DateTimeString().withFormat("102")
            .withValue(issueDateTimeFormatter.print(invoice.getInvoiceDate()))));
  }

  /**
   * Map the signature from the ebInterface
   */
  private void mapSignature(CrossIndustryDocumentType zugferd, Signature signature) {
    //TODO not supported in ZUGFeRD
    if (signature != null) {
      //eb:Signature
      //TODO - no field in ZUGFeRD for that
      mLog.add(
          "Signature not mapped to ZUGFeRD: no proper element in ZUGFeRD",
          "/Invoice/Signature",
          "???");
    }
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

      //eb:VATIdentification
      productEndUserTradeParty.withSpecifiedTaxRegistration(new TaxRegistrationType().withID(
          new IDType().withValue(orderingParty.getVATIdentificationNumber()).withSchemeID("VA")));

      //eb:FurtherIdentification
      if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
          && orderingParty.getFurtherIdentifications() != null
          && orderingParty.getFurtherIdentifications().size() > 0) {

        int i = 0;

        for (FurtherIdentification furtherIdentification : orderingParty
            .getFurtherIdentifications()) {
          if (i == 0) {
            productEndUserTradeParty
                .withID(new IDType().withValue(furtherIdentification.getValue()));
            mLog.add(
                "IdentificationType '" + furtherIdentification.getIdentificationType() + "' not mapped to ZUGFeRD: Attribute @schemeID marked as not used in the given context",
                "/Invoice/OrderingParty/FurtherIdentification["+i+"]",
                "/rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:ProductEndUserTradeParty/ram:ID");
          } else {
            mLog.add(
                "FurtherIdentification '" + furtherIdentification.getValue() + "'not mapped to ZUGFeRD: only 1 element ID allowed",
                "/Invoice/OrderingParty/FurtherIdentification["+i+"]",
                "/rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:ProductEndUserTradeParty/ram:ID");
          }
          i++;
        }

        //eb:OrderingParty/Address/Addressidentifier/@AddressIdentifierType=(GLN, DUNS)
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ProductEndUserTradeParty/GlobalID
        if (orderingParty.getAddress().getAddressIdentifiers().get(0) != null) {
          String schema = null;
          if (orderingParty.getAddress().getAddressIdentifiers().get(0).getAddressIdentifierType()
              .equals(AddressIdentifierTypeType.DUNS)) {
            schema = "0060";
          } else if (orderingParty.getAddress().getAddressIdentifiers().get(0)
              .getAddressIdentifierType()
              .equals(AddressIdentifierTypeType.GLN)) {
            schema = "0088";
          }
          if (schema != null) {
            productEndUserTradeParty.withGlobalID(
                new IDType().withValue(
                    orderingParty.getAddress().getAddressIdentifiers().get(0).getValue())
                    .withSchemeID(schema));
          }
        }
      }


      if (orderingParty.getOrderReference() != null) {
        //OrderReference
        //TODO
        mLog.add(
            "OrderReference not mapped to ZUGFeRD: no proper element in ZUGFeRD",
            "/Invoice/OrderingParty/OrderReference",
            "???");
      }

      //eb:Address
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


      mLog.add(
          "BillersInvoiceRecipientID not mapped to ZUGFeRD: no proper element in ZUGFeRD",
          "/Invoice/OrderingParty/BillersInvoiceRecipientID",
          "???");
      /*//eb:BillersInvoiceRecipientID
      if (!Strings.isNullOrEmpty(orderingParty.getBillersOrderingPartyID())) {
        productEndUserTradeParty.withID(
            new IDType().withValue(orderingParty.getBillersOrderingPartyID()).withSchemeID(
                "Rechnungsempfänger-ID des Rechnungsstellers"));
      }*/
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

    //eb:VATIdentification
    invoiceRecipientTradePartyType.withSpecifiedTaxRegistration(new TaxRegistrationType().withID(
        new IDType().withValue(invoiceRecipient.getVATIdentificationNumber()).withSchemeID("VA")));

    //eb:FurtherIdentification
    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)
        && invoiceRecipient.getFurtherIdentifications() != null
        && invoiceRecipient.getFurtherIdentifications().size() > 0) {

      int i = 0;

      for (FurtherIdentification furtherIdentification : invoiceRecipient
          .getFurtherIdentifications()) {
        if (i == 0) {
          invoiceRecipientTradePartyType
              .withID(new IDType().withValue(furtherIdentification.getValue()));
          mLog.add(
              "IdentificationType '" + furtherIdentification.getIdentificationType()
              + "' not mapped to ZUGFeRD: Attribute @schemeID marked as not used in the given context",
              "/Invoice/InvoiceRecipient/FurtherIdentification[" + i + "]",
              "/rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:BuyerTradeParty/ram:ID");
        } else {
          mLog.add(
              "FurtherIdentification '" + furtherIdentification.getValue()
              + "'not mapped to ZUGFeRD: only 1 element ID allowed",
              "/Invoice/InvoiceRecipient/FurtherIdentification[" + i + "]",
              "/rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:BuyerTradeParty/ram:ID");
        }
        i++;
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

    if (invoiceRecipient.getOrderReference() != null) {
      //OrderReference
      //TODO
      mLog.add(
          "OrderReference not mapped to ZUGFeRD: no proper element in ZUGFeRD",
          "/Invoice/InvoiceRecipient/OrderReference",
          "???");
    }

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

    if (invoiceRecipient.getBillersInvoiceRecipientID() != null) {
      mLog.add(
          "BillersInvoiceRecipientID not mapped to ZUGFeRD: no proper element in ZUGFeRD",
          "/Invoice/InvoiceRecipient/BillersInvoiceRecipientID",
          "???");
      //TODO
      /*
      //eb:BillersInvoiceRecipientID
      if (!Strings.isNullOrEmpty(invoiceRecipient.getBillersInvoiceRecipientID())) {
        invoiceRecipientTradePartyType.withID(
            new IDType().withValue(invoiceRecipient.getBillersInvoiceRecipientID()).withSchemeID(
                "Rechnungsempfänger-ID des Rechnungsstellers"));
      }
      */
    }

    if (invoiceRecipient.getAccountingArea() != null) {
      //eb:AccountingArea
      //TODO - no respective field
      mLog.add(
          "AccountingArea not mapped to ZUGFeRD: no proper element in ZUGFeRD",
          "/Invoice/InvoiceRecipient/AccountingArea",
          "???");
    }

    if (invoiceRecipient.getSubOrganizationID() != null) {
      //eb:SubOrganizationID
      //TODO - no respective field
      mLog.add(
          "SubOrganizationID not mapped to ZUGFeRD: no proper element in ZUGFeRD",
          "/Invoice/InvoiceRecipient/SubOrganizationID",
          "???");
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

    //eb:VATIdentification
    sellerTradePartyType.withSpecifiedTaxRegistration(new TaxRegistrationType().withID(
        new IDType().withValue(biller.getVATIdentificationNumber()).withSchemeID("VA")));

    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
      //eb:Biller/Address/Addressidentifier/@AddressIdentifierType
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

        //eb:OrderReference
        //CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/BuyerReference
        if (biller.getOrderReference().getOrderID() != null) {
          supplyChainTradeAgreementType
              .withBuyerReference(new TextType().withValue(biller.getOrderReference().getOrderID()));
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
    }

    if (!MappingFactory.MappingType.ZUGFeRD_BASIC_1p0.equals(mappingType)) {
      //eb:Biller/InvoiceRecipientsBillerID
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/SellerTradeParty/ID
      if (biller.getInvoiceRecipientsBillerID() != null) {
        sellerTradePartyType.withID(new IDType().withValue(biller.getInvoiceRecipientsBillerID()));
      }
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

    int i = 0;

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
            issueDateTimeFormatter.print(relatedDocument.getInvoiceDate()));

        //eb:Document type
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument/ram:TypeCode
        //OI = Reference number identifying a previously issued invoice. (http://www.unece.org/trade/untdid/i98a/uncl/uncl1153.htm)
        referencedDocumentType.withTypeCode(
            new DocumentCodeType().withValue("OI"));

        if (relatedDocument.getComment() != null) {
          //eb:Comment
          //TODO - not really an element in ZUGFeRD which fits here...
          mLog.add(
              "OrderReference not mapped to ZUGFeRD: no proper element in ZUGFeRD",
              "/Invoice/RelatedDocument["+i+"]/Comment",
              "???");
        }
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

      i++;
    }
  }

  /**
   * Map the details of the cancelled document Target in ZUGFeRD:
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
          issueDateTimeFormatter.print(cancelledOriginalDocument.getInvoiceDate()));

      //eb:Document type
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeAgreement/ram:AdditionalReferencedDocument/ram:TypeCode
      //ACW = Reference number assigned to the message which was previously issued (e.g. in the case of a cancellation,
      //      the primary reference of the message to be cancelled will be quoted in this element). (http://www.unece.org/trade/untdid/i98a/uncl/uncl1153.htm)
      referencedDocumentType.withTypeCode(
          new DocumentCodeType().withValue("ACW"));

      if (cancelledOriginalDocument.getComment() != null) {
        //eb:Comment
        //TODO - not really an element in ZUGFeRD which fits here...
        mLog.add(
            "CancelledOriginalDocument not mapped to ZUGFeRD: no proper element in ZUGFeRD",
            "/Invoice/CancelledOriginalDocument/Comment",
            "???");
      }

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
                new UniversalCommunicationType().withURIID(
                    new IDType().withValue(address.getEmail())));

        //eb:Contact
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:ApplicableSupplyChainTradeDelivery/ram:ShipToTradeParty/ram:DefinedTradeContact/ram:PersonName
        zugferd.getSpecifiedSupplyChainTradeTransaction().getApplicableSupplyChainTradeDelivery()
            .getShipToTradeParty().getDefinedTradeContact().get(0)
            .setPersonName(new TextType().withValue(address.getContact()));

        if (address.getAddressExtensions() != null) {
          //eb:Address extension
          //TODO - no field in ZUGFeRD for that
          mLog.add(
              "AddressExtensions not mapped to ZUGFeRD: no proper element in ZUGFeRD",
              "/Invoice/Delivery/Address/AddressExtensions",
              "???");
        }
      }
    }

    if (delivery.getDescription() != null) {
      //eb:Description
      //TODO - no field in ZUGFeRD for that
      mLog.add(
          "Description not mapped to ZUGFeRD: no proper element in ZUGFeRD",
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

  private TradeAllowanceChargeType getTradeAllowanceCharge (boolean chargeIndicator, BigDecimal baseAmount, String documentCurrency, BigDecimal percentage,
                                                                BigDecimal amount, String comment) {
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge
    TradeAllowanceChargeType atac = new TradeAllowanceChargeType();

    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:ChargeIndicator
    //surcharge: true
    //reduction: false
    atac.withChargeIndicator(new IndicatorType().withIndicator(chargeIndicator));

    if (MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0.equals(mappingType)) {
      if (baseAmount != null) {
        //eb:BaseAmount
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:BasisAmount
        atac.withBasisAmount(
            new AmountType().withValue(baseAmount)
                .withCurrencyID(
                    documentCurrency));
      }

      if (percentage != null) {
        //eb:Percentage
        //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:CalculationPercent
        atac.withCalculationPercent(
            new PercentType().withValue(percentage));
      }
    }

    if (amount != null) {
      //eb:Amount
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:ActualAmount
      atac.withActualAmount(new AmountType().withValue(amount)
                                .withCurrencyID(
                                    documentCurrency));
    }

    if (comment != null) {
      //eb:Comment
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedSupplyChainTradeAgreement/ram:GrossPriceProductTradePrice/ram:AppliedTradeAllowanceCharge/ram:Reason
      atac.withReason(new TextType().withValue(comment));
    }

    return atac;
  }

  private ProductCharacteristicType getApplicableProductCharacteristic (String typeCode, String description, BigDecimal valueMeasure, String unitCode, String value){
    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedTradeProduct/ram:ApplicableProductCharacteristic
    ProductCharacteristicType pc = new ProductCharacteristicType();

    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedTradeProduct/ram:ApplicableProductCharacteristic/ram:TypeCode
    if (typeCode != null) {
      pc.withTypeCode(new CodeType().withValue(typeCode));
    }

    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedTradeProduct/ram:ApplicableProductCharacteristic/ram:Description
    if (description != null) {
      pc.withDescription(new TextType().withValue(description));
    }

    if (valueMeasure != null) {
      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedTradeProduct/ram:ApplicableProductCharacteristic/ram:ValueMeasure
      MeasureType m = new MeasureType();

      m.withValue(valueMeasure);

      //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedTradeProduct/ram:ApplicableProductCharacteristic/ram:ValueMeasure/ram:unitCode
      if (unitCode != null){
        m.withUnitCode(unitCode);
      }

      pc.withValueMeasure(m);
    }

    //rsm:CrossIndustryDocument/rsm:SpecifiedSupplyChainTradeTransaction/ram:IncludedSupplyChainTradeLineItem/ram:SpecifiedTradeProduct/ram:ApplicableProductCharacteristic/ram:Value
    if (value != null) {
      pc.withValue(new TextType().withValue(value));
    }

    return pc;
  }
}