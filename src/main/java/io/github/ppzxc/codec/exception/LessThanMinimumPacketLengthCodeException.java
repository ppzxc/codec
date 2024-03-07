package io.github.ppzxc.codec.exception;

/**
 * The type Less than minimum packet length code exception.
 */
public class LessThanMinimumPacketLengthCodeException extends ProblemCodeException {

  private static final long serialVersionUID = -3624512198523022524L;

  /**
   * Instantiates a new Less than minimum packet length code exception.
   *
   * @param message the message
   */
  public LessThanMinimumPacketLengthCodeException(String message) {
    super(message, 0, ProblemCode.LESS_THAN_MINIMUM_PACKET_LENGTH);
  }
}
