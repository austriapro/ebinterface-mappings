package at.austriapro;

/**
 * Created by Paul on 16.10.2015.
 */
public class MappingLogEntry {

  String message, source, destination;
  MappingLogLevel logLevel = MappingLogLevel.WARNING;

  public MappingLogEntry(String message, String source, String destination) {
    this.source = source;
    this.destination = destination;
    this.message = message;
  }

  public MappingLogEntry(String message, String source, String destination,
                         MappingLogLevel logLevel) {
    this(message, source, destination);
    this.logLevel = logLevel;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public MappingLogLevel getLogLevel() {
    return logLevel;
  }

  public void setLogLevel(MappingLogLevel logLevel) {
    this.logLevel = logLevel;
  }

  public String toString() {
    StringBuilder text = new StringBuilder();

    text.append(logLevel.toString()).append(": ").append(message).append("\n\tsource:\t\t\t")
        .append(source).append("\n\tdestination:\t").append(destination);

    return text.toString();
  }

  public String toHTML() {
    StringBuilder text = new StringBuilder();

    text.append(logLevel.toString()).append(": ").append(message).append("<br/>").append("source: ")
        .append(source).append("<br/>").append("destination: ").append(destination);

    return text.toString();
  }

}
