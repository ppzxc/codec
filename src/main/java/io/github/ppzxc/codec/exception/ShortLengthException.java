package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecProblemCode;

public class ShortLengthException extends CodecProblemException {

  private static final long serialVersionUID = -3624512198523022524L;

  public ShortLengthException(int rejectedValue, int minimumLength) {
    super("reject " + rejectedValue + ", less than " + minimumLength, 0L, String.valueOf(rejectedValue), "/decoder",
      new Object[]{rejectedValue, minimumLength}, CodecProblemCode.SHORT_LENGTH);
  }
}
