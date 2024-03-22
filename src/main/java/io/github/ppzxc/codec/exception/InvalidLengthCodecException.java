package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecCode;

public class InvalidLengthCodecException extends CodecException {

  private static final long serialVersionUID = 6676349819530503971L;

  public InvalidLengthCodecException(int length, int readable) {
    super("reject " + length + ", not equals " + readable, DEFAULT_ID, String.valueOf(length), INSTANCE_DECODER,
      new Object[]{length, readable}, CodecCode.LENGTH_NOT_EQUALS_READABLE);
  }
}
