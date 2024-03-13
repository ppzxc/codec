package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Not supported body length problem exception.
 */
public class NotSupportedBodyLengthProblemException extends CodecProblemException {

  private static final long serialVersionUID = 5628609465590812055L;

  /**
   * Instantiates a new Not supported body length problem exception.
   *
   * @param header        the header
   * @param rejectedValue the rejected value
   */
  public NotSupportedBodyLengthProblemException(Header header, String rejectedValue) {
    super("not supported body length: " + header.getBodyLength(), header.getId(), rejectedValue, "/decoder",
      new Object[]{rejectedValue}, CodecProblem.NOT_SUPPORTED_BODY_LENGTH);
  }
}
