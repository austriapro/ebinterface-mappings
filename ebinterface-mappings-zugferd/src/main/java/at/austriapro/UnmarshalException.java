package at.austriapro;

/**
 * Created by paul on 1/18/16.
 */
public class UnmarshalException extends Exception{

  public UnmarshalException() {
    super();
  }

  public UnmarshalException(String message) {
    super(message);
  }

  public UnmarshalException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnmarshalException(Throwable cause) {
    super(cause);
  }
}
