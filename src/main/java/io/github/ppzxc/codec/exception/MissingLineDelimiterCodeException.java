package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Missing line delimiter code exception.
 */
public class MissingLineDelimiterCodeException extends ProblemCodeException {

  private static final long serialVersionUID = -7027414899972912972L;

  /**
   * Instantiates a new Missing line delimiter code exception.
   *
   * @param header the header
   */
  public MissingLineDelimiterCodeException(Header header) {
    super("missing line delimiter", header.getId(), ProblemCode.CORRUPTED_BODY_LENGTH);
  }
}
