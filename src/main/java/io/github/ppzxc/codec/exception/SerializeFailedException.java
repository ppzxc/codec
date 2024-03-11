package io.github.ppzxc.codec.exception;

/**
 * The type Serialize failed exception.
 */
public class SerializeFailedException extends ProblemCodeException {

  private static final long serialVersionUID = -4862359733551583748L;

  /**
   * Instantiates a new Serialize failed exception.
   */
  public SerializeFailedException() {
    super("serialize failed", 0, ProblemCode.SERIALIZE_FAILED);
  }

  /**
   * Instantiates a new Serialize failed exception.
   *
   * @param throwable the throwable
   */
  public SerializeFailedException(Throwable throwable) {
    super("serialize failed", throwable, 0, ProblemCode.SERIALIZE_FAILED);
  }
}
