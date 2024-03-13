package io.github.ppzxc.codec.exception;

public class MinimumLengthLimitException extends CodecProblemException {

  private static final long serialVersionUID = -3624512198523022524L;

  public MinimumLengthLimitException(String message) {
    super(message, 0, "[UNRECOGNIZED]", "/decoder", new Object[]{}, CodecProblem.LESS_THAN_MINIMUM_MESSAGE_LENGTH);
  }
}
