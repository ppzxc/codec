package io.github.ppzxc.codec.exception;

/**
 * The type Deserialize failed exception.
 */
public class DeserializeFailedException extends ProblemCodeException {

  private static final long serialVersionUID = 465217152991183951L;

  /**
   * Instantiates a new Deserialize failed exception.
   *
   * @param throwable the throwable
   */
  public DeserializeFailedException(Throwable throwable) {
    super("deserialize failed", throwable, 0, ProblemCode.DESERIALIZE_FAILED);
  }
}
