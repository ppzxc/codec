package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Not same length code problem exception.
 */
public class NotSameLengthCodeProblemException extends CodecProblemException {

  private static final long serialVersionUID = 1186906307527013515L;

  /**
   * Instantiates a new Not same length code problem exception.
   *
   * @param header        the header
   * @param rejectedValue the rejected value
   */
  public NotSameLengthCodeProblemException(Header header, String rejectedValue) {
    super(String.format("not same length: %d != %s", header.getBodyLength(), rejectedValue), header.getId(),
      rejectedValue, "/decoder", new Object[]{rejectedValue}, CodecProblem.NOT_SAME_LENGTH);
  }
}
