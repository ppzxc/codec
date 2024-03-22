package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecCode;

public class ShortLengthCodecException extends CodecException {

  private static final long serialVersionUID = -3624512198523022524L;

  public ShortLengthCodecException(int rejectedValue, int minimumLength) {
    super("reject " + rejectedValue + ", less than " + minimumLength, DEFAULT_ID, String.valueOf(rejectedValue),
      INSTANCE_DECODER, new Object[]{rejectedValue, minimumLength}, CodecCode.SHORT_LENGTH);
  }
}
