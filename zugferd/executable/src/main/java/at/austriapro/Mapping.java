package at.austriapro;

import java.io.InputStream;

/**
 * Common interface, shared among all mappings implemented by AUSTRIAPro
 */
public abstract class Mapping {


  /**
   * Performs a mapping from ebInterface to a given target format, specified
   * by the implementing class
   * @param ebinterface
   * @return
   * @throws MappingException
   */
   public abstract InputStream mapFromebInterface(String ebinterface) throws MappingException;


}
