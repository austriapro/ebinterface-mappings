package at.austriapro.test.zugferd;

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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.helger.commons.io.stream.StreamHelper;
import com.helger.ebinterface.EEbInterfaceVersion;

import at.austriapro.Mapping;
import at.austriapro.MappingErrorHandler;
import at.austriapro.MappingErrorListener;
import at.austriapro.MappingFactory;
import at.austriapro.utils.DocumentTypeUtils;
import net.sf.saxon.jaxp.TransformerImpl;
import net.sf.saxon.serialize.MessageWarner;

/**
 * Test for ZUGFeRD mapping
 */
public class ZUGFeRDMappingTest
{

  private static final Logger LOG = LoggerFactory.getLogger (ZUGFeRDMappingTest.class.getName ());

  private static Validator validator;

  private static Templates templates;

  static
  {

    final SchemaFactory factory = SchemaFactory.newInstance (XMLConstants.W3C_XML_SCHEMA_NS_URI);

    Schema schema = null;
    try
    {
      schema = factory.newSchema (new StreamSource (new File (ZUGFeRDMappingTest.class.getResource ("/zugferd1p0/ZUGFeRD1p0.xsd")
                                                                                      .toURI ())));
    }
    catch (final SAXException e)
    {
      LOG.error (e.getMessage (), e);
    }
    catch (final URISyntaxException e)
    {
      LOG.error (e.getMessage (), e);
    }

    validator = schema.newValidator ();

    try
    {
      final Source xsl = new StreamSource (new File (ZUGFeRDMappingTest.class.getResource ("/zugferd1p0/ZUGFeRD_1p0-compiled.xsl")
                                                                             .toURI ()));
      final TransformerFactory transFactory = TransformerFactory.newInstance ();
      templates = transFactory.newTemplates (xsl);
    }
    catch (final Exception e)
    {
      LOG.error (e.getMessage (), e);
    }
  }

  @Test
  public void testMappingBasic ()
  {
    assert (testMapping ("/ebinterface/rechnung_ebI_4.1_sample_1.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.1_sample_2.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.2_sample_1.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.2_sample_2.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.3_sample_1.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0));
  }

  @Test
  public void testMappingComfort ()
  {
    assert (testMapping ("/ebinterface/rechnung_ebI_4.1_sample_1.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.1_sample_2.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.2_sample_1.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.2_sample_2.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.3_sample_1.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0));
  }

  @Test
  public void testMappingExtended ()
  {
    assert (testMapping ("/ebinterface/rechnung_ebI_4.1_sample_1.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.1_sample_2.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.2_sample_1.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.2_sample_2.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0));
    assert (testMapping ("/ebinterface/rechnung_ebI_4.3_sample_1.xml",
                         MappingFactory.ZugferdMappingType.ZUGFeRD_EXTENDED_1p0));
  }

  public boolean testMapping (final String ebInterfacePath, final MappingFactory.ZugferdMappingType level)
  {
    byte [] ebInterfaceXML;
    // Read an eb4p2 sample
    try
    {
      ebInterfaceXML = StreamHelper.getAllBytes (ZUGFeRDMappingTest.class.getResourceAsStream (ebInterfacePath));

      if (ebInterfaceXML == null || ebInterfaceXML.length == 0)
      {
        throw new Exception ("ebInterfaceXML is empty, mapping is not possible.");
      }
    }
    catch (final Exception e)
    {
      LOG.error ("Unable to read ebInterfaceXML");

      return false;
    }

    String levelText;

    if (MappingFactory.ZugferdMappingType.ZUGFeRD_BASIC_1p0.equals (level))
    {
      levelText = "BASIC";
    }
    else
      if (MappingFactory.ZugferdMappingType.ZUGFeRD_COMFORT_1p0.equals (level))
      {
        levelText = "COMFORT";
      }
      else
      {
        levelText = "EXTENDED";
      }

    try
    {
      final EEbInterfaceVersion ebType = DocumentTypeUtils.getEbInterfaceType (ebInterfaceXML);

      final String ebTypeText = ebType.getVersion ().getAsString (false, true);

      LOG.info ("ebInterface input version: ebInterface {}", ebTypeText);

      final MappingFactory mf = new MappingFactory ();
      final Mapping zugFeRDMapping = mf.getMapper (level, ebType);

      // Map to ZUGFeRD Basic
      final String zugferd = new String (zugFeRDMapping.mapFromebInterface (ebInterfaceXML));

      if (LOG.isDebugEnabled ())
      {
        LOG.debug (zugferd);
      }

      final SAXSource saxSource = new SAXSource (new InputSource (new ByteArrayInputStream (zugferd.getBytes (StandardCharsets.UTF_8))));

      final MappingErrorHandler eh = new MappingErrorHandler ();
      validator.setErrorHandler (eh);
      validator.validate (saxSource);

      if (eh.catchedError ())
      {
        throw new RuntimeException ("ZUGFeRD-" + levelText + " XSD validation failed:\n" + eh.toString ());
      }

      System.out.println (zugFeRDMapping.getMappingLog ());

      final Source source = new StreamSource (new StringReader (zugferd));
      final Result result = new StreamResult (new StringWriter ());

      final Transformer transformer = templates.newTransformer ();
      transformer.setOutputProperty (OutputKeys.INDENT, "yes");

      final MappingErrorListener el = new MappingErrorListener ();

      transformer.setErrorListener (el);

      // saxon is used, MessageErmitter has to be set, otherwise, ErrorListener
      // will mention Errors
      ((TransformerImpl) transformer).getUnderlyingXsltTransformer ()
                                     .getUnderlyingController ()
                                     .setMessageEmitter (new MessageWarner ());

      transformer.transform (source, result);

      if (el.catchedError ())
      {
        throw new RuntimeException ("ZUGFeRD-" + levelText + " Schematron validation failed:\n" + el.toString ());
      }

      LOG.info ("Result of ZUGFeRD-" + levelText + " mapping is {}", zugferd);

      return true;

    }
    catch (final Exception e)
    {
      LOG.error ("Unable to perform ZUGFeRD-" + levelText + " mapping.", e);

      return false;
    }
  }
}
