package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Not supported body length exception.
 */
public class NotSupportedBodyLengthException extends ProblemCodeException {

  private static final long serialVersionUID = 5628609465590812055L;

  /**
   * Instantiates a new Not supported body length exception.
   *
   * @param header the header
   */
  public NotSupportedBodyLengthException(Header header) {
    super("not supported body length: " + header.getBodyLength(), header.getId(), ProblemCode.NOT_SUPPORTED_BODY_LENGTH);
  }
}
