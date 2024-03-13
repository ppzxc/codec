package io.github.ppzxc.codec.exception;

/**
 * The type Less than minimum message length code problem exception.
 */
public class LessThanMinimumMessageLengthCodeProblemException extends CodecProblemException {

  private static final long serialVersionUID = -3624512198523022524L;

  /**
   * Instantiates a new Less than minimum message length code problem exception.
   *
   * @param message the message
   */
  public LessThanMinimumMessageLengthCodeProblemException(String message) {
    super(message, 0, "[UNRECOGNIZED]", "/decoder", new Object[]{}, CodecProblem.LESS_THAN_MINIMUM_MESSAGE_LENGTH);
  }
}
