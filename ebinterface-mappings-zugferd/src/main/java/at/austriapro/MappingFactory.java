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
    ZUGFeRD_BASIC_1p0, ZUGFeRD_COMFORT_1p0, ZUGFeRD_EXTENDED_1p0
  }

  //Aggregation of all ZUGFeRD sub types
  private static final EnumSet<ZugferdMappingType>
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
    switch (ebInterfaceMappingType) {
      case V41: return new ZUGFeRDMappingFromEbInterface4p1(zugferdMappingType);
      case V42: return new ZUGFeRDMappingFromEbInterface4p2(zugferdMappingType);
      case V43: return new ZUGFeRDMappingFromEbInterface4p3(zugferdMappingType);
    }
    return null;
  }

}
