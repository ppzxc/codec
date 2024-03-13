package io.github.ppzxc.codec.exception;

/**
 * The type Deserialize failed problem exception.
 */
public class DeserializeFailedProblemException extends Exception {

  private static final long serialVersionUID = 465217152991183951L;

  /**
   * Instantiates a new Deserialize failed problem exception.
   */
  public DeserializeFailedProblemException() {
    super("deserialize failed");
  }

  /**
   * Instantiates a new Deserialize failed problem exception.
   *
   * @param message the message
   */
  public DeserializeFailedProblemException(String message) {
    super(message);
  }

  /**
   * Instantiates a new Deserialize failed problem exception.
   *
   * @param throwable the throwable
   */
  public DeserializeFailedProblemException(Throwable throwable) {
    super("deserialize failed", throwable);
  }
}
