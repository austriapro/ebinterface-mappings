package at.austriapro.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  static JAXBContext ebInterfaceContext4p2;
  static Unmarshaller ebInterfaceUnmarshaller4p2;

  static JAXBContext ebInterfaceContext4p1;
  static Unmarshaller ebInterfaceUnmarshaller4p1;

  static JAXBContext zugferdContext;
  static Unmarshaller zugferdUnmarshaller;
  static Marshaller zugferdMarshaller;

  static {
    try {
      ebInterfaceContext4p1 =
          JAXBContext.newInstance(at.austriapro.mappings.ebinterface4p1.generated.Invoice.class);
      ebInterfaceUnmarshaller4p1 = ebInterfaceContext4p1.createUnmarshaller();

      ebInterfaceContext4p2 =
          JAXBContext.newInstance(at.austriapro.mappings.ebinterface4p2.generated.Invoice.class);
      ebInterfaceUnmarshaller4p2 = ebInterfaceContext4p2.createUnmarshaller();

      zugferdContext = JAXBContext.newInstance(CrossIndustryDocumentType.class);
      zugferdUnmarshaller = zugferdContext.createUnmarshaller();
      zugferdMarshaller = zugferdContext.createMarshaller();
      zugferdMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    } catch (JAXBException e) {
      LOG.error("Unable to initialize JAXB context.", e);
    }
  }

  public static MappingFactory.EbInterfaceMappingType getEbInterfaceType (String input) throws MappingException{
    MappingFactory.EbInterfaceMappingType ebType;

    try{
      at.austriapro.mappings.ebinterface4p1.generated.Invoice inv = parseebInterface4p1(input);
      return MappingFactory.EbInterfaceMappingType.EBINTERFACE_4p1;
    }catch(UnmarshalException ue){
      try{
        at.austriapro.mappings.ebinterface4p2.generated.Invoice inv = parseebInterface4p2(input);
        return MappingFactory.EbInterfaceMappingType.EBINTERFACE_4p2;
      }catch(UnmarshalException uex){
        throw new MappingException("Unable to retrieve ebInterface version for ebInterface XML string.");
      }
    }
  }

  /**
   * Parses the given ebInterface input XML string and creates a JAX-B object (ebInterface4p1)
   */
  public static at.austriapro.mappings.ebinterface4p1.generated.Invoice parseebInterface4p1(
      String input) throws UnmarshalException {

    StringReader reader = new StringReader(input);
    at.austriapro.mappings.ebinterface4p1.generated.Invoice ebInterface4p1 = null;
    try {
      ebInterface4p1 =
          (at.austriapro.mappings.ebinterface4p1.generated.Invoice) ebInterfaceUnmarshaller4p1
              .unmarshal(reader);
    } catch (JAXBException e) {
      throw new UnmarshalException("Unable to retrieve JAX-B object (ebInterface4p1) for ebInterface XML string.");
    }
    return ebInterface4p1;
  }

  /**
   * Parses the given ebInterface input XML string and creates a JAX-B object (ebInterface4p2)
   */
  public static at.austriapro.mappings.ebinterface4p2.generated.Invoice parseebInterface4p2(
      String input) throws UnmarshalException {

    StringReader reader = new StringReader(input);
    at.austriapro.mappings.ebinterface4p2.generated.Invoice ebInterface4p2 = null;
    try {
      ebInterface4p2 =
          (at.austriapro.mappings.ebinterface4p2.generated.Invoice) ebInterfaceUnmarshaller4p2
              .unmarshal(reader);
    } catch (JAXBException e) {
      throw new UnmarshalException("Unable to retrieve JAX-B object (ebInterface4p2) for ebInterface XML string.");
    }
    return ebInterface4p2;
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
