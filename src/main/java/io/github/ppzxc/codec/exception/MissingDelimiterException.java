package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

public class MissingDelimiterException extends CodecProblemException {

  private static final long serialVersionUID = -7027414899972912972L;

  public MissingDelimiterException(Header header) {
    super("missing line delimiter", header.getId(), "[UNRECOGNIZED]", "/decoder", new Object[]{},
      CodecProblem.MISSING_LINE_DELIMITER);
  }
}
