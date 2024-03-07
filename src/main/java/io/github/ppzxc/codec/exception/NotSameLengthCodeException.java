package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Not same length code exception.
 */
public class NotSameLengthCodeException extends ProblemCodeException {

  private static final long serialVersionUID = 1186906307527013515L;

  /**
   * Instantiates a new Not same length code exception.
   *
   * @param header            the header
   * @param readableBytesBody the readable bytes body
   */
  public NotSameLengthCodeException(Header header, int readableBytesBody) {
    super(String.format("not same length: %d != %d", header.getBodyLength(), readableBytesBody), header.getId(),
      ProblemCode.NOT_SAME_LENGTH);
  }

  /**
   * Instantiates a new Not same length code exception.
   *
   * @param id the id
   */
  public NotSameLengthCodeException(int id) {
    super(id, ProblemCode.NOT_SAME_LENGTH);
  }

  /**
   * Instantiates a new Not same length code exception.
   *
   * @param message the message
   */
  public NotSameLengthCodeException(String message) {
    super(message, 0, ProblemCode.NOT_SAME_LENGTH);
  }

  /**
   * Instantiates a new Not same length code exception.
   *
   * @param id      the id
   * @param message the message
   */
  public NotSameLengthCodeException(int id, String message) {
    super(message, id, ProblemCode.NOT_SAME_LENGTH);
  }
}
