package at.austriapro;

import java.util.EnumSet;

import at.austriapro.mappings.zugferd.ZUGFeRDMapping;

/**
 * Creates a Mapping of a given type
 */
public class MappingFactory {


  //The currently supported mapping types
  public enum MappingType {ZUGFeRD_BASIC_1p0, ZUGFeRD_COMFORT_1p0, ZUGFeRD_EXTENDED_1p0, UBL, GS1XML, FATTURAPA}

  ;

  //Aggregation of all ZUGFeRD sub types
  private EnumSet<MappingType>
      ZUGFeRDTYPES =
      EnumSet.of(MappingType.ZUGFeRD_BASIC_1p0, MappingType.ZUGFeRD_COMFORT_1p0,
                 MappingType.ZUGFeRD_EXTENDED_1p0);

  //References for singleton
  private static Mapping zUGFeRDMapping;

  /**
   * Create a mapper of the given type
   */
  public Mapping getMapper(MappingType mappingType) {

    if (ZUGFeRDTYPES.contains(mappingType)) {
      return createZUGFeRDMapper(mappingType);
    } else {
      throw new UnsupportedOperationException(
          "Unable to create mapper. Only ZUGFeRD supported at the moment.");
    }

  }


  /**
   * Create a ZUGFeRD mapper
   */
  private Mapping createZUGFeRDMapper(MappingType mappingType) {
    if (zUGFeRDMapping == null) {
      zUGFeRDMapping = new ZUGFeRDMapping(mappingType);
    }
    return  zUGFeRDMapping;
  }

}
