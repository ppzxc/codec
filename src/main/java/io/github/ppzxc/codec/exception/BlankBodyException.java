package io.github.ppzxc.codec.exception;

public class BlankBodyException extends CodecProblemException {

  private static final long serialVersionUID = 8254481768801292762L;

  public BlankBodyException(String message) {
    super(message, 0, "[UNRECOGNIZED]", "/decoder", new Object[]{}, CodecProblem.BLANK_BODY);
  }
}
