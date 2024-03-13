package io.github.ppzxc.codec.exception;

/**
 * The type Null pointer code problem exception.
 */
public class NullPointerCodeProblemException extends CodecProblemException {

  private static final long serialVersionUID = 8254481768801292762L;

  /**
   * Instantiates a new Null pointer code problem exception.
   *
   * @param message the message
   */
  public NullPointerCodeProblemException(String message) {
    super(message, 0, "[UNRECOGNIZED]", "/decoder", new Object[]{}, CodecProblem.INBOUND_DATA_NULL_POINTER);
  }
}
