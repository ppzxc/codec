package io.github.ppzxc.codec.exception;

/**
 * The type Codec problem exception.
 */
public class CodecProblemException extends Exception {

  private static final long serialVersionUID = -927664338530046046L;
  private final int id;
  private final String rejectedValue;
  private final String instance;
  private final transient Object[] args;
  private final CodecProblem codecProblem;

  /**
   * Instantiates a new Codec problem exception.
   *
   * @param id            the id
   * @param rejectedValue the rejected value
   * @param instance      the instance
   * @param args          the args
   * @param codecProblem  the codec problem
   */
  public CodecProblemException(int id, String rejectedValue, String instance, Object[] args,
    CodecProblem codecProblem) {
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblem = codecProblem;
  }

  /**
   * Instantiates a new Codec problem exception.
   *
   * @param message       the message
   * @param id            the id
   * @param rejectedValue the rejected value
   * @param instance      the instance
   * @param args          the args
   * @param codecProblem  the codec problem
   */
  public CodecProblemException(String message, int id, String rejectedValue, String instance, Object[] args,
    CodecProblem codecProblem) {
    super(message);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblem = codecProblem;
  }

  /**
   * Instantiates a new Codec problem exception.
   *
   * @param message       the message
   * @param cause         the cause
   * @param id            the id
   * @param rejectedValue the rejected value
   * @param instance      the instance
   * @param args          the args
   * @param codecProblem  the codec problem
   */
  public CodecProblemException(String message, Throwable cause, int id, String rejectedValue, String instance,
    Object[] args, CodecProblem codecProblem) {
    super(message, cause);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblem = codecProblem;
  }

  /**
   * Instantiates a new Codec problem exception.
   *
   * @param cause         the cause
   * @param id            the id
   * @param rejectedValue the rejected value
   * @param instance      the instance
   * @param args          the args
   * @param codecProblem  the codec problem
   */
  public CodecProblemException(Throwable cause, int id, String rejectedValue, String instance, Object[] args,
    CodecProblem codecProblem) {
    super(cause);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblem = codecProblem;
  }

  /**
   * Instantiates a new Codec problem exception.
   *
   * @param message            the message
   * @param cause              the cause
   * @param enableSuppression  the enable suppression
   * @param writableStackTrace the writable stack trace
   * @param id                 the id
   * @param rejectedValue      the rejected value
   * @param instance           the instance
   * @param args               the args
   * @param codecProblem       the codec problem
   */
  public CodecProblemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
    int id, String rejectedValue, String instance, Object[] args, CodecProblem codecProblem) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.id = id;
    this.rejectedValue = rejectedValue;
    this.instance = instance;
    this.args = args;
    this.codecProblem = codecProblem;
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
  public CodecProblem getProblemCode() {
    return codecProblem;
  }

  /**
   * Gets rejected value.
   *
   * @return the rejected value
   */
  public String getRejectedValue() {
    return rejectedValue;
  }

  /**
   * Gets instance.
   *
   * @return the instance
   */
  public String getInstance() {
    return instance;
  }

  /**
   * Get args object [ ].
   *
   * @return the object [ ]
   */
  public Object[] getArgs() {
    return args;
  }

  /**
   * Gets codec problem.
   *
   * @return the codec problem
   */
  public CodecProblem getCodecProblem() {
    return codecProblem;
  }
}
