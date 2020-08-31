package at.austriapro;

/**
 * Common interface, shared among all mappings implemented by AUSTRIAPro
 */
public abstract class Mapping {

  public final MappingLog mLog = new MappingLog(MappingLogLevel.WARNING);

  /**
   * Performs a mapping from ebInterface to a given target format, specified by the implementing
   * class
   */
  public abstract byte[] mapFromebInterface(byte[] ebinterface) throws MappingException;

  public String getMappingLog() {
    return mLog.toString();
  }

  public String getMappingLogHTML() {
    return mLog.toHTML();
  }
}
