package at.austriapro;

import java.util.EnumSet;

import com.helger.ebinterface.EEbInterfaceVersion;

import at.austriapro.mappings.zugferd.ZUGFeRDMappingFromEbInterface4p1;
import at.austriapro.mappings.zugferd.ZUGFeRDMappingFromEbInterface4p2;
import at.austriapro.mappings.zugferd.ZUGFeRDMappingFromEbInterface4p3;

/**
 * Creates a Mapping of a given type
 */
public class MappingFactory {

  //The currently supported mapping types
  public enum ZugferdMappingType {
    ZUGFeRD_BASIC_1p0, ZUGFeRD_COMFORT_1p0, ZUGFeRD_EXTENDED_1p0, UBL, GS1XML, FATTURAPA
  }

  //Aggregation of all ZUGFeRD sub types
  private EnumSet<ZugferdMappingType>
      ZUGFeRDTYPES =
      EnumSet.of(ZugferdMappingType.ZUGFeRD_BASIC_1p0, ZugferdMappingType.ZUGFeRD_COMFORT_1p0,
                 ZugferdMappingType.ZUGFeRD_EXTENDED_1p0);

  /**
   * Create a mapper of the given type
   */
  public Mapping getMapper(ZugferdMappingType zugferdMappingType, EEbInterfaceVersion ebInterfaceMappingType) {

    if (ZUGFeRDTYPES.contains(zugferdMappingType)) {
      return createZUGFeRDMapper(zugferdMappingType, ebInterfaceMappingType);
    } else {
      throw new UnsupportedOperationException(
          "Unable to create mapper. Only ZUGFeRD supported at the moment.");
    }

  }

  /**
   * Create a ZUGFeRD mapper
   */
  private Mapping createZUGFeRDMapper(ZugferdMappingType zugferdMappingType, EEbInterfaceVersion ebInterfaceMappingType) {
    Mapping zUGFeRDMapping;

    if (ebInterfaceMappingType == EEbInterfaceVersion.V41) {
      zUGFeRDMapping = new ZUGFeRDMappingFromEbInterface4p1(zugferdMappingType);
    }else if (ebInterfaceMappingType == EEbInterfaceVersion.V42) {
      zUGFeRDMapping = new ZUGFeRDMappingFromEbInterface4p2(zugferdMappingType);
    }
    else if (ebInterfaceMappingType == EEbInterfaceVersion.V43) {
      zUGFeRDMapping = new ZUGFeRDMappingFromEbInterface4p3(zugferdMappingType);
    }
    else
      zUGFeRDMapping = null;
    return zUGFeRDMapping;
  }

}
