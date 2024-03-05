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

  /**
   * Instantiates a new Not supported body length exception.
   *
   * @param id the id
   */
  public NotSupportedBodyLengthException(int id) {
    super(id, ProblemCode.NOT_SUPPORTED_BODY_LENGTH);
  }

  /**
   * Instantiates a new Not supported body length exception.
   *
   * @param message the message
   */
  public NotSupportedBodyLengthException(String message) {
    super(message, 0, ProblemCode.NOT_SUPPORTED_BODY_LENGTH);
  }

  /**
   * Instantiates a new Not supported body length exception.
   *
   * @param id      the id
   * @param message the message
   */
  public NotSupportedBodyLengthException(int id, String message) {
    super(message, id, ProblemCode.NOT_SUPPORTED_BODY_LENGTH);
  }
}
