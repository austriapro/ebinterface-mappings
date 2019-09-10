package at.austriapro;

/**
 * Created by Paul on 16.10.2015.
 */
public enum MappingLogLevel {
  DEBUG("Debug"), INFO("Info"), WARNING("Warning"), ERROR("Error");

  private final String name;

  private MappingLogLevel(String s) {
    name = s;
  }

  @Override
  public String toString() {
    return this.name;
  }
}
