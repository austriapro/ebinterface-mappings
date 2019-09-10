package at.austriapro.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.ebinterface.builder.EbInterfaceReader;
import com.helger.jaxb.validation.CollectingValidationEventHandler;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import at.austriapro.MappingException;
import at.austriapro.MappingFactory;
import at.austriapro.UnmarshalException;
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
      zugferdMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    } catch (JAXBException e) {
      LOG.error("Unable to initialize JAXB context.", e);
    }
  }

  public static MappingFactory.EbInterfaceMappingType getEbInterfaceType (String input) throws MappingException{
    CollectingValidationEventHandler aVEH = new CollectingValidationEventHandler ();
    if (EbInterfaceReader.ebInterface41 ().setValidationEventHandler (aVEH).read (input) != null) 
      return MappingFactory.EbInterfaceMappingType.EBINTERFACE_4p1;
    if (EbInterfaceReader.ebInterface42 ().setValidationEventHandler (aVEH).read (input) != null) 
      return MappingFactory.EbInterfaceMappingType.EBINTERFACE_4p2;
    if (EbInterfaceReader.ebInterface43 ().setValidationEventHandler (aVEH).read (input) != null) 
      return MappingFactory.EbInterfaceMappingType.EBINTERFACE_4p3;

    throw new MappingException("Unable to retrieve ebInterface version for ebInterface XML string.");
  }

  /**
   * Parse the given ZUGFeRD input XML string and creates a JAX-B object
   */
  public static CrossIndustryDocumentType parseZUGFeRD(String input) throws MappingException {
    StringReader reader = new StringReader(input);
    CrossIndustryDocumentType zugferd;
    try {
      zugferd = (CrossIndustryDocumentType) zugferdUnmarshaller.unmarshal(reader);
    } catch (JAXBException e) {
      throw new MappingException("Unable to retrieve JAX-B object for ZUGFeRD XML string.");
    }
    return zugferd;
  }

  /**
   * Write the ZUGFeRD JAX-B object to a byte array
   */
  public static byte[] writeZUGFeRD(CrossIndustryDocumentType zugferd) throws MappingException {

    StringWriter sw = new StringWriter();
    try {
      ObjectFactory of = new ObjectFactory();
      JAXBElement<CrossIndustryDocumentType> cii = of.createCrossIndustryDocument(zugferd);
      zugferdMarshaller.marshal(cii, sw);
    } catch (JAXBException e) {
      throw new MappingException("Unable to retrieve String from JAX-B ZUGFeRD object.", e);
    }

    return sw.toString().getBytes();
  }

}
