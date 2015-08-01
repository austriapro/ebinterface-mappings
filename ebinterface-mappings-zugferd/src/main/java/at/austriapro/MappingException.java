package at.austriapro;

/**
 * Exception which might be thrown during a mapping from/to ebInterface
 */
public class MappingException extends Exception {


  public MappingException() {
    super();
  }

  public MappingException(String message) {
    super(message);
  }

  public MappingException(String message, Throwable cause) {
    super(message, cause);
  }

  public MappingException(Throwable cause) {
    super(cause);
  }

}
