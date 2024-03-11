package io.github.ppzxc.codec.exception;

/**
 * The type Less than minimum message length code exception.
 */
public class LessThanMinimumMessageLengthCodeException extends ProblemCodeException {

  private static final long serialVersionUID = -3624512198523022524L;

  /**
   * Instantiates a new Less than minimum message length code exception.
   *
   * @param message the message
   */
  public LessThanMinimumMessageLengthCodeException(String message) {
    super(message, 0, ProblemCode.LESS_THAN_MINIMUM_MESSAGE_LENGTH);
  }
}
