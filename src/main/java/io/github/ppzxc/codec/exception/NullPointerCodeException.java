package io.github.ppzxc.codec.exception;

/**
 * The type Null pointer code exception.
 */
public class NullPointerCodeException extends ProblemCodeException {

  private static final long serialVersionUID = 8254481768801292762L;

  /**
   * Instantiates a new Null pointer code exception.
   *
   * @param id the id
   */
  public NullPointerCodeException(int id) {
    super(id, ProblemCode.INBOUND_DATA_NULL_POINTER);
  }

  /**
   * Instantiates a new Null pointer code exception.
   *
   * @param message the message
   */
  public NullPointerCodeException(String message) {
    super(message, 0, ProblemCode.INBOUND_DATA_NULL_POINTER);
  }

  /**
   * Instantiates a new Null pointer code exception.
   *
   * @param id      the id
   * @param message the message
   */
  public NullPointerCodeException(int id, String message) {
    super(message, id, ProblemCode.INBOUND_DATA_NULL_POINTER);
  }
}
