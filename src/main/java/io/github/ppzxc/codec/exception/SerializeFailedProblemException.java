package io.github.ppzxc.codec.exception;

/**
 * The type Serialize failed problem exception.
 */
public class SerializeFailedProblemException extends Exception {

  private static final long serialVersionUID = -4862359733551583748L;

  /**
   * Instantiates a new Serialize failed problem exception.
   */
  public SerializeFailedProblemException() {
    super("serialize failed");
  }

  /**
   * Instantiates a new Serialize failed problem exception.
   *
   * @param message the message
   */
  public SerializeFailedProblemException(String message) {
    super(message);
  }

  /**
   * Instantiates a new Serialize failed problem exception.
   *
   * @param throwable the throwable
   */
  public SerializeFailedProblemException(Throwable throwable) {
    super("serialize failed", throwable);
  }
}
