package io.github.ppzxc.codec.exception;

/**
 * The type Problem code exception.
 */
public class ProblemCodeException extends Exception {

  private static final long serialVersionUID = -927664338530046046L;
  private final int id;
  private final ProblemCode problemCode;

  /**
   * Instantiates a new Problem code exception.
   *
   * @param id          the id
   * @param problemCode the problem code
   */
  public ProblemCodeException(int id, ProblemCode problemCode) {
    this.id = id;
    this.problemCode = problemCode;
  }

  /**
   * Instantiates a new Problem code exception.
   *
   * @param message     the message
   * @param id          the id
   * @param problemCode the problem code
   */
  public ProblemCodeException(String message, int id, ProblemCode problemCode) {
    super(message);
    this.id = id;
    this.problemCode = problemCode;
  }

  /**
   * Instantiates a new Problem code exception.
   *
   * @param message     the message
   * @param cause       the cause
   * @param id          the id
   * @param problemCode the problem code
   */
  public ProblemCodeException(String message, Throwable cause, int id, ProblemCode problemCode) {
    super(message, cause);
    this.id = id;
    this.problemCode = problemCode;
  }

  /**
   * Instantiates a new Problem code exception.
   *
   * @param cause       the cause
   * @param id          the id
   * @param problemCode the problem code
   */
  public ProblemCodeException(Throwable cause, int id, ProblemCode problemCode) {
    super(cause);
    this.id = id;
    this.problemCode = problemCode;
  }

  /**
   * Instantiates a new Problem code exception.
   *
   * @param message            the message
   * @param cause              the cause
   * @param enableSuppression  the enable suppression
   * @param writableStackTrace the writable stack trace
   * @param id                 the id
   * @param problemCode        the problem code
   */
  public ProblemCodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
    int id, ProblemCode problemCode) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.id = id;
    this.problemCode = problemCode;
  }

  /**
   * Gets id.
   *
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * Gets problem code.
   *
   * @return the problem code
   */
  public ProblemCode getProblemCode() {
    return problemCode;
  }
}
