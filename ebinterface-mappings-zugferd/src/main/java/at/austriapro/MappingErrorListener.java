package at.austriapro;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 * Created by Paul on 16.10.2015.
 */
public class MappingErrorListener implements ErrorListener {

  StringBuilder errors = new StringBuilder();
  boolean catchedError = false;

  public void warning(TransformerException exception) throws TransformerException {
    errors.append("Warning: ").append(exception.getMessage()).append("\n");
    catchedError = true;
  }

  public void error(TransformerException exception) throws TransformerException {
    errors.append("Error: ").append(exception.getMessage()).append("\n");
    catchedError = true;
  }

  public void fatalError(TransformerException exception) throws TransformerException {
    errors.append("Fatal error: ").append(exception.getMessage()).append("\n");
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
