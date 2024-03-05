package io.github.ppzxc.codec.exception;

import io.github.ppzxc.codec.model.Header;

/**
 * The type Corrupted body length code exception.
 */
public class CorruptedBodyLengthCodeException extends ProblemCodeException {

  private static final long serialVersionUID = 5959633122010960093L;

  /**
   * Instantiates a new Corrupted body length code exception.
   *
   * @param header the header
   */
  public CorruptedBodyLengthCodeException(Header header) {
    super("corrupted header: bodyLength: " + header.getBodyLength(), header.getId(), ProblemCode.CORRUPTED_BODY_LENGTH);
  }

  /**
   * Instantiates a new Corrupted body length code exception.
   *
   * @param id the id
   */
  public CorruptedBodyLengthCodeException(int id) {
    super(id, ProblemCode.CORRUPTED_BODY_LENGTH);
  }

  /**
   * Instantiates a new Corrupted body length code exception.
   *
   * @param message the message
   */
  public CorruptedBodyLengthCodeException(String message) {
    super(message, 0, ProblemCode.CORRUPTED_BODY_LENGTH);
  }

  /**
   * Instantiates a new Corrupted body length code exception.
   *
   * @param id      the id
   * @param message the message
   */
  public CorruptedBodyLengthCodeException(int id, String message) {
    super(message, id, ProblemCode.CORRUPTED_BODY_LENGTH);
  }
}
