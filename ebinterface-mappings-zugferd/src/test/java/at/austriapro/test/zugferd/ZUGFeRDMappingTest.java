package at.austriapro.test.zugferd;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
      Mapping zugFeRDMapping = mf.getMapper(MappingFactory.MappingType.ZUGFeRD_BASIC_1p0);

      //Map to ZUGFeRD Basic
      String zugferd = new String(zugFeRDMapping.mapFromebInterface(ebInterfaceXML));

      LOG.info("Result of ZUGFeRD mapping is {}", zugferd);


    } catch (Exception e) {
      LOG.error("Unable to perform mapping.", e);
    }


  }


}
