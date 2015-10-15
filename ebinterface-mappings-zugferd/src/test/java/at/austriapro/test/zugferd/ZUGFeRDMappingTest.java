package at.austriapro.test.zugferd;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import at.austriapro.Mapping;
import at.austriapro.MappingFactory;

/**
 * Test for ZUGFeRD mapping
 */
public class ZUGFeRDMappingTest {


  private static final Logger LOG = LoggerFactory.getLogger(ZUGFeRDMappingTest.class.getName());


  @Test
  public void testMapping() {

    //Read an eb4p1 sample
    try {
      String
          ebInterfaceXML =
          IOUtils.toString(
              this.getClass().getResourceAsStream("/ebinterface/ebInterface_4p1_sample.xml"),
              "UTF-8");

      MappingFactory mf = new MappingFactory();
      Mapping zugFeRDMapping = mf.getMapper(MappingFactory.MappingType.ZUGFeRD_EXTENDED_1p0);

      //Map to ZUGFeRD Basic
      String zugferd = new String(zugFeRDMapping.mapFromebInterface(ebInterfaceXML));

      SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

      Schema schema = factory.newSchema(new StreamSource(new File(
          ZUGFeRDMappingTest.class
              .getResource("/ZUGFeRD/Schema/ZUGFeRD1p0.xsd").toURI())));

      Validator validator = schema.newValidator();

      SAXSource saxSource = new SAXSource(new InputSource(
          new ByteArrayInputStream(zugferd.getBytes("UTF-8"))));

      validator.validate(saxSource);

      LOG.info("Result of ZUGFeRD mapping is {}", zugferd);

      assert (true);
    } catch (Exception e) {
      LOG.error("Unable to perform mapping.", e);

      assert (false);
    }
  }
}
