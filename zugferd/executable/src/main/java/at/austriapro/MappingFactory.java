package at.austriapro;

import at.austriapro.mappings.zugferd.ZUGFeRDMapping;

/**
 * Creates a Mapping of a given type
 */
public class MappingFactory {


  public enum MappingType {ZUGFeRD1p0, UBL, GS1XML, FATTURAPA}

  ;


  /**
   * Create a mapper of the given type
   */
  public Mapping getMapper(MappingType mappingType) {

    if (MappingType.ZUGFeRD1p0.equals(mappingType)) {
      return createZUGFeRDMapper();
    } else {
      throw new UnsupportedOperationException(
          "Unable to create mapper. Only ZUGFeRD supported at the moment.");
    }

  }


  /**
   * Create a ZUGFeRD mapper
   */
  private Mapping createZUGFeRDMapper() {
    return new ZUGFeRDMapping();
  }

}
