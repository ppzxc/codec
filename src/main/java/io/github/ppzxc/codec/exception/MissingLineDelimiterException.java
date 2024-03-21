package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.CodecProblemCode;

public class MissingLineDelimiterException extends CodecProblemException {

  private static final long serialVersionUID = 4560329205369603852L;

  public MissingLineDelimiterException() {
    super(0L, "[UNRECOGNIZED]", "/decoder", new Object[]{}, CodecProblemCode.MISSING_LINE_DELIMITER);
  }
}
