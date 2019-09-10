package at.austriapro.test.zugferd;

import net.sf.saxon.Controller;
import net.sf.saxon.serialize.MessageWarner;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import at.austriapro.Mapping;
import at.austriapro.MappingErrorHandler;
import at.austriapro.MappingErrorListener;
import at.austriapro.MappingFactory;
import at.austriapro.utils.DocumentTypeUtils;

/**
 * Test for ZUGFeRD mapping
 */
public class ZUGFeRDMappingTest {


  private static final Logger LOG = LoggerFactory.getLogger(ZUGFeRDMappingTest.class.getName());

  private static Validator validator;

  private static Templates templates;

  static {

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    Schema schema = null;
    try {
      schema = factory.newSchema(new StreamSource(new File(
          ZUGFeRDMappingTest.class
              .getResource("/zugferd1p0/ZUGFeRD1p0.xsd").toURI())));
    } catch (SAXException e) {
      LOG.error(e.getMessage(), e);
    } catch (URISyntaxException e) {
      LOG.error(e.getMessage(), e);
    }

    validator = schema.newValidator();

    try {
      Source xsl = new StreamSource(new File(
          ZUGFeRDMappingTest.class
              .getResource("/zugferd1p0/ZUGFeRD_1p0-compiled.xsl").toURI()));
      TransformerFactory transFactory = net.sf.saxon.TransformerFactoryImpl.newInstance();
      templates = transFactory.newTemplates(xsl);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
    }
  }

  @Test
  public void testMappingBasic(){
    assert(testMapping("/ebinterface/rechnung_ebI_4.1_sample_1.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0));
    assert(testMapping("/ebinterface/rechnung_ebI_4.1_sample_2.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0));
    assert(testMapping("/ebinterface/rechnung_ebI_4.2_sample_1.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0));
    assert(testMapping("/ebinterface/rechnung_ebI_4.2_sample_2.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0));
    assert(testMapping("/ebinterface/rechnung_ebI_4.3_sample_1.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0));
  }

  @Test
  public void testMappingComfort(){
    assert(testMapping("/ebinterface/rechnung_ebI_4.1_sample_1.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0));
    assert(testMapping("/ebinterface/rechnung_ebI_4.1_sample_2.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0));
    assert(testMapping("/ebinterface/rechnung_ebI_4.2_sample_1.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0));
    assert(testMapping("/ebinterface/rechnung_ebI_4.2_sample_2.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0));
    assert(testMapping("/ebinterface/rechnung_ebI_4.3_sample_1.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0));
  }

  @Test
  public void testMappingExtended(){
    assert (testMapping("/ebinterface/rechnung_ebI_4.1_sample_1.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0));
    assert (testMapping("/ebinterface/rechnung_ebI_4.1_sample_2.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0));
    assert (testMapping("/ebinterface/rechnung_ebI_4.2_sample_1.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0));
    assert (testMapping("/ebinterface/rechnung_ebI_4.2_sample_2.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0));
    assert (testMapping("/ebinterface/rechnung_ebI_4.3_sample_1.xml", MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0));
  }

  public boolean testMapping (String ebInterfacePath, MappingFactory.ZugferdMappingType level) {
    String ebInterfaceXML;
    //Read an eb4p2 sample
    try {
      ebInterfaceXML =
          IOUtils.toString(
              ZUGFeRDMappingTest.class.getResourceAsStream(ebInterfacePath),
              StandardCharsets.UTF_8);

      if (ebInterfaceXML == null || ebInterfaceXML.length() == 0){
        throw new Exception("ebInterfaceXML is empty, mapping is not possible.");
      }
    } catch (Exception e) {
      LOG.error("Unable to read ebInterfaceXML");

      return false;
    }

    String levelText;

    if (MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals(level)) {
      levelText = "BASIC";
    } else if (MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0.equals(level)) {
      levelText = "COMFORT";
    } else {
      levelText = "EXTENDED";
    }

    try {
      MappingFactory.EbInterfaceMappingType ebType = DocumentTypeUtils.getEbInterfaceType(ebInterfaceXML);

      String ebTypeText;

      if (ebType == MappingFactory.EbInterfaceMappingType.EBINTERFACE_4p1) {
        ebTypeText = "4.1";
      }else if (ebType == MappingFactory.EbInterfaceMappingType.EBINTERFACE_4p2) {
        ebTypeText = "4.2";
      }
      else {
        ebTypeText = "4.3";
      }

      LOG.info("ebInterface input version: ebInterface {}", ebTypeText);

      MappingFactory mf = new MappingFactory();
      Mapping zugFeRDMapping = mf.getMapper(level, ebType);

      //Map to ZUGFeRD Basic
      String zugferd = new String(zugFeRDMapping.mapFromebInterface(ebInterfaceXML));

      if (LOG.isDebugEnabled()) {
        LOG.debug(zugferd);
      }

      SAXSource saxSource = new SAXSource(new InputSource(
          new ByteArrayInputStream(zugferd.getBytes("UTF-8"))));

      MappingErrorHandler eh = new MappingErrorHandler();
      validator.setErrorHandler(eh);
      validator.validate(saxSource);

      if (eh.catchedError()) {
        throw new RuntimeException(
            "ZUGFeRD-" + levelText + " XSD validation failed:\n" + eh.toString());
      }

      System.out.println(zugFeRDMapping.getMappingLog());

      Source source = new StreamSource(new StringReader(zugferd));
      Result result = new StreamResult(new StringWriter());

      Transformer transformer = templates.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      MappingErrorListener el = new MappingErrorListener();

      transformer.setErrorListener(el);

      //saxon is used, MessageErmitter has to be set, otherwise, ErrorListener will mention Errors
      ((Controller) transformer).setMessageEmitter(new MessageWarner());

      transformer.transform(source, result);

      if (el.catchedError()) {
        throw new RuntimeException(
            "ZUGFeRD-" + levelText + " Schematron validation failed:\n" + el.toString());
      }

      LOG.info("Result of ZUGFeRD-" + levelText + " mapping is {}", zugferd);

      return true;

    } catch (Exception e) {
      LOG.error("Unable to perform ZUGFeRD-" + levelText + " mapping.", e);

      return false;
    }
  }
}
