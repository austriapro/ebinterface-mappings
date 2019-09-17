package at.austriapro;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 16.10.2015.
 */
public class MappingLog {

  final MappingLogLevel appliedLogLevel;
  final List<MappingLogEntry> logEntries = new ArrayList<>();

  public MappingLog(MappingLogLevel appliedLogLevel) {
    this.appliedLogLevel = appliedLogLevel;
  }

  public MappingLogLevel getAppliedLogLevel() {
    return appliedLogLevel;
  }

  public List<MappingLogEntry> getLogEntries() {
    return logEntries;
  }

  @Override
  public String toString() {
    StringBuilder log = new StringBuilder();

    for (MappingLogEntry logEntry : logEntries) {
      log.append(logEntry.toString() + "\n\n");
    }

    return log.toString();
  }

  public String toHTML() {
    StringBuilder log = new StringBuilder();

    for (MappingLogEntry logEntry : logEntries) {
      log.append("<p>" + logEntry.toHTML() + "</p>");
    }

    return log.toString();
  }

  public void add(MappingLogEntry entry) {
    boolean insertIntoLog = false;

    if (appliedLogLevel.equals(MappingLogLevel.DEBUG)) {
      insertIntoLog = true;
    } else if (appliedLogLevel.equals(MappingLogLevel.INFO) && !entry.logLevel
        .equals(MappingLogLevel.DEBUG)) {
      insertIntoLog = true;
    } else if (appliedLogLevel.equals(MappingLogLevel.WARNING) && !entry.logLevel
        .equals(MappingLogLevel.DEBUG) && !entry.logLevel.equals(MappingLogLevel.INFO)) {
      insertIntoLog = true;

    } else if (appliedLogLevel.equals(MappingLogLevel.ERROR) && entry.logLevel
        .equals(MappingLogLevel.ERROR)) {
      insertIntoLog = true;
    }

    if (insertIntoLog) {
      logEntries.add(entry);
    }
  }

  public void add(String message, String source, String destination,
                  MappingLogLevel logLevel) {
    add(new MappingLogEntry(message, source, destination, logLevel));
  }

  public void add(String message, String source, String destination) {
    add(message, source, destination, MappingLogLevel.WARNING);
  }
}
