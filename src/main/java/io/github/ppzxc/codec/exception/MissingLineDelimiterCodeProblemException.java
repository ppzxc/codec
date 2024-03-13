package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Missing line delimiter code problem exception.
 */
public class MissingLineDelimiterCodeProblemException extends CodecProblemException {

  private static final long serialVersionUID = -7027414899972912972L;

  /**
   * Instantiates a new Missing line delimiter code problem exception.
   *
   * @param header the header
   */
  public MissingLineDelimiterCodeProblemException(Header header) {
    super("missing line delimiter", header.getId(), "[UNRECOGNIZED]", "/decoder", new Object[]{},
      CodecProblem.MISSING_LINE_DELIMITER);
  }
}
