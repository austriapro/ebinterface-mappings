package at.austriapro.utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.annotation.Nonnull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.ebinterface.EEbInterfaceVersion;
import com.helger.ebinterface.builder.EbInterfaceReader;
import com.helger.jaxb.validation.CollectingValidationEventHandler;

import at.austriapro.MappingException;
import at.austriapro.mappings.zugferd.generated.CrossIndustryDocumentType;
import at.austriapro.mappings.zugferd.generated.ObjectFactory;

/**
 * Provides some convenience methods for dealing with ebInterface and ZUGFeRD instances
 */
public class DocumentTypeUtils {


  private static final Logger LOG = LoggerFactory.getLogger(DocumentTypeUtils.class.getName());

  static JAXBContext zugferdContext;
  static Unmarshaller zugferdUnmarshaller;
  static Marshaller zugferdMarshaller;

  static {
    try {
      zugferdContext = JAXBContext.newInstance(CrossIndustryDocumentType.class);
      zugferdUnmarshaller = zugferdContext.createUnmarshaller();
      zugferdMarshaller = zugferdContext.createMarshaller();
      zugferdMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

    } catch (final JAXBException e) {
      LOG.error("Unable to initialize JAXB context.", e);
    }
  }

  @Nonnull
  public static EEbInterfaceVersion getEbInterfaceType (final byte[] input) throws MappingException{
    final CollectingValidationEventHandler aVEH = new CollectingValidationEventHandler ();
    if (EbInterfaceReader.ebInterface41 ().setValidationEventHandler (aVEH).read (input) != null)
      return EEbInterfaceVersion.V41;
    if (EbInterfaceReader.ebInterface42 ().setValidationEventHandler (aVEH).read (input) != null)
      return EEbInterfaceVersion.V42;
    if (EbInterfaceReader.ebInterface43 ().setValidationEventHandler (aVEH).read (input) != null)
      return EEbInterfaceVersion.V43;
    // Other mappings are not supported

    throw new MappingException("Unable to retrieve ebInterface version for ebInterface XML string.");
  }

  /**
   * Parse the given ZUGFeRD input XML string and creates a JAX-B object
   */
  public static CrossIndustryDocumentType parseZUGFeRD(final String input) throws MappingException {
    final StringReader reader = new StringReader(input);
    CrossIndustryDocumentType zugferd;
    try {
      zugferd = (CrossIndustryDocumentType) zugferdUnmarshaller.unmarshal(reader);
    } catch (final JAXBException e) {
      throw new MappingException("Unable to retrieve JAX-B object for ZUGFeRD XML string.");
    }
    return zugferd;
  }

  /**
   * Write the ZUGFeRD JAX-B object to a byte array
   */
  public static byte[] writeZUGFeRD(final CrossIndustryDocumentType zugferd) throws MappingException {

    final StringWriter sw = new StringWriter();
    try {
      final ObjectFactory of = new ObjectFactory();
      final JAXBElement<CrossIndustryDocumentType> cii = of.createCrossIndustryDocument(zugferd);
      zugferdMarshaller.marshal(cii, sw);
    } catch (final JAXBException e) {
      throw new MappingException("Unable to retrieve String from JAX-B ZUGFeRD object.", e);
    }

    return sw.toString().getBytes();
  }

}
