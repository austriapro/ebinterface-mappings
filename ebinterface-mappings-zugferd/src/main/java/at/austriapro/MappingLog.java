package at.austriapro;

import java.util.ArrayList;

/**
 * Created by Paul on 16.10.2015.
 */
public class MappingLog {

  MappingLogLevel appliedLogLevel = MappingLogLevel.DEBUG;
  ArrayList<MappingLogEntry> logEntries = new ArrayList();

  public MappingLog(MappingLogLevel appliedLogLevel) {
    this.appliedLogLevel = appliedLogLevel;
  }

  public MappingLogLevel getAppliedLogLevel() {
    return appliedLogLevel;
  }

  public ArrayList<MappingLogEntry> getLogEntries() {
    return logEntries;
  }

  public String toString() {
    StringBuilder log = new StringBuilder();

    for (MappingLogEntry logEntry : logEntries) {
      log.append(logEntry.toString() + "\n");
    }

    return log.toString();
  }

  public void add(MappingLogEntry entry) {
    boolean insertIntoLog = false;

    if (appliedLogLevel.equals(MappingLogLevel.DEBUG)) {
      insertIntoLog = true;
    } else if (appliedLogLevel.equals(MappingLogLevel.INFO) && !entry
        .equals(MappingLogLevel.DEBUG)) {
      insertIntoLog = true;
    } else if (appliedLogLevel.equals(MappingLogLevel.WARNING) && !entry
        .equals(MappingLogLevel.DEBUG) && !entry.equals(MappingLogLevel.INFO)) {
      insertIntoLog = true;

    } else if (appliedLogLevel.equals(MappingLogLevel.ERROR) && entry
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
