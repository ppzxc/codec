package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecProblemCode;

public class InvalidLengthException extends CodecProblemException {

  private static final long serialVersionUID = 6676349819530503971L;

  public InvalidLengthException(int length, int readable) {
    super("reject " + length + ", not equals " + readable, 0L, String.valueOf(length), "/decoder",
      new Object[]{length, readable}, CodecProblemCode.LENGTH_NOT_EQUALS_READABLE);
  }
}
