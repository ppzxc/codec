package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecCode;

public class MissingLineDelimiterCodecException extends CodecException {

  private static final long serialVersionUID = 4560329205369603852L;

  public MissingLineDelimiterCodecException() {
    super(0L, DEFAULT_REJECT, INSTANCE_DECODER, new Object[]{}, CodecCode.MISSING_LINE_DELIMITER);
  }
}
