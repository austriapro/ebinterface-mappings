package at.austriapro;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Created by Paul on 16.10.2015.
 */
public class MappingErrorHandler implements ErrorHandler {

  StringBuilder errors = new StringBuilder();
  boolean catchedError = false;

  public void warning(SAXParseException exception) throws SAXException {
    errors.append("Warning: ").append(exception.getMessage()).append("\n");
    catchedError = true;
  }

  public void error(SAXParseException exception) throws SAXException {
    errors.append("Error: ").append(exception.getMessage()).append("\n");
    catchedError = true;
  }

  public void fatalError(SAXParseException exception) throws SAXException {
    errors.append("Fatal error: : ").append(exception.getMessage()).append("\n");
    catchedError = true;
  }

  public boolean catchedError() {
    return catchedError;
  }

  @Override
  public String toString() {
    return errors.toString();
  }
}
